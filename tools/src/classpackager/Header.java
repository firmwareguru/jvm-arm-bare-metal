/*
 * Header.java
 *
 * Created on December 10, 2006, 4:23 PM
 *
 */

package classpackager;
import emr.elements.common.Element;
import emr.elements.common.GenericElement;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * Header:
 *    next_class   u4     // pointer to next InternalClass in NVM
 *    this_class   u4     // hashed name of this_class
 *    super_class  u4     // pointer to super class in NVM
 *    access_flags u2     // access flags
 *    object_size  u2     // pre-computed size of memory region for an object of this class
 *    constant_pool_offset u4  // relative address of start of ConstantPoolTable
 *    interfaces_table     u4  // relative address of start of InterfaceTable
 *    method_table_offset  u4  // relative address of start of MethodTable
 *    field_table_offset   u4  // relative address of start of FieldTable
 *
 * Note: the relative addresses are specified from the 'next_class' element.
 *
 * The method and field table offsets are determined after the entire
 * package is 'test written' to the JVMOutputStream to determine the offsets for each
 * element.  In the meantime, the header has references to the field and method
 * tables so that it can pull out the offset value when the header writes out
 * the method_table_offset and field_table_offset.
 *
 * @author ERoss
 */
public class Header extends Element {
    
    /* The NextClass */
    InternalClass nextClass;
    
    /* The FieldTable */
    Element fieldTable;
    
    /* The MethodTable */
    Element methodTable;
    
    /* The InterfacesTable */
    Element interfaceTable;
    
    /* The InternalConstantPool */
    Element constantPoolTable;
    
    /* Reference to the SuperClass */
    Element superClass;
    
    /* Reference to the ThisClass */
    Element thisClass;
    
    
    /** Creates a new instance of Header */
    public Header() 
    {
        // setup the header
        add(new GenericElement(u4, "next_class"));   // 0
        add(new GenericElement(u4, "this_class"));   // 1
        add(new GenericElement(u4, "super_class"));  // 2
        add(new GenericElement(u2, "access_flags")); // 3
        add(new GenericElement(u2, "object_size"));  // 
                
        add(new GenericElement(u4, "constant_pool_table_offset"));  // 4
        add(new GenericElement(u4, "interface_table_offset"));      // 5
        add(new GenericElement(u4, "method_table_offset"));         // 6
        add(new GenericElement(u4, "field_table_offset"));          // 7

        // initialize object_size to -1 so that the ClassBuilder can use this 
        // information to avoid recomputing field sizes and counts.
        getElement("object_size").setValue(-1);
    }
    
    public Element getNextClass()
    {
        return getElement("next_class");
    }
    
    public Element getThisClass()
    {
        return getElement("this_class");
    }
    
    public Element getSuperClass()
    {
        return getElement("super_class");
    }

    public Element getAccessFlags()
    {
        return getElement("access_flags");
    }
    
    public Element getObjectSize()
    {
        return getElement("object_size");
    }
    
    public void setNextClass(InternalClass nextClass_)
    {
        nextClass = nextClass_;
    }

    public void setFieldTable(Element fieldTable_)
    {
        fieldTable = fieldTable_;
    }
    
    public void setMethodTable(Element methodTable_)
    {
        methodTable = methodTable_;
    }
    
    public void setInterfaceTable(Element interfaceTable_)
    {
        interfaceTable = interfaceTable_;
    }
    
    public void setConstantPoolTable(Element constantPoolTable_)
    {
        constantPoolTable = constantPoolTable_;
    }
    
    public void setThisClass(Element thisClass_)
    {
        thisClass = thisClass_;
    }
    
    public void setSuperClass(Element superClass_)
    {
        superClass = superClass_;
    }
    
    /** get the offsets of the method and field tables and 
     * set the corresponding element values.  Each element is
     * written out automatically in writeChildren
     */
    public void writeElement(OutputStream os) throws IOException
    {
        // throw an exception if the method or field tables haven't been set
        if( fieldTable == null ||
                methodTable == null ||
                        constantPoolTable == null )
        {
            throw new IOException("Header error: tables not set.");
        }
        
        getElement("constant_pool_table_offset").setValue(constantPoolTable.getAlignedOffset(4));
        
        // InterfaceTable can be null if this Header is for an Interface.
        if( interfaceTable != null )
            getElement("interface_table_offset").setValue(interfaceTable.getAlignedOffset(4));
        
        getElement("method_table_offset").setValue(methodTable.getAlignedOffset(4));
        
        getElement("field_table_offset").setValue(fieldTable.getAlignedOffset(4));
        
        getElement("this_class").setValue(thisClass.getValue());

        // superClass can be null if there is no super class (class Object)
        if( superClass != null )
            getElement("super_class").setValue(superClass.getValue());

        // nextClass can be null if this is the last class in the list
        if( nextClass != null )
            getElement("next_class").setValue(nextClass.getHeader().getAlignedOffset(4));
        
    }
    
    public String toString()
    {
        return "header";
    }
    
    
}
