/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package emr.jvm.memory;

import emr.jvm.memory.peripheral.*;
import java.awt.Color;
import java.io.InputStream;
import java.util.Vector;

/**
 * Here we direct requests to the appropriate individual
 * peripheral module.
 * 
 * Each module spans at a minimum an address range of 0x4000 bytes.
 * Therefore the "chunk" size of peripheral modules is 0x4000.
 * 
 * @author Evan Ross
 */
public class PeripheralController implements MemoryInterface {

    
    /* Minimum size of an individual peripheral */
    private final int chunkSize = 0x4000;
    
    /* Size of the Peripheral region */
    private final int regionSize = 0x02000000;
    
    /*
     * Size of the active region:
     *    0x400FEFFF - 0x40000000
     */
    private final int activeSize = 0x400FEFFF - 0x40000000;
    
    /* Base address of the peripheral region */
    private final int baseAddress = 0x40000000;
    
    private Vector<MemoryInterface> peripherals;
    
    
    /*
     * Individual peripherals are assigned to this map based on
     * the chunk size.
     */
    private MemoryInterface[] peripheralMap;
    
    public PeripheralController()
    {
        peripherals = new Vector<MemoryInterface>();
        
        int numChunks = activeSize / chunkSize;
        peripheralMap = new MemoryInterface[numChunks];
        
        // Instantiate each kind of peripheral
        MemoryInterface gpio_ad = new GPIO(0x04000, 0x4000, "GPIO_AD");
        MemoryInterface gpio_eh = new GPIO(0x24000, 0x4000, "GPIO_EH");
        MemoryInterface ssi = new SSI();
        
        peripherals.add(gpio_ad);
        peripherals.add(gpio_eh);
        
        
        //peripheralMap[0] = watchDog;

        // GPIO ports A,B,C,D (0x4000)
        peripheralMap[1] = gpio_ad;
        
        // SSI0 (0x8000)
        peripheralMap[2] = ssi;
        
        // UART0
        

        // GPIO ports E,F,G,H
        peripheralMap[4] = gpio_eh;
        
        
    }
    
    public int readWord(int address) {
        // Global address
        // Make the address relative to the base and determine the
        // module index.
        int relativeAddress = (address - baseAddress);
        int index = relativeAddress / chunkSize;
        
        return peripheralMap[index].readWord(relativeAddress);        
    }

    public int readShort(int address) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int readByte(int address) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void writeWord(int address, int value, Color attribute) {
        // Global address
        // Make the address relative to the base and determine the
        // module index.
        int relativeAddress = (address - baseAddress);
        int index = relativeAddress / chunkSize;
        
        peripheralMap[index].writeWord(relativeAddress, value, attribute);        
    }

    public String getName() {
        return "Peripheral";
    }

    public int getSize() {
        return regionSize;
    }
    
    public int getBaseAddress()
    {
        return baseAddress;
    }


    public void initialize(InputStream is, Color color) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void writeShort(int address, int value, Color attribute) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void writeByte(int address, int value, Color attribute) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * Options are:
     *   "GPIO_AD", "GPIO_EH", "SSI0", "UART0".  Not case sensitive.
     * 
     * @param name identifier of MemoryInterface instance to obtain
     * @return a MemoryInterface instance
     */
    public MemoryInterface getInterface(String name)
    {
        for (MemoryInterface m : peripherals)
        {
            if (m.getName().equalsIgnoreCase(name))
                return m;
        }
        return null;
    }
    
    
    
    public static void main(String[] args)
    {
        PeripheralController pc = new PeripheralController();
        GPIO gpio = (GPIO)pc.getInterface("gpio_ad");
        GPIOPort port = gpio.getPort("porta");
        port.addDevice(new Signalable() 
            { public void sendSignal(boolean signal) { }

            public boolean getSignal() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }, 4);
        
        int address = pc.getBaseAddress() + 0x4000 + 0x98;
        int value = 0xeb;
        
        pc.writeWord(address, value, null);
        
        address = pc.getBaseAddress() + 0x4000 + 0xC4;
        pc.readWord(address);
                
    }

}
