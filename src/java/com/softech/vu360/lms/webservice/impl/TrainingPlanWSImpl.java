package com.softech.vu360.lms.webservice.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.TrainingPlan;
import com.softech.vu360.lms.model.TrainingPlanAssignment;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.TrainingPlanService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.webservice.TrainingPlanWS;
import com.softech.vu360.lms.webservice.message.predict360.trainingplan.GetAllTraingPlansByIdsRequest;
import com.softech.vu360.lms.webservice.message.predict360.trainingplan.GetAllTraingPlansByIdsResponse;
import com.softech.vu360.lms.webservice.message.predict360.trainingplan.GetEmployeeCoursesStatusRequest;
import com.softech.vu360.lms.webservice.message.predict360.trainingplan.GetEmployeeCoursesStatusResponse;
import com.softech.vu360.lms.webservice.message.predict360.trainingplan.GetTrainingPlanAssignmentsByDateRequest;
import com.softech.vu360.lms.webservice.message.predict360.trainingplan.GetTrainingPlanAssignmentsByDateResponse;
import com.softech.vu360.lms.webservice.message.predict360.trainingplan.GetTrainingPlanCoursesByDueDateRequest;
import com.softech.vu360.lms.webservice.message.predict360.trainingplan.GetTrainingPlanCoursesByDueDateResponse;
import com.softech.vu360.lms.webservice.message.predict360.trainingplan.TrainingPlanAssignmentSoapVO;
import com.softech.vu360.lms.webservice.message.predict360.trainingplan.TrainingPlanCourseSoapVO;
import com.softech.vu360.lms.webservice.message.predict360.trainingplan.TrainingPlanCourseStatusSoapVO;
import com.softech.vu360.lms.webservice.message.predict360.trainingplan.TrainingPlanPredictRequest;
import com.softech.vu360.lms.webservice.message.predict360.trainingplan.TrainingPlanPredictResponse;
import com.softech.vu360.lms.webservice.message.predict360.trainingplan.TrainingPlanSoapVO;


@Endpoint
public class TrainingPlanWSImpl implements TrainingPlanWS {

	private static final Logger log = Logger.getLogger(TrainingPlanWSImpl.class.getName());
	
	private static final String TRAINING_PLAN_EVENT = "TrainingPlanPredictRequest";
	private static final String MESSAGES_NAMESPACE = "http://www.360training.com/vu360/schemas/lms/trainingplans";
	private static final String ACTIVE = "ACTIVE";
	private static final String EXPIRED = "EXPIRED";
	private static final String COMPLETED = "COMPLETED";
	private static final String NOTSTARTED = "NOTSTARTED";
	private static final String INPROGRESS = "INPROGRESS";
	
	protected TrainingPlanService trainingPlanService;
	protected CustomerService customerService;
	protected EnrollmentService enrollmentService;
	protected VU360UserService vu360UserService;
	
	@Override
	@PayloadRoot(localPart = TRAINING_PLAN_EVENT, namespace = MESSAGES_NAMESPACE)
	public TrainingPlanPredictResponse getAllTrainingPlansByCustomerId( TrainingPlanPredictRequest request) {
		TrainingPlanPredictResponse response = new TrainingPlanPredictResponse();
		try {
			log.debug("received request data >> " + request.getUserName());
			String userName = request.getUserName();
			Long customerId;
			VU360User vu360User = vu360UserService.findUserByUserName(userName);
			
			if(vu360User!=null && vu360User.getLearner()!=null && vu360User.getLearner().getCustomer()!=null) {
				customerId = vu360User.getLearner().getCustomer().getId();

			} else {
				throw new UsernameNotFoundException("FOUND NO SUCH USER NAME : " + userName);
			}
			
			Customer customer = customerService.getCustomerById(customerId);
			//trainingPlanService.findTrainingPlanByCustomerId(1);
			response.setCustomerId(customerId);
			if(customer !=null)
			{	
				List<TrainingPlan> lstTrainingPlan =  trainingPlanService.findTrainingPlanByCustomerId(customer);
				TrainingPlanSoapVO trainingPlanSoapVo = null;
				
				for (TrainingPlan trainingPlan : lstTrainingPlan) {
					trainingPlanSoapVo = new TrainingPlanSoapVO();
					trainingPlanSoapVo.setTrainingPlanId(trainingPlan.getId());
					trainingPlanSoapVo.setTrainingPlanName(trainingPlan.getName());
					response.getTrainingplans().add(trainingPlanSoapVo);
				}
			}
				
			//response.setResultMessage("OK");
		} catch(Exception e) {
			log.error("problem in finding Training Plans", e);

		}
		return response;
	}
	
	@Override
	@PayloadRoot(localPart = "GetEmployeeCoursesStatusRequest", namespace = MESSAGES_NAMESPACE)
	public GetEmployeeCoursesStatusResponse getEmployeeCoursesStatus(GetEmployeeCoursesStatusRequest trainingPlanRequest) {
		GetEmployeeCoursesStatusResponse response = new GetEmployeeCoursesStatusResponse();
		try {
			log.debug("received request data >> " + trainingPlanRequest.getUserName());
			String userName = trainingPlanRequest.getUserName();
			boolean isCustomer = trainingPlanRequest.getIsCustomer();
//			Long customerId;
			VU360User vu360User = vu360UserService.findUserByUserName(userName);
			
			List<LearnerEnrollment> learnerEnrollments = GetTrainingPlanLearnerEnrollments(vu360User.getLearner().getCustomer());
/*			if(vu360User!=null && vu360User.getLearner()!=null && vu360User.getLearner().getCustomer()!=null) {
				customerId = vu360User.getLearner().getCustomer().getId();
			} else {
				throw new UsernameNotFoundException("FOUND NO SUCH USER NAME : " + userName);
			}*/
			
			int completed = 0;
			int inProgress = 0;
			int notStarted = 0;
			int expired = 0;
			for (LearnerEnrollment learnerEnrollment : learnerEnrollments) {
				
				if(isCustomer)
				{
					if(learnerEnrollment.getCourseStatistics().getStatus().toUpperCase().equals(COMPLETED)) {
						completed++;
					}
					if(learnerEnrollment.getCourseStatistics().getStatus().toUpperCase().equals(INPROGRESS)) {
						inProgress++;
					}
					if(learnerEnrollment.getCourseStatistics().getStatus().toUpperCase().equals(EXPIRED)) {
						expired++;
					}
					if(learnerEnrollment.getCourseStatistics().getStatus().toUpperCase().equals(NOTSTARTED)) {
						notStarted++;
					}
				}else if(learnerEnrollment.getLearner().getVu360User().getUsername().equals(userName)) {
					if(learnerEnrollment.getCourseStatistics().getStatus().toUpperCase().equals(COMPLETED)) {
						completed++;
					}
					if(learnerEnrollment.getCourseStatistics().getStatus().toUpperCase().equals(INPROGRESS)) {
						inProgress++;
					}
					if(learnerEnrollment.getCourseStatistics().getStatus().toUpperCase().equals(EXPIRED)) {
						expired++;
					}
					if(learnerEnrollment.getCourseStatistics().getStatus().toUpperCase().equals(NOTSTARTED)) {
						notStarted++;
					}
				}
			}
			TrainingPlanCourseStatusSoapVO courseStatusSoapVO = new TrainingPlanCourseStatusSoapVO();
			courseStatusSoapVO.setCompletedCourses(completed);
			courseStatusSoapVO.setExpiredCourses(expired);
			courseStatusSoapVO.setNotStartedCourses(notStarted);
			courseStatusSoapVO.setInProgressCourses(inProgress);
			
			response.getTrainingplancoursesstatus().add(courseStatusSoapVO);
			
		//	learnerService.g
				
			//response.setResultMessage("OK");
		} catch(Exception e) {
			log.error("problem in finding Training Plans", e);

		}
		return response;
	}
	
	
	@Override
	@PayloadRoot(localPart = "GetTrainingPlanCoursesByDueDateRequest", namespace = MESSAGES_NAMESPACE)
	public GetTrainingPlanCoursesByDueDateResponse getTrainingPlansCoursesByDueDate(GetTrainingPlanCoursesByDueDateRequest request) {
		GetTrainingPlanCoursesByDueDateResponse response = new GetTrainingPlanCoursesByDueDateResponse();
		try {
			log.debug("received request data >> " + request.getUserName());
			String userName = request.getUserName();
			Long customerId;
			VU360User vu360User = vu360UserService.findUserByUserName(userName);
			
			if(vu360User!=null && vu360User.getLearner()!=null && vu360User.getLearner().getCustomer()!=null) {
				customerId = vu360User.getLearner().getCustomer().getId();

			} else {
				throw new UsernameNotFoundException("FOUND NO SUCH USER NAME : " + userName);
			}
			
			Customer customer = customerService.getCustomerById(customerId);
			//trainingPlanService.findTrainingPlanByCustomerId(1);
			//response.setCustomerId(customerId);
			if(customer !=null)
			{	
				List<LearnerEnrollment> learnerEnrollments = GetTrainingPlanLearnerEnrollments(customer);
				TrainingPlanCourseSoapVO trainingPlanCourseVo;
				Calendar filterDate = Calendar.getInstance();
				filterDate.add(Calendar.DATE, 9);
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				
						for (LearnerEnrollment learnerEnrollment : learnerEnrollments) {
							log.debug("Calculationg difference ====="  + learnerEnrollment.getEnrollmentEndDate() + "=====" + filterDate.getTime());
							long diffinDays = daysBetween(learnerEnrollment.getEnrollmentEndDate(), filterDate.getTime());
							log.debug("Diff i ndays =====" + diffinDays);
							log.debug(learnerEnrollment.getEnrollmentStatus().toUpperCase());
							if(learnerEnrollment.getEnrollmentStatus().toUpperCase().equals(ACTIVE) &&
										((diffinDays>=0 && diffinDays<10 ) || (diffinDays >=10 && !learnerEnrollment.getCourseStatistics().getStatus().equalsIgnoreCase("COMPLETED") ) )
							) {
								log.debug("Here ========");
								trainingPlanCourseVo = new TrainingPlanCourseSoapVO();
								trainingPlanCourseVo.setUserName(learnerEnrollment.getLearner().getVu360User().getUsername());
								trainingPlanCourseVo.setEmail(learnerEnrollment.getLearner().getVu360User().getEmailAddress());
								trainingPlanCourseVo.setTitle(learnerEnrollment.getCourse().getCourseTitle());
								Date enrolmentEndDate = learnerEnrollment.getEnrollmentEndDate();
								
								trainingPlanCourseVo.setDueDate(sdf.format(enrolmentEndDate));
								
								trainingPlanCourseVo.setProgress(learnerEnrollment.getCourseStatistics().getPercentComplete());
								response.getTrainingplancourses().add(trainingPlanCourseVo);
							}	
						}

			}
				
		} catch(Exception e) {
			log.error("problem in finding Training Plans", e);

		}
		return response;
	}
	
	@Override
	@PayloadRoot(localPart = "GetAllTraingPlansByIdsRequest", namespace = MESSAGES_NAMESPACE)
	public GetAllTraingPlansByIdsResponse getAllTrainingPlansByIds( GetAllTraingPlansByIdsRequest request) {
		GetAllTraingPlansByIdsResponse response = new GetAllTraingPlansByIdsResponse();
		try {
			List<Long> trainingPlanIds = request.getTrainingPlanIds();
			if(trainingPlanIds !=null && trainingPlanIds.size()>0)
			{	
				TrainingPlanSoapVO trainingPlanSoapVo = null;
				
				for (Long trainingPlanId : trainingPlanIds) {
					TrainingPlan trainingPlan =  trainingPlanService.getTrainingPlanByID(trainingPlanId);
					if(trainingPlan !=null) {
						trainingPlanSoapVo = new TrainingPlanSoapVO();
						trainingPlanSoapVo.setTrainingPlanId(trainingPlan.getId());
						trainingPlanSoapVo.setTrainingPlanName(trainingPlan.getName());
						response.getTrainingplans().add(trainingPlanSoapVo);
					}	
				}
			}
				
			//response.setResultMessage("OK");
		} catch(Exception e) {
			log.error("problem in finding Training Plans", e);

		}
		return response;
	}
	
	@Override
	@PayloadRoot(localPart = "GetTrainingPlanAssignmentsByDateRequest", namespace = MESSAGES_NAMESPACE)
	public GetTrainingPlanAssignmentsByDateResponse getTrainingPlanAssignmentsByDate(GetTrainingPlanAssignmentsByDateRequest trainingPlanRequest){
		GetTrainingPlanAssignmentsByDateResponse response = new GetTrainingPlanAssignmentsByDateResponse();
		log.debug("getTrainingPlanAssignmentsByDate Web Service");
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = df.parse(trainingPlanRequest.getStartDate());
			Date endDate = df.parse(trainingPlanRequest.getEndDate());
			List<TrainingPlanAssignmentSoapVO> trainingPlanAssignmentSoapVOs =  trainingPlanService.getTraingPlanAssignmentsByTraingPlanIdAndDate(trainingPlanRequest.getTrainingPlanIds(), startDate, endDate);
			response.setTrainingPlanAssignments(trainingPlanAssignmentSoapVOs);
			log.debug(response.getTrainingPlanAssignments().size());
			log.error(response.getTrainingPlanAssignments().size());
		} catch(Exception e) {
			log.error("problem in finding Training Plans", e);

		}
		return response;
	}
	
	
	
	private List<LearnerEnrollment> GetTrainingPlanLearnerEnrollments(Customer customer)
	{
		List<TrainingPlan> lstTrainingPlan =  trainingPlanService.findTrainingPlanByCustomerId(customer);
		//TrainingPlanSoapVO trainingPlanSoapVo = null;
		List<TrainingPlanAssignment> trainingPlanAssignments;
		TrainingPlanCourseSoapVO trainingPlanCourseVo;
		//Date filterDate = new Date();
		List<LearnerEnrollment> learnerEnrollments = new ArrayList<LearnerEnrollment>();
		
		for (TrainingPlan trainingPlan : lstTrainingPlan) {
			trainingPlanAssignments =  trainingPlanService.getTraingPlanAssignmentsByTraingPlanId(trainingPlan.getId());
			for (TrainingPlanAssignment trainingPlanAssignment : trainingPlanAssignments) {
				learnerEnrollments.addAll(trainingPlanAssignment.getLearnerEnrollments());
			}
		}
		return learnerEnrollments;
	}
	
	public long daysBetween(final Date startDate, final Date endDate) {  
		log.debug("In daysBetween");
		long startTime = startDate.getTime();
		long endTime = endDate.getTime();
		long diffTime = endTime - startTime;
		long diffDays = diffTime / (1000 * 60 * 60 * 24);
		log.debug("Difference in date in number of days ==== " + diffDays);
		return diffDays;
	} 
	
	public TrainingPlanService getTrainingPlanService() {
		return trainingPlanService;
	}

	public void setTrainingPlanService(TrainingPlanService trainingPlanService) {
		this.trainingPlanService = trainingPlanService;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

}
