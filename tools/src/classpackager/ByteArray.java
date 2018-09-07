/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package classpackager;

import emr.elements.common.GenericElement;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Evan Ross
 */
public class ByteArray extends GenericElement
{
    
    byte[] bytes;
    
    public ByteArray(byte[] bytes_, String name_)
    {
        super(bytes_.length, name_);
        bytes = bytes_;
    }
    
    @Override
    public void writeElement(OutputStream os) throws IOException
    {
        os.write(bytes);
    }    

}
