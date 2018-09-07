;****************************************************************
; 
;    Date: Apr 29, 2008
;    Author: Evan Ross
;
;    'lang' handlers.
; 
;	 Custom bytecode handlers for bytecode 254.  
; 
;    The registers are NOT required to be preserved across
;    bytecodes, however some registers MAY be required to be
;    preserved across process calls within a handler.
;
;    IMPORTANT!  If bytecode operands are retrieved in the handlers,
;    the vmpc MUST be incremented past the operands to point to the
;    next vmpc.
;
;****************************************************************


	AREA langhandlerarea, CODE, READONLY

	INCLUDE vmregisters.s
	INCLUDE ramconstants.s
	INCLUDE vmexceptions.s
	INCLUDE internalclass.s
	INCLUDE lookupsetup.s ; macros for setting process flags

	INCLUDE stackmacros.s
	INCLUDE fieldmacros.s
	INCLUDE arraymacros.s
	INCLUDE constantpoolmacros.s
	INCLUDE arithmeticmacros.s

	IMPORT reference_lookup
	IMPORT threadrun_reference
	IMPORT threadrun_setup
	IMPORT table_lookup
	IMPORT objectclass_lookup
	IMPORT objectsize_lookup
	IMPORT object_setup
	IMPORT frameinfo_lookup
	IMPORT allocate_memory
	IMPORT deallocate_memory
	IMPORT frame_setup
	IMPORT frame_teardown
	IMPORT set_pc
	IMPORT classinfo_lookup		  
	IMPORT arraysize_lookup
	IMPORT eligiblequeue_insert

	IMPORT main_execution_loop


;****************************************************************
;
;  T_start
;
;  Start a Thread.
;
;  Operands:
;      none.
;
;  Notes:
;      The Thread instance to be started is that referred to
;      by 'this' in local variable 0 of the current frame.
;
;****************************************************************
	EXPORT handler_lang_tstart
handler_lang_tstart

	BL threadrun_reference
	SetupMethodLookup
	SetupVirtualLookup
	BL table_lookup
	BL frameinfo_lookup
	BL allocate_memory
	BL threadrun_setup
	BL eligiblequeue_insert 

	B main_execution_loop  ; done


;****************************************************************
;
;  T_yield
;
;  Yield a Thread.
;
;  Operands:
;      none.
;
;  Notes:
;      Invokes the scheduler.  Class (static) method.
;      Yields the currentthread.
;
;****************************************************************
	EXPORT handler_lang_tyield 
handler_lang_tyield 


	; Set the PCW to invoke the scheduler 
	; at the next opportunity.

	SetPCW PCW_PROCESS_SCHEDULER
	
	B main_execution_loop  ; done

;****************************************************************
;
;  T_sleep
;
;  Sleep a Thread
;
;  Operands:
;      none.
;
;  Notes:
;      Invokes an advanced scheduler.  Class (static) method.
;      Sleeps the currentthread.
;
;****************************************************************
	EXPORT handler_lang_tsleep 
handler_lang_tsleep


	; Set the PCW to invoke the sleep function 
	; at the next opportunity.				  
	SetPCW PCW_PROCESS_SLEEP
	
	B main_execution_loop  ; done			  

	END
