/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package java.io;

/**
 *
 * @author Evan Ross
 */
public class BufferedInputStream extends InputStream {

    protected byte[] buf;

    protected int count;

    protected int markLimit;

    protected int markPos;

    protected int pos;

    protected InputStream in;

    BufferedInputStream(InputStream in) {
        this.in = in;
    }

    BufferedInputStream(InputStream in, int buffSize) {
        this.in = in;
    }    

    @Override
    public int read() {
        return in.read();
    }

    @Override
    public int read(byte[] b, int off, int len) {
        return in.read(b, off, len);
    }

}
