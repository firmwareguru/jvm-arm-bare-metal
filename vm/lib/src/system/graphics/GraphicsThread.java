/*
 * GraphicsThread.java
 *
 * Created on April 5, 2008, 2:49 PM
 *
 *
 */

package system.graphics;

/**
 * 
 * The GraphicsThread renders the graphicscomponent tree using the given
 * Graphics object.  The rendering process is driven by external events,
 * though these events are not yet defined.  Initially the GraphicsThread
 * will just render the tree once then simply idle indefinately.
 *
 * @author Evan Ross
 */
public class GraphicsThread extends Thread {
    
    GraphicsContext context;
    
    GraphicsComponent tree;
    
    /** Creates a new instance of GraphicsThread */
    public GraphicsThread(GraphicsContext context, GraphicsComponent tree) {
        super();
        setPriority(1);
        setId(5);
        this.context = context;
        this.tree = tree;
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            tree.paint(context);
            
            //Thread.sleep(5000);
            Thread.yield();
        }
        
        
    }
    
}
