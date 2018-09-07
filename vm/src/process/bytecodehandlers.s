;****************************************************************
; 
;    Date: Apr 9, 2008
;    Author: Evan Ross
;
;    Bytecode handlers.  The handlers for all of the byte codes
;    are located here.  Simple bytecodes may be handled entirely
;    in its handler procedure, however many bytecode handlers
;    are composed of calls to several other processes.
; 
;    The registers are NOT required to be preserved across
;    bytecodes, however some registers MAY be required to be
;    preserved across process calls within a handler.
;
;    IMPORTANT!  If bytecode operands are retrieved in the handlers,
;    the vmpc MUST be incremented past the operands to point to the
;    next vmpc.
;
;    Each handler must directly branch ("return") to the 
;    main_execution_loop because nothing was saved in the link register.
;
;****************************************************************

	AREA bytecodehandlerarea, CODE, READONLY

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
	INCLUDE comparemacros.s

	IMPORT reference_lookup
	IMPORT table_lookup
	IMPORT objectclass_lookup
	IMPORT objectsize_lookup
	IMPORT object_setup
	IMPORT frameinfo_lookup
	IMPORT allocate_memory
	IMPORT deallocate_memory
	IMPORT frame_setup
	IMPORT frame_teardown
	IMPORT set_pc
	IMPORT classinfo_lookup		  
	IMPORT arraysize_lookup

	IMPORT main_execution_loop


	EXPORT handler_unused
handler_unused

	; note that the current opcode is in register 'value' and
	; can be used in a debug output dump.
	ThrowExceptionUnconditional UnhandledOpcodeException




;****************************************************************
;
; Load Single
; 
;   Loads any 32-bit value from the local variable array onto
;   the operand stack.
;
;   Handles the following bytecodes:
;
;   aload, aload_0, aload_1, aload_2, aload_3
;   fload, fload_0, fload_1, fload_2, fload_3
;   iload, iload_0, iload_1, iload_2, iload_3
;   
;
;   Operands:
;      loadsingle - index, 1 byte, range [0,3]
;      loadsingle_0/1/2 - none.
;
;   Function:
;      aload - push reference at local variable[index] onto operand stack    
;      iload - push integer at local variable[index] onto operand stack    
;      fload - push float at local variable[index] onto operand stack    
;
;   Notes:
;      For instance methods, aload[0] refers to 'this'.
  
	EXPORT handler_loadsingle0
handler_loadsingle0

	; load local var 0.
	MOV index, #0
	; Use index register to determine which local var to load.
	LocalGetSingle
	StackPushSingle

	B main_execution_loop ; done

	EXPORT handler_loadsingle1
handler_loadsingle1

	; load local var 1.
	MOV index, #1
	; Use index register to determine which local var to load.
	LocalGetSingle
	StackPushSingle

	B main_execution_loop ; done

	EXPORT handler_loadsingle2
handler_loadsingle2

	; load local var 2.
	MOV index, #2
	; Use index register to determine which local var to load.
	LocalGetSingle
	StackPushSingle

	B main_execution_loop ; done

	EXPORT handler_loadsingle3
handler_loadsingle3

	; load local var 3.
	MOV index, #3
	; Use index register to determine which local var to load.
	LocalGetSingle
	StackPushSingle

	B main_execution_loop ; done

	EXPORT handler_loadsingle
handler_loadsingle

	; load the index from the operand and increment vmpc by 1 byte.
	LDRB index, [vmpc], #1

	; Use index register to determine which local var to load.
	LocalGetSingle
	StackPushSingle

	B main_execution_loop ; done



;****************************************************************
;
; Store Single
; 
;   Stores any 32-bit value from the operand stack to the
;   local variable array 
;
;   Handles the following bytecodes:
;
;   astore, astore_0, astore_1, astore_2, astore_3
;   fstore, fstore_0, fstore_1, fstore_2, fstore_3
;   istore, istore_0, istore_1, istore_2, istore_3
;   
;
;   Operands:
;      storesingle - index, 1 byte, range [0,3]
;      storesingle_0/1/2 - none.
;
;   Function:
;      astore - pop reference from operand stack and store into local variable[index]   
;      istore - pop integer from operand stack and store into local variable[index]    
;      fstore - pop float from operand stack and store into local variable[index]   
;

  
	EXPORT handler_storesingle0
handler_storesingle0

	; load local var 0.
	MOV index, #0
	; Use index register to determine which local var to store.
	StackPopSingle
	LocalPutSingle

	B main_execution_loop ; done

	EXPORT handler_storesingle1
handler_storesingle1

	; load local var 1.
	MOV index, #1
	; Use index register to determine which local var to store.
	StackPopSingle
	LocalPutSingle

	B main_execution_loop ; done

	EXPORT handler_storesingle2
handler_storesingle2

	; load local var 2.
	MOV index, #2
	; Use index register to determine which local var to store.
	StackPopSingle
	LocalPutSingle

	B main_execution_loop ; done

	EXPORT handler_storesingle3
handler_storesingle3

	; load local var 3.
	MOV index, #3
	; Use index register to determine which local var to store.
	StackPopSingle
	LocalPutSingle

	B main_execution_loop ; done

	EXPORT handler_storesingle
handler_storesingle

	; load the index from the operand and increment vmpc by 1 byte.
	LDRB index, [vmpc], #1

	; Use index register to determine which local var to store.
	StackPopSingle
	LocalPutSingle

	B main_execution_loop ; done


;****************************************************************
;
; Const Push Int
; 
;   Pushes a pre-defined 32-bit integer value onto operand stack.
;
;   Handles the following bytecodes:
;
;   iconst_m1, iconst_0, iconst_1, iconst_2, iconst_3
;   iconst_4, iconst_5
;   
;
;   Operands:
;      None.
;
;****************************************************************
 
	EXPORT handler_iconst_m1
handler_iconst_m1

	; load constant -1.
	;LDR value, =#0xffffffff
	MOV value, #-1
	; push value onto stack
	StackPushSingle

	B main_execution_loop ; done iconst_m1

;*********************************
	EXPORT handler_iconst_0
handler_iconst_0

	; load constant 0.
	MOV value, #0
	; push value onto stack
	StackPushSingle

	B main_execution_loop ; done iconst_0

;*********************************
	EXPORT handler_iconst_1
handler_iconst_1

	; load constant 1.
	MOV value, #1
	; push value onto stack
	StackPushSingle

	B main_execution_loop ; done iconst_1

;*********************************
	EXPORT handler_iconst_2
handler_iconst_2

	; load constant 2.
	MOV value, #2
	; push value onto stack
	StackPushSingle

	B main_execution_loop ; done iconst_2

;*********************************
	EXPORT handler_iconst_3
handler_iconst_3

	; load constant 3.
	MOV value, #3
	; push value onto stack
	StackPushSingle

	B main_execution_loop ; done iconst_3

;*********************************
	EXPORT handler_iconst_4
handler_iconst_4

	; load constant 4.
	MOV value, #4
	; push value onto stack
	StackPushSingle

	B main_execution_loop ; done iconst_4

;*********************************
	EXPORT handler_iconst_5
handler_iconst_5

	; load constant 5.
	MOV value, #5
	; push value onto stack
	StackPushSingle

	B main_execution_loop ; done iconst_5

		 

;****************************************************************
;
; Const Push Float
; 
;   Pushes a pre-defined 32-bit float value onto operand stack.
;
;   Handles the following bytecodes:
;
;   fconst_0, fconst_1, fconst_2
;
;   Operands:
;      None.
;
;****************************************************************
 

	EXPORT handler_fconst_0
handler_fconst_0

	; load constant 0.0
	MOV value, #0x0	; Float.floatToIntBits = 0x0
	; push value onto stack
	StackPushSingle

	B main_execution_loop ; done fconst_0

;*********************************
	EXPORT handler_fconst_1
handler_fconst_1

	; load constant 1.0
	MOV value, #0x3f800000	 ; Float.floatToIntBits = 0x3f800000
	; push value onto stack
	StackPushSingle

	B main_execution_loop ; done fconst_1

;*********************************
	EXPORT handler_fconst_2
handler_fconst_2

	; load constant 2.0
	MOV value, #0x40000000	; Float.floatToIntBits = 0x40000000  
	; push value onto stack
	StackPushSingle

	B main_execution_loop ; done fconst_2


;****************************************************************
;
;  aconst_null
;
;****************************************************************

	EXPORT handler_aconst_null
handler_aconst_null

	; load NULL
	MOV32 value, #NULLREGISTER
	; push value onto stack
	StackPushSingle

	B main_execution_loop ; done aconst_null

;****************************************************************
;
;  ldc
;
;  Load from constant pool and push onto stack
;
;  Loads a single (int, float, or string reference) to the CP.
;
;  Operands:
;      index - a 1 byte index into the CP table in units of words.
;
;****************************************************************

	EXPORT handler_ldc
handler_ldc

	; load the index into the CP table
	LDRB index, [vmpc], #1

	LoadConstantPool  ; put the item into value register
	StackPushSingle   ; push the item onto the opstack

	B main_execution_loop  ; done handler_ldc		  



;****************************************************************
;
;  bipush
;
;  Push immediate byte onto operand stack
;
;  Operands:
;     value - a signed byte value to push onto the stack.
;
;****************************************************************
	EXPORT handler_bipush
handler_bipush
	
	LDRSB value, [vmpc], #1
	StackPushSingle

	B main_execution_loop ; done


;****************************************************************
;
;  sipush
;
;  Push immediate 2-byte value onto operand stack
;
;  Operands:
;     value - a signed 2-byte value to push onto the stack.
;
;****************************************************************
	EXPORT handler_sipush
handler_sipush
	
	LDRSH value, [vmpc], #2
	StackPushSingle

	B main_execution_loop ; done

;****************************************************************
;
;  dup
;
;  Duplicate top stack operand.
;
;  Operands:
;     value - a 1 byte value to push onto the stack.
;
;****************************************************************
	EXPORT handler_dup
handler_dup
	
	StackPeekSingle
	StackPushSingle

	B main_execution_loop ; done


;****************************************************************
;
;  dupx1
;
; Reads the value at the top of the operand stack and inserts it
; into a slot further down in the operand stack.  
; 
; Note!
; The inserted value moves the values on top up by one.
;
;****************************************************************


	EXPORT handler_dupx1
handler_dupx1

	StackDuplicateXSingle


 	B main_execution_loop  ; done	

;****************************************************************
;
; pop
;
; Pop and discard a single from the operand stack
;
;****************************************************************

	EXPORT handler_pop
handler_pop

	StackPopSingle

 	B main_execution_loop  ; done pop


;****************************************************************
;
;  invokevirtual
;
;  Invoke instance method.
;
;  Operands:
;      index - a 2-byte index into the constant pool of the
;              current class.  Points to a MethodRefInfo
;
;****************************************************************
	EXPORT handler_invokevirtual
handler_invokevirtual
	


	; Get the 2-byte index
	LDRH index, [vmpc], #2   ; vmpc += 2

	BL reference_lookup
	SetupVirtualLookup
	SetupMethodLookup
	BL table_lookup     ; find method in referenced class to get argument count
	BL objectclass_lookup
	BL table_lookup 	; find actual method of 'this' class
	BL frameinfo_lookup ; 7. FrameInfoLookup
	BL allocate_memory  ; 8. AllocateMemory
	SetupVirtualLookup	; - framesetup needs this
	BL frame_setup      ; 10. FrameSetup
	BL set_pc 			; 11. SetPC

	B main_execution_loop  ; done

;****************************************************************
;
;  invokestatic
;
;  Invoke static (class) method.  Static method invocations do
;  not follow up with a lookup based on objectref.
;
;  Operands:
;      index - a 2-byte index into the constant pool of the
;              current class.  Points to a MethodRefInfo
;
;****************************************************************
	EXPORT handler_invokestatic
handler_invokestatic
	

	; Get the 2-byte index
	LDRH index, [vmpc], #2   ; vmpc += 2

	BL reference_lookup
	SetupStaticLookup
	SetupMethodLookup
	BL table_lookup     ; find method in referenced class to get argument count
	BL frameinfo_lookup ; 7. FrameInfoLookup
	BL allocate_memory  ; 8. AllocateMemory
	BL frame_setup      ; 10. FrameSetup
	BL set_pc 			; 11. SetPC

	B main_execution_loop  ; done

;****************************************************************
;
;  invokespecial
;
;  Invoke instance initialization method (constructor). 
;
;  Operands:
;      index - a 2-byte index into the constant pool of the
;              current class.  Points to a MethodRefInfo
;
;****************************************************************
	EXPORT handler_invokespecial
handler_invokespecial
	

	; Get the 2-byte index
	LDRH index, [vmpc], #2   ; vmpc += 2

	BL reference_lookup
	SetupStaticLookup
	SetupMethodLookup
	BL table_lookup     ; find method in referenced class to get argument count
	BL frameinfo_lookup ; 7. FrameInfoLookup
	BL allocate_memory  ; 8. AllocateMemory
	SetupVirtualLookup  ; get objectref copied over with the args.
	BL frame_setup      ; 10. FrameSetup
	BL set_pc 			; 11. SetPC

	B main_execution_loop  ; done



;****************************************************************
;
;  getfield
;
;  Get instance field.
;
;  Operands:
;      index - a 2-byte index into the constant pool of the
;              current class.  Points to a FieldRefInfo
;
;****************************************************************

	EXPORT handler_getfield
handler_getfield

	; Get the 2-byte index
	LDRH index, [vmpc], #2  ; vmpc += 2

	BL reference_lookup
	SetupFieldLookup      ; Search the field table
	SetupVirtualLookup    ; Field may be part of super class or above
	BL table_lookup
	FieldGetInfo
	StackPopObjectref
	ObjectGetField
	FieldStackPush

	B main_execution_loop  ; done handler_getfield


;****************************************************************
;
;  putfield
;
;  Put instance field.
;
;  Operands:
;      index - a 2-byte index into the constant pool of the
;              current class.  Points to a FieldRefInfo
;
;****************************************************************

	EXPORT handler_putfield
handler_putfield

	; Get the 2-byte index
	LDRH index, [vmpc], #2  ; vmpc += 2

	BL reference_lookup
	SetupFieldLookup      ; Search the field table
	SetupVirtualLookup    ; Field may be part of super class or above
	BL table_lookup
	FieldGetInfo
	FieldStackPop
	StackPopObjectref
	ObjectPutField

	B main_execution_loop  ; done handler_putfield

;****************************************************************
;
; return (void)
;
; pop the current frame and restore the previous frame's state.
;
;
;****************************************************************

	EXPORT handler_returnvoid
handler_returnvoid

	BL frame_teardown	  ; restore the previous frame
	BL deallocate_memory  ; remove the frame's allocated memory


	B main_execution_loop  ; done handler_returnvoid

;****************************************************************
;
; return (single word)
;
; pop the current frame and restore the previous frame's state.
; push the return value onto the opstack.
;
;
;****************************************************************

	EXPORT handler_returnsingle
handler_returnsingle

	StackPopSingle        ; pop from current frame
	BL frame_teardown	  ; restore the previous frame
	BL deallocate_memory  ; remove the frame's allocated memory
	StackPushSingle       ; push to restored frame's opstack


	B main_execution_loop  ; done handler_returnsingle


;****************************************************************
;
;  new (instance)
;
;  Create a new instance of a class.
;
;  Operands:
;      index - a 2-byte index into the constant pool of the
;              current class.  Points to a ClassInfo
;
;****************************************************************

	EXPORT handler_new
handler_new

	; Get the 2-byte index
	LDRH index, [vmpc], #2  ; vmpc += 2	

	BL classinfo_lookup
	BL objectsize_lookup
	BL allocate_memory
	BL object_setup
	StackPushSingle

	B main_execution_loop  ; done handler_new

;****************************************************************
;
;  anewarray 
;
;  Create a new array of reference types (32-bit words).
;
;  Operands:
;      index - a 2-byte index into the constant pool of the
;              current class indicating the type of the elements.
;              We ignore this and simply create an array of singles.
;
;****************************************************************

	EXPORT handler_anewarray
handler_anewarray

	; Skip the 2-byte index
	ADD vmpc, vmpc, #2

	; Set index = 10 for 'single' array types
	MOV index, #10

	StackPopSingle
	BL arraysize_lookup
	BL allocate_memory
	ArraySetup
	BL object_setup
	StackPushSingle

	B main_execution_loop  ; done handler_anewarray

;****************************************************************
;
;  newarray 
;
;  Create a new array of type indicated in operand.  This process
;  is otherwise identical to anewarray.
;
;  Operands:
;      index - (atype) code to indicate type of array to create.
;
;
;****************************************************************

	EXPORT handler_newarray
handler_newarray

	; load in the array type operand
	LDRB index, [vmpc], #1  ; vmpc += 1

	StackPopSingle
	BL arraysize_lookup
	BL allocate_memory
	ArraySetup
	BL object_setup
	StackPushSingle

	B main_execution_loop  ; done handler_newarray


;****************************************************************
; arraystoresingle  : Stores a 32-bit word to an array.
; arraystorebyte    : Stores an 8-bit word to an array.
; arraystoreshort   : Stores a 16-bit word to an array.
;
; Operands: none, however the array reference and index
;           are popped from the operand stack.
;
;****************************************************************

	EXPORT handler_arraystoresingle
handler_arraystoresingle

	StackPopSingle
	ArrayAccessSetup
	ArrayPutSingle	

	B main_execution_loop  ; done handler_arraystoresingle

	EXPORT handler_arraystorebyte
handler_arraystorebyte

	StackPopSingle
	ArrayAccessSetup
	ArrayPutByte	

	B main_execution_loop  ; done handler_arraystorebyte

	EXPORT handler_arraystoreshort
handler_arraystoreshort

	StackPopSingle
	ArrayAccessSetup
	ArrayPutShort	

	B main_execution_loop  ; done handler_arraystoreshort


;****************************************************************
; arrayloadbyte   : Loads an 8-bit signed word from an array.
; arrayloadsingle : Loads a 32-bit word from an array.
; arrayloadchar   : Loads an 8-bit unsigned word from an array.
; arrayloadshort  : Loads a 16-bit signed word from an array.
;
; Operands: none, however the array reference and index
;           are popped from the operand stack.
;
;****************************************************************

	EXPORT handler_arrayloadsingle
handler_arrayloadsingle

	ArrayAccessSetup
	ArrayGetSingle	
	StackPushSingle

	B main_execution_loop  ; done handler_arrayloadsingle


	EXPORT handler_arrayloadbyte
handler_arrayloadbyte

	ArrayAccessSetup
	ArrayGetByte	
	StackPushSingle

	B main_execution_loop  ; done handler_arrayloadbyte

	EXPORT handler_arrayloadchar
handler_arrayloadchar

	ArrayAccessSetup
	ArrayGetChar	
	StackPushSingle

	B main_execution_loop  ; done handler_arrayloadchar

	EXPORT handler_arrayloadshort
handler_arrayloadshort

	ArrayAccessSetup
	ArrayGetShort	
	StackPushSingle

	B main_execution_loop  ; done handler_arrayloadshort

;****************************************************************
; arraylength
;
; Obtain the ArrayLength field from Array objects.
; 
; Register Set:
;  Input: VALUE - Array reference 'arrayref' popped from opstack
;  Output: VALUE - Array length 'length' to be pushed onto opstack
;
;
;****************************************************************
	EXPORT handler_arraylength
handler_arraylength

	StackPopSingle
	LDR value, [value, #ARRAY_ArrayLengthOffset]
	StackPushSingle

	B main_execution_loop  ; done handler_arraylength


;****************************************************************
; iinc
;
; Increments a local variable by a variable amount
;
; Operands:
;     index - unsigned word index into the local variable array
;     constant - singed byte to add to the variable
;
;****************************************************************

	EXPORT handler_iinc
handler_iinc

	; Get the local var index
	LDRB index, [vmpc], #1

	; Get the signed constant into value1
	LDRSB value1, [vmpc], #1

	LocalGetSingleAlt  ; Get the local var into value2 and get name
	AddSingleInt       ; Add value1 and value2 and place into value

	; store the local variable back.
	; note that 'name' has already been offset to point to the local
	; variable.
	STR value, [name, #FRAME_LocalVarOffset]
		

	B main_execution_loop  ; done handler_iinc


;****************************************************************
; checkcast
;
; checks a reference cast.
;
; Operands:
;     index - 2-byte unsigned index into the constant pool
;
; Notes:
; Not implemented, unconditionally passes.
; Requires that Collections be paramaterized in the IDE
; (e.g. Vector<SomeObject>) to enforce type checking
; at compile time since it won't be checked here.
; Must increment PC by 2 bytes to pass 'index'.
;
;****************************************************************

	EXPORT handler_checkcast
handler_checkcast
	
	ADD vmpc, vmpc, #2 ; vmpc += 2

	B main_execution_loop  ; done handler_checkcast


;****************************************************************
;
; goto
;
; Unconditional branch
; 
; Notes:
;    The operand is a 2-byte signed value that is added to the
;    address of the goto opcode to get the new vmpc.
;
;****************************************************************
	EXPORT handler_goto
handler_goto

	; The 'value' is referenced to the instruction address so we can
	; take advantage of a post-indexes load to do the subtraction.

	LDRSH value, [vmpc], #-1 ; subtract one here: vmpc -= 1 

	; apply the offset to the vm pc
	ADD vmpc, vmpc, value

	B main_execution_loop  ; done handler_goto
	
;****************************************************************
;
; i2b
;
; Convert int to byte
; 
; Notes: int is truncated to a byte and signed extended.
;    
;
;****************************************************************	

	EXPORT handler_i2b
handler_i2b

	; load the value from the top of the opstack as a byte 
	; and have it sign extended
	; Note that the stackpointer points to the next available slot
	LDRSB value, [stackpointer, #-4]

	; put it back
	STR value, [stackpointer, #-4]

	B main_execution_loop  ; done handler_i2b

	
;****************************************************************
;
; i2c
;
; Convert int to char (byte in this architecture)
; 
; Notes: int is truncated to a byte and zero extended.
;    
;
;****************************************************************	

	EXPORT handler_i2c
handler_i2c

	; load the value from the top of the opstack as a byte 
	; and have it zero extended
	; Note that the stackpointer points to the next available slot
	LDRB value, [stackpointer, #-4]

	; put it back
	STR value, [stackpointer, #-4]

	B main_execution_loop  ; done handler_i2c

;****************************************************************
;
; i2s
;
; Convert int to short
; 
; Notes: int is truncated to a short and sign extended.
;    
;
;****************************************************************	

	EXPORT handler_i2s
handler_i2s

	; load the value from the top of the opstack as a byte 
	; and have it sign extended
	; Note that the stackpointer points to the next available slot
	LDRSH value, [stackpointer, #-4]

	; put it back
	STR value, [stackpointer, #-4]

	B main_execution_loop  ; done handler_i2s

;****************************************************************
;****************************************************************
;
;   Branch bytecode handlers
;
;****************************************************************
;****************************************************************

;****************************************************************
;
;  if_icmpeq, if_acmpeq (references are treated the same as ints)
;
;  Compare two values popped from the operand stack and 
;  branch if value1 == value2
;
;****************************************************************

	EXPORT handler_ificmpeq
handler_ificmpeq

	; Get the 2-byte signed index
	LDRSH index, [vmpc], #2  ; vmpc += 2	

	CompareSingleIntStackPop2
	CompareAndBranchSingleIntEQ
	
	B main_execution_loop 	 ; done handler_ificmpeq

;****************************************************************
;
;  if_icmpne, if_acmpne (references are treated the same as ints)
;
;  Compare two values popped from the operand stack and 
;  branch if value1 != value2
;
;****************************************************************

	EXPORT handler_ificmpne
handler_ificmpne

	; Get the 2-byte signed index
	LDRSH index, [vmpc], #2  ; vmpc += 2	

	CompareSingleIntStackPop2
	CompareAndBranchSingleIntNE
	
	B main_execution_loop 	 ; done handler_ificmpne

;****************************************************************
;
;  if_icmplt
;
;  Compare two values popped from the operand stack and 
;  branch if value1 < value2
;
;****************************************************************

	EXPORT handler_ificmplt
handler_ificmplt

	; Get the 2-byte signed index
	LDRSH index, [vmpc], #2  ; vmpc += 2	

	CompareSingleIntStackPop2
	CompareAndBranchSingleIntLT
	
	B main_execution_loop 	 ; done handler_ificmplt

;****************************************************************
;
;  if_icmpge
;
;  Compare two values popped from the operand stack and 
;  branch if value1 > value2
;
;****************************************************************

	EXPORT handler_ificmpge
handler_ificmpge

	; Get the 2-byte signed index
	LDRSH index, [vmpc], #2  ; vmpc += 2	

	CompareSingleIntStackPop2
	CompareAndBranchSingleIntGE
	
	B main_execution_loop 	 ; done handler_ificmpge

;****************************************************************
;
;  if_icmpgt
;
;  Compare two values popped from the operand stack and 
;  branch if value1 > value2
;
;****************************************************************

	EXPORT handler_ificmpgt
handler_ificmpgt

	; Get the 2-byte signed index
	LDRSH index, [vmpc], #2  ; vmpc += 2	

	CompareSingleIntStackPop2
	CompareAndBranchSingleIntGT
	
	B main_execution_loop 	 ; done handler_ificmpgt

;****************************************************************
;
;  if_icmple
;
;  Compare two values popped from the operand stack and 
;  branch if value1 <= value2
;
;****************************************************************

	EXPORT handler_ificmple
handler_ificmple

	; Get the 2-byte signed index
	LDRSH index, [vmpc], #2  ; vmpc += 2	

	CompareSingleIntStackPop2
	CompareAndBranchSingleIntLE
	
	B main_execution_loop 	 ; done handler_ificmple

;****************************************************************
;
;  ifeq
;
;  Compare one value popped from the operand stack with 0 and
;  branch if value1 == 0
;
;****************************************************************

	EXPORT handler_ifeq
handler_ifeq

	; Get the 2-byte signed index
	LDRSH index, [vmpc], #2  ; vmpc += 2	

	CompareSingleIntStackPop0
	CompareAndBranchSingleIntEQ
	
	B main_execution_loop 	 ; done handler_ifeq

;****************************************************************
;
;  ifne
;
;  Compare one value popped from the operand stack with 0 and
;  branch if value1 != 0
;
;****************************************************************

	EXPORT handler_ifne
handler_ifne

	; Get the 2-byte signed index
	LDRSH index, [vmpc], #2  ; vmpc += 2	

	CompareSingleIntStackPop0
	CompareAndBranchSingleIntNE
	
	B main_execution_loop 	 ; done handler_ifne

;****************************************************************
;
;  iflt
;
;  Compare one value popped from the operand stack with 0 and
;  branch if value1 < 0
;
;****************************************************************

	EXPORT handler_iflt
handler_iflt

	; Get the 2-byte signed index
	LDRSH index, [vmpc], #2  ; vmpc += 2	

	CompareSingleIntStackPop0
	CompareAndBranchSingleIntLT
	
	B main_execution_loop 	 ; done handler_iflt

;****************************************************************
;
;  ifge
;
;  Compare one value popped from the operand stack with 0 and
;  branch if value1 >= 0
;
;****************************************************************

	EXPORT handler_ifge
handler_ifge

	; Get the 2-byte signed index
	LDRSH index, [vmpc], #2  ; vmpc += 2	

	CompareSingleIntStackPop0
	CompareAndBranchSingleIntGE
	
	B main_execution_loop 	 ; done handler_ifge

;****************************************************************
;
;  ifgt
;
;  Compare one value popped from the operand stack with 0 and
;  branch if value1 > 0
;
;****************************************************************

	EXPORT handler_ifgt
handler_ifgt

	; Get the 2-byte signed index
	LDRSH index, [vmpc], #2  ; vmpc += 2	

	CompareSingleIntStackPop0
	CompareAndBranchSingleIntGT
	
	B main_execution_loop 	 ; done handler_ifgt

;****************************************************************
;
;  ifle
;
;  Compare one value popped from the operand stack with 0 and
;  branch if value1 <= 0
;
;****************************************************************

	EXPORT handler_ifle
handler_ifle

	; Get the 2-byte signed index
	LDRSH index, [vmpc], #2  ; vmpc += 2	

	CompareSingleIntStackPop0
	CompareAndBranchSingleIntLE
	
	B main_execution_loop 	 ; done handler_ifle

;****************************************************************
;
;  if_nonnull
;
;  Compare a value (reference) popped from the operand stack and 
;  null; branch if not equal
;
;****************************************************************

	EXPORT handler_ifnonnull
handler_ifnonnull

	; Get the 2-byte signed index
	LDRSH index, [vmpc], #2  ; vmpc += 2	

	CompareSingleIntStackPopNull
	CompareAndBranchSingleIntNE
	
	B main_execution_loop 	 ; done handler_ifnonnull

;****************************************************************
;
;  if_null
;
;  Compare a value (reference) popped from the operand stack and 
;  null; branch if equal
;
;****************************************************************

	EXPORT handler_ifnull
handler_ifnull

	; Get the 2-byte signed index
	LDRSH index, [vmpc], #2  ; vmpc += 2	

	CompareSingleIntStackPopNull
	CompareAndBranchSingleIntEQ
	
	B main_execution_loop 	 ; done handler_ifnull

;****************************************************************
;****************************************************************
;
;   Arithmetic bytecode handlers
;
;****************************************************************
;****************************************************************


;****************************************************************
;
;  iadd
;
;  Add two values popped from the operand stack and push
;  the result back on.
;
;****************************************************************

	EXPORT handler_iadd
handler_iadd

	PrepareALUSingle
	AddSingleInt
	StackPushSingle

	B main_execution_loop 	 ; done handler_iadd

;****************************************************************
;
;  isub
;
;  Subtract two values popped from the operand stack and push
;  the result back on.
;
;****************************************************************

	EXPORT handler_isub
handler_isub

	PrepareALUSingle
	SubSingleInt
	StackPushSingle

	B main_execution_loop 	 ; done handler_isub	

;****************************************************************
;
;  imul
;
;  Multiply two values popped from the operand stack and push
;  the result back on.
;
;****************************************************************

	EXPORT handler_imul
handler_imul

	PrepareALUSingle
	MultSingleInt
	StackPushSingle

	B main_execution_loop 	 ; done handler_imul

;****************************************************************
;
;  idiv
;
;  Divide two values popped from the operand stack and push
;  the result back on.
;
;****************************************************************

	EXPORT handler_idiv
handler_idiv

	PrepareALUSingle
	DivSingleInt
	StackPushSingle

	B main_execution_loop 	 ; done handler_idiv

;****************************************************************
;
;  irem
;
;  Mods two values popped from the operand stack and push
;  the result back on.
;
;****************************************************************

	EXPORT handler_irem
handler_irem

	PrepareALUSingle
	RemSingleInt
	StackPushSingle

	B main_execution_loop 	 ; done handler_irem

;****************************************************************
;
;  ineg
;
;  Negates the value popped from the operand stack and pushes
;  the result back on.
;
;****************************************************************

	EXPORT handler_ineg
handler_ineg

	StackPopSingle
	NegSingleInt
	StackPushSingle

	B main_execution_loop 	 ; done handler_ineg

;****************************************************************
;
;  ishl
;
;  Left shift the value popped from the operand stack and pushes
;  the result back on.
;
;****************************************************************

	EXPORT handler_ishl
handler_ishl

	PrepareALUSingle
	ShiftLeftInt
	StackPushSingle

	B main_execution_loop 	 ; done handler_ishl

;****************************************************************
;
;  ishr
;
;  Right shift the value popped from the operand stack and pushes
;  the result back on.
;
;****************************************************************

	EXPORT handler_ishr
handler_ishr

	PrepareALUSingle
	ShiftRightInt
	StackPushSingle

	B main_execution_loop 	 ; done handler_ishr


;****************************************************************
;
;  iushr
;
;  Arithmetic right shift the value popped from the operand stack and pushes
;  the result back on.
;
;****************************************************************

	EXPORT handler_iushr
handler_iushr

	PrepareALUSingle
	ShiftRightLogicalInt
	StackPushSingle

	B main_execution_loop 	 ; done handler_iushr

;****************************************************************
;
;  iand
;
;  Logical AND the values popped from the operand stack and pushes
;  the result back on.
;
;****************************************************************

	EXPORT handler_iand
handler_iand

	PrepareALUSingle
	AND value, value1, value2
	StackPushSingle

	B main_execution_loop 	 ; done handler_iand

;****************************************************************
;
;  ior
;
;  Logical OR the values popped from the operand stack and pushes
;  the result back on.
;
;****************************************************************

	EXPORT handler_ior
handler_ior

	PrepareALUSingle
	ORR value, value1, value2
	StackPushSingle

	B main_execution_loop 	 ; done handler_ior

;****************************************************************
;
;  ixor
;
;  Logical XOR the values popped from the operand stack and pushes
;  the result back on.
;
;****************************************************************

	EXPORT handler_ixor
handler_ixor

	PrepareALUSingle
	EOR value, value1, value2
	StackPushSingle

	B main_execution_loop 	 ; done handler_ixor


	END