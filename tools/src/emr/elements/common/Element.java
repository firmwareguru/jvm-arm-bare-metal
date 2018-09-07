/*
 * Element.java
 *
 * Created on May 29, 2006, 8:40 PM
 *
 * An Element is the base class of any element in a Class file.  An Element can represent simple types such as
 * u2 or u4 without any additional code.  More complex structures such as the ConstantPool or Methods extend ComplexElement
 * which in turn extends Element.  The ComplexElements typically override readElement to read in the first subelement 
 * which is the count.  The count is then used to determine how many additional subelements are added to itself.
 *
 * An Element at the very least is able to read from and write to a serial stream.  It has a 'size'.  Elements operate
 * on the stream in bytes.
 *
 * A subclass of Element MAY overide read/writeElement.  An Element extends Vector to hold sub elements.
 * 
 * readData() causes readElement() and readChildren() to be called in that order
 *
 * writeData() causes writeElement() and writeChildren() to be called in that order
 *
 * Each Element is responsible for adding the appropriate sub elements
 *
 */

package emr.elements.common;

import java.util.Vector;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 *
 * @author Evan Ross
 */
public class Element extends Vector<Element> 
{
    public static boolean debug = false;
    public static boolean status = false;
    
    /* Some basic constants that subclasses can use */
    protected final int u2 = 2;
    protected final int u3 = 3;
    protected final int u4 = 4;
    protected final int u1 = 1;
    
    /* The value of a Class file Element */
    protected int value = 0;
    
    /* The size of a Class file Element in bytes */
    protected int size = 0;
    
    /* Determines if this element is written out */
    protected boolean isWritable = true;
    
    /* The offset in a particular OutputStream that this Element starts at */
    protected int offset = 0;
    
    /* Any Element can have metadata associated with it.  This information is
     * not used in any Element processes but is viewed in the ClassFileViewer
     */
    private String metaData = null;
    
    // Endian support for WriteElement.
    public enum EndianEnum
    {
        BIG,
        LITTLE               
    }
    
    
    public static EndianEnum endian = EndianEnum.BIG;
    
    
    /** Creates a new instance of Element */
    public Element() 
    {
        
    }
    
    /** Causes Element to read its data from the InputStream and then causes it's
     *  children to read their data, in that order
     */
    public void readData(InputStream is) throws IOException
    {
        readElement(is);
        readChildren(is);
    }
    
    /** Can be overriden by subclass.
     *
     *  A basic Element need only set the size of bytes contained in the Element.
     *  More complex Elements may override this method to dynamically generate rawBytes
     *
     *  Read data from the supplied InputStream
     */
    public void readElement(InputStream is) throws IOException
    {
        if(size > 0 && size <= 4)
        {
            byte[] bytes = new byte[size];
            is.read(bytes);
            value = bytesToInt(bytes);
            if (status) System.out.println("Read " + size + " bytes = " + value + " " + this.getClass().getName());
        }
    }
    
    /** Called by readElement 
     * 
     *  Calls readData() of all child Elements in the order they were added 
     */
    public void readChildren(InputStream is) throws IOException
    {
        for(int i = 0; i < size(); i++)
        {
            get(i).readData(is);
        }
    }
    
    /* write out this Element and all sub-Elements to the supplied OutputStream.
     * If an Element is marked as not writable, this Element will not be written
     * out but It's children will be.  If the OutputStream is an instance of
     * JVMOutputStream then the current offset is read and set in
     * this Element before any writing is done.
     */
    public void writeData(OutputStream os) throws IOException
    {
        if(os instanceof JVMOutputStream)
        {
            offset = ((JVMOutputStream)os).getOffset();
        }
        
        if(isWritable)  // write out if so set.
            writeElement(os);
        
        writeChildren(os);
    }
    
    public void writeElement(OutputStream os) throws IOException
    {
        
        if(size > 0 && size <= 4)
        {
            os.write(intToBytes(value, endian)); // Write using current endian setting.
            if(status) System.out.println("Wrote " + size + " bytes.");
        }
        
    }
    
    public void writeChildren(OutputStream os) throws IOException
    {
        for(int i = 0; i < size(); i++)
        {
            get(i).writeData(os);
        }
        
    }
    
    /** Helper function turns array of bytes into an int */
    public int bytesToInt(byte[] bytes)
    {
        int theInt = 0;
        int shift;
        
        for(int i = 0; i < bytes.length; i++)
        {
            shift = (bytes.length - 1 - i) * 8;
            theInt |= ((int)bytes[i] << shift) & (255 << shift);
            
        }

        return theInt;
    }
    
    /** Helper function turns value into an array of bytes based on size */
    public byte[] intToBytes(int val, EndianEnum endian_)
    {
        /*
        byte[] bytes = new byte[size];
        
        // for a 4 byte int...
        int shift;
        for(int i = 0; i < size; i++)
        {
            shift = (size - 1 - i) * 8;
            bytes[i] = (byte) (val >> shift);
        }
        
        //System.out.println(" " + bytesToInt(bytes));
        return bytes;
        */
        
        byte[] bytes = new byte[size];
        
        int shift;
        
        switch (endian_)
        {
            // top byte first = big-endian.
            case BIG:
            {
                for(int i = 0; i < size; i++)
                {
                    shift = (size - 1 - i) * 8;
                    bytes[i] = (byte) (val >> shift);
                }
                break;
            }
            case LITTLE:
            {
                // little-endian
                for(int i = size - 1; i >= 0; i--)
                {
                    shift = i * 8;
                    bytes[i] = (byte)(val >> shift);
                }
                break;
            }
        }
        
        //System.err.println("intToBytes: " + bytesToInt(bytes));
        return bytes;        
    }
    
    /** Returns a reference to the Element matching the String elementName.
     *  Delegates to sub Elements if no match found.
     */
    public Element getElement(String elementName)
    {
        // search through the first level of sub elements to find a match
        for(int i = 0; i < size(); i++)
        {
            Element e = get(i);
            if (e.toString().equalsIgnoreCase(elementName))
            {
                return e;
            }
        }
        
        // delegate search to each sub element
        for(int i = 0; i < size(); i++)
        {
            Element e = get(i);
            Element subElement = e.getElement(elementName);
            if (subElement != null)
                return subElement;
        }

        // if no match found in subelement tree, return null
        return null;
    }

    private int cachedTreeSize = -1;
    /** Returns the size of the (sub)tree starting from this Element 
     * by recursively calling getTreeSize().  OnlyWritable_ specifies
     * weather to include only those elements with their writable flag set.
     */
    public int getTreeSize(boolean onlyWritable_)
    {
        if (cachedTreeSize == -1) {
            int tsize = 0;

            // get this element's size depending on the onlyWritable condition
            if( !onlyWritable_ || onlyWritable_ && isWritable() )
                tsize += getSize();

            // go through all sub-elements
            for( int i = 0; i < size(); i++ )
            {
                tsize += get(i).getTreeSize( onlyWritable_ );
            }
            
            cachedTreeSize = tsize;
        }
        
        return cachedTreeSize;
    }
    
    /** Basic Element accessor functions. 
     *  Some Elements may not have a size or value, in which case they return 0.
     */
    public void setSize(int size_)
    {
        size = size_;
    }
    
    public int getSize()
    {
        return size;
    }
    
    public void setValue(int value_)
    {
        value = value_;
    }
    
    public int getValue()
    {
        return value;
    }
    
    @Override
    public String toString()
    {
        return "u" + getSize();
    }
    
    public boolean isWritable()
    {
        return isWritable;
    }
    
    public void setWritable(boolean writeable_)
    {
        isWritable = writeable_;
    }
    
    /* Return this Element's offset */
    public int getOffset() 
    {
        return offset;
    }
    
    /** Return this Element's offset in terms of the given alignement 
     * Fails if offset is not aligned. */
    public int getAlignedOffset(int alignment_)
    {
        // furthermore, we throw an exception to indicate that a requested offset was NOT aligned properly
        if( (offset % alignment_) != 0)
        {
            System.err.println("<"+toString() + "> Offset alignment exception at byte offset: " + offset);
            System.exit(1);
        }
        
        
        // Change this to return offsets in different units, i.e. bytes, words, etc.
        //return offset / alignment_;
        return offset;
    }
    
    /** Get this Element's meta data (if any) */
    public String getMetaData()
    {
        return metaData;
    }
    
    /** Sets this Element's meta data */
    public void setMetaData(String metaData_)
    {
        metaData = metaData_;
    }



//    /**
//     * An attempt to identify Elements uniquely.
//     * This doesn't cover all cases and some Elements
//     * will generate the same hash.
//     *
//     * @return
//     */
//    @Override
//    public int hashCode() {
//
//
//        int hash = 3;
//        hash = 59 * hash + this.value;
//        hash = 59 * hash + this.size;
//        hash = 59 * hash + this.elementCount;
//        hash = 59 * hash + this.toString().hashCode();
//        hash = 59 * hash + this.metaData.hashCode();
//
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (o instanceof Element) {
//            return ((Element)o).hashCode() == this.hashCode();
//        } else {
//            return false;
//        }
//    }
    
}
