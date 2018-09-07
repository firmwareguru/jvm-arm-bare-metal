/*
 * ObjectPutField.java
 *
 * Created on February 28, 2007, 9:06 PM
 *
 * Uses the count of words to find where in the object to place the field.  Value1 determines whether to
 * do a double put or a single put.
 * 
 * Register Set:
 *   Input:  VALUE1 - size of this field.  1= single, 2=double
 *           case VALUE1=1:
 *              VALUE - value of single field
 *           case VALUE1=2:
 *              VALUE1 - value of first half of double field
 *              VALUE2 - value of second half of double field
 *          HANDLE - reference to object
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
public class ObjectPutField extends JVMProcess
{
    
    /**
     * Creates a new instance of ObjectPutField
     */
    public ObjectPutField()
    {
        super("ObjectPutField");
    }
    
    public void runProcess()  
    {
        checkStatus();
        
        // Get start address of field = handle + size of object base + field index 
        JVMRuntime.handle +=  ObjectBase.size + (JVMRuntime.index * 4);
        
        checkStatus();

        // Now get the field and place depending on the size (1 or 2 words)
        if( JVMRuntime.cphandle == 1 )  // Single
        {
            MemoryController.writeWord( JVMRuntime.handle, JVMRuntime.value, MemoryVisualizer.INSTANCE );
        }
        else if ( JVMRuntime.cphandle == 2 ) // Double
        {
            MemoryController.writeWord( JVMRuntime.handle, JVMRuntime.value1, MemoryVisualizer.INSTANCE );
            
            checkStatus();
            
            JVMRuntime.handle += 4;
            
            checkStatus();
            
            MemoryController.writeWord( JVMRuntime.handle, JVMRuntime.value2, MemoryVisualizer.INSTANCE );
        }
        else  // for debugging
        {
            throwException(INVALID_FIELD_SIZE);
        }
          
    }
    
}
