/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package emr.jvm.memory.peripheral;

/**
 * A Signalable class can be signalled by pins from a GPIOPort.
 * 
 * @author Evan Ross
 */
public interface Signalable {

    /**
     * Send a signal to the device.
     */
    public void sendSignal(boolean signal);
    
    /**
     * 
     * @return true - a signal is sent by the device.
     */
    public boolean getSignal();
}
