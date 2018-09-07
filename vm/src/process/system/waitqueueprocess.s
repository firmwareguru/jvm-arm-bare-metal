;****************************************************************
;
; waitqueue_process.s
; 
; Date: May 3, 2008
; Author: Evan Ross
;
;
; Processing the wait queue is performed as follows.
;    Wait queue is iterated through starting at the head.
;    Thread elements whose wakeuptime is greater than current system timer count 
;    are removed from the wait queue and added to the eligible queue.
;    When a wait queue thread is found with a wakeuptime less than system timer count
;    the processing stops because wait queue elements are ordered according to
;    increasing wakeuptime (decreasing time).
;
;    The costly activity is EligibleQueueInsert, whose time to insert is linearly 
;    dependent on number of items already in the queue.
;
;  Registers:
;     input: none
;     output: index - flag for the scheduler to determine if something
;             was unlinked.
;
;****************************************************************

   	AREA waitqueueprocessarea, CODE, READONLY

	INCLUDE vmregisters.s
	INCLUDE ramconstants.s  
	INCLUDE internalclass.s
	INCLUDE vmexceptions.s

	EXPORT waitqueue_process

waitqueue_process

	USES_waitinghead  ; load in the waitinghead register
	CMP waitinghead, #NULLREGISTER
	MOVEQ index, #0 ; 'return' 0 to indicate the nothing was unlinked
	BXEQ lr  ; return

	; Examine 'wakeuptime' at element at head of queue.
	LDR value, [waitinghead, #(OBJECT_Size + THREAD_DelayOffset)]

	; If system timer is greater than wakeuptime, sleep time has yet to expire.
	; Load the current timer into value1
	LoadTimer

	CMP value1, value
	MOVGT index, #0  ; return 0 to indicate nothing was unlinked
	BXGT lr  ; return

    ; *** Set a flag that EligibleQueueInsert process uses to decide whether to call
    ;     WaitingQueueProcess again or not 

	MOV index, #1

	; Value is used by EligibleQueueInsert to insert the Thread
	MOV value, waitinghead

	; Unlink this element
	LDR waitinghead, [waitinghead, #(OBJECT_Size + THREAD_NextOffset)]

	; store waitinghead
	FINISHED_waitinghead

	BX lr ; return
	

	END