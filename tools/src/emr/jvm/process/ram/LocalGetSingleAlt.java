/*
 * LocalGetSingleAlt.java
 *
 * Created on April 10, 2007, 8:04 PM
 *
 * This process is the alternate version of LocalGetSingle: it places the local
 * variable into the VALUE2 register instead of VALUE.  This is used for implementing
 * the iinc instruction where VALUE1 contains count.
 *
 */

package emr.jvm.process.ram;

import emr.jvm.process.JVMProcess;
import emr.jvm.Debug;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.nvm.*; // for the nvm
import emr.jvm.memory.ram.*; // for the ram


/**
 *
 * @author Evan Ross
 */
public class LocalGetSingleAlt extends JVMProcess
{
    
    /**
     * Creates a new instance of LocalGetSingleAlt
     */
    public LocalGetSingleAlt()
    {
        super("LocalGetSingleAlt");
    }
    
    public void runProcess()  
    {

        checkStatus();
        
        // Load currentframe into name
        JVMRuntime.name = MemoryController.readWord(JVMRuntime.currentthread + ObjectThread.CurrentframeOffset);
        
        // Get local variable
        // Turn index into units of bytes.
       JVMRuntime.value2 = MemoryController.readWord( JVMRuntime.name + Frame.LOCAL_VAR_OFFSET + (JVMRuntime.index * 4));
        
    }
    
    
}
