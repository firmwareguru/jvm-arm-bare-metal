/*
 * NVM.java
 *
 * Created on January 8, 2007, 10:10 PM
 *
 * This class encapsulates the program NVM.  A simple interface, getWord() and setWord(),
 * is provided.
 */

package emr.jvm.memory.nvm;

import emr.jvm.Debug;

import java.io.InputStream;
import java.io.IOException;

import java.io.ByteArrayInputStream; // testing only

import emr.jvm.visualization.MemoryVisualizer;
import java.awt.Color;

/**
 * @deprecated
 * @author Evan Ross
 */
public class NVM
{
    
    /* The NVM is an array of bytes however ints are used so that the current
     * visualization system can be used */
    private static int[] nvm = null;
    
    /* Visializes the contents of the HEAP */
    public static MemoryVisualizer visualizer;
    /* array of tags corresponding to each memory location */
    public static Color[] tags;
    
    
    /** The initialization method. */
    public static void initialize(InputStream is_, int size_)
    {
        Debug.message("Initializing NVM with " + size_ + " bytes.");
        nvm = new int[size_];
        
        try
        {
            int byteRead = -1;
            int byteCount = 0;
            
            while( ( byteRead = is_.read()) != -1 )
            {
                nvm[byteCount] = byteRead;
                byteCount++;
            }
            
            Debug.message("NVM Initialized.");
        }
        catch (IOException e)
        {
            Debug.fatalError("Failed to initialize NVM");
        }
        
        tags = new Color[ nvm.length ];
        for(int i = 0; i < tags.length; i++)
            tags[i] = MemoryVisualizer.EMPTY;
        
        //visualizer = new MemoryVisualizer("NVM", nvm, tags);
        
    }
    
    public static MemoryVisualizer getVisualizer()
    {
        return visualizer;
    }
    
    // A byte buffer
    static byte[] byteBuffer = new byte[8];
    
    /** Gets a variable byte word out of NVM.
     * Note: byte addressable! 
     * No alignment restrictions */
    public static int getWord(int index_, int bytes_)
    {
        // check bounds here
        if( index_ >= nvm.length || index_ < 0)
            Debug.fatalError("NVM: getWord out of bounds.  index = " + index_);

        //byte[] b = new byte[bytes_];
        byte[] b = byteBuffer;

        // get the next four bytes following the index_ address
        for( int i = 0; i < bytes_; i++ )
        {
            if( index_ + i < nvm.length )
                b[i] = (byte) nvm[index_ + i];
            else // beyond the nvm bounds
                b[i] = 0;
                
        }
        visualizer.repaintZoomPanels(index_);
        return bytesToInt(b, bytes_);
    }
    
    /** Get a 4-byte word out of NVM starting at index_ 
     * Note: byte addressable only! Word aligned only!
     */
    public static int get4ByteWord(int index_)
    {
        //return getWord(index_ * 4, 4);
        if (index_ % 4 != 0)
            Debug.fatalError("NVM Get4ByteWord: " + index_ + " not divisible by 4.");

        return getWord(index_, 4);
        
    }
    
    /** Get a 2-byte word out of NVM starting at index_ 
     * Note byte addressable only! */
    public static int get2ByteWord(int index_)
    {
        short word = (short) getWord(index_, 2);
        return (int) word; // sign extend to get negative numbers!
    }
    
    /** Gets a byte from NVM at index_.  Note: byte addressable! */
    public static int getByte(int index_)
    {
        visualizer.repaintZoomPanels(index_);
        if( index_ < nvm.length )
            return (nvm[index_]);
        else
        {
            Debug.fatalError("NVM: getByte index out of bounds: " + index_);
            return 0; // doesn't get here
        }
    }
    
    /** Helper function turns array of bytes into an int */
    public static int bytesToInt(byte[] bytes, int length_)
    {
        int theInt = 0;
        int shift;
        
        for(int i = 0; i < length_; i++)
        {
            shift = (length_ - 1 - i) * 8;
            theInt |= ((int)bytes[i] << shift) & (255 << shift);
            
        }
        return theInt;
    }
    
    /* Testing */
    public static void main(String[] args)
    {
        // create array of bytes
        int length = 10;
        byte[] b = new byte[length];
        for(int i = 0; i < b.length; i++)
        {
            b[i] = (byte)i;
        }
        
        // wrap
        ByteArrayInputStream bis = new ByteArrayInputStream(b);
        
        // initialize
        NVM.initialize( bis, b.length );
        
        // get some words and see if they are correct
        int word;
        
        // range starting at 0
        compareTestWord(0);
        // range starting at end
        compareTestWord(length - 1);

        // range starting at half way beyond end
        compareTestWord(length - 1 - 2);

        // range somewhere in the middle
        compareTestWord(4);


    
    
    }
    
    public static void compareTestWord(int index_)
    {
        byte[] b = new byte[4];
        
        for( int i = 0; i < 4; i++ )
        {
            b[i] = (byte) (index_ + i);
        }
        
        int control = bytesToInt(b, b.length);

        int word = NVM.get4ByteWord(index_);
        System.out.println(Integer.toHexString(word) + " " + Integer.toHexString(control));
    }
    
    
}
