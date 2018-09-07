/*
 * ArrayLengthGetProcess.java
 *
 * Created on April 1, 2007, 1:39 PM
 *
 * Obtain the ArrayLength field from Array objects.
 * 
 * Register Set:
 *  Input: VALUE - Array reference 'arrayref' popped from opstack
 *  Output: VALUE - Array length 'length' to be pushed onto opstack
 *
 *
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
public class ArrayLengthGetProcess extends JVMProcess
{
    
    /** Creates a new instance of ArrayLengthGetProcess */
    public ArrayLengthGetProcess()
    {
        super("ArrayLengthGet");
    }
    
    
    public void runProcess()
    {
        checkStatus();
        
        // Get array length field from array object.
        JVMRuntime.value = MemoryController.readWord( JVMRuntime.value + Array.ArrayLengthOffset );
    }
   
}
