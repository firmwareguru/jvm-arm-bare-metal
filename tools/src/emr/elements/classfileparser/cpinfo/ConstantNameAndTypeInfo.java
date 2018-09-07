/*
 * ConstantNameAndTypeInfo.java
 *
 * Created on May 31, 2006, 6:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.elements.classfileparser.cpinfo;

import emr.elements.common.Element;
import emr.elements.classfileparser.common.DescriptorIndex;
import emr.elements.classfileparser.common.NameIndex;

/**
 *
 * @author Ross
 */
public class ConstantNameAndTypeInfo extends Element
{
    
    /** Creates a new instance of ConstantNameAndTypeInfo */
    public ConstantNameAndTypeInfo()
    {
        add(new NameIndex());
        add(new DescriptorIndex());
        
    }
    
    public String toString()
    {
        return "CONSTANT_NameAndType_info";
    }
    
}
