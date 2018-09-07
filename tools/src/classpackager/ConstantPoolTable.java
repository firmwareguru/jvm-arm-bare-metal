/*
 * ConstantPoolTable.java
 *
 * Created on December 10, 2006, 4:23 PM
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
public class ConstantPoolTable extends Element 
{
    int entryCount;
    
    /**
     * Creates a new instance of ConstantPoolTable
     */
    public ConstantPoolTable() 
    {
        entryCount = 0;
    }
    
    /** Add a ConstantPoolTableEntry.  This method
     * gives the entry a custom name useful only
     * when viewing in the ClassFile Viewer.
     */
    public void add(TableEntry entry_)
    {
        entry_.setName("entry-" + entryCount);
        entryCount++;
        super.add( entry_ );
    }
    
    public String toString()
    {
        return "constant_pool_table";
    }
    
    
}
