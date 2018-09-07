/*
 * SampleClass.java
 *
 * Created on April 24, 2006, 8:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.classanalyzer.samples;

import java.util.*;


/**
 *
 * @author Ross
 */
public class SampleClass implements SimpleInterface {
    
   final int constant = 551127;
   
   Thread thread1;
   
   Thread thread2;
   
   Thread thread3;
   
    /** Creates a new instance of SampleClass */
    public SampleClass() {
        
        float float1 = 88.23f;
        
        double double1 = 192.3345;
        int t = 21;
        try {
            long long1 = 99112;
        
            int int2 = 99999;
        
            int int1 = 87119;
            int y = constant;

        } catch (Exception e) {
            
        }
        
        aMethod(t);
   
        /*
        thread1 = new Thread(new Runnable() {
            public void run() {
                int a = 69;
                int b = 1072;
                while(true) {
                    System.out.println("thread1 running");
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        
                    }
                }
            }
        });
        
        thread2 = new Thread(new Runnable() {
            public void run() {
                while(true) {
                    System.out.println("thread2 running.");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        
                    }
                }
            }
        });
        
        thread1.start();
        thread2.start();
         */
    }
    
    public int aMethod(int i) 
    {
        AbstractList vec = new Vector();
        vec.add(6);
        int x = 67;
        
        switch(i)
        {
            case 1:
                x = x * i;
                break;
            case 2:
                x = x + i;
                break;
            default:
                x = i;
                break;
                
        }
        
        Object[] obj = new Object[5];
        SimpleInterface[] iface = new SimpleInterface[2];
        SimpleInterface[] iface2 = new SampleClass[7];
        int[] ints = new int[66];
        
        return x;
    }
    
    static public void main(String[] args) {
        new SampleClass();
    }
    
}
