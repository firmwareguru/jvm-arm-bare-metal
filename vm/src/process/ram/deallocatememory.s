;****************************************************************
; 
;  	 Deallocatememory.s
; 
;    Date: Apr 26, 2008
;    Author: Evan Ross
;
;
;    Register Set:
;    Input: HANDLE - address of object to deallocate
;
;    Registers used:
;           tablehandle - 
;           descriptor - 
;           cphandle -
;
;    Protected (do not use): VALUE, VALUE1 or VALUE2, for non-void returns.
;        
; The DeallocateMemoryProcess is designed to compliment the AllocateMemory process.  These
; techniques are described in Kernighan & Ritchie C programming book, section 8.7 "A storage allocator".
; 
; Deallocating memory is a little more complicated, but not too much.  This technique is really quite
; interesting and should be quite efficient.
;
; There are 4 steps to memory deallocation:
;    1.  Iterate through free list, updating 'current' until a suitable location is found.
;        A suitable location is:
;           a.  block is between 2 existing blocks, i.e., handle > current && handle < current.next
;           b.  block is at either end of list:
;                  current >= current.next && (handle > current || handle < current.next)
;    2.  Combine upper adjacent block if possible.  The upper block is higher in memory (larger address)
;    3.  Combine lower adjacent block if possible.  The lower block is lower in memory (lower address)
;    4.  Set freehead = current
;
; These steps must be performed in order since step 3 relies on step 2 having been performed.
;
; Note that for step 4 is not obvious why it is done, but by doing it, in step 1.A.b the test "handle < current.next" is necessary.
; The freehead moves around thus "beginning" and "end" of the list may have different meanings each time.  Just remember that
; the "end" of the free list (the last block in the chain) always points to the first block in the chain.
;
; Examples of list configurations prior to deallocation:
;
; Deallocated block at beginning of list:
;
;               |--------------|
;              \/              |
;    used    free    used    free  
;      ^        |              ^
;      |        ---------------|
;   handle                     |
;                         freehead
;   
; Deallocated block in middle of list:
;
;   -------------------------------|
;   | |-------------|    handle    |
;  \/ |            \/      \/      |
;  free  used    free    used    free  
;   ^               |              ^
;   |               ---------------|
;   |
; freehead
;
;****************************************************************


	AREA deallocatememoryarea, CODE, READONLY

	INCLUDE vmregisters.s
	INCLUDE vmexceptions.s
	INCLUDE internalclass.s
	INCLUDE ramconstants.s  

	EXPORT deallocate_memory

deallocate_memory

	; *** Load the freehead register (overwrite index register).
	;     Freehead is moved into a working register, and then only
	; 	  updated at the END of this process. The index register is
	;     therefore available as a temporary register.
	USES_freehead

	; cphandle = 'current'

	; current = freehead
	MOV cphandle, freehead

	; 1. Search freelist for a spot to put pack the block
search_loop

	LDR name, [cphandle, #RAM_BlockHeaderNextOffset]

	CMP handle, cphandle
	BLE search_loop_test2 ; if handle <= cphandle, go to next test set
	CMP handle, name
	BGE search_loop_test2 ; if handle >= name, go to next test set
	B end_search_loop ; both conditions were met, break

search_loop_test2
	CMP cphandle, name
	BLT continue_search_loop ; if cphandle < name continue searching
	CMP handle, cphandle
	BGT end_search_loop ; if handle > cphandle, break
	CMP handle, name
	BLT end_search_loop ; or if handle < name, break

continue_search_loop
	
	; current = current.next
	MOV cphandle, name

	B search_loop ; while(true)


end_search_loop


	; descriptor = handle.size
	LDR descriptor, [handle, #RAM_BlockHeaderSizeOffset]

	; 2.  Merge upper blocks
	; if handle + handle.size == current.next
	ADD index, handle, descriptor
	CMP index, name
		
		; tablehandle = current.next.size
		LDREQ tablehandle, [name, #RAM_BlockHeaderSizeOffset]

		; handle.size += current.next.size
		ADDEQ descriptor, descriptor, tablehandle

		; store handle.size
		STREQ descriptor, [handle, #RAM_BlockHeaderSizeOffset]

		; tablehandle = current.next.next
		LDREQ tablehandle, [name, #RAM_BlockHeaderNextOffset]

		; handle.next = current.next.next
		STREQ tablehandle, [handle, #RAM_BlockHeaderNextOffset]

	; else

		; not merged to upper block.  inserted into free list before current.next
		; handle.next = current.next
		STRNE name, [handle, #RAM_BlockHeaderNextOffset]

	; 

	; 3. Merge lower block
	; tablehandler = current.size
	LDR tablehandle, [cphandle, #RAM_BlockHeaderSizeOffset]

	; if current + current.size == handle
	ADD index, cphandle, tablehandle
	CMP index, handle

		; current.size += handle.size
		ADDEQ tablehandle, tablehandle, descriptor

		; store it
		STREQ tablehandle, [cphandle, #RAM_BlockHeaderSizeOffset]

		; tablehandle = handle.next
		LDREQ tablehandle, [handle, #RAM_BlockHeaderNextOffset]

		; current.next = handle.next
		STREQ tablehandle, [cphandle, #RAM_BlockHeaderNextOffset]

	; else

		; current.next = handle
		STRNE handle, [cphandle, #RAM_BlockHeaderNextOffset]

	
	; *** Store the freehead now.
	; freehead = current
	MOV freehead, cphandle
	FINISHED_freehead



	BX lr  ; return

	END


