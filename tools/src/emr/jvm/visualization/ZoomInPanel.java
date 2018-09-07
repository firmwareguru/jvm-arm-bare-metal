/*
 * ZoomInPanel.java
 *
 * Created on May 22, 2006, 9:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.jvm.visualization;

import javax.swing.*;
import java.awt.*;
import java.nio.ByteBuffer;
/**
 *
 * @author Ross
 */

public class ZoomInPanel extends JPanel {
    
    static final int columnDividerXCoord = 80; // pixels from left
    public static final int boxHeight = 20; // depends on the font...
    private static final int addressWidth = columnDividerXCoord; // width of the address field
    private static final Color addressBGColor = Color.LIGHT_GRAY;
    private static final Color addressFGColor = Color.BLACK;
    static final int textYOffset = 5; // from bottom of box
    static final int textXOffset = 20; // from left
    static final int textAddressXCoord = 5;

    static final Color highLiteColor = Color.RED;
    
    int drawCount = 0;
    
    int visibleIndex = 0;
    
    ByteBuffer memory;
    Color[] tags;
    int baseAddress;
    
    /** Creates a new instance of ZoomInPanel */
    public ZoomInPanel() {
        super();
        setOpaque(true);
        
        
    }
    
    public int getBoxHeight()
    {
        return boxHeight;
    }
    
    public void setVisibleIndex(int index_)
    {
        visibleIndex = index_;
    }
    
   
    @Override
    protected void paintComponent(Graphics g) {
        
        //super.paintComponent(g);
        // cast graphics
        Graphics2D g2d = (Graphics2D) g;
        
 
        
        // get dimensions of panel which can change dynamically due to user resize etc.
        Insets insets = getInsets();
        int width = getWidth() - insets.right - insets.left;
        int byteWidth = (width - addressWidth)/ 4; // remaining space / 4
        int height = getHeight() - insets.top - insets.bottom;
         
        if(memory == null || tags == null)
            return;

        Rectangle region = g.getClipBounds();

        //System.err.println("Start paint -----");
        for(int lineCount = region.y / boxHeight ; lineCount < ((region.height + region.y) / boxHeight) + 1; lineCount++)
        {
            // Fill in the address background color
            g2d.setColor(getBackground());            
            g2d.fillRect(0, lineCount * boxHeight, addressWidth, boxHeight);
            
            g2d.setColor(addressFGColor);
            //g2d.drawString("0x" + Integer.toHexString(lineCount * 4).toUpperCase(), textAddressXCoord, (lineCount + 1) * boxHeight - textYOffset );
            g2d.drawString("0x" + String.format("%08X", baseAddress + (lineCount * 4)), textAddressXCoord, (lineCount + 1) * boxHeight - textYOffset );

            for (int byteCount = 0; byteCount < 4; byteCount++)
            {
                int index = lineCount * 4 + byteCount;
                
                // don't go beyond memory bounds when painting at bottom of array
                if(index >= tags.length )
                    continue;

                // Fill in the data background color
                if (tags[index] != null)
                    g2d.setColor(tags[index]);            
                else
                    g2d.setColor(getBackground());            
                    
                g2d.fillRect(addressWidth + byteCount * byteWidth, lineCount * boxHeight, byteWidth, boxHeight);

            // highlight the particular location that was read/written
//                if(index == visibleIndex )
//                {
//                    //Color c = tags[count];
//                    //c = c.brighter();
//                    g2d.setColor(highLiteColor);
//                }
//                else
                if(tags[index] == Color.black)
                    g2d.setColor(Color.white);
                else
                    g2d.setColor(Color.black);
                //g2d.drawString(Integer.toHexString(memory.getInt(index)).toUpperCase(), columnDividerXCoord + textXOffset, (lineCount + 1) * boxHeight - textYOffset );
                g2d.drawString(String.format("%02X", memory.get(index) & 0xff), addressWidth + textAddressXCoord + byteCount * byteWidth, (lineCount + 1) * boxHeight - textYOffset );
                //System.out.println("Drawing count: " + count );
            }

        }
        // now draw a vertical line separating Address column from Contents
        //g2d.drawLine(columnDividerXCoord, 0, columnDividerXCoord, getHeight() - 1);
        g2d.drawLine(columnDividerXCoord, region.y, columnDividerXCoord, region.y + region.height);

        //System.err.println("End paint -----");
        
        
        //drawCount++;
        //System.out.println("ZoomInPanel: clip x: " + r.x + " y: " + r.y + " height: " + r.height + " drawCount = " + drawCount);
        
        

    }
    
    public void setMemory(ByteBuffer memory_, Color[] tags_, int baseAddress_) {
        memory = memory_;
        tags = tags_;
        baseAddress = baseAddress_;
        
        
                    
    }
 
    
}
