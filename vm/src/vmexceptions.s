;****************************************************************
;  
;    vmexceptions.s
;  
;    Date: Mar 3, 2008
;    Author: Evan Ross
;
;    Exception handlers for the VM.
;    Exceptions can be called from anywhere in the VM.
;    This file should be included in other asm files to access
;    the EQUs.  vmexceptionimpl.s contains the implementation.
;
;****************************************************************

ClassNotFoundException	EQU	0
MethodNotFoundException EQU 1
MemoryAllocationError	EQU 2
UnhandledOpcodeException EQU 3
NotImplementedException EQU 4
FieldNotFoundException 	EQU 5
MethodOrFieldNotFoundException EQU 6
InvalidFieldSizeException EQU 7
InvalidArrayTypeException EQU 8
EligibleQueueEmpty		  EQU 9
ThreadTerminatedException EQU 10


; Some lovely little macros
; These are conditional on the Z (equal to zero) flag being set prior.

	IMPORT exception_setup

	MACRO
	ThrowException $exception

	MOVEQ r1, #$exception  ; move the exception number into r1 only is zero flag is set
	BEQ	exception_setup ; branch to the exception handler only if zero flag is set

	MEND

	MACRO
	ThrowExceptionNE $exception

	MOVNE r1, #$exception  ; move the exception number into r1 only is zero flag is NOT set
	BNE	exception_setup ; branch to the exception handler only if zero flag is NOT set

	MEND
	

	MACRO
	ThrowExceptionUnconditional $exception

	MOV r1, #$exception  ; move the exception number into r1 only is zero flag is set
	B	exception_setup ; branch to the exception handler 

	MEND


	END