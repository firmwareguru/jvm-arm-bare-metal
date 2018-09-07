/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package emr.jvm.adapterlib;

/**
 *
 * @author Evan Ross
 */
public class Math {

    /**
     * Float version.  This has to use a native implementation.
     * 
     * @param a
     * @param b
     * @return
     */
    public static native float pow(float a, float b);

    
}
