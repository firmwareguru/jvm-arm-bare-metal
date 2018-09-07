/*
 * OLEDGraphicsContext.java
 *
 * Created on April 5, 2008, 3:42 PM
 *
 *
 */

package system.graphics;

/**
 *
 * @author Evan Ross
 */
public class OLEDGraphicsContext extends GraphicsContext {
    
    /** Creates a new instance of OLEDGraphicsContext */
    public OLEDGraphicsContext() {
    }

    public native void drawString0(String string, int x, int y);

    public native void drawBytes0(byte[] b, int x, int y);
    
    public native void drawInt0(int value, int x, int y);

    @Override
    public native void drawLine0(int x1, int y1, int x2, int y2);
    
}
