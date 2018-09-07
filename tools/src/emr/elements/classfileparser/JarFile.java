/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package emr.elements.classfileparser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * A JarFile is a list of ClassFile objects (hence is a subclass
 * of ClassFiles).  It overrides readElement to find ClassFile objects
 * in the inputstream from a JarFile.
 *
 * @author Evan Ross
 */
public class JarFile extends ClassFiles {

    @Override
    public void readElement(InputStream is)
    {
        // This better be a JarInputStream!
        if ((is instanceof JarInputStream) == false)
            return;

        JarInputStream jis = (JarInputStream)is;

        //log("Reading " + jarName + " ...");
        try
        {
            java.util.jar.JarEntry entry;
            while((entry = jis.getNextJarEntry()) != null)
            {
                // Process .class files
                if (entry.getName().endsWith(".class"))
                {
                    System.out.println("   Loading ClassFile from: " + entry.getName());
                    ClassFile classFile = new ClassFile();
                    classFile.readData(jis);

                    // add the class to the ClassFileTree
                    add(classFile);
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Error building table: " + e.getMessage());
        }
    }
}
