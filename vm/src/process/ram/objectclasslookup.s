;****************************************************************
;  
;    objectclasslookup.s
;  
;    Date: Apr 19, 2008
;    Author: Evan Ross
;
;    The ObjectClassLookupProcess process fetches the ClassReference from the object pointed to 
;    by a handle.  This process eventually sets the currentclass to the class of the object
;    rather than the class of the type (for virtual lookups).
;
;    To get access to objectref, which is on the stack, the number of arguments that
;    are piled on top of it must be known.  This value is obtained from the MethodTableEntry
;    that has been identified in a preceeding TableLookup process, i.e., TABLEHANDLE register
;    points to this entry.  The arg count field is located in word 1, low 2 bytes.
;
;    Register set:
;    Input: VALUE - points to an object (objectref popped from the stack in previous process)
;           TABLEHANDLE - points to methodtableentry
;    Output: CLASSHANDLE - points to the InternalClass that is the type of the object
;
;    Intermediate: VALUE1 - store stackpointer pointing to objectref
;
;****************************************************************

	AREA objectclasslookuparea, CODE, READONLY

	INCLUDE vmregisters.s
	INCLUDE internalclass.s
	INCLUDE ramconstants.s

	EXPORT objectclass_lookup

objectclass_lookup


	;
    ; Get count of arguments into VALUE (from NVM)
    ;
	LDRH value, [tablehandle, #METHODTABLEENTRY_ArgCountHalfOffset]
        
    ;
    ; Decrement the stackpointer by the count of arguments
	; ('value' in in units of words not bytes so left shift 2)
    ;
	SUB value1, stackpointer, value, LSL#2  
    
        
    ;
    ; Now get objectref into VALUE.
    ; (stackpointer points to top of stack plus 1 * 4 so decrement by that amount)
    ;
	LDR value, [value1, #-4]

    ;
    ; Now get the class of the objectref
    ; Overwrite classhandle with actual object's class handle and let Field/MethodLookup do its magic.
    ;
	LDR classhandle, [value, #OBJECT_ClassReferenceOffset]
	
	BX lr  ; return


	END
