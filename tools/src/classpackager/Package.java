/*
 * Package.java
 *
 * Created on December 18, 2006, 8:06 PM
 *
 */

package classpackager;
import classpackager.file.FileData;
import classpackager.file.FileTable;
import classpackager.file.Files;
import emr.elements.common.Element;
import emr.elements.common.GenericElement;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
/**
 * The Package contains a self-contained collection of InternalClasses as
 * well as files.
 *
 * Package format:
 *
 *      Header
 *         - offset of startup class
 *         - offset of startup method name
 *         - offset of startup method descriptor
 *         - file table
 *      InternalClasses
 *      StringTable
 *      Files
 *
 * The StringTable is a Package-global pool of strings that are referenced
 * by elements of the file table as well as from InternalClasses.
 *
 * @author Evan Ross
 */
public class Package extends Element
{
    
    /* The Package's Header reference */
    PackageHeader header;

    /* The Package's list of InternalClasses */
    InternalClasses internalClasses;

    /* The pool of strings in the package */
    StringTable strings;

    /* The Package's list of files */
    Files files;

    /** Creates a new instance of Package */
    public Package()
    {
        add(header = new PackageHeader());
        add(internalClasses = new InternalClasses());
        add(strings = new StringTable());
        add(files = new Files());
    }

    public FileTable getFileTable() {
        return header.getFileTable();
    }

    public StringTable getStringTable() {
        return strings;
    }

    /**
     * Package Header initialization:
     * Set the startup internalclass reference
     *
     * @param c
     */
    public void setStartupClass(InternalClass c) {
        header.getClassOffsetEntry().setInternalClass(c);
    }

    /**
     * Package Header initialization:
     * Set the startup method name
     * @param name
     */
    public void setStartupMethodName(String name) {
        header.getStartupMethodName().setValue(name.hashCode());
    }

    /**
     * Package Header initialization:
     * Set the startup method descriptor
     * @param descriptor
     */
    public void setStartupMethodDescriptor(String descriptor) {
        header.getStartupMethodDescriptor().setValue(descriptor.hashCode());
    }

    /**
     * Add a file to the package whose contents are located on the file system.
     *
     * @param f the file to add.
     */
    public void addFile(File f) throws IOException {
        // Create a FileData object to encapsualte the File data,
        // Link it, along with the File, to the FileTable.
        // Also create a string in the StringTable for the file name.

        FileData fd = new FileData(f);
        header.getFileTable().addFileEntry(addString(f.getName()), fd);

        // file data is added to the package root, although it can be added
        // arbitrarily anywhere.
        // Align them to 4 byte boundaries, though not sure if this is really
        // necessary.
        files.add(new ByteAligner(4));
        files.add(fd);
    }

    /**
     * Add a file to the package that is represented by an Element graph.
     *
     * @param e
     * @throws IOException
     */
    public void addFile(String fileName, Element e) throws IOException {

        header.getFileTable().addFileEntry(addString(fileName), e);

        // Now append the Element graph to the files section
        files.add(new ByteAligner(4));
        files.add(e);
    }

    /**
     * Add internal classes to the pre-existing root element.
     * The purpose is to impose some order on the package elements,
     * that is, the files are always appended after the internal classes.
     * @param e
     */
    public void addInternalClasses(Element e) {
        internalClasses.addAll(e);
    }

    /**
     * Add the string to the StringTable.  Return
     * a StringTableEntry for the string.
     *
     * Only one string and StringTableEntry exist for
     * each string.
     * 
     * @param s
     * @return
     */
    public StringTableEntry addString(String s) {

        return strings.getStringTableEntry(s);
    }

    /**
     * Do some housekeeping that can only be done on the fly.
     *
     * @param os
     */
    @Override
    public void writeElement(OutputStream os) throws IOException
    {
        /* Need to set the StringClassRef in the StringTable */
        strings.setStringClassRef(internalClasses.getInternalClass("java/lang/String"));

        System.err.println("Writing Package. " + internalClasses.getInternalClass("java/lang/String"));

        super.writeElement(os);
    }

    
    @Override
    public String toString()
    {
        return "package";
    }




    /**
     * The PackageHeader is a container for the header elements.
     *
     */
    private static final class PackageHeader extends Element {


        public PackageHeader() {

            add(new ClassOffsetTableEntry());
            add(new GenericElement(4, "startup_method_name"));
            add(new GenericElement(4, "startup_method_descriptor"));
            add(new FileTable());
        }



        public ClassOffsetTableEntry getClassOffsetEntry() {
            return (ClassOffsetTableEntry)get(0);
        }

        public Element getStartupMethodName() {
            return get(1);
        }

        public Element getStartupMethodDescriptor() {
            return get(2);
        }

        public FileTable getFileTable() {
            return (FileTable)get(3);
        }


        @Override
        public String toString() {
            return "package_header";
        }
    }
    
}
