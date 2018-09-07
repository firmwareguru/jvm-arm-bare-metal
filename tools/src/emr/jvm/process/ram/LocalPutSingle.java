/*
 * LocalPutSingle.java
 *
 * Created on February 20, 2007, 6:20 PM
 *
 * Places VALUE into local variable array referenced by INDEX
 */

package emr.jvm.process.ram;

import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.ram.*; // for the ram
import emr.jvm.visualization.MemoryVisualizer;
/**
 *
 * @author Evan Ross
 */
public class LocalPutSingle extends JVMProcess
{
    
    /** Creates a new instance of LocalPutSingle */
    public LocalPutSingle()
    {
        super("LocalPutSingle");
    }

    public void runProcess()  
    {
        checkStatus();
        
        // Load currentframe into name
        JVMRuntime.name = MemoryController.readWord(JVMRuntime.currentthread + ObjectThread.CurrentframeOffset);
        
        // Turn index into units of bytes and add to name register
        JVMRuntime.name += (JVMRuntime.index * 4);
         
        MemoryController.writeWord( JVMRuntime.name + Frame.LOCAL_VAR_OFFSET, JVMRuntime.value, MemoryVisualizer.FRAME );
        
        
    }
    
}
