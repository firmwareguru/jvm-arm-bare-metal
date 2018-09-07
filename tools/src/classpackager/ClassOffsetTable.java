/*
 * ClassOffsetTable.java
 *
 * Created on December 18, 2006, 7:53 PM
 *
 * The ClassOffsetTable contains an array of ClassOffsetTableEntries.  These entries
 * contain a 4-byte relative address of an InternalClass; that is, the entries contain
 * an address relative to the beginning of the Package.
 */

package classpackager;
import emr.elements.common.Element;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
/**
 *
 * @author Evan Ross
 */
public class ClassOffsetTable extends Table
{
    
    /** Creates a new instance of ClassOffsetTable */
    public ClassOffsetTable() 
    {
    }
    
    public String toString()
    {
        return "class_offset_table";
    }
    
}
