;****************************************************************
;  
;    referencelookup.h
;  
;    Date: Feb 22, 2008
;    Author: Evan Ross
;
;    The first phase in a dynamic method dispatch.
;  
;    The ReferenceLookupProcess is responsible for obtaining the classname, 
;    method or field name and method or field descriptor from a ConstantPoolTable
;    item that is a InterfaceMethodRefInfo, MethodRefInfo or FieldRefInfo structure.
;
;    This process is intended to be used as part of a composite process that handles
;    invokevirtual #2 and similar instructions that directly index the constant pool table.
;
;    The input 'index' typically comes from an instruction operand. 
;    'currentclass' is the class of the current frame.
;
;    Register set:
;    Input:  index - index into CP table, pointing to one of the afore mentioned structures.
;            currentclass - the handle (address) of the class of the current frame
;    Output: classhandle - pointer to the class in which this reference may be found
;            name        - the method or field name of the reference
;            descriptor  - the method of field descriptor of the reference
;
;****************************************************************

	AREA referencelookuparea, CODE, READONLY

	INCLUDE vmregisters.s
	INCLUDE internalclass.s
	INCLUDE ramconstants.s  

	EXPORT reference_lookup
reference_lookup

    ; Load currentframe into name
    ; Load currentclass into descriptor
	LDR name, [currentthread, #THREAD_CurrentFrameOffset]
	LDR descriptor, [name, #FRAME_CurrentClassOffset]

    ; Get the constant pool table address

	LDR cphandle, [descriptor, #HEADER_CPTableOffset]

	; Get the particular RefInfo structure indicated by the input register 'index'
	; 'index' must be multiplied by 4 to turn it from "slot" to byte address.
	LDR value, [cphandle, index, LSL#2]

	MOV value2, value

	; Get the NameAndType index, little endian mode: NameAndType is in lower 16 bits.
	BFC value2, #16, #16 ; clear the upper 16 bits, note this value is a WORD offset.

	; Get the ClassInfo index, little endian mode: is in upper 16 bits.
	; Just clear lower 16 bits, then shift it right 16 then shift left 2 in LDR
	LSR value1, value, #16

	; Get the Name index from the ClassInfo
	LDR value, [cphandle, value1, LSL#2]

    ; Get the pre-resolved class handle from the item pointed to by the ClassInfo index
	LDR classhandle, [cphandle, value, LSL#2]

	; Get the NameAndType structure that was pointed to by NameAndTypeInfo index
	LDR value, [cphandle, value2, LSL#2]

	MOV value1, value

	; little endian again.  sigh.

	; Get the descriptopr index out of NameAndType
	BFC value1, #16, #16
	
	; Get the name index out of NameAndType
	LSR value2, value, #16

	; And finally...

    ;///////////////////////////////////////////////////////////////////////
    ;// Get the field or method name
    ;///////////////////////////////////////////////////////////////////////
	LDR name, [cphandle, value2, LSL#2]
        

    ;///////////////////////////////////////////////////////////////////////
    ;// Get the field or method descriptor
    ;///////////////////////////////////////////////////////////////////////
	LDR descriptor, [cphandle, value1, LSL#2]


	BX lr ; return

	END
