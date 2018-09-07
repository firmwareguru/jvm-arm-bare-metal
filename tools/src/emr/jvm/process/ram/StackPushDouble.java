/*
 * StackPushDouble.java
 *
 * Implemented on August 9, 2009, 9:45 PM
 *
 * Push 64-bit value to top of Frame's stack.
 * Stack pointer is already pointing to where the new word will go.
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
import emr.jvm.visualization.MemoryVisualizer;

/**
 *
 * @author Evan Ross
 */
public class StackPushDouble extends JVMProcess
{
    
    /** Creates a new instance of StackPushDouble */
    public StackPushDouble()
    {
        super("PushStackDouble");
    }
    
    @Override
    public void runProcess()
    {
        // ### Push on value1 (high word)
        checkStatus();

        // 1. push word to address pointed to by stack pointer
        MemoryController.writeWord(JVMRuntime.stackpointer, JVMRuntime.value1, MemoryVisualizer.FRAME);

        checkStatus();

        // 2. increment stackpointer
        JVMRuntime.stackpointer += 4;

        // ### Push on value2 (low word)
        checkStatus();

        // 1. push word to address pointed to by stack pointer
        MemoryController.writeWord(JVMRuntime.stackpointer, JVMRuntime.value2, MemoryVisualizer.FRAME);

        checkStatus();

        // 2. increment stackpointer
        JVMRuntime.stackpointer += 4;
    }
    
}
