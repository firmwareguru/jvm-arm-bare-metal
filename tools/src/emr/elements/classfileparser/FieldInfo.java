/*
 * FieldInfo.java
 *
 * Created on June 3, 2006, 11:30 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.elements.classfileparser;

import emr.elements.classfileparser.common.AccessFlags;
import emr.elements.classfileparser.common.Attributes;
import emr.elements.classfileparser.attributes.ConstantValueAttribute;
import emr.elements.classfileparser.common.DescriptorIndex;
import emr.elements.classfileparser.common.NameIndex;
import emr.elements.classfileparser.cpinfo.ConstantUTF8Info;
import emr.elements.classfileparser.cpinfo.CPInfo;

import emr.elements.common.Element;


/**
 *
 * @author Ross
 */
public class FieldInfo extends Info
{
    
    /** Creates a new instance of FieldInfo */
    public FieldInfo(ConstantPool constantPool_)
    {
        // protected variable from Info
        constantPool = constantPool_;
        
        add(new AccessFlags());
        add(new NameIndex());
        add(new DescriptorIndex());
        add(new Attributes(constantPool));
    }
    
    public ConstantValueAttribute getConstantValueAttribute()
    {
        Element e = getElement("constantvalue_attribute");
        if( e == null) // no attribute
            return null;
        
        return (ConstantValueAttribute) e;
    }
    
    public String toString()
    {
        //return "field_info";
        // More ugliness just to improve the readability of the ClassFile in the ClassFileViewer
        CPInfo info = (CPInfo)(constantPool.getElement(get(1).getValue()));
        ConstantUTF8Info string = (ConstantUTF8Info)info.getElement("CONSTANT_UTF8_Info");
        return "field_info: " + string.getString();
    }
    
}
