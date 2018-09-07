/*
 * ConstantDoubleInfo.java
 *
 * Created on March 23, 2006, 5:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.classanalyzer.structures;
import java.util.Vector;

/**
 *
 * @author Ross
 */
public class ConstantDoubleInfo extends CPInfo {
    
    long theBits;
    
    /** Creates a new instance of ConstantDoubleInfo */
    public ConstantDoubleInfo(int index_) {
        size = 9;
        name = new String("CONSTANT_Double");
        index = index_;

        theBits  = ((long)data[index + 1] << 56) & 0xFF00000000000000L;
        theBits |= ((long)data[index + 2] << 48) & 0x00FF000000000000L;
        theBits |= ((long)data[index + 3] << 40) & 0x0000FF0000000000L;
        theBits |= ((long)data[index + 4] << 32) & 0x000000FF00000000L;
        theBits |= ((long)data[index + 5] << 24) & 0x00000000FF000000L;
        theBits |= ((long)data[index + 6] << 16) & 0x0000000000FF0000L;
        theBits |= ((long)data[index + 7] << 8)  & 0x000000000000FF00L;
        theBits |= ((long)data[index + 8])       & 0x00000000000000FFL;
        
    
    }

    public Vector<String> displayContents() {
        Vector<String> v = new Vector();
        v.add("CONSTANT_Double_info: " + Double.longBitsToDouble(theBits));
        return v;
    }

}
