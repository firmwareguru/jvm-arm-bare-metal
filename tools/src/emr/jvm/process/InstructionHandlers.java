/*
 * InstructionHandlers.java
 *
 * Created on January 10, 2008, 10:33 PM
 *
 *
 */

package emr.jvm.process;

import emr.jvm.JVMRuntime;
import emr.jvm.memory.MemoryController;
import emr.jvm.memory.ram.Array;
import emr.jvm.memory.ram.Frame;
import emr.jvm.memory.ram.ObjectThread;
import emr.jvm.visualization.MemoryVisualizer;

/**
 *
 * @author Evan Ross
 */
public class InstructionHandlers implements ProcessEnums {
    
        
        
    // 4: GetField (composite: ReferenceLookup, SetupFieldLookup, SetupStaticLookup, TableLookup, FieldGetInfo, StackPopObjectref, ObjectGetField, FieldStackPush)
    public static final void GetField()
    {
        ProcessList.processList[PROCESS_REFERENCELOOKUP].runProcess();
        ProcessList.processList[PROCESS_SETUPFIELDLOOKUP].runProcess();
        //ProcessList.processList[PROCESS_SETUPSTATICLOOKUP].runProcess();
        ProcessList.processList[PROCESS_SETUPVIRTUALLOOKUP].runProcess();
        
        ProcessList.processList[PROCESS_TABLELOOKUP].runProcess();
        ProcessList.processList[PROCESS_FIELDGETINFO].runProcess();
        ProcessList.processList[PROCESS_STACKPOPOBJECTREF].runProcess();
        ProcessList.processList[PROCESS_OBJECTGETFIELD].runProcess();
        ProcessList.processList[PROCESS_FIELDSTACKPUSH].runProcess();
    }

    // i2b & i2c instructions
    public static void IntToByte() 
    {
        ProcessList.processList[PROCESS_STACKPOPSINGLE].runProcess();
        JVMRuntime.value = (byte)JVMRuntime.value; // chop it to 8 bits and sign extend
        ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();        
    }

    public static void IntToChar()
    {
        ProcessList.processList[PROCESS_STACKPOPSINGLE].runProcess();
        JVMRuntime.value = JVMRuntime.value & 0xff; // chop it to 8 bits and zero extend
        ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
    }

    // i2s
    public static void IntToShort()
    {
        ProcessList.processList[PROCESS_STACKPOPSINGLE].runProcess();
        JVMRuntime.value = (short)JVMRuntime.value; // chop it to 16 bits and sign extend
        ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
    }

    public static void IntToFloat()
    {
        ProcessList.processList[PROCESS_STACKPOPSINGLE].runProcess();
        JVMRuntime.value = Float.floatToIntBits((float)JVMRuntime.value);
        ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
    }

    public static void FloatToInt()
    {
        ProcessList.processList[PROCESS_STACKPOPSINGLE].runProcess();
        JVMRuntime.value = (int)Float.intBitsToFloat(JVMRuntime.value);
        ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
    }

     
    // 5: PutField (composite: ReferenceLookup, SetupFieldLookup, SetupStaticLookup, TableLookup, FieldGetInfo, FieldStackPop, StackPopObjectref, ObjectPutField)
    public static final void PutField()
    { 
        ProcessList.processList[PROCESS_REFERENCELOOKUP].runProcess();
        ProcessList.processList[PROCESS_SETUPFIELDLOOKUP].runProcess();
        //ProcessList.processList[PROCESS_SETUPSTATICLOOKUP].runProcess();
        ProcessList.processList[PROCESS_SETUPVIRTUALLOOKUP].runProcess();
        ProcessList.processList[PROCESS_TABLELOOKUP].runProcess();
        ProcessList.processList[PROCESS_FIELDGETINFO].runProcess();
        ProcessList.processList[PROCESS_FIELDSTACKPOP].runProcess();
        ProcessList.processList[PROCESS_STACKPOPOBJECTREF].runProcess();
        ProcessList.processList[PROCESS_OBJECTPUTFIELD].runProcess();        
    }
        
    
      
    // 6: Startup (composite: SetupStaticLookup, SetupMethodLookup, TableLookup, FrameInfoLookup, AllocateMemory, FrameSetup, SetPC )
    public static final void Startup()
    {
        // New startup:
        // 
        //    1. Create a new Thread instance for Thread class "Main".

        // Load first word (pointer to a Thread class to create instance of) into classhandle
        JVMRuntime.classhandle = MemoryController.readWord(0 * 4); // first NVM word
        
        // Partial NewInstance:
        ProcessList.processList[PROCESS_OBJECTSIZELOOKUP].runProcess();
        ProcessList.processList[PROCESS_ALLOCATEMEMORY].runProcess();
        ProcessList.processList[PROCESS_OBJECTSETUP].runProcess();
        
        // Load up name and descriptor of method to run (this must be void run())
        JVMRuntime.name = MemoryController.readWord( 1 * 4 );        // second NVM word
        JVMRuntime.descriptor = MemoryController.readWord( 2 * 4 );  // third NVM word
        
        // Set currentthread to the new object.
        JVMRuntime.currentthread = JVMRuntime.value; 

        //
        //    2. Start the Thread using partial ThreadStart:
        //
        ProcessList.processList[PROCESS_SETUPMETHODLOOKUP].runProcess();
        ProcessList.processList[PROCESS_SETUPVIRTUALLOOKUP].runProcess();
        ProcessList.processList[PROCESS_TABLELOOKUP].runProcess();
        ProcessList.processList[PROCESS_FRAMEINFOLOOKUP].runProcess();
        ProcessList.processList[PROCESS_ALLOCATEMEMORY].runProcess();
        
        // Pre-initialize the Thread so that currentframe is pointing to the new frame already
        // and that 'this' in that frame is setup properly to support ThreadRunSetup without a calling frame.
        
        // Store currentframe into thread
        MemoryController.writeWord(JVMRuntime.currentthread + ObjectThread.CurrentframeOffset, JVMRuntime.handle, MemoryVisualizer.INSTANCE);
         
        // Store 'this' into local var[0] directly
        MemoryController.writeWord( JVMRuntime.handle + Frame.LOCAL_VAR_OFFSET + 0, JVMRuntime.currentthread, MemoryVisualizer.FRAME );
        
        ProcessList.processList[PROCESS_THREADRUNSETUP].runProcess();
        
        // Clear the currentthread again to make these processes work.
        JVMRuntime.currentthread = JVMRuntime.nullregister;
        
        ProcessList.processList[PROCESS_ELIGIBLEQUEUEINSERT].runProcess();  // Ensure it is in the queue.
        ProcessList.processList[PROCESS_ELIGIBLEQUEUEPROCESS].runProcess(); // Finally the context switched in.
 
        
        /*
        // Register initialization
        //    CURRENTFRAME is set to 0xFFFFFFFF to indicate a null frame (required)
        //JVMRuntime.currentframe = 0xffffffff;
                    
        // load the first word of NVM into CLASSHANDLE register
        JVMRuntime.classhandle = MemoryController.readWord( 0 * 4);
                    
        // also place the first word of NVM into the CURRENTCLASS register
        //JVMRuntime.currentclass = MemoryController.readWord( 0 * 4 );
                    
                    
        // load the second word of NVM into the NAME register
        JVMRuntime.name = MemoryController.readWord( 1 * 4 );
                   
        // load the third word of NVM into the DESCRIPTOR register
        JVMRuntime.descriptor = MemoryController.readWord( 2 * 4 );
                    
        //requestProcess( STARTUP_PROCESS );
            
            
        ProcessList.processList[PROCESS_SETUPSTATICLOOKUP].runProcess();
        ProcessList.processList[PROCESS_SETUPMETHODLOOKUP].runProcess();
        ProcessList.processList[PROCESS_TABLELOOKUP].runProcess();
        ProcessList.processList[PROCESS_FRAMEINFOLOOKUP].runProcess();
        ProcessList.processList[PROCESS_ALLOCATEMEMORY].runProcess();
        ProcessList.processList[PROCESS_FRAMESETUP].runProcess();
        ProcessList.processList[PROCESS_SETPC].runProcess();
         */
    }
        
        // 7: FetchInstruction (atomic) : InstructionExecutionEngine
        public static final void FetchInstruction()
        { 
            ProcessList.processList[PROCESS_IEE].runProcess(); 
        }
        
        // 8: New Instance (Composite: ClassInfoLookup, ObjectSizeLookup, AllocateMemory, ObjectSetup, PushStack )
        public static final void NewInstance()
        { 
            ProcessList.processList[PROCESS_CLASSINFOLOOKUP].runProcess();
            ProcessList.processList[PROCESS_OBJECTSIZELOOKUP].runProcess();
            ProcessList.processList[PROCESS_ALLOCATEMEMORY].runProcess();
            ProcessList.processList[PROCESS_OBJECTSETUP].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }

        // 9: InovkeStatic (composite: ReferenceLookup, SetupStaticLookup, SetupMethodLookup, TableLookup, 
        //                             FrameInfoLookup, AllocateMemory, FrameSetup, SetPC )
        public static final void InvokeStatic()
        {
            ProcessList.processList[PROCESS_REFERENCELOOKUP].runProcess();
            ProcessList.processList[PROCESS_SETUPSTATICLOOKUP].runProcess();
            ProcessList.processList[PROCESS_SETUPMETHODLOOKUP].runProcess();
            ProcessList.processList[PROCESS_TABLELOOKUP].runProcess();
            ProcessList.processList[PROCESS_FRAMEINFOLOOKUP].runProcess();
            ProcessList.processList[PROCESS_ALLOCATEMEMORY].runProcess();
            ProcessList.processList[PROCESS_FRAMESETUP].runProcess();
            ProcessList.processList[PROCESS_SETPC].runProcess();
        }
        
        // 10: InvokeVirtual (composite: ReferenceLookup, SetupStaticLookup, SetupMethodLookup, TableLookup,
        //                               FrameInfoLookup, AllocateMemory, SetupVirtualLookup,  
        //                               FrameSetup, ObjectClassLookup, SetupMethodLookup, TableLookup, SetPC )
        public static final void InvokeVirtual()
        {
            ProcessList.processList[PROCESS_REFERENCELOOKUP].runProcess();     // 1.
            ProcessList.processList[PROCESS_SETUPVIRTUALLOOKUP].runProcess();  // 2 pre1 The class referenced might not actually contain the method 
            ProcessList.processList[PROCESS_SETUPMETHODLOOKUP].runProcess();   // 2 pre2
            ProcessList.processList[PROCESS_TABLELOOKUP].runProcess();         // 2.
   
            ProcessList.processList[PROCESS_OBJECTCLASSLOOKUP].runProcess();   // 6. << all this extra stuff does is finds the PC of the actual method

            //ProcessList.processList[PROCESS_SETUPMETHODLOOKUP].runProcess();   //  << but we need to also set the currentclass to the class 
            ProcessList.processList[PROCESS_TABLELOOKUP].runProcess();         // 7. << of the actual method
            
            ProcessList.processList[PROCESS_FRAMEINFOLOOKUP].runProcess();     // 3.  Look up max locals and stack size to get total frame size
            ProcessList.processList[PROCESS_ALLOCATEMEMORY].runProcess();      // 4. Allocate memory for total frame size
            ProcessList.processList[PROCESS_SETUPVIRTUALLOOKUP].runProcess();  // Set the virtual lookup mode here because FrameSetup also needs it.
            ProcessList.processList[PROCESS_FRAMESETUP].runProcess();          // 5.
            ProcessList.processList[PROCESS_SETPC].runProcess();               // 8.
/*
            ProcessList.processList[PROCESS_REFERENCELOOKUP].runProcess();     // 1.
            ProcessList.processList[PROCESS_SETUPVIRTUALLOOKUP].runProcess();  // 2 pre1 The class referenced might not actually contain the method 
            ProcessList.processList[PROCESS_SETUPMETHODLOOKUP].runProcess();   // 2 pre2
            ProcessList.processList[PROCESS_TABLELOOKUP].runProcess();         // 2.
            ProcessList.processList[PROCESS_FRAMEINFOLOOKUP].runProcess();     // 3.  Look up max locals and stack size to get total frame size
            ProcessList.processList[PROCESS_ALLOCATEMEMORY].runProcess();      // 4. Allocate memory for total frame size
            ProcessList.processList[PROCESS_SETUPVIRTUALLOOKUP].runProcess();  // Set the virtual lookup mode here because FrameSetup also needs it.
            ProcessList.processList[PROCESS_FRAMESETUP].runProcess();          // 5.
            ProcessList.processList[PROCESS_OBJECTCLASSLOOKUP].runProcess();   // 6. << all this extra stuff does is finds the PC of the actual method
            ProcessList.processList[PROCESS_SETUPMETHODLOOKUP].runProcess();   //  << but we need to also set the currentclass to the class 
            ProcessList.processList[PROCESS_TABLELOOKUP].runProcess();         // 7. << of the actual method
            ProcessList.processList[PROCESS_SETPC].runProcess();               // 8.
 */
        }
        
        // 11: InvokeSpecial ( composite: ReferenceLookup, SetupStaticLookup, SetupMethodLookup, TableLookup,
        //                                FrameInfoLookup, AllocateMemory, SetupVirtualLookup, FrameSetup, SetPC)
        public static final void InvokeSpecial()
        {
            ProcessList.processList[PROCESS_REFERENCELOOKUP].runProcess();
            ProcessList.processList[PROCESS_SETUPSTATICLOOKUP].runProcess();
            ProcessList.processList[PROCESS_SETUPMETHODLOOKUP].runProcess();
            ProcessList.processList[PROCESS_TABLELOOKUP].runProcess();
            ProcessList.processList[PROCESS_FRAMEINFOLOOKUP].runProcess();
            ProcessList.processList[PROCESS_ALLOCATEMEMORY].runProcess();
            ProcessList.processList[PROCESS_SETUPVIRTUALLOOKUP].runProcess();
            ProcessList.processList[PROCESS_FRAMESETUP].runProcess();
            ProcessList.processList[PROCESS_SETPC].runProcess();
        }
        
        // 12: InvokeInterface ( Same as InvokeVirtual )
        
        // 13: StackDuplicateSingle (composite: StackPeekSingle, StackPushSingle)
        public static final void StackDuplicateSingle()
        { 
            ProcessList.processList[PROCESS_STACKPEEKSINGLE].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }
        
        // 13: StackDuplicateXSingle
        public static final void StackDuplicateXSingle()
        { 
            ProcessList.processList[PROCESS_STACKDUPLICATEXSINGLE].runProcess();
        }

        // 14: LoadSingle ( composite: LocalGetSingle, StackPushSingle )
        public static final void LoadSingle()
        { 
            ProcessList.processList[PROCESS_LOCALGETSINGLE].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }
        
        // 15: StoreSingle ( composite: StackPopSingle, LocalPutSingle )
        public static final void StoreSingle()
        { 
            ProcessList.processList[PROCESS_STACKPOPSINGLE].runProcess();
            ProcessList.processList[PROCESS_LOCALPUTSINGLE].runProcess();
        }
        
        // 16: LoadDouble ( composite: LocalGetDouble, StackPushDouble )
        public static final void LoadDouble()
        { 
            ProcessList.processList[PROCESS_LOCALGETDOUBLE].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHDOUBLE].runProcess();
        }
        
        // 17: StoreDouble ( composite: StackPopDouble, LocalPutDouble )
        public static final void StoreDouble()
        { 
            ProcessList.processList[PROCESS_STACKPOPDOUBLE].runProcess();
            ProcessList.processList[PROCESS_LOCALPUTDOUBLE].runProcess();
        }
        
        // 18: ReturnSingle ( composite: StackPopSingle, FrameTearDown, DeallocateMemory, StackPushSingle )
        public static final void ReturnSingle()
        { 
            ProcessList.processList[PROCESS_STACKPOPSINGLE].runProcess();
            ProcessList.processList[PROCESS_FRAMETEARDOWN].runProcess();
            ProcessList.processList[PROCESS_DEALLOCATEMEMORY].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }
        
        // 19: ReturnDouble ( composite: StackPopDouble, FrameTearDown, DeallocateMemory, StackPushDouble )
        public static final void ReturnDouble()
        { 
            ProcessList.processList[PROCESS_STACKPOPDOUBLE].runProcess();
            ProcessList.processList[PROCESS_FRAMETEARDOWN].runProcess();
            ProcessList.processList[PROCESS_DEALLOCATEMEMORY].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHDOUBLE].runProcess();
        }
        
        // 20: ReturnVoid ( composte: FrameTearDown, DeallocateMemory  )
        public static final void ReturnVoid()
        { 
            ProcessList.processList[PROCESS_FRAMETEARDOWN].runProcess();
            ProcessList.processList[PROCESS_DEALLOCATEMEMORY].runProcess();
        }
        
        // 21: Const Store Single ( atomic: StackPushSingle )
        public static final void ConstStoreSingle()
        { 
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }
        
        // 22: Const Store Double
        //{ },
        
        // 23: StackPopSingle
        public static final void StackPopSingle()
        { 
            ProcessList.processList[PROCESS_STACKPOPSINGLE].runProcess();
        }
        
        // 24: StackPopDouble
        public static final void StackPopDouble()
        { 
            ProcessList.processList[PROCESS_STACKPOPDOUBLE].runProcess();
        }
        
        // 25: StackPushSingle
        public static final void StackPushSingle()
        {
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }
        
        // 26: StackPushDouble
        //{ },
        
        // 27: IncrementSingleInt ( LocalGetSingleAlt, AddSingleInt, LocalPutSingle )
        public static final void IncrementSingleInt()
        {
           ProcessList.processList[PROCESS_LOCALGETSINGLEALT].runProcess();
           ProcessList.processList[PROCESS_ADDSINGLEINT].runProcess();
           ProcessList.processList[PROCESS_LOCALPUTSINGLE].runProcess();
        }
        
        // 28: ArrayLengthGet ( StackPopSingle, ArrayLengthGet, StackPushSingle )
        public static final void ArrayLengthGet()
        { 
            ProcessList.processList[PROCESS_STACKPOPSINGLE].runProcess();
            ProcessList.processList[PROCESS_ARRAYLENGTHGET].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }
        
        // 29: NewArrayProcess ( StackPopSingle, ArraySizeLookup, AllocateMemory, ArraySetup, ObjectSetup, StackPushSingle )
        public static final void NewArrayProcess()
        { 
            ProcessList.processList[PROCESS_STACKPOPSINGLE].runProcess();
            ProcessList.processList[PROCESS_ARRAYSIZELOOKUP].runProcess();
            ProcessList.processList[PROCESS_ALLOCATEMEMORY].runProcess();
            ProcessList.processList[PROCESS_ARRAYSETUP].runProcess();
            ProcessList.processList[PROCESS_OBJECTSETUP].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }


        // Inputs: index - CP index for array type and total dimensions
       
        public static boolean MultiAnewArray() {

            // Get array type and total dimensions (generated by ClassPackager)
            // from constant pool with ClassInfoLookupProcess
            // result is in CLASSHANDLE register
            ProcessList.processList[PROCESS_CLASSINFOLOOKUP].runProcess();


            // PC is at number of dimensions operand .. can access as needed
            //   TABLEHANDLE   - counter
            //   DESCRIPTOR    - depth
            //   VALUE2        - count
            //   INDEX         - temp2

            // Global registers:
            //   TABLEHANDLE   - base arrayref
            //   DESCRIPTOR    - depth
            //   VALUE2        - handle that moves up and down
            //   INDEX         - temp2

            // stack model:
            //
            // The counts are displaced by counters.  The counts
            // are available as the array length
            // ----------------
            // | counter1        => count1 => array length @ depth0
            // ----------------
            // | counter2        => count2 => array length @ depth1
            // ----------------
            // | counterN        => countN => array length @ depth(N-1)
            // ----------------


            // Get dimensions
            // if dimensions >= 1 proceed
            JVMRuntime.value1 = MemoryController.readByte(JVMRuntime.pc);
            System.out.println("Creating array of : " + JVMRuntime.value1 + " of " + (JVMRuntime.classhandle >>> 8) + " dimensions.");




            if (JVMRuntime.value1 == 0)
                return true;

            // Initialize stackpointer
            // Move stackpointer to base of counts
            JVMRuntime.stackpointer -= (JVMRuntime.value1 * 4);

            // initialize depth = 0;
            JVMRuntime.descriptor = 0;

            // initialize base arrayref
            JVMRuntime.tablehandle = JVMRuntime.nullregister;

            for (int i = 0; i < JVMRuntime.value1; i++) {
                int count = MemoryController.readWord(JVMRuntime.stackpointer + i * 4);
                System.out.println("  Count" + i + " = " + count);
            }

            // LOOP:
            // do { } while (depth > 0 && counter[depth] > 0)

            JVMRuntime.value1 = MemoryController.readWord(JVMRuntime.stackpointer + (JVMRuntime.descriptor * 4));
            //do
            while (JVMRuntime.descriptor >= 0 && JVMRuntime.value1 >= 0)
            {
                System.out.println("  Depth: " + JVMRuntime.descriptor);

                //////////////////
                // Pre-decrement the counter.
                //////////////////
                // decrement the count and put it back on the stack for the next level
                // This is essentially initialization of the counter - it starts at 1 less than the count.
                JVMRuntime.value1 = JVMRuntime.value1 - 1;
                MemoryController.writeWord(JVMRuntime.stackpointer + JVMRuntime.descriptor * 4, JVMRuntime.value1, MemoryVisualizer.FRAME);
                System.out.println("  Decrement counter to " + JVMRuntime.value1);
                //////////////////


                // Find if we need to create one
                // Find where to put it: value2
                // If depth == 0,
                //   put it into base register
                // else if depth == 1
                //   put it into base register[count[depth]]
                // else if depth > 1
                //   do iterative find

                if (JVMRuntime.descriptor == 0)
                {
                    JVMRuntime.value2 = JVMRuntime.tablehandle;
                    System.out.println("    Checking base = " + Integer.toHexString(JVMRuntime.tablehandle));
                }
                else if (JVMRuntime.descriptor == 1)
                {
                    // index = counter(depth-1)
                    JVMRuntime.index = MemoryController.readWord(JVMRuntime.stackpointer + (JVMRuntime.descriptor - 1) * 4);
                    JVMRuntime.handle = JVMRuntime.tablehandle;
                    ProcessList.processList[PROCESS_ARRAYGETSINGLE].runProcess();
                    JVMRuntime.value2 = JVMRuntime.value;
                    System.out.println("    Checking " + Integer.toHexString(JVMRuntime.handle) + "[" + JVMRuntime.index + "] = " + Integer.toHexString(JVMRuntime.value));
                }
                else
                {
                    // Now traverse down to the array
                    // Start if off: base arrayref into handle
                    JVMRuntime.handle = JVMRuntime.tablehandle;
                    JVMRuntime.value1 = 0; // init a counter
                    while (JVMRuntime.value1 < JVMRuntime.descriptor - 1)
                    {
                        // index = counter(tdepth) + 1
                        // Add one because the counter will point to where the next array should be added at this level;

                        JVMRuntime.index = MemoryController.readWord(JVMRuntime.stackpointer + (JVMRuntime.value1 * 4));

                        System.out.print("    Checking (" + JVMRuntime.value1 + ") " + Integer.toHexString(JVMRuntime.handle) + "[" +  JVMRuntime.index + "] =");
                        // value = handle[index]
                        ProcessList.processList[PROCESS_ARRAYGETSINGLE].runProcess();

                        JVMRuntime.handle = JVMRuntime.value;

                        System.out.println(Integer.toHexString(JVMRuntime.handle));
                        
                        JVMRuntime.value1++;
                    }

                    // INDEX = counter
                    // HANDLE = arrayref
                    // arrayref[counter] = value
                    JVMRuntime.index = MemoryController.readWord(JVMRuntime.stackpointer + (JVMRuntime.value1 * 4));
                    ProcessList.processList[PROCESS_ARRAYGETSINGLE].runProcess();
                    JVMRuntime.value2 = JVMRuntime.value;
                    System.out.println("    Checking " + Integer.toHexString(JVMRuntime.handle) + "[" + JVMRuntime.index + "] = " + Integer.toHexString(JVMRuntime.value));
                }


                // FOUND where to put it... is there something there already?
                // if NO, create the array at this depth.
                // if YES, don't create the array at this depth.
                if (JVMRuntime.value2 == JVMRuntime.nullregister)
                {
                    System.out.println("  nothing there.. creating array");
                    // Create an array of count[depth] size of
                    // the required type.

                    JVMRuntime.value1 = MemoryController.readByte(JVMRuntime.pc);
                    if ((JVMRuntime.descriptor + 1) == JVMRuntime.value1) // This is the last dimension to make
                    {
                        // Pull out the total dimensions from classhandle
                        JVMRuntime.index = JVMRuntime.classhandle >>> 8;
                        if (JVMRuntime.value1 == JVMRuntime.index) // Same number of dimensions
                        {
                            JVMRuntime.index = JVMRuntime.classhandle & 0xff;
                        }
                        else // Not same number - make the last dimension also reference type
                        {
                            JVMRuntime.index = 12;
                        }
                    }
                    else // Not last dimension - make reference type
                    {
                        JVMRuntime.index = 12;
                    }

                    // count of elements
                    JVMRuntime.value = MemoryController.readWord(JVMRuntime.stackpointer + JVMRuntime.descriptor * 4);
                    //!! Add one because it was pre-decremented at the start of the loop.
                    JVMRuntime.value += 1;

//                    //////////////////
//                    // Pull the count out and initialize the counter.
//                    //////////////////
//                    // decrement the count and put it back on the stack for the next level
//                    // This is essentially initialization of the counter - it starts at 1 less than the count.
//                    JVMRuntime.value1 = JVMRuntime.value - 1;
//                    MemoryController.writeWord(JVMRuntime.stackpointer + JVMRuntime.descriptor * 4, JVMRuntime.value1, MemoryVisualizer.FRAME);
//                    System.out.println("  Initialize counter to " + JVMRuntime.value1);
//                    //////////////////


                    System.out.println("  Create array of count: " + JVMRuntime.value + " at depth " + JVMRuntime.descriptor + " of type " + JVMRuntime.index);

                    // Input: value - count of elements to create - this would be the counter pulled off from the last iteration
                    //        index - type of array to create
                    ProcessList.processList[PROCESS_ARRAYSIZELOOKUP].runProcess();
                    ProcessList.processList[PROCESS_ALLOCATEMEMORY].runProcess();
                    ProcessList.processList[PROCESS_ARRAYSETUP].runProcess();
                    ProcessList.processList[PROCESS_OBJECTSETUP].runProcess();
                    // Output: value - object ref
                    // backup into VALUE2
                    JVMRuntime.value2 = JVMRuntime.value;

                    System.out.println("    Created: " + Integer.toHexString(JVMRuntime.value));

                    // If depth is 0, put it in the base ref regiser
                    if (JVMRuntime.descriptor == 0)
                    {
                        JVMRuntime.tablehandle = JVMRuntime.value;
                        System.out.println("    Putting array into base.");
                    }
                    else if (JVMRuntime.descriptor == 1)
                    {
                        // index = counter(depth-1)
                        JVMRuntime.index = MemoryController.readWord(JVMRuntime.stackpointer + (JVMRuntime.descriptor - 1) * 4);
                        JVMRuntime.handle = JVMRuntime.tablehandle;
                        ProcessList.processList[PROCESS_ARRAYPUTSINGLE].runProcess();
                        System.out.println("    Putting array " + Integer.toHexString(JVMRuntime.value) + " into " + Integer.toHexString(JVMRuntime.handle) + "[" + JVMRuntime.index + "]");

//                        JVMRuntime.value1 = MemoryController.readWord(JVMRuntime.stackpointer + (JVMRuntime.descriptor - 1) * 4);
//                        JVMRuntime.value1--;
//                        MemoryController.writeWord(JVMRuntime.stackpointer + (JVMRuntime.descriptor - 1)* 4, JVMRuntime.value1, MemoryVisualizer.FRAME);
//                        System.out.println("  Decrement counter to " + JVMRuntime.value1);

                    }
                    else
                    {
                        // Now traverse down to the array
                        // Start if off: base arrayref into handle
                        JVMRuntime.handle = JVMRuntime.tablehandle;
                        JVMRuntime.value1 = 0; // init a counter
                        while (JVMRuntime.value1 < JVMRuntime.descriptor - 1)
                        {
                            // index = counter(tdepth)
                            JVMRuntime.index = MemoryController.readWord(JVMRuntime.stackpointer + (JVMRuntime.value1 * 4));

                            // value = handle[index]
                            ProcessList.processList[PROCESS_ARRAYGETSINGLE].runProcess();

                            JVMRuntime.handle = JVMRuntime.value;

                            JVMRuntime.value1++;
                        }

                        // INDEX = counter
                        // HANDLE = arrayref
                        // arrayref[counter] = value
                        JVMRuntime.index = MemoryController.readWord(JVMRuntime.stackpointer + (JVMRuntime.value1 * 4));
                        JVMRuntime.value = JVMRuntime.value2;
                        ProcessList.processList[PROCESS_ARRAYPUTSINGLE].runProcess();
                        System.out.println("    Putting array " + Integer.toHexString(JVMRuntime.value)+ " into " + Integer.toHexString(JVMRuntime.handle) + "[" + JVMRuntime.index + "]");

//                        JVMRuntime.value1 = MemoryController.readWord(JVMRuntime.stackpointer + (JVMRuntime.descriptor) * 4);
//                        JVMRuntime.value1--;
//                        MemoryController.writeWord(JVMRuntime.stackpointer + JVMRuntime.descriptor * 4, JVMRuntime.value1, MemoryVisualizer.FRAME);
//                        System.out.println("  Decrement counter to " + JVMRuntime.value1);
                    }

                }


//                // if count was zero of elements, now -1, exit early, don't create any more dimensions.
//                JVMRuntime.value = MemoryController.readWord(JVMRuntime.stackpointer + JVMRuntime.descriptor * 4);
//                if (JVMRuntime.value == -1)
//                {
//                    System.out.println("  Terminating: count at depth " + JVMRuntime.descriptor + " = -1");
//                    break;
//                }

                // Now test the end condition:
                //   if counter[depth] = 0 or
                //   if depth = number of dimensions, depth--;
                JVMRuntime.cphandle = MemoryController.readWord(JVMRuntime.stackpointer + (JVMRuntime.descriptor * 4));
                JVMRuntime.value1 = MemoryController.readByte(JVMRuntime.pc);
                if (JVMRuntime.cphandle < 0 || JVMRuntime.descriptor + 1 == JVMRuntime.value1)
                //if (JVMRuntime.descriptor + 1 == JVMRuntime.value1)
                {
                    // just made last entry.  move up in depth
                    // first put the total count back in
                    // Get the length :  value <- value(length)
                    JVMRuntime.value = JVMRuntime.value2;
                    ProcessList.processList[PROCESS_ARRAYLENGTHGET].runProcess();
                    MemoryController.writeWord(JVMRuntime.stackpointer + JVMRuntime.descriptor * 4, JVMRuntime.value, MemoryVisualizer.FRAME);

                    JVMRuntime.descriptor--;
                    System.out.println("  Move up to " + JVMRuntime.descriptor);

                }
                else
                {

                    // if count was zero of elements, now -1, exit early, don't create any more dimensions.
                    JVMRuntime.value = MemoryController.readWord(JVMRuntime.stackpointer + JVMRuntime.descriptor * 4);
                    if (JVMRuntime.value == -1)
                    {
                        System.out.println("  Terminating: count is 0 at depth" + JVMRuntime.descriptor + ", create no more dimensions.");
                        break;
                    }
                    // Not zero... decrement the count
                    //JVMRuntime.cphandle--;
                    //MemoryController.writeWord(JVMRuntime.stackpointer + (JVMRuntime.descriptor * 4), JVMRuntime.cphandle, MemoryVisualizer.FRAME);
                    JVMRuntime.descriptor ++; // depth++
                    System.out.println("  Move down to " + JVMRuntime.descriptor);
                }
            
                
     


                JVMRuntime.value1 = MemoryController.readWord(JVMRuntime.stackpointer + (JVMRuntime.descriptor)* 4);

                System.out.println("  Testing: " + JVMRuntime.descriptor + " : " + JVMRuntime.value1 );

                // (depth > 0 && counter[depth] > 0)
            } // while (JVMRuntime.descriptor >= 0 && JVMRuntime.value1 >= 0);


            // Lastly...
            //   put the base array ref on the stack,
            //   increment the stackpointer.
            MemoryController.writeWord(JVMRuntime.stackpointer , JVMRuntime.tablehandle, MemoryVisualizer.FRAME);
            JVMRuntime.stackpointer += 4;

            System.out.println("All done.");





            /*
            // Get dimensions
            JVMRuntime.value1 = MemoryController.readByte(JVMRuntime.pc);

            // if dimensions >= 1 proceed
            if (JVMRuntime.value1 == 0)
                return true;
            
            // Move stackpointer to base of counts
            JVMRuntime.stackpointer -= (JVMRuntime.value1 * 4);

            // Get the number of elements of the first dimension
            JVMRuntime.value = MemoryController.readWord(JVMRuntime.stackpointer);

            // Create the first dimension
            JVMRuntime.index = 12; // reference type
            // Input: value - count of elements to create
            //        index - type of array to create
            ProcessList.processList[PROCESS_ARRAYSIZELOOKUP].runProcess();
            ProcessList.processList[PROCESS_ALLOCATEMEMORY].runProcess();
            ProcessList.processList[PROCESS_ARRAYSETUP].runProcess();
            ProcessList.processList[PROCESS_OBJECTSETUP].runProcess();
            // Output: value - object ref

            // Move base arrayref to value2 - thats a 'global' variable here.
            JVMRuntime.value2 = JVMRuntime.value;

            // This is the root of the multidimensional array -
            // it must go back on the stack where 'count1' was taken
            MemoryController.writeWord(JVMRuntime.stackpointer, JVMRuntime.value, MemoryVisualizer.FRAME);

            // Initialize depth counter
            JVMRuntime.descriptor = 1;

            // Build out the multidimentional array, then work backwards
            // until all counts are zero.

            JVMRuntime.value1 = MemoryController.readByte(JVMRuntime.pc);

            if (JVMRuntime.descriptor < JVMRuntime.value1)
            {
                // Initialize the counter at this level to be equal to count1 which
                // is the size of the previous array (no longer in the stack).
                //JVMRuntime.index = JVMRuntime.descriptor - 1; // depth - 1;

                // Get the base arrayref
                JVMRuntime.handle = MemoryController.readWord(JVMRuntime.stackpointer);

                // Loop here:
                //   Inputs: handle = previous level arrayref
                do
                {
                    // Get the length :  value <- value(length)
                    JVMRuntime.value = JVMRuntime.handle;
                    ProcessList.processList[PROCESS_ARRAYLENGTHGET].runProcess();

                    // Initialize counter(depth) = count(depth-1) - 1
                    JVMRuntime.tablehandle = JVMRuntime.value - 1;

                    // Pull out current level's count from stack
                    // This is the size of the arrays to be created at this level.
                    JVMRuntime.value2 = MemoryController.readWord(JVMRuntime.stackpointer + (JVMRuntime.descriptor * 4));

                    // Put the counter in
                    MemoryController.writeWord(JVMRuntime.stackpointer + (JVMRuntime.descriptor * 4), JVMRuntime.tablehandle, MemoryVisualizer.FRAME);

                    // Test at the next depth level
                    JVMRuntime.cphandle = JVMRuntime.descriptor + 1;

                    // Now create this level: depending on whether this is the last dimension
                    if (JVMRuntime.cphandle == JVMRuntime.value1) // This is the last dimension to make
                    {
                        // Pull out the total dimensions from classhandle
                        JVMRuntime.index = JVMRuntime.classhandle >>> 8;
                        if (JVMRuntime.cphandle == JVMRuntime.index) // Same number of dimensions
                        {
                            JVMRuntime.index = JVMRuntime.classhandle & 0xff;
                        }
                        else // Not same number - make the last dimension also reference type
                        {
                            JVMRuntime.index = 12;
                        }
                    }
                    else // Not last dimension - make reference type
                    {
                        JVMRuntime.index = 12;
                    }

                    JVMRuntime.value1 = MemoryController.readByte(JVMRuntime.pc);

                    JVMRuntime.value = JVMRuntime.value2; // Make the previous count number of elements.

                    // Input: value - count of elements to create - this would be the counter pulled off from the last iteration
                    //        index - type of array to create
                    ProcessList.processList[PROCESS_ARRAYSIZELOOKUP].runProcess();
                    ProcessList.processList[PROCESS_ALLOCATEMEMORY].runProcess();
                    ProcessList.processList[PROCESS_ARRAYSETUP].runProcess();
                    ProcessList.processList[PROCESS_OBJECTSETUP].runProcess();
                    // Output: value - object ref
                    // Back it up:
                    JVMRuntime.name = JVMRuntime.value;

                    // dump this arrayref into the previous level array at index 'counter2'

                    JVMRuntime.value1 = 1;
                    // Get the base arrayref
                    JVMRuntime.handle = MemoryController.readWord(JVMRuntime.stackpointer);
                    // Now traverse down to the array
                    while (JVMRuntime.value1 < JVMRuntime.descriptor)
                    {
                        // index = counter(currentdepth)
                        JVMRuntime.index = MemoryController.readWord(JVMRuntime.stackpointer + (JVMRuntime.value1 * 4));

                        // value = handle[index]
                        ProcessList.processList[PROCESS_ARRAYGETSINGLE].runProcess();

                        JVMRuntime.handle = JVMRuntime.value;

                        JVMRuntime.value1++;
                    }

                    // Found where I should put it (handle)
                    // Index I should put it is the counter at this depth
                    JVMRuntime.index = MemoryController.readWord(JVMRuntime.stackpointer + (JVMRuntime.descriptor * 4));
                    JVMRuntime.value = JVMRuntime.name;
                    // handle[index] = value
                    ProcessList.processList[PROCESS_ARRAYPUTSINGLE].runProcess();

                    JVMRuntime.tablehandle--;
                    // store counter back in stack

                    // Are we at end of tree?
                    JVMRuntime.value1 = MemoryController.readByte(JVMRuntime.pc);
                    if (JVMRuntime.value1 == JVMRuntime.descriptor - 1)
                        break;

                    // Modify the depth counter:
                    //   If counter == 0 ||  depth == num dimensions
                    //       depth--;
                    //   else
                    //       depth++


                    JVMRuntime.value1 = MemoryController.readByte(JVMRuntime.pc);
                    if (JVMRuntime.tablehandle == 0 || JVMRuntime.descriptor == JVMRuntime.value1 )
                    {
                        // put the array length (count) back onto the stack at this depth.
                        MemoryController.writeWord(JVMRuntime.stackpointer + (JVMRuntime.descriptor * 4), JVMRuntime.value2, MemoryVisualizer.FRAME);
                        JVMRuntime.descriptor--;
                    }
                    else
                    {
                        // Increment the depth
                        JVMRuntime.descriptor++;
                    }


                    System.out.println("Depth: " + JVMRuntime.descriptor);


                    // if the depth is back at the base, we are done
                } while (JVMRuntime.descriptor > 0);



            }



            // Work backwards now, until the count at stackpointer[depth] is zero.


*/







/*
            // INDEX is now free.
            // Allowed intermediary registers:
            //   TABLEHANDLE, VALUE2, DESCRIPTOR

            JVMRuntime.value2 = MemoryController.readByte(JVMRuntime.pc);
            System.out.println("Num Dimensions: " + JVMRuntime.value2);
            
            // Can't handle more then 2 dimentions right now.
            if (JVMRuntime.value2 > 2)
            {
                return false; // not handled
            }


            // Setup the stackpointer to point to the first dimension
            JVMRuntime.stackpointer -= (JVMRuntime.value2 * 4);
            System.out.println("SP: " + Integer.toHexString(JVMRuntime.stackpointer));

            // Get number of elements of first dimension
            JVMRuntime.value = MemoryController.readWord(JVMRuntime.stackpointer);
            System.out.println("First dimension count: " + JVMRuntime.value);

            // Set type to reference (int)
            JVMRuntime.index = 12;

            // Input: value - count of elements to create
            //        index - type of array to create
            ProcessList.processList[PROCESS_ARRAYSIZELOOKUP].runProcess();
            ProcessList.processList[PROCESS_ALLOCATEMEMORY].runProcess();
            ProcessList.processList[PROCESS_ARRAYSETUP].runProcess();
            ProcessList.processList[PROCESS_OBJECTSETUP].runProcess();
            // Output: value - object ref

            // This is the root of the multidimensional array -
            // it must go back on the stack where the first 'count' was taken
            MemoryController.writeWord(JVMRuntime.stackpointer, JVMRuntime.value, MemoryVisualizer.FRAME);

            // Need to copy objectref somewhere else so we track where to put the arrayref of the
            // next dimension.
            JVMRuntime.descriptor = JVMRuntime.value;
            System.out.println("Copy descriptor: " + Integer.toHexString(JVMRuntime.descriptor));

            // Now that the first dimension is created, work on the rest (if there are any)
            JVMRuntime.tablehandle = 1; // Counter

            // Get the number of dimensions to create...
            JVMRuntime.value2 = MemoryController.readByte(JVMRuntime.pc);

            while (JVMRuntime.tablehandle < JVMRuntime.value2) // While there are more dimensions to create
            {

                System.out.println("Loop iteration: " + JVMRuntime.tablehandle);
                JVMRuntime.tablehandle++;

                // Test for the last dimension and whether we need to create a different array type.
                // Now, if they are the same the last dimension must be of the type in
                // classhandle.  Otherwise, the last dimension is just another reference type.
                if (JVMRuntime.tablehandle == JVMRuntime.value2) // This is the last dimension
                {
                    // Pull out the total dimensions from classhandle
                    JVMRuntime.index = JVMRuntime.classhandle >>> 8;
                    if (JVMRuntime.tablehandle == JVMRuntime.index) // Same number of dimensions
                    {
                        JVMRuntime.index = JVMRuntime.classhandle & 0xff;
                    }
                    else // Not same number - make the last dimension also reference type
                    {
                        JVMRuntime.index = 12;
                    }
                }
                else // Not last dimension - make reference tyoe
                {
                    JVMRuntime.index = 12;
                }



                // Get number of elements of previous dimension - this is the number
                // of array objects to create of the next dimension's size
                // If it is zero, do not continue - return.

                // Need to pull this from the length field of the previous arrayref.
                JVMRuntime.value2 = MemoryController.readWord(JVMRuntime.stackpointer);
                JVMRuntime.value2 = MemoryController.readWord(JVMRuntime.value2 + Array.ArrayLengthOffset);

                System.out.println("count: " + JVMRuntime.value2);

                while(JVMRuntime.value2 > 0)
                {
                    JVMRuntime.value2--;

                    // fill each element of the last array.
                    JVMRuntime.value = MemoryController.readWord(JVMRuntime.stackpointer + (JVMRuntime.tablehandle - 1) * 4);

                    // Input: value - count of elements to create
                    //        index - type of array to create
                    ProcessList.processList[PROCESS_ARRAYSIZELOOKUP].runProcess();
                    ProcessList.processList[PROCESS_ALLOCATEMEMORY].runProcess();
                    ProcessList.processList[PROCESS_ARRAYSETUP].runProcess();
                    ProcessList.processList[PROCESS_OBJECTSETUP].runProcess();
                    // Output: value - object ref

                    // Now, the previous array's reference is in descriptor.
                    // Load in this arrays reference into each element of the last.
                    // Put value into arrayref + (object fields + arraylength) + value2 (count of elements)
                    System.out.println("D: " + Integer.toHexString(JVMRuntime.descriptor) + "V2: " + Integer.toHexString(JVMRuntime.value2));
                    MemoryController.writeWord(JVMRuntime.descriptor + Array.ArrayBaseSize + (JVMRuntime.value2 * 4), JVMRuntime.value, MemoryVisualizer.INSTANCE);

                }

                // Re-get the number of dimensions to create for loop test
                JVMRuntime.value2 = MemoryController.readByte(JVMRuntime.pc);
            
            }

            // Lastly, increment the stackpoint one up to where it should be - the next slot
            JVMRuntime.stackpointer += 4;
 */

            return true;

        }
        
        ////////////////////////////////////////////////////////////////////////
        // Computational Process Flows
        ////////////////////////////////////////////////////////////////////////
        
        /////////
        // ADD //
        /////////
        
        // 30: AddSingleInt ( PrepareALUSingleProcess, AddSingleIntProcess, StackPushSingle )
        public static final void AddSingleInt()
        {
            ProcessList.processList[PROCESS_PREPAREALUSINGLE].runProcess();
            ProcessList.processList[PROCESS_ADDSINGLEINT].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }
        
        // 31: AddDoubleInt ( PrepareALUDoubleProcess, AddDoubleIntProcess, StackPushDouble )
        public static final void AddDoubleInt()
        {
            ProcessList.processList[PROCESS_PREPAREALUDOUBLE].runProcess();
            ProcessList.processList[PROCESS_ADDDOUBLEINT].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHDOUBLE].runProcess();
        }

        // 32: AddSingleFloat ( PrepareALUSingleProcess, AddSingleFloatProcess, StackPushSingle )
        public static final void AddSingleFloat()
        {
            ProcessList.processList[PROCESS_PREPAREALUSINGLE].runProcess();
            ProcessList.processList[PROCESS_ADDSINGLEFLOAT].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }
        
        // 33: AddDoubleFloat ( PrepareALUDoubleProcess, AddDoubleFloatProcess, StackPushDouble )
        public static final void AddDoubleFloat()
        {
            ProcessList.processList[PROCESS_PREPAREALUDOUBLE].runProcess();
            ProcessList.processList[PROCESS_ADDDOUBLEFLOAT].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHDOUBLE].runProcess();
        }
        
        /////////
        // SUB //
        /////////
        
        // 34: SubSingleInt ( PrepareALUSingleProcess, SubSingleIntProcess, StackPushSingle )
        public static final void SubSingleInt()
        {
            ProcessList.processList[PROCESS_PREPAREALUSINGLE].runProcess();
            ProcessList.processList[PROCESS_SUBSINGLEINT].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }
        
        // 35: SubDoubleInt ( PrepareALUDoubleProcess, SubDoubleIntProcess, StackPushDouble )
        public static final void SubDoubleInt()
        {
            ProcessList.processList[PROCESS_PREPAREALUDOUBLE].runProcess();
            ProcessList.processList[PROCESS_SUBDOUBLEINT].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHDOUBLE].runProcess();
        }

        // 36: SubSingleFloat ( PrepareALUSingleProcess, SubSingleFloatProcess, StackPushSingle )
        public static final void SubSingleFloat()
        {
            ProcessList.processList[PROCESS_PREPAREALUSINGLE].runProcess();
            ProcessList.processList[PROCESS_SUBSINGLEFLOAT].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }
        
        // 37: SubDoubleFloat ( PrepareALUDoubleProcess, SubDoubleFloatProcess, StackPushDouble )
        public static final void SubDoubleFloat()
        {
            ProcessList.processList[PROCESS_PREPAREALUDOUBLE].runProcess();
            ProcessList.processList[PROCESS_SUBDOUBLEFLOAT].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHDOUBLE].runProcess();
        }

        //////////
        // MULT //
        //////////
        
        // 38: MultSingleInt ( PrepareALUSingleProcess, MultSingleIntProcess, StackPushSingle )
        public static final void MultSingleInt()
        {
            ProcessList.processList[PROCESS_PREPAREALUSINGLE].runProcess();
            ProcessList.processList[PROCESS_MULTSINGLEINT].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }
        
        // 39: MultDoubleInt ( PrepareALUDoubleProcess, MultDoubleIntProcess, StackPushDouble )
        public static final void MultDoubleInt()
        {
            ProcessList.processList[PROCESS_PREPAREALUDOUBLE].runProcess();
            ProcessList.processList[PROCESS_MULTDOUBLEINT].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHDOUBLE].runProcess();
        }

        // 40: MultSingleFloat ( PrepareALUSingleProcess, MultSingleFloatProcess, StackPushSingle )
        public static final void MultSingleFloat()
        {
            ProcessList.processList[PROCESS_PREPAREALUSINGLE].runProcess();
            ProcessList.processList[PROCESS_MULTSINGLEFLOAT].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }
        
        // 41: MultDoubleFloat ( PrepareALUDoubleProcess, MultDoubleFloatProcess, StackPushDouble )
        public static final void MultDoubleFloat()
        {
            ProcessList.processList[PROCESS_PREPAREALUDOUBLE].runProcess();
            ProcessList.processList[PROCESS_MULTDOUBLEFLOAT].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHDOUBLE].runProcess();
        }


        /////////
        // DIV //
        /////////
        
        // 42: DivSingleInt ( PrepareALUSingleProcess, DivSingleIntProcess, StackPushSingle )
        public static final void DivSingleInt()
        {
            ProcessList.processList[PROCESS_PREPAREALUSINGLE].runProcess();
            ProcessList.processList[PROCESS_DIVSINGLEINT].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }
        
        // 43: DivDoubleInt ( PrepareALUDoubleProcess, DivDoubleIntProcess, StackPushDouble )
        public static final void DivDoubleInt()
        {
            ProcessList.processList[PROCESS_PREPAREALUDOUBLE].runProcess();
            ProcessList.processList[PROCESS_DIVDOUBLEINT].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHDOUBLE].runProcess();
        }

        // 44: DivSingleFloat ( PrepareALUSingleProcess, DivSingleFloatProcess, StackPushSingle )
        public static final void DivSingleFloat()
        {
            ProcessList.processList[PROCESS_PREPAREALUSINGLE].runProcess();
            ProcessList.processList[PROCESS_DIVSINGLEFLOAT].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }
        
        // 45: DivDoubleFloat ( PrepareALUDoubleProcess, DivDoubleFloatProcess, StackPushDouble )
        public static final void DivDoubleFloat()
        {
            ProcessList.processList[PROCESS_PREPAREALUDOUBLE].runProcess();
            ProcessList.processList[PROCESS_DIVDOUBLEFLOAT].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHDOUBLE].runProcess();
        }
        
        public static final void RemainderInt()
        {
            ProcessList.processList[PROCESS_PREPAREALUSINGLE].runProcess();
            ProcessList.processList[PROCESS_REMAINDERINT].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }

        public static final void NegateInt()
        {
            ProcessList.processList[PROCESS_STACKPOPSINGLE].runProcess();
            ProcessList.processList[PROCESS_NEGATEINT].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }

        public static final void NegateFloat()
        {
            ProcessList.processList[PROCESS_STACKPOPSINGLE].runProcess();
            JVMRuntime.value = Float.floatToIntBits(-Float.intBitsToFloat(JVMRuntime.value));
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }

        /**
         * ShiftLeftInt
         *
         * Implements ISHL instruction.
         *
         * Register usage:
         *    Input: VALUE1, VALUE2
         *    Output: VALUE (result)
         *
         * value = value1 << value2
         */
        public static final void ShiftLeftInt()
        {
            ProcessList.processList[PROCESS_PREPAREALUSINGLE].runProcess();
            JVMRuntime.value = JVMRuntime.value1 << JVMRuntime.value2;
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }

        /**
         * ShiftRightInt
         *
         * Implements ISHR instruction.
         *
         * Register usage:
         *    Input: VALUE1, VALUE2
         *    Output: VALUE (result)
         *
         * value = value1 >> value2 with sign extension
         */
        public static final void ShiftRightInt()
        {
            ProcessList.processList[PROCESS_PREPAREALUSINGLE].runProcess();
            JVMRuntime.value = JVMRuntime.value1 >> JVMRuntime.value2; // Arithmetic shift
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }

        /**
         * ShiftRightLogicalInt
         *
         * Implements IUSHR instruction.
         *
         * Register usage:
         *    Input: VALUE1, VALUE2
         *    Output: VALUE (result)
         *
         * value = value1 >> value2 with zero extension
         */
        public static final void ShiftRightLogicalInt()
        {
            ProcessList.processList[PROCESS_PREPAREALUSINGLE].runProcess();
            JVMRuntime.value = JVMRuntime.value1 >>> JVMRuntime.value2; // Logical shift
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }

        /**
         * AndInt
         *
         * Implements IAND instruction.
         *
         * Register usage:
         *    Input: VALUE1, VALUE2
         *    Output: VALUE (result)
         *
         * value = value1 & value2
         */
        public static final void AndInt()
        {
            ProcessList.processList[PROCESS_PREPAREALUSINGLE].runProcess();
            JVMRuntime.value = JVMRuntime.value1 & JVMRuntime.value2;
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }

        /**
         * OrInt
         *
         * Implements IOR instruction.
         *
         * Register usage:
         *    Input: VALUE1, VALUE2
         *    Output: VALUE (result)
         *
         * value = value1 | value2
         */
        public static final void OrInt()
        {
            ProcessList.processList[PROCESS_PREPAREALUSINGLE].runProcess();
            JVMRuntime.value = JVMRuntime.value1 | JVMRuntime.value2;
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }

        /**
         * XorInt
         *
         * Implements IXOR instruction.
         *
         * Register usage:
         *    Input: VALUE1, VALUE2
         *    Output: VALUE (result)
         *
         * value = value1 ^ value2
         */
        public static final void XorInt()
        {
            ProcessList.processList[PROCESS_PREPAREALUSINGLE].runProcess();
            JVMRuntime.value = JVMRuntime.value1 ^ JVMRuntime.value2;
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }


        
       
        // 72: BranchCompareSingleIntEQ
        public static final void BranchCompareSingleIntEQ()
        {
            ProcessList.processList[PROCESS_COMPARESINGLEINTPOP2].runProcess();
            ProcessList.processList[PROCESS_COMPARESINGLEINTEQ].runProcess();
            ProcessList.processList[PROCESS_COMPAREBRANCH].runProcess();
        }
        
        // 73: BranchCompareSingleIntNE
        public static final void BranchCompareSingleIntNE()
        {
            ProcessList.processList[PROCESS_COMPARESINGLEINTPOP2].runProcess();
            ProcessList.processList[PROCESS_COMPARESINGLEINTNE].runProcess();
            ProcessList.processList[PROCESS_COMPAREBRANCH].runProcess();
        }
        
        // 74: BranchCompareSingleIntLT
        public static final void BranchCompareSingleIntLT()
        {
            ProcessList.processList[PROCESS_COMPARESINGLEINTPOP2].runProcess();
            ProcessList.processList[PROCESS_COMPARESINGLEINTLT].runProcess();
            ProcessList.processList[PROCESS_COMPAREBRANCH].runProcess();
        }
        
        // 75: BranchCompareSingleIntGE
        public static final void BranchCompareSingleIntGE()
        {
            ProcessList.processList[PROCESS_COMPARESINGLEINTPOP2].runProcess();
            ProcessList.processList[PROCESS_COMPARESINGLEINTGE].runProcess();
            ProcessList.processList[PROCESS_COMPAREBRANCH].runProcess();
        }

        // 76: BranchCompareSingleIntGT
        public static final void BranchCompareSingleIntGT()
        {
            ProcessList.processList[PROCESS_COMPARESINGLEINTPOP2].runProcess();
            ProcessList.processList[PROCESS_COMPARESINGLEINTGT].runProcess();
            ProcessList.processList[PROCESS_COMPAREBRANCH].runProcess();
        }

        // 77: BranchCompareSingleIntLE
        public static final void BranchCompareSingleIntLE()
        {
            ProcessList.processList[PROCESS_COMPARESINGLEINTPOP2].runProcess();
            ProcessList.processList[PROCESS_COMPARESINGLEINTLE].runProcess();
            ProcessList.processList[PROCESS_COMPAREBRANCH].runProcess();
        }

        // 78: BranchCompareSingleIntEQZero
        public static final void BranchCompareSingleIntEQZero()
        {
            ProcessList.processList[PROCESS_COMPARESINGLEINTPOP0].runProcess();
            ProcessList.processList[PROCESS_COMPARESINGLEINTEQ].runProcess();
            ProcessList.processList[PROCESS_COMPAREBRANCH].runProcess();
        }

        // 79: BranchCompareSingleIntNEZero
        public static final void BranchCompareSingleIntNEZero()
        {
            ProcessList.processList[PROCESS_COMPARESINGLEINTPOP0].runProcess();
            ProcessList.processList[PROCESS_COMPARESINGLEINTNE].runProcess();
            ProcessList.processList[PROCESS_COMPAREBRANCH].runProcess();
        }

        // 80: BranchCompareSingleIntLTZero
        public static final void BranchCompareSingleIntLTZero()
        {
            ProcessList.processList[PROCESS_COMPARESINGLEINTPOP0].runProcess();
            ProcessList.processList[PROCESS_COMPARESINGLEINTLT].runProcess();
            ProcessList.processList[PROCESS_COMPAREBRANCH].runProcess();
        }

        // 81: BranchCompareSingleIntGEZero
        public static final void BranchCompareSingleIntGEZero()
        {
            ProcessList.processList[PROCESS_COMPARESINGLEINTPOP0].runProcess();
            ProcessList.processList[PROCESS_COMPARESINGLEINTGE].runProcess();
            ProcessList.processList[PROCESS_COMPAREBRANCH].runProcess();
        }

        // 82: BranchCompareSingleIntGTZero
        public static final void BranchCompareSingleIntGTZero()
        {
            ProcessList.processList[PROCESS_COMPARESINGLEINTPOP0].runProcess();
            ProcessList.processList[PROCESS_COMPARESINGLEINTGT].runProcess();
            ProcessList.processList[PROCESS_COMPAREBRANCH].runProcess();
        }
        
        // 83: BranchCompareSingleIntLEZero
        public static final void BranchCompareSingleIntLEZero()
        {
            ProcessList.processList[PROCESS_COMPARESINGLEINTPOP0].runProcess();
            ProcessList.processList[PROCESS_COMPARESINGLEINTLE].runProcess();
            ProcessList.processList[PROCESS_COMPAREBRANCH].runProcess();
        }
        
        // 84: BranchCompareSingleIntEQNull
        public static final void BranchCompareSingleIntEQNull()
        {
            ProcessList.processList[PROCESS_COMPARESINGLEINTPOPNULL].runProcess();
            ProcessList.processList[PROCESS_COMPARESINGLEINTEQ].runProcess();
            ProcessList.processList[PROCESS_COMPAREBRANCH].runProcess();
        }

        // 85: BranchCompareSingleIntNENull
        public static final void BranchCompareSingleIntNENull()
        {
            ProcessList.processList[PROCESS_COMPARESINGLEINTPOPNULL].runProcess();
            ProcessList.processList[PROCESS_COMPARESINGLEINTNE].runProcess();
            ProcessList.processList[PROCESS_COMPAREBRANCH].runProcess();
        }

        public static final void BranchCompareFloatLT()
        {
            ProcessList.processList[PROCESS_COMPARESINGLEINTPOP2].runProcess();
            float value1 = Float.intBitsToFloat(JVMRuntime.value1);
            float value2 = Float.intBitsToFloat(JVMRuntime.value2);
            if (value1 < value2) {
                JVMRuntime.value = -1;
            } else if (value1 > value2) {
                JVMRuntime.value = 1;
            } else {
                JVMRuntime.value = 0;
            }
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }

        public static final void BranchCompareFloatGT()
        {
            ProcessList.processList[PROCESS_COMPARESINGLEINTPOP2].runProcess();
            float value1 = Float.intBitsToFloat(JVMRuntime.value1);
            float value2 = Float.intBitsToFloat(JVMRuntime.value2);
            if (value1 < value2) {
                JVMRuntime.value = -1;
            } else if (value1 > value2) {
                JVMRuntime.value = 1;
            } else {
                JVMRuntime.value = 0;
            }

            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();
        }




        /**
         * Lookupswitch
         *
         * Notes:
         *   The ClassPackager guarantees methods start on 4-byte boundaries,
         *   therefore defaultbyte1 will start on a 4-byte boundary.
         */
        public static final void LookupSwitch()
        {

            // Compute the number of padbytes, and store that value.
            // Move the PC to the start of defaultbyte1.
            // Load defaultbyte and npairs.
            // pop key off stack.
            // move PC to start of math-offset pairs.
            // do split search:
            //    compare key to middle value of pairs.
            //    if key greater, compare key to middle of upper half
            //    if key lower, compare key to middle of lower half
            //

            // Duplicate PC - and move duplicate to start of defaultbyte1
            // Move the PC to the opcode so we can calculate the
            // branch target which is referenced to the lookupswitch opcode.
            JVMRuntime.index = JVMRuntime.pc;

            // There is always a branch taken, PC is auto incremented by IEE.
            JVMRuntime.pc -= 1;

            // Check if PC is not already a multiple of 4 bytes.
            // If not, add 4 then clear the 2 LSBs to get to the next 4 byte boundary.
            if ((JVMRuntime.index & 0x3) != 0)
            {
                JVMRuntime.index += 4; // Add 4
                JVMRuntime.index &= 0xfffffffc; // clear 2 LSB
            }

            // We are at defaultbyte1.
            // HANDLE = defaultbyte
            // VALUE2 = npairs
            JVMRuntime.handle = MemoryController.readWord(JVMRuntime.index);
            JVMRuntime.value2 = MemoryController.readWord(JVMRuntime.index + 4);

            // Increment temp PC to start of match-pairs
            JVMRuntime.index += 8;
            
            // Pop key off into VALUE
            ProcessList.processList[PROCESS_STACKPOPSINGLE].runProcess();

            // Binary search algorithm:
            //    bool jw_search ( int *list, int size, int key, int*& rec )
            //    {
            //      bool found = false;
            //      int low = 0, high = size - 1;
            //
            //      while ( high >= low ) {
            //        int mid = ( low + high ) / 2;
            //        if ( key < list[mid] )
            //          high = mid - 1;
            //        else if ( key > list[mid] )
            //          low = mid + 1;
            //        else {
            //          found = true;
            //          rec = &list[mid];
            //          break;
            //        }
            //      }
            //      return found;
            //    }

            // value2 = high
            // cphandle = mid
            // value1 = low
            // tablehandle = temp
            JVMRuntime.value1 = 0;    // low = 0
            JVMRuntime.value2 -= 1; // high = size - 1
            while (JVMRuntime.value2 >= JVMRuntime.value1)
            {
                JVMRuntime.cphandle = (JVMRuntime.value1 + JVMRuntime.value1) / 2; // mid = (high + low) / 2

                // There are 2 words per list entry:
                //  match, then offset.  So double the 'mid' (list index) to get the value in the list.
                //  Also remember to multiply by 4 to get word-aligned indexes.
                JVMRuntime.tablehandle = MemoryController.readWord(JVMRuntime.index + (JVMRuntime.cphandle * 4 * 2)); // list[mid]

                if (JVMRuntime.value < JVMRuntime.tablehandle) // key < list[mid]
                {
                    JVMRuntime.value2 = JVMRuntime.cphandle - 1; // high = mid - 2;
                }
                else if (JVMRuntime.value > JVMRuntime.tablehandle)
                {
                    JVMRuntime.value1 = JVMRuntime.cphandle + 1; // low = mid + 2;
                }
                else
                {
                    // Found.
                    // Take the branch from the match offset
                    JVMRuntime.index += 4; // shift up the base by one word to access the 'offset'
                    JVMRuntime.value = MemoryController.readWord(JVMRuntime.index + (JVMRuntime.cphandle * 4 * 2)); // list[mid+1]
                    JVMRuntime.pc = JVMRuntime.pc + JVMRuntime.value;
                    return;
                }
            }

            // Not Found.
            // Take the default branch from the match offset
            JVMRuntime.pc = JVMRuntime.pc + JVMRuntime.handle;
            return;
        }

        public static final void TableSwitch()
        {
            // Duplicate PC - and move duplicate to start of defaultbyte1
            // Move the PC to the opcode so we can calculate the
            // branch target which is referenced to the lookupswitch opcode.
            JVMRuntime.index = JVMRuntime.pc;

            // There is always a branch taken, PC is auto incremented by IEE.
            JVMRuntime.pc -= 1;

            // Check if PC is not already a multiple of 4 bytes.
            // If not, add 4 then clear the 2 LSBs to get to the next 4 byte boundary.
            if ((JVMRuntime.index & 0x3) != 0)
            {
                JVMRuntime.index += 4; // Add 4
                JVMRuntime.index &= 0xfffffffc; // clear 2 LSB
            }

            // We are at defaultbyte1.
            // HANDLE = defaultbyte
            // CPHANDLE = low
            // TABLEHANDLE = high
            JVMRuntime.handle = MemoryController.readWord(JVMRuntime.index);
            JVMRuntime.cphandle = MemoryController.readWord(JVMRuntime.index + 4);
            JVMRuntime.tablehandle = MemoryController.readWord(JVMRuntime.index + 8);


            // Pop index off the stack
            ProcessList.processList[PROCESS_STACKPOPSINGLE].runProcess();

            // Increment temp PC to start of jump offsets
            JVMRuntime.index += 12;

            if (JVMRuntime.value < JVMRuntime.cphandle ||
                JVMRuntime.value > JVMRuntime.tablehandle)
            {
                // Take the default branch
                JVMRuntime.pc = JVMRuntime.pc + JVMRuntime.handle;
            }
            else
            {
                // Load in the offset for the index
                JVMRuntime.value1 = MemoryController.readWord(JVMRuntime.index + ((JVMRuntime.value - JVMRuntime.cphandle) * 4));
                JVMRuntime.pc = JVMRuntime.pc + JVMRuntime.value1;
            }
        }

        
        //{ }, { }, { }, { },
        public static final void LoadConstantPool()
        {
            ProcessList.processList[PROCESS_LOADCONSTANTPOOL].runProcess(); // Get the constant
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();  // push it onto the stack.
        }
        
        //{ }, { }, { }, { }, { }, { },
        //{ }, { }, { }, { },
        
        // 100: ThreadStart ( ThreadRunReference, SetupMethodLookup, SetupVirtualLookup, ObjectClassLookup,
        //                    TableLookup, FrameInfoLookup, AllocateMemory, ThreadRunSetup,
        //                    EligibleQueueInsert )
        public static final void ThreadStart()
        {
            ProcessList.processList[PROCESS_THREADRUNREFERENCE].runProcess();  // manually set reference registers for the run() method
            //ProcessList.processList[PROCESS_LOCALGETSINGLE].runProcess();      // 'this' -> Value -- not needs as ObjectClassLookup fetch
            //ProcessList.processList[PROCESS_OBJECTCLASSLOOKUP].runProcess();   // Value -> classhandle
            ProcessList.processList[PROCESS_SETUPMETHODLOOKUP].runProcess();
            ProcessList.processList[PROCESS_SETUPVIRTUALLOOKUP].runProcess();
            ProcessList.processList[PROCESS_TABLELOOKUP].runProcess();
            ProcessList.processList[PROCESS_FRAMEINFOLOOKUP].runProcess();
            ProcessList.processList[PROCESS_ALLOCATEMEMORY].runProcess();
            ProcessList.processList[PROCESS_THREADRUNSETUP].runProcess();
            ProcessList.processList[PROCESS_ELIGIBLEQUEUEINSERT].runProcess();
        }
        
        // 101: ThreadSleep ( LocalGetSingleAlt, TimerLoad, SubSingleInt, ThreadSetWakeupTime, 
        //                    EligibleQueueUnlink, WaitQueueInsert, EligibleQueueProcess )
        public static final void ThreadSleep()
        {
            ProcessList.processList[PROCESS_LOCALGETSINGLEALT].runProcess();
            ProcessList.processList[PROCESS_TIMERLOAD].runProcess();
            ProcessList.processList[PROCESS_SUBSINGLEINT].runProcess();
            ProcessList.processList[PROCESS_THREADSETWAKEUPTIME].runProcess();
            ProcessList.processList[PROCESS_ELIGIBLEQUEUEUNLINK].runProcess();
            ProcessList.processList[PROCESS_WAITQUEUEINSERT].runProcess();
            ProcessList.processList[PROCESS_ELIGIBLEQUEUEPROCESS].runProcess();   // find a new thread to run
        }
        
        // 102: InvokeScheduler --> Not used directly.  Scheduler is performed by WaitQueueProcess
        //{ },
        
        // 103: Wait
        //{ },
        
        // 104: Notify
        //{ },
        
        // 105: NotifyAll
        //{ },
        
        // 106: EligibleQueueInsert
        public static final void EligibleQueueInsert()
        {
            ProcessList.processList[PROCESS_ELIGIBLEQUEUEINSERT].runProcess();
        }
        
        // 107: EligibleQueueProcess
        public static final void EligibleQueueProcess()
        {
            ProcessList.processList[PROCESS_ELIGIBLEQUEUEPROCESS].runProcess();
        }
        
        // 108: WaitQueueProcess
        public static final void WaitQueueProcess()
        {
            ProcessList.processList[PROCESS_WAITQUEUEPROCESS].runProcess();
        }
        
        // The scheduler.  Composed of: 
        //    1. WaitQueueProcess     - unlink expired Threads
        //    2. EligibleQueueInsert  - place them into eligible queue
        //    3. EligibleQueueProcess - perform context switch if necessary
        // 
        // The scheduler is called by 'yield' directly, and 'sleep' after inserting
        // into wait queue.
        //
        //  The 'index' register is used as a return value from WaitQueueProcess
        //  to determine if the EligibleQueueInsert must be run.
        //
        public static void Scheduler()
        {
            ProcessList.processList[PROCESS_WAITQUEUEPROCESS].runProcess();
            while( JVMRuntime.index == 1 )
            {
                ProcessList.processList[PROCESS_ELIGIBLEQUEUEINSERT].runProcess();
                ProcessList.processList[PROCESS_WAITQUEUEPROCESS].runProcess();
            }
            
            ProcessList.processList[PROCESS_ELIGIBLEQUEUEPROCESS].runProcess();
            
        }
        
        // null
        //{ },
        
        // 110: InterruptProcess: Place Thread at the specified interrupt vector in the eligible queue
        public static final void InterruptProcess()
        {
            ProcessList.processList[PROCESS_INTERRUPT_VECTOR_RETRIEVE].runProcess();
            ProcessList.processList[PROCESS_ELIGIBLEQUEUEINSERT].runProcess();
            ProcessList.processList[PROCESS_ELIGIBLEQUEUEPROCESS].runProcess();
        }
        
        // 111: InterruptStart ( ThreadRunReference, SetupMethodLookup, SetupVirtualLookup, ObjectClassLookup,
        //                       TableLookup, FrameInfoLookup, AllocateMemory, ThreadRunSetup,
        //                       EligibleQueueInsert )
        public static final void InterruptStart()
        {
            ProcessList.processList[PROCESS_THREADRUNREFERENCE].runProcess();    // manually set reference registers for the run() method
            ProcessList.processList[PROCESS_LOCALGETSINGLE].runProcess();        // 'thread_' -> Value (first argument of setInterruptHandler())
            ProcessList.processList[PROCESS_OBJECTCLASSLOOKUP].runProcess();     // Value -> Classhandle
            ProcessList.processList[PROCESS_SETUPMETHODLOOKUP].runProcess();
            ProcessList.processList[PROCESS_SETUPVIRTUALLOOKUP].runProcess();
            ProcessList.processList[PROCESS_TABLELOOKUP].runProcess();
            ProcessList.processList[PROCESS_FRAMEINFOLOOKUP].runProcess();
            ProcessList.processList[PROCESS_ALLOCATEMEMORY].runProcess();
            ProcessList.processList[PROCESS_THREADRUNSETUP].runProcess();
            ProcessList.processList[PROCESS_INTERRUPT_VECTOR_SET].runProcess();  // Value -> vector table[Index]
                    
        }
        
        //{ }, { }, { }, { }, { }, { }, { }, { }, 
        
        // 120:  ARRAY_GET_SINGLE ( ArrayAccessSetup, ArrayGetSingle, StackPushSingle )
        public static final void ArrayGetSingle()
        {
            ProcessList.processList[PROCESS_ARRAYACCESSSETUP].runProcess();
            ProcessList.processList[PROCESS_ARRAYGETSINGLE].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();   // push value from array
        }
        //{ }, { }, { }, { },

        public static final void ArrayGetByte()
        {
            ProcessList.processList[PROCESS_ARRAYACCESSSETUP].runProcess();
            ProcessList.processList[PROCESS_ARRAYGETBYTE].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();   // push value from array
        }

        public static final void ArrayGetChar()
        {
            ProcessList.processList[PROCESS_ARRAYACCESSSETUP].runProcess();
            ProcessList.processList[PROCESS_ARRAYGETCHAR].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();   // push value from array
        }

        public static final void ArrayGetShort()
        {
            ProcessList.processList[PROCESS_ARRAYACCESSSETUP].runProcess();
            ProcessList.processList[PROCESS_ARRAYGETSHORT].runProcess();
            ProcessList.processList[PROCESS_STACKPUSHSINGLE].runProcess();   // push value from array
        }

        // 125:  ARRAY_PUT_SINGLE ( StackPopSingle, ArrayAccessSetup, ArrayPutSingle )
        public static final void ArrayPutSingle()
        {
            ProcessList.processList[PROCESS_STACKPOPSINGLE].runProcess();  // get value
            ProcessList.processList[PROCESS_ARRAYACCESSSETUP].runProcess(); // get index, arrayref
            ProcessList.processList[PROCESS_ARRAYPUTSINGLE].runProcess();
        }

        public static final void ArrayPutByte()
        {
            ProcessList.processList[PROCESS_STACKPOPSINGLE].runProcess();  // get value
            ProcessList.processList[PROCESS_ARRAYACCESSSETUP].runProcess(); // get index, arrayref
            ProcessList.processList[PROCESS_ARRAYPUTBYTE].runProcess();
        }

        public static final void ArrayPutChar()
        {
            ProcessList.processList[PROCESS_STACKPOPSINGLE].runProcess();  // get value
            ProcessList.processList[PROCESS_ARRAYACCESSSETUP].runProcess(); // get index, arrayref
            ProcessList.processList[PROCESS_ARRAYPUTCHAR].runProcess();
        }

        public static final void ArrayPutShort()
        {
            ProcessList.processList[PROCESS_STACKPOPSINGLE].runProcess();  // get value
            ProcessList.processList[PROCESS_ARRAYACCESSSETUP].runProcess(); // get index, arrayref
            ProcessList.processList[PROCESS_ARRAYPUTSHORT].runProcess();
        }
      
    
    /** Creates a new instance of InstructionHandlers */
    public InstructionHandlers() {
    }
    
}
