/*
 * ReferenceLookupProcess.java
 *
 * Created on January 16, 2007, 8:49 PM
 *
 * The ReferenceLookupProcess is responsible for obtaining the classname, method or field name and method or field descriptor
 * from a ConstantPoolTable item that is a InterfaceMethodRefInfo, MethodRefInfo or FieldRefInfo structure.
 *
 * This process is intended to be used as part of a composite process that handles invokevirtual #2 and similar
 * instructions that directly index the constant pool table.
 *
 * The input 'index' typically comes from an instruction operand.  'currentclass' is the class of the current frame.
 *
 * Register set:
 * Input:  index - index into CP table, pointing to one of the afore mentioned structures.
 *         currentclass - the handle (address) of the class of the current frame
 * Output: classhandle - pointer to the class in which this reference may be found
 *         name        - the method or field name of the reference
 *         descriptor  - the method of field descriptor of the reference
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
public class ReferenceLookupProcess extends JVMProcess
{
    
    /**
     * Creates a new instance of ReferenceLookupProcess
     */
    public ReferenceLookupProcess()
    {
        super("ReferenceLookup");
    }
   /** Process:
     *    1.  Get the constant pool address -> cphandle
     *    2.  get the RefInfo as in cphandle+index => value
     *    3.  get the ClassInfo index as in RefInfo >> 16 & 0xffff (upper 2 bytes) => value1
     *    4.  get the NameAndTypeRef index as in RefInfo & 0xffff (lower 2 bytes) => value2
     *    5.  get the classname index from the classinfo => value
     *   *6.  get the classname as in cphandle+value => classname
     *    7.  get the NameAndType as in cphandle+value2 => value
     *    8.  get the name index as in nameandtype >> 16 & 0xffff (upper 2 bytes) => value1
     *    9.  get the descriptor index as in nameandtype & 0xffff (lower 2 bytes) => value2
     *  *10.  get the name as in cphandle+value1 => name
     *  *11.  get the descriptor as in cphandle+value2 => descriptor
     */
    public void runProcess() 
    {
        checkStatus();

        // Load currentframe into name
        // Load currentclass into descriptor
        JVMRuntime.name = MemoryController.readWord(JVMRuntime.currentthread + ObjectThread.CurrentframeOffset);
        JVMRuntime.descriptor = MemoryController.readWord(JVMRuntime.name + Frame.CURRENT_CLASS_OFFSET);
 
        // Get the constant pool table address
        JVMRuntime.cphandle = MemoryController.readWord( JVMRuntime.descriptor + InternalClass.HEADER_CPTableOffset );

        checkStatus();
        
        // 'index' must be multiplied by 4 to turn it from "slot" to byte address.
        JVMRuntime.index <<= 2;

        // Get the particular RefInfo structure indicated by the input register 'index'
        JVMRuntime.value = MemoryController.readWord( JVMRuntime.cphandle + JVMRuntime.index );
        
        checkStatus();

        // Get the ClassInfo index
        JVMRuntime.value1 = ( JVMRuntime.value >> 16 ) & 0xffff;
        JVMRuntime.value1 <<= 2;
        
        checkStatus();

        // Get the NameAndTypeInfo index
        JVMRuntime.value2 = ( JVMRuntime.value       ) & 0xffff;
        JVMRuntime.value2 <<= 2;
        
        checkStatus();

        // Now get the name index out of the ClassInfo index
        JVMRuntime.value = MemoryController.readWord( JVMRuntime.cphandle + JVMRuntime.value1 );
        JVMRuntime.value <<= 2;

        checkStatus();

        ///////////////////////////////////////////////////////////////////////
        // Get the pre-resolved class handle from the item pointed to by the ClassInfo index
        ///////////////////////////////////////////////////////////////////////
        JVMRuntime.classhandle = MemoryController.readWord( JVMRuntime.cphandle + JVMRuntime.value );
        
        //JVMRuntime.dumpRegisters();
        
        checkStatus();

        // Get the NameAndType structure that was pointed to by NameAndTypeInfo index
        JVMRuntime.value = MemoryController.readWord( JVMRuntime.cphandle + JVMRuntime.value2 );
        
        checkStatus();

        // Get the name index out of NameAndType
        JVMRuntime.value1 = ( JVMRuntime.value >> 16 ) & 0xffff;
        JVMRuntime.value1 <<= 2;
        
        checkStatus();

        // Get the descriptor index out of NameAndType
        JVMRuntime.value2 = ( JVMRuntime.value       ) & 0xffff;
        JVMRuntime.value2 <<= 2;
        
        checkStatus();

        ///////////////////////////////////////////////////////////////////////
        // Get the field or method name
        ///////////////////////////////////////////////////////////////////////
        JVMRuntime.name = MemoryController.readWord( JVMRuntime.cphandle + JVMRuntime.value1 );
        
        checkStatus();

        ///////////////////////////////////////////////////////////////////////
        // Get the field or method descriptor
        ///////////////////////////////////////////////////////////////////////
        JVMRuntime.descriptor = MemoryController.readWord( JVMRuntime.cphandle + JVMRuntime.value2 );
    }
    
}
