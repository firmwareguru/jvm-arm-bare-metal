/*
 * MultDoubleIntProcess.java
 *
 * Created on March 14, 2007, 10:18 PM
 *
 * MultDoubleIntProcess:  Multiply value is register VALUE1 with value in register VALUE2 and put result in VALUE
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
public class MultDoubleIntProcess extends JVMProcess
{
    
    /**
     * Creates a new instance of AddSingleIntProcess
     */
    public MultDoubleIntProcess()
    {
        super("MultDoubleInt");
    }
    
    public void runProcess()  
    {
        
        checkStatus();

        throwException(NOT_IMPLEMENTED);
    
    }
}
