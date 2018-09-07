/*
 * PrepareALUDoubleProcess.java
 *
 * Created on March 29, 2007, 8:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.jvm.process.computation;
import emr.jvm.process.JVMProcess;

import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.ram.*; // for the nvm
/**
 *
 * @author Evan Ross
 */
public class PrepareALUDoubleProcess extends JVMProcess
{
    
    /** Creates a new instance of PrepareALUDoubleProcess */
    public PrepareALUDoubleProcess()
    {
        super("PrepareALUDouble");
    }

    public void runProcess()  
    {
        throwException(NOT_IMPLEMENTED);
    }
}
