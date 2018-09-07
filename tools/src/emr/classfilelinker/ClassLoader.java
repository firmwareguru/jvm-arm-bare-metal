/*
 * ClassLoader.java
 *
 * Created on September 20, 2006, 9:32 PM
 *
 * A ClassLoader loads a .class file into memory and returns a ClassFile object.
 */

package emr.classfilelinker;

import emr.elements.classfileparser.*;
import emr.elements.common.*;
import java.io.*;

/**
 *
 * @author Ross
 */
public class ClassLoader {
    
    /** Creates a new instance of ClassLoader */
    public ClassLoader() 
    {
    }
    
    public static ClassFile loadClass(InputStream in)
    {
        ClassFile classFile = null;
        // try to open the file
        try 
        {
            classFile = new ClassFile();
            classFile.readData(in);
        }
        catch (IOException e)
        {
            log("Error reading classfile: " + e.getMessage());
        }
        return classFile;
    }
    
    public static ClassFile loadClass(String file)
    {
        ClassFile classFile = null;
        // try to open the file
        try 
        {
            FileInputStream fin = openInputStream(file);
            classFile = new ClassFile();
            classFile.readData(fin);
        }
        catch (IOException e)
        {
            log("Error reading classfile: " + e.getMessage());
        }
        return classFile;
    }
    
    public static ClassFile loadClass(File file)
    {
        ClassFile classFile = null;
        // try to open the file
        try 
        {
            FileInputStream fin = openInputStream(file);
            classFile = new ClassFile();
            classFile.readData(fin);
        }
        catch (IOException e)
        {
            log("Error reading classfile: " + e.getMessage());
        }
        return classFile;
        
    }
    
    private static FileInputStream openInputStream(String name) throws IOException
    {
        File file = new File(name);
        return openInputStream(file);
    }
    
    private static FileInputStream openInputStream(File file) throws IOException
    {
        if(!file.exists())
        {
            throw new IOException("File not found: " + file.getName());
        }    
        FileInputStream fin = new FileInputStream(file);
        log("Reading input file " + file.getName() + " : " + file.length() + " bytes.");
        return fin;
    }
    
    public static void log(String message)
    {
        System.out.println("> " + message);
    }
}
