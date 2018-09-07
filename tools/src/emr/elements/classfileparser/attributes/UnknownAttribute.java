/*
 * UnknownAttribute.java
 *
 * Created on June 6, 2006, 6:36 PM
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

public class UnknownAttribute extends Element
{
    
    byte[] info;
    
    /** Creates a new instance of UnknownAttribute */
    public UnknownAttribute(int length)
    {
        info = new byte[length];
    }
    
    public void readElement(InputStream is) throws IOException
    {
        is.read(info);
    }
    
    public void writeElement(OutputStream os) throws IOException
    {
        os.write(info);
    }
    
    
    public String toString() 
    {
        return "unknown_attribute";
    }
    
}
