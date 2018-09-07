/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package system;

import java.io.InputStream;
import java.io.OutputStream;
import system.devices.DeviceOutputStream;

/**
 *
 * @author Evan Ross
 */
public abstract class Device 
{
    
    /**
     *  Base address of the Device's
     *  register set.
     */
    protected int baseAddress;
    
    protected int readAddress;
    protected int writeAddress;
    
    public Device(int baseAddress, int readAddressOffset, int writeAddressOffset)
    {
        this.baseAddress = baseAddress;
        this.readAddress = baseAddress + readAddressOffset;
        this.writeAddress = baseAddress + writeAddressOffset;
    }
    
    
    public void write(int b)
    {
        Memory.setWord(writeAddress, b);
    }
    
    public int read()
    {
        return Memory.getWord(readAddress);
    }
    
    public OutputStream getOutputStream() {
        return new DeviceOutputStream(this);
    }
    
    public InputStream getInputStream() {
        return null;
    }
    

}
