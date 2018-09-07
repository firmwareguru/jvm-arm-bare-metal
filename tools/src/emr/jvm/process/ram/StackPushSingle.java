/*
 * StackPushSingle.java
 *
 * Created on February 13, 2007, 9:46 PM
 *
 * Push a single word onto the opstack of the currentframe.  Stack grows up.  Stack pointer is already pointing 
 * to where the new word will go.
 *
 * Register set:
 * Inputs:  stackpointer
 *          value - word to push
 *
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
public class StackPushSingle extends JVMProcess
{
    
    /** Creates a new instance of StackPushSingle */
    public StackPushSingle()
    {
        super("PushStackSingle");
    }
    
    @Override
    public void runProcess()  
    {

        checkStatus();
        
        // 1. push word to address pointed to by stack pointer
        MemoryController.writeWord(JVMRuntime.stackpointer, JVMRuntime.value, MemoryVisualizer.FRAME);
        
        checkStatus();

        // 2. increment stackpointer
        JVMRuntime.stackpointer += 4;

    }
    
}
