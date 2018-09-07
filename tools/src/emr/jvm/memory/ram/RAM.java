/*
 * RAM.java
 *
 * Created on February 4, 2007, 8:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.jvm.memory.ram;

import emr.jvm.visualization.MemoryVisualizer;
import java.awt.Color;

import emr.jvm.JVMRuntime;

/**
 *
 * @author Evan Ross
 */
public class RAM
{
    
    /**
     *  The following constants are used by the memory allocation processes.
     *  These offsets are negative because the free list pointer points to 
     *  the start of the available memory region!
     */
    public static final int BLOCK_HEADER_SIZE_OFFSET        = -2;  // two words back
    public static final int BLOCK_HEADER_NEXT_OFFSET        = -1;  // one word back
    public static final int BLOCK_REMAINDER_MINIMUM_SIZE    =  5; // 5 words
    public static final int BLOCK_HEADER_SIZE               =  2;
    
    /**
     *  These constants specify the interrupt vector table location
     */
    private static final int INT_VECTOR_TABLE_SIZE = 8;
    
    // References to instances of RAM
    public static RAM bank0;
    public static RAM bank1;
    
    private int[] memoryArray;

    /* Visializes the contents of the HEAP */
    public MemoryVisualizer visualizer;
    /* array of tags corresponding to each memory location */
    public Color[] tags;
    
    
    /**
     *  Initialize the RAM bank.
     *  - Create an array of words (32-bit)
     *  - Initialize the memory management: freehead points to single region of entire memory's size
     *  - Create an array of tags to support visualization of the memory
     *  - Initialize the Visualizer
     *
     */
    public RAM(int heapSize_, int bank_ )
    {
        // Create the physical memory array
        memoryArray = new int[ heapSize_ ];
        
        tags = new Color[ heapSize_ ];
        for(int i = 0; i < tags.length; i++)
            tags[i] = MemoryVisualizer.EMPTY;
        
        //visualizer = new MemoryVisualizer("RAM Bank " + Integer.toString( bank_ ), memoryArray, tags);
        
        // Now setup the initial state of the memory management system
        // freehead points to single region
        //      |                       |                   |                             |
        //      | size = empty + header | next = itself (2) | ........ empty ............ |
        //      |    word 0             |   word 1          |^                            |
        //                                                   |
        //  freehead ----------------------------------------|
        
        // | <- start of RAM address   ----> increasing RAM address ---->
        // Note: memory is allocated at the END of the block, so the first chunk is allocated
        // at the highest RAM addresses.
        
        // The interrupt vector table exists in the first few words (single) of RAM.  Initialize it
        for( int i = 0; i < INT_VECTOR_TABLE_SIZE; i++ )
            putINTWord(i, JVMRuntime.nullregister);
            
        // Setup the initial memory block.  Offset everthing by the size of the interrupt vector table.
        JVMRuntime.freehead = 2 + INT_VECTOR_TABLE_SIZE;  // start of first word (32-bit) of empty memory.
        putMMWord(0 + INT_VECTOR_TABLE_SIZE, heapSize_ - INT_VECTOR_TABLE_SIZE);  // setup 'size' (word 0) of the block (one block for the whole memory region)
        putMMWord(1 + INT_VECTOR_TABLE_SIZE, 2 + INT_VECTOR_TABLE_SIZE);          // 'next' (word 1) points to itself which is the start of the free space (at word 2)
        
    }
    
    public MemoryVisualizer getVisualizer()
    {
        return visualizer;
    }

    
    /** Get a 4-byte word out of RAM */
    public int get4ByteWord(int index_)
    {
        visualizer.repaintZoomPanels(index_);
        return memoryArray[index_];
    }
    
    public void putWord(int index_, int value_)
    {
        memoryArray[index_] = value_;
        visualizer.repaintZoomPanels(index_);
    }
    
    
    /////////////////////////////////////////////////////////////////////
    // These methods update the visualizer with
    // the specific type of object that this memory represents
    ////////////////////////////////////////////////////////////////////
        
    public void putFrameWord(int index_, int value_)
    {
        tags[index_] = MemoryVisualizer.FRAME;
        putWord( index_, value_ );
    }
    
    public void putInstanceWord(int index_, int value_)
    {
        tags[index_] = MemoryVisualizer.INSTANCE;
        putWord( index_, value_ );
    }
    
    /** Tags a word as Memory Management */
    public void putMMWord(int index_, int value_)
    {
        tags[index_] = MemoryVisualizer.MM;
        putWord( index_, value_ );
    }
    
    /** Tags a word as Interrupt Vector Table */
    public void putINTWord(int index_, int value_)
    {
        tags[index_] = MemoryVisualizer.INTERRUPT_VECTOR;
        putWord( index_, value_ );
    }
    
    
    /** Alternate form of specifying the color of the memory region */
    public void putWord(int index_, int value_, Color type_)
    {
        tags[index_] = type_;
        putWord( index_, value_ );
    }
    
}
