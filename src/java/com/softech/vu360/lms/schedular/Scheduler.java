package com.softech.vu360.lms.schedular;

import com.softech.vu360.lms.exception.LMSSchedulerException;

public interface Scheduler {
	
	/**
	 * This method will be the entry point for all types of schedulers
	 * @throws LMSSchedularException
	 */
	public void execute() throws LMSSchedulerException;

}
