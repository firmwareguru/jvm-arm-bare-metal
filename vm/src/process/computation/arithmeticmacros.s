;****************************************************************
; 
;    Date: Apr 27, 2008
;    Author: Evan Ross
;
;    Macros for Arithmetic related operations including:
;      - Add, sub, mul, div
;      
;
;****************************************************************


;****************************************************************
;
;  PrepareALUSingle
;
;  Pop stack into VALUE2 and pop stack again into VALUE1
;
;      value2 = frame.popInt();
;      value1 = frame.popInt(); 
;
;****************************************************************

	MACRO 
	PrepareALUSingle

	;LDR value2, [stackpointer, #-4]!  ; value2 = [--stackpointer]
	;LDR value1, [stackpointer, #-4]!  ; value1 = [--stackpointer]

	LDMDB stackpointer!, {value2, value1}

	MEND

;****************************************************************
;
;   AddSingleInt
;
;   Add value is register VALUE1 with value in register VALUE2 and put result in VALUE 
;
;****************************************************************

	MACRO
	AddSingleInt

	ADD value, value1, value2

	MEND

;****************************************************************
;
;   SubSingleInt
;
;   Sub value is register VALUE1 with value in register VALUE2 and put result in VALUE 
;
;****************************************************************

	MACRO
	SubSingleInt

	SUB value, value1, value2

	MEND

;****************************************************************
;
;   MultSingleInt
;
;   Multiply value is register VALUE1 with value in register VALUE2 and put result in VALUE 
;
;****************************************************************

	MACRO
	MultSingleInt

	MUL value, value1, value2

	MEND

;****************************************************************
;
;   DivSingleInt
;
;   Divide value in register VALUE1 with value in register VALUE2 and put result in VALUE 
;
;****************************************************************

	MACRO
	DivSingleInt

	SDIV value, value1, value2 ; ARMv7-M instruction only

	MEND

;****************************************************************
;
;   RemSingleInt
;
;   Mod value in register VALUE1 with value in register VALUE2 and put result in VALUE 
;   Algorithm: value = value1 - (value1/value2) * value2
;
;****************************************************************

	MACRO
	RemSingleInt

	SDIV value, value1, value2 ; ARMv7-M instruction only
	MUL value2, value, value2 
	SUB value, value1, value2  

	MEND


;****************************************************************
;
;   NegSingleInt
;
;   Negeate value in register VALUE
;   Algorithm: subtract from 0
;
;****************************************************************

	MACRO
	NegSingleInt

	RSB value, value, #0

	MEND

;****************************************************************
;
;   ShiftLeftInt
;
;   Shift left value in register VALUE1 by value in register VALUE2 and 
;   put result in VALUE 
;
;****************************************************************

	MACRO
	ShiftLeftInt

	LSL value, value1, value2

	MEND

;****************************************************************
;
;   ShiftRightInt
;
;   Arithmetic Shift right value in register VALUE1 by value in register VALUE2 
;   and put result in VALUE 
;
;****************************************************************

	MACRO
	ShiftRightInt

	ASR value, value1, value2

	MEND

;****************************************************************
;
;   ShiftRightLogicalInt
;
;   Logical shift right value in register VALUE1 by value in register VALUE2 
;   and put result in VALUE 
;
;****************************************************************

	MACRO
	ShiftRightLogicalInt

	LSR value, value1, value2

	MEND


	END
