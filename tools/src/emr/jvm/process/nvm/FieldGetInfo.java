/*
 * FieldGetInfo.java
 *
 * Created on March 24, 2007, 1:54 PM
 *
 * Retrieves the size and index of a field referenced by TABLEHANDLE register.
 * 
 * Register Set:
 *    Input:  TABLEHANDLE - pointer to a FieldTableEntry
 *    Output: INDEX - field index value
 *            CPHANDLE - field size
 *
 */

package emr.jvm.process.nvm;

import emr.jvm.process.JVMProcess;
import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.nvm.*; // for the nvm

/**
 *
 * @author Evan Ross
 */
public class FieldGetInfo extends JVMProcess
{
    
    /** Creates a new instance of FieldGetInfo */
    public FieldGetInfo()
    {
        super("FieldGetInfo");
    }
    
    public void runProcess() 
    {
        checkStatus();
        
        // Get the field index from TableEntryHandle which was found during TableLookupProcess
        JVMRuntime.index = MemoryController.readWord( JVMRuntime.tablehandle + InternalClass.FIELDTABLEENTRY_FieldSizeIndexOffset );
        
        checkStatus();
        
        // Get the field size from TableEntryHandle which was found during TableLookupProcess
        JVMRuntime.cphandle = JVMRuntime.index;

        checkStatus();

        // Mask off the field size bits
        JVMRuntime.cphandle = ( JVMRuntime.index >> 16 ) & 0xffff ;

        checkStatus();
        
        // Mask off the field index bits
        JVMRuntime.index &= 0xffff; // lower 2 bytes are field index
        
    }
    
}
