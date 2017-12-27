/**
 * 
 */
package com.softech.vu360.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.softech.vu360.lms.helpers.ProxyVOHelper;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerLMSFeature;
import com.softech.vu360.lms.model.CustomerPreferences;
import com.softech.vu360.lms.model.DistributorLMSFeature;
import com.softech.vu360.lms.model.DistributorPreferences;
import com.softech.vu360.lms.model.EnrollmentCourseView;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearnerPreferences;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyLearner;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CertificateService;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.SecurityAndRolesService;
import com.softech.vu360.lms.service.StatisticsService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.vo.SaveSurveyParam;
import com.softech.vu360.lms.web.controller.model.CourseGroupItem;
import com.softech.vu360.lms.web.controller.model.EnrollmentItem;
import com.softech.vu360.lms.web.controller.model.SubscriptionItemForm;
import com.softech.vu360.lms.web.controller.model.SurveyItem;

/**
 * @author tapas
 *
 */
public class LearnersToBeMailedService {
	
	
	private static final String EMAIL_MANAGER_COMPLETION_CERTIFICATES_FEATURE_CODE = "lms.permissions.manager.planAndEnroll.emailManagerCompletionCertificates.featureCode";
	private VelocityEngine velocityEngine=null;
	private static Logger log = Logger.getLogger(LearnersToBeMailedService.class.getName());
	private CourseAndCourseGroupService courseAndCourseGroupService=null;
	private CertificateService certificateService= null;
	private EntitlementService entitlementService= null;
	private StatisticsService statisticsService= null;
	
	@Autowired
	private VU360UserService vu360UserService;

	
	/**
	 * Security and roles service
	 * @see {@link SecurityAndRolesService}
	 */
	private SecurityAndRolesService securityAndRolesService = null;
	
	/**
	 * @return the velocityEngine
	 */
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	/**
	 * @param velocityEngine the velocityEngine to set
	 */
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}


	public boolean SendMailToLearners( List<LearnerEnrollment> learnerEnrollments,String url,VU360User username,Brander brander){
		return SendMailToLearners(learnerEnrollments, url, username, brander, null);
	}
	
	public boolean SendMailToLearners( List<LearnerEnrollment> learnerEnrollments,String url,VU360User username,Brander brander, List<EnrollmentCourseView> courseEnt){
		
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Map <Learner, List<CourseGroupItem>>learnerToBeMailed = new LinkedHashMap <Learner, List<CourseGroupItem> >();
		
		for (LearnerEnrollment learnerEnrollment :learnerEnrollments){
			
			//LMS-11283, Skip learner enrollment e-mails if the learner is not enabled for enrollment e-mails.
			if(learnerEnrollment!=null && learnerEnrollment.getLearner()!=null && learnerEnrollment.getLearner().getPreference()!=null 
					&& !learnerEnrollment.getLearner().getPreference().isEnrollmentEmailEnabled()) 
				continue;
			
			List<CourseGroup> courseGroups=null;
			Long courseGroupId = 0L;
			CourseGroup cg = null;
			if(courseEnt != null){
				EnrollmentCourseView ecv = null;
				for(int i=0 ; i<courseEnt.size() ; i++){
					ecv = courseEnt.get(i);
					/*if(ecv.getCourseId() == Long.valueOf(learnerEnrollment.getCourse().getId()) && 
					   ecv.getEnrollmentId() == learnerEnrollment.getId()){*/
					if(ecv.getCourseId().equals(learnerEnrollment.getCourse().getId()) && 
							ecv.getEnrollmentId() !=null && ecv.getEnrollmentId().equals(learnerEnrollment.getId())){
						courseGroupId = ecv.getCourseGroupId();
						break;
					}
				}
				if(courseGroupId!=null){
					cg = courseAndCourseGroupService.getCourseGroupById(courseGroupId);
				}
			}else{
				List<CourseGroup> cgList=courseAndCourseGroupService.getCourseGroupsForCourse(learnerEnrollment.getCourse());
				if(cgList!=null && cgList.size()>0){
					cg = courseAndCourseGroupService.getCourseGroupsForCourse(learnerEnrollment.getCourse()).get(0);
				}
			}
			 
			
			CourseGroupItem courseGroupItem=new CourseGroupItem();
			EnrollmentItem enrollmentItem = new EnrollmentItem();
			if (cg != null){
				courseGroupItem.setCourseGroupName(cg.getName());
				
				enrollmentItem.setCourseName(learnerEnrollment.getCourse().getCourseTitle());
				enrollmentItem.setEnrollmentEndDate(formatter.format(learnerEnrollment.getEnrollmentEndDate()));
				courseGroupItem.addEnrollmentItem(enrollmentItem);
			}else{
				courseGroupItem.setCourseGroupName("");
				
				enrollmentItem.setCourseName(learnerEnrollment.getCourse().getCourseTitle());
				enrollmentItem.setEnrollmentEndDate(formatter.format(learnerEnrollment.getEnrollmentEndDate()));
				courseGroupItem.addEnrollmentItem(enrollmentItem);
			}
			if(learnerToBeMailed.containsKey(learnerEnrollment.getLearner())){
				
				List<CourseGroupItem> courseGroupItems=learnerToBeMailed.get(learnerEnrollment.getLearner());
				int index=0;
				boolean courseGroupFound=false;
				for(index=0;index< courseGroupItems.size();index++){
					if (courseGroups != null && courseGroups.size()>0){
						if(courseGroupItems.get(index).getCourseGroupName().equalsIgnoreCase(cg.getName())){
							
							courseGroupItems.get(index).addEnrollmentItem(enrollmentItem);
							 courseGroupFound=true;
							learnerToBeMailed.put(learnerEnrollment.getLearner(), courseGroupItems);
							break;
						}
					}
					
				}
				
				if(courseGroupFound==false){
					courseGroupItems.add(courseGroupItem);
				}
			}else{
				List<CourseGroupItem> courseGroupItems=new ArrayList<CourseGroupItem>();
				courseGroupItems.add(courseGroupItem);
				learnerToBeMailed.put(learnerEnrollment.getLearner(), courseGroupItems);
				
				
			}
		}
			
		
		

		Set keys = learnerToBeMailed.keySet();
		Iterator It = keys.iterator();
		while (It.hasNext()) {
			/*Learner lrn = (Learner)(It.next());
			List<CourseGroupItem> courseGroupItems=learnerToBeMailed.get(lrn);
			Map model = new HashMap();
			//request.
			model.put("manager", username);
			model.put("learner", lrn.getVu360User());
			model.put("customer", lrn.getCustomer());
			model.put("loginUrl", url);
			model.put("courseGroupItems", courseGroupItems);
			//commented by Faisal A. Siddiqui July 21 09 for new branding
			//Brander brander= VU360Branding.getInstance().getBrander("default", new Language());

			String enrolmentTemplatePath =  brander.getBrandElement("lms.email.enrollment.body");
			String fromAddress =  brander.getBrandElement("lms.email.enrollment.fromAddress");
			String fromCommonName =  brander.getBrandElement("lms.email.enrollment.fromCommonName");
			String subject = brander.getBrandElement("lms.email.enrollment.subject");
			String support =  brander.getBrandElement("lms.email.enrollment.fromCommonName");
			model.put("support", support);
			
			String lmsDomain="";
			lmsDomain=FormUtil.getInstance().getLMSDomain(brander);
			model.put("lmsDomain",lmsDomain);
			model.put("brander", brander);
			
			String text = VelocityEngineUtils.mergeTemplateIntoString(
					velocityEngine, enrolmentTemplatePath, model);

			SendMailService.sendSMTPMessage(lrn.getVu360User().getEmailAddress(), 
					fromAddress, 
					fromCommonName, 
					subject, 
					text); 

			*/
			
			Learner lrn = (Learner)(It.next());
			List<CourseGroupItem> courseGroupItems=learnerToBeMailed.get(lrn);
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("url", VU360Properties.getVU360Property("lms.loginURL"));
			
			//Brander brander= VU360Branding.getInstance().getBrander("default", new Language());
            //Brander brander = VU360Branding.getInstance().getBranderByUser(request,vu360UserList.get(0));
            String batchImportTemplatePath = brander.getBrandElement("lms.email.enrollment.body");
            String fromAddress =  brander.getBrandElement("lms.email.enrollment.fromAddress");
			String subject = brander.getBrandElement("lms.email.enrollment.subject");
			String support =  brander.getBrandElement("lms.email.enrollment.fromCommonName");
		 
            String templateText=brander.getBrandElement("lms.branding.email.enrollmentNotice.templateText");			                
            
            templateText=templateText.replaceAll("&lt;learnerfirstname&gt;", lrn.getVu360User().getFirstName());
            templateText=templateText.replaceAll("&lt;learnerlastname&gt;", lrn.getVu360User().getLastName());			                
            templateText=templateText.replaceAll("&lt;managerfirstname&gt;", username.getFirstName());
            templateText=templateText.replaceAll("&lt;managerlastname&gt;", username.getLastName());
            templateText=templateText.replaceAll("&lt;customername&gt;", lrn.getCustomer().getName());

            
            
            StringBuffer courseGrouptxt = new StringBuffer();
            courseGrouptxt.append("<table border='0' width='800px' style='font:Verdana, Arial, Helvetica, sans-serif; font-size:13px;'>");
            for(CourseGroupItem courseGroupItem : courseGroupItems){
            	courseGrouptxt.append("<tr>");
            	courseGrouptxt.append("<td style='font-family:Verdana; font-size:13px;width:350px' width='350px'> COURSE GROUP: </td>");
            	if(courseGroupItem.getCourseGroupName()=="")
            		courseGrouptxt.append("<td style='font-family:Verdana; font-size:13px;width:450px' width='450px'> Miscellaneous </td>");
            	else
            		courseGrouptxt.append("<td style='font-family:Verdana; font-size:13px;width:450px' width='450px' > "+courseGroupItem.getCourseGroupName()+"</td>");
 
            	courseGrouptxt.append("</tr>");
            	
                for(EnrollmentItem course : courseGroupItem.getEnrollmentItems()){
                	courseGrouptxt.append("</tr>");
                	courseGrouptxt.append("<td style='font-family:Verdana; font-size:13px'>"+course.getCourseName()+"</td >");
                	courseGrouptxt.append("<td style='font-family:Verdana; font-size:13px'>"+course.getEnrollmentEndDate()+"</td>");
                	courseGrouptxt.append("</tr>");
                }
            }
            
        	courseGrouptxt.append("</table>");
        	            
            templateText=templateText.replaceAll("&lt;courselist&gt;", courseGrouptxt.toString());
        	
        	
            String lmsDomain=VU360Properties.getVU360Property("lms.domain");			                
            String loginUrl=lmsDomain.concat("/lms/login.do?brand=").concat(brander.getName());
            templateText=templateText.replaceAll("&lt;loginurl&gt;", loginUrl);
            
            model.put("brander", brander);
            model.put("support", support);
            model.put("templateText", templateText);
            String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, batchImportTemplatePath, model);
            SendMailService.sendSMTPMessage(lrn.getVu360User().getEmailAddress(),fromAddress,support,subject,text);
    
		}


		//learnersEnrolled = learnerEnrollments.size();


		//Send mail

	
		return true;
	}
	
	public boolean sendAffidavitReportedNotificationEmail(List<LearnerEnrollment> learnerEnrollments, Brander brander)	{
		
		Map<Learner, List<Course>> learnerToBeMailed = new LinkedHashMap<Learner, List<Course>>();
		
		for (LearnerEnrollment learnerEnrollment : learnerEnrollments) {			
			List<Course> courses = learnerToBeMailed.get(learnerEnrollment.getLearner());
			
			if(courses == null)
				courses = new ArrayList<Course>();
			
			courses.add(learnerEnrollment.getCourse());
			
			learnerToBeMailed.put(learnerEnrollment.getLearner(), courses);
		}
		
		Set<Learner> keys = learnerToBeMailed.keySet();
		Iterator<Learner> It = keys.iterator();
		while (It.hasNext()) {
			Learner lrn = (Learner)(It.next());
			List<Course> courses = learnerToBeMailed.get(lrn);
			
			StringBuffer coursestxt = new StringBuffer();
			coursestxt.append("<br /><table border='0' width='800px' style='font:Verdana, Arial, Helvetica, sans-serif; font-size:13px;'>");
			coursestxt.append("<tr>");
			coursestxt.append("<td style='font-family:Verdana; font-size:13px;width:350px' width='350px'><b> Course Name: </b></td>");
			//LMS-14542 - temporary remove this column because incorrect data is pulled and show in this column
			//coursestxt.append("<th style='font-family:Verdana; font-size:13px;width:350px' width='350px'> Course Approval Number: </th>");
			coursestxt.append("</tr>");
            for (Course course : courses) {
    			coursestxt.append("<tr>");
    			coursestxt.append("<td style='font-family:Verdana; font-size:13px;width:350px' width='350px'> " + course.getCourseTitle() + " </td>");
    			//LMS-14542 - temporary remove this column because incorrect data is pulled and show in this column
    			//coursestxt.append("<th style='font-family:Verdana; font-size:13px;width:350px' width='350px'> " + course.getApprovalNumber() + ": </th>");
    			coursestxt.append("</tr>");
			}
            coursestxt.append("</table><br /><br />");

			Map<String, Object> model = new HashMap<String, Object>();
			model.put("learnerfirstname", lrn.getVu360User().getFirstName());
			model.put("customerSupportContactNumber", brander.getBrandElement("lms.footerLinks.contactus.contactPhone"));
			model.put("courseNameAndApprovalNumberTable", coursestxt.toString());
			model.put("logoUrl", brander.getBrandElement("lms.email.reportednotification.footer.logo.src"));

			String templateLocation = brander.getBrandElement("lms.email.reportednotification.body");
			String fromAddress = brander.getBrandElement("lms.email.reportednotification.fromAddress");
			String support = brander.getBrandElement("lms.email.reportednotification.fromCommonName");
			String subject = brander.getBrandElement("lms.email.reportednotification.subject");
			
            String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateLocation, model);
            
            SendMailService.sendSMTPMessage(lrn.getVu360User().getEmailAddress(),fromAddress,support,subject,text);
		}
		
		return true;
	}
	

	public boolean SendMailToServeyLearners( List<SurveyLearner> learnerSurveys,String url,VU360User username,Brander brander ,SaveSurveyParam saveSurveyParam){
		
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Map <Learner, String> learnerToBeMailed = new LinkedHashMap <Learner, String >();
		List<Survey> selectedSurveyList = new ArrayList<Survey>();

		for (SurveyLearner learnerSurvey :learnerSurveys){ // creating a unique list of learners  , from SurveyLearner List
			
			if( ! learnerToBeMailed.containsKey(learnerSurvey.getLearner())){
				
				learnerToBeMailed.put(learnerSurvey.getLearner(), "-");
		
			}
		}
		
	 
		
		Set keys = learnerToBeMailed.keySet();
		Iterator It = keys.iterator();
		while (It.hasNext()) {
			Learner lrn = (Learner)(It.next());
			Map model = new HashMap();
			//request.
			model.put("manager", username);
			model.put("learner", lrn.getVu360User());
			model.put("customer", lrn.getCustomer());
			model.put("loginUrl", url);
			model.put("learnerSurveyItems", learnerSurveys);
			model.put("brander", brander); 
			model.put("saveSurveyParam", saveSurveyParam);
			 
			prepareDateObjects(saveSurveyParam);
			 
			String surveyAssignmentTemplatePath =  brander.getBrandElement("lms.email.SurveyAssignment.body");
			String fromAddress =  brander.getBrandElement("lms.email.SurveyAssignment.fromAddress");
			String fromCommonName =  brander.getBrandElement("lms.email.SurveyAssignment.fromCommonName");
			String subject = brander.getBrandElement("lms.email.SurveyAssignment.subject");
			String support =  brander.getBrandElement("lms.email.SurveyAssignment.fromCommonName");
			model.put("support", support);
			String lmsDomain="";
			lmsDomain=FormUtil.getInstance().getLMSDomain(brander);
			model.put("lmsDomain",lmsDomain);
			String text = VelocityEngineUtils.mergeTemplateIntoString(
					velocityEngine, surveyAssignmentTemplatePath, model);

			SendMailService.sendSMTPMessage(lrn.getVu360User().getEmailAddress(), 
					fromAddress, 
					fromCommonName, 
					subject, 
					text);
		}
 
		return true;
	}
	
	private void prepareDateObjects(SaveSurveyParam saveSurveyParam){
		if( ! saveSurveyParam.isOpenSurvey()){
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			if( saveSurveyParam.isModifyAllSurveys() ) {
		        
	    		try {
					Date surveyStartDate = formatter.parse(saveSurveyParam.getStartDate());
					Date surveyEndDate = formatter.parse(saveSurveyParam.getEndDate());
					saveSurveyParam.setSurveyStartDateObject(surveyStartDate);
					saveSurveyParam.setSurveyEndDateObject(surveyEndDate);
				} catch (ParseException e) {
					 
					log.error(e);
				}
	        	
	        	
			}
			else {
		         
	    		try {
	    			for(SurveyItem surveyItem : saveSurveyParam.getSurveyItemList()) {
	    				
	    				if( surveyItem.isSelected()) {
							Date surveyStartDate = formatter.parse(surveyItem.getSurveyStartDate());
							Date surveyEndDate = formatter.parse(surveyItem.getSurveyEndDate());
							surveyItem.setSurveyStartDateObject(surveyStartDate);
							surveyItem.setSurveyEndDateObject(surveyEndDate);
	    				}
	    			}
				} catch (ParseException e) {
					
					log.error(e);
				}
			}
		}
			
	}
public boolean SendMailToLearnersForTrainingPlan( List<LearnerEnrollment> learnerEnrollments,String url,VU360User username,String trainingPlanName,Brander brander)throws IOException{
		
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Map <Learner, List<LearnerEnrollment>>learnerToBeMailed = new LinkedHashMap <Learner, List<LearnerEnrollment> >();
		Map <String, List<EnrollmentItem>>mapCourseGroupItem = new LinkedHashMap <String, List<EnrollmentItem> >();
	
		for (LearnerEnrollment learnerEnrollment :learnerEnrollments){
			List<CourseGroup> courseGroups=null;
			
			if(learnerToBeMailed.containsKey(learnerEnrollment.getLearner())){
				
				List<LearnerEnrollment> learnerEnts=learnerToBeMailed.get(learnerEnrollment.getLearner());
				learnerEnts.add(learnerEnrollment);
				
				
			}else{
				List<LearnerEnrollment> learnerEnts=new ArrayList<LearnerEnrollment>();
				learnerEnts.add(learnerEnrollment);
				learnerToBeMailed.put(learnerEnrollment.getLearner(), learnerEnts);
				
				
			}
		}
			
		

		Set keys = learnerToBeMailed.keySet();
		Iterator It = keys.iterator();
		while (It.hasNext()) {
			Learner lrn = (Learner)(It.next());
			List<LearnerEnrollment> learnerEnts=learnerToBeMailed.get(lrn);
			Map model = new HashMap();
			//request.

			model.put("manager", username);
			model.put("learner", lrn.getVu360User());
			model.put("customer", lrn.getCustomer());
			
			model.put("trainingPlanName", trainingPlanName);
			model.put("loginUrl", url);
			model.put("learnerEnrollments", learnerEnrollments);
			//commented by Faisal A. Siddiqui July 21 09 for new branding
			//Brander brander= VU360Branding.getInstance().getBrander("default", new Language());

			String enrolmentTemplatePath =  brander.getBrandElement("lms.email.trainingPlan.body");
			String fromAddress =  brander.getBrandElement("lms.email.trainingPlan.fromAddress");
			String fromCommonName =  brander.getBrandElement("lms.email.trainingPlan.fromCommonName");
			String subject = brander.getBrandElement("lms.email.trainingPlan.subject");
			String support =  brander.getBrandElement("lms.email.enrollment.fromCommonName");
			model.put("support", support);
			
			String lmsDomain="";
			lmsDomain=FormUtil.getInstance().getLMSDomain(brander);
			model.put("lmsDomain",lmsDomain);
			model.put("brandeR",brander);
			
			String text = VelocityEngineUtils.mergeTemplateIntoString(
					velocityEngine, enrolmentTemplatePath, model);

			SendMailService.sendSMTPMessage(lrn.getVu360User().getEmailAddress(), 
					fromAddress, 
					fromCommonName, 
					subject, 
					text); 

			
		}


		//learnersEnrolled = learnerEnrollments.size();


		//Send mail

	
		return true;
	}

public boolean SendMailToLearnersForSubscription( Learner learner, List<SubscriptionItemForm> lstsubscription,List <LearnerEnrollment> learnerenrollment,String url,Brander brander)throws IOException{
		StringBuffer coursestxt = new StringBuffer();
		coursestxt.append("<br /><table border='0' width='800px' style='font-family:Verdana; font-size:11px;'>");
		for (SubscriptionItemForm subscription : lstsubscription) {
			coursestxt.append("<tr>");
			coursestxt.append("<td style='font-family:Verdana; font-size:11px;width:350px' width='350px'> <b>" + subscription.getSubscription().getSubscriptionName() + "</b> </td>");
			coursestxt.append("</tr>");
		}
        coursestxt.append("</table><br />");
		//commented by Faisal A. Siddiqui July 21 09 for new branding
		//Brander brander= VU360Branding.getInstance().getBrander("default", new Language());

        Map<String, Object> model = new HashMap<String, Object>();
		model.put("learnerfirstname", learner.getVu360User().getFirstName());
		model.put("customerSupportContactNumber", brander.getBrandElement("lms.footerLinks.contactus.contactPhone"));
		model.put("courseNameAndApprovalNumberTable", coursestxt.toString());
		model.put("logoUrl", brander.getBrandElement("lms.email.reportednotification.footer.logo.src"));
		model.put("distributorName", learner.getCustomer().getDistributor().getName());
		model.put("distributorPhoneNumber", learner.getCustomer().getDistributor().getOfficePhone());

		String templateLocation = brander.getBrandElement("lms.email.subscription.body");
		String fromAddress = brander.getBrandElement("lms.email.enrollment.fromAddress");
		String support = brander.getBrandElement("lms.email.enrollment.fromCommonName");
		String subject = brander.getBrandElement("lms.email.enrollment.subject");
		
        String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateLocation, model);
        
        SendMailService.sendSMTPMessage(learner.getVu360User().getEmailAddress(),fromAddress,support,subject,text);

	

	return true;
}

public boolean SendMailforSubscriptionClassroomEnrollment( String studentName, String subscriberName,String subscriberEmail,String subscriptionName,String studentEmail,String studentPhone,String className,String comments ,Brander brander)throws IOException{
	
	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	
	
		
		//commented by Faisal A. Siddiqui July 21 09 for new branding
		//Brander brander= VU360Branding.getInstance().getBrander("default", new Language());

        Map<String, Object> model = new HashMap<String, Object>();
		model.put("studentName", studentName);
		model.put("subscriberName",subscriberName);
		model.put("subscriberEmail",subscriberEmail);
		model.put("subscriptionName",subscriptionName);
		model.put("studentEmail",studentEmail);
		model.put("studentPhone",studentPhone);
		model.put("className",className);
		model.put("comments", comments);
		

		String templateLocation = brander.getBrandElement("lms.email.subscriptionClassroom.body");
		String toAddress = VU360Properties.getVU360Property("lms.email.subscriptionClassroom.supportEmailAddress");
		String fromAddress = brander.getBrandElement("lms.email.enrollment.fromAddress");
		String support = brander.getBrandElement("lms.email.enrollment.fromCommonName");
		String subject = brander.getBrandElement("lms.email.subscriptionClassroom.subject") + "" + studentName;
		
        String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateLocation, model);
        
        //SendMailService.sendSMTPMessage(learner.getVu360User().getEmailAddress(),fromAddress,support,subject,text);
        
        SendMailService.sendSMTPMessage(toAddress,studentEmail,"",subject ,text);

	

	return true;
}

public boolean SendMailToLearnersForLaunchingInvalidIp( Learner learner,String [] to,String [] bcc,String from,String subject,String message,Brander brander,String strLaunchDate)throws IOException{
	
    Map<String, Object> model = new HashMap<String, Object>();
    model.put("learnerfirstname", learner.getVu360User().getFirstName());
    model.put("learnerlastname", learner.getVu360User().getLastName());
    model.put("launchdatetime", strLaunchDate);

    String templateLocation = brander.getBrandElement("lms.secureIp.email.body");
	String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateLocation, model);
    
    SendMailService.sendSMTPMessage(to, null, bcc, from, null, subject, text, null, null, null, null);
    
    //SendMailService.sendSMTPMessage(learner.getVu360User().getEmailAddress(),fromAddress,support,subject,text);
    
    return true;
}
	

	public boolean emailCourseCompletionCertificate(Long learnerEnrollmentId) {			 
		return emailCourseCompletionCertificate(learnerEnrollmentId, true);
	}
	/**
	* If permissions allow, this method emails completion certificate to learner. 
	*/
	public boolean emailCourseCompletionCertificate(Long learnerEnrollmentId , boolean isSelfReported) {			 
			try {			 				
				LearnerEnrollment le = entitlementService.getLearnerEnrollmentById(learnerEnrollmentId);
				VU360User user = le.getLearner().getVu360User();
				Customer customer = le.getLearner().getCustomer();
				LearnerPreferences learnerPref=user.getLearner().getPreference(); 
				CustomerPreferences custPref= customer.getCustomerPreferences(); 
				DistributorPreferences distPref= customer.getDistributor().getDistributorPreferences();
				LearnerCourseStatistics lcs= statisticsService.loadForUpdateLearnerCourseStatistics(le.getCourseStatistics().getId());
			 if(!le.getCourse().getCourseType().equalsIgnoreCase("DFC") && /*lcs.isCompleted()*/ lcs.isCourseCompleted()&& 
				  learnerPref.isCourseCompletionCertificateEmailEnabled() &&
				  custPref.isCourseCompletionCertificateEmailEnabled() &&  distPref.isCourseCompletionCertificateEmailEnabled()  ){					
					
					Language lang = new Language();lang.setLanguage(Language.DEFAULT_LANG);
					Brander brander = VU360Branding.getInstance().getBranderByUser(null, ProxyVOHelper.setUserProxy(user));
					Map<String, Object> model = new HashMap <String, Object>();			
														
					String certificateTemplatePath =  brander.getBrandElement("lms.email.completionCertificate.body");
					
					String certificateToManagerTemplatePath =  brander.getBrandElement("lms.email.completionCertificate.toManager.body");
					String managerEmailSubject =  String.format("%s %s %s", le.getLearner().getVu360User().getFirstName(), le.getLearner().getVu360User().getLastName(), brander.getBrandElement("lms.email.completionCertificate.toManager.subject"));
					String fromAddress =  brander.getBrandElement("lms.email.completionCertificate.fromAddress");
					String fromCommonName =  brander.getBrandElement("lms.email.completionCertificate.fromCommonName");
					String subject =  brander.getBrandElement("lms.email.completionCertificate.subject")+"-"+le.getCourse().getCourseTitle();
					String support =  brander.getBrandElement("lms.email.completionCertificate.fromCommonName");
											
					model.put("brander", brander);			
					model.put("user",user);
					model.put("manager", le.getLearner().getCustomer());
					model.put("course",le.getCourse());
					model.put("support", support);			
					String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,certificateTemplatePath,model);
					String managerEmailBody = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,certificateToManagerTemplatePath,model);
					String fileName= le.getCourse().getCourseTitle()+"_"+learnerEnrollmentId+".pdf";
					String certificateURL = lcs.getCertificateURL()!=null && lcs.getCertificateURL().length()>0 ? lcs.getCertificateURL():null;
					
					if (certificateURL==null)
					{
						certificateURL= certificateService.getCertificateURL(learnerEnrollmentId);
					}
					/*If we are generating a certificate we'll have to delete that certificate after sending it.*/
					
					ByteArrayInputStream byteArrayInputStream= null;
					ByteArrayOutputStream byteArrayOS = null;
					if(certificateURL!= null && certificateURL.trim().length()>0){				
						byteArrayInputStream= certificateService.fetchCertificateFromURL(certificateURL, le);
						certificateService.updateCertificateIssueDate(le.getCourseStatistics());
					}else{										
						byteArrayOS = certificateService.generateCertificate(le, isSelfReported);
						byteArrayInputStream = new ByteArrayInputStream(certificateService.generateCertificate(le, isSelfReported).toByteArray());
																		        				        
					}
					SendMailService.sendSMTPMessage(user.getEmailAddress(), fromAddress,fromCommonName, subject, text,byteArrayInputStream,fileName);
					
					boolean customerFeatureEmailToManagerEnable = false, resellerFeatureEmailToManaggerEnable = false;
					
					String emailToManagerFeatureCode = "";
					
					if(VU360Properties.getVU360Property(EMAIL_MANAGER_COMPLETION_CERTIFICATES_FEATURE_CODE) != null) {
						emailToManagerFeatureCode = VU360Properties.getVU360Property(EMAIL_MANAGER_COMPLETION_CERTIFICATES_FEATURE_CODE).toString();
					}
					
					if(!StringUtils.isEmpty(emailToManagerFeatureCode)) {
					
						String featureCode = "";
						String roleType = LMSRole.getRoleType(1);
						
						List<CustomerLMSFeature> customerLMSFeatures = securityAndRolesService.getCustomerLMSFeatures(le.getLearner().getCustomer(), roleType);
						List<DistributorLMSFeature> distributorLMSFeatures = securityAndRolesService.getDistributorLMSFeatures(le.getLearner().getCustomer().getDistributor(), roleType);
						
						for(CustomerLMSFeature feature: customerLMSFeatures) {
							featureCode = feature.getLmsFeature().getFeatureCode();
							if(!StringUtils.isBlank(featureCode) 
									&& feature.getEnabled() 
									&& emailToManagerFeatureCode.equalsIgnoreCase(featureCode)) {
								customerFeatureEmailToManagerEnable = feature.getEnabled();
							}
						}
						
						for(DistributorLMSFeature feature: distributorLMSFeatures) {
							featureCode = feature.getLmsFeature().getFeatureCode();
							if(!StringUtils.isBlank(featureCode) 
									&& feature.getEnabled() 
									&& emailToManagerFeatureCode.equalsIgnoreCase(featureCode)) {
								resellerFeatureEmailToManaggerEnable = feature.getEnabled();
							}
						}
						
												
						
						if(resellerFeatureEmailToManaggerEnable && customerFeatureEmailToManagerEnable) {
							
							List<VU360User> users = vu360UserService.findTrainingAdministratorsOfUser(user.getId());
							
							Set<String> managersEmailAddresses = new HashSet<>();
							
							for(VU360User u: users) {
								
								model.put("manager", u);
								
								String emailBody = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, certificateToManagerTemplatePath, model);
								
								managersEmailAddresses.add(u.getEmailAddress());
								
								byteArrayInputStream.reset();
								
								SendMailService.sendSMTPMessage(
										u.getEmailAddress(), fromAddress,fromCommonName, 
										managerEmailSubject, 
										emailBody, byteArrayInputStream, fileName
										);
								
							}
							
							if(!managersEmailAddresses.contains(customer.getEmail())) {
								
								byteArrayInputStream.reset();
								
								SendMailService.sendSMTPMessage(
										customer.getEmail(), fromAddress,fromCommonName, 
										managerEmailSubject, managerEmailBody, 
										byteArrayInputStream, fileName
										);
							}
						}
						
					}
					
					if(byteArrayInputStream!=null) byteArrayInputStream.close();
					if(byteArrayOS!=null) byteArrayOS.close(); 
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
				return false;
			}
			return true;
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

	public StatisticsService getStatisticsService() {
		return statisticsService;
	}

	public void setStatisticsService(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	/**
	 * Return securityAndRoleService
	 * @return securityAndRoleServic
	 * @see {@link SecurityAndRolesService}
	 */
	public SecurityAndRolesService getSecurityAndRolesService() {
		return securityAndRolesService;
	}
	
	/**
	 * Set securityAndRoleService
	 * @param securityAndRolesService
	 * @see {@link SecurityAndRolesService}
	 */
	public void setSecurityAndRolesService(SecurityAndRolesService securityAndRolesService) {
		this.securityAndRolesService = securityAndRolesService;
	}
	
}
