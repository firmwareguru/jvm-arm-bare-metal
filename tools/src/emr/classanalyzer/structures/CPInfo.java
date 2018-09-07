/*
 * CPInfo.java
 *
 * Created on March 23, 2006, 5:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.classanalyzer.structures;
import emr.classanalyzer.Displayable;
import java.util.*;

/**
 *
 * @author Ross
 */

public abstract class CPInfo implements Displayable {
    
    protected int index; // starting index into the data array

    static public byte[] data; // class file data array
    
    protected int size;  // specified by subclass.
    
    protected String name;
    
    private int poolIndex;  // for display purposes
    
    /** Creates a new instance of CPInfo */
    
    public CPInfo() {
        
    }
    
    public void setPoolIndex(int _poolIndex) {
        poolIndex = _poolIndex;
    }
   
    public abstract Vector<String> displayContents(); 
       
    
    
    public int getSize() {
        return size;
    }
    
    public String getName() {
        return name;
    }
    
    public String toString() {
        return Integer.toString(poolIndex) + " " + name;
    }
    
}
