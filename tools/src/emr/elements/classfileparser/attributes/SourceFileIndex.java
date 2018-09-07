/*
 * SouceFileIndex.java
 *
 * Created on June 7, 2006, 9:31 PM
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
public class SourceFileIndex extends Element
{
    
    /** Creates a new instance of SouceFileIndex */
    public SourceFileIndex()
    {
        setSize(u2);
    }
    
    public String toString()
    {
        return "sourcefile_index";
    }
    
}
