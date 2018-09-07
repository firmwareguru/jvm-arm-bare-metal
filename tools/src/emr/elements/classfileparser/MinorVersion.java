/*
 * MinorVersion.java
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
public class MinorVersion extends Element
{
    
    /** Creates a new instance of MinorVersion */
    public MinorVersion()
    {
        setSize(u2);
    }
    
    public String toString()
    {
        return "minor_version";
    }    
    
}
