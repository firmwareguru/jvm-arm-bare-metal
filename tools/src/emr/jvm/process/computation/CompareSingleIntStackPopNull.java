/*
 * CompareSingleIntStackPopNull.java
 *
 * Created on April 9, 2007, 9:31 PM
 *
 * Prepare for branch comparison against null. 
 * Null in this implementation is represented as -1 or 0xffffffff
 */

package emr.jvm.process.computation;

import emr.jvm.process.JVMProcess;
import emr.jvm.Debug;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.nvm.*; // for the nvm
import emr.jvm.memory.ram.*; // for the ram


/**
 *
 * @author Evan Ross
 */
public class CompareSingleIntStackPopNull extends JVMProcess
{
    
    /** Creates a new instance of CompareSingleIntStackPopNull */
    public CompareSingleIntStackPopNull()
    {
        super("CompareSingleIntStackPopNull");
    }
    
    public void runProcess()  
    {
        checkStatus();
        JVMRuntime.stackpointer -= 4;
        
        // Pop value1
        JVMRuntime.value1 = MemoryController.readWord(JVMRuntime.stackpointer);

        // Fix value2 at 0
        JVMRuntime.value2 = JVMRuntime.nullregister;  // -1
            
    }
    
    
}
