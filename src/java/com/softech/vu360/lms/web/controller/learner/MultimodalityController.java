package com.softech.vu360.lms.web.controller.learner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.Subscription;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.SubscriptionService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.model.MultimodalityForm;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.LearnersToBeMailedService;
import com.softech.vu360.util.VU360Branding;

public class MultimodalityController  implements Controller {
	private static final Logger log = Logger.getLogger(MultimodalityController.class.getName());
	private String multiModalityPage = null;
	private SubscriptionService subscriptionService = null;
	private CourseAndCourseGroupService courseCourseGrpService = null;
	private LearnersToBeMailedService learnersToBeMailedService = null;
	private LearnerService learnerService = null;
	private VU360UserService vu360UserService = null;
	
	MultimodalityForm multimodalityForm = new MultimodalityForm();
	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		Map<Object, Object> context = new HashMap<Object, Object>();
		Map<Object, Object> errorMap = null;
		
		String action = request.getParameter("_frmAction");
		
		Brander brand=VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());	
		
		//learnerService.get
		/*
		if(action!=null && action.equals("New")){
		try{
			
			errorMap = this.validateMultimodalityForm(null,this.multimodalityForm);
			
			if(errorMap.get("error")==null) {
				
				String subscriberName = request.getParameter("subscriberName");
				String subscriberEmail = request.getParameter("subscriberEmail");
				String subscribtionName = request.getParameter("subscribtionName");
				String studentName = request.getParameter("studentName");
				String studentEmail = request.getParameter("studentEmail");
				String className = request.getParameter("className");
				
				context.put("modularityForm", this.multimodalityForm);
				return new ModelAndView("", "context", context);
				
			}
			else{
				context.put("status", errorMap);
				context.put("modularityForm", this.multimodalityForm);
			}
		}
		
		catch(Exception e){
			log.debug(e.getMessage());
		}
		
	  }*/
		
		
		
		if(action!=null && action.equals("New")){
			
			multimodalityForm = convertRequestToModel(request);
			
			errorMap = this.validateMultimodalityForm(null,this.multimodalityForm);
			
			if(errorMap.get("error")==null) {
				
				String subscriberName = request.getParameter("subscriberName");
				String subscriberEmail = request.getParameter("subscriberEmail");
				String subscriptionName = request.getParameter("subscribtionName");
				String studentName = request.getParameter("studentName");
				String studentEmail = request.getParameter("studentEmail");
				String studentPhone = request.getParameter("studentPhone");
				String className = request.getParameter("className");
				String studentComments = request.getParameter("comments");
				
				
				learnersToBeMailedService.SendMailforSubscriptionClassroomEnrollment(studentName,subscriberName,subscriberEmail,subscriptionName,studentEmail,studentPhone,className , studentComments , brand);
				context.put("emailSent", "1");
				return new ModelAndView(multiModalityPage, "context", context);
			}
			
			
				else{
					context.put("status", errorMap);
					context.put("newUserForm", this.multimodalityForm);
				}
			
			
		}
		else
		{
			multimodalityForm = fillModalityform(request);
			context.put("newUserForm", this.multimodalityForm);
		}
		return new ModelAndView(multiModalityPage, "context", context);
	}
	
	private MultimodalityForm convertRequestToModel(HttpServletRequest request){
		MultimodalityForm model = new MultimodalityForm();
		try{
			
			String subscriberName = request.getParameter("subscriberName");
			String subscriberEmail = request.getParameter("subscriberEmail");
			String subscribtionName = request.getParameter("subscribtionName");
			String studentName = request.getParameter("studentName");
			String studentEmail = request.getParameter("studentEmail");
			String className = request.getParameter("className");
			String studentComments = request.getParameter("comments");
			String studentPhone = request.getParameter("studentPhone");
			
			model.setStudentName(studentName);
			model.setStudentEmail(studentEmail);
			model.setSubscriptionName(subscribtionName);
			model.setClassName(className);
			model.setSubscriberName(subscriberName);
			model.setSubscriberEmail(subscriberEmail);
			model.setStudentPhone(studentPhone);

		}
		catch(Exception e){
			log.debug(e.getStackTrace());
		}
		
		return model;
	}
	
	private MultimodalityForm fillModalityform(HttpServletRequest request)
	{
		String strsubscriptionId = request.getParameter("subscriptionId");
		String strcourseId = request.getParameter("courseId");
		long courseId = Long.parseLong(strcourseId);
		String strSubscriberName = null;
		String strSubscriberEmail = null;
		String strCourseName = null;
		MultimodalityForm model = new MultimodalityForm();		
		long subscriptionId = Long.parseLong(strsubscriptionId);
		List<LMSRole> listOfRoles = new ArrayList <LMSRole>();
		
		
		
		com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Subscription subscription = subscriptionService.findSubscriptionById(subscriptionId);
		
		Course course = courseCourseGrpService.getCourseById(courseId);
		
		if(course != null)
			strCourseName = course.getCourseTitle();
		else
			strCourseName = "";
		com.softech.vu360.lms.model.Customer customerModel = new com.softech.vu360.lms.model.Customer();
		customerModel.setId(user.getLearner().getCustomer().getId());
		List<Learner> lstLearner = learnerService.getAllLearnersOfCustomer(customerModel);
		
		strSubscriberName = user.getFirstName()+ " " + user.getLastName();
		strSubscriberEmail = user.getEmailAddress() ;
		
		for(Learner selLearner: lstLearner)
		{
			listOfRoles = vu360UserService.getLMSRolesByUserById(selLearner.getVu360User().getId());
			for(LMSRole role :listOfRoles)
			{
				if(role.getRoleName().equals("MANAGER"))
				{
					strSubscriberName = selLearner.getVu360User().getFirstName() + " " + selLearner.getVu360User().getLastName();
					strSubscriberEmail = selLearner.getVu360User().getEmailAddress();
				}
			}
		}
		
		//listOfRolls = vu360UserService.findRolesByName("MANAGER", learner.getCustomer(), user);
		//vu360UserService.getLMSRolesByUserById(id);
		//vu360UserService.getAllRoles(customer, loggedInUser)
		//vu360UserService.getLMSRolesByUserById(id);
		
		
		
		
		//learnerService.getAllLearnersOfCustomer(learner.getCustomer());
		
		
/*		
		for(Learner lrn : lstLearner)
		{
			if(lrn.getVu360User().isManagerMode())
			{
				strSubscriberName = lrn.getVu360User().getFirstName() + "" + lrn.getVu360User().getLastName();
				strSubscriberEmail = lrn.getVu360User().getEmailAddress();
			}
		}
*/		
		model.setStudentName(user.getFirstName()+ " " + user.getLastName());
		model.setStudentEmail(user.getEmailAddress());
		model.setSubscriptionName(subscription.getSubscriptionName());
		model.setClassName(strCourseName);
		model.setSubscriberName(strSubscriberName);
		model.setSubscriberEmail(strSubscriberEmail);
		model.setStudentPhone(user.getLearner().getLearnerProfile().getMobilePhone());
		
		/*
		context.put("studentName", user.getFirstName()+ " " + user.getLastName());
		context.put("studentEmail", user.getEmailAddress());
		context.put("subscriptionName", subscription.getSubscriptionName());
		context.put("courseName", strCourseName);
		context.put("subscriberName", strSubscriberName);
		context.put("subscriberEmail", strSubscriberEmail);
		context.put("studentPhone", learner.getLearnerProfile().getMobilePhone());
		*/
		return model;
	}
	
	private Map<Object, Object> validateMultimodalityForm(Map<Object, Object> stat, MultimodalityForm modalityForm){
		
		
		List<String> errorList = new ArrayList<String>();
		Map<Object, Object> status = new HashMap<Object, Object>();
			// Email
			if(modalityForm.getSubscriberName().length()<1){
				errorList.add("error.lms.multimodality.subscribername.blank");
			}
			if(modalityForm.getSubscriberEmail().length()<1){
				errorList.add("error.lms.multimodality.subscriberemail.blank");
			}
			if(modalityForm.getSubscriptionName().length()<1){
				errorList.add("error.lms.multimodality.subscriptionname.blank");
			}

			if(modalityForm.getStudentName().length()<1){
				errorList.add("error.lms.multimodality.studentname.blank");
			}
			if(modalityForm.getStudentPhone().length()<1){
				errorList.add("error.lms.multimodality.studentphone.blank");
			}
			if(modalityForm.getStudentEmail().length()<1){
				errorList.add("error.lms.multimodality.studentemail.blank");
			}
			if(modalityForm.getClassName().length()<1){
				errorList.add("error.lms.multimodality.classname.blank");
			}
		
		if(errorList!=null && errorList.size()>0){
			status.put("error", true);
			status.put("errorCodes", errorList);
		}
		
        return status;
	}
	
	public String getMultiModalityPage() {
		return multiModalityPage;
	}
	public void setMultiModalityPage(String multiModalityPage) {
		this.multiModalityPage = multiModalityPage;
	}

	public SubscriptionService getSubscriptionService() {
		return subscriptionService;
	}

	public void setSubscriptionService(SubscriptionService subscriptionService) {
		this.subscriptionService = subscriptionService;
	}


	public LearnersToBeMailedService getLearnersToBeMailedService() {
		return learnersToBeMailedService;
	}

	public void setLearnersToBeMailedService(
			LearnersToBeMailedService learnersToBeMailedService) {
		this.learnersToBeMailedService = learnersToBeMailedService;
	}

	public CourseAndCourseGroupService getCourseCourseGrpService() {
		return courseCourseGrpService;
	}

	public void setCourseCourseGrpService(
			CourseAndCourseGroupService courseCourseGrpService) {
		this.courseCourseGrpService = courseCourseGrpService;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

}
