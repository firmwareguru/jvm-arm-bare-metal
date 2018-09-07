/*
 * ExceptionsAttributeInfo.java
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
public class ExceptionsAttributeInfo extends AttributeInfo {
    
    int numberExceptions;

    int[] poolIndex;
    
    ConstantPool cp;
    
    /** Creates a new instance of ExceptionsAttributeInfo */
    public ExceptionsAttributeInfo(String name_, int index_, DefaultMutableTreeNode node_, ConstantPool cp) {
        super(name_, index_, node_, cp);
    
                
        this.cp = cp;
        
      
        // u2 number of exceptions
        // u2 exception table
        
        numberExceptions =  ((int)data[index] << 8) & 0x0000FF00;
        numberExceptions |= ((int)data[index + 1])  & 0x000000FF;

        Log.event("Exceptions number: " + numberExceptions);
        poolIndex = new int[numberExceptions];
        
        index += 2;
        
        // parse the indexes
        int idx;
        for(int i = 0; i < numberExceptions; i++) {
            idx =  ((int)data[index] << 8) & 0x0000FF00;
            idx |= ((int)data[index + 1])  & 0x000000FF;
            index += 2;
            poolIndex[i] = idx;
        }
    }
    
    
    public Vector<String> displayContents() {
        Vector<String> v = new Vector<String>();
        
        v.add("Exceptions attribute:\n");
       
        for(int i = 0; i < numberExceptions; i++) {
            v.addAll(constantPool.getItem(poolIndex[i]).displayContents());
        }
        return v;
         
    }    
     
    
}
