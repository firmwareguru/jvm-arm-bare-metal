/*
 * MethodInfo.java
 *
 * Created on April 3, 2006, 10:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.classanalyzer.structures;
import emr.classanalyzer.*;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Ross
 */
public class MethodInfo implements Displayable {
    
    Attributes attributes;

    static public byte[] data; // class file data array

    int methodIndex;
    
    String name;
    int nameIndex;
    
    String descriptor;
    int descriptorIndex;
    
    int index;

    int attributesCount;
    
    ConstantPool constantPool;

    Vector<String> accessFlags;
    
    DefaultMutableTreeNode node;
    
    /** Creates a new instance of MethodInfo */
    public MethodInfo(int index_, DefaultMutableTreeNode node_, ConstantPool cp) {
        Log.event("MethodInfo init, index = " + index_);
        index = index_;
        constantPool = cp;
        node = node_;
        
        node.setUserObject(this);
        
        /* lets process everything here
         * need to identify: 
         *   u2 accessflags
         *   u2 name_index
         *   u2 descriptor_index
         *   u2 attributes_count
         *   attribute_info attributes[attributes_count]
         */
        
        
        // accessflags
        getAccessFlags();
        
        // name
        nameIndex = ((int)data[index] << 8) & 0x0000FF00;
        nameIndex |= ((int)data[index+1]) & 0x000000FF;
        
        name = constantPool.getItem(nameIndex).displayContents().get(0);
        index += 2;
        
        // descriptor
        descriptorIndex = ((int)data[index] << 8) & 0x0000FF00;
        descriptorIndex |= ((int)data[index+1]) & 0x000000FF;
        
        descriptor = constantPool.getItem(descriptorIndex).displayContents().get(0);
        index += 2;
        
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode();
        node.add(childNode);
        attributes = new Attributes(data, childNode, constantPool);
        
        attributes.parseAttributes(index);
        
        attributesCount = attributes.getCount();
        
        index = attributes.getNextIndex();
        
    }
    
    public void setMethodIndex(int methodIndex_) {
        methodIndex = methodIndex_;
    }

    public int getSize() {
        return 6 + attributes.getSize();
    }
    
    private void getAccessFlags() {

        byte upper = data[index];
        byte lower = data[index + 1];
        
        int flags = ((int)upper << 8) & 0x0000FF00;
        flags |= (int)lower & 0x000000FF;
        
        accessFlags = new Vector<String>();
        
        if((flags & 0x00000001) == 1) // 0x0001
            accessFlags.add(new String("Public"));
        
        if((flags >> 1 & 0x00000001) == 1) // 0x0002            
            accessFlags.add(new String("Private"));
        
        if((flags >> 2 & 0x00000001) == 1) // 0x0004
            accessFlags.add(new String("Protected"));

        if((flags >> 3 & 0x00000001) == 1) // 0x0008
            accessFlags.add(new String("Static"));
        
        if((flags >> 4 & 0x00000001) == 1) // 0x0010
            accessFlags.add(new String("Final"));
        
        if((flags >> 5 & 0x00000001) == 1) // 0x0020
            accessFlags.add(new String("Synchronized"));
         
        if((flags >> 8 & 0x00000001) == 1) // 0x0100
            accessFlags.add(new String("Native"));

        if((flags >> 10 & 0x00000001) == 1) // 0x0400
            accessFlags.add(new String("Abstract"));
        
        if((flags >> 11 & 0x00000001) == 1) // 0x0800 0000 1000 0000 0000
            accessFlags.add(new String("String"));


        index += 2; // move past accessflags
        
        
    }
    
    // name to display in Tree
    public String toString() {
       return Integer.toString(methodIndex) + " " + "MethodInfo";
        
    }
    
    public Vector<String> displayContents() {
        Vector<String> v = new Vector();
        v.add("MethodInfo contents: \n\n");
        v.add("Access flags : " );
        for(int i = 0; i < accessFlags.size(); i++)
            v.add(accessFlags.get(i) + ", ");
        v.add("\n");
        v.add("Name : " + nameIndex + " : " + name + "\n");
        v.add("Descriptor : " + descriptorIndex + " : " + descriptor + "\n");
        v.add("Attributes count : " + attributesCount);
        
        return v;
    }
    
}
