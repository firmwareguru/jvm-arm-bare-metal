;****************************************************************
;  
;    tablelookup.s
;  
;    Date: Mar 3, 2008
;    Author: Evan Ross
;  
;    TableLookup begins at the object pointed to by CLASSHANDLE register.  
;    A TableLookup will look up either a field or a method depending on 
;    which table TABLEHANDLE points to (FieldTable or MethodTable).
;
;    Register map:
;    Input: CLASSHANDLE - pointer to InternalClass to begin search 
;           NAME  - name of method or field to match
;           DESCRIPTOR  -  descriptor of method or field to match
;
;    Intermediate: CPHANDLE - pointer to constant pool of classhandle
;                  VALUE, VALUE1, VALUE2
;
;    Output: TABLEHANDLE - points to matching Field or Method TableEntry
;
;****************************************************************

	AREA tablelookuparea, CODE, READONLY

	INCLUDE vmregisters.s
	INCLUDE vmexceptions.s
	INCLUDE internalclass.s
	INCLUDE lookupsetup.s   ; for PCW flags

	EXPORT table_lookup

table_lookup

		; If CLASSHANDLE is 0 then throw MethodOrFieldNotFoundException
        CMP classhandle, #0   ; if( JVMRuntime.classhandle == 0 )
		ThrowException MethodOrFieldNotFoundException  ; throwException(METHOD_OR_FIELD_NOT_FOUND)

        ; Get ConstantPool from the classhandle's header
		LDR cphandle, [classhandle, #HEADER_CPTableOffset]

        ; Initialize the tablehandle pointer to point to the start of the table based on PCW flags
		TST PCW, #PCW_FLAG_TABLE_SET  ; test bit 1
		LDRNE tablehandle, [classhandle, #HEADER_MethodTableOffset] ; if set, load method table
		LDREQ tablehandle, [classhandle, #HEADER_FieldTableOffset]  ; it not set, load field table


search_table_loop

        	; Loop through all method table entries while the name & descriptor word is not 0 (end of table)
	    	LDR value1, [tablehandle, #TABLEENTRY_NameDescriptorIndexOffset] ; load first entry

			CBZ value1, recurse_loop ; found the end of the list, break
			
			; !! Little-endian: the name and descriptor operations are swapped.
			; this is opposite from the VM testbed.

			; Get descriptor index
			LSR value2, value1, #14 ; value2 = (value1 >> 16) * 4 (>> 16 - 2 = 14) 
			BFC value2, #16, #16    ; Bit Field Clear -> value2 & 0xffff

			; Get descriptor
			LDR value2, [cphandle, value2]
							
			; Get name index
			BFC value1, #16, #16     ; Bit Field Clear -> value1 & 0xffff

			; Get name
			LDR value1, [cphandle, value1, LSL#2]

			; quit if value1 == descriptor AND value2 == name
			CMP value2, descriptor
			BNE search_loop_continue
			CMP value1, name
			BNE search_loop_continue
			
			BX lr  ; field or method table entry found.  return

search_loop_continue

			; go to next table entry
			ADD tablehandle, tablehandle, #TABLEENTRY_Size ; tablehandle += tableentry_size			
			
			B search_table_loop ; this one didn't match, get another entry

recurse_loop
	
	    
		; recurse to super class if PCW virtual flag is set
		TST PCW, #PCW_FLAG_LOOKUP_SET  ; test bit 0, set = virtual (recursive)
		LDRNE classhandle, [classhandle, #HEADER_SuperClassOffset] ; if set load super class
		BNE table_lookup ; start the lookup procedure again

		; if virtual flag not set (0), then throw an exception
		ThrowException MethodOrFieldNotFoundException  ; throwException(METHOD_OR_FIELD_NOT_FOUND)

		; never get here
		BX lr ; return		


	END

