/*
 * InterfaceTest.java
 *
 * Created on January 25, 2008, 9:06 PM
 *
 *
 */

package system.test;

/**
 *
 * @author Evan Ross
 */
public class InterfaceTest {
    
    /** Creates a new instance of InterfaceTest */
    public InterfaceTest() {
    }
    
    public static void doTests()
    {
        AnInterface a = new InterfaceImplementor();
        a.interfaceMethod(3);
    }
    
}
