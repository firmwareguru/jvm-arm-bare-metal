/*
 * ConstantPool.java
 *
 * Created on March 24, 2006, 6:36 PM
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
public class ConstantPool implements Displayable {
    
    byte[] data;
    
    DefaultMutableTreeNode node;
    
    int count = 0;
    int index;
    
    CPInfo[] structures;
    
    int startIndex; // for size calculation
    
    /** Creates a new instance of ConstantPool */
    public ConstantPool(byte[] data_, DefaultMutableTreeNode node_) {
        Log.event("Constant pool init");
        node = node_;
        data = data_;
        node.setUserObject(this);
    }
    
    public void parsePool(int index_) {
        Log.event("Parse Constant pool from index " + index_);
        index = index_;
        startIndex = index_;
        
        count = ((int)data[index] << 8) & 0x0000FF00;
        count |= ((int)data[index+1]) & 0x000000FF;
        
        index += 2;
        
        CPInfo.data = data; // set the data item once (static).
        
        structures = new CPInfo[count];
        CPInfo structure = null;
        byte tag;
        
        for(int i = 0; i < (count - 1); i++) {
            tag = data[index];
            System.out.println("i: " + i + " tag : " + tag);
            switch (tag) {
                case 1:
                    structure = new ConstantUTF8Info(index);
                    break;
                case 3:
                    structure = new ConstantIntegerInfo(index);
                    break;
                case 4:
                    structure = new ConstantFloatInfo(index);
                    break;
                case 5:
                    structure = new ConstantLongInfo(index);
                    break;
                case 6:
                    structure = new ConstantDoubleInfo(index);
                    break;
                case 7:
                    structure = new ConstantClassInfo(this, index);
                    break;
                case 8:
                    structure = new ConstantStringInfo(this, index);
                    break;
                case 9:
                    structure = new ConstantFieldRefInfo(this, index);
                    break;
                case 10:
                    structure = new ConstantMethodRefInfo(this, index);
                    break;
                case 11:
                    structure = new ConstantInterfaceMethodRefInfo(this, index);
                    break;
                case 12:
                    structure = new ConstantNameAndTypeInfo(this, index);
                    break;
                default:
                    System.out.println("error resolving tag : " + tag);
                    break;
            }  
            
            structure.setPoolIndex(i + 1);
            structures[i] = structure;
            
            // now some funky stuff for longs or doubles 'cause they "take up" two entries in the CP table
            if(tag == 5 || tag == 6)
                i++;
            
            // add each structure as a node to the tree
            node.add(new DefaultMutableTreeNode(structure));
            
            // increment the index to the 'tag' of the next structure
            index += structure.getSize();
                   
            //System.out.println(i + " " + index +  " " + structure.getSize() + " " + structure.getName());
        }

    }
    
    public CPInfo getItem(int itemIndex) {
        // since the pool is indexed from 1 to count-1, 1 must be subtracted to access the element in the array.
        itemIndex--;
        if(itemIndex >= 0 && itemIndex < structures.length )
            return structures[itemIndex];
        else
            return null;
    }
    
    public int getCount() {
        return count;
    
    }
    
    public int getNextIndex() {
        System.out.println("CP returning next index : " + index);
        return (index);
    }
    
    public int getSize() {
        return index - startIndex;
    }
    
    public Vector<String> displayContents() {
        Vector<String> v = new Vector();
        v.add("Number of Constant Pool items: " + Integer.toString(count) + "\n");
        return v;
    }
    
    public String toString() {
        return new String("Constant Pool");
    }
}
