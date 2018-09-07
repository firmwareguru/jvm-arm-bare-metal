/*
 * ClassLookupProcess.java
 *
 * Created on January 3, 2007, 9:40 PM
 *
 * The ClassLookup process is an atomic process.  It takes as input a class name
 * in the form of the 32-bit hash word.  It then searches through the class offset table
 * using the following algorithm:
 *    for each entry (32-bit word) in the table:
 *      1. take the address at that offset and apply an InternalClass Header to the
 *         NVM region starting at that address.  
 *      2. The "this_class" entry in the Header points to an NVM address containing
 *         the 32-bit word representing the address.
 * Return the handle of the InternalClass.
 * 
 * Registers used:
 *   value - hash of the desired class name
 *   index - address in NVM of the ClassOffset element
 *   handle - address in NVM of the found InternalClass with the given class name
 *   
 * Note: The ClassLookupProcess is no longer necessary as of Feb 1.  The class names
 * referenced by ClassInfo structures have been pre-resolved in the ClassPackager to
 * the NVM address (absolute, DWord addressable) of the InternalClass structure.
 *
 */

package emr.jvm.process.nvm;

import emr.jvm.process.JVMProcess;
import emr.jvm.Debug;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.nvm.*; // for the nvm

/**
 *
 * @author Evan Ross
 */
public class ClassLookupProcess extends JVMProcess
{
    
    /** Creates a new instance of ClassLookupProcess */
    public ClassLookupProcess()
    {
        super("ClassLookup");
    }
    
    /** Requires that register 'value' be setup with the desired class name hash */
    public void runProcess()
    {
        throwException(NOT_IMPLEMENTED);
        /*
        // ClassOffsetTable is the the first array of 32-bit words in NVM.
        // Loop until offset of '0' is found.  This denotes end of ClassOffsetTable.
        
        JVMRuntime.index = 0;
        do
        {
            JVMRuntime.handle = NVM.get4ByteWord( JVMRuntime.index );

            if( JVMRuntime.handle != 0 )
                JVMRuntime.classname = NVM.get4ByteWord( JVMRuntime.handle + InternalClass.HEADER_ThisClassOffset );

            debug("index = " + JVMRuntime.index + " handle = " + JVMRuntime.handle + " name = " + 
                    JVMRuntime.value + " classname = " + JVMRuntime.classname );

            if( JVMRuntime.value == JVMRuntime.classname ) 
                return;
            
            JVMRuntime.index += 1;
        }
        while( JVMRuntime.handle != 0 );
        
        // if the handle is 0 then the lookup failed.  an exception must be thrown.
        throwException(JVMProcess.CLASS_NOT_FOUND);
         */
    }
    
    
    
}
