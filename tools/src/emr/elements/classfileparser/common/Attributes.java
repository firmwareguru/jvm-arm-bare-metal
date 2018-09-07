/*
 * Attributes.java
 *
 * Created on May 30, 2006, 7:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.elements.classfileparser.common;

import emr.elements.classfileparser.ComplexElement;
import emr.elements.classfileparser.ConstantPool;
import emr.elements.classfileparser.attributes.AttributeInfo;
import emr.elements.common.Element;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 *
 * @author Ross
 */
public class Attributes extends ComplexElement
{
    
    /* reference to the class' ConstantPool */
    ConstantPool constantPool;
    
    
    /** Creates a new instance of Attributes
     *  Needs reference to ConstantPool to pass to AttributeInfo Elements 
     */
    public Attributes(ConstantPool constantPool_)
    {
        constantPool = constantPool_;
        
        add(new AttributesCount());
    }
    
    public void readChildren(InputStream is) throws IOException
    {
        get(0).readData(is);
        
        int count = getCount();
        
        for(int i = 0; i < count; i++)
        {
            Element e = new AttributeInfo(constantPool);
            e.readData(is);
            if(debug) System.out.println("  adding new attribute length: " + e.getElement("attribute_length").getValue());
            add(e);
            
        }
        
        
    }
    
    
    public String toString()
    {
        return "attributes";
    }
    
}
