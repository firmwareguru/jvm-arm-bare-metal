/*
 * SuperClass.java
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
public class SuperClass
{
    public int field1 = 0x39;
    
    /** Creates a new instance of SuperClass */
    public SuperClass()
    {
    }
    
    public void doSomething()
    {
        int a = 0x66;
    }
    
    public void doSomethingMore(int arg)
    {
        field1 += arg;
        //field1 = 8;
    }
    
    public int getField()
    {
        return field1;
    }
    
    
}
