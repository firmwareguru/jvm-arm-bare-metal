/*
 * EligibleQueueProcess.java
 *
 * Created on April 7, 2007, 6:58 PM
 *
 * This is part II of the Scheduler.  EligibleQueueProcess performs the context switching if necessary.
 * If currentthread is not the thread at the top of the eligible queue, then there is a higher priority 
 * thread ready to run:
 *     1. Save registers to currentthread
 *     2. Restore registers from eligiblehead
 *     3. Set currentthread = eligiblehead
 */

package emr.jvm.process.system;
import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.ram.*; // for the object stuff
import emr.jvm.visualization.MemoryVisualizer;
/**
 *
 * @author Evan Ross
 */
public class EligibleQueueProcess extends JVMProcess
{   
    /**
     * Creates a new instance of EligibleQueueProcess
     */
    public EligibleQueueProcess()
    {
        super("EligibleQueueProcess");
        
    }

    public void runProcess()  
    {
        checkStatus();
        
        
        if( JVMRuntime.currentthread == JVMRuntime.eligiblehead )
        {
            // nothing to do, currentthread is highest priority thread ready to run
            return;
        }
        else
        {
            // Load the Thread's 'currentframe' pointer into a local register and use that to reference the current frame
            // store load and store the context:
            //      currentclass <- this is not context since this IS where this value is maintained.
            //  *   pc           <- this IS context (an actual register that needs backing up)
            //  *   stackpointer <- this IS context (an actual register that needs backing up)
            
                        
            // Save registers to currentthread only if it is non-null.  It could be null if a context-less frame died.
            if( JVMRuntime.currentthread != JVMRuntime.nullregister )
            {
                // name = currentframe
                JVMRuntime.name = MemoryController.readWord(JVMRuntime.currentthread + ObjectThread.CurrentframeOffset);
                
                MemoryController.writeWord(JVMRuntime.name + Frame.PC_OFFSET, JVMRuntime.pc, MemoryVisualizer.FRAME);
                MemoryController.writeWord(JVMRuntime.name + Frame.STACK_POINTER_OFFSET, JVMRuntime.stackpointer, MemoryVisualizer.FRAME);
                
                
                //RAM.bank1.putInstanceWord( JVMRuntime.currentthread + ObjectThread.RegistersOffset + ObjectThread.PCOffset,
                //                           JVMRuntime.pc );
                //RAM.bank1.putInstanceWord( JVMRuntime.currentthread + ObjectThread.RegistersOffset + ObjectThread.StackpointerOffset,
                //                           JVMRuntime.stackpointer );
                //RAM.bank1.putInstanceWord( JVMRuntime.currentthread + ObjectThread.RegistersOffset + ObjectThread.CurrentframeOffset,
                //                           JVMRuntime.currentframe );
                //RAM.bank1.putInstanceWord( JVMRuntime.currentthread + ObjectThread.RegistersOffset + ObjectThread.CurrentclassOffset,
                //                           JVMRuntime.currentclass );
            }
            
            // What if eligiblehead is null?  What if some idiot sleeps the only thread?  or the idle thread?
            // We should issue an error!
            if (JVMRuntime.eligiblehead == JVMRuntime.nullregister)
                throwException(ELIGIBLE_QUEUE_EMPTY);
            
            checkStatus();
            
            JVMRuntime.name = MemoryController.readWord(JVMRuntime.eligiblehead + ObjectThread.CurrentframeOffset);
            
            // Restore registers from eligiblehead
            //JVMRuntime.pc               = MemoryController.readWord( JVMRuntime.eligiblehead + ObjectThread.RegistersOffset + ObjectThread.PCOffset );
            //JVMRuntime.currentframe     = MemoryController.readWord( JVMRuntime.eligiblehead + ObjectThread.RegistersOffset + ObjectThread.CurrentframeOffset );
            //JVMRuntime.stackpointer     = MemoryController.readWord( JVMRuntime.eligiblehead + ObjectThread.RegistersOffset + ObjectThread.StackpointerOffset );
            //JVMRuntime.currentclass     = MemoryController.readWord( JVMRuntime.eligiblehead + ObjectThread.RegistersOffset + ObjectThread.CurrentclassOffset );
            JVMRuntime.stackpointer       = MemoryController.readWord(JVMRuntime.name + Frame.STACK_POINTER_OFFSET);
            JVMRuntime.pc                 = MemoryController.readWord(JVMRuntime.name + Frame.PC_OFFSET);
            
            checkStatus();
            
            // Set currentthread = eligiblehead
            JVMRuntime.currentthread = JVMRuntime.eligiblehead;
        }
    }
}
