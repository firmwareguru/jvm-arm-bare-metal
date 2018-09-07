/*
 * ArrayGetShort.java
 *
 * Created on July 26, 2009, 9:11 PM
 *
 * ArrayGetShort obtains the contents of the indexed field in an Array of shorts.
 * Supports instructions saload.
 *
 * Requires ArrayAccessSetup process be run prior.
 *
 * Register set:
 *   Input: INDEX - popped from stack, index into array
 *          HANDLE - popped from stack, the array object reference (arrayref)
 *   Output: VALUE - the result of the array get operation.  In this case,
 *                   the short is sign extended to fit a single.
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
public class ArrayGetShort extends JVMProcess {

    /** Creates a new instance of ArrayGetShort */
    public ArrayGetShort()
    {
        super("ArrayGetShort");
    }

    @Override
    public void runProcess()
    {
        checkStatus();


        // Get the array field, arrayref + (object fields + arraylength) + index
	    // Index is in units of array elements (in this case short) so multiply by 2
        // Sign extention is required for short types.
        // In this implementation, sign extention is implicitly performed as the short is retreived from
        // the memory's ByteBuffer and converted to an int type. (shorts are automatically sign extended to ints).
        JVMRuntime.value = MemoryController.readShort(JVMRuntime.handle + Array.ArrayBaseSize + (JVMRuntime.index * 2));

    }
}
