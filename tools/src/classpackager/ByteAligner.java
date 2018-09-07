/*
 * ByteAligner.java
 *
 * Created on December 29, 2006, 3:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package classpackager;

import emr.elements.common.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
/**
 *
 * @author Evan Ross
 */
public class ByteAligner extends Element
{
    private int alignment;
    private int padBytes;
    /** Creates a new instance of ByteAligner that will align the immediately following element
     * on the specified byte boundary, i.e., 4 = 4 byte alignment. */
    public ByteAligner(int align_)
    {
        alignment = align_;
    }
    
    public void writeElement(OutputStream os_) throws IOException
    {
        if( os_ instanceof JVMOutputStream )
        {
            if( alignment % 2 != 0 )
                throw new IOException("ByteAligner invalid alignment " + alignment);
            
            // get the current offset and see if it is aligned
             JVMOutputStream jos = (JVMOutputStream) os_;
             int offset = jos.getOffset();
             padBytes = offset % alignment;
             
             // if not already an even multiple of alignment, pad by alignment - padbytes.
             // i.e., 4 - 1 = 3 more bytes.
             if( padBytes != 0 )
                 padBytes = alignment - padBytes;
             
             setSize( padBytes ); // set size just for viewing purposes
             // write out the pad bytes
             System.err.println("padding with " + padBytes + " bytes");
        }
        
        for( int i = 0; i < padBytes; i++ )
            os_.write(0);
    }
    
    public String toString()
    {
        return ("pad_" + padBytes + "_bytes");
    }
    
}
