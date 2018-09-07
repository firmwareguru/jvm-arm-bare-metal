/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package classpackager.file;

import classpackager.ByteArray;
import classpackager.StringTableEntry;
import classpackager.TableEntry;
import emr.elements.common.Element;
import emr.elements.common.GenericElement;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A FileTableEntry contains a reference to a file name string in the
 * Package's StringTable and a 4-byte-aligned offset to the actual file data (raw bytes).
 *
 *     |-------------| 0
 *     |  StringRef  |
 *     |-------------| 4
 *     |  FileRef    |
 *     |-------------|
 * 
 * and a
 * @author Evan Ross
 */
public class FileTableEntry extends TableEntry {

    /* Link to the associated Element (could be a FileData) */
    Element fd;

    /* Link to the associated fileName (String) */
    Element fileName;

    public FileTableEntry(Element fileName, Element fd) {

        super("file_table_entry");
        this.fd = fd;
        this.fileName = fileName;
        this.setMetaData(fileName.getMetaData());

        add(new GenericElement(4, "string_ref"));
        add(new GenericElement(4, "filedata_offset"));
    }

    @Override
    public void writeElement(OutputStream os) throws IOException
    {
        if (fd == null || fileName == null)
            throw new IOException("FileTableEntry error.");

        getElement("string_ref").setValue(fileName.getOffset());
        getElement("filedata_offset").setValue(fd.getAlignedOffset(4));
    }
}
