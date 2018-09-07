/*
 * MagicNumber.java
 *
 * Created on May 30, 2006, 8:21 PM
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
public class MagicNumber extends Element
{
    
    /** Creates a new instance of MagicNumber */
    public MagicNumber()
    {
        setSize(u4);
    }
    
    public String toString() 
    {
        return "magic_number";
    }
    
}
