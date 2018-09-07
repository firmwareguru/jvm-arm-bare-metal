;****************************************************************
; 
;  scheduler.s
;
;  Date: May 3, 2008
;  Author: Evan Ross
;
;  This file contains the thread scheduler processes:
;       
;      The scheduler process itself, called by Thread.yield() and
;      the system timer
;
;      The Thread sleep process, which invokes the scheduler.
;      Called by Thread.sleep().  		
;
;****************************************************************

	AREA langhandlerarea, CODE, READONLY

	INCLUDE vmregisters.s
	INCLUDE ramconstants.s
	INCLUDE vmexceptions.s
	INCLUDE internalclass.s
	INCLUDE lookupsetup.s ; macros for setting process flags
	INCLUDE stackmacros.s
	INCLUDE arithmeticmacros.s
	
 	IMPORT eligiblequeue_insert
	IMPORT eligiblequeue_process		
	IMPORT eligiblequeue_unlink
	IMPORT main_execution_loop
	IMPORT waitqueue_process
	IMPORT waitqueue_insert

; The scheduler.  Composed of: 
;    1. WaitQueueProcess     - unlink expired Threads
;    2. EligibleQueueInsert  - place them into eligible queue
;    3. EligibleQueueProcess - perform context switch if necessary
;
; The scheduler is called by 'yield' directly, and 'sleep' after inserting
; into wait queue.
;
;  The 'index' register is used as a return value from WaitQueueProcess
;  to determine if the EligibleQueueInsert must be run.

	EXPORT invoke_scheduler
invoke_scheduler

	;LoadTimer
    BL waitqueue_process  ; unlink first expired waiting thread
scheduler_loop
	CMP index, #1         
	BNE scheduler_finish
	BL eligiblequeue_insert ; insert the unlinked thread
	BL waitqueue_process  ; check and unlink another expired waiting thread
	B scheduler_loop	

scheduler_finish
	BL eligiblequeue_process   ; select the highest priority thread and switch context
	B main_execution_loop ; done, run highest priority eligible thread



;--------------------------------------------------------------
;
;  thread_sleep
;
;   handles Thread.sleep(int sleeptime)

	EXPORT thread_sleep
thread_sleep

	; Get the sleep time in units of timer counts in local var 0
	MOV index, #0
	LocalGetSingleAlt

	; load the current timer value
	LoadTimer

	; subtract the sleep time from the current timer value
	SubSingleInt

	; Store the thread's new wakeup time
	STR value, [currentthread, #(OBJECT_Size + THREAD_DelayOffset)]
	
	; unlink the currentthread from the eligible queue
	BL eligiblequeue_unlink

	; stuff it into the wait queue
	BL waitqueue_insert

	; get the best thread in the eligible queue
	BL eligiblequeue_process	
											   
	B main_execution_loop ; done, run highest priority eligible thread



	END