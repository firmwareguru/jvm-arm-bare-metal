/*
 * ConstantPool.java
 *
 * Created on May 30, 2006, 7:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.elements.classfileparser;

import emr.elements.classfileparser.cpinfo.*;
import emr.elements.common.Element;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;


/**
 *
 * @author Ross
 */
public class ConstantPool extends ComplexElement
{
    
    /* The number of elements in the Constant Pool */
    //int count = 0;
    
    /** Creates a new instance of ConstantPool */
    public ConstantPool()
    {
        add(new ConstantPoolCount());
    }
    
    /* Dynamically adds instances of CPInfo subclasses.  It must compensate for Long or Double
     * Info items by inserting a blank Element. 
     */
    public void readChildren(InputStream is) throws IOException
    {
        // first, read the ConstantPoolCount Element
        get(0).readData(is);
   
        // next, read in count number of CPInfo elements.
        for(int i = 0; i < get(0).getValue() - 1; i++)
        {
            if(debug) System.out.println(i + " " + get(0).getValue());
            
            CPInfo info = new CPInfo(i+1);  // create CPInfo passing in index which must start at '1' hence the i+1
            info.readData(is);
                    
            add(info);

            // now some funky stuff for longs or doubles 'cause they "take up" two entries in the CP table
            // add empty element after a Long or Double
            int tag = info.getTag();
            if(tag == 5 || tag == 6)
            {
                i++;
                add(new EmptyCPInfo());
            }
        }    
    }
    
    /*
    public void writeChildren(OutputStream os) throws IOException
    {
        get(0).writeData(os);
        
    }
    */
    
    /* Constant Pool info Elements are indexed from 1 to count-1  so
     * ComplexElement's version needs to be overriden. 
     */
    public Element getElement(int index)
    {
        if(index < 1 || index > (getCount() - 1))
            return null;
        
        return get(index);
    }
    
    /***************
     *  Helper:
     *     Returns the actual ConstantInfo element at the specified index
     */
    public Element getCPElement(int index)
    {
        // first get the CPInfo at this index using the standard method above
        Element element = getElement(index);
        if(element == null)
            return null;
        
        // now get the subElement of this CPInfo (the Tag is at index 0)
        if (element.size() > 0)
            return element.get(1);
        else
            return null;
    }

    /////////////////////////////////////////////////
    //
    //   Lets whip things up a notch and employ Collections
    //   to better manage the elements.

    /*
    public <E extends Element> Set<E> getInfoSet(Class<E> t) {
        Set<E> set = new TreeSet<E>();
        for (Element e : this) {
            if (e instanceof t) {
                
            }
        }
        return new TreeSet<E>();
    }
     */

    public Set<ConstantClassInfo> getClassInfoSet() {
        Set<ConstantClassInfo> set = new TreeSet<ConstantClassInfo>();
        for (int i = 1; i < getCount(); i++) {
            Element e = getCPElement(i);
            if (e instanceof ConstantClassInfo) {
                set.add((ConstantClassInfo)e);
            }
        }

        return set;
    }

    /**
     * Resolve the ClassInfo entries to their UTF8Info entries
     * and return a set of those.
     *
     * @return
     */
    public Set<String> getClassInfoStringSet() {
        Set<String> set = new TreeSet<String>();

        for (int i = 1; i < getCount(); i++) {
            Element e = getCPElement(i);
            if (e instanceof ConstantClassInfo) {
                int index = e.getElement("name_index").getValue();
                ConstantUTF8Info utf8 = (ConstantUTF8Info)this.getCPElement(index);
                set.add(utf8.getString());
            }
        }

        return set;
    }


        
    public String toString()
    {
        return "constant_pool";
    }
    
}
