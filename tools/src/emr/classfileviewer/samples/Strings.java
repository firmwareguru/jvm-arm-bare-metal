/*
 * Strings.java
 *
 * Created on April 2, 2008, 4:11 PM
 *
 *
 */

package samples;

/**
 *
 * @author Evan Ross
 */
public class Strings {

    
    String s1 = "String1";
    final String s2 = "String2";
    
    /** Creates a new instance of Strings */
    public Strings() {
        
        String s3 = "string 3";
        int k = s3.length();
    }
    
    public void stringProvider()
    {
        String s4 = "string1";
        stringUser(s4);
        stringUser2(s4);
    }
    
    public void stringUser(String s)
    {
        
        char a = s.charAt(0);
    }
    
    public void stringUser2(String s)
    {
        
        int size = s.length();
    }
}
