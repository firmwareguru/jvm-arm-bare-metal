/*
 * ConstantNameAndTypeInfo.java
 *
 * Created on March 23, 2006, 5:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.classanalyzer.structures;
import java.util.Vector;
import emr.classanalyzer.*;

/**
 *
 * @author Ross
 */

public class ConstantNameAndTypeInfo extends CPInfo {

    
    String names;
    int nameIndex;
    
    String descriptor;
    int descriptorIndex;
    
    ConstantPool constantPool;
    int index;
    
    /** Creates a new instance of ConstantNameAndTypeInfo */
    public ConstantNameAndTypeInfo(ConstantPool constantPool_, int index_) {
        size = 5;
        name = new String("CONSTANT_NameAndType");
        index = index_ + 1; // skip tag
        constantPool = constantPool_;
        
    }
    public Vector<String> displayContents() {
        Vector<String> v = new Vector();
                
        Log.event("index " + index);
        // name
        nameIndex = ((int)data[index] << 8) & 0x0000FF00;
        nameIndex |= ((int)data[index+1]) & 0x000000FF;
        Log.event("name index " + nameIndex +   " " + index);
        names = constantPool.getItem(nameIndex).displayContents().get(0);
        
        // descriptor
        descriptorIndex = ((int)data[index+2] << 8) & 0x0000FF00;
        descriptorIndex |= ((int)data[index+3]) & 0x000000FF;
        Log.event("descriptor index " + descriptorIndex + " " +  index);
        descriptor = constantPool.getItem(descriptorIndex).displayContents().get(0);
        
        
        v.add("Name : " + nameIndex + " : " + names + "\n");
        v.add("Descriptor : " + descriptorIndex + " : " + descriptor + "\n");
        return v;
    }
    
}
