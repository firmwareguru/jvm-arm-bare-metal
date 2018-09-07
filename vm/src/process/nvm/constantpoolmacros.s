;****************************************************************
; 
;    Date: Apr 27, 2008
;    Author: Evan Ross
;
;    Macros for Constant Pool related operations.
;      
;
;****************************************************************

;****************************************************************
;
;  Load Constant Pool
;
;  Implements the LDC instruction; Obtains an int constant, a float
;  constant or a String reference from the constant pool of the 
;  current class.
;
;  Input:
;    CurrentClass - class from which to obtain the constant pool item.
;    Index - index into constant pool.
;
;  Output:
;    Value - item obtained from constant pool.
;
;****************************************************************

	MACRO
	LoadConstantPool

	LDR name, [currentthread, #THREAD_CurrentFrameOffset]
	LDR descriptor, [name, #FRAME_CurrentClassOffset]

	; Get the constant pool table address
	LDR cphandle, [descriptor, #HEADER_CPTableOffset]

    ; Get the particular RefInfo structure indicated by the input register 'index'
	LDR value, [cphandle, index, LSL#2] ; multiply index by 4 to get byte offsets.
  


	MEND





	END