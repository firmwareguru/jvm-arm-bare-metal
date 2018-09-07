/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package system.devices;

import java.io.OutputStream;
import system.graphics.GraphicsContext;

/**
 * The OLEDGraphicsOutputStream wraps an OLEDGraphicsContext and
 * uses the drawString methods to render text to the display.  This
 * is currently the only way to access the display (hardware or simulator).
 * OLEDOutputStream used SSI and GPIO devices which are not implemented yet.
 *
 * This class represents the display with a buffer of byte arrays.
 * Each array is a line of the display.  Line feeds (byte 10) return
 * the cursor to the beginning of the current line.  Carriage returns (13)
 * move the cursor to the next line at the same position.
 *
 * Display scrolling is supported - lines of text rolls off the top as it
 * fills the bottom.
 *
 * @author Evan Ross
 */
public class OLEDGraphicsOutputStream extends OutputStream {

    // Don't support 2D arrays yet.
    // 12 rows, 21 chars each
    static final int NUMROWS = 12;
    static final int NUMCHARS = 21;
    
    byte[] row1 = new byte[NUMCHARS];
    byte[] row2 = new byte[NUMCHARS];

    int cursorCol = 0;
    int cursorRow = 0;

    int cursorX = 0;
    int cursorY = 0;

    int displayWidth;
    int displayHeight;

    int fontWidth;
    int fontHeight;


    GraphicsContext context;

    public OLEDGraphicsOutputStream(GraphicsContext c, Display d) {
        this.context = c;

        // Define the window to be one character less on both dimensions.
        fontWidth = d.getFontWidth();
        fontHeight = d.getFontHeight();

        displayWidth = d.getDisplayWidth() - fontWidth;
        displayHeight = d.getDisplayHeight() - fontHeight;

    }

    @Override
    public void write(byte b) {
        // Interpret the bytes
        if (b == (byte)10) {
            // Line feed.  Increment the cursor's X coord.
            // For now also look like CR.
            cursorY += fontHeight;
            cursorX = 0;
        } else {
            //row1[cursorCol++] = b;
        }
    }

    @Override
    public void write(byte[] b) {
        for (int i = 0; i < b.length; i++) {
            //row1[cursorCol++] = b[i];
        }

        context.drawBytes(b, cursorX, cursorY);
        cursorX += b.length * fontWidth;
    }

}
