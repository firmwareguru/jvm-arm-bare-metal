/*
 * FrameTeardownProcess.java
 *
 * Created on February 15, 2007, 9:07 PM
 *
 * FrameTeardown.  Do opposite of FrameSetup.  Restore registers.
 *
 * Registers:
 *    Input:
 *    Output: handle, for deallocate memory
 *
 * Protected (do not use): VALUE, VALUE1 or VALUE2, for non-void returns.
 */

package emr.jvm.process.ram;

import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.ram.*; // for the ram
import emr.jvm.process.ProcessManager;
import emr.jvm.visualization.MemoryVisualizer;
/**
 *
 * @author Evan Ross
 */
public class FrameTeardownProcess extends JVMProcess
{
    
    /** Creates a new instance of FrameTeardownProcess */
    public FrameTeardownProcess()
    {
        super("FrameTeardown");
    }
    
    public void runProcess()  
    {
        //setIdle();
        //return;
        
        checkStatus();
        
        // Load currentframe into handle
        //JVMRuntime.name = MemoryController.readWord(JVMRuntime.currentthread + ObjectThread.CurrentframeOffset);
        JVMRuntime.handle = MemoryController.readWord(JVMRuntime.currentthread + ObjectThread.CurrentframeOffset);
 
        // Move CURRENTFRAME into HANDLE so that DeallocateMemory has something to work with
        //JVMRuntime.handle = JVMRuntime.name;
              
        checkStatus();


        // Restore CURRENTFRAME register from old frame's "previous frame" pointer
        //JVMRuntime.name = MemoryController.readWord(JVMRuntime.name + Frame.PREVIOUS_FRAME_OFFSET);
        JVMRuntime.name = MemoryController.readWord(JVMRuntime.handle + Frame.PREVIOUS_FRAME_OFFSET);

        // Put a check here:
        // If name is -1, there are no more frames to pop!  ThreadDeath.
       // *** We must stop here if the restored currentframe is null.  This means that the current thread
        //     must terminate.  To do this we send the ProcessManager into a new state via the PCW.
        if( JVMRuntime.name == 0xffffffff )
        {
            debug("Currentframe is null.  CurrentThread must terminate.");

            //step();

            checkStatus();

            if( JVMRuntime.waitinghead == JVMRuntime.nullregister &&
                JVMRuntime.eligiblehead == JVMRuntime.nullregister )
            {
                // nothing to run.
                debug("   nothing to run.  terminating VM");
                System.exit(1);
            }
            else
            {
                debug("   Invoking Scheduler.");

                // Note that the thread must be unlinked from the eligible queue before running
                // the scheduler or else it crashes.
                ProcessManager.setPCW( ProcessManager.INVOKE_SCHEDULER );

                // Disable Thread termination.
                throwException(NOT_IMPLEMENTED);
            }
        }



        // 2. Restore PC register from old frame's "old pc" register
        JVMRuntime.pc = MemoryController.readWord(JVMRuntime.name + Frame.PC_OFFSET );
        
        // 4. Restore the STACKPOINTER from the now restored frame.
        JVMRuntime.stackpointer = MemoryController.readWord(JVMRuntime.name + Frame.STACK_POINTER_OFFSET );        

        checkStatus();

        // Store currentframe
        MemoryController.writeWord(JVMRuntime.currentthread + ObjectThread.CurrentframeOffset, JVMRuntime.name, MemoryVisualizer.INSTANCE);
        
        // Store currentclass
        //MemoryController.writeWord(JVMRuntime.name + Frame.CURRENT_CLASS_OFFSET, JVMRuntime.descriptor, MemoryVisualizer.INSTANCE);
        
        
        checkStatus();

        
         //debug("end teardown");
        //JVMRuntime.dumpRegisters();
            
    }
    
}
