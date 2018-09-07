/*
 * ArrayPutSingle.java
 *
 * Created on June 8, 2007, 8:42 PM
 *
 * ArrayPutSingle places the contents of VALUE into the indexed field in an Array of Single words.
 * Supports instructions aastore, iastore, fastore
 *
 * Requires StackPopSingle and ArrayAccessSetup process be run prior in that order.
 *
 *
 * Register set:
 *   Input: VALUE - the value to be placed into the array (from StackPopSingle)
 *          INDEX - popped from stack, index into array   (from ArrayAccessSetup)
 *          HANDLE - popped from stack, the array object reference
 *         
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
public class ArrayPutSingle extends JVMProcess
{
    
    /** Creates a new instance of ArrayPutSingle */
    public ArrayPutSingle() 
    {
        super("ArrayPutSingle");
    }
    
    @Override
    public void runProcess() 
    {
        
        checkStatus();
 
        // Put value into arrayref + (object fields + arraylength) + index
	    // Index is in units of array elements (in this case words) so multiply by 4
        MemoryController.writeWord(JVMRuntime.handle + Array.ArrayBaseSize + (JVMRuntime.index * 4), JVMRuntime.value, MemoryVisualizer.INSTANCE);
        
    }
    
}
