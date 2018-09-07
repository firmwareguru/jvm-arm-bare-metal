/*
 * InterruptVectorRetrieve.java
 *
 * Created on May 24, 2007, 9:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.jvm.process.system;
import emr.jvm.process.JVMProcess;
import emr.jvm.process.ProcessManager;
import emr.jvm.Debug;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.nvm.*; // for the nvm
import emr.jvm.memory.ram.*; // for the ram

/**
 * The InterruptVectorRetrieve process has two responsibilities:
 *    1.  Place objectref of the Thread pointed to in the interrupt vector table
 *        by bits 3-6 in PCW in the register VALUE
 *    2.  Set register INDEX = 0 so that EligibleQueueInsert does not 'call' EligibleQueueWait
 *
 *
 * @author Evan Ross
 */
public class InterruptVectorRetrieve extends JVMProcess
{
    
    /** Creates a new instance of InterruptVectorRetrieve */
    public InterruptVectorRetrieve() 
    {
        super("InterruptVectorRetrieve");
    }
    
    public void runProcess()  
    {
        checkStatus();
        
        // Place the output of the priority encoder into the index register
        JVMRuntime.index = (JVMRuntime.PCW >> 8);

        checkStatus();
        
        // The interrupt vector table is at the start of RAM.  Get the value at the given location
        JVMRuntime.value = MemoryController.readWord(JVMRuntime.index);

        checkStatus();
        
        // reset index
        JVMRuntime.index = 0;
        
        // Note!  If an interrupt gets through for which there is no handler setup (Thread reference in the vector table)
        //        there will be hell to pay!  The InterruptManager.mask register must be set such that no unused interrupts get past.
        
    }
     
    
}
