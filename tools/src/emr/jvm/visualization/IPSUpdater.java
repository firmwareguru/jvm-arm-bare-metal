/*
 * IPSUpdater.java
 *
 * Created on April 22, 2007, 7:06 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.jvm.visualization;

/**
 *
 * @author Evan Ross
 */
public class IPSUpdater extends Thread
{
    private static final int averageTime = 1000; // average over 1 seconds
    
    private static int instructionCount = 0;
    
    static
    {
        new IPSUpdater().start();
    }
    
    public void run()
    {
        // compute average instructions per averageTime
        
        double ips = 4.0;
        
        while(true)
        {
            try { Thread.sleep(averageTime); } catch (InterruptedException e) { }
            ips = (double) instructionCount / averageTime * 1000.0;
            //System.err.println("IPS: " + ips + " " + instructionCount );
            CoreVisualizer.setIPSField( ips );
            
            setInstructionCount(0);
            
        }
        
    }
    
    public static synchronized void setInstructionCount(int i)
    {
        instructionCount = i;
    }
    
    //public static synchronized void incInstructionCount()
    public static void incInstructionCount()  // this gets me about 20,000 more IPS by not using synchronized.
    {
        instructionCount++;
    }
    
}
