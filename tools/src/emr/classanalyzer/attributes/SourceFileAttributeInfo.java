/*
 * SourceFileAttributeInfo.java
 *
 * Created on April 16, 2006, 8:30 PM
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
public class SourceFileAttributeInfo extends AttributeInfo {
    
    int sourceFileIndex;
    
    /** Creates a new instance of SourceFileAttributeInfo */
    public SourceFileAttributeInfo(String name_, int index_, DefaultMutableTreeNode node_, ConstantPool cp) {
        super(name_, index_, node_, cp);
        
        sourceFileIndex = ((int)data[index] << 8) & 0x0000FF00;
        sourceFileIndex |= ((int)data[index+1]) & 0x000000FF;
    }
    
    public Vector<String> displayContents() {
        Vector<String> v = new Vector<String>();
        
        v.add("Source file: " + constantPool.getItem(sourceFileIndex).displayContents().get(0));
        return v;
         
    }    
     
    
}
