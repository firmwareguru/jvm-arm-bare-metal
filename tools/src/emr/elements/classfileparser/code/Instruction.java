/*
 * Instruction.java
 *
 * Created on July 5, 2006, 9:18 PM
 *
 * An Element in the Code package.
 */

package emr.elements.classfileparser.code;

import emr.elements.classfileparser.OpcodeMnemonics;
import emr.elements.common.Element;
import emr.elements.common.GenericElement;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 *
 * @author Ross
 */
public class Instruction extends Element
{
    // current pc in the method for use with tableswitch
    int pc;
    
    // The name of this instruction.
    // This string is reported when viewing it using a tree viewer.
    String instructionName;
    
    // This is a brief description.  Can be used as metadata.
    String instructionDescription;

    // Size of the instruction in bytes - used by Code element
    // The size of this element must still be 0 - the subelements contain the data
    int instructionSize = 0;
    
    /** Creates a new instance of Instruction */
    public Instruction(int pc_)
    {
       pc = pc_;
       
       add(new GenericElement(u1, "opcode"));   
    }
    
    /** No arg constructor for just getting instruction names through getInstructionName
     */
    public Instruction()
    {
        
    }

    // overridden so we don't convert the size (if <=4) to a value
    public void readElement(InputStream is) throws IOException
    {
        
    }
    
    // overridden so we don't convert the size (if <=4) to a value
    public void writeElement(OutputStream os) throws IOException
    {
        
    }
    
    public void readChildren(InputStream is) throws IOException
    {
        // Need to set the total size of this instruction so that
        // the Code element who is adding these instructions can
        // figure out when its done.
        instructionSize = 0;
        
        // Read in the opcode.  Configure the instruction.  
        Element opcodeElement = get(0);
        opcodeElement.readData(is);
        
        int opcode = opcodeElement.getValue();
        
        configureInstruction(opcode, is);
        
        for (int i = 0; i < size(); i++)
        {
            instructionSize += get(i).getSize();
        }
  
   }
            
    
    
    public String toString()
    {
        return instructionName;
    }
    

    public int getInstructionSize() {
        return instructionSize;
    }
    
    /**
     *
     * Configure this Instruction for the given opcode by:
     *    1. Setting the name.
     *    2. Adding Generic Elements to handle the operands.
     */
    //public Element getInstruction(int opcode) throws IOException
    private void configureInstruction(int opcode, InputStream is) throws IOException
    {
        
        // Set the instruction name.
        instructionName = getInstructionName(opcode);
        
        // Add the operands.
        
        if(debug) System.out.println("---- parsing opcode " + opcode);
        
            
            // configure Instruction based on opcode
            switch(opcode)
            {
            
                //---------------------------------------------------
                //     A
                // --------------------------------------------------
                
                case 50: // aaload
                    instructionDescription = "Load reference from array";
                    break;
                case 83: // aastore
                    instructionDescription = "Store reference into array";
                    break;
                case 1:  // aconst_null
                    instructionDescription = "Push null";
                    break;
                case 25:  // aload
                    instructionDescription = "Load reference from local variable";
                    add(new GenericElement(u1, "index"));
                    break;
                case 42:  // aload_0
                    instructionDescription = "Load reference from local variable";
                    break;
                case 43:  // aload_1
                    instructionDescription = "Load reference from local variable";
                    break;
                case 44:  // aload_2
                    instructionDescription = "Load reference from local variable";
                    break;
                case 45:  // aload_3
                    instructionDescription = "Load reference from local variable";
                    break;
                case 189:  // anewarray
                    instructionDescription = "Create new array of reference";
                    add(new GenericElement(u2, "index"));
                    break;
                case 176:  // areturn =
                    instructionDescription = "Return reference from method";
                    break;
                case 190:  // arraylength
                    instructionDescription = "Get length of array";
                    break;
                case 58:  // astore
                    instructionDescription = "Store reference into local variable";
                    add(new GenericElement(u1, "index"));
                    break;
                case 75:  // astore_0
                    instructionDescription = "Store reference into local variable";
                    break;
                case 76:  // astore_1
                    instructionDescription = "Store reference into local variable";
                    break;
                case 77:  // astore_2
                    instructionDescription = "Store reference into local variable";
                    break;
                case 78:  // astore_3
                    instructionDescription = "Store reference into local variable";
                    break;
                case 191:  // athrow
                    instructionDescription = "Throw exception or error";
                    break;
                    
                //---------------------------------------------------
                //     B
                // --------------------------------------------------
                    
                case 51:  // baload
                    instructionDescription = "Load byte or boolean from array";
                    break;
                case 84:  // bastore
                    instructionDescription = "Store into byte or boolean array";
                    break;
                case 16:  // bipush
                    instructionDescription = "Push byte";
                    add(new GenericElement(u1, "byte"));
                    break;
                    
                //---------------------------------------------------
                //     C
                // --------------------------------------------------
                    
                case 52:  // caload
                    instructionDescription = "Load char from array";
                    break;
                case 85:  // castore
                    instructionDescription = "";
                    break;
                case 192: // checkcast
                    instructionDescription = "";
                    add(new GenericElement(u2, "index"));
                    break; 
                //---------------------------------------------------
                //     D
                // --------------------------------------------------
                
                case 144:  // d2f
                    instructionDescription = "";
                    break;
                case 142:  // d2i
                    instructionDescription = "";
                    break;
                case 143:  // d2l
                    instructionDescription = "";
                    break;
                case 99:  // dadd
                    instructionDescription = "";
                    break;
                case 49:  // daload
                    instructionDescription = "";
                    break;
                case 82:  // dastore
                    instructionDescription = "";
                    break;
                case 152:  // dcmpg
                    instructionDescription = "";
                    break;
                case 151:  // dcmpl
                    instructionDescription = "";
                    break;
                case 14:  // dconst_0
                    instructionDescription = "";
                    break;
                case 15:  // dconst_1
                    instructionDescription = "";
                    break;
                case 111:  // ddiv
                    instructionDescription = "";
                    break;
                case 24:  // dload
                    instructionDescription = "";
                    add(new GenericElement(u1, "index"));
                    break;
                case 38:  // dload_0
                    instructionDescription = "";
                    break;
                case 39:  // dload_1
                    instructionDescription = "";
                    break;
                case 40:  // dload_2
                    instructionDescription = "";
                    break;
                case 41:  // dload_3
                    instructionDescription = "";
                    break;
                case 107:  // dmul
                    instructionDescription = "";
                    break;
                case 119:  // dneg
                    instructionDescription = "";
                    break;
                case 115:  // drem
                    instructionDescription = "";
                    break;
                case 175:  // dreturn =
                    instructionDescription = "";
                    break;
                case 57:  // dstore
                    instructionDescription = "";
                    add(new GenericElement(u1, "index"));
                    break;
                case 71:  // dstore_0
                    instructionDescription = "";
                    break;
                case 72:  // dstore_1
                    instructionDescription = "";
                    break;
                case 73:  // dstore_2
                    instructionDescription = "";
                    break;
                case 74:  // dstore_3
                    instructionDescription = "";
                    break;
                case 103:  // dsub
                    instructionDescription = "";
                    break;
                case 89:  // dup
                    instructionDescription = "";
                    break;
                case 90:  // dup_x1
                    instructionDescription = "";
                    break;
                case 91:  // dup_x2
                    instructionDescription = "";
                    break;
                case 92:  // dup2
                    instructionDescription = "";
                    break;
                case 93:  // dup2_x1
                    instructionDescription = "";
                    break;
                case 94:  // dup2_x2
                    instructionDescription = "";
                    break;
                    
                //---------------------------------------------------
                //     F
                // --------------------------------------------------

                case 141:  // f2d
                    instructionDescription = "";
                    break;
                case 139:  // f2i
                    instructionDescription = "";
                    break;
                case 140:  // f2l
                    instructionDescription = "";
                    break;
                case 98:  // fadd
                    instructionDescription = "";
                    break;
                case 48:  // faload
                    instructionDescription = "";
                    break;
                case 81: // fastore
                    instructionDescription = "";
                    break;
                case 150:  // fcmpg
                    instructionDescription = "";
                    break;
                case 149:  // fcmpl
                    instructionDescription = "";
                    break;
                case 11:  // fconst_0
                    instructionDescription = "";
                    break;
                case 12:  // fconst_1
                    instructionDescription = "";
                    break;
                case 13:  // fconst_2
                    instructionDescription = "";
                    break;
                case 110:  // fdiv
                    instructionDescription = "";
                    break;
                case 23:  // fload
                    instructionDescription = "";
                    add(new GenericElement(u1, "index"));
                    break;
                case 34:  // fload_0
                    instructionDescription = "";
                    break;
                case 35:  // fload_1
                    instructionDescription = "";
                    break;
                case 36:  // fload_2
                    instructionDescription = "";
                    break;
                case 37:  // fload_3
                    instructionDescription = "";
                    break;
                case 106:  // fmul
                    instructionDescription = "";
                    break;
                case 118:  // fneg
                    instructionDescription = "";
                    break;
                case 114:  // frem
                    instructionDescription = "";
                    break;
                case 174:  // freturn 
                    instructionDescription = "";
                    break;
                case 56:  // fstore
                    instructionDescription = "";
                    add(new GenericElement(u1, "index"));
                    break;
                case 67:  // fstore_0
                    instructionDescription = "";
                    break;
                case 68:  // fstore_1
                    instructionDescription = "";
                    break;
                case 69:  // fstore_2
                    instructionDescription = "";
                    break;
                case 70:  // fstore_3
                    instructionDescription = "";
                    break;
                case 102:  // fsub
                    instructionDescription = "";
                    break;
                    
                    
                //---------------------------------------------------
                //     G
                // --------------------------------------------------

                case 180:  // getfield
                    instructionDescription = "";
                    add(new GenericElement(u2, "index"));
                    break;
                case 178:  // getstatic
                    instructionDescription = "";
                    add(new GenericElement(u2, "index"));
                    break;
                case 167:  // goto
                    instructionDescription = "";
                    add(new GenericElement(u2, "branch"));
                    break;
                case 200:  // goto_w
                    instructionDescription = "";
                    add(new GenericElement(u4, "branch"));
                    break;
                    
                //---------------------------------------------------
                //     I
                // --------------------------------------------------
 
                case 145:  // i2b
                    instructionDescription = "";
                    break;
                case 146:  // i2c
                    instructionDescription = "";
                    break;
                case 135:  // i2d
                    instructionDescription = "";
                    break;
                case 134:  // i2f
                    instructionDescription = "";
                    break;
                case 133:  // i2l
                    instructionDescription = "";
                    break;
                case 147:  // i2s
                    instructionDescription = "";
                    break;
                case 96:  // iadd
                    instructionDescription = "";
                    break;
                case 46:  // iaload
                    instructionDescription = "";
                    break;
                case 126:  // iand
                    instructionDescription = "";
                    break;
                case 79:  // iastore
                    instructionDescription = "";
                    break;
                case 2:  // iconst_m1
                    instructionDescription = "";
                    break;
                case 3:  // iconst_0
                    instructionDescription = "";
                    break;
                case 4:  // iconst_1
                    instructionDescription = "";
                    break;
                case 5:  // iconst_2
                    instructionDescription = "";
                    break;
                case 6:  // iconst_3
                    instructionDescription = "";
                    break;
                case 7:  // iconst_4
                    instructionDescription = "";
                    break;
                case 8:  // iconst_5
                    instructionDescription = "";
                    break;
                case 108:  // idiv
                    instructionDescription = "";
                    break;
                case 165:  // if_acmpeq
                    instructionDescription = "";
                    add(new GenericElement(u2, "branch"));
                    break;
                case 166:  // if_acmpne
                    instructionDescription = "";
                    add(new GenericElement(u2, "branch"));
                    break;
                case 159:  // if_icmpeq
                    instructionDescription = "";
                    add(new GenericElement(u2, "branch"));
                    break;
                case 160:  // if_icmpne
                    instructionDescription = "";
                    add(new GenericElement(u2, "branch"));
                    break;
                case 161:  // if_icmplt
                    instructionDescription = "";
                    add(new GenericElement(u2, "branch"));
                    break;
                case 162:  // if_icmpge
                    instructionDescription = "";
                    add(new GenericElement(u2, "branch"));
                    break;
                case 163:  // if_icmpgt
                    instructionDescription = "";
                    add(new GenericElement(u2, "branch"));
                    break;
                case 164:  // if_icmple
                    instructionDescription = "";
                    add(new GenericElement(u2, "branch"));
                    break;
                case 153:  // ifeq
                    instructionDescription = "";
                    add(new GenericElement(u2, "branch"));
                    break;
                case 154:  // ifne
                    instructionDescription = "";
                    add(new GenericElement(u2, "branch"));
                    break;
                case 155:  // iflt
                    instructionDescription = "";
                    add(new GenericElement(u2, "branch"));
                    break;
                case 156:  // ifge
                    instructionDescription = "";
                    add(new GenericElement(u2, "branch"));
                    break;
                case 157:  // ifgt
                    instructionDescription = "";
                    add(new GenericElement(u2, "branch"));
                    break;
                case 158:  // ifle
                    instructionDescription = "";
                    add(new GenericElement(u2, "branch"));
                    break;
                case 199:  // ifnonnull
                    instructionDescription = "";
                    add(new GenericElement(u2, "branch"));
                    break;
                case 198: // ifnull
                    instructionDescription = "";
                    add(new GenericElement(u2, "branch"));
                    break;
                case 132:  // iinc
                    instructionDescription = "";
                    add(new GenericElement(u1, "index"));
                    add(new GenericElement(u1, "const"));
                    break;
                case 21:  // iload
                    instructionDescription = "";
                    add(new GenericElement(u1, "index"));
                    break;
                case 26:  // iload_0
                    instructionDescription = "";
                    break;
                case 27:  // iload_1
                    instructionDescription = "";
                    break;
                case 28:  // iload_2
                    instructionDescription = "";
                    break;
                case 29:  // iload_3
                    instructionDescription = "";
                    break;
                case 104:  // imul
                    instructionDescription = "";
                    break;
                case 116:  // ineg
                    instructionDescription = "";
                    break;
                case 193:  // instanceof
                    instructionDescription = "";
                    add(new GenericElement(u2, "index"));
                    break;
                case 185:  // invokeinterface
                    instructionDescription = "";
                    add(new GenericElement(u2, "index"));
                    add(new GenericElement(u1, "count"));
                    add(new GenericElement(u1, "reserved"));
                    break;
                case 183:  // invokespecial
                    instructionDescription = "";
                    add(new GenericElement(u2, "index"));
                    break;
                case 184:  // invokestatic
                    instructionDescription = "";
                    add(new GenericElement(u2, "index"));
                    break;
                case 182:  // invokevirtual
                    instructionDescription = "";
                    add(new GenericElement(u2, "index"));
                    break;
                case 128:  // ior
                    instructionDescription = "";
                    break;
                case 112:  // irem
                    instructionDescription = "";
                    break;
                case 172:  // ireturn
                    instructionDescription = "";
                    break;
                case 120:  // ishl
                    instructionDescription = "";
                    break;
                case 122:  // ishr
                    instructionDescription = "";
                    break;
                case 54:  // istore
                    instructionDescription = "";
                    add(new GenericElement(u1, "index"));
                    break;
                case 59: // istore_0
                    instructionDescription = "";
                    break;
                case 60: // istore_1
                    instructionDescription = "";
                    break;
                case 61: // istore_2
                    instructionDescription = "";
                    break;
                case 62: // istore_3
                    instructionDescription = "";
                    break;
                case 100:  // isub
                    instructionDescription = "";
                    break;
                case 124:  // iushr
                    instructionDescription = "";
                    break;
                case 130:  // ixor
                    instructionDescription = "";
                    break;
                    
                //---------------------------------------------------
                //     J
                // --------------------------------------------------

                case 168:  // jsr
                    instructionDescription = "";
                    add(new GenericElement(u2, "branch"));
                    break;
                case 201:  // jsr_w
                    instructionDescription = "";
                    add(new GenericElement(u4, "branch"));
                    break;
                    
                    
                //---------------------------------------------------
                //     L
                // --------------------------------------------------

                case 138:  // l2d
                    instructionDescription = "";
                    break;
                case 137: // l2f
                    instructionDescription = "";
                    break;
                case 136: // l2i
                    instructionDescription = "";
                    break;
                case 97: // ladd
                    instructionDescription = "";
                    break;
                case 47:  // laload
                    instructionDescription = "";
                    break;
                case 127:  // land
                    instructionDescription = "";
                    break;
                case 80:  // lastore
                    instructionDescription = "";
                    break;
                case 148:  // lcmp
                    instructionDescription = "";
                    break;
                case 9:  // lconst_0
                    instructionDescription = "";
                    break;
                case 10:  // lconst_1
                    instructionDescription = "";
                    break;
                case 18:  // ldc
                    instructionDescription = "";
                    add(new GenericElement(u1, "index"));
                    break;
                case 19: // ldc_w
                    instructionDescription = "";
                    add(new GenericElement(u2, "index"));
                    break;
                case 20:  // ldc2_w
                    instructionDescription = "";
                    add(new GenericElement(u2, "index"));
                    break;
                case 109:  // ldiv
                    instructionDescription = "";
                    break;
                case 22:  // lload
                    instructionDescription = "";
                    add(new GenericElement(u1, "index"));
                    break;
                case 30:  // lload_0
                    instructionDescription = "";
                    break;
                case 31:  // lload_1
                    instructionDescription = "";
                    break;
                case 32:  // lload_2
                    instructionDescription = "";
                    break;
                case 33:  // lload_3
                    instructionDescription = "";
                    break;
                case 105:  // lmul
                    instructionDescription = "";
                    break;
                case 117:  // lneg
                    instructionDescription = "";
                    break;
                case 171: // lookupswitch
                    instructionDescription = "";
                    handleLookupSwitch(is, pc);                    
                    return ; // !!! MUST RETURN to avoid re-reading operands
                case 129:  // lor
                    instructionDescription = "";
                    break;
                case 113:  // lrem
                    instructionDescription = "";
                    break;
                case 173:  // lreturn
                    instructionDescription = "";
                    break;
                case 121:  // lshl
                    instructionDescription = "";
                    break;
                case 123:  // lshr
                    instructionDescription = "";
                    break;
                case 55:  // lstore
                    instructionDescription = "";
                    add(new GenericElement(u1, "index"));
                    break;
                case 63:  // lstore_0
                    instructionDescription = "";
                    break;
                case 64:  // lstore_1
                    instructionDescription = "";
                    break;
                case 65:  // lstore_2
                    instructionDescription = "";
                    break;
                case 66:  // lstore_3
                    instructionDescription = "";
                    break;
                case 101:  // lsub
                    instructionDescription = "";
                    break;
                case 125:  // lushr
                    instructionDescription = "";
                    break;
                case 131:  // lxor
                    instructionDescription = "";
                    break;
                    
                //---------------------------------------------------
                //     M
                // --------------------------------------------------

                case 194:  // monitorenter
                    instructionDescription = "";
                    break;
                case 195:  // monitorexit
                    instructionDescription = "";
                    break;
                case 197:  // multianewarray
                    instructionDescription = "";
                    add(new GenericElement(u2, "index"));
                    add(new GenericElement(u1, "dimensions"));
                    break;
                    
                //---------------------------------------------------
                //     N
                // --------------------------------------------------

                case 187: // new
                    instructionDescription = "";
                    add(new GenericElement(u2, "index"));
                    break;
                case 188:  // newarray
                    instructionDescription = "";
                    add(new GenericElement(u1, "atype"));
                    break;
                case 0:  // nop
                    instructionDescription = "";
                    break;
                    
                    
                    
                //---------------------------------------------------
                //     P
                // --------------------------------------------------
                case 87: // pop
                    instructionDescription = "";
                    break;
                case 88:  // pop2
                    instructionDescription = "";
                    break;
                case 181: // putfield
                    instructionDescription = "";
                    add(new GenericElement(u2, "index"));
                    break;
                case 179: // putstatic
                    instructionDescription = "";
                    add(new GenericElement(u2, "index"));
                    break;
                    
                    
                    
                    
                    
                    
                //---------------------------------------------------
                //     R
                // --------------------------------------------------

                case 169:  // ret
                    instructionDescription = "";
                    add(new GenericElement(u1, "index"));
                    break;
                case 177: // return
                    instructionDescription = "";
                    break;
                    
                    
                //---------------------------------------------------
                //     S
                // --------------------------------------------------

                case 53:  // saload
                    instructionDescription = "";
                    break;
                case 86:  // sastore
                    instructionDescription = "";
                    break;
                case 17:  // sipush
                    instructionDescription = "";
                    add(new GenericElement(u2, "short"));
                    break;
                case 95:  // swap
                    instructionDescription = "";
                    break;
                    
                    
                    
                    
                    
                //---------------------------------------------------
                //     T
                // --------------------------------------------------

                case 170:  // tableswitch
                    instructionDescription = "";
                    handleTableSwitch(is, pc);
                    return ; // !!! MUST RETURN to avoid re-reading operands
                //---------------------------------------------------
                //     W
                // --------------------------------------------------
                    
                case 196:  // wide
                    instructionDescription = "";
                    handleWide(is);
                    return ; // !!! MUST RETURN to avoid re-reading operands
                    
                    
                //----------------------------------------------------
                // JVM Native Instructions
                //----------------------------------------------------
                case 254:  // lang
                    instructionDescription = "";
                    add(new GenericElement(u1, "opcode"));
                    break;
                
                case 255:  // peripheral
                    instructionDescription = "";
                    add(new GenericElement(u1, "opcode"));
                    break;
                    
                default:
                    // unknown instruction
                    throw new IOException("Unknown instruction " + opcode + " pc: " + pc);
                   
            }  // end switch
            
            
       
        // Now read in the operands, if any.
        for (int i = 1; i < size(); i++)
        {
            get(i).readData(is);
        }
             
    }

    /**
     *  Perform special handling of the wide opcode.
     *  Responsible for reading in any added elements.
     */
    private void handleWide(InputStream is) throws IOException
    {
        if(debug) System.out.println("handling wide.");
        // Two formats based on the following opcode.
        GenericElement opcodeElement = new GenericElement(u1, null);
        add(opcodeElement);
        opcodeElement.readData(is);
        int opcode = opcodeElement.getValue();
        opcodeElement.setName(getInstructionName(opcode));

        if(debug) System.out.println("handling wide for: " + opcode);

        switch(opcode)
        {
            case OpcodeMnemonics.iload:
            case OpcodeMnemonics.fload:
            case OpcodeMnemonics.aload:
            case OpcodeMnemonics.lload:
            case OpcodeMnemonics.dload:
            case OpcodeMnemonics.istore:
            case OpcodeMnemonics.fstore:
            case OpcodeMnemonics.astore:
            case OpcodeMnemonics.lstore:
            case OpcodeMnemonics.dstore:
            case OpcodeMnemonics.ret:
                Element e = new GenericElement(u2, "index");
                e.readData(is);
                add(e);
                break;
                
            case OpcodeMnemonics.iinc:
                Element e1 = new GenericElement(u2, "index");
                Element e2 = new GenericElement(u2, "const");
                e1.readData(is);
                e2.readData(is);
                add(e1);
                add(e2);
                break;
                
            default:
                throw new IOException("Invalid wide opcode " + opcode);
        }
        
    }
        
        
   /**
     *  Perform special handling of the tableswitch opcode.
     *  Responsible for reading in any added elements.
    */
    private void handleTableSwitch(InputStream is, int pc) throws IOException
    {
       // given the pc, we can compute the size of PadBytes
       
        int pcmod4 = pc % 4; 
        int pad;
        
        if (pcmod4 == 0) // pc is multiple of 4.  don't need pad bytes.
            pad = 0;
        else
            pad = 3 - pcmod4;  // else add pad bytes to bring to next muliple of 4. (0 to 3 pad bytes).

        Element padBytes = new GenericElement(0, "padbytes");
        padBytes.setSize(pad);
        padBytes.readData(is); // read in the pad bytes
        add(padBytes);

        GenericElement defaultElement = new GenericElement(u4, "default");
        GenericElement lowElement = new GenericElement(u4, "low");
        GenericElement highElement = new GenericElement(u4, "high");
        add(defaultElement);
        add(lowElement);
        add(highElement);  
        
        defaultElement.readData(is);
        lowElement.readData(is);
        highElement.readData(is);
        
        int high = highElement.getValue();
        int low = lowElement.getValue();
        
        if(debug) System.out.println("handling tableswitch pad(" + pad + ") high(" + high + ") low(" + low + ")");
        
        for(int i = 0; i < (high - low + 1); i ++)
        {
            // read in the JumpOffsets
            Element e = new GenericElement(u4, "jumpoffset");
            add(e);
            e.readData(is);
        }
        
        //setSize(padBytes.getSize() + 12 + (high - low + 1) * 4);
        
    }
 
        
   /**
     *  Perform special handling of the lookupswitch opcode.
     *  Responsible for reading in any added elements.
     */
    private void handleLookupSwitch(InputStream is, int pc) throws IOException
    {
        // given the pc, we can compute the size of PadBytes
        int pcmod4 = pc % 4; 
        int pad;
        
        if (pcmod4 == 0) // pc is multiple of 4.  don't need pad bytes.
            pad = 0;
        else
            pad = 3 - pcmod4;  // else add pad bytes to bring to next muliple of 4. (0 to 3 pad bytes).

        Element padBytes = new GenericElement(0, "padbytes");
        padBytes.setSize(pad);
        padBytes.readData(is); // read in the pad bytes
        add(padBytes);

        GenericElement defaultElement = new GenericElement(u4, "default");
        GenericElement npairsElement = new GenericElement(u4, "numpairs");
        add(defaultElement);
        add(npairsElement);
        
        defaultElement.readData(is);
        npairsElement.readData(is);
        
        int npairs = npairsElement.getValue();
 
        if(debug) System.out.println("handling tableswitch pad(" + pad + ") npairs(" + npairs + ")");
       
        for(int i = 0; i < npairs; i++)
        {
            // read in and add each match-offset pair
            Element match = new GenericElement(u4, "match");
            Element offset = new GenericElement(u4, "offset");
            add(match);
            add(offset);
            match.readData(is);
            offset.readData(is);
       }
        
        // need to set the size of this Instruction
        // size = number of pad bytes + default + numpairs + numpairs * (match + offset)
        //setSize(padBytes.getSize() + 8 + npairs * 8);
       
    }
   
    
    
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    
    
    
    /**
     *  Return the name of the bytecode corresponding to the given opcode.
     */
    public static String getInstructionName(int opcode)
    {
        String instructionName;
        
            switch(opcode)
            {
            
                //---------------------------------------------------
                //     A
                // --------------------------------------------------
                
                case 50: // aaload
                    instructionName = "aaload";
                    break;
                case 83: // aastore
                    instructionName = "aastore";
                    break;
                case 1:  // aconst_null
                    instructionName = "aconst_null";
                    break;
                case 25:  // aload
                    instructionName = "aload";
                    break;
                case 42:  // aload_0
                    instructionName = "aload_0";
                    break;
                case 43:  // aload_1
                    instructionName = "aload_1";
                    break;
                case 44:  // aload_2
                    instructionName = "aload_2";
                    break;
                case 45:  // aload_3
                    instructionName = "aload_3";
                    break;
                case 189:  // anewarray
                    instructionName = "anewarray";
                    break;
                case 176:  // areturn =
                    instructionName = "areturn";
                    break;
                case 190:  // arraylength
                    instructionName = "arraylength";
                    break;
                case 58:  // astore
                    instructionName = "astore";
                    break;
                case 75:  // astore_0
                    instructionName = "astore_0";
                    break;
                case 76:  // astore_1
                    instructionName = "astore_1";
                    break;
                case 77:  // astore_2
                    instructionName = "astore_2";
                    break;
                case 78:  // astore_3
                    instructionName = "astore_3";
                    break;
                case 191:  // athrow
                    instructionName = "athrow";
                    break;
                    
                //---------------------------------------------------
                //     B
                // --------------------------------------------------
                    
                case 51:  // baload
                    instructionName = "baload";
                    break;
                case 84:  // bastore
                    instructionName = "bastore";
                    break;
                case 16:  // bipush
                    instructionName = "bipush";
                    break;
                    
                //---------------------------------------------------
                //     C
                // --------------------------------------------------
                    
                case 52:  // caload
                    instructionName = "caload";
                    break;
                case 85:  // castore
                    instructionName = "castore";
                    break;
                case 192: // checkcast
                    instructionName = "checkcast";
                    break; 
                //---------------------------------------------------
                //     D
                // --------------------------------------------------
                
                case 144:  // d2f
                    instructionName = "d2f";
                    break;
                case 142:  // d2i
                    instructionName = "d2i";
                    break;
                case 143:  // d2l
                    instructionName = "d2l";
                    break;
                case 99:  // dadd
                    instructionName = "dadd";
                    break;
                case 49:  // daload
                    instructionName = "daload";
                    break;
                case 82:  // dastore
                    instructionName = "dastore";
                    break;
                case 152:  // dcmpg
                    instructionName = "dcmpg";
                    break;
                case 151:  // dcmpl
                    instructionName = "dcmpl";
                    break;
                case 14:  // dconst_0
                    instructionName = "dconst_0";
                    break;
                case 15:  // dconst_1
                    instructionName = "dconst_1";
                    break;
                case 111:  // ddiv
                    instructionName = "ddiv";
                    break;
                case 24:  // dload
                    instructionName = "dload";
                    break;
                case 38:  // dload_0
                    instructionName = "dload_0";
                    break;
                case 39:  // dload_1
                    instructionName = "dload_1";
                    break;
                case 40:  // dload_2
                    instructionName = "dload_2";
                    break;
                case 41:  // dload_3
                    instructionName = "dload_3";
                    break;
                case 107:  // dmul
                    instructionName = "dmul";
                    break;
                case 119:  // dneg
                    instructionName = "dneg";
                    break;
                case 115:  // drem
                    instructionName = "drem";
                    break;
                case 175:  // dreturn =
                    instructionName = "dreturn";
                    break;
                case 57:  // dstore
                    instructionName = "dstore";
                    break;
                case 71:  // dstore_0
                    instructionName = "dstore_0";
                    break;
                case 72:  // dstore_1
                    instructionName = "dstore_1";
                    break;
                case 73:  // dstore_2
                    instructionName = "dstore_2";
                    break;
                case 74:  // dstore_3
                    instructionName = "dstore_3";
                    break;
                case 103:  // dsub
                    instructionName = "dsub";
                    break;
                case 89:  // dup
                    instructionName = "dup";
                    break;
                case 90:  // dup_x1
                    instructionName = "dup_x1";
                    break;
                case 91:  // dup_x2
                    instructionName = "dup_x2";
                    break;
                case 92:  // dup2
                    instructionName = "dup2";
                    break;
                case 93:  // dup2_x1
                    instructionName = "dup2_x1";
                    break;
                case 94:  // dup2_x2
                    instructionName = "dup2_x2";
                    break;
                    
                //---------------------------------------------------
                //     F
                // --------------------------------------------------

                case 141:  // f2d
                    instructionName = "f2d";
                    break;
                case 139:  // f2i
                    instructionName = "f2i";
                    break;
                case 140:  // f2l
                    instructionName = "f2l";
                    break;
                case 98:  // fadd
                    instructionName = "fadd";
                    break;
                case 48:  // faload
                    instructionName = "faload";
                    break;
                case 81: // fastore
                    instructionName = "fastore";
                    break;
                case 150:  // fcmpg
                    instructionName = "fcmpg";
                    break;
                case 149:  // fcmpl
                    instructionName = "fcmpl";
                    break;
                case 11:  // fconst_0
                    instructionName = "fconst_0";
                    break;
                case 12:  // fconst_1
                    instructionName = "fconst_1";
                    break;
                case 13:  // fconst_2
                    instructionName = "fconst_2";
                    break;
                case 110:  // fdiv
                    instructionName = "fdiv";
                    break;
                case 23:  // fload
                    instructionName = "fload";
                    break;
                case 34:  // fload_0
                    instructionName = "fload_0";
                    break;
                case 35:  // fload_1
                    instructionName = "fload_1";
                    break;
                case 36:  // fload_2
                    instructionName = "fload_2";
                    break;
                case 37:  // fload_3
                    instructionName = "fload_3";
                    break;
                case 106:  // fmul
                    instructionName = "fmul";
                    break;
                case 118:  // fneg
                    instructionName = "fneg";
                    break;
                case 114:  // frem
                    instructionName = "frem";
                    break;
                case 174:  // freturn 
                    instructionName = "freturn";
                    break;
                case 56:  // fstore
                    instructionName = "fstore";
                    break;
                case 67:  // fstore_0
                    instructionName = "fstore_0";
                    break;
                case 68:  // fstore_1
                    instructionName = "fstore_1";
                    break;
                case 69:  // fstore_2
                    instructionName = "fstore_2";
                    break;
                case 70:  // fstore_3
                    instructionName = "fstore_3";
                    break;
                case 102:  // fsub
                    instructionName = "fsub";
                    break;
                    
                    
                //---------------------------------------------------
                //     G
                // --------------------------------------------------

                case 180:  // getfield
                    instructionName = "getfield";
                    break;
                case 178:  // getstatic
                    instructionName = "getstatic";
                    break;
                case 167:  // goto
                    instructionName = "goto";
                    break;
                case 200:  // goto_w
                    instructionName = "goto_w";
                    break;
                    
                //---------------------------------------------------
                //     I
                // --------------------------------------------------
 
                case 145:  // i2b
                    instructionName = "i2b";
                    break;
                case 146:  // i2c
                    instructionName = "i2c";
                    break;
                case 135:  // i2d
                    instructionName = "i2d";
                    break;
                case 134:  // i2f
                    instructionName = "i2f";
                    break;
                case 133:  // i2l
                    instructionName = "i2l";
                    break;
                case 147:  // i2s
                    instructionName = "i2s";
                    break;
                case 96:  // iadd
                    instructionName = "iadd";
                    break;
                case 46:  // iaload
                    instructionName = "iaload";
                    break;
                case 126:  // iand
                    instructionName = "iand";
                    break;
                case 79:  // iastore
                    instructionName = "iastore";
                    break;
                case 2:  // iconst_m1
                    instructionName = "iconst_m1";
                    break;
                case 3:  // iconst_0
                    instructionName = "iconst_0";
                    break;
                case 4:  // iconst_1
                    instructionName = "iconst_1";
                    break;
                case 5:  // iconst_2
                    instructionName = "iconst_2";
                    break;
                case 6:  // iconst_3
                    instructionName = "iconst_3";
                    break;
                case 7:  // iconst_4
                    instructionName = "iconst_4";
                    break;
                case 8:  // iconst_5
                    instructionName = "iconst_5";
                    break;
                case 108:  // idiv
                    instructionName = "idiv";
                    break;
                case 165:  // if_acmpeq
                    instructionName = "if_acmpeq";
                    break;
                case 166:  // if_acmpne
                    instructionName = "if_acmpne";
                    break;
                case 159:  // if_icmpeq
                    instructionName = "if_icmpeq";
                    break;
                case 160:  // if_icmpne
                    instructionName = "if_icmpne";
                    break;
                case 161:  // if_icmplt
                    instructionName = "if_icmplt";
                    break;
                case 162:  // if_icmpge
                    instructionName = "if_icmpge";
                    break;
                case 163:  // if_icmpgt
                    instructionName = "if_icmpgt";
                    break;
                case 164:  // if_icmple
                    instructionName = "if_icmple";
                    break;
                case 153:  // ifeq
                    instructionName = "ifeq";
                    break;
                case 154:  // ifne
                    instructionName = "ifne";
                    break;
                case 155:  // iflt
                    instructionName = "iflt";
                    break;
                case 156:  // ifge
                    instructionName = "ifge";
                    break;
                case 157:  // ifgt
                    instructionName = "ifgt";
                    break;
                case 158:  // ifle
                    instructionName = "ifle";
                    break;
                case 199:  // ifnonnull
                    instructionName = "ifnonnull";
                    break;
                case 198: // ifnull
                    instructionName = "ifnull";
                    break;
                case 132:  // iinc
                    instructionName = "iinc";
                    break;
                case 21:  // iload
                    instructionName = "iload";
                    break;
                case 26:  // iload_0
                    instructionName = "iload_0";
                    break;
                case 27:  // iload_1
                    instructionName = "iload_1";
                    break;
                case 28:  // iload_2
                    instructionName = "iload_2";
                    break;
                case 29:  // iload_3
                    instructionName = "iload_3";
                    break;
                case 104:  // imul
                    instructionName = "imul";
                    break;
                case 116:  // ineg
                    instructionName = "ineg";
                    break;
                case 193:  // instanceof
                    instructionName = "instanceof";
                    break;
                case 185:  // invokeinterface
                    instructionName = "invokeinterface";
                    break;
                case 183:  // invokespecial
                    instructionName = "invokespecial";
                    break;
                case 184:  // invokestatic
                    instructionName = "invokestatic";
                    break;
                case 182:  // invokevirtual
                    instructionName = "invokevirtual";
                    break;
                case 128:  // ior
                    instructionName = "ior";
                    break;
                case 112:  // irem
                    instructionName = "irem";
                    break;
                case 172:  // ireturn
                    instructionName = "ireturn";
                    break;
                case 120:  // ishl
                    instructionName = "ishl";
                    break;
                case 122:  // ishr
                    instructionName = "ishr";
                    break;
                case 54:  // istore
                    instructionName = "istore";
                    break;
                case 59: // istore_0
                    instructionName = "istore_0";
                    break;
                case 60: // istore_1
                    instructionName = "istore_1";
                    break;
                case 61: // istore_2
                    instructionName = "istore_2";
                    break;
                case 62: // istore_3
                    instructionName = "istore_3";
                    break;
                case 100:  // isub
                    instructionName = "isub";
                    break;
                case 124:  // iushr
                    instructionName = "iushr";
                    break;
                case 130:  // ixor
                    instructionName = "ixor";
                    break;
                    
                //---------------------------------------------------
                //     J
                // --------------------------------------------------

                case 168:  // jsr
                    instructionName = "jsr";
                    break;
                case 201:  // jsr_w
                    instructionName = "jsr_w";
                    break;
                    
                    
                //---------------------------------------------------
                //     L
                // --------------------------------------------------

                case 138:  // l2d
                    instructionName = "l2d";
                    break;
                case 137: // l2f
                    instructionName = "l2f";
                    break;
                case 136: // l2i
                    instructionName = "l2i";
                    break;
                case 97: // ladd
                    instructionName = "ladd";
                    break;
                case 47:  // laload
                    instructionName = "laload";
                    break;
                case 127:  // land
                    instructionName = "land";
                    break;
                case 80:  // lastore
                    instructionName = "lastore";
                    break;
                case 148:  // lcmp
                    instructionName = "lcmp";
                    break;
                case 9:  // lconst_0
                    instructionName = "lconst_0";
                    break;
                case 10:  // lconst_1
                    instructionName = "lconst_1";
                    break;
                case 18:  // ldc
                    instructionName = "ldc";
                    break;
                case 19: // ldc_w
                    instructionName = "ldc_w";
                    break;
                case 20:  // ldc2_w
                    instructionName = "ldc2_w";
                    break;
                case 109:  // ldiv
                    instructionName = "ldiv";
                    break;
                case 22:  // lload
                    instructionName = "lload";
                    break;
                case 30:  // lload_0
                    instructionName = "lload_0";
                    break;
                case 31:  // lload_1
                    instructionName = "lload_1";
                    break;
                case 32:  // lload_2
                    instructionName = "lload_2";
                    break;
                case 33:  // lload_3
                    instructionName = "lload_3";
                    break;
                case 105:  // lmul
                    instructionName = "lmul";
                    break;
                case 117:  // lneg
                    instructionName = "lneg";
                    break;
                case 171: // lookupswitch
                    instructionName = "lookupswitch";
                    break;
                case 129:  // lor
                    instructionName = "lor";
                    break;
                case 113:  // lrem
                    instructionName = "lrem";
                    break;
                case 173:  // lreturn
                    instructionName = "lreturn";
                    break;
                case 121:  // lshl
                    instructionName = "lshl";
                    break;
                case 123:  // lshr
                    instructionName = "lshr";
                    break;
                case 55:  // lstore
                    instructionName = "lstore";
                    break;
                case 63:  // lstore_0
                    instructionName = "lstore_0";
                    break;
                case 64:  // lstore_1
                    instructionName = "lstore_1";
                    break;
                case 65:  // lstore_2
                    instructionName = "lstore_2";
                    break;
                case 66:  // lstore_3
                    instructionName = "lstore_3";
                    break;
                case 101:  // lsub
                    instructionName = "lsub";
                    break;
                case 125:  // lushr
                    instructionName = "lushr";
                    break;
                case 131:  // lxor
                    instructionName = "lxor";
                    break;
                    
                //---------------------------------------------------
                //     M
                // --------------------------------------------------

                case 194:  // monitorenter
                    instructionName = "monitorenter";
                    break;
                case 195:  // monitorexit
                    instructionName = "monitorexit";
                    break;
                case 197:  // multianewarray
                    instructionName = "multianewarray";
                    break;
                    
                //---------------------------------------------------
                //     N
                // --------------------------------------------------

                case 187: // new
                    instructionName = "new";
                    break;
                case 188:  // newarray
                    instructionName = "newarray";
                    break;
                case 0:  // nop
                    instructionName = "nop";
                    break;
                    
                    
                    
                //---------------------------------------------------
                //     P
                // --------------------------------------------------
                case 87: // pop
                    instructionName = "pop";
                    break;
                case 88:  // pop2
                    instructionName = "pop2";
                    break;
                case 181: // putfield
                    instructionName = "putfield";
                    break;
                case 179: // putstatic
                    instructionName = "putstatic";
                    break;
                    
                    
                    
                    
                    
                    
                //---------------------------------------------------
                //     R
                // --------------------------------------------------

                case 169:  // ret
                    instructionName = "ret";
                    break;
                case 177: // return
                    instructionName = "return";
                    break;
                    
                    
                //---------------------------------------------------
                //     S
                // --------------------------------------------------

                case 53:  // saload
                    instructionName = "saload";
                    break;
                case 86:  // sastore
                    instructionName = "sastore";
                    break;
                case 17:  // sipush
                    instructionName = "sipush";
                    break;
                case 95:  // swap
                    instructionName = "swap";
                    break;
                    
                    
                    
                    
                    
                //---------------------------------------------------
                //     T
                // --------------------------------------------------

                case 170:  // tableswitch
                    instructionName = "tableswitch";
                    break;
                //---------------------------------------------------
                //     W
                // --------------------------------------------------
                    
                case 196:  // wide
                    instructionName = "wide";
                    break;
                    
                    
                //----------------------------------------------------
                // JVM Native Instructions
                //----------------------------------------------------
                case 254:  // lang
                    instructionName = "lang";
                    break;
                
                case 255:  // peripheral
                    instructionName = "peripheral";
                    break;
                    
                default:
                    // unknown instruction
                    return ("Unknown instruction " + opcode);
                    
                    
            }  // end switch
            
            return instructionName;
    }
    
}
