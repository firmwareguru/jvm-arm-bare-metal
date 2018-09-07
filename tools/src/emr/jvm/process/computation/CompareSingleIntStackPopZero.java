/*
 * CompareSingleIntStackPopZero.java
 *
 * Created on April 9, 2007, 9:29 PM
 *
 * Prepare for branch comparison against 0.
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
public class CompareSingleIntStackPopZero extends JVMProcess
{
    
    /** Creates a new instance of CompareSingleIntStackPopZero */
    public CompareSingleIntStackPopZero()
    {
        super("CompareSingleIntStackPopZero");
    }
    
    public void runProcess()  
    {
        checkStatus();

        JVMRuntime.stackpointer -= 4;
        
        // Pop value1
        JVMRuntime.value1 = MemoryController.readWord(JVMRuntime.stackpointer);

        // Fix value2 at 0
        JVMRuntime.value2 = 0;
        
    }
    
    
}
