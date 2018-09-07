/*
 * ArrayPutShort.java
 *
 * Created on July 26, 2009, 9:13 PM
 *
 * ArrayPutShort places the contents of VALUE into the indexed field in an Array of short words.
 * Supports instructions sastore
 *
 * Requires StackPopSingle and ArrayAccessSetup process be run prior in that order.
 *
  * Register set:
 *   Input: VALUE - the value to be placed into the array (from StackPopSingle)
 *          INDEX - popped from stack, index into array   (from ArrayAccessSetup)
 *          HANDLE - popped from stack, the array object reference
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
public class ArrayPutShort extends JVMProcess {

    /** Creates a new instance of ArrayPutShort */
    public ArrayPutShort()
    {
        super("ArrayPutShort");
    }

    @Override
    public void runProcess()
    {

        checkStatus();

        // Put value into arrayref + (object fields + arraylength) + index
	    // Index is in units of array elements (in this case shorts) so multiply by 2
        // Note that the int value popped off the stack should be truncated to a short.
        // this is automatically handled by the memory controllers explicit (short) conversion.
        MemoryController.writeShort(JVMRuntime.handle + Array.ArrayBaseSize + (JVMRuntime.index * 2), JVMRuntime.value, MemoryVisualizer.INSTANCE);

    }

}
