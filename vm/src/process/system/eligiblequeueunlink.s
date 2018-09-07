;****************************************************************
;
;  eligiblequeueunlink.s
; 
;    Date: May 5, 2008
;    Author: Evan Ross
;
; Unlink the Thread object in the Eligible Queue linked list.  For a singly linked list, this
; requires traversing the list from the head until the currentthread element is found, 
; while tracking the 'before' element (yes we already know currentthread, but we need to find
; the thread, if any, in the list before it).
;
; For a doubly linked list, this is simply:
;    currentthread.previous.next = currentthread.next;
;    currentthread.next.previous = currentthread.previous;
;
; A Doubly linked list would be a faster and simpler implementation, however this is at the expense
; of increased memory usage (the 'previous' variable in each Thread).
;
; In moderate to small thread systems the currentthread will almost always be at the top of
; the queue anyways, so a doubly linked list doesn't offer so great an advantage.
;
; There are two cases to consider: currentthread is at the head of the queue or
;  currentthread is in the middle or end.
;
; Register Set:
;   Input: currentthread - this is always the thread we are unlinking
;   Intermediate:
;          eligiblehead
;          name
;          descriptor
;
;****************************************************************

   	AREA eligiblequeueunlinkarea, CODE, READONLY

	INCLUDE vmregisters.s
	INCLUDE ramconstants.s  
	INCLUDE internalclass.s
	INCLUDE vmexceptions.s

	EXPORT eligiblequeue_unlink

eligiblequeue_unlink


	USES_eligiblehead  ; r8 => cphandle

    ; Handle case 1: Currentthread is at the head of the queue (most likely case)
	CMP eligiblehead, currentthread
	BNE unlink_middle

    	; unlink: eligiblehead = currentthread.next
		LDR eligiblehead, [currentthread, #(OBJECT_Size + THREAD_NextOffset)]
	
		FINISHED_eligiblehead
	
		; that's it!
		BX lr ; return	


unlink_middle

	; name = before
	; descriptor = current
	MOV name, eligiblehead
	MOV descriptor, eligiblehead

unlink_loop
	; traverse the list to find the currentthread
	; while (descriptor != currentthread)
	CMP descriptor, currentthread
	BEQ unlink_finish

		; before = current
		MOV name, descriptor

		; current = current.next
		LDR descriptor, [descriptor, #(OBJECT_Size + THREAD_NextOffset)]
		
		B unlink_loop
		

unlink_finish

	; get current.next
	LDR descriptor, [descriptor, #(OBJECT_Size + THREAD_NextOffset)]

	; set before.next
	STR descriptor, [name, #(OBJECT_Size + THREAD_NextOffset)]

	; eligiblehead not modified so no need to save it.

	BX lr  ; return


	END


