/*
 * Thread.java
 *
 * Created on September 20, 2006, 10:08 PM
 *
 * The Thread class as implemented here is designed to closely mimic the interface of the
 * real Java Thread class.
 *
 * Note that the location of the run() method in the constant pool is closely tied to
 * the lang.threadstart instruction.  This instruction sets the INDEX register to point
 * to Run()'s MethodRefInfo structure in class Thread.  Run() need only be fixed in the Thread class..
 * subclasses have their Runs looked up virtually.
 *
 */

package java.lang;

/**
 *
 * @author Ross
 */
public class Thread {

    /////////////////////////////////////////////////////////////////////////
    // The following fields are required for the JVM to function.
    /////////////////////////////////////////////////////////////////////////
    
    private int next;       /* System pointer to next thread is list */
    private int priority;   /* Lower is higher thread priority */
    private int wakeuptime; /* Timer value after which this thread should become active */
    
    private int id;        /* ID of thread.  This is field is for the benefit of Thread users */
    
    /* 
     * CPU Registers composing this Thread's context.  When a context switch occurs,
     * system registers are copied to or from these memory locations.
     */
  
    //private int reg1; // pc
    //private int reg2; // currentclass;
    //private int reg3; // stackpointer
    //private int reg4; // currentframe
    
    // This is the only context value now.  The rest of the above are stored in the frame.
    private int currentframe; 
    
    /////////////////////////////////////////////////////////////////////
    // The following fields do are not used by the JVM
    /////////////////////////////////////////////////////////////////////
    
    
    
    /** Creates a new instance of Thread */
    /*
    public Thread(int priority_, int id_)
    {
        priority = priority_;

        id = id_;
    }
    */

    public Thread()
    {
        priority = 1;
        id = 1;
    }

    public Thread(Runnable target)
    {

    }
    
    /** Does nothing */
    public void run()
    {
       while(true)
       {
           sleep(4000);
       }
    }
    
    public void setPriority(int priority_)
    {
        priority = priority_;
    }
    
    public void setId(int id_)
    {
        id = id_;
    }
    
    /** Start the referenced Thread */
    public native void start();
    
    /** Delay the current Thread */
    public static native void sleep(int delay);
    
    /** Yield the current thread to other eligible Threads */
    public static native void yield();
   
}
