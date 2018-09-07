/*
 * ConstantPoolTableEntry.java
 *
 * Created on December 17, 2006, 5:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package classpackager;

import emr.elements.classfileparser.ThisClass;
import emr.elements.common.Element;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
/**
 *
 * @author Ross
 */
public class ConstantPoolTableEntry extends TableEntry 
{

    /* An entry for a UTFInfo may contain an actual reference
     * to an InternalClass object for resolution when writing out.
     */
    Element reference = null;
    
    /**
     * The alignment offset required of the reference.
     * Default to 4.
     */
    int alignmentOffset;
    
    /** Creates a new instance of ConstantPoolTableEntry */
    public ConstantPoolTableEntry() 
    {
        this(4); // default to 4 byte alignment offset requirement.
    }
    
    public ConstantPoolTableEntry(int alignmentoffset)
    {
        setSize(u4);  // entries are 4 bytes.
        this.alignmentOffset = alignmentoffset;                 
    }
    
    public void setAlignmentOffset(int offset)
    {
        alignmentOffset = offset;
    }
    
    /** Set a reference object from which an offset is obtained at write-time */
    public void setReference(Element ref_)
    {
        reference = ref_;
    }
    
    public void writeElement(OutputStream os) throws IOException
    {
  
        // If a reference has been set, then set the value to that object's offset
        if( reference != null )
            setValue( reference.getAlignedOffset(alignmentOffset) );
        
        // call Element's standard writeElement
        super.writeElement(os);
    }
    
    
    
}
