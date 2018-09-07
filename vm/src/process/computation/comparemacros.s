;****************************************************************
; 
;    Date: May 5, 2008
;    Author: Evan Ross
;
;    Macros for Branch related operations including:
;      
;
;****************************************************************

;****************************************************************
;
; CompareSingleIntStackPop2
;
; Prepares for a branch compare by popping value1 and value2 
; off of the stack
;
;****************************************************************

	MACRO
	CompareSingleIntStackPop2

	;LDR value2, [stackpointer, #-4]!  ; value2 = [--stackpointer]
	;LDR value1, [stackpointer, #-4]!  ; value1 = [--stackpointer]

	LDMDB stackpointer!, {value2, value1}

	MEND

;****************************************************************
;
; CompareSingleIntStackPopNull
;
; Prepares for a branch compare by popping value1 off of the stack
; and placing NULL into value2
;
;****************************************************************

	MACRO
	CompareSingleIntStackPopNull

	LDR value1, [stackpointer, #-4]!  ; value1 = [--stackpointer]

	; Fix value2 at null
	MOV32 value2, #NULLREGISTER

	MEND

;****************************************************************
;
; CompareSingleIntStackPop0
;
; Prepares for a branch compare by popping value1 off of the stack
; and placing 0 into value2
;
;****************************************************************

	MACRO
	CompareSingleIntStackPop0

	LDR value1, [stackpointer, #-4]!  ; value1 = [--stackpointer]

	; Fix value2 at 0
	MOV value2, #0

	MEND


;****************************************************************
;
; CompareSingleIntGE
;
; Perform the greater than or equal to comparison and take 
; the branch if necessary.
;
;****************************************************************

	MACRO
	CompareAndBranchSingleIntGE

	CMP value1, value2
	SUBGE vmpc, vmpc, #3  ; move back to address of branch instruction
	ADDGE vmpc, vmpc, index ; add the branch offset

	MEND

;****************************************************************
;
; CompareSingleIntGT
;
; Perform the greater than comparison and take 
; the branch if necessary.
;
;****************************************************************

	MACRO
	CompareAndBranchSingleIntGT

	CMP value1, value2
	SUBGT vmpc, vmpc, #3  ; move back to address of branch instruction
	ADDGT vmpc, vmpc, index ; add the branch offset

	MEND

;****************************************************************
;
; CompareSingleIntLT
;
; Perform the less than comparison and take 
; the branch if necessary.
;
;****************************************************************

	MACRO
	CompareAndBranchSingleIntLT

	CMP value1, value2
	SUBLT vmpc, vmpc, #3  ; move back to address of branch instruction
	ADDLT vmpc, vmpc, index ; add the branch offset

	MEND

;****************************************************************
;
; CompareSingleIntLE
;
; Perform the less than or equal to comparison and take 
; the branch if necessary.
;
;****************************************************************

	MACRO
	CompareAndBranchSingleIntLE

	CMP value1, value2
	SUBLE vmpc, vmpc, #3  ; move back to address of branch instruction
	ADDLE vmpc, vmpc, index ; add the branch offset

	MEND

;****************************************************************
;
; CompareSingleIntNE
;
; Perform the not equal comparison and take the branch if necessary.
;
;****************************************************************

	MACRO
	CompareAndBranchSingleIntNE

	CMP value1, value2
	SUBNE vmpc, vmpc, #3  ; move back to address of branch instruction
	ADDNE vmpc, vmpc, index ; add the branch offset

	MEND

;****************************************************************
;
; CompareSingleIntEQ
;
; Perform the equal comparison and take the branch if necessary.
;
;****************************************************************

	MACRO
	CompareAndBranchSingleIntEQ

	CMP value1, value2
	SUBEQ vmpc, vmpc, #3  ; move back to address of branch instruction
	ADDEQ vmpc, vmpc, index ; add the branch offset

	MEND


	END


