/*
 * GraphicsContainer.java
 *
 * Created on April 5, 2008, 3:07 PM
 *
 *
 */

package system.graphics;

/**
 * A GraphicsContainer is a special kind of GraphicsComponent that transforms
 * coordinates into coordinates relative to the container.
 * A Container is not strictly necessary, but can provide localized handling
 * of other GraphicsComponents in a way similar to a JPanel.
 *
 * @author Evan Ross
 */
public class GraphicsContainer extends GraphicsComponent {
    
    /** Creates a new instance of GraphicsContainer */
    public GraphicsContainer() {
    }
    
}
