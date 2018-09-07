/*
 * MainThread.java
 *
 * Created on April 23, 2007, 9:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package system;

import system.test.*;
import com.emr.sudokusolver.CellArray;
import com.emr.sudokusolver.Solver;
import com.emr.sudokusolver.IteratorSolver;
import com.emr.sudokusolver.iterators.CellIterator;
import com.emr.sudokusolver.iterators.RowIterator;
import java.io.OutputStream;
import java.io.PrintStream;
import system.devices.Display;
import system.devices.OLEDGraphicsOutputStream;
import system.graphics.GraphicsContext;
import system.graphics.OLEDGraphicsContext;


/**
 *
 * @author Evan Ross
 */
public class MainThread extends Thread
{
    
    /** Creates a new instance of MainThread */
    public MainThread()
    {
        super();
        setPriority(1);
        setId(0x55);               
    }
    
    @Override
    public void run()
    {
        setPriority(99);
        setId(0x21);
        
        //Thread t = new CountingThread(2000, 50, 5);
        //t.start();
        
        //startSudokuSolver();

        // This does some nice GUI work on the display.
        //system.graphics.test.GraphicsTest.runTests();

        // Here we initialize the display for PrintStream output.
        GraphicsContext context = system.DeviceManager.getGraphicsContext();
        PrintStream ps = new PrintStream(new OLEDGraphicsOutputStream(context, new Display()));

        //String filename = "accessed_sample.mp3";
        String filename = "march.mp3";



        try {
            ps.print("Playing ");
            ps.println(filename);
            javazoom.jlme.util.Player.main(filename);
        } catch (Exception e) {
            
        }

        //DeviceManager deviceManager = new DeviceManager();

        // UART tests
        //Device uart = deviceManager.getDevice(DeviceManager.DEVICE_UART);
        //OutputStream os = uart.getOutputStream();
        //PrintStream ps = new PrintStream(uart.getOutputStream());
        //ps.println("Howdy there!");

        ArrayTest.doTests(ps);
        FloatTests.doTests(ps);
        ArithmeticTest.doTests(ps);
        BranchCompareTest.doTests(ps);

        // Normally need this, or VM crashes, but thats OK.
        
        int c = 0x40;
        byte b = 65;  // A
        while(true)
        {
            c += 4;
            // CANNOT sleep here since this IS the idle thread.
            Thread.yield();
            //os.write(b);
            b++;
        }
        
        
    }


    /*
    private static void startSudokuSolver()
    {
        CellArray cells = new CellArray();
        CellIterator iterator = new RowIterator(cells);
        Solver solver = new IteratorSolver(cells, iterator);
        
        solver.start();
    }
    */
    
}
