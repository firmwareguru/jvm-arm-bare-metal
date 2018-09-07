/*
 * MethodInfo.java
 *
 * Created on June 5, 2006, 10:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.elements.classfileparser;


import emr.elements.classfileparser.common.AccessFlags;
import emr.elements.classfileparser.common.Attributes;
import emr.elements.classfileparser.attributes.CodeAttribute;
import emr.elements.classfileparser.attributes.ExceptionTable;

import emr.elements.classfileparser.common.DescriptorIndex;
import emr.elements.classfileparser.common.NameIndex;
import emr.elements.classfileparser.cpinfo.ConstantUTF8Info;
import emr.elements.classfileparser.cpinfo.CPInfo;
import emr.elements.common.Element;
/**
 *
 * @author Ross
 */
public class MethodInfo extends Info
{
    /** Creates a new instance of MethodInfo */
    public MethodInfo(ConstantPool constantPool_)
    {
        // inherited protected field from Info
        constantPool = constantPool_;
        
        add(new AccessFlags());
        add(new NameIndex());
        add(new DescriptorIndex());
        add(new Attributes(constantPool_));
        
               
    }
    
    //////////////////////////////////////////////
    // Provide some helpers to get information
    // directly out of MethodInfo
    //////////////////////////////////////////////
    
    
    /** Return true if this MethodInfo structure refers to a native method.
     * Native MethodInfo structures will return an empty Code Element.
     */
    public boolean isNative()
    {
        AccessFlags flags = (AccessFlags) getElement("access_flags");
        return flags.isNative();
    }
    
    /** Sets or clears the native flag */
    public void setNative(boolean native_)
    {
        AccessFlags flags = (AccessFlags) getElement("access_flags");
        flags.setNative( native_ );
    }
    
    /**
     *  Return true if this MethodInfo is Abstract.
     */
    public boolean isAbstract()
    {
        AccessFlags flags = (AccessFlags) getElement("access_flags");
        return flags.isAbstract();        
    }
     
            
    /** Return the Code Attribute of this MethodInfo.  Returns null if this
     * method is native.
     */
    public CodeAttribute getCodeAttribute()
    {
        Element e = getElement("code_attribute");
        if( e == null )
            return null;
        
        return (CodeAttribute) e;
    }

    private String cachedName = null;
    @Override
    public String getName() {
        if (cachedName == null) {
            CPInfo info = (CPInfo)(constantPool.getElement(get(1).getValue()));
            ConstantUTF8Info string = (ConstantUTF8Info)info.getElement("CONSTANT_UTF8_Info");
            cachedName = string.getString();
        }
        return cachedName;
    }

    private String cachedDescriptor = null;
    public String getDescriptor() {
        if (cachedDescriptor == null) {
            CPInfo info = (CPInfo)(constantPool.getElement(get(2).getValue()));
            ConstantUTF8Info string = (ConstantUTF8Info)info.getElement("CONSTANT_UTF8_Info");
            cachedDescriptor = string.getString();
        }
        return cachedDescriptor;
    }
    
   
    
    public String toString()
    {
        //return "method_info";
        // More ugliness just to improve the readability of the ClassFile in the ClassFileViewer
        //CPInfo info = (CPInfo)(constantPool.getElement(get(1).getValue()));
        //ConstantUTF8Info string = (ConstantUTF8Info)info.getElement("CONSTANT_UTF8_Info");
        return "method_info: " + getName();

    }
    
}
