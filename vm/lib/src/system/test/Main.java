/*
 * Main.java
 *
 * Created on October 6, 2006, 11:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package system.test;

/**
 *
 * @author Ross
 *
 * Notes.
 *
 * 1.  Must explicity extend emr.jvm.lang.Object.
 *     The linktable builder finds this class and removes the
 *     reference to java.lang.Object.
 */
public class Main {
    
    public int field1;
    public int field2;
    
    public long field3;
    
    /** Creates a new instance of Main */
    public Main() 
    {
        field1 = 0xaa;
        field2 = 0xbb;
        field3 = 0xcc;
        
    }
    
    public int doSomething(int x)
    {
        //try 
        {
            return x * 2;
        }
        //catch (Exception e)
        //{
          //  return 0;
        //}
    }
    
    /** Main entry point into the application */
    public static void main(String[] args)
    {
        
        Main m1 = new Main();
        Main m2 = new Main();
        m2.field2 = 0xcc;
        
        //emr.jvm.lang.Thread testThread = new CountingThread();
        //testThread.start();
        
        Arrays a = new Arrays();
        
        // local thread
        for(int i = 0; i < 50; i++ )
        {
            m2.field1 = i;
            Thread.sleep(1000);
        }
        
        
      
                
    }
    
}
