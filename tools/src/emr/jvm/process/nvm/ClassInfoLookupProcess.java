/*
 * ClassInfoLookupProcess.java
 *
 * Created on February 18, 2007, 5:51 PM
 *
 * The ClassInfoLookup process is quite simple - it returns the class address reference by a ClassInfo 
 * object in the constant pool.
 *
 * This process is used by instructions: New
 *
 * Currently, this is as 2 step process : ClassInfo -> Absolute NVM index of class.
 *
 * Step 1:  use INDEX to get the INDEX in the ClassInfo item.
 * Step 2:  use INDEX to get the CLASSHANDLE
 *
 * Register set:
 * Input: INDEX - from opcode operand
 * Input: CURRENTCLASS - the currentclass containing the constant pool
 * Intermediate: NAME, DESCRIPTOR, CPHANDLE
 * Output: CLASSHANDLE
 *
 * 
 *
 */

package emr.jvm.process.nvm;

import emr.jvm.memory.ram.Frame;
import emr.jvm.memory.ram.ObjectThread;
import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.nvm.*; // for the nvm

/**
 *
 * @author Evan Ross
 */
public class ClassInfoLookupProcess extends JVMProcess
{
    
    /** Creates a new instance of ClassInfoLookupProcess */
    public ClassInfoLookupProcess()
    {
        super("ClassInfoLookup");
    }
    
    public void runProcess()  
    {
        checkStatus();        
        
        // 1. Get the cphandle from the currentclass
        
        // Load currentframe into name
        // Load currentclass into descriptor
        JVMRuntime.name = MemoryController.readWord(JVMRuntime.currentthread + ObjectThread.CurrentframeOffset);
        JVMRuntime.descriptor = MemoryController.readWord(JVMRuntime.name + Frame.CURRENT_CLASS_OFFSET);
        
        JVMRuntime.cphandle = MemoryController.readWord(JVMRuntime.descriptor + InternalClass.HEADER_CPTableOffset);

        checkStatus();

        // 'index' must be converted to byte addresses
        JVMRuntime.index <<= 2;
        
        // 2. Get the index from the ClassInfo
        JVMRuntime.index = MemoryController.readWord( JVMRuntime.cphandle + JVMRuntime.index );
        JVMRuntime.index <<= 2;
        
        checkStatus();

        // 3. Get the classhandle from the index
        JVMRuntime.classhandle = MemoryController.readWord( JVMRuntime.cphandle + JVMRuntime.index );
        
        //JVMRuntime.dumpRegisters();
    }
           
    
}
