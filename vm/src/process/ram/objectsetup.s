;****************************************************************
;  
;    objectsetup.s
;  
;    Date: Mar 29, 2008
;    Author: Evan Ross
;  
;    Initialize the new object.
;
;    Register Set:
;    Input: CLASSHANDLE - Class of the object
;    Output: VALUE - objectref
;
;****************************************************************

   	AREA objectsetuparea, CODE, READONLY

	INCLUDE vmregisters.s
	INCLUDE ramconstants.s  

	EXPORT object_setup

object_setup

    ; Initialize the class type of this object ... hmm, what would be the class of array objects?
	STR classhandle, [handle, #OBJECT_ClassReferenceOffset]

	; Initialize the object's monitor 
    MOV value, #NULLREGISTER
	STR value, [handle, #OBJECT_MonitorOffset]

    ; Move the handle (objectref) into VALUE to put pushed onto the stack
	MOV value, handle
        
	BX lr
	
	END		