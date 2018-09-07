/*
 * ConstantInterfaceMethodRefInfo.java
 *
 * Created on May 31, 2006, 6:55 PM
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
public class ConstantInterfaceMethodRefInfo extends Element
{
    
    /** Creates a new instance of ConstantInterfaceMethodRefInfo */
    public ConstantInterfaceMethodRefInfo()
    {
        add(new ClassIndex());
        add(new NameAndTypeIndex());
    }
    
    public String toString()
    {
        return "CONSTANT_InterfaceMethodref_info";
    }
    
}
