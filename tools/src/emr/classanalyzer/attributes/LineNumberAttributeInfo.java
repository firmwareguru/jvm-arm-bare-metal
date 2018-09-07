/*
 * LineNumberAttributeInfo.java
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
public class LineNumberAttributeInfo extends AttributeInfo {
    
    int tableLength;
    int[] startpc;
    int[] linenumber;
    
    /** Creates a new instance of LineNumberAttributeInfo */
    public LineNumberAttributeInfo(String name_, int index_, DefaultMutableTreeNode node_, ConstantPool cp) {
        super(name_, index_, node_, cp);
    
        
        tableLength = ((int)data[index] << 8) & 0x0000FF00;
        tableLength |= ((int)data[index+1]) & 0x000000FF;

        index += 2;
        
        startpc = new int[tableLength];
        linenumber = new int[tableLength];
    
        for(int i = 0; i < tableLength; i++) {
            startpc[i]  = ((int)data[index] << 8) & 0x0000FF00;
            startpc[i] |= ((int)data[index+1]) & 0x000000FF;
            index += 2;
            linenumber[i] = ((int)data[index] << 8) & 0x0000FF00;
            linenumber[i] |= ((int)data[index+1]) & 0x000000FF;
            index += 2;
        }
    
    }
    
    public Vector<String> displayContents() {
        Vector<String> v = new Vector<String>();
        
        v.add("Line Number attribute:\n\n");
        for(int i = 0; i < tableLength; i++) {
            v.add("code index: " + startpc[i] + "\n");
            v.add("   line number: " + linenumber[i] + "\n");
        }
        return v;
         
    }    
     
    
}
