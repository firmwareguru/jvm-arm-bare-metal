/*
 * ZoomOutPanel.java
 *
 * Created on May 16, 2006, 9:29 PM
 * 
 * This panel is the drawing area to which the lines representing memory (byte) locations are drawn.
 * The thickness of each line is calculated based on the size of the memory and the size of the panel.
 * If the line thickness reduces to one pixel, then the panel is resized to accomodate all the lines
 * and a JScrollPane will allow this.
 */

package emr.jvm.visualization;

import javax.swing.*;
import java.awt.*;
import java.nio.ByteBuffer;


/**
 *
 * @author Ross
 */
public class ZoomOutPanel extends JPanel {
    
    ByteBuffer memory;
            
    Color[] tags;
    
    int drawCount = 0;
    
    int visibleIndex = 0;

    static final Color highLiteColor = Color.RED;
    
    /** Creates a new instance of ZoomOutPanel */
    public ZoomOutPanel() {
        
        super();
        setOpaque(true);
        
        //this.setPreferredSize(new Dimension(300, 16384));
        
    }
    
    public void setVisibleIndex(int index_)
    {
        visibleIndex = index_;
    }
    
    /* override the paintComponent() method to draw the lines */
    @Override
    protected void paintComponent(Graphics g) {
        
        //super.paintComponent(g);

        // cast graphics
        Graphics2D g2d = (Graphics2D) g.create();

        // get dimensions of panel which can change dynamically due to user resize etc.
        Insets insets = getInsets();
        int byteWidth = (getWidth() - insets.right - insets.left) / 4;
        int height = getHeight() - insets.top - insets.bottom;

        if(tags == null)
            return;

        Rectangle region = g.getClipBounds();
        
        // set the 'scale factor'
        int scaleFactor = height / (tags.length / 4); // 4 bytes per line
        if (scaleFactor < 1)
            scaleFactor = 1;
        
        // make sure minimum size is the size of the memory
        // TODO
        
        // setup the line width
        g2d.setStroke(new BasicStroke(scaleFactor));
        //Log.event("Painting zoomOutPanel | line width = " + scaleFactor);
        
        //g2d.setColor(Color.red);
        //Color c;
        
        /*
        Rectangle r = g.getClipBounds();
        drawCount++;
        System.out.println("ZoomOutPanel: clip x: " + r.x + " y: " + r.y + " height: " + r.height + " drawCount = " + drawCount);
         */
       
        //for(int count = 0; count < memory.length; count++)
        for( int lineCount = region.y / scaleFactor; lineCount < ((region.y + region.height) / scaleFactor ) + 1; lineCount++ )
        {
            // For each line we draw 4 bytes
            for (int byteCount = 0; byteCount < 4; byteCount++)
            {
                int index = (lineCount * 4 + byteCount);
                
                // don't go beyond memory bounds when painting at bottom of array
                if(index >= tags.length)
                    continue;
                //if(count % 10 == 0) c = Color.red;
                //else if(count % 2 == 0) c = Color.yellow;
                //else c = Color.blue;
                //if(count == visibleIndex)
                //    g2d.setColor(highLiteColor);
                if(tags[index] == Color.white || tags[index] == null)
                    g2d.setColor(getBackground());
                else
                    g2d.setColor(tags[index]);
            
                // draw 1/4 of the line, one for each byte.
                g2d.drawLine(byteCount * byteWidth, lineCount * scaleFactor , (byteCount + 1) * byteWidth, lineCount * scaleFactor);
        
            }
        }
        
        
        
        //g2d.setPaint(Color.red);
        //g2d.fill(new Rectangle2D.Double(0,0,getWidth()-1, getHeight()-1));
        
        
        g2d.dispose();
        
    }
    
    public void setMemory(ByteBuffer memory_, Color[] tags_) {
        memory = memory_;
        tags = tags_;
        
    }
    
    
    
}
