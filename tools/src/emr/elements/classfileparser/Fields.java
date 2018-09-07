/*
 * Fields.java
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
public class Fields extends ComplexElement
{
    
    /* reference to the class' ConstantPool */
    ConstantPool constantPool;
    
    /** Creates a new instance of Fields */
    public Fields(ConstantPool constantPool_)
    {
        constantPool = constantPool_;
        
        add(new FieldsCount());
    }
    
    public void readChildren(InputStream is) throws IOException
    {
        get(0).readData(is);
        
        int count = get(0).getValue();
        
        if(debug) System.out.println("Fields: " + count);
        for(int i = 0; i < count; i++)
        {
            Element e = new FieldInfo(constantPool);
            e.readData(is);
            add(e);
        }
    }
    
    
    
    public String toString()
    {
       return "fields";
        
    }
    
}
