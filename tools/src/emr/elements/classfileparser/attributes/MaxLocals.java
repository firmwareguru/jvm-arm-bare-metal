/*
 * MaxLocals.java
 *
 * Created on June 7, 2006, 8:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.elements.classfileparser.attributes;

import emr.elements.common.Element;
/**
 *
 * @author Ross
 */
public class MaxLocals extends Element
{
    
    /** Creates a new instance of MaxLocals */
    public MaxLocals()
    {
        setSize(u2);
    }

    public String toString()
    {
        return "max_locals";
    }
     
    
}
