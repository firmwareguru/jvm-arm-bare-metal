/*
 * ConstantFloatInfo.java
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
public class ConstantFloatInfo extends CPInfo {
    
    int theBits;
    
    /** Creates a new instance of ConstantFloatInfo */
    public ConstantFloatInfo(int index_) {
        size = 5;
        name = new String("CONSTANT_Float_info");
        index = index_;
        
        theBits = ((int)data[index + 1] << 24) & 0xFF000000;
        theBits |= ((int)data[index + 2] << 16) & 0x00FF0000;
        theBits |= ((int)data[index + 3] << 8) & 0x0000FF00;
        theBits |= ((int)data[index + 4]) & 0x000000FF;
    }
    
    public Vector<String> displayContents() {
        Vector<String> v = new Vector();
        v.add("CONSTANT_Float_info: " + Float.intBitsToFloat(theBits));
        return v;
    }
    
}
