/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package emr.classserializer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Evan Ross
 */
public class FloatArray extends ArrayInstance {

    private final float[] array;

    public FloatArray(float[] array) {
        super(array.length, 4);
        this.array = array;
    }

    @Override
    public void writeElement(OutputStream os) throws IOException {
        super.writeElement(os);

        DataOutputStream dout = new DataOutputStream(os);

        // write the value of each element.
        for (float value : array) {
            dout.writeFloat(value);
        }

    }


    @Override
    public String toString() {
        return "float_array";
    }
}
