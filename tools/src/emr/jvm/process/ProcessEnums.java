/*
 * ProcessEnums.java
 *
 * Created on January 10, 2008, 11:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package emr.jvm.process;

/**
 *
 * @author Evan Ross
 */
public interface ProcessEnums {
    
    
    
    //////////////////////////////////////////////////////////////////////////////////////
    //
    //    The following are the enumerations references each individual process
    //    referenced by an instruction handler.
    //
    //////////////////////////////////////////////////////////////////////////////////////
    
    public static final int PROCESS_CLASSLOOKUP        = 0;
    public static final int PROCESS_FIELDLOOKUP        = 1;
    public static final int PROCESS_TABLELOOKUP        = 2;
    public static final int PROCESS_REFERENCELOOKUP    = 3;  
    
    public static final int PROCESS_ALLOCATEMEMORY     = 4;        
    public static final int PROCESS_DEALLOCATEMEMORY   = 5;
    
    public static final int PROCESS_IEE                = 6;
    
    public static final int PROCESS_FRAMEINFOLOOKUP    = 7;        
    public static final int PROCESS_CLASSINFOLOOKUP    = 8;        
    public static final int PROCESS_OBJECTCLASSLOOKUP  = 9;
    public static final int PROCESS_OBJECTSIZELOOKUP   = 10;        
    
    public static final int PROCESS_FRAMESETUP         = 11;       
    public static final int PROCESS_FRAMETEARDOWN      = 12;        
    
    public static final int PROCESS_STACKPEEKSINGLE    = 13;        
    public static final int PROCESS_STACKPEEKDOUBLE    = 14;        
    public static final int PROCESS_STACKPUSHSINGLE    = 15;        
    public static final int PROCESS_STACKPOPSINGLE     = 16;   
    public static final int PROCESS_STACKPUSHDOUBLE    = 17;        
    public static final int PROCESS_STACKPOPDOUBLE     = 18;        
    
    public static final int PROCESS_LOCALGETSINGLE     = 19;   
    public static final int PROCESS_LOCALPUTSINGLE     = 20;   
    public static final int PROCESS_LOCALGETDOUBLE     = 21;   
    public static final int PROCESS_LOCALPUTDOUBLE     = 22;
    
    public static final int PROCESS_OBJECTSETUP        = 23;       
    
    public static final int PROCESS_SETUPFIELDLOOKUP   = 24;
    public static final int PROCESS_SETUPMETHODLOOKUP  = 25;
    public static final int PROCESS_SETUPSTATICLOOKUP  = 26;
    public static final int PROCESS_SETUPVIRTUALLOOKUP = 27;        
    public static final int PROCESS_SETPC              = 28;
    
    public static final int PROCESS_OBJECTGETFIELD     = 29;
    public static final int PROCESS_OBJECTPUTFIELD     = 30;    
    public static final int PROCESS_FIELDSTACKPUSH     = 31;        
    public static final int PROCESS_FIELDSTACKPOP      = 32;        
    public static final int PROCESS_STACKPOPOBJECTREF  = 33;        
    public static final int PROCESS_FIELDGETINFO       = 34;
    
    public static final int PROCESS_ARRAYSIZELOOKUP    = 35;
    public static final int PROCESS_ARRAYSETUP         = 36;
    public static final int PROCESS_ARRAYLENGTHGET     = 37;
    
    
    // COMPUTATIONAL
    public static final int PROCESS_PREPAREALUSINGLE   = 39;        
    public static final int PROCESS_PREPAREALUDOUBLE   = 40; 
    
    // ADD
    public static final int PROCESS_ADDSINGLEINT       = 41;        
    public static final int PROCESS_ADDDOUBLEINT       = 42;        
    public static final int PROCESS_ADDSINGLEFLOAT     = 43;
    public static final int PROCESS_ADDDOUBLEFLOAT     = 44;
    
    // SUB
    public static final int PROCESS_SUBSINGLEINT       = 45;        
    public static final int PROCESS_SUBDOUBLEINT       = 46;        
    public static final int PROCESS_SUBSINGLEFLOAT     = 47;
    public static final int PROCESS_SUBDOUBLEFLOAT     = 48;
    
    // MULT
    public static final int PROCESS_MULTSINGLEINT       = 49;        
    public static final int PROCESS_MULTDOUBLEINT       = 50;        
    public static final int PROCESS_MULTSINGLEFLOAT     = 51;
    public static final int PROCESS_MULTDOUBLEFLOAT     = 52;
     
    // DIV
    public static final int PROCESS_DIVSINGLEINT       = 53;        
    public static final int PROCESS_DIVDOUBLEINT       = 54;        
    public static final int PROCESS_DIVSINGLEFLOAT     = 55;
    public static final int PROCESS_DIVDOUBLEFLOAT     = 56;
    
    // Remainder
    public static final int PROCESS_REMAINDERINT       = 57;
    public static final int PROCESS_REMAINDERFLOAT     = 58;
    public static final int PROCESS_REMAINDERDOUBLE    = 59;
    public static final int PROCESS_REMAINDERLONG      = 60;

    // Negate
    public static final int PROCESS_NEGATEINT          = 61;
    public static final int PROCESS_NEGATEFLOAT        = 62;
    public static final int PROCESS_NEGATEDOUBLE       = 63;
    public static final int PROCESS_NEGATELONG         = 64;

    // Shift
    public static final int PROCESS_SHIFTLEFTINT       = 65;
    public static final int PROCESS_SHIFTLEFTLONG      = 66;
    public static final int PROCESS_SHIFTRIGHTINT      = 67;
    public static final int PROCESS_SHIFTRIGHTLONG     = 68;
    
    
    /////////////////////////// 
    // Other stuff that couldn't fit above
    
    public static final int PROCESS_STACKDUPLICATEXSINGLE = 80;

    
    ////////////////////////////
    // Branch compare processes
    ////////////////////////////
    public static final int PROCESS_COMPARESINGLEINTPOP2    = 83;
    public static final int PROCESS_COMPARESINGLEINTPOP0    = 84;
    public static final int PROCESS_COMPARESINGLEINTPOPNULL = 85;
    
    public static final int PROCESS_COMPARESINGLEINTEQ    = 86;
    public static final int PROCESS_COMPARESINGLEINTNE    = 87;
    public static final int PROCESS_COMPARESINGLEINTLT    = 88;
    public static final int PROCESS_COMPARESINGLEINTGE    = 89;
    public static final int PROCESS_COMPARESINGLEINTGT    = 90;
    public static final int PROCESS_COMPARESINGLEINTLE    = 91;
    public static final int PROCESS_COMPAREBRANCH         = 92;
    
    
    public static final int PROCESS_LOADCONSTANTPOOL       = 95;
    public static final int PROCESS_LOADCONSTANTPOOLWIDE       = 96;
    public static final int PROCESS_LOADCONSTANTPOOLWIDEDOUBLE   = 97;
    public static final int PROCESS_LOCALGETSINGLEALT      = 98;
    
    ////////////////////////////
    // System processes - Thread
    ////////////////////////////
    public static final int PROCESS_ELIGIBLEQUEUEINSERT  = 99;
    public static final int PROCESS_ELIGIBLEQUEUEUNLINK  = 100;
    public static final int PROCESS_ELIGIBLEQUEUEPROCESS = 101;
    
    public static final int PROCESS_WAITQUEUEINSERT      = 102;
    public static final int PROCESS_WAITQUEUEPROCESS     = 103;

    public static final int PROCESS_TIMERLOAD            = 104;
    public static final int PROCESS_THREADSETWAKEUPTIME  = 105;
    public static final int PROCESS_THREADRUNSETUP       = 106;
    public static final int PROCESS_THREADRUNREFERENCE   = 107;
    
    
    public static final int PROCESS_INTERRUPT_VECTOR_RETRIEVE = 109;
    public static final int PROCESS_INTERRUPT_VECTOR_SET = 110;
   
    ///////////////////////////////
    //  Array Processes
    ///////////////////////////////
    public static final int PROCESS_ARRAYACCESSSETUP       = 118;
    public static final int PROCESS_ARRAYGETSINGLE         = 119;
    public static final int PROCESS_ARRAYGETDOUBLE         = 120;
    public static final int PROCESS_ARRAYGETCHAR           = 121;
    public static final int PROCESS_ARRAYGETBYTE           = 122;
    public static final int PROCESS_ARRAYGETSHORT          = 123;
    
    public static final int PROCESS_ARRAYPUTSINGLE         = 124;
    public static final int PROCESS_ARRAYPUTDOUBLE         = 125;
    public static final int PROCESS_ARRAYPUTCHAR           = 126;
    public static final int PROCESS_ARRAYPUTBYTE           = 127;
    public static final int PROCESS_ARRAYPUTSHORT          = 128;
    
    
    
    //////////////////////////////////////////////////////////////////////////////////////
    //
    //    The following are the process sequence enumerations.  These are
    //    referenced directly by the instruction execution engine.
    //
    //////////////////////////////////////////////////////////////////////////////////////

    /* 
     * Enumerated constants used by requestors to request a specific process.  These constants represent
     * the public interface between the ProcessManager and process requestors (typically the instruction
     * execution engine)
     */

    public static final int NULL_PROCESS        = 0;
    
    public static final int FIELD_LOOKUP        = 1;
    public static final int METHOD_LOOKUP       = 2;
    public static final int REFERENCE_LOOKUP    = 3;
    
    public static final int GETFIELD            = 4;
    public static final int PUTFIELD            = 5;
    
    public static final int STARTUP_PROCESS     = 6;
    public static final int FETCH_INSTRUCTION   = 7;
    public static final int NEW_INSTANCE        = 8;
    
    public static final int INVOKE_STATIC       = 9;
    public static final int INVOKE_VIRTUAL      = 10;
    public static final int INVOKE_SPECIAL      = 11;  
    public static final int INVOKE_INTERFACE    = 12;
    
    public static final int STACK_DUP_SINGLE    = 13;
    
    public static final int LOAD_SINGLE         = 14;
    public static final int STORE_SINGLE        = 15;
    public static final int LOAD_DOUBLE         = 16;
    public static final int STORE_DOUBLE        = 17;
    public static final int RETURN_SINGLE       = 18;
    public static final int RETURN_DOUBLE       = 19;
    public static final int RETURN_VOID         = 20;
    
    public static final int CONST_STORE_SINGLE  = 21;
    public static final int CONST_STORE_DOUBLE  = 22;
    
    public static final int STACK_POP_SINGLE    = 23;
    public static final int STACK_POP_DOUBLE    = 24;
    public static final int STACK_PUSH_SINGLE   = 25;
    public static final int STACK_PUSH_DOUBLE   = 26;
    public static final int INCREMENT_SINGLE_INT = 27;
    
    public static final int GET_ARRAY_LENGTH    = 28;
    public static final int NEW_ARRAY           = 29;
    
    public static final int ADD_SINGLE_INT      = 30;
    public static final int ADD_DOUBLE_INT      = 31;
    public static final int ADD_SINGLE_FLOAT    = 32;
    public static final int ADD_DOUBLE_FLOAT    = 33;
    
    public static final int SUB_SINGLE_INT      = 34;
    public static final int SUB_DOUBLE_INT      = 35;
    public static final int SUB_SINGLE_FLOAT    = 36;
    public static final int SUB_DOUBLE_FLOAT    = 37;

    public static final int MULT_SINGLE_INT     = 38;
    public static final int MULT_DOUBLE_INT     = 39;
    public static final int MULT_SINGLE_FLOAT   = 40;
    public static final int MULT_DOUBLE_FLOAT   = 41;
    
    public static final int DIV_SINGLE_INT      = 42;
    public static final int DIV_DOUBLE_INT      = 43;
    public static final int DIV_SINGLE_FLOAT    = 44;
    public static final int DIV_DOUBLE_FLOAT    = 45;
    
    public static final int REMAINDER_INT       = 46;
    public static final int REMAINDER_FLOAT     = 47;
    public static final int REMAINDER_DOUBLE    = 48;
    public static final int REMAINDER_LONG      = 49;
    
    
    ///////////////////////////////
    // Branch compares
    ///////////////////////////////
    public static final int BRANCHCOMPARE_SINGLE_INT_EQ = 72;
    public static final int BRANCHCOMPARE_SINGLE_INT_NE = 73;
    public static final int BRANCHCOMPARE_SINGLE_INT_LT = 74;
    public static final int BRANCHCOMPARE_SINGLE_INT_GE = 75;
    public static final int BRANCHCOMPARE_SINGLE_INT_GT = 76;
    public static final int BRANCHCOMPARE_SINGLE_INT_LE = 77;
    
    public static final int BRANCHCOMPARE_SINGLE_INT_EQ_ZERO = 78;
    public static final int BRANCHCOMPARE_SINGLE_INT_NE_ZERO = 79;
    public static final int BRANCHCOMPARE_SINGLE_INT_LT_ZERO = 80;
    public static final int BRANCHCOMPARE_SINGLE_INT_GE_ZERO = 81;
    public static final int BRANCHCOMPARE_SINGLE_INT_GT_ZERO = 82;
    public static final int BRANCHCOMPARE_SINGLE_INT_LE_ZERO = 83;
    
    public static final int BRANCHCOMPARE_SINGLE_INT_EQ_NULL = 84;
    public static final int BRANCHCOMPARE_SINGLE_INT_NE_NULL = 85;
    
    
    ////////////////////////////////
    // System  management - Threads
    ////////////////////////////////
    public static final int THREAD_START       = 100;
    public static final int THREAD_SLEEP       = 101;
    public static final int INVOKE_SCHEDULER   = 102;
    
    public static final int THREAD_WAIT        = 103;
    public static final int THREAD_NOTIFY      = 104;
    public static final int THREAD_NOTIFY_ALL  = 105;
    
    public static final int ELIGIBLE_QUEUE_INSERT  = 106; // access to atomic process
    public static final int ELIGIBLE_QUEUE_PROCESS = 107; // access to atomic process
    public static final int WAIT_QUEUE_PROCESS     = 108; // access to atomic process
    
    public static final int INTERRUPT_HANDLE   = 110;
    public static final int INTERRUPT_START    = 111;
    
    ////////////////////////////
    // More Array Process Flows
    ////////////////////////////
    public static final int ARRAY_GET_SINGLE   = 120;
    public static final int ARRAY_GET_DOUBLE   = 121;
    public static final int ARRAY_GET_CHAR     = 122;
    public static final int ARRAY_GET_BYTE     = 123;
    public static final int ARRAY_GET_SHORT    = 124;
    
    public static final int ARRAY_PUT_SINGLE   = 125;
    public static final int ARRAY_PUT_DOUBLE   = 126;
    public static final int ARRAY_PUT_CHAR     = 127;
    public static final int ARRAY_PUT_BYTE     = 128;
    public static final int ARRAY_PUT_SHORT    = 129;
        
}
