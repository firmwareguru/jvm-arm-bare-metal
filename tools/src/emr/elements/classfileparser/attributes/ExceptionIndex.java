/*
 * ExceptionIndex.java
 *
 * Created on June 7, 2006, 9:00 PM
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
public class ExceptionIndex extends Element
{
    
    /** Creates a new instance of ExceptionIndex */
    public ExceptionIndex()
    {
        setSize(u2);
    }
    
    public String toString()
    {
        return "exception_index";
    }
    
}
