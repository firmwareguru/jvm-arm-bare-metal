/*
 * Frame.java
 *
 * Created on February 7, 2007, 9:11 PM
 *
 * Frame Layout:
 *      opstack[max_stack - 1]
 *             ...
 *      opstack[0]
 *      variable array index[max_locals - 1]
 *             ...
 *   4  variable array index[0]
 *   3  stack pointer
 *   2  previous frame pointer
 *   1  previous PC
 *   0  CurrentClass
 *
 * Note that the opstack grows upward.  Thus, an empty frame stack will have it's stack pointer point
 * to the opstack[0].
 */
 
package emr.jvm.memory.ram;

/**
 *
 * @author Evan Ross
 */
public class Frame
{
    
    public static final int CURRENT_CLASS_OFFSET    = 0 * 4;
    public static final int PC_OFFSET      = 1 * 4;
    public static final int PREVIOUS_FRAME_OFFSET   = 2 * 4;
    public static final int STACK_POINTER_OFFSET    = 3 * 4;
    public static final int LOCAL_VAR_OFFSET        = 4 * 4;
    
    
    
}
