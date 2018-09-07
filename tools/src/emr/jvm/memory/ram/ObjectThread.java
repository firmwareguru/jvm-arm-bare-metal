/*
 * ObjectThread.java
 *
 * Created on April 4, 2007, 7:32 PM
 *
 * Defines locations of variables in the Thread object.
 */

package emr.jvm.memory.ram;

/**
 *
 * @author Evan Ross
 */
public class ObjectThread
{
    public static final int NextOffset = 0 * 4;
    public static final int PriorityOffset = 1 * 4;
    public static final int DelayOffset = 2 * 4;
    public static final int IdOffset = 3 * 4;
    public static final int RegistersOffset = ObjectBase.size + 4 * 4;
    
    //public static final int PCOffset = 0;
    //public static final int StackpointerOffset = 1;
    //public static final int CurrentframeOffset = 2;
    //public static final int CurrentclassOffset = 3;
    
    // This is the only value to store now.
    // The rest of the context is in the Frame.    
    public static final int CurrentframeOffset = RegistersOffset + 0 * 4; 
    
}
