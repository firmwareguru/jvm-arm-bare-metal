/*
 * InterruptVectorSet.java
 *
 * Created on May 24, 2007, 9:35 PM
 *
 * 
 * Requirements:
 *    This process is executed as part of the INTERRUPT_START process which is in turn initiated from a method with these characteristics:
 *       1. no stack
 *       2. local var[0] = 'this' or objectref of Thread 
 *          local var[1] = int type_ which is index into interrupt vector table to place objectref
 *
 *  This method is:
 *     public static native void Interrupt.setInterruptHandler(Thread thread_, int type_);
 *
 *  Register Set:
 *     Input: VALUE - the objectref of the Thread to place into the vector table
 *
 */

package emr.jvm.process.system;
import emr.jvm.process.JVMProcess;
import emr.jvm.process.ProcessManager;
import emr.jvm.Debug;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.nvm.*; // for the nvm
import emr.jvm.memory.ram.*; // for the ram
import emr.jvm.visualization.MemoryVisualizer;

/**
 * The InterruptVectorSet process has one responsibility:
 *    1.  Place objectref of the Thread pointed to in the VALUE register into the vector
 *        table indexed by the INDEX register
 *
 *
 * @author Evan Ross
 */
public class InterruptVectorSet extends JVMProcess
{
    
    /** Creates a new instance of InterruptVectorSet */
    public InterruptVectorSet() 
    {
        super("InterruptVectorSet");
    }
    
    public void runProcess()  
    {
        checkStatus();

         // Load currentframe into name
        JVMRuntime.name = MemoryController.readWord(JVMRuntime.currentthread + ObjectThread.CurrentframeOffset);

        // Get the second argument (local variable array index 1) of the setInterruptHandler() method - 'type_'.  
        // This value is actually the index into the vector table.
        JVMRuntime.index = MemoryController.readWord( JVMRuntime.name + Frame.LOCAL_VAR_OFFSET + 4 );

        checkStatus();
        
        // The interrupt vector table is at the start of RAM.  Put the value into the given location
        MemoryController.writeWord(JVMRuntime.index, JVMRuntime.value, MemoryVisualizer.INTERRUPT_VECTOR);
        
        
    }
     
    
}
