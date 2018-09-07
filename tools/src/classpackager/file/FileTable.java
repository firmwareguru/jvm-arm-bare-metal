/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package classpackager.file;

import classpackager.*;
import emr.elements.common.Element;
import emr.elements.common.GenericElement;
import java.io.File;

/**
 * A FileTable sits somewhere in a Package and contains a number
 * of FileTableEntry objects that point to the file data.
 *
 * The FileTable is populated with a series of 4-byte entries that
 * point to the actual FileTableEntry objects.  The table lookup
 * method is linear.  The table must be null terminated.
 * 
 * @author Evan Ross
 */
public class FileTable extends Table {

    public FileTable() {

    }


    /**
     * Add an entry to the file table...
     * @param f
     */
    public void addFileEntry(Element fileName, Element fd) {

        add(new FileTableEntry(fileName, fd));

    }

    @Override
    public String toString()
    {
        return "file_table";
    }
}
