/*
 * AttributesCount.java
 *
 * Created on May 30, 2006, 8:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.elements.classfileparser.common;

import emr.elements.common.Element;

/**
 *
 * @author Ross
 */
public class AttributesCount extends Element
{
    
    /** Creates a new instance of AttributesCount */
    public AttributesCount()
    {
        setSize(u2);
    }
    
    public String toString() 
    {
        return "attributes_count";
    }
    
}
