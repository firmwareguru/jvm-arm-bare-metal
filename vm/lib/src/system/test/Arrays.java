/*
 * Arrays.java
 *
 * Created on October 25, 2006, 8:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package system.test;

/**
 *
 * @author Ross
 */
public class Arrays {
    
    private int arraysField;
    
        
    /** Creates a new instance of Arrays */
    public Arrays() 
    {
        int[] bArray = new int[4];
        bArray[2] = 10;
        bArray[3] = 254;
        
        arraysField = bArray.length;
        
        //int[][] hmm = new int[10][2];
        
    }

    public void multiInt2DimenArray() {
        int[][] twoD1 = new int[3][6];
        int[][] twoD2 = new int[10][];

        int x = twoD2[5][7];

    }

    public void multiInt3DimenArray() {

        int[][][] threeD = new int[12][24][36];
        int[][][] threeD2 = new int[12][24][];
        int[][][] threeD3 = new int[12][24][36];

        int x = threeD3[5][6][22];
    }
    
    public void multiByte2DimenArray() {
        byte[][] twoD1 = new byte[3][6];
        byte[][] twoD2 = new byte[10][];

        byte x = twoD2[5][7];

    }

    public void multiByte3DimenArray() {

        byte[][][] threeD = new byte[12][24][36];
        byte[][][] threeD2 = new byte[12][24][];
        byte[][][] threeD3 = new byte[12][24][36];

        byte x = threeD3[5][6][22];
    }

    public void multiRef2DimenArray() {
        Arrays[][] twoD1 = new Arrays[3][6];
        Arrays[][] twoD2 = new Arrays[10][];

        Arrays x = twoD2[5][7];

    }

    public void multiRef3DimenArray() {

        Arrays[][][] threeD = new Arrays[12][24][36];
        Arrays[][][] threeD2 = new Arrays[12][24][];
        Arrays[][][] threeD3 = new Arrays[12][24][36];

        Arrays x = threeD3[5][6][22];
    }

}
