/*
 * AttributeInfo.java
 *
 * Created on April 11, 2006, 9:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.classanalyzer.attributes;

import emr.classanalyzer.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Vector;
/**
 *
 * @author Ross
 */
public abstract class AttributeInfo implements Displayable {
    
    ConstantPool constantPool;

    DefaultMutableTreeNode node;
    
    static public byte[] data;
    
    int index;  // index is left at the next byte after attribute_length
    
    int attributeIndex; // for display only
    
    int length;
    
    String name;
    int nameIndex;
    
    /** Creates a new instance of AttributeInfo */
    public AttributeInfo(String name_, int index_, DefaultMutableTreeNode node_, ConstantPool cp) {
        Log.event("AttributeInfo init, index = " + index_);
        index = index_;
        constantPool = cp;
        node = node_;
        name = name_;

        node.setUserObject(this);
        
        /* u2 name index
         * u4 attribute length
         * u1 attributedata[length]
         */
        
        
        // lets get the name index
        
        /*
        nameIndex = ((int)data[index] << 8) & 0x0000FF00;
        nameIndex |= ((int)data[index+1]) & 0x000000FF;
        
        // get the name
        name = constantPool.getItem(nameIndex).displayContents().get(0);
         */
        index += 2;
        
        // get the attribute length
        length = ((int)data[index] << 24) & 0xFF000000;
        length |= ((int)data[index+1] << 16) & 0x00FF0000;
        length |= ((int)data[index+2] << 8) & 0x0000FF00;
        length |= ((int)data[index+3]) & 0x000000FF;
        
        index += 4;
        
        

    }

    public void setAttributeIndex(int attributeIndex_) {
        attributeIndex = attributeIndex_;
    }
    
    public int getSize() {
        return 6 + length;
    }
    
    public String toString() {
       return Integer.toString(attributeIndex) + " " + name;
        
    }
    
    
    public abstract Vector<String> displayContents(); /* {
        Vector<String> v = new Vector<String>();
        v.add("Attribute data:\n\n");
        v.add("Name : " + nameIndex + " : " + name);
        return v;
        
    } */
    
}
