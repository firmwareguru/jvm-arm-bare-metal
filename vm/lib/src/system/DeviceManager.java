/*
 * DeviceManager.java
 *
 * Created on April 5, 2008, 3:41 PM
 *
 *
 */

package system;

import system.devices.SSI;
import system.graphics.GraphicsContext;
import system.graphics.OLEDGraphicsContext;
import system.devices.UART;

/**
 *
 * @author Evan Ross
 */
public class DeviceManager {
    
    
    private static final int UARTBase = 0x4000C000; 
    private static final int SSIBase = 0x40008000;
    
    private static final int SYSCTRL_RCGC1 = 0x400fe104;
    private static final int SYSCTRL_RCGC2 = 0x400fe108;
    
    public static final int DEVICE_UART = 0;
    
    /* 
     *  Track the instances of each device
     */
    Device deviceUART = null;
    Device deviceSSI = null;
    
    public Device getDevice(int device)
    {
        if (deviceUART == null)
            deviceUART = new UART(UARTBase, 8000000);
        
        return deviceUART;
    }
    
    public Device getSSI()
    {
        if (deviceSSI == null)
            deviceSSI = new SSI(SSIBase);
        
        return deviceSSI;
    }
    
    
    
    /**
     * Gets the processor clock rate.
     
     * This function determines the clock rate of the processor clock.  
     * This is also the clock rate of all the peripheral modules (with the 
     * exception of PWM, which has its own clock divider).
     * 
     * This will not return accurate results if SysCtlClockSet() has not
     * been called to configure the clocking of the device, or if the device is
     * directly clocked from a crystal (or a clock source) that is not one of the
     * supported crystal frequencies.  In the later case, this function should be
     * modified to directly return the correct system clock rate.
     * @return The processor clock rate.
     */
    public static int getProcessorClockRate()
    {
        return 8000000; // value returned by SysCtlClockGet on eval kit.
    }
    
    /**
     * 
     * @param address
     * @return
     */
    //public static native int getValue(int address);
    
    /**
     * 
     * @param address
     * @param value
     * @return
     */
    //public static native int setValue(int address, int value);    
    
    //public static native void setBit(int address, int bit);

    //public static native boolean isBitSet(int address, int bit);
    
    
 
    /** 
     * Get a GraphicsContext appropriate for the current hardware.
     * @return the current GraphicsContext
     */
    public static GraphicsContext getGraphicsContext()
    {
        return new OLEDGraphicsContext();
    }

    /**
     * Get an AudioSystem appropriate for the current hardware.
     */
    /*
     public static AudioSystem getAudioSystem()
     {
       return null;
     }
     */
    
    /**
     * Get the platform's UART instance.  The platform is configured
     * to support the UART and the UART instance is configured to support
     * the platform's current configuration.
     * 
     * @return a UART instance encapsulating the UART device.
     */
    /**
    public static UART getUART()
    {
        // Enable the peripherals:
        // UART0
        setBit(SYSCTRL_RCGC1, 1);
        // GPIO0
        setBit(SYSCTRL_RCGC2, 1);
        
        
        // UARTBase is part of the platform.
        // The clock rate is required.        
        UART uart = new UART(UARTBase, getProcessorClockRate());
                               
        return uart;
    }
    */
}
