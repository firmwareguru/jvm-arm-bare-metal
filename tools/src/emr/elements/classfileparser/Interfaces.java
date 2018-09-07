/*
 * Interfaces.java
 *
 * Created on May 30, 2006, 7:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.elements.classfileparser;

import emr.elements.common.Element;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


/**
 *
 * @author Ross
 */
public class Interfaces extends ComplexElement
{
        
    /** Creates a new instance of Interfaces */
    public Interfaces()
    {
        add(new InterfacesCount());
    }
    
    public void readChildren(InputStream is) throws IOException
    {
        get(0).readData(is);
        
        int count = get(0).getValue();
        
        for(int i = 0; i < count; i++)
        {
            // Create an anonymous element to represent each entry in the interfaces array
            Element e = new Element();
            e.setSize(u2);
            e.readData(is);
            add(e);
            
            if(debug) System.out.println("Interfaces adding element with index: " + e.getValue());
        }
    }
    
    /** Return the value of the Interface index into the ConstantPool.
     * Indexed from 0 to size() - 1 
     */
    public int getInterfaceIndex(int index_)
    {
        // index starting from 1 because element 0 is the number of elements
        return get(index_ + 1).getValue();
    }
    
    public String toString()
    {
        return "interfaces";
    }   
    
    
}
