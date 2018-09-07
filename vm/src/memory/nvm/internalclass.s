;****************************************************************
;  
;    internalclass.s
;  
;    Date: Mar 3, 2008
;    Author: Evan Ross
;
;	 This file specifies the relative offsets of fields in the
;    InternalClass NVM structure.
;    
;****************************************************************


HEADER_NextClassOffset EQU 0 * 4
HEADER_ThisClassOffset EQU 1 * 4
HEADER_SuperClassOffset EQU 2 * 4
HEADER_AccessFlagsOffset EQU 3 * 4
HEADER_ObjectSizeOffset EQU 3 * 4
HEADER_CPTableOffset EQU 4 * 4
HEADER_InterfaceTableOffset EQU 5 * 4
HEADER_MethodTableOffset EQU 6 * 4
HEADER_FieldTableOffset EQU 7 * 4
    
; Generic TableEntry offsets.
TABLEENTRY_NameDescriptorIndexOffset EQU 0 * 4
TABLEENTRY_Size EQU 3 * 4 ; 3 words, used to increment pointer
    
METHODTABLEENTRY_NameDescriptorIndexOffset EQU 0 * 4
METHODTABLEENTRY_FlagsOffset EQU 1 * 4
METHODTABLEENTRY_ArgCountOffset EQU 1 * 4
METHODTABLEENTRY_FlagsHalfOffset EQU (1 * 4) + 0   ; flags comes first
METHODTABLEENTRY_ArgCountHalfOffset EQU (1 * 4) + 2 ; arg count comes second
METHODTABLEENTRY_CodePtrOffset EQU 2 * 4
METHODTABLEENTRY_ExceptionPtrOffset EQU 2 * 4
METHODTABLEENTRY_CodePtrHalfOffset EQU (2 * 4) + 2	   ; accesses code ptr using direct half-word access
METHODTABLEENTRY_ExceptionPtrHalfOffset EQU (2 * 4) + 0 ; access using direct half-word access

METHODTABLEENTRY_Size EQU 3 * 4 ; 3 words, used to increment pointer
    
FIELDTABLEENTRY_NameDescriptorIndexOffset EQU 0 * 4 ; word 0 : name index, descriptor index
FIELDTABLEENTRY_FlagsConstantOffset EQU 1 * 4       ; word 1 : flags, constant index
FIELDTABLEENTRY_FieldSizeIndexOffset EQU 2 * 4      ; word 2 : field size, field index
FIELDTABLEENTRY_Size EQU 3 * 4   ; 3 32-bit words
    
CODETABLEENTRY_ArgCountOffset EQU 1 * 4 ; no longer used!
CODETABLEENTRY_CodeOffset EQU 1 * 4     ;

	END