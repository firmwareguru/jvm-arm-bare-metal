/*
 * AccessFlagsOffset.java
 *
 * Created on June 21, 2006, 9:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.elements.linktable;

import emr.elements.common.Element;

/**
 *
 * @author Ross
 */
public class AccessFlagsOffset extends Element
{
    
    /** Creates a new instance of AccessFlagsOffset */
    public AccessFlagsOffset()
    {
        setSize(u2);
    }
    
    public String toString()
    {
        return "access_flags_offset";
    }
    
}
