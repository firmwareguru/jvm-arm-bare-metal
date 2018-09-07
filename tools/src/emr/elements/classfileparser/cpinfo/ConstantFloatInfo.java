/*
 * ConstantFloatInfo.java
 *
 * Created on May 31, 2006, 6:52 PM
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
public class ConstantFloatInfo extends Element
{
    
    /** Creates a new instance of ConstantFloatInfo */
    public ConstantFloatInfo()
    {
        setSize(u4);
    }
    
    public String toString()
    {
        return "CONSTANT_Float_info";
    }
    
}
