/*
 * LinkTable.java
 *
 * Created on June 11, 2006, 5:10 PM
 *
 * The link table is a structure in NVM that acts like a table on contents to a class file.  The purpose of the link table
 * is to improve the efficiency of the JVM field/method/class resolution process.  If addresses to structures are known
 * ahead of time, the JVM can avoid having to search a class file for a particular element constantly looking up and comparing 
 * UTF8Info structures in the constant pool.  On a small memory and speed constrained device, this process is too costly.
 *
 * The JVM knows of only one NVM address - the start of the Link Table.  From the link table the JVM can directly determine
 * where the various classes and their sub structures reside.  Address entries in the link table are 16bits - suitable for 
 * NVM up to 64K. 
 *
 * The LinkTable class extends Element and follows the same construction technique as in a ClassFile.
 * The LinkTable class encapsulates the array of bytes containing the link table.  Typically, classes to be statically linked are 
 * read in and organized, and the link table is formed by determining the memory addresses of relevant class structures.  The table
 * is then written out to a device loading file as the first structure  in the "user space", followed by all the classes that it
 * referes to.  The classes themselves may or may not be modified before they are witten.  (User space refers to the region of NVM
 * that is not occupied by the compiled JVM).
 *
 * The link table structure is static, ie no variable length fields.  Classes pointed to in the link table are organized in no
 * particular order (left up to the ClassFileLinker implementation), however the Main class must come first.
 *
 * High level structure consists of "ClassReference" structures:
 * 
 *  <ClassReference for Main>
 *  <ClassReference for class 1>
 *  <ClassReference for class 2>
 *  <ClassReference for class 3>
 *     ...
 *  <ClassReference for class n>
 *
 *  Each ClassReference is assembled first and then added to the LinkTable for writing.
 *
 *  In essence, the LinkTable is just a placeholder for ClassReferences since it is unknown at compile time how many
 *  classes will be part of the package. 
 */

package emr.elements.linktable;

import emr.elements.common.Element;

/**
 *
 * @author Ross
 */
public class LinkTable extends Element
{
    
    /** Creates a new instance of LinkTable */
    public LinkTable()
    {

    }
    
    public String toString()
    {
        return "link_table";
    }
    
}
