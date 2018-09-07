/*
 * ConstantStringInfo.java
 *
 * Created on March 23, 2006, 5:26 PM
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
public class ConstantStringInfo extends CPInfo {
    ConstantPool constantPool; // reference to the ConstantPool class

    int stringIndex;
    
    /** Creates a new instance of ConstantStringInfo */
    public ConstantStringInfo(ConstantPool _constantPool, int _index) {
        index = _index;
        size = 3;
        constantPool = _constantPool;
        name = new String("CONSTANT_String");
        stringIndex = ((int)data[index+1] << 8) & 0x0000FF00;
        stringIndex |= ((int)data[index+2]) & 0x000000FF;
    }
    public Vector<String> displayContents() {
        Vector<String> v = new Vector();
        CPInfo stringInfo = constantPool.getItem(stringIndex);
        
        Vector<String> x = stringInfo.displayContents();
        for(int i = 0; i < x.size(); i++)
            v.add(x.get(i));

        return v;
    }
    
}
