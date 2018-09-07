/*
 * TableEntry.java
 *
 * Created on December 16, 2006, 10:09 AM
 *
 * A TableEntry is a super class to all table entry classes.  This class provides implementations
 * for common functions such as setting upper and lower regions of words.
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
public class TableEntry extends Element 
{
    
    private String name;
    
    /** Creates a new instance of TableEntry */
    public TableEntry() 
    {
        name = "default_table_entry";
    }

    public TableEntry(String name) {
        setName(name);
    }
    
    public void setName(String name_)
    {
        name = name_;
    }
    
    /** Given a raw, 4-byte source value, this function places the lower
     * 2 bytes into the upper 2 bytes of the destination word.  The lower
     * 2 bytes of the destination word are untouched. 
     */
    public int setUpper2Bytes(int sourceValue_)
    {
        // shift up 16 bits, clearing lower 16 bits as well
        sourceValue_ <<= 16; 
        
        // clear the upper 2 bytes, then OR them with value_
        int destWord = getValue();
        destWord &= 0x0000ffff;
        destWord |= sourceValue_;
        
        setValue( destWord );
        
        return destWord;
        
    }
    
    /** Places lower 2 bytes of sourceValue_ into lower 2 bytes
     * of this Element's value
     */
    public int setLower2Bytes(int sourceValue_)
    {
        int destWord = getValue();
        
        // clear lower 2 bytes of destWord_
        destWord &= 0xffff0000;
        
        // clear upper 2 bytes of sourceValue_
        sourceValue_ &= 0x0000ffff;
        
        // OR them together
        destWord |= sourceValue_;
        
        setValue( destWord );

        return destWord;
        
    }
    
    public String toString()
    {
        return name;
    }
    
}
