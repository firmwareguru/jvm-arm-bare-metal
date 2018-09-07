/*
 * TableLookupProcess.java
 *
 * Created on January 3, 2007, 10:05 PM
 *
 * TableLookup begins at the object pointed to by CLASSHANDLE register.  A TableLookup will look up
 * either a field or a method depending on which table TABLEHANDLE points to (FieldTable or MethodTable).
 *
 * Register map:
 * Input: CLASSHANDLE - pointer to InternalClass to begin search 
 *        NAME  - name of method or field to match
 *        DESCRIPTOR  -  descriptor of method or field to match
 *
 * Intermediate: CPHANDLE - pointer to constant pool of classhandle
 *               VALUE, VALUE1, VALUE2
 *
 * Output: TABLEHANDLE - points to matching Field or Method TableEntry
  */

package emr.jvm.process.nvm;

import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.nvm.*; // for the nvm
/**
 *
 * @author Evan Ross
 */
public class TableLookupProcess extends JVMProcess
{
    
    /**
     * Creates a new instance of TableLookupProcess
     */
    public TableLookupProcess()
    {
        super("TableLookup");
    }
    
    
    /**
     * Requires: CLASSHANDLE
     *
     * 1. Get MethodTable address from header of CLASSHANDLE => TABLEHANDLE
     * 2. Get CP address from header of CLASSHANDLE => CPHANDLE
     * 3. For each MethodTableEntry -
     *      if '0' goto step 12
     * 4.   get name index  => VALUE1
     * 5.   get name from CPHANDLE + VALUE1 => VALUE1
     * 6.   get descriptor index => VALUE2
     * 7.   get descriptor from CPHANDLE + VALUE2 => VALUE2
     * 8.   compare VALUE1 <=> NAME
     * 9.   compare VALUE2 <=> DESCRIPTOR
     * 10.  If same, quit.
     * 11. else goto step 3
     * 12. Get superclass from header of CLASSHANDLE => CLASSHANDLE
     * 13  If HANDLE = 0 then throw MethodNotFoundException
     * 14   else got step 1.
     */
     
    public void runProcess()
    {
            
        do
        {
            // If CLASSHANDLE is 0 then throw MethodOrFieldNotFoundException
            if( JVMRuntime.classhandle == 0 )
                throwException(METHOD_OR_FIELD_NOT_FOUND);

            checkStatus();

            // Get ConstantPool from the header
            JVMRuntime.cphandle = MemoryController.readWord(JVMRuntime.classhandle + InternalClass.HEADER_CPTableOffset);
        
            checkStatus();
         
            // Initialize the tablehandle pointer to point to the start of the table.
            //if( (JVMRuntime.index & 0x2) == 0x2 ) // 2nd bit 1=methods, 0=fields
            if ((JVMRuntime.PCW & JVMRuntime.PCW_FLAG_TABLE) == JVMRuntime.PCW_FLAG_TABLE) 
                // Get address of MethodTable from the header of the InternalClass pointed to be CLASSHANDLE
                JVMRuntime.tablehandle = MemoryController.readWord(JVMRuntime.classhandle + InternalClass.HEADER_MethodTableOffset);
            else // field lookup mode
                // Get address of FieldTable from the header of the InternalClass pointed to be CLASSHANDLE
                JVMRuntime.tablehandle = MemoryController.readWord(JVMRuntime.classhandle + InternalClass.HEADER_FieldTableOffset);

            checkStatus();
            
            // Loop through all method table entries while the name & descriptor word is not 0 (end of table)
            while( ( JVMRuntime.value1 = MemoryController.readWord( JVMRuntime.tablehandle + InternalClass.TABLEENTRY_NameDescriptorIndexOffset )) != 0 )
            {
                checkStatus();

                // Get name index
                JVMRuntime.value2 = ( JVMRuntime.value1 >> 16 ) & 0xffff;
                JVMRuntime.value2 <<= 2; // index is specified as slots or words, must turn into byte address
                
        
                checkStatus();
            
                // Get name
                JVMRuntime.value2 = MemoryController.readWord( JVMRuntime.cphandle + JVMRuntime.value2 );
        
                checkStatus();
            
                // Get descriptor index
                JVMRuntime.value1 = ( JVMRuntime.value1       ) & 0xffff;
                JVMRuntime.value1 <<= 2; // ditto
            
                checkStatus();
        
                // get descriptor
                JVMRuntime.value1 = MemoryController.readWord( JVMRuntime.cphandle + JVMRuntime.value1 );
               
                checkStatus();
        
                // check if value1 == descriptor AND value2 == name
                if( JVMRuntime.value1 == JVMRuntime.descriptor && JVMRuntime.value2 == JVMRuntime.name )
                {
                    // found.  quit.
                    debug("Method or Field found.");
                    //JVMRuntime.dumpRegisters();
                    return;
                }

                checkStatus();

                // Go to next TableEntry
                JVMRuntime.tablehandle += InternalClass.TABLEENTRY_Size;
                
            }               

            checkStatus();

            // now setup CLASSHANDLE with pointer to super class
            // do only if recursive/virtual lookup is specified (index == 1)
            //if( (JVMRuntime.index & 0x1) == 0x1 ) // 1st bit, 1=virtual (recursive), 0=static
            if ((JVMRuntime.PCW & JVMRuntime.PCW_FLAG_LOOKUP) == JVMRuntime.PCW_FLAG_LOOKUP)
            {
                JVMRuntime.classhandle = MemoryController.readWord( JVMRuntime.classhandle + InternalClass.HEADER_SuperClassOffset );
                debug("Recursing to super class: " + JVMRuntime.classhandle);
            }
            else
            {
                throwException(METHOD_OR_FIELD_NOT_FOUND);
            }

        // start lookup again with super class
        } while( true ); 
        
    } 
    
}
