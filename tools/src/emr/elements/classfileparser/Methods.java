/*
 * Methods.java
 *
 * Created on May 30, 2006, 7:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.elements.classfileparser;

import emr.elements.common.Element;
import emr.elements.classfileparser.attributes.CodeAttribute;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ross
 */
public class Methods extends ComplexElement
{
    /* reference to the class' ConstantPool */
    ConstantPool constantPool;
    
    /** Creates a new instance of Methods */
    public Methods(ConstantPool constantPool_)
    {
        constantPool = constantPool_;
        
        add(new MethodsCount());
    }
    
    public void readChildren(InputStream is) throws IOException
    {
        get(0).readData(is);
        
        int count = get(0).getValue();
        
        if(debug) System.out.println("Methods: " + count);
        for(int i = 0; i < count; i++)
        {
            Element e = new MethodInfo(constantPool);
            e.readData(is);
            add(e);
        }
        
    }
    
    /* This method returns the CodeAttribute object for the given method name */
    public CodeAttribute getCodeAttribute(String name)
    {
        // get the MethodInfo associated with this method name
        Element methodInfo = getElement("method_info: " + name);    
        
        if (methodInfo == null)
            return null;
        
        // get the code attribute (there is only one for each methodinfo)
        CodeAttribute codeAttribute = (CodeAttribute)methodInfo.getElement("code_attribute");
        
        return codeAttribute;
    }

    /**
     * Return a List of all the MethodInfo structures.
     * @return
     */
    public List<MethodInfo> getMethods() {
        List<MethodInfo> list = new ArrayList<MethodInfo>(size()-1);
        for (int i = 1; i < size(); i++) {
            list.add((MethodInfo)get(i));
        }
        return list;
    }
  
    public String toString()
    {
        return "methods";
    }    
}
