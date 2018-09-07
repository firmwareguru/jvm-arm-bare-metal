/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package java.io;

/**
 *
 * @author Evan Ross
 */
public abstract class InputStream {

    public int available() {
        return 0;
    }

    public void close() {

    }

    public void mark(int readLimit) {

    }

    public boolean markSupported() {
        return false;
    }

    public abstract int read();

    public int read(byte[] b) {

        return read(b, 0, b.length);
    }

    public int read(byte[] b, int off, int len) {
        for (int i = 0; i < len; i++) {
            b[i + off] = (byte)read();
        }

        return len;
    }

    public void reset() {

    }

    /* No support for long yet
    public long skip(long n) {
        return 0;
    }
    */

}
