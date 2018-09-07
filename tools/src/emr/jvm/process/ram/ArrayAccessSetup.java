/*
 * ArrayAccessSetup.java
 *
 * Created on June 10, 2007, 3:36 PM
 *
 * ArrayAccesssSetup pops the index and the arrayref from the opstack in preparation
 * for an array access process - either a get or a put
 *
 * Register Set:
 *   Input: STACKPOINTER
 *   Output: INDEX - the index into the array
 *           HANDLE - the arrayref
 */

package emr.jvm.process.ram;

import emr.jvm.process.JVMProcess;
import emr.jvm.Debug;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.nvm.*; // for the nvm
import emr.jvm.memory.ram.*; // for the ram


/**
 *
 * @author Evan Ross
 */
public class ArrayAccessSetup extends JVMProcess
{
    
    /** Creates a new instance of ArrayAccessSetup */
    public ArrayAccessSetup()
    {
        super("ArrayAccessSetup");
    }
    
    @Override
    public void runProcess()
    {
        checkStatus();

        // Get index from opstack
        // 1. decrement stackpointer
        JVMRuntime.stackpointer -= 4;

        checkStatus();
        
        // 2. pop word from address pointed to by stack pointer to INDEX
        JVMRuntime.index = MemoryController.readWord(JVMRuntime.stackpointer);

        checkStatus();

        // Get the arrayref
        // 1. decrement stackpointer
        JVMRuntime.stackpointer -= 4;

        checkStatus();
        
        // 2. pop word from address pointed to by stack pointer to HANDLE
        JVMRuntime.handle = MemoryController.readWord(JVMRuntime.stackpointer);

        
    }
    
    
}
