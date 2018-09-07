/*
 * StringIndex.java
 *
 * Created on June 2, 2006, 10:13 PM
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
public class StringIndex extends Element
{
    
    /** Creates a new instance of StringIndex */
    public StringIndex()
    {
        setSize(u2);
                
    }
    
    public String toString()
    {
        return "string_index";
    }
}
