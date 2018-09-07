/*
 * CodeAttributeInfo.java
 *
 * Created on April 16, 2006, 8:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.classanalyzer.attributes;
import java.util.Vector;
import emr.classanalyzer.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Ross
 */
public class CodeAttributeInfo extends AttributeInfo {

    Attributes attributes; // has LineNumber and LocalVariable Attribute
    
    int max_stack;
    int max_local;
    int code_length;
    
    int code_index;
    
    Vector<String> codeString;
    
    int isize;
    
    int eTableLength;
    int attributesCount;
    
    Vector<ExceptionTableEntry> exceptionTable;
    
    
    /** Creates a new instance of CodeAttributeInfo */
    public CodeAttributeInfo(String name_, int index_, DefaultMutableTreeNode node_, ConstantPool cp) {
        super(name_, index_, node_, cp);
        
        codeString = new Vector<String>();
        exceptionTable = new Vector<ExceptionTableEntry>();
        
        // parse the remainder of the code attribute.
        // index points to the byte following the attribute_length item.
        
        // u2 max_stack
        // u2 max_local
        // u4 code_length
        // u1 code_index[]
        // u2 exception_table_length
        // exception_table[]
        // u2 attributes_count;
        // attribute_info attributes[]
        
        max_stack = ((int)data[index] << 8) & 0x0000FF00;
        max_stack |= ((int)data[index+1]) & 0x000000FF;
        
        index += 2;
        
        max_local = ((int)data[index] << 8) & 0x0000FF00;
        max_local |= ((int)data[index+1]) & 0x000000FF;
        
        index += 2;
 
        code_length = ((int)data[index] << 24) & 0xFF000000;
        code_length |= ((int)data[index+1] << 16) & 0x00FF0000;
        code_length |= ((int)data[index+2] << 8) & 0x0000FF00;
        code_length |= ((int)data[index+3]) & 0x000000FF;
        
        index += 4;
        
        code_index = index; // set up the code_index to point at the first byte of code.
        
        parseCode();
        
        index += code_length; // move to exception_table_length
        
        eTableLength |= ((int)data[index] << 8) & 0x0000FF00;
        eTableLength |= ((int)data[index + 1])  & 0x000000FF;
        
        index += 2;

        // parse the exception table
        for(int i = 0; i < eTableLength; i++) {
            
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode();
            childNode.setUserObject(new ExceptionTableEntry(data, index, constantPool));
            node.add(childNode);
            //exceptionTable.add(new ExceptionTableEntry(data, index));
            index += 8;
        }
        
        //index += 8 * eTableLength; // pass the exception table array
 
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode();
        node.add(childNode);
        attributes = new Attributes(data, childNode, constantPool);
        
        attributes.parseAttributes(index);
        attributesCount = attributes.getCount();
        index = attributes.getNextIndex();
        
    }
    
    // parse code
    private void parseCode() {
        // parse code with a switch table
        isize = 0;
         
        int opcode;
        int pc = code_index;
        
        Log.event("---- parsing code, index = " + index + " code_length = " + code_length);
        // initiate code engine
        while (pc < code_index + code_length)
        {
            
            opcode = data[pc] & 0x000000FF; // damn sign extending!
            Log.event("---- opcode: " + opcode);
            // branch based on opcode
            switch(opcode)
            {
            
                //---------------------------------------------------
                //     A
                // --------------------------------------------------
                
                case 50: // aaload
                    parseOpcode(pc, 1, "aaload");
                    break;
                case 83: // aastore
                    parseOpcode(pc, 1, "aastore");
                    break;
                case 1:  // aconst_null
                    parseOpcode(pc, 1, "aconst_null");
                    break;
                case 25:  // aload
                    parseOpcode(pc, 2, "aload");
                    break;
                case 42:  // aload_0
                    parseOpcode(pc, 1, "aload_0");
                    break;
                case 43:  // aload_1
                    parseOpcode(pc, 1, "aload_1");
                    break;
                case 44:  // aload_2
                    parseOpcode(pc, 1, "aload_2");
                    break;
                case 45:  // aload_3
                    parseOpcode(pc, 1, "aload_3");
                    break;
                case 189:  // anewarray
                    parseOpcode(pc, 3, "anewarray");
                    break;
                case 176:  // areturn
                    parseOpcode(pc, 1, "areturn");
                    break;
                case 190:  // arraylength
                    parseOpcode(pc, 1, "arraylength");
                    break;
                case 58:  // astore
                    parseOpcode(pc, 2, "astore");
                    break;
                case 75:  // astore_0
                    parseOpcode(pc, 1, "astore_0");
                    break;
                case 76: // astore_1
                    parseOpcode(pc, 1, "astore_1");
                    break;
                case 77:  // astore_2
                    parseOpcode(pc, 1, "astore_2");
                    break;
                case 78:  // astore_3
                    parseOpcode(pc, 1, "astore_3");
                    break;
                case 191:  // athrow
                    parseOpcode(pc, 1, "athrow");
                    break;
                    
                //---------------------------------------------------
                //     B
                // --------------------------------------------------
                    
                case 51:  // baload
                    parseOpcode(pc, 1, "baload");
                    break;
                case 84:  // bastore
                    parseOpcode(pc, 1, "bastore");
                    break;
                case 16:  // bipush
                    parseOpcode(pc, 2, "bipush");
                    break;
                    
                //---------------------------------------------------
                //     C
                // --------------------------------------------------
                    
                case 52:  // caload
                    parseOpcode(pc, 1, "caload");
                    break;
                case 85:  // castore
                    parseOpcode(pc, 1, "castore");
                    break;
                case 192: // checkcast
                    parseOpcode(pc, 3, "checkcast");
                    break; 
                //---------------------------------------------------
                //     D
                // --------------------------------------------------
                
                case 144:  // d2f
                    parseOpcode(pc, 1, "d2f");
                    break;
                case 142:  // d2i
                    parseOpcode(pc, 1, "d2i");
                    break;
                case 143:  // d2l
                    parseOpcode(pc, 1, "d2l");
                    break;
                case 99:  // dadd
                    parseOpcode(pc, 1, "dadd");
                    break;
                case 49:  // daload
                    parseOpcode(pc, 1, "daload");
                    break;
                case 82:  // dastore
                    parseOpcode(pc, 1, "dastore");
                    break;
                case 152:  // dcmpg
                    parseOpcode(pc, 1, "dcmpg");
                    break;
                case 151:  // dcmpl
                    parseOpcode(pc, 1, "dcmpl");
                    break;
                case 14:  // dconst_0
                    parseOpcode(pc, 1, "dconst_0");
                    break;
                case 15:  // dconst_1
                    parseOpcode(pc, 1, "dconst_1");
                    break;
                case 111:  // ddiv
                    parseOpcode(pc, 1, "ddiv");
                    break;
                case 24:  // dload
                    parseOpcode(pc, 2, "dload");
                    break;
                case 38:  // dload_0
                    parseOpcode(pc, 1, "dload_0");
                    break;
                case 39:  // dload_1
                    parseOpcode(pc, 1, "dload_1");
                    break;
                case 40:  // dload_2
                    parseOpcode(pc, 1, "dload_2");
                    break;
                case 41:  // dload_3
                    parseOpcode(pc, 1, "dload_3");
                    break;
                case 107:  // dmul
                    parseOpcode(pc, 1, "dmul");
                    break;
                case 119:  // dneg
                    parseOpcode(pc, 1, "dneg");
                    break;
                case 115:  // drem
                    parseOpcode(pc, 1, "drem");
                    break;
                case 175:  // dreturn
                    parseOpcode(pc, 1, "dreturn");
                    break;
                case 57:  // dstore
                    parseOpcode(pc, 2, "dstore");
                    break;
                case 71:  // dstore_0
                    parseOpcode(pc, 1, "dstore_0");
                    break;
                case 72:  // dstore_1
                    parseOpcode(pc, 1, "dstore_1");
                    break;
                case 73:  // dstore_2
                    parseOpcode(pc, 1, "dstore_2");
                    break;
                case 74:  // dstore_3
                    parseOpcode(pc, 1, "dstore_3");
                    break;
                case 103:  // dsub
                    parseOpcode(pc, 1, "dsub");
                    break;
                case 89:  // dup
                    parseOpcode(pc, 1, "dup");
                    break;
                case 90:  // dup_x1
                    parseOpcode(pc, 1, "dup_x1");
                    break;
                case 91:  // dup_x2
                    parseOpcode(pc, 1, "dup_x2");
                    break;
                case 92:  // dup2
                    parseOpcode(pc, 1, "dup2");
                    break;
                case 93:  // dup2_x1
                    parseOpcode(pc, 1, "dup2_x1");
                    break;
                case 94:  // dup2_x2
                    parseOpcode(pc, 1, "dup2_x2");
                    break;
                    
                //---------------------------------------------------
                //     F
                // --------------------------------------------------

                case 141:  // f2d
                    parseOpcode(pc, 1, "f2d");
                    break;
                case 139:  // f2i
                    parseOpcode(pc, 1, "f2i");
                    break;
                case 140:  // f2l
                    parseOpcode(pc, 1, "f2l");
                    break;
                case 98:  // fadd
                    parseOpcode(pc, 1, "fadd");
                    break;
                case 48:  // faload
                    parseOpcode(pc, 1, "faload");
                    break;
                case 81: // fastore
                    parseOpcode(pc, 1, "fastore");
                    break;
                case 150:  // fcmpg
                    parseOpcode(pc, 1, "fcmpg");
                    break;
                case 149:  // fcmpl
                    parseOpcode(pc, 1, "fcmpl");
                    break;
                case 11:  // fconst_0
                    parseOpcode(pc, 1, "fconst_0");
                    break;
                case 12:  // fconst_1
                    parseOpcode(pc, 1, "fconst_1");
                    break;
                case 13:  // fconst_2
                    parseOpcode(pc, 1, "fconst_2");
                    break;
                case 110:  // fdiv
                    parseOpcode(pc, 1, "fdiv");
                    break;
                case 23:  // fload
                    parseOpcode(pc, 2, "fload");
                    break;
                case 34:  // fload_0
                    parseOpcode(pc, 1, "fload_0");
                    break;
                case 35:  // fload_1
                    parseOpcode(pc, 1, "fload_1");
                    break;
                case 36:  // fload_2
                    parseOpcode(pc, 1, "fload_2");
                    break;
                case 37:  // fload_3
                    parseOpcode(pc, 1, "fload_3");
                    break;
                case 106:  // fmul
                    parseOpcode(pc, 1, "fmul");
                    break;
                case 118:  // fneg
                    parseOpcode(pc, 1, "fneg");
                    break;
                case 114:  // frem
                    parseOpcode(pc, 1, "frem");
                    break;
                case 174:  // freturn
                    parseOpcode(pc, 1, "freturn");
                    break;
                case 56:  // fstore
                    parseOpcode(pc, 2, "fstore");
                    break;
                case 67:  // fstore_0
                    parseOpcode(pc, 1, "fstore_0");
                    break;
                case 68:  // fstore_1
                    parseOpcode(pc, 1, "fstore_1");
                    break;
                case 69:  // fstore_2
                    parseOpcode(pc, 1, "fstore_2");
                    break;
                case 70:  // fstore_3
                    parseOpcode(pc, 1, "fstore_3");
                    break;
                case 102:  // fsub
                    parseOpcode(pc, 1, "fsub");
                    break;
                    
                    
                //---------------------------------------------------
                //     G
                // --------------------------------------------------

                case 180:  // getfield
                    parseOpcode(pc, 3, "getfield");
                    break;
                case 178:  // getstatic
                    parseOpcode(pc, 3, "getstatic");
                    break;
                case 167:  // goto
                    parseOpcode(pc, 3, "goto");
                    break;
                case 200:  // goto_w
                    parseOpcode(pc, 5, "goto_w");
                    break;
                    
                //---------------------------------------------------
                //     I
                // --------------------------------------------------
 
                case 145:  // i2b
                    parseOpcode(pc, 1, "i2b");
                    break;
                case 146:  // i2c
                    parseOpcode(pc, 1, "i2c");
                    break;
                case 135:  // i2d
                    parseOpcode(pc, 1, "i2d");
                    break;
                case 134:  // i2f
                    parseOpcode(pc, 1, "i2f");
                    break;
                case 133:  // i2l
                    parseOpcode(pc, 1, "i2l");
                    break;
                case 147:  // i2s
                    parseOpcode(pc, 1, "i2s");
                    break;
                case 96:  // iadd
                    parseOpcode(pc, 1, "iadd");
                    break;
                case 46:  // iaload
                    parseOpcode(pc, 1, "iaload");
                    break;
                case 126:  // iand
                    parseOpcode(pc, 1, "iand");
                    break;
                case 79:  // iastore
                    parseOpcode(pc, 1, "iastore");
                    break;
                case 2:  // iconst_m1
                    parseOpcode(pc, 1, "iconst_m1");
                    break;
                case 3:  // iconst_0
                    parseOpcode(pc, 1, "iconst_0");
                    break;
                case 4:  // iconst_1
                    parseOpcode(pc, 1, "iconst_1");
                    break;
                case 5:  // iconst_2
                    parseOpcode(pc, 1, "iconst_2");
                    break;
                case 6:  // iconst_3
                    parseOpcode(pc, 1, "iconst_3");
                    break;
                case 7:  // iconst_4
                    parseOpcode(pc, 1, "iconst_4");
                    break;
                case 8:  // iconst_5
                    parseOpcode(pc, 1, "iconst_5");
                    break;
                case 108:  // idiv
                    parseOpcode(pc, 1, "idiv");
                    break;
                case 165:  // if_acmpeq
                    parseOpcode(pc, 3, "if_acmpeq");
                    break;
                case 166:  // if_acmpne
                    parseOpcode(pc, 3, "if_acmpne");
                    break;
                case 159:  // if_icmpeq
                    parseOpcode(pc, 3, "if_icmpeq");
                    break;
                case 160:  // if_icmpne
                    parseOpcode(pc, 3, "if_icmpne");
                    break;
                case 161:  // if_icmplt
                    parseOpcode(pc, 3, "if_icmplt");
                    break;
                case 162:  // if_icmpge
                    parseOpcode(pc, 3, "if_icmpge");
                    break;
                case 163:  // if_icmpgt
                    parseOpcode(pc, 3, "if_icmpgt");
                    break;
                case 164:  // if_icmple
                    parseOpcode(pc, 3, "if_icmple");
                    break;
                case 153:  // ifeq
                    parseOpcode(pc, 3, "ifeq");
                    break;
                case 154:  // ifne
                    parseOpcode(pc, 3, "ifne");
                    break;
                case 155:  // iflt
                    parseOpcode(pc, 3, "iflt");
                    break;
                case 156:  // ifge
                    parseOpcode(pc, 3, "ifge");
                    break;
                case 157:  // ifgt
                    parseOpcode(pc, 3, "ifgt");
                    break;
                case 158:  // ifle
                    parseOpcode(pc, 3, "ifle");
                    break;
                case 199:  // ifnonnull
                    parseOpcode(pc, 3, "ifnonnull");
                    break;
                case 198: // ifnull
                    parseOpcode(pc, 3, "ifnull");
                    break;
                case 132:  // iinc
                    parseOpcode(pc, 3, "iinc");
                    break;
                case 21:  // iload
                    parseOpcode(pc, 2, "iload");
                    break;
                case 26:  // iload_0
                    parseOpcode(pc, 1, "iload_0");
                    break;
                case 27:  // iload_1
                    parseOpcode(pc, 1, "iload_1");
                    break;
                case 28:  // iload_2
                    parseOpcode(pc, 1, "iload_2");
                    break;
                case 29:  // iload_3
                    parseOpcode(pc, 1, "iload_3");
                    break;
                case 104:  // imul
                    parseOpcode(pc, 1, "imul");
                    break;
                case 116:  // ineg
                    parseOpcode(pc, 1, "ineg");
                    break;
                case 193:  // instanceof
                    parseOpcode(pc, 3, "instanceof");
                    break;
                case 185:  // invokeinterface
                    parseOpcode(pc, 5, "invokeinterface");
                    break;
                case 183:  // invokespecial
                    parseOpcode(pc, 3, "invokespecial");
                    break;
                case 184:  // invokestatic
                    parseOpcode(pc, 3, "invokestatic");
                    break;
                case 182:  // invokevirtual
                    parseOpcode(pc, 3, "invokevirtual");
                    break;
                case 128:  // ior
                    parseOpcode(pc, 1, "ior");
                    break;
                case 112:  // irem
                    parseOpcode(pc, 1, "irem");
                    break;
                case 172:  // ireturn
                    parseOpcode(pc, 1, "ireturn");
                    break;
                case 120:  // ishl
                    parseOpcode(pc, 1, "ishl");
                    break;
                case 122:  // ishr
                    parseOpcode(pc, 1, "ishr");
                    break;
                case 54:  // istore
                    parseOpcode(pc, 2, "istore");
                    break;
                case 59: // istore_0
                    parseOpcode(pc, 1, "istore_0");
                    break;
                case 60: // istore_1
                    parseOpcode(pc, 1, "istore_1");
                    break;
                case 61: // istore_2
                    parseOpcode(pc, 1, "istore_2");
                    break;
                case 62: // istore_3
                    parseOpcode(pc, 1, "istore_3");
                    break;
                case 100:  // isub
                    parseOpcode(pc, 1, "isub");
                    break;
                case 124:  // iushr
                    parseOpcode(pc, 1, "iushr");
                    break;
                case 130:  // ixor
                    parseOpcode(pc, 1, "ixor");
                    break;
                    
                //---------------------------------------------------
                //     J
                // --------------------------------------------------

                case 168:  // jsr
                    parseOpcode(pc, 3, "jsr");
                    break;
                case 201:  // jsr_w
                    parseOpcode(pc, 5, "jsr_w");
                    break;
                    
                    
                //---------------------------------------------------
                //     L
                // --------------------------------------------------

                case 138:  // l2d
                    parseOpcode(pc, 1, "l2d");
                    break;
                case 137: // l2f
                    parseOpcode(pc, 1, "l2f");
                    break;
                case 136: // l2i
                    parseOpcode(pc, 1, "l2i");
                    break;
                case 97: // ladd
                    parseOpcode(pc, 1, "ladd");
                    break;
                case 47:  // laload
                    parseOpcode(pc, 1, "laload");
                    break;
                case 127:  // land
                    parseOpcode(pc, 1, "land");
                    break;
                case 80:  // lastore
                    parseOpcode(pc, 1, "lastore");
                    break;
                case 148:  // lcmp
                    parseOpcode(pc, 1, "lcmp");
                    break;
                case 9:  // lconst_0
                    parseOpcode(pc, 1, "lconst_0");
                    break;
                case 10:  // lconst_1
                    parseOpcode(pc, 1, "lconst_1");
                    break;
                case 18:  // ldc
                    parseOpcode(pc, 2, "ldc");
                    break;
                case 19: // ldc_w
                    parseOpcode(pc, 3, "ldc_w");
                    break;
                case 20:  // ldc2_w
                    parseOpcode(pc, 3, "ldc2_w");
                    break;
                case 109:  // ldiv
                    parseOpcode(pc, 1, "ldiv");
                    break;
                case 22:  // lload
                    parseOpcode(pc, 2, "lload");
                    break;
                case 30:  // lload_0
                    parseOpcode(pc, 1, "lload_0");
                    break;
                case 31:  // lload_1
                    parseOpcode(pc, 1, "lload_1");
                    break;
                case 32:  // lload_2
                    parseOpcode(pc, 1, "lload_2");
                    break;
                case 33:  // lload_3
                    parseOpcode(pc, 1, "lload_3");
                    break;
                case 105:  // lmul
                    parseOpcode(pc, 1, "lmul");
                    break;
                case 117:  // lneg
                    parseOpcode(pc, 1, "lneg");
                    break;
                case 171: // lookupswitch
                    parseLookupswitch(pc);
                    break;
                case 129:  // lor
                    parseOpcode(pc, 1, "lor");
                    break;
                case 113:  // lrem
                    parseOpcode(pc, 1, "lrem");
                    break;
                case 173:  // lreturn
                    parseOpcode(pc, 1, "lreturn");
                    break;
                case 121:  // lshl
                    parseOpcode(pc, 1, "lshl");
                    break;
                case 123:  // lshr
                    parseOpcode(pc, 1, "lshr");
                    break;
                case 55:  // lstore
                    parseOpcode(pc, 2, "lstore");
                    break;
                case 63:  // lstore_0
                    parseOpcode(pc, 1, "lstore_0");
                    break;
                case 64:  // lstore_1
                    parseOpcode(pc, 1, "lstore_1");
                    break;
                case 65:  // lstore_2
                    parseOpcode(pc, 1, "lstore_2");
                    break;
                case 66:  // lstore_3
                    parseOpcode(pc, 1, "lstore_3");
                    break;
                case 101:  // lsub
                    parseOpcode(pc, 1, "lsub");
                    break;
                case 125:  // lushr
                    parseOpcode(pc, 1, "lushr");
                    break;
                case 131:  // lxor
                    parseOpcode(pc, 1, "lxor");
                    break;
                    
                //---------------------------------------------------
                //     M
                // --------------------------------------------------

                case 194:  // monitorenter
                    parseOpcode(pc, 1, "monitorenter");
                    break;
                case 195:  // monitorexit
                    parseOpcode(pc, 1, "monitorexit");
                    break;
                case 197:  // multianewarray
                    parseOpcode(pc, 4, "multianewarray");
                    break;
                    
                //---------------------------------------------------
                //     N
                // --------------------------------------------------

                case 187: // new
                    parseOpcode(pc, 3, "new");
                    break;
                case 188:  // newarray
                    parseOpcode(pc, 2, "newarray");
                    break;
                case 0:  // nop
                    parseOpcode(pc, 1, "nop");
                    break;
                    
                    
                    
                //---------------------------------------------------
                //     P
                // --------------------------------------------------
                case 87: // pop
                    parseOpcode(pc, 1, "pop");
                    break;
                case 88:  // pop2
                    parseOpcode(pc, 1, "pop2");
                    break;
                case 181: // putfield
                    parseOpcode(pc, 3, "putfield");
                    break;
                case 179: // putstatic
                    parseOpcode(pc, 3, "putstatic");
                    break;
                    
                    
                    
                    
                    
                    
                //---------------------------------------------------
                //     R
                // --------------------------------------------------

                case 169:  // ret
                    parseOpcode(pc, 2, "ret");
                    break;
                case 177: // return
                    parseOpcode(pc, 1, "return");
                    break;
                    
                    
                //---------------------------------------------------
                //     S
                // --------------------------------------------------

                case 53:  // saload
                    parseOpcode(pc, 1, "saload");
                    break;
                case 86:  // sastore
                    parseOpcode(pc, 1, "sastore");
                    break;
                case 17:  // sipush
                    parseOpcode(pc, 3, "sipush");
                    break;
                case 95:  // swap
                    parseOpcode(pc, 1, "swap");
                    break;
                    
                    
                    
                    
                    
                //---------------------------------------------------
                //     T
                // --------------------------------------------------

                case 170:  // tableswitch
                    parseTableswitch(pc);
                    break;
                //---------------------------------------------------
                //     W
                // --------------------------------------------------
                    
                case 196:  // wide
                    if(((int)data[pc+1] & 0x000000FF) == 132)  // iinc 
                        parseOpcode(pc, 6, "wide2");
                    else
                        parseOpcode(pc, 4, "wide1");
                    break;
                    
                    
                
                default:
                    parseOpcode(pc, 1, "unknown " + opcode);
                    break;
            }
            
            pc += isize;
        }
        
        
        
    }
    
    
    private void parseOpcode(int pc_, int size_, String opCode_) {
        codeString.add((pc_ - code_index) + "  " + opCode_ + "\n");
        for(int i = 1; i < size_; i++) 
        {
            codeString.add("        " + data[pc_ + i] + "\n");
        }
        isize = size_;
        
    }
    
    private void parseLookupswitch(int pc_) {
        int startpc = pc_;
        codeString.add((pc_ - code_index) + "  lookupswitch\n");
 
        pc_++; // increment to next byte

        // now, is pc multiple of 4? if not, how many pad bytes to expect?
        
        pc_ += (pc_ - code_index) % 4;
         
        
        int def;
        def = ((int)data[pc_++] << 24) & 0xFF000000;
        def |= ((int)data[pc_++] << 16) & 0x00FF0000;
        def |= ((int)data[pc_++] << 8) & 0x0000FF00;
        def |= ((int)data[pc_++]) & 0x000000FF;
        codeString.add("\tdefault: " + def + "\n");
        
        int npairs;
        npairs = ((int)data[pc_++] << 24) & 0xFF000000;
        npairs |= ((int)data[pc_++] << 16) & 0x00FF0000;
        npairs |= ((int)data[pc_++] << 8) & 0x0000FF00;
        npairs |= ((int)data[pc_++]) & 0x000000FF;
        codeString.add("\tnpairs: " + npairs + "\n");
        
        int match[] = new int[npairs];
        int offset[] = new int[npairs];
        
        for(int i = 0; i < npairs; i++) {
            match[i] = ((int)data[pc_++] << 24) & 0xFF000000;
            match[i] |= ((int)data[pc_++] << 16) & 0x00FF0000;
            match[i] |= ((int)data[pc_++] << 8) & 0x0000FF00;
            match[i] |= ((int)data[pc_++]) & 0x000000FF;
        
            offset[i] = ((int)data[pc_++] << 24) & 0xFF000000;
            offset[i] |= ((int)data[pc_++] << 16) & 0x00FF0000;
            offset[i] |= ((int)data[pc_++] << 8) & 0x0000FF00;
            offset[i] |= ((int)data[pc_++]) & 0x000000FF;
            
            codeString.add("\tmatch: " + match[i] + " offset: " + offset[i] + "\n");
            
        }
        
        isize = pc_ - startpc;
        
    }
    
    private void parseTableswitch(int pc_) {
        // pc is at the opcode here
        int startpc = pc_;
        codeString.add((pc_ - code_index) + "  tableswitch\n");
        
        
        pc_++; // increment to next byte

        // now, is pc multiple of 4? if not, how many pad bytes to expect?
        
        pc_ += (pc_ - code_index) % 4;
        
        Log.event("parseTableswitch pad bytes: " + ((pc_ - code_index) % 4));
        
        
        // pc_ should point to next non-null byte
        
        int def;
        def = ((int)data[pc_] << 24) & 0xFF000000;
        def |= ((int)data[pc_ + 1] << 16) & 0x00FF0000;
        def |= ((int)data[pc_ + 2] << 8) & 0x0000FF00;
        def |= ((int)data[pc_ + 3]) & 0x000000FF;
        codeString.add("\tdefault: " + def + "\n");
        Log.event("parseTableswitch def = " + def);
        
        pc_ += 4;
        
        int low;
        low = ((int)data[pc_] << 24) & 0xFF000000;
        low |= ((int)data[pc_ + 1] << 16) & 0x00FF0000;
        low |= ((int)data[pc_ + 2] << 8) & 0x0000FF00;
        low |= ((int)data[pc_ + 3]) & 0x000000FF;
        codeString.add("\tlow: " + low + "\n");
        Log.event("parseTableswitch low = " + low);
        
        pc_ += 4;
        
        int high;
        high = ((int)data[pc_] << 24) & 0xFF000000;
        high |= ((int)data[pc_ + 1] << 16) & 0x00FF0000;
        high |= ((int)data[pc_ + 2] << 8) & 0x0000FF00;
        high |= ((int)data[pc_ + 3]) & 0x000000FF;
        codeString.add("\thigh: " + high + "\n");
        Log.event("parseTableswitch high = " + high);

        pc_ += 4;
        
        int offset[] = new int[high - low + 1];
        Log.event("parseTableswitch high-low+1 = " + (high-low+1));

        for(int i = 0; i < high - low + 1; i++) {
        
            offset[i] = ((int)data[pc_] << 24) & 0xFF000000;
            offset[i] |= ((int)data[pc_ + 1] << 16) & 0x00FF0000;
            offset[i] |= ((int)data[pc_ + 2] << 8) & 0x0000FF00;
            offset[i] |= ((int)data[pc_ + 3]) & 0x000000FF;
            
            codeString.add("\toffset: " + offset[i] + "\n");
            pc_ += 4;
        }
        
        isize = pc_ - startpc;
        
    }

    
    
    public Vector<String> displayContents() {
        Vector<String> v = new Vector<String>();
        
        v.add("Code attribute\n\n");
        v.add("max_stack : " + max_stack + "\n");
        v.add("max local : " + max_local + "\n");
        v.add("code length : " + code_length + "\n\n");
        v.add("exception table entries : " + eTableLength + "\n");
        v.add("attributes count : " + attributesCount + "\n\n");
        v.add("Code:\n\n");
        v.add("pc   mnemonic\n");
        v.add("-----------------------\n");
        v.addAll(codeString);
        return v;
         
    }
    
}
