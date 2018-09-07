//****************************************************************
//  
//   vmexceptionsimpl.c
//  
//    Date: Apr 14, 2008
//    Author: Evan Ross
//
//    Exception handlers for the VM, written in C.
//    Exceptions can be called from anywhere in the VM.
//    This file contains the implementation.
//
//****************************************************************


#include "rit128x96x4.h"

#include "c:/driverlib/utils/ustdlib.h"

//*****************************************************************************


														  
#define ClassNotFoundException	0
#define MethodNotFoundException 1
#define MemoryAllocationError	 2
#define UnhandledOpcodeException  3
#define NotImplementedException 4
#define FieldNotFoundException 	 5
#define MethodOrFieldNotFoundException  6
#define InvalidFieldSizeException  7
#define InvalidArrayTypeException  8
#define EligibleQueueEmpty		   9
#define ThreadTerminatedException  10
#define FaultISR				11

typedef struct
{

//	int handle;
//	int index;
//	int name; 
//	int descriptor;
//	int classhandle;
//	int cphandle;
//	int tablehandle;
	int classhandle;
	int currentthread;
	int PCW;
	int vmpc;
} regStruct;


//                       r0          r1        r2
void exception_handler(int code, int cause, regStruct* regs)
{
	// location in flash.
	//unsigned int* memPtr = (unsigned int*)(0x00010000);
	//unsigned char* cMemPtr = (unsigned char*)memPtr;

    char a[32];

	char* textPtr;



	switch (cause)
	{
		case ClassNotFoundException:
			textPtr = "ClassNotFound";
			break;
		case MethodNotFoundException:
			textPtr = "MethodNotFoundException";
			break;
		case MemoryAllocationError:
			textPtr = "MemoryAllocationError";
			break;
		case UnhandledOpcodeException:
			textPtr = "UnhandledOpcodeException";
			break;
		case NotImplementedException:
			textPtr = "NotImplementedException";
			break;
		case FieldNotFoundException:
			textPtr = "FieldNotFoundException";
			break;
		case MethodOrFieldNotFoundException:
			textPtr = "MethodOrFieldNotFoundException";
			break;
		case InvalidFieldSizeException:
			textPtr = "InvalidFieldSizeException";
			break;
		case EligibleQueueEmpty:
			textPtr = "EligibleQueueEmpty";
			break;
		case ThreadTerminatedException:
			textPtr = "ThreadTerminated";
			break;
		case FaultISR:
			textPtr = "FaultISR";
			break;
		default:
			textPtr = "UnknownException";
			break;
	}



	usprintf(a, "%s", textPtr);
    RIT128x96x4StringDraw(a, 0, 0, 15);

	usprintf(a, "PCW: %x", regs->PCW);
    RIT128x96x4StringDraw(a, 0, 10, 15);

	usprintf(a, "VMPC: %x", regs->vmpc);
    RIT128x96x4StringDraw(a, 0, 20, 15);

	usprintf(a, "CurrentThread: %x", regs->currentthread);
    RIT128x96x4StringDraw(a, 0, 30, 15);

	while(1)
	{
	}
}



