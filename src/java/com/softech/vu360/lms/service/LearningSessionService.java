package com.softech.vu360.lms.service;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.LearningSession;
import com.softech.vu360.lms.webservice.message.lcms.LearnerCourseProgressRequest;
import com.softech.vu360.lms.webservice.message.lcms.LearnerStatsTransferRequest;
import com.softech.vu360.lms.webservice.message.lcms.LearningSessionCompleteRequest;

public interface LearningSessionService {

	public boolean processLearningSessionEnded(LearningSessionCompleteRequest req);	
	public boolean recordLearnerStats(LearnerStatsTransferRequest req);
	public Map recordCourseProgress(LearnerCourseProgressRequest req);
	
	public List<LearningSession> getLearningSessionByCourseApproval(List<CourseApproval> lstCourseApproval);
	
	
	
}
