/*
 * StackPopSingle.java
 *
 * Created on February 15, 2007, 9:20 PM
 *
 * Pop value from top of Frame's stack
 *
 * Register set:
 * Output: VALUE - value popped from stack
 *
 */

package emr.jvm.process.ram;
import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
/**
 *
 * @author Evan Ross
 */
public class StackPopSingle extends JVMProcess
{
    
    /** Creates a new instance of StackPopSingle */
    public StackPopSingle()
    {
        super("StackPopSingle");
    }
    
    public void runProcess()  
    {
        checkStatus();

        // 1. decrement stackpointer
        JVMRuntime.stackpointer -= 4;

        checkStatus();
        
        // 2. pop word from address pointed to by stack pointer to VALUE
        JVMRuntime.value = MemoryController.readWord(JVMRuntime.stackpointer);
        
    }    
}
