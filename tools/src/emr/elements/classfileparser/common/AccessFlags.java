/*
 * AccessFlags.java
 *
 * Created on May 30, 2006, 8:22 PM
 *
 * The AccessFlags field in a MethodInfo or a FieldInfo.
 *
 * Provides methods to conveniently obtain test the flags.
 */

package emr.elements.classfileparser.common;

import emr.elements.common.Element;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 *
 * @author Ross
 */
public class AccessFlags extends Element
{
    // These access flags are a combination of:
    //    Table 4.1 - access_flags of Class
    //    Table 4.4 - access_flags of Fields
    //    Tabke 4.5 - access_flags of Methods
    //
    // They are mostly unique amoung tables so they can be
    // combined here.
    //
    // Exceptions are:
    //    1. ACC_SUPER = 0x0020 in access_flags for Class and
    //       ACC_SYNCHRONIZED = 0x0020 in access_flags for Methods
    //
    private static final int ACC_PUBLIC       = 0x0001;
    private static final int ACC_PRIVATE      = 0x0002;
    private static final int ACC_PROTECTED    = 0x0004;
    private static final int ACC_STATIC       = 0x0008;
    private static final int ACC_FINAL        = 0x0010;
    private static final int ACC_SYNCHRONIZED = 0x0020;
    private static final int ACC_SUPER        = 0x0020;
    private static final int ACC_VOLATILE     = 0x0040;
    private static final int ACC_TRANSIENT    = 0x0080;
    private static final int ACC_NATIVE       = 0x0100;
    private static final int ACC_INTERFACE    = 0x0200;
    private static final int ACC_ABSTRACT     = 0x0400;
    private static final int ACC_STRICT       = 0x0800;
    
    /** Creates a new instance of AccessFlags */
    public AccessFlags()
    {
        setSize(u2);
    }
    
    public String toString()
    {
        return "access_flags";
    }
    
    //-------------------------------------------------
    // Flag accessors
    //-------------------------------------------------
    
    public boolean isPublic()
    {
        return (getValue() & ACC_PUBLIC) == ACC_PUBLIC;
    }
    
    public boolean isProtected()
    {
        return (getValue() & ACC_PROTECTED) == ACC_PROTECTED;
    }

    public boolean isPrivate()
    {
        return (getValue() & ACC_PRIVATE) == ACC_PRIVATE;
    }
    
    public boolean isNative()
    {
        return (getValue() & ACC_NATIVE) == ACC_NATIVE;
    }
    
    public void setNative(boolean native_)
    {
        int bytes = getValue();
        if( native_ )
        {
            bytes |= ACC_NATIVE;
        }
        else
        {
            bytes &= ~ACC_NATIVE;
        }
        setValue(bytes);
    }
    
    public boolean isFinal()
    {
        return (getValue() & ACC_FINAL) == ACC_FINAL;
    }
    
    public boolean isInterface()
    {
        return (getValue() & ACC_INTERFACE) == ACC_INTERFACE;
    }
    
    public boolean isAbstract()
    {
        return (getValue() & ACC_ABSTRACT) == ACC_ABSTRACT;
    }
    
    public boolean isStatic()
    {
        return (getValue() & ACC_STATIC) == ACC_STATIC;
    }

    public static void main(String[] args)
    {
        AccessFlags af = new AccessFlags();
        af.setValue(0x0103);
        
        System.out.println("public  : " + af.isPublic());
        System.out.println("private : " + af.isPrivate());
        System.out.println("native  : " + af.isNative());
        
        af.setNative( false );
        
        System.out.println("public  : " + af.isPublic());
        System.out.println("private : " + af.isPrivate());
        System.out.println("native  : " + af.isNative());
        
        af.setNative( true );
        
        System.out.println("public  : " + af.isPublic());
        System.out.println("private : " + af.isPrivate());
        System.out.println("native  : " + af.isNative());
        
    }
}
