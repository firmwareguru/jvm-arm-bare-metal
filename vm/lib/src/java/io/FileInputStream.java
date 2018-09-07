/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package java.io;

/**
 *
 * @author Evan Ross
 */
public class FileInputStream extends InputStream {

    private int position;

    private int handle;

    /**
     * Wrap a FileInputStream around the given file.
     * This file must be in the Package's FileTable.
     *
     * @param name filename
     */
    public FileInputStream(String name) {

        position = 0;
        
        /*
         * Must call native constructor to get the file handle.
         * The handle is to an array of bytes (what else could it be?)
         * We can also get files from a file system that sits atop
         * a mass storage device - SD card etc.  In that case
         * we don't lookup the file handle in the Package's file table.
         * Hmmmm.  The lookup should proceed with all available file
         * resources.
         */

        /* Currently files stored in packages are simply a collection of
         * bytes - not technically an "array".  Therefore accesses can't
         * be made with array bytecodes which would be more efficient...
         */

        handle = construct(name);
        

    }

    /**
     * Search the file table for the given name.  If found,
     * return the handle (offset of file data in Package).
     *
     * @param name
     * @return
     */
    private static native int construct(String name);

    /**
     *
     * @return
     */
    @Override
    public int read() {
        return 0;
    }

    @Override
    public int read(byte[] b, int off, int len) {

        /* ensure that b can hold the number of bytes desired or
         * shorten the length as necessary 
         */
        if (off + len > b.length) {
            len = b.length - off;
        }

        int bytesRead = 0;
        if (len > 0) {
            bytesRead = read0(b, off, len, position, handle);
        }
        position += bytesRead;
        return bytesRead;
    }

    /**
     * Read bytes from the file wrapped by this FileInputStream.
     *
     * Read 'len' bytes from file denoted by 'handle' at position 'position'
     * into array 'b' at offset 'off',
     *
     * Return the number of bytes actually read.
     *
     * @param b
     * @param off
     * @param len
     * @param position
     * @param handle
     * @return
     */
    private static native int read0(byte[] b, int off, int len, int position, int handle);



}
