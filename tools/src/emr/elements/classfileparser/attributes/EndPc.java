/*
 * EndPc.java
 *
 * Created on June 7, 2006, 8:31 PM
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
public class EndPc extends Element
{
    
    /** Creates a new instance of EndPc */
    public EndPc()
    {
        setSize(u2);
    }
    
    public String toString() 
    {
        return "end_pc";
    }    
    
}
