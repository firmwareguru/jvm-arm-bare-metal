/*
 * InternalClasses.java
 *
 * Created on December 13, 2006, 9:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package classpackager;

import emr.elements.common.Element;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 *
 * @author Ross
 */
public class InternalClasses extends Element 
{
    /*
     * Cache the lookup
     */
    InternalClass cachedClass = null;
    
    /** Creates a new instance of InternalClasses */
    public InternalClasses() 
    {
    }
    
    public InternalClass getInternalClass(String className_)
    {
        // Lookup the cache first
        if (cachedClass != null)
            if (className_.equals(cachedClass.getName()))
                return cachedClass;
        
        for (int i = 0; i < size(); i++)
        {
            InternalClass c = (InternalClass)get(i);
            if (className_.equals(c.getName()))
            {
                cachedClass = c;
                return c;
            }
        }
        return null;
    }
    
    @Override
    public String toString()
    {
        return "internal_classes";
    }
    
           
    
}
