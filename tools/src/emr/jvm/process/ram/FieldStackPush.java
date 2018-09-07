/*
 * FieldStackPush.java
 *
 * Created on March 14, 2007, 11:07 PM
 *
 * A FieldStackPush differs from a regular stack push in that it may push a Single or a Double
 * depending on the value of a register (CPHANDLE) set in FieldGetInfo process.
 *
 * Register Set:
 *   Input: STACKPOINTER
 *           case CPHANDLE = 1:
 *              VALUE - value of single field
 *           case CPHANDLE = 2:
 *              VALUE1 - value of first half of double field
 *              VALUE2 - value of second half of double field
 */

package emr.jvm.process.ram;
import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.visualization.MemoryVisualizer;

/**
 *
 * @author Evan Ross
 */
public class FieldStackPush extends JVMProcess
{
    
    /** Creates a new instance of FieldStackPush */
    public FieldStackPush()
    {
        super("FieldStackPush");
    }
    
    public void runProcess()  
    {
        checkStatus();
        
        // StackPointer is pointing to where the next word will go...
        
        // Check if we are pushing a single or a double
        if( JVMRuntime.cphandle == 1 )  // Single
        {
            MemoryController.writeWord( JVMRuntime.stackpointer, JVMRuntime.value, MemoryVisualizer.FRAME );
        }
        else if ( JVMRuntime.cphandle == 2 )  // Double
        {
            MemoryController.writeWord( JVMRuntime.stackpointer, JVMRuntime.value1, MemoryVisualizer.FRAME );
            
            JVMRuntime.stackpointer += 4;
            
            MemoryController.writeWord( JVMRuntime.stackpointer, JVMRuntime.value2, MemoryVisualizer.FRAME );
            
        }
        else  // for debugging
        {
            throwException(INVALID_FIELD_SIZE);
        }
        
        
        // don't forget this
        JVMRuntime.stackpointer += 4;
        
    }
    
}
