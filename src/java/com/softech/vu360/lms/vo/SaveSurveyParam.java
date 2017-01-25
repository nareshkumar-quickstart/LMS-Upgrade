package com.softech.vu360.lms.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.web.controller.model.SurveyItem;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.LearnersToBeMailedService;

/**
 *  Value object to hold necessary parameters to save enrollment
 *  @author Adeel
 *  @since LMS-4383
 */
public class SaveSurveyParam {
    private final List<SurveyItem> surveyItemList;
    private final List<Learner> learnersToBeAssigned;
    private final boolean modifyAllSurveys;
    private final String startDate;
    private final String endDate;
 
	private Date surveyStartDateObject =null;
	private Date surveyEndDateObject =null;
	
    private LearnersToBeMailedService emailService;
    private VU360User user;
    private Brander brander;
    private boolean notifyLearner;
    private boolean openSurvey;
    private boolean notifyManagerOnConfirmation;
    private boolean notifyMeOnLearnerSurveyCompletions;
     
    private VelocityEngine velocityEngine;
    
    public SaveSurveyParam(List<SurveyItem> surveyItemList, List<Learner> learnersToBeAssigned,
                               boolean modifyAllSurveys, String startDate, String endDate ,boolean emailOnCompletion , boolean openSurvey ) {
        this.surveyItemList = surveyItemList;
        this.learnersToBeAssigned = learnersToBeAssigned;
        this.modifyAllSurveys = modifyAllSurveys;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notifyMeOnLearnerSurveyCompletions = emailOnCompletion ;
        this.openSurvey = openSurvey;
    }

    public Map<String,Object> createHashMapModelForSurveyAssignmentManagerUserEmail() {
        Map<String,Object> model = new HashMap<String,Object>();
        //model.put("user", enrollUserEmailParam.getUser());
        model.put("user", getUser());
        model.put("totalLearners", learnersToBeAssigned.size());
        int selectedCourses = 0 ;
        List<Survey> surveyList = new ArrayList<Survey>(surveyItemList.size());
        for( SurveyItem surveyItem :surveyItemList) {
            if( surveyItem.isSelected()){
            	selectedCourses++ ;
            	surveyList.add(surveyItem.getSurvey());
            }
        }
        model.put("surveyList" ,surveyList);	
        model.put("totalSurveys", selectedCourses );          
 
        model.put("brander", brander); 
        String support = brander.getBrandElement("lms.email.SurveyAssignment.fromCommonName");
        model.put("support", support);
        
        String lmsDomain="";
        lmsDomain=FormUtil.getInstance().getLMSDomain(brander);
        model.put("lmsDomain",lmsDomain);
        
        return model;
    }

   

    public boolean isModifyAllSurveys() {
        return modifyAllSurveys;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
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

	/**
	 * @param notifyMeOnLearnerSurveyCompletions the notifyMeOnLearnerSurveyCompletions to set
	 */
	public void setNotifyMeOnLearnerSurveyCompletions(
			boolean notifyMeOnLearnerSurveyCompletions) {
		this.notifyMeOnLearnerSurveyCompletions = notifyMeOnLearnerSurveyCompletions;
	}

	/**
	 * @return the notifyMeOnLearnerSurveyCompletions
	 */
	public boolean isNotifyMeOnLearnerSurveyCompletions() {
		return notifyMeOnLearnerSurveyCompletions;
	}

	/**
	 * @return the surveyItemList
	 */
	public List<SurveyItem> getSurveyItemList() {
		return surveyItemList;
	}

	/**
	 * @return the learnersToBeAssigned
	 */
	public List<Learner> getLearnersToBeAssigned() {
		return learnersToBeAssigned;
	}

	/**
	 * @param openSurvey the openSurvey to set
	 */
	public void setOpenSurvey(boolean openSurvey) {
		this.openSurvey = openSurvey;
	}

	/**
	 * @return the openSurvey
	 */
	public boolean isOpenSurvey() {
		return openSurvey;
	}

	/**
	 * @param surveyStartDateObject the surveyStartDateObject to set
	 */
	public void setSurveyStartDateObject(Date surveyStartDateObject) {
		this.surveyStartDateObject = surveyStartDateObject;
	}

	/**
	 * @return the surveyStartDateObject
	 */
	public Date getSurveyStartDateObject() {
		return surveyStartDateObject;
	}

	/**
	 * @param surveyEndDateObject the surveyEndDateObject to set
	 */
	public void setSurveyEndDateObject(Date surveyEndDateObject) {
		this.surveyEndDateObject = surveyEndDateObject;
	}

	/**
	 * @return the surveyEndDateObject
	 */
	public Date getSurveyEndDateObject() {
		return surveyEndDateObject;
	}
}
