/*
 * FrameSetupProcess.java
 *
 * Created on February 12, 2007, 10:18 PM
 *
 * Responsible for backing up registers to old frame and copying arguments over to new frame
 * including objectref if a virtual method is used ('index' == 1)
 *
 * Also sets CURRENTCLASS to be the class of the new frame, which was set in CLASSHANDLE during
 * the TableLookup process.  Assign CLASSHANDLE to CURRENTCLASS after backing up CURRENTCLASS
 * into the old frame.
 *
 * Register set:
 *   Input:  handle - address of newly allocated frame
 *           PCW - specifies if virtual method and objectref is copied to local array
 *           classhandle - pointer to InternalClass of new frame.
 *           value2 - number of args to copy over in units of words.
 *           value1 - number of locals from Allocate memory
 */

package emr.jvm.process.ram;

import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.ram.*; // for the ram
import emr.jvm.visualization.MemoryVisualizer;
/**
 *
 * @author Evan Ross
 */
public class FrameSetupProcess extends JVMProcess
{
    
    /** Creates a new instance of FrameSetupProcess */
    public FrameSetupProcess()
    {
        super("FrameSetupProcess");
    }
    
    @Override
    public void runProcess()  
    {
        // Convert value2 into units of bytes.
        JVMRuntime.value2 <<= 2;
        
        // JVMRuntime.dumpRegisters();
        checkStatus();
        
        // Load currentframe into name
        // Load currentclass into descriptor
        JVMRuntime.name = MemoryController.readWord(JVMRuntime.currentthread + ObjectThread.CurrentframeOffset);
        JVMRuntime.descriptor = MemoryController.readWord(JVMRuntime.name + Frame.CURRENT_CLASS_OFFSET);
 
        // 1. Store CURRENTFRAME register into new frame's "previous frame" pointer
        MemoryController.writeWord(JVMRuntime.handle + Frame.PREVIOUS_FRAME_OFFSET, JVMRuntime.name, MemoryVisualizer.FRAME);
        
        checkStatus();
        
        // 2. Store PC register into old frame's "pc" word
        //MemoryController.writeWord(JVMRuntime.handle + Frame.PREVIOUS_PC_OFFSET, JVMRuntime.pc);
        MemoryController.writeWord(JVMRuntime.name + Frame.PC_OFFSET, JVMRuntime.pc, MemoryVisualizer.FRAME);
        

        checkStatus();
        
        // 3. Store new current class (stored in classhandle) in new frame.
        MemoryController.writeWord(JVMRuntime.handle + Frame.CURRENT_CLASS_OFFSET, JVMRuntime.classhandle, MemoryVisualizer.FRAME);


        checkStatus();

        // 4. Store the STACKPOINTER - nope -> gotta run it down first.
        //MemoryController.writeWord(JVMRuntime.handle + Frame.STACK_POINTER_OFFSET, JVMRuntime.stackpointer);        
        
        // 6. Copy the args (count in VALUE2) only if there was a previous frame (i.e., a stack)
        if( JVMRuntime.name != 0xffffffff )
        {
            // !! Add index (=1) to value2 so that argcount + objectref is popped off the stack
            //    Note that since objectref is the last value popped off the stack, it is in VALUE
            //    for the ObjectClassLookupProcess
            //if( (JVMRuntime.index & 0x1) == 0x1 ) // 1st bit, 1=virtual, 0=static
            if ((JVMRuntime.PCW & JVMRuntime.PCW_FLAG_LOOKUP) == JVMRuntime.PCW_FLAG_LOOKUP)
            
                JVMRuntime.value2 += 4; // (add 1 word (4 bytes) for objectref)
                
            // pop off the args of the current frame and copy them into the local variable array
            // top of stack goes into end of var array, etc, while objectref goes into local[0]
            // STACKPOINTER points to the old frame's stack at this point
            while( JVMRuntime.value2 > 0 )
            {
                // count down by word
                JVMRuntime.value2 -= 4;
        
                checkStatus();
                // pop the top arg of the old frame into value
                JVMRuntime.stackpointer -= 4;
                JVMRuntime.value = MemoryController.readWord( JVMRuntime.stackpointer );

                checkStatus();
                
                // copy the arg into the new frame
                JVMRuntime.index = JVMRuntime.value2 + JVMRuntime.handle;
                MemoryController.writeWord(JVMRuntime.index + Frame.LOCAL_VAR_OFFSET, JVMRuntime.value, MemoryVisualizer.FRAME );

                checkStatus();

            
            }

            
            /////////////////////////////////////
            // What to do with the stack pointer!!!!!?????
            
            checkStatus();
            
            // now save the old stack pointer to the previous frame 
            MemoryController.writeWord(JVMRuntime.name + Frame.STACK_POINTER_OFFSET, JVMRuntime.stackpointer, MemoryVisualizer.FRAME);
            
        }


        checkStatus();
        
        // setup the new stack pointer = base pointer + start of local vars + size of local vars
        JVMRuntime.stackpointer = JVMRuntime.handle + Frame.LOCAL_VAR_OFFSET + JVMRuntime.value1;

        checkStatus();
        
        // and finally set the currentframe to the new frame pointer in 'handle'
        MemoryController.writeWord(JVMRuntime.currentthread + ObjectThread.CurrentframeOffset, JVMRuntime.handle, MemoryVisualizer.INSTANCE);
        
        //JVMRuntime.dumpRegisters();
    }
    
}
