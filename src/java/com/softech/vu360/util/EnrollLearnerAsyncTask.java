package com.softech.vu360.util;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.TrainingPlanAssignment;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.TrainingPlanService;
import com.softech.vu360.lms.vo.EnrollmentVO;
import com.softech.vu360.lms.vo.SaveEnrollmentParam;
import com.softech.vu360.lms.web.controller.manager.ManageEnrollmentController;

/**
 * User: joong
 * Date: Dec 3, 2009
 * @since LMS-3212
 */
public class EnrollLearnerAsyncTask implements Runnable {
    private SaveEnrollmentParam saveEnrollmentParam;
    private EnrollmentService enrollmentService;
    private TrainingPlanService trainingPlanService;
   

	private static final Logger log = Logger.getLogger(EnrollLearnerAsyncTask.class.getName());
    public void run() {
        try {
        	EnrollmentVO enrollmentVO = this.enrollmentService.processEnrollments(saveEnrollmentParam);

        	log.debug("IN process EnrollLearnerAsyncTask *11 " + saveEnrollmentParam.getUser().getId());
            List<LearnerEnrollment> learnerEnrollments = enrollmentVO.getLearnerEnrollments();
            Brander brander=saveEnrollmentParam.getBrander();
            if (saveEnrollmentParam.isEnableEnrollmentEmailsForNewCustomers() && !learnerEnrollments.isEmpty()) {
                if(saveEnrollmentParam.isNotifyLearner()){//LEARNER EMAIL
                	log.debug("IN process EnrollLearnerAsyncTask (isNotifyLearner) *12 " + saveEnrollmentParam.getUser().getFirstName());
    				String loginURL = VU360Properties.getVU360Property("lms.loginURL") + "login.do";
    				LearnersToBeMailedService emailService=saveEnrollmentParam.getEmailService();
    				emailService.SendMailToLearners(learnerEnrollments, loginURL, saveEnrollmentParam.getUser(), brander, saveEnrollmentParam.getCourseEntItems());
    				log.debug("IN process EnrollLearnerAsyncTask (isNotifyLearner mail sent) *12 " + enrollmentVO);
    			}
            }
            HashSet<Learner> uniqueLearners = enrollmentVO.getUniqueLearners();
            HashSet<Course> uniqueCourses = enrollmentVO.getUniqueCourses();
            List<Learner> learnersFailedToEnroll = enrollmentVO.getLearnersFailedToEnroll();
            int enrollmentsCreated = enrollmentVO.getEnrollmentsCreated();
            int courseNumber = enrollmentVO.getCourseNumber();
            int enrollmentsUpdated = enrollmentVO.getEnrollmentsUpdated();
            
           
            try{
            	if(saveEnrollmentParam.getTrainingPlan()!=null){
            		
            		log.debug("IN process EnrollLearnerAsyncTask (getTrainingPlan ) *13 " + enrollmentVO);
            		TrainingPlanAssignment tpa = new TrainingPlanAssignment();
        			tpa.setTrainingPlan(saveEnrollmentParam.getTrainingPlan());
        			tpa.setLearnerEnrollments(enrollmentVO.getLearnerEnrollments());
        			if(enrollmentVO.getEnrollmentsToBeModified()!=null && enrollmentVO.getEnrollmentsToBeModified().size()>0){
        				//tpa.getLearnerEnrollments().addAll(enrollmentVO.getEnrollmentsToBeModified());
        				for(LearnerEnrollment le : enrollmentVO.getEnrollmentsToBeModified()){
        					log.debug("IN process EnrollLearnerAsyncTask (getTrainingPlan ) *14 " + le.getId());
        					if(!tpa.getLearnerEnrollments().contains(le)){
        						log.debug("IN process EnrollLearnerAsyncTask (learnerEnrollment Add to trainingPlan ) *15 " + le.getId());
        						tpa.getLearnerEnrollments().add(le);
        					}
        				}
        			}
        			log.debug("IN process EnrollLearnerAsyncTask (trainingPlan add ) *15 " + tpa.getId());
        			trainingPlanService.addTrainingPlanAssignments(tpa);
        			
            	}
            }
            catch(Exception ex){
            	log.error("Error in Saving TrainingPlanAssignment: " + ex.getMessage());
            	log.debug("IN process EnrollLearnerAsyncTask *16 " + ex.getMessage());
            }
            
            /**
             * LMS-7920
             * @author sultan.mubasher
             */
            if(saveEnrollmentParam.isEnableEnrollmentEmailsForNewCustomers() ) {
            	
            	log.debug("IN process EnrollLearnerAsyncTask  (isEnableEnrollmentEmailsForNewCustomers) *17 " );
            	if( saveEnrollmentParam.isNotifyManagerOnConfirmation() ) {//MANAGER EMAIL
            		log.debug("IN process EnrollLearnerAsyncTask  (MANAGER EMAILED) *18 "+ saveEnrollmentParam.getUser().getEmailAddress() );
	                Map model = saveEnrollmentParam.createHashMapModelForEnrollUserEmail(uniqueLearners, uniqueCourses, learnersFailedToEnroll, enrollmentsCreated, courseNumber, enrollmentsUpdated);
	                
	                String enrolmentTemplatePath =  brander.getBrandElement("lms.email.managerenrollment.body");
	    			String fromAddress =  brander.getBrandElement("lms.email.managerenrollment.fromAddress");
	    			String fromCommonName =  brander.getBrandElement("lms.email.managerenrollment.fromCommonName");
	    			String subject =  brander.getBrandElement("lms.email.managerenrollment.subject");
	    			String text = VelocityEngineUtils.mergeTemplateIntoString(saveEnrollmentParam.getVelocityEngine(), enrolmentTemplatePath, model);
	
	    			SendMailService.sendSMTPMessage(saveEnrollmentParam.getUser().getEmailAddress(),	fromAddress, fromCommonName, subject, text);
            	}
	            // CUSTOMER EMAIL
	            //send email to  customer 
	            // 	LMS-3490
    			
            	log.debug("IN process EnrollLearnerAsyncTask  (Customer EMAILED) *19 " );
	            Map modelCustomer = saveEnrollmentParam.createHashMapModelForEnrollUserEmail(uniqueLearners, uniqueCourses, learnersFailedToEnroll, enrollmentsCreated, courseNumber, enrollmentsUpdated);
	    		String enrolmentTemplatePathCustomer =  brander.getBrandElement("lms.email.managerenrollment.summary.body");
	    		String fromAddressCustomer =  brander.getBrandElement("lms.email.managerenrollment.fromAddress");
	    		String fromCommonNameCustomer =  brander.getBrandElement("lms.email.managerenrollment.fromCommonName");
	    		String subjectCustomer =  brander.getBrandElement("lms.email.managerenrollment.subject");
	    		String textCustomer = VelocityEngineUtils.mergeTemplateIntoString(saveEnrollmentParam.getVelocityEngine(), enrolmentTemplatePathCustomer, modelCustomer);
	
	    		SendMailService.sendSMTPMessage(saveEnrollmentParam.getUser().getEmailAddress(),	fromAddressCustomer, fromCommonNameCustomer, subjectCustomer, textCustomer);
	    		log.debug("IN process EnrollLearnerAsyncTask  (Customer EMAILED) *20 "+saveEnrollmentParam.getUser().getEmailAddress() );
            }
    		
        } catch (Exception e) {
            e.printStackTrace();
        	log.error(e);
        }
    }

    public void setEnrollmentService(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    public SaveEnrollmentParam getSaveEnrollmentParam() {
        return saveEnrollmentParam;
    }

    public void setSaveEnrollmentParam(SaveEnrollmentParam saveEnrollmentParam) {
        this.saveEnrollmentParam = saveEnrollmentParam;
    }

    public TrainingPlanService getTrainingPlanService() {
		return trainingPlanService;
	}

	public void setTrainingPlanService(TrainingPlanService trainingPlanService) {
		this.trainingPlanService = trainingPlanService;
	}

}
