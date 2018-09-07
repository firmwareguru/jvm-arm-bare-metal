/*
 * ExceptionTableEntry.java
 *
 * Created on June 7, 2006, 8:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.elements.classfileparser.attributes;

import emr.elements.common.Element;
    
/**
 *
 * @author Ross
 */
public class ExceptionTableEntry extends Element
{
    
    /** Creates a new instance of ExceptionTableEntry */
    public ExceptionTableEntry()
    {
        add(new StartPc());
        add(new EndPc());
        add(new HandlerPc());
        add(new CatchType());
    }
    
    /////////////////////////////////////////
    // Accessors
    /////////////////////////////////////////
    
    public int getStartPc()
    {
        return getElement("start_pc").getValue();
    }
    
    public int getEndPc()
    {
        return getElement("end_pc").getValue();
    }
    
    public int getHandlerPc()
    {
        return getElement("handler_pc").getValue();
    }
    
    public int getCatchType()
    {
        return getElement("catch_type").getValue();
    }
    
    public String toString() 
    {
        return "exception_table_entry";
    }    
    
}
