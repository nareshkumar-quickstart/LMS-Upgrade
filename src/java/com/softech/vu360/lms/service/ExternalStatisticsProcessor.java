package com.softech.vu360.lms.service;

import com.softech.vu360.lms.model.LearningSession;

/**
 * ExternalStatisticsProcessor defines the set of interfaces 
 * that need to be implemented for stats to be sent to a 3rd
 * party.  This was originally designed when AICC was introduced
 * into the LMS so that we had a plug-able architecture for integrating
 * 3rd party LMS systems into The LS 360 Platform LMS.
 * 
 * @author jason
 *
 */
public interface ExternalStatisticsProcessor {
	
	public void handleLearingSessionCompleteEvent(LearningSession learningSession,String CallerFunction);

}
