/*
 * WaitQueueInsert.java
 *
 * Created on April 3, 2007, 6:18 PM
 *
 * Inserts a thread into the wait queue.  The thread is inserted based on delay.  The system timer is
 * a count down timer hence higher delay values are placed towards the head of the queue.  The delay
 * value is computed at the time of sleep invocation as current time - requested delay (in ticks).
 *
 * This queue is a priority queue, implemented as a linked list.  The linked elements are the
 * Thread objects created in the JVM runtime.  The method Thread.sleep(int delay) adds the current Thread object, 
 * pointed to by currentthread, to the queue.
 *
 * Register Set:
 *   Input: WAITHEAD - points to head of queue
 *          CURRENTTHREAD - points to Thread object to add to queue.
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
import emr.jvm.visualization.MemoryVisualizer;
/**
 *
 * @author Evan Ross
 */
public class WaitQueueInsert extends JVMProcess
{

    /**
     * Creates a new instance of EligibleQueueInsert
     */
    public WaitQueueInsert()
    {
        super("WaitQueueInsert");
    }
    
    public void runProcess()  
    {
        checkStatus();
        
        // Handle special case first -- empty list
        if( JVMRuntime.waitinghead == JVMRuntime.nullregister )
        {
            //Debug.message("Wait list empty.");
            checkStatus();
            // Make sure 'next' is null, newelement.next = null
            MemoryController.writeWord( JVMRuntime.currentthread + ObjectBase.size + ObjectThread.NextOffset, JVMRuntime.nullregister, MemoryVisualizer.INSTANCE );

            // Assign Thread as first element of the list
            // head = newelement
            JVMRuntime.waitinghead = JVMRuntime.currentthread;

            checkStatus();
            
            return;
        }

        // There is at least one element in the list

        checkStatus();
        
        // Copy the queue head to a temp variable - 'before'
        JVMRuntime.name = JVMRuntime.waitinghead;
        
        checkStatus();

        // Copy the queue head to a temp variable - 'current'
        JVMRuntime.descriptor = JVMRuntime.waitinghead;
        
        checkStatus();

        // Get the thing we are comparing against -- delay
        JVMRuntime.value1 = MemoryController.readWord( JVMRuntime.currentthread + ObjectBase.size + ObjectThread.DelayOffset );
        
        checkStatus();
        // Keep searching the list while current (descritor) is not null.
        // If it is null, we are inserting into an empty list, or we have
        // traversed a list and this is where the comparison has led us (all
        // other elements greater than)
        while( JVMRuntime.descriptor != JVMRuntime.nullregister )
        {
            checkStatus();
            // Each element in the list is a Thread object.
            // Get delay, in objectbase + 0x1
            JVMRuntime.value2 = MemoryController.readWord( JVMRuntime.descriptor + ObjectBase.size + ObjectThread.DelayOffset );

            checkStatus();
    
            // do compare -- is the new thread's delay less than the current thread in the list?
            if( JVMRuntime.value1 > JVMRuntime.value2 )
            {
                // Ok this is it
                //Debug.message("Inserting into top or middle of wait list.");
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
        if( JVMRuntime.descriptor == JVMRuntime.waitinghead )
        {
            //Debug.message("Inserting at top of wait list");
            
            checkStatus();
            // inserting element at top of list
            // head = newelement
            JVMRuntime.waitinghead = JVMRuntime.currentthread;

            // newelement.next = current
            MemoryController.writeWord( JVMRuntime.currentthread + ObjectBase.size + ObjectThread.NextOffset, JVMRuntime.descriptor, MemoryVisualizer.INSTANCE );
            
            checkStatus();
        }
        else
        {
            //Debug.message("Inserting into middle or end of wait list");
            checkStatus();
            // otherwise, insert in middle or end of list
            // before.next = newelement
            MemoryController.writeWord( JVMRuntime.name + ObjectBase.size + ObjectThread.NextOffset, JVMRuntime.currentthread, MemoryVisualizer.INSTANCE );
            
            // newelement.next = current
            MemoryController.writeWord( JVMRuntime.currentthread + ObjectBase.size + ObjectThread.NextOffset, JVMRuntime.descriptor, MemoryVisualizer.INSTANCE );
            
            checkStatus();
            
        }
    }
    
}
