/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package emr.jvm.memory;

import emr.jvm.JVMRuntime;
import emr.jvm.visualization.MemoryVisualizer;

/**
 *
 * @author Evan Ross
 */
public class RAMMemoryModule extends MemoryModule
{
    
    /**
     *  The following constants are used by the memory allocation processes.
     *  These offsets are negative because the free list pointer points to 
     *  the start of the available memory region!
     */
    public static final int BLOCK_HEADER_SIZE_OFFSET        = -8;  // two words back
    public static final int BLOCK_HEADER_NEXT_OFFSET        = -4;  // one word back
    public static final int BLOCK_REMAINDER_MINIMUM_SIZE    =  20; // 5 words
    public static final int BLOCK_HEADER_SIZE               =  8;  // 2 words
    
    /**
     *  These constants specify the interrupt vector table location
     */
    private static final int INT_VECTOR_TABLE_SIZE = 32; // 8 words

    RAMMemoryModule(String name, int size, int baseAddress)
    {
        super(name, size, baseAddress);
        
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
        for( int i = baseAddress; i < INT_VECTOR_TABLE_SIZE; i +=4 )
            writeWord(i, JVMRuntime.nullregister, MemoryVisualizer.INTERRUPT_VECTOR);
           
        //////////////////////////////////////////////////////////////////////////////////////////////
        // Setup the initial memory block.  The value in this register determines where in memory
        // objects in RAM are actually stored, i.e. the base offset in RAM.
        // Offset everthing by the RAM's base address plus size of the interrupt vector table.
        //////////////////////////////////////////////////////////////////////////////////////////////
        JVMRuntime.freehead = baseAddress + INT_VECTOR_TABLE_SIZE + BLOCK_HEADER_SIZE;  // start of first word (32-bit) of empty memory.       
        //////////////////////////////////////////////////////////////////////////////////////////////
        
        writeWord(baseAddress + 0 + INT_VECTOR_TABLE_SIZE,
                  size - INT_VECTOR_TABLE_SIZE, 
                  MemoryVisualizer.MM);  // setup 'size' (word 0) of the block (one block for the whole memory region)
        writeWord(baseAddress + 4 + INT_VECTOR_TABLE_SIZE, 
                  JVMRuntime.freehead, 
                  MemoryVisualizer.MM);          // 'next' (word 1) points to itself which is the start of the free space (at word 2)
        
    }
}
