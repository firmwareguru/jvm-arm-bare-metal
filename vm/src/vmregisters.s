;****************************************************************
;  
;    vmregisters.h
;  
;    Date: Feb 22, 2008
;    Author: Evan Ross
;  
;    The CPU registers that the VM uses are
;    aliased here to provide names that are consistent with the
;    the names used in the Java simulation version.
;  
;    The ARMv7-M processor has 13 registers available for use.
;    However, there are more than 13 registers used by the VM.
;    Therefore there are macros that load in values from the
;    main stack into registers for use by processes.
;
;****************************************************************

; Offset definitions for system registers stored in the system register block
;				 timer          [3]
;                eligiblehead   [2]
;                waitinghead	[1]    
; base offset -> freehead		[0]

TIMER_OFFSET	    EQU 12
ELIGIBLEHEAD_OFFSET	EQU  8
WAITINGHEAD_OFFSET	EQU  4
FREEHEAD_OFFSET		EQU	 0

value			RN r0
value1			RN r1
value2			RN r2
handle			RN r3
index			RN r4
name			RN r5 
descriptor		RN r6
classhandle		RN r7
cphandle		RN r8
tablehandle		RN r9
currentthread	RN r10	; refers to remainder of context storage
PCW				RN r11	; the Process Control Word
vmpc			RN r12  ; Thread context registers.
stackpointer	RN r13	; Use the Thread-mode stack pointer - SP_process

; alternate layout for some registers
freehead		RN r4 ; freehead used in place of index by memory allocation processes
eligiblehead	RN r8 ; eligiblehead used in place of cphandle in scheduling processes
waitinghead		RN r8 ; waitinghead used in place of cphandle in scheduling processes

; other defines
NULLREGISTER	EQU	0xffffffff

; Memory regions for VM
;
;   ------------ TOP 0x10000 (64K)
;	|          |
;	~		   ~
;   |   HEAP   |  <- the VM's heap.  begins at the top of RAM and ends here.
;   |          |
;   |----------|
;   | Interrupt|  <- pointers to Threads that handle VM-level interrupts.
;   | vector   |
;   | table    |
;   |----------|
;   | system   |  <- any registers that don't fit into the processor's registers
;   | registers|
;   |----------|
;   | SP_main  |  <- main stack used by exceptions in Handler mode.
;   |          |
;   |__________| 0x0
;
;   These constants are used to derive the heap size and the system register block offset 
RAM_BASE			EQU 0x20000000
MAIN_STACK_SIZE		EQU 0x110 + 4 ; I don't know why, but this is whats in the PC on startup (extra 16 bytes)
SYSTEM_REG_SIZE		EQU 16 ; 4 registers
VECTOR_TABLE_SIZE	EQU	48 ; 16 entries (words) * 4 bytes each
VM_NVM_BASE			EQU 0x10000 ; 64k is start of NVM
RAM_SIZE			EQU 0x10000 ; 64k of total RAM

; Size of heap.  Starts from bottom of RAM.  Must not overwrite stack or any
; other RAM area.
;    64K - stack - vector table
; Absolute start of heap
VM_HEAP_START	EQU RAM_BASE + MAIN_STACK_SIZE + SYSTEM_REG_SIZE + VECTOR_TABLE_SIZE
VM_HEAP_SIZE	EQU	RAM_SIZE - MAIN_STACK_SIZE - SYSTEM_REG_SIZE - VECTOR_TABLE_SIZE	
SYSTEM_REG_OFFSET 	EQU	MAIN_STACK_SIZE  ; address in RAM where system regs are stored.

; PCW masks
PCW_INTERRUPT_MASK  EQU 0xffff0000
PCW_PROCESS_MASK 	EQU 0x000000ff   
PCW_FLAGS_MASK 		EQU 0x0000ff00
PCW_FLAGS_MASK_INV	EQU 0xffff00ff
PCW_FLAGS_SHIFT 	EQU 8
PCW_PROCESS_SCHEDULER EQU 1
PCW_PROCESS_SLEEP	  EQU 2

; Macros for accessing off-processor registers.  The values in the specified
; location in memory are loaded into/from a designated register.

    ;----------------------------------------
	;  Load and store the freehead register
	;----------------------------------------
	MACRO
	USES_freehead

	LDR freehead, =RAM_BASE  
	LDR	freehead, [freehead, #(SYSTEM_REG_OFFSET + FREEHEAD_OFFSET)]

	MEND

	MACRO
	FINISHED_freehead

	LDR cphandle, =RAM_BASE  ; trample over cphandle
	STR	freehead, [cphandle, #(SYSTEM_REG_OFFSET + FREEHEAD_OFFSET)]

	MEND


    ;----------------------------------------
	;  Load and store the eligiblehead register
	;----------------------------------------
	MACRO
	USES_eligiblehead

	LDR eligiblehead, =RAM_BASE  
	LDR	eligiblehead, [eligiblehead, #(SYSTEM_REG_OFFSET + ELIGIBLEHEAD_OFFSET)]

	MEND

	MACRO
	FINISHED_eligiblehead

	LDR index, =RAM_BASE  ; trample over index
	STR	eligiblehead, [index, #(SYSTEM_REG_OFFSET + ELIGIBLEHEAD_OFFSET)]

	MEND


    ;----------------------------------------
	;  Load and store the freehead register
	;----------------------------------------
	MACRO
	USES_waitinghead

	LDR waitinghead, =RAM_BASE  
	LDR	waitinghead, [waitinghead, #(SYSTEM_REG_OFFSET + WAITINGHEAD_OFFSET)]

	MEND

	MACRO
	FINISHED_waitinghead

	LDR index, =RAM_BASE  ; trample over index
	STR	waitinghead, [index, #(SYSTEM_REG_OFFSET + WAITINGHEAD_OFFSET)]

	MEND

    ;----------------------------------------
	;  Load the timer 
	;  The timer gets loaded into VALUE1
	;----------------------------------------
	MACRO
	LoadTimer

	;LDR value1, =RAM_BASE
	;LDR value1, [value1, #(SYSTEM_REG_OFFSET + TIMER_OFFSET)]
	MOV32 value1, #0xE000E018 ; address of NVIC_ST_CURRENT
	LDR value1, [value1]

	MEND



; Macros for setting stack used in Thread mode.
; System starts in Thread mode with SP_main by default.
; Use R0 (value) for temporary storage
	
	MACRO
	SET_StackModeProcess

	MRS	r0, CONTROL   ; copy out CONTROL register
	ORR r0, #0x2      ; set CONTROL[1]
	MSR	CONTROL, r0   ; move it back into CONTROL register	

	MEND

	MACRO
	SET_StackModeMain

	MRS	r0, CONTROL   ; copy out CONTROL register
	AND r0, #0xfffffffd      ; clear CONTROL[1] 
	MSR	CONTROL, r0   ; move it back into CONTROL register	

	MEND

;----------------------------------------
;   Macros for setting the PCW
;----------------------------------------

	MACRO
	SetPCW $process

	AND PCW, PCW, #~PCW_PROCESS_MASK  ; clear the process mask bits
	ORR PCW, PCW, #$process            ; or in the process bits


	MEND


	END