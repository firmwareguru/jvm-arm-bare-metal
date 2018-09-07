/*
 * Tag.java
 *
 * Created on May 31, 2006, 10:16 PM
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
public class Tag extends Element
{
    
    /** Creates a new instance of Tag */
    public Tag()
    {
        setSize(u1);
    }
    
    public String toString()
    {
        return "tag";
    }
    
}
