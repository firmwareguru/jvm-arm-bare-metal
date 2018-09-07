/*
 * Debug.java
 *
 * Created on September 25, 2006, 10:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.jvm;
import java.io.*;

/**
 *
 * @author Ross
 */
public class Debug {
    
    // An output file
    static PrintWriter debugwriter;
    
    static String debuglogfile = "jvmcore.log";
    
    public static boolean consoleLoggingEnabled = true;
    public static boolean fileLoggingEnabled = false;
    
    static
    {
        try 
        {
            debugwriter = new PrintWriter(new BufferedWriter(new FileWriter(debuglogfile)));
        }
        catch (IOException e)
        {
            System.out.println("Debug initialize failed.");
            System.exit(1);
        }

        System.out.println("Debug initialized:");
        System.out.println("   Console logging: " + consoleLoggingEnabled);
        System.out.println("   File logging: " + fileLoggingEnabled);
        
    }
    
    /** Creates a new instance of Debug */
    public Debug() 
    {   
    }
    
    public static void message(String text_)
    {
        log(text_);
    }
    
    public static void warning(String text_)
    {
        log("Warning | " + text_);
    }
    
    public static void error(String text_)
    {
        log("Error | " + text_);
    }
    
    public static void fatalError(String text_)
    {
        log("Fatal Error | " + text_);
        JVMRuntime.dumpRegisters();
              
        throw new RuntimeException();
        
        /*
        Object o = new Object();
        synchronized(o)
        {
            try {o.wait();} catch (InterruptedException e) { }
            
        }
        System.exit(1);
         */
    }
    
    private static void log(String text_)
    {
        if(consoleLoggingEnabled)
            System.out.println(text_);
        
        if(fileLoggingEnabled)
            debugwriter.println(text_);
        
        
    }
    
    
            
    
}
