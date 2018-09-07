;****************************************************************
;  
;    arraysizelookup.s
;  
;    Date: Apr 27, 2008
;    Author: Evan Ross
;
; Determine the total amount of memory to allocate to this Array object type.  This is different than
; the ObjectSizeLookupProcess because the size of this Array object is dependent on the count of elements
; and not on the count of fields determined prior in the ClassPackager.
;
; There are two instructions that create arrays:
;   newarray, anewarray.
;
; Either of these instructions calls the same array creation process.  The 'count' of array elements
; is on the operand stack and made available to this process in the register VALUE from a prior StackPopSingle.
; 
; However, the size of memory required for this array object is dependent on the type of elements in this array.
; There are 4 categories of elements:
;    8 bits: Byte T_BOOL, T_CHAR, T_BYTE
;   16 bits: T_SHORT
;   32 bits: T_INT, T_FLOAT, Reference (from anewarray)
;   64 bits: T_LONG, T_DOUBLE
;
; The 32 bit types are the standard 'Single' words.  The 64 bit types are the standard 'Double' words.  The additional
; types are created here to reduce wasted memory.  This is possible because there are separate instructions
; for accessing arrays of byte, short, int etc.
;
; The count of array elements, 'count', is modified to indicate the count of fundamental memory units (bytes)
; in terms of the element type.  For example, the count for int types is multiplied by 4 to get the number
; of bytes to hold those bytes.  We use shifts to do the division or multiplication.
;
; Register Set:
;   Input: VALUE - 'count' of array elements
;          INDEX - type of array elements.  for newarray, index is 'atype' operand.  for anewarray, index is
;                  manually set to T_INT (10) for Single element type.
;     
;   Output: VALUE - total memory size to allocate. 
;           VALUE1 - 'count' of array elements.
;
;
;****************************************************************

	AREA arraysizelookuparea, CODE, READONLY

	INCLUDE vmregisters.s
	INCLUDE internalclass.s
	INCLUDE ramconstants.s  
	INCLUDE vmexceptions.s

	EXPORT arraysize_lookup

arraysize_lookup

	; perform range checks.
	CMP index, #11
	BGT invalid_array_type

	; copy VALUE (currently the size of array in elements) to VALUE1
	; for use by ArraySetupProcess
    MOV value1, value

	ADR value2, arraytype_switch_table ; get address of jump table into a register
	LDR pc, [value2, index, LSL#2]     ; jump to jumptable[index].

	ALIGN

arraytype_switch_table

	DCD invalid_array_type 	; 0
	DCD invalid_array_type 	; 1
	DCD invalid_array_type 	; 2
	DCD invalid_array_type 	; 3
	DCD finish_up     		; 4  T_BOOLEAN
	DCD finish_up     		; 5  T_CHAR
	DCD arraytype_single	; 6  T_FLOAT 
	DCD arraytype_double   	; 7  T_DOUBLE
	DCD finish_up     		; 8  T_BYTE
	DCD arraytype_16bit    	; 9  T_SHORT
	DCD arraytype_single	; 10 T_INT  
	DCD arraytype_double   	; 11 T_LONG

invalid_array_type

	ThrowExceptionUnconditional InvalidArrayTypeException 


arraytype_16bit

	; Multiply by 2 to get number of bytes required
	LSL value, value, #1

	B finish_up

arraytype_double

	; Multiply by 8 to get number of bytes required
	LSL value, value, #3

	B finish_up

arraytype_single

	; Multiply by 4 to get number of bytes required
	LSL value, value, #2


finish_up

	; Check if number of elements is not multiple of 4 bytes
	TST value, #0x3
	ADDNE value, value, #4 ; add 4 to bring it to the next word

	; deviation from VM testbed: this memory system is in units of bytes not words.
	;LSR value, value, #2
	; Clear the 2 LSBs
	BFCNE value, #0, #2

    ; now add Array.size to VALUE to get total memory footprint for AllocateMemoryProcess
	ADD value, value, #ARRAY_ArraySize
	
	BX lr ; return



	END
