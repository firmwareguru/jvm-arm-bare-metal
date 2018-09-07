/*
 * ExceptionTableLength.java
 *
 * Created on June 7, 2006, 8:45 PM
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
public class ExceptionTableLength extends Element
{
    
    /** Creates a new instance of ExceptionTableLength */
    public ExceptionTableLength()
    {
        setSize(u2);
    }
    
    public String toString()
    {
        return "exception_table_length";
    }
    
}
