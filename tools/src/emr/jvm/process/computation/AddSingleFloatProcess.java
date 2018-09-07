/*
 * AddSingleFloatProcess.java
 *
 * Created on March 14, 2007, 10:18 PM
 *
 * AddSingleFloatProcess:  Add value is register VALUE1 with value in register VALUE2 and put result in VALUE
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
/**
 *
 * @author Evan Ross
 */
public class AddSingleFloatProcess extends JVMProcess
{
    
    /**
     * Creates a new instance of AddSingleIntProcess
     */
    public AddSingleFloatProcess()
    {
        super("AddSingleFloat");
    }
    
    public void runProcess() 
    {
        
        checkStatus();

        /////////////////////////////////////////////////////////////////
        JVMRuntime.value = Float.floatToIntBits( 
                Float.intBitsToFloat(JVMRuntime.value1) + 
                Float.intBitsToFloat(JVMRuntime.value2) );
        /////////////////////////////////////////////////////////////////
    
    }
}
