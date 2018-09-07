;****************************************************************
; 
;    Date: Feb 22, 2008
;    Author: Evan Ross
;
;    vm_main is the entry point of the Virtual Machine.
;    It contains initialization procedures and the main bytecode 
;    execution loop
;
;
;****************************************************************


	AREA vmmain, CODE, READONLY
		
	INCLUDE vmregisters.s ; register definitions and defines
	INCLUDE lookupsetup.s ; macros for setting process flags
	INCLUDE ramconstants.s ; constants for heap initialization
	INCLUDE vmexceptions.s
		
	ENTRY

;*************************************
;
;  Virtual Machine entry point
;
;*************************************
	EXPORT vm_main
vm_main
		
		IMPORT invoke_scheduler
		IMPORT thread_sleep
		IMPORT instruction_execution_engine
		IMPORT handler_unused


		BL peripheral_init ; initialize peripherals first

		BL vm_init	; call vm_init

		BL vm_startup ; create the initial frame and get ready to execute bytecodes


		; The VM engine.  A forever loop.
		EXPORT main_execution_loop 
main_execution_loop

        ; 1.  If PCW is not null:
        ;    a) if PCW & 0xff non-zero, run that process
        ;    b) if PCW & 0xffff0000 non-zero, schedule that interrupt
        ; 2.  Fetch an instruction and execute it.
		; Use INDEX register as a temporary register here.
		MOV index, #PCW_FLAGS_MASK_INV ; load ~PCW_FLAGS_MASK
		ANDS index, index, PCW	       ; PCW & ~PCW_FLAGS_MASK
		BEQ instruction_execution_engine	; if (PCW & ~PCW_FLAGS_MASK == 0)

			; An interrupt, scheduler process or both is pending.
			ANDS value2, PCW, #PCW_PROCESS_MASK
			BEQ test_interrupt ; if (PCW & PCW_PROCESS_MASK == 0)

				MOV PCW, #0  ; setPCW(0) clear it
				ADR index, scheduler_table
				LDR pc, [index, value2, LSL#2]  ; Jump to process in scheduler table

				ALIGN
scheduler_table
				DCD handler_unused   ; 0 throw an exception
				DCD invoke_scheduler ; 1
				DCD thread_sleep	 ; 2


test_interrupt
						
			MOV32 value2, #PCW_FLAGS_MASK 
			ANDS value2, value2, PCW
			BEQ main_execution_loop ; no interrupts pending.

			; handle them by restoring context and going back to execution_loop.

			B main_execution_loop;			



;*************************************
;
;   Virtual Machine initialization
;
;   Tasks:
;      1.  Setup and initialize the heap
;      2.  Initialize the system registers:
;             eligiblehead = NULL
;             waitinghead  = NULL
;             freehead     = top of heap
;	   3.  Initialize PCW
;
;*************************************
vm_init
		
	
		; --- -Initialize the Heap ----
		; The heap is the entire RAM less the stack and the interrupt vector table.

        ; Now setup the initial state of the memory management system
        ; freehead points to single region
        ;      |                       |                   |                             |
        ;      | size = empty + header | next = itself (2) | ........ empty ............ |
        ;      |    word 0             |   word 1          |^                            |
        ;                                                   |
        ;  freehead ----------------------------------------|

		LDR freehead, =(VM_HEAP_START + RAM_BlockHeaderSize	); initialize to heap start = start of 'empty' region
	    LDR value, =(VM_HEAP_SIZE) ; initial heap size is the entire heap (allocated to the first block)
		STR value, [freehead, #RAM_BlockHeaderSizeOffset] ; store initial size
		STR freehead, [freehead, #RAM_BlockHeaderNextOffset] ; 'next' points to itself (start of 'empty' region)
		FINISHED_freehead  ; store freehead register

		; initialize to 0's

	    LDR eligiblehead, =NULLREGISTER
		FINISHED_eligiblehead

		LDR waitinghead, =NULLREGISTER
		FINISHED_waitinghead

															 
		MOV PCW, #0 ; 4-byte instruction because PCW is not a low register.


	 	
		; The last thing we do is set Thread mode's stack to SP_process.
		SET_StackModeProcess


		BX lr	; return

;*************************************
;
;   Virtual Machine Startup
;
;   Tasks:
;      Create a new instance of a Thread object
;      and initialize as if Thread.start() was called.      
;      Return control to the main loop where
;      the first instruction to be executed
;      will be the first one in the run() method.
;
;*************************************
vm_startup

	; The goal of this process is to instantiate a new Thread to serve
	; as the initial context in which to start execution.  Execution
	; beings with the run() method.

	IMPORT table_lookup
	IMPORT frameinfo_lookup
	IMPORT allocate_memory
	IMPORT frame_setup
	IMPORT objectsize_lookup
	IMPORT object_setup
	IMPORT threadrun_setup
 	IMPORT eligiblequeue_insert
	IMPORT eligiblequeue_process
        
	          
    ; Load the first word of NVM into CLASSHANDLE register.
	; This is the Thread to create an instance of.

	LDR classhandle, =(VM_NVM_BASE) ; load the base offset into the temp register
	LDR classhandle, [classhandle, #0] ; load classhandle
                    
	; Partial "NewInstance" process:
	BL objectsize_lookup
	BL allocate_memory
	BL object_setup

	; Load name and descriptor of Thread's void run() method.
	LDR descriptor, =(VM_NVM_BASE)
	LDR name, [descriptor, #4]	   ; NVM_BASE[4]

	LDR descriptor, [descriptor, #8] ; NVM_BASE[8]

	; Set currentthread to the new Thread instance.
	MOV currentthread, value
	
	; Now start the Thread using partial "ThreadStart" process:
	SetupMethodLookup
	SetupVirtualLookup
	BL table_lookup
	BL frameinfo_lookup
	BL allocate_memory	
                  
    ; Pre-initialize the Thread so that currentframe is pointing to the new frame already
    ; and that 'this' in that frame is setup properly to support ThreadRunSetup without a calling frame.
        
    ; Store currentframe into thread
	STR handle, [currentthread, #THREAD_CurrentFrameOffset]
	     
    ; Store 'this' into local var[0] directly
    STR currentthread, [handle, #FRAME_LocalVarOffset]

	; Setup the new Thread's context and run() frame.
	BL threadrun_setup

	; Clear the currentthread again to make the following processes work.
	MOV currentthread, #NULLREGISTER            

	BL eligiblequeue_insert
	BL eligiblequeue_process


	B main_execution_loop ; all roads lead to the execution loop.


;*************************************************************
;
;   Peripheral initialization
;
;   Tasks:
;      Initialize the peripheral hardware
;      that may be access via the 'peripheral'
;      bytecode.
;
;*************************************************************
peripheral_init

	INCLUDE vmperipheralinclude.s
	IMPORT IntMasterDisable
	
	; push the link register onto the stack (main stack!)
	PUSH {r0, lr}	 ; perserve 8-byte alignment

	; Disable main interrupts before doing anything!
	BL IntMasterDisable

;----------------------------------------
	; Initialize the OLED display.
		; SysCtlClockSet(SYSCTL_SYSDIV_1 | SYSCTL_USE_OSC | SYSCTL_OSC_MAIN |
    ;                SYSCTL_XTAL_8MHZ);

	IMPORT SysCtlClockSet

	; setup the parameters
	;LDR r1, =SYSCTL_SYSDIV_1
	;ORR r0, r1, #SYSCTL_USE_OSC
	MOV r0, #0 ; clear the reg first
	ORR r0, r0, #SYSCTL_SYSDIV_1
	ORR r0, r0, #SYSCTL_USE_OSC
	ORR r0, r0, #SYSCTL_OSC_MAIN
	ORR r0, r0, #SYSCTL_XTAL_8MHZ
	BL SysCtlClockSet
;----------------------------------------

    ; //
    ; // Initialize the OLED display.
    ; //
    ; RIT128x96x4Init(1000000);

	IMPORT RIT128x96x4Init
	
	LDR r0, =1000000
	BL RIT128x96x4Init

   ; RIT128x96x4StringDraw("Hello?", 20, 10, 15);
	IMPORT RIT128x96x4StringDraw
	
	LDR r0, =startupstring
	LDR r1, =0
	LDR r2, =0
	LDR r3, =15
	BL RIT128x96x4StringDraw
;----------------------------------------


	; Initialize the System timer (systick timer)
	; but don't enable interrupts yet!

	; Initialize the timer register:
	; NOTE! We must be using MAIN STACK still.
	MOV32 value2, #0x7fffffff ; largest signed value
	LDR value1, =RAM_BASE
	STR value2, [value1, #(SYSTEM_REG_OFFSET + TIMER_OFFSET)]

	IMPORT SysTickPeriodSet
	IMPORT SysTickEnable
	IMPORT SysTickIntEnable
	IMPORT SysCtlClockGet

	;MOV32 r0, #0x007fffff  ; load in the maximum number of ticks
	BL SysCtlClockGet ; put result in r0 I hope
	;MOV r0, #1000
	BL SysTickPeriodSet ; use result of SysCtlClockGet to setup a 1 second period timer.
	BL SysTickEnable
	;BL SysTickIntEnable

;----------------------------------------

	; Initialize the UART

	IMPORT SysCtlPeripheralEnable
	MOV32 r0, #0x10000001   ; UART 0 
	BL SysCtlPeripheralEnable
	MOV32 r0, #0x20000001   ; GPIO A 
	BL SysCtlPeripheralEnable

	; Set GPIO A0 and A1 as UART pins.
	IMPORT GPIOPinTypeUART
	MOV32 r0, #0x40004000     ; GPIO PORTA BASE
	MOV r1, #3              ; PIN 0 (1) | PIN 1 (2)
	BL GPIOPinTypeUART

	; Configure for 115,200, 8-N-1 operation
	IMPORT UARTConfigSetExpClk
	MOV32 r0, #0x4000C000  ; UART0 BASE
	MOV32 r1, #8000000  ; SysClkGet
	MOV32 r2, #115200   ; baud
	MOV r3, #0x60          ; 8-n-1
	BL UARTConfigSetExpClk

	IMPORT UARTCharPutNonBlocking
	LDR r6, =startupstring
	MOV r5, #17
uart_send_loop
	CBZ r5, continue_init
	MOV32 r0, #0x4000C000  ; UART0 BASE 
	LDRB r1, [r6], #1
	BL UARTCharPutNonBlocking
	SUB r5, r5, #1
	B uart_send_loop
continue_init

;----------------------------------------

	; Now we can enable interrupts... well... we should wait more but who cares.
	; As long as the timer register is initialized to something.
	IMPORT IntMasterEnable
	;BL IntMasterEnable


	POP {r0, pc} ; return

;*************************************

	ALIGN 4
	
	;AREA VMStrings, DATA, READWRITE
startupstring	DCB "VM starting up...",0
	ALIGN

   		END
