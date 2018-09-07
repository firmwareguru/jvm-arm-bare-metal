/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package java.util;

/**
 *
 * @author Evan Ross
 */
public class Arrays {

    public static float[] copyOfRange(float[] original, int from, int to) {
        int size = to - from;
        if (size < 0)
            return null;
        if (original == null)
            return null;
        if (original.length < size)
            return null;

        float[] newArray = new float[size];
        for (int i = 0; i < size; i++) {
            newArray[i] = original[i + from];
        }

        return newArray;
    }

}
