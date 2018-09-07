/*
 * InternalClass.java
 *
 * Created on January 10, 2007, 8:05 PM
 *
 * This is a wrapper over an NVM InternalClass structure.  All methods take an address in NVM
 * (the handle) that is at the beginning of an InternalClass structure.
 */

package emr.jvm.memory.nvm;

/**
 *
 * @author Evan Ross
 */
public class InternalClass
{

    // -- Word aligned offsets
    public static final int HEADER_NextClassOfsset = 0 * 4;
    public static final int HEADER_ThisClassOffset = 1 * 4;
    public static final int HEADER_SuperClassOffset = 2 * 4;
    public static final int HEADER_AccessFlagsOffset = 3 * 4;
    public static final int HEADER_ObjectSizeOffset = 3 * 4;
    public static final int HEADER_CPTableOffset = 4 * 4;
    public static final int HEADER_InterfaceTableOffset = 5 * 4;
    public static final int HEADER_MethodTableOffset = 6 * 4;
    public static final int HEADER_FieldTableOffset = 7 * 4;
    
    // Generic TableEntry offsets.
    public static final int TABLEENTRY_NameDescriptorIndexOffset = 0 * 4; // word aligned
    public static final int TABLEENTRY_NameIndexOffset = (0 * 4) + 2; // half-word aligned, upper 2 bytes
    public static final int TABLEENTRY_DescriptorIndexOffset = (0 * 4) + 0; // half-word aligned, lower 2 bytes
    public static final int TABLEENTRY_Size = 3 * 4; // 3 words, used to increment pointer
    
    public static final int METHODTABLEENTRY_NameDescriptorIndexOffset = 0 * 4;
    public static final int METHODTABLEENTRY_FlagsOffset = 1 * 4;
    public static final int METHODTABLEENTRY_ArgCountOffset = (1 * 4);
    
    public static final int METHODTABLEENTRY_FlagsHalfOffset = (1 * 4) + 0; // flags comes first
    public static final int METHODTABLEENTRY_ArgCountHalfOffset = (1 * 4) + 2;
    
    public static final int METHODTABLEENTRY_CodePtrOffset = 2 * 4;
    public static final int METHODTABLEENTRY_ExceptionPtrOffset = 2 * 4;
    
    // Note!  Half-word offsets must directly access the half-words are they appear in NVM,
    // rather than "upper/lower 2 bytes".  Hence these are in "opposite" order if they were accessed from a word using shifts.
    public static final int METHODTABLEENTRY_ExceptionPtrHalfOffset = (2 * 4) + 0; // half-word aligned, first 2 bytes
    public static final int METHODTABLEENTRY_CodePtrHalfOffset = (2 * 4) + 2; // half-word aligned, second 2 bytes
    
    public static final int METHODTABLEENTRY_Size = 3 * 4; // 3 words, used to increment pointer
    
    public static final int FIELDTABLEENTRY_NameDescriptorIndexOffset = 0 * 4;  // word 0 : name index, descriptor index
    public static final int FIELDTABLEENTRY_FlagsConstantOffset = 1 * 4;        // word 1 : flags, constant index
    public static final int FIELDTABLEENTRY_FieldSizeIndexOffset = 2 * 4;       // word 2 : field size, field index
    public static final int FIELDTABLEENTRY_Size = 3 * 4;  // 3 32-bit words
    
    public static final int CODETABLEENTRY_ArgCountOffset = 1 * 4; // no longer used!
    public static final int CODETABLEENTRY_CodeOffset = 1 * 4;
        

}
