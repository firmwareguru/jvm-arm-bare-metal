/*
 * StackDuplicateXSingle.java
 *
 * Created on January 17, 2008, 7:38 AM
 *
 *
 */

package emr.jvm.process.ram;

import emr.jvm.process.JVMProcess;
import emr.jvm.Debug;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.nvm.*; // for the nvm
import emr.jvm.memory.ram.*; // for the ram
import emr.jvm.visualization.MemoryVisualizer;

/**
 * Reads the value at the top of the operand stack and inserts it
 * into a slot further down in the operand stack.  
 * 
 * Note!
 * The inserted value moves the values on top up by one.
 *
 * Implements DUP_X1 and DUP_X2.
 *
 * Parameterized by index register. Index should be 2 for DUP_X1
 * and 3 for DUP_X2.
 *
 * Register set:
 * 
 * Intermediate:  stackpointer - initially points to 1 beyond the top of the stack
 *                value - the values from the stack
 *                handle - variable
 *
 *
 * @author Evan Ross
 */
public class StackDuplicateXSingle extends JVMProcess {
    
    /** Creates a new instance of StackDuplicateSingle */
    public StackDuplicateXSingle() {
        super("StackDuplicateSingle");
    }
    
    public void runProcess()
    {
        
        /*
         *  RAM[SP] = RAM[SP-1]
         *  RAM[SP-1] = RAM[SP-2]
         *  RAM[SP-2] = RAM[SP]
         *  SP++
         */
        
        checkStatus();
        
        // HANDLE = SP-1
        JVMRuntime.handle = JVMRuntime.stackpointer - 4;

        checkStatus();
        
        // RAM[SP-1]
        JVMRuntime.value = MemoryController.readWord(JVMRuntime.handle);

        checkStatus();

        // RAM[SP] = RAM[SP-1]
        MemoryController.writeWord(JVMRuntime.stackpointer, JVMRuntime.value, MemoryVisualizer.FRAME);
        
        checkStatus();

        // SP-2
        JVMRuntime.index = JVMRuntime.handle - 4;
        
        checkStatus();

        // RAM[SP-2]
        JVMRuntime.value = MemoryController.readWord(JVMRuntime.index);
        
        checkStatus();
        
        // RAM[SP-1] = RAM[SP-2]
        MemoryController.writeWord(JVMRuntime.handle, JVMRuntime.value, MemoryVisualizer.FRAME);
                
        checkStatus();
        
        // RAM[SP]
        JVMRuntime.value = MemoryController.readWord(JVMRuntime.stackpointer);

        checkStatus();

        // RAM[SP-2] = RAM[SP]
        MemoryController.writeWord(JVMRuntime.index, JVMRuntime.value, MemoryVisualizer.FRAME);

        checkStatus();
        
        // SP++
        JVMRuntime.stackpointer += 4;
        
        
    }
    
}
