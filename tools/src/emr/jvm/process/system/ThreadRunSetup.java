/*
 * ThreadRunSetupProcess.java
 *
 * Created on February 12, 2007, 10:18 PM
 *
 * Responsible for setting up a Frame for the run() method of a new Thread.  It copies over arguments to new frame
 * including objectref.  Combines functionality of SetPCProcess.
 *
 * The currentframe is assumed to be the Thread.start() method.  The start() method has no stack.  'this' is in local array [0].
 * The currentframe is not modified!
 *
 * Sets run()'s frame's previousframe to NULLREGISTER.
 * 
 * New Thread has it's context pre-set:
 *    - currentframe is set to handle
 *    - stackpointer in thread is set
 *    - pc in thread is set
 *    - currentclass is set to classhandle
 *
 * Register set:
 *   Input:  handle - address of newly allocated frame
 *           tablehandle - method table entry for the run method.
 *           index - specifies if virtual method and objectref is copied to local array
 */

package emr.jvm.process.system;

import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.ram.*; // for the ram
import emr.jvm.memory.nvm.*; // for the ram
import emr.jvm.visualization.MemoryVisualizer;
/**
 *
 * @author Evan Ross
 */
public class ThreadRunSetup extends JVMProcess
{
    
    /** Creates a new instance of FrameSetupProcess */
    public ThreadRunSetup()
    {
        super("ThreadRunSetup");
    }
    
    public void runProcess()  
    {
        // JVMRuntime.dumpRegisters();
        checkStatus();
        
               
        // set the new frame's "previousframe" pointer to null
         MemoryController.writeWord(JVMRuntime.handle + Frame.PREVIOUS_FRAME_OFFSET, JVMRuntime.nullregister, MemoryVisualizer.FRAME );
        
        // The run argument has no args so we know we only need to pop and copy over 'this'
                
        // pop off the args of the current frame and copy them into the local variable array
        // top of stack goes into end of var array, etc, while objectref goes into local[0]
        // STACKPOINTER points to the old frame's stack at this point
        
         // pop the top arg of the old frame into value - no!  no stack in start().
         //JVMRuntime.stackpointer--;  // not sure if this is actually the correct thing to do.

        checkStatus();


        // Don't need these two when setting up a Thread upon VM startup because there is no Frame
        // and 'this' is already in value register after the NewInstance process.
        
        // Load currentframe into name
        JVMRuntime.name = MemoryController.readWord(JVMRuntime.currentthread + ObjectThread.CurrentframeOffset);
         
        // access local var[0] directly
        JVMRuntime.value = MemoryController.readWord( JVMRuntime.name + Frame.LOCAL_VAR_OFFSET + 0 );

        checkStatus();

        // copy the arg into the new frame
        MemoryController.writeWord(JVMRuntime.handle + Frame.LOCAL_VAR_OFFSET, JVMRuntime.value, MemoryVisualizer.FRAME );

        //==================================================
        //   Thread reference is in VALUE -- don't modify!
        //==================================================
        
        
        // This stuff goes into Thread
        
        //-------------------
        // 1. Stackpointer
        //-------------------
        checkStatus();

        // setup the new stack pointer = base pointer + start of local vars + size of local vars
        JVMRuntime.value1 = JVMRuntime.handle + Frame.LOCAL_VAR_OFFSET + JVMRuntime.value1;
        
        checkStatus();
        
        // Store in new Frame's STACKPOINTER. handle points to new frame.
        //MemoryController.writeWord( JVMRuntime.value + ObjectThread.RegistersOffset + ObjectThread.StackpointerOffset, JVMRuntime.value1, MemoryVisualizer.INSTANCE );
        MemoryController.writeWord( JVMRuntime.handle + Frame.STACK_POINTER_OFFSET, JVMRuntime.value1, MemoryVisualizer.FRAME );
        
        checkStatus();
        
        //-------------------
        // 2. Currentframe
        //-------------------
        
        // Set the Thread's currentframe to the new frame for Run()
        MemoryController.writeWord( JVMRuntime.value + ObjectThread.CurrentframeOffset, JVMRuntime.handle, MemoryVisualizer.INSTANCE );
        
        checkStatus();

        ////////////////////////////////
        // Setup the PC
        ////////////////////////////////
        
        // Get code pointer from MethodTableEntry
        //JVMRuntime.value1 = NVM.get4ByteWord(JVMRuntime.tablehandle + InternalClass.METHODTABLEENTRY_CodePtrOffset);
        JVMRuntime.value1 = MemoryController.readShort(JVMRuntime.tablehandle + InternalClass.METHODTABLEENTRY_CodePtrHalfOffset);
        
        checkStatus();

        // 2. Get code table entry
        JVMRuntime.index = ( JVMRuntime.value1 + JVMRuntime.tablehandle );  // code ptr is tablehandle + code ptr offset

        checkStatus();
        
        // 2.5 Get the start of the code array.  This is the new PC.  For now, store in tableentryhandle
        // Set the PC.  
        JVMRuntime.value2 = JVMRuntime.index + InternalClass.CODETABLEENTRY_CodeOffset;

        checkStatus();
        
        MemoryController.writeWord( JVMRuntime.handle + Frame.PC_OFFSET, JVMRuntime.value2, MemoryVisualizer.FRAME );
        
        
        //-------------------------
        //  Currentclass
        //-------------------------

        checkStatus();
        
        // Set the all important currentclass after any previous TableLookups have found the class containing the method
        // Currentclass was already backed up in the FrameSetup process
        //MemoryController.writeWord( JVMRuntime.value + ObjectThread.RegistersOffset + ObjectThread.CurrentclassOffset, JVMRuntime.classhandle ); // classhandle contains currentclass
        MemoryController.writeWord( JVMRuntime.handle + Frame.CURRENT_CLASS_OFFSET, JVMRuntime.classhandle, MemoryVisualizer.FRAME);
        
        // As a last step, set the index register to 0 so that the following EligibleQueueProcess doesn't
        // request a call to WaitQueueProcess
        JVMRuntime.index = 0;
    }
    
}
