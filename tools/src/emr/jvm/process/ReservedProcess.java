/*
 * ReservedProcess.java
 *
 * Created on March 31, 2007, 4:37 PM
 *
 * Dummy placeholder process.
 */

package emr.jvm.process;
import emr.jvm.process.JVMProcess;
/**
 *
 * @author Evan Ross
 */
public class ReservedProcess extends JVMProcess
{
    
    /** Creates a new instance of ReservedProcess */
    public ReservedProcess()
    {
        super("ReservedProcess");
    }
    
    public void runProcess()  
    {
        throwException(NOT_IMPLEMENTED);
    }    
    
}
