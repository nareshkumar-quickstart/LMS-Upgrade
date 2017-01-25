package com.softech.vu360.lms.web.controller.learner;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.lms.service.TrainingPlanService;


public class SelfEnrollmentController implements Controller{

	private static final Logger log = Logger.getLogger(LaunchCourseController.class.getName());
	private String selfEnrollmentTemplate;
	private String mySyncCoursesTemplate;
	private String errorTemplate;
	private EnrollmentService enrollmentService;
	private CourseAndCourseGroupService courseAndCourseGroupService;
	private EntitlementService entitlementService;
	private TrainingPlanService trainingPlanService;
	private SynchronousClassService synchronousClassService;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService; 
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request,HttpServletResponse response) 
	throws Exception {

		try {
			
			com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			log.debug(user == null ? "User null" : " learnerId="+ user.getLearner());
			String courseId = request.getParameter("courseId");
			String synchronousClassId = request.getParameter("syncId");
			String subscriptionId = request.getParameter("subscriptionId");
			
			com.softech.vu360.lms.model.Learner learnerModel = new com.softech.vu360.lms.model.Learner();
			learnerModel.setId(user.getLearner().getId());
			
			Customer customerModel = new Customer();
			customerModel.setId(user.getLearner().getCustomer().getId());
			
			learnerModel.setCustomer(customerModel);
			
			VU360User vu360UserModel = new VU360User();
			vu360UserModel.setLearner(learnerModel);
			
			
			Map<Object, Object> context = new HashMap<Object, Object>();
			
			if(!StringUtils.isBlank(subscriptionId)){
				
				LearnerEnrollment le = enrollmentService.addSelfEnrollmentsForSubscription(learnerModel,subscriptionId , courseId);
				context.put("enrollment",le);
				return new ModelAndView(selfEnrollmentTemplate,"context", context);
			}
			
			if(StringUtils.isBlank(courseId)){
				context.put("error","Course Id Missing");
				log.debug("redirecting to "+errorTemplate);
				return new ModelAndView(errorTemplate,"context", context);
			}
			Map<Object, Object> returnVal = enrollmentService.selfEnrollTheLearner(vu360UserModel, courseId, synchronousClassId);
			log.debug("returnVal:"+returnVal);
			if(returnVal.get("error")!=null){
				log.warn("error occured in selfEnrollment..."+returnVal.get("error").toString());
				context.put("error",returnVal.get("error").toString());
				log.debug("redirecting to "+errorTemplate);
				return new ModelAndView(errorTemplate,"context", context);
			}
			if(returnVal.get("syncCourseEnrollment")!=null){
				log.debug("course is of type synchronous...courseId:"+courseId);
				context.put("enrollment",(LearnerEnrollment)returnVal.get("syncCourseEnrollment"));
				log.debug("redirecting to :"+selfEnrollmentTemplate);
				return new ModelAndView(selfEnrollmentTemplate,"context", context);
			}
			if(returnVal.get("courseEnrollment")!=null){
				log.debug("No Error, No Synch Course courseId:"+courseId);
				context.put("enrollment",(LearnerEnrollment)returnVal.get("courseEnrollment"));
				log.debug("redirecting to "+selfEnrollmentTemplate);
				return new ModelAndView(selfEnrollmentTemplate,"context", context);
			}

		}catch(Exception e){
			log.debug("exception", e);
		}

		return new ModelAndView(selfEnrollmentTemplate);
	}
	/**
	 * @param selfEnrollmentTemplate the selfEnrollmentTemplate to set
	 */
	public void setSelfEnrollmentTemplate(String selfEnrollmentTemplate) {
		this.selfEnrollmentTemplate = selfEnrollmentTemplate;
	}
	/**
	 * @return the enrollmentService
	 */
	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}
	/**
	 * @param enrollmentService the enrollmentService to set
	 */
	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}
	/**
	 * @return the courseAndCourseGroupService
	 */
	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}
	/**
	 * @param courseAndCourseGroupService the courseAndCourseGroupService to set
	 */
	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
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
	/**
	 * @return the selfEnrollmentTemplate
	 */
	public String getSelfEnrollmentTemplate() {
		return selfEnrollmentTemplate;
	}
	/**
	 * @return the trainingPlanService
	 */
	public TrainingPlanService getTrainingPlanService() {
		return trainingPlanService;
	}
	/**
	 * @param trainingPlanService the trainingPlanService to set
	 */
	public void setTrainingPlanService(TrainingPlanService trainingPlanService) {
		this.trainingPlanService = trainingPlanService;
	}

	public SynchronousClassService getSynchronousClassService() {
		return synchronousClassService;
	}
	public void setSynchronousClassService(
			SynchronousClassService synchronousClassService) {
		this.synchronousClassService = synchronousClassService;
	}
	public String getMySyncCoursesTemplate() {
		return mySyncCoursesTemplate;
	}
	public void setMySyncCoursesTemplate(String mySyncCoursesTemplate) {
		this.mySyncCoursesTemplate = mySyncCoursesTemplate;
	}	
	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}
	public void setOrgGroupLearnerGroupService(
			OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}
	public String getErrorTemplate() {
		return errorTemplate;
	}
	public void setErrorTemplate(String errorTemplate) {
		this.errorTemplate = errorTemplate;
	}
}
