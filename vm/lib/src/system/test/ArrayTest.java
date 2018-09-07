/*
 * ArrayTest.java
 *
 * Created on March 31, 2007, 4:48 PM
 *
 * Test the array implementation.
 *
 * There are many instructions for arrays.  
 * Array creation instructions:
 *    newarray, anewarray
 */

package system.test;

import java.io.PrintStream;

/**
 *
 * @author Evan Ross
 */
public class ArrayTest
{
    
    public static void doTests(PrintStream ps)
    {
        testArrayInit(ps);
        test1DArrayAccess(ps);
        test2DArrayAccess(ps);
        test3DArrayAccess(ps);
        
        //int[] x = fibonacci(10);
        
    }

    /**
     * Array init consists of initializing the array length field
     * as well as each element to its default init type.
     *
     * The array length returned by the .length operation should be
     * the same as the number of elements for each type of array:
     * char/byte : byte
     * short     : short
     * int, float, reference: single
     * double, long : double
     *
     * the default init type is (int)0 for byte,bool,char,short,int types,
     * (float)0.0 for float, (double)0.0 for double and null for ref types.
     */
    public static void testArrayInit(PrintStream ps) {
        // test sizes 0 to 20 for each type
        int count = 0;
        int failCount = -1;
        int failLength = -1;

        byte defaultByteValue = 0;
        short defaultShortValue = 0;
        int defaultIntValue = 0;
        Object defaultRefValue = null;


        byte[] byteArray;
        count++;
        for (int i = 0; i < 10; i++) {
            byteArray = new byte[i];
            if (i != byteArray.length) {
                failCount = i;
                failLength = byteArray.length;
                break;
            }
            for (int j = 0; j < i; j++) {
                if (byteArray[j] != defaultByteValue) {
                    failCount = i;
                    failLength = byteArray.length;
                    break;
                }
            }
        }

        char[] charArray;
        count++;
        for (int i = 0; i < 10; i++) {
            charArray = new char[i];
            if (i != charArray.length) {
                failCount = i;
                failLength = charArray.length;
                break;
            }
            for (int j = 0; j < i; j++) {
                if (charArray[j] != defaultByteValue) {
                    failCount = i;
                    failLength = charArray.length;
                    break;
                }
            }
        }

        short[] shortArray;
        count++;
        for (int i = 0; i < 10; i++) {
            shortArray = new short[i];
            if (i != shortArray.length) {
                failCount = i;
                failLength = shortArray.length;
                break;
            }
            for (int j = 0; j < i; j++) {
                if (shortArray[j] != defaultShortValue) {
                    failCount = i;
                    failLength = shortArray.length;
                    break;
                }
            }

        }
        
        int[] intArray;
        count++;
        for (int i = 0; i < 10; i++) {
            intArray = new int[i];
            if (i != intArray.length) {
                failCount = i;
                failLength = intArray.length;
                break;
            }
            for (int j = 0; j < i; j++) {
                if (intArray[j] != defaultIntValue) {
                    failCount = i;
                    failLength = intArray.length;
                    break;
                }
            }
        }

        float[] floatArray;
        count++;
        for (int i = 0; i < 10; i++) {
            floatArray = new float[i];
            if (i != floatArray.length) {
                failCount = i;
                failLength = floatArray.length;
                break;
            }
        }

        long[] longArray;
        count++;
        for (int i = 0; i < 10; i++) {
            longArray = new long[i];
            if (i != longArray.length) {
                failCount = i;
                failLength = longArray.length;
                break;
            }
        }

        double[] doubleArray;
        count++;
        for (int i = 0; i < 10; i++) {
            doubleArray = new double[i];
            if (i != doubleArray.length) {
                failCount = i;
                failLength = doubleArray.length;
                break;
            }
        }

        Object[] objectArray;
        count++;
        for (int i = 0; i < 10; i++) {
            objectArray = new Object[i];
            if (i != objectArray.length) {
                failCount = i;
                failLength = objectArray.length;
                break;
            }
            for (int j = 0; j < i; j++) {
                if (objectArray[j] != defaultRefValue) {
                    failCount = i;
                    failLength = objectArray.length;
                    break;
                }
            }
        }

        if (failLength != -1) { // a failure.  output the results
            ps.print("Fail ArrayInit: ");
            ps.print(Integer.toString(count));
            ps.print(" ");
            ps.print(Integer.toString(failCount));
            ps.print(" ");
            ps.println(Integer.toString(failLength));
        } else {
            ps.println("Pass ArrayInit");
        }
    
    }

    /**
     * Test array load and store instructions for all 1D array types.
     * 
     * @param ps
     */
    private static void test1DArrayAccess(PrintStream ps) {
        int count = 0;
        int failCount = -1;
        int failIndex = -1;

        
        byte[] byteArray;
        count++;
        for (int i = 0; i < 10; i++) {
            byteArray = new byte[i];
            for (int n = 0; n < i; n++) {
                byte value = (byte)(n + i + 0xaa);
                byteArray[n] = value;
                byte test = byteArray[n];
                if (test != value) {
                    failCount = i;
                    failIndex = n;
                    break;
                }
            }
        }

        char[] charArray;
        count++;
        for (int i = 0; i < 10; i++) {
            charArray = new char[i];
            for (int n = 0; n < i; n++) {
                char value = (char)(n + i + 0xaa);
                charArray[n] = value;
                char test = charArray[n];
                if (test != value) {
                    failCount = i;
                    failIndex = n;
                    break;
                }
            }
        }

        short[] shortArray;
        count++;
        for (int i = 0; i < 10; i++) {
            shortArray = new short[i];
            for (int n = 0; n < i; n++) {
                short value = (short)(n + i + 0xaa);
                shortArray[n] = value;
                short test = shortArray[n];
                if (test != value) {
                    failCount = i;
                    failIndex = n;
                    break;
                }
            }
        }
        
        int[] intArray;
        count++;
        for (int i = 0; i < 10; i++) {
            intArray = new int[i];
            for (int n = 0; n < i; n++) {
                int value = (n + i + 0xaa);
                intArray[n] = value;
                int test = intArray[n];
                if (test != value) {
                    failCount = i;
                    failIndex = n;
                    break;
                }
            }
        }

        float[] floatArray;
        count++;
        for (int i = 0; i < 10; i++) {
            floatArray = new float[i];
            for (int n = 0; n < i; n++) {
                float value = (n + i + 4.73f);
                floatArray[n] = value;
                float test = floatArray[n];
                if (test != value) {
                    failCount = i;
                    failIndex = n;
                    break;
                }
            }
        }

        if (failIndex != -1) { // a failure.  output the results
            ps.print("Fail 1DArrayAccess: ");
            ps.print(Integer.toString(count));
            ps.print(" ");
            ps.print(Integer.toString(failCount));
            ps.print(" ");
            ps.println(Integer.toString(failIndex));
        } else {
            ps.println("Pass 1DArrayAccess");
        }


    }

    /**
     * Test array load and store instructions for all 1D array types.
     *
     * @param ps
     */
    private static void test2DArrayAccess(PrintStream ps) {
        int count = 0;
        int failCount = -1;
        int failIndex = -1;


        byte[][] byteArray;
        count++;

        // Create all arrays first, then read them out.

        for (int i = 0; i < 10; i++) { // Create all combination of dimensions
            for (int j = 0; j < 10; j++) {
                byteArray = new byte[i][j];
                for (int n = 0; n < i; n++) {
                    for (int p = 0; p < j; p++) {
                        // Make sure the array is empty and initialized.
                        if (byteArray[n][p] != 0) {
                            failCount = i;
                            failIndex = n;
                            break;
                        }
                        byte value = (byte)(n + p + i + j + 0x88);
                        byteArray[n][p] = value;
                        byte test = byteArray[n][p];
                        if (test != value) {
                            failCount = i;
                            failIndex = n;
                            break;
                        }
                    }
                }
            }
        }


        /*
        char[] charArray;
        count++;
        for (int i = 0; i < 10; i++) {
            charArray = new char[i];
            for (int n = 0; n < i; n++) {
                char value = (char)(n + 0xaa);
                charArray[n] = value;
                char test = charArray[n];
                if (test != value) {
                    failCount = i;
                    failIndex = n;
                    break;
                }
            }
        }

        short[] shortArray;
        count++;
        for (int i = 0; i < 10; i++) {
            shortArray = new short[i];
            for (int n = 0; n < i; n++) {
                short value = (short)(n + 0xaa);
                shortArray[n] = value;
                short test = shortArray[n];
                if (test != value) {
                    failCount = i;
                    failIndex = n;
                    break;
                }
            }
        }

        int[] intArray;
        count++;
        for (int i = 0; i < 10; i++) {
            intArray = new int[i];
            for (int n = 0; n < i; n++) {
                int value = (n + 0xaa);
                intArray[n] = value;
                int test = intArray[n];
                if (test != value) {
                    failCount = i;
                    failIndex = n;
                    break;
                }
            }
        }
        */
        
        if (failIndex != -1) { // a failure.  output the results
            ps.print("Fail 2DArrayAccess: ");
            ps.print(Integer.toString(count));
            ps.print(" ");
            ps.print(Integer.toString(failCount));
            ps.print(" ");
            ps.println(Integer.toString(failIndex));
        } else {
            ps.println("Pass 2DArrayAccess");
        }


    }
    

    /**
     * Test array load and store instructions for all 1D array types.
     *
     * @param ps
     */
    private static void test3DArrayAccess(PrintStream ps) {
        int count = 0;
        int failCount = -1;
        int failIndex = -1;

        int nD = 5;

        byte[][][] byteArray;
        count++;

        // Create all arrays first, then read them out.

        for (int i = 0; i < nD; i++) { // Create all combination of dimensions
            for (int j = 0; j < nD; j++) {
                for (int k = 0; k < nD; k++) {
                    byteArray = new byte[i][j][k];
                    for (int n = 0; n < i; n++) {
                        for (int p = 0; p < j; p++) {
                            for (int o = 0; o < k; o++) {
                                // Make sure the array is empty and initialized.
                                if (byteArray[n][p][o] != 0) {
                                    failCount = i;
                                    failIndex = n;
                                    break;
                                }
                                byte value = (byte)(n + p + o + i + j + 0x88);
                                byteArray[n][p][o] = value;
                                byte test = byteArray[n][p][o];
                                if (test != value) {
                                    failCount = i;
                                    failIndex = n;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }


        /*
        char[] charArray;
        count++;
        for (int i = 0; i < 10; i++) {
            charArray = new char[i];
            for (int n = 0; n < i; n++) {
                char value = (char)(n + 0xaa);
                charArray[n] = value;
                char test = charArray[n];
                if (test != value) {
                    failCount = i;
                    failIndex = n;
                    break;
                }
            }
        }

        short[] shortArray;
        count++;
        for (int i = 0; i < 10; i++) {
            shortArray = new short[i];
            for (int n = 0; n < i; n++) {
                short value = (short)(n + 0xaa);
                shortArray[n] = value;
                short test = shortArray[n];
                if (test != value) {
                    failCount = i;
                    failIndex = n;
                    break;
                }
            }
        }

        int[] intArray;
        count++;
        for (int i = 0; i < 10; i++) {
            intArray = new int[i];
            for (int n = 0; n < i; n++) {
                int value = (n + 0xaa);
                intArray[n] = value;
                int test = intArray[n];
                if (test != value) {
                    failCount = i;
                    failIndex = n;
                    break;
                }
            }
        }
        */

        if (failIndex != -1) { // a failure.  output the results
            ps.print("Fail 3DArrayAccess: ");
            ps.print(Integer.toString(count));
            ps.print(" ");
            ps.print(Integer.toString(failCount));
            ps.print(" ");
            ps.println(Integer.toString(failIndex));
        } else {
            ps.println("Pass 3DArrayAccess");
        }


    }


    /**
     *  Returns an array filled with fibonacci numbers up to the desired array size
     */
    public static int[] fibonacci(int size)
    {
        if( size < 2 )
            return null;
        
        int[] x = new int[size];
        
        
        x[0] = 0;
        x[1] = 1;
        
        for( int i = 2; i < x.length; i++ )
        {
            x[i] = x[i-1] + x[i-2];
        }
        
        return x;
    }

   
    
}
