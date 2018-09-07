/*
 * ProcessList.java
 *
 * Created on January 14, 2008, 10:21 PM
 *
 *
 */

package emr.jvm.process;

import emr.jvm.process.nvm.*;
import emr.jvm.process.ram.*;
import emr.jvm.process.computation.*;
import emr.jvm.process.system.*;

/**
 *
 * @author Evan Ross
 */
public class ProcessList {
    
    /* The list of atomic processes.  The indexes of processes in this list correspond
     * to the bit positions of the process sequence definition words */
    public static final JVMProcess[] processList =
    {                                       //bit
        new ClassLookupProcess(),           // 1   0x001
        null,                               // 2   0x002
        new TableLookupProcess(),           // 3   0x004
        new ReferenceLookupProcess(),       // 4   0x008
        
        new AllocateMemoryProcess(),        // 5   0x010
        new DeallocateMemoryProcess(),      // 6 0x400000

        new InstructionExecutionEngine(),   // 7   0x100
        
        new FrameInfoLookupProcess(),       // 8   0x020
        new ClassInfoLookupProcess(),       // 9  0x800
        new ObjectClassLookupProcess(),     // 10   0x040
        new ObjectSizeLookupProcess(),      // 11  0x400
        new FrameSetupProcess(),            // 12   0x080
        new FrameTeardownProcess(),         // 13 0x200000
        
        new StackPeekSingle(),              // 14  0x1000
        new StackPeekDouble(),              // 15  0x2000
        new StackPushSingle(),              // 16  0x200
        new StackPopSingle(),               // 17  0x4000
        new StackPushDouble(),              // 18  0x8000
        new StackPopDouble(),               // 19 0x10000
        
        new LocalGetSingle(),               // 20 0x20000
        new LocalPutSingle(),               // 21 0x40000
        new LocalGetDouble(),               // 22 0x80000
        new LocalPutDouble(),               // 23 0x100000

        new ObjectSetupProcess(),           // 24 0x800000
        
        new SetupFieldLookupProcess(),     // 25 0x1000000
        new SetupMethodLookupProcess(),    // 26 0x2000000
        new SetupStaticLookupProcess(),    // 27 0x4000000
        new SetupVirtualLookupProcess(),   // 28 0x8000000
        new SetPCProcess(),               // 29 0x10000000
        
        new ObjectGetField(),             // 30 0x20000000
        new ObjectPutField(),             // 31 0x40000000
        new FieldStackPush(),             // 32 0x80000000
        new FieldStackPop(),             // 33 0x100000000
        new StackPopObjectref(),         // 34 0x200000000
        new FieldGetInfo(),              // 35 0x400000000
        
        new ArraySizeLookupProcess(),   // 36
        new ArraySetupProcess(),        // 37
        new ArrayLengthGetProcess(),    // 38
   
        
        null,                         // 39
        
        // Computational Processes
        new PrepareALUSingleProcess(),   // 40
        new PrepareALUDoubleProcess(),   // 41
        
        new AddSingleIntProcess(),       // 42
        new AddDoubleIntProcess(),       // 43
        new AddSingleFloatProcess(),    // 44
        new AddDoubleFloatProcess(),    // 45
        
        new SubSingleIntProcess(),      // 46
        new SubDoubleIntProcess(),      // 47
        new SubSingleFloatProcess(),    // 48
        new SubDoubleFloatProcess(),    // 49

        new MultSingleIntProcess(),     // 50
        new MultDoubleIntProcess(),     // 51
        new MultSingleFloatProcess(),   // 52
        new MultDoubleFloatProcess(),   // 53
        
        new DivSingleIntProcess(),      // 54
        new DivDoubleIntProcess(),      // 55
        new DivSingleFloatProcess(),    // 56
        new DivDoubleFloatProcess(),    // 57
        
        new RemainderIntProcess(),      // 58
        
        // 59 - 80
        null,   // RemainderFloat
        null,   // RemainderDouble
        null,   // RemainderLong
        new NegateIntProcess(),         // 62
        null,   // Negate Long
        null,   // Negate Float
        null,   // Negate Double
        new ShiftLeftInt(),             // 66
        null,
        new ShiftRightInt(),            // 68
        null, 
        //new ShiftRightLogicalInt(),     // 70
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        
        new StackDuplicateXSingle(),        // 81

        null,
        null, 
        
        new CompareSingleIntStackPopTwo(),  // 84
        new CompareSingleIntStackPopZero(), // 85
        new CompareSingleIntStackPopNull(), // 86
        new CompareSingleIntEQ(),           // 87
        new CompareSingleIntNE(),           // 88
        new CompareSingleIntLT(),           // 89
        new CompareSingleIntGE(),           // 90
        new CompareSingleIntGT(),           // 91
        new CompareSingleIntLE(),           // 92
        new CompareBranch(),                // 93
        
        null, null, 
        new LoadConstantPoolProcess(),      // 96
        null,                             
        null, 
        
        new LocalGetSingleAlt(),            // 99
        
        // Thread stuff
        new EligibleQueueInsert(),          // 100
        new EligibleQueueUnlink(),          // 101
        new EligibleQueueProcess(),         // 102
        
        new WaitQueueInsert(),              // 103
        new WaitQueueProcess(),             // 104
        
        new TimerLoad(),                    // 105
        new ThreadSetWakeupTime(),          // 106
        new ThreadRunSetup(),               // 107
        new ThreadRunReference(),           // 108
        
        null,
        
        new InterruptVectorRetrieve(),      // 110
        new InterruptVectorSet(),           // 111
        
        null, null, null, null, null,
        null, null,
        
        new ArrayAccessSetup(),             // 119
        new ArrayGetSingle(),               // 120
        null,
        new ArrayGetChar(),
        new ArrayGetByte(),
        new ArrayGetShort(),
        new ArrayPutSingle(),               // 125
        null,
        new ArrayPutChar(),
        new ArrayPutByte(),
        new ArrayPutShort(),

        
        
    };

    /** Creates a new instance of ProcessList */
    public ProcessList() {
    }
    
}
