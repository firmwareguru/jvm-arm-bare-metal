/*
 * MethodTableEntry.java
 *
 * Created on December 10, 2006, 4:24 PM
 *
 * A MethodTableEntry represents a structure or element of the MethodTable.
 * This structure is fixed size and is of the following format:
 * All words are 2 bytes, but padding words must be added since NVM is 4-byte aligned on the target platform.
 *     word 0: nameIndex (2 bytes)
 *     word 1: descriptorIndex (2 bytes) 
 *     word 2: flags (2 bytes)
 *     word 3: codeTableEntry pointer (2 bytes)
 *     word 4: ExceptionHandlerPointer (2 bytes)
 *     word 5: Reserved (2 bytes)
 *
 *
 * Notes:
 *     nameIndex and descriptorIndex are indexes into the constant pool.  Unmodified
 *     from the ClassFile.
 *     codeTableEntry pointer and ExceptionHandlerEntryPointer are NVM offset addresses to
 *     the start of the pointed to structure, offset from the beginning address of the InternalClass.
 *
 * Convenience methods are provided to set the values in these words.
 */

package classpackager;
import emr.elements.common.Element;
import emr.elements.classfileparser.common.AccessFlags;
import emr.elements.common.GenericElement;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 *
 * @author Ross
 */
public class MethodTableEntry extends TableEntry {
    
    /* The reference to the CodeTableEntry for this MethodTableEntry */
    Element codeTableEntry = null;
    
    /* The reference to the ExceptionHandlerTableEntry for this MethodTableEntry */
    Element exceptionHandlerTableEntry = null; 
         
    /** Creates a new instance of MethodTableEntry by constructing empty elements */
    public MethodTableEntry() 
    {
        // word 0
        add(new GenericElement(u2, "name_index"));   // upper 2 bytes
        // word 1 
        add(new GenericElement(u2, "descriptor_index"));  // lower 2 bytes
        // word 2
        add(new GenericElement(u2, "flags"));   // upper 2
        // word 3
        add(new GenericElement(u2, "arg_count"));  // lower 2
        // word 4
        add(new GenericElement(u2, "exception_handler_ptr"));  // upper 2
        // word 5
        add(new GenericElement(u2, "codeptr"));  // lower 2
                
    }
    
    public void setCodeTableEntry(Element e)
    {
        codeTableEntry = e;
    }
    
    public void setExceptionHandlerTableEntry(Element e)
    {
        exceptionHandlerTableEntry = e;
    }
    
    /** Override writeElement so that the CodeTableEntry pointer and the
     * ExceptionHandlerTableEntry pointer can be dynamically determined at write time.
     * These pointers are retreived from the element's offset value.
     */
    public void writeElement(OutputStream os) throws IOException
    {

        //
        // A null CodeTableEntry is possible if this MethodTableEntry represents
        // an abstract method which includes Interface methods.  
        // A null CodeTableEntry implies that there is no ExceptioHandlerTable.
        //
        if( codeTableEntry == null )
            return;
        
        // set the code entry pointer from the offset of the codeTableEntry
        setCodePtr( codeTableEntry.getAlignedOffset(4) );
        
        // set the exceptionhandler pointer from the offset of the exceptionHandlerEntry
        if( exceptionHandlerTableEntry != null )
            setExceptionHandlerPtr( exceptionHandlerTableEntry.getAlignedOffset(4) );
    }
    
    
    public void setNameIndex(int value_)
    {
        Element e = getElement("name_index");
        
        // put the value back
        e.setValue(value_);
    }
    
    public void setDescriptorIndex(int value_)
    {
        Element e = getElement("descriptor_index");
        
        // put the value back
        e.setValue(value_);
        
    }
    
    public void setAccessFlags(int flags_)
    {
        Element e = getElement("flags");
        
        e.setValue(flags_);
    }
    
    public AccessFlags getAcessFlags()
    {
        return (AccessFlags) getElement("flags");
    }
    
    /** Set the number of words that this method needs to copy over
     *  from the previous frame to fullfill its argument requirements
     */
    public void setArgCount(int value_)
    {
        Element e = getElement("arg_count");
        
        e.setValue(value_);
    }
    
    
    /**
     * Note!  The code pointer must be set dynamically during write-out, thus it
     * is done by this class in writeElement() and hence is private
     *
     * The Code and Exception pointers are relative to this MethodTableEntry
     * so that they can fit into 2-byte half-words.  This is a decision
     * to trade an extra instruction for saving a few bytes of memory.
     * The alternative is to make the Code and Exception pointers each 4 bytes
     * and continue to be absolute addresses.
     */
    private void setCodePtr(int ptr_)
    {
        // ptr_ is the aligned offset of the code table.
        // We can subtract the MethodTableEntry's aligned offset to get
        // the relative offset.  Of course, VM code will need to know
        // to add this offset to this table entry, fortunately the table
        // entry is always available the the necessary processes.
        int thisOffset = this.getAlignedOffset(4);
        
        Element e = getElement("codeptr");
        
        e.setValue(ptr_ - thisOffset);
    }
    
    private void setExceptionHandlerPtr(int ptr_)
    {
        int thisOffset = getAlignedOffset(4);
        
        Element e = getElement("exception_handler_ptr");
        
        e.setValue(ptr_ - thisOffset);
    }
 
    public String toString()
    {
        return "method_table_entry";
    }
}
