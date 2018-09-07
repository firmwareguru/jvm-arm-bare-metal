/*
 * ArrayPutByte.java
 *
 * Created on July 25, 2009, 9:19 PM
 *
 * ArrayPutByte places the contents of VALUE into the indexed field in an Array of byte words.
 * Supports instructions bastore
 *
 * Requires StackPopSingle and ArrayAccessSetup process be run prior in that order.
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
public class ArrayPutByte extends JVMProcess {

    /** Creates a new instance of ArrayPutByte */
    public ArrayPutByte()
    {
        super("ArrayPutByte");
    }

    @Override
    public void runProcess()
    {

        checkStatus();

        // Put value into arrayref + (object fields + arraylength) + index
	    // Index is in units of array elements (in this case bytes) so multiply by 1
        // Note that the int value popped off the stack should be truncated to a byte...
        // this is automatically handled by the memory controllers explicit (byte) conversion.
        MemoryController.writeByte(JVMRuntime.handle + Array.ArrayBaseSize + (JVMRuntime.index * 1), JVMRuntime.value, MemoryVisualizer.INSTANCE);

    }
}
