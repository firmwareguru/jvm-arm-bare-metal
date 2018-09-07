/*
 * FrameInfoLookupProcess.java
 *
 * Created on February 7, 2007, 9:07 PM
 *
 * The FrameInfoLookupProcess process is responsible for obtaining the frame information from the InternalClass'
 * CodeTableEntry:
 *    max stack, max locals => obtains frame size
 * and from 
 * MethodTableEntry:
 *    arg count => sets number of args to copy in FrameSetup process.
 *
 * Register set:
 * Input from TableLookup (method):  
 *        TABLEHANDLE (points to MethodTableEntry)
 * Output: VALUE - total size of frame
 *         VALUE1 - max locals
 *         VALUE2 - arg count
 *
 */

package emr.jvm.process.ram;

import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.ram.*; // for the ram
import emr.jvm.memory.nvm.*; // for the nvm

import emr.jvm.visualization.MemoryVisualizer;


/**
 *
 * @author Evan Ross
 */
public class FrameInfoLookupProcess extends JVMProcess
{
    
    /**
     * Creates a new instance of FrameInfoLookupProcess
     */
    public FrameInfoLookupProcess()
    {
        super("FrameInfoLookup");
    }
    
    public void runProcess()  
    {
        checkStatus();

        // 1. Get code pointer offset from MethodTableEntry
        //JVMRuntime.value = MemoryController.readWord(JVMRuntime.tablehandle + InternalClass.METHODTABLEENTRY_CodePtrOffset);
        JVMRuntime.handle = MemoryController.readShort(JVMRuntime.tablehandle + InternalClass.METHODTABLEENTRY_CodePtrHalfOffset);
        
        // Note: 'handle' is the code pointer, offset from the tablehandle register.
        
        
        // 2. Get code table entry
        // JVMRuntime.handle = ( JVMRuntime.value       ) & 0xffff;  // code ptr is in lower 2 bytes
                
        checkStatus();
        // 3. handle plus tablehandle points to the max stack and max locals.
        //    max_stack is in the upper 2 bytes, locals lower 2
        // This line approximates an LDR value2, [tablehandle, handle] instruction.
        JVMRuntime.value2 = MemoryController.readWord( JVMRuntime.tablehandle + JVMRuntime.handle );
        
        checkStatus();
        
        JVMRuntime.value =  ( JVMRuntime.value2 >> 16 ) & 0xffff;  // max_stack
        JVMRuntime.value *= 4; // Get in terms of bytes
        
        checkStatus();
        
        JVMRuntime.value1 = ( JVMRuntime.value2       ) & 0xffff;  // max_locals
        JVMRuntime.value1 *= 4; // Get in terms of bytes
        
        checkStatus();

        // 4. Add them to get the total size and place in VALUE
        JVMRuntime.value = Frame.LOCAL_VAR_OFFSET + JVMRuntime.value + JVMRuntime.value1;  // total size

        checkStatus();
        
        //
        // Get arg count from MethodTableEntry.
        // Place in value2
        //
        JVMRuntime.value2 = MemoryController.readShort( JVMRuntime.tablehandle + InternalClass.METHODTABLEENTRY_ArgCountHalfOffset );
        //JVMRuntime.value2 &= 0xffff;

        // Outputs:
        //   value  - total size   -- used by AllocateMemory process
        //   value1 - max locals   -- used by FrameSetup process  <<< must be preserved across AllocateMemory
        //   value2 - arg count    -- used by FrameSetup process  <<< must be preserved across AllocateMemory
        
        // Set the color that subsequent memory should be initialized to
        MemoryPool.initializationColor = MemoryVisualizer.FRAME;
        
    }
    
}
