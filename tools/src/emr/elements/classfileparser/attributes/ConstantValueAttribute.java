/*
 * ConstantValueAttributeInfo.java
 *
 * Created on June 5, 2006, 10:24 PM
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
public class ConstantValueAttribute extends Element
{
    
    /** Creates a new instance of ConstantValueAttributeInfo */
    public ConstantValueAttribute()
    {
        add(new ConstantValueIndex());
    }
    
    /** Returns the index into the Constant Pool that contains the constant value */
    public int getConstantValueIndex()
    {
        return getElement("constantvalue_index").getValue();
    }
    
    public String toString()
    {
        return "ConstantValue_attribute";
    }
    
}
