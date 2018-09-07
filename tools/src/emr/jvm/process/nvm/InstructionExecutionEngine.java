/*
 * InstructionExecutionEngine.java
 *
 * Created on January 18, 2007, 8:46 PM
 *
 * The Instruction Execution Engine (IEE) is a module that organizes fetches of instructions and operands
 * from NVM and presents them to the ProcessManager for handling.
 *
 * The IEE fetches instructions from the NVM at the address of the PC.  After the instructions are fetched,
 * a number of operands are also fetched.  During instruction retrieval, the NVM is read in byte mode.
 * Thus, the first 8 bits of the word read from NVM is the opcode.
 *
 *
 */

package emr.jvm.process.nvm;

import emr.jvm.JVMRuntime;
import emr.jvm.OpcodeMnemonics;
import emr.jvm.Debug;
import emr.elements.classfileparser.code.Instruction;
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.ram.Array;
import emr.jvm.visualization.IPSUpdater;

import emr.jvm.process.*;
    

/**
 *
 * @author Evan Ross
 */
public class InstructionExecutionEngine extends JVMProcess implements OpcodeMnemonics
{
    
    static boolean paused = true;
    
    /** Creates a new instance of InstructionExecutionEngine */
    public InstructionExecutionEngine()
    {
        super("InstructionExecutionEngine");
    }
    
    @Override
    public void runProcess()
    {        
        // Debugging: if an instruction is handled, then this boolean
        // is set true.  Otherwise, a debug message is displayed
        boolean handled = false;
            
        // get the opcode
        JVMRuntime.opcode = MemoryController.readByte( JVMRuntime.pc ) & 0xff;
        
        debug("processing << " + Instruction.getInstructionName(JVMRuntime.opcode) + " >>");
        IPSUpdater.incInstructionCount(); // track instructions per second

        if(instructionStep) 
            step(); // enable instruction level stepping.
        
        checkStatus();
        
        // increment pc past opcode by default
        JVMRuntime.pc += 1;
            
            // perform instruction based on opcode
            switch (JVMRuntime.opcode)
            {
                //---------------------------------------------------
                //     A
                // --------------------------------------------------
                
                case 50: // aaload
                    handled = true;
                    InstructionHandlers.ArrayGetSingle();
                    break;
                case 83: // aastore
                    handled = true;
                    InstructionHandlers.ArrayPutSingle();
                    break;
                case 1:  // aconst_null
                    handled = true;
                    JVMRuntime.value = JVMRuntime.nullregister;
                    InstructionHandlers.StackPushSingle();
                    break;
                    
                case 25:  // aload
                    // Function:
                    //    push reference at local variable[ NVM[pc] ] onto operand stack    
                    // Notes:
                    //    pc is incremented by 1 then value read is the index into the variable array
                    handled = true;
                    // get the 1-byte index
                    JVMRuntime.index = MemoryController.readByte( JVMRuntime.pc );
                    JVMRuntime.pc = ( JVMRuntime.pc + 1 );
                    InstructionHandlers.LoadSingle();

                    break;
                    
                case 42:  // aload_0
                    // Function:
                    //    push reference at local variable[0] onto operand stack    
                    // Notes:
                    //    For instance methods, the reference is to 'this'.
                    handled = true;
                    JVMRuntime.index = 0;
                    InstructionHandlers.LoadSingle();

                    break;

                case 43:  // aload_1
                    // Function:
                    //    push reference at local variable[1] onto operand stack    
                    // Notes:
                    handled = true;
                    JVMRuntime.index = 1;
                    InstructionHandlers.LoadSingle();

                    break;
                case 44:  // aload_2
                    // Function:
                    //    push reference at local variable[2] onto operand stack    
                    // Notes:
                    handled = true;
                    JVMRuntime.index = 2;
                    InstructionHandlers.LoadSingle();

                    break;
                case 45:  // aload_3
                    // Function:
                    //    push reference at local variable[3] onto operand stack    
                    // Notes:
                    handled = true;
                    JVMRuntime.index = 3;
                    InstructionHandlers.LoadSingle();
                    
                    break;
                case 189:  // anewarray
                    handled = true;
                    // We don't resolve the object type.  Skip the 2 byte operand.
                    JVMRuntime.pc = ( JVMRuntime.pc + 2 );
                    // Set INDEX = 12 for 'ref' type (32bits).
                    JVMRuntime.index = 12;
                    InstructionHandlers.NewArrayProcess();
                    break;
                case 176:  // areturn
                
                    handled = true;
                    InstructionHandlers.ReturnSingle();
                    break;
                 
                case 190:  // arraylength
                    handled = true;
                    InstructionHandlers.ArrayLengthGet();
                    break;
                case 58:  // astore
                    handled = true;
                    // get the 1-byte index
                    JVMRuntime.index = MemoryController.readByte( JVMRuntime.pc );
                    JVMRuntime.pc = ( JVMRuntime.pc + 1 );
                    InstructionHandlers.StoreSingle();
                    break;
                case 75:  // astore_0
                    handled = true;
                    JVMRuntime.index = 0;
                    InstructionHandlers.StoreSingle();
                    
                    break;
                case 76: // astore_1
                    handled = true;
                    JVMRuntime.index = 1;
                    InstructionHandlers.StoreSingle();

                    break;
                case 77:  // astore_2
                    handled = true;
                    JVMRuntime.index = 2;
                    InstructionHandlers.StoreSingle();

                    break;
                case 78:  // astore_3
                    handled = true;
                    JVMRuntime.index = 3;
                    InstructionHandlers.StoreSingle();


                    break;
                    
                case 191:  // athrow

                    break;
                    
                //---------------------------------------------------
                //     B
                // --------------------------------------------------
                    
                case 51:  // baload
                    // Function:
                    //    Load byte or boolean from array
                    // Notes:
                    //    Handles boolean type as well
                    handled = true;
                    InstructionHandlers.ArrayGetByte();
                    break;
                case 84:  // bastore
                    handled = true;
                    InstructionHandlers.ArrayPutByte();
                    break;
                case 16:  // bipush
                    handled = true;
                    // get the immediate byte
                    JVMRuntime.value = MemoryController.readByte(JVMRuntime.pc);
                    JVMRuntime.pc += 1;
                    // push it onto the stack
                    InstructionHandlers.StackPushSingle();
                    //Debug.message("                  bipush " + value);
                    break;
                    
                //---------------------------------------------------
                //     C
                // --------------------------------------------------
                    
                case 52:  // caload
                    // Function:
                    //    Load char from array
                    // Notes:
                    //    chars are 8-bit unsigned
                    handled = true;
                    InstructionHandlers.ArrayGetChar();
                    break;
                case 85:  // castore
                    handled = true;
                    InstructionHandlers.ArrayPutChar();
                    break;
                case 192: // checkcast
                    // Not implemented, unconditionally passes.
                    // Requires that Collections be paramaterized in the IDE
                    // (e.g. Vector<SomeObject>) to enforce type checking
                    // at compile-time since it won't be checked here.
                    // Must increment PC by 2 bytes to pass 'index'.
                    JVMRuntime.pc += 2;
                    handled = true;
                    debug("Unconditional checkcast used.");
                    break; 
                //---------------------------------------------------
                //     D
                // --------------------------------------------------
                
                case 144:  // d2f
                    break;
                case 142:  // d2i
                    break;
                case 143:  // d2l
                    break;
                case 99:  // dadd
                    handled = true;
                    InstructionHandlers.AddDoubleFloat();
                    break;
                case 49:  // daload
                    break;
                case 82:  // dastore
                    break;
                case 152:  // dcmpg
                    break;
                case 151:  // dcmpl
                    break;
                case 14:  // dconst_0
                    break;
                case 15:  // dconst_1
                    break;
                case 111:  // ddiv
                    handled = true;
                    InstructionHandlers.DivDoubleFloat();
                    break;
                case 24:  // dload
                    break;
                case 38:  // dload_0
                    break;
                case 39:  // dload_1
                    break;
                case 40:  // dload_2
                    break;
                case 41:  // dload_3
                    break;
                case 107:  // dmul
                    handled = true;
                    InstructionHandlers.MultDoubleFloat();
                    break;
                case 119:  // dneg
                    break;
                case 115:  // drem
                    break;
                case 175:  // dreturn =
                    break;
                case 57:  // dstore
                    break;
                case 71:  // dstore_0
                    break;
                case 72:  // dstore_1
                    break;
                case 73:  // dstore_2
                    break;
                case 74:  // dstore_3
                    break;
                case 103:  // dsub
                    handled = true;
                    InstructionHandlers.SubDoubleFloat();
                    break;
                case 89:  // dup
                    handled = true;
                    
                    // Request the stack duplicate single process
                    InstructionHandlers.StackDuplicateSingle();
                    //Debug.message("                  dup " + value);
                    break;
                case 90:  // dup_x1
                    handled = true;
                    InstructionHandlers.StackDuplicateXSingle();
                    break;
                case 91:  // dup_x2
                    break;
                case 92:  // dup2
                    break;
                case 93:  // dup2_x1
                    break;
                case 94:  // dup2_x2
                    break;
                    
                //---------------------------------------------------
                //     F
                // --------------------------------------------------

                case 141:  // f2d
                    break;
                case 139:  // f2i
                    handled = true;
                    InstructionHandlers.FloatToInt();
                    break;
                case 140:  // f2l
                    break;
                case 98:  // fadd
                    handled = true;
                    InstructionHandlers.AddSingleFloat();
                   break;
                case 48:  // faload
                    handled = true;
                    InstructionHandlers.ArrayGetSingle();
                    break;
                case 81: // fastore
                    handled = true;
                    InstructionHandlers.ArrayPutSingle();
                    break;
                case 150:  // fcmpg
                    handled = true;
                    InstructionHandlers.BranchCompareFloatGT();
                    break;
                case 149:  // fcmpl
                    handled = true;
                    InstructionHandlers.BranchCompareFloatLT();
                    break;
                case 11:  // fconst_0
                    handled = true;
                    JVMRuntime.value = 0x0; // Float.floatToIntBits = 0x0
                    InstructionHandlers.StackPushSingle();
                    break;
                case 12:  // fconst_1
                    handled = true;
                    JVMRuntime.value = 0x3f800000; // Float.floatToIntBits = 0x3f800000
                    InstructionHandlers.StackPushSingle();
                    break;
                case 13:  // fconst_2
                    handled = true;
                    JVMRuntime.value = 0x40000000; // Float.floatToIntBits = 0x40000000
                    InstructionHandlers.StackPushSingle();
                    break;
                case 110:  // fdiv
                    handled = true;
                    InstructionHandlers.DivSingleFloat();
                    break;
                case 23:  // fload
                    handled = true;
                    // get the 1-byte index
                    JVMRuntime.index = MemoryController.readByte(JVMRuntime.pc);
                    JVMRuntime.pc = JVMRuntime.pc + 1;
                    InstructionHandlers.LoadSingle();
                    break;

                case 34:  // fload_0
                    handled = true;
                    JVMRuntime.index = 0;
                    InstructionHandlers.LoadSingle();
                    break;
                case 35:  // fload_1
                    handled = true;
                    JVMRuntime.index = 1;
                    InstructionHandlers.LoadSingle();
                    break;
                case 36:  // fload_2
                    handled = true;
                    JVMRuntime.index = 2;
                    InstructionHandlers.LoadSingle();
                    break;
                case 37:  // fload_3
                    handled = true;
                    JVMRuntime.index = 3;
                    InstructionHandlers.LoadSingle();
                    break;
                case 106:  // fmul
                    handled = true;
                    InstructionHandlers.MultSingleFloat();
                    break;
                case 118:  // fneg
                    handled = true;
                    InstructionHandlers.NegateFloat();
                    break;
                case 114:  // frem
                    break;
                case 174:  // freturn
                    //handled = true;
                    //returnSingleProcess();
                    break;
                    
                case 56:  // fstore
                    handled = true;
                    JVMRuntime.index = MemoryController.readByte(JVMRuntime.pc); // index
                    JVMRuntime.pc = JVMRuntime.pc + 1;
                    InstructionHandlers.StoreSingle();
                    break;
                case 67:  // fstore_0
                    handled = true;
                    JVMRuntime.index = 0;
                    InstructionHandlers.StoreSingle();
                    break;
                case 68:  // fstore_1
                    handled = true;
                    JVMRuntime.index = 1;
                    InstructionHandlers.StoreSingle();
                    break;
                case 69:  // fstore_2
                    handled = true;
                    JVMRuntime.index = 2;
                    InstructionHandlers.StoreSingle();
                    break;
                case 70:  // fstore_3
                    handled = true;
                    JVMRuntime.index = 3;
                    InstructionHandlers.StoreSingle();
                    break;
                case 102:  // fsub
                    handled = true;
                    InstructionHandlers.SubSingleFloat();
                   break;
                    
                    
                //---------------------------------------------------
                //     G
                // --------------------------------------------------

                case 180:  // getfield
                    handled = true;
                    // Get the Index operand and place into INDEX register
                    JVMRuntime.index = MemoryController.readShort( JVMRuntime.pc );
                    JVMRuntime.pc = ( JVMRuntime.pc + 2 );
                    
                    // Request process from ProcessManager
                    InstructionHandlers.GetField();
                   
                    break;
                case 178:  // getstatic
                    break;
                case 167:  // goto
                    handled = true;
                    JVMRuntime.value = MemoryController.readShort( JVMRuntime.pc );
                    JVMRuntime.pc = JVMRuntime.pc - 1 + JVMRuntime.value;  // pc already inc by 1
                    // size of instruction is 3 but pc is only already incremented by 1 so subtract 1
                    break;
                case 200:  // goto_w
                    break;
                    
                //---------------------------------------------------
                //     I
                // --------------------------------------------------
 
                case 145:  // i2b
                    // Function:
                    //    Convert int to byte
                    // Notes:
                    //    The int is truncated to 8 bits and sign extended.
                    handled = true;
                    InstructionHandlers.IntToByte();
                    break;
                case 146:  // i2c
                    // Function:
                    //    Convert int to char
                    // Notes:
                    //    The int is truncated to 8 bits and zero extended (result is always positive)
                    handled = true;
                    InstructionHandlers.IntToChar();
                    break;
                case 135:  // i2d
                    break;
                case 134:  // i2f
                    handled = true;
                    InstructionHandlers.IntToFloat();
                    break;
                case 133:  // i2l
                    break;
                case 147:  // i2s
                    // Function:
                    //    Convert int to short
                    // Notes:
                    //    The int is truncated to 16 bits and sign extended.
                    handled = true;
                    InstructionHandlers.IntToShort();
                    break;
                case 96:  // iadd
                    handled = true;
                    InstructionHandlers.AddSingleInt();
                    break;
                case 46:  // iaload
                    handled = true;
                    InstructionHandlers.ArrayGetSingle();
                    break;
                case 126:  // iand
                    handled = true;
                    InstructionHandlers.AndInt();
                    break;
                case 79:  // iastore
                    handled = true;
                    InstructionHandlers.ArrayPutSingle();
                    break;
                case 2:  // iconst_m1
                    handled = true;
                    JVMRuntime.value = -1;
                    InstructionHandlers.ConstStoreSingle();
                    break;
                case 3:  // iconst_0
                    handled = true;
                    JVMRuntime.value = 0;
                    InstructionHandlers.ConstStoreSingle();
                    break;
                case 4:  // iconst_1
                    handled = true;
                    JVMRuntime.value = 1;
                    InstructionHandlers.ConstStoreSingle();
                    break;
                case 5:  // iconst_2
                    handled = true;
                    JVMRuntime.value = 2;
                    InstructionHandlers.ConstStoreSingle();
                    break;
                case 6:  // iconst_3
                    handled = true;
                    JVMRuntime.value = 3;
                    InstructionHandlers.ConstStoreSingle();
                    break;
                case 7:  // iconst_4
                    handled = true;
                    JVMRuntime.value = 4;
                    InstructionHandlers.ConstStoreSingle();
                    break;
                case 8:  // iconst_5
                    handled = true;
                    JVMRuntime.value = 5;
                    InstructionHandlers.ConstStoreSingle();
                    break;
                case 108:  // idiv
                    handled = true;
                    InstructionHandlers.DivSingleInt();
                    break;
                case 165:  // if_acmpeq 
                    handled = true;
                    // Get the branch offset
                    JVMRuntime.index = MemoryController.readShort(JVMRuntime.pc);
                    JVMRuntime.pc = (JVMRuntime.pc + 2);
                    InstructionHandlers.BranchCompareSingleIntEQ();
                    
                    break;
                case 166:  // if_acmpne   
                    handled = true;
                    // Get the branch offset
                    JVMRuntime.index = MemoryController.readShort(JVMRuntime.pc);
                    JVMRuntime.pc = (JVMRuntime.pc + 2);
                    InstructionHandlers.BranchCompareSingleIntNE();
                    break;
                case 159:  // if_icmpeq
                    handled = true;
                    // Get the branch offset
                    JVMRuntime.index = MemoryController.readShort( JVMRuntime.pc );
                    JVMRuntime.pc = ( JVMRuntime.pc + 2 );
                    InstructionHandlers.BranchCompareSingleIntEQ();
                    break;
                case 160:  // if_icmpne
                    handled = true;
                    // Get the branch offset
                    JVMRuntime.index = MemoryController.readShort( JVMRuntime.pc );
                    JVMRuntime.pc = ( JVMRuntime.pc + 2 );
                    InstructionHandlers.BranchCompareSingleIntNE();
                    
                    break;
                case 161:  // if_icmplt
                    handled = true;
                    // Get the branch offset
                    JVMRuntime.index = MemoryController.readShort( JVMRuntime.pc );
                    JVMRuntime.pc = ( JVMRuntime.pc + 2 );
                    InstructionHandlers.BranchCompareSingleIntLT();
                    
                    break;
                case 162:  // if_icmpge
                    handled = true;
                    // Get the branch offset
                    JVMRuntime.index = MemoryController.readShort( JVMRuntime.pc );
                    JVMRuntime.pc = ( JVMRuntime.pc + 2 );
                    InstructionHandlers.BranchCompareSingleIntGE();
                    break;
                case 163:  // if_icmpgt
                    handled = true;
                    // Get the branch offset
                    JVMRuntime.index = MemoryController.readShort( JVMRuntime.pc );
                    JVMRuntime.pc = ( JVMRuntime.pc + 2 );
                    InstructionHandlers.BranchCompareSingleIntGT();
                    
                    break;
                case 164:  // if_icmple
                    handled = true;
                    // Get the branch offset
                    JVMRuntime.index = MemoryController.readShort( JVMRuntime.pc );
                    JVMRuntime.pc = ( JVMRuntime.pc + 2 );
                    InstructionHandlers.BranchCompareSingleIntLE();
                    
                    break;
                case 153:  // ifeq
                    handled = true;
                    JVMRuntime.index = MemoryController.readShort( JVMRuntime.pc );
                    JVMRuntime.pc = ( JVMRuntime.pc + 2 );
                    InstructionHandlers.BranchCompareSingleIntEQZero();
                    
                    break;
                case 154:  // ifne
                    handled = true;
                    JVMRuntime.index = MemoryController.readShort(JVMRuntime.pc);
                    JVMRuntime.pc = (JVMRuntime.pc + 2);
                    InstructionHandlers.BranchCompareSingleIntNEZero();
                    break;
                case 155:  // iflt
                    handled = true;
                    JVMRuntime.index = MemoryController.readShort(JVMRuntime.pc);
                    JVMRuntime.pc = (JVMRuntime.pc + 2);
                    InstructionHandlers.BranchCompareSingleIntLTZero();
                    break;
                case 156:  // ifge
                    handled = true;
                    // Get the 2-byte branch offset
                    JVMRuntime.index = MemoryController.readShort( JVMRuntime.pc );
                    JVMRuntime.pc = ( JVMRuntime.pc + 2 );
                    InstructionHandlers.BranchCompareSingleIntGEZero();
                    //intBranchCompare( COMPARE_Z_GE, compute2ByteValue() );
                    break;
                case 157:  // ifgt
                    handled = true;
                    JVMRuntime.index = MemoryController.readShort(JVMRuntime.pc);
                    JVMRuntime.pc = (JVMRuntime.pc + 2);
                    InstructionHandlers.BranchCompareSingleIntGTZero();
                    break;
                case 158:  // ifle
                    handled = true;
                    JVMRuntime.index = MemoryController.readShort(JVMRuntime.pc);
                    JVMRuntime.pc = (JVMRuntime.pc + 2);
                    InstructionHandlers.BranchCompareSingleIntLEZero();
                    break;
                case 199:  // ifnonnull
                    handled = true;
                    // Get the branch offset
                    JVMRuntime.index = MemoryController.readShort(JVMRuntime.pc);
                    JVMRuntime.pc = (JVMRuntime.pc + 2);
                    InstructionHandlers.BranchCompareSingleIntNENull();
                    break;
                case 198: // ifnull
                    handled = true;
                    // Get the branch offset
                    JVMRuntime.index = MemoryController.readShort(JVMRuntime.pc);
                    JVMRuntime.pc = (JVMRuntime.pc + 2);
                    InstructionHandlers.BranchCompareSingleIntEQNull();
                    break;
                case 132:  // iinc
                    handled = true;
                    // next byte is index
                    JVMRuntime.index = MemoryController.readByte( JVMRuntime.pc );
                    JVMRuntime.pc ++;
                    // next byte is constant to increment by.  value2 gets the local variable
                    JVMRuntime.value1 = MemoryController.readByte( JVMRuntime.pc );
                    JVMRuntime.pc ++;

                                
                    //ProcessManager.setPCW( ProcessManager.INCREMENT_SINGLE_INT );
                    InstructionHandlers.IncrementSingleInt();
                    break;

                case 21:  // iload
                    handled = true;
                    // get the 1-byte index
                    JVMRuntime.index = MemoryController.readByte(JVMRuntime.pc);
                    JVMRuntime.pc = JVMRuntime.pc + 1;
                    InstructionHandlers.LoadSingle();
                    break;
                    
                case 26:  // iload_0
                    handled = true;
                    JVMRuntime.index = 0;
                    InstructionHandlers.LoadSingle();
                    break;
                    
                case 27:  // iload_1
                    handled = true;
                    JVMRuntime.index = 1;
                    InstructionHandlers.LoadSingle();
                    break;
                    
                case 28:  // iload_2
                    handled = true;
                    JVMRuntime.index = 2;
                    InstructionHandlers.LoadSingle();
                    break;
                    
                case 29:  // iload_3
                    handled = true;
                    JVMRuntime.index = 3;
                    InstructionHandlers.LoadSingle();
                    break;
                    
                case 104:  // imul
                    handled = true;
                    InstructionHandlers.MultSingleInt();

                    break;
                case 116:  // ineg
                    handled = true;
                    InstructionHandlers.NegateInt();
                    break;
                case 193:  // instanceof
                    break;
                case 185:  // invokeinterface
                    // Function:
                    //    Invoke interface method
                    // Notes:
                    //    Interface methods use the same process as virtual methods.
                    //    If the count operand is used, then the initial lookup to
                    //    the interface, and consequently the Interface itself, can
                    //    be eliminated.  This is not implemented yet.
                    handled = true;

                    JVMRuntime.index = MemoryController.readShort( JVMRuntime.pc );
                    JVMRuntime.pc = ( JVMRuntime.pc + 4 ); // count and reserved operands also
                    InstructionHandlers.InvokeVirtual();
                    break;
                case 183:  // invokespecial
                    // Function:
                    //    Invoke instance initialization method (constructor)
                    // Notes:
                    handled = true;
                    // get the 2-byte index
                    JVMRuntime.index = MemoryController.readShort( JVMRuntime.pc );
                    JVMRuntime.pc = ( JVMRuntime.pc + 2 );
                    
                    InstructionHandlers.InvokeSpecial();

                    //index = compute2ByteIndex();
                    //InvokeSpecialProcess(index);
                    break;
                    
                case 184:  // invokestatic
                    // Function:
                    //    Invoke static method
                    // Notes:
                    handled = true;
                    // get the 2-byte index
                    JVMRuntime.index = MemoryController.readShort( JVMRuntime.pc );
                    JVMRuntime.pc = ( JVMRuntime.pc + 2 );
                    InstructionHandlers.InvokeStatic();
                    
                    break;
                case 182:  // invokevirtual
                    // Function:
                    //    Invoke instance method
                    // Notes:
                    handled = true;
                    // get the 2-byte index
                    JVMRuntime.index = MemoryController.readShort( JVMRuntime.pc );
                    JVMRuntime.pc = ( JVMRuntime.pc + 2 );
                    InstructionHandlers.InvokeVirtual();
                    break;
                    
                case 128:  // ior
                    handled = true;
                    InstructionHandlers.OrInt();
                    break;
                case 112:  // irem
                    // Function:
                    //    Remainder of a / b
                    // Notes:
                    //    Implements % operator
                    InstructionHandlers.RemainderInt();
                    handled = true;
                    
                    break;
                case 172:  // ireturn
                    handled = true;
                    InstructionHandlers.ReturnSingle();
                    break;
                    
                    
                case 120:  // ishl
                    handled = true;
                    InstructionHandlers.ShiftLeftInt();
                    break;
                case 122:  // ishr
                    handled = true;
                    InstructionHandlers.ShiftRightInt();
                    break;
                case 54:  // istore
                    handled = true;
                    // get the 1-byte index
                    JVMRuntime.index = MemoryController.readByte( JVMRuntime.pc );
                    JVMRuntime.pc = ( JVMRuntime.pc + 1 );
                    InstructionHandlers.StoreSingle();
                    break;
                    
                case 59: // istore_0
                    handled = true;
                    
                    // Set register INDEX to 0
                    JVMRuntime.index = 0;
                    InstructionHandlers.StoreSingle();
                    
                    break;
                case 60: // istore_1
                    handled = true;
                    
                    // Set register INDEX to 0
                    JVMRuntime.index = 1;
                    InstructionHandlers.StoreSingle();
                    break;
                    
                case 61: // istore_2
                    handled = true;
                    
                    // Set register INDEX to 0
                    JVMRuntime.index = 2;
                    InstructionHandlers.StoreSingle();
                    break;
                    
                case 62: // istore_3
                    handled = true;
                    
                    // Set register INDEX to 0
                    JVMRuntime.index = 3;
                    InstructionHandlers.StoreSingle();
                    
                    break;
                    
                case 100:  // isub
                    handled = true;
                    InstructionHandlers.SubSingleInt();
                    break;
                case 124:  // iushr
                    handled = true;
                    InstructionHandlers.ShiftRightLogicalInt();
                    break;
                case 130:  // ixor
                    handled = true;
                    InstructionHandlers.XorInt();
                    break;
                    
                //---------------------------------------------------
                //     J
                // --------------------------------------------------

                case 168:  // jsr
                    break;
                case 201:  // jsr_w
                    break;
                    
                    
                //---------------------------------------------------
                //     L
                // --------------------------------------------------

                case 138:  // l2d
                    break;
                case 137: // l2f
                    break;
                case 136: // l2i
                    break;
                case 97: // ladd
                    handled = true;
                    InstructionHandlers.AddDoubleInt();
                    break;
                case 47:  // laload
                    break;
                case 127:  // land
                    break;
                case 80:  // lastore
                    break;
                case 148:  // lcmp
                    break;
                case 9:  // lconst_0
                    break;
                case 10:  // lconst_1
                    break;
                case 18:  // ldc
                    handled = true;
                    // get the 1-byte index
                    JVMRuntime.index = MemoryController.readByte( JVMRuntime.pc );
                    JVMRuntime.index <<= 2; // multiply by 4 to get it into byte offset.
                    JVMRuntime.pc = ( JVMRuntime.pc + 1 );
                    InstructionHandlers.LoadConstantPool();
                    
                    break;
                case 19: // ldc_w
                    break;
                case 20:  // ldc2_w
                    break;
                case 109:  // ldiv
                    handled = true;
                    InstructionHandlers.DivDoubleInt();
                    break;
                case 22:  // lload
                    break;
                case 30:  // lload_0
                    break;
                case 31:  // lload_1
                    break;
                case 32:  // lload_2
                    break;
                case 33:  // lload_3

                    break;
                case 105:  // lmul
                    handled = true;
                    InstructionHandlers.MultDoubleInt();
                    break;
                case 117:  // lneg
                    break;
                case 171: // lookupswitch
                    handled = true;
                    InstructionHandlers.LookupSwitch();
                    break;
                case 129:  // lor
                    break;
                case 113:  // lrem
                    break;
                case 173:  // lreturn
                    break;
                case 121:  // lshl
                    break;
                case 123:  // lshr
                    break;
                case 55:  // lstore
                    break;
                case 63:  // lstore_0
                    break;
                case 64:  // lstore_1
                    break;
                case 65:  // lstore_2
                    break;
                case 66:  // lstore_3
                    break;
                case 101:  // lsub
                    handled = true;
                    InstructionHandlers.SubDoubleInt();
                    break;
                case 125:  // lushr
                    break;
                case 131:  // lxor
                    break;
                    
                //---------------------------------------------------
                //     M
                // --------------------------------------------------

                case 194:  // monitorenter
                    break;
                case 195:  // monitorexit
                    break;
                case 197:  // multianewarray
                    // Get index into constant pool for the array type and dimensions
                    JVMRuntime.index = MemoryController.readShort(JVMRuntime.pc);
                    JVMRuntime.pc = (JVMRuntime.pc + 2);

                    // PC now points to the dimensions operand - do not retrieve
                    // until needed.

                    // Only set handled if the number of dimensions is supported.
                    handled = InstructionHandlers.MultiAnewArray();

                    //Move past number of dimensions operand
                    JVMRuntime.pc = (JVMRuntime.pc + 1);

                    break;
                    
                //---------------------------------------------------
                //     N
                // --------------------------------------------------

                case 187: // new
                    handled = true;
                    // Get the Index operand and place into INDEX register
                    JVMRuntime.index = MemoryController.readShort( JVMRuntime.pc );
                    JVMRuntime.pc = ( JVMRuntime.pc + 2 );
                    
                    // Request process from ProcessManager
                    InstructionHandlers.NewInstance();
                    
                    break;

                case 188:  // newarray
                    handled = true;
                    JVMRuntime.index = MemoryController.readByte( JVMRuntime.pc ); // get 'atype'
                    JVMRuntime.pc = ( JVMRuntime.pc + 1 );
                    // Request process from ProcessManager
                    InstructionHandlers.NewArrayProcess();
                    break;

                case 0:  // nop
                    // Do nothing
                    handled = true;
                    break;                   
                    
                    
                //---------------------------------------------------
                //     P
                // --------------------------------------------------
                case 87: // pop
                    handled = true;
                    InstructionHandlers.StackPopSingle();
                    break;
                    
                case 88:  // pop2
                    break;
                    
                case 181: // putfield
                    handled = true;
                    // Get the Index operand and place into INDEX register
                    JVMRuntime.index = MemoryController.readShort( JVMRuntime.pc );
                    JVMRuntime.pc = ( JVMRuntime.pc + 2 );
                    
                    // Request process from ProcessManager
                    InstructionHandlers.PutField();
                    break;
                    
                case 179: // putstatic
                    break;
                    
                    
                    
                    
                    
                    
                //---------------------------------------------------
                //     R
                // --------------------------------------------------

                case 169:  // ret
                    break;
                case 177: // return
                    // Pop the frame and the next iteration through the instruction engine
                    // will run the new method's code
                    
                    handled = true;
                    InstructionHandlers.ReturnVoid();
                    break;
                    
                    
                    
                    
                //---------------------------------------------------
                //     S
                // --------------------------------------------------

                case 53:  // saload
                    handled = true;
                    InstructionHandlers.ArrayGetShort();
                    break;
                case 86:  // sastore
                    handled = true;
                    InstructionHandlers.ArrayPutShort();
                    break;
                case 17:  // sipush
                    handled = true;
                    // the next 2 bytes are byte1 byte2
                    JVMRuntime.value = MemoryController.readShort( JVMRuntime.pc );
                    JVMRuntime.pc += 2;
                    InstructionHandlers.StackPushSingle();
                    break;
                case 95:  // swap
                    break;
                    
                    
                    
                    
                    
                //---------------------------------------------------
                //     T
                // --------------------------------------------------

                case 170:  // tableswitch
                    handled = true;
                    InstructionHandlers.TableSwitch();
                    break;
                    
                //---------------------------------------------------
                //     W
                // --------------------------------------------------
                    
                case 196:  // wide
                    break;
                    
                //---------------------------------------------------
                //    Reserved Opcodes: 254, 255
                //    For JVM "native" methods
                //---------------------------------------------------

                case lang: // 254  supports core library "lang" package functions
                {
                    JVMRuntime.opcode = MemoryController.readByte( JVMRuntime.pc ); // get native opcode
                    JVMRuntime.pc = ( JVMRuntime.pc + 1 );
                    debug("   processing << " + Instruction.getInstructionName(JVMRuntime.opcode) + " >>");
                    
                    switch( JVMRuntime.opcode )
                    {
                        case T_start: // T_start  ; Thread start
                            handled = true;
                            Debug.message("|||| Starting Thread.");
                            InstructionHandlers.ThreadStart();
                            break;
                            
                        case T_yield: // T_yield  ; Thread yield
                            handled = true;
                            // Invoke the scheduler by activating WaitQueueProcess
                            //InstructionHandlers.Scheduler();
                            ProcessManager.setPCW( ProcessManager.INVOKE_SCHEDULER );
                            
                            break;
                        case T_sleep: // T_sleep  ; Thread sleep
                            handled = true;
                            // Prepare for the processes that perform sleep request --
                            //   get local variable in location 0 (int sleep)
                            JVMRuntime.index = 0;
                            ProcessManager.setPCW( ProcessManager.THREAD_SLEEP );
                            Debug.message("sleeping!");
                            break;
                            
                        case INT_start:  // INT_start; Interrupt start (similar to Thread start but object is placed in vector table)
                            handled = true;
                            InstructionHandlers.InterruptStart();
                            break;
                            
                        default:
                            break;
                    }
                    
                    break;
                }   
                
                case peripheral: // peripheral   supports core library hardware peripheral functions
                {
                    JVMRuntime.opcode = MemoryController.readByte( JVMRuntime.pc ); // get native opcode
                    JVMRuntime.pc = ( JVMRuntime.pc + 1 );
                    switch( JVMRuntime.opcode )
                    {
                        case display_DrawString:
                        {
                            handled = true;
                            //  The arguments are in local variables as shown:
                            //     0 - this
                            //     1 - String => Formatted as an Array object 
                            //     2 - int
                            //     3 - int
                            
                            // String: [monitor][classref][length][data]
                            
                            // Get them out into temp vars and pass into simulated display driver.
                            JVMRuntime.index = 1;
                            ProcessList.processList[ProcessEnums.PROCESS_LOCALGETSINGLE].runProcess(); // value = String reference (array of bytes)
                            // crap, how many bytes to get?  It's null terminated, which is fine for the hardware driver
                            // since it uses the pointer directly, but I don't have that luxury here: must pull each
                            // byte out until it is 0.
                            JVMRuntime.handle = JVMRuntime.value + 12; // bypass monitor, classref and length

                            // Get the length
                            int count = MemoryController.readWord(JVMRuntime.value + 8);

                            byte[] bytes = new byte[count];
                            count = 0;
                            int b = 0;
                            while ((b = MemoryController.readByte(JVMRuntime.handle++)) != 0) {
                                bytes[count] = (byte)b;
                                count++;
                            }
                            
                            JVMRuntime.index = 2;
                            ProcessList.processList[ProcessEnums.PROCESS_LOCALGETSINGLE].runProcess(); // value = int x
                            int xcoord = JVMRuntime.value;
                            
                            JVMRuntime.index = 3;
                            ProcessList.processList[ProcessEnums.PROCESS_LOCALGETSINGLE].runProcess(); // value = int y
                            int ycoord = JVMRuntime.value;
                            
                            //System.err.println("Executing DrawString : " + bytes.length + " bytes, x: " + xcoord + " y: " + ycoord);
                                                        
                            emr.jvm.peripheral.sim.display.DisplayDriver.getDisplayDriver().drawString(bytes, xcoord, ycoord);
                            
                            break;
                        }
                        case display_DrawBytes:
                        {
                            handled = true;
                            //  The arguments are in local variables as shown:
                            //     0 - this
                            //     1 - byte[] => Formatted as an Array object
                            //     2 - int
                            //     3 - int

                            // byte[] array: [monitor][classref][length][data]

                            // Get them out into temp vars and pass into simulated display driver.
                            JVMRuntime.index = 1;
                            ProcessList.processList[ProcessEnums.PROCESS_LOCALGETSINGLE].runProcess(); // value = byte[] reference (array of bytes)
                            
                            JVMRuntime.handle = JVMRuntime.value + 12; // bypass monitor, classref and length

                            // Get the length
                            int count = MemoryController.readWord(JVMRuntime.value + 8);

                            byte[] bytes = new byte[count];
                            for (int i = 0; i < count; i++) {
                                bytes[i] = (byte)MemoryController.readByte(JVMRuntime.handle + i);
                            }

                            JVMRuntime.index = 2;
                            ProcessList.processList[ProcessEnums.PROCESS_LOCALGETSINGLE].runProcess(); // value = int x
                            int xcoord = JVMRuntime.value;

                            JVMRuntime.index = 3;
                            ProcessList.processList[ProcessEnums.PROCESS_LOCALGETSINGLE].runProcess(); // value = int y
                            int ycoord = JVMRuntime.value;

                            //System.err.println("Executing DrawBytes : " + bytes.length + " bytes, x: " + xcoord + " y: " + ycoord);

                            emr.jvm.peripheral.sim.display.DisplayDriver.getDisplayDriver().drawString(bytes, xcoord, ycoord);

                            break;
                        }
                        case display_DrawInt:
                        {
                            handled = true;
                            //  The arguments are in local variables as shown:
                            //     0 - this
                            //     1 - int
                            //     2 - int
                            //     3 - int
                            
                            // Get them out into temp vars and pass into simulated display driver.
                            JVMRuntime.index = 1;
                            ProcessList.processList[ProcessEnums.PROCESS_LOCALGETSINGLE].runProcess(); // value = int
                            int value = JVMRuntime.value;
                            
                            JVMRuntime.index = 2;
                            ProcessList.processList[ProcessEnums.PROCESS_LOCALGETSINGLE].runProcess(); // value = int x
                            int xcoord = JVMRuntime.value;
                            
                            JVMRuntime.index = 3;
                            ProcessList.processList[ProcessEnums.PROCESS_LOCALGETSINGLE].runProcess(); // value = int y
                            int ycoord = JVMRuntime.value;
                            
                            //System.err.println("Executing DrawInt : " + value + " x: " + xcoord + " y: " + ycoord);
                                                        
                            emr.jvm.peripheral.sim.display.DisplayDriver.getDisplayDriver().drawString(Integer.toString(value).getBytes(), xcoord, ycoord);
                            
                            break;
                        }
                        case display_DrawLine:
                        {
                            handled = true;
                            //  The arguments are in local variables as shown:
                            //     0 - this
                            //     1 - int
                            //     2 - int
                            //     3 - int
                            //     4 - int

                            // Get them out into temp vars and pass into simulated display driver.
                            JVMRuntime.index = 1;
                            ProcessList.processList[ProcessEnums.PROCESS_LOCALGETSINGLE].runProcess(); // value = x1
                            int x1 = JVMRuntime.value;

                            JVMRuntime.index = 2;
                            ProcessList.processList[ProcessEnums.PROCESS_LOCALGETSINGLE].runProcess(); // value = y1
                            int y1 = JVMRuntime.value;

                            JVMRuntime.index = 3;
                            ProcessList.processList[ProcessEnums.PROCESS_LOCALGETSINGLE].runProcess(); // value = x2
                            int x2 = JVMRuntime.value;

                            JVMRuntime.index = 4;
                            ProcessList.processList[ProcessEnums.PROCESS_LOCALGETSINGLE].runProcess(); // value = y2
                            int y2 = JVMRuntime.value;

                            emr.jvm.peripheral.sim.display.DisplayDriver.getDisplayDriver().drawLine(x1, y1, x2, y2);

                            break;
                        }
                        case get_file_handle:
                        {

                            handled = true;
                            System.out.println("Get file handle...");
                            

                            // Get pointer to the filename string out of the local var at index 0
                            JVMRuntime.index = 0;
                            ProcessList.processList[ProcessEnums.PROCESS_LOCALGETSINGLE].runProcess(); // value = String ref
                      
                            String name = createStringFromVM(JVMRuntime.value);

                            // Get the handle of the start of the file table.
                            // The NVM begins at address 0x0.
                            // The package header is 12 bytes, after which the file table begins.
                            // 
                            JVMRuntime.cphandle = 12;

                            // We terminate the lookup when the first element of a file table entry is null (0).
                            // FileTableEntry:
                            //    filename string ref (4)
                            //    file handle (4)

                            JVMRuntime.value1 = JVMRuntime.value;
                            JVMRuntime.value = JVMRuntime.nullregister;

                            JVMRuntime.tablehandle = MemoryController.readWord(JVMRuntime.cphandle); // string ref
                            while (JVMRuntime.tablehandle != 0)
                            {
                                if (stringCompare(JVMRuntime.value1, JVMRuntime.tablehandle))
                                {
                                    // gotcha
                                    JVMRuntime.value = MemoryController.readWord(JVMRuntime.cphandle + 4);
                                    break;
                                }

                                JVMRuntime.cphandle += 8; // move to next entry
                                JVMRuntime.tablehandle = MemoryController.readWord(JVMRuntime.cphandle);
                            }

                            System.out.println("Return file handle [" + name + "] : "  + JVMRuntime.value); 


                            ProcessList.processList[ProcessEnums.PROCESS_STACKPUSHSINGLE].runProcess(); // push file handle onto stack

                            break;
                        }

                        case fileinputstream_read0:

                            handled = true;

                            /*
                             * called by static method of this signature:
                             *   private static native int read0(byte[] b, int off, int len, int position, int handle);
                             *
                             * Five input arguments:
                             *    'b'         - cphandle
                             *    'off'       - value1
                             *    'len'       - value2
                             *    'position'  - index
                             *    'handle'    - handle
                             *
                             * The requirement is to place 'len' bytes from handle[position] into array 'b' and
                             * return the number of bytes read (which is usually just 'len')
                             *
                             * local variables:
                             *     counter - value
                             */

                            /* Get the arguments */
                            
                            /* 'b' */
                            JVMRuntime.index = 0;
                            ProcessList.processList[ProcessEnums.PROCESS_LOCALGETSINGLE].runProcess();
                            JVMRuntime.cphandle = JVMRuntime.value;

                            /* 'off' */
                            JVMRuntime.index = 1;
                            ProcessList.processList[ProcessEnums.PROCESS_LOCALGETSINGLE].runProcess();
                            JVMRuntime.value1 = JVMRuntime.value;

                             /* 'len' */
                            JVMRuntime.index = 2;
                            ProcessList.processList[ProcessEnums.PROCESS_LOCALGETSINGLE].runProcess();
                            JVMRuntime.value2 = JVMRuntime.value;

                             /* 'handle' */
                            JVMRuntime.index = 4;
                            ProcessList.processList[ProcessEnums.PROCESS_LOCALGETSINGLE].runProcess();
                            JVMRuntime.handle = JVMRuntime.value;

                             /* 'position' */
                            JVMRuntime.index = 3;
                            ProcessList.processList[ProcessEnums.PROCESS_LOCALGETSINGLE].runProcess();
                            JVMRuntime.index = JVMRuntime.value;

                            /* Some checks that could/should be done:
                             *   Can array hold the number of bytes?
                             * These checks should probably be performed in the calling Java method.
                             * Assumptions: this method is called if len > 0.
                             * Problem: I have no way of knowing the file length!
                             * File length needs to be embedded in a 'file' structure.
                             */

                            JVMRuntime.value = 0;

                            /* Need some kind of memcpy equivalent */
                            while (JVMRuntime.value < JVMRuntime.value2)
                            {
                                /* Get the byte from the file */
                                JVMRuntime.tablehandle = MemoryController.readByte(JVMRuntime.handle +
                                                                                   JVMRuntime.index +
                                                                                   JVMRuntime.value);

                                /* Put the byte in the array
                                 * Two options:
                                 *   - direct manipulation of memory, or
                                 *   - call array put byte process
                                 */

                                MemoryController.writeByte(JVMRuntime.cphandle + Array.ArrayBaseSize + JVMRuntime.value1 + JVMRuntime.value,
                                        JVMRuntime.tablehandle, emr.jvm.visualization.MemoryVisualizer.ARRAY);

                                

                                System.out.println(JVMRuntime.tablehandle);

                                JVMRuntime.value++;
                            }

                            System.out.println("Read " + JVMRuntime.value + " bytes from " + JVMRuntime.handle + "[" + JVMRuntime.index + "] " +
                                    "to " + JVMRuntime.cphandle + "[" + JVMRuntime.value1 + "]");



                            /* Push 'len' number of bytes read back on the stack as a return value*/
                            ProcessList.processList[ProcessEnums.PROCESS_STACKPUSHSINGLE].runProcess();

                            break;

                        case sourcedataline_write:
                            break;

                        default:
                            break;                        
                    }
                    break;
                }
                default:
                    
                    break;
                
            } // end switch
            
            if( !handled )
                throwException(UNHANDLED_OPCODE_EXCEPTION);

            handled = false;          
         
    }


    private static boolean stringCompare(int refA, int refB)
    {
        String name1 = createStringFromVM(refA);
        String name2 = createStringFromVM(refB);

        System.out.println("Comparing " + name1 + " " + name2);

        int refAlength = MemoryController.readWord(refA + Array.ArrayLengthOffset);
        int refBlength = MemoryController.readWord(refB + Array.ArrayLengthOffset);

        if (refAlength != refBlength)
            return false;

        for (JVMRuntime.value2 = 0; JVMRuntime.value2 < refAlength; JVMRuntime.value2++)
        {
            JVMRuntime.name = MemoryController.readByte(refA + Array.ArrayBaseSize + JVMRuntime.value2);
            JVMRuntime.descriptor = MemoryController.readByte(refB + Array.ArrayBaseSize + JVMRuntime.value2);
            if (JVMRuntime.name != JVMRuntime.descriptor)
                return false;
        }

        return true;
    }

    /**
     * Utility function: make a Java string from a reference to a VM string
     *
     * VM String is a VM array of bytes.
     *   monitor (4)
     *   classref (4)
     *   length (4)
     *   data (length bytes)
     *
     */
    private static String createStringFromVM(int stringRef) {

        /* Option to use VM registers and risk overwrite, or use private
         * variables (which go on a stack)
         */

        int length = MemoryController.readWord(stringRef + Array.ArrayLengthOffset);

        byte[] b = new byte[length];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte)MemoryController.readByte(stringRef + Array.ArrayBaseSize + i);
        }

        String s = new String(b);
        return s;
    }
}
