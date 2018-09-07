/*
 * ConstantUTF8Info.java
 *
 * Created on March 23, 2006, 5:28 PM
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
public class ConstantUTF8Info extends CPInfo {
    
    private String utfString;
    
    /** Creates a new instance of ConstantUTF8Info */
    public ConstantUTF8Info(int _index) {
        index = _index;
        name = new String("CONSTANT_Utf8");
        
        size = ((int)data[index+1] << 8) & 0x0000FF00;
        size |= ((int)data[index+2]) & 0x000000FF;

        size += 3;
        
        
    }
    
    public Vector<String> displayContents() {

        Vector<String> v = new Vector();
        byte[] s = new byte[size - 3];
        System.arraycopy(data, index + 3, s, 0, size - 3);
        String str = new String(s);
        
        v.add("(UTF8) " + str);
        return v;
    }
    
}
