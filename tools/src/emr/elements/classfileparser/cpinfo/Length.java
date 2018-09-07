/*
 * Length.java
 *
 * Created on June 2, 2006, 11:04 PM
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
public class Length extends Element
{
    
    /** Creates a new instance of Length */
    public Length()
    {
        setSize(u2);
    }
    
    public String toString()
    {
        return "length";
    }
    
}
