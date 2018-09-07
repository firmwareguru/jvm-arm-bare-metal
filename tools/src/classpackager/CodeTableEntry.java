/*
 * CodeTableEntry.java
 *
 * Created on December 10, 2006, 4:25 PM
 *
 * The CodeTableEntry structure contains the information the JVM needs to 
 * build a frame for a method and the code bytes to execute.
 * The structure is:
 *   word 0: max stack size (2 bytes)
 *   word 1: max locals (2 bytes)
 *   word 2...: array of code bytes
 *
 */

package classpackager;

import emr.elements.common.Element;
import emr.elements.common.GenericElement;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 *
 * @author Ross
 */
public class CodeTableEntry extends TableEntry {
    
    /** Creates a new instance of CodeTableEntry */
    public CodeTableEntry() 
    {
        // add a word that holds the maxstack and maxlocals
        add(new GenericElement(u2, "max_stack"));
        
        // word 1
        add(new GenericElement(u2, "max_locals"));
        
        // The upper 2 bytes of the next word holds the number of args
        //add(new GenericElement(u2, "arg_count"));
        //  arg count moved to MethodTableEntry
        
        // need to pad out the word to 4 bytes...
        //add(new GenericElement(u2, "reserved"));
        //  don't need this padding anymore
        
    }
    
    /** Set the max local variables field of this CodeTableEntry.  The JVM uses
     * this field to build the method's Frame.
     */
    public void setMaxLocals(int value_)
    {
        Element e = getElement("max_locals");
        
        e.setValue(value_);
    }
    
    /** Set the max stack size field of this CodeTableEntry.  The JVM uses
     * this field to build the method's Frame.
     */
    public void setMaxStack(int value_)
    {
        Element e = getElement("max_stack");
        
        e.setValue(value_);
        
    }
    
    /** Set the Code of this CodeTableEntry.  This must be called only once.
     * The Code Element must be the head of a tree containing all instructions.
     * The Code Element must not contain the code_length sub-Element.
     */
    public void addCode(Element code_)
    {
        add( code_ );
    }
    
    public String toString()
    {
        return "code_table_entry";
    }
    
    
}
