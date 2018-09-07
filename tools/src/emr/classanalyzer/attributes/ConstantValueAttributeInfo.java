/*
 * ConstantValueAttributeInfo.java
 *
 * Created on April 16, 2006, 8:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.classanalyzer.attributes;
import java.util.Vector;
import emr.classanalyzer.*;
import emr.classanalyzer.structures.*;
import javax.swing.tree.DefaultMutableTreeNode;
/**
 *
 * @author Ross
 */
public class ConstantValueAttributeInfo extends AttributeInfo {
    
    int cvIndex; 
    
    /** Creates a new instance of ConstantValueAttributeInfo */
    public ConstantValueAttributeInfo(String name_, int index_, DefaultMutableTreeNode node_, ConstantPool cp) {
        super(name_, index_, node_, cp);
       
        // parse the remainder of the constantValue attribute.
        // index points to the byte following the attribute_length item.
        
        // u2 constantvalue_index;

        cvIndex = ((int)data[index] << 8) & 0x0000FF00;
        cvIndex |= ((int)data[index+1]) & 0x000000FF;
    
    }
    
    public Vector<String> displayContents() {
        Vector<String> v = new Vector<String>();
        
        CPInfo info = constantPool.getItem(cvIndex);
        
        v.add("Constant value: " + info.displayContents().get(0));
        return v;
         
    }    
    
}
