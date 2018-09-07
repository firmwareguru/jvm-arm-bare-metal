/*
 * Initialization.java
 *
 * Created on February 12, 2007, 9:00 PM
 *
 * This class' startup() method is called during JVM startup.  The startup method is
 * responsible for starting any "system" threads (such as idle - mandatory, and garbage collector ),
 * initializing any hardware and finally starting the "Main" thread where the application
 * code resides.
 *
 */

package system;

import system.test.*;

import com.emr.sudokusolver.CellArray;
import com.emr.sudokusolver.Solver;
import com.emr.sudokusolver.IteratorSolver;
import com.emr.sudokusolver.iterators.CellIterator;
import com.emr.sudokusolver.iterators.RowIterator;


/**
 *
 * @author Evan Ross
 */
public class Initialization
{

    
    public static void startup()
    {
        //ArrayTest.doTests();
        //int[][] newInt = new int[1][2];
        //test(newInt);
        int a = 3;
        int b = 6;
        
        //Initialization.test();
        // recursive initialization tests
        //Initialization.startup();
        
        //BranchCompareTest.doTests();
        
        //Thread idleThread = new IdleThread();
        //Thread countingThread = new CountingThread(1000, 2,5);
        //Thread c2 = new CountingThread(2000, 3, 4);
                
        //Thread mainThread = new MainThread();
        //idleThread.start();
        //countingThread.start();
        //c2.start();
        //mainThread.start();
        
        //Interrupt.setInterruptHandler( idleThread, 2 );
        
        
        //Thread countingThread = new CountingThread();
        //countingThread.start();
        
        /*
        for(int i = 0; i < 0x100; i++)
        {
            Thread.yield();
        }
         */
        //Thread[] threadArray = new Thread[10];
        //Thread[][] t2 = new Thread[3][2];
        //int[] t = new int[5];
        //int[][] h = new int[7][7];
        //Thread idleThread = new Thread(5,9);
        //idleThread.start();
        
        //ArithmeticTest.doTests();
        //ArrayTest.doTests();
        
        Thread t = new IdleThread();
        t.start();
        
        //Thread t2 = new CountingThread(500, 5, 6);
        //t2.start();
        
        //startSudokuSolver();

        //SuperClass cl = new SubClass();
        //cl.doSomething();
        
        //InterfaceTest.doTests();

        //system.test.MiscTest.checkCast();
        //system.test.MiscTest.remainderInt();
        
        //java.util.test.JavaUtilTest.testVector();
        
        
        //int f = a.field1;
        
        //SuperClass c = new SubClass();
        //int j = c.getField();
        //SubClass d = new SubClass();
        //int k = d.getSuperField();
        
        //a.doSomethingMore(0x10);
        //int f = a.getField() + 0x20;
        //c.field1 = 9;
        //System.out.println(c.getField());
        
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

    public static void test(int[][] x)
    {
        x[1][2] = 2;
        int a = 9;
    }
    
    /////////////////////////////////////////////////////
    // Entry point for PC platform
    /////////////////////////////////////////////////////
    public static void main(String[] args)
    {
        Initialization.startup();
    }
    
}
