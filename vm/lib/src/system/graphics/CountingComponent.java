/*
 * CountingComponent.java
 *
 * Created on May 6, 2008, 9:50 PM
 *
 *
 */

package system.graphics;

/**
 *
 * @author Evan Ross
 */
public class CountingComponent   extends GraphicsComponent 
{
    int count = 0;
    
    /** Creates a new instance of CountingComponent */
    public CountingComponent(int x, int y) {
        super(x, y);    
    }
    
    
    
    /** 
     * Increments then renders the count to the context.
     */
    public void paintComponent(GraphicsContext g)
    {
        count++;
        g.drawInt(count, xcoord, ycoord);
    }      
    
}
