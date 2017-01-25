package com.softech.vu360.lms.webservice;

import com.softech.vu360.lms.webservice.message.integration.BatchImportLearnerRequest;
import com.softech.vu360.lms.webservice.message.integration.BatchImportLearnerResponse;


/**
 * LMSIntegrationWS defines the set of interfaces 
 * to control the interactions and business logic
 * between external applications.client & LMS
 * 
 * @author Faisal A. Siddiqui
 *
 */
public interface LMSIntegrationWS extends AbstractWS {
	
	public BatchImportLearnerResponse importBatchFileEvent(BatchImportLearnerRequest request);
	
}
