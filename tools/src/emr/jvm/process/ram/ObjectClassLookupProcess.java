/*
 * ObjectClassLookupProcess.java
 *
 * Created on February 4, 2007, 7:17 PM
 *
 * The ObjectClassLookupProcess process fetches the ClassReference from the object pointed to 
 * by a handle.  This process eventually sets the currentclass to the class of the object
 * rather than the class of the type (for virtual lookups).
 *
 * To get access to objectref, which is on the stack, the number of arguments that
 * are piled on top of it must be known.  This value is obtained from the MethodTableEntry
 * that has been identified in a preceeding TableLookup process, i.e., TABLEHANDLE register
 * points to this entry.  The arg count field is located in word 1, low 2 bytes.
 *
 * Register set:
 * Input: VALUE - points to an object (objectref popped from the stack in previous process)
 *        TABLEHANDLE - points to methodtableentry
 * Output: CLASSHANDLE - points to the InternalClass that is the type of the object
 *
 * Intermediate: VALUE1 - store stackpointer pointing to objectref
 */

package emr.jvm.process.ram;

import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.ram.*; // for the ram
import emr.jvm.memory.nvm.*; // for the ram

/**
 *
 * @author Evan Ross
 */
public class ObjectClassLookupProcess extends JVMProcess
{
    
    /**
     * Creates a new instance of ObjectClassLookupProcess
     */
    public ObjectClassLookupProcess()
    {
        super("ObjectClassLookup");
    }
    
   
    public void runProcess()  
    {
        checkStatus();
        
        //
        // Get count of arguments into VALUE (from NVM)
        //
        JVMRuntime.value = MemoryController.readShort( JVMRuntime.tablehandle + InternalClass.METHODTABLEENTRY_ArgCountHalfOffset );
        //JVMRuntime.value &= 0xffff;       
        
        
        checkStatus();
        
        //
        // Decrement the stackpointer by the count of arguments
	// ('value' in in units of words not bytes so left shift 2)
        //
        JVMRuntime.value1 = JVMRuntime.stackpointer - (JVMRuntime.value * 4);
        //JVMRuntime.value1 = JVMRuntime.value1 - 1;

        checkStatus();
        
        //
        // Now get objectref into VALUE.
        //
        //JVMRuntime.value = MemoryController.readWord( JVMRuntime.value1 );
        JVMRuntime.value = MemoryController.readWord(JVMRuntime.value1 - 4);  // (stackpointer points to top of stack plus 4 bytes)

        checkStatus();

        //
        // Now get the class of the objectref
        // Overwrite classhandle with actual object's class handle and let Field/MethodLookup do its magic.
        //
        JVMRuntime.classhandle = MemoryController.readWord( JVMRuntime.value + ObjectBase.classReferenceOffset );
        
    }
    
}
