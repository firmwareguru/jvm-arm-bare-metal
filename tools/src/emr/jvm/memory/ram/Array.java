/*
 * Array.java
 *
 * Created on March 31, 2007, 3:28 PM
 *
 * This class provides constants that extend ObjectBase into Arrays.
 *
 * Arrays have another field beyond those in ObjectBase: arraylength.
 */

package emr.jvm.memory.ram;

/**
 *
 * @author Evan Ross
 */
public class Array
{
    
    /* Array Length */
    public static final int ArrayLengthOffset = ObjectBase.size;  // Where ObjectBase ends

    
    /* Array Base Size where the fields start */
    public static final int ArrayBaseSize = ObjectBase.size + 1 * 4;

    
}
