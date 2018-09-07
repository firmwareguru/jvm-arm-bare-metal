/*
 * GraphicsComponent.java
 *
 * Created on April 5, 2008, 2:51 PM
 *
 *
 */

package system.graphics;

import java.util.Vector;

/**
 * A GraphicsComponent is similar to concept to JComponent in that all types
 * of graphics components must inherit from GraphicsComponent.
 *
 * Although GraphicsComponent is nowhere near as sophisticated as JComponent -
 * it is designed to run on very resource constrained embedded systems.
 *
 * All GraphicsComponents have some common features:
 *
 *      - Location (x,y)
 *
 * @author Evan Ross
 */
public class GraphicsComponent extends Vector<GraphicsComponent> {
    
    protected int xcoord;
    protected int ycoord;

    /* Each component maintains a link to its parent */
    protected GraphicsComponent parent;
    
    /** Creates a new instance of GraphicsComponent */
    public GraphicsComponent()
    {
        this(0,0);
    }
    
    public GraphicsComponent(int xcoord, int ycoord)
    {
        this.xcoord = xcoord;
        this.ycoord = ycoord;
    }
    
    /**
     *  Causes this component and all children to be rendered to the given
     *  GraphicsContext.
     */
    public void paint(GraphicsContext g)
    {
        paintComponent(g);
        paintChildren(g);
    }
    
    /** 
     * PaintComponent
     *
     * Renders this component to the context.
     */
    public void paintComponent(GraphicsContext g)
    {
        
    }
    
    /** 
     * PaintChildren
     *
     * Renders this component's children to the context.
     */
    public void paintChildren(GraphicsContext g)
    {
        for (int i = 0; i < size(); i++)
        {
            get(i).paint(g);
        }        
    }
    
    
}
