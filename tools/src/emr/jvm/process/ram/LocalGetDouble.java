/*
 * LocalGetDouble.java
 *
 * Implemented on August 7, 2009, 9:55 PM
 *
 * Places VALUE1,VALUE2 into local variable array referenced by INDEX
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

/**
 *
 * @author Evan Ross
 */
public class LocalGetDouble extends JVMProcess
{
    
    /** Creates a new instance of LocalGetDouble */
    public LocalGetDouble()
    {
        super("LocalGetDouble");
    }
    
    public void runProcess()
    {
        checkStatus();

        // Load currentframe into name
        JVMRuntime.name = MemoryController.readWord(JVMRuntime.currentthread + ObjectThread.CurrentframeOffset);

        // Turn index into units of bytes
        // Load value1 from slot at lowest index.
        JVMRuntime.value1 = MemoryController.readWord( JVMRuntime.name + Frame.LOCAL_VAR_OFFSET + (JVMRuntime.index * 4) );
        
        // Turn index into units of bytes.
        // Load value2 from next higher 32-bit slot (+4 bytes)
        JVMRuntime.value2 = MemoryController.readWord( JVMRuntime.name + Frame.LOCAL_VAR_OFFSET + (JVMRuntime.index * 4) + 4);
    }
    
}
