/*
 * AttributeLength.java
 *
 * Created on June 5, 2006, 10:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.elements.classfileparser.attributes;

import emr.elements.common.Element;

/**
 *
 * @author Ross
 */
public class AttributeLength extends Element
{
    
    /** Creates a new instance of AttributeLength */
    public AttributeLength()
    {
        setSize(u4);
    }
    
    public String toString() 
    {
        return "attribute_length";
    }
    
}
