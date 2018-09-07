/*
 * Linker.java
 *
 * Created on June 8, 2006, 7:15 PM
 *
 * The Linker does a number of things.
 *
 *      1. Sets up a FileInputStream for each Class file it needs to read and reads in a ClassFile object for that class.
 *      2. From each ClassFile it builds a LinkedClass.
 *      3. The ClassReferences are arranged, with Main first, and added to the LinkTable.
 *      4. The LinkTable and ClassFile objects are added to the ProgramMemory.
 *      5. A ProgramOutputStream is created and used to write out the ProgramMemory.
 *
 * Note that in step 4 the compiled/assembled JVM code should be encapsulated in an Element containing a byte array 
 * so that it may also be written out of ProgramMemory along with the LinkTable and ClassFile objects.
 * 
 * Creates a Link Table, which contains statically resolved NVM addresses to classes and/or class structures,
 * as needed.  As inputs to the program, we need a list of class files.  Also need a memory map for the link table,
 * something that the program uses to determine where addresses should be placed.
 *
 * I might encapsulate the memory mapping / writing in some kind of outputstream.  While the tree of Elements in within
 * the top-level ProgramMemory (JVMCode, LinkTable, ClassFiles) each specify the order and relavtive location of bytes 
 * to be written (linear order), they contain no information about relative offsets etc.
 *
 * I can envision multiple types of outputstreams.  One would encapsulate the writing to a HEX file format suitable for downloading
 * to a device's NVM.  The other would just write out the raw bytes for loading into the simulator.  Maybe the same stream
 * can do these tasks, with each option being enabled or not.  Ok, so the outputstream is responsible for writing the bytes...
 * but it needs to know where the bytes are comming from or at least be told, from time to time, to shift the offset.  There needs
 * to be a controller that tells the outputstream to shift the offset as the writeout moves from Top-Level tree to the next
 * (LinkTable, ClassFile, JVMCode, etc).  Perhaps there can be a "TopLevel" class that extends Element.  The TopLevel can have an
 * extra hidden Element at the beginning of its list that takes the outputstream and sets the offset...
 *
 * This is just one solution to one problem.  Another problem is how to generate the LinkTable.  Maybe I just need to put the
 * pieces together (ClassFiles) and then simulate a  "write" out,  having each Element track the output stream offset it was written to.
 * Yes... Each Element has an "offset" variable that is updated on a write out.  The variable just captures the current value of a
 * running counter that adds up the bytes as they are written.
 *
 * Maybe I can create links from Elements in ClassReferences to corresponding Elements in ClassFiles.  These links allow the
 * Elements in the ClassReferences to grab the offset value recorded in the ClassFile Element after the simulated writeout.
 * Now the outputstream can track the offset of bytes written such that when a method is called on it, such as getOffset(),
 * the current file offset is returned.  Thus, in each Element's writeData method, the first thing it does is get the Offset
 * from the passed in OutputStream and stores it in the offset variable.
 *
 * The 'ProgramMemory' is the top level of the heirarchy as shown below.
 *
 *          ProgramMemory
 *                |
 *                ---------------------------------------------
 *                |                                           |
 *          Link Table                                   ClassFiles
 *                |                                           |
 *                ---------------------- ... -------          -------------------- ... ----
 *                |            |                   |          |           |               |
 *           LinkedClass1   LinkedClass2 ... LinkedClassN    ClassFile1 ClassFile2 ... ClassFileN
 * 
 *  Each LinkedClass is associated with a ClassFile.  Before the actual write-out of ProgramMemory to a file,
 *  the ClassFiles is written-out to a JVMOutputStream.
 */

package emr.classfilelinker;

import emr.elements.classfileparser.*;
import emr.elements.classfileparser.cpinfo.*;
import emr.elements.classfileparser.attributes.CodeAttribute;
import emr.elements.linktable.*;
import emr.elements.common.*;

import java.util.*;
import java.util.jar.*;
import java.io.*;

import emr.classfileviewer.ClassViewerUI;


/**
 *
 * @author Ross
 */
public class Linker
{
    
    // make a String array of classes to load...
    String[] classes =
    {"C:/projects/java/ClassFileLinker/build/classes/samples/Main.class",
     "C:/projects/java/ClassFileLinker/build/classes/samples/Class1.class",
    };
         
    
    // This thing is some properties...
    Properties memMap = null;
    
    //String inputFileName = "C:/java/classanalyzer/build/classes/emr/classanalyzer/samples/SampleClass.class";
    //String inputFileName = "C:/java/JLayer1.0/classes/javazoom/jl/decoder/LayerIIIDecoder.class";
    String inputFileName = "C:/projects/java/JLayer1.0/classes/javazoom/jl/decoder/huffcodetab.class";

    String outputFileName = "C:/projects/java/classanalyzer/build/classes/emr/classanalyzer/samples/SampleClassOut.class";
    
    /** Creates a new instance of Linker */
    public Linker()
    {
        
        getMemMap();
        
        // run some testing code...
        //linkTest();
        try {
            link();
        } 
        catch (IOException e)
        {
            log("Link error! " + e.getMessage());
        }
        
    }
    
    public void getMemMap()
    {
        memMap = new Properties();
        memMap.setProperty("LinkTableStart", "4096");
        memMap.setProperty("MainClassStart","4608");  // LinkTableStart + 512 (arbitrary)
        memMap.setProperty("JVMCoreFile","c:/projects/java/jvmcore.bin");
        
    }
    
    
    /* It is the Linker's job to: 
     *    1. Connect the LinkedClass Elements to the ClassFile Elements of interest.
     *    2. Add the ClassReferences to the LinkTable.
     *    3. Add MemoryPads of appropriate length between JVMCore and LinkTable, and
     *       between LinkTable and the Main class.
     */
        
    private void link() throws IOException
    {
        
        log("Begin linking");
        
        // The ProgramMemory represents all programmable flash memory...
        ProgramMemory progMem = new ProgramMemory();
        
        // to which we add a LinkTable...
        LinkTable linkTable = new LinkTable();
        progMem.add(linkTable);
        
        int classStart = Integer.parseInt(memMap.getProperty("MainClassStart"));
        
        // another MemoryPad
        MemoryPad linkPad = new MemoryPad(classStart);
        progMem.add(linkPad);
        
        // and our ClassFiles.
        ClassFiles classFiles = new ClassFiles();
        progMem.add(classFiles);
        
        ClassFile classFile = null;
        // create a LinkedClass for each ClassFile...
        for(int i  = 0; i < classes.length; i++)
        {
            // create a ClassFile
            classFile = new ClassFile();
            // add it to ClassFiles
            classFiles.add(classFile);
            // open an inputstream and read in the class
            classFile.readData(openInputStream(classes[i]));
        
            log("linking ClassFile to ClassReference");
            // link up the LinkedClass to the ClassFile
            LinkedClass classRef = new LinkedClass(classFile);
            linkTable.add(classRef);
            
        }
        
        // Now, a special case.  The address of start of the CodeAttribute of the main() method
        // of the Main class is obtained and added as a MainMethodOffset to the beginning of the LinkTable
        for(int i = 0; i < classFiles.size(); i++)
        {
            classFile = (ClassFile)classFiles.get(i);
            log("Searching classFile for main...");
            if(classFile.isClass("samples/Main"))
            {
                log("Found Main class");
                
                
                Methods methods = (Methods) classFile.getElement("methods");
                
                CodeAttribute codeAttribute = methods.getCodeAttribute("main");

                if(codeAttribute != null)
                {
                    log("Found main method.");
                    log("Adding MainMethodOffset to start of LinkTable");

                    linkTable.add(0, new MainMethodOffset(classFile));
                    
                    break;
                    
                }
                
                
                
            }
        }
        
        
        
        
        
        /**********************
         *  Now do a write out of the ClassFiles with a JVMOutputStream setup with the
         *  classfile offset
         */
        JVMOutputStream gatherOffsets = new JVMOutputStream(classStart);
        classFiles.writeData(gatherOffsets);
        
        /**********************
         *  Now write out the ProgramMemory to a JVMOutputStream initialized with a real OutputStream
         *
         */
        JVMOutputStream outputStream = new JVMOutputStream(openOutputStream("c:/projects/java/nvm.out"));
        progMem.writeData(outputStream);
        
        log("Linking done!");
        

        
        //java.awt.EventQueue.invokeLater(new Runnable() {
        //    public void run() {
                ClassViewerUI classViewer = new ClassViewerUI();
                classViewer.setVisible(true);
                classViewer.buildTreeFromElement(progMem);
        //    }
        //});
        
        
        
    }
    
    
    /////////////////////////////////////////////////////////////////////////
    // Load classfiles from a JAR file
    /////////////////////////////////////////////////////////////////////////
    private void buildTable(LinkTable linkTable, String jarName)
    {
        try
        {
            JarFile jarFile = new JarFile(jarName);
            Enumeration<JarEntry> entries = jarFile.entries();
            
            ClassFile classFile = null;
            while(entries.hasMoreElements())
            {
                JarEntry entry = entries.nextElement();
                if ( entry.getName().endsWith(".class") )
                {
                    log("Building LinkTable from: " + entry.getName());
                    InputStream is = jarFile.getInputStream(entry);
                    classFile = ClassLoader.loadClass(is);
                    
                    // add the class to the linktable
                    linkTable.add(buildLinkedClass(classFile));
                }
            }
        } 
        catch (IOException e)
        {
            ClassLoader.log("Error building table: " + e.getMessage());
        }
        
        
    } 
    
    /////////////////////////////////////////////////
    // Construct a LinkedClass structure from the
    // given ClassFile.
    /////////////////////////////////////////////////
    private Element buildLinkedClass(ClassFile classFile)
    {
        
        LinkedClass linkedClass = new LinkedClass(classFile);
        return linkedClass;
    }
    
    
    
    /** Run the linker */
    public static void main(String[] args)
    {
        new Linker();
    }
    

     
    
    /* A testing method */
    public void linkTest()
    {
        ClassFile classFile = new ClassFile();
        
        // read in a class file then write it out.
        try {
            log("Reading class file.");
            classFile.readData(openInputStream(inputFileName));
            log("Done.");
            
            log("Getting Element offsets.");
            JVMOutputStream oos = new JVMOutputStream(512);
            classFile.writeData(oos);
            
            // lets get a known offset...
            Element e = classFile.getElement("constant_pool_count");
            log("constant pool count offset: " + e.getOffset());
            
            log("Done.");
            
            
            log("Writing class file.");
            classFile.writeData(openOutputStream(outputFileName));
            log("Done.");
        } 
        catch (IOException e)
        {
            System.err.println("Exception: " + e.getMessage());
            return;
        }                
    }
    
    
    
    private FileOutputStream openOutputStream(String name) throws IOException
    {
        File file = new File(name);
        if(file.exists())
        {
            file.delete();
        }        

        FileOutputStream fout = new FileOutputStream(file);
        log("Openned output file " + name);
        
        return fout;
    }
    
    private FileInputStream openInputStream(String name) throws IOException
    {
        File file = new File(name);
        if(!file.exists())
        {
            throw new IOException("File not found: " + name);
        }    
        
        FileInputStream fin = new FileInputStream(file);
        
        log("Reading input file " + name + " : " + file.length() + " bytes.");
        
        return fin;
        
    }
    
    private void log(String message)
    {
        System.out.println("> " + message);
    }
          
    
}
