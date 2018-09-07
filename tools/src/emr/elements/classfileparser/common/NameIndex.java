/*
 * NameIndex.java
 *
 * Created on June 2, 2006, 10:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.elements.classfileparser.common;

import emr.elements.common.Element;

/**
 *
 * @author Ross
 */
public class NameIndex extends Element
{
    
    /** Creates a new instance of NameIndex */
    public NameIndex()
    {
        setSize(u2);
    }
    
    public String toString()
    {
        return "name_index";
    }
    
}
