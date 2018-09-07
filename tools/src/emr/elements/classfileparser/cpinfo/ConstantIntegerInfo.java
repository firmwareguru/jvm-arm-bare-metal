/*
 * ConstantIntegerInfo.java
 *
 * Created on May 31, 2006, 6:50 PM
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
public class ConstantIntegerInfo extends Element
{
    
    /** Creates a new instance of ConstantIntegerInfo */
    public ConstantIntegerInfo()
    {
        setSize(u4);
    }
    
    public String toString()
    {
        return "CONSTANT_Integer_info";
    }
    
}
