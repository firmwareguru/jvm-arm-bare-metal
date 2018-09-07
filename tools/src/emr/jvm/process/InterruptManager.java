/*
 * InterruptManager.java
 *
 * Created on February 24, 2007, 1:38 PM
 *
 * The Interrupt Manager simulates interrupt signals received from various sources.  It implements a 16 to 4 priority encoder.
 *
 * When an interrupt occurs the INT pin of the priority encoder is activated, signalling to the processor core that an interrupt is pending.
 * This implementation relies on the ProcessManager state machine continuously polling the interruptPending() method to detect this activation.
 *
 * The ProcessManager PCW bits 3-6 are set according to the output of the priority encoder.
 */

package emr.jvm.process;

/**
 *
 * @author Evan Ross
 */
public class InterruptManager
{
    
    /**
     *  Interrupt pending signal.  Interpret as TRUE = pending, FALSE = nothing pending.
     */
    private static boolean INT = false;
    
    /** 
     *  Interrupt mask word.  1 = accept.  0 = mask (ignore).
     */
    private static int mask = 0x00;
    
    /**
     *  latch register
     */
    private static int latch = 0x00;
    
    /**
     *  Bit defines for each interrupt in the latch and mask registers
     */
    private static int INT0 = 0x1;
    private static int INT1 = 0x2;
    private static int INT2 = 0x4;
    private static int INT3 = 0x8;
    private static int INT4 = 0x10;
    private static int INT5 = 0x20;
    private static int INT6 = 0x40;
    private static int INT7 = 0x40;
    private static int INT8 = 0x40;
    
    
    /** 
     *  Check if there are pending interrupts.  Return true if there are.
     *
     *  This function is akin to sampling an INT pin or an IDLE pin of a
     *  priority encoder.  If there were no interrupts pending, then return false, otherwise
     *  something is pending and return true.
     */
    public static boolean interruptPending()
    {
        // set the PCW here as well since this method is called in the context of the core and thus
        // the PCW is not being concurently modified.
        priorityEncode();
        
        return INT;
    }
    
    /**
     *  Activate interrupt 0
     */
    public static void setINT0()
    {
        latch |= INT0;
    }

    /**
     *  Clear posted interrupt 0
     */
    public static void resetINT0()
    {
        latch &= ~INT0;
    }
    
    /**
     *  Pass the value of 'latch' through 'mask' and perform a priority encoding operation on the bits.
     *  The PCW is updated with the 4 bits of the encoded vector offset while the INT pin is set if there
     *  is anything set. 
     */
    private static void priorityEncode()
    {
        // apply the mask to the posted interrupts
        int input = latch & mask;
        
        // assume there is a posted interrupt initially
        INT = true;
        
        int output = 0;
        
        if( (input & INT0) == INT0 )
            output = 0;
        else if( (input & INT1) == INT1)
            output = 1;
        else if( (input & INT2) == INT2)
            output = 2;
        else if( (input & INT3) == INT3)
            output = 3;
        else if( (input & INT4) == INT4)
            output = 4;
        else if( (input & INT5) == INT5)
            output = 5;
        else if( (input & INT6) == INT6)
            output = 6;
        else if( (input & INT7) == INT7)
            output = 7;
        else if( (input & INT8) == INT8)
            output = 8;
        else 
            INT = false;  // nothing was set
       
        ProcessManager.setPCWInt(output);
            
        
            
    }
    
    
    //////////////////////////////////////////////////////////
    ///
    /// Interrupt Threads that randomly trigger interrupts
    ///
    //////////////////////////////////////////////////////////
    
    class INT0Thread extends Thread
    {
        public void run()
        {
            // set an interrupt every 5 seconds
            while(true)
            {
                try { sleep(5000); } catch (InterruptedException e ) { }
                setINT0();
            }
        }
    }
    
    
}
