;****************************************************************
;
;   frameteardown.s
;    
;	Date: Apr 22, 2008
;   Author: Evan Ross
;
;
;
;   FrameTeardown.  Do opposite of FrameSetup.  Restore registers.
;
;   Registers:
;     Input:
;     Output: handle, for deallocate memory
; 
;     Protected (do not use): VALUE, VALUE1 or VALUE2, for non-void returns.
;
;
;****************************************************************

	AREA frameteardownarea, CODE, READONLY

	INCLUDE vmregisters.s
	INCLUDE vmexceptions.s
	INCLUDE ramconstants.s  
	INCLUDE internalclass.s
	INCLUDE lookupsetup.s

	EXPORT frame_teardown

frame_teardown


	; Load currentframe into 'handle'
	; so that DeallocateMemory has something to work with
	LDR handle, [currentthread, #THREAD_CurrentFrameOffset]

	; Restore CURRENTFRAME register from old frame's "previous frame" pointer
	LDR name, [handle, #FRAME_PreviousFrameOffset]

	; these two are a good candidate for a load multiple.

	; Restore PC register from old frame's "old pc" register
	LDR vmpc, [name, #FRAME_PCOffset]

	; Restore stackpointer from the now restored frame
	LDR stackpointer, [name, #FRAME_StackPointerOffset]

	; Store the old frame as currentframe 
	STR name, [currentthread, #THREAD_CurrentFrameOffset]


    ; *** We must stop here if the restored currentframe is null.  This means that the current thread
    ;     must terminate. 
	CMP name, #NULLREGISTER
	BXNE lr  ; return if not null (normal exit).

	; The thread has terminated.  The software shouldn't really do this.
	; Throw an exception.
	ThrowException ThreadTerminatedException
 

	END
