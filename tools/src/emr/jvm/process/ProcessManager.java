/*
 * ProcessManager.java
 *
 * Created on January 1, 2007, 2:20 PM
 *
 * The ProcessManager is a key component of the JVM system.  The Manager takes requests
 * from the Instruction Execution Engine (IEE) and initiates processes to carry out the tasks
 * required to complete the instuction (s).  These processes are, in the Java enivornment,
 * implemented as Threads to as closely mimic the parallel nature of VHDL processes as 
 * possible.
 *
 * All processes (process threads) are atomic.  For each possible request from the IEE,
 * the ProcessManager maintains a list that specifies the order execution of atomic processes 
 * required to carry out a request.  Some atomic processes must execute sequentially due to shared
 * resources or result depenencies, and some may execute in parallel.  This management of processes
 * may be likended to the 'instruction pipeline' of traditional processors.  Optimizing the parallelism
 * of processes will be key to achieving high performance in the finished product.
 *
 * Implementation details.
 * Each process extends class JVMProcess.  The JVMProcess class
 * provides the interface needed by the ProcessManager to start, stop and react to process activities.
 * Each process' code is in the run() method.  Each process uses the common register set in the static
 * JVMRuntime class to use as memory.
 */

package emr.jvm.process;


import emr.jvm.JVMRuntime;
import emr.jvm.visualization.*;

// for testing

import emr.jvm.Timer;

import emr.jvm.memory.MemoryController;
import emr.jvm.memory.MemoryModule;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author Evan Ross
 */
public class ProcessManager extends Thread implements ProcessEnums
{
    
    
    /* A delay factor is set to slow down the JVM to aid in debugging */
    public static int delayFactor = 500; // milliseconds
    
    
    /* The System Timer used for thread scheduling */
    private static Timer systemTimer = new Timer();
    
    /* The IPSUpdater computes instructions per second and displayes them in the corevisualizer */
     
    
    /** Creates a new instance of ProcessManager */
    public ProcessManager()
    {
        // Start any system hardware emmulation classes here
        //systemTimer.start();
        
        new IPSUpdater();
        
    }
    
    /** 
     * The Process Management main execution loop.
     *
     * This is the essentially the "main()" function.
     *
     */
    @Override
    public void run()
    {
        
        // Startup the JVM!
        InstructionHandlers.Startup();
        
        // run forever
        while (true)
        {
            // 1.  If PCW is not null:
            //    a) if PCW & 0xff non-zero, run that process
            //    b) if PCW & 0xffff0000 non-zero, schedule that interrupt
            // 2.  Fetch an instruction and execute it.
            
            if ((JVMRuntime.PCW & ~JVMRuntime.PCW_FLAGS_MASK) != 0)
            {
                // scheduler processes
                if ((JVMRuntime.PCW & JVMRuntime.PCW_PROCESS_MASK) != 0)
                {
                    switch (JVMRuntime.PCW & JVMRuntime.PCW_PROCESS_MASK)
                    {
                        case INVOKE_SCHEDULER:
                            debug("Invoking Scheduler");
                            InstructionHandlers.Scheduler();
                            break;
                        case THREAD_SLEEP:
                            debug("Invoking ThreadSleep");
                            InstructionHandlers.ThreadSleep();
                            break;
                        default:
                            debug("Invalid PCW!");
                            System.exit(1);
                    }
                    setPCW(0);
                }
                
                // interrupts
                else if ((JVMRuntime.PCW & JVMRuntime.PCW_INTERRUPT_MASK) != 0)
                {
                    debug("Interrupt!");
                }
                else
                {
                    debug("Invalid PCW!");
                    System.exit(1);                            
                }                    
            }
            else
            {
                // Execute an instruction
                ProcessList.processList[PROCESS_IEE].runProcess();
            }               
        }           
    }
    
    /**
     *  Sets the Process bits of the PCW
     */
    public static void setPCW(int process_)
    {
        /*
        // clear bits 7 - 31
        JVMRuntime.PCW &= ~0xFFFFFF80;
        
        // set bits 7 - 31
        JVMRuntime.PCW |= ( process_ << 7 );
         */
        
        // clear bits 0 - 7
        JVMRuntime.PCW &= ~0x000000ff;
        
        // set bits 0 - 7
        JVMRuntime.PCW |= ( process_ & 0xff );
    }
    
    /**
     *  Sets the interrupt vector bits of the PCW
     */
    public static void setPCWInt(int vector_)
    {
        /*
        // lower 4 bits only
        vector_ &= 0xf;
        
        // shift up 3 to align with PCW bits 3-6
        vector_ <<= 3;
        
        // clear PCW bits 3-6
        JVMRuntime.PCW &= 0xffffff87;
        
        // set bits 3-6
        JVMRuntime.PCW |= vector_;
         */
        
    }
    
    public static int getInterruptMask()
    {
        /*
        // shift out the state bits (0-2)
        int mask = JVMRuntime.PCW >> 3;
        
        // clear the process request bits
        return (mask & 0xf);
         */
        
         return 0;
    }
    
    //////////////////////////////////////////////////////////////////////////////////
    
    /** Debugging: Idle the JVM so that the memory viewers remain active */
//    public static void setIdle()
//    {
//        setPCW( 9 );
//    }
    
    /** Steps through the processes */
    public static void step()
    {
        JVMProcess.step();
    }
    
    /** Stops stepping
     */
    public static void continueRun()
    {
        JVMProcess.continueRun();
    }
     
   
    public static boolean debug = true;
    public static void debug(String msg)
    {
        if(debug)
            System.err.println("<ProcessManager> " + msg);
       
    }
    

//////////////////////////////////////////////////////////////////////////////////////////////////
// Startup
//////////////////////////////////////////////////////////////////////////////////////////////////
    
    /** Starts the ProcessManager */
    public static void main(String[] args)
    {
        runJVM();
    }
    
    // starts the JVM 
    public static void runJVM()
    {
        // Initialize the simulation environment.
        initializeJVM();
        
        // Start an instance of the JVM
        ProcessManager m = new ProcessManager();
        m.start();
    }
    
    static int baseX = 100;
    //static int baseX = 1681;  // right screen
    static int windowHeight = 1050;
    
    public static void initializeJVM()
    {
        //try {
            // This stuff should be executed on Swing's dispatch thread.
        //    SwingUtilities.invokeAndWait(new Runnable() {

       //         public void run() {
                    CoreVisualizer cv = new CoreVisualizer();
                    // set the location
                    cv.setLocation(baseX, 0);
                    cv.setVisible(true);
                    JVMProcess.regDumpArea = cv.getRegisterArea();
                    // Initialize the VM's memory controller.  All NVM, RAM and peripheral
                    // accesses go through this.
                    MemoryController.initialize();
                    // position the windows
                    MemoryModule nvmModule = (MemoryModule) MemoryController.getInterface("NVM");
                    MemoryModule ramModule = (MemoryModule) MemoryController.getInterface("RAM");
                    nvmModule.getVisualizer().setLocation(baseX + cv.getWidth(), 0);
                    ramModule.getVisualizer().setLocation(baseX + cv.getWidth() + nvmModule.getVisualizer().getWidth(), 0);
                    ramModule.getVisualizer().setSize(ramModule.getVisualizer().getWidth(), windowHeight);
                    nvmModule.getVisualizer().setSize(nvmModule.getVisualizer().getWidth(), windowHeight);
                    nvmModule.getVisualizer().pack();
                    //ramModule.getVisualizer().pack();
                    nvmModule.getVisualizer().setVisible(true);
                    ramModule.getVisualizer().setVisible(true);
                //}
        //    });
        //} catch (InterruptedException ex) {
        //    Logger.getLogger(ProcessManager.class.getName()).log(Level.SEVERE, null, ex);
        //} catch (InvocationTargetException ex) {
        //    Logger.getLogger(ProcessManager.class.getName()).log(Level.SEVERE, null, ex);
        //}
    }
    
   
    
}
