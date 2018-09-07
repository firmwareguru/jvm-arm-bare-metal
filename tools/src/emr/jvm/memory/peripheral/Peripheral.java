/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package emr.jvm.memory.peripheral;

import emr.jvm.memory.MemoryInterface;
import java.awt.Color;
import java.io.InputStream;

/**
 * This is the super class for all peripheral devices.
 * 
 * 
 *
 * @author Evan Ross
 */
public abstract class Peripheral implements MemoryInterface 
{
    // The addresses given to the methods of a peripheral
    // are relative to the peripheral region's base address.
    
    // This is the base address of this particular peripheral,
    // relative to the peripheral base address.
    int baseAddress;
    
    int size;
    
    String name;
    
    public Peripheral(int base, int size, String name)
    {
        baseAddress = base;
        this.size = size;
        this.name = name;
    }
    
    // This address here is already offset from the base of the
    // Peripheral Controller.
    public int readWord(int address) {
        return readWordRelative(address - baseAddress);
    }
    
    /**
     * Read a word from the given address.
     * @param address - relative to the base of this peripheral.
     */
    protected abstract int readWordRelative(int address);
    
    protected abstract void writeWordRelative(int address, int value);
    

    public int readShort(int address) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int readByte(int address) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void writeWord(int address, int value, Color attribute) {
        writeWordRelative(address - baseAddress, value);
    }

    public void writeShort(int address, int value, Color attribute) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void writeByte(int address, int value, Color attribute) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public int getBaseAddress() {
        return baseAddress;
    }

    public void initialize(InputStream is, Color color) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
