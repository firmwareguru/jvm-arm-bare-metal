/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package system.graphics;

/**
 *
 * @author Evan Ross
 */
public class ButtonComponent extends GraphicsComponent{

    int width;
    int height;

    public ButtonComponent(int x, int y, int width, int height) {
        super(x, y);
        this.width = width;
        this.height = height;
    }

    @Override
    public void paintComponent(GraphicsContext g)
    {
        g.drawLine(xcoord, ycoord, xcoord + width, ycoord);
        g.drawLine(xcoord + width, ycoord, xcoord + width, ycoord + height);
        g.drawLine(xcoord + width, ycoord + height, xcoord, ycoord + height);
        g.drawLine(xcoord, ycoord + height, xcoord, ycoord);

    }
}
