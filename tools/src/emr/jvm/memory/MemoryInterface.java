/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package emr.jvm.memory;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;

/**
 * A MemoryInterface is implemented by anything that is accessable
 * for reads and writes by the MemoryController.  Such things include
 * MemoryModules (NVM, RAM) or memory mapped peripherals.
 * 
 * @author Evan Ross
 */
public interface MemoryInterface {
    
    
    
    public int readWord(int address);

    
    public int readShort(int address);

    
    public int readByte(int address);

    /**
     * 
     * @param address - 32-bit address of memory location to write value
     * @param value - the value to write to the indicated memory location
     * @param attribute - an attribute passed to the implementation of
     * the MemoryInterface that receives this request.
     */
    public void writeWord(int address, int value, Color attribute);

    
    public void writeShort(int address, int value, Color attribute);

    
    public void writeByte(int address, int value, Color attribute);
    
    /**
     * A name by which this memory interface can be
     * referenced by.
     * 
     * @return Name of the type of memory interface.
     */
    public String getName();
    
    public int getSize();
    
    /**
     * Get the base address offset.
     * 
     * @return
     */
    public int getBaseAddress();
    
    /**
     * Initialize this memory module with the bytes in the given
     * InputStream.  As many bytes as there are in the stream, or
     * until the memory is full, are read.
     * @param is - stream supplying the bytes.
     * @param color - color to apply to all the locations
     * 
     */
    public void initialize(InputStream is, Color color) throws IOException;
    

}
