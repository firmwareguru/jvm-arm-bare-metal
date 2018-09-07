/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package system.devices;

import system.Device;
import system.Memory;

/**
 *
 * @author Evan Ross
 */
public class GPIO extends Device
{
    
    public static final int GPIO_PORTA_BASE = 0x40004000;  // GPIO Port A
    public static final int GPIO_PORTB_BASE = 0x40005000;  // GPIO Port B
    public static final int GPIO_PORTC_BASE = 0x40006000;  // GPIO Port C
    public static final int GPIO_PORTD_BASE = 0x40007000;  // GPIO Port D
    
    public static final int GPIO_PIN_0 = 0x00000001;  // GPIO pin 0
    public static final int GPIO_PIN_1 = 0x00000002;  // GPIO pin 1
    public static final int GPIO_PIN_2 = 0x00000004;  // GPIO pin 2
    public static final int GPIO_PIN_3 = 0x00000008;  // GPIO pin 3
    public static final int GPIO_PIN_4 = 0x00000010;  // GPIO pin 4
    public static final int GPIO_PIN_5 = 0x00000020;  // GPIO pin 5
    public static final int GPIO_PIN_6 = 0x00000040;  // GPIO pin 6
    public static final int GPIO_PIN_7 = 0x00000080;  // GPIO pin 7
    
            
    
    private static final int GPIO_0_DATA = 0x00000000; 
    public GPIO()
    {
        super(0,0,0);
    }
    
    public void pinWrite(int port, int pins, int val)
    {
        Memory.setWord(port + GPIO_0_DATA + (pins << 2), val);
    }
}
