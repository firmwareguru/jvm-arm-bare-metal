;****************************************************************
;  
;    setpc.s
;  
;    Date: Apr 19, 2008
;    Author: Evan Ross
;
; 	 Get the address in NVM that is the start of the code segment for the method.
; 	 The address is transfered to the PC register.
;
; 	 Note that CLASSHANDLE register is initially set all the way back in ReferenceLookup;
; 	 it is subsequently modified by TableLookup if the lookup recurses, and it
; 	 is again modified to point to an instance's class during ObjectClassLookup.
; 
;
; 	 Register set:
;       Input  : TABLEHANDLE - contains pointer to MethodTableEntry
;   	Output : PC
; 
;
;****************************************************************

	AREA setpcarea, CODE, READONLY

	INCLUDE vmregisters.s
	INCLUDE internalclass.s

	EXPORT set_pc

set_pc
    
	;
	; Get code pointer offset from MethodTableEntry
	;
	LDRH value, [tablehandle, #METHODTABLEENTRY_CodePtrHalfOffset]
	
	;
    ; Get code table entry
	;
	ADD handle, tablehandle, value  ; code ptr is tablehandle + code ptr offset

	;
 	; Get the start of the code array.  This is the new PC.
	;
	ADD vmpc, handle, #CODETABLEENTRY_CodeOffset


	BX lr  ; return

	END
