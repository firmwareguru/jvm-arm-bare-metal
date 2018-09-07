;****************************************************************
;  
;    ramconstants.s
;  
;    Date: Mar 23, 2008
;    Author: Evan Ross
;
;    Constants for offsets of various RAM-based structures are
;    defined here.
;
;
;****************************************************************

; Frame Layout:
;      opstack[max_stack - 1]
;             ...
;      opstack[0]
;      variable array index[max_locals - 1]
;             ...
;   4  variable array index[0]
;   3  stack pointer
;   2  previous frame pointer
;   1  previous PC
;   0  CurrentClass
;
; Note that the opstack grows upward.  Thus, an empty frame stack will have it's stack pointer point
; to the opstack[0].    

FRAME_CurrentClassOffset EQU 0 * 4
FRAME_PCOffset 	 	 	 EQU 1 * 4
FRAME_PreviousFrameOffset EQU 2 * 4
FRAME_StackPointerOffset EQU 3 * 4
FRAME_LocalVarOffset     EQU 4 * 4

; An 'Object' defines common fields for arrays or instances.
; Instances do not define any special fields beyond those in Object, except for a few special cases
; such as Thread instances.
;
; All objects have:
;   1. Monitors: a 32-bit word used to track thread locks of objects.
;   2. ClassReference: a reference (handle) to the NVM class template for the type of the object

OBJECT_MonitorOffset		EQU 0 * 4
OBJECT_ClassReferenceOffset EQU 1 * 4
OBJECT_Size					EQU 2 * 4

; These constants extend Object into Arrays.
;
; Arrays have another field beyond those in Objects: arraylength.

ARRAY_ArrayLengthOffset EQU OBJECT_Size
ARRAY_ArraySize			EQU OBJECT_Size + (1 * 4)

; These constants define locations of variables in the Thread Object.

THREAD_NextOffset		EQU	0 * 4
THREAD_PriorityOffset	EQU 1 * 4
THREAD_DelayOffset  	EQU 2 * 4
THREAD_IdOffset			EQU 3 * 4
THREAD_RegistersOffset	EQU OBJECT_Size + (4 * 4)

; This is the only context related value stored in Threads.
; The rest of the context is stored in the current frame.
THREAD_CurrentFrameOffset EQU (THREAD_RegistersOffset + 0)

;
; The following constants are used by the memory allocation processes.
; These offsets are negative because the free list pointer points to 
; the start of the available memory region!

RAM_BlockHeaderSizeOffset 	EQU	(-2 * 4)
RAM_BlockHeaderNextOffset	EQU (-1 * 4)
RAM_BlockRemainderMinimumSize	EQU (5 * 4)
RAM_BlockHeaderSize			EQU (2 * 4)

	END



