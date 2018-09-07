/*
 * Code.java
 *
 * Created on June 21, 2006, 8:56 PM
 *
 * The Code Element has, as first child, a code_length.  The code_length determines how many
 * bytes to process in the readChildren method.
 *
 * ReadChildren will 
 */

package emr.elements.classfileparser.code;

import emr.elements.common.Element;
import emr.elements.common.GenericElement;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


/**
 *
 * @author Ross
 */
public class Code extends Element
{
    
    /* the array of code bytes */
    //byte[] code;
    
    /** Creates a new instance of Code */
    public Code()
    {
        add(new GenericElement(u4, "code_length"));
    }
    
    public void readChildren(InputStream is) throws IOException
    {
        // read in codelength first.
        get(0).readData(is);
        
        // set the size to the codelength.
        //setSize(get(0).getValue());
        int codeLength = get(0).getValue();

        // need running count of the code length
        int size = 0;
        
        // now we need to read in code and create Instruction objects.
        // Instruction objects have a size that can be used to determine if the end
        // of the code attribute has been reached.
        do 
        {
           // add instruction and read it in, passing in the current pc
           Instruction e = new Instruction(size);
           e.readData(is);
           add(e);
           
           size += e.getInstructionSize();
           // System.out.println("Element size: " + e.getSize() + ". Running instruction size = " + size + ". code size = " + getSize());
           
        } while(size < codeLength); // size and getSize should be same after last instruction.
    }
    
    // override so that code <= 4 bytes is not read in as a value
    public void readElement(InputStream is) throws IOException
    {
        
    }

    // override so that code <= 4 bytes is not written out as a value
    public void writeElement(OutputStream os) throws IOException
    {
    }
    /*
    public void readElement(InputStream is) throws IOException
    {
        if(getSize() == 0)
            throw new IOException("Code length not set.");
        
        code = new byte[getSize()];
        is.read(code);
    }
     
  
    public void writeElement(OutputStream os) throws IOException
    {
        os.write(code);
    }
        
 
    public byte[] getCode()
    {
        return code;
    }
    
    public void setCode(byte[] code_)
    {
        code = code_;
    }
     
     */
    
    public String toString()
    {
        return "code";
    }
    
    
    
}
