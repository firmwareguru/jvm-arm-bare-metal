/*
 * ObjectSizeLookupProcess.java
 *
 * Created on February 13, 2007, 10:02 PM
 *
 * Register set:
 * Input: CLASSHANDLE - reference to the class template for the new object
 * Output: VALUE - the total amount of memory to allocate to the new object
 * 
 */

package emr.jvm.process.nvm;


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
public class ObjectSizeLookupProcess extends JVMProcess
{
    
    /**
     * Creates a new instance of ObjectSizeLookupProcess
     */
    public ObjectSizeLookupProcess()
    {
        super("ObjectSizeLookup");
    }
    
    @Override
    public void runProcess() 
    {
        checkStatus();
        // 1. Get the object size out of the header and place into VALUE
        JVMRuntime.value = MemoryController.readWord(JVMRuntime.classhandle + InternalClass.HEADER_ObjectSizeOffset);
        
        checkStatus();
        // 2. Lower 2 bytes is the object size.  upper 2 are the access flags.
        // size is given as words, must turn into bytes.
        JVMRuntime.value &= 0xffff;
        JVMRuntime.value *= 4;
        
        checkStatus();
        // 3. Add to this the size of the ObjectBase (monitors, classreference and the like)
        JVMRuntime.value = JVMRuntime.value + ObjectBase.size;
        
        // Set the color that subsequent memory should be initialized to
        MemoryPool.initializationColor = MemoryVisualizer.OBJECT;
        
    }
    
}
