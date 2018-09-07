/*
 * StackPopObjectref.java
 *
 * Created on March 24, 2007, 1:28 PM
 *
 * This is a special, slightly modifed version of StackPopSingle.  This process moves the single word
 * popped from the stack into the HANDLE register rather than the VALUE register.  This is needed to avoid
 * colliding with Field instruction processes Object(Get/Put)Field and FieldStack(Pop/Push) since these use
 * the VALUE register.
 *
 * Register set:
 * Output: HANDLE - objectref popped from stack
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
public class StackPopObjectref extends JVMProcess
{
    
    /** Creates a new instance of StackPopObjectref */
    public StackPopObjectref()
    {
        super("StackPopObjectref");
    }

    @Override
    public void runProcess()  
    {
        checkStatus();

        // 1. decrement stackpointer
        JVMRuntime.stackpointer -= 4;

        checkStatus();
        
        // 2. pop word from address pointed to by stack pointer to VALUE
        JVMRuntime.handle = MemoryController.readWord(JVMRuntime.stackpointer);
        
    }    
    
}
