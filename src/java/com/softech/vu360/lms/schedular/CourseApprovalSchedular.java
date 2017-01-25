package com.softech.vu360.lms.schedular;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.softech.vu360.lms.autoAlertGenerator.MailConfigProperties;
import com.softech.vu360.lms.exception.LMSSchedulerException;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.vo.ExpireCourseApprovalVO;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.SendMailService;


public class CourseApprovalSchedular implements Scheduler {

	private static final Logger log = Logger.getLogger(CourseApprovalSchedular.class.getName());
	
	private AccreditationService accreditationService;
	private EnrollmentService enrollmentService;
	private VelocityEngine velocityEngine=null;
	
	public void execute() throws LMSSchedulerException {
		
		List<LearnerEnrollment> lstLearnerEnrollments = null;
		List<ExpireCourseApprovalVO> model = new ArrayList<ExpireCourseApprovalVO>();
		ExpireCourseApprovalVO expireCourseApprovalVO = new ExpireCourseApprovalVO();
		try 
		{
			//Get Active course approvals that expires in 7 days.
			List<CourseApproval> lstCourseApproval  = accreditationService.getCourseApprovalsByDateRange(new Date(), FormUtil.formatToDayEnd(DateUtils.addDays(new Date(), 7)));
			
			//Check these course approvals in Learning session.
			if(lstCourseApproval  != null && lstCourseApproval.size() > 0)
			{
				for(CourseApproval approval : lstCourseApproval)
				{
					lstLearnerEnrollments = enrollmentService.getLearerEnrollmentsByCourse(approval.getCourse().getId());
					
					expireCourseApprovalVO.setCourseApprovalName(approval.getApprovedCourseName());
					expireCourseApprovalVO.setCourseApprovalStartDate(approval.getCourseApprovalEffectivelyStartDate());
					expireCourseApprovalVO.setCourseApprovalExpirationDate(approval.getCourseApprovalEffectivelyEndsDate());
					expireCourseApprovalVO.setEnrollments(lstLearnerEnrollments);
					
					model.add(expireCourseApprovalVO);
				}
				notifyLearner(model);
			}
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			log.debug(ex);
		}
	}
	
	/**
	 * get learner from enrollment and notify them
	 * @param learningSession
	 */
	private void notifyLearner(List<ExpireCourseApprovalVO> expireCourseApprovalVOs) {
		
		Map<String, Object> model = new HashMap <String, Object>();
		if(expireCourseApprovalVOs != null && expireCourseApprovalVOs.size() > 0)
		{
			for(ExpireCourseApprovalVO expireCourseApprovalVO : expireCourseApprovalVOs)
			{
				for(LearnerEnrollment enrollment : expireCourseApprovalVO.getEnrollments()){
					model.put("courseName", enrollment.getCourse().getCourseTitle());
					model.put("courseApprovalName", expireCourseApprovalVO.getCourseApprovalName());
					model.put("courseApprovalStartDate", expireCourseApprovalVO.getCourseApprovalStartDate());
					model.put("courseApprovalEndDate", expireCourseApprovalVO.getCourseApprovalExpirationDate());
					model.put("learnerFirstName", enrollment.getLearner().getVu360User().getFirstName());
					model.put("learnerLastName", enrollment.getLearner().getVu360User().getFirstName());
					model.put("emailAddress", enrollment.getLearner().getVu360User().getEmailAddress());
					emailLeaner(model);
				}
			}
		}
	}

	private void emailLeaner(Map<String, Object> model) {

		String expireCourseApprovalTemplatePath =  MailConfigProperties.getMailProperty("lms.email.courseApprovalSchedular.body");
		String fromAddress =  MailConfigProperties.getMailProperty("lms.email.courseApprovalSchedular.fromAddress");
		String fromCommonName =  MailConfigProperties.getMailProperty("lms.email.courseApprovalSchedular.fromCommonName");
		String subject =  MailConfigProperties.getMailProperty("lms.email.courseApprovalSchedular.subject");
		String support =  MailConfigProperties.getMailProperty("lms.email.courseApprovalSchedular.fromCommonName");
		String templateText=MailConfigProperties.getMailProperty("lms.email.courseApprovalSchedular.templateText");
		
		templateText=templateText.replaceAll("&lt;courseName&gt;",(String)model.get("courseName"));			                
		templateText=templateText.replaceAll("&lt;startDate&gt;",(String)String.valueOf(model.get("courseApprovalStartDate")));
		templateText=templateText.replaceAll("&lt;expirationDate&gt;",(String)String.valueOf(model.get("courseApprovalEndDate")));
        templateText=templateText.replaceAll("&lt;firstname&gt;",(String)model.get("learnerFirstName"));            			                            
        model.put("templateText", templateText);
		model.put("support", support);

		String body = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, expireCourseApprovalTemplatePath, model);
		
		SendMailService.sendSMTPMessage((String)model.get("emailAddress"), fromAddress, fromCommonName, subject, body);
	}

	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}


	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	

}
