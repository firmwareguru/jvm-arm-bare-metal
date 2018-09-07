;****************************************************************
; 
;    Date: May 5, 2008
;    Author: Evan Ross
;
; Inserts a thread into the wait queue.  The thread is inserted based on delay.  The system timer is
; a count down timer hence higher delay values are placed towards the head of the queue.  The delay
; value is computed at the time of sleep invocation as current time - requested delay (in ticks).
;
; This queue is a priority queue, implemented as a linked list.  The linked elements are the
; Thread objects created in the JVM runtime.  The method Thread.sleep(int delay) adds the current Thread object, 
; pointed to by currentthread, to the queue.
;
; Register Set:
;   Input: WAITHEAD - points to head of queue
;          CURRENTTHREAD - points to Thread object to add to queue.
;   Intermediate:
;          NAME
;          DESCRIPTOR
;          VALUE1
;          VALUE2
;
;****************************************************************

   	AREA waitqueueinsertarea, CODE, READONLY

	INCLUDE vmregisters.s
	INCLUDE ramconstants.s  
	INCLUDE internalclass.s

	EXPORT waitqueue_insert

waitqueue_insert
		   
	; First load register with waitinghead
	USES_waitinghead

	; Handle special case first -- empty list
	CMP waitinghead, #NULLREGISTER
	BNE middle_or_end

		; Make sure 'next' is null, Thread.next = null
		MOV name, #NULLREGISTER
		STR name, [currentthread, #(OBJECT_Size + THREAD_NextOffset)]

		; Assign Thread as first element of the list
		; head = Thread
		MOV waitinghead, currentthread

		B end_waitqueue_insert ; return

middle_or_end

	; There is at least one element in the list

	; Copy the queue head to a temp variable - 'before'
	MOV name, waitinghead

	; Copy the queue head to a temp variable - 'current'
	MOV descriptor, waitinghead

    ; Get the thing we are comparing against -- delay
    LDR value1, [currentthread, #(OBJECT_Size + THREAD_DelayOffset)]	

    ; Keep searching the list while current (descritor) is not null.
    ; If it is null, we are inserting into an empty list, or we have
    ; traversed a list and this is where the comparison has led us (all
    ; other elements greater than)
search_loop_top

	CMP descriptor, #NULLREGISTER
	BEQ search_loop_end ; loop while not equal
		 
		; Get delay of this Thread in the list
		LDR value2, [descriptor, #(OBJECT_Size + THREAD_DelayOffset)]

		; Do a compare - is the new thread's delay less than the current thread in the list?
		CMP value1, value2
		BGT search_loop_end ; if (value1 > value2) break;

		; before = current
		MOV name, descriptor

		; get the next element in the list current = current.next
		LDR descriptor, [descriptor, #(OBJECT_Size + THREAD_NextOffset)]

		B search_loop_top  ; loop

search_loop_end

	; Found a slot in the linked list - now insert using one of two techniques
	
 	; Inserting at top of list?
	CMP descriptor, waitinghead
	BNE insert_middle_or_end

		; Yes
		; Thread.next = current
		STR descriptor, [currentthread, #(OBJECT_Size + THREAD_NextOffset)]
		
		; Inserting element at top of list:
		MOV waitinghead, currentthread
			
		B end_waitqueue_insert

insert_middle_or_end

	; else	insert into middle or end of list

		; before.next = Thread
		STR currentthread, [name, #(OBJECT_Size + THREAD_NextOffset)]

		; Thread.next = current
		STR descriptor, [currentthread, #(OBJECT_Size + THREAD_NextOffset)] 


end_waitqueue_insert

	FINISHED_waitinghead
 
	BX lr ; return

	
	END
	
		

