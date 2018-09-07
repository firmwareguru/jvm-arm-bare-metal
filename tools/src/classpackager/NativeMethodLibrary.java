/*
 * NativeMethodLibrary.java
 *
 * Created on December 27, 2006, 12:51 PM
 *
 * The NativeMethodLibrary serves as an interface between the user (ClassBuilder) 
 * and a particular NativeMethodLibrary.  The implementation of a NativeMethodLibrary
 * must implement the <code>lookup</code> method.  A helper method 
 * <code>assembleCodeAttribute</code> is provided that creates the necessary code
 * attribute from values: maxstack, maxlocals; and an array of code bytes which
 * constitute the native method.
 */

package classpackager;

import emr.elements.classfileparser.attributes.CodeAttribute;
import emr.elements.classfileparser.code.Code;
import emr.elements.common.Element;
import java.io.ByteArrayInputStream;
import java.io.IOException;
/**
 *
 * @author Evan Ross
 */
public abstract class NativeMethodLibrary
{

    /** Assemble the CodeAttribute from given information */
    public CodeAttribute assembleCodeAttribute(int maxStack_, int maxLocals_, byte[] code_) throws IOException
    {
        // if there is no code_ bytes, then native method not found.
        if ( code_ == null )
            return null;

        System.err.println("  assembling CodeAttribute");
        // create an empty CodeAttribute
        CodeAttribute codeAttr = new CodeAttribute(null);
        
        // set the max locals and max stack
        codeAttr.setMaxLocals( maxLocals_ );
        codeAttr.setMaxStack( maxStack_ );
        
        // get the empty Code element
        Code code = (Code) codeAttr.getCode();

        // create a new byte array containing the the code plus the length of the code
        Element codeLength = code.getElement("code_length");
        byte[] codeWithLength = new byte[code_.length + codeLength.getSize()];  // 4 bytes for length
        
        // copy over the length to the new array.  The code length must be converted using big endian format.
        byte[] lengthBytes = codeLength.intToBytes(code_.length, Element.EndianEnum.BIG ); // codeLength has size set to 4 bytes
        System.arraycopy( lengthBytes, 0, codeWithLength, 0, lengthBytes.length );
        
        // copy over code bytes to new array starting after code length
        System.arraycopy( code_, 0, codeWithLength, codeLength.getSize(), code_.length );
        
        // wrap the new code array with a ByteArrayInputStream
        ByteArrayInputStream is = new ByteArrayInputStream(codeWithLength);
        
        // finally build the code element
        code.readData( is );
        
        // return the completed code attribute
        return codeAttr;
        
    }
    
    
    /** Build and return a CodeAttribute containing the native method */
    public abstract CodeAttribute lookup(String className_, String methodName_, String methodDescriptor_) throws IOException;
    
}
