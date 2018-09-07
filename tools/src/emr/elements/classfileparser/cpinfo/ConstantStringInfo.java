/*
 * ConstantStringInfo.java
 *
 * Created on May 31, 2006, 6:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.elements.classfileparser.cpinfo;

import emr.elements.common.Element;

/**
 *
 * @author Ross
 */
public class ConstantStringInfo extends Element
{
    
    /** Creates a new instance of ConstantStringInfo */
    public ConstantStringInfo()
    {
        add(new StringIndex());
    }
    
    public String toString()
    {
        return "CONSTANT_String_info";
    }
    
}
