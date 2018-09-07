/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package java.io;

/**
 *
 * @author Evan Ross
 */
public abstract class OutputStream {
    
    public abstract void write(byte b);
    
    public void write(byte[] b)
    {
        for (int i = 0; i < b.length; i++) {
            write(b[i]);
        }
    }

}
