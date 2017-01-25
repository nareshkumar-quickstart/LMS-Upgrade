package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.AggregateSurveyQuestion;
import com.softech.vu360.lms.model.AggregateSurveyQuestionItem;
import com.softech.vu360.lms.model.MultipleSelectSurveyQuestion;
import com.softech.vu360.lms.model.PersonalInformationSurveyQuestion;
import com.softech.vu360.lms.model.SingleSelectSurveyQuestion;
import com.softech.vu360.lms.model.SuggestedTraining;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyAnswerItem;
import com.softech.vu360.lms.model.SurveyFlagTemplate;
import com.softech.vu360.lms.model.SurveyQuestion;
import com.softech.vu360.lms.model.SurveyResult;
import com.softech.vu360.lms.model.TextBoxSurveyQuestion;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author Saptarshi
 *
 */
public class AdminSurveyForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	public AdminSurveyForm() {
	}

	private String surveyName = null;
	private boolean published = true;
	private boolean locked = true;
	private String surveyEvent = ""; //hack to avoid the error on vm
	private boolean allQuestionPerPage = true;
	private String questionsPerPage;
	private List<SurveyQuestion> surveyQuestionList = new ArrayList<SurveyQuestion>();
	private List<SurveyQuestion> surveyQuestions = new ArrayList<SurveyQuestion>();
	private List<SurveyQuestion> editableSurveyQuestions = new ArrayList<SurveyQuestion>();
	private List<Boolean> deleteableQuestions = new ArrayList<Boolean>();
	private List<Boolean> deleteableCourses = new ArrayList<Boolean>();
	private List<Long> answeredSurveyQyestionId = new ArrayList<Long>();
	private String surveyQuestionType = "";
	private TextBoxSurveyQuestion currentFillInTheBlankSurveyQuestion;
	private MultipleSelectSurveyQuestion currentMultipleSelectSurveyQuestion;
	private SingleSelectSurveyQuestion currentSingleSelectSurveyQuestion;
	private PersonalInformationSurveyQuestion currentPersonalInformationQuestion;
	private String surveyQuestionText = null;
	private boolean surveyQuestionRequired = false;
	
	private List<String> responseCheck = new ArrayList <String>();
	

	public List<String> getResponseCheck() {
		return responseCheck;
	}
	public void setResponseCheck(List<String> responseCheck) {
		this.responseCheck = responseCheck;
	}

	private List<AggregateSurveyQuestionItem> aggregateSurveyQuestionItem = new ArrayList<AggregateSurveyQuestionItem>();
	private AggregateSurveyQuestion currentCustomSurveyQuestion;
	private List<String> customQuestionResponceTypes = new ArrayList <String>();
	
	//For Personal Information Type Question
	private List<String> responceLabels = new ArrayList <String>();
	private List<String> answerChoices = new ArrayList <String>();
	private List<Boolean> isMultiline = new ArrayList <Boolean>();
	private List<Boolean> resRequired = new ArrayList <Boolean>();
	private List<ManagePersonalInformation> mngPersonalInfos = new ArrayList<ManagePersonalInformation>();
	
	private String searchType = ""; //Initialization....very very important
	private String searchCourseName = null;
	private String searchCourseID = null;
	private String searchKeyword = null;
	private String simpleSearchKey = null;
	
	private List<Survey> selectedSurveys = new ArrayList<Survey>(); // for storing selected surveys to show their training analysis details.
	private List<SurveyCourse> surveyCourses = new ArrayList<SurveyCourse>();
	
	// For personal information
	private List<String> personalInfos = new ArrayList<String>();
	private List<Boolean> resposeRequired = new ArrayList<Boolean>();
	
	private long sid = 0;  //for edit survey.
	private long editableQuestionId  = 0;  //for edit survey.
	private long questionId  = 0;
	public long getQuestionId() {
		return questionId;
	}
	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	//For Learner's Survey Response
	private String firstName="";
	private String lastName="";
	private String mailAddress="";
	private long learnerId = 0;
	private int pageIndex = 0;
	private List<Long> surveyQuestionsToBeDeletedFromDB = new ArrayList<Long>();
	private int displayQuestionOrder = 0;
	private boolean rememberPriorResponse=false;
	private boolean allowAnonymousResponse = false;
	private boolean electronicSignature = false;
	private boolean links = false;
	private boolean editable = false;
	private String electronicSignatureValue = "";
	private String linksValue = "";
	private boolean readOnly = false;
	
	//For surveyFlagTemplates
	private String searchedFlagName = "";
	private SurveyFlagTemplate surveyFlagTemplate = new SurveyFlagTemplate();
	private List<SurveyFlagTemplate> surveyFlagTemplates = new ArrayList<SurveyFlagTemplate>();
	private List<ManageFlag> mngFlags = new ArrayList<ManageFlag>();
	private long flagId = 0;  //for edit survey.
	private long selectedQuestionId=0;
	private List<SurveyAnswerItem> surveyAnswers = new ArrayList<SurveyAnswerItem>();
	private List<SurveyQuestion> flagSurveyQuestions = new ArrayList<SurveyQuestion>();
	private String[] selectedAnswerItems;
	
	//For Survey Analyze Individuals
	private List<SurveyResult> surveyResults = new ArrayList<SurveyResult>();
	private long srId = 0;
	private com.softech.vu360.lms.web.controller.model.survey.Survey survey;
	private SurveyResult surveyResult;
	
	//For Create Training Plan Analysis
	private List<SuggestedTraining> suggTrainings = null;
	
	
	public List<Boolean> getDeleteableCourses() {
		return deleteableCourses;
	}
	public void setDeleteableCourses(List<Boolean> deleteableCourses) {
		this.deleteableCourses = deleteableCourses;
	}

	public String getElectronicSignatureValue() {
		return electronicSignatureValue;
	}
	public void setElectronicSignatureValue(String electronicSignatureValue) {
		this.electronicSignatureValue = electronicSignatureValue;
	}
	public String getLinksValue() {
		return linksValue;
	}
	public void setLinksValue(String linksValue) {
		this.linksValue = linksValue;
	}
	public boolean isLinks() {
		return links;
	}
	public void setLinks(boolean links) {
		this.links = links;
	}
	public boolean isElectronicSignature() {
		return electronicSignature;
	}
	public void setElectronicSignature(boolean electronicSignature) {
		this.electronicSignature = electronicSignature;
	}
	public boolean isAllowAnonymousResponse() {
		return allowAnonymousResponse;
	}
	public void setAllowAnonymousResponse(boolean allowAnonymousResponse) {
		this.allowAnonymousResponse = allowAnonymousResponse;
	}
	public boolean isRememberPriorResponse() {
		return rememberPriorResponse;
	}
	public void setRememberPriorResponse(boolean rememberPriorResponse) {
		this.rememberPriorResponse = rememberPriorResponse;
	}
	/**
	 * @return the allQuestionPerPage
	 */
	public boolean isAllQuestionPerPage() {
		return allQuestionPerPage;
	}
	/**
	 * @param allQuestionPerPage the allQuestionPerPage to set
	 */
	public void setAllQuestionPerPage(boolean allQuestionPerPage) {
		this.allQuestionPerPage = allQuestionPerPage;
	}
	/**
	 * @return the published
	 */
	public boolean isPublished() {
		return published;
	}
	/**
	 * @param published the published to set
	 */
	public void setPublished(boolean published) {
		this.published = published;
	}
	/**
	 * @return the questionsPerPage
	 */
	public String getQuestionsPerPage() {
		return questionsPerPage;
	}
	/**
	 * @param questionsPerPage the questionsPerPage to set
	 */
	public void setQuestionsPerPage(String questionsPerPage) {
		this.questionsPerPage = questionsPerPage;
	}
	/**
	 * @return the surveyEvent
	 */
	public String getSurveyEvent() {
		return surveyEvent;
	}
	/**
	 * @param surveyEvent the surveyEvent to set
	 */
	public void setSurveyEvent(String surveyEvent) {
		this.surveyEvent = surveyEvent;
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
	 * @return the currentFillInTheBlankSurveyQuestion
	 */
	public TextBoxSurveyQuestion getCurrentFillInTheBlankSurveyQuestion() {
		return currentFillInTheBlankSurveyQuestion;
	}
	/**
	 * @param currentFillInTheBlankSurveyQuestion the currentFillInTheBlankSurveyQuestion to set
	 */
	public void setCurrentFillInTheBlankSurveyQuestion(
			TextBoxSurveyQuestion currentFillInTheBlankSurveyQuestion) {
		this.currentFillInTheBlankSurveyQuestion = currentFillInTheBlankSurveyQuestion;
	}
	/**
	 * @return the currentMultipleSelectSurveyQuestion
	 */
	public MultipleSelectSurveyQuestion getCurrentMultipleSelectSurveyQuestion() {
		return currentMultipleSelectSurveyQuestion;
	}
	/**
	 * @param currentMultipleSelectSurveyQuestion the currentMultipleSelectSurveyQuestion to set
	 */
	public void setCurrentMultipleSelectSurveyQuestion(
			MultipleSelectSurveyQuestion currentMultipleSelectSurveyQuestion) {
		this.currentMultipleSelectSurveyQuestion = currentMultipleSelectSurveyQuestion;
	}
	/**
	 * @return the currentSingleSelectSurveyQuestion
	 */
	public SingleSelectSurveyQuestion getCurrentSingleSelectSurveyQuestion() {
		return currentSingleSelectSurveyQuestion;
	}
	/**
	 * @param currentSingleSelectSurveyQuestion the currentSingleSelectSurveyQuestion to set
	 */
	public void setCurrentSingleSelectSurveyQuestion(
			SingleSelectSurveyQuestion currentSingleSelectSurveyQuestion) {
		this.currentSingleSelectSurveyQuestion = currentSingleSelectSurveyQuestion;
	}

	/**
	 * @return the surveyQuestions
	 */
	public List<SurveyQuestion> getSurveyQuestions() {
		return surveyQuestions;
	}
	/**
	 * @param surveyQuestions the surveyQuestions to set
	 */
	public void setSurveyQuestions(List<SurveyQuestion> surveyQuestions) {
		this.surveyQuestions = surveyQuestions;
	}
	/**
	 * @return the deleteableQuestions
	 */
	public List<Boolean> getDeleteableQuestions() {
		return deleteableQuestions;
	}
	/**
	 * @param deleteableQuestions the deleteableQuestions to set
	 */
	public void setDeleteableQuestions(List<Boolean> deleteableQuestions) {
		this.deleteableQuestions = deleteableQuestions;
	}
	
	/**
	 * 
	 * @return the selected question type 
	 */
	public String getSurveyQuestionType() {
		return surveyQuestionType;
	}
	
	/**
	 * 
	 * @param surveyQuestionTypes the selected question type to set
	 */
	public void setSurveyQuestionType(String surveyQuestionType) {
		this.surveyQuestionType = surveyQuestionType;
	}
	/**
	 * @return the searchType
	 */
	public String getSearchType() {
		return searchType;
	}
	/**
	 * @param searchType the searchType to set
	 */
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	/**
	 * @return the searchCourseID
	 */
	public String getSearchCourseID() {
		return searchCourseID;
	}
	/**
	 * @param searchCourseID the searchCourseID to set
	 */
	public void setSearchCourseID(String searchCourseID) {
		this.searchCourseID = searchCourseID;
	}
	/**
	 * @return the searchCourseName
	 */
	public String getSearchCourseName() {
		return searchCourseName;
	}
	/**
	 * @param searchCourseName the searchCourseName to set
	 */
	public void setSearchCourseName(String searchCourseName) {
		this.searchCourseName = searchCourseName;
	}
	/**
	 * @return the searchKeyword
	 */
	public String getSearchKeyword() {
		return searchKeyword;
	}
	/**
	 * @param searchKeyword the searchKeyword to set
	 */
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
	/**
	 * @return the simpleSearchKey
	 */
	public String getSimpleSearchKey() {
		return simpleSearchKey;
	}
	/**
	 * @param simpleSearchKey the simpleSearchKey to set
	 */
	public void setSimpleSearchKey(String simpleSearchKey) {
		this.simpleSearchKey = simpleSearchKey;
	}
	/**
	 * @return the surveyCourses
	 */
	public List<SurveyCourse> getSurveyCourses() {
		return surveyCourses;
	}
	/**
	 * @param surveyCourses the surveyCourses to set
	 */
	public void setSurveyCourses(List<SurveyCourse> surveyCourses) {
		this.surveyCourses = surveyCourses;
	}
	public long getSid() {
		return sid;
	}
	public void setSid(long sid) {
		this.sid = sid;
	}
		
	public void reset() {
		
		surveyName = null;
		published = false;
		surveyEvent = ""; //hack to avoid the error on vm
		allQuestionPerPage = true;
		questionsPerPage = null;
		
		surveyQuestions = new ArrayList<SurveyQuestion>();
		//deleteableQuestions;
		surveyQuestionType = "";
		currentFillInTheBlankSurveyQuestion = null;
		currentMultipleSelectSurveyQuestion = null;
		currentSingleSelectSurveyQuestion = null;
		
		searchType=""; //initilaization....very very important
		searchCourseName = null;
		searchCourseID = null;
		searchKeyword = null;
		simpleSearchKey = null;
		
		rememberPriorResponse=false;
		allowAnonymousResponse = false;
		electronicSignature = false;
		links = false;
		electronicSignatureValue = "";
		linksValue = "";
		
		surveyCourses = new ArrayList<SurveyCourse>();
	}
	/**
	 * @return the editableQuestionId
	 */
	public long getEditableQuestionId() {
		return editableQuestionId;
	}
	/**
	 * @param editableQuestionId the editableQuestionId to set
	 */
	public void setEditableQuestionId(long editableQuestionId) {
		this.editableQuestionId = editableQuestionId;
	}
	/**
	 * @return the editableSurveyQuestions
	 */
	public List<SurveyQuestion> getEditableSurveyQuestions() {
		return editableSurveyQuestions;
	}
	/**
	 * @param editableSurveyQuestions the editableSurveyQuestions to set
	 */
	public void setEditableSurveyQuestions(
			List<SurveyQuestion> editableSurveyQuestions) {
		this.editableSurveyQuestions = editableSurveyQuestions;
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
	 * @return the mailAddress
	 */
	public String getMailAddress() {
		return mailAddress;
	}
	/**
	 * @param mailAddress the mailAddress to set
	 */
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
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
	 * @return the pageIndex
	 */
	public int getPageIndex() {
		return pageIndex;
	}
	/**
	 * @param pageIndex the pageIndex to set
	 */
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	
	/**
	 * @return the displayQuestionOrder
	 */
	public int getDisplayQuestionOrder() {
		return displayQuestionOrder;
	}
	/**
	 * @param displayQuestionOrder the displayQuestionOrder to set
	 */
	public void setDisplayQuestionOrder(int displayQuestionOrder) {
		this.displayQuestionOrder = displayQuestionOrder;
	}
	/**
	 * @return the surveyQuestionText
	 */
	public String getSurveyQuestionText() {
		return surveyQuestionText;
	}
	/**
	 * @param surveyQuestionText the surveyQuestionText to set
	 */
	public void setSurveyQuestionText(String surveyQuestionText) {
		this.surveyQuestionText = surveyQuestionText;
	}
	/**
	 * @return the locked
	 */
	public boolean isLocked() {
		return locked;
	}
	/**
	 * @param locked the locked to set
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	/**
	 * @param surveyQuestionRequired the surveyQuestionRequired to set
	 */
	public void setSurveyQuestionRequired(boolean surveyQuestionRequired) {
		this.surveyQuestionRequired = surveyQuestionRequired;
	}
	/**
	 * @return the surveyQuestionRequired
	 */
	public boolean isSurveyQuestionRequired() {
		return surveyQuestionRequired;
	}
	/**
	 * @return the searchedFlagName
	 */
	public String getSearchedFlagName() {
		return searchedFlagName;
	}
	/**
	 * @param searchedFlagName the searchedFlagName to set
	 */
	public void setSearchedFlagName(String searchedFlagName) {
		this.searchedFlagName = searchedFlagName;
	}
	/**
	 * @return the surveyFlagTemplates
	 */
	public List<SurveyFlagTemplate> getFlags() {
		return surveyFlagTemplates;
	}
	/**
	 * @param surveyFlagTemplates the surveyFlagTemplates to set
	 */
	public void setFlags(List<SurveyFlagTemplate> surveyFlagTemplates) {
		this.surveyFlagTemplates = surveyFlagTemplates;
	}
	/**
	 * @return the surveyFlagTemplate
	 */
	public SurveyFlagTemplate getFlag() {
		return surveyFlagTemplate;
	}
	/**
	 * @param surveyFlagTemplate the surveyFlagTemplate to set
	 */
	public void setFlag(SurveyFlagTemplate surveyFlagTemplate) {
		this.surveyFlagTemplate = surveyFlagTemplate;
	}
	/**
	 * @return the mngFlags
	 */
	public List<ManageFlag> getMngFlags() {
		return mngFlags;
	}
	/**
	 * @param mngFlags the mngFlags to set
	 */
	public void setMngFlags(List<ManageFlag> mngFlags) {
		this.mngFlags = mngFlags;
	}
	/**
	 * @return the flagId
	 */
	public long getFlagId() {
		return flagId;
	}
	/**
	 * @param flagId the flagId to set
	 */
	public void setFlagId(long flagId) {
		this.flagId = flagId;
	}
	public List<Survey> getSelectedSurveys() {
		return selectedSurveys;
	}
	public void setSelectedSurveys(List<Survey> selectedSurveys) {
		this.selectedSurveys = selectedSurveys;
	}
	public void setSelectedQuestionId(long selectedQuestionId) {
		this.selectedQuestionId = selectedQuestionId;
	}
	public long getSelectedQuestionId() {
		return selectedQuestionId;
	}
	public void setSurveyAnswers(List<SurveyAnswerItem> surveyAnswers) {
		this.surveyAnswers = surveyAnswers;
	}
	public List<SurveyAnswerItem> getSurveyAnswers() {
		return surveyAnswers;
	}
	public List<SurveyQuestion> getFlagSurveyQuestions() {
		return flagSurveyQuestions;
	}
	public void setFlagSurveyQuestions(List<SurveyQuestion> flagSurveyQuestions) {
		this.flagSurveyQuestions = flagSurveyQuestions;
	}
	public void setSelectedAnswerItems(String[] selectedAnswerItems) {
		this.selectedAnswerItems = selectedAnswerItems;
	}
	public String[] getSelectedAnswerItems() {
		return selectedAnswerItems;
	}
	public AggregateSurveyQuestion getCurrentCustomSurveyQuestion() {
		return currentCustomSurveyQuestion;
	}
	public void setCurrentCustomSurveyQuestion(
			AggregateSurveyQuestion currentCustomSurveyQuestion) {
		this.currentCustomSurveyQuestion = currentCustomSurveyQuestion;
	}
	public List<String> getCustomQuestionResponceTypes() {
		return customQuestionResponceTypes;
	}
	public void setCustomQuestionResponceTypes(
			List<String> customQuestionResponceTypes) {
		this.customQuestionResponceTypes = customQuestionResponceTypes;
	}
	public List<String> getResponceLabels() {
		return responceLabels;
	}
	public void setResponceLabels(List<String> responceLabels) {
		this.responceLabels = responceLabels;
	}
	public List<String> getAnswerChoices() {
		return answerChoices;
	}
	public void setAnswerChoices(List<String> answerChoices) {
		this.answerChoices = answerChoices;
	}
	public List<Boolean> getIsMultiline() {
		return isMultiline;
	}
	public void setIsMultiline(List<Boolean> isMultiline) {
		this.isMultiline = isMultiline;
	}
	public List<Boolean> getResRequired() {
		return resRequired;
	}
	public void setResRequired(List<Boolean> resRequired) {
		this.resRequired = resRequired;
	}
	/**
	 * @return the aggregateSurveyQuestionItem
	 */
	public List<AggregateSurveyQuestionItem> getAggregateSurveyQuestionItem() {
		return aggregateSurveyQuestionItem;
	}
	/**
	 * @param aggregateSurveyQuestionItem the aggregateSurveyQuestionItem to set
	 */
	public void setAggregateSurveyQuestionItem(
			List<AggregateSurveyQuestionItem> aggregateSurveyQuestionItem) {
		this.aggregateSurveyQuestionItem = aggregateSurveyQuestionItem;
	}
	public PersonalInformationSurveyQuestion getCurrentPersonalInformationQuestion() {
		return currentPersonalInformationQuestion;
	}
	public void setCurrentPersonalInformationQuestion(
			PersonalInformationSurveyQuestion currentPersonalInformationQuestion) {
		this.currentPersonalInformationQuestion = currentPersonalInformationQuestion;
	}
	public List<String> getPersonalInfos() {
		return personalInfos;
	}
	public void setPersonalInfos(List<String> personalInfos) {
		this.personalInfos = personalInfos;
	}
	public List<Boolean> getResposeRequired() {
		return resposeRequired;
	}
	public void setResposeRequired(List<Boolean> resposeRequired) {
		this.resposeRequired = resposeRequired;
	}
	/**
	 * @return the surveyResults
	 */
	public List<SurveyResult> getSurveyResults() {
		return surveyResults;
	}
	/**
	 * @param surveyResults the surveyResults to set
	 */
	public void setSurveyResults(List<SurveyResult> surveyResults) {
		this.surveyResults = surveyResults;
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
	 * @return the survey
	 */
	public com.softech.vu360.lms.web.controller.model.survey.Survey getSurvey() {
		return survey;
	}
	/**
	 * @param survey the survey to set
	 */
	public void setSurvey(
			com.softech.vu360.lms.web.controller.model.survey.Survey survey) {
		this.survey = survey;
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
	 * @return the suggTrainings
	 */
	public List<SuggestedTraining> getSuggTrainings() {
		return suggTrainings;
	}
	/**
	 * @param suggTrainings the suggTrainings to set
	 */
	public void setSuggTrainings(List<SuggestedTraining> suggTrainings) {
		this.suggTrainings = suggTrainings;
	}
	/**
	 * @return the mngPersonalInfos
	 */
	public List<ManagePersonalInformation> getMngPersonalInfos() {
		return mngPersonalInfos;
	}
	/**
	 * @param mngPersonalInfos the mngPersonalInfos to set
	 */
	public void setMngPersonalInfos(List<ManagePersonalInformation> mngPersonalInfos) {
		this.mngPersonalInfos = mngPersonalInfos;
	}
	/**
	 * @return the readOnly
	 */
	public boolean isReadOnly() {
		return readOnly;
	}
	/**
	 * @param readOnly the readOnly to set
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	/**
	 * @return the editable
	 */
	public boolean isEditable() {
		return editable;
	}
	/**
	 * @param editable the editable to set
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	public void setSurveyQuestionList(List<SurveyQuestion> surveyQuestionList) {
		this.surveyQuestionList = surveyQuestionList;
	}
	public List<Long> getAnsweredSurveyQyestionId() {
		return answeredSurveyQyestionId;
	}
	public void setAnsweredSurveyQyestionId(List<Long> answeredSurveyQyestionId) {
		this.answeredSurveyQyestionId = answeredSurveyQyestionId;
	}
	public List<Long> getSurveyQuestionsToBeDeletedFromDB() {
		return surveyQuestionsToBeDeletedFromDB;
	}
	public void setSurveyQuestionsToBeDeletedFromDB(
			List<Long> surveyQuestionsToBeDeletedFromDB) {
		this.surveyQuestionsToBeDeletedFromDB = surveyQuestionsToBeDeletedFromDB;
	}
	public List<SurveyQuestion> getSurveyQuestionList() {
		return surveyQuestionList;
	}
	

}
