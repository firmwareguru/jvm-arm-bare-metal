/*
 * DivSingleFloatProcess.java
 *
 * Created on March 31, 2007, 10:18 PM
 *
 * DivSingleFloatProcess:  Div value is register VALUE1 with value in register VALUE2 and put result in VALUE
 *
 *
 *      value  = value1 / value2;
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
public class DivSingleFloatProcess extends JVMProcess
{
    
    /**
     * Creates a new instance of AddSingleIntProcess
     */
    public DivSingleFloatProcess()
    {
        super("DivSingleFloat");
    }
    
    public void runProcess()  
    {
        
        checkStatus();

        /////////////////////////////////////////////////////////////////
        JVMRuntime.value = Float.floatToIntBits(
                Float.intBitsToFloat(JVMRuntime.value1) / 
                Float.intBitsToFloat(JVMRuntime.value2) );
        /////////////////////////////////////////////////////////////////
    
    }
}
