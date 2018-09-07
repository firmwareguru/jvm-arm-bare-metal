;****************************************************************
;  
;    frameinfolookup.s
;  
;    Date: Mar 23, 2008
;    Author: Evan Ross
;  
;    The FrameInfoLookupProcess process is responsible for obtaining the 
;    frame information from the InternalClass'
;    CodeTableEntry:
;       max stack, max locals => obtains frame size
;    and from 
;    MethodTableEntry:
;       arg count => sets number of args to copy in FrameSetup process.
; 
;    Register set:
;    Input from TableLookup (Method):  
;       TABLEHANDLE
;    Output:
;       VALUE - total size of frame
;       VALUE1 - max locals
;       VALUE2 - arg count
; 
;
;****************************************************************

	AREA frameinfolookuparea, CODE, READONLY

	INCLUDE vmregisters.s
	INCLUDE internalclass.s
	INCLUDE ramconstants.s  

	EXPORT frameinfo_lookup

frameinfo_lookup

	    ; 1. Get code pointer from MethodTableEntry using direct half-word access
		LDRH handle, [tablehandle, #METHODTABLEENTRY_CodePtrHalfOffset]
        
        ; Note: 'handle' is the code pointer, offset from the tablehandle register.

        ; 3. handle plus tablehandle points to the max stack and max locals.
        ;    max_stack is in the upper 2 bytes, locals lower 2
		LDR value2, [tablehandle, handle] ; tablehandle + handle = code pointer

		; value2 = | max locals | max stack |

		MOV value1, value2, LSR #14	; extract max_locals into value1, shifting right, upper bits will be 0.
		                            ; multiply by 4 (shift left 2) to get in terms of bytes.

		MOV value, value2, LSL #2       ; extract max_stack into value, multiply by 4 to get in terms of bytes
		BFC value, #16, #16	    ; value &= 0xffff
        
        ; 4. Add them to get the total size and place in VALUE
        ADD value, value, value1 ; max_stack + max_locals
		ADD value, #FRAME_LocalVarOffset ; add the other Frame fields to get total Frame size.

        
        ; Get arg count from MethodTableEntry using half-word access
        ; Place in value2
        LDRH value2, [tablehandle, #METHODTABLEENTRY_ArgCountHalfOffset]

        ; Outputs:
        ;   value  - total size   -- used by AllocateMemory process
        ;   value1 - max locals   -- used by FrameSetup process  <<< must be preserved across AllocateMemory
        ;   value2 - arg count    -- used by FrameSetup process  <<< must be preserved across AllocateMemory
        

		BX lr ; return

	END
