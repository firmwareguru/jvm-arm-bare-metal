/*
 * EligibleQueueInsert.java
 *
 * Created on April 3, 2007, 6:18 PM
 *
 * Inserts a thread into the eligible queue.  The thread is inserted based on priority.  Lower priority value
 * is placed towards the head of the queue and consequently is considered to be higher priority.
 *
 * This queue is a priority queue, implemented as a linked list.  The linked elements are the
 * Thread objects created in the JVM runtime.  The method Thread.start() adds the current Thread object, 
 * pointed to by the implicit variable "this", to the queue.
 *
 * Register Set:
 *   Input: ELIGIBLEHEAD - points to head of queue
 *          VALUE - points to Thread object to add to queue ('this' popped off stack into value)
 *   Intermediate:
 *          NAME
 *          DESCRIPTOR
 *          VALUE1
 *          VALUE2
 *
 *
 */

package emr.jvm.process.system;
import emr.jvm.process.JVMProcess;
import emr.jvm.Debug;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.ram.*; // for the object stuff
import emr.jvm.process.ProcessManager;
import emr.jvm.visualization.CoreVisualizer;
import emr.jvm.visualization.MemoryVisualizer;

/**
 *
 * @author Evan Ross
 */
public class EligibleQueueInsert extends JVMProcess
{

    /**
     * Creates a new instance of EligibleQueueInsert
     */
    public EligibleQueueInsert()
    {
        super("EligibleQueueInsert");
    }
    
    public void runProcess()  
    {
        checkStatus();
        
        debug("starting");
        
        // Set the next process to run according to the flag INDEX.
        //   INDEX = 0, do nothing
        //   INDEX = 1, set PCW to WaitQueueProcess.  Means that EligibleQueueInsert was
        //         invoked as part of a thread yield() process (scheduler)
        //if( JVMRuntime.index == 1 )
        //    ProcessManager.setPCW( ProcessManager.WAIT_QUEUE_PROCESS );
        
        debug("set PCW");
        
        // Handle special case first -- empty list
        if( JVMRuntime.eligiblehead == JVMRuntime.nullregister )
        {
            debug("Eligible list empty.");
            
            checkStatus();

            // Do this first or else queue painter will crap out!
            // Make sure 'next' is null, newelement.next = null
            MemoryController.writeWord( JVMRuntime.value + ObjectBase.size + ObjectThread.NextOffset, JVMRuntime.nullregister, MemoryVisualizer.INSTANCE );
            
            // Assign Thread as first element of the list
            // head = newelement
            JVMRuntime.eligiblehead = JVMRuntime.value;

            checkStatus();
            
            return;
        }

        // There is at least one element in the list

        checkStatus();
        
        // Copy the queue head to a temp variable - 'before'
        JVMRuntime.name = JVMRuntime.eligiblehead;
        
        checkStatus();

        // Copy the queue head to a temp variable - 'current'
        JVMRuntime.descriptor = JVMRuntime.eligiblehead;
        
        checkStatus();

        // Get the thing we are comparing against -- priority
        JVMRuntime.value1 = MemoryController.readWord( JVMRuntime.value + ObjectBase.size + ObjectThread.PriorityOffset );
        
        
        checkStatus();
        // Keep searching the list while current (descritor) is not null.
        // If it is null, we are inserting into an empty list, or we have
        // traversed a list and this is where the comparison has led us (all
        // other elements greater/less than)
        while( JVMRuntime.descriptor != JVMRuntime.nullregister )
        {
            debug("searching list");
            checkStatus();
            // Each element in the list is a Thread object.
            // Get priority, in objectbase + 0x1
            JVMRuntime.value2 = MemoryController.readWord( JVMRuntime.descriptor + ObjectBase.size + ObjectThread.PriorityOffset );

            checkStatus();
    
            // do compare -- higher priority than this element (lower value)?
            if( JVMRuntime.value1 < JVMRuntime.value2 )
            {
                // Ok this is it
                debug("Inserting into top or middle of eligible list.");
                break;
            }
            
            checkStatus();
            // before = current
            JVMRuntime.name = JVMRuntime.descriptor;
            
            checkStatus();
            // get the next element in the list, current = current.next
            JVMRuntime.descriptor = MemoryController.readWord( JVMRuntime.descriptor + ObjectBase.size + ObjectThread.NextOffset );
            
        }

        checkStatus();
        
        // Another special case - inserting at top of list?
        if( JVMRuntime.descriptor == JVMRuntime.eligiblehead )
        {
            debug("Inserting at top of eligible list");

            checkStatus();
            
            // newelement.next = current
            MemoryController.writeWord( JVMRuntime.value + ObjectBase.size + ObjectThread.NextOffset, JVMRuntime.descriptor, MemoryVisualizer.INSTANCE );
            
            // inserting element at top of list
            // head = newelement
            JVMRuntime.eligiblehead = JVMRuntime.value;
            
        }
        else
        {
            debug("Inserting into middle or end of eligible list");
            checkStatus();
            // otherwise, insert in middle or end of list
            // before.next = newelement
            MemoryController.writeWord( JVMRuntime.name + ObjectBase.size + ObjectThread.NextOffset, JVMRuntime.value, MemoryVisualizer.INSTANCE );
            
            // newelement.next = current
            MemoryController.writeWord( JVMRuntime.value + ObjectBase.size + ObjectThread.NextOffset, JVMRuntime.descriptor, MemoryVisualizer.INSTANCE );
            
        }
    }
    
}
