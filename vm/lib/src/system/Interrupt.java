/*
 * Interrupt.java
 *
 * Created on May 15, 2007, 9:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package system;

import java.lang.Thread;

/**
 *
 * @author Evan Ross
 */
public class Interrupt
{
    
    public static final int UART = 0;
    public static final int SPI  = 1;
    
    /**
     *  Set the Thread object that handles the specified interrupt type.
     */
    public static native void setInterruptHandler(Thread handler_, int type_);
    
    /**
     *  A special yield method.  This yield must be called by Interrupt 
     *  Threads to remove them from the eligible queue so they will 
     *  execute only when another interrupt is handled.
     */
    //public static native void yield();
    
}
