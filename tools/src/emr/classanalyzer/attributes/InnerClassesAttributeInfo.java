/*
 * InnerClassesAttributeInfo.java
 *
 * Created on April 16, 2006, 8:36 PM
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
public class InnerClassesAttributeInfo extends AttributeInfo {
    
    /** Creates a new instance of InnerClassesAttributeInfo */
    public InnerClassesAttributeInfo(String name_, int index_, DefaultMutableTreeNode node_, ConstantPool cp) {
        super(name_, index_, node_, cp);
    }

    public Vector<String> displayContents() {
        Vector<String> v = new Vector<String>();
        
        v.add("Inner classes attribute");
        return v;
         
    }    
     
}
