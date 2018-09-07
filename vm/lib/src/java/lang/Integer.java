/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package java.lang;

/**
 *
 * @author Evan Ross
 */
public class Integer {

    public static String toString(int i) {
        // Very lame implementation.
        // Just enough to get some numbers out for debugging.
        // Don't have tableswitch byte codes implemented yet.
        if (i == 0) {
            return "0";
        } else if (i == 1) {
            return "1";
        } else if (i == 2) {
            return "2";
        } else if (i == 3) {
            return "3";
        } else if (i == 4) {
            return "4";
        } else if (i == 5) {
            return "5";
        } else if (i == 6) {
            return "6";
        } else if (i == 7) {
            return "7";
        } else if (i == 8) {
            return "8";
        } else if (i == 9) {
            return "9";
        } else if (i == 10) {
            return "10";
        } else {
            return "No Int";
        }
    }

}
