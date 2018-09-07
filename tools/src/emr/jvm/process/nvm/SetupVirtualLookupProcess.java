/*
 * SetupVirtualLookupProcess.java
 *
 * Created on March 6, 2007, 9:55 PM
 *
 * A Virtual lookup means that fields or methods are searched recursively through super classes.
 *
 * Register set:
 *    Input: NONE
 *    Output: INDEX = 1 (virtual)
 */

package emr.jvm.process.nvm;
import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.nvm.*; // for the nvm
/**
 *
 * @author Evan Ross
 */
public class SetupVirtualLookupProcess extends JVMProcess
{
    
    /** Creates a new instance of SetupVirtualLookupProcess */
    public SetupVirtualLookupProcess()
    {
        super("SetupVirtualLookup");
    }
    public void runProcess()
    {
        checkStatus();
        
        // set bit 1: 1=virtual, 0=static
        //JVMRuntime.index |= 0x1;
        JVMRuntime.PCW |= JVMRuntime.PCW_FLAG_LOOKUP;
    }
     
}
