/*
 * LocalGetSingle.java
 *
 * Created on February 20, 2007, 6:21 PM
 *
 * Get a single word from the local variable array at the specified index
 *
 * Register Set:
 * Input: INDEX
 * Output: VALUE
 */

package emr.jvm.process.ram;

import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.ram.*; // for the ram
/**
 *
 * @author Evan Ross
 */
public class LocalGetSingle extends JVMProcess
{
    
    /** Creates a new instance of LocalGetSingle */
    public LocalGetSingle()
    {
        super("LocalGetSingle");
    }
    
    public void runProcess()  
    {
        checkStatus();
        

        // Load currentframe into name
        JVMRuntime.name = MemoryController.readWord(JVMRuntime.currentthread + ObjectThread.CurrentframeOffset);
        
        // Turn index into units of bytes.
        JVMRuntime.value = MemoryController.readWord( JVMRuntime.name + Frame.LOCAL_VAR_OFFSET + (JVMRuntime.index * 4) );
    }
    
}
