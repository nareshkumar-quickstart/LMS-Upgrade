package com.softech.vu360.lms.vo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.EnrollmentCourseView;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.TrainingPlan;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.LearnersToBeMailedService;

/**
 *  Value object to hold necessary parameters to save enrollment
 *  @author joong
 *  @since LMS-3212
 */
public class SaveEnrollmentParam {
    private final List<EnrollmentCourseView> courseEntItems;
    private final List<Learner> learnersToBeEnrolled;
    private final boolean modifyAllEntitlements;
    private final String startDate;
    private final String endDate;
    private final boolean duplicates;
    private LearnersToBeMailedService emailService;
    private VU360User user;
    private Brander brander;
    private boolean notifyLearner;
    private boolean notifyManagerOnConfirmation;
    private VelocityEngine velocityEngine;
    
	public boolean isEnableEnrollmentEmailsForNewCustomers() {
		return enableEnrollmentEmailsForNewCustomers;
	}

	public void setEnableEnrollmentEmailsForNewCustomers(
			boolean enableEnrollmentEmailsForNewCustomers) {
		this.enableEnrollmentEmailsForNewCustomers = enableEnrollmentEmailsForNewCustomers;
	}

	private boolean enableEnrollmentEmailsForNewCustomers = false;
    //private boolean fromTrainingPlan=false;
    private TrainingPlan trainingPlan=null;
    
    

	

	public SaveEnrollmentParam(List<EnrollmentCourseView> courseEntItems, List<Learner> learnersToBeEnrolled,
                               boolean modifyAllEntitlements, String startDate, String endDate, boolean duplicates) {
        this.courseEntItems = courseEntItems;
        this.learnersToBeEnrolled = learnersToBeEnrolled;
        this.modifyAllEntitlements = modifyAllEntitlements;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duplicates = duplicates;
    }

    public Map createHashMapModelForEnrollUserEmail(HashSet<Learner> uniqueLearners,HashSet<Course> uniqueCourses,List<Learner> learnersFailedToEnroll,int enrollmentsCreated,int courseNumber,int enrollmentsUpdated ) {
        Map model = new HashMap();
        //model.put("user", enrollUserEmailParam.getUser());
        model.put("user", getUser());
        //model.put("learnersAttemptedToEnroll", enrollUserEmailParam.getLearnersToBeEnrolled().size());
        model.put("learnersAttemptedToEnroll", learnersToBeEnrolled.size());
        if(uniqueLearners!=null && uniqueLearners.size()>0)
            model.put("learnersEnrolledSucssessfully", uniqueLearners.size());
        else
            model.put("learnersEnrolledSucssessfully", 0);
        if(uniqueCourses !=null && uniqueCourses.size()>0)
            model.put("courses", uniqueCourses);
        else
            model.put("courses", 0);

        model.put("coursesAssigned", uniqueCourses.size());
        model.put("enrollmentsCreated", enrollmentsCreated);
        model.put("learnersNotEnrolled", learnersFailedToEnroll);
        model.put("enrollmentsUpdated", enrollmentsUpdated);
        model.put("brander", brander);//to fix LMS-3214
        String support = brander.getBrandElement("lms.email.managerenrollment.fromCommonName");
        model.put("support", support);
        
        String lmsDomain="";
        lmsDomain=FormUtil.getInstance().getLMSDomain(brander);
        model.put("lmsDomain",lmsDomain);
        
        /*START-BRANDING EMAILTEMPLATE WORK*/
        int successCount= uniqueLearners!=null&&uniqueLearners.size()>=0?uniqueLearners.size():0;        
        int coursecount= uniqueCourses!=null&&uniqueCourses.size()>=0?uniqueCourses.size():0;
		String templateText=brander.getBrandElement("lms.branding.email.enrollmentResult.templateText");			                            		 			
		templateText=templateText.replaceAll("&lt;attemptcount&gt;", String.valueOf(learnersToBeEnrolled.size()));			                
        templateText=templateText.replaceAll("&lt;successcount&gt;",String.valueOf(successCount));	                     			                            
        templateText=templateText.replaceAll("&lt;coursecount&gt;", String.valueOf(coursecount));                                                
        templateText=templateText.replaceAll("&lt;enrollmentcount&gt;", String.valueOf(enrollmentsCreated));
        templateText=templateText.replaceAll("&lt;errorcount&gt;", String.valueOf(learnersFailedToEnroll.size()));        
           
        model.put("templateText", templateText);
		/*END BRANDING EMAIL TEMPLATE WORK*/	
        return model;
    }

    
    public List<EnrollmentCourseView> getCourseEntItems() {
        return courseEntItems;
    }

    public List<Learner> getLearnersToBeEnrolled() {
        return learnersToBeEnrolled;
    }

    public boolean isModifyAllEntitlements() {
        return modifyAllEntitlements;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public boolean isDuplicates() {
        return duplicates;
    }

	public LearnersToBeMailedService getEmailService() {
		return emailService;
	}

	public void setEmailService(LearnersToBeMailedService emailService) {
		this.emailService = emailService;
	}

	public VU360User getUser() {
		return user;
	}

	public void setUser(VU360User user) {
		this.user = user;
	}

	public Brander getBrander() {
		return brander;
	}

	public void setBrander(Brander brander) {
		this.brander = brander;
	}

	public boolean isNotifyLearner() {
		return notifyLearner;
	}

	public void setNotifyLearner(boolean notifyLearner) {
		this.notifyLearner = notifyLearner;
	}

	public boolean isNotifyManagerOnConfirmation() {
		return notifyManagerOnConfirmation;
	}

	public void setNotifyManagerOnConfirmation(boolean notifyManagerOnConfirmation) {
		this.notifyManagerOnConfirmation = notifyManagerOnConfirmation;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public TrainingPlan getTrainingPlan() {
		return trainingPlan;
	}

	public void setTrainingPlan(TrainingPlan trainingPlan) {
		this.trainingPlan = trainingPlan;
	}
}
