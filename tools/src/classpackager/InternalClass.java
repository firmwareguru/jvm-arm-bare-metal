/*
 * InternalClass.java
 *
 * Created on December 10, 2006, 4:23 PM
 *
 * The InternalClass is the top-level container for the InternalClass' Header, 
 * ConstantPoolTable, MethodTable, FieldTable, CodeTable and ExceptionHandlerTable
 */

package classpackager;
import emr.elements.common.Element;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 *
 * @author Ross
 */
public class InternalClass extends Element {
    
    /*
     *  This is the full java name of that this InternalClass
     *  represents.  Used in search and display functions.
     */
    String internalClassName; 
    
    /** 
     * Creates a new instance of InternalClass.
     *
     * The interface paramater indicates whether to contruct an InternalClass
     * for Interfaces. 
     *
     * TRUE = construct for Interface.
     * FALSE = construct for Class. 
     *
     * An Interface is different from a Class.  In order to save space, an InterClass
     * built for an Interface has these properties:
     *    no InterfaceTable
     *    no CodeTable
     *    no ExceptionHandlerTable
     *    
     */
    public InternalClass(boolean isInterface_, String name_) 
    {
        internalClassName = name_;
                
        // ensure we are 4-byte aligned
        add( new ByteAligner(4)  );

        // build up the structure
        Header header = new Header();
        add(header);
        
        ConstantPoolTable constantPool = new ConstantPoolTable();
        add(constantPool);

        //
        //  No InterfaceTable for Interfaces.
        //
        InterfaceTable interfaceTable = null;
        if( isInterface_ == false ) {
            interfaceTable = new InterfaceTable();
            add(interfaceTable);
        }

        MethodTable methodTable = new MethodTable();
        add(methodTable);

        FieldTable  fieldTable  = new FieldTable();
        add(fieldTable);
        
        //
        //  Only add the ExceptionHandlerTable, CodeTable and StringTable if this
        //  is a Class and not an Interface.
        //
        if( isInterface_ == false ) {
            add(new ExceptionHandlerTable());
            add(new CodeTable());
            add(new StringTable());
        }
        
        // header points to the MethodTable and FieldTable
        header.setConstantPoolTable(constantPool);
        header.setInterfaceTable(interfaceTable);
        header.setMethodTable(methodTable);
        header.setFieldTable(fieldTable);
    }
    
    public Header getHeader()
    {
        return (Header) getElement("header");
    }
    
    public ConstantPoolTable getConstantPoolTable()
    {
        Element e = getElement("constant_pool_table");
        if( e == null )
            return null;
        
        return (ConstantPoolTable)e;
    }
    
    public MethodTable getMethodTable()
    {
        Element e = getElement("method_table");
        if( e == null )
            return null;
        
        return (MethodTable)e; 
    }
    
    public InterfaceTable getInterfaceTable()
    {
        Element e = getElement("interface_table");
        if( e == null )
            return null;
        
        return (InterfaceTable)e; 
    }
    
    public FieldTable getFieldTable()
    {
        Element e = getElement("field_table");
        if( e == null )
            return null;
        
        return (FieldTable)e; 
    }
    
    public CodeTable getCodeTable()
    {
        Element e = getElement("code_table");
        if( e == null )
            return null;
        
        return (CodeTable)e; 
    }
    
    public ExceptionHandlerTable getExceptionHandlerTable()
    {
        Element e = getElement("exception_handler_table");
        if( e == null )
            return null;
        
        return (ExceptionHandlerTable)e;
    }
    
    public StringTable getStringTable()
    {
        Element e = getElement("string_table");
        if( e == null )
            return null;
        
        return (StringTable)e;
        
    }
    
    /**
     * 
     * @return The full Java class name this InternalClass represents.
     */
    public String getName()
    {
        return internalClassName;
    }
    
    @Override
    public String toString()
    {
        return ("internal_class (" + internalClassName + ")");
    }
    
}
