/*
 * ClassFile.java
 *
 * Created on March 23, 2006, 5:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */



package emr.classanalyzer;
import java.util.*;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.JTextArea;



/**
 *
 * @author Ross
 *
 * This is the top-level structure contained in a class file.  
 * All other structures exist as sub-structures.
 *
 * Maintain a running counter used throught the parsing process.
 * Use a method that calls various parsing methods of each sub structure, passing in the index.
 *
 * Responsibility of each method is to place the 'index' value at the location of
 * the next byte in the file.
 */

public class ClassFile implements Displayable {
    
    /* Contains file data */
    private byte[] data;
    
    private ConstantPool constantPool;
    
    private Interfaces interfaces;
    
    private Methods methods;
    
    private Fields fields;
    
    /* Attributes local to this structure */
    private Attributes attributes;
    private String magicNumber;
    private String version;
    private int constantPoolCount;
    private Vector<String> accessFlags;
    private int thisClass; // index into the CP
    private int superClass;  // index into the CP
    private int interfacesCount;
    private int fieldsCount;
    private int methodsCount;
    private int attributesCount;
    
    
    
    // current position in data array
    private int index;
    
    private JTextArea infoArea;
    
    /** Creates a new instance of ClassFile */
    public ClassFile(byte[] d, DefaultMutableTreeNode root, JTextArea iA) {
        data = d;
        infoArea = iA;
        // create and add nodes.
        
        // set ClassFile as root user object
        root.setUserObject(this);        
        
        DefaultMutableTreeNode node;

        node = new DefaultMutableTreeNode();
        root.add(node);
        constantPool = new ConstantPool(d, node);        
        
        node = new DefaultMutableTreeNode();
        root.add(node);
        interfaces = new Interfaces(d, node, constantPool);
        
        node = new DefaultMutableTreeNode();
        root.add(node);
        fields = new Fields(d, node, constantPool);
        
        node = new DefaultMutableTreeNode();
        root.add(node);
        methods = new Methods(d, node, constantPool);

        node = new DefaultMutableTreeNode();
        root.add(node);
        attributes = new Attributes(d, node, constantPool);
        
    }
    
    /*******************************************************************
     *  Main function to direct parsing of entire class file
     *******************************************************************/
    
    public void parseClassFile() {
        /* Steps:
         *     1. Get magic number
         *     2. Get Version
         *     3. Get ConstantPoolCount
         *     4. Parse Constant Pool
         *     4. Get 
         */
        index = 0;
        
        infoArea.append("Identifying magic number.\n");
        getMagicNumber();
        infoArea.append("Identifying version number.\n");        
        getClassVersion();
        
        infoArea.append("Identifying constant pool count.\n");        
        // get the index in the byte array that points to the data immediately following the
        // constant pool

        constantPool.parsePool(index);
        index = constantPool.getNextIndex();
        
        infoArea.append("Identifying access flags.\n");
        getAccessFlags();
        
        getThisSuperClass();
        
        infoArea.append("Parsing Interfaces.\n");
        interfaces.parseInterfaces(index);
        index = interfaces.getNextIndex();
        
        infoArea.append("Parsing Fields.\n");
        fields.parseFields(index);
        index = fields.getNextIndex();
        
        infoArea.append("Parsing Methods.\n");
        methods.parseMethods(index);
        index = methods.getNextIndex();
        
        infoArea.append("Parsing Attributes.\n");
        attributes.parseAttributes(index);
        index = attributes.getNextIndex();
        
        

        
    }
    
    private void getMagicNumber() {
        int magicNum;
        
        magicNum = ((int)data[index] << 24) & 0xFF000000;
        magicNum |= ((int)data[index+1] << 16) & 0x00FF0000;
        magicNum |= ((int)data[index+2] << 8) & 0x0000FF00;
        magicNum |= ((int)data[index+3]) & 0x000000FF;

        magicNumber = Integer.toHexString(magicNum);
        
        index += 4;
    }
    
    
    private void getClassVersion() {
        /* bytes 7,8 compose the major version
         * bytes 5,6 compose the minor version
         */
        
        int majorVersion;
        int minorVersion;
        
        majorVersion = ((int)data[index] << 8) & 0x0000FF00;
        majorVersion |= ((int)data[index+1]) & 0x000000FF;
        
        minorVersion = ((int)data[index+2] << 8) & 0x0000FF00;
        minorVersion |= ((int)data[index+3]) & 0x000000FF;
                
        version = (Integer.toString(majorVersion) + "." + Integer.toString(minorVersion));
        index += 4;
    }
    
    private void getAccessFlags() {

        byte upper = data[index];
        byte lower = data[index + 1];
        
        int flags = ((int)upper << 8) & 0x0000FF00;
        flags |= (int)lower & 0x000000FF;
        
        accessFlags = new Vector<String>();
        
        if((flags & 0x00000001) == 1)
            accessFlags.add(new String("Public"));


        if((flags >> 4 & 0x00000001) == 1) 
            accessFlags.add(new String("Final"));
        
        if((flags >> 5 & 0x00000001) == 1)
            accessFlags.add(new String("Super"));

        if((flags >> 9 & 0x00000001) == 1) 
            accessFlags.add(new String("Interface"));

        if((flags >> 10 & 0x00000001) == 1) 
            accessFlags.add(new String("Abstract"));

        index += 2; // move past accessflags
        
        
    }
    
    private void getThisSuperClass() {
        
        thisClass = ((int)data[index] << 8) & 0x0000FF00;
        thisClass |= ((int)data[index+1]) & 0x000000FF;
        
        superClass = ((int)data[index+2] << 8) & 0x0000FF00;
        superClass |= ((int)data[index+3]) & 0x000000FF;

        index += 4;
    }
    
    //-------------------------------
    
    
    public String toString() {
        return "Class File";
    }
    
    public Vector<String> displayContents() {
        Vector<String> contents = new Vector<String>();
        
        contents.add("ClassFile contents:\n");
        contents.add("Magic number : " + magicNumber + "\n");
        contents.add("Version      : " + version + "\n");
        contents.add("Access flags : " );
        for(int i = 0; i < accessFlags.size(); i++)
            contents.add(accessFlags.get(i) + ", ");
        contents.add("\n");
        contents.add("This class index: " + thisClass + "\n");
                
        contents.add("           name : ");
        Vector<String> x1 = constantPool.getItem(thisClass).displayContents();
        for(int i = 0; i < x1.size(); i++)
            contents.add(x1.get(i));
        contents.add("\n");

        contents.add("Super class index: " + superClass + "\n");
        if(superClass > 0) {
            contents.add("           name : ");
            Vector<String> x2 = constantPool.getItem(superClass).displayContents();
            for(int i = 0; i < x2.size(); i++)
                contents.add(x2.get(i));
            contents.add("\n");
        }

        contents.add("Constant Pool \n");
        contents.add("    count : " + constantPool.getCount() + "\n");
        contents.add("    size  : " + (constantPool.getSize() - 2) + "\n");
        
        contents.add("Interfaces \n");
        contents.add("    count : " + interfaces.getCount() + "\n");
        contents.add("    size  : " + (interfaces.getSize() - 2) + "\n");
        
        contents.add("Fields \n");
        contents.add("    count : " + fields.getCount() + "\n");
        contents.add("    size  : " + (fields.getSize() - 2) + "\n");
        contents.add("Methods \n");
        contents.add("    count : " + methods.getCount() + "\n");
        contents.add("    size  : " + (methods.getSize() - 2) + "\n");
        contents.add("Attributes \n");
        contents.add("    count : " + attributes.getCount() + "\n");
        contents.add("    size  : " + (attributes.getSize() - 2) + "\n");
        return contents;
    }
}
