package com.softech.vu360.lms.service;

import com.softech.vu360.lms.exception.ReportNotExecutableException;
import com.softech.vu360.lms.model.VU360Report;
import com.softech.vu360.lms.model.VU360User;

/**
 * Simple interface for executing a report including
 * export of results in HTML and CSV.
 * 
 * @author jason
 *
 */
public interface ReportExecutionService {
	
	public void executeReport(VU360Report report, com.softech.vu360.lms.vo.VU360User user) throws ReportNotExecutableException;

}
