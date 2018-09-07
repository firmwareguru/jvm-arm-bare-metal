/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package system;

/**
 *
 * @author Evan Ross
 */
public class Memory {

    /**
     * 
     * @param address - the memory location to obtain the value from
     * @return the value at the given memory location
     */
    public static native final int getWord(int address);
    
    /**
     * 
     * @param address the memory location to set value to
     * @param word the value to set
     */
    public static native final void setWord(int address, int word);

}
