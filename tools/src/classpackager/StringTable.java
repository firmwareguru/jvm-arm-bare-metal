/*
 * StringTable.java
 *
 * Created on April 2, 2008, 4:54 PM
 *
 * Updated Oct 12, 2009
 *
 */

package classpackager;

import emr.elements.common.Element;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * A StringTable holds the raw byte data that representds UTF8-Info CPTable entries
 * pointed to by Constant_String_Info entries.  Other UTF8-Info entries NOT pointed
 * to by Constant_String_Info entries are not here.
 *
 * Other strings are also contained here, such as those in the file table.
 * Therefore this is a pool of Package-global strings.
 *
 * Note that the strings are formatted as a VM array to facilitate simpler
 * use within the VM.  This means that for each string, there are 12 bytes
 * overhead:
 *     monitor, class reference, length.
 *
 * The strings exist in this pool exactly once, and there may be many
 * references to a particular string.
 *
 * The StringTable is backed by a Map of StringTableEntry objects.
 * When a string needs to be added to the table, a request is made
 * for an entry.  If it exists, that object is returned, otherwise
 * it is created, added and returned.
 *
 * The StringTable needs a reference to an existing java/lang/String 
 * InternalClass so that the String array's class reference can be set
 * to point to it.
 * 
 *
 *
 * @author Evan Ross
 */
public class StringTable extends Element {

    /* Backed by a Map of Strings to StringTableEntrys */
    private Map <String, StringTableEntry> stringMap;

    /* The reference to the java/lang/String internalClass */
    private InternalClass stringClassRef;

    /** Creates a new instance of StringTable */
    public StringTable(InternalClass stringRef) {
        stringMap = new HashMap<String, StringTableEntry>();
        stringClassRef = stringRef;
    }

    public StringTable() {
        this(null);
    }

    public void setStringClassRef(InternalClass ref) {

        System.err.println("Setting string ref " + ref);
        stringClassRef = ref;
    }

    /**
     * Return the StringTableEntry that is or should be associated
     * with the given string.  Only one entry exists for each string.
     * 
     * @param s
     * @return
     */
    public StringTableEntry getStringTableEntry(String s)
    {
        StringTableEntry entry = stringMap.get(s);
        if (entry == null) {
            entry = addStringTableEntry(s);
            stringMap.put(s, entry);
        }
        return entry;
    }


    /**
     * Alternate technique.  Requires string class reference at invocation time.
     * 
     * @param s
     * @param stringClassRef
     * @return
     */
    public StringTableEntry getStringTableEntry(String s, InternalClass stringClassRef)
    {
        this.stringClassRef = stringClassRef;
        return getStringTableEntry(s);
    }

    /**
     * Create a new StringTableEntry for the given string, add it to the
     * StringTable and ensure it is 4-byte aligned.  Return the new entry.
     *
     * @param s
     * @return
     */
    private StringTableEntry addStringTableEntry(String s)
    {
        System.err.println("Adding string table entry for " + s + " " + stringClassRef);

        StringTableEntry ste = new StringTableEntry(s, this);
        ste.setMetaData(s);

        // ensure we are 4-byte aligned
        add(new ByteAligner(4));
        add(ste);

        return ste;
    }


    @Override
    public String toString()
    {
        return "string_table";
    }

    InternalClass getStringClassRef() {
        return stringClassRef;
    }
}
