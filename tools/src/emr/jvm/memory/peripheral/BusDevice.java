/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package emr.jvm.memory.peripheral;

/**
 *
 * @author Evan Ross
 */
public interface BusDevice {
    
    public void write(int data);
    
    public int read(int data);
    

}
