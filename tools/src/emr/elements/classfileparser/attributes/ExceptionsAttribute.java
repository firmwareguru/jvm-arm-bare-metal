/*
 * ExceptionsAttribute.java
 *
 * Created on June 6, 2006, 6:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.elements.classfileparser.attributes;

import emr.elements.common.Element;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 *
 * @author Ross
 */
public class ExceptionsAttribute extends Element
{
    
    /** Creates a new instance of ExceptionsAttribute */
    public ExceptionsAttribute()
    {
        add(new NumberOfExceptions());
    }
    
    public void readChildren(InputStream is) throws IOException
    {
        get(0).readData(is);
        
        int count = get(0).getValue();
        
        for(int i = 0; i < count; i++)
        {
            // Create an anonymous element to represent each entry in the array
            Element e = new Element();
            e.setSize(u2);
            e.readData(is);
            add(e);
        }
    }
    
    public String toString()
    {
        return "Exceptions_attribute";
    }
    
}
