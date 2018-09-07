/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package emr.jvm.memory.peripheral;

import emr.jvm.Debug;
import java.util.Vector;

/**
 * The GPIO peripheral spans 0x40004000 to 0x40007FFF
 * 
 * It is further divided into 4 ports:
 *    PortA 0x40004000 - 0x40004FFF
 *    PortB 0x40005000 - 0x40005FFF
 *    PortC 0x40006000 - 0x40006FFF
 *    PortD 0x40007000 - 0x40007FFF
 *   
 * @author Evan Ross
 */
public class GPIO extends Peripheral
{

    int dataReg;
    int dirReg;
    
    Vector<GPIOPort> ports;
    
    private static int chunkSize = 0x1000;
    
    public GPIO(int base, int size, String name)
    {
        super(base, size, name);
        
        dataReg = 0xbe;
        dirReg = 0;
        
        ports = new Vector<GPIOPort>(4);   
        
        ports.add(new GPIOPort(0x0000, "porta"));
        ports.add(new GPIOPort(0x1000, "portb"));
        ports.add(new GPIOPort(0x2000, "portc"));
        ports.add(new GPIOPort(0x3000, "portd"));        
    }
    
    @Override
    protected int readWordRelative(int address) {
        // This address is relative to the base of the peripheral.
        
        // Get the index into one of 4 ports.
        int index = address / chunkSize;
        
        GPIOPort port = ports.get(index);
        return port.readWord(address);
    }

    @Override
    protected void writeWordRelative(int address, int value) {
        // Get the index into one of 4 ports.
        int index = address / chunkSize;
        
        GPIOPort port = ports.get(index);
        port.writeWord(address, value, null);        
    }
    
   /**
     * Options are:
     *   "porta/b/c/d".  Not case sensitive.
     * 
     * @param name identifier of GPIOPort instance to obtain
     * @return a GPIOPort instance
     */
    public GPIOPort getPort(String name)
    {
        for (GPIOPort m : ports)
        {
            if (m.getName().equalsIgnoreCase(name))
                return m;
        }
        return null;
    }    
    



}
