/*
 * ConstantLongInfo.java
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
public class ConstantLongInfo extends Element
{
    
    /** Creates a new instance of ConstantLongInfo */
    public ConstantLongInfo()
    {
        add(new HighBytes());
        add(new LowBytes());
    }
    
    
    public String toString()
    {
        return "CONSTANT_Long_info";
    }
    
}
