/*
 * LoadConstantPool.java
 *
 * Created on April 5, 2008, 4:55 PM
 *
 * Implements the LDC instruction; Obtains an int constant, a float constant or a 
 * String reference from the constant pool of the current class.
 *
 * Input:
 *    CurrentClass - class from which to obtain the constant pool item.
 *    Index - index into constant pool.
 *
 * Output:
 *    Value - item obtained from constant pool.
 */

package emr.jvm.process.nvm;
import emr.jvm.process.JVMProcess;
import emr.jvm.memory.ram.Frame;
import emr.jvm.memory.ram.ObjectThread;
import emr.jvm.JVMRuntime; // for the registers
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.nvm.*; // for the nvm
/**
 *
 * @author Evan Ross
 */
public class LoadConstantPoolProcess  extends JVMProcess {
    
    /** Creates a new instance of LoadConstant */
    public LoadConstantPoolProcess() 
    {
        super("LoadConstant");
    }
    
    public void runProcess()
    {
        checkStatus();
        
        // Load currentframe into name (to get currentclass)
        // Load currentclass into descriptor
        JVMRuntime.name = MemoryController.readWord(JVMRuntime.currentthread + ObjectThread.CurrentframeOffset);
        JVMRuntime.descriptor = MemoryController.readWord(JVMRuntime.name + Frame.CURRENT_CLASS_OFFSET);
 
        // Get the constant pool table address
        JVMRuntime.cphandle = MemoryController.readWord( JVMRuntime.descriptor + InternalClass.HEADER_CPTableOffset );
    
        // Get the particular RefInfo structure indicated by the input register 'index'
        JVMRuntime.value = MemoryController.readWord( JVMRuntime.cphandle + JVMRuntime.index );
  
        checkStatus();
        
    }
       
    
}
