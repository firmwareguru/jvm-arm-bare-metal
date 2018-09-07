/*
 * Main.java
 *
 * Created on June 26, 2006, 10:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package samples;

/**
 *
 * @author Ross
 */
public class Main
{
    int variable;
    
    /** Creates a new instance of Main */
    public Main()
    {
        int x = 1;
        int y = 2;
        
        int z = method1(x, y);
        
        variable = z;
        
    }
    
    public int method1(int arg1, int arg2)
    {
        return arg1 + arg2;
    }
    
    public static void main(String[] args)
    {
        new Main();
    }
    
}
