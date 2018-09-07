/*
 * ClassBuilder.java
 *
 * Created on December 10, 2006, 4:03 PM
 *
 * The ClassBuilder produces the tree of InternalClasses formated for the JVM from 
 * the tree of ClassFiles.
 *
 * To assemble the Package:
 *   1. Build a Class from each ClassFile.  This requires these steps:
 *       a. Initialize the InternalClass object with a
 *             i.   Header
 *             ii.  ConstantPool
 *             iii. MethodTable
 *             iv.  FieldTable
 *             v.   CodeTable
 *             vi.  ExceptionHandlerTable
 *             vii. StringTable
 *          
 *       b. Point the Header to the ConstantPool, MethodTable and FieldTable.
 *
 * The ClassBuilder is also responsible for resolving native methods at load time.
 * The informtation defining native methods are found in a class implementing
 * the NativeMethodLibrary interface.
 *
 * The ClassBuilder is also responsible for finding the 'Main' Thread class that
 * is to be created on VM startup and making two things available: the InternalClass
 * pointer of the Main Thread class; and hashed name and descriptor of the Run method.
  */

package classpackager;

import emr.elements.common.Element;
import emr.elements.classfileparser.*;
import emr.elements.classfileparser.cpinfo.*;
import emr.elements.classfileparser.attributes.ExceptionTable;
import emr.elements.classfileparser.attributes.ExceptionTableEntry;
import emr.elements.classfileparser.attributes.CodeAttribute;
import emr.elements.classfileparser.attributes.ConstantValueAttribute;
import emr.elements.classfileparser.common.AccessFlags;

import java.io.IOException;
import java.util.Vector;


/**
 *
 * @author Ross
 */
public class ClassBuilder 
{
    
    /* Some global stats */
    private int totalStringBytes = 0;
    private int totalHashBytes = 0;
   
    /* Reference to ClassFiles tree head */
    ClassFiles classFiles;
    
    /* Reference to InternalClasses tree head */
    InternalClasses internalClasses;
    
    /* index of the Initialization class in InternalClasses */
    int initializationClassIndex = 0;
    
    /* index of the startup() method in the constant pool */
    int startupIndex = 0;
    
    
    /** Creates a new instance of ClassBuilder */
    public ClassBuilder() 
    {
    }
    
    /** Returns the found initialization class */
    public InternalClass getInitializationClass()
    {
        return (InternalClass)internalClasses.get ( initializationClassIndex );
    }
    
    public int getStartupIndex()
    {
        return startupIndex;
    }
    
    /**
     * For each ClassFile:
     *     1.  Create an InternalClass
     *     2.  Get the ConstantPool and
     *        a.  based on tag:
     *            i. '1' hash the string to a 4 byte code and track it.
     *               store it in the ConstantPoolTable
     *            ii.  otherwise, strip the tag and store the bytes directly
     *     3.  Get the Interfaces.  Copy each element to the InternalClass' InterfaceTable
     *     4.  Get the Methods.  Create the MethodTableEntry and add.  Create the CodeTableEntry and add. 
     *         Create the ExceptionTableEntry and add.
     *     5.  Get the Fields.  Create the FieldTableEntry and add.
     */
    public Element buildClasses(ClassFiles classFiles_)
    {
        // Set global reference for use by computeObjectSize()
        classFiles = classFiles_;

        // Create the top-level "InternalClasses" Element
        internalClasses = new InternalClasses();

        //
        // Pre-create empty internalClass objects, one for each classFile and link
        // the next_class element from one InternalClass to the next.  This is done first
        // because computeObjectSize requires access to entire array of InternalClasses
        // when computing sizes of each ClassFile.
        //
        for( int i = 0; i < classFiles_.size(); i++)
        {
            // 
            // Get the ClassFile's Interface attribute and set flag in InternalClass contructor.
            //
            ClassFile classFile = (ClassFile)classFiles.get(i);
            AccessFlags flags = (AccessFlags)classFile.getAccessFlags();
            
            InternalClass newClass = new InternalClass(flags.isInterface(), classFile.getName());
            if( i > 0 )
            {
                InternalClass previous = (InternalClass) internalClasses.get( i - 1 );
                previous.getHeader().setNextClass( newClass );
            }
            internalClasses.add( newClass );
            
        }
        
        
        // process each ClassFile
        for(int i = 0; i < classFiles_.size(); i++)
        {
            // get the ClassFile
            ClassFile classFile = (ClassFile)classFiles_.get(i);
            
            // get the InternalClass that corresponds to this ClassFile
            InternalClass internalClass = (InternalClass) internalClasses.get(i);
            internalClass.setMetaData( classFile.getName() );
            
            // Identify the Initialization Class
            if( classFile.getName().equals("system/MainThread"))
                initializationClassIndex = i ;
            
            log("<BuildClasses> building InternalClass for " + classFile.getName());
            
            ///////////////////////////////////////
            // Form the Header
            ///////////////////////////////////////
            Header header = internalClass.getHeader();
            formHeader(classFile, header);
            
            ///////////////////////////////////////
            // Process the ConstantPool
            ///////////////////////////////////////
            ConstantPool cp = classFile.getConstantPool();
            ConstantPoolTable icp = internalClass.getConstantPoolTable();
            processConstantPool(cp, icp, i);
            resolveStringInfo(internalClass, cp, icp);
            
            ///////////////////////////////////////
            // Process Interfaces
            ///////////////////////////////////////
            Interfaces interfaces = classFile.getInterfaces();
            InterfaceTable interfaceTable = internalClass.getInterfaceTable();
            processInterfaces(interfaces, interfaceTable);
            
            ///////////////////////////////////////
            // Process Methods
            ///////////////////////////////////////
            MethodTable methodTable = internalClass.getMethodTable();
            CodeTable codeTable = internalClass.getCodeTable();
            ExceptionHandlerTable exceptionHandlerTable = internalClass.getExceptionHandlerTable();
            processMethods(classFile, methodTable, codeTable, exceptionHandlerTable);
            
            ///////////////////////////////////////
            // Process Fields
            ///////////////////////////////////////
            ////////////////////////////////////////////////////////////////
            // Now determine and set all field sizes including super classes
            ///////////////////////////////////////////////////////////////
            computeObjectSize( classFile );  // calls processFields()
            
            //////////////////////////////////////////////////
            // Update Header with after-the-fact information
            //////////////////////////////////////////////////
            updateHeader(header, icp);

            
            log("--------- Done --------");
        }
        
        if( initializationClassIndex == 0 )
        {
            log("Initialization Class not found!");
            System.exit(1);
        }
        
        log("<BuildClasses> done!");
        log("<BuildClasses>    total string bytes: " + totalStringBytes);
        log("<BuildClasses>    total hash bytes:   " + totalHashBytes);
        log("<BuildClasses>    bytes saved:        " + (totalStringBytes - totalHashBytes));
        
        return internalClasses;
    }

    // Transfer values of primitive elements in ClassFile to Header, compute
    // object size for the object.
    private void formHeader(ClassFile classFile_, Header header_)
    {
        // set this class ! Done later in updateHeader()
        header_.getThisClass().setValue(classFile_.getThisClass().getValue());
        
        // set super class ! Done later in updateHeader()
        header_.getSuperClass().setValue(classFile_.getSuperClass().getValue());
        
        // set access flags
        header_.getAccessFlags().setValue(classFile_.getAccessFlags().getValue());
        
        // compute object size
        //header_.getObjectSize().setValue( computeObjectSize( classFile_, false ) );
    }
    
    /** Update the header with information available after processing other structures.
     * We replace this_class and super_class with the actual hashed names.
     */
    private void updateHeader(Header header_, ConstantPoolTable cpt_)
    {
        /* When we write out this_class, we replace it with the actual hashed name to speed up
         * ClassLookups */
        int index = -1;
        int hashedName = -1;
        Element classInfo = null;
        Element utf8Info = null;
        
        index = header_.getThisClass().getValue();
        classInfo = cpt_.get(index ); 
                                                         
        index = classInfo.getValue();
        utf8Info = cpt_.get( index ); // get the hashed name Element
        hashedName = utf8Info.getValue();
        
        header_.setThisClass(utf8Info);
        
        //header_.getThisClass().setValue( hashedName );  // get and set the hashed value
        log("<UpdateHeader> this_class index " + index + " -> " + hashedName);
        
        /* When we write out super_class, we replace it with the actual hashed name to speed up
         * ClassLookups */
        index = header_.getSuperClass().getValue();
        if( index != 0 ) // not all classes have a super class
        {
            classInfo = cpt_.get(index ); 
            index = classInfo.getValue();
            utf8Info = cpt_.get( index  );
            hashedName = utf8Info.getValue();
            
            header_.setSuperClass(utf8Info);
            //header_.getSuperClass().setValue( hashedName );  // get and set the hashed value
            log("<UpdateHeader> super_class index " + index + " -> " + hashedName);
        }
        
    }
    
    // Stores list of indexes to Utf8Info's containing class names for post-resolution
    Vector<Integer> ClassInfoResolutionList = new Vector<Integer>();
    
    // Stores list of indexes to Utf8Info's containing strings for placement into
    // the StringTable.
    Vector<Integer> StringInfoResolutionList = new Vector<Integer>();
    
    /** Convert the Constant Pool of the given class file into the ConstantPoolTable
     * format required of the InternalClass structure.
     */
    private void processConstantPool(ConstantPool cp_, ConstantPoolTable cpt_, int classIndex_)
    {
        ClassInfoResolutionList.clear();
        StringInfoResolutionList.clear();
        
        int hashCode = 0;
        int value = 0;
        Element element;
        Element high;
        Element low;
        
        log("  <ProcessConstantPool> processing constant pool...");
        
        // Note that the constant pool is indexed starting from 1, so we must insert
        // a nulled entry at the beginning
        cpt_.add( new ConstantPoolTableEntry() );
        
        // create all the tableEntries up front.
        for(int j = 1; j < cp_.getCount(); j++)
        {
            // create an empty ConstantPoolTableEntry 
            ConstantPoolTableEntry cptEntry = new ConstantPoolTableEntry();
            
            // add it to the ConstantPoolTable
            cpt_.add( cptEntry );
        }
        
        // loop through CPInfo elements
        for(int j = 1; j < cp_.getCount(); j++)
        {
            ConstantPoolTableEntry cptEntry = (ConstantPoolTableEntry) cpt_.get(j);
            
            Element temp = cp_.getElement(j);
            // see if it is an empty cpinfo used as placeholder
            // these are the second halfs of longs and doubles
            if(temp.toString().equalsIgnoreCase("<empty>"))
            {
                log("Skipping empty cp element.");
                continue;
            }
            CPInfo info = (CPInfo) temp;
            Element tag = info.getElement("tag");
            
            // get the actual sub-element
            Element subElement = info.get(1); 
            
            
            switch(tag.getValue())
            {
                
                // Hash UTF8Info strings into a 4 byte code using the hashCode() method,
                // EXCEPT class names (UTF8's pointed to by ClassInfo structures).
                case 1: // UTF8Info
                    // The cptEntry created for this UTF8 is modified by following other links
                    // hash all UTF8Infos on the first pass.  All Classinfo's are subsequently
                    // resolved.
                    String utf8String = ((ConstantUTF8Info)subElement).getString();
                    log("  <ProcessConstantPool> found utf8Info " + utf8String);
                    hashUtf8(j, cp_, cpt_);        
                    break;
                    
                case 3:  // Integer
                    cptEntry.setMetaData( "IntegerInfo" );
                    cptEntry.setValue( subElement.getValue() );
                    
                    break;
                case 4:  // Float

                    cptEntry.setMetaData( "FloatInfo" );
                    cptEntry.setValue( subElement.getValue() );
                    
                    break;
                case 7:  // ClassInfo
                    // copy over the value.  the value is an index.
                    element = subElement.get(0);
                    cptEntry.setValue( element.getValue() );
                    cptEntry.setMetaData( "ClassInfo" );
                    
                    // Must find the referenced UTF8info 'class name' and 'resolve' it
                    //resolveClassInfo(element.getValue(), cp_, cpt_);
                    // store the utf8Info's index.  the string can be pulled from the ConstantPool.
                    ClassInfoResolutionList.add( element.getValue() );
                    break;
                    
                case 8:  // StringInfo
                    
                    element = subElement.get(0);
                    // This must now point to the bytes in the StringTable.
                    // This is a dynamically calculated value.
                    // Set a reference to the StringTableEntry.
                    cptEntry.setValue( element.getValue() );
                    cptEntry.setMetaData( "StringInfo" );
                    StringInfoResolutionList.add(j);
                    break;
                    
                case 12:  // NameAndTypeInfo
                    // Must follow both UTF8Info structures pointed to by the name and type and
                    // hash those elements
                    element = subElement.get(0); // name index
                    cptEntry.setUpper2Bytes(element.getValue());
                    //hashUtf8(element.getValue(), cp_, cpt_);
                    element = subElement.get(1); // descriptor index
                    cptEntry.setLower2Bytes(element.getValue());
                    //hashUtf8(element.getValue(), cp_, cpt_);
                    cptEntry.setMetaData("NameAndTypeInfo");
                    break;
                    
                case 9:   // FieldRef
                    // special consideration required.  These elements contain 2 fields that
                    // must be placed into the upper 2 bytes or lower 2 bytes.
                    // get the fist sub-element (name_index or class_index)
                    element = subElement.get(0);  // first word, upper 2 bytes
                    cptEntry.setUpper2Bytes(element.getValue());
                    element = subElement.get(1);  // second word, lower 2 bytes
                    cptEntry.setLower2Bytes(element.getValue());
                    cptEntry.setMetaData( "FieldRef");
                    break;
                    
                case 10:  // MethodRef
                    // special consideration required.  These elements contain 2 fields that
                    // must be placed into the upper 2 bytes or lower 2 bytes.
                    // get the fist sub-element (name_index or class_index)
                    element = subElement.get(0);  // first word, upper 2 bytes
                    cptEntry.setUpper2Bytes(element.getValue());
                    element = subElement.get(1);  // second word, lower 2 bytes
                    cptEntry.setLower2Bytes(element.getValue());
                    cptEntry.setMetaData("MethodRef");
                    
                    break;
                    
                case 11:  // InterfaceMethodRef
                    
                    // special consideration required.  These elements contain 2 fields that
                    // must be placed into the upper 2 bytes or lower 2 bytes.
                    // get the fist sub-element (name_index or class_index)
                    element = subElement.get(0);  // first word, upper 2 bytes
                    cptEntry.setUpper2Bytes(element.getValue());
                    element = subElement.get(1);  // second word, lower 2 bytes
                    cptEntry.setLower2Bytes(element.getValue());
                    cptEntry.setMetaData("InterfaceMethodRef");
                    
                    break;

                case 5:  // Long
                case 6:  // Double
                    cptEntry.setMetaData("Long or Double word 1");
                    // add two words for the high and low words
                    high = subElement.getElement("high_bytes");
                    cptEntry.setValue( high.getValue() );
                    
                    // get a second entry to hold the low bytes
                    ConstantPoolTableEntry cptEntry2 = (ConstantPoolTableEntry) cpt_.get( j + 1 );
  
                    low = subElement.getElement("low_bytes");
                    cptEntry2.setValue( low.getValue() );
                    cptEntry2.setMetaData("Long or Double word 2");

                    log("  <ProcessConstantPool> Long or Double," +
                                      " high bytes: " + Integer.toHexString( high.getValue()) + 
                                      " low bytes: "  + Integer.toHexString( low.getValue() ) );
                    break;
                default:
                    
                    System.out.println("error processing constant pool ");
                    System.exit(1);
                    break;
            } // end switch
            
        } // end for
        
        // now resolve the list of UTF8Info elements identified as class names
        resolveClassInfo(cp_, cpt_);
    }
    
    // Resolve the utf8 string containing the class name by finding the index of the class
    // matching that name in the ClassFiles list then getting the InternalClass at that
    // index in the InternalClasses list and setting the ConstantPoolTableEntry reference
    // to that InternalClass.  When written out, the offset of the InternalClass gets
    // written to the ConstantPoolTableEntry.
    private void resolveClassInfo(ConstantPool cp_, ConstantPoolTable cpt_)
    {
        for(int i = 0; i < ClassInfoResolutionList.size(); i++)
        {
            int index = ClassInfoResolutionList.get( i );
        
            log("     <resolveClassInfo> resolving utf8 at index " + index);
            // Get the UTF8Info structure from the cp table
            ConstantUTF8Info utf8Info = (ConstantUTF8Info) cp_.getCPElement(index);
        
            // Get the corresponding tableEntry from the cpt
            ConstantPoolTableEntry cptEntry = (ConstantPoolTableEntry) cpt_.get( index );

            // ----- Array Special Handling ------
            // Before we resolve the class in the ClassFiles list, see if this entry
            // is array related (leading '[').  If it is, resolve it to 'java/lang/object'
            // The reason for doing this is to get arrays past this algorithm.
            // The array type is not used currently by the VM.
            // We do have some options for class types:
            //   1. resolve to the class of the array type and refer to that
            //   2. resolve to "object" and use that.
            // It would only matter if reflection or other array type comparison
            // operations were implemented... they are not at this time.
            //   3. do something else with the utf8info entry in the constant pool.
            //
            // However, we do need to help the multianewarray instruction.
            // The UTF8Info element indirectly referred to by the instruction will tell
            // us the type (bool,byte,char,short,int,long,float,double,reference)
            // as well as the number of required dimensions of the array.  This
            // information must be made available to the instruction... so instead
            // of hashing the string to something meaningless, or linking to a
            // class type we don't use, we can parse it and record the number of
            // dimensions in byte1 and the array type in byte0 (remaining bytes 2,3 unused).
            // The alternative is to parse the string in the VM on each invocation,
            // which requires:
            //     1. extra code in the VM
            //     2. extra CPU cycles
            //     3. extra storage space for the entire string.
            // 
            int arrayInfoValue = 0;

            if (utf8Info.getString().startsWith("[") &&
                (arrayInfoValue = computeArrayInfo(utf8Info.getString())) != 0)
            {
                // --------- Array Type ------------

                log("     <resolveClassInfo>   == computing array info : " + Integer.toHexString(arrayInfoValue));
                cptEntry.setValue(arrayInfoValue);
            }
            else
            {
                // --------- Class Type ------------

                int classIndex = -1;

                // Find the index of this class in the ClassFiles list
                classIndex = classFiles.findClassFileIndex(utf8Info.getString());

                if (classIndex == -1)
                {
                    log("     <resolveClassInfo> Error! " + utf8Info.getString() + " not found!");
                    System.exit(1);
                }

                log("     <resolveClassInfo> resolved " + utf8Info.getString() + " to index " + classIndex);

                // Get the InternalClass
                InternalClass internalClass = (InternalClass) internalClasses.get(classIndex);

                // now set the reference
                cptEntry.setReference(internalClass.getHeader());
            }
        
            cptEntry.setMetaData(utf8Info.getString());

            // update global stats
            totalStringBytes += utf8Info.getString().length();
            totalHashBytes += 4;
        }
        
    }
    
    /**
     *  Note that it is ClassInfo that points to the StringTableEntry and NOT the UTF8Info object.
     */
    private void resolveStringInfo(InternalClass internalClass_, ConstantPool cp_, ConstantPoolTable cpt_)
    {
        for(int i = 0; i < StringInfoResolutionList.size(); i++)
        {
            // This is the index to the ClassInfo entry.
            int index = StringInfoResolutionList.get( i );
            
            Element classInfo = cp_.getCPElement(index);

            // Get the corresponding ClassInfo tableEntry from the cpt
            ConstantPoolTableEntry cptEntry = (ConstantPoolTableEntry) cpt_.get( index );
            
            // This index is the UTF8 one.
            index = cptEntry.getValue();
        
            log("     <resolveStringInfo> resolving utf8 at index " + index);
            // Get the UTF8Info structure from the cp table
            ConstantUTF8Info utf8Info = (ConstantUTF8Info) cp_.getCPElement(index);
            cpt_.get(index).setValue(0xcafebabe);
        
            cptEntry.setAlignmentOffset(1); // These entries point to byte aligned data.
            cptEntry.setMetaData(utf8Info.getString());


            // Now need to create a StringTableEntry for this InternalClass.
            //StringTableEntry ste = new StringTableEntry(utf8Info.getString(), internalClasses.getInternalClass("java/lang/String"));
            //ste.setMetaData(utf8Info.getString());
            //cptEntry.setReference(ste);

            cptEntry.setReference(internalClass_.getStringTable().getStringTableEntry(utf8Info.getString(),
                    internalClasses.getInternalClass("java/lang/String")));


            //internalClass_.getStringTable().addStringTableEntry(ste);
        }
    }

    // Hash 
    private void hashUtf8(int index_, ConstantPool cp_, ConstantPoolTable cpt_)
    {
        ConstantUTF8Info utf8Info = (ConstantUTF8Info) cp_.getCPElement(index_);

        // Get the corresponding tableEntry from the cpt
        ConstantPoolTableEntry cptEntry = (ConstantPoolTableEntry) cpt_.get( index_ );
        
        String s = utf8Info.getString();
        int hashCode = s.hashCode();
        
        log("     <hashUtf8> hashing utf8 at index " + index_ + " (" + s + ")");
        
        // update global stats
        totalStringBytes += s.length();
        totalHashBytes += 4;

        // set the value of the ConstantPoolEntry
        cptEntry.setValue(hashCode);
        cptEntry.setMetaData( utf8Info.getString() );
        
    }
    
    /////////////////////////////////////////////////////////////
    // Process Interfaces
    //    
    //    Each element in a ClassFile's interfaces is an index
    //    into the ConstantPool.  Just copy each index value
    //    over to the InterfaceTable.
    /////////////////////////////////////////////////////////////
    private void processInterfaces(Interfaces interfaces_, InterfaceTable interfaceTable_)
    {
        //
        //  InternalClasses representing Interfaces have no InterfaceTable.
        //
        if( interfaceTable_ == null )
            return;
        
        for( int i = 0; i < interfaces_.getCount(); i++ )
        {
            log("  <ProcessInterfaces> processing interface " + i);
            interfaceTable_.addInterfaceIndex( interfaces_.getInterfaceIndex(i) );
        }
        
        // terminate with a null entry
        interfaceTable_.addNullTableEntry();
    }
    
    /////////////////////////////////////////////////////////////
    // Process Methods
    //
    //    1. Create a CodeTableEntry, a MethodTableEntry and a ExceptionHandlerTableEntry
    //    2. Setup the CodeTableEntry
    //    3. Setup the ExceptionHandlerTableEntry for each ExceptionTableEntry in the MethodInfo.
    //       Note that there may be multiple or no ExceptionTableHandlerEntries for each method.
    //    4. Setup the MethodTableEntry, given reference to the CodeTableEntry
    //       and the ExceptionHandlerTableEntry
    //    5. Add each element to their respective tables.
    /////////////////////////////////////////////////////////////
    
    private void processMethods(ClassFile classFile_,    // input from ClassFile (convert FROM)
                                MethodTable methodTable_,   
                                CodeTable codeTable_,
                                ExceptionHandlerTable exceptionHandlerTable_)
    {
        Methods methods = classFile_.getMethods();
        
        // Loop through each MethodInfo pulled out of Methods
        for(int i = 0; i < methods.getCount(); i++)
        {
            // get the MethodInfo
            MethodInfo info = (MethodInfo) methods.getElement(i);
            
            log("  <ProcessMethods> building MethodTableEntry from ( "+ info.getName() + " " + info.getDescriptor() + " )");
            
            // Create the MethodTableEntry 
            MethodTableEntry methodTableEntry = new MethodTableEntry();
            // set metadata to name of method for easier viewing & debugging
            methodTableEntry.setMetaData(info.getName() + " " + info.getDescriptor()); 
            
            // add it to the MethodTable
            methodTable_.add( methodTableEntry );
            
            // setup the MethodTableEntry
            methodTableEntry.setNameIndex( info.getNameIndex() );
            methodTableEntry.setDescriptorIndex( info.getDescriptorIndex() );

            methodTableEntry.setArgCount( computeArgCount( info.getDescriptor() ));
  
            //
            // If this Method is abstract (includes Interface methods) we finish up 
            // here and skip the CodeTable and ExceptionTable stuff.
            //
            if( info.isAbstract() ) {
                log("  <ProcessMethods>   <> Abstract Method.");
                continue;            
            }
            
            // Create a single CodeTableEntry for each MethodTableEntry
            CodeTableEntry codeTableEntry = new CodeTableEntry();
            // set metadata to name of method for easier viewing & debugging
            codeTableEntry.setMetaData(info.getName() + " " + info.getDescriptor() ); 

            CodeAttribute codeAttr = null;
            
            // If the method is native, find the codeAttribute containing native code
            if( info.isNative() )
            {
                // lookup the native method in all NativeMethodLibraries
                try 
                {
                    codeAttr = Packager.lookupNativeMethod( classFile_.getName(), info.getName(), info.getDescriptor() );
                    info.setNative(false);  // clear native flag since it will be resolved.
                }
                catch (IOException e)
                {
                    log("  <ProcessMethods> fatal error: " + e.getMessage());
                    System.exit(1);
                }
            }
            else
            {
                // Get the codeAttribute of the MethodInfo
                codeAttr = info.getCodeAttribute();
            }
            
            methodTableEntry.setAccessFlags( info.getAccessFlags() );
                
            // Setup the CodeTableEntry for this MethodTableEntry
            codeTableEntry.setMaxLocals( codeAttr.getMaxLocals() );
            codeTableEntry.setMaxStack( codeAttr.getMaxStack() );
            codeTableEntry.addCode( codeAttr.getCodeNoLength() );
            
            // Add the codeTableEntry to the CodeTable
            codeTable_.add( codeTableEntry );
            
            // set the codeTableEntry for this MethodTableEntry
            methodTableEntry.setCodeTableEntry( codeTableEntry );
            
            // Setup the ExceptionHandlerTable with an ExceptionHandlerTableEntry for each
            // exception handler entry in the MethodInfo.
            ExceptionTable eTable = codeAttr.getExceptionTable(); 
            for(int j = 0; j < eTable.getCount(); j++)
            {
                // create the internalClass version of the exception handler table entry
                ExceptionHandlerTableEntry exceptionEntry = new ExceptionHandlerTableEntry();
            
                // The exception_handler_ptr in the MethodTableEntry points to the first
                // ExceptionHandlerTableEntry.
                if( j == 0 ) // first entry only
                    methodTableEntry.setExceptionHandlerTableEntry( exceptionEntry );
                
                // get the ClassFile version and copy out the values
                ExceptionTableEntry eEntry = (ExceptionTableEntry) eTable.getElement(j);
                exceptionEntry.setStartPc( eEntry.getStartPc() );
                exceptionEntry.setEndPc( eEntry.getEndPc() );
                exceptionEntry.setHandlerPc( eEntry.getHandlerPc() );
                exceptionEntry.setCatchType( eEntry.getCatchType() );
                
                // add the ExceptionHandlerTableEntry to the ExceptionHandlerTable
                exceptionHandlerTable_.add( exceptionEntry );
            }

            // done adding exception table entries.  terminate the string of entries only
            // if there were any exception table entries, otherwise nothing is added.
            if( eTable.getCount() > 0 )
            {
                exceptionHandlerTable_.addNullTableEntry();
                log("  <ProcessMethods> created " + eTable.getCount() + " exception handler table entries.");
            }
            else
            {
                log("  <ProcessMethods> created no exception table entries.");
            }
            
        } // end for
        
        // terminate the MethodTable
        methodTable_.addNullTableEntry();
    }
    
    /////////////////////////////////////////////////////////////
    // Process Fields
    //    Purpose is to copy over values from ClassFile FieldInfo
    //    to InternalClass FieldTableEntry structure plus:
    //       - set the field_size to either single (1) or double (2)
    //         based on descriptor
    //       - accumulate field sizes to get total size of objects
    //       - set the running total of field sizes for each field.
    //         this aids the JVM in directly accessing memory locations
    //         of fields.
    //    processFields() is called from computeObjectSize()
    //
    //    For each FieldInfo structure in a ClassFile:
    //    1. create a FieldTableEntry
    //    2. copy over
    //      a. name
    //      b. descriptor
    //      c. flags
    //      d. constant value index (if there is one)
    //    3. determine size of field, either 1 - single, or 2 - double
    /////////////////////////////////////////////////////////////
    public int processFields(Fields fields_, FieldTable fieldTable_, int objectSize)
    {
        
        // process each field in the ClassFile
        for( int i = 0; i < fields_.getCount(); i++ )
        {
            FieldInfo info = (FieldInfo) fields_.getElement(i);
            log("  <ProcessFields> processing field ( " + info.getName() + " " + info.getDescriptor() + " )");
            
            FieldTableEntry entry = new FieldTableEntry();
            
            entry.setMetaData(info.getName() + " " + info.getDescriptor()); 
            
            // add the entry to the FieldTable
            fieldTable_.add( entry );
            
            entry.setNameIndex( info.getNameIndex() );
            entry.setDescriptorIndex( info.getDescriptorIndex() );
            entry.setFlags( info.getAccessFlags() );
            
            ConstantValueAttribute constAttr = info.getConstantValueAttribute();
            if( constAttr != null )
            {
                entry.setConstantIndex( constAttr.getConstantValueIndex() );
            }
            
            //
            // To do:
            // The handling of static and final fields is different from normal fields.
            //
            // Static fields must be located in NVM, preferably inside the FieldEntryTable itself.
            // The size of static fields must be still be known.  The 'index' of these fields is already known
            // if the static field is located in the FieldTableEntry.
            //
            // Final fields are instance variables, however they are initialized by the <init> constructor
            // from a value in the constant pool.
                       
            // find the 'size' of the field using computeArgCount method
            int fieldSize = computeFieldSize( info.getDescriptor() );
            entry.setFieldSize( fieldSize );
            entry.setFieldIndex( objectSize );  // set the running total
            log("  <ProcessFields>      set field size to " + fieldSize + " and field index to " + objectSize );

            //
            //  If the field is static, the objectsize must NOT be increased.
            //
            if( info.isStatic() == false )
                objectSize += fieldSize;  // accumulate the running total of field sizes
            
        }
        
        // terminate the field table
        fieldTable_.addNullTableEntry();
        
        return objectSize;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ComputeObjectSize
    //
    // Pre compute the memory footprint required of this InternalClass as well as the individual field indexes
    // required of the JVM implementation of GetField and PutField instructions.
    // To do this, we start with a InternalClass then move to the top most super class and begin counting.  If
    // a super class has a object size value of greater than -1 then we skip counting and use that value.  Otherwise, 
    // for each field counted the accumulated count is set into the FieldTableEntry's FieldIndex element.
    // We count all fields in that InternalClass and then if we have exhausted the list of immediate fields we
    // set the object size for that particular class.  The recursive function unwinds to the next lower InternalClass counting
    // fields in this way.
    //
    
    private int computeObjectSize(ClassFile classFile_)
    {
        log("     <computeObjectSize> processing classfile: " + classFile_.getName());
        
        int totalSize = 0;
        
        int superClassIndex = classFile_.getSuperClass().getValue();
        log("     <computeObjectSize> super class index is " + superClassIndex);
        
        if( superClassIndex != 0 ) // go to the top of the tree
        {
            // get the field name out of the constant pool
            ConstantPool constantPool = classFile_.getConstantPool();
            Element classInfo = constantPool.getCPElement(superClassIndex); // get the ClassInfo
            Element nameIndex = classInfo.getElement("name_index");
            ConstantUTF8Info utf8Info = (ConstantUTF8Info) constantPool.getCPElement( nameIndex.getValue() ) ; // get the UTF8Info
            int classIndex = classFiles.findClassFileIndex( utf8Info.getString() );
            ClassFile superClass = (ClassFile) classFiles.get(classIndex);
            
            totalSize += computeObjectSize( superClass );
        }
        
        // at the top of the inheritance tree
        // if this class already has the size computed then return that size
        // Find the index of this class in the ClassFiles list
        int classIndex = classFiles.findClassFileIndex( classFile_.getName() );
        if( classIndex == -1)
        {
            log("     <computeObjectSize Error! " + classFile_.getName() + " not found!");
            System.exit(1);
        }
        
        log("     <computeObjectSize> resolved " + classFile_.getName() + " to index " + classIndex);
        
        // Get the InternalClass 
        InternalClass internalClass = (InternalClass) internalClasses.get( classIndex );
        
        // check if it has it's size computed and if so return it to the next class
        int size = internalClass.getHeader().getObjectSize().getValue();
        log(" InternalClass " + classFile_.getName() + " size is " + size);
        if( size != -1 )
            return size;  // return the size of this class
        
                
        // otherwise compute the totalSize and update the running total of each field's position
        // Get the ClassFile's Fields
        Fields fields = classFile_.getFields();
        FieldTable fieldTable = internalClass.getFieldTable();
        
        // Process the fields and update totalSize with the sizes of the fields
        totalSize = processFields( fields, fieldTable, totalSize );
        
        // set the object's size
        internalClass.getHeader().getObjectSize().setValue( totalSize );
        log(" InternalClass " + classFile_.getName() + " set size to " + totalSize);
        
        // return the size
        return totalSize;
    }
    /*
    private int computeObjectSize(ClassFile classFile_, boolean isSuper_)
    {
        log("Computing object size for " + classFile_.getName() + " ...");

        // first, get the Fields Element
        Fields fields = classFile_.getFields();

        int count = 0;
        
        // check access flags of fields of super-classes
        for( int num = 0; num < fields.getCount(); num++ )
        {
            // only count non-private fields - this might be wrong.
            FieldInfo info = (FieldInfo)fields.getElement( num );
            // if( info.isPrivate() == false )
                count += computeFieldSize( info.getDescriptor() );
        }
        
        if( classFile_.getSuperClassName() == null )
        {
            // no super class, we are done.
            return count;
        }
        else
        {
            // find the super class
            ClassFile superClass = classFiles.findClassFile( classFile_.getSuperClassName() );
            if( superClass == null )
            {
                log("Error!  Cannot find super class <" + classFile_.getSuperClassName() );
                System.exit(1);
            }
                
            count += computeObjectSize( superClass, true );
        }
        
        return count;
        
    }
    */
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final int ARG_SEARCHING = 0;
    private static final int ARG_BASE = 1;
    private static final int ARG_OBJECT = 2;

    // Determine the number of 4-byte words that need to be copied to the new frame
    // by parsing the method descriptor.
    private int computeArgCount( String descriptor_ )
    {
        int argCount = 0;
        
        // parameters can be one of base, object, array types
        // types   size in ints
        // base:
        //   B           1
        //   C           1
        //   D  (double) 2
        //   F           1
        //   I           1
        //   J  (long)   2
        //   S           1
        //   Z           1
        // object:
        //   L           1
        // array:
        //   [           1
        int parseState = ARG_SEARCHING;
        
        char chars[] = descriptor_.toCharArray();
        
        for(int i = 0; i < chars.length; i++)
        {
            switch(parseState)
            {
                case ARG_SEARCHING:
                    
                    switch(chars[i])
                    {
                        case '(':  // found start of argument list
                            parseState = ARG_BASE;
                            break;
                        default:
                            break;
                    }
                    break;
                
                case ARG_BASE: // standard base type parsing
                    
                    switch(chars[i])
                    {
                        case 'D':  // double, count 2
                        case 'J':  // long, count 2
                            argCount++; // add extra one to the count and fall-through
                        case 'B':
                        case 'C':
                        case 'F':
                        case 'I':
                        case 'S':
                        case 'Z':
                            argCount++;  // for any of these add one to the count
                            break;
                            
                        case 'L':
                            argCount++;  // just one count for an object reference
                            parseState = ARG_OBJECT;
                            break;
                            
                        case ')':  // end of arguments
                            parseState = ARG_SEARCHING;
                            break;
                          
                        case '[':  // just ignore array dimensions
                            break;
                            
                        default:
                           log("Error: unhandled method descriptor " + chars[i]);
                           System.exit(1);
                           break;
                    }
                    break;
                    
                case ARG_OBJECT:   // parsing object reference name (ie, do nothing)
                    
                    switch(chars[i])
                    {
                        case ';':   // denotes end of object name
                            parseState = ARG_BASE; // stop parsing the object reference
                            break;
                        default:
                            break;
                    }
                    break;
                    
                default:
                    break;
            }
        }  // end for
        
        log("   ComputeArgCount finds " + argCount + " args in " + descriptor_);
        return argCount;
        
    }
    
    /** Determines the number of 4-byte words that compose the field type (single or double)
     */
    private int computeFieldSize(String descriptor_)
    {
        // parameters can be one of base, object, array types
        // types   size in ints
        // base:
        //   B           1
        //   C           1
        //   D  (double) 2
        //   F           1
        //   I           1
        //   J  (long)   2
        //   S           1
        //   Z           1
        // object:
        //   L           1
        // array:
        //   [           1
        char chars[] = descriptor_.toCharArray();
        int count = 0;
        
        // look at first character only
        switch(chars[0])
        {
            case 'D':  // double, count 2
            case 'J':  // long, count 2
                count++; // add extra one to the count and fall-through
            case 'B':
            case 'C':
            case 'F':
            case 'I':
            case 'S':
            case 'L':
            case 'Z':
            case '[':
                    
                count++;  // for any of these add one to the count
                break;
                            
            default:
                log("Error: unhandled field descriptor " + chars[0]);
                System.exit(1);
                break;
        }
        
        return count;
    
    }


    /**
     * Build an entry in the internal constant pool table that supports the
     * multianewarray instruction.
     *
     * This entry is formatted as follows:
     *
     *  MSB                      LSB
     *   byte3  byte2  byte1  byte0
     *  ----------------------------
     *  |                          |
     *  ----------------------------
     *
     *  byte0 = type of array identified in the given string
     *  byte1 = number of dimensions identified in the given string
     *
     *  The array type is coded the same as that for the newarray instruction
     *  (becasue newarray will create the array object for the final dimension)
     *
     *
     *
     * @param string
     * @return  formated array info value, or 0 to indicate error.
     */
    private int computeArrayInfo(String string)
    {

        // descriptors            atype code
        // 
        //   B           byte        8
        //   C           char        5
        //   D           double      7
        //   F           float       6
        //   I           int         10
        //   J           long        11
        //   S           short       9
        //   Z           boolean     4
        //   L           reference   10
        //
        //   [           array dimension

        // Note that:
        // reference = 10 (not in newarray atype table, but used by VM to create arrays for
        //                 reference types)


        int dimensions = 0;
        int type = 0;

        char chars[] = string.toCharArray();

        for(int i = 0; i < chars.length; i++)
        {
            switch(chars[i])
            {
                case 'D': type = 7; break;
                case 'J': type = 11; break;
                case 'B': type = 8; break;
                case 'C': type = 5; break;
                case 'F': type = 6; break;
                case 'I': type = 10; break;
                case 'S': type = 9; break;
                case 'Z': type = 4; break;
                case 'L': type = 10; break;
                case '[':  // count array dimensions
                    dimensions++;
                    break;

                default:
                   return 0;
            }

            // Got the type so break (avoid parsing remainder of class type strings)
            if (type != 0)
                break;

        }  // end for


        // Create the value:
        type |= ((dimensions & 0xff) << 8);

        if (dimensions == 0)
            return 0;
        else
            return type;
    }
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    public static void log(String msg)
    {
        System.err.println(msg);
    }
    
}
