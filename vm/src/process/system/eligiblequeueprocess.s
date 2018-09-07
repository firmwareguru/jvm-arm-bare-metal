;****************************************************************
; 
;    Date: Apr 7, 2008
;    Author: Evan Ross
;
; This is part II of the Scheduler.  EligibleQueueProcess performs 
; the context switching if necessary. If currentthread is not the 
; thread at the top of the eligible queue, then there is a higher priority 
; thread ready to run:
;     1. Save registers to currentthread
;     2. Restore registers from eligiblehead
;     3. Set currentthread = eligiblehead
;
;****************************************************************

   	AREA eligiblequeueprocessarea, CODE, READONLY

	INCLUDE vmregisters.s
	INCLUDE ramconstants.s  
	INCLUDE internalclass.s
	INCLUDE vmexceptions.s

	EXPORT eligiblequeue_process

eligiblequeue_process

	USES_eligiblehead

	CMP eligiblehead, currentthread
	BXEQ lr ; if eligiblehead == currentthread, return

	; else

    ; Save registers to currentthread only if it is non-null.  It could be null if a context-less frame died.
	; I don't think there are any context-less frames now...
	CMP currentthread, #NULLREGISTER
	BEQ restore_registers

	; save registers to currentthread

	    ; The only registers we save are PC and stackpointer.  They are saved
		; to the Thread's current frame.
	
		; name = currentframe
		LDR name, [currentthread, #THREAD_CurrentFrameOffset]			

		STR vmpc, [name, #FRAME_PCOffset]
		STR stackpointer, [name, #FRAME_StackPointerOffset]
	

restore_registers
 
	; restore the registers	from the highest priority thread in the queue.

    ; What if eligiblehead is null?  What if some idiot sleeps the only thread?  or the idle thread?
    ; We should issue an error!
    CMP eligiblehead, #NULLREGISTER   ; if( eligiblehead == nullregister )
	ThrowException EligibleQueueEmpty ; throwException(ELIGIBLE_QUEUE_EMPTY)

	; Get pointer to new Thread's current frame
	LDR name, [eligiblehead, #THREAD_CurrentFrameOffset]
	
	LDR	stackpointer, [name, #FRAME_StackPointerOffset]
	LDR vmpc, [name, #FRAME_PCOffset]
	
	; lastly set the currentthread to the high priority thread at top of the queue
	MOV currentthread, eligiblehead	 
	
	; Don't need to save eligiblehead because it wasn't modified.

	BX lr ; return

	END
