;****************************************************************
; 
;    Date: May 5, 2008
;    Author: Evan Ross
;
;    'peripheral' handlers.
; 
;	 Custome bytecode handlers for bytecode 255.  
; 
;    The registers are NOT required to be preserved across
;    bytecodes, however some registers MAY be required to be
;    preserved across process calls within a handler.
;
;    IMPORTANT!  If bytecode operands are retrieved in the handlers,
;    the vmpc MUST be incremented past the operands to point to the
;    next vmpc.
;
;****************************************************************


	AREA peripheralhandlerarea, CODE, READONLY

	INCLUDE vmregisters.s
	INCLUDE ramconstants.s
	INCLUDE vmexceptions.s
	INCLUDE internalclass.s
	INCLUDE lookupsetup.s ; macros for setting process flags

	INCLUDE stackmacros.s
	INCLUDE fieldmacros.s
	INCLUDE arraymacros.s
	INCLUDE constantpoolmacros.s
	INCLUDE arithmeticmacros.s
								
	IMPORT main_execution_loop


;****************************************************************
;
;  display_drawstring
;
;  Calls the OLED display driver's DrawString() function.
;
;
;  Notes:
;      This bytecode must be called from a native method that
;      has the following local variable arrangement:
;              0 - 'this' (reference) 
;              1 - String (reference)
;              2 - int (x coord)
;              3 - int (y coord)
;
;      Must switch to the 'main' stack before calling any functions
;      and must switch back to 'process' (thread mode) stack before
;      branching back to the main_execution_loop.
;
;****************************************************************
	EXPORT handler_peripheral_display_drawstring
handler_peripheral_display_drawstring

    ; The OLEDs function prototype
    ; RIT128x96x4StringDraw("Hello?", 20, 10, 15) [&String, x, y, brightness]
	;IMPORT RIT128x96x4StringDraw
	IMPORT StringDrawWithLength

	; Switch in the main SP so as not to corrupt the current thread's SP.
	SET_StackModeMain
	
	; Load currentframe into name
	LDR name, [currentthread, #THREAD_CurrentFrameOffset]

	; Offset it to start of local variable 1 (String reference)
	ADD name, name, #(FRAME_LocalVarOffset + 4)

	; Now load the parameters from variables 1,2,3 into r0, r1, r2.
	;LDM name, {r0, r1, r2}
	LDM name, {r0, r2, r3}	; r0 points to String object

	; increment the pointer to the 'length' field (8 bytes in)
	ADD r0, r0, #8
	LDR r1, [r0], #4 ; load the length then increment r0 to point to the byte array

	;MOV r3, #15 ; set brightness maximum.

	; call the function.
	;BL RIT128x96x4StringDraw
	BL StringDrawWithLength

	SET_StackModeProcess

	B main_execution_loop  ; done


;****************************************************************
;
;  display_drawint
;
;  Calls the OLED display driver's DrawString() function after
;  converting the int to a string.
;
;
;  Notes:
;      This bytecode must be called from a native method that
;      has the following local variable arrangement:
;              0 - 'this' (reference) 
;              1 - String (reference)
;              2 - int (x coord)
;              3 - int (y coord)
;
;      Must switch to the 'main' stack before calling any functions
;      and must switch back to 'process' (thread mode) stack before
;      branching back to the main_execution_loop.
;
;****************************************************************
	EXPORT handler_peripheral_display_drawint
handler_peripheral_display_drawint

    ; The OLEDs function prototype
    ; RIT128x96x4StringDraw("Hello?", 20, 10, 15) [&String, x, y, brightness]
	IMPORT RIT128x96x4StringDraw

	; Switch in the main SP so as not to corrupt the current thread's SP.
	SET_StackModeMain
	
	; Load currentframe into name
	LDR name, [currentthread, #THREAD_CurrentFrameOffset]

	; Offset it to start of local variable 1 (String reference)
	ADD name, name, #(FRAME_LocalVarOffset + 4)

	; Now load the parameters from variables 1,2,3 into r0, r1, r2.
	LDM name, {r0, r1, r2}

	IMPORT draw_int
	BL draw_int

	SET_StackModeProcess

	B main_execution_loop  ; done


;****************************************************************
;
;  mem_write
;
;  Called from a STATIC method (no 'this)
;
;****************************************************************
	EXPORT handler_peripheral_mem_write
handler_peripheral_mem_write

	; Load currentframe into name
	LDR name, [currentthread, #THREAD_CurrentFrameOffset]

	; Offset it to start of local variable 0 (arg 0 of frame - address)
	ADD name, name, #(FRAME_LocalVarOffset + 0)
	
	; arg 1 of frame - value to write
	; Now load the parameters from variables 0,1 into r0, r1.
	LDM name, {r0, r1}

	str r1, [r0]

	B main_execution_loop  ; done

;****************************************************************
;
;  mem_read
;
;  Called from a STATIC method (no 'this)
;
;****************************************************************
;	EXPORT handler_peripheral_mem_read
;handler_peripheral_mem_read
;
;	; Load currentframe into name
;	LDR name, [currentthread, #THREAD_CurrentFrameOffset]
;
;	; Offset it to start of local variable 0 (arg 0 of frame - address)
;	ADD name, name, #(FRAME_LocalVarOffset + 0)
;
;    ; arg 0 of frame - address
;	LDR r1, [name] 
;
;	LDR value, [r1]
;
;	StackPushSingle  ; push the return value onto the stack
;
;	B main_execution_loop  ; done

	END

