/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package classpackager.file;

import emr.elements.common.Element;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * FileData references a java.io.File object and writes the contents
 * of that File to the outputstream.
 * 
 * @author Evan Ross
 */
public class FileData extends Element {

    private final String name;
    private final int fileSize;
    File f;

    public FileData(File f) throws IOException {
        name = f.getName();
        fileSize = (int)f.length();
        this.f = f;
        setSize(fileSize); // enable correct package size calc
    }

    @Override
    public void writeElement(OutputStream os) throws IOException
    {
        FileInputStream fin = new FileInputStream(f);
        byte[] buf = new byte[fileSize];
        fin.read(buf);
        os.write(buf);
        buf = null;
        fin.close();
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString()
    {
        return "file_data [" + name + "]";
    }
}
