;****************************************************************
;  
;    objectsizelookup.s
;  
;    Date: Mar 29, 2008
;    Author: Evan Ross
;  
;    Input: CLASSHANDLE - reference to the class template for the new object
;    Output: VALUE - the total amount of memory to allocate to the new object
;
;
;****************************************************************

	AREA objectsizelookuparea, CODE, READONLY

	INCLUDE vmregisters.s
	INCLUDE internalclass.s
	INCLUDE ramconstants.s  

	EXPORT objectsize_lookup

objectsize_lookup

    ; Get the object size out of the header and place into VALUE	
	LDR value, [classhandle, #HEADER_ObjectSizeOffset]


	; Upper 2 bytes is the object size (little endian!)
	; object size is specified in units of 4-byte words.
	LSR value, value, #14  ; value = (value >> 16) * 4 = value >> 14

	; Add to this the size of the ObjectBase (monitors, classreference and the like)
	ADD value, value, #OBJECT_Size

	; thats it.
	BX lr

	END


