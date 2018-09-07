/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package emr.jvm.memory.peripheral;

import java.util.Vector;

/**
 *
 * @author Evan Ross
 */
public class SSI extends Peripheral
{

    // These are indexes into 32-bit register bank
    private static final int CR0    = 0;
    private static final int DR     = 1;
    private static final int SR     = 2;
    private static final int CPSR   = 3;
    private static final int IIM    = 4;
    private static final int IRIS   = 5;
    private static final int IMIS   = 6;

    private int[] regs;
    
    private Vector<BusDevice> devices;

    public SSI()
    {
        // Base address is offset from the peripheral region base.
        super(0x8000, 0x4000, "ssi");
        
        // 20 regs
        regs = new int[20];
        
        devices = new Vector<BusDevice>();
    }
    
    
    @Override
    protected int readWordRelative(int address) {
        
        throw new UnsupportedOperationException("SSI read not supported yet.");
        /*
        int reg = address / 4;
        if (reg == DR)
        {   
            // Don't worry about a FIFO, just send to the devices. 
        }
         */
    }

    @Override
    protected void writeWordRelative(int address, int value) {
        int reg = address / 4;
        if (reg == DR)
        {   
            // Don't worry about a FIFO, just send to the devices. 
            for (BusDevice d : devices)
            {
                d.write(value);
            }
        }
        else
        {
            throw new UnsupportedOperationException("SSI register not supported yet.");
        }
    }
    
    public void addDevice(BusDevice d)
    {
        devices.add(d);
    }
}
