;****************************************************************
; 
;    Date: Apr 27, 2008
;    Author: Evan Ross
;
;    Macros for Array related operations.
;      
;
;****************************************************************

;****************************************************************
;
; ArraySetup
;
; Setup an Array which is just an Object with an added hidden field, arraylength.  
;
; What about the class type of primitive array types?
;
; Register Set:
;   Input:
;     VALUE1 - size of array, count of array fields
;     HANDLE - reference to newly allocated array object
;   Output:
;     n/a
;****************************************************************

	MACRO
	ArraySetup

	; Copy this value into the arraylength field
	STR value1, [handle, #ARRAY_ArrayLengthOffset]


	MEND

;****************************************************************
;
; ArrayAccessSetup
;
; ArrayAccesssSetup pops the index and the arrayref from the opstack in preparation
; for an array access process - either a get or a put
;
; Register Set:
;   Input: STACKPOINTER
;   Output: INDEX - the index into the array
;           HANDLE - the arrayref
;
;****************************************************************

	MACRO
	ArrayAccessSetup

    LDMDB stackpointer!, {index, handle}

	MEND

;****************************************************************
;
; ArrayPutSingle
; ArrayPutByte
; ArrayPutShort
;
; ArrayPutSingle places the contents of VALUE into the indexed field in an Array of Single words.
; Supports instructions aastore, iastore, fastore
;
; ArrayPutByte places the contents of VALUE into the indexed field in an Array of bytes.
; Supports instructions bastore, castore
;
; ArrayPutShort places the contents of VALUE into the indexed field in an Array of shorts.
; Supports instructions sastore
;
; Requires StackPopSingle and ArrayAccessSetup process be run prior in that order.
;
;
; Register set:
;   Input: VALUE - the value to be placed into the array (from StackPopSingle)
;          INDEX - popped from stack, index into array   (from ArrayAccessSetup)
;          HANDLE - popped from stack, the array object reference
;
;****************************************************************

	MACRO
	ArrayPutSingle

	; add the array element offset to handle
	ADD handle, handle, #ARRAY_ArraySize

	; index is in units of array elements (in this case words)
	; so multiply by 4 (lsl#2)
	STR value, [handle, index, LSL#2]

	MEND

	MACRO
	ArrayPutByte

	; add the array element offset to handle
	ADD handle, handle, #ARRAY_ArraySize

	; index is in units of array elements (in this case bytes)
	STRB value, [handle, index]

	MEND

	MACRO
	ArrayPutShort

	; add the array element offset to handle
	ADD handle, handle, #ARRAY_ArraySize

	; index is in units of array elements (in this case shorts)
	STRH value, [handle, index, LSL#1]

	MEND
	
;****************************************************************
;
; ArrayGetSingle
; ArrayGetByte
; ArrayGetChar
;
; ArrayGetSingle obtains the contents of the indexed field in an Array of Single words.
; Supports instructions aaload, iaload, faload
;
; ArrayGetByte obtains the contents of the indexed field in an Array of bytes.
; The byte is sign extended.  Supports instructions baload.
;
; ArrayGetChar obtains the contents of the indexed field in an Array of bytes.
; The byte is zero extended (char is unsigned).  Supports instructions baload.
;
; Requires ArrayAccessSetup process be run prior.
;
;
; Register set:
;   Input: INDEX - popped from stack, index into array
;          HANDLE - popped from stack, the array object reference (arrayref)
;   Output: VALUE - the result of the array get operation.
;
;****************************************************************

	MACRO
	ArrayGetSingle

	; add the array element offset to handle
	ADD handle, handle, #ARRAY_ArraySize

	; index is in units of array elements (in this case words)
	; so multiply by 4 (lsl#2)
	LDR value, [handle, index, LSL#2]

	MEND

	MACRO
	ArrayGetByte

	; add the array element offset to handle
	ADD handle, handle, #ARRAY_ArraySize

	; index is in units of array elements (in this case bytes)
	; load byte and sign extend
	LDRSB value, [handle, index]

	MEND

	MACRO
	ArrayGetChar

	; add the array element offset to handle
	ADD handle, handle, #ARRAY_ArraySize

	; index is in units of array elements (in this case bytes)
	; load byte and zero extend
	LDRB value, [handle, index]

	MEND

	MACRO
	ArrayGetShort

	; add the array element offset to handle
	ADD handle, handle, #ARRAY_ArraySize

	; index is in units of array elements (in this case shorts)
	; load short and sign extend
	LDRSH value, [handle, index, LSL#1]

	MEND





	END
