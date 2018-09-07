/*
 * LinkedClass.java
 *
 * Created on June 21, 2006, 9:54 PM
 *
 * A LinkedClass structure in the LinkTable contains addresses in NVM for each major section of a Class.
 *
 * The sections are:
 *      ClassOffset : address of first byte of class
 *      ConstantPoolffset : address of constant_pool
 *      AccessFlagsOffset : address of access flags value following the constant pool\
 *      InterfacesOffset
 *      MethodsOffset
 *      AttributesOffset
 *      
 */

package emr.elements.linktable;

import emr.elements.common.Element;
import emr.elements.classfileparser.ClassFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 *
 * @author Ross
 */
public class LinkedClass extends Element
{
    
    /* reference to the ClassFile */
    ClassFile classFile;
    
    /**
     * Creates a new instance of LinkedClass
     */
    public LinkedClass(ClassFile classFile_)
    {
        classFile = classFile_;
        
        add(new ClassOffset());
        add(new ConstantPoolOffset());
        add(new AccessFlagsOffset());
        add(new InterfacesOffset());
        add(new MethodsOffset());
        add(new AttributesOffset());
    }
    
    /* This method will set the values of the child Elements to the
     * offset() values at "write time".
     * The writeChildren will write out each child element with the value obtained in the following
     * statements.
     */
    public void writeElement(OutputStream os) throws IOException
    {
        
        // set the classoffset
        getElement("class_offset").setValue(classFile.getOffset());
        
        // set the constant pool offset
        getElement("constant_pool_offset").setValue(classFile.getElement("constant_pool").getOffset());
        
        // set the access flags offset
        getElement("access_flags_offset").setValue(classFile.getElement("access_flags").getOffset());
        
        // set the interfaces offset
        getElement("interfaces_offset").setValue(classFile.getElement("interfaces").getOffset());
        
        // set the methods offset
        getElement("methods_offset").setValue(classFile.getElement("methods").getOffset());
        
        // set the attributes offset
        getElement("attributes_offset").setValue(classFile.getElement("attributes").getOffset());
        
    }
    
    public String toString()
    {
        return "class_reference";
    }
    
}
