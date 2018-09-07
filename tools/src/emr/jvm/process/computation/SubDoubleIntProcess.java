/*
 * SubDoubleIntProcess.java
 *
 * Created on March 14, 2007, 10:18 PM
 *
 * SubDoubleIntProcess:  Sub value is register VALUE1 with value in register VALUE2 and put result in VALUE
 *
 *      value1 = frame.popInt();
 *      value2 = frame.popInt();
 *      value  = (float)value1 + (float)value2;
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
public class SubDoubleIntProcess extends JVMProcess
{
    
    /**
     * Creates a new instance of AddSingleIntProcess
     */
    public SubDoubleIntProcess()
    {
        super("SubDoubleInt");
    }
    
    public void runProcess()  
    {
        
        checkStatus();

        throwException(NOT_IMPLEMENTED);
    
    }
}
