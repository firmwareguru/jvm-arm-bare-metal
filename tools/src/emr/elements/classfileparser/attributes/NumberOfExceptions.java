/*
 * NumberOfExceptions.java
 *
 * Created on June 7, 2006, 9:02 PM
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
public class NumberOfExceptions extends Element
{
    
    /** Creates a new instance of NumberOfExceptions */
    public NumberOfExceptions()
    {
        setSize(u2);
    }
    
    public String toString()
    {
        return "number_of_exceptions";
    }
}
