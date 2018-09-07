/*
 * InterfaceTable.java
 *
 * Created on December 12, 2006, 10:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
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
public class InterfaceTable extends Table {
    
    /* track interface elements for naming purposes */
    private int interfaceCount;
    
    /**
     * Creates a new instance of InterfaceTable
     */
    public InterfaceTable() 
    {
        interfaceCount = 0;
    }
    
    public void addInterfaceIndex(int index_)
    {
        interfaceCount++;
        GenericElement e = new GenericElement(u4, "index-" + interfaceCount);
        e.setValue( index_ );
        add( e );
    }
    
    public String toString()
    {
        return "interface_table";
    }
   
}
