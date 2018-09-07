/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package system.test;

import java.io.PrintStream;

/**
 *
 * @author Evan Ross
 */
public class FloatTests {

    public static void doTests(PrintStream ps) {
        // Test fconst_0/1/2
        // fstore, fstore_0/1/2
        float f0 = 0.0f;
        float f1 = 1.0f;
        float f2 = 2.0f;
        float f3 = 1.0f;
        float f4 = 2.0f;
        float f5 = 1.0f;



        int a = (int)(-f2);

        if (a == -2) {
            ps.println("Pass FloatTest");
        } else {
            ps.println("Fail FloatTest");
        }

    }



}
