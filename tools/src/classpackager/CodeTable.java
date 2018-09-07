/*
 * CodeTable.java
 *
 * Created on December 10, 2006, 4:24 PM
 *
 * The CodeTable stores the code for each method plus information needed
 * by the JVM to set up a frame (max stack and max locals).  
 * 
 * The CodeTable must be aligned on a 4-byte boundary so that the JVM may begin
 * executing code bytes at a known location within the 4-byte word read from NVM.
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
public class CodeTable extends Element {
    
    /** Creates a new instance of CodeTable */
    public CodeTable() 
    {
        
    }
    
    /** aligns the CodeTableEntry to a 4-byte boundary before */
    public void add(TableEntry e)
    {
        // add a padding element whose size is determined by
        // the current offset at write time
        super.add( new ByteAligner(4) );
        super.add(e);
    }
    
    public String toString()
    {
        return "code_table";
    }
    
    
}
