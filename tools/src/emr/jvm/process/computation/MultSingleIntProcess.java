/*
 * MultSingleIntProcess.java
 *
 * Created on March 14, 2007, 10:18 PM
 *
 * MultSingleIntProcess:  Multiply value is register VALUE1 with value in register VALUE2 and put result in VALUE
 *
 *
 *      value  = value1 * value2;
 *
 *
 */

package emr.jvm.process.computation;

import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.ram.*; // for the nvm
/**
 *
 * @author Evan Ross
 */
public class MultSingleIntProcess extends JVMProcess
{
    
    /**
     * Creates a new instance of AddSingleIntProcess
     */
    public MultSingleIntProcess()
    {
        super("MultSingleInt");
    }
    
    public void runProcess()  
    {
        
        checkStatus();

        /////////////////////////////////////////////////////////////////
        JVMRuntime.value = JVMRuntime.value1 * JVMRuntime.value2;
        /////////////////////////////////////////////////////////////////
    
    }
}
