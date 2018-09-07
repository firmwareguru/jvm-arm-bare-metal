/*
 * PrepareALUSingleProcess.java
 *
 * Created on March 29, 2007, 8:39 PM
 *
 * PrepareALUSingle:  pop stack into VALUE2 and pop stack again into VALUE1
 *
 *      value2 = frame.popInt();
 *      value1 = frame.popInt();
 */

package emr.jvm.process.computation;
import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.ram.*; // for the nvm
/**
 *
 * @author Evan Ross
 */
public class PrepareALUSingleProcess extends JVMProcess
{
    
    /** Creates a new instance of PrepareALUSingleProcess */
    public PrepareALUSingleProcess()
    {
        super("PrepareALUSingle");
    }

    public void runProcess()  
    {
        checkStatus();
        
        // Stackpointer points to next available spot, so decrement to point to the first value
        JVMRuntime.stackpointer -= 4;
    
        checkStatus();
        
        // Get the first operand
        JVMRuntime.value2 = MemoryController.readWord( JVMRuntime.stackpointer );
        
        checkStatus();

        JVMRuntime.stackpointer -= 4;
        
        checkStatus();

        // Get the second operand
        JVMRuntime.value1 = MemoryController.readWord( JVMRuntime.stackpointer );
        
    }
}
