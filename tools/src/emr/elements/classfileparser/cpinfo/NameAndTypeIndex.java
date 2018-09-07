/*
 * NameAndTypeIndex.java
 *
 * Created on June 2, 2006, 10:10 PM
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
public class NameAndTypeIndex extends Element
{
    
    /** Creates a new instance of NameAndTypeIndex */
    public NameAndTypeIndex()
    {
        setSize(u2);
                
    }
    
    public String toString()
    {
        return "name_and_type_index";
    }
    
}
