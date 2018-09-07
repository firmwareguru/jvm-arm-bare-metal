/*
 * AttributeNameIndex.java
 *
 * Created on June 5, 2006, 10:32 PM
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
public class AttributeNameIndex extends Element
{
    
    /** Creates a new instance of AttributeNameIndex */
    public AttributeNameIndex()
    {
        setSize(u2);
    }
    
    public String toString()
    {
        return "attribute_name_index";
    }
    
}
