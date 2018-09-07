/*
 * Packager.java
 *
 * Created on December 8, 2006, 7:41 PM
 * 
 * The ClassPackager is a key component of the JVM implementation.  The ClassPackager 
 * builds, from a collection of compiled .class files, a binary file containing class
 * data formated specifically for the JVM.
 *
 * The Packager class is the main entry point into this application.  The tasks that
 * are performed to create the Package are:
 *    1.  build a tree of ClassFiles from .class files contained in specified collection
 *        of .jar files
 *    2.  Take information from that tree and build the InternalClasses tree
 *    3.  
 *    
 *
 */

package classpackager;

import classpackager.file.Files;
import emr.elements.common.GenericElement;
//import java.util.jar.*;
import emr.elements.common.Element;
import emr.elements.common.JVMOutputStream;

import emr.elements.classfileparser.*;
import emr.elements.classfileparser.cpinfo.*;
import emr.elements.classfileparser.attributes.CodeAttribute;
import java.util.Enumeration;
import java.util.List;
import java.util.Iterator;
import java.util.Vector;

import emr.classfileviewer.ClassViewerUI;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
/**
 *
 * @author Ross
 */
public class Packager 
{

    /* The tree of input ClassFiles */
    private ClassFiles classFiles;

    /* The tree of output InternalClasses */
    private InternalClasses internalClasses;

    /* The Package */
    private Package classPackage;
    
    /* Installed NativeMethodLibraries */
    private static Vector<NativeMethodLibrary> nativeLibraries;

    static
    {
        nativeLibraries = new Vector<NativeMethodLibrary>();
    }
    
    /** Creates a new instance of Packager.
     */
    public Packager() 
    {
        classFiles = new ClassFiles();
        classPackage = new Package();
     
    }
    
    /** Installs a NativeMethodLibrary into the Packager */
    public static void installNativeLibrary(NativeMethodLibrary lib_)
    {
        nativeLibraries.add(lib_);
    }
    
    /** Looks up a Native method in all installed NativeMethodLibraries */
    public static CodeAttribute lookupNativeMethod(String className_, String methodName_, String methodDescriptor_) throws IOException
    {
        log("Native method lookup: " + className_ + " " + methodName_ + " " + methodDescriptor_);
        for( int i = 0; i < nativeLibraries.size(); i++ )
        {
            log("  library " + i);
            NativeMethodLibrary nml = nativeLibraries.get(i);
            CodeAttribute attr = nml.lookup(className_, methodName_, methodDescriptor_);
            if( attr != null)
                return attr;  // found it
        }
        
        // didn't find anything throw IOException
        throw new IOException("Native method lookup failed.");
    }
     
    
    /** Creates a package of classes for the JVM.
     *  Requires a List of full path names of Jar files in the file system.
     *  These Jar files contain classes that are to "packaged".
     */
    public void createPackage(DefaultListModel jarFileNames, String outputFileName_, int offset_)
    {
        
        // load in the .class files from the jars
        buildClassFileTree(jarFileNames);
        
        // convert the ClassFiles into the internal Class structures
        ClassBuilder classBuilder = new ClassBuilder();
        internalClasses = (InternalClasses) classBuilder.buildClasses(classFiles);
        
        // Now we need to build the ClassOffsetTable.  Add one entry for each InternalClass
        // and link it to that InternalClass.
        //Element offsetTable = buildClassOffsetTable();
        
        // Create the Package now -
        //    Manually assemble the Package header
        //    - offset of startup class
        //    - offset of startup method name
        //    - offset of startup method descriptor
        //    - offset of file table

        // Setup the reference to the Initialization class
        classPackage.setStartupClass(classBuilder.getInitializationClass());
        
        // Setup the name of the method to start
        classPackage.setStartupMethodName("run");
        
        // Setup the descriptor of the method to start
        classPackage.setStartupMethodDescriptor("()V");

        // Setup the file offset table


        // End header assembly

        // add list of Classes
        classPackage.addInternalClasses(internalClasses);

        // One of the last things we do is simulate a write-out of each InternalClass to a
        // JVMOutputStream to establish the offsets of structures.
        
        JVMOutputStream offsetOut = new JVMOutputStream(null);
        
        try
        {
            // write it out once to establish the offsets of each element
            offsetOut.setOffset(offset_);
            classPackage.writeData(offsetOut);
        
            // write it out again to transfer the offsets to appropriate elements
            offsetOut.setOffset(offset_);
            classPackage.writeData(offsetOut);
        }
        catch (IOException e)
        {
            //log("ack! " + e.getMessage());
            //System.exit(1);
            e.printStackTrace();
        }
        
        // Finally, write out the package to the package file
        writePackage(outputFileName_);
        
        log("<Package> Total package size is " + classPackage.getTreeSize(true) + " bytes.");
        log("<Package> Original ClassFiles size is " + classFiles.getTreeSize(true) + " bytes.");  
        
    }
    

    
    /**
     * Loads ClassFiles from each Jar in the provided List
     * Strictly speacking this doesn't have to be jar files.
     * If this isn't a jar file, it is appended to the package as
     * a file and added to the file table.
     *
     * But, if the file ends in .ser, it is considered a serialized,
     * serialized (yes, double), object.  The first serialized pertains
     * to the PC's VM (running the Packager), in that it is a serialized
     * Element graph representing the object (or array) structure
     * that constitutes a serialized object or array in the VM.  The
     * Element graph is de-serialized and appended to the ClassPackage
     * as a "raw" file (i.e., without any File wrapping).
     */
    private void buildClassFileTree(DefaultListModel jarFileNames)
    {
        // iterate through the list of jar files
        for (int jar = 0; jar < jarFileNames.getSize(); jar++)
        {
            String fileName = (String)jarFileNames.get(jar);

            // if this is a Jar... load it
            if (fileName.endsWith(".jar")) {
                loadClassFilesFromJar(fileName);
            } else if (fileName.endsWith(".ser")) { // special serialized object
                deserializeVMObjectFile(fileName);
            } else { // regular file
                try {
                    classPackage.addFile(new File(fileName));
                } catch (IOException ex) {
                    
                }
            }
        }

        // We have processed all possible input files, now terminate
        // the file table with a null entry (like all other tables)
        classPackage.getFileTable().addNullTableEntry();
    }
    
    /** Populates the ClassOffsetTable with a ClassOffsetEntry for each
     * InternalClass, and returns the new ClassOffsetTable
     */
    private Element buildClassOffsetTable()
    {
        ClassOffsetTable offsetTable = new ClassOffsetTable();
        
        // for each InternalClass, add an entry
        for( int i = 0; i < internalClasses.size(); i++ )
        {
            Element internalClass = internalClasses.get(i);
            
            // link the entry to the class
            ClassOffsetTableEntry entry = new ClassOffsetTableEntry( internalClass );
            
            // add the entry to the table...
            offsetTable.add( entry );
            
        }
        
        // terminate with a null table entry
        offsetTable.addNullTableEntry();
        
        return offsetTable;        
    }
    
    private void writePackage(String fileName_)
    {
        log("<Package> Writing Package to file " + fileName_ + " ...");
        
        OutputStream os = openOutputStream(fileName_);
        if(os != null) {
            try {
                classPackage.writeData( os );
                log("<Packager> Done!");
                os.close();
            }
            catch (IOException e) {
                log("<Packager> Error writing stream!");
            }
        } else {
            log("<Packager> failed!");
        }
    }    
    
    /** Open an OutputStream for the given file name */
    private OutputStream openOutputStream(String outputFileName)
    {
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(outputFileName);
        } catch (IOException e) {
            log("Error openning output stream for file " + outputFileName + " : " + e.getMessage());
        }
        
        return fout;
    }
    

    /////////////////////////////////////////////////////////////////////////
    // Load classfiles from a JAR file and append them to the ClassFileTree.
    // Code for native methods is resolved here.  The code bytes are retrieved
    // from a NativeMethodLibrary and read into a Code structure.
    // and added to the CodeAttribute
    // of the ClassFile's MethodInfo structure.  The native attribute is then
    // cleared.
    //
    // If there are any native methods in any class in the jar, then
    // a 
    /////////////////////////////////////////////////////////////////////////
    private void loadClassFilesFromJar(String jarName)
    {
        log("Reading " + jarName + " ...");
        try
        {
            java.util.jar.JarFile jarFile = new java.util.jar.JarFile(jarName);
            Enumeration<java.util.jar.JarEntry> entries = jarFile.entries();
            
            ClassFile classFile = null;
            while(entries.hasMoreElements())
            {
                java.util.jar.JarEntry entry = entries.nextElement();
                // Process .class files
                if (entry.getName().endsWith(".class"))
                {
                    log("   Loading ClassFile from: " + entry.getName());
                    InputStream is = jarFile.getInputStream(entry);
                    classFile = ClassLoader.loadClass(is);
                    
                    // add the class to the ClassFileTree
                    classFiles.add(classFile);
                }

                
            }
        } 
        catch (IOException e)
        {
            log("Error building table: " + e.getMessage());
        }       
        
    } 
    
    public Element getClassFileTree()
    {
        return classFiles;
    }
    
    public Element getInternalClassTree()
    {
        return internalClasses;
    }
    
    public Element getPackage()
    {
        return classPackage;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
       
        // Create a gui around the packager
        PackagerGUI gui = PackagerGUI.startGUI();
        
        gui.addLibrary("c:/projects/jvm/jvmcorelibrary/dist/jvmcorelibrary.jar"); // the jvm core libraries
        //gui.addLibrary("c:/projects/jvm/sudokusolver/dist/sudokusolver.jar"); // the jvm core libraries
        //gui.addLibrary("c:/projects/java/classpackager/dist/classpackager.jar"); //
        gui.addLibrary("c:/projects/jvm/jvmsoundlibrary/dist/jvmsoundlibrary.jar");
        gui.addLibrary("c:/projects/java_stuff/jlayerme4vm/dist/jlayerme4vm.jar");
        //gui.addLibrary("c:/projects/java_stuff/jlayerme4vm/accessed_sample.mp3"); // Jlayer can't play this
        gui.addLibrary("c:/projects/java_stuff/jlayer1.0/march.mp3");

        
        gui.setDefaultPackageName("c:/projects/jvm/javavirtualmachine/corelibrary.package");
        

    }

    
    
    public static void log(String msg)
    {
        System.err.println(msg);
    }

    private void deserializeVMObjectFile(String fileName) {
        try {
            ObjectInputStream oin = new ObjectInputStream(new FileInputStream(fileName));
            Element e = (Element)oin.readObject();

            // Add this to the package's file table using just the simple name...
            File f = new File(fileName);
            classPackage.addFile(f.getName(), e);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Packager.class.getName()).log(Level.SEVERE, fileName + " " + ex.getMessage(), ex);

        } catch (IOException ex) {
            Logger.getLogger(Packager.class.getName()).log(Level.SEVERE, fileName + " " + ex.getMessage(), ex);
        }
    }
    
}
