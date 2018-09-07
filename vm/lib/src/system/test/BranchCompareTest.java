/*
 * BranchCompareTest.java
 *
 * Created on April 9, 2007, 10:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package system.test;

import java.io.PrintStream;

/**
 *
 * @author Evan Ross
 */
public class BranchCompareTest
{
    
    
    public static void doTests(PrintStream ps)
    {
        doIntCompares(ps);
        doRefCompares(ps);
        testLookupSwitch(ps);
        testTableSwitch(ps);
    }
    
    private static void doIntCompares(PrintStream ps)
    {
        // Test each compare
        // Test both taken and failure paths for each compare
        // Note that branch instructions work on negative logic, that is
        // a branch instruction is generated to jump over the following
        // section of code if the test fails.
        //    For example:
        //        if (0 == 0)
        //           doSomething;
        //        nextStuff;
        //
        //    is translated into a ifne so that if the test is not
        //    true (equal to zero), jump over the following code (doSomething).
        //
        // Basically this means:
        //     ==  ifne
        //     !=  ifeq
        //     <   ifge
        //     >   ifle
        //     <=  ifgt
        //     >=  iflt

        
        

        // First test the ifxx instructions (compare with 0):
        //   ifeq, ifne, iflt, ifge, ifgt, ifle

        int ref = 0x0;
        int refGreater = 0x1;
        int refLess = -0x1;
        boolean fail = false;
        boolean anyFail = false;

        //////////////////////////////////////////
        // ifne
        if (ref == 0) {
            // pass
            fail = false;
        } else {
            // fail
            fail = true;
            anyFail = true;
            ps.println("Fail TestComp: ifne");
        }

        if (refGreater == 0x0) {
            // fail
            fail = true;
            anyFail = true;
            ps.println("Fail TestComp: ifne");
        } else {
            // pass
            fail = false;
        }

        if (refLess == 0x0) {
            // fail
            fail = true;
            anyFail = true;
            ps.println("Fail TestComp: ifne");
        } else {
            // pass
            fail = false;
        }


        //////////////////////////////////////////
        // ifeq
        if (refGreater != 0) {
            // pass
            fail = false;
        } else {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: ifeq");
        }

        if (refLess != 0) {
            // pass
            fail = false;
        } else {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: ifeq");
        }

        if (ref != 0x0) {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: ifeq");
        } else {
            // pass
            fail = false;
        }

        //////////////////////////////////////////
        // ifge
        if (refLess < 0) {
            // pass
            fail = false;
        } else {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: ifge");
        }

        if (refGreater < 0x0) {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: ifge");
        } else {
            // pass
            fail = false;
        }

        if (ref < 0x0) {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: ifge");
        } else {
            // pass
            fail = false;
        }

        //////////////////////////////////////////
        // ifle
        if (refGreater > 0) {
            // pass
            fail = false;
        } else {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: ifle");
        }

        if (refLess > 0x0) {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: ifle");
        } else {
            // pass
            fail = false;
        }

        if (ref > 0x0) {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: ifle");
        } else {
            // pass
            fail = false;
        }

        //////////////////////////////////////////
        // ifgt
        if (refLess <= 0) {
            // pass
            fail = false;
        } else {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: ifgt");
        }

        if (refGreater <= 0x0) {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: ifgt");
        } else {
            // pass
            fail = false;
        }

        if (ref <= 0x0) {
            // pass
            fail = false;
        } else {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: ifgt");
        }

        //////////////////////////////////////////
        // iflt
        if (refGreater >= 0) {
            // pass
            fail = false;
        } else {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: iflt");
        }

        if (refLess >= 0x0) {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: iflt");
        } else {
            // pass
            fail = false;
        }

        if (ref >= 0x0) {
            // pass
            fail = false;
        } else {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: iflt");
        }






        /////////////////////////////////////////////////////////
        // Now test the if_icmpxx instructions (compare with int):
        //   if_icmpeq, if_icmpne, if_icmplt, if_icmpge, if_icmpgt, if_icmple,


        ref = 0x55555;
        int test = 0x55555;
        refGreater = ref + 0x1;
        refLess = ref - 0x1;
        fail = false;

        //////////////////////////////////////////
        // if_icmpne
        if (ref == test) {
            // pass
            fail = false;
        } else {
            // fail
            fail = true;
            anyFail = true;
            ps.println("Fail TestComp: if_icmpne");
        }

        if (refGreater == test) {
            // fail
            fail = true;
            anyFail = true;
            ps.println("Fail TestComp: if_icmpne");
        } else {
            // pass
            fail = false;
        }

        if (refLess == test) {
            // fail
            fail = true;
            anyFail = true;
            ps.println("Fail TestComp: if_icmpne");
        } else {
            // pass
            fail = false;
        }


        //////////////////////////////////////////
        // if_icmpeq
        if (refGreater != test) {
            // pass
            fail = false;
        } else {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: if_icmpeq");
        }

        if (refLess != test) {
            // pass
            fail = false;
        } else {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: if_icmpeq");
        }

        if (ref != test) {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: if_icmpeq");
        } else {
            // pass
            fail = false;
        }

        //////////////////////////////////////////
        // if_icmpge
        if (refLess < test) {
            // pass
            fail = false;
        } else {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: if_icmpge");
        }

        if (refGreater < test) {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: if_icmpge");
        } else {
            // pass
            fail = false;
        }

        if (ref < test) {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: if_icmpge");
        } else {
            // pass
            fail = false;
        }

        //////////////////////////////////////////
        // if_icmple
        if (refGreater > test) {
            // pass
            fail = false;
        } else {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: if_icmple");
        }

        if (refLess > test) {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: if_icmple");
        } else {
            // pass
            fail = false;
        }

        if (ref > test) {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: if_icmple");
        } else {
            // pass
            fail = false;
        }

        //////////////////////////////////////////
        // if_icmpgt
        if (refLess <= test) {
            // pass
            fail = false;
        } else {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: if_icmpgt");
        }

        if (refGreater <= test) {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: if_icmpgt");
        } else {
            // pass
            fail = false;
        }

        if (ref <= test) {
            // pass
            fail = false;
        } else {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: if_icmpgt");
        }

        //////////////////////////////////////////
        // if_icmplt
        if (refGreater >= test) {
            // pass
            fail = false;
        } else {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: if_icmplt");
        }

        if (refLess >= test) {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: if_icmplt");
        } else {
            // pass
            fail = false;
        }

        if (ref >= test) {
            // pass
            fail = false;
        } else {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: if_icmplt");
        }



        // Use the variables in an attempt to limit compiler optimsations.
        // The javac compiler by Sun does very little if not any optimisations.
        if (!anyFail && !fail)
            ps.println("Pass TestIntComp");
        
    }

    private static void doRefCompares(PrintStream ps)
    {
        // Test each compare

        // Basically this means:
        //     ==  if_acmpne
        //     !=  if_acmpeq




        // Test the if_acmpxx instructions (compare with ref):
        //   if_acmpeq, if_acmpne

        // Memory leak! (no GC)
        Object ref1 = new Object();
        Object ref2 = new Object();
        Object ref1Same = ref1;
        Object refnull = null;

        boolean fail = false;
        boolean anyFail = false;

        //////////////////////////////////////////
        // if_acmpne
        if (ref1Same == ref1) { // same
            // pass
            fail = false;
        } else {
            // fail
            fail = true;
            anyFail = true;
            ps.println("Fail TestComp: if_acmpne");
        }

        if (ref2 == ref1) { // different
            // fail
            fail = true;
            anyFail = true;
            ps.println("Fail TestComp: if_acmpne");
        } else {
            // pass
            fail = false;
        }

        //////////////////////////////////////////
        // if_acmpeq
        if (ref2 != ref1) {
            // pass
            fail = false;
        } else {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: if_acmpeq");
        }

        if (ref1Same != ref1) {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: if_acmpeq");
        } else {
            // pass
            fail = false;
        }

        //////////////////////////////////////////
        // ifnull
        if (ref1 != null) {
            // pass
            fail = false;
        } else {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: ifnull");
        }

        if (refnull != null) {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: ifnull");
        } else {
            // pass
            fail = false;
        }

        //////////////////////////////////////////
        // ifnonnull
        if (refnull == null) {
            // pass
            fail = false;
        } else {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: ifnonnull");
        }

        if (ref1 == null) {
            // fail
            anyFail = true;
            fail = true;
            ps.println("Fail TestComp: ifnonnull");
        } else {
            // pass
            fail = false;
        }

        // Use the variables in an attempt to limit compiler optimsations.
        // The javac compiler by Sun does very little if not any optimisations.
        if (!anyFail && !fail)
            ps.println("Pass TestRefComp");

    }


    private static final int DEFAULT_RESULT = 0x999;

    private static void testLookupSwitch(PrintStream ps)
    {
        // Run the keys through the switch...
        //  if the key matches we should get back the key,
        //  otherwise the default is run where we get back
        //  the default value.

        // Run through all keys 0:99

        int failKey = -1;
        for (int key = 0; key <= 99; key++)
        {
            int result = doLookupSwitch(key);

            // test for special keys
            if (key == 1 ||
                key == 2 ||
                key == 4 ||
                key == 6 ||
                key == 8 ||
                key == 9 ||
                key == 19 ||
                key == 31 ||
                key == 97)
            {
                if (result != key) {
                    failKey = key;
                    break;
                }
            }
            else
            {
                if (result != DEFAULT_RESULT) {
                    failKey = key;
                    break;
                }
            }
        }

        if (failKey != -1) {
            ps.print("Fail LookupSwitch: ");
            ps.print(failKey);
            ps.println();
        } else {
            ps.println("Pass LookupSwitch");
        }
    }

    private static int doLookupSwitch(int key)
    {
        int result;

        switch(key)
        {
            case 1: result = 1; break;
            case 2: result = 2; break;
            //case 3: result = 3; break;
            case 4: result = 4; break;
            //case 5: result = 5; break;
            case 6: result = 6; break;
            //case 7: result = 7; break;
            case 8: result = 8; break;
            case 9: result = 9; break;
            case 19: result = 19; break;
            case 31: result = 31; break;
            case 97: result = 97; break;

            default:
                result = DEFAULT_RESULT;
                break;
        }

        return result;
    }


    private static void testTableSwitch(PrintStream ps)
    {
        // Run the keys through the switch...
        //  if the key matches we should get back the key,
        //  otherwise the default is run where we get back
        //  the default value.

        // Run through all keys 0:25

        int failKey = -1;
        for (int key = 0; key <= 25; key++)
        {
            int result = doTableSwitch(key);

            // test for special keys
            if (key == 4 ||
                key == 6 ||
                key == 8 ||
                key == 9 ||
                key == 12 ||
                key == 19)
            {
                if (result != key) {
                    failKey = key;
                    break;
                }
            }
            else
            {
                if (result != DEFAULT_RESULT) {
                    failKey = key;
                    break;
                }
            }
        }

        if (failKey != -1) {
            ps.print("Fail TableSwitch: ");
            ps.print(failKey);
            ps.println();
        } else {
            ps.println("Pass TableSwitch");
        }
    }

    private static int doTableSwitch(int key)
    {
        int result;

        switch(key)
        {
            //case 1: result = 1; break;
            //case 2: result = 2; break;
            //case 3: result = 3; break;
            case 4: result = 4; break;
            //case 5: result = 5; break;
            case 6: result = 6; break;
            //case 7: result = 7; break;
            case 8: result = 8; break;
            case 9: result = 9; break;
            case 12: result = 12; break;
            case 19: result = 19; break;

            default:
                result = DEFAULT_RESULT;
                break;
        }

        return result;
    }

    
    
    
}
