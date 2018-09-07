/*
 * GraphicsContext.java
 *
 * Created on April 5, 2008, 3:04 PM
 *
 *
 */

package system.graphics;

/**
 * A GraphicsContext is an abstract representation of the rendering target for
 * GraphicsComponents.
 *
 * @author Evan Ross
 */
public abstract class GraphicsContext {

    int x = 0;
    int y = 0;

    /** Creates a new instance of GraphicsContext */
    public GraphicsContext() {
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void drawString(String string, int x, int y) {
        drawString0(string, this.x + x, this.y + y);
    }

    public void drawBytes(byte[] b, int x, int y) {
        drawBytes0(b, this.x + x, this.y + y);
    }

    public void drawInt(int value, int x, int y) {
        drawInt0(value, this.x + x, this.y + y);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        drawLine0(this.x + x1, this.y + y1, this.x + x2, this.y + y2);
    }


    protected abstract void drawString0(String string, int x, int y);
    protected abstract void drawInt0(int value, int x, int y);
    protected abstract void drawLine0(int x1, int y1, int x2, int y2);
    protected abstract void drawBytes0(byte[] b, int x, int y);
}
