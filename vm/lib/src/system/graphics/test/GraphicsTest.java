/*
 * GraphicsTest.java
 *
 * Created on April 5, 2008, 3:33 PM
 *
 *
 */

package system.graphics.test;

import system.graphics.ButtonComponent;
import system.graphics.CountingComponent;
import system.graphics.GraphicsContainer;
import system.graphics.GraphicsContext;
import system.graphics.GraphicsThread;
import system.graphics.TextComponent;

/**
 *
 * @author Evan Ross
 */
public class GraphicsTest {
    
    /** Creates a new instance of GraphicsTest */
    public GraphicsTest() {
    }
    
    public static void runTests()
    {
        // Get a GraphicsContext from the System.
        // The System supplies whatever is appropriate for the hardware
        // it has enumerated.
        GraphicsContext context = system.DeviceManager.getGraphicsContext();
        
        // Create a simple tree to render.
        GraphicsContainer container = new GraphicsContainer();
        TextComponent text1 = new TextComponent("Its alive!", 5, 20);
        TextComponent text2 = new TextComponent("Really.", 20, 40);
        ButtonComponent button1 = new ButtonComponent(15, 15, 50, 50);
        CountingComponent counter = new CountingComponent(30, 60);
        
        container.add(text1);
        container.add(text2);
        container.add(counter);
        container.add(button1);
        
        // Create a GraphicsThread
        GraphicsThread thread = new GraphicsThread(context, container);
        
        thread.start();
        
        
        
    }
    
}
