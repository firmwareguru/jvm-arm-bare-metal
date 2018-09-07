/*
 * ConstantClassInfo.java
 *
 * Created on May 30, 2006, 7:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.elements.classfileparser.cpinfo;

import emr.elements.common.Element;
import emr.elements.classfileparser.common.NameIndex;

/**
 *
 * @author Ross
 */
public class ConstantClassInfo extends Element
{
    
    /** Creates a new instance of ConstantClassInfo */
    public ConstantClassInfo()
    {
        add(new NameIndex());
    }
    
    public String toString()
    {
        return "CONSTANT_Class_info";
    }
    
    
}
