/*
 * ContantMethodRefInfo.java
 *
 * Created on March 23, 2006, 5:25 PM
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
public class ConstantMethodRefInfo extends CPInfo {
    int classIndex;
    int nameTypeIndex;
    ConstantPool constantPool; // reference to the ConstantPool class
    
    /** Creates a new instance of ContantMethodRefInfo */
    public ConstantMethodRefInfo(ConstantPool _constantPool, int _index) {
        index = _index;
        constantPool = _constantPool;
        size = 5;
        name = new String("CONSTANT_Methodref");
 
        classIndex = ((int)data[index+1] << 8) & 0x0000FF00;
        classIndex |= ((int)data[index+2]) & 0x000000FF;
        
        nameTypeIndex = ((int)data[index+3] << 8) & 0x0000FF00;
        nameTypeIndex |= ((int)data[index+4]) & 0x000000FF;
    
    }
    public Vector<String> displayContents() {
        Vector<String> v = new Vector();

        v.add("Class Index: " + classIndex + "\n");
       
        CPInfo classInfo = constantPool.getItem(classIndex);
        
        Vector<String> x = classInfo.displayContents();
        for(int i = 0; i < x.size(); i++)
            v.add(x.get(i));
        v.add("\n\n");

        v.add("Name and Type Index: " + nameTypeIndex + "\n");
        CPInfo nameTypeInfo = constantPool.getItem(nameTypeIndex);
        x = nameTypeInfo.displayContents();
        for(int i = 0; i < x.size(); i++)
            v.add(x.get(i));
          
        
        v.add("\n");
        return v;
    }
    
}
