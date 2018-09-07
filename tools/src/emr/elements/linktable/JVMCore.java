/*
 * JVMCore.java
 *
 * Created on June 26, 2006, 9:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.elements.linktable;

import emr.elements.linktable.*;
import emr.elements.common.Element;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
/**
 *
 * @author Ross
 */
public class JVMCore extends Element
{
    
    byte[] coreData = null;
    
    /** Creates a new instance of JVMCore */
    public JVMCore()
    {
        
    }
    
    public void readElement(InputStream is) throws IOException
    {
        coreData = new byte[is.available()];
        setSize(is.available());
        is.read(coreData);
        
    }
    
    public void writeElement(OutputStream os) throws IOException
    {
        os.write(coreData);
    }
    
    public String toString()
    {
        return "jvm_core";
    }
    

    
}
