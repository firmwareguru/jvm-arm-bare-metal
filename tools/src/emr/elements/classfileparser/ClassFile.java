/*
 * ClassFile.java
 *
 * Created on May 29, 2006, 9:34 PM
 *
 * This class is the top-level container in the Class file heirarchy.
 *
 * It has some accessor functions:
 *
 *     readClass(InputStream)
 *     writeClass(OutputStream)
 *     getElement(name)
 *
 */

package emr.elements.classfileparser;


import emr.elements.classfileparser.common.AccessFlags;
import emr.elements.classfileparser.common.Attributes;
import emr.elements.classfileparser.cpinfo.ConstantUTF8Info;
import emr.elements.classfileparser.cpinfo.ConstantClassInfo;

import emr.elements.common.Element;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


import java.io.FileInputStream;
import java.io.File;

/**
 *
 * @author Ross
 */
public class ClassFile extends Element {
    
    //public static boolean debug = true;
        
    /** Build the top-level Elements of a ClassFile */
    public ClassFile() 
    {
        /* create the tree */
        add(new MagicNumber());
        add(new MinorVersion());
        add(new MajorVersion());
        
        ConstantPool constantPool = new ConstantPool();
        add(constantPool);

        add(new AccessFlags());
        add(new ThisClass());
        add(new SuperClass());
        
        add(new Interfaces());
        
        // The following need links to the ConstantPool because they have internal attributes
        // that need to pull the name out of the CP in order to determine what the attribute is.
        add(new Fields(constantPool));
        add(new Methods(constantPool));
        add(new Attributes(constantPool));
        
    }
    
    @Override
    public String toString()
    {
        //return "class_file";
        return getName();
    }
    
    //////////////////////////////////////////////////////////////////////////
    // Helpers
    //////////////////////////////////////////////////////////////////////////
    
    /** Return ThisClass element */
    public Element getThisClass()
    {
        return getElement("this_class");
    }
    
    /** Return reference to SuperClass element */
    public Element getSuperClass()
    {
        return getElement("super_class");
    }
    
    /** Return refernce to the MajorVersion element */
    public Element getMajorVersion()
    {
        return getElement("major_version");
    }
    
    /** Return reference to MinorVersion element */
    public Element getMinorVersion()
    {
        return getElement("minor_version");
    }
    
    /** Return reference to MagicNumber element */
    public Element getMagicNumber()
    {
        return getElement("magic_number");
    }
    
    /** Return reference to AccessFlags */
    public Element getAccessFlags()
    {
        return getElement("access_flags");
    }
    
    
    
    /** Returns a reference to the ConstantPool */
    public ConstantPool getConstantPool()
    {
        ConstantPool cp = (ConstantPool)getElement("constant_pool");
        return cp;
    }
    
    /** Returns a reference to the Methods table */
    public Methods getMethods()
    {
        Methods m = (Methods)getElement("methods");
        return m;
    }
    
    /** Returns a reference to the Fields table */
    public Fields getFields()
    {
        Fields f = (Fields)getElement("fields");
        return f;
    }
    
    /** Returns a reference to the Interfaces table */
    public Interfaces getInterfaces()
    {
        Interfaces i = (Interfaces)getElement("interfaces");
        return i;
    }


    private String cachedName = null;
    public String getName()
    {
        if (cachedName == null) {
            ConstantPool cp = getConstantPool();

            // get the ConstantClassInfo
            int thisClassIndex = getElement("this_class").getValue();
            Element classInfo = cp.getCPElement(thisClassIndex);
        
            // get the ConstantUTF8Info
            Element nameIndex = classInfo.getElement("name_index");

            int utf8index = nameIndex.getValue();
            Element utf8info = cp.getCPElement(utf8index);

            cachedName = ((ConstantUTF8Info)utf8info).getString();
        }

        return cachedName;
    }

    private String cachedSuperClassName = null;
    public String getSuperClassName()
    {

        if (cachedSuperClassName == null)
        {
            ConstantPool cp = getConstantPool();

            // get the ConstantClassInfo
            int thisClassIndex = getElement("super_class").getValue();
            if( thisClassIndex == 0)
                return null; // no super class

            Element classInfo = cp.getCPElement(thisClassIndex);

            // get the ConstantUTF8Info
            Element nameIndex = classInfo.getElement("name_index");

            int utf8index = nameIndex.getValue();
            Element utf8info = cp.getCPElement(utf8index);

            cachedSuperClassName = ((ConstantUTF8Info)utf8info).getString();
        }

        return cachedSuperClassName;
        
    }
    
    /***************
     *  Helper Function:
     *     Return true if this ClassFile corresponds to the given name
     */
    public boolean isClass(String name)
    {
        
        
        if(debug)
            System.out.println("isClass " + name + "?");
        ConstantPool cp = getConstantPool();
     
        // get the ConstantClassInfo
        int thisClassIndex = getElement("this_class").getValue();
        Element classInfo = cp.getCPElement(thisClassIndex);
        if( ! (classInfo instanceof ConstantClassInfo))
            return false;
        
        // get the ConstantUTF8Info
        Element nameIndex = classInfo.getElement("name_index");
        if(nameIndex == null)
            return false;
        int utf8index = nameIndex.getValue();
        Element utf8info = cp.getCPElement(utf8index);
        
        if(utf8info instanceof ConstantUTF8Info)
        {
            String thisClassName = ((ConstantUTF8Info)utf8info).getString();
            
            if(debug)
                System.out.println("isClass comparing: " + thisClassName + " and " + name);
            
            if(name.equalsIgnoreCase(thisClassName))
                return true;
        }
        
        return false;
        
        
    }
    
    /* END ******************************************************************************************************/
    
    
    /** TEST */
    public static void main(String[] args)
    {
        //String basePath = "C:/java/classanalyzer/build/classes/emr/classanalyzer/samples/SampleClass.class";
        String basePath = "C:/java/JLayer1.0/classes/javazoom/jl/decoder/LayerIIIDecoder.class";
        if(debug) System.out.println("running classfile test");
        
        ClassFile classFile = new ClassFile();
        
        try {

            File cf = new File(basePath);
            if(!cf.exists())
                return;

            if(debug) System.out.println("file " + basePath + " exists");
        
            FileInputStream fin = new FileInputStream(cf);
            classFile.readData(fin);
            
            for(int i = 0; i < classFile.size(); i++)
            {
                Element e = classFile.get(i);
            
                if(e != null)
                {
                    
                    System.out.println("Element name: " + e.toString() + " value " + e.getValue());
                    if(e instanceof ComplexElement)
                        System.out.println("  count: " + ((ComplexElement)e).getCount());
                }
                else
                    if(debug) System.out.println("Element null");
            }

           
            fin.close();
        } catch(IOException e) {
            System.out.println("Exception: " + e.getClass().getName());
            System.out.println("           " + e.getMessage());
        }
    }
    
    
    
}
