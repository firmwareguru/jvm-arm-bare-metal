/*
 * StackPeekDouble.java
 *
 * Implemented on August 7, 2009, 9:55 PM
 *
 * Reads the 2 words of a 64-bit value at the top of the stack
 * but does not remove it.  Used for: Dup2
 *
 * Convention:
 * - high word is in value1, low word in value2
 * - value1 is at lowest address, value2 is at top of stack
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
public class StackPeekDouble extends JVMProcess
{
    
    /** Creates a new instance of StackPeekDouble */
    public StackPeekDouble()
    {
        super("StackPeekDouble");
    }
    
    public void runProcess()
    {
        checkStatus();

        //peek low word from address pointed to by stack pointer -4 to VALUE2
        JVMRuntime.value2 = MemoryController.readWord(JVMRuntime.stackpointer - 4);
        
        //peek high word from address pointed to by stack pointer -8 to VALUE1
        JVMRuntime.value1 = MemoryController.readWord(JVMRuntime.stackpointer - 8);
        
    }
    
}
