;****************************************************************
;  
;    threadrunsetup.s
;  
;    Date: Mar 29, 2008
;    Author: Evan Ross
;  
; Responsible for setting up a Frame for the run() method of a new Thread.  It copies over arguments to new frame
; including objectref.  Combines functionality of SetPCProcess.
;
; The currentframe is assumed to be the Thread.start() method.  The start() method has no stack.  'this' is in local array [0].
; The currentframe is not modified!
;
; Sets run()'s frame's previousframe to NULLREGISTER.
; 
; New Thread has it's context pre-set:
;    - currentframe is set to handle
;    - stackpointer in thread is set
;    - pc in thread is set
;     - currentclass is set to classhandle
;
; Register set:
;   Input:  handle - address of newly allocated frame
;           tablehandle - method table entry for the run method.
;           index - specifies if virtual method and objectref is copied to local array
;
;****************************************************************

   	AREA threadrunsetuparea, CODE, READONLY

	INCLUDE vmregisters.s
	INCLUDE ramconstants.s  
	INCLUDE internalclass.s

	EXPORT threadrun_setup

threadrun_setup

    ; Set the new frame's "previousframe" pointer to null
	MOV value, #NULLREGISTER
	STR value, [handle, #FRAME_PreviousFrameOffset]

	; Load currentframe into name register
	LDR name, [currentthread, #THREAD_CurrentFrameOffset]
	
	; Load 'this' from var[0] of run() frame directly
	LDR value, [name, #(FRAME_LocalVarOffset + 0)]

	; Copy 'this' into new frame as arg 0
	STR value, [handle, #(FRAME_LocalVarOffset + 0)]

    ;==================================================
    ;   Thread reference is in VALUE -- don't modify!
    ;==================================================
        
    ; This stuff goes into Thread
        
    ; Setup the new stack pointer = base pointer + start of local vars + size of local vars
    ADD value1, value1, handle
	ADD value1, value1, #FRAME_LocalVarOffset    
        
    ; Store in new Frame's STACKPOINTER. handle points to new frame.
    STR value1, [handle, #FRAME_StackPointerOffset]

    ; Set the Thread's currentframe to the new frame for Run()
	STR handle, [value, #THREAD_CurrentFrameOffset]

    ; Get code pointer from MethodTableEntry 
	LDRH value1, [tablehandle, #METHODTABLEENTRY_CodePtrHalfOffset]  ; halfword load.

	; Get code table entry
	ADD index, value1, tablehandle ; code ptr is tablehandle + code ptr offset

    ; Get the start of the code array.  This is the new PC.  For now, store in tableentryhandle
    ; Set the PC.  
	ADD value2, index, #CODETABLEENTRY_CodeOffset
 	STR value2, [handle, #FRAME_PCOffset]

    ; Set the all important currentclass after any previous TableLookups have found the class containing the method
    ; Currentclass was already backed up in the FrameSetup process
	STR classhandle, [handle, #FRAME_CurrentClassOffset]
        
    ; As a last step, set the index register to 0 so that the following EligibleQueueProcess doesn't
    ; request a call to WaitQueueProcess
	MOV index, #0

	BX lr


	END

