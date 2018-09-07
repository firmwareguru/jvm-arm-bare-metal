/*
 * ThisClass.java
 *
 * Created on May 30, 2006, 8:23 PM
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
public class ThisClass extends Element
{
    
    /** Creates a new instance of ThisClass */
    public ThisClass()
    {
        setSize(u2);
    }
    
    public String toString()
    {
        return "this_class";
    }    
    
}
