;****************************************************************
;  
;    vmexceptionsetup.s
;  
;    Date: Apr 14, 2008
;    Author: Evan Ross
;
;    Sets up the registers and stack prior to calling
;    the C-implementation of the exception handler.
;
;****************************************************************


	AREA vmexceptionsetup, CODE, READONLY
		
	INCLUDE vmregisters.s ; register definitions and defines
	
	EXPORT exception_setup

exception_setup

	IMPORT exception_handler

	
	; Move back to main stack mode
	SET_StackModeMain



	; store some registers onto the stack
	PUSH {classhandle, currentthread, PCW, vmpc}

 	; store reference to the stack in r2
	MOV r2, sp

	; r0 is an optional code
	; r1 is the exception cause
	; r2 is a pointer to a regStruct on the stack.
	B exception_handler


	END
