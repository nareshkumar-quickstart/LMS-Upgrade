package com.softech.vu360.lms.webservice.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import com.lowagie.text.DocumentException;
import com.softech.vu360.lms.exception.NoCertificateNumberFoundException;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.service.CertificateService;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.LearningSessionService;
import com.softech.vu360.lms.service.UpdateProfileService;
import com.softech.vu360.lms.webservice.LCMSAPIWS;
import com.softech.vu360.lms.webservice.message.lcms.ContentOwnerVO;
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
import com.softech.vu360.lms.webservice.message.lcms.TransactionResultType;
import com.softech.vu360.lms.webservice.message.lcms.UpdateProfileRequest;
import com.softech.vu360.lms.webservice.message.lcms.UpdateProfileResponse;
import com.softech.vu360.lms.webservice.message.lcms.VU360User;
import com.softech.vu360.lms.webservice.message.lcms.client.ArrayOfUnsignedByte;
import com.softech.vu360.util.GUIDGeneratorUtil;

/**
 * LcmsWSImpl defines the set of interfaces 
 * to control the interactions and business logic
 * of the integration between LCMS & LMS.
 * 
 * @author jason
 */
@Endpoint
public class LcmsWSImpl implements LCMSAPIWS {
	private static final Logger log = Logger.getLogger(LcmsWSImpl.class.getName());
	
	private static final String LEARNING_SESSION_COMPLETE = "LearningSessionCompleteRequest";
	private static final String MESSAGES_NAMESPACE = "http://www.360training.com/vu360/schemas/lms/lcmsMessages";
	private static final String UPDATE_PROFILE_EVENT = "UpdateProfileRequest";
	private static final String LEARNER_SETUP_EVENT = "LearnerSetupRequest";
	private static final String COURSE_PUBLISH_EVENT = "PublishCourseRequest";
	private static final String LEARNER_STATS_EVENT = "LearnerStatsTransferRequest";
	private static final String LEARNER_COURSEPROGRESS_EVENT = "LearnerCourseProgressRequest";
	private static final String GENERATE_CERTIFICATE_EVENT = "GenerateCertificateRequest";
	
	private LearningSessionService learningSessionService=null;
	private UpdateProfileService updateProfileService=null;
	private LearnerService learnerService = null;
	private CourseAndCourseGroupService courseAndCourseGroupService = null;
	private CertificateService certificateService=null;
	private EntitlementService entitlementService=null;
	
	@PayloadRoot(localPart = COURSE_PUBLISH_EVENT, namespace = MESSAGES_NAMESPACE)
	public PublishCourseResponse publishCourses(PublishCourseRequest  request) 
	{
		PublishCourseResponse response=new PublishCourseResponse();
		StringBuilder message = new StringBuilder("");
		List<ContentOwnerVO> contentOwnerList = request.getContentOwnerList();
		
		try{
			HashMap<String, Integer> publishingResult = courseAndCourseGroupService.publishCourses(contentOwnerList,true);// since this web-service is called from different platform/system therefore needs to refresh the course cache		
			response.setTransactionResult(TransactionResultType.SUCCESS);
			message.append("COURSE(S) PUBLISHED SUCCESSFULLY!");
		}catch (Exception e){
			log.error("Error Occured while publishing course:"+e.getMessage());
			message.append("COURSE(S) PUBLISHED FAILED!");
			response.setTransactionResult(TransactionResultType.FAILURE);
		}
		
		response.setEventDate(request.getEventDate());
		response.setTransactionGUID(request.getTransactionGUID());
		response.setTransactionResultMessage(message.toString());
		
		log.info("Transaction Message: " + response.getTransactionResultMessage());
		log.info("Transaction Result: " + response.getTransactionResult());
		
		return response;
	}
	

	@PayloadRoot(localPart = LEARNER_SETUP_EVENT, namespace = MESSAGES_NAMESPACE)
	public LearnerSetupResponse setupLearners(LearnerSetupRequest  learnerSetupRequest) 
	 {
		List<VU360User> userList = learnerSetupRequest.getUserList();
		List<String> userGUIDList = new ArrayList<String>(userList.size());
		for(VU360User user:userList){
			userGUIDList.add(user.getUserGUID());
		}
		String contentOwnerGUID = learnerSetupRequest.getContentOwnerGUID();
		List<Learner> listOfLearnerSetupSuccessfully = learnerService.setupLearnerForUsers(userGUIDList,contentOwnerGUID);
		boolean result= listOfLearnerSetupSuccessfully.size()>0?true:false; 
		LearnerSetupResponse response=new LearnerSetupResponse();
		response.setEventDate(learnerSetupRequest.getEventDate());
		response.setTransactionGUID(learnerSetupRequest.getTransactionGUID());
		response.setNoOfLearnerSetupSuccessfully(BigInteger.valueOf(listOfLearnerSetupSuccessfully.size()));
		if(result)
		{
			response.setTransactionResult(TransactionResultType.SUCCESS);
			response.setTransactionResultMessage("Learning Session Ended Sucessfully!");
		}
		else
		{
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage("Learning Session Failed..");
		}
		return response;
	}
	 @PayloadRoot(localPart = LEARNING_SESSION_COMPLETE, namespace = MESSAGES_NAMESPACE)
	public LearningSessionCompleteResponse learningSessionCompleted(LearningSessionCompleteRequest learningSessionCompleteRequest) 
	 {
		boolean result=learningSessionService.processLearningSessionEnded(learningSessionCompleteRequest); 
		LearningSessionCompleteResponse response=new LearningSessionCompleteResponse();
		response.setEventDate(learningSessionCompleteRequest.getEventDate());
		response.setTransactionGUID(GUIDGeneratorUtil.generateGUID());
		
		if(result)
		{
			response.setTransactionResult(TransactionResultType.SUCCESS);
			response.setTransactionResultMessage("Learning Session Ended Sucessfully!");
		}
		else
		{
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage("Learning Session Failed..");
		}
		return response;
	}

	/**
	 * Following method will be mapped based on PayloadRoot annotation definitation i.e. UpdateProfile.
	 * Client will be passing in all the relevant details pertaining to updating the profile of a user
	 *  
	 */
	@PayloadRoot(localPart = UPDATE_PROFILE_EVENT, namespace = MESSAGES_NAMESPACE)
	public UpdateProfileResponse updateProfileEvent(UpdateProfileRequest request) {
		UpdateProfileResponse response = new UpdateProfileResponse();
		log.trace( "UpdateProfileRequest method called...." + "\n\n");
		boolean result=updateProfileService.updateProfile(request);
		
		if(result)
		{
			response.setTransactionResult(TransactionResultType.SUCCESS);
			response.setTransactionResultMessage("Profile Updated Successfully");
			
		}
		else
		{
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage("Profile Update Failed");
		}
		response.setEventDate(request.getEventDate());
		response.setTransactionGUID(GUIDGeneratorUtil.generateGUID());
		return response;
	}
	
	
	 @PayloadRoot(localPart = LEARNER_STATS_EVENT, namespace = MESSAGES_NAMESPACE)
		public LearnerStatsTransferResponse recordLearnerStats(LearnerStatsTransferRequest learnerStatsRequest) 
		 {//TODO
		 boolean result=learningSessionService.recordLearnerStats(learnerStatsRequest); 
			LearnerStatsTransferResponse response=new LearnerStatsTransferResponse();
			response.setEventDate(learnerStatsRequest.getEventDate());
			response.setTransactionGUID(GUIDGeneratorUtil.generateGUID());
			
			if(result)
			{
				response.setTransactionResult(TransactionResultType.SUCCESS);
				response.setTransactionResultMessage("Stats Saved Successfully!");
			}
			else
			{
				response.setTransactionResult(TransactionResultType.FAILURE);
				response.setTransactionResultMessage("Could not save Stats..");
			}
			return response;
		}
	 
	 @PayloadRoot(localPart = LEARNER_COURSEPROGRESS_EVENT, namespace = MESSAGES_NAMESPACE)
		public LearnerCourseProgressResponse recordCourseProgress(LearnerCourseProgressRequest learnerCourseProgress) 
		 {//TODO
			boolean result = true;
			String redirectSurveyURL = null;
			Map resultMap =learningSessionService.recordCourseProgress(learnerCourseProgress);
			if(resultMap!=null){
				result = (Boolean)resultMap.get("RESULT");
				redirectSurveyURL = (String)resultMap.get("SURVEY_URL");
			}
			LearnerCourseProgressResponse response=new LearnerCourseProgressResponse();
			response.setEventDate(learnerCourseProgress.getEventDate());
			response.setTransactionGUID(GUIDGeneratorUtil.generateGUID());
			
			if(result)
			{
				response.setTransactionResult(TransactionResultType.SUCCESS);
				if(redirectSurveyURL!=null && !redirectSurveyURL.isEmpty()){
					response.setTransactionResultMessage(redirectSurveyURL);
				}
				else{
					response.setTransactionResultMessage("Course Progress Recorded Sucessfully!");
				}
			}
			else
			{
				response.setTransactionResult(TransactionResultType.FAILURE);
				response.setTransactionResultMessage("Could not save Course Progess");
			}
			return response;
		}
	 
	 @PayloadRoot(localPart = GENERATE_CERTIFICATE_EVENT, namespace = MESSAGES_NAMESPACE)
	 public GenerateCertificateResponse getCertificateUnsignedBytes(GenerateCertificateRequest request) throws URISyntaxException{
		 GenerateCertificateResponse response = new GenerateCertificateResponse();
		 
		 try {
			
		 	ByteArrayOutputStream baos = getCertificateService().generateCertificate(getEntitlementService().getLearnerEnrollmentById(request.getLearnerEnrollmentId()));
			 
			if(baos != null){
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(baos.toByteArray(), request.getOffset(), request.getChunkSize()));
				ArrayOfUnsignedByte bytes = new ArrayOfUnsignedByte();
				while(dis.available() > 0){					
					bytes.getUnsignedByte().add((short)dis.readUnsignedByte());
				}
				response.setBytes(bytes);
			}
			
			response.setTransactionResult(TransactionResultType.SUCCESS);
			response.setTransactionResultMessage("");
			
		} catch (DocumentException e) {
			log.error(e.getMessage(), e);
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage(e.getMessage());
		} catch (NoCertificateNumberFoundException e) {
			log.error(e.getMessage(), e);
			response.setTransactionResult(TransactionResultType.FAILURE);
			response.setTransactionResultMessage(e.getMessage());
		}
		return response;
	 }
	 
	public LearningSessionService getLearningSessionService() 
	{
		return learningSessionService;
	}
	public void setLearningSessionService(
			LearningSessionService learningSessionService) {
		this.learningSessionService = learningSessionService;
	}
	public UpdateProfileService getUpdateProfileService() {
		return updateProfileService;
	}

	public void setUpdateProfileService(UpdateProfileService updateProfileService) {
		this.updateProfileService = updateProfileService;
	}
	/**
	 * @return the learnerService
	 */
	public LearnerService getLearnerService() {
		return learnerService;
	}
	/**
	 * @param learnerService the learnerService to set
	 */
	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}


	/**
	 * @return the certificateService
	 */
	public CertificateService getCertificateService() {
		return certificateService;
	}


	/**
	 * @param certificateService the certificateService to set
	 */
	public void setCertificateService(CertificateService certificateService) {
		this.certificateService = certificateService;
	}


	/**
	 * @return the entitlementService
	 */
	public EntitlementService getEntitlementService() {
		return entitlementService;
	}


	/**
	 * @param entitlementService the entitlementService to set
	 */
	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}


	
	

	
}
