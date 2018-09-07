/*
 * AttributesOffset.java
 *
 * Created on June 21, 2006, 9:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.elements.linktable;

import emr.elements.common.Element;

/**
 *
 * @author Ross
 */
public class AttributesOffset extends Element
{
    
    /** Creates a new instance of AttributesOffset */
    public AttributesOffset()
    {
        setSize(u2);
    }
    public String toString()
    {
        return "attributes_offset";
    }
    
}
