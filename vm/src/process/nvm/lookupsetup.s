;****************************************************************
;
;  lookupsetup.s
;
;  Date: Mar 2, 2008
;  Author: Evan Ross
;
;  The macros in this file simply set the PCW register
;  flags for the type of lookup desired.
;
;  Lookups can be static or virtual.  A static lookup terminates
;  if the lookup fails in the current class.  A virtual lookup will
;  recurse to the super class and so on.
;
;  Lookups can be for methods or for fields.
;
;   PCW flag bits = 0x0000ff00
;
;   Bit 0 :  0 = static lookup
;            1 = virtual lookup
;
;   Bit 1 :  0 = field lookup
;            1 = method lookup
;
;****************************************************************


PCW_FLAG_TABLE_SET 		EQU 0x00000200   ; bit 1 of flag bits 0x0000xx00
PCW_FLAG_TABLE_CLEAR 	EQU 0xfffffdff   ; bit 1 of flag bits 0x0000xx00

PCW_FLAG_LOOKUP_SET 	EQU 0x00000100   ; bit 0 of flag bits 0x0000xx00
PCW_FLAG_LOOKUP_CLEAR 	EQU 0xfffffeff   ; bit 0 of flag bits 0x0000xx00



	MACRO
	SetupMethodLookup
	; set bit 1
	ORR PCW, #PCW_FLAG_TABLE_SET
	MEND

	MACRO
	SetupFieldLookup

	; clear bit 1
	AND PCW, #PCW_FLAG_TABLE_CLEAR
	MEND

	MACRO
	SetupVirtualLookup

	; set bit 0
	ORR PCW, #PCW_FLAG_LOOKUP_SET

	MEND

	MACRO
	SetupStaticLookup

	; clear bit 0
	AND PCW, #PCW_FLAG_LOOKUP_CLEAR
	MEND

	END