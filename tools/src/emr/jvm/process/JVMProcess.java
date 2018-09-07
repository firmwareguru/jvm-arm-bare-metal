/*
 * JVMProcess.java
 *
 * Created on January 1, 2007, 4:33 PM
 *
 * JVMProcess implements the interface between the ProcessManager and a process.
 *
 * StartProcess() simulates an enable signal.  The ProcessManager calls this function
 * which returns immediately.  The ProcessManager may or may not 'block' after this call
 * depending entirely on a rule set internal to the ProcessManager.  This rule set specifies
 * the order of execution of atomic processes to facilitate the operation of composite 
 * processes.
 *
 * StopProcess() is merely a signal to the proccess to stop at its earliest convenience when
 * it is safe to do so, where 'safe' is determined solely by the process itself.  There is no
 * guarantee as to when, if at all, it will prematurely stop.  When it does stop, which may
 * be when it normally ends, the ProcessManager is notified via the standard notification
 * mechanism which is discussed below.
 *
 * Processes 'interrupt' the ProcessManager when they are done.
 *
 * Implementation details.
 * Processes are Threads that are started but waiting on a ProcessMonitor object.  When that object is notified,
 * the process becomes active and proceeds until completion, where it notifies th
 *
 */

package emr.jvm.process;

import emr.jvm.JVMRuntime;

import javax.swing.JTextArea;
import emr.jvm.visualization.CoreVisualizer;

/**
 *
 * @author Evan Ross
 */
public class JVMProcess
{
    /* debug constant */
    public static boolean processDebug = true;
    
    /* Handles synchronization between ProcessManager and this Process */
    final static private java.lang.Object processMonitor = new java.lang.Object();
    
    /** supports stepping mode */
    private static boolean step = false;
    
    /** supports instruction-level stepping mode */
    public static boolean instructionStep = false;
    

    private String processName;
    
    /* textarea to output registers to in a checkStatus call
     */
    static public JTextArea regDumpArea = null;
    
    static public boolean dumpRegisters = true;
     
    
    
    
    /** Creates a new instance of JVMProcess */
    public JVMProcess(String name_)
    {
        processName = name_;
    }
   
    
    /** Processes implement their tasks in runProcess()
     */
    public void runProcess()
    {
        
        int delay = (int)Math.round(Math.random() * 5000);
        for( int i = 0; i < 5; i++)
        {
            debug("Running proces, delay " + delay);
            try { Thread.sleep( delay ); } catch ( InterruptedException e ){ }
        }
    }
    
    /** Checks the status of the JVM:
     *    If an interrupt is pending, throw InterruptedException
     *    Delays by configurable amount to "slow down" the execution of the JVM
     *
     *  This method MUST be called after each process instruction so that Interrupts
     *  can be safely handled immediately (all running processes stop).  This method
     *  simulates a signal that would direct the state to transition out of the normal process flow.
     */
    public static void checkStatus() 
    {
        
        // decrement the system timer
        JVMRuntime.systemtimer--;

        // don't bother with rest if showOutput is false
        if (processDebug == false)
            return;
        
        if (dumpRegisters == true)
            JVMRuntime.dumpRegisters(regDumpArea);
        
        
        CoreVisualizer.repaintQueues();
        
         
        //debug("synch processMonitor");
        
        // step if so set
        synchronized(processMonitor)
        {
            if( step == true )
                try {
                    processMonitor.wait();
                } catch (InterruptedException e) { }
            else
                // sleep for a bit, slowing down the JVM.  If it is interrupted during the sleep,
                // rethrow it.
                try { Thread.sleep(ProcessManager.delayFactor); } catch (InterruptedException e) { 
                    //System.err.println(processName + " checkstatus interrupted!");
                    
                }
        }
        
    }
    
    /** 
     * If step = false, step is set true and execution waits on the processMonitor.
     * If step = true, the processMonitor is notified, one instruction is executed and we wait again.
     */
    public static void step()
    {
        // Turn on process debug mode automatically.
        processDebug = true;
        
        synchronized( processMonitor )
        {
            if( step == false )
                step = true;
            else
                processMonitor.notify();
        }
    }
    
    /** Sets step to false and allows continuous execution of instructions */
    public static void continueRun()
    {
        synchronized( processMonitor )
        {
            step = false;
            processMonitor.notify();
        }
    }
    
    /** Simple debugging exception mechanism */
    public static final int CLASS_NOT_FOUND = 0;
    public static final int METHOD_NOT_FOUND = 1;
    public static final int MEMORY_ALLOCATION_ERROR = 2;
    public static final int UNHANDLED_OPCODE_EXCEPTION = 3;
    public static final int NOT_IMPLEMENTED = 4;
    public static final int FIELD_NOT_FOUND = 5;
    public static final int METHOD_OR_FIELD_NOT_FOUND = 6;
    public static final int INVALID_FIELD_SIZE = 7;
    public static final int INVALID_ARRAY_TYPE = 8;
    public static final int ELIGIBLE_QUEUE_EMPTY = 9;
    
    public void throwException(int type_)
    {
        switch (type_)
        {
            case CLASS_NOT_FOUND:
                debug("[ClassNotFoundException]", true);
                break;
                
            case METHOD_NOT_FOUND:
                debug("[MethodNotFoundException]", true);
                break;
                
            case MEMORY_ALLOCATION_ERROR:
                debug("[MemoryAllocationError]", true);
                break;
                
            case UNHANDLED_OPCODE_EXCEPTION:
                debug("[UnhandledOpcodeException]", true);
                break;
                
            case NOT_IMPLEMENTED:
                debug("[NotImplementedException]", true);
                break;
                
            case FIELD_NOT_FOUND:
                debug("[FieldNotFoundException]", true);
                break;
                
            case METHOD_OR_FIELD_NOT_FOUND:
                debug("[MethodOrFieldNotFoundException]", true);
                break;
                
            case INVALID_FIELD_SIZE:
                debug("[InvalidFieldSizeException]", true);
                break;
                
            case INVALID_ARRAY_TYPE:
                debug("[InvalidArrayTypeException]", true);
                break;
                
            case ELIGIBLE_QUEUE_EMPTY:
                debug("You idiot.  The eligible queue is empty.  At least one thread must be eligible at all times!");
                break;

            default:
                debug("[Unknown Exception]", true);
                break;
        }
        
        // dump the register set
        JVMRuntime.dumpRegisters();
        
        // halt execution but keep the program running 
        try { while(true) Thread.sleep( 100 ); } catch (InterruptedException e ) { }
              
        //System.exit(1);
    }

    /////////////////////////////////////////////////////////////////////////////
    public void setIdle()
    {
        // Just loop indefinitely
        
        try
        {
            while(true)
                Thread.sleep(100);
        
        } catch (InterruptedException e)
        {
            
        }
    }
    
    /////////////////////////////////////////////////////////////////////////////
    
    public void debug(String msg)
    {
        if( processDebug )
            debug(msg, true);
    }

    public void debug(String msg, boolean outputEnable)
    {
        if( outputEnable )
            System.err.println("<Process " + processName + "> " + msg);
    }

    /////////////////////////////////////////////////////////////////////////////
    
    public String getProcessName()
    {
        return processName;
    }
    
    
    ///////////////////////////////////////////////////////////
    // Test
    
    public static void main(String[] args)
    {
        JVMProcess p = new JVMProcess("1");
        
        try { Thread.sleep( 1000 ); } catch ( InterruptedException e ){ }
        
            
        
        
        //p.startProcess(new WaitMonitor());
        
    }
    
}
