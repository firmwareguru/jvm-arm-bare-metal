/*
 * MiscTest.java
 *
 * Created on January 20, 2008, 6:19 PM
 *
 *
 */

package system.test;

import java.util.Vector;

/**
 *
 * @author Evan Ross
 */
public class MiscTest {
    
    /** Creates a new instance of MiscTest */
    public MiscTest() {
    }
    
    public static void checkCast()
    {
        // Implicit checkcast invocation.
        // In this case the IDE will enforce it and checkcast is not required to be implemented.
        //Vector<SomeClass> v = new Vector<SomeClass>(1);
        //v.add(new SomeClass());
        //SomeClass c = v.get(0);
        
        // Explicit checkcast invocation.
        // In this case the IDE will NOT enforce it and checkcast is required to be implemented.
        Vector v = new Vector();
        v.add(new SomeClass());
        SomeClass c = (SomeClass)v.get(0);
        
        
    }
    
    public static void remainderInt()
    {
        int a = 5;
        int b = 3;
        
        int c = a % b; // = 2
        c = a % c; // = 1
        c = b % c; // = 0
    }
    
}
