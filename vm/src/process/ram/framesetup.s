;****************************************************************
;  
;    framesetup.s
;  
;    Date: Mar 25, 2008
;    Author: Evan Ross
;
; Responsible for backing up registers to old frame and copying arguments over to new frame
; including objectref if a virtual method is used ('index' == 1)
;
; Also sets CURRENTCLASS to be the class of the new frame, which was set in CLASSHANDLE during
; the TableLookup process.  Assign CLASSHANDLE to CURRENTCLASS after backing up CURRENTCLASS
; into the old frame.
;
; Register set:
;   Input:  handle - address of newly allocated frame
;           pcw - specifies if virtual method and objectref is copied to local array
;			classhandle - pointer to InternalClass of new frame.
;           value2 - number of args to copy over in units of words.
;           value1 - number of locals from Allocate memory
; 
;
;****************************************************************

	AREA framesetuparea, CODE, READONLY

	INCLUDE vmregisters.s
	INCLUDE vmexceptions.s
	INCLUDE ramconstants.s  
	INCLUDE internalclass.s
	INCLUDE lookupsetup.s

	EXPORT frame_setup

frame_setup

		; Convert value2 into units of bytes.
		LSL value2, value2, #2

		; Load currentframe into name
        ; Load currentclass into descriptor
		LDR name, [currentthread, #THREAD_CurrentFrameOffset]
		LDR descriptor, [name, #FRAME_CurrentClassOffset]
 
        ; 1. Store CURRENTFRAME register into *new* frame's "previous frame" pointer
        STR name, [handle, #FRAME_PreviousFrameOffset]

        ; 2. Store PC register into *old* frame's "pc" word
		STR vmpc, [name, #FRAME_PCOffset]

        ; 3. Store new current class (stored in classhandle) in *new* frame.
		STR classhandle, [handle, #FRAME_CurrentClassOffset]
        
   		; 4 Copy the args (count in VALUE2) only if there was a previous frame (i.e., a stack)
		CMP name, #NULLREGISTER
		BEQ no_stack
			
			; If the 'virtual' lookup flag was set, then this is an instance frame and
			; therefore 'this' needs to be copied over.
			TST PCW, #PCW_FLAG_LOOKUP_SET  ; test 'virtual' lookup bit
			ADDNE value2, value2, #4 ; add 1 word for objectref (this)

		  		; while (value2 > 0)
start_loop		CBZ	value2, end_loop  ; if == 0 end loop

				; count down for each argument to copy over			
				SUB value2, value2, #4

				; pop the top arg off of the old frame and decrement its stackpointer
				LDR value, [stackpointer, #-4]!  ; value = RAM[--stackpointer]

				; copy the arg into the new frame
				ADD index, handle, value2
				STR value, [index, #FRAME_LocalVarOffset]
				
				B start_loop
end_loop

			; Now save the old stackpointer to the previous frame
			STR stackpointer, [name, #FRAME_StackPointerOffset]


no_stack

        ; setup the new stack pointer = base pointer + start of local vars + size of local vars
		ADD index, handle, value1
		ADD index, index, #FRAME_LocalVarOffset
		MOV stackpointer, index		  ; stupid assembler won't let me use stackpointer in the add instructions.		
        
        ; and finally set the currentframe to the new frame pointer in 'handle'
		STR handle, [currentthread, #THREAD_CurrentFrameOffset]
	
	        
 		BX lr ; return
    
	
	END	