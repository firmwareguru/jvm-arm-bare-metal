/*
 * ConstantFieldRefInfo.java
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
public class ConstantFieldRefInfo extends Element
{
    
    /** Creates a new instance of ConstantFieldRefInfo */
    public ConstantFieldRefInfo()
    {
        add(new ClassIndex());
        add(new NameAndTypeIndex());
    }
    
    public String toString()
    {
        return "CONSTANT_Fieldref_info";
    }
    
}
