/*
 * DataBlock.java
 *
 * Created on September 25, 2006, 9:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.jvm.memory.ram;

/**
 *
 * @author Ross
 */
public class DataBlock {
    
    /* size of the available memory region */
    private int availableSpace;
    
    /* pointer or reference to a contiguous chunk of data */
    private int dataIndex;
    
    /* size of data pointed to by this block */
    private int blockSize;
    
    /* An identifying number for debugging */
    private int blockId;
    
    /* The size of a datablock, in terms of ints */
    public static final int size = 5; // space, index, size, next, previous
    
    // Linked List stuff
    DataBlock nextBlock;
    DataBlock prevBlock;
    
    /** Creates a new instance of DataBlock */
    public DataBlock(int memorySize_) 
    {
        // initialize available space to entire region 
        availableSpace = memorySize_;
        
        dataIndex = -1;
        blockSize = 0;
        nextBlock = null;
        prevBlock = null;
    }
    
    public int getSize()
    {
        return blockSize;
    }
    
    public void setSize(int size_)
    {
        blockSize = size_;
    }
    
   
    public int getHandle()
    {
        return dataIndex;
    }
    
    public void setHandle(int handle_) 
    {
        dataIndex = handle_;
    }
    
    public int getAvailableSpace()
    {
        return availableSpace;
    }
    
    public void setAvailableSpace(int space_)
    {
        availableSpace = space_;
    }
    
    public boolean isAvailable()
    {
        return getSize() == 0;
    }
    
    public void setId(int id_)
    {
        blockId = id_;
    }
    
    public int getId()
    {
        return blockId;
    }
    
    
    ///////////////////////////////////
    /// Linked List
    ///////////////////////////////////
    public DataBlock getNextBlock()
    {
        return nextBlock;
    }
    
    public DataBlock getPrevBlock()
    {
        return prevBlock;
    }
    
    public void setNextBlock(DataBlock next_)
    {
        nextBlock = next_;
    }
    
    public void setPrevBlock(DataBlock prev_)
    {
        prevBlock = prev_;
    }
    
}
