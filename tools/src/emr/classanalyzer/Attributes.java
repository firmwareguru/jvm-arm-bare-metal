/*
 * Attributes.java
 *
 * Created on March 24, 2006, 6:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.classanalyzer;

import emr.classanalyzer.attributes.*;
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
public class Attributes implements Displayable {
    
    byte[] data;
    
    ConstantPool constantPool;
    
    DefaultMutableTreeNode node;

    int index;
    int startIndex; // for size calculations;
    
    int count;
    
    Vector <AttributeInfo> attributes;

    
    
    String name;

    /** Creates a new instance of Attributes */
    public Attributes(byte[] d, DefaultMutableTreeNode node_, ConstantPool cp) {
        Log.event("Attributes init");
        data = d;
        node = node_;
        node.setUserObject(this);
        constantPool = cp;
        //index = index_;
    }
    
    public void parseAttributes(int index_) {
        Log.event("Parse Attributes from index " + index_);
        index = index_;
        startIndex = index_;

        count = ((int)data[index_] << 8) & 0x0000FF00;
        count |= ((int)data[index_+1]) & 0x000000FF;
        
        index += 2;
        
        AttributeInfo.data = data;
        
        attributes = new Vector<AttributeInfo>();
        
        for(int i = 0; i < (count); i++) {
            
            int nameIndex;
            nameIndex = ((int)data[index] << 8) & 0x0000FF00;
            nameIndex |= ((int)data[index+1]) & 0x000000FF;
        
             // get the name
            String name = constantPool.getItem(nameIndex).displayContents().get(0);
            Log.event("Attributes parsing new attribute : " + name);
            
            AttributeInfo info = null;
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode();
            node.add(childNode); // add a new node for each FieldInfo
              
            if (name.contains("Code")) // Code attribute
            {
                info = new CodeAttributeInfo(name, index, childNode, constantPool);                
            }
            else if (name.contains("ConstantValue"))
            {
                info = new ConstantValueAttributeInfo(name, index, childNode, constantPool);                
            }  
            else if (name.contains("Exceptions"))
            {
                info = new ExceptionsAttributeInfo(name, index, childNode, constantPool);                
            } 
            else if (name.contains("SourceFile"))
            {
                info = new SourceFileAttributeInfo(name, index, childNode, constantPool);                
            }  
            else if (name.contains("LineNumberTable"))
            {
                info = new LineNumberAttributeInfo(name, index, childNode, constantPool);                
            }  
            else if (name.contains("LocalVariableTable"))
            {
                info = new LocalVariableAttributeInfo(name, index, childNode, constantPool);                
            }  
            else // unhandled
            {
                info = new UnknownAttributeInfo(name, index, childNode, constantPool);                
                
            }
            
            info.setAttributeIndex(i);
            attributes.add(info);
            
            index += info.getSize();
 
        }
        
                
    }
  
    public int getSize() {
        return index - startIndex;
    }
    
    public int getNextIndex() {
        Log.event("Attributes returning next index " + index);
        return index;
    }
    
    public int getCount() {
        return count;
    }
    
    public String toString() {
        return "Attributes";
                
    }
    
    
    public Vector<String> displayContents() {
        Vector<String> v = new Vector();
        v.add("Attributes\n");
        return v;
    }
           
    
}
