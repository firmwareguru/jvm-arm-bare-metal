/*
 * LocalVariableAttributeInfo.java
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
public class LocalVariableAttributeInfo extends AttributeInfo {
    
    int tableLength;
    
    int[] startpc;
    int[] len;
    int[] nameindex;
    int[] descindex;
    int[] idx;
    
    /** Creates a new instance of LocalVariableAttributeInfo */
    public LocalVariableAttributeInfo(String name_, int index_, DefaultMutableTreeNode node_, ConstantPool cp) {
        super(name_, index_, node_, cp);

        tableLength = ((int)data[index] << 8) & 0x0000FF00;
        tableLength |= ((int)data[index+1]) & 0x000000FF;

        index += 2;
        
        startpc = new int[tableLength];
        len = new int[tableLength];
        nameindex = new int[tableLength];
        descindex = new int[tableLength];
        idx = new int[tableLength];
        
        
        for(int i = 0; i < tableLength; i++) {
            startpc[i]  = ((int)data[index] << 8) & 0x0000FF00;
            startpc[i] |= ((int)data[index+1]) & 0x000000FF;
            
            index += 2;
        
            len[i] = ((int)data[index] << 8) & 0x0000FF00;
            len[i] |= ((int)data[index+1]) & 0x000000FF;
            
            index += 2;

            nameindex[i] = ((int)data[index] << 8) & 0x0000FF00;
            nameindex[i] |= ((int)data[index+1]) & 0x000000FF;
            
            index += 2;
            descindex[i] = ((int)data[index] << 8) & 0x0000FF00;
            descindex[i] |= ((int)data[index+1]) & 0x000000FF;
            
            index += 2;
            idx[i] = ((int)data[index] << 8) & 0x0000FF00;
            idx[i] |= ((int)data[index+1]) & 0x000000FF;
            
            index += 2;
            
        }
    }
    
    public Vector<String> displayContents() {
        Vector<String> v = new Vector<String>();
        
        v.add("Local variable attribute\n");
        for(int i = 0; i < tableLength; i++) {
            v.add("-- start pc : " + startpc[i] + "\n");
            v.add("     length : " + len[i] + "\n");
            v.add("   name : " + nameindex[i] + " " + constantPool.getItem(nameindex[i]).displayContents().get(0) + "\n");
            v.add("   desc : " + descindex[i] + " " + constantPool.getItem(descindex[i]).displayContents().get(0) + "\n");
            v.add("      index : " + idx[i] + "\n");
        }
        return v;
         
    }    
     
    
}
