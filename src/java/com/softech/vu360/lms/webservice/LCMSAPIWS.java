package com.softech.vu360.lms.webservice;

import java.net.URISyntaxException;

import com.softech.vu360.lms.webservice.message.lcms.GenerateCertificateRequest;
import com.softech.vu360.lms.webservice.message.lcms.GenerateCertificateResponse;
import com.softech.vu360.lms.webservice.message.lcms.LearnerCourseProgressRequest;
import com.softech.vu360.lms.webservice.message.lcms.LearnerCourseProgressResponse;
import com.softech.vu360.lms.webservice.message.lcms.LearnerSetupRequest;
import com.softech.vu360.lms.webservice.message.lcms.LearnerSetupResponse;
import com.softech.vu360.lms.webservice.message.lcms.LearnerStatsTransferRequest;
import com.softech.vu360.lms.webservice.message.lcms.LearnerStatsTransferResponse;
import com.softech.vu360.lms.webservice.message.lcms.LearningSessionCompleteRequest;
import com.softech.vu360.lms.webservice.message.lcms.LearningSessionCompleteResponse;
import com.softech.vu360.lms.webservice.message.lcms.PublishCourseRequest;
import com.softech.vu360.lms.webservice.message.lcms.PublishCourseResponse;
import com.softech.vu360.lms.webservice.message.lcms.UpdateProfileRequest;
import com.softech.vu360.lms.webservice.message.lcms.UpdateProfileResponse;



/**
 * LCMSAPIWS defines the set of interfaces 
 * to control the interactions and business logic
 * of the integration between LCMS & LMS.
 * 
 * @author jason
 *
 */
public interface LCMSAPIWS extends AbstractWS {
	
	public LearningSessionCompleteResponse learningSessionCompleted(LearningSessionCompleteRequest learningSessionCompleteEvent);
	public UpdateProfileResponse updateProfileEvent(UpdateProfileRequest updateProfileEvent);
	public LearnerSetupResponse setupLearners(LearnerSetupRequest  learnerSetupRequest);
	public PublishCourseResponse publishCourses(PublishCourseRequest  request);
	public LearnerStatsTransferResponse recordLearnerStats(LearnerStatsTransferRequest learnerStatsRequest);
	public LearnerCourseProgressResponse recordCourseProgress(LearnerCourseProgressRequest learnerCourseProgress);
	public GenerateCertificateResponse getCertificateUnsignedBytes(GenerateCertificateRequest request) throws URISyntaxException;
}
