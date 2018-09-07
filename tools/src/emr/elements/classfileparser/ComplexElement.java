/*
 * ComplexElement.java
 *
 * Created on June 6, 2006, 5:37 PM
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
public class ComplexElement extends Element
{
    
    /** Creates a new instance of ComplexElement */
    public ComplexElement()
    {
    }
    
    /** Returns the Element at the index location in the logical array.
     * The sub-elements of this element are indexed from 0 to getCount() - 1 */
    public Element getElement(int index)
    {
        if(index < 0 || index >= getCount())
            return null;
        
        // account for Count Elements at index 0
        return get(index + 1);
    }

    /** Sets the number of sub Elements in this ComplexElement */
    public void setCount(int count_)
    {
        get(0).setValue(count_);
    }
    
    /** Returns the number of sub Elements in this ComplexElement */
    public int getCount()
    {
        return get(0).getValue();
    }


    
}
