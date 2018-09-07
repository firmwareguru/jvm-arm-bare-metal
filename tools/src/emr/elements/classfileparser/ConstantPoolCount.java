/*
 * ConstantPoolCount.java
 *
 * Created on May 30, 2006, 8:22 PM
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
public class ConstantPoolCount extends Element
{
    

    /** Creates a new instance of ConstantPoolCount */
    public ConstantPoolCount()
    {
        setSize(u2);
    }
    
    public String toString()
    {
        return "constant_pool_count";
    }    
}
