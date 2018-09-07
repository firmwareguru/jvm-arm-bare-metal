/*
 * StackPeekSingle.java
 *
 * Created on February 19, 2007, 10:49 PM
 *
 * Reads the value at the top of the stack but does not remove it.  Used for:
 * Dup
 *
 * Register Set:
 * Output: VALUE - value peeked from the stack
 * Internal: STACKPOINTER
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
public class StackPeekSingle extends JVMProcess
{
    
    /** Creates a new instance of StackPeekSingle */
    public StackPeekSingle()
    {
        super("StackPeekSingle");
    }
    
    public void runProcess()  
    {
        checkStatus();

        // 2. peek word from address pointed to by stack pointer -1 to VALUE
        JVMRuntime.value = MemoryController.readWord(JVMRuntime.stackpointer - 4);
        
    }
}
