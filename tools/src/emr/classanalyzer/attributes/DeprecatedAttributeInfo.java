/*
 * DeprecatedAttributeInfo.java
 *
 * Created on April 16, 2006, 8:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.classanalyzer.attributes;
import java.util.Vector;
import emr.classanalyzer.*;
import javax.swing.tree.DefaultMutableTreeNode;
/**
 *
 * @author Ross
 */
public class DeprecatedAttributeInfo extends AttributeInfo {
    
    /** Creates a new instance of DeprecatedAttributeInfo */
    public DeprecatedAttributeInfo(String name_, int index_, DefaultMutableTreeNode node_, ConstantPool cp) {
        super(name_, index_, node_, cp);
    }
    public Vector<String> displayContents() {
        Vector<String> v = new Vector<String>();
        
        v.add("Deprecated attribute");
        return v;
         
    }    
    
}
