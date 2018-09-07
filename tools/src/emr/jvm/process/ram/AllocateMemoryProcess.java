/*
 * AllocateMemoryProcess.java
 *
 * Created on February 7, 2007, 10:08 PM
 *
 * The AllocateMemoryProcess process uses the MemoryPool to find a contiguous block of RAM.
 *
 * A handle to the allocated memory is returned.
 *
 * Register Set:
 *   Input: value - total size of memory region to allocate
 *   Output : handle - address of allocated memory region
 *
 * Used Registers:
 *   cphandle - 'current'
 *   name = 'current.size'
 *
 * Protected registers -- do not modify:
 *   value1 - max locals used by FrameSetup process
 *   value2 - arg count used by FrameSetup process
 *   tablehandle - used by ThreadRunSetup
 *   classhandle - used by ThreadRunSetup
 *   index - used by ArraySetup to know what type to initialize to
 *
 * The protected registers are those registers that are expected by one or more processes
 * to be preserved across calls to AllocateMemoryProcess.
 */

package emr.jvm.process.ram;

import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.RAMMemoryModule;
import emr.jvm.memory.ram.*; // for the ram

import emr.jvm.visualization.MemoryVisualizer;


/**
 * Allocate memory in a manner similar to the technique described in the K & R 'C' book.  This algorithm works as follows:
 * 
 * A memory block is composed of a header and the useable memory region.  The header has a 'size' field and a 'next' field.  'size'
 * refers to the user space in the block plus the header size.  'next' points to the start of the useable empty space in the next
 * available block in the freelist.
 *
 * Initially, the entire heap is allocated to a single block.  This is the only block in the free list.  The free list header points
 * to this block (as if it was a 'next' pointer).  The 'next' in the block points to itself.
 *
 * Allocating memory searches the free list for a block with sufficient size.  If a block of larger size is found, then that block is divided up.  
 * The end of the block is allocated by placing a header at the start of the requested region which is at the end of the original block.  The
 * original block's size is reduced appropriately.  This is an efficient way of allocating memory (starting from the _end_ of the memory region)
 * because no other pointers need to be modified.  The address of the start of the available memory region in the new block is returned.
 *
 * Note: there is a deficiency in the algorithm presented by K & R.  If there is only one region (contiguous)
 * remaining in the free list, and it is exactly the size required, then all subsequent allocation
 * requests will keep re-allocating that same space to each requestor!  The solution is to add a check for the exact case.
 * If prev.next = current.next then set the freehead to null.  BUT this will screw up the deallcation process.  So just forget about it for now.
 *
 *
 * @author Evan Ross
 */
public class AllocateMemoryProcess extends JVMProcess
{
    
    /**
     * Creates a new instance of AllocateMemoryProcess
     */
    public AllocateMemoryProcess()
    {
        super("AllocateMemoryProcess");
    }
    
    @Override
    public void runProcess() 
    {
        checkStatus();
        // 1. Allocate the memory.  size is already in value
        //JVMRuntime.handle = MemoryPool.allocate( JVMRuntime.value );
        
        /*
         * This is a two step process.
         * 1. traverse free list to find a free block of sufficient size
         * 2. once block is found, either return it if size is exact, or break it up.
         */

        
        // add header size to the requested size, must also be multiple of header size
        JVMRuntime.value += RAMMemoryModule.BLOCK_HEADER_SIZE;
        
        // previous = freehead
        JVMRuntime.cphandle = JVMRuntime.freehead;

        // while size of block is less than requested size
        while( true )
        {
            checkStatus();
            
            
            // current = previous.next
            JVMRuntime.handle = MemoryController.readWord(JVMRuntime.cphandle + RAMMemoryModule.BLOCK_HEADER_NEXT_OFFSET);
            
            // current.size
            JVMRuntime.name = MemoryController.readWord(JVMRuntime.handle + RAMMemoryModule.BLOCK_HEADER_SIZE_OFFSET);

            checkStatus();
            
            if( JVMRuntime.name == JVMRuntime.value || JVMRuntime.name >= JVMRuntime.value + RAMMemoryModule.BLOCK_HEADER_SIZE )  // current block has sufficient size
            {
                //debug("previous = " + Integer.toHexString(JVMRuntime.cphandle) + " current = " + Integer.toHexString(JVMRuntime.handle));
                if( JVMRuntime.name == JVMRuntime.value ) // exact match (remember that header is included as well)
                {
                    // unlink this block from the list
                    // previous.next = current.next
                    JVMRuntime.name = MemoryController.readWord( JVMRuntime.handle + RAMMemoryModule.BLOCK_HEADER_NEXT_OFFSET ); // current.next
                    //debug(">< Allocating exactly: current.next " + JVMRuntime.name);
                    
                    MemoryController.writeWord(JVMRuntime.cphandle + RAMMemoryModule.BLOCK_HEADER_NEXT_OFFSET, JVMRuntime.name, MemoryVisualizer.MM );  // previous.next = current.next
                    
                }
                else  // allocate tail end
                {
                    //debug(">< Allocating tail end.");
                    // Adjust current block's size by subtracting the requested size (including header) (value).
                    JVMRuntime.name -= JVMRuntime.value;
                    
                    MemoryController.writeWord(JVMRuntime.handle + RAMMemoryModule.BLOCK_HEADER_SIZE_OFFSET, JVMRuntime.name, MemoryVisualizer.MM);
                    
                    // Move current pointer to start of newly allocated block (tail end)
                    JVMRuntime.handle += JVMRuntime.name;
                    
                    // Set size of newly allocated block
                    MemoryController.writeWord(JVMRuntime.handle + RAMMemoryModule.BLOCK_HEADER_SIZE_OFFSET, JVMRuntime.value, MemoryVisualizer.MM);
                    
                    // FOR VISUALIZATION ONLY - set 'next' to make it visible
                    //MemoryController.writeWord(JVMRuntime.handle + RAMMemoryModule.BLOCK_HEADER_NEXT_OFFSET, 0);
                   
                }
                
                // Set freehead = previous (point where last block was found).
                // The reason for this is indicated as keeping the list "homogeneous".  Not sure if that really benefits us.
                JVMRuntime.freehead = JVMRuntime.cphandle;
                
                /////// For Visualization Only //////////////////////////
                // initialize the block
                MemoryPool.initializeBlock(JVMRuntime.handle, JVMRuntime.value - RAMMemoryModule.BLOCK_HEADER_SIZE, MemoryPool.initializationColor);
                
                // return 'handle' in JVMRuntime.handle
                //debug("Allocating " + Integer.toHexString(JVMRuntime.handle));
                return;
            }
        
            checkStatus();
            
            // previous = current
            JVMRuntime.cphandle = JVMRuntime.handle;
            
            // current = current.next
            JVMRuntime.handle = MemoryController.readWord(JVMRuntime.handle + RAMMemoryModule.BLOCK_HEADER_NEXT_OFFSET);
            
            // if current = freehead, we have arrived back at the beginning.  No block found.
            if( JVMRuntime.freehead == JVMRuntime.cphandle )
                throwException(MEMORY_ALLOCATION_ERROR);
            
        } 
        
        
        


        
        
    }
    
}
