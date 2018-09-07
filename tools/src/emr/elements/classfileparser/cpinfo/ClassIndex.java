/*
 * ClassIndex.java
 *
 * Created on June 2, 2006, 10:09 PM
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
public class ClassIndex extends Element
{
    
    /** Creates a new instance of ClassIndex */
    public ClassIndex()
    {
        setSize(u2);
    }
    
    public String toString()
    {
        return "class_index";
    }
}
