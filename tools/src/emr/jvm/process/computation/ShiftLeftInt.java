/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package emr.jvm.process.computation;
import emr.jvm.process.JVMProcess;
import emr.jvm.JVMRuntime; // for the registers
/**
 *
 * ShiftLeftInt
 *
 * Implements ISHL instruction.
 *
 * Register usage:
 *    Input: VALUE1, VALUE2
 *    Output: VALUE (result)
 *
 * value = value1 << value2  
 * @author Evan Ross
 */
public class ShiftLeftInt extends JVMProcess {

    public ShiftLeftInt() {
        super("ShiftLeftIntProcess");
    }


    @Override
    public void runProcess()
    {
        checkStatus();

        JVMRuntime.value = JVMRuntime.value1 << JVMRuntime.value2;

    }

}
