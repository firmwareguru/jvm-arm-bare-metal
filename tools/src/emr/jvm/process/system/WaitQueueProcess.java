/*
 * WaitQueueProcess.java
 *
 * Created on April 10, 2007, 9:31 PM
 *
 * Processing the wait queue is performed as follows.
 *    Wait queue is iterated through starting at the head.
 *    Thread elements whose wakeuptime is greater than current system timer count 
 *    are removed from the wait queue and added to the eligible queue.
 *    When a wait queue thread is found with a wakeuptime less than system timer count
 *    the processing stops because wait queue elements are ordered according to
 *    increasing wakeuptime (decreasing time).
 *
 *    The costly activity is EligibleQueueInsert, whose time to insert is linearly 
 *    dependent on number of items already in the queue.
 */

package emr.jvm.process.system;

import emr.jvm.process.JVMProcess;
import emr.jvm.Debug;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.ram.*; // for the ram

/**
 *
 * @author Evan Ross
 */
public class WaitQueueProcess extends JVMProcess
{
    
    /** Creates a new instance of WaitQueueProcess */
    public WaitQueueProcess()
    {
        super("WaitQueueProcess");
    }
    
    public void runProcess()  
    {
        checkStatus();
        
        
        // no sleeping threads to wake up.  run scheduler (EligibleQueueProcess)
        if( JVMRuntime.waitinghead == JVMRuntime.nullregister )
        {
            //ProcessManager.setPCW( ProcessManager.ELIGIBLE_QUEUE_PROCESS );
            JVMRuntime.index = 0; // 'return' 0 to indicate that nothing was unlinked.
            return;
        }
        
        checkStatus();
        
        // examine 'wakeuptime' at element at head of queue.
        JVMRuntime.value = MemoryController.readWord( JVMRuntime.waitinghead + ObjectBase.size + ObjectThread.DelayOffset );

        checkStatus();
        
        // system timer counts down.
        // if system timer is greater than wakeuptime, sleep time has yet to expire.
        if( JVMRuntime.systemtimer > JVMRuntime.value )
        {
            //ProcessManager.setPCW( ProcessManager.ELIGIBLE_QUEUE_PROCESS );
            JVMRuntime.index = 0; // 'return' 0 to indicate that nothing was unlinked.
            return;
        }

        checkStatus();
        
        // *** Set a flag that EligibleQueueInsert process uses to decide whether to call
        //     WaitingQueueProcess again or not
        JVMRuntime.index = 1;

        checkStatus();

        // Value is used by EligibleQueueInsert
        JVMRuntime.value = JVMRuntime.waitinghead;

        checkStatus();
        
        // Unlink this element
        JVMRuntime.waitinghead = MemoryController.readWord( JVMRuntime.waitinghead + ObjectBase.size + ObjectThread.NextOffset );
        
        checkStatus();

        // Insert it into EligibleQueue
        //ProcessManager.setPCW( ProcessManager.ELIGIBLE_QUEUE_INSERT );
        
        
    }
    
    
}
