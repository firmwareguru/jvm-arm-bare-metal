/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package emr.classserializer;

import emr.elements.common.Element;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * An ArrayInstance element is the superelement of all
 * specific types of arrays.  It knows how to create the array
 * object headers, etc.  Children must call writeElement() before executing
 * their own implementations.
 *
 * @author Evan Ross
 */
public class ArrayInstance extends Element {

    /* Specify the size in bytes of each element of this ArrayInstance.
     * Used to calculate the Element's size in bytes */
    private final int elementSize;
    private final int numElements;

    public ArrayInstance(int numElements, int elementSize) {
        this.numElements = numElements;
        this.elementSize = elementSize;

        // Add 12 bytes for the 3 array object header fields.
        setSize(numElements * elementSize + 12);
    }


    @Override
    public void writeElement(OutputStream os) throws IOException {

        // if there are any children, this is another dimension so throw
        // an exception if the counts don't line up
        if (size() > 0 && numElements != size()) {
            throw new IOException("ArrayInstance element count mismatch: " + size() + " expected " + numElements);
        }
        
        //    0 monitor  -> 0
        //    4 class ref  -> have no where to link this - lets hope it isn't needed
        //    8 array length (in terms of count of elements)
        //     .....
        //     .....
        // Wrap in a DataOutputStream
        DataOutputStream dout = new DataOutputStream(os);
        dout.writeInt(0); // monitor
        dout.writeInt(0); // classref
        dout.writeInt(numElements); // array length
    }


}
