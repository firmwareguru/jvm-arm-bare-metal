/*
 * ExceptionHandlerTableEntry.java
 *
 * Created on December 10, 2006, 4:25 PM
 *
 * The ExceptionHandlerTableEntry contains entries reproduced from the
 * ExceptionHandler table in the CodeAttribute of a MethodInfo.
 * Format:
 *   word 0: start pc (2 bytes)
 *   word 1: end pc (2 bytes)
 *   word 2: handler pc (2 bytes)
 *   word 3: catch type (2 bytes)
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
public class ExceptionHandlerTableEntry extends TableEntry {
    
    /** Creates a new instance of ExceptionHandlerTableEntry */
    public ExceptionHandlerTableEntry() 
    {
        add(new GenericElement(u2, "start_pc"));
        add(new GenericElement(u2, "end_pc"));
        add(new GenericElement(u2, "handler_pc"));
        add(new GenericElement(u2, "catch_type"));
    }
    
    public void setStartPc(int value_)
    {
        Element e = getElement("start_pc");
        e.setValue(value_);
    }
    
    public void setEndPc(int value_)
    {
        Element e = getElement("end_pc");
        e.setValue(value_);
    }
    
    public void setHandlerPc(int value_)
    {
        Element e = getElement("handler_pc");
        e.setValue(value_);
    }
    
    public void setCatchType(int value_)
    {
        Element e = getElement("catch_type");
        e.setValue(value_);
    }
    
    public String toString()
    {
        return "exception_handler_table_entry";
    }
    
    
}
