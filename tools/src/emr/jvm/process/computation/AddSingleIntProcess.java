/*
 * AddSingleIntProcess.java
 *
 * Created on March 14, 2007, 10:18 PM
 *
 * AddSingleIntProcess:  Add value is register VALUE1 with value in register VALUE2 and put result in VALUE
 *
 *      value1 = frame.popInt();
 *      value2 = frame.popInt();
 *      value  = value1 + value2;
 *      frame.pushInt(value);
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
public class AddSingleIntProcess extends JVMProcess
{
    
    /**
     * Creates a new instance of AddSingleIntProcess
     */
    public AddSingleIntProcess()
    {
        super("AddSingleInt");
    }
    
    public void runProcess() 
    {
        checkStatus();

        /////////////////////////////////////////////////////////////////
        JVMRuntime.value = JVMRuntime.value1 + JVMRuntime.value2;
        /////////////////////////////////////////////////////////////////
    
    }
}
