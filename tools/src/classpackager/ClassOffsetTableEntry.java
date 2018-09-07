/*
 * ClassOffsetTableEntry.java
 *
 * Created on December 18, 2006, 7:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package classpackager;
import emr.elements.common.Element;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
/**
 *
 * @author Ross
 */
public class ClassOffsetTableEntry extends TableEntry
{
    
    /* Reference to the InternalClass that this entry refers to */
    Element internalClass;
    
    /** Creates a new instance of ClassOffsetTableEntry */
    public ClassOffsetTableEntry(Element internalClass_) 
    {
        setSize(u4);
        internalClass = internalClass_;
    }
    
    public ClassOffsetTableEntry() {
        this(null);
    }

    public void setInternalClass(InternalClass c) {
        internalClass = c;
    }
    
    public void writeElement(OutputStream os) throws IOException
    {
        if (internalClass == null)
            throw new IOException("ClassOffsetTableEntry not configured.");
        
        setValue(internalClass.getElement("header").getAlignedOffset(4));
        
        // call Element's standard writeElement
        super.writeElement(os);
    }
    
    public String toString()
    {
        return "class_offset";
    }
    
}
