/*
 * Table.java
 *
 * Created on December 17, 2006, 11:50 AM
 *
 * The Table is the super class for all Tables (MethodTable, FieldTable, etc) that need a 
 * null-table entry.
 * Contains implementations of common helper functions 
 *    addNullTableEntry : adds a null entry 
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
public class Table extends Element 
{
    
    /** Creates a new instance of Table */
    public Table() 
    {
    }
    
    /** Adds a null table entry to this Table.  Used to indicate 
     * the end of the table, much like a string's null terminator.
     * Used with simple linear search lookups.
     *
     * I'll put a note in here that this might change if the lookup
     * algorithms are improved with binary search which needs the table
     * length up front.
     */
    public void addNullTableEntry()
    {
        add( new GenericElement(u4, "null_entry") );
    }

    /**
     * For use with more advanced table lookup algorithms, the size
     * of the table (number of entries) is appended to the beginning
     * of the table and is initialized to the size of the table.
     */
    public void insertTableSize() {
        Element e = new GenericElement(u4, "table_size");
        e.setValue(this.size());
        this.insertElementAt(e, 0);
    }

    /**
     * Tables may eventually be uniform in design - a size value is located
     * at the top of the table.  If so, then this method simply updates that
     * element.
     */
    /*
    protected void setTableSize(int newsize) {

    }
    */
    
}
