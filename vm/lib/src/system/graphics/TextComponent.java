/*
 * TextComponent.java
 *
 * Created on April 5, 2008, 3:03 PM
 *
 *
 */

package system.graphics;

/**
 * A TextComponent displays its text using the Graphics object.
 *
 * The text can be dynamically updated by calling the setText(String)
 * method.  Each call will request the painting engine to repaint
 * (all components or just this component?).
 *
 * @author Evan Ross
 */
public class TextComponent  extends GraphicsComponent {
    
    private String text;
    
    /** Creates a new instance of TextComponent */
    public TextComponent(String s, int x, int y) {
        super(x, y);
        text = s;
    }
    
    public void setText(String text)
    {
        this.text = text;
    }
    
    /** 
     * Renders the text to the context.
     */
    @Override
    public void paintComponent(GraphicsContext g)
    {
        g.drawString(text, xcoord, ycoord);
    }    
    
}
