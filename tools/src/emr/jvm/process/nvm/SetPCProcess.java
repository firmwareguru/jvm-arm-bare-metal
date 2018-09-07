/*
 * SetPCProcess.java
 *
 * Created on March 13, 2007, 9:50 PM
 *
 * Get the address in NVM that is the start of the code segment for the method.
 * The address is transfered to the PC register.
 *
 * Note that CLASSHANDLE register is initially set all the way back in ReferenceLookup;
 * it is subsequently modified by TableLookup if the lookup recurses, and it
 * is again modified to point to an instance's class during ObjectClassLookup.
 * 
 *
 * Register set:
 *   Input  : TABLEHANDLE - contains pointer to MethodTableEntry
 *   Output : PC
 *
 */

package emr.jvm.process.nvm;

import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.nvm.*;

/**
 *
 * @author Evan Ross
 */
public class SetPCProcess extends JVMProcess
{
    
    /** Creates a new instance of SetPCProcess */
    public SetPCProcess()
    {
        super("SetPC");
    }
    public void runProcess() 
    {
        checkStatus();

        // 1. Get code pointer offset from MethodTableEntry
        JVMRuntime.value = MemoryController.readShort(JVMRuntime.tablehandle + InternalClass.METHODTABLEENTRY_CodePtrHalfOffset);
        
        checkStatus();

        // 2. Get code table entry
        JVMRuntime.handle = (JVMRuntime.value + JVMRuntime.tablehandle); // code ptr is tablehandle + code ptr offset

        checkStatus();
        
        // 2.5 Get the start of the code array.  This is the new PC.  For now, store in tablehandle
        //JVMRuntime.tablehandle = JVMRuntime.handle + InternalClass.CODETABLEENTRY_CodeOffset;
        JVMRuntime.pc = JVMRuntime.handle + InternalClass.CODETABLEENTRY_CodeOffset;

        // Set the PC.  
        //JVMRuntime.pc = JVMRuntime.tablehandle;  
        
        // Set the all important currentclass after any previous TableLookups have found the class containing the method
        // Currentclass was already backed up in the FrameSetup process
        //JVMRuntime.currentclass = JVMRuntime.classhandle; // classhandle contains currentclass
         
    }
     
}
