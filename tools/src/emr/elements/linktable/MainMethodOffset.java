/*
 * MainMethodOffset.java
 *
 * Created on July 17, 2006, 7:45 PM
 *
 * An Element.
 */

package emr.elements.linktable;

import emr.elements.common.Element;
import emr.elements.classfileparser.ClassFile;
import emr.elements.classfileparser.Methods;
import emr.elements.classfileparser.attributes.CodeAttribute;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 *
 * @author Ross
 */
public class MainMethodOffset extends Element
{
    
    private ClassFile classFile;
    
    /** Creates a new instance of MainMethodOffset */
    public MainMethodOffset(ClassFile classFile_)
    {
        setSize(u2);
        classFile = classFile_;
    }
    
    public void writeElement(OutputStream os) throws IOException
    {
        // This stuff will work because it was already confirmed that the main method exists.
        Methods methods = (Methods) classFile.getElement("methods");
                
        CodeAttribute codeAttribute = methods.getCodeAttribute("main");
              
        setValue(codeAttribute.getOffset());
        
    }
    
    public String toString()
    {
        return "main_method_offset";
    }
    
}
