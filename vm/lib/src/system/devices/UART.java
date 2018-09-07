/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package system.devices;

import java.io.InputStream;
import java.io.OutputStream;
import system.Device;

/**
 *
 * @author Evan Ross
 */
public class UART extends Device {
    
    
    // UART register offsets
    private static final int DR     = 0x0;  // UART Data
    private static final int RSR    = 0x4;  // UART Receive Status/Error Clear
    private static final int FR     = 0x18; // UART Flag
    private static final int ILPR   = 0x20; // UART IrDA Low-Power Register
    private static final int IBRD   = 0x24; // UART Integer Baud-Rate Divisor
    private static final int FBRD   = 0x28; // UART Fractional Baud-Rate Divisor
    private static final int LCRH   = 0x2C; // UART Line Control
    private static final int CTL    = 0x30; // UART Control
    
    // Data register bits
    private static final int DR_OE  = 0x00000800; // Overrun Error
    private static final int DR_BE  = 0x00000400; // Break Error
    
    // The UARTs clock input, used to derive baud rates.
    private int clock;
    
     
     
    /**
     * 
     * @param baseAddress 
     * @param clock - rate of the clock supplied to the UART.  This
     * will be the same as the processor clock.
     */
    
    
    public UART(int baseAddress, int clock)
    {
        super(baseAddress, DR, DR);        
        this.clock = clock;
    }
    

            
}
