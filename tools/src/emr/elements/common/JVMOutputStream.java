/*
 * OffsetOutputStream.java
 *
 * Created on June 24, 2006, 2:35 PM
 *
 * OffsetOutputStream is used as the OutputStream when the offsets of ClassFile components needs to be determined.
 * This class doesn't write out to anything; the write() method is a place holder.  New methods added are:
 * setInitialOffset() : sets the offset from which Elements base their offsets.
 * getOffset() : Elements call this in their writeData() method and set their own offsets
 *               (only if the OutputStream is an instance of this class).  Note that the value returned
 *               must be the byte offset immediately following the last "written" byte.
 */

package emr.elements.common;

import java.io.OutputStream;
import java.io.IOException;

/**
 *
 * @author Ross
 */
public class JVMOutputStream extends OutputStream
{
    
    /* Tracks the number of bytes witten.
     * The offset starts at 0! i.e. the first byte is at 0 offset.
     */
    int offsetCounter = 0;
    
    /* Forwards writes to this outputstream
     */
    OutputStream os = null;
    
    // Forwards calls to any supplied outputstream
    public JVMOutputStream(OutputStream os_)
    {
        os = os_;
    }
    
    public JVMOutputStream(int initialOffset_)
    {
        offsetCounter = initialOffset_;
    }

    /* Inrement the offset counter by 1 */
    public void write(int i) throws IOException
    {
        offsetCounter++;
        if(os != null)
            os.write(i);
    }
    
    public void write(byte[] b) throws IOException
    {
        offsetCounter += b.length;
        if(os != null)
            os.write(b);
    }
    
    public void write(byte[] b, int off, int len) throws IOException
    {
        offsetCounter += len;
        if(os != null)
            os.write(b, off, len);
    }
    
    public int getOffset()
    {
        return offsetCounter;
    }
    
    public void setOffset(int offset_)
    {
        offsetCounter = offset_;
    }
    
    
    
   
    
}
