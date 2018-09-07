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
 * An ObjectArray represents an array dimension
 * that references objects.  This includes 2-D primitive array types
 * where the first dimension is always an array that references objects
 * of the type of the next dimension.
 *
 * @author Evan Ross
 */
public class ObjectArray extends ArrayInstance {

    public ObjectArray(int numElements) {
        super(numElements, 4);
    }


    @Override
    public void writeElement(OutputStream os) throws IOException {
        super.writeElement(os);

        DataOutputStream dout = new DataOutputStream(os);

        // write the offset of each child.
        for (Element child : this) {
            dout.writeInt(child.getAlignedOffset(4));
        }
    }


    public String toString() {
        return "object_array";
    }

}
