/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package system.devices;

import system.Device;

/**
 *
 * @author Evan Ross
 */
public class SSI extends Device {
    
    private static final int SSI_0_DR = 0x00000008;  // Data register
           
    public SSI(int baseAddress)
    {
        super(baseAddress, SSI_0_DR, SSI_0_DR);
    }

}
