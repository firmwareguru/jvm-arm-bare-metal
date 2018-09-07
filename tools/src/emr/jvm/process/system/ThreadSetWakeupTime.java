/*
 * ThreadSetWakeupTime.java
 *
 * Created on April 15, 2007, 6:11 PM
 *
 * Set the new wakeup time computed in processes earlier in VALUE into thread object's wakeuptime variable.
 * Thread object is pointed to by CURRENTTHREAD register
 */

package emr.jvm.process.system;

import emr.jvm.process.JVMProcess;
import emr.jvm.Debug;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.nvm.*; // for the nvm
import emr.jvm.memory.ram.*; // for the ram
import emr.jvm.visualization.MemoryVisualizer;


/**
 *
 * @author Evan Ross
 */
public class ThreadSetWakeupTime extends JVMProcess
{
    
    /** Creates a new instance of ThreadSetWakeupTime */
    public ThreadSetWakeupTime()
    {
        super("ThreadSetWakeupTime");
    }
    
    public void runProcess()  
    {
        checkStatus();
        
        MemoryController.writeWord( JVMRuntime.currentthread + ObjectBase.size + ObjectThread.DelayOffset, 
                                   JVMRuntime.value, MemoryVisualizer.INSTANCE );
    }
    
    
}
