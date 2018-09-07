/*
 * CompareBranch.java
 *
 * Created on April 9, 2007, 9:40 PM
 *
 * Perform the branch if the compare is true.
 *
 * Register set:
 *    Input:  VALUE - result of comparison.  1 = true, 0 = false
 *            INDEX - branch offset from instruction
 *    Output: PC = - 3 + branch offset ( 3 is the instruction size )
 */

package emr.jvm.process.computation;

import emr.jvm.process.JVMProcess;
import emr.jvm.Debug;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.nvm.*; // for the nvm
import emr.jvm.memory.ram.*; // for the ram


/**
 *
 * @author Evan Ross
 */
public class CompareBranch extends JVMProcess
{
    
    /** Creates a new instance of CompareBranch */
    public CompareBranch()
    {
        super("CompareBranch");
    }
    
    public void runProcess() 
    {
        checkStatus();
        
        if( JVMRuntime.value == 1 ) // take the branch
            JVMRuntime.pc = JVMRuntime.pc - 3 + JVMRuntime.index;
    }
    
    
}
