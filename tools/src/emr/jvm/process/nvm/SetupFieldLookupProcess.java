/*
 * SetupFieldLookupProcess.java
 *
 * Created on March 6, 2007, 9:55 PM
 *
 * To instruct the TableLookupProcess to lookup fields, we simply set the tablehandle register to
 * point to the FieldTable.  FieldTableEntries are the same as MethodTableEntries.
 */

package emr.jvm.process.nvm;
import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.nvm.*; // for the nvm
/**
 *
 * @author Evan Ross
 */
public class SetupFieldLookupProcess extends JVMProcess
{
    
    /** Creates a new instance of SetupFieldLookupProcess */
    public SetupFieldLookupProcess()
    {
        super("SetupFieldLookup");
    }
    
    public void runProcess() 
    {
        checkStatus();

        // clear bit 2: 0=field
        //JVMRuntime.index &= ~0x2;
        JVMRuntime.PCW &= ~JVMRuntime.PCW_FLAG_TABLE;
    }
}
