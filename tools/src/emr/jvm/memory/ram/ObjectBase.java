/*
 * ObjectBase.java
 *
 * Created on February 4, 2007, 8:51 PM
 *
 * This class provides the common functionality of all objects, be them arrays or instances.
 *
 * All objects have:
 *   1. Monitors: a 32-bit word used to track thread locks of objects.
 *   2. ClassReference: a reference (handle) to the NVM class template for the type of the object
 */

package emr.jvm.memory.ram;

/**
 *
 * @author Evan Ross
 */
public class ObjectBase
{
    
    ///////////////////////////////////////////////////
    // Fields common to all objects
    ///////////////////////////////////////////////////
    
    /* Monitor Offset */
    public static final int monitorOffset = 0 * 4;
    
    /* Class Type Reference Offset */
    public static final int classReferenceOffset = 1 * 4;
    
    ///////////////////////////////////////////////////
    
    /* Size of the object base */
    public static final int size = 2 * 4;
    
    
}
