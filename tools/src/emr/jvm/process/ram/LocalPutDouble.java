/*
 * LocalPutDouble.java
 *
 * Implemented on August 7, 2009, 9:55 PM
 *
 * Register Set:
 * Input: INDEX
 * Output: VALUE1, VALUE2
 *
 * Convention:
 * - high word is in value1, low word in value2
 * - value1 is at lowest address
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
public class LocalPutDouble extends JVMProcess
{
    
    /** Creates a new instance of LocalPutDouble */
    public LocalPutDouble()
    {
        super("LocalPutDouble");
    }

    public void runProcess()
    {
        checkStatus();

        // Load currentframe into name
        JVMRuntime.name = MemoryController.readWord(JVMRuntime.currentthread + ObjectThread.CurrentframeOffset);

        // Turn index into units of bytes and add to name register
        JVMRuntime.name += (JVMRuntime.index * 4);

        // Store value at base slot
        MemoryController.writeWord( JVMRuntime.name + Frame.LOCAL_VAR_OFFSET, JVMRuntime.value1, MemoryVisualizer.FRAME );

        // Store value2 at next higher slot (+4 bytes)
        MemoryController.writeWord( JVMRuntime.name + Frame.LOCAL_VAR_OFFSET + 4, JVMRuntime.value2, MemoryVisualizer.FRAME );

    }
    
}
