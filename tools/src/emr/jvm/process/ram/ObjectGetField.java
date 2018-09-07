/*
 * ObjectGetField.java
 *
 * Created on February 28, 2007, 9:15 PM
 *
 * Uses the "field index" from FieldGetInfo process to find where 
 * in the object to get the field.  CPHANDLE determines whether to do a double get or a single get.
 * 
 * Register Set:
 *   Input: HANDLE - objectref popped from stack (from StackPopObjecref)
 *          INDEX  - index into object for the field
 *          CPHANDLE - size of the field
 *          TABLEENTRYHANDLE - size of this field, index into object for this field
 *   Output: case CPHANDLE = 1:
 *              VALUE - value of single field
 *           case CPHANDLE = 2:
 *              VALUE1 - value of first half of double field
 *              VALUE2 - value of second half of double field
 */

package emr.jvm.process.ram;
import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.ram.*; // for the ram
import emr.jvm.memory.nvm.*; // for the nvm

/**
 *
 * @author Evan Ross
 */
public class ObjectGetField extends JVMProcess
{
    
    /**
     * Creates a new instance of ObjectGetField
     */
    public ObjectGetField()
    {
        super("ObjectGetField");
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
            JVMRuntime.value = MemoryController.readWord( JVMRuntime.handle );
        }
        else if ( JVMRuntime.cphandle == 2 ) // Double
        {
            JVMRuntime.value1 = MemoryController.readWord( JVMRuntime.handle );
            
            checkStatus();
            
            JVMRuntime.handle += 4;
            
            checkStatus();
            
            JVMRuntime.value2 = MemoryController.readWord( JVMRuntime.handle );
        }
        else  // for debugging
        {
            throwException(INVALID_FIELD_SIZE);
        }
         
        
        
    }
    
}
