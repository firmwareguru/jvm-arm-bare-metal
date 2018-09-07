/*
 * SourceFileAttribute.java
 *
 * Created on June 6, 2006, 6:40 PM
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
public class SourceFileAttribute extends Element
{
    
    /** Creates a new instance of SourceFileAttribute */
    public SourceFileAttribute()
    {
        add(new SourceFileIndex());
    }
    
    public String toString()
    {
        return "SourceFile_attribute";
    }
    
}
