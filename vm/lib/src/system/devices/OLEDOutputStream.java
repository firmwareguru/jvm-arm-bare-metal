/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package system.devices;

import java.io.OutputStream;

/**
 *
 * @author Evan Ross
 */
public class OLEDOutputStream extends OutputStream {
    
    Display display = null;
    
    int x = 0;
    int y = 0;
    
    int displayWidth;
    int displayHeight;
    
    int fontWidth;
    int fontHeight;
    
    public OLEDOutputStream(Display d)
    {
        display = d;
        
        // Define the window to be one character less on both dimensions.
        fontWidth = d.getFontWidth();
        fontHeight = d.getFontHeight();
        
        displayWidth = d.getDisplayWidth() - fontWidth;
        displayHeight = d.getDisplayHeight() - fontHeight;
    }

    @Override
    public void write(byte b) {
        display.write(b, x, y);
        
        if (x >= displayWidth) {
            x = 0;
            if (y >= displayHeight)
                y = 0;
            else
                y += fontHeight;
        }
        else 
            x += fontWidth;
        
    }

}
