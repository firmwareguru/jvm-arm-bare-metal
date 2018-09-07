/*
 * AttributeInfo.java
 *
 * Created on June 5, 2006, 10:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.elements.classfileparser.attributes;

import emr.elements.classfileparser.cpinfo.CPInfo;
import emr.elements.classfileparser.cpinfo.ConstantUTF8Info;
import emr.elements.common.Element;
import emr.elements.classfileparser.ConstantPool;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 *
 * @author Ross
 *
 * We can cast
 */
public class AttributeInfo extends Element
{
    
    // A bit of an oddball... attributes needs access to the constant pool to know what kind of
    // attribute it is.  All I can do here is just read in AttributeLength bytes.
    byte[] info;
    
    /* Needs reference to the ConstantPool to resolve Attribute names */
    ConstantPool constantPool;
            
    
    /** AttributeInfo needs reference to the ConstantPool to resolve the name of the
     *  attribute so that the correct handler class can be instantiated.
     */
    public AttributeInfo(ConstantPool constantPool_)
    {
        constantPool = constantPool_;
        
        add(new AttributeNameIndex());
        add(new AttributeLength());
    }
    
    public void readChildren(InputStream is) throws IOException
    {
        get(0).readData(is);  // read name index
        get(1).readData(is);  // read length
        
        int index = get(0).getValue();
        
        
        // index references a ConstantUTF8Info structure
        CPInfo cpInfo = (CPInfo)constantPool.getElement(index);
        if(cpInfo == null)
            throw new IOException("Cannot read constant pool at index " + index);

        ConstantUTF8Info utf8Info = (ConstantUTF8Info)cpInfo.getElement("CONSTANT_utf8_info");
        if(utf8Info == null)
            throw new IOException("Cannot read utf8 string from cpinfo " + cpInfo.getTag());
        
        
        String name = utf8Info.getString();
        if(name == null)
            throw new IOException("Cannot read utf8 string");
        
        if(debug) System.out.println("Attribute name: " + name);
        
        Element attribute;
        
        
        
        if (name.contains("Code")) // Code attribute
        {
            attribute = new CodeAttribute(constantPool);                
        }
        else if (name.contains("ConstantValue"))
        {
            attribute = new ConstantValueAttribute();                
        }  
        else if (name.contains("Exceptions"))
        {
            attribute = new ExceptionsAttribute();                
        } 
        else if (name.contains("SourceFile"))
        {
            attribute = new SourceFileAttribute();                
        }  
        //else if (name.contains("LineNumberTable"))
        //{
        //    attribute = new LineNumberAttribute();                
        //}  
        //else if (name.contains("LocalVariableTable"))
        //{
        //    attribute = new LocalVariableAttribute();                
        //}  
        else // unhandled
        {
         
            attribute = new UnknownAttribute(getElement("attribute_length").getValue());                
        }  
        

        attribute.readData(is);
        add(attribute);

    }
    
    public String toString()
    {
        return "attribute_info";
    }
    
}
