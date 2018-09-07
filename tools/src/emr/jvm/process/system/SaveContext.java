/*
 * SaveContext.java
 *
 * Created on May 18, 2007, 8:15 PM
 *
 * This SaveContext process is intended to be used as part of the Interrupt mechanism.  This process performs part of the same function as
 * saving the context in EligibleQueueProcess.  However it is repeated here because invoking an interrupt method requires the existing context to be 
 * immediately saved and not bother with the restoring performed in EligibleQueueProcess.
 *
 */

package emr.jvm.process.system;

import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.ram.*; // for the nvm

/**
 *
 * @author Evan Ross
 */
public class SaveContext extends JVMProcess {
    
    /** Creates a new instance of SaveContext */
    public SaveContext() 
    {
        super("SaveContext");
    }
    
    public void runProcess()  
    {
        //checkStatus();
        
        throwException(NOT_IMPLEMENTED);
        
            // Save registers to currentthread only if it is non-null.  It could be null if a context-less frame died.
            if( JVMRuntime.currentthread != JVMRuntime.nullregister )
            {
                //RAM.bank1.putInstanceWord( JVMRuntime.currentthread + ObjectThread.PCOffset,
                //                           JVMRuntime.pc );
                //RAM.bank1.putInstanceWord( JVMRuntime.currentthread + ObjectThread.CurrentframeOffset,
                //                           JVMRuntime.currentframe );
                //RAM.bank1.putInstanceWord( JVMRuntime.currentthread + ObjectThread.StackpointerOffset,
                //                           JVMRuntime.stackpointer );
                //RAM.bank1.putInstanceWord( JVMRuntime.currentthread + ObjectThread.CurrentclassOffset,
                //                           JVMRuntime.currentclass );
            }
            
        
    }
    
}
