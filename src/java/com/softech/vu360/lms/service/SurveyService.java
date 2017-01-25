package com.softech.vu360.lms.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.softech.vu360.lms.model.AggregateSurveyQuestionItem;
import com.softech.vu360.lms.model.Alert;
import com.softech.vu360.lms.model.AlertQueue;
import com.softech.vu360.lms.model.AlertRecipient;
import com.softech.vu360.lms.model.AlertTrigger;
import com.softech.vu360.lms.model.AlertTriggerFilter;
import com.softech.vu360.lms.model.AvailableAlertEvent;
import com.softech.vu360.lms.model.AvailablePersonalInformationfieldItem;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.EmailAddress;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearningSession;
import com.softech.vu360.lms.model.MessageTemplate;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.PredictSurveyQuestionMapping;
import com.softech.vu360.lms.model.SuggestedTraining;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyAnswerItem;
import com.softech.vu360.lms.model.SurveyFlag;
import com.softech.vu360.lms.model.SurveyFlagTemplate;
import com.softech.vu360.lms.model.SurveyLearner;
import com.softech.vu360.lms.model.SurveyLink;
import com.softech.vu360.lms.model.SurveyOwner;
import com.softech.vu360.lms.model.SurveyQuestion;
import com.softech.vu360.lms.model.SurveyQuestionBank;
import com.softech.vu360.lms.model.SurveyResult;
import com.softech.vu360.lms.model.SurveyResultAnswer;
import com.softech.vu360.lms.model.SurveyResultAnswerFile;
import com.softech.vu360.lms.model.SurveyReviewComment;
import com.softech.vu360.lms.model.SurveySection;
import com.softech.vu360.lms.model.SurveySectionSurveyQuestionBank;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.WrittenTrainingPlan;
import com.softech.vu360.lms.vo.SaveSurveyParam;
import com.softech.vu360.lms.vo.SurveyVO;
import com.softech.vu360.lms.web.controller.model.AddCourseInSuggestedTrainingForm;
import com.softech.vu360.lms.web.controller.model.SuggestedTrainingForm;
import com.softech.vu360.lms.web.controller.model.SurveyFlagAndSurveyResult;
import com.softech.vu360.lms.web.controller.model.survey.SurveyAnalysis;
import com.softech.vu360.lms.web.controller.model.survey.TakeSurveyForm;
import com.softech.vu360.util.Brander;

public interface SurveyService {
		
	public List<Survey> getAllSurvey(SurveyOwner surveyowner);

	public void deleteSurvey(Survey survey);

	public Survey addSurvey(Survey survey);
	public Survey saveSurvey(Survey survey);
	public SurveyResult addSurveyResult(VU360User user,TakeSurveyForm form);
	public SurveyResult addAnonymousSurveyResult( TakeSurveyForm form );
	public Survey getSurveyByID(long id);
	
	public void deleteSurveyQuestions(Long surveyQuestionIdArray[]);
	
	public Survey findSurveyByName(String name);
	public void deleteSurveys(long surveyIdArray[]);
	public void deleteSurveyQuestionAnswerItems(List<SurveyAnswerItem> surveyAnswerItems);
	public List<SurveyResult> findSurveyResult(long id);
	public List<SurveyResult> findAllSurveyResults();
	public Survey saveEditedQuestion(long id, List<SurveyQuestion> surveyQuestionList);
	public SurveyAnalysis getSurveyResponseAnalysis(Long surveyId);
	public List<Survey> getSurveysByCourses(long courseIdArray[],VU360User loggedInUser, String search);
	public List<Survey> getCompletedSurveysByUser(VU360User loggedInUser, String search);
	public List<Survey> getCompletedSurveysByUser(VU360User loggedInUser);
	public List<Survey> getDueSurveysByLearnerEnrollment(long learnerEnrollmentId, String search);
	public List<Survey> getDueSurveysByLearnerEnrollment(long learnerEnrollmentId);
	public List<Survey> getDueSurveysByUser(VU360User loggedInUser);
	public List<Survey> getDueSurveysByUser(com.softech.vu360.lms.vo.VU360User loggedInUser);
	public List<Survey> getDueSurveysByLearningSession(LearningSession learningSession);
	public List<Survey> getDueSurveysByLearningSession(String learningSessionId);
	public List<Survey> getDueSurveysByLearningSession(String learningSessionId, String search);
	public SurveyResult getSurveyResultByUserAndCourse(VU360User loggedInUser,Course course, Survey survey);
//	public SurveyResult getSurveyResultByUserAndCourse(VU360User loggedInUser,Course course, Survey survey, String search);
	public Map<String,Integer> getTotalSurveyByUser(VU360User loggedInUser);
	public Map<String,Integer> getTotalSurveyByUser(VU360User loggedInUser, String search);
	public List<Survey> getSurveyByName(SurveyOwner surveyowner, String surveyName, String surveyStatus , String isLocked,String readonly);
	public List<Survey> getManualSurveyByName(SurveyOwner surveyowner, String surveyName, String surveyStatus, String isLocked,String readonly);
	public List<Survey> findManualSurveys(String surveyName ,String surveyStatus ,String  retiredSurvey, Customer customer  ,int intLimit );
	public SurveyVO saveSurveyAssignment(SaveSurveyParam saveSurveyParam) throws ParseException ;
	public List<SurveyLearner> getSurveyAssignmentOfLearners( List<Learner> learners );
	public SurveyResult getSurveyResultByLearnerAndSurvey(VU360User loggedInUser, Survey survey);
	public SurveyResultAnswer getSurveyResultByLearnerAndSurveyAndQuestion(Long id, Long SurveyResult_Id);
	public SurveyResult getSurveyResultByLearnerAndSurvey(VU360User loggedInUser, Survey survey, String search);
	public List<SurveyLearner> getLearnerSurveys(VU360User user);
	public List<SurveyLearner> getLearnerSurveys(VU360User user, String search);
	public Map<String,Object> getSurveyQASummary(VU360User user,TakeSurveyForm form);
	public VU360User getManagerOfSurveyLearner(VU360User user , Survey survey);
	public SurveyQuestion addSurveyQuestion(SurveyQuestion surveyQuestion);
	public Survey   loadForUpdateSurvey(long id);
		public void deleteSurveyQuestions(Object surveyQuestionIdArray[]);
	public SurveyFlagTemplate getFlagTemplateByID(long id);
	public List<SurveyFlagTemplate> findAllFlagTemplate(String flagName, long surveryId);
	public SurveyLink saveSurveyLink(SurveyLink surveyLink);
	public SurveyFlagTemplate saveSurveyFlagTemplate(SurveyFlagTemplate surveyFlagTemplate);
	public List<SurveyLink>getSurveyLinksById(long surveyId);
	public void saveSurveyLinkList(List<SurveyLink> surveyLinks);
	public List<SurveyAnswerItem>getSurveyAnswerItemByIdArray(long []surveyAnswerItemArray);
	public SurveyQuestion getSurveyQuestionById(long id);
	public void saveAggregateSurveyQuestionItem( List<AggregateSurveyQuestionItem> aggregateSurveyQuestions);
	public void deleteSurveyFlagTemplates(long []idArray);
	public List<AggregateSurveyQuestionItem> getAggregateSurveyQuestionItemsByQuestionId(long id);
	public void addSuggestedTraining(AddCourseInSuggestedTrainingForm form);
	public SuggestedTraining addSuggestedTrainingAndCoursesUnderIt(AddCourseInSuggestedTrainingForm form); // Note: LMS-7612
	public void editSuggestedTraining(SuggestedTrainingForm form);
	public SurveyResult saveSurveyResult(SurveyResult surveyResult);
	public Set<SurveyResult> getNotReviewedFlaggedSurveyResult(SurveyOwner surveyowner);
	public SurveyResult getSurveyResultBySurveyResultID(long id);
	public List<SuggestedTraining> getSuggestedTrainingsBySurveyID(long id);	
	public SuggestedTraining loadForUpdateSuggestedTraining(long id);
	public List<SuggestedTraining> getSuggestedTrainingsBySurveyIDs(long []idArray);
	public List<AvailablePersonalInformationfieldItem> getAllAvailablePersonalInformationfields();
	public SurveyResult   loadForUpdateSurveyResult(long id);
	public SurveyResultAnswer loadForUpdateSurveyResultAnswer(long id);
	
	// LMS 7617
	public Set<SurveyFlagAndSurveyResult> getNotReviewedFlaggedSurveyResult2(SurveyOwner surveyowner,String surveyName,String status,String firstName,String lastName,String userName);
	// LMS 7617
	public SurveyFlag saveSurveyFlag(SurveyFlag surveyFlag);
	
	//LMS : 7617
	public SurveyFlag loadSurveyFlagForUpdate(long id);
	
	//LMS : 7617
	public List<SurveyResult> getSendBackSurveyResultsByLoggedInUser(VU360User loggenInUser);
	
	//[Note: LMS: 7614]
	public void deleteSuggestedTraining(SuggestedTraining suggestedTraining);
	
	public SuggestedTraining loadSuggestedTrainingForUpdate(Long suggestedTrainingId);
	
	public SuggestedTraining saveSuggestedTraining(SuggestedTraining suggestedTraining);
	
	//************************************* ALERT MODULE *******************************************
	
	public Alert addAlert(Alert alert);
	
	public Alert getAlertByID(long id);
	
	public List<Alert> findAlert(Long loggedInUserId , String alertName);
	
	public void deleteAlerts(long alertIdArray[]);
	
	public Alert loadAlertForUpdate(long id);
	
	public List<Alert> getAllAlertByCreatedUserId(Long createdUserId);
	
	public AlertRecipient addAlertRecipient(AlertRecipient alertRecipient);
	
	public AlertRecipient loadAlertRecipientForUpdate(long id);
	
	public List<AlertRecipient> findAlertRecipient(String name  , String type , Long alertId);
	
	public void deleteAlertRecipients(long alertRecipientIdArray[]);
	
	public AlertRecipient getAlertRecipientById(Long id);

    public List<Learner> getFilteredRecipientsByAlert(Long alertId);
    public List<Course> getFilteredCoursesByAlert(Long alertId);
    
	public List<Learner> getLearnersUnderAlertRecipient(Long alertRecipientId , String firstName , String lastName , String emailAddress );
	public List<LearnerGroup> getLearnerGroupsUnderAlertRecipient(Long alertRecipientId , String groupName );
	public List<OrganizationalGroup> getOrganisationGroupsUnderAlertRecipient(Long alertRecipientId , String groupName );
	public List<EmailAddress> getEmailAddressUnderAlertRecipient(Long alertRecipientId , String emailAddress );
	
	public List<AvailableAlertEvent> getAllAvailableAlertEvents();
	
	public AlertTrigger addAlertTrigger(AlertTrigger alertTrigger);
	
	public List<AlertTrigger> getAllAlertTriggerByAlertId(Long alertId);
	
	public List<AlertRecipient> getAllAlertRecipientByAlertId(Long alertId);
	
	public void deleteAlertTriggers(long alertTriggerIdArray[]);
	
	public AlertTrigger loadAlertTriggerForUpdate(long id);
	
	public AlertTrigger getAlertTriggerByID(long id);
	
	
	
	public AlertTriggerFilter addAlertTriggerFilter(AlertTriggerFilter alertTriggerFilter);
	
	public AlertTriggerFilter getAlertTriggerFilterByID(long id);
	public List<AlertTriggerFilter> getAllAlertTriggerFilterByAlertTriggerId(Long AlertTriggerId);	
	
	public void deleteAlertTriggerFilter(long alertTriggerFilterIdArray[]);
	
	public AlertTriggerFilter loadAlertTriggerFilterForUpdate(long id);
	
	public List<Learner> getLearnersUnderAlertTriggerFilter(Long alertTriggerFilterId , String firstName , String lastName , String emailAddress );
	public List<Learner> getLearnersUnderAlertTriggerFilter(Long alertTriggerFilterId);
	public List<LearnerGroup> getLearnerGroupsUnderAlertTriggerFilter(Long alertTriggerFilterId , String groupName );
	public List<OrganizationalGroup> getOrganisationGroupsUnderAlertTriggerFilter(Long alertTriggerFilterId , String groupName );
	public List<Course> getCourseUnderAlertTriggerFilter(Long alertTriggerFilterId , String name , String courseType);
	public List<Course> getSuggestedTrainingCoursesBySurveyId(long surveyId);
	
	public List<Course> getSuggestedTrainingCoursesBySurveyIdAndContentOwner(long surveyId , String contentOwnerName);
	
	public List<SurveyQuestion> getSurveyQuestionsBySurveyId(long surveyId);
	
	public List<SurveyAnswerItem> getSurveyAnswerItemsBySurveyId(long surveyId);

//	public List<Object> getDataFromTablenamePresentInAvailableAlertEventTable(String tableNameToScanFor , String columnNameToRead);
	public AlertQueue saveAlertQueue(AlertQueue alertQueue);
	/*
	// no reference found
	public List<AlertTrigger> getAlertTriggerbyAvailableAlertEventId(long AvailableAlertEventId);
	*/
	//public AlertQueue getMaxDateAlertQueuebyTableName(String tableName);
	public AlertQueue loadAlertQueueForUpdate(long id);
	public List<AlertQueue> getAllAlertQueues();
	public List<AlertQueue> getAlertQueueByUserId(long userId);
	//public CourseAlertTriggeerFilter getCourseAlertTriggeerFilterByCourse(long courseId);


	public List<AlertTrigger> getAllAlertTrigger();	
	public AlertQueue getAlertQueueByLearnerIdAndEventTable(long learnerId , String tableName ,  long tableNameId,  long triggerId);
	public AlertQueue getAlertQueueForDateDrivenTriggerByLearnerId(long learnerId , String triggerType);
	public MessageTemplate getMessageTemplateByEventType(String eventType);
	public List<AlertQueue> getAllAlertQueuesWithPendingMailStatus();
	public MessageTemplate getMessageTemplateByTriggerType(String triggerType);
	
	
	public List<Survey> getSurveysHavingSuggestedTraining(SurveyOwner surveyowner, String surveyName, String surveyStatus , String isLocked,String readonly);
	//public AlertTrigger findTriggerByName(String name);
	// [2/15/2011] LMS-8972 :: Course Evaluation Criteria is appearing incorrectly
	public boolean isCourseEvaluationCompleted ( Long learnerEnrollmentId );
	public void deleteCustomResponse(Object surveyQuestionIdArray[]);
	
	public SurveyResultAnswer getSurveyResultAnswerBySurveyQuestionId(long id);//added for LMS-9035
	public SurveyReviewComment addSurveyReviewComment(SurveyReviewComment surveyReviewComment);
	
	//Need this for FirmElement RTF file Generator
	public boolean generateTrainingNeedAnalysis(Long surveyId, Brander brand );
	public WrittenTrainingPlan generateWrittenTrainingPlanSurveyResults(VU360User user, long surveyId);
	public SurveyResult addSurveyResultForCEPlanner(VU360User user,TakeSurveyForm form, HashMap<Long, Long> questionListMapping,HashMap<Long, Long> answerListMapping);
	
	/*
	 * TSM - Technology Stack Migration
	 * 
	 * Following code piece was written to enforce
	 * cache update and such calls are now unnecessary.  
	 * Cache must not be enforced in such manner, therefore
	 * this code has been taken off
	 * 
	 * */
	//public Survey refreshSurveys(Long surveyId);

    public boolean isRequiredProctorApproval(Long tableNameId, String[] businessKeys);
    
    public List<SurveySection> getSurveySectionsBySurveyId(Long surveyId);
    
    public SurveySection getSurveySectionByID(Long id);
    
    public SurveyQuestion getSurveyQuestionBySurveySectionQuestionID(Long id);
    
    public SurveySection saveSurveySection(SurveySection surveySection);
    
    public PredictSurveyQuestionMapping getPredictSurveyQuestionMappingByLMSSurveyId(Long surveyId, Long questionId);
    
    public PredictSurveyQuestionMapping savePredictSurveyQuestionMapping(PredictSurveyQuestionMapping predictSurveyQuestionMapping);
    
    // PD-675 Survey Review Comment model beans
    public SurveyReviewComment loadForUpdateSurveyReviewComment(long id);
    public SurveyReviewComment getSurveyReviewCommentByResultId(long surveyResultId);
    public SurveyReviewComment updateSurveyReviewComment(SurveyReviewComment surveyReviewComment);
    public List<SurveyResultAnswer> getSurveyResultAnswersByQuestionId(long questionId);
    public SurveyResultAnswer getSurveyResultAnswerByQuestionIdAndVU360UserId(long questionId, long userId);
    public SurveyResultAnswer getSurveyResultAnswerByQuestionIdAndResultId(long questionId, long userId);
    public SurveyResultAnswer getSurveyResultAnswer(Long questionId, Long frameworkId, Long sectionId, Long userId);
    
    public SurveySectionSurveyQuestionBank saveSurveySectionSurveyQuestionBank(SurveySectionSurveyQuestionBank sectionSurveyQuestionBank);
    public void delete(SurveySectionSurveyQuestionBank sectionSurveyQuestionBank);
    
    public SurveyQuestionBank saveSurveyQuestionBank(SurveyQuestionBank surveyQuestionBank);
    public void delete(SurveyQuestionBank surveyQuestionBank);

	public SurveyAnswerItem saveSurveyAnswerItem(SurveyAnswerItem answerItem);
	public void delete(SurveyAnswerItem answerItem);

	public List<SurveySection> getRootSurveySections(Survey survey);
	public List<SurveySection> getRootSurveySections(Long surveyId);
	
	public List<SurveyResult> getSurveyResultsBySurveyId(Long surveyId);
	
	public List<PredictSurveyQuestionMapping> getPredictSurveyQuestionMappingBySurveyId(Long surveyId);

	public Integer getNextSectionCloneNumber(Long predictSurveyId, Long lmsSurveyId, String predictSectionId);
	public PredictSurveyQuestionMapping getPredictSurveyQuestionMapping(Long surveyId, String sectionId, Long frameworkId, Long questionId);
	public SurveyQuestionBank getSurveyQuestionBankById(Long surveyQuestionBankId);
	public SurveyAnswerItem getAnswerItemById(Long answerId);
	public Integer getStartedSurveys(Long surveyId);
	public Integer getAnsweredQuestionCount(Long surveyId, Long sectionId, Long questionId);
	public Integer getQuestionAnswersCount(Long surveyId, Long sectionId, Long questionId);
	public Integer getAnswerCount(Long surveyId, Long sectionId, Long questionId, Long answerId);
	public PredictSurveyQuestionMapping getPredictSurveyQuestionMappingBySectionId(Long surveySectiondId);
	public SurveyResultAnswerFile getSurveyFileById(Long fileId);
	public PredictSurveyQuestionMapping getPredictSurveyQuestionMappingByFrameworkId(Long frameworkId);
	public List<Survey> getCurrentSurveyListByCoursesForUser(VU360User loggedInUser);
	public List<Survey> getAssignedInspectionSurveys(SurveyOwner surveyOwner);
	public List<Survey> getOverDueSurveysByUser(VU360User loggedInUser);
	List<AlertTriggerFilter> findByAlerttriggerIdAndAlerttriggerIsDeleteFalseAndIsDeleteFalse(Long alertTriggerId);
}

