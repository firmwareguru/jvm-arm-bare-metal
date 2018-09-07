/*
 * SetupMethodLookupProcess.java
 *
 * Created on March 6, 2007, 9:55 PM
 *
 * To instruct the TableLookupProcess to lookup methods, we set the table bit in the 'index' register.
 */

package emr.jvm.process.nvm;
import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.nvm.*; // for the nvm
/**
 *
 * @author Evan Ross
 */
public class SetupMethodLookupProcess extends JVMProcess
{
    
    /** Creates a new instance of SetupMethodLookupProcess */
    public SetupMethodLookupProcess()
    {
        super("SetupMethodLookup");
    }
    public void runProcess()
    {
        checkStatus();

        // set bit 2: 1=method, 0=field
        //JVMRuntime.index |= 0x2;
        JVMRuntime.PCW |= JVMRuntime.PCW_FLAG_TABLE;
        
    }
     
}
