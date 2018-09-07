/*
 * Timer.java
 *
 * Created on October 19, 2006, 8:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.jvm;

import java.util.*;
import emr.jvm.memory.*;
/**
 *
 * @author Ross
 */
public class Timer extends Thread 
{
    // the timer's resolution
    private static final int timerTick = 10;
    
    // the timer
   // private int time = Integer.MAX_VALUE; // initialize to largest value (counts down)
    
    public void run()
    {
        JVMRuntime.systemtimer = Integer.MAX_VALUE;
        while(true)
        {
            try { sleep(timerTick); } catch (InterruptedException e) { }
            // every 10ms.
            //setTimer(time - timerTick);
            
            // decrement timer in ticks
            JVMRuntime.systemtimer--;
        }
    }
    
    public int getTime()
    {
        return JVMRuntime.systemtimer;
        //return (int) Calendar.getInstance().getTimeInMillis();
    }
    
    public void resetTimer()
    {
        setTimer(Integer.MAX_VALUE);
    }
    
    synchronized private void setTimer(int time_)
    {
        JVMRuntime.systemtimer = time_;
    }
    
}
