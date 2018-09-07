/*
 * CompareSingleIntStackPopTwo.java
 *
 * Created on April 9, 2007, 9:26 PM
 *
 * Prepares for a branch compare by popping value1 and value2 off of the stack
 */

package emr.jvm.process.computation;

import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;


/**
 *
 * @author Evan Ross
 */
public class CompareSingleIntStackPopTwo extends JVMProcess
{
    
    /** Creates a new instance of CompareSingleIntStackPopTwo */
    public CompareSingleIntStackPopTwo()
    {
        super("CompareSingleIntStackPopTwo");
    }
    
    public void runProcess() 
    {
        checkStatus();
        
        JVMRuntime.stackpointer -= 4;
        
        // Pop value2 first
        JVMRuntime.value2 = MemoryController.readWord(JVMRuntime.stackpointer);

        JVMRuntime.stackpointer -= 4;
        
        // Pop value1 
        JVMRuntime.value1 = MemoryController.readWord(JVMRuntime.stackpointer);
    }
    
    
}
