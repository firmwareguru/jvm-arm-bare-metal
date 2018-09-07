/*
 * ConstantValueIndex.java
 *
 * Created on June 7, 2006, 8:15 PM
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

public class ConstantValueIndex extends Element
{
    
    /** Creates a new instance of ConstantValueIndex */
    public ConstantValueIndex()
    {
        setSize(u2);
    }
    
    public String toString()
    {
        return "constantvalue_index";
    }
    
}
