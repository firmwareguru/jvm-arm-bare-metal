/*
 * FieldStackPop.java
 *
 * Created on February 28, 2007, 9:39 PM
 *
 * FieldStackPop differs from regular stack pop processes.  This process may pop a single or a double depending
 * on what type of field it is.  The field type (single or double) is determined after the field is found.
 *
 * Register Set:
 *   Input:  CPHANDLE - size of this field, index into object for this field
 *           STACKPOINTER
 *   Output: case CPHANDLE = 1: (size == 1)
 *              VALUE - value of single field
 *           case CPHANDLE = 2:
 *              VALUE1 - value of first half of double field
 *              VALUE2 - value of second half of double field

 */

package emr.jvm.process.ram;
import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;

/**
 *
 * @author Evan Ross
 */
public class FieldStackPop extends JVMProcess
{
    
    /** Creates a new instance of FieldStackPop */
    public FieldStackPop()  
    {
        super("FieldStackPop");
    }
    
    public void runProcess() 
    {
        checkStatus();
        
        JVMRuntime.stackpointer -= 4;
        
        if( JVMRuntime.cphandle == 1 )
        {
            JVMRuntime.value = MemoryController.readWord( JVMRuntime.stackpointer );
        }
        else if ( JVMRuntime.cphandle == 2 )
        {
            JVMRuntime.value1 = MemoryController.readWord( JVMRuntime.stackpointer );

            JVMRuntime.stackpointer -= 4;
            
            JVMRuntime.value2 = MemoryController.readWord( JVMRuntime.stackpointer );
             
        }
        else  // for debugging
        {
            throwException(INVALID_FIELD_SIZE);
        }
    }
    
}
