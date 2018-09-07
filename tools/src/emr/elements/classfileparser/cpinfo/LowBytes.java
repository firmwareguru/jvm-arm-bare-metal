/*
 * LowBytes.java
 *
 * Created on June 2, 2006, 10:30 PM
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
public class LowBytes extends Element
{
    
    /** Creates a new instance of LowBytes */
    public LowBytes()
    {
        setSize(u4);
    }
    
    public String toString()
    {
        return "low_bytes";
    }
    
}
