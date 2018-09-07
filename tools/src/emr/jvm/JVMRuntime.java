/*
 * JVMRuntime.java
 *
 * Created on September 17, 2006, 5:50 PM
 *
 * The JVMRuntime object encapsulates all registers and variables the
 * JVMCore uses during operation.  The types of each element of this class
 * will not necessarily reflect the implementation used in a real device.
 *
 * All knowledge of the location and status of resouces is located here.
 *
 * All constants pertaining to the operation of the JVM are also located here.
 *
 * The runtime state of the JVMCore is stored in the following variables
 *
 * 
 *     CurrentFrame :
 *
 *     PC :
 *   
 *     CurrentClass : A pointer to the ClassFile object of the "current class" of the
 *                    currently executing method.
 *
 *     
 *  The RuntimeState also wraps access to state variables so that the memory visualizer will
 *  get updated 
 */

package emr.jvm;

import emr.elements.classfileparser.code.Instruction;
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.ram.Frame;
import emr.jvm.memory.ram.ObjectThread;

import javax.swing.JTextArea;

/**
 *
 * @author Ross
 */
public class JVMRuntime {
    
    
 
    // Constant: NULL register.  
    public static final int nullregister = 0xffffffff; // can't really use 0

    public static final int INIT_INT = 0;
    public static final int INIT_FLOAT = Float.floatToIntBits(0.0f);
    public static final int INIT_DOUBLE1 = (int)((Double.doubleToLongBits(0.0) >>> 32) & 0xffffffffl);
    public static final int INIT_DOUBLE2 = (int)(Double.doubleToLongBits(0.0) & 0xffffffffl);
    public static final int INIT_REF = nullregister;

    
    ////////////////////////////////////////////////////////////////////
    //  System Registers
    ////////////////////////////////////////////////////////////////////
    
    public static int value;    // general purpose
    public static int value1;   // branch compares or general purpose
    public static int value2;   // 
    public static int handle;   // general references to objects
    public static int index;    // index into various objects
    public static int cphandle; // reference to the constant pool
    public static int tablehandle; // reference to a particular table
    
    // -- the following are the 3 items used in field or method resolution --
    public static int classhandle;  // points directly to internalclass
    public static int name;         // hashed name of field or method
    public static int descriptor;   // hashed descriptor of field or method
    
    // registers for immediate frame operations
    // -- the currentclass is stored in each frame and is what ReferenceLookup works on
    //public static int currentclass;
    public static int stackpointer;
    //public static int currentframe;
    
    public static int pc;  // the program counter
    public static int opcode;  // the most recently fetched opcode
    
    // CPU-wide registers.  Do not need to be backed up in a context switch
    // These are static and must persist, however they do not necessarily need
    // to be in the local register file all the time - they could be
    // in RAM somewhere and pulled in when needed.
    public static int currentthread = 0xffffffff;
    public static int eligiblehead = 0xffffffff;
    public static int waitinghead = 0xffffffff;
    public static int freehead;

    public static int systemtimer = Integer.MAX_VALUE;

    /* The Process Control Word (PCW) is the means by which the Instruction Execution Engine
     * communicates to the Process Manager the processes that must be executed as a result
     * of handling instructions.  
     *   PCW bits 0 - 7  specify an alternative process to run other than the default instruction executor.   
     *   PCW bits 8 - 15 flags
     *   PCW bits 16 - 31: interrupt flag bits. indicate the interrupt that should be run.
     */
    public static int PCW = 0;
    
    public static final int PCW_INTERRUPT_MASK = 0xffff0000;
    public static final int PCW_PROCESS_MASK = 0x000000ff;   
    public static final int PCW_FLAGS_MASK = 0x0000ff00;
    public static final int PCW_FLAGS_SHIFT = 8;
    
    // The flags 
    //  bit 0:  1=virtual, 0=static
    //  bit 1:  1=method, 0=field
    // to clear, &= ~flag
    public static final int PCW_FLAG_LOOKUP = 0x00000100; // set flag bit 0
    public static final int PCW_FLAG_TABLE = 0x00000200;  // set flag bit 1
        
    
    public static void dumpRegisters()
    {
        dumpRegisters(null);
    }
           
    public static void dumpRegisters(JTextArea tA_)
    {
        
        if( tA_ != null)
            tA_.setText("");
        else
            System.err.println(" ------ Register Dump -----");
        
        int currentframe = nullregister;
        int currentclass = nullregister;
        
        if (currentthread != nullregister)
            currentframe = MemoryController.readWord(currentthread + ObjectThread.CurrentframeOffset);
        
        if (currentframe != nullregister)
            currentclass = MemoryController.readWord(currentframe + Frame.CURRENT_CLASS_OFFSET);
        
        printReg("value", value, tA_);
        printReg("value1", value1, tA_);
        printReg("value2", value2, tA_);
        printReg("handle", handle, tA_);
        printReg("index", index, tA_);
        printReg("cphandle", cphandle, tA_);
        printReg("classhandle", classhandle, tA_);
        printReg("tablehandle", tablehandle, tA_);
        printReg("name", name, tA_);
        printReg("descriptor", descriptor, tA_);
        printReg("currentclass", currentclass, tA_); // From currentthread's currentframe
        printReg("stackpointer", stackpointer, tA_);
        printReg("currentframe", currentframe, tA_); // From currentthread
        printReg("pc", pc, tA_);
        printReg("opcode", opcode, tA_);
        printReg("currentthread", currentthread, tA_);
        printReg("eligiblehead", eligiblehead, tA_);
        printReg("waitinghead", waitinghead, tA_);
        printReg("systemtimer", systemtimer, tA_);
        printReg("freehead", freehead, tA_);
        printReg("pcw", PCW, tA_);
        

        if( tA_ != null )
            tA_.append("                     : " + Instruction.getInstructionName(opcode) + "\n");
        else
            System.err.printf("                     : " + Instruction.getInstructionName(opcode) + "\n");
        
    }
    
    private static void printReg(String rS_, int r_, JTextArea tA_)
    {
        if( tA_ != null )
        {
            tA_.append(String.format("%20s : ", rS_));
            tA_.append(r_  + " " + Integer.toHexString(r_) + "\n");
        }
        else
        {
        
            System.err.printf("%20s : ", rS_);
            System.err.println( r_  + " " + Integer.toHexString(r_));
        }
    }

    /**
     * A main for dinking around.
     *
     * @param args
     */
    public static void main(String[] args) {
        float f0 = 0.0f;
        float f1 = 1.0f;
        float f2 = 2.0f;
        System.out.println(Integer.toHexString(Float.floatToIntBits(f0)));
        System.out.println(Integer.toHexString(Float.floatToIntBits(f1)));
        System.out.println(Integer.toHexString(Float.floatToIntBits(f2)));
    }
    
}
