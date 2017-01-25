package com.softech.vu360.util;
 
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.softech.vu360.lms.model.SurveyLearner;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.vo.SaveSurveyParam;
import com.softech.vu360.lms.vo.SurveyVO;

/**
 * User: Adeel
 * Date: 01 March , 2010
 * @since LMS-4502
 */
public class AssignSurveyAsyncTask implements Runnable {
    private SaveSurveyParam saveSurveyParam;
    private SurveyService surveyService;
    private static final Logger log = Logger.getLogger(AssignSurveyAsyncTask.class.getName()); 
    
    public void run() {
        try {
        	SurveyVO surveyVO = surveyService.saveSurveyAssignment(saveSurveyParam);

            List<SurveyLearner> learnerSurveyList = surveyVO.getLearnerSurveyList();
            Brander brander=saveSurveyParam.getBrander();
            if (! learnerSurveyList.isEmpty()) {
                if( saveSurveyParam.isNotifyLearner()){
    				String loginURL = VU360Properties.getVU360Property("lms.loginURL") + "login.do";
    				LearnersToBeMailedService emailService=saveSurveyParam.getEmailService();
    				emailService.SendMailToServeyLearners(learnerSurveyList, loginURL, saveSurveyParam.getUser(), brander, saveSurveyParam);
    			}
            }
            if( saveSurveyParam.isNotifyManagerOnConfirmation()){
                Map<String,Object> model = saveSurveyParam.createHashMapModelForSurveyAssignmentManagerUserEmail();
                 
                String surveyAssignmentManagerTemplatePath =  brander.getBrandElement("lms.email.managerSurveyAssignment.body");
    			String fromAddress =  brander.getBrandElement("lms.email.managerSurveyAssignment.fromAddress");
    			String fromCommonName =  brander.getBrandElement("lms.email.managerSurveyAssignment.fromCommonName");
    			String subject =  brander.getBrandElement("lms.email.managerSurveyAssignment.subject");
    			String text = VelocityEngineUtils.mergeTemplateIntoString(saveSurveyParam.getVelocityEngine(), surveyAssignmentManagerTemplatePath, model);

    			SendMailService.sendSMTPMessage(saveSurveyParam.getUser().getEmailAddress(),	fromAddress, fromCommonName, subject, text);
            }
  
            Map model = saveSurveyParam.createHashMapModelForSurveyAssignmentManagerUserEmail();// saveSurveyParam.createHashMapModelForEnrollUserEmail(uniqueLearners, uniqueCourses, learnersFailedToEnroll, enrollmentsCreated, courseNumber, enrollmentsUpdated);
    		String surveyAssignmentManagerSummaryTemplatePath =   brander.getBrandElement("lms.email.managerSurveyAssignment.summary.body");
			String fromAddress =  brander.getBrandElement("lms.email.managerSurveyAssignment.fromAddress");
			String fromCommonName =  brander.getBrandElement("lms.email.managerSurveyAssignment.fromCommonName");
			String subject =  brander.getBrandElement("lms.email.managerSurveyAssignment.subject");
    		String text = VelocityEngineUtils.mergeTemplateIntoString(saveSurveyParam.getVelocityEngine(), surveyAssignmentManagerSummaryTemplatePath, model);

    		SendMailService.sendSMTPMessage( saveSurveyParam.getUser().getEmailAddress() ,	fromAddress, fromCommonName, subject, text);
            log.debug("TOTAL ENROLLS --  "+surveyVO.getLearnerSurveyList().size()+"--");
        } catch (ParseException e) {
        	log.error("exception", e); //do something smart here
        }
    }

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public void setSaveSurveyParam(SaveSurveyParam saveSurveyParam) {
		this.saveSurveyParam = saveSurveyParam;
	}
  
}
