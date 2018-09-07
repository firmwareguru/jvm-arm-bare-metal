/*
 * Log.java
 *
 * Created on April 14, 2006, 6:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.classanalyzer;

import java.util.*;

/**
 *
 * @author Ross
 */
public class Log {

    static final boolean debug = true;
    
    static {
        
        
        
    }
    /** Creates a new instance of Log */
    public Log() {
    }
    
    public static void event(String s) {
        
        Date d;
        
        String time = Calendar.getInstance().getTime().toString();
        if (debug) {
            System.err.println("Log event : <" + time + "> " + s);
        }
    }
}
