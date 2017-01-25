package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.SurveyResult;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.lms.web.controller.model.survey.Survey;

/**
 * @author Dyutiman
 * created on 31st May 2010.
 *
 */
public class SurveyApprovalForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	public static final String SURVEY_STATUS_ALL = "All";
	public static final String SURVEY_STATUS_Published = "Published";
	public static final String SURVEY_STATUS_Unpublished = "Unpublished";
	private String selectedAll[] ;
	List<SurveyResult> surveyResults = new ArrayList<SurveyResult>();
	List<SurveyResult> selectedsurveyResults = new ArrayList<SurveyResult>();
	List<SurveyFlagAndSurveyResult> surveyResultsAndFlags = new ArrayList<SurveyFlagAndSurveyResult>();
	List<SurveyFlagAndSurveyResult> selectedsurveyFlagAndsurveyResult = new ArrayList<SurveyFlagAndSurveyResult>();
	
	private Survey survey;
	private long surveyId = 0;
	private long learnerId = 0;
	private long srId = 0;
	private VU360User user;
	private SurveyResult surveyResult;
	
	
	
	private String surveyName = "";
	private String Status = "";
	private String firstName = "";
	private String lastName = "";
	private String userName = "";
	
	private Learner learner;

	public Survey getSurvey() {
		return survey;
	}
	public void setSurvey(Survey survey) {
		this.survey = survey;
	}
	/**
	 * @return the learnerId
	 */
	public long getLearnerId() {
		return learnerId;
	}
	/**
	 * @param learnerId the learnerId to set
	 */
	public void setLearnerId(long learnerId) {
		this.learnerId = learnerId;
	}
	/**
	 * @return the user
	 */
	public VU360User getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(VU360User user) {
		this.user = user;
	}
	/**
	 * @return the surveyId
	 */
	public long getSurveyId() {
		return surveyId;
	}
	/**
	 * @param surveyId the surveyId to set
	 */
	public void setSurveyId(long surveyId) {
		this.surveyId = surveyId;
	}
	/**
	 * @return the surveyResult
	 */
	public SurveyResult getSurveyResult() {
		return surveyResult;
	}
	/**
	 * @param surveyResult the surveyResult to set
	 */
	public void setSurveyResult(SurveyResult surveyResult) {
		this.surveyResult = surveyResult;
	}
	/**
	 * @return the surveyName
	 */
	public String getSurveyName() {
		return surveyName;
	}
	/**
	 * @param surveyName the surveyName to set
	 */
	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return Status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		Status = status;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the srId
	 */
	public long getSrId() {
		return srId;
	}
	/**
	 * @param srId the srId to set
	 */
	public void setSrId(long srId) {
		this.srId = srId;
	}
	/**
	 * @return the learner
	 */
	public Learner getLearner() {
		return learner;
	}
	/**
	 * @param learner the learner to set
	 */
	public void setLearner(Learner learner) {
		this.learner = learner;
	}
	public String[] getSelectedAll() {
		return selectedAll;
	}
	public void setSelectedAll(String[] selectedAll) {
		this.selectedAll = selectedAll;
	}
	public List<SurveyResult> getSelectedsurveyResults() {
		return selectedsurveyResults;
	}
	public void setSelectedsurveyResults(List<SurveyResult> selectedsurveyResults) {
		this.selectedsurveyResults = selectedsurveyResults;
	}
	public List<SurveyResult> getSurveyResults() {
		return surveyResults;
	}
	public void setSurveyResults(List<SurveyResult> surveyResults) {
		this.surveyResults = surveyResults;
	}
	public List<SurveyFlagAndSurveyResult> getSurveyResultsAndFlags() {
		return surveyResultsAndFlags;
	}
	public void setSurveyResultsAndFlags(
			List<SurveyFlagAndSurveyResult> surveyResultsAndFlags) {
		this.surveyResultsAndFlags = surveyResultsAndFlags;
	}
	public List<SurveyFlagAndSurveyResult> getSelectedsurveyFlagAndsurveyResult() {
		return selectedsurveyFlagAndsurveyResult;
	}
	public void setSelectedsurveyFlagAndsurveyResult(
			List<SurveyFlagAndSurveyResult> selectedsurveyFlagAndsurveyResult) {
		this.selectedsurveyFlagAndsurveyResult = selectedsurveyFlagAndsurveyResult;
	}
	
	
}