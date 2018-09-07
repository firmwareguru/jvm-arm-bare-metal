/*
 * SubClass.java
 *
 * Created on February 28, 2007, 10:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package system.test;

/**
 *
 * @author Evan Ross
 */
public class SubClass extends SuperClass
{
    public int field2 = 7;
    
    
    /** Creates a new instance of SubClass */
    public SubClass()
    {
    }
    
    public void doSomething()
    {
        int a = 0x33;
        //field1 = 3;
        
    }
   
    
    public int getField()
    {
        return field2;
    }
    
    public int getSuperField()
    {
        return field1;
    }
    
}
