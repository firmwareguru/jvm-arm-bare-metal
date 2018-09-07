/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package emr.jvm.adapterlib;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A FlashObjectInputStream emmulates the standard ObjectInputStream
 * by providing the ability to deserialize objects and primitive
 * data types directly from flash.  A key feature of this class is
 * that the returned object reference points to the object's location
 * in flash, avoiding the need to allocate runtime space in the heap.
 * This is an excellent way to save memory when constant data in the
 * form of arrays or other primitive types can be pre-loaded with the
 * Package.
 *
 * Some caveats: The object references point to regions of flash, therefore
 * the objects must be handled as read-only.  Furthermore, the monitors
 * that may be associated with these objects must not be used or the result
 * is undefined (i.e., no synchronized methods, synchronized blocks nor
 * invocations of wait(), notify() or notifyAll()).
 *
 * The PC Adapter version delegates to java.io.ObjectInputStream.
 *
 * This should probably not even be an instance but rather a static
 * method class where the methods accept a filename... This is OK
 * only if there is one class per file which may not be the case.
 *
 * @author Evan Ross
 */
public class FlashObjectInputStream {

    //int fileAddress;
    //int filePos;

    /**
     * Open an input stream on the resource identified by the filename using the
     * classloader.  Suitable for Jar embedded files.
     * @param filename
     */
    //public FlashObjectInputStream(String filename) {
        // delegate to a native method to find the file in the Package and
        // setup the fields.
    //    construct(filename);
    //}

    //private native void construct(String filename);



    //public native Object readObject();
        // return the reference to the object at the current filePos, then
        // increment the filePos to the next object (if any) in this file.


    /**
     * Return a reference to the object identified by the given filename.
     * PC version uses the classloader's getresourceasstream method to access
     * jar-embedded files.
     *
     * @param filename
     * @return
     */
    public static native Object readObject(String filename);

}

