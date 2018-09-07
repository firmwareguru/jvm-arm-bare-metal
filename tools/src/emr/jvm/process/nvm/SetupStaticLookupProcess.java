/*
 * SetupStaticLookupProcess.java
 *
 * Created on March 6, 2007, 9:55 PM
 *
 * The purpose of this process is to set a status register such that a subsequent call to
 * EntryLookupProcess will not proceed recursively.  This is for initial field lookups to
 * determine the number of words to pop off/push onto the stack, as well as for initial
 * 
 */

package emr.jvm.process.nvm;
import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.nvm.*; // for the nvm
/**
 *
 * @author Evan Ross
 */
public class SetupStaticLookupProcess extends JVMProcess
{
    
    /** Creates a new instance of SetupStaticLookupProcess */
    public SetupStaticLookupProcess()
    {
        super("SetupStaticLookup");
    }
    public void runProcess()
    {
        checkStatus();

        // clear bit 1: 1=virtual, 0=static
        //JVMRuntime.index &= ~0x1;
        JVMRuntime.PCW &= ~JVMRuntime.PCW_FLAG_LOOKUP;
    
    }
     
}
