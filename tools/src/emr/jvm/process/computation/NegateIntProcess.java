/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package emr.jvm.process.computation;
import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
/**
 *
 * NegateIntProcess
 *
 * Implements INEG instruction.
 *
 * Register usage:
 *    Input: VALUE
 *    Output: VALUE (result)
 *
 * value = 0 - value.
 *
 * @author Evan Ross
 */
public class NegateIntProcess extends JVMProcess {

    /** Creates a new instance of RemainderInt */
    public NegateIntProcess() {
        super("NegateIntProcess");
    }

    @Override
    public void runProcess()
    {
        checkStatus();

        JVMRuntime.value = 0 - JVMRuntime.value;

    }
}
