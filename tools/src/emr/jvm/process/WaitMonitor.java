/*
 * WaitMonitor.java
 *
 * Created on January 3, 2007, 10:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.jvm.process;

/**
 *
 * @author Evan Ross
 */
public class WaitMonitor
{
    /* wait for this many notifications */
    int count;
    
    /** Creates a new instance of WaitMonitor */
    public WaitMonitor()
    {
        count = 1;
    }
    
    public void setCount(int count_)
    {
        count = count_;
    }
    
    public synchronized void processFinished()
    {
        count--;
        if( count == 0 )
        {
            synchronized(this)
            {
                notify();
            }
        }
    }
}
