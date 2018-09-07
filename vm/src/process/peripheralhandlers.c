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


												  

//                 r0      r1     r2
void draw_int(int value, int x, int y)
{
    char a[32];
	usprintf(a, "%d", value);
    RIT128x96x4StringDraw(a, x, y, 15);
}



