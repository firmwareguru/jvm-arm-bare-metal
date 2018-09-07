/*
 * CodeAttributeInfo.java
 *
 * Created on June 5, 2006, 10:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.elements.classfileparser.attributes;

import emr.elements.classfileparser.code.Code;
import emr.elements.common.Element;
import emr.elements.classfileparser.ConstantPool;
import emr.elements.classfileparser.common.Attributes;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 *
 * @author Ross
 */
public class CodeAttribute extends Element
{
    
    /* byte array containing the code for the method */
    //byte[] code;
    
    /** Creates a new instance of CodeAttributeInfo */
    public CodeAttribute(ConstantPool constantPool)
    {
        add(new MaxStack());
        add(new MaxLocals());
        add(new Code());
        add(new ExceptionTable());
        add(new Attributes(constantPool));
    }

    /** Returns the value of max_locals in the code attribute.
     */
    public int getMaxLocals()
    {
        Element maxLocals = getElement("max_locals");
        return maxLocals.getValue();
    }
    
    /** Sets the value of the max_locals in the code attribute.
     */
    public void setMaxLocals(int value_)
    {
        Element maxLocals = getElement("max_locals");
        maxLocals.setValue(value_);
    }
            
    
    /** Returns the value of max_stack in the code attribute.
     */
    public int getMaxStack()
    {
        Element maxStack = getElement("max_stack");
        return maxStack.getValue();
    }
    
    /** Sets the value of the max_stack in the code attribute.
     */
    public void setMaxStack(int value_)
    {
        Element maxStack = getElement("max_stack");
        maxStack.setValue(value_);
    }
    
    /**
     * Returns the Code Element that heads the tree containing all instructions.
     * The code_length Element has been disabled and will not be included in Code
     * writes.  I wonder if this should be copying the element before returning it.
     * This is used by the packager to attach code blocks to internalclasses.
     */
    public Element getCodeNoLength()
    {
        Element code = getCode();
        
        // set to false so that getTreeSize() won't incorrectly
        // add the code size to the total size.
        code.setWritable(false); 
        
        // "remove" the code_length sub-element by disabling it for write
        //Element length = code.getElement("code_length");
        //length.setWritable(false);
        code.remove(0); // the code_length
        
        // return the Code element
        return code;        
    }
    
    public Element getCode()
    {
        Element code = getElement("code");
        
        return code;
    }
    
    /** Return the ExceptionTable in this CodeAttribute */
    public ExceptionTable getExceptionTable()
    {
        // get the ExceptionTable out
        ExceptionTable table = (ExceptionTable) getElement("exception_table");
        
        return table;
    }
    
    
    /*
    public void readChildren(InputStream is) throws IOException
    {
        getElement("max_stack").readData(is);
        getElement("max_locals").readData(is);
        //getElement("code_length").readData(is);
        
        //int codeLength = getElement("code_length").getValue();
        
        //Element code = getElement("code");
        //code.setSize(codeLength);
        //code.readData(is);
        
        getElement("exception_table").readData(is);
        getElement("Attributes").readData(is);
        
    }
    
     */
    
    public String toString()
    {
        return "Code_attribute";
    }
     
    
}
