/*
 * ClassFileParser.java
 *
 * Created on November 15, 2006, 9:32 PM
 *
 * This ClassFileParser uses a state machine to process the byte stream.
 * Memory requirements are minimal.
 */

package emr.jvm.memory.nvm;

import emr.jvm.Debug;

import java.io.*;

/**
 *
 * @author Ross
 */
public class ClassFileParser 
{
    
    private static final int SYNC1 = 0xCA;
    private static final int SYNC2 = 0xFE;
    private static final int SYNC3 = 0xBA;
    private static final int SYNC4 = 0xBE;
    
    private static final int STATE_SEARCHING    = 0;
    private static final int STATE_SYNC1        = 1;
    private static final int STATE_SYNC2        = 2;
    private static final int STATE_SYNC3        = 3;
    private static final int STATE_SYNC4        = 4;
    
    private static final int STATE_READING_VERSION    = 5;
    private static final int VERSION_SIZE = 2;
    
    private static final int STATE_READING_CPCOUNT    = 6;
    private static final int CPCOUNT_SIZE = 2;
    
    private static final int STATE_READING_TAG        = 7;
    private static final int TAG_SIZE = 1;
            
    
    // General purpose registers
    private int stateCounter = 0;
    private int stateValue = 0;
    private int blockCount = 0;
    
    private int masterByteCount = 0;

    private boolean write = false;
    
    // other goodies
    private int CPCount = 0;
    
    // The state variable
    private int state;
    
    /** Creates a new instance of ClassFileParser */
    public ClassFileParser() 
    {
        
    }
    
    public void parse(InputStream in, OutputStream out)
    {
        state = STATE_SEARCHING;
        
        int inByte = 0;
        
        do
        {
            try 
            {
                inByte = in.read();
            } 
            catch (IOException e)
            {
                Debug.fatalError("IOException parsing class file: " + e.getMessage());
            }
            
            // Lets rock
            switch( state )
            {
                ////////////////////////////////////////////////
                //  STATE SEARCHING
                ////////////////////////////////////////////////
                case STATE_SEARCHING:
                    
                    write = false;
                    
                    if( inByte == SYNC1 )
                        transition( STATE_SYNC1 );
                    
                    break;
                   
                ////////////////////////////////////////////////
                //  STATE Syncing 
                //  Reading magic number 0xCAFEBABE
                ////////////////////////////////////////////////
                case STATE_SYNC1:
                    
                    write = false;

                    if ( inByte == SYNC2 )
                        transition( STATE_SYNC2 );
                    else if ( inByte == SYNC1 )
                        transition( STATE_SYNC1 );
                    
                    break;
                    
                case STATE_SYNC2:

                    write = false;

                    if ( inByte == SYNC3 )
                        transition( STATE_SYNC3 );
                    else if ( inByte == SYNC1 )
                        transition( STATE_SYNC1 );
                    
                    break;
                    
                case STATE_SYNC3:
                    
                    write = false;

                    if ( inByte == SYNC4 )
                    {
                        transition( STATE_READING_VERSION );
                        stateCounter = 0;
                    }
                    else if ( inByte == SYNC1 )
                    {
                        transition( STATE_SYNC1 );
                    }
                    
                    break;
                    
                    
                case STATE_READING_VERSION:
                    
                    write = false;
                    stateCounter++;
                    
                    if ( stateCounter == VERSION_SIZE )
                    {
                        transition( STATE_READING_CPCOUNT );
                        stateCounter = 0;
                    }
                    
                    break;
                    
                case STATE_READING_CPCOUNT:
                    
                    write = true;
                    CPCount |= (inByte << ( (CPCOUNT_SIZE - stateCounter - 1) * 8));
                    stateCounter++;
                    
                    if ( stateCounter == CPCOUNT_SIZE )
                    {
                        transition( STATE_READING_TAG );
                        stateCounter = 0;
                    }
                    
                    break;
                    
               
                default:
                    
                    Debug.warning("Unhandled state " + state);

                    
                    
            }
                 
            // If the byte is flagged for write, increment the count of bytes written
            if ( write == true )
                masterByteCount++;
            
            // Now write the byte if an output stream has been supplied
            try
            {
                if( out != null && write == true )
                    out.write(inByte);
            }
            catch (IOException e)
            {
                Debug.fatalError("IOException writing byte: " + e.getMessage());
            }
            
            
        } 
        while (inByte != -1);
        
    }
    
    private void transition(int newState)
    {
        Debug.message("Transitioning to state " + newState);
        Debug.message("   Bytes written: " + masterByteCount );
        state = newState;
        Debug.message(" CPCount = " + CPCount );
    }
    
    
    public static void main(String args[])
    {
        String className = "C:/projects/java/ClassFileLinker/build/classes/samples/Main.class";

        ClassFileParser parser = new ClassFileParser();
        
        try 
        {
            FileInputStream fin = new FileInputStream(className);
            parser.parse(fin, null);
        }
        catch (IOException e)
        {
            Debug.fatalError("Error reading classfile: " + e.getMessage());
        }
         
    }
    
}
