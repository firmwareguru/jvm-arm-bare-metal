;****************************************************************
;  
;    vmexceptionsimpl.s
;  
;    Date: Mar 3, 2008
;    Author: Evan Ross
;
;    Exception handlers for the VM.
;    Exceptions can be called from anywhere in the VM.
;    This file contains the implementation.
;
;****************************************************************


	AREA vmexceptions, CODE, READONLY


	EXPORT exception_handler

exception_handler
	
	 ; RIT128x96x4StringDraw("Hello?", 20, 10, 15);
	IMPORT RIT128x96x4StringDraw
	
	; LDR r0, =mystring
	LDR r1, =20
	LDR r2, =30
	LDR r3, =15
	BL RIT128x96x4StringDraw
	; examine r0.
	; dump regisers, etc.

	B exception_handler ; loop forever


	END