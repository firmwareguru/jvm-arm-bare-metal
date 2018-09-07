/*
 * DescriptorIndex.java
 *
 * Created on June 2, 2006, 10:14 PM
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
public class DescriptorIndex extends Element
{
    
    /** Creates a new instance of DescriptorIndex */
    public DescriptorIndex()
    {
        setSize(u2);
    }
    
    public String toString()
    {
        return "descriptor_index";
    }
    
}
