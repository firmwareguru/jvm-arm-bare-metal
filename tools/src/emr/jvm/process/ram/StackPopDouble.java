/*
 * StackPopDouble.java
 *
 * Implemented on August 9, 2009, 9:26 PM
 *
 * Pop 64-bit value from top of Frame's stack
 *
 * Convention:
 * - high word is in value1, low word in value2
 * - value1 is at lowest address, value2 is at top of stack
 *
 * Register set:
 * Output: VALUE1, VALUE2 - Double value popped from stack
 */

package emr.jvm.process.ram;
import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;

/**
 *
 * @author Evan Ross
 */
public class StackPopDouble extends JVMProcess
{
    
    /** Creates a new instance of StackPopDouble */
    public StackPopDouble()
    {
        super("StackPopDouble");
    }
    
    public void runProcess()
    {
        // ### Pop off value2 (low word)
        checkStatus();

        // 1. decrement stackpointer
        JVMRuntime.stackpointer -= 4;

        checkStatus();

        // 2. pop word from address pointed to by stack pointer to VALUE
        JVMRuntime.value2 = MemoryController.readWord(JVMRuntime.stackpointer);

        // ### Pop off value1 (high word)
        checkStatus();

        // 1. decrement stackpointer
        JVMRuntime.stackpointer -= 4;

        checkStatus();

        // 2. pop word from address pointed to by stack pointer to VALUE
        JVMRuntime.value1 = MemoryController.readWord(JVMRuntime.stackpointer);
    }
    
}
