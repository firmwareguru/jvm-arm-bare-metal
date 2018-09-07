;****************************************************************
; 
;    Date: Apr 9, 2008
;    Author: Evan Ross
;
;    Instruction Execution Engine
;
;    The "engine" is little more than a jump table that
;    transfers control to a procedure based on the fetched opcode.
;    Each procedure is responsible for obtaining the necessary
;    operands following the opcode, and to increment the vm's PC
;    past those operands to point to the next opcode.
;
;****************************************************************

   	AREA instructionexecutionenginearea, CODE, READONLY

	INCLUDE vmregisters.s
	INCLUDE vmexceptions.s

	IMPORT main_execution_loop
	IMPORT handler_unused
	IMPORT handler_loadsingle
	IMPORT handler_loadsingle0
	IMPORT handler_loadsingle1
	IMPORT handler_loadsingle2
	IMPORT handler_loadsingle3
	IMPORT handler_storesingle
	IMPORT handler_storesingle0
	IMPORT handler_storesingle1
	IMPORT handler_storesingle2
	IMPORT handler_storesingle3
	IMPORT handler_arraystoresingle
	IMPORT handler_arraystorebyte
	IMPORT handler_arraystoreshort
	IMPORT handler_arrayloadsingle
	IMPORT handler_arrayloadbyte
	IMPORT handler_arrayloadchar
	IMPORT handler_arrayloadshort
	IMPORT handler_iconst_m1
	IMPORT handler_iconst_0
	IMPORT handler_iconst_1
	IMPORT handler_iconst_2
	IMPORT handler_iconst_3
	IMPORT handler_iconst_4
	IMPORT handler_iconst_5
	IMPORT handler_fconst_0
	IMPORT handler_fconst_1
	IMPORT handler_fconst_2
	IMPORT handler_aconst_null
	IMPORT handler_bipush 
	IMPORT handler_sipush 
	IMPORT handler_pop
	IMPORT handler_invokevirtual
	IMPORT handler_invokestatic
	IMPORT handler_invokespecial
	IMPORT handler_putfield
	IMPORT handler_getfield
	IMPORT handler_returnvoid
	IMPORT handler_returnsingle
	IMPORT handler_new
	IMPORT handler_dup
	IMPORT handler_dupx1
	IMPORT handler_anewarray
	IMPORT handler_newarray
	IMPORT handler_ldc
	IMPORT handler_iinc
	IMPORT handler_checkcast
	IMPORT handler_goto
	IMPORT handler_i2b
	IMPORT handler_i2c
	IMPORT handler_i2s
	IMPORT handler_arraylength

	IMPORT handler_ificmpeq
	IMPORT handler_ificmpne
	IMPORT handler_ificmplt
	IMPORT handler_ificmpge
	IMPORT handler_ificmpgt
	IMPORT handler_ificmple
	IMPORT handler_ifnonnull
	IMPORT handler_ifnull
	IMPORT handler_ifne
	IMPORT handler_ifeq
	IMPORT handler_iflt
	IMPORT handler_ifge
	IMPORT handler_ifgt
	IMPORT handler_ifle

	IMPORT handler_iadd
	IMPORT handler_isub
	IMPORT handler_imul
	IMPORT handler_idiv
	IMPORT handler_irem
	IMPORT handler_ineg
	IMPORT handler_ishl
	IMPORT handler_ishr
	IMPORT handler_iushr
	IMPORT handler_iand
	IMPORT handler_ior
	IMPORT handler_ixor


	EXPORT instruction_execution_engine
instruction_execution_engine

	; Load the opcode into value, post-incrementing the vmpc
	LDRB value, [vmpc], #1  ; vmpc++

	ADR value1, bytecode_jump_table ; get address of jump table into a register
	LDR pc, [value1, value, LSL#2] ; jump to jumptable[opcode].

	ALIGN

bytecode_jump_table

    ; The jump table is necessarily organized by increasing
	; opcode value.
		   
	DCD main_execution_loop     ; 0  nop instruction

	;---------------------------------------------------
	; Push an immediate constant onto the operand stack
	;---------------------------------------------------

	DCD handler_aconst_null     ; 1     reference null

	DCD handler_iconst_m1       ; 2    interger constant -1
	DCD handler_iconst_0 	    ; 3	   interger constant 0
	DCD handler_iconst_1	    ; 4	   interger constant 1
	DCD handler_iconst_2	    ; 5	   interger constant 2
	DCD handler_iconst_3        ; 6	   interger constant 3
	DCD handler_iconst_4	    ; 7	   interger constant 4
	DCD handler_iconst_5        ; 8	   interger constant 5

	DCD handler_unused;DCD handler_lconst_0	    ; 9
	DCD handler_unused;DCD handler_lconst_1	    ; 10    long constant 1

	DCD handler_fconst_0	    ; 11    float constant 0.0  
	DCD handler_fconst_1	    ; 12	float constant 1.0
	DCD handler_fconst_2	    ; 13	float constant 2.0

	DCD handler_unused;DCD handler_dcosnt_0	    ; 14
	DCD handler_unused;DCD handler_dconst_1        ; 15

	;---------------------------------------------------
	; Push a value onto the operand stack
	;---------------------------------------------------

	DCD handler_bipush		    ; 16  bipush
	DCD handler_sipush		    ; 17  sipush
	DCD handler_ldc	            ; 18  ldc
	DCD handler_unused;DCD handler_ldcw		    ; 19  ldcw
	DCD handler_unused;DCD handler_ldc2w		    ; 20  ldc2w
	DCD handler_loadsingle      ; 21  iload
	DCD handler_unused;DCD handler_lload           ; 22  lload
	DCD handler_loadsingle	    ; 23  fload
	DCD handler_unused;DCD handler_dload	        ; 24  dload
	DCD handler_loadsingle      ; 25  aload
	DCD handler_loadsingle0	    ; 26  iload0
	DCD handler_loadsingle1     ; 27  iload1
	DCD handler_loadsingle2	    ; 28  iload2
	DCD handler_loadsingle3	    ; 29  iload3
	DCD handler_unused;DCD handler_lload0		    ; 30  lload0
	DCD handler_unused;DCD handler_lload1	  	    ; 31  lload1
	DCD handler_unused;DCD handler_lload2		    ; 32  lload2
	DCD handler_unused;DCD handler_lload3		    ; 33  lload3
	DCD handler_loadsingle0		; 34  fload0
	DCD handler_loadsingle1		; 35  fload1
	DCD handler_loadsingle2		; 36  fload2
	DCD handler_loadsingle3		; 37  fload3
	DCD handler_unused;DCD handler_dload0			; 38  dload0
	DCD handler_unused;DCD handler_dload1			; 39  dload1
	DCD handler_unused;DCD handler_dload2			; 40  dload2
	DCD handler_unused;DCD handler_dload3			; 41  dload3
	DCD handler_loadsingle0		; 42  aload0
	DCD handler_loadsingle1		; 43  aload1
	DCD handler_loadsingle2		; 44  aload2
	DCD handler_loadsingle3		; 45  aload3

	; array loads
	DCD handler_arrayloadsingle	; 46  iaload
	DCD handler_unused;DCD handler_laload			; 47
	DCD handler_unused;DCD handler_faload			; 48
	DCD handler_unused;DCD handler_daload			; 49
	DCD handler_arrayloadsingle ; 50 aaload
	DCD handler_arrayloadbyte   ; 51 baload
	DCD handler_arrayloadchar	; 52 caload
	DCD handler_arrayloadshort	; 53 saload

	;---------------------------------------------------
	; Pop a value from the operand stack (store in local var)
	;---------------------------------------------------

	DCD handler_storesingle 	; 54 istore			
	DCD handler_unused;DCD handler_lstore			; 55
	DCD handler_storesingle		; 56 fstore
	DCD handler_unused;DCD handler_dstore			; 57
	DCD handler_storesingle 	; 58 astore			

	DCD handler_storesingle0	; 59 istore0			
	DCD handler_storesingle1	; 60 istore1		
	DCD handler_storesingle2	; 61 istore2		
	DCD handler_storesingle3	; 62 istore3		
	DCD handler_unused;DCD handler_lstore0			; 63
	DCD handler_unused;DCD handler_lstore1			; 64
	DCD handler_unused;DCD handler_lstore2			; 65
	DCD handler_unused;DCD handler_lstore3			; 66

	DCD handler_storesingle0	; 67 fstore0
	DCD handler_storesingle1	; 68 fstore1
	DCD handler_storesingle2	; 69 fstore2
	DCD handler_storesingle3	; 70 fstore3
	DCD handler_unused;DCD handler_dstore0			; 71
	DCD handler_unused;DCD handler_dstore1			; 72
	DCD handler_unused;DCD handler_dstore2			; 73
	DCD handler_unused;DCD handler_dstore3			; 74
	DCD handler_storesingle0 	; 75 astore0	
	DCD handler_storesingle1 	; 76 astore1			
	DCD handler_storesingle2 	; 77 astore2			
	DCD handler_storesingle3 	; 78 astore3			

	DCD handler_arraystoresingle ; 79 iastore
	DCD handler_unused 	 	     ; 80 lastore
	DCD handler_unused;DCD handler_fastore			; 81
	DCD handler_unused;DCD handler_dastore			; 82
	DCD handler_arraystoresingle ; 83 aastore		
	DCD handler_arraystorebyte 	 ; 84 bastore
	DCD handler_arraystorebyte	 ; 85 castore
	DCD handler_arraystoreshort  ; 86 sastore

	;---------------------------------------------------
	; Operand stack management
	;---------------------------------------------------

	DCD handler_pop				; 87
	DCD handler_unused;DCD handler_pop2			; 88
	DCD handler_dup				; 89
	DCD handler_dupx1			; 90
	DCD handler_unused;DCD handler_dupx2			; 91
	DCD handler_unused;DCD handler_dup2			; 92
	DCD handler_unused;DCD handler_dup2x1			; 93
	DCD handler_unused;DCD handler_dup2x2			; 94
	DCD handler_unused;DCD handler_swap			; 95

	;---------------------------------------------------
	; Computational operations
	;---------------------------------------------------

	DCD handler_iadd			; 96
	DCD handler_unused;DCD handler_ladd			; 97
	DCD handler_unused;DCD handler_fadd			; 98
	DCD handler_unused;DCD handler_dadd			; 99
	DCD handler_isub			; 100
	DCD handler_unused;DCD handler_lsub			; 101
	DCD handler_unused;DCD handler_fsub			; 102
	DCD handler_unused;DCD handler_dsub			; 103
	DCD handler_imul			; 104
	DCD handler_unused;DCD handler_lmul			; 105
	DCD handler_unused;DCD handler_fmul			; 106
	DCD handler_unused;DCD handler_dmul			; 107
	DCD handler_idiv			; 108
	DCD handler_unused;DCD handler_ldiv			; 109
	DCD handler_unused;DCD handler_fdiv			; 110
	DCD handler_unused;DCD handler_ddiv			; 111
	DCD handler_irem			; 112
	DCD handler_unused;DCD handler_lrem			; 113
	DCD handler_unused;DCD handler_frem			; 114
	DCD handler_unused;DCD handler_drem			; 115
	DCD handler_ineg			; 116
	DCD handler_unused;DCD handler_lneg			; 117
	DCD handler_unused;DCD handler_fneg			; 118
	DCD handler_unused;DCD handler_dneg			; 119
	DCD handler_ishl			; 120
	DCD handler_unused;DCD handler_lshl			; 121
	DCD handler_ishr			; 122
	DCD handler_unused;DCD handler_lshr			; 123
	DCD handler_iushr			; 124
	DCD handler_unused;DCD handler_lushr			; 125
	DCD handler_iand			; 126
	DCD handler_unused;DCD handler_land			; 127
	DCD handler_ior				; 128
	DCD handler_unused;DCD handler_lor				; 129
	DCD handler_ixor			; 130
	DCD handler_unused;DCD handler_lxor			; 131
	DCD handler_iinc			; 132

	; conversion
	DCD handler_unused;DCD handler_i2l				; 133
	DCD handler_unused;DCD handler_i2f				; 134
	DCD handler_unused;DCD handler_i2d				; 135
	DCD handler_unused;DCD handler_l2i				; 136
	DCD handler_unused;DCD handler_l2f				; 137
	DCD handler_unused;DCD handler_l2d				; 138
	DCD handler_unused;DCD handler_f2i				; 139
	DCD handler_unused;DCD handler_f2l				; 140
	DCD handler_unused;DCD handler_f2d				; 141
	DCD handler_unused;DCD handler_d2i				; 142
	DCD handler_unused;DCD handler_d2l				; 143
	DCD handler_unused;DCD handler_d2f				; 144
	DCD handler_i2b				; 145 i2b
	DCD handler_i2c				; 146 i2c
	DCD handler_i2s				; 147 i2s

	; comparison operations
	DCD handler_unused;DCD handler_lcmp			; 148
	DCD handler_unused;DCD handler_fcmpl			; 149
	DCD handler_unused;DCD handler_fcmpg			; 150
	DCD handler_unused;DCD handler_dcmpl			; 151
	DCD handler_unused;DCD handler_dcmpg			; 152

	DCD handler_ifeq			; 153
	DCD handler_ifne			; 154
	DCD handler_iflt			; 155
	DCD handler_ifge			; 156
	DCD handler_ifgt			; 157
	DCD handler_ifle			; 158

	DCD handler_ificmpeq		; 159
	DCD handler_ificmpne		; 160
	DCD handler_ificmplt		; 161
	DCD handler_ificmpge		; 162
	DCD handler_ificmpgt		; 163
	DCD handler_ificmple		; 164

	DCD handler_ificmpeq		; 165 if_acmpeq (treated same as ints)
	DCD handler_ificmpne		; 166 if_acmpne (treated same as ints)

	; branch
	DCD handler_goto			; 167
	DCD handler_unused;DCD handler_jsr				; 168
	DCD handler_unused;DCD handler_ret				; 169

	; switch
	DCD handler_unused;DCD handler_tableswitch		; 170
	DCD handler_unused;DCD handler_lookupswitch	; 171

	; return									
	DCD handler_returnsingle 	; 172 ireturn			
	DCD handler_unused;DCD handler_lreturn			; 173
	DCD handler_unused;DCD handler_freturn			; 174
	DCD handler_unused;DCD handler_dreturn			; 175
	DCD handler_returnsingle	; 176 areturn
	DCD handler_returnvoid		; 177 return

	DCD handler_unused;DCD handler_getstatic		; 178
	DCD handler_unused;DCD handler_putstatic		; 179
	DCD handler_getfield		; 180
	DCD handler_putfield		; 181

	DCD handler_invokevirtual	; 182
	DCD handler_invokespecial	; 183
	DCD handler_invokestatic	; 184
	DCD handler_unused;DCD handler_invokeinterface	; 185
	DCD handler_unused;DCD handler_unused			; 186 --- NOT USED ---

	DCD handler_new				; 187
	DCD handler_newarray		; 188
	DCD handler_anewarray		; 189
	DCD handler_arraylength		; 190
	DCD handler_unused;DCD handler_athrow			; 191
	DCD handler_checkcast		; 192
	DCD handler_unused;DCD handler_instanceof		; 193
	DCD handler_unused;DCD handler_monitorenter	; 194
	DCD handler_unused;DCD handler_monitorexit		; 195
	DCD handler_unused;DCD handler_wide			; 196
	DCD handler_unused;DCD handler_multianewarray	; 197
	DCD handler_ifnull			; 198
	DCD handler_ifnonnull		; 199

	; wide branches
	DCD handler_unused;DCD handler_gotow			; 200
	DCD handler_unused;DCD handler_jsrw 		    ; 201
	
	; reserved opcodes
	DCD handler_unused			; 202 breakpoint (unused)
	DCD handler_unused			; 203
	DCD handler_unused			; 204
	DCD handler_unused			; 205
	DCD handler_unused			; 206
	DCD handler_unused			; 207
	DCD handler_unused			; 208
	DCD handler_unused			; 209
	DCD handler_unused			; 210
	DCD handler_unused			; 211
	DCD handler_unused			; 212
	DCD handler_unused			; 213
	DCD handler_unused			; 214
	DCD handler_unused			; 215
	DCD handler_unused			; 216
	DCD handler_unused			; 217
	DCD handler_unused			; 218
	DCD handler_unused			; 219
	DCD handler_unused			; 220
	DCD handler_unused			; 221
	DCD handler_unused			; 222
	DCD handler_unused			; 223
	DCD handler_unused			; 224
	DCD handler_unused			; 225
	DCD handler_unused			; 226
	DCD handler_unused			; 227
	DCD handler_unused			; 228
	DCD handler_unused			; 229
	DCD handler_unused			; 230
	DCD handler_unused			; 231
	DCD handler_unused			; 232
	DCD handler_unused			; 233
	DCD handler_unused			; 234
	DCD handler_unused			; 235
	DCD handler_unused			; 236
	DCD handler_unused			; 237
	DCD handler_unused			; 238
	DCD handler_unused			; 239
	DCD handler_unused			; 240
	DCD handler_unused			; 241
	DCD handler_unused			; 242
	DCD handler_unused			; 243
	DCD handler_unused			; 244
	DCD handler_unused			; 245
	DCD handler_unused			; 246
	DCD handler_unused			; 247
	DCD handler_unused			; 248
	DCD handler_unused			; 249
	DCD handler_unused			; 250
	DCD handler_unused			; 251
	DCD handler_unused			; 252
	DCD handler_unused			; 253
	

	; 'native' opcodes specifically for this VM
	DCD handler_lang			; 254
	DCD handler_peripheral		; 255




;---------------------------------------------------
;---------------------------------------------------

	IMPORT handler_lang_tstart
	IMPORT handler_lang_tyield 
	IMPORT handler_lang_tsleep

; The 'lang' byte code handler, which is in turn another
; jump table.
handler_lang

	; Load the next byte after 'lang' which is the custom opcode 
	; of a 'lang' type instruction.
	LDRB value, [vmpc], #1  ; vmpc++

	ADR value1, lang_jump_table ; get address of jump table into a register
	LDR pc, [value1, value, LSL#2] ; jump to jumptable[opcode].

	ALIGN

lang_jump_table

	DCD handler_unused			; nop 0
	DCD handler_lang_tstart  	; 1
	DCD handler_lang_tyield  	; 2
	DCD handler_lang_tsleep  	; 3
	DCD handler_unused;DCD handler_lang_intstart; 10 ?


;---------------------------------------------------
;---------------------------------------------------

	IMPORT handler_peripheral_display_drawstring
	IMPORT handler_peripheral_display_drawint
	IMPORT handler_peripheral_mem_write

; The 'peripheral' byte code handler, which is in turn another
; jump table.
handler_peripheral

	; Load the next byte after 'lang' which is the custom opcode 
	; of a 'lang' type instruction.
	LDRB value, [vmpc], #1  ; vmpc++

	ADR value1, peripheral_jump_table ; get address of jump table into a register
	LDR pc, [value1, value, LSL#2] ; jump to jumptable[opcode].

	ALIGN

peripheral_jump_table

	DCD handler_unused			                ; nop 0
	DCD handler_peripheral_display_drawstring  	; 1
	DCD handler_peripheral_display_drawint		; 2	
	DCD handler_unused;DCD handler_peripheral_mem_read				; 3
	DCD handler_peripheral_mem_write			; 4


		




				  


	




	BX lr  ; return
	END