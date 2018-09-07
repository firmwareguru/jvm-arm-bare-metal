/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package java.io;

/**
 *
 * @author Evan Ross
 */
public class PrintStream {
    
    
    
    /**
     *  Delegate OutputStream
     */
    OutputStream os;
    
    public PrintStream(OutputStream os)
    {
        this.os = os;
    }

    public void println() {
        os.write((byte)10);  // LF
    }
    
    public void println(String s)
    {
        byte[] bytes = s.getBytes();
        os.write(bytes);
        os.write((byte)10);  // LF
    }

    public void print(String s) {
        os.write(s.getBytes());
    }

    public void print(int x) {
        //os.write(Integer.toString(x).getBytes());
    }


}
