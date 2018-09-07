/*
 * ArrayGetChar.java
 *
 * Created on July 26, 2009, 8:49 PM
 *
 * ArrayGetChar obtains the contents of the indexed field in an Array of bytes.
 * Supports instructions caload.
 *
 * Requires ArrayAccessSetup process be run prior.
 *
 * Register set:
 *   Input: INDEX - popped from stack, index into array
 *          HANDLE - popped from stack, the array object reference (arrayref)
 *   Output: VALUE - the result of the array get operation.  In this case,
 *                   the byte is zero extended to fit a single.
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
public class ArrayGetChar extends JVMProcess {

    /** Creates a new instance of ArrayGetChar */
    public ArrayGetChar()
    {
        super("ArrayGetChar");
    }

    @Override
    public void runProcess()
    {
        checkStatus();


        // Get the array field, arrayref + (object fields + arraylength) + index
	    // Index is in units of array elements (in this case byte) so no need to multiply
        // Zero extention is required for char types.

        JVMRuntime.value = MemoryController.readByte(JVMRuntime.handle + Array.ArrayBaseSize + (JVMRuntime.index * 1));
        JVMRuntime.value &= 0xff; // truncate and zero extend.

    }
}
