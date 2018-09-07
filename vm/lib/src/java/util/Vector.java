/*
 * Vector.java
 *
 * Created on January 15, 2008, 10:01 PM
 *
 *
 */

package java.util;

/**
 *
 * @author Evan Ross
 */
public class Vector<E> {
    
    protected Object[] elementData;
    
    protected int elementCount;
    
    protected int capacityIncrement;
    
    /** Creates a new instance of Vector */
    public Vector() {
        this(10);
    }
    
    public Vector(int initialCapacity) {
	this(initialCapacity, 0);
    }

    public Vector(int initialCapacity_, int capacityIncrement_) {
	super();
        //if (initialCapacity_ < 0) // some kind of error but can't report it yet
            // throw new IllegalArgumentException("Illegal Capacity: "+ initialCapacity);
	elementData = new Object[initialCapacity_];
	capacityIncrement = capacityIncrement_;
        elementCount = 0;
    }
    
    /**
     * Returns the number of components in this vector.
     *
     * @return  the number of components in this vector.
     */
    public synchronized int size() {
	return elementCount;
    }
    
   /**
     * Tests if this vector has no components.
     *
     * @return  <code>true</code> if and only if this vector has 
     *          no components, that is, its size is zero;
     *          <code>false</code> otherwise.
     */
    public synchronized boolean isEmpty() {
	return elementCount == 0;
    }
    
    /**
     * Returns the element at the specified position in this Vector.
     *
     * @param index index of element to return.
     * @return object at the specified index
     * @exception ArrayIndexOutOfBoundsException index is out of range (index
     * 		  &lt; 0 || index &gt;= size()).
     * @since 1.2
     */
    public synchronized E get(int index) {
	//if (index >= elementCount)
	    //throw new ArrayIndexOutOfBoundsException(index);

	return (E)elementData[index];
    }    
    
    /**
     * Returns the current capacity of this vector.
     *
     * @return  the current capacity (the length of its internal 
     *          data array, kept in the field <tt>elementData</tt> 
     *          of this vector).
     */
    public int capacity() {
	return elementData.length;
    }
    

   /**
     * Appends the specified element to the end of this Vector.
     *
     * @param o element to be appended to this Vector.
     * @return true (as per the general contract of Collection.add).
     * @since 1.2
     */
    public synchronized boolean add(E object_) {
	//ensureCapacityHelper(elementCount + 1);
	elementData[elementCount++] = object_;
        return true;
    }    
    
    
}
