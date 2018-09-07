/*
 * Fields.java
 *
 * Created on March 24, 2006, 6:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.classanalyzer;

import emr.classanalyzer.structures.*;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.util.Vector;
/**
 *
 * @author Ross
 */
public class Fields implements Displayable {
    
    byte[] data;
    
    int index;
    int startIndex; // get size
    
    int count;
    
    Vector<FieldInfo> fields;

    ConstantPool constantPool;
    
    DefaultMutableTreeNode node;    

    
    
    /** Creates a new instance of Fields */
    public Fields(byte[] d, DefaultMutableTreeNode node_, ConstantPool cp) {
        Log.event("Fields init");
        data = d;
        node_.setUserObject(this);
        constantPool = cp;
        node = node_;
    }
    
    public int getNextIndex() {
        System.out.println("Fields returning next index : " + index);
        return index;
    }
    
    public void parseFields(int index_) {
        Log.event("Parse Fields from index " + index_);
        // index must point to start of interfaces_count item
        startIndex = index_;
        index = index_;
        
        count = ((int)data[index_] << 8) & 0x0000FF00;
        count |= ((int)data[index_+1]) & 0x000000FF;
        
        index += 2;
        
        FieldInfo.data = data;
        
        fields = new Vector<FieldInfo>();
        
        for(int i = 0; i < (count); i++) {
            
            
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode();
            node.add(childNode); // add a new node for each FieldInfo
            
            FieldInfo info = new FieldInfo(index, childNode, constantPool);
            info.setFieldIndex(i);
            fields.add(info);
            
            index += info.getSize();
 
        }

        
        
        
        
    }
    
    public int getCount() {
        return count;
    }
    
    public int getSize() {
        return index - startIndex;
    }
    
    //-------------------------------------
    
    public String toString() {
        return "Fields";
                
    }
    
    public Vector<String> displayContents() {
       Vector<String> v = new Vector();
       v.add("Number of Field items: " + Integer.toString(count) + "\n");
       return v;
    }
  
    
}
