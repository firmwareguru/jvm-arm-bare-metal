/*
 * RemainderInt.java
 *
 * Created on January 20, 2008, 7:37 PM
 *
 *
 */

package emr.jvm.process.computation;

import emr.jvm.process.JVMProcess;
import emr.jvm.JVMRuntime; // for the registers

/**
 *
 * RemainderIntProcess
 *
 * Implements IREM instruction.
 *
 * Register usage:
 *    Input: VALUE1, VALUE2
 *    Output: VALUE (result)
 *
 * value = value1 % value2  (implements Java's mod operator by using the mod operator).
 *
 *
 * @author Evan Ross
 */
public class RemainderIntProcess extends JVMProcess {
    
    /** Creates a new instance of RemainderInt */
    public RemainderIntProcess() {
        super("RemainderIntProcess");
    }
    
    
    @Override
    public void runProcess()
    {
        checkStatus();
        
        JVMRuntime.value = JVMRuntime.value1 % JVMRuntime.value2;
                
    }
    
}
