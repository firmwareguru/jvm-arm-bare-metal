/*
 * ExceptionTable.java
 *
 * Created on June 7, 2006, 8:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.elements.classfileparser.attributes;

import emr.elements.classfileparser.ComplexElement;
import emr.elements.common.Element;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 *
 * @author Ross
 */

public class ExceptionTable extends ComplexElement
{
    
    /** Creates a new instance of ExceptionTable */
    public ExceptionTable()
    {
        add(new ExceptionTableLength());
    }
    
    public void readChildren(InputStream is) throws IOException
    {
        get(0).readData(is);
        
        int length = get(0).getValue();
        
        for(int i = 0; i < length; i++)
        {
            Element e = new ExceptionTableEntry();
            e.readData(is);
            add(e);
        }
        
    }
    
    public String toString() 
    {
        return "exception_table";
    }
    
}
