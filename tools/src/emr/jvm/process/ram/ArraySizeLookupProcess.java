/*
 * ArraySizeLookupProcess.java
 *
 * Created on March 31, 2007, 3:33 PM
 *
 * Determine the total amount of memory to allocate to this Array object type.  This is different than
 * the ObjectSizeLookupProcess because the size of this Array object is dependent on the count of elements
 * and not on the count of fields determined prior in the ClassPackager.
 *
 * There are two instructions that create arrays:
 *   newarray, anewarray.
 *
 * Either of these instructions calls the same array creation process.  The 'count' of array elements
 * is on the operand stack and made available to this process in the register VALUE from a prior StackPopSingle.
 * 
 * However, the size of memory required for this array object is dependent on the type of elements in this array.
 * There are 4 categories of elements:
 *    8 bits: Byte T_BOOL, T_CHAR, T_BYTE
 *   16 bits: T_SHORT
 *   32 bits: T_INT, T_FLOAT, Reference (from anewarray)
 *   64 bits: T_LONG, T_DOUBLE
 *
 * The 32 bit types are the standard 'Single' words.  The 64 bit types are the standard 'Double' words.  The additional
 * types are created here to reduce wasted memory.  This is possible because there are separate instructions
 * for accessing arrays of byte, short, int etc.
 *
 * The count of array elements, 'count', is modified to indicate the count of fundamental memory units (Single, 32-bit)
 * in terms of the element type.  For example, the count for byte types is divided by 4 to get the number
 * of Singles to hold those bytes.  We use shifts to do the division or multiplication.
 *
 * Register Set:
 *   Input: VALUE - 'count' of array elements
 *          INDEX - type of array elements.  for newarray, index is 'atype' operand.  for anewarray, index is
 *                  manually set to custom type T_REF (12) for Single element type.
 *     
 *   Output: VALUE - total memory size to allocate. 
 *           VALUE1 - 'count' of array elements.
 *
 *
 */

package emr.jvm.process.ram;
import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.ram.*; // for the ram

import emr.jvm.visualization.MemoryVisualizer;

/**
 *
 * @author Evan Ross
 */
public class ArraySizeLookupProcess extends JVMProcess
{
    
    /** Creates a new instance of ArraySizeLookupProcess */
    public ArraySizeLookupProcess()
    {
        super("ArraySizeLookup");
    }
    
    public void runProcess() 
    {
        checkStatus();

        // copy VALUE (size of array) to VALUE1 for use by ArraySetupProcess before modifying VALUE
        JVMRuntime.value1 = JVMRuntime.value;

        
        switch( JVMRuntime.index )
        {
            // 8-bit types
            case 4: // T_BOOLEAN
            case 5: // T_CHAR
            case 8: // T_BYTE
                // Do nothing, already in byte format.
                break;
                
            // 16-bit types
            case 9: // T_SHORT

                // Multiply by 2 to get number of bytes required.
                JVMRuntime.value *= 2;
                break;
               
            // Single types   
            case 6: // T_FLOAT
            case 10: // T_INT
            case 12: // T_REF
                
                // Multiply by 4 to get number of bytes required.
                JVMRuntime.value *= 4;
                break;
                
            // Double types
            case 7: // T_DOUBLE
            case 11:// T_LONG

                // Multiply by 8 to get number of bytes required.
                JVMRuntime.value *= 8;
                break;
                
            default:
                throwException(INVALID_ARRAY_TYPE);
        }
        
        // Check if number of elements is not a multiple of 4 bytes (required).
        // If not, add 4 then clear the 2 LSBs.
        if ((JVMRuntime.value & 0x3) != 0)
        {
            JVMRuntime.value += 4; // Add 4
            JVMRuntime.value &= 0xfffffffc; // clear 2 LSB
        }
        
        checkStatus();
        
        // now add Array.size to VALUE to get total memory footprint for AllocateMemoryProcess
        JVMRuntime.value += Array.ArrayBaseSize;
        
        // Set the color that subsequent memory should be initialized to
        MemoryPool.initializationColor = MemoryVisualizer.ARRAY;

        
    }
    
}
