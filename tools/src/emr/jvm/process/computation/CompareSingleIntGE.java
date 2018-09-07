/*
 * CompareSingleIntGE.java
 *
 * Created on April 9, 2007, 9:38 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
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
public class CompareSingleIntGE extends JVMProcess
{
    
    /** Creates a new instance of CompareSingleIntGE */
    public CompareSingleIntGE()
    {
        super("CompareSingleIntGE");
    }
    
    public void runProcess()  
    {
        checkStatus();
        if ( JVMRuntime.value1 >=JVMRuntime.value2 )
            JVMRuntime.value = 1; // true
        else
            JVMRuntime.value = 0; // false
         
    }
    
    
}
