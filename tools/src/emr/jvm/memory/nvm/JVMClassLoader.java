/*
 * JVMClassLoader.java
 *
 * Created on November 20, 2006, 8:49 PM
 *
 * The JVMClassLoader is a hybrid between a tree-like ClassFileElements
 * and a state machine style loader.  
 *
 * This one uses function calls to handle individual elements.
 *
 */

package emr.jvm.memory.nvm;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 *
 * @author Ross
 */
public class JVMClassLoader 
{
    // The Class file is read from the input stream
    // and written to the output stream
    InputStream in;
    OutputStream out;
    
    // The byte read in
    int inByte;
    
    // the number of bytes passed through
    int totalByteCount = 0;
    
    /** Creates a new instance of JVMClassLoader */
    public JVMClassLoader(InputStream in_, OutputStream out_) 
    {
        in = in_;
        out_ = out_;
    }
    
    /**
     * The top-level ClassFile processing function
     */
    public void loadClass()
    {
        try 
        {
            // Search for the magic number in the byte stream
            synchronize();
            readBytes(4, false);  // read version
            readConstantPool();  // read constant pool
            readBytes(2, true);  // read and write accessflags
            readBytes(2, true);  // read and write thisclass
            readBytes(2, true);  // read and write superclass
            readInterfaces();    // read interfaces
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Read given number of bytes out of the InputStream
     */
    public long readBytes(int numBytes_, boolean passThrough_) throws IOException
    {
        System.out.println("Reading " + numBytes_ + " bytes.  Written total of " + totalByteCount + " bytes.");
        long bytes = 0;
        int count = 0;
        while( (inByte = in.read()) != -1 && count < numBytes_ )
        {
            if ( numBytes_ <= 8 )
                bytes |= inByte << ((numBytes_ - count - 1) * 8);
            
            if ( passThrough_ == true )
            {
                out.write( inByte );
                totalByteCount += 1;
            }
        }
        
        return bytes;
    }
    
    
    public void readConstantPool() throws IOException
    {
        int CPCount = (int) readBytes(2, false);
        for( int i = 0; i < CPCount; i++ )
        {
            int tag = (int) readBytes(1, false);
            switch( tag )
            {
                case 1:  // UTF8
                    // need to hash this string!
                    int length = (int) readBytes(2, false);
                    readBytes(length, false);
                    break;
                case 3:  // Integer
                case 4:  // Float
                case 9:  // FieldRef
                case 10: // MethodRef
                case 11: // InterfaceMethodRef
                case 12: // NameAndType
                    // These are all 4 bytes
                   readBytes(4, true);
                   break;
                case 5: // Long
                case 6: // Double
                    readBytes(8, true);
                    break;
                case 7: // Class
                case 8: // String
                    readBytes(2, true);
                    break;
                    
            }
        }
    }
    
    public void readInterfaces() throws IOException
    {
        int ICount = (int) readBytes(2, false); // read interfaces count
        for ( int i = 0; i < ICount; i++ )
        {
            //readBytes
        }
    }
    
    private static final int STATE_SEARCHING = 0;
    private static final int STATE_SYNC1 = 1;
    private static final int STATE_SYNC2 = 2;
    private static final int STATE_SYNC3 = 3;
    private static int state = STATE_SEARCHING;
    /**
     * Searches for the magic number 0xCAFEBABE in the
     * InputStream.  The magic number is read but not written and
     * the function returns.
     */
    public void synchronize() throws IOException 
    {
        while( (inByte = in.read()) != -1 )
        {
            switch(state)
            {
                case STATE_SEARCHING:
                    
                    if ( inByte == 0xCA )
                    {
                        state = STATE_SYNC1;
                    }
                    else
                    {
                        state = STATE_SEARCHING;
                    }
                    break;

                case STATE_SYNC1:
                    
                    if ( inByte == 0xCA )
                    {
                        state = STATE_SYNC1;
                    }
                    else if ( inByte == 0xFE )
                    {
                        state = STATE_SYNC2;
                    }
                    else
                    {
                        state = STATE_SEARCHING;
                    }
                    break;
                    
                case STATE_SYNC2:
                    
                    if ( inByte == 0xCA )
                    {
                        state = STATE_SYNC1;
                    }
                    else if ( inByte == 0xBA )
                    {
                        state = STATE_SYNC3;
                    }
                    else
                    {
                        state = STATE_SEARCHING;
                    }
                    break;
                    
                case STATE_SYNC3:
                    
                    if ( inByte == 0xCA )
                    {
                        state = STATE_SYNC1;
                    }
                    else if ( inByte == 0xBE )
                    {
                        // read in magic number
                        System.out.println("Synchronized.");
                        return;
                    }
                    else
                    {
                        state = STATE_SEARCHING;
                    }
                           
                    break;
                    
                default:
                    return;
            }
        }
    }
    
    
}
