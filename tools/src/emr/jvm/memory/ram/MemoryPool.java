/*
 * MemoryPool.java
 *
 * Created on September 25, 2006, 10:00 PM
 *
 * The MemoryPool acts as an interface between a contiguous block of memory and an
 * application wishing to access that memory.  The MemoryPool allocates handles, which
 * are integer indexes into the memory array.
 */

package emr.jvm.memory.ram;


import emr.jvm.visualization.MemoryVisualizer;
import emr.jvm.Debug;
import emr.jvm.JVMRuntime;
import emr.jvm.memory.MemoryController;
import java.awt.Color;

/**
 *
 * @author Ross
 */
public class MemoryPool {

    /* Constant: Number of blocks */
    private static final int maxBlocks = 100;
    
    /* Constant: Index of first location in the memory pool */
    //private static final int MEMORY_HEAP_START = JVMRuntime.RuntimeStateSize;
    private static final int MEMORY_HEAP_START = 0;  // start of heap
    
    /* The array of bytes composing the RAM */
    //static int[] memoryArray;
    static int heapSize;
    
    
    public static Color initializationColor = MemoryVisualizer.EMPTY;
    
    //////////////////////
    // Linked list stuff
    //////////////////////
    
    
    /* start of list */
    static DataBlock listHead;
    
    /* current block in list */
    static DataBlock listCurrent;

            
    public static void initialize( int heapSize_ )
    {
        heapSize = heapSize_;
        
        DataBlock currentBlock;
        DataBlock previousBlock;
        DataBlock nextBlock;
        
        // create array of DataBlocks
        DataBlock[] blocks = new DataBlock[maxBlocks];
        for( int i = 0; i < maxBlocks; i++ )
        {
            blocks[i] = new DataBlock(heapSize_);
            blocks[i].setId(i);
        }
        
        for( int i = 0; i < maxBlocks; i++ )
        {
            if(i == 0)
                previousBlock = null;
            else
                previousBlock = blocks[i - 1];
            
            currentBlock = blocks[i];
            
            if(i == maxBlocks - 1)
                nextBlock = null;
            else
                nextBlock = blocks[i + 1];
            
            // setup the links
            currentBlock.setNextBlock(nextBlock);
            currentBlock.setPrevBlock(previousBlock);
        }
        
        listHead = blocks[0];
        listCurrent = listHead;

    }
    
    
    /////////////////////////////////////////////////////////////////////////
    // The Public Interface
    /////////////////////////////////////////////////////////////////////////

    /** Allocates a region of memory of size numBytes_ 
     * Returns a handle to the newly allocated memory
     */
    public static int allocate(int numBytes_ )
    {
        if( numBytes_ <= 0 )
            return -1;
        
        // start searching from beginning of block list
        reset();
        
        // look for an available block
        while(getNextAvailableBlock())
        {
            // an available block was found
            if ( listCurrent.getAvailableSpace() >= numBytes_ )
            {
                // we found the block
                DataBlock newBlock = allocateBlock(numBytes_);
                
                // initialize the block
                initializeBlock(newBlock.getHandle(), newBlock.getSize(), initializationColor);
                
                return newBlock.getHandle();
            }
        }

        // went through all available blocks, none have space
        Debug.error("unable to allocate " + numBytes_ + " bytes");
        return -1;
            
    }
    
    /** Returns the memory specified by the given handle to the pool */
    public static boolean deallocate(int handle_)
    {
        Debug.message("deallocate...");
        // optimization: see if listCurrent matches given handle
        if( listCurrent != null && listCurrent.getHandle() == handle_ )
        {
            deallocateBlock();
            computeSpace();
            return true;
        }
        
        reset();
        
        while( getNextUsedBlock() )
        {
            //Debug.message("get next used block... id " + listCurrent.getId());
            if ( listCurrent.getHandle() == handle_ )
            {
                deallocateBlock();
                computeSpace();
                return true;
            }
        }
        
        Debug.error(" -- MemoryPool: deallocate cannot find handle: " + handle_);
        return false;
    }

    /** Returns the size of the memory pool */
    public static int getSize()
    {
        return heapSize;
    }
    
    
    ////////////////////////////////////////////////////////////////////////////////
    // PRIVATE Methods
    ////////////////////////////////////////////////////////////////////////////////

    private static void deallocateBlock()
    {
        Debug.message(" --- MemoryPool : deallocate ---");
        Debug.message("             ID : " + listCurrent.getId());
        Debug.message("         handle : " + listCurrent.getHandle());
        Debug.message("           size : " + listCurrent.getSize());
        
        // clear the allocated space in the visualizer
        initializeBlock( listCurrent.getHandle(), listCurrent.getSize(), MemoryVisualizer.EMPTY);
        
        
        // clear the block
        listCurrent.setSize(0);
    }
    
    private static DataBlock allocateBlock(int size_)
    {
        // we are allocating the block pointed to by listCurrent
        DataBlock newBlock = listCurrent;
        
        // find the index into this array
        getPreviousUsedBlock();
        int index;
        
        if( listCurrent == null )
        {
            index = MEMORY_HEAP_START;
        }
        else
        {
            index = listCurrent.getHandle() + listCurrent.getSize();
        }
        
        newBlock.setHandle(index);
        newBlock.setAvailableSpace(0);
        newBlock.setSize(size_);

        Debug.message(" +++ MemoryPool : allocate +++");
        Debug.message("             ID : " + newBlock.getId());
        Debug.message("         handle : " + newBlock.getHandle());
        Debug.message("           size : " + newBlock.getSize());
        
        return newBlock;
        
    }
    
    
    
    /**************************************************************************/
    //  List Traversal functions
    /**************************************************************************/
    
    /** Places listCurrent at first available block or null */
    private static boolean getAvailableBlock()
    {
        while( listCurrent != null )
        {
        
            if ( listCurrent.isAvailable() == true )
            {
                // found a an available block
                return true;
            }
            
            // move to next block
            listCurrent = listCurrent.getNextBlock();
        }
        
        return false;
    }
    
    /** Places listCurrent at next used block or null */
    private static boolean getUsedBlock()
    {
        while( listCurrent != null )
        {
        
            if ( listCurrent.isAvailable() == false )
            {
                // found a used block
                return true;
            }
            // move to next block
            listCurrent = listCurrent.getNextBlock();
        }
        return false;
    }
    
    
    
    /** Places listCurrent at next available block or null */
    private static boolean getNextAvailableBlock()
    {

        while( listCurrent != null )
        {
            // move to next block
            listCurrent = listCurrent.getNextBlock();
        
            if ( listCurrent != null && listCurrent.isAvailable() == true )
            {
                // found a an available block
                return true;
            }
        }
        
        return false;
    }
    
    /** Places listCurrent at next used block or null */
    private static boolean getNextUsedBlock()
    {
        while( listCurrent != null )
        {
            // move to next block
            listCurrent = listCurrent.getNextBlock();
        
            if ( listCurrent != null && listCurrent.isAvailable() == false )
            {
                // found a used block
                return true;
            }
        }
        return false;
    }

    private static boolean getPreviousAvailableBlock()
    {
        while( listCurrent != null )
        {
            // move to next block
            listCurrent = listCurrent.getPrevBlock();
        
            if ( listCurrent != null && listCurrent.isAvailable() == true )
            {
                // found a an available block
                return true;
            }
        }
        return false;
    }
    
    private static boolean getPreviousUsedBlock()
    {

        while( listCurrent != null )
        {
            // move to previous block
            listCurrent = listCurrent.getPrevBlock();
        
            if ( listCurrent != null && listCurrent.isAvailable() == false )
            {
                // found an available block
                return true;
            }

        }
        
        return false;
    }
    
    /** computes the block space of the block pointed to by listCurrent */
    private static void computeBlockSpace()
    {
        DataBlock prevUsed = null;
        DataBlock nextUsed = null;
        DataBlock currentBlock = listCurrent;
        
        int newIndex;
        int nextIndex;
        
        getNextUsedBlock();
        nextUsed = listCurrent;

        // restore current pointer
        listCurrent = currentBlock;

        getPreviousUsedBlock();
        prevUsed = listCurrent;
        
        // restore current pointer
        listCurrent = currentBlock;
        
        // from previous data block:
        // find the next available address in the memory pool based on size of previous allocated block
        if( prevUsed == null )
            newIndex = 0;
        else
            newIndex = prevUsed.getHandle() + prevUsed.getSize();

        // find next used address in the memory pool and compute the "space" assigned to this new block
        if( nextUsed == null )
            nextIndex = getSize(); // size of entire memory array
        else
            nextIndex = nextUsed.getHandle();
        
        currentBlock.setAvailableSpace(nextIndex - newIndex);
    }
    
    
    /** this function must be called after each allocation and deallocation
     *  recomputes the available space for all unused blocks
     */
    private static void computeSpace()
    {
        reset();
        
        // go through all available blocks and call computeBlockSpace on each
        while( getNextAvailableBlock() )
        {
            computeBlockSpace();
        }
    }

    /** Initialize a region of memory with 0's and set the color according to type */
    public static void initializeBlock(int index_, int size_, Color type_)
    {
        for( int i = index_; i < index_ + size_; i++ )
        {
            MemoryController.writeByte(i, 0, type_);
        }
    }
    
    
    
    private static void reset()
    {
        listCurrent = listHead;
    }
    
    

    
}
