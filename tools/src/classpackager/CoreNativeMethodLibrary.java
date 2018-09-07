/*
 * CoreNativeMethodLibrary.java
 *
 * Created on October 11, 2006, 9:17 PM
 *
 * This file contains the implementation (java byte codes) for native methods in the Core Library.
 */

package classpackager;
import emr.elements.classfileparser.attributes.CodeAttribute;
import java.io.IOException;
import emr.jvm.OpcodeMnemonics;

/**
 *
 * @author Ross
 */
public class CoreNativeMethodLibrary 
        extends NativeMethodLibrary
        implements OpcodeMnemonics
{
    
    /*
    protected int maxStack;
    protected int maxLocals;
    protected byte[] code = null;    
    */
    /**
     * Creates a new instance of CoreNativeMethodLibrary
     */
    public CoreNativeMethodLibrary() 
    {
    }
    
    ///////////////////////////////////////////////////////////////////////
    // Lookup, create and return a byte array wrapping the desired native code
    ////////////////////////////////////////////////////////////////////////
    // The native methods are composed of Java instructions > Java Assembler!
    ////////////////////////////////////////////////////////////////////////
    public CodeAttribute lookup(String className_, String methodName_, String methodDescriptor_) throws IOException
    {
        //System.out.println("Native method lookup: " + methodName_ + " " + methodDescriptor_ );

        int maxStack = 0;
        int maxLocals = 0;
        byte[] code = null;    

        /********************************************************************************
         *
         *  Package java.lang
         *
         ********************************************************************************/
        
        //////////////////////////////////////////////////////////////////////////////////
        // Native methods for class Object
        //////////////////////////////////////////////////////////////////////////////////
        if( className_.equalsIgnoreCase("java/lang/Object") )
        {
            // hashCode() method
            if( methodName_.equalsIgnoreCase("hashCode") && methodDescriptor_.equalsIgnoreCase("()I") )
            {
                maxStack  = 0;
                maxLocals = 1;  // 'this'
                code = new byte[1];
                code[0] = (byte)ireturn;  // return 'this'
            }
        }

        //////////////////////////////////////////////////////////////////////////////////
        // Native methods for class Thread
        //////////////////////////////////////////////////////////////////////////////////
        if( className_.equalsIgnoreCase("java/lang/Thread") )
        {
            // Thread start (instance method)
            if(methodName_.equalsIgnoreCase("start") && methodDescriptor_.equalsIgnoreCase("()V"))
            {
                code = new byte[3];
                maxStack  = 0;
                maxLocals = 1;  // reference ('this')
                
                code[0] = (byte)lang;     // lang     ; JVM native lang instruction 
                code[1] = (byte)T_start;  // t_start  ; start the thread
                code[2] = (byte)return_;  // return   ; return void
                
            }
            
            // Thread sleep  Static!!
            if(methodName_.equalsIgnoreCase("sleep") && methodDescriptor_.equalsIgnoreCase("(I)V"))
            {
                code = new byte[3];
                maxStack  = 0;
                maxLocals = 1;  // sleep time
                
                //code[0] = (byte)iload_0;   // iload_0  ; push sleep time in millis onto opstack
                                           //          ; Xnote! assumes instance method with reference in position 0
                code[0] = (byte)lang;      // lang     ; JVM native lang instruction
                code[1] = (byte)T_sleep;   // t_sleep  ; sleep the thread
                code[2] = (byte)return_;   // return   ; return void
                
                        
            }
            
            // Thread yield  - Static -
            if(methodName_.equalsIgnoreCase("yield") && methodDescriptor_.equalsIgnoreCase("()V"))
            {
                code = new byte[3];
                maxStack  = 0;
                maxLocals = 0; 
                
                //code[0] = (byte)iload_0;   // iload_0  ; push sleep time in millis onto opstack
                                           //          ; Xnote! assumes instance method with reference in position 0
                code[0] = (byte)lang;      // lang     ; JVM native lang instruction
                code[1] = (byte)T_yield;   // t_sleep  ; sleep the thread
                code[2] = (byte)return_;   // return   ; return void
                
                        
            }
            
        }
        
        //////////////////////////////////////////////////////////////////////////////////
        // Native methods for class String
        //////////////////////////////////////////////////////////////////////////////////
        if (className_.equalsIgnoreCase("java/lang/String"))
        {
            if (methodName_.equalsIgnoreCase("getBytes") && methodDescriptor_.equalsIgnoreCase("()[B"))
            {
                // Arguments: 
                //    int address
                //    int word
                
                code = new byte[2];
                maxStack  = 1;  // the reference (this)
                maxLocals = 1;  // this
                
                // Simply return 'this' since a StringTableEntry is formatted as an Array.
                
                code[0] = (byte)aload_0;             // push 'this' onto stack
                code[1] = (byte)areturn;             // return 'this'
            }
            
        } 
        
        
        /********************************************************************************
         *
         *  Package system
         *
         ********************************************************************************/
        
        //////////////////////////////////////////////////////////////////////////////////
        // Native methods for class Interrupt
        //////////////////////////////////////////////////////////////////////////////////
        if( className_.equalsIgnoreCase("system/Interrupt") )
        {
            // Interrupt setInterruptHandler (static method)
            if(methodName_.equalsIgnoreCase("setInterruptHandler") && methodDescriptor_.equalsIgnoreCase("(Ljava/lang/Thread;I)V"))
            {
                code = new byte[3];
                maxStack  = 0;
                maxLocals = 2;  // Thread reference, interrupt type (index into vector table)
                
                code[0] = (byte)lang;       // lang       ; JVM native lang instruction 
                code[1] = (byte)INT_start;  // INT_start  ; start the Interrupt thread but add it to the vector table
                code[2] = (byte)return_;    // return     ; return void
                
            }
            
        }        
        
        
        /********************************************************************************
         *
         *  Package system.graphics
         *
         ********************************************************************************/

        //////////////////////////////////////////////////////////////////////////////////
        // Native methods for class OLEDGraphicsContext
        //////////////////////////////////////////////////////////////////////////////////
        if( className_.equalsIgnoreCase("system/graphics/OLEDGraphicsContext") )
        {
            // void drawString(String, int, int)
            if(methodName_.equalsIgnoreCase("drawString0") && methodDescriptor_.equalsIgnoreCase("(Ljava/lang/String;II)V"))
            {
                // This method calls special JVM 'peripheral' opcode to call the display driver's equivalent drawString
                // function.
                //
                //  The arguments are in local variables as shown:
                //     0 - this
                //     1 - String
                //     2 - int
                //     3 - int
                //    ---------
                //     4 local vars.
                //   
                
                code = new byte[3];
                maxStack  = 0;  // Don't need a stack since VM will pull these variables out into registers.
                maxLocals = 4;  // 
                
                code[0] = (byte)peripheral;          // peripheral          ; JVM native lang instruction 
                code[1] = (byte)display_DrawString;  // display_DrawString  ; reqest that the parameters String, int, int be drawn by the hardware.
                code[2] = (byte)return_;             // return              ; return void
                
                
            }

            // void drawBytes0(byte[], int, int)
            if(methodName_.equalsIgnoreCase("drawBytes0") && methodDescriptor_.equalsIgnoreCase("([BII)V"))
            {
                // This method calls special JVM 'peripheral' opcode to call the display driver's equivalent drawBytes
                // function.
                //
                //  The arguments are in local variables as shown:
                //     0 - this
                //     1 - byte[]
                //     2 - int
                //     3 - int
                //    ---------
                //     4 local vars.
                //

                code = new byte[3];
                maxStack  = 0;  // Don't need a stack since VM will pull these variables out into registers.
                maxLocals = 4;  //

                code[0] = (byte)peripheral;          // peripheral          ; JVM native lang instruction
                //code[1] = (byte)display_DrawBytes;   // display_DrawBytes   ; request that the parameters byte[], int, int be drawn by the hardware.
                code[1] = (byte)display_DrawString;   // display_DrawBytes   ; request that the parameters byte[], int, int be drawn by the hardware.
                code[2] = (byte)return_;             // return              ; return void


            }
            
            // void drawInt(int, int, int)
            if(methodName_.equalsIgnoreCase("drawInt0") && methodDescriptor_.equalsIgnoreCase("(III)V"))
            {
                // This method calls special JVM 'peripheral' opcode to call the display driver's equivalent drawString
                // function after having first generated a string from the int.
                //
                //  The arguments are in local variables as shown:
                //     0 - this
                //     1 - int
                //     2 - int
                //     3 - int
                //    ---------
                //     4 local vars.
                //   
                
                code = new byte[3];
                maxStack  = 0;  // Don't need a stack since VM will pull these variables out into registers.
                maxLocals = 4;  // 
                
                code[0] = (byte)peripheral;          // peripheral          ; JVM native lang instruction 
                code[1] = (byte)display_DrawInt;     // display_DrawInt     ; reqest that the parameters String, int, int be drawn by the hardware.
                code[2] = (byte)return_;             // return              ; return void
            }

            // void drawLine(int, int, int, int)
            if(methodName_.equalsIgnoreCase("drawLine0") && methodDescriptor_.equalsIgnoreCase("(IIII)V"))
            {
                // This method calls special JVM 'peripheral' opcode to call the display driver's equivalent drawLine
                // function after having first generated a string from the int.
                //
                //  The arguments are in local variables as shown:
                //     0 - this
                //     1 - int
                //     2 - int
                //     3 - int
                //     4 - int
                //    ---------
                //     5 local vars.
                //

                code = new byte[3];
                maxStack  = 0;  // Don't need a stack since VM will pull these variables out into registers.
                maxLocals = 5;  //

                code[0] = (byte)peripheral;          // peripheral          ; JVM native lang instruction
                code[1] = (byte)display_DrawLine;    // display_DrawLine    ; reqest that the parameters int, int, int, int be drawn by the hardware.
                code[2] = (byte)return_;             // return              ; return void
            }

        }        
        
        //////////////////////////////////////////////////////////////////////////////////
        // Native methods for class Memory
        //////////////////////////////////////////////////////////////////////////////////
        if (className_.equalsIgnoreCase("system/Memory"))
        {
            if (methodName_.equalsIgnoreCase("setWord") && methodDescriptor_.equalsIgnoreCase("(II)V"))
            {
                // Arguments: 
                //    int address
                //    int word
                
                code = new byte[3];
                maxStack  = 0;  // Don't need a stack since VM will pull these variables out into registers.
                maxLocals = 2;  // offset, value   STATIC method.
                
                code[0] = (byte)peripheral;          // peripheral          ; JVM native lang instruction 
                code[1] = (byte)mem_write;           // mem_write           ; reqest that the byte be written to the port.
                code[2] = (byte)return_;             // return              ; return void
            }
            if (methodName_.equalsIgnoreCase("getWord") && methodDescriptor_.equalsIgnoreCase("(I)I"))
            {
                // Arguments: 
                //    int address
                
                code = new byte[3];
                maxStack  = 1;  // Returned value
                maxLocals = 1;  // offset   STATIC method.
                
                code[0] = (byte)peripheral;          // peripheral          ; JVM native lang instruction 
                code[1] = (byte)mem_read;            // mem_write           ; reqest that the byte be written to the port.
                code[2] = (byte)ireturn;             // ireturn             ; return int
            }
            
        } 
        
        //////////////////////////////////////////////////////////////////////////////////
        // Native methods for class FlashObjectInputStream
        //////////////////////////////////////////////////////////////////////////////////
        if (className_.equalsIgnoreCase("emr/jvm/adapterlib/FlashObjectInputStream"))
        {
            // public static native Object readObject(String filename);
            if (methodName_.equalsIgnoreCase("readObject") && methodDescriptor_.equalsIgnoreCase("(LJava/lang/String;)Ljava/lang/Object;"))
            {
                // Arguments:
                //    int address
                //    int word

                code = new byte[3];
                maxStack  = 1;  // Returned value
                maxLocals = 1;  // filename   STATIC method (no 'this')

                code[0] = (byte)peripheral;          // peripheral          ; JVM native lang instruction
                code[1] = (byte)get_file_handle;     // get_file_handle     ; get file handle
                code[2] = (byte)areturn;             // return              ; return handle
            }
        }

        //////////////////////////////////////////////////////////////////////////////////
        // Native methods for class emr.jvm.adapterlib.Math
        //////////////////////////////////////////////////////////////////////////////////
        if (className_.equalsIgnoreCase("emr/jvm/adapterlib/Math"))
        {
            // public static native float pow(float a, float b);
            if (methodName_.equalsIgnoreCase("pow") && methodDescriptor_.equalsIgnoreCase("(FF)F"))
            {
                // Arguments:
                //    float base
                //    float exponent

                code = new byte[3];
                maxStack  = 1;  // Returned value
                maxLocals = 2;  // float a, float b   STATIC method (no 'this')

                code[0] = (byte)lang;         // 
                code[1] = (byte)math_pow;     // run math.pow
                code[2] = (byte)freturn;      // return float result
            }
        }

        //////////////////////////////////////////////////////////////////////////////////
        // Native methods for class java.io.FileInputStream
        //////////////////////////////////////////////////////////////////////////////////
        if (className_.equalsIgnoreCase("java/io/FileInputStream"))
        {
            // private static native int construct(String name);
            if (methodName_.equalsIgnoreCase("construct") && methodDescriptor_.equalsIgnoreCase("(LJava/lang/String;)I"))
            {
                // Arguments:
                //    String filename

                code = new byte[3];
                maxStack  = 1;  // Returned value
                maxLocals = 1;  // LJava/lang/String;   STATIC method (no 'this')

                code[0] = (byte)peripheral;
                code[1] = (byte)get_file_handle;
                code[2] = (byte)ireturn;      // return int file handle
            }
            // private static native int read0(byte[] b, int off, int len, int position, int handle);
            if (methodName_.equalsIgnoreCase("read0") && methodDescriptor_.equalsIgnoreCase("([BIIII)I"))
            {
                // Arguments:
                //    byte[] array
                //    int offset
                //    int length
                //    int position
                //    int handle

                code = new byte[3];
                maxStack  = 1;  // Returned value
                maxLocals = 5;  // 5 arguments;   STATIC method (no 'this')

                code[0] = (byte)peripheral;
                code[1] = (byte)fileinputstream_read0;
                code[2] = (byte)ireturn;      // return int file handle
            }
        }


       //////////////////////////////////////////////////////////////////////////////////
        // Native methods for class javax.sound.sampled.SourceDataLine
        //////////////////////////////////////////////////////////////////////////////////
        if (className_.equalsIgnoreCase("javax/sound/sampled/SourceDataLine"))
        {
            // public native int write(byte[] b, int off, int len);
            if (methodName_.equalsIgnoreCase("write") && methodDescriptor_.equalsIgnoreCase("([BII)I"))
            {
                // Arguments:
                //    byte[] array
                //    int offset
                //    int length

                code = new byte[3];
                maxStack  = 1;  // Returned value
                maxLocals = 3;  // 5 arguments;   STATIC method (no 'this')

                code[0] = (byte)peripheral;
                code[1] = (byte)sourcedataline_write;
                code[2] = (byte)ireturn;      // return int file handle
            }
        }

        if (code == null)
            throw new IOException("Native method not found: " + methodName_ + " " + methodDescriptor_);
        
        return assembleCodeAttribute(maxStack, maxLocals, code);
    }

    
}
