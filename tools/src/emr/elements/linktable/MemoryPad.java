/*
 * MemoryPad.java
 *
 * Created on June 26, 2006, 9:44 PM
 *
 *
 * A MemoryPad simply writes out a stream of null or blank characters with
 * the purpose of taking up space ("padding") between useful memory elements.
 *
 * The number of padding bytes is determined by reading the current offset in a JVMOutputStream
 * and then writing out 
 *
 */

package emr.elements.linktable;

import emr.elements.linktable.*;
import emr.elements.common.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 *
 * @author Ross
 */
public class MemoryPad extends Element
{
    /* the address to pad up to */
    int nextOffset = 0;
    
    /* the value to pad with */
    byte padValue = 0;
    
    /** Creates a new instance of MemoryPad */
    public MemoryPad(int bytes_)
    {
        nextOffset = bytes_;
    }
    
    /* override writeElement to write the padding bytes */
    
    public void writeElement(OutputStream os) throws IOException
    {
        // we can't pad unless os is an instance of JVMOutputStream
        if(os instanceof JVMOutputStream)
        {
            if(nextOffset > 0)
            {
                int padBytes = nextOffset - ((JVMOutputStream)os).getOffset();
                setSize(padBytes); // set the size just for display purposes.
                for(int i = 0; i < padBytes; i++)
                    os.write(padValue);
            }
        }
    }
    
    public String toString()
    {
        return "memory_pad";
    }

    
            
    
}
