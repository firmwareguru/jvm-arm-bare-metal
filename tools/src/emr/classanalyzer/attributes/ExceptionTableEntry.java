/*
 * ExceptionTable.java
 *
 * Created on April 25, 2006, 6:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.classanalyzer.attributes;

import emr.classanalyzer.*;
import java.util.Vector;

/**
 *
 * @author Ross
 */
public class ExceptionTableEntry implements Displayable {
    
    int startpc;
    int endpc;
    int handlerpc;
    int catchType;
    
    ConstantPool cp;
    
    /** Creates a new instance of ExceptionTable */
    public ExceptionTableEntry(byte[] data, int index, ConstantPool cp_) {
        cp = cp_;
        // exception table entry
        // u2 start_pc
        // u2 end_pc
        // u2 handler_pc
        // u2 catch_type
        
        startpc =  ((int)data[index] << 8) & 0x0000FF00;
        startpc |= ((int)data[index + 1])  & 0x000000FF;

        endpc =  ((int)data[index + 2] << 8) & 0x0000FF00;
        endpc |= ((int)data[index + 3])      & 0x000000FF;

        handlerpc =  ((int)data[index + 4] << 8) & 0x0000FF00;
        handlerpc |= ((int)data[index + 5])      & 0x000000FF;

        catchType =  ((int)data[index + 6] << 8) & 0x0000FF00;
        catchType |= ((int)data[index + 7])      & 0x000000FF;
        
        
        
    }
    
    public Vector<String> displayContents() {
        Vector<String> v = new Vector();
        v.add("Exception Table Entry:\n\n");
        v.add("start pc: " + startpc + "\n");
        v.add("end pc: " + endpc + "\n");
        v.add("handler pc: " + handlerpc + "\n");
        v.add("catch type: " + catchType + "\n");
        v.addAll(cp.getItem(catchType).displayContents());
        
        return v;
    }
    
    public String toString() {
        return "ExceptionTableEntry";
    }
            
         
    
}
