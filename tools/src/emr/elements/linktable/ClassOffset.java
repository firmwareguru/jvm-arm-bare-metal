/*
 * ClassFileOffset.java
 *
 * Created on June 21, 2006, 9:51 PM
 *
 * A ClassFileOffset is the first element in the LinkTable's "ClassReference" structures
 */

package emr.elements.linktable;

import emr.elements.common.Element;

/**
 *
 * @author Ross
 */
public class ClassOffset extends Element
{
    
    /** Creates a new instance of ClassFileOffset */
    public ClassOffset()
    {
        setSize(u2);
    }
    public String toString()
    {
        return "class_offset";
    }
    
}
