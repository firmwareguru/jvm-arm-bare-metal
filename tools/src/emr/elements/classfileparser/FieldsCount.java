/*
 * FieldsCount.java
 *
 * Created on May 30, 2006, 8:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.elements.classfileparser;

import emr.elements.common.Element;

/**
 *
 * @author Ross
 */
public class FieldsCount extends Element
{
    
    /** Creates a new instance of FieldsCount */
    public FieldsCount()
    {
        setSize(u2);
    }
    
    public String toString()
    {
        return "fields_count";
    }    
    
}
