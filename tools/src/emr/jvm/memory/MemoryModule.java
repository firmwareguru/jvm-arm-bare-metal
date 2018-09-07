/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package emr.jvm.memory;

import emr.jvm.Debug;
import emr.jvm.visualization.MemoryVisualizer;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * A MemoryModule is an array of bytes that has a visualizer 
 * attached.  This array may represent NVM or RAM.
 *
 * @author Evan Ross
 */
public class MemoryModule implements MemoryInterface {

    /* Id of this module */
    private String name;
    
    /* The ByteBuffer provides native type access to
     * a byte array that is the memory of this module.
     */
    private ByteBuffer byteBuffer;
    
    /*
     * The base address of this module.  This address is subtracted
     * from the given address to make the index 0-based.  This address
     * is given to the visualizer as well.
     */
    private int baseAddress;
    
    /*
     * These colors are associated with each byte in the array.
     */ 
    private Color[] tags;
    
    /* Visializes the contents of the memory array */
    public static MemoryVisualizer visualizer;
        
    /**
     * 
     * @param name Identifier of this MemoryModule
     * @param size size of the MemoryModule in bytes
     */
    public MemoryModule(String name, int size, int baseAddress)
    {
        this.name = name;
        this.baseAddress = baseAddress;
        byteBuffer = ByteBuffer.allocateDirect(size);
        tags = new Color[size];

        // Initialize each memory location
        for(int i = 0; i < size; i++) {
           byteBuffer.put(i, (byte)0xCC);
            tags[i] = MemoryVisualizer.EMPTY;
        }
        
        visualizer = new MemoryVisualizer(name, byteBuffer, tags, baseAddress);
        
    }       

    public void initialize(InputStream is, Color color) throws IOException
    {
        Debug.message("Initializing [" + name + "]... ");
        

        int byteRead = -1;
        int byteCount = 0;

        while ((byteRead = is.read()) != -1)
        {
            if (byteCount >= byteBuffer.capacity())
                throw new IOException("Capacity exceeded.");

            byteBuffer.put(byteCount, (byte)byteRead);
            tags[byteCount] = color;
            byteCount++;
        }

        Debug.message("   " + byteCount + " bytes loaded.");

    }
    
    
    /**
     * Reads four bytes at the given address and composes a word.
     * 
     * @param address - The 4-byte-aligned address of the word.
     * @return The word.
     */
    public int readWord(int address) {
        int index = address - baseAddress;
        if (index % 4 != 0)
            Debug.fatalError("[" + name + "] readWord(" + index + ") not 4-byte aligned");
        
        return byteBuffer.getInt(index);
    }

    /**
     * Reads two bytes at the given address and composes a word.
     * 
     * @param address - The 2-byte-aligned address of the short.
     * @return The short.
     */
    public int readShort(int address) {
        int index = address - baseAddress;
        
        // 2-byte alignment enforcement is not possible because some reads are based on
        // the PC (which is byte aligned) as part of opcode operands.
        //if (index % 2 != 0)
        //    Debug.fatalError("[" + name + "] readShort(" + index + ") not 2-byte aligned");
        
        return byteBuffer.getShort(index);
    }

    /**
     * Returns 1 byte at the given address.
     * 
     * @param address - The address of the byte.
     * @return The byte.
     */
    public int readByte(int address) {
        int index = address - baseAddress;
        return byteBuffer.get(index);
    }

    /**
     * Writes the given word into the memory array at the given address.
     * 
     * @param address - The 4-byte-aligned address to write the word to.
     * @param value - The word to write.
     * @param attribute - A Color to visualize the word at.
     */
    public void writeWord(int address, int value, Color attribute) {
        int index = address - baseAddress;
        if (index % 4 != 0)
            Debug.fatalError("[" + name + "] writeWord(" + index + ") not 4-byte aligned");
        
        byteBuffer.putInt(index, value);
        
        tags[index] = attribute;
        tags[index+1] = attribute;
        tags[index+2] = attribute;
        tags[index+3] = attribute;
        
        visualizer.repaintZoomPanels(index, 4);
    }

    public void writeShort(int address, int value, Color attribute) {
        int index = address - baseAddress;
        if (index % 2 != 0)
            Debug.fatalError("[" + name + "] writeShort(" + index + ") not 2-byte aligned");
        
        byteBuffer.putShort(index, (short)value);
        tags[index] = attribute;
        tags[index+1] = attribute;
        
        visualizer.repaintZoomPanels(index, 2);
    }

    public void writeByte(int address, int value, Color attribute) {
        int index = address - baseAddress;
        byteBuffer.put(index, (byte)value);
        tags[index] = attribute;
        
        visualizer.repaintZoomPanels(index, 1);
    }
    
    public String getName() {
        return name;
    }

    public int getSize() {
        return byteBuffer.capacity();
    }
    
    public int getBaseAddress()
    {
        return baseAddress;
    }
    
    public MemoryVisualizer getVisualizer()
    {
        return visualizer;
    }
    
    public static void main(String[] args)
    {
        int base = 0x0;
        MemoryModule m = new MemoryModule("Test", 0x10000, base);
        
        m.writeWord(base + 500, 0x11223344, MemoryVisualizer.INSTANCE);
        
        System.out.println("Got: " + Integer.toHexString(m.readWord(base + 500)));
        System.out.println("Got: " + Integer.toHexString(m.readShort(base + 500)));
        System.out.println("Got: " + Integer.toHexString(m.readShort(base + 502)));
        System.out.println("Got: " + Integer.toHexString(m.readByte(base + 501)));
        
        m.writeShort(base + 504, (short)0x5566, MemoryVisualizer.FRAME);
        
        System.out.println("Got: " + Integer.toHexString(m.readWord(base + 504)));
        System.out.println("Got: " + Integer.toHexString(m.readShort(base + 504)));
        System.out.println("Got: " + Integer.toHexString(m.readShort(base + 506)));
        System.out.println("Got: " + Integer.toHexString(m.readByte(base + 505)));
        
        Debug.message("Initializing NVM");
        // load up the NVM
        try
        {
            File input = new File("c:/projects/jvm/javavirtualmachine/corelibrary.package");
            FileInputStream is = new FileInputStream(input);
            
            m.initialize(is, MemoryVisualizer.NVMDATA);
        }
        catch (IOException e)
        {
            Debug.message("JVM initialization aborted. " + e.getMessage());
        }
        
        
    }
           

    

}
