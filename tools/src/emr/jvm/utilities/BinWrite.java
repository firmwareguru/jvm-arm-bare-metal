/*
 * BinWrite.java
 *
 * Created on February 5, 2008, 6:51 PM
 *
 *
 */

package emr.jvm.utilities;

import java.io.*;

/**
 *
 * @author Evan Ross
 */
public class BinWrite {
    
    /** Creates a new instance of BinWrite */
    public BinWrite() {
    }
    
    public static void main(String[] args)
    {
        try {
            FileOutputStream fout = new FileOutputStream("test.bin");
            
            
            fout.write(0xca);
            fout.write(0xfe);
            fout.write(0xba);
            fout.write(0xbe);
              
            fout.close();
            
        } catch (IOException e)
        { 
            System.out.println(e.getMessage());
        }
        
    }
    
}
