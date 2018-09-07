;****************************************************************
; 
;    Date: Apr 7, 2008
;    Author: Evan Ross
;
;	 Inserts a thread into the eligible queue.  The thread is inserted based on priority.  Lower priority value
;    is placed towards the head of the queue and consequently is considered to be higher priority.
;
;    This queue is a priority queue, implemented as a linked list.  The linked elements are the
;    Thread objects created in the JVM runtime.  
;
;    Register Set:
;       Input: ELIGIBLEHEAD - points to head of queue
;              VALUE - points to Thread object to add to queue ('this' popped off stack into value)
;       Intermediate:
;              NAME
;              DESCRIPTOR
;              VALUE1
;              VALUE2
;
;****************************************************************

   	AREA eligiblequeueinsertarea, CODE, READONLY

	INCLUDE vmregisters.s
	INCLUDE ramconstants.s  
	INCLUDE internalclass.s

	EXPORT eligiblequeue_insert

eligiblequeue_insert
		   
	; First load register with eligiblehead
	USES_eligiblehead

	; Handle special case first -- empty list
	CMP eligiblehead, #NULLREGISTER
	BNE middle_or_end

		; Thread.next = null
		MOV name, #NULLREGISTER
		STR name, [value, #(OBJECT_Size + THREAD_NextOffset)]

		; Assign Thread as first element of the list
		; head = Thread
		MOV eligiblehead, value

		B end_eligiblequeue_insert ; return

middle_or_end

	; There is at least one element in the list

	; Copy the queue head to a temp variable - 'before'
	MOV name, eligiblehead

	; Copy the queue head to a temp variable - 'current'
	MOV descriptor, eligiblehead

    ; Get the thing we are comparing against -- priority
    LDR value1, [value, #(OBJECT_Size + THREAD_PriorityOffset)]	

    ; Keep searching the list while current (descritor) is not null.
    ; If it is null, we are inserting into an empty list, or we have
    ; traversed a list and this is where the comparison has led us (all
    ; other elements greater/less than)
search_loop_top

	CMP descriptor, #NULLREGISTER
	BEQ search_loop_end ; loop while not equal
		 
		; Get priority of this Thread in the list
		LDR value2, [descriptor, #(OBJECT_Size + THREAD_PriorityOffset)]

		; Do a compare - higher priority than this element (lower value)?
		CMP value1, value2
		BLT search_loop_end ; if (value1 < value2) break;

		; before = current
		MOV name, descriptor

		; get the next element in the list current = current.next
		LDR descriptor, [descriptor, #(OBJECT_Size + THREAD_NextOffset)]

		B search_loop_top  ; loop

search_loop_end

	; Found a slot in the linked list - now insert using one of two techniques
	
 	; Inserting at top of list?
	CMP descriptor, eligiblehead
	BNE insert_middle_or_end

		; Yes
		; Thread.next = current
		STR descriptor, [value, #(OBJECT_Size + THREAD_NextOffset)]
		
		; Inserting element at top of list:
		MOV eligiblehead, value
			
		B end_eligiblequeue_insert

insert_middle_or_end

	; else	insert into middle or end of list

		; before.next = Thread
		STR value, [name, #(OBJECT_Size + THREAD_NextOffset)]

		; Thread.next = current
		STR descriptor, [value, #(OBJECT_Size + THREAD_NextOffset)] 


end_eligiblequeue_insert

	FINISHED_eligiblehead
 
	BX lr ; return

	
	END
	
		

