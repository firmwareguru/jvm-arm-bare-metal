/*
 * ConstantUTF8Info.java
 *
 * Created on May 31, 2006, 6:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.elements.classfileparser.cpinfo;

import emr.elements.common.Element;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 *
 * @author Ross
 */
public class ConstantUTF8Info extends Element
{
    
    String utf8String;
    
    /** Creates a new instance of ConstantUTF8Info */
    public ConstantUTF8Info()
    {
        add(new Length());
    }
    
    public void readChildren(InputStream is) throws IOException
    {
        get(0).readData(is);
        
        int length = get(0).getValue();
        
        byte[] bytes = new byte[length];
        is.read(bytes);
        
        utf8String = new String(bytes);
        if(debug) System.out.println(utf8String);
    }
    
    public void writeChildren(OutputStream os) throws IOException
    {
        get(0).writeData(os);
        
        byte[] bytes = utf8String.getBytes();
        
        if(bytes.length != get(0).getValue())
            throw new IOException("Cannot write utf8 string: size incorrect.");
        
        os.write(bytes);
    }
    
    public int getLength()
    {
        return get(0).getValue();
    }
    
    public String getString()
    {
        return utf8String;
    }
    
    public void setString(String newString)
    {
        utf8String = newString;
    }
    
    public String toString()
    {
        return "CONSTANT_Utf8_info";
    }
    
    
    
}
