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
 *
 * @author Evan Ross
 */
public class IntArray extends ArrayInstance {

    private final int[] array;

    public IntArray(int[] array) {
        super(array.length, 4);
        this.array = array;
    }

    @Override
    public void writeElement(OutputStream os) throws IOException {
        super.writeElement(os);

        DataOutputStream dout = new DataOutputStream(os);

        // write the value of each element.
        for (int value : array) {
            dout.writeInt(value);
        }
    }


    @Override
    public String toString() {
        return "int_array";
    }



}

