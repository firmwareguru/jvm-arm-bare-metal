/*
 * ArrayGetSingle.java
 *
 * Created on June 8, 2007, 8:42 PM
 *
 * ArrayGetSingle obtains the contents of the indexed field in an Array of Single words.
 * Supports instructions aaload, iaload, faload
 *
 * Requires ArrayAccessSetup process be run prior.
 *
 *
 * Register set:
 *   Input: INDEX - popped from stack, index into array
 *          HANDLE - popped from stack, the array object reference (arrayref)
 *   Output: VALUE - the result of the array get operation.
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
public class ArrayGetSingle extends JVMProcess
{
    
    /** Creates a new instance of ArrayGetSingle */
    public ArrayGetSingle() 
    {
        super("ArrayGetSingle");
    }
    
    @Override
    public void runProcess() 
    {
        checkStatus();
        
        
        // Get the array field, arrayref + (object fields + arraylength) + index
	    // Index is in units of array elements (in this case words) so multiply by 4
        JVMRuntime.value = MemoryController.readWord(JVMRuntime.handle + Array.ArrayBaseSize + (JVMRuntime.index * 4));        
        
    }
    
}
