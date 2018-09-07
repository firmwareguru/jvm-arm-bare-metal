/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package emr.classserializer;

import emr.jvm.adapterlib.FlashObjectInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Evan Ross
 */
public class Serializer {

    public static final String OUTPUT_ROOT_DIR = "C:/Projects/Java_stuff/JLayerME4VM/src/vm_serialized/";

    public Serializer() {
    }

    public static void main(String[] args) {


        // Can possibly only serialize native types and
        // arrays of native types.  How would I serialize
        // a class object in a general way?  Would have to write
        // a serializer for each type of object.  May be a way, using
        // reflecting, to access each field and build an object
        // for the VM.

        // Could directly serialize classes that are running in the VM.


        // Lets do this specifically for the JLayer's huffman tables and such.
        // Procedure:
        //   Read each array in, then write each one out to a separate file
        //   in a format that represents an array instance in the VM.
        // Will also need to re-serialize them in Sun's format to separate files so that the
        // normal version can still use the same FlashObjectInputStream.getObject(name)
        // idiom.
        //
        // Result: one set of files for the VM, one set for the PC.  The set for the
        // PC needs to be accessible by the PCAdapterLibrary - that is, put in
        // the location that the PCAdapterLibrary expects to find files.

        // d16.ser : SynthesisFilter
        //     float[]
        try {
            File d16fin = new File("C:/Projects/Java_stuff/JLayerME4VM/d16.ser");
            ObjectInputStream d16ois = new ObjectInputStream(new FileInputStream(d16fin));
            float[] d16data = (float[])d16ois.readObject();

            //File d16fout_pc = new File(FlashObjectInputStream.PC_ROOT_PATH + "d16.ser");
            File d16fout_vm = new File(OUTPUT_ROOT_DIR + "d16.ser");

            ObjectOutputStream dout = new ObjectOutputStream(new FileOutputStream(d16fout_vm));

            // Build an array of 1-D floats
            //
            //    0 monitor  -> 0
            //    4 class ref  -> have no where to link this - lets hope it isn't needed
            //    8 array length (in terms of count of elements)
            //     .....
            //     .....

            FloatArray array = new FloatArray(d16data);
            array.setMetaData(Integer.toString(d16data.length) + " elements");

            System.out.println("Serializing... " + d16fout_vm.getName() + " " + d16data.length + " " + array.getSize());

            dout.writeObject(array);
            
            dout.close();


        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Serializer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).severe(e.getMessage());
        }
        // huffman.ser : HuffmanTables
        //     20 int[][] instances
        try {
            File huffin = new File("C:/Projects/Java_stuff/JLayerME4VM/huffman.ser");
            ObjectInputStream huffins = new ObjectInputStream(new FileInputStream(huffin));

            for (int obj = 0; obj < 20; obj++) {

                int[][] huffdata = (int[][])huffins.readObject();

                String fname = "huffman" + obj + ".ser";


                // Output files: 1 for PC, 1 for VM
                File huffout_pcfile = new File(FlashObjectInputStream.PC_ROOT_PATH + fname);
                File huffout_vmfile = new File(OUTPUT_ROOT_DIR + fname);

                ObjectOutputStream huffout_vm = new ObjectOutputStream(new FileOutputStream(huffout_vmfile));
                ObjectOutputStream huffout_pc = new ObjectOutputStream(new FileOutputStream(huffout_pcfile));

                huffout_pc.writeObject(huffdata);
                
                // Build an array of 2-D ints.  First dimension is an array of int[] objects.
                // The elements need to point to the correct instance.  This can get hairy.
                // Ouch.  This requires that the 2-D array be represented as an Element graph
                // that is written out by the Packager.  We need to submit this to the Packager
                // instead of a flat file of bytes.  So... we could serialize an Element graph
                // and somehow indicate to the packager that it should be reconstituted.
                // Special file names?  Are serialzed objects always submitted as Element graphs?
                // Is the serializer performed as part of the Packaging operation?
                //

                ObjectArray oa = new ObjectArray(huffdata.length); // first dimension
                oa.setMetaData(Integer.toString(huffdata.length) + " elements");

                for (int i = 0; i < huffdata.length; i++) {
                    // add second dimension elements of type int[]
                    IntArray ia = new IntArray(huffdata[i]);
                    ia.setMetaData(Integer.toString(huffdata[i].length) + " elements");
                    oa.add(ia);
                }

                System.out.println("Serializing... " + fname + " " + huffdata.length + " x " + huffdata[0].length + " " + oa.getSize());

                huffout_vm.writeObject(oa);

                huffout_vm.close();
                huffout_pc.close();

            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Serializer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).severe(e.getMessage());
        }
    }

}
