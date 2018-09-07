/*
 * EligibleQueueUnlink.java
 *
 * Created on April 15, 2007, 6:09 PM
 *
 * Unlink the Thread object in the Eligible Queue linked list.  For a singly linked list, this
 * requires traversing the list from the head until the currentthread element is found, 
 * while tracking the 'before' element (yes we already know currentthread, but we need to find
 * the thread, if any, in the list before it).
 *
 * For a doubly linked list, this is simply:
 *    currentthread.previous.next = currentthread.next;
 *    currentthread.next.previous = currentthread.previous;
 *
 * A Doubly linked list would be a faster and simpler implementation, however this is at the expense
 * of increased memory usage (the 'previous' variable in each Thread).
 *
 * In moderate to small thread systems the currentthread will almost always be at the top of
 * the queue anyways, so a doubly linked list doesn't offer so great an advantage.
 *
 * There are two cases to consider: currentthread is at the head of the queue or
 *  currentthread is in the middle or end.
 *
 * Register Set:
 *   Input: currentthread - this is always the thread we are unlinking
 *   Intermediate:
 *          eligiblehead
 *          name
 *          descriptor
 */

package emr.jvm.process.system;

import emr.jvm.process.JVMProcess;
import emr.jvm.Debug;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.ram.*; // for the ram
import emr.jvm.visualization.MemoryVisualizer;


/**
 *
 * @author Evan Ross
 */
public class EligibleQueueUnlink extends JVMProcess
{
    
    /** Creates a new instance of EligibleQueueUnlink */
    public EligibleQueueUnlink()
    {
        super("EligibleQueueUnlink");
    }
    
    public void runProcess()  
    {
        checkStatus();
        
        // Handle case 1: Currentthread is at the head of the queue (most likely case)
        // unlink: eligiblehead = currentthread.next
        if( JVMRuntime.eligiblehead == JVMRuntime.currentthread )
        {
            JVMRuntime.eligiblehead = MemoryController.readWord( JVMRuntime.currentthread + ObjectBase.size + ObjectThread.NextOffset );
            
            Debug.message("unlinking at head.");
            
            // that's it!
            return;
        }
        
        // JVMRuntime.name = 'before'
        // JVMRuntime.descriptor = 'current'
        JVMRuntime.name = JVMRuntime.eligiblehead;
        JVMRuntime.descriptor = JVMRuntime.eligiblehead;

        checkStatus();
        
        // traverse the list to find currentthread
        while( JVMRuntime.descriptor != JVMRuntime.currentthread )
        {
            // before = current
            JVMRuntime.name = JVMRuntime.descriptor;

             checkStatus();
             
            // current = current.next
            JVMRuntime.descriptor = MemoryController.readWord( JVMRuntime.descriptor + ObjectBase.size + ObjectThread.NextOffset );
        }
        
        // now unlink
        // before.next = current.next

        checkStatus();
        
        // get current.next
        JVMRuntime.descriptor = MemoryController.readWord( JVMRuntime.descriptor + ObjectBase.size + ObjectThread.NextOffset );

        checkStatus();
        
        // set before.next
        MemoryController.writeWord( JVMRuntime.name + ObjectBase.size + ObjectThread.NextOffset, JVMRuntime.descriptor, MemoryVisualizer.INSTANCE );
        
    }
    
    
}
