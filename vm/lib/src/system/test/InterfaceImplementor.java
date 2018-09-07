/*
 * InterfaceImplementor.java
 *
 * Created on January 25, 2008, 9:07 PM
 *
 *
 */

package system.test;

/**
 *
 * @author Evan Ross
 */
public class InterfaceImplementor implements AnInterface {
    
    /** Creates a new instance of InterfaceImplementor */
    public InterfaceImplementor() {
    }

    public void interfaceMethod(int var) {
        int a = var * 2;
    }
    
}
