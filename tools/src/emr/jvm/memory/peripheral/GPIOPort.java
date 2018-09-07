/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package emr.jvm.memory.peripheral;

import emr.jvm.Debug;

/**
 *
 * @author Evan Ross
 */
public class GPIOPort extends Peripheral {
    
    Signalable[] signalables;
    
    int dataReg;
    int dirReg;
    
  
    public GPIOPort(int base, String name)
    {
        super(base, 0x1000, name);
        
        dataReg = 0xbe;
        dirReg = 0;
  
        signalables = new Signalable[8];
        
        for (int i = 0; i < 8; i++)
            signalables[i] = null;
    }
    
    /**
     * 
     * @param d - a device to receive a pin signal.
     * @param pin - port pin from 1 to 8 to attach Signalable device
     */
     
    public void addDevice(Signalable d, int pin)
    {
        if (pin > 8 || pin < 1)
            Debug.fatalError("GPIOPort: pin out of range " + pin);
        
        signalables[pin - 1] = d;        
    }

    @Override
    protected int readWordRelative(int address) {
        int value = 0;

        Debug.message("GPIO" + name + ": readWordRelative: " + Integer.toHexString(address) + " " + Integer.toHexString(value));
        
        // DataReg
        if (address < 0x400)
        {
            // Take bits 9:2 of address and mask them with dataReg to get result.
            int mask = (address & 0x3fc) >> 2;
        
            value = mask & dataReg;
            Debug.message("   Value, DataReg: " + Integer.toHexString(value) + " " + Integer.toHexString(dataReg));
            
        }
        
        return value;
     }

    @Override
    protected void writeWordRelative(int address, int value) {
        
        // Address is relative to base of this port
        Debug.message("GPIO" + name + ": writeWordRelative: " + Integer.toHexString(address) + " " + Integer.toHexString(value));
        
        // DataReg
        if (address < 0x400)
        {
            // Take bits 9:2 of address and mask them with dataReg to get result.
            int mask = (address & 0x3fc) >> 2;
        
            dataReg &= ~mask; // clear the masked bits first
            dataReg |= mask & value;  // set the bits
            Debug.message("   DataReg:" + Integer.toHexString(dataReg));
            
            // For each write we signal the device.
            signalDevice();
        }
    }
    
    
    private void signalDevice()
    {
        for (int i = 0; i < 8; i++)
        {
            int bit = (dataReg >> i) & 0x1;

            if (signalables[i] != null)
            {
                Debug.message("Signalling device: " + i + " " + (bit == 1));
                signalables[i].sendSignal(bit == 1);
            }                            
        }
    }
    

       
    
 
    
    

}
