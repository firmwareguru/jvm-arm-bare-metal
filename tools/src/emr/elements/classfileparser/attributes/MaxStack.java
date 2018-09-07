/*
 * MaxStack.java
 *
 * Created on June 7, 2006, 8:17 PM
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
public class MaxStack extends Element
{
    
    /** Creates a new instance of MaxStack */
    public MaxStack()
    {
        setSize(u2);
    }
    
    public String toString()
    {
        return "max_stack";
    }
    
}
