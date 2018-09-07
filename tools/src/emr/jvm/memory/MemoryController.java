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
import java.util.Vector;

/**
 * The Memory system is encapsulated in this class.
 * The addresses for reads and writes are examined and
 * then redirected/remapped to the appropriate memory subsystem (RAM, NVM, peripherals).
 * 
 * @author Evan Ross
 */
public class MemoryController {
    
    // A Collection of MemoryInterfaces used to retrieve them.
    private static Vector<MemoryInterface> interfaceList;
    
    // An array of MemoryInterfaces that provides direct-index
    // mapping of addresses to modules.
    private static MemoryInterface[] interfaceMap;
    
    // Granularity of the interfaceMap.
    private static final int chunkSize = 0x10000;  // 64k
    
    // Address range of code space.  This is not the actual
    // amount of code space in the device.
    private static final int totalCodeSize = 0x20000000;
    
    // Address range of SRAM space.  This is not the actual
    // amount of SRAM space in the device.
    private static final int totalSramSize = 0x20000000;
    
    // Address range of Peripheral space
    private static final int totalPeripheralSize = 0x02000000;
    
    // This is the file that contains the code to initialize the
    // NVM module with.
    private static final String codeSourceFile = "c:/projects/jvm/javavirtualmachine/corelibrary.package";
    
    
    public static void initialize()
    {        
        // Create the MemoryInterface instances.
        // RAM : 64K @ base=0x20000000
        // NVM : 256K @ base=0x0
        MemoryInterface ram = new RAMMemoryModule("RAM", 0x10000, 0x20000000);
        //MemoryInterface nvm = new MemoryModule("NVM", 0x40000, 0x00000000);
        MemoryInterface peripheral = new PeripheralController();

        // temporarily crank up NVM to hold mp3 files / 8MB
        MemoryInterface nvm = new MemoryModule("NVM", 0x800000, 0x00000000);

        initializeModule(nvm, codeSourceFile);
        
        interfaceList = new Vector<MemoryInterface>();
        interfaceList.add(ram);
        interfaceList.add(nvm);
        interfaceList.add(peripheral);
        
        // Map the MemoryInterfaces.
        //
        // The "NVM" region begins at 0x0 and at cannot extend beyond 0x1fff ffff.
        // The "RAM" begins at 0x2000 0000 and cannot extend beyond 0x3fff ffff
        // The peripherals begin at 0x4000 0000 and end at 0x4200 0000
        // Note that these are the total address space allocated to various
        // functions within each region and not the actual ammount of NVM or RAM
        // that will exist.
        int numChunks = (totalCodeSize + totalSramSize + totalPeripheralSize) / chunkSize;
        interfaceMap = new MemoryInterface[numChunks];
        
        // Assign the MemoryInterfaces to the memoryMap at each index based on 
        // chunk size.  The unassigned indexes will generate a null pointer
        // exception which is caught when accessed.
        int numNvmChunks = nvm.getSize() / chunkSize;
        int numSramChunks = ram.getSize() / chunkSize;
        int numPeripheralChunks = peripheral.getSize() / chunkSize;

        for (int i = 0; i < numNvmChunks; i++)
            interfaceMap[i] = nvm;
        
        int sramStart = totalCodeSize / chunkSize;
        for (int i = sramStart; i < sramStart + numSramChunks; i++)
            interfaceMap[i] = ram;
        
        int peripheralStart = (totalCodeSize + totalSramSize) / chunkSize;
        for (int i = peripheralStart; i < peripheralStart + numPeripheralChunks; i++)
            interfaceMap[i] = peripheral;
        
        Debug.message("MemoryController initialized.");
        Debug.message("   chunkSize = " + chunkSize + " bytes");        
        Debug.message("   numChunks = " + numChunks);        
        Debug.message("   numNvmChunks = " + numNvmChunks);
        Debug.message("   numSramChunks = " + numSramChunks);
        Debug.message("   numPeripheralChunks = " + numPeripheralChunks);

    }
    
    /**
     * Load up the module with data from the given file.
     * 
     * @param memory
     * @param fileName
     */
    private static void initializeModule(MemoryInterface memory, String fileName)
    {
        try {
            File input = new File(fileName);
            FileInputStream is = new FileInputStream(input);
            
            memory.initialize(is, MemoryVisualizer.NVMDATA);
        }
        catch (IOException e) {
            Debug.fatalError(memory.getName() + " | Module initialization aborted. " + e.getMessage());
        }        
    }
    
    public static int readWord(int address)
    {
        int index = address / chunkSize;
        int value = -1;
        try {
            // Delegate to the appropriate MemoryModule
            value = interfaceMap[index].readWord(address);
        } catch (NullPointerException e) {
            Debug.fatalError("[MemoryController] readWord address references null handler: " + Integer.toHexString(address) + " index " + index);
        }        
        return value;        
   }
    
    public static int readShort(int address)
    {
        int index = address / chunkSize;
        int value = -1;
        try {
            // Delegate to the appropriate MemoryModule
            value = interfaceMap[index].readShort(address);
        } catch (NullPointerException e) {
            Debug.fatalError("[MemoryController] readShort address references null handler: " + Integer.toHexString(address) + " index " + index);
        }   
        return value;
    }
    
    public static int readByte(int address)
    {
        int index = address / chunkSize;
        int value = -1;
        try {
            // Delegate to the appropriate MemoryModule
            value = interfaceMap[index].readByte(address);
        } catch (NullPointerException e) {
            Debug.fatalError("[MemoryController] readByte address references null handler: " + Integer.toHexString(address) + " index " + index);
        }        
        return value;
    }
    
    public static void writeWord(int address, int value, Color attribute)
    {
        int index = address / chunkSize;
        try {
            // Delegate to the appropriate MemoryModule
            interfaceMap[index].writeWord(address, value, attribute);
        } catch (NullPointerException e) {
            Debug.fatalError("[MemoryController] writeWord address references null handler: " + Integer.toHexString(address) + " index " + index);
        }        
    }
    
    public static void writeShort(int address, int value, Color attribute)
    {
        int index = address / chunkSize;
        try {
            // Delegate to the appropriate MemoryModule
            interfaceMap[index].writeShort(address, value, attribute);
        } catch (NullPointerException e) {
            Debug.fatalError("[MemoryController] writeShort address references null handler: " + Integer.toHexString(address) + " index " + index);
        }        
        
    }
    
    public static void writeByte(int address, int value, Color  attribute)
    {
        int index = address / chunkSize;
        try {
            // Delegate to the appropriate MemoryModule
            interfaceMap[index].writeByte(address, value, attribute);
        } catch (NullPointerException e) {
            Debug.fatalError("[MemoryController] writeByte address references null handler: " + Integer.toHexString(address) + " index " + index);
        }       
    }
    
    /**
     * Options are:
     *   "NVM", "RAM", "Peripheral".  Not case sensitive.
     * 
     * @param name identifier of MemoryInterface instance to obtain
     * @return a MemoryInterface instance
     */
    public static MemoryInterface getInterface(String name)
    {
        for (MemoryInterface m : interfaceList)
        {
            if (m.getName().equalsIgnoreCase(name))
                return m;
        }
        return null;
    }
    
    
    public static void main(String[] args)
    {
        // NVM
        int base = 0x0;
        MemoryController.initialize();
        
        MemoryController.writeWord(base + 500, 0x11223344, MemoryVisualizer.INSTANCE);
        
        System.out.println("Got: " + Integer.toHexString(MemoryController.readWord(base + 500)));
        System.out.println("Got: " + Integer.toHexString(MemoryController.readShort(base + 500)));
        System.out.println("Got: " + Integer.toHexString(MemoryController.readShort(base + 502)));
        System.out.println("Got: " + Integer.toHexString(MemoryController.readByte(base + 501)));
        
        MemoryController.writeShort(base + 504, (short)0x5566, MemoryVisualizer.FRAME);
        
        System.out.println("Got: " + Integer.toHexString(MemoryController.readWord(base + 504)));
        System.out.println("Got: " + Integer.toHexString(MemoryController.readShort(base + 504)));
        System.out.println("Got: " + Integer.toHexString(MemoryController.readShort(base + 506)));
        System.out.println("Got: " + Integer.toHexString(MemoryController.readByte(base + 505)));

        // Try RAM
        base = 0x20000000;
        MemoryController.writeWord(base + 500, 0x11223344, MemoryVisualizer.INSTANCE);
        
        System.out.println("Got: " + Integer.toHexString(MemoryController.readWord(base + 500)));
        System.out.println("Got: " + Integer.toHexString(MemoryController.readShort(base + 500)));
        System.out.println("Got: " + Integer.toHexString(MemoryController.readShort(base + 502)));
        System.out.println("Got: " + Integer.toHexString(MemoryController.readByte(base + 501)));
        
    }
    

}
