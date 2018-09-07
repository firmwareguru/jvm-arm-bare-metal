/*
 * ClassFiles.java
 *
 * Created on June 27, 2006, 7:37 PM
 *
 */

package emr.elements.classfileparser;

import emr.elements.common.Element;
import java.util.Set;
import java.util.TreeSet;

/**
 * A holder for multiple ClassFile objects
 * 
 * @author Ross
 */
public class ClassFiles extends Element
{
    
    /** Creates a new instance of ClassFiles */
    public ClassFiles()
    {
    }
    
    public String toString()
    {
        return "class_files";
    }
    
    /** Returns the ClassFile matching the given class name
     */
    public ClassFile findClassFile(String classFileName_)
    {
        // A ClassFiles contains a tree of classes.
        // Search the top level of the tree
        for( int i = 0; i < size(); i++ )
        {
            ClassFile classFile = (ClassFile) get(i);
            if( classFile.getName().equals( classFileName_ ))
                return classFile;
        }
        
        return null;
    }
    
    /** Returns the index of the ClassFile matching the given class name
     */
    public int findClassFileIndex(String classFileName_)
    {
        // A ClassFiles contains a tree of classes.
        // Search the top level of the tree
        for( int i = 0; i < size(); i++ )
        {
            ClassFile classFile = (ClassFile) get(i);
            if( classFile.getName().equals( classFileName_ ))
                return i;
        }
        
        return -1;
    }

    public Set<String> getClassNameSet() {
        Set<String> set = new TreeSet<String>();
        for (Element e : this) {
            set.add(((ClassFile)e).getName());
        }
        return set;
    }
    
}
