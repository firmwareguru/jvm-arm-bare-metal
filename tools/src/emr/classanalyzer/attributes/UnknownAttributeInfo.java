/*
 * UnknownAttributeInfo.java
 *
 * Created on April 17, 2006, 6:33 PM
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
public class UnknownAttributeInfo extends AttributeInfo {
    
    /** Creates a new instance of UnknownAttributeInfo */
    public UnknownAttributeInfo(String name_, int index_, DefaultMutableTreeNode node_, ConstantPool cp) {
        super(name_, index_, node_, cp);
    }
    
    public Vector<String>  displayContents() {
        Vector<String> v = new Vector();
        v.add("Unknown Attribute");
        return v;
    }
    
 
     
    
}
