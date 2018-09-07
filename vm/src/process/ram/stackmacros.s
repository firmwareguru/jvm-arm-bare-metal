;****************************************************************
; 
;    Date: Apr 12, 2008
;    Author: Evan Ross
;
;    Macros for Frame related operations including:
;      - Operand Stack related operations - load, store, etc.
;      - Local variables
;      
;
;****************************************************************



;****************************************************************
;
; LocalGetSingle
;
; Get a single word from the local variable array at the specified index.
; The index must be specified in units of words.  It is multiplied
; by 4 to turn into a byte offset.
;
; Register Set:
;   Input: INDEX
;   Output: VALUE
;           NAME - currentthread
;
;****************************************************************
	MACRO
	LocalGetSingle

	; Load currentframe into name
	LDR name, [currentthread, #THREAD_CurrentFrameOffset]
	ADD name, name, index, LSL#2 ; offset by value in index * 4
	LDR value, [name, #FRAME_LocalVarOffset] ; offset by local variable offset.

	MEND

;****************************************************************
;
; LocalGetSingleAlt
;
; This process is the alternate version of LocalGetSingle: it places the local
; variable into the VALUE2 register instead of VALUE.  This is used for implementing
; the iinc instruction where VALUE1 contains count.
;
; Register Set:
;   Input: INDEX
;   Output: VALUE2
;           NAME - currentthread
;
;****************************************************************
	MACRO
	LocalGetSingleAlt

	; Load currentframe into name
	LDR name, [currentthread, #THREAD_CurrentFrameOffset]
	ADD name, name, index, LSL#2 ; offset by value in index * 4
	LDR value2, [name, #FRAME_LocalVarOffset] ; offset by local variable offset.

	MEND

;****************************************************************
;
; LocalPutSingle
;
; Put a single word into the local variable array at the specified index.
; The index must be specified in units of words.  It is multiplied
; by 4 to turn into a byte offset.
;
; Register Set:
;   Input: INDEX, VALUE
;
;****************************************************************
	MACRO
	LocalPutSingle

	; Load currentframe into name
	LDR name, [currentthread, #THREAD_CurrentFrameOffset]
	ADD name, name, index, LSL#2 ; offset by value in index * 4
	STR value, [name, #FRAME_LocalVarOffset] ; offset by local variable offset.

	MEND


;****************************************************************
;
; StackPushSingle
; Push a single word onto the opstack of the currentframe.  Stack grows up.
; Stack pointer is already pointing 
; to where the new word will go.
;
; Register set:
; Inputs:  stackpointer
;          value - word to push
; Modifies: stackpointer
;
;****************************************************************

	MACRO
	StackPushSingle

	STR value, [stackpointer], #4   ; push word to address pointed to by stack pointer.
	                                ; stackpointer++ (+=4 because it is byte oriented).

	MEND

;****************************************************************
;
; StackPeekSingle
;
; Reads the value at the top of the stack but does not remove it.  Used for:
; Dup
;
; Register Set:
; Output: VALUE - value peeked from the stack
; Internal: STACKPOINTER
;
;****************************************************************

	MACRO
	StackPeekSingle

	; read the value at stackpointer - 1 word (-4 bytes)
	LDR value, [stackpointer, #-4] ; value = RAM[stackpointer - 1]


	MEND

;****************************************************************
;
; StackPopSingle
;
; Pop value from top of frame's operand stack
;
; Register set:
; Output: VALUE - value popped from stack
; Modifies: stackpointer
;
;****************************************************************

	MACRO
	StackPopSingle

	LDR value, [stackpointer, #-4]!  ; value = RAM[--stackpointer]
	
	MEND



; !!! Note: FieldStackPush and FieldStackPop should be combined into one.



;****************************************************************
;
; FieldStackPush
;
; A FieldStackPush differs from a regular stack push in that it may push a Single or a Double
; depending on the value of a register (CPHANDLE) set in FieldGetInfo process.
;
; Register Set:
;   Input: STACKPOINTER
;           case CPHANDLE = 1:
;              VALUE - value of single field
;           case CPHANDLE = 2:
;              VALUE1 - value of first half of double field
;              VALUE2 - value of second half of double field
;****************************************************************

 	MACRO
	FieldStackPush

	CMP cphandle, #1
	STREQ value, [stackpointer], #4  ; push it then increment SP
	; else double
	STRDNE value1, value2, [stackpointer], #8 ; push it then increment SP by 2 words

	MEND



;****************************************************************
;
; FieldStackPop
;
; FieldStackPop differs from regular stack pop processes.  This process may pop a single or a double depending
; on what type of field it is.  The field type (single or double) is determined after the field is found.
;
; Register Set:
;   Input:  CPHANDLE - size of this field, index into object for this field
;           STACKPOINTER
;   Output: case CPHANDLE = 1: (size == 1)
;              VALUE - value of single field
;           case CPHANDLE = 2:
;              VALUE1 - value of first half of double field
;              VALUE2 - value of second half of double field
;
;****************************************************************

	MACRO
	FieldStackPop

	CMP cphandle, #1
	LDREQ value, [stackpointer, #-4]!  ; value = RAM[--stackpointer]
	SUBNE stackpointer, stackpointer, #-4   ; stackpointer--
	LDRDNE value1, value2, [stackpointer], #-4  ; value1 = RAM[stackpointer], value2 = RAM[stackpointer-1], stackpointer--

	MEND


;****************************************************************
;
; StackPopObjectref
;
; This is a special, slightly modifed version of StackPopSingle.  This process moves the single word
; popped from the stack into the HANDLE register rather than the VALUE register.  This is needed to avoid
; colliding with Field instruction processes Object(Get/Put)Field and FieldStack(Pop/Push) since these use
; the VALUE register.
;
; Register set:
; Output: HANDLE - objectref popped from stack
;
;****************************************************************

	MACRO
	StackPopObjectref

	LDR handle, [stackpointer, #-4]!  ; handle = RAM[--stackpointer]
	
	MEND


;****************************************************************
;
; StackDuplicateXSingle
;
; Reads the value at the top of the operand stack and inserts it
; into a slot further down in the operand stack.  
; 
; Note!
; The inserted value moves the values on top up by one.
;
; Implements DUP_X1 and DUP_X2.
;
; Parameterized by index register. Index should be 2 for DUP_X1
; and 3 for DUP_X2.
;
; Register set:
; 
; Intermediate:  stackpointer - initially points to 1 beyond the top of the stack
;                value - the values from the stack
;                handle - variable
;
;****************************************************************

	MACRO
	StackDuplicateXSingle

	;  RAM[SP] = RAM[SP-1]
    ;  RAM[SP-1] = RAM[SP-2]
    ;  RAM[SP-2] = RAM[SP]
    ;  SP++

	; handle = SP - 4
	SUB handle, stackpointer, #4

	; RAM[SP-4]
	LDR value, [handle]
	
	; RAM[SP] = RAM[SP-4]
	STR value, [stackpointer]

	; SP-8
	SUB index, handle, #4

	; RAM[SP-8]
	LDR value, [index]

	; RAM[SP-4] = RAM[SP-8]
	STR value, [handle]

	; RAM[SP]
	LDR value, [stackpointer]

	; RAM[SP-2] = RAM[SP]
	STR value, [index]

	; SP+=4
	ADD stackpointer, stackpointer, #4

	MEND









;****************************************************************

	END

