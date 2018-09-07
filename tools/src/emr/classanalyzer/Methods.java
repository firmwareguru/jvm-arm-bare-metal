/*
 * Methods.java
 *
 * Created on March 24, 2006, 6:44 PM
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
import emr.classanalyzer.structures.*;
import java.util.Vector;
/**
 *
 * @author Ross
 */
public class Methods implements Displayable {
    
    byte[] data;
    
    DefaultMutableTreeNode node;
    
    ConstantPool constantPool;
    
    int index;
    int startIndex;
    
    int count;
    
    Vector<MethodInfo> methods = new Vector();
    
    /** Creates a new instance of Methods */
    public Methods(byte[] d, DefaultMutableTreeNode node_, ConstantPool cp) {
        Log.event("Methods init");
        node = node_;
        data = d;
        node.setUserObject(this);
        constantPool = cp;
    }
    
    public void parseMethods(int index_) {
        Log.event("Parse Methods from index " + index_);
        index =  index_;
        startIndex = index_;
        
        count = ((int)data[index_] << 8) & 0x0000FF00;
        count |= ((int)data[index_+1]) & 0x000000FF;
        
        index += 2;
        
        MethodInfo.data = data;
        
        methods = new Vector<MethodInfo>();
        
        for(int i = 0; i < (count); i++) {
            
            
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode();
            node.add(childNode); // add a new node for each FieldInfo
            
            MethodInfo info = new MethodInfo(index, childNode, constantPool);
            info.setMethodIndex(i);
            methods.add(info);
            
            index += info.getSize();
 
        }
        
    }
    
    public int getNextIndex() {
        System.out.println("Methods returning next index : " + index);
        return index;
    }

    public int getCount() {
        return count;
    }

    public int getSize() {
        return index - startIndex;
    }
    
    public String toString() {
        return "Methods";
                
    }

    public Vector<String> displayContents() {
       Vector<String> v = new Vector();
       v.add("Number of Method items: " + Integer.toString(count) + "\n");
       return v;
    }
      
    
}
