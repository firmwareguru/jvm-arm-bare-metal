/*
 * JavaUtilTest.java
 *
 * Created on January 17, 2008, 9:26 PM
 *
 *
 */

package java.util.test;
import java.util.Vector;
/**
 *
 * @author Evan Ross
 */
public class JavaUtilTest {
    
    /** Creates a new instance of JavaUtilTest */
    public JavaUtilTest() {
    
    }
    
    public static void testVector()
    {
        int a = 0x69;
        Object o = new Object();
        
        Vector v = new Vector(10);
        int b = v.capacity();
        
        
        for( int i = 0; i < v.capacity(); i++ )
        {
            v.add(o);
        }
       
        for( int i = 0; i < v.size(); i++ )
            v.get(i).hashCode();
            
    }
    
}
