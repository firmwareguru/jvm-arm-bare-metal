/*
 * TimerLoad.java
 *
 * Created on April 15, 2007, 6:03 PM
 *
 * Copy the current timer value in SYSTEMTIMER register to VALUE1 register in preparation for
 * subtraction with delay in VALUE2 (placed there in LocalGetSingleAlt).
 */

package emr.jvm.process.system;

import emr.jvm.process.JVMProcess;
import emr.jvm.Debug;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.nvm.*; // for the nvm
import emr.jvm.memory.ram.*; // for the ram


/**
 *
 * @author Evan Ross
 */
public class TimerLoad extends JVMProcess
{
    
    /** Creates a new instance of TimerLoad */
    public TimerLoad()
    {
        super("TimerLoad");
    }
    
    public void runProcess()  
    {
        checkStatus();
        
        JVMRuntime.value1 = JVMRuntime.systemtimer;
    }
    
    
}
