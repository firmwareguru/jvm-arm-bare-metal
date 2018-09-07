/*
 * ArithmeticTest.java
 *
 * Created on March 31, 2007, 1:20 PM
 *
 * Test correctness of implementation of arithmetic instructions:
 *    add, sub, mul, div for each of int, long, float, double
 */

package system.test;

import java.io.PrintStream;

/**
 *
 * @author Evan Ross
 */
public class ArithmeticTest
{
    
     
    // Main function to initiate various arithmetic tests 
    public static void doTests(PrintStream ps)
    {
        testInt(ps);
        testShiftInt(ps);
        testLogicalInt(ps);
    }
    
    
    // Test int arithmetic operations: iadd, isub, imul, idiv
    private static void testInt(PrintStream ps)
    {
        // A frame is created for this test.
        //    create 4 local variables.
        int a, b, c, d, e, f, g;
        boolean fail = false;

        a = 0x30;
        b = a + 0x15; // 0x45 iadd
        c = a - 0x12; // 0x1E isub
        d = a * 0x4;  // 0xC0 imul
        e = a / 0x5;  // 0x9  idiv
        f = a % 0x9;  // 0x3  irem
        g = -a;       //      ineg

        if (b != 0x45) {
            ps.println("Fail TestInt: add"); fail = true;
        }
        if (c != 0x1E) {
            ps.println("Fail TestInt: sub"); fail = true;
        }
        if (d != 0xC0) {
            ps.println("Fail TestInt: mult"); fail = true;
        }
        if (e != 0x9) {
            ps.println("Fail TestInt: div"); fail = true;
        }
        if (f != 0x3) {
            ps.println("Fail TestInt: rem"); fail = true;
        }
        if (g != -0x30) {
            ps.println("Fail TestInt: neg"); fail = true;
        }

        if (!fail)
            ps.println("Pass TestInt");
    }
        
    // Test int arithmetic operations: ishr, ishl, iushr
    private static void testShiftInt(PrintStream ps)
    {
        // A frame is created for this test.
        //    create 4 local variables.
        int a, b, c, d, e;
        boolean fail = false;

        a = 0x2;
        e = 0x5;
        b = 0xFF << a;        // 0x000003FC
        c = 0xFFFF8000 >> e;  // 0xFFFFFC00
        d = 0xFFFF8000 >>>e; // 0x07FFFC00

        if (b != 0x000003FC) {
            ps.println("Fail TestShiftInt: ishl"); fail = true;
        }
        if (c != 0xFFFFFC00) {
            ps.println("Fail TestShiftInt: ishr"); fail = true;
        }
        if (d != 0x07FFFC00) {
            ps.println("Fail TestShiftInt: iushr"); fail = true;
        }

        if (!fail)
            ps.println("Pass TestShiftInt");
    }

    // Test int logical operations: iand, ior, ixor
    private static void testLogicalInt(PrintStream ps)
    {
        // A frame is created for this test.
        //    create 4 local variables.
        int a, b, c, d, e;
        boolean fail = false;

        a = 0x99999999;
        b = 0xcccccccc;
        c = a & b;  // 0x88888888
        d = a | b;  // 0xdddddddd
        e = a ^ b;  // 0x55555555

        if (c != 0x88888888) {
            ps.println("Fail TestLogiInt: iand"); fail = true;
        }
        if (d !=0xdddddddd) {
            ps.println("Fail TestLogiInt: ior"); fail = true;
        }
        if (e != 0x55555555) {
            ps.println("Fail TestLogiInt: ixor"); fail = true;
        }

        if (!fail)
            ps.println("Pass TestLogiInt");
    }


}
