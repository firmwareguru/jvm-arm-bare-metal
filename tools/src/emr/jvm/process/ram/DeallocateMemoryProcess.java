/*
 * DeallocateMemoryProcess.java
 *
 * Created on February 15, 2007, 9:23 PM
 *
 * Register Set:
 * Input: HANDLE - address of object to deallocate
 *
 * Registers used:
 *        tablehandle - 
 *        descriptor - 
 *        cphandle -
 *        name -
 *        
 * Protected (do not use): VALUE, VALUE1 or VALUE2, for non-void returns.
 */

package emr.jvm.process.ram;
import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.RAMMemoryModule;
import emr.jvm.memory.ram.*; // for the ram

import emr.jvm.visualization.MemoryVisualizer;

/**
 * The DeallocateMemoryProcess is designed to compliment the AllocateMemory process.  These
 * techniques are described in Kernighan & Ritchie C programming book, section 8.7 "A storage allocator".
 * 
 * Deallocating memory is a little more complicated, but not too much.  This technique is really quite
 * interesting and should be quite efficient.
 *
 * There are 4 steps to memory deallocation:
 *    1.  Iterate through free list, updating 'current' until a suitable location is found.
 *        A suitable location is:
 *           a.  block is between 2 existing blocks, i.e., handle > current && handle < current.next
 *           b.  block is at either end of list:
 *                  current >= current.next && (handle > current || handle < current.next)
 *    2.  Combine upper adjacent block if possible.  The upper block is higher in memory (larger address)
 *    3.  Combine lower adjacent block if possible.  The lower block is lower in memory (lower address)
 *    4.  Set freehead = current
 *
 * These steps must be performed in order since step 3 relies on step 2 having been performed.
 *
 * Note that for step 4 is not obvious why it is done, but by doing it, in step 1.A.b the test "handle < current.next" is necessary.
 * The freehead moves around thus "beginning" and "end" of the list may have different meanings each time.  Just remember that
 * the "end" of the free list (the last block in the chain) always points to the first block in the chain.
 *
 * Examples of list configurations prior to deallocation:
 *
 * Deallocated block at beginning of list:
 *
 *               |--------------|
 *              \/              |
 *    used    free    used    free  
 *      ^        |              ^
 *      |        ---------------|
 *   handle                     |
 *                         freehead
 *   
 * Deallocated block in middle of list:
 *
 *   -------------------------------|
     | |-------------|    handle    |
 *  \/ |            \/      \/      |
 *  free  used    free    used    free  
 *   ^               |              ^
 *   |               ---------------|
 *   |
 * freehead
 *
 *
 *
 *
 * @author Evan Ross
 */
public class DeallocateMemoryProcess extends JVMProcess
{
    
    /** Creates a new instance of DeallocateMemoryProcess */
    public DeallocateMemoryProcess()
    {
        super("DeallocateMemoryProcess");
    }
    
    public void runProcess() 
    {
        checkStatus();

        // current = freehead
        JVMRuntime.cphandle = JVMRuntime.freehead;
        
        checkStatus();
        
        // 1. Search freelist for spot to put back block
        while(true)
        {
            //System.err.println("Searching list for handle: " + Integer.toHexString(JVMRuntime.handle));
            
            // current.next
            JVMRuntime.name = MemoryController.readWord( JVMRuntime.cphandle + RAMMemoryModule.BLOCK_HEADER_NEXT_OFFSET );
                        
            checkStatus();
    
            if( JVMRuntime.handle > JVMRuntime.cphandle && JVMRuntime.handle < JVMRuntime.name )
                break;  // in the middle
            
            if( JVMRuntime.cphandle >= JVMRuntime.name &&
                    ( JVMRuntime.handle > JVMRuntime.cphandle || JVMRuntime.handle < JVMRuntime.name ))
                break;  // at the beginning or end

            checkStatus();
            
            // current = current.next
            JVMRuntime.cphandle = JVMRuntime.name;

        }
        
        //System.err.println("<> spot found: " + Integer.toHexString(JVMRuntime.cphandle) + " for handle: " + Integer.toHexString(JVMRuntime.handle));
        
        checkStatus();
    
        // descriptor = handle.size
        JVMRuntime.descriptor = MemoryController.readWord( JVMRuntime.handle + RAMMemoryModule.BLOCK_HEADER_SIZE_OFFSET );
         
        checkStatus();
        
        /////// For Visualization Only //////////////////////////
        // Erase the data portion of the block            // handle.size - header size  -- don't erase the next block's header
        MemoryPool.initializeBlock(JVMRuntime.handle, JVMRuntime.descriptor - RAMMemoryModule.BLOCK_HEADER_SIZE, MemoryVisualizer.EMPTY);
        
        
        checkStatus();

        // 2.  merge upper blocks
        // if handle + handle.size == current.next
        JVMRuntime.index = JVMRuntime.handle + JVMRuntime.descriptor;
        if( JVMRuntime.index == JVMRuntime.name)
        {
            //System.err.println("<> Merging upper blocks for handle: " + Integer.toHexString(JVMRuntime.handle));
            
            // current.next.size
            JVMRuntime.tablehandle = MemoryController.readWord( JVMRuntime.name + RAMMemoryModule.BLOCK_HEADER_SIZE_OFFSET );
            
            checkStatus();
    
            // handle.size += current.next.size
            JVMRuntime.descriptor += JVMRuntime.tablehandle;
            
            checkStatus();
    
            // store handle.size
            MemoryController.writeWord( JVMRuntime.handle + RAMMemoryModule.BLOCK_HEADER_SIZE_OFFSET, JVMRuntime.descriptor, MemoryVisualizer.MM);
            
            checkStatus();
    
            // current.next.next
            JVMRuntime.tablehandle = MemoryController.readWord( JVMRuntime.name + RAMMemoryModule.BLOCK_HEADER_NEXT_OFFSET ); // current.next.next
            
            checkStatus();
        
            // handle.next = current.next.next
            MemoryController.writeWord( JVMRuntime.handle + RAMMemoryModule.BLOCK_HEADER_NEXT_OFFSET, JVMRuntime.tablehandle, MemoryVisualizer.MM );
                    
        }
        else
        {
            //System.err.println("<> Upper blocks not merged for handle: " + Integer.toHexString(JVMRuntime.handle));
            // not merged to upper block.  inserted into free list before current.next
            checkStatus();
        
            // handle.next = current.next
            MemoryController.writeWord( JVMRuntime.handle + RAMMemoryModule.BLOCK_HEADER_NEXT_OFFSET, JVMRuntime.name, MemoryVisualizer.MM );
        }
        
        checkStatus();

        // 3.  merge lower block
        // current.size
        JVMRuntime.tablehandle = MemoryController.readWord( JVMRuntime.cphandle + RAMMemoryModule.BLOCK_HEADER_SIZE_OFFSET );
        
        checkStatus();

        // if current + current.size == handle
        JVMRuntime.index = JVMRuntime.cphandle + JVMRuntime.tablehandle;
        if( JVMRuntime.index  == JVMRuntime.handle )
        {
            //System.err.println("<> Merging lower block for handle: " + Integer.toHexString(JVMRuntime.handle));
            checkStatus();
    
            // current.size += handle.size
            JVMRuntime.tablehandle += JVMRuntime.descriptor;
            
            checkStatus();
    
            MemoryController.writeWord( JVMRuntime.cphandle + RAMMemoryModule.BLOCK_HEADER_SIZE_OFFSET, JVMRuntime.tablehandle, MemoryVisualizer.MM );
            
            checkStatus();
    
            // handle.next
            JVMRuntime.tablehandle = MemoryController.readWord( JVMRuntime.handle + RAMMemoryModule.BLOCK_HEADER_NEXT_OFFSET );
            
            checkStatus();
    
            // current.next = handle.next
            MemoryController.writeWord( JVMRuntime.cphandle + RAMMemoryModule.BLOCK_HEADER_NEXT_OFFSET, JVMRuntime.tablehandle, MemoryVisualizer.MM );
            
            checkStatus();
            
            // Here also clear the header bytes in the visualization system
            MemoryPool.initializeBlock(JVMRuntime.handle - RAMMemoryModule.BLOCK_HEADER_SIZE, RAMMemoryModule.BLOCK_HEADER_SIZE, MemoryVisualizer.EMPTY);
            
        }
        else
        {
            //System.err.println("<> lower block not merged for handle: " + Integer.toHexString(JVMRuntime.handle));
            // not merged with lower block.  inserted into free list after current.
            checkStatus();
    
            // current.next = handle
            MemoryController.writeWord( JVMRuntime.cphandle + RAMMemoryModule.BLOCK_HEADER_NEXT_OFFSET, JVMRuntime.handle, MemoryVisualizer.MM );
            
        }
        
        checkStatus();

        // 4.  freehead = current
        JVMRuntime.freehead = JVMRuntime.cphandle;
        
        
        
    }
    
}
