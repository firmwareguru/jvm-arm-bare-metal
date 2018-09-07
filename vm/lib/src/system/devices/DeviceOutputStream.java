/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package system.devices;

import java.io.OutputStream;
import system.Device;

/**
 *
 * @author Evan Ross
 */
public class DeviceOutputStream extends OutputStream 
{

    Device device;
    
    public DeviceOutputStream(Device d)
    {
        device = d;
    }
    
    @Override
    public void write(byte b) {
        device.write(b);
    }        

}
