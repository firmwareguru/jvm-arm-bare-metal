/*
 * CountingThread.java
 *
 * Created on October 21, 2006, 5:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package system.test;

/**
 *
 * @author Ross
 */
public class CountingThread extends Thread {
    
    private int count;
    
    private int sleepInterval;
    
    /** Creates a new instance of CountingThread */
    public CountingThread(int interval_, int id_, int priority_) 
    {
        // priority, id
        super();
        setPriority(priority_);
        setId(id_);
        
        sleepInterval = interval_;
        
        //count = 1;
    }
    
    public void run()
    {
        int c = 0;
        int interval = sleepInterval;
        
        while(true)
        {
            c += 2;
            Thread.sleep(interval);
            //Thread.yield();
        }
    }
    
}
