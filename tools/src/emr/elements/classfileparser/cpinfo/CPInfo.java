/*
 * CPInfo.java
 *
 * Created on May 30, 2006, 7:43 PM
 *
 * CPInfo's have a Tag and a particular Info instance as children.  The Info instance is determined by
 * the tag.
 *
 * The name of the CPInfo as shown in the ClassFileViewer can be matched to the particular type of Info it retains
 * by removing the constant String reference in the toString() method and replacing it with an instance String variable
 * that points to the toString() method of it's Info child.
 *
 */

package emr.elements.classfileparser.cpinfo;

import emr.elements.common.Element;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 *
 * @author Ross
 */
public class CPInfo extends Element
{
    /**
     *  The index of this CPInfo in the Constant Pool.  Aids in viewing in the ClassFileViewer.
     */
    private int index;
    
    /** Creates a new instance of CPInfo */
    public CPInfo(int index_)
    {
        index = index_;
        
        add(new Tag());
    }
    

    /* determine the specific CPInfo by the value of Tag */
    public void readChildren(InputStream is) throws IOException
    {
        
        get(0).readData(is);
        
        int tag = get(0).getValue();
        if(debug) System.out.println("CPInfo processing tag " + tag);
        
        switch (tag) 
        {
                case 1:
                    add(new ConstantUTF8Info());
                    break;
                case 3:
                    add(new ConstantIntegerInfo());
                    break;
                case 4:
                    add(new ConstantFloatInfo());
                    break;
                case 5:
                    add(new ConstantLongInfo());
                    break;
                case 6:
                    add(new ConstantDoubleInfo());
                    break;
                case 7:
                    add(new ConstantClassInfo());
                    break;
                case 8:
                    add(new ConstantStringInfo());
                    break;
                case 9:
                    add(new ConstantFieldRefInfo());
                    break;
                case 10:
                    add(new ConstantMethodRefInfo());
                    break;
                case 11:
                    add(new ConstantInterfaceMethodRefInfo());
                    break;
                case 12:
                    add(new ConstantNameAndTypeInfo());
                    break;
                default:
                    System.out.println("error resolving tag ");
                    break;
            }  
       
        // read in the newly added Info structure
        if(size() == 2)
            get(1).readData(is);
    }
    
    public int getTag()
    {
        return getElement("tag").getValue();
    }
    
    public String toString()
    {
        //return "cp_info";
        //This is a bit ugly but whatcha gonna do...
        if(size() == 2)
            return "cp_info (" + index + "): " + get(1).toString();
                    
        else
            return "cp_info";
    }
}
