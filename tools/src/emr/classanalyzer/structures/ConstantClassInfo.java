/*
 * ConstantClassInfo.java
 *
 * Created on March 23, 2006, 5:24 PM
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
public class ConstantClassInfo extends CPInfo {
    
    ConstantPool constantPool; // reference to the ConstantPool class
    /** Creates a new instance of ConstantClassInfo */
    
    int itemIndex; // pointer to another entry in to the CP
    
    public ConstantClassInfo(ConstantPool _constantPool, int _index) {
        index = _index;
        constantPool = _constantPool;
        
        size = 3;
        name = new String("CONSTANT_Class_Info");
        
        itemIndex = ((int)data[index+1] << 8) & 0x0000FF00;
        itemIndex |= ((int)data[index+2]) & 0x000000FF;
        
    }
    public Vector<String> displayContents() {
        Vector<String> v = new Vector();
        // this item contains an index into the constant pool to a Utf8 structure indicating the class name.
        
        // get the CPInfo structure pointed to by this object
        int item;
        
        CPInfo info = constantPool.getItem(itemIndex);
        
        v.add(itemIndex + " : ");
        Vector<String> x = info.displayContents();
        for(int i = 0; i < x.size(); i++)
            v.add(x.get(i));
        
        
        return v;
    }
    
    
}
