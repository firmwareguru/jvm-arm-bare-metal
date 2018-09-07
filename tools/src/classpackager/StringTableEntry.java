/*
 * StringTableEntry.java
 *
 * Created on April 2, 2008, 6:18 PM
 *
 *
 */

package classpackager;
import emr.elements.common.Element;
import emr.elements.common.GenericElement;
import java.io.IOException;
import java.io.OutputStream;


/**
 * A StringTableEntry is constructed to mimic an Array
 * object as if it was allocated in RAM.
 * 
 * Structure:
 *     Monitor (4)
 *     ClassRef (4) -> Must refer to class String.
 *     ArrayLength (4)
 *     Array...
 * @author Evan Ross
 */
public class StringTableEntry extends GenericElement {
    
    
    /* Reference to a String InternalClass */     
    StringTable stringTable;

    /** Creates a new instance of StringTableEntry */
    public StringTableEntry(String s, StringTable stringTable_)
    {
        super(0, s);

        stringTable = stringTable_;

        this.setMetaData(s);
 
        Element monitor = new GenericElement(4, "monitor_pad");
        Element classRef = new GenericElement(4, "class_ref");
        
        Element length = new GenericElement(4, "string_length");
        Element bytes = new ByteArray(s.getBytes(), "string_bytes");
        
        length.setValue(s.length());
        
        add(monitor);
        add(classRef);
        add(length);
        add(bytes);
    }
    
    /**
     * Set the String InternalClass reference offset
     * 
     * @param os
     * @throws IOException
     */
    @Override
    public void writeElement(OutputStream os) throws IOException
    {
        InternalClass stringClassRef = stringTable.getStringClassRef();

        if (stringClassRef == null)
            throw new IOException("StringTableEntry error.");
        
        // We need the start of the header which is padded to maintain 4 byte alignment.
        getElement("class_ref").setValue(stringClassRef.getHeader().getAlignedOffset(4));
    }

}
