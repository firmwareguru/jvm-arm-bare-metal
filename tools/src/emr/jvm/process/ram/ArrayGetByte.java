/*
 * ArrayGetByte.java
 *
 * Created on July 25, 2009, 8:36 PM
 *
 * ArrayGetByte obtains the contents of the indexed field in an Array of bytes.
 * Supports instructions baload.
 *
 * Requires ArrayAccessSetup process be run prior.
 *
 * Register set:
 *   Input: INDEX - popped from stack, index into array
 *          HANDLE - popped from stack, the array object reference (arrayref)
 *   Output: VALUE - the result of the array get operation.  In this case,
 *                   the byte is sign extended to fit a single.
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
public class ArrayGetByte  extends JVMProcess {

    /** Creates a new instance of ArrayGetByte */
    public ArrayGetByte()
    {
        super("ArrayGetByte");
    }

    @Override
    public void runProcess()
    {
        checkStatus();


        // Get the array field, arrayref + (object fields + arraylength) + index
	    // Index is in units of array elements (in this case byte) so no need to multiply
        // Sign extention is required for byte types.  Boolean not so, but I don't think it
        // harms to sign extend a boolean (0 is false either way).
        // In this implementation, sign extention is implicitly performed as the byte is retreived from
        // the memory's ByteBuffer and converted to an int type. (bytes are automatically sign extended to ints).
        JVMRuntime.value = MemoryController.readByte(JVMRuntime.handle + Array.ArrayBaseSize + (JVMRuntime.index * 1));

    }
}
