;****************************************************************
;  
;    classinfolookup.s
;  
;    Date: Apr 26, 2008
;    Author: Evan Ross
;
;    The ClassInfoLookup process is quite simple - it returns the class address reference by a ClassInfo 
;    object in the constant pool.
;
;    This process is used by instructions: New
;
;    Currently, this is as 2 step process : ClassInfo -> Absolute NVM index of class.
;
;    Step 1:  use INDEX to get the INDEX in the ClassInfo item.
;    Step 2:  use INDEX to get the CLASSHANDLE
;
;    Register set:
;    Input: INDEX - from opcode operand
;    Input: CURRENTCLASS - the currentclass containing the constant pool
;    Intermediate: CPHANDLE
;    Output: CLASSHANDLE
;
;
;****************************************************************

	AREA classinfolookuparea, CODE, READONLY

	INCLUDE vmregisters.s
	INCLUDE internalclass.s
	INCLUDE ramconstants.s  

	EXPORT classinfo_lookup

classinfo_lookup


    ; 1. Get the cphandle from the currentclass
     
    ;  Load currentframe into name
    ; Load currentclass into descriptor
	LDR name, [currentthread, #THREAD_CurrentFrameOffset]
	LDR descriptor, [name, #FRAME_CurrentClassOffset]
	LDR cphandle, [descriptor, #HEADER_CPTableOffset]

	; 2. Get the index from the ClassInfo
	; index is given in words; must convert to bytes (<< 2)
	LDR index, [cphandle, index, LSL#2]

	; 3. Get the classhandle from the index
	LDR classhandle, [cphandle, index, LSL#2]

	BX lr ; return


	END