/*
 * HighBytes.java
 *
 * Created on June 2, 2006, 10:28 PM
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
public class HighBytes extends Element
{
    
    /** Creates a new instance of HighBytes */
    public HighBytes()
    {
        setSize(u4);
    }
    
    public String toString()
    {
        return "high_bytes";
    }
    
}
