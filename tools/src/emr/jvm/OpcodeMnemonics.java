/*
 * OpcodeMnemonics.java
 *
 * Created on October 25, 2006, 8;27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.jvm;

/**
 *
 * @author Ross
 */
public interface OpcodeMnemonics 
{
    
    public static final int aaload = 50; // aaload
    
    public static final int aastore = 83; // aastore

    public static final int aconst_null = 1;  // aconst_null

    public static final int aload =  25;  // aload

    
    public static final int aload_0 = 42;  // aload_0

    public static final int aload_1 = 43;  // aload_1
    
    public static final int aload_2 = 44;  // aload_2

    public static final int aload_3 = 45;  // aload_3

    public static final int anewarray = 189;  // anewarray
    
    
    public static final int areturn = 176;  // areturn

    public static final int arraylength = 190;  // arraylength

    public static final int astore = 58;  // astore

    public static final int astore_0 = 75;  // astore_0

    public static final int astore_1 = 76; // astore_1
                
    public static final int astore_2 = 77;  // astore_2

    public static final int astore_3 = 78;  // astore_3

    public static final int athrow = 191;  // athrow
                    
    //---------------------------------------------------
    //     B
    // --------------------------------------------------
                    
    public static final int baload = 51;  // baload

    public static final int bastore = 84;  // bastore

    public static final int bipush = 16;  // bipush
                    
    //---------------------------------------------------
    //     C
    // --------------------------------------------------

    public static final int caload = 52;  // caload

    public static final int castore = 85;  // castore

    public static final int checkcast = 192; // checkcast

    //---------------------------------------------------
    //     D
    // --------------------------------------------------

    public static final int d2f = 144;  // d2f

    public static final int d2i = 142;  // d2i

    public static final int d2l = 143;  // d2l

    public static final int dadd = 99;  // dadd

    public static final int daload = 49;  // daload

    public static final int dastore = 82;  // dastore

    public static final int dcmpg = 152;  // dcmpg

    public static final int dcmpl = 151;  // dcmpl

    public static final int dconst_0 = 14;  // dconst_0

    public static final int dconst_1 = 15;  // dconst_1

    public static final int ddiv = 111;  // ddiv

    public static final int dload = 24;  // dload

    public static final int dload_0 = 38;  // dload_0

    public static final int dload_1 = 39;  // dload_1

    public static final int dload_2 = 40;  // dload_2

    public static final int dload_3 = 41;  // dload_3

    public static final int dmul = 107;  // dmul

    public static final int dneg = 119;  // dneg

    public static final int drem = 115;  // drem

    public static final int dreturn = 175;  // dreturn

    public static final int dstore = 57;  // dstore

    public static final int dstore_0 = 71;  // dstore_0

    public static final int dstore_1 = 72;  // dstore_1

    public static final int dstore_2 = 73;  // dstore_2

    public static final int dstore_3 = 74;  // dstore_3

    public static final int dsub = 103;  // dsub

    public static final int dup = 89;  // dup

    public static final int dup_x1 = 90;  // dup_x1

    public static final int dup_x2 = 91;  // dup_x2

    public static final int dup2 = 92;  // dup2

    public static final int dup2_x1 = 93;  // dup2_x1

    public static final int dup2_x2 = 94;  // dup2_x2


    //---------------------------------------------------
    //     F
    // --------------------------------------------------

    public static final int f2d = 141;  // f2d

    public static final int f2i = 139;  // f2i

    public static final int f2l = 140;  // f2l

    public static final int fadd = 98;  // fadd

    public static final int faload = 48;  // faload

    public static final int fastore = 81; // fastore

    public static final int fcmpg = 150;  // fcmpg

    public static final int fcmpg1 = 149;  // fcmpl

    public static final int fconst_0 = 11;  // fconst_0

    public static final int fconst_1 = 12;  // fconst_1

    public static final int fconst_2 = 13;  // fconst_2

    public static final int fdiv = 110;  // fdiv

    public static final int fload = 23;  // fload

    public static final int fload_0 = 34;  // fload_0

    public static final int fload_1 = 35;  // fload_1

    public static final int fload_2 = 36;  // fload_2

    public static final int fload_3 = 37;  // fload_3

    public static final int fmul = 106;  // fmul

    public static final int fneg = 118;  // fneg

    public static final int frem = 114;  // frem

    public static final int freturn = 174;  // freturn

    public static final int fstore = 56;  // fstore

    public static final int fstore_0 = 67;  // fstore_0

    public static final int fstore_1 = 68;  // fstore_1

    public static final int fstore_2 = 69;  // fstore_2

    public static final int fstore_3 = 70;  // fstore_3

    public static final int fsub = 102;  // fsub



    //---------------------------------------------------
    //     G
    // --------------------------------------------------

    public static final int getfield = 180;  // getfield

    public static final int getstatic = 178;  // getstatic

    public static final int goto_n = 167;  // goto (normal)

    public static final int goto_w = 200;  // goto_w


    //---------------------------------------------------
    //     I
    // --------------------------------------------------

    public static final int i2b = 145;  // i2b

    public static final int i2c = 146;  // i2c

    public static final int i2d = 135;  // i2d

    public static final int i2f = 134;  // i2f

    public static final int i2l = 133;  // i2l

    public static final int i2s = 147;  // i2s

    public static final int iadd = 96;  // iadd

    public static final int iaload = 46;  // iaload

    public static final int iand = 126;  // iand

    public static final int iastore = 79;  // iastore

    public static final int iconst_m1 = 2;  // iconst_m1

    public static final int iconst_0 = 3;  // iconst_0

    public static final int icsont_1 = 4;  // iconst_1

    public static final int iconsnt_2 = 5;  // iconst_2

    public static final int iconst_3 = 6;  // iconst_3

    public static final int iconst_4 = 7;  // iconst_4

    public static final int iconst_5 = 8;  // iconst_5

    public static final int idiv = 108;  // idiv

    public static final int if_acmpeq = 165;  // if_acmpeq

    public static final int if_acmpne = 166;  // if_acmpne

    public static final int if_icmpeq = 159;  // if_icmpeq

    public static final int if_icmpne = 160;  // if_icmpne

    public static final int if_icmplt = 161;  // if_icmplt

    public static final int if_icmpge = 162;  // if_icmpge

    public static final int if_icmpgt = 163;  // if_icmpgt

    public static final int if_icmple = 164;  // if_icmple

    public static final int ifeq = 153;  // ifeq

    public static final int ifne = 154;  // ifne

    public static final int iflt = 155;  // iflt

    public static final int ifge = 156;  // ifge

    public static final int ifgt = 157;  // ifgt

    public static final int ifle = 158;  // ifle

    public static final int ifnonnull = 199;  // ifnonnull

    public static final int ifnull = 198; // ifnull

    public static final int iinc = 132;  // iinc

    public static final int iload = 21;  // iload

    public static final int iload_0 = 26;  // iload_0

    public static final int iload_1 = 27;  // iload_1

    public static final int iload_2 = 28;  // iload_2

    public static final int iload_3 = 29;  // iload_3

    public static final int imul = 104;  // imul

    public static final int ineg = 116;  // ineg

    public static final int instanceof_ = 193;  // instanceof (instruction)

    public static final int invokeinterface = 185;  // invokeinterface

    public static final int invokespecial = 183;  // invokespecial

    public static final int invokestastic = 184;  // invokestatic

    public static final int invokevirtual = 182;  // invokevirtual

    public static final int ior = 128;  // ior

    public static final int irem = 112;  // irem

    public static final int ireturn = 172;  // ireturn

    public static final int ishl = 120;  // ishl

    public static final int ishr = 122;  // ishr

    public static final int istore = 54;  // istore

    public static final int istore_0 = 59; // istore_0

    public static final int istore_1 = 60; // istore_1

    public static final int istore_2 = 61; // istore_2

    public static final int istore_3 = 62; // istore_3

    public static final int isub = 100;  // isub

    public static final int iushr = 124;  // iushr

    public static final int ixor = 130;  // ixor


    //---------------------------------------------------
    //     J
    // --------------------------------------------------

    public static final int jsr = 168;  // jsr

    public static final int jsr_w = 201;  // jsr_w



    //---------------------------------------------------
    //     L
    // --------------------------------------------------

    public static final int l2d = 138;  // l2d

    public static final int l2f = 137; // l2f

    public static final int l2i = 136; // l2i

    public static final int ladd = 97; // ladd

    public static final int laload = 47;  // laload

    public static final int land = 127;  // land

    public static final int lastore = 80;  // lastore

    public static final int lcmp = 148;  // lcmp

    public static final int lconst_0 = 9;  // lconst_0

    public static final int lconst_1 = 10;  // lconst_1

    public static final int ldc = 18;  // ldc

    public static final int ldc_w = 19; // ldc_w

    public static final int ldc_2 = 20;  // ldc2_w

    public static final int ldiv = 109;  // ldiv

    public static final int lload = 22;  // lload

    public static final int lload_0 = 30;  // lload_0

    public static final int lload_1 = 31;  // lload_1

    public static final int lload_2 = 32;  // lload_2

    public static final int lload_3 = 33;  // lload_3

    public static final int lmul = 105;  // lmul

    public static final int lneg = 117;  // lneg

    public static final int lookupswitch = 171; // lookupswitch

    public static final int lor = 129;  // lor

    public static final int lrem = 113;  // lrem

    public static final int lreturn = 173;  // lreturn

    public static final int lshl = 121;  // lshl

    public static final int lshr = 123;  // lshr

    public static final int lstore = 55;  // lstore

    public static final int lstore_0 = 63;  // lstore_0

    public static final int lstore_1 = 64;  // lstore_1

    public static final int lstore_2 = 65;  // lstore_2

    public static final int lstore_3 = 66;  // lstore_3

    public static final int lsub = 101;  // lsub

    public static final int lushr = 125;  // lushr

    public static final int lxor = 131;  // lxor


    //---------------------------------------------------
    //     M
    // --------------------------------------------------

    public static final int monitorenter = 194;  // monitorenter

    public static final int monitorexit = 195;  // monitorexit

    public static final int multianewarray = 197;  // multianewarray


    //---------------------------------------------------
    //     N
    // --------------------------------------------------

    public static final int new_ = 187; // new

    public static final int newarray = 188;  // newarray

    public static final int nop = 0;  // nop




    //---------------------------------------------------
    //     P
    // --------------------------------------------------
    public static final int pop = 87; // pop

    public static final int pop2 = 88;  // pop2

    public static final int putfield = 181; // putfield

    public static final int pustatic = 179; // putstatic







    //---------------------------------------------------
    //     R
    // --------------------------------------------------

    public static final int ret = 169;  // ret

    public static final int return_ = 177; // return



    //---------------------------------------------------
    //     S
    // --------------------------------------------------

    public static final int saload = 53;  // saload

    public static final int sastore = 86;  // sastore

    public static final int sipush = 17;  // sipush

    public static final int swap = 95;  // swap






    //---------------------------------------------------
    //     T
    // --------------------------------------------------

    public static final int tableswitch = 170;  // tableswitch

    //---------------------------------------------------
    //     W
    // --------------------------------------------------

    public static final int wide = 196;  // wide

                
                
/////////////////////////////////////////////////////////////////////
//  Native JVM Core Instruction Mnemonics 
/////////////////////////////////////////////////////////////////////
                
    public static final int lang = 254;
    public static final int T_start = 1;
    public static final int T_yield = 2;
    public static final int T_sleep = 3;
    public static final int INT_start = 10;
    public static final int INT_activate = 11;

    public static final int math_pow = 20;
    
    // Peripheral instruction Mnemonics
    public static final int peripheral = 255; // 
    public static final int display_DrawString = 1;
    public static final int display_DrawInt = 2;
    public static final int mem_read = 3;
    public static final int mem_write = 4;
    public static final int display_DrawLine = 5;
    public static final int display_DrawBytes = 6;
    public static final int get_file_handle = 20;
    public static final int fileinputstream_read0 = 30;
    public static final int sourcedataline_write = 40;


    
}
