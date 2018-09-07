/*
 * FieldTableEntry.java
 *
 * Created on December 10, 2006, 4:24 PM
 *
 * Format of FieldTableEntry:
 *   word 0 : name index (2 bytes)
 *   word 1 : descriptor index (2 bytes)
 *   word 2 : flags (2 bytes)
 *   word 3 : field constant index (2 bytes)  <- this is an index into the Constant Pool Table and is
 *     set only if the field has a ConstantValue attribute.
 *   word 4 : size of field in 32-bit words: Singles are 1, while Doubles are 2.
 *
 */

package classpackager;
import emr.elements.common.Element;
import emr.elements.common.GenericElement;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 *
 * @author Ross
 */
public class FieldTableEntry extends TableEntry {
    
    /** Creates a new instance of FieldTableEntry */
    public FieldTableEntry() 
    {
        add( new GenericElement(u2, "name_index"));
        add( new GenericElement(u2, "descriptor_index"));
        add( new GenericElement(u2, "flags"));
        add( new GenericElement(u2, "constant_index"));
        add( new GenericElement(u2, "field_size"));
        add( new GenericElement(u2, "field_index"));
        
        
    }
    
    public void setNameIndex(int value_)
    {
        getElement("name_index").setValue(value_);
    }
    
    public void setDescriptorIndex(int value_)
    {
        getElement("descriptor_index").setValue(value_);
    }
    
    public void setFlags(int value_)
    {
        getElement("flags").setValue(value_);
    }
    
    public void setConstantIndex(int value_)
    {
        getElement("constant_index").setValue(value_);
    }
    
    public void setFieldSize(int size_)
    {
        getElement("field_size").setValue(size_);
    }
    
    public void setFieldIndex(int index_)
    {
        getElement("field_index").setValue(index_);
    }
    
    public String toString()
    {
        return "field_table_entry";
    }
    
    
}
