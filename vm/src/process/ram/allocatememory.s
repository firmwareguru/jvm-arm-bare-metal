;****************************************************************
;  
;    allocatememory.s
;  
;    Date: Mar 23, 2008
;    Author: Evan Ross
;
;    A handle to the allocated memory is returned.
;
;    Register Set:
;      Input: value - total size of memory region to allocate IN BYTES.
;                     Multiple of 4!
;      Output : handle - address of allocated memory region
;
;    Used Registers:
;      cphandle - 'current'
;      name = 'current.size'
;
;    Protected registers -- do not modify:
;      value1 - max locals used by FrameSetup process
;      value2 - arg count used by FrameSetup process
;      tablehandle - used by ThreadRunSetup
;      classhandle - used by ThreadRunSetup
;
;    The protected registers are those registers that are expected by o
;    ne or more processes to be preserved across calls to AllocateMemoryProcess.
; 
;
;****************************************************************

	AREA allocatememoryarea, CODE, READONLY

	INCLUDE vmregisters.s
	INCLUDE vmexceptions.s
	INCLUDE internalclass.s
	INCLUDE ramconstants.s  

	EXPORT allocate_memory

allocate_memory

		USES_freehead ; load the freehead register

        ; add header size to the requested size, must also be multiple of header size
		ADD value, value, #RAM_BlockHeaderSize

		; check if this value is indeed mod 4
		TST value, #0x3
		ThrowExceptionNE MemoryAllocationError
        
        ; previous = freehead
        MOV cphandle, freehead

        ; while size of block is less than requested size
search_for_block 
            
            ; current = previous.next
			LDR handle, [cphandle, #RAM_BlockHeaderNextOffset]
            
            ; current.size
			LDR name, [handle, #RAM_BlockHeaderSizeOffset]	  
						
            ; if( JVMRuntime.name == JVMRuntime.value || JVMRuntime.name >= JVMRuntime.value + RAM.BLOCK_HEADER_SIZE )  // current block has sufficient size
			CMP name, value ; test first condition
			BEQ found_block

			ADD descriptor, value, #RAM_BlockHeaderSize ; temp register descriptor = value + block header size
			CMP name, descriptor
			BLT block_not_found ; name < descriptor goto block not found  

found_block

                ; if( JVMRuntime.name == JVMRuntime.value ) // exact match (remember that header is included as well)
				CMP name, value

                	; unlink this block from the list
                	; previous.next = current.next
					LDREQ name, [handle, #RAM_BlockHeaderNextOffset]
                    
                    ; previous.next = current.next
					STREQ name, [cphandle, #RAM_BlockHeaderNextOffset]

	            ; else allocate tail end

                    ; Adjust current block's size by subtracting the requested size (including header) (value).
					SUBNE name, name, value ; name = name - value
                    
					STRNE name, [handle, #RAM_BlockHeaderSizeOffset]
                    
                    ; Move current pointer to start of newly allocated block (tail end)
					ADDNE handle, handle, name ; handle = handle + name
                    
                    ; Set size of newly allocated block
					STRNE value, [handle, #RAM_BlockHeaderSizeOffset]
                    

                ; Set freehead = previous (point where last block was found).
                ; The reason for this is indicated as keeping the list "homogeneous".  Not sure if that really benefits us.
				MOV freehead, cphandle
                
                ; this is where we end up if we have found a block

				FINISHED_freehead  ; done with freehead register, so store it

                ; return 'handle' in JVMRuntime.handle
				BX lr ; return
        
block_not_found

            ; previous = current
			MOV cphandle, handle
            
            ; current = current.next
			LDR handle, [handle, #RAM_BlockHeaderNextOffset]
            
            ; if current = freehead, we have arrived back at the beginning.  No block found.
            ; if( JVMRuntime.freehead == JVMRuntime.cphandle )
            ;     throwException(MEMORY_ALLOCATION_ERROR);
			CMP freehead, cphandle ; if current = freehead, throw an exception
			ThrowException MemoryAllocationError
            
        	B search_for_block ; while(true)

			        
		END