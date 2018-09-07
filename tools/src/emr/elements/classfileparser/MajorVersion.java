/*
 * MajorVersion.java
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
public class MajorVersion extends Element
{
    
    /** Creates a new instance of MajorVersion */
    public MajorVersion()
    {
        setSize(u2);
    }
    
    public String toString()
    {
        return "major_version";
    }    
    
}
