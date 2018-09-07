;****************************************************************
;  
; threadrunreference.s
;  
; Date: Apr 29, 2008
; Author: Evan Ross
;  
; Replaces the function of the ReferenceLookup process during the 
; Invokevirtual process flow embedded within the Thread start process.
;
; The outputs must be:
;    NAME
;    DESCRIPTOR
;    CLASSHANDLE
;
; Since the Run() method doesn't get a MethodRefInfo structure placed into Thread's constant
; pool by the compiler unless it is referenced somewhere in Thread (it is only called from the
; native method Start()), we can't look Run up using the ReferenceLookup process.
;
; Therefore we manually set the registers to the state they would be had ReferenceLookup been
; run.
;
;      name = "run".hashCode() = 0x1ba8b   
;      descriptor = "()V".hashCode() = 0x9b75
;  
; classhandle is set by looking up the class of objectref ('this' in local variable 0).
;
; Notes:
;
;   The hashcodes for these strings MUST match the hashcodes generated by ClassPackager. 
;   ClassPackager uses the Java hashCode() method to generate the hash numbers.
;
;****************************************************************


   	AREA threadrunreferencearea, CODE, READONLY

	INCLUDE vmregisters.s
	INCLUDE ramconstants.s  
	INCLUDE internalclass.s

	EXPORT threadrun_reference

threadrun_reference


	; load currentframe into name
	LDR name, [currentthread, #THREAD_CurrentFrameOffset]

    ; Get 'this' from local variable at index 0
    ; where the current frame is that of start()
	LDR value, [name, #(FRAME_LocalVarOffset + 0)]

	; Set the name register to the hascode of "run"
	MOV32 name, #0x1ba8b

	; Set the descriptor register to the hashcode of "()V"
	MOV32 descriptor, #0x9b75

 	;
	; Now get the class of the objectref
    ; Overwrite classhandle with actual object's class handle and let Field/MethodLookup do its magic.
    ;    
	LDR classhandle, [value, #OBJECT_ClassReferenceOffset]

	BX lr ; return




	END