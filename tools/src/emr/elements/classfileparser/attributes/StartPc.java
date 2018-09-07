/*
 * StartPc.java
 *
 * Created on June 7, 2006, 8:30 PM
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
public class StartPc extends Element
{
    
    /** Creates a new instance of StartPc */
    public StartPc()
    {
        setSize(u2);
    }
    
    public String toString() 
    {
        return "start_pc";
    }    
    
}
