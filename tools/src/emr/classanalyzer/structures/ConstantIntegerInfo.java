/*
 * ConstantIntegerInfo.java
 *
 * Created on March 23, 2006, 5:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.classanalyzer.structures;
import emr.classanalyzer.*;
import java.util.Vector;

/**
 *
 * @author Ross
 */
public class ConstantIntegerInfo extends CPInfo {
    
    int theInt;
    
    
    /** Creates a new instance of ConstantIntegerInfo */
    public ConstantIntegerInfo(int index_) {
        size = 5;
        name = new String("CONSTANT_Integer");
        index = index_;

        theInt = ((int)data[index + 1] << 24) & 0xFF000000;
        theInt |= ((int)data[index + 2] << 16) & 0x00FF0000;
        theInt |= ((int)data[index + 3] << 8) & 0x0000FF00;
        theInt |= ((int)data[index + 4]) & 0x000000FF;
               
    }
    
    public Vector<String> displayContents() {
        Vector<String> v = new Vector();
        v.add("CONSTANT_Integer_info: " + theInt);
        return v;
    }
    
}
