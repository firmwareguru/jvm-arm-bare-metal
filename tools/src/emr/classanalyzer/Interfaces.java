/*
 * Interfaces.java
 *
 * Created on March 24, 2006, 6:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.classanalyzer;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.util.Vector;
import emr.classanalyzer.structures.*;
/**
 *
 * @author Ross
 */
public class Interfaces implements Displayable {
    
    byte[] data;
    
    int count; 
    
    int startIndex;
    
    int index;
    
    ConstantPool constantPool;
    
    /** Creates a new instance of Interfaces */
    public Interfaces(byte[] d, DefaultMutableTreeNode node, ConstantPool cp) {
        Log.event("Interfaces init");
        data = d;
        node.setUserObject(this);
        
        constantPool = cp;
        
        
    }
    
    public void parseInterfaces(int _index) {
        // index must point to start of interfaces_count item
        Log.event("Parse Interfaces from index " + _index);
        index = _index;
        startIndex = _index;
        count = ((int)data[_index] << 8) & 0x0000FF00;
        count |= ((int)data[_index+1]) & 0x000000FF;
        
        index += 2; // move it to start of data

        
        
    }
    
    public int getCount() {
        return count;
    }
    
    
    public int getNextIndex() {
        int newIndex = startIndex + 2 + count * 2;
        System.out.println("Interfaces returning next index : " + newIndex);
        return newIndex; // 2 bytes per item in array
    }
    
    public int getInterfacesCount() {
        return count;
    }
    
    public int getSize() {
        return 2 + count * 2;
    }
  
    //----------------------------------------
    
    public String toString() {
        return "Interfaces";
                
    }
    public Vector<String> displayContents() {
        Vector<String> v = new Vector();
        v.add("Number of Interface items: " + count + "\n\n");
        
        for(int i = index; i < index + (count * 2); i += 2) {
            int idx;
            idx = ((int)data[i] << 8) & 0x0000FF00;
            idx |= ((int)data[i+1]) & 0x000000FF;
            System.out.println(i + " " + idx);
            v.add("constant pool index : " + idx + "\n");
            v.add("   " + constantPool.getItem(idx).displayContents().get(0) + "\n");
        }
        
        return v;
    }
      
}
