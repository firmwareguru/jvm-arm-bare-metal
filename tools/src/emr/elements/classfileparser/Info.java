/*
 * Info.java
 *
 * Created on September 28, 2006, 9:24 PM
 * 
 * Provides convenience methods getName and getDescriptor, common to MethodInfo and FieldInfo Elements.
 */

package emr.elements.classfileparser;

import emr.elements.common.Element;
import emr.elements.classfileparser.cpinfo.*;
import emr.elements.classfileparser.common.AccessFlags;
/**
 *
 * @author Ross
 */
public class Info extends Element {
    
    /* ConstantPool */
    protected ConstantPool constantPool;
    
    /** Creates a new instance of Info */
    public Info() 
    {
    }
    
    //////////////////////////////////////////////
    // Convenience methods
    //////////////////////////////////////////////
    
   /** Return the descriptor index */
    public int getDescriptorIndex()
    {
        Element e = getElement("descriptor_index");
        return e.getValue();
    }
    
    /** Return the name index */
    public int getNameIndex()
    {
        Element e = getElement("name_index");
        return e.getValue();
    }
    
    /** Return the access flags */
    public int getAccessFlags()
    {
        Element e = getElement("access_flags");
        return e.getValue();
    }    
    
    /** Returns the name of this Info structure */
    public String getName()
    {
        // get the nameindex
        Element eNameIndex = getElement("name_index");
        int nameIndex = eNameIndex.getValue();
          
        // get the field name out of the constant pool
        Element cpinfo = constantPool.getCPElement(nameIndex);
        ConstantUTF8Info utf8Info = (ConstantUTF8Info) cpinfo;
        
        return utf8Info.getString();
    }
    
    /** Returns the descriptor of this Info structure */
    public String getDescriptor()
    {
        // get the descriptor index
        Element eDescriptorIndex = getElement("descriptor_index");
        int descriptorIndex = eDescriptorIndex.getValue();
         
        // get the field descriptor
        Element cpinfo = constantPool.getCPElement(descriptorIndex);
        ConstantUTF8Info utf8Info = (ConstantUTF8Info) cpinfo;
            
        return utf8Info.getString();
    }
    
    /** Is this field or method public?
     */
    public boolean isPublic()
    {
        AccessFlags flags = (AccessFlags) getElement("access_flags");
        return flags.isPublic();
    }
    
    /** Is this field or method private?
     */
    public boolean isPrivate()
    {
        AccessFlags flags = (AccessFlags) getElement("access_flags");
        return flags.isPrivate();
    }
    
    /** Is this field or method protected?
     */
    public boolean isProtected()
    {
        AccessFlags flags = (AccessFlags) getElement("access_flags");
        return flags.isProtected();
    }
    
    /**
     *  Is this field or method static?
     */
    public boolean isStatic()
    {
        AccessFlags flags = (AccessFlags) getElement("access_flags");
        return flags.isStatic();
    }
    
    /** 
     *  Is this field or method final?
     */
    public boolean isFinal()
    {
        AccessFlags flags = (AccessFlags) getElement("access_flags");
        return flags.isFinal();
    }
    
}
