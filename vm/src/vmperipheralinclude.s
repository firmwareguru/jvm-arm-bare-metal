;****************************************************************
; 
;    Date: Apr 14, 2008
;    Author: Evan Ross
;
;    Includes for use with peripheral hardware drivers.
;
;
;****************************************************************



; For the RIT128x96x4 OLED display driver
SYSCTL_SYSDIV_1 	EQU     0x07800000  ; Processor clock is osc/pll /1
SYSCTL_USE_OSC     	EQU     0x00003800  ; System clock is the osc clock
SYSCTL_OSC_MAIN    	EQU     0x00000000  ; Oscillator source is main osc
SYSCTL_XTAL_8MHZ    EQU	    0x00000380  ; External crystal is 8MHz


	END