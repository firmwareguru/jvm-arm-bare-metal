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
 * Implements ISHR instruction.
 *
 * Register usage:
 *    Input: VALUE1, VALUE2
 *    Output: VALUE (result)
 *
 * value = value1 >> value2  with sign extension
 *
 * @author Evan Ross
 */
public class ShiftRightInt extends JVMProcess {

    public ShiftRightInt() {
        super("ShiftRightIntProcess");
    }

    @Override
    public void runProcess()
    {
        checkStatus();

        JVMRuntime.value = JVMRuntime.value1 >> JVMRuntime.value2;
    }

}
