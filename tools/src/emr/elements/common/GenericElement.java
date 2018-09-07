/*
 * GenericElement.java
 *
 * Created on December 10, 2006, 6:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.elements.common;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * A GenericElement allows you to specify its size and name using the 
 * constructor rather than creating a whole new class.
 *
 * @author Ross
 */
public class GenericElement extends Element
{
    String name;
    
    /** Creates a new instance of GenericElement */
    public GenericElement(int size_, String name_) 
    {
        setSize(size_);
        name = name_;
    }
    
    @Override
    public String toString()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
}
