/*
 * IdleThread.java
 *
 * Created on February 12, 2007, 9:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package system;



/**
 *
 * @author Evan Ross
 */
public class IdleThread extends Thread
{
    //int count = 0;
    
    /** Creates a new instance of IdleThread */
    public IdleThread()
    {
        super();
        setPriority(15);
        setId(32);
    }
    
    public void run()
    {
        int count = 0;
        // increment a counter
        while( true )
        {
            count ++;
            Thread.yield();
        }
    }
    
}
