/*
 * ObjectSetupProcess.java
 *
 * Created on February 22, 2007, 9:51 PM
 *
 *
 * Initialize the new object.
 *
 * Register Set:
 * Input: CLASSHANDLE - Class of the object
 * Output: VALUE - objectref
 */

package emr.jvm.process.ram;

import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.ram.*; // for the ram
import emr.jvm.visualization.MemoryVisualizer;

/**
 *
 * @author Evan Ross
 */
public class ObjectSetupProcess extends JVMProcess
{
    
    /** Creates a new instance of ObjectSetupProcess */
    public ObjectSetupProcess()
    {
        super("ObjectSetup");
    }
    
    public void runProcess()  
    {
        checkStatus();

        // 1. Initialize the class type of this object ... hmm, what would be the class of array objects?
        MemoryController.writeWord(JVMRuntime.handle + ObjectBase.classReferenceOffset, JVMRuntime.classhandle, MemoryVisualizer.INSTANCE);
        
        checkStatus();

        // 2. Initialize the object's monitor 
        MemoryController.writeWord(JVMRuntime.handle + ObjectBase.monitorOffset, 0xFFFFFFFF, MemoryVisualizer.INSTANCE);
        
        // 3. Move the handle (objectref) into VALUE to put pushed onto the stack
        JVMRuntime.value = JVMRuntime.handle;
    }
    
}
