;****************************************************************
; 
;    Date: Apr 12, 2008
;    Author: Evan Ross
;
;    Macros for Field related operations including:
;      - Field put & get
;      
;
;****************************************************************

;****************************************************************
;
; FieldGetInfo
;
; Retrieves the size and index of a field referenced by TABLEHANDLE register.
; 
; Register Set:
;    Input:  TABLEHANDLE - pointer to a FieldTableEntry
;    Output: INDEX - field index value
;            CPHANDLE - field size
;
;****************************************************************

	MACRO
	FieldGetInfo

	; These are little endian so they use opposite registers than those in the VM testbed.

    ; Get the field index from TableEntryHandle which was found during TableLookupProcess
	LDR cphandle, [tablehandle, #FIELDTABLEENTRY_FieldSizeIndexOffset]

    ; Get the field size from TableEntryHandle which was found during TableLookupProcess
	LSR index, cphandle, #16  ; 

	BFC cphandle, #16, #16     ; Mask of the field index bits

	MEND




;****************************************************************
;
; ObjectGetField
; 
; Uses the "field index" from FieldGetInfo process to find where 
; in the object to get the field.  CPHANDLE determines whether to do a double get or a single get.
; 
; Register Set:
;   Input: HANDLE - objectref popped from stack (from StackPopObjecref)
;          INDEX  - index into object for the field
;          CPHANDLE - size of the field
;          TABLEENTRYHANDLE - size of this field, index into object for this field
;   Output: case CPHANDLE = 1:
;              VALUE - value of single field
;           case CPHANDLE = 2:
;              VALUE1 - value of first half of double field
;              VALUE2 - value of second half of double field
;
;****************************************************************

	MACRO
	ObjectGetField

    ; Get start address of field = handle + size of object base + field index 
  	ADD handle, handle, index, LSL#2
	ADD handle, handle, #OBJECT_Size

	CMP cphandle, #1  ; single
	LDREQ value, [handle]
	; double
	LDRNE value1, [handle], #4 ; post-increment by 4
	LDRNE value2, [handle]


	MEND


;****************************************************************
;
; ObjectPutField
; 
; Uses the count of words to find where in the object to place the field.  
; cphandle determines whether to do a double put or a single put.
; 
; Register Set:
;   Input:  INDEX - index into object of the field in units of words.
;			CPHANDLE - size of this field.  1= single, 2=double
;           case CPHANDLE=1:
;              VALUE - value of single field
;           case VALUE1=2:
;              VALUE1 - value of first half of double field
;              VALUE2 - value of second half of double field
;          HANDLE - reference to object
;
;****************************************************************

	MACRO
	ObjectPutField

    ; Get start address of field = handle + size of object base + field index 
	; Index is in units of words so shift left 2
	ADD handle, handle, index, LSL#2
	ADD handle, handle, #OBJECT_Size

	CMP cphandle, #1
	STREQ value, [handle]
	STRNE value1, [handle], #4
	STRNE value2, [handle]

	MEND




 END
