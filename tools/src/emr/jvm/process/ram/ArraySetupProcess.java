/*
 * ArraySetupProcess.java
 *
 * Created on March 31, 2007, 3:40 PM
 *
 * Setup an Array which is just an Object with an added hidden field, arraylength.  
 *
 * What about the class type of primitive array types?
 *
 * Register Set:
 *   Input:
 *     INDEX - type of array to initialize
 *     VALUE1 - size of array, count of array fields
 *     HANDLE - reference to newly allocated array object
 *   Output:
 *     n/a
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
public class ArraySetupProcess extends JVMProcess
{
    
    /** Creates a new instance of ArraySetupProcess */
    public ArraySetupProcess()
    {
        super("ArraySetupProcess");
    }

    public void runProcess() 
    {
        checkStatus();
        
        // copy this value into the arraylength field
        MemoryController.writeWord( JVMRuntime.handle + Array.ArrayLengthOffset, JVMRuntime.value1, MemoryVisualizer.INSTANCE);

        // Move handle to start of array data region
        JVMRuntime.value2 = JVMRuntime.handle + Array.ArrayBaseSize;

        // Initialize the array:
        switch (JVMRuntime.index)
        {
            // 8-bit int types: default = 0
            case 4: // T_BOOLEAN
            case 5: // T_CHAR
            case 8: // T_BYTE
                while (JVMRuntime.value1 > 0)
                {
                    JVMRuntime.value1--;
                    MemoryController.writeByte(JVMRuntime.value2 + JVMRuntime.value1, JVMRuntime.INIT_INT, MemoryVisualizer.ARRAY);

                }
                break;

            // 16-bit types: default = 0
            case 9: // T_SHORT

                while (JVMRuntime.value1 > 0)
                {
                    JVMRuntime.value1--;
                    MemoryController.writeShort(JVMRuntime.value2 + JVMRuntime.value1 * 2, JVMRuntime.INIT_INT, MemoryVisualizer.ARRAY);
                }
                break;

            // Single types
            case 6: // T_FLOAT
                while (JVMRuntime.value1 > 0)
                {
                    JVMRuntime.value1--;
                    MemoryController.writeWord(JVMRuntime.value2 + JVMRuntime.value1 * 4, JVMRuntime.INIT_FLOAT, MemoryVisualizer.ARRAY);
                }
                break;

            case 10: // T_INT

                while (JVMRuntime.value1 > 0)
                {
                    JVMRuntime.value1--;
                    MemoryController.writeWord(JVMRuntime.value2 + JVMRuntime.value1 * 4, JVMRuntime.INIT_INT, MemoryVisualizer.ARRAY);
                }
                break;

            // Double types
            case 7: // T_DOUBLE
                while (JVMRuntime.value1 > 0)
                {
                    JVMRuntime.value1--;
                    MemoryController.writeWord(JVMRuntime.value2 + JVMRuntime.value1 * 8, JVMRuntime.INIT_DOUBLE1, MemoryVisualizer.ARRAY);
                    MemoryController.writeWord(JVMRuntime.value2 + (JVMRuntime.value1 * 8) + 4, JVMRuntime.INIT_DOUBLE2, MemoryVisualizer.ARRAY);
                }
                break;
            case 11:// T_LONG

                while (JVMRuntime.value1 > 0)
                {
                    JVMRuntime.value1--;
                    MemoryController.writeWord(JVMRuntime.value2 + JVMRuntime.value1 * 8, JVMRuntime.INIT_INT, MemoryVisualizer.ARRAY);
                    MemoryController.writeWord(JVMRuntime.value2 + (JVMRuntime.value1 * 8) + 4, JVMRuntime.INIT_INT, MemoryVisualizer.ARRAY);
                }
                break;
            case 12:// T_REF

                while (JVMRuntime.value1 > 0)
                {
                    JVMRuntime.value1--;
                    MemoryController.writeWord(JVMRuntime.value2 + JVMRuntime.value1 * 4, JVMRuntime.INIT_REF, MemoryVisualizer.ARRAY);
                }
                break;

            default:
                throwException(INVALID_ARRAY_TYPE);
        }


    }
    
}
