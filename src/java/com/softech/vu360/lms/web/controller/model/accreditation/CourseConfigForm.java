package com.softech.vu360.lms.web.controller.model.accreditation;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.AssessmentConfiguration;
import com.softech.vu360.lms.model.Certificate;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.CourseConfiguration;
import com.softech.vu360.lms.model.CourseConfigurationTemplate;
import com.softech.vu360.lms.vo.UniqueQuestionsVO;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author PG
 *
 */
public class CourseConfigForm implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	public static final String COURSE_APPROVAL = "Course";
	public static final String UNIT_MONTHS="Months";
	public static final String UNIT_DAYS="Days";
	public static final String UNIT_MINUTES="Minutes";
	
	private CourseConfiguration courseConfiguration = new CourseConfiguration();
	private CourseConfigurationTemplate courseConfigurationTemplate = new CourseConfigurationTemplate();
	
	private String entity = "";
	private CourseApproval courseApproval = null;
	private List<CourseConfigurationTemplate> courseConfigurationTemplates = new ArrayList<CourseConfigurationTemplate>();

	private String templateName = "";
	private String lastUpdated = "";
	
	private String selectedCourseConfigID = null;
	
	// course Policy
	private boolean showStandardIntroduction = false;
	private boolean showOrientationScenes = false;
	private boolean showEndOfCourseScene = false;
	private boolean showContent = false;
	private boolean enforceTimedOutlineAllScenes = false;
	private boolean allowUserToReviewCourseAfterCompletion = false;
	private boolean respondToCourseEvaluation = false;
	
	// Course Completion

	private Certificate completionCertificate = null;
	private boolean mustAttemptPostAssessment = false;
	private boolean mustDemonstratePostAssessmentMastery = false;
	private boolean mustDemonstratePreAssessmentMastery = false;
	private boolean mustDemonstrateQuizAssessmentMastery = false;
	private boolean mustCompleteAnySurveys = false;
	private boolean mustViewEverySceneInTheCourse = false;
	private boolean canOnlyBeCompleteAfterNumberOfCourseLaunches = false;
	private boolean completeWithinTimePeriod = false;
	private boolean completeWithinDaysOfRegistration = false;
	private String unitOfTimeToComplete=UNIT_MINUTES;
	private boolean courseEvaluationSpecified = false;
	
	private boolean enableDefineUniqueQuestionValidation;
	private boolean enableSelfRegistrationProctor;
	private boolean enableSmartProfileValidation;
	
	// for pagination
	private String pageIndex;
	private String showAll = "false";
	private String pageCurrIndex;
	
	// for sorting
	private String sortColumnIndex;
	private String sortDirection;
	
	private String idleTimeAmount = "1800";
	private String numberOfCoursesLaunch = "1";
	private String minutesAfterFirstCourseLaunch = "0";
	private String daysOfRegistraion = "0";


	//Validation
	private String requireIdentityValidationEverySeconds = "600";
	private String allowSecondsToAnswerEachQuestion = "30";
	//private String allowNumberofAttemptsToAnswerCorrectly = "0";
	private String validationTimeBetweenQuestion = "600";
	private String validationNoMissedQuestionsAllowed = "3";
	private String numberOfValidationQuestions="1";
	
	
	
	private String unitOfDaysOfRegistration = "0";
	private String []listOfUnitOfDaysOfRegistration;
	private boolean courseStrictlyEnforcePolicyToBeUsed;
	private String guid;
	private String playerCourseFlow; //enum
	private boolean playerStrictlyEnforcePolicyToBeUsed;
	private String displayCourseEvaluation;
	private Boolean certificateEnabled = false;
	private String certificateName = null;
	private boolean validationStrictlyEnforcePolicyToBeUsed;
	private boolean mustCompleteCourseEvaluation;
	private boolean suggestedCourse;
	private boolean rateCourse;
	
	//acknowledgement
	private String acknowledgeText = "";
	private boolean acknowledgeEnabled = false;
	
	// End Of Course
	private String endOfCourseInstructions="";
	
	
	// Validation
	private boolean enableIdentityValidation = false;
	
	
	

	// Seat Time
	private boolean seattimeenabled;
	private String seattimeinhour = "0";
	private String seattimeinmin = "0";
	private String messageseattimeexceeds="";
	private String messageseattimecourselaunch="";
	
	private boolean displaySeatTimeTextMessage = false;
	private boolean lockPostAssessmentBeforeSeatTime = false;
	
	// Idle TimeOut
	
	private String actiontotakeuponidletimeout;
	
	//For Pre
	
	private String preassessmentNoquestion = "20";
	private String preassessmentMasteryscore = "80";
	private boolean preassessmentEnforcemaximumtimelimitCB = true;
	private String preassessmentEnforcemaximumtimelimit = "120";
	private boolean preAssessmentMaximumNoAttemptCB=true;
	private String preassessmentMaximumnoattempt = "5";
	private boolean preAssessmentMaximumnoattemptEnabled = true;
	private boolean preAssessmentEnabled = false;
	private String preActionToTakeAfterMaximumAttemptsReached = AssessmentConfiguration.
                ActionToTakeAfterFailingMaxAttempt.GoToNextLesson.getValue();
	
	private int preMaximumTimeLimitMinutes = 0;
	private boolean preUseUniqueQuestionsOnRetake = false;
	private int preMinimumSeatTimeBeforeAssessmentStart = 0;
	private String preMinimumSeatTimeBeforeAssessmentStartUnits = UNIT_MINUTES;
	private boolean preEnableContentRemediation = true;
	private boolean preEnableAssessmentProctoring = false;
	private boolean preShowQuestionLevelResults = false;
	private boolean preRandomizeQuestions = true;
	private boolean preRandomizeAnswers = true;
	private String preScoringType = "";
	private String preNoResultMessage="";
	private boolean preShowQuestionAnswerReview = false;
	private boolean preEnableRestrictiveAssessmentMode = false;
	private String preGradingBehavior = "";
	private boolean preEnableWeightedScoring = false;
	private boolean preAdvancedQuestionSelectionTypeCB;
	private String preAdvancedQuestionSelectionType = "";	
	private boolean preEnableAdvancedQuestionSelectionType = false;

	private boolean preAllowQuestionSkipping = true;
	private boolean preAllowPauseResumeAssessment = false;
	
	private boolean preLockoutAssessmentActiveWindow = false;

	//For Post
	
	private String postassessmentNoquestion = "20";
	private String postassessmentMasteryscore = "80";
	private String postassessmentEnforcemaximumtimelimit = "120";
	private boolean postAssessmentMaximumNoAttemptCB=true;
	private String postassessmentMaximumnoattempt = "5";
	private boolean postAssessmentMaximumnoattemptEnabled = true;
        
	private boolean postAssessmentEnabled = false;
	private String postActionToTakeAfterMaximumAttemptsReached = AssessmentConfiguration.
                ActionToTakeAfterFailingMaxAttempt.RetakeContent.getValue();
        
	private int postMaximumTimeLimitMinutes = 120;
	private boolean postUseUniqueQuestionsOnRetake = false;
	private String postMinimumSeatTimeBeforeAssessmentStart = "0";
	private String postMinimumSeatTimeBeforeAssessmentStartUnits = UNIT_MINUTES;
	private boolean postEnableContentRemediation = false;
	private boolean postEnableAssessmentProctoring = false;
	private boolean postShowQuestionLevelResults = false;
	private boolean postRandomizeQuestions = true;
	private boolean postRandomizeAnswers = true;
	private String postScoringType = "";
	private String postNoResultMessage="";
	private boolean postShowQuestionAnswerReview = false;
	private boolean postEnableRestrictiveAssessmentMode = false;
	private String postGradingBehavior = "";
	private boolean postEnableWeightedScoring = false;
	private boolean postAdvancedQuestionSelectionTypeCB=true;
	private String postAdvancedQuestionSelectionType = "";
	private boolean postEnableAdvancedQuestionSelectionType = false;
	private boolean postAllowQuestionSkipping = true;
	private boolean postAllowPauseResumeAssessment = false;
	private boolean postEnableviewAssessmentResults = false;
	private boolean postLockoutAssessmentActiveWindow = false;
	
	//For QuizAssessment
	
	private String quizAssessmentNoquestion = "20";
	private String quizAssessmentMasteryscore = "80";
	
	private String quizAssessmentEnforcemaximumtimelimit = "120";
	private boolean quizAssessmentMaximumNoAttemptCB = true;
	private String quizAssessmentMaximumnoattempt = "5";
	private boolean quizAssessmentEnabled = false;
	private String quizAssessmentActionToTakeAfterMaximumAttemptsReached = AssessmentConfiguration.
                ActionToTakeAfterFailingMaxAttempt.GoToNextLesson.getValue();
        
	private boolean quizAssessmentMaximumnoattemptEnabled = true;
                
	private int quizAssessmentMaximumTimeLimitMinutes = -1;
	private boolean quizAssessmentUseUniqueQuestionsOnRetake = false;
	private int quizAssessmentMinimumSeatTimeBeforeAssessmentStart = 0;
	private String quizAssessmentMinimumSeatTimeBeforeAssessmentStartUnits = UNIT_MINUTES;
	private boolean quizAssessmentEnableContentRemediation = false;
	private boolean quizAssessmentEnableAssessmentProctoring = false;
	private boolean quizAssessmentShowQuestionLevelResults = false;
	private boolean quizAssessmentRandomizeQuestions = true;
	private boolean quizAssessmentRandomizeAnswers = true;
	private String quizAssessmentScoringType = "";
	private String quizAssessmentNoResultMessage="";
	private boolean quizAssessmentShowQuestionAnswerReview = false;
	private boolean quizAssessmentEnableRestrictiveAssessmentMode = false;
	private String quizAssessmentGradingBehavior = "";
	private boolean quizAssessmentEnableWeightedScoring = false;
	private boolean quizAssessmentAdvancedQuestionSelectionTypeCB = true;
	private String quizAssessmentAdvancedQuestionSelectionType = "";
	private boolean quizAssessmentEnableAdvancedQuestionSelectionType = false;
	private boolean quizAssessmentAllowQuestionSkipping = true;
	private boolean quizAssessmentEnforcemaximumtimelimitCB = true;
	private boolean postassessmentEnforcemaximumtimelimitCB = true;
	private boolean quizAllowPauseResumeAssessment = false;
	private boolean quizLockoutAssessmentActiveWindow = false;
	
	private boolean mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified;
	private int mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration;
	private String mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationUnit;
	
	private boolean specialQuestionnaireSpecified;
	private boolean mustCompleteSpecialQuestionnaire;
	private String displaySpecialQuestionnaire;
	
	// Proctor Policies and Learner Policies
	private boolean requireProctorValidation;
	private String proctorValidatorName;
	private boolean requireLearnerValidation;
	private boolean caRealEstateCE;
	
	//Learners Unique Questions
	private List<UniqueQuestionsVO> lstUniqueQuestionsVO = new ArrayList<>();

	
	public CourseConfigForm() {
		System.out.println("print form bean load");
	}
	
	public CourseConfiguration getCourseConfiguration() {
		return courseConfiguration;
	}

	public void setCourseConfiguration(CourseConfiguration courseConfiguration) {
		this.courseConfiguration = courseConfiguration;
	}

	public CourseConfigurationTemplate getCourseConfigurationTemplate() {
		return courseConfigurationTemplate;
	}

	public void setCourseConfigurationTemplate(
			CourseConfigurationTemplate courseConfigurationTemplate) {
		this.courseConfigurationTemplate = courseConfigurationTemplate;
	}

	/**
	 * @return the courseApproval
	 */
	public CourseApproval getCourseApproval() {
		return courseApproval;
	}

	/**
	 * @param courseApproval the courseApproval to set
	 */
	public void setCourseApproval(CourseApproval courseApproval) {
		this.courseApproval = courseApproval;
	}

	/**
	 * @return the courseConfigurationTemplates
	 */
	public List<CourseConfigurationTemplate> getCourseConfigurationTemplates() {
		return courseConfigurationTemplates;
	}

	/**
	 * @param courseConfigurationTemplates the courseConfigurationTemplates to set
	 */
	public void setCourseConfigurationTemplates(
			List<CourseConfigurationTemplate> courseConfigurationTemplates) {
		this.courseConfigurationTemplates = courseConfigurationTemplates;
	}

	/**
	 * @return the entity
	 */
	public String getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}

	/**
	 * @return the templateName
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * @param templateName the templateName to set
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * @return the lastUpdated
	 */
	public String getLastUpdated() {
		return lastUpdated;
	}

	/**
	 * @param lastUpdated the lastUpdated to set
	 */
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	/**
	 * @return the selectedCourseConfigID
	 */
	public String getSelectedCourseConfigID() {
		return selectedCourseConfigID;
	}

	/**
	 * @param selectedCourseConfigID the selectedCourseConfigID to set
	 */
	public void setSelectedCourseConfigID(String selectedCourseConfigID) {
		this.selectedCourseConfigID = selectedCourseConfigID;
	}

	/**
	 * @return the pageIndex
	 */
	public String getPageIndex() {
		return pageIndex;
	}

	/**
	 * @param pageIndex the pageIndex to set
	 */
	public void setPageIndex(String pageIndex) {
		this.pageIndex = pageIndex;
	}

	/**
	 * @return the showAll
	 */
	public String getShowAll() {
		return showAll;
	}

	/**
	 * @param showAll the showAll to set
	 */
	public void setShowAll(String showAll) {
		this.showAll = showAll;
	}

	/**
	 * @return the pageCurrIndex
	 */
	public String getPageCurrIndex() {
		return pageCurrIndex;
	}

	/**
	 * @param pageCurrIndex the pageCurrIndex to set
	 */
	public void setPageCurrIndex(String pageCurrIndex) {
		this.pageCurrIndex = pageCurrIndex;
	}

	/**
	 * @return the sortColumnIndex
	 */
	public String getSortColumnIndex() {
		return sortColumnIndex;
	}

	/**
	 * @param sortColumnIndex the sortColumnIndex to set
	 */
	public void setSortColumnIndex(String sortColumnIndex) {
		this.sortColumnIndex = sortColumnIndex;
	}

	/**
	 * @return the sortDirection
	 */
	public String getSortDirection() {
		return sortDirection;
	}

	/**
	 * @param sortDirection the sortDirection to set
	 */
	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	public String getDaysOfRegistraion() {
		return daysOfRegistraion;
	}

	public void setDaysOfRegistraion(String daysOfRegistraion) {
		this.daysOfRegistraion = daysOfRegistraion;
	}

	public String getIdleTimeAmount() {
		return idleTimeAmount;
	}

	public void setIdleTimeAmount(String idleTimeAmount) {
		this.idleTimeAmount = idleTimeAmount;
	}

	public String getMinutesAfterFirstCourseLaunch() {
		return minutesAfterFirstCourseLaunch;
	}

	public void setMinutesAfterFirstCourseLaunch(
			String minutesAfterFirstCourseLaunch) {
		this.minutesAfterFirstCourseLaunch = minutesAfterFirstCourseLaunch;
	}

	public String getNumberOfCoursesLaunch() {
		return numberOfCoursesLaunch;
	}

	public void setNumberOfCoursesLaunch(String numberOfCoursesLaunch) {
		this.numberOfCoursesLaunch = numberOfCoursesLaunch;
	}

		
	public boolean isPreassessmentEnforcemaximumtimelimitCB() {
		return preassessmentEnforcemaximumtimelimitCB;
	}

	public void setPreassessmentEnforcemaximumtimelimitCB(
			boolean preassessmentEnforcemaximumtimelimitCB) {
		this.preassessmentEnforcemaximumtimelimitCB = preassessmentEnforcemaximumtimelimitCB;
	}

	public String getPreassessmentEnforcemaximumtimelimit() {
		return preassessmentEnforcemaximumtimelimit;
	}

	public void setPreassessmentEnforcemaximumtimelimit(
			String preassessmentEnforcemaximumtimelimit) {
		this.preassessmentEnforcemaximumtimelimit = preassessmentEnforcemaximumtimelimit;
	}

	public String getPreassessmentMasteryscore() {
		return preassessmentMasteryscore;
	}

	public void setPreassessmentMasteryscore(String preassessmentMasteryscore) {
		this.preassessmentMasteryscore = preassessmentMasteryscore;
	}
	
	

	public boolean isPreAssessmentMaximumNoAttemptCB() {
		return preAssessmentMaximumNoAttemptCB;
	}

	public void setPreAssessmentMaximumNoAttemptCB(
			boolean preAssessmentMaximumNoAttemptCB) {
		this.preAssessmentMaximumNoAttemptCB = preAssessmentMaximumNoAttemptCB;
	}

	public String getPreassessmentMaximumnoattempt() {
		return preassessmentMaximumnoattempt;
	}

	public void setPreassessmentMaximumnoattempt(
			String preassessmentMaximumnoattempt) {
		this.preassessmentMaximumnoattempt = preassessmentMaximumnoattempt;
	}

	public String getPreassessmentNoquestion() {
		return preassessmentNoquestion;
	}

	public void setPreassessmentNoquestion(String preassessmentNoquestion) {
		this.preassessmentNoquestion = preassessmentNoquestion;
	}

	public String getPostassessmentEnforcemaximumtimelimit() {
		return postassessmentEnforcemaximumtimelimit;
	}

	public void setPostassessmentEnforcemaximumtimelimit(
			String postassessmentEnforcemaximumtimelimit) {
		this.postassessmentEnforcemaximumtimelimit = postassessmentEnforcemaximumtimelimit;
	}

	public String getPostassessmentMasteryscore() {
		return postassessmentMasteryscore;
	}

	public void setPostassessmentMasteryscore(String postassessmentMasteryscore) {
		this.postassessmentMasteryscore = postassessmentMasteryscore;
	}

	public String getPostassessmentMaximumnoattempt() {
		return postassessmentMaximumnoattempt;
	}

	public void setPostassessmentMaximumnoattempt(
			String postassessmentMaximumnoattempt) {
		this.postassessmentMaximumnoattempt = postassessmentMaximumnoattempt;
	}

	public String getPostassessmentNoquestion() {
		return postassessmentNoquestion;
	}

	public void setPostassessmentNoquestion(String postassessmentNoquestion) {
		this.postassessmentNoquestion = postassessmentNoquestion;
	}

	public String getQuizAssessmentEnforcemaximumtimelimit() {
		return quizAssessmentEnforcemaximumtimelimit;
	}

	public void setQuizAssessmentEnforcemaximumtimelimit(String quizAssessmentEnforcemaximumtimelimit) {
		this.quizAssessmentEnforcemaximumtimelimit = quizAssessmentEnforcemaximumtimelimit;
	}

	public String getQuizAssessmentMasteryscore() {
		return quizAssessmentMasteryscore;
	}

	public void setQuizAssessmentMasteryscore(String quizAssessmentMasteryscore) {
		this.quizAssessmentMasteryscore = quizAssessmentMasteryscore;
	}

	public String getQuizAssessmentMaximumnoattempt() {
		return quizAssessmentMaximumnoattempt;
	}

	public void setQuizAssessmentMaximumnoattempt(String quizAssessmentMaximumnoattempt) {
		this.quizAssessmentMaximumnoattempt = quizAssessmentMaximumnoattempt;
	}

	public String getQuizAssessmentNoquestion() {
		return quizAssessmentNoquestion;
	}

	public void setQuizAssessmentNoquestion(String quizAssessmentNoquestion) {
		this.quizAssessmentNoquestion = quizAssessmentNoquestion;
	}

//	public String getAllowNumberofAttemptsToAnswerCorrectly() {
//		return allowNumberofAttemptsToAnswerCorrectly;
//	}
//
//	public void setAllowNumberofAttemptsToAnswerCorrectly(
//			String allowNumberofAttemptsToAnswerCorrectly) {
//		this.allowNumberofAttemptsToAnswerCorrectly = allowNumberofAttemptsToAnswerCorrectly;
//	}

	public String getAllowSecondsToAnswerEachQuestion() {
		return allowSecondsToAnswerEachQuestion;
	}

	public void setAllowSecondsToAnswerEachQuestion(
			String allowSecondsToAnswerEachQuestion) {
		this.allowSecondsToAnswerEachQuestion = allowSecondsToAnswerEachQuestion;
	}

	public String getRequireIdentityValidationEverySeconds() {
		return requireIdentityValidationEverySeconds;
	}

	public void setRequireIdentityValidationEverySeconds(
			String requireIdentityValidationEverySeconds) {
		this.requireIdentityValidationEverySeconds = requireIdentityValidationEverySeconds;
	}

	public String getValidationNoMissedQuestionsAllowed() {
		return validationNoMissedQuestionsAllowed;
	}

	public void setValidationNoMissedQuestionsAllowed(
			String validationNoMissedQuestionsAllowed) {
		this.validationNoMissedQuestionsAllowed = validationNoMissedQuestionsAllowed;
	}

	public String getValidationTimeBetweenQuestion() {
		return validationTimeBetweenQuestion;
	}

	public void setValidationTimeBetweenQuestion(
			String validationTimeBetweenQuestion) {
		this.validationTimeBetweenQuestion = validationTimeBetweenQuestion;
	}
	public String getUnitOfDaysOfRegistration() {
		return unitOfDaysOfRegistration;
	}

	public void setUnitOfDaysOfRegistration(String unitOfDaysOfRegistration) {
		this.unitOfDaysOfRegistration = unitOfDaysOfRegistration;
	}

	public String[] getListOfUnitOfDaysOfRegistration() {
		
		listOfUnitOfDaysOfRegistration=new String[3];
		listOfUnitOfDaysOfRegistration[0]=UNIT_MINUTES;
		listOfUnitOfDaysOfRegistration[1]=UNIT_DAYS;
		listOfUnitOfDaysOfRegistration[2]=UNIT_MONTHS;
		
		return listOfUnitOfDaysOfRegistration;
		
	}

	public boolean isCourseStrictlyEnforcePolicyToBeUsed() {
		return courseStrictlyEnforcePolicyToBeUsed;
	}

	public void setCourseStrictlyEnforcePolicyToBeUsed(
			boolean courseStrictlyEnforcePolicyToBeUsed) {
		this.courseStrictlyEnforcePolicyToBeUsed = courseStrictlyEnforcePolicyToBeUsed;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getPlayerCourseFlow() {
		return playerCourseFlow;
	}

	public void setPlayerCourseFlow(String playerCourseFlow) {
		this.playerCourseFlow = playerCourseFlow;
	}

	public boolean isPlayerStrictlyEnforcePolicyToBeUsed() {
		return playerStrictlyEnforcePolicyToBeUsed;
	}

	public void setPlayerStrictlyEnforcePolicyToBeUsed(
			boolean playerStrictlyEnforcePolicyToBeUsed) {
		this.playerStrictlyEnforcePolicyToBeUsed = playerStrictlyEnforcePolicyToBeUsed;
	}

	public String getDisplayCourseEvaluation() {
		return displayCourseEvaluation;
	}

	public void setDisplayCourseEvaluation(String displayCourseEvaluation) {
		this.displayCourseEvaluation = displayCourseEvaluation;
	}

	public Boolean isCertificateEnabled() {
		return certificateEnabled;
	}

	public void setCertificateEnabled(Boolean certificateEnabled) {
		this.certificateEnabled = certificateEnabled;
	}

	public boolean isValidationStrictlyEnforcePolicyToBeUsed() {
		return validationStrictlyEnforcePolicyToBeUsed;
	}

	public void setValidationStrictlyEnforcePolicyToBeUsed(
			boolean validationStrictlyEnforcePolicyToBeUsed) {
		this.validationStrictlyEnforcePolicyToBeUsed = validationStrictlyEnforcePolicyToBeUsed;
	}

	public String getEndOfCourseInstructions() {
		return endOfCourseInstructions;
	}

	public void setEndOfCourseInstructions(String endOfCourseInstructions) {
		this.endOfCourseInstructions = endOfCourseInstructions;
	}

	public boolean isSeattimeenabled() {
		return seattimeenabled;
	}
	
	public boolean getSeattimeenabled() {
		return seattimeenabled;
	}

	public void setSeattimeenabled(boolean seattimeenabled) {
		this.seattimeenabled = seattimeenabled;
	}

	public String getSeattimeinhour() {
		return seattimeinhour;
	}

	public void setSeattimeinhour(String seattimeinhour) {
		this.seattimeinhour = seattimeinhour;
	}

	public String getSeattimeinmin() {
		return seattimeinmin;
	}

	public void setSeattimeinmin(String seattimeinmin) {
		this.seattimeinmin = seattimeinmin;
	}

	public String getMessageseattimeexceeds() {
		return messageseattimeexceeds;
	}

	public void setMessageseattimeexceeds(String messageseattimeexceeds) {
		this.messageseattimeexceeds = messageseattimeexceeds;
	}

	public String getMessageseattimecourselaunch() {
		return messageseattimecourselaunch;
	}

	public void setMessageseattimecourselaunch(String messageseattimecourselaunch) {
		this.messageseattimecourselaunch = messageseattimecourselaunch;
	}

	public String getActiontotakeuponidletimeout() {
		return actiontotakeuponidletimeout;
	}

	public void setActiontotakeuponidletimeout(String actiontotakeuponidletimeout) {
		this.actiontotakeuponidletimeout = actiontotakeuponidletimeout;
	}

	public void setListOfUnitOfDaysOfRegistration(
			String[] listOfUnitOfDaysOfRegistration) {
		this.listOfUnitOfDaysOfRegistration = listOfUnitOfDaysOfRegistration;
	}

	public boolean isPreAssessmentEnabled() {
		return preAssessmentEnabled;
	}

	public void setPreAssessmentEnabled(boolean preAssessmentEnabled) {
		this.preAssessmentEnabled = preAssessmentEnabled;
	}


	public String getPreActionToTakeAfterMaximumAttemptsReached() {
		return preActionToTakeAfterMaximumAttemptsReached;
	}

	public void setPreActionToTakeAfterMaximumAttemptsReached(
			String preActionToTakeAfterMaximumAttemptsReached) {
		this.preActionToTakeAfterMaximumAttemptsReached = preActionToTakeAfterMaximumAttemptsReached;
	}



	public int getPreMaximumTimeLimitMinutes() {
		return preMaximumTimeLimitMinutes;
	}

	public void setPreMaximumTimeLimitMinutes(int preMaximumTimeLimitMinutes) {
		this.preMaximumTimeLimitMinutes = preMaximumTimeLimitMinutes;
	}

	public boolean isPreUseUniqueQuestionsOnRetake() {
		return preUseUniqueQuestionsOnRetake;
	}

	public void setPreUseUniqueQuestionsOnRetake(
			boolean preUseUniqueQuestionsOnRetake) {
		this.preUseUniqueQuestionsOnRetake = preUseUniqueQuestionsOnRetake;
	}

	public int getPreMinimumSeatTimeBeforeAssessmentStart() {
		return preMinimumSeatTimeBeforeAssessmentStart;
	}

	public void setPreMinimumSeatTimeBeforeAssessmentStart(
			int preMinimumSeatTimeBeforeAssessmentStart) {
		this.preMinimumSeatTimeBeforeAssessmentStart = preMinimumSeatTimeBeforeAssessmentStart;
	}

	public String getPreMinimumSeatTimeBeforeAssessmentStartUnits() {
		return preMinimumSeatTimeBeforeAssessmentStartUnits;
	}

	public void setPreMinimumSeatTimeBeforeAssessmentStartUnits(
			String preMinimumSeatTimeBeforeAssessmentStartUnits) {
		this.preMinimumSeatTimeBeforeAssessmentStartUnits = preMinimumSeatTimeBeforeAssessmentStartUnits;
	}

	public boolean isPreEnableContentRemediation() {
		return preEnableContentRemediation;
	}

	public void setPreEnableContentRemediation(boolean preEnableContentRemediation) {
		this.preEnableContentRemediation = preEnableContentRemediation;
	}

	public boolean isPreEnableAssessmentProctoring() {
		return preEnableAssessmentProctoring;
	}

	public void setPreEnableAssessmentProctoring(
			boolean preEnableAssessmentProctoring) {
		this.preEnableAssessmentProctoring = preEnableAssessmentProctoring;
	}

	public boolean isPreShowQuestionLevelResults() {
		return preShowQuestionLevelResults;
	}

	public void setPreShowQuestionLevelResults(boolean preShowQuestionLevelResults) {
		this.preShowQuestionLevelResults = preShowQuestionLevelResults;
	}

	public boolean isPreRandomizeQuestions() {
		return preRandomizeQuestions;
	}

	public void setPreRandomizeQuestions(boolean preRandomizeQuestions) {
		this.preRandomizeQuestions = preRandomizeQuestions;
	}

	public boolean isPreRandomizeAnswers() {
		return preRandomizeAnswers;
	}

	public void setPreRandomizeAnswers(boolean preRandomizeAnswers) {
		this.preRandomizeAnswers = preRandomizeAnswers;
	}

	public String getPreScoringType() {
		return preScoringType;
	}

	public void setPreScoringType(String preScoringType) {
		this.preScoringType = preScoringType;
	}

	public boolean isPreShowQuestionAnswerReview() {
		return preShowQuestionAnswerReview;
	}

	public void setPreShowQuestionAnswerReview(boolean preShowQuestionAnswerReview) {
		this.preShowQuestionAnswerReview = preShowQuestionAnswerReview;
	}

	public boolean isPreEnableRestrictiveAssessmentMode() {
		return preEnableRestrictiveAssessmentMode;
	}

	public void setPreEnableRestrictiveAssessmentMode(
			boolean preEnableRestrictiveAssessmentMode) {
		this.preEnableRestrictiveAssessmentMode = preEnableRestrictiveAssessmentMode;
	}

	public String getPreGradingBehavior() {
		return preGradingBehavior;
	}

	public void setPreGradingBehavior(String preGradingBehavior) {
		this.preGradingBehavior = preGradingBehavior;
	}

	public boolean isPreEnableWeightedScoring() {
		return preEnableWeightedScoring;
	}

	public void setPreEnableWeightedScoring(boolean preEnableWeightedScoring) {
		this.preEnableWeightedScoring = preEnableWeightedScoring;
	}
		

	public boolean isPreAdvancedQuestionSelectionTypeCB() {
		return preAdvancedQuestionSelectionTypeCB;
	}

	public void setPreAdvancedQuestionSelectionTypeCB(
			boolean preAdvancedQuestionSelectionTypeCB) {
		this.preAdvancedQuestionSelectionTypeCB = preAdvancedQuestionSelectionTypeCB;
	}

	public String getPreAdvancedQuestionSelectionType() {
		return preAdvancedQuestionSelectionType;
	}

	public void setPreAdvancedQuestionSelectionType(
			String preAdvancedQuestionSelectionType) {
		this.preAdvancedQuestionSelectionType = preAdvancedQuestionSelectionType;
	}

	public boolean isPreAllowQuestionSkipping() {
		return preAllowQuestionSkipping;
	}

	public void setPreAllowQuestionSkipping(boolean preAllowQuestionSkipping) {
		this.preAllowQuestionSkipping = preAllowQuestionSkipping;
	}

	public boolean isPostAssessmentEnabled() {
		return postAssessmentEnabled;
	}

	public void setPostAssessmentEnabled(boolean postAssessmentEnabled) {
		this.postAssessmentEnabled = postAssessmentEnabled;
	}

	
	public String getPostActionToTakeAfterMaximumAttemptsReached() {
		return postActionToTakeAfterMaximumAttemptsReached;
	}

	public void setPostActionToTakeAfterMaximumAttemptsReached(
			String postActionToTakeAfterMaximumAttemptsReached) {
		this.postActionToTakeAfterMaximumAttemptsReached = postActionToTakeAfterMaximumAttemptsReached;
	}



	public int getPostMaximumTimeLimitMinutes() {
		return postMaximumTimeLimitMinutes;
	}

	public void setPostMaximumTimeLimitMinutes(int postMaximumTimeLimitMinutes) {
		this.postMaximumTimeLimitMinutes = postMaximumTimeLimitMinutes;
	}

	public boolean isPostUseUniqueQuestionsOnRetake() {
		return postUseUniqueQuestionsOnRetake;
	}

	public void setPostUseUniqueQuestionsOnRetake(
			boolean postUseUniqueQuestionsOnRetake) {
		this.postUseUniqueQuestionsOnRetake = postUseUniqueQuestionsOnRetake;
	}

	public String getPostMinimumSeatTimeBeforeAssessmentStart() {
		return postMinimumSeatTimeBeforeAssessmentStart;
	}

	public void setPostMinimumSeatTimeBeforeAssessmentStart(
			String postMinimumSeatTimeBeforeAssessmentStart) {
		this.postMinimumSeatTimeBeforeAssessmentStart = postMinimumSeatTimeBeforeAssessmentStart;
	}

	public String getPostMinimumSeatTimeBeforeAssessmentStartUnits() {
		return postMinimumSeatTimeBeforeAssessmentStartUnits;
	}

	public void setPostMinimumSeatTimeBeforeAssessmentStartUnits(
			String postMinimumSeatTimeBeforeAssessmentStartUnits) {
		this.postMinimumSeatTimeBeforeAssessmentStartUnits = postMinimumSeatTimeBeforeAssessmentStartUnits;
	}

	public boolean isPostEnableContentRemediation() {
		return postEnableContentRemediation;
	}

	public void setPostEnableContentRemediation(boolean postEnableContentRemediation) {
		this.postEnableContentRemediation = postEnableContentRemediation;
	}

	public boolean isPostEnableAssessmentProctoring() {
		return postEnableAssessmentProctoring;
	}

	public void setPostEnableAssessmentProctoring(
			boolean postEnableAssessmentProctoring) {
		this.postEnableAssessmentProctoring = postEnableAssessmentProctoring;
	}

	public boolean isPostShowQuestionLevelResults() {
		return postShowQuestionLevelResults;
	}

	public void setPostShowQuestionLevelResults(boolean postShowQuestionLevelResults) {
		this.postShowQuestionLevelResults = postShowQuestionLevelResults;
	}

	public boolean isPostRandomizeQuestions() {
		return postRandomizeQuestions;
	}

	public void setPostRandomizeQuestions(boolean postRandomizeQuestions) {
		this.postRandomizeQuestions = postRandomizeQuestions;
	}

	public boolean isPostRandomizeAnswers() {
		return postRandomizeAnswers;
	}

	public void setPostRandomizeAnswers(boolean postRandomizeAnswers) {
		this.postRandomizeAnswers = postRandomizeAnswers;
	}

	public String getPostScoringType() {
		return postScoringType;
	}

	public void setPostScoringType(String postScoringType) {
		this.postScoringType = postScoringType;
	}

	public boolean isPostShowQuestionAnswerReview() {
		return postShowQuestionAnswerReview;
	}

	public void setPostShowQuestionAnswerReview(boolean postShowQuestionAnswerReview) {
		this.postShowQuestionAnswerReview = postShowQuestionAnswerReview;
	}

	public boolean isPostEnableRestrictiveAssessmentMode() {
		return postEnableRestrictiveAssessmentMode;
	}

	public void setPostEnableRestrictiveAssessmentMode(
			boolean postEnableRestrictiveAssessmentMode) {
		this.postEnableRestrictiveAssessmentMode = postEnableRestrictiveAssessmentMode;
	}

	public String getPostGradingBehavior() {
		return postGradingBehavior;
	}

	public void setPostGradingBehavior(String postGradingBehavior) {
		this.postGradingBehavior = postGradingBehavior;
	}

	public boolean isPostEnableWeightedScoring() {
		return postEnableWeightedScoring;
	}

	public void setPostEnableWeightedScoring(boolean postEnableWeightedScoring) {
		this.postEnableWeightedScoring = postEnableWeightedScoring;
	}

	public String getPostAdvancedQuestionSelectionType() {
		return postAdvancedQuestionSelectionType;
	}

	public void setPostAdvancedQuestionSelectionType(
			String postAdvancedQuestionSelectionType) {
		this.postAdvancedQuestionSelectionType = postAdvancedQuestionSelectionType;
	}

	public boolean isPostAllowQuestionSkipping() {
		return postAllowQuestionSkipping;
	}

	public void setPostAllowQuestionSkipping(boolean postAllowQuestionSkipping) {
		this.postAllowQuestionSkipping = postAllowQuestionSkipping;
	}

	public boolean isQuizAssessmentEnabled() {
		return quizAssessmentEnabled;
	}

	public void setQuizAssessmentEnabled(boolean quizAssessmentEnabled) {
		this.quizAssessmentEnabled = quizAssessmentEnabled;
	}

	
	public String getQuizAssessmentActionToTakeAfterMaximumAttemptsReached() {
		return quizAssessmentActionToTakeAfterMaximumAttemptsReached;
	}

	public void setQuizAssessmentActionToTakeAfterMaximumAttemptsReached(
			String quizAssessmentActionToTakeAfterMaximumAttemptsReached) {
		this.quizAssessmentActionToTakeAfterMaximumAttemptsReached = quizAssessmentActionToTakeAfterMaximumAttemptsReached;
	}

	
	public int getQuizAssessmentMaximumTimeLimitMinutes() {
		return quizAssessmentMaximumTimeLimitMinutes;
	}

	public void setQuizAssessmentMaximumTimeLimitMinutes(int quizAssessmentMaximumTimeLimitMinutes) {
		this.quizAssessmentMaximumTimeLimitMinutes = quizAssessmentMaximumTimeLimitMinutes;
	}

	public boolean isQuizAssessmentUseUniqueQuestionsOnRetake() {
		return quizAssessmentUseUniqueQuestionsOnRetake;
	}

	public void setQuizAssessmentUseUniqueQuestionsOnRetake(
			boolean quizAssessmentUseUniqueQuestionsOnRetake) {
		this.quizAssessmentUseUniqueQuestionsOnRetake = quizAssessmentUseUniqueQuestionsOnRetake;
	}

	public int getQuizAssessmentMinimumSeatTimeBeforeAssessmentStart() {
		return quizAssessmentMinimumSeatTimeBeforeAssessmentStart;
	}

	public void setQuizAssessmentMinimumSeatTimeBeforeAssessmentStart(
			int quizAssessmentMinimumSeatTimeBeforeAssessmentStart) {
		this.quizAssessmentMinimumSeatTimeBeforeAssessmentStart = quizAssessmentMinimumSeatTimeBeforeAssessmentStart;
	}

	public String getQuizAssessmentMinimumSeatTimeBeforeAssessmentStartUnits() {
		return quizAssessmentMinimumSeatTimeBeforeAssessmentStartUnits;
	}

	public void setQuizAssessmentMinimumSeatTimeBeforeAssessmentStartUnits(
			String quizAssessmentMinimumSeatTimeBeforeAssessmentStartUnits) {
		this.quizAssessmentMinimumSeatTimeBeforeAssessmentStartUnits = quizAssessmentMinimumSeatTimeBeforeAssessmentStartUnits;
	}

	public boolean isQuizAssessmentEnableContentRemediation() {
		return quizAssessmentEnableContentRemediation;
	}

	public void setQuizAssessmentEnableContentRemediation(boolean quizAssessmentEnableContentRemediation) {
		this.quizAssessmentEnableContentRemediation = quizAssessmentEnableContentRemediation;
	}

	public boolean isQuizAssessmentEnableAssessmentProctoring() {
		return quizAssessmentEnableAssessmentProctoring;
	}

	public void setQuizAssessmentEnableAssessmentProctoring(
			boolean quizAssessmentEnableAssessmentProctoring) {
		this.quizAssessmentEnableAssessmentProctoring = quizAssessmentEnableAssessmentProctoring;
	}

	public boolean isQuizAssessmentShowQuestionLevelResults() {
		return quizAssessmentShowQuestionLevelResults;
	}

	public void setQuizAssessmentShowQuestionLevelResults(boolean quizAssessmentShowQuestionLevelResults) {
		this.quizAssessmentShowQuestionLevelResults = quizAssessmentShowQuestionLevelResults;
	}

	public boolean isQuizAssessmentRandomizeQuestions() {
		return quizAssessmentRandomizeQuestions;
	}

	public void setQuizAssessmentRandomizeQuestions(boolean quizAssessmentRandomizeQuestions) {
		this.quizAssessmentRandomizeQuestions = quizAssessmentRandomizeQuestions;
	}

	public boolean isQuizAssessmentRandomizeAnswers() {
		return quizAssessmentRandomizeAnswers;
	}

	public void setQuizAssessmentRandomizeAnswers(boolean quizAssessmentRandomizeAnswers) {
		this.quizAssessmentRandomizeAnswers = quizAssessmentRandomizeAnswers;
	}

	public String getQuizAssessmentScoringType() {
		return quizAssessmentScoringType;
	}

	public void setQuizAssessmentScoringType(String quizAssessmentScoringType) {
		this.quizAssessmentScoringType = quizAssessmentScoringType;
	}

	public boolean isQuizAssessmentShowQuestionAnswerReview() {
		return quizAssessmentShowQuestionAnswerReview;
	}

	public void setQuizAssessmentShowQuestionAnswerReview(boolean quizAssessmentShowQuestionAnswerReview) {
		this.quizAssessmentShowQuestionAnswerReview = quizAssessmentShowQuestionAnswerReview;
	}

	public boolean isQuizAssessmentEnableRestrictiveAssessmentMode() {
		return quizAssessmentEnableRestrictiveAssessmentMode;
	}

	public void setQuizAssessmentEnableRestrictiveAssessmentMode(
			boolean quizAssessmentEnableRestrictiveAssessmentMode) {
		this.quizAssessmentEnableRestrictiveAssessmentMode = quizAssessmentEnableRestrictiveAssessmentMode;
	}

	public String getQuizAssessmentGradingBehavior() {
		return quizAssessmentGradingBehavior;
	}

	public void setQuizAssessmentGradingBehavior(String quizAssessmentGradingBehavior) {
		this.quizAssessmentGradingBehavior = quizAssessmentGradingBehavior;
	}

	public boolean isQuizAssessmentEnableWeightedScoring() {
		return quizAssessmentEnableWeightedScoring;
	}

	public void setQuizAssessmentEnableWeightedScoring(boolean quizAssessmentEnableWeightedScoring) {
		this.quizAssessmentEnableWeightedScoring = quizAssessmentEnableWeightedScoring;
	}

	public String getQuizAssessmentAdvancedQuestionSelectionType() {
		return quizAssessmentAdvancedQuestionSelectionType;
	}

	public void setQuizAssessmentAdvancedQuestionSelectionType(
			String quizAssessmentAdvancedQuestionSelectionType) {
		this.quizAssessmentAdvancedQuestionSelectionType = quizAssessmentAdvancedQuestionSelectionType;
	}

	public boolean isQuizAssessmentAllowQuestionSkipping() {
		return quizAssessmentAllowQuestionSkipping;
	}

	public void setQuizAssessmentAllowQuestionSkipping(boolean quizAssessmentAllowQuestionSkipping) {
		this.quizAssessmentAllowQuestionSkipping = quizAssessmentAllowQuestionSkipping;
	}
	
	
	
	public boolean isMustCompleteCourseEvaluation() {
		return mustCompleteCourseEvaluation;
	}

	public void setMustCompleteCourseEvaluation(boolean mustCompleteCourseEvaluation) {
		this.mustCompleteCourseEvaluation = mustCompleteCourseEvaluation;
	}
	
	public boolean isSuggestedCourse() {
		return suggestedCourse;
	}

    public void setSuggestedCourse(boolean suggestedCourse) {
		this.suggestedCourse = suggestedCourse;
	}

	public boolean isRateCourse() {
		return rateCourse;
	}

    public void setRateCourse(boolean rateCourse) {
		this.rateCourse = rateCourse;
	}


	public boolean isDisplaySeatTimeTextMessage() {
		return displaySeatTimeTextMessage;
	}
	public void setDisplaySeatTimeTextMessage(boolean displaySeatTimeTextMessage) {
		this.displaySeatTimeTextMessage = displaySeatTimeTextMessage;
	}
	public boolean isLockPostAssessmentBeforeSeatTime() {
		return lockPostAssessmentBeforeSeatTime;
	}
	public void setLockPostAssessmentBeforeSeatTime(
			boolean lockPostAssessmentBeforeSeatTime) {
		this.lockPostAssessmentBeforeSeatTime = lockPostAssessmentBeforeSeatTime;
	}

	public String getAcknowledgeText() {
		return acknowledgeText;
	}

	public void setAcknowledgeText(String acknowledgeText) {
		this.acknowledgeText = acknowledgeText;
	}

	public boolean isAcknowledgeEnabled() {
		return acknowledgeEnabled;
	}

	public void setAcknowledgeEnabled(boolean acknowledgeEnabled) {
		this.acknowledgeEnabled = acknowledgeEnabled;
	}

	public boolean isShowStandardIntroduction() {
		return showStandardIntroduction;
	}

	public void setShowStandardIntroduction(boolean showStandardIntroduction) {
		this.showStandardIntroduction = showStandardIntroduction;
	}

	public boolean isShowOrientationScenes() {
		return showOrientationScenes;
	}
	
	public boolean getShowOrientationScenes() {
		return showOrientationScenes;
	}

	public void setShowOrientationScenes(boolean showOrientationScenes) {
		this.showOrientationScenes = showOrientationScenes;
	}

	public boolean isShowEndOfCourseScene() {
		return showEndOfCourseScene;
	}
	
	public boolean getShowEndOfCourseScene() {
		return showEndOfCourseScene;
	}

	public void setShowEndOfCourseScene(boolean showEndOfCourseScene) {
		this.showEndOfCourseScene = showEndOfCourseScene;
	}

	public boolean isShowContent() {
		return showContent;
	}
	
	public boolean getShowContent() {
		return showContent;
	}

	public void setShowContent(boolean showContent) {
		this.showContent = showContent;
	}

	public boolean isEnforceTimedOutlineAllScenes() {
		return enforceTimedOutlineAllScenes;
	}
	
	public boolean getEnforceTimedOutlineAllScenes() {
		return enforceTimedOutlineAllScenes;
	}

	public void setEnforceTimedOutlineAllScenes(boolean enforceTimedOutlineAllScenes) {
		this.enforceTimedOutlineAllScenes = enforceTimedOutlineAllScenes;
	}

	public boolean isAllowUserToReviewCourseAfterCompletion() {
		return allowUserToReviewCourseAfterCompletion;
	}
	
	public boolean getAllowUserToReviewCourseAfterCompletion() {
		return allowUserToReviewCourseAfterCompletion;
	}

	public void setAllowUserToReviewCourseAfterCompletion(
			boolean allowUserToReviewCourseAfterCompletion) {
		this.allowUserToReviewCourseAfterCompletion = allowUserToReviewCourseAfterCompletion;
	}

	public boolean isMustAttemptPostAssessment() {
		return mustAttemptPostAssessment;
	}
	
	public boolean getMustAttemptPostAssessment() {
		return mustAttemptPostAssessment;
	}

	public void setMustAttemptPostAssessment(boolean mustAttemptPostAssessment) {
		this.mustAttemptPostAssessment = mustAttemptPostAssessment;
	}

	public boolean isMustDemonstratePostAssessmentMastery() {
		return mustDemonstratePostAssessmentMastery;
	}
	
	public boolean getMustDemonstratePostAssessmentMastery() {
		return mustDemonstratePostAssessmentMastery;
	}

	public void setMustDemonstratePostAssessmentMastery(
			boolean mustDemonstratePostAssessmentMastery) {
		this.mustDemonstratePostAssessmentMastery = mustDemonstratePostAssessmentMastery;
	}

	public boolean isMustDemonstratePreAssessmentMastery() {
		return mustDemonstratePreAssessmentMastery;
	}
	
	public boolean getMustDemonstratePreAssessmentMastery() {
		return mustDemonstratePreAssessmentMastery;
	}

	public void setMustDemonstratePreAssessmentMastery(
			boolean mustDemonstratePreAssessmentMastery) {
		this.mustDemonstratePreAssessmentMastery = mustDemonstratePreAssessmentMastery;
	}

	public boolean isMustDemonstrateQuizAssessmentMastery() {
		return mustDemonstrateQuizAssessmentMastery;
	}
	
	public boolean getMustDemonstrateQuizAssessmentMastery() {
		return mustDemonstrateQuizAssessmentMastery;
	}

	public void setMustDemonstrateQuizAssessmentMastery(boolean mustDemonstrateQuizAssessmentMastery) {
		this.mustDemonstrateQuizAssessmentMastery = mustDemonstrateQuizAssessmentMastery;
	}

	public boolean isMustCompleteAnySurveys() {
		return mustCompleteAnySurveys;
	}
	
	public boolean getMustCompleteAnySurveys() {
		return mustCompleteAnySurveys;
	}

	public void setMustCompleteAnySurveys(boolean mustCompleteAnySurveys) {
		this.mustCompleteAnySurveys = mustCompleteAnySurveys;
	}

	public boolean isMustViewEverySceneInTheCourse() {
		return mustViewEverySceneInTheCourse;
	}
	
	public boolean getMustViewEverySceneInTheCourse() {
		return mustViewEverySceneInTheCourse;
	}

	public void setMustViewEverySceneInTheCourse(
			boolean mustViewEverySceneInTheCourse) {
		this.mustViewEverySceneInTheCourse = mustViewEverySceneInTheCourse;
	}

	public boolean isCanOnlyBeCompleteAfterNumberOfCourseLaunches() {
		return canOnlyBeCompleteAfterNumberOfCourseLaunches;
	}
	
	public boolean getCanOnlyBeCompleteAfterNumberOfCourseLaunches() {
		return canOnlyBeCompleteAfterNumberOfCourseLaunches;
	}

	public void setCanOnlyBeCompleteAfterNumberOfCourseLaunches(
			boolean canOnlyBeCompleteAfterNumberOfCourseLaunches) {
		this.canOnlyBeCompleteAfterNumberOfCourseLaunches = canOnlyBeCompleteAfterNumberOfCourseLaunches;
	}

	public boolean isCompleteWithinTimePeriod() {
		return completeWithinTimePeriod;
	}
	
	public boolean getCompleteWithinTimePeriod() {
		return completeWithinTimePeriod;
	}

	public void setCompleteWithinTimePeriod(boolean completeWithinTimePeriod) {
		this.completeWithinTimePeriod = completeWithinTimePeriod;
	}

	public boolean isCompleteWithinDaysOfRegistration() {
		return completeWithinDaysOfRegistration;
	}
	
	public boolean getCompleteWithinDaysOfRegistration() {
		return completeWithinDaysOfRegistration;
	}

	public void setCompleteWithinDaysOfRegistration(
			boolean completeWithinDaysOfRegistration) {
		this.completeWithinDaysOfRegistration = completeWithinDaysOfRegistration;
	}

	public String getUnitOfTimeToComplete() {
		return unitOfTimeToComplete;
	}

	public void setUnitOfTimeToComplete(String unitOfTimeToComplete) {
		this.unitOfTimeToComplete = unitOfTimeToComplete;
	}





	public boolean isEnableIdentityValidation() {
		return enableIdentityValidation;
	}

	public void setEnableIdentityValidation(boolean enableIdentityValidation) {
		this.enableIdentityValidation = enableIdentityValidation;
	}

	

	public Certificate getCompletionCertificate() {
		return completionCertificate;
	}

	public void setCompletionCertificate(Certificate completionCertificate) {
		this.completionCertificate = completionCertificate;
	}
	
	public boolean isRespondToCourseEvaluation() {
		return respondToCourseEvaluation;
	}

	public void setRespondToCourseEvaluation(boolean respondToCourseEvaluation) {
		this.respondToCourseEvaluation = respondToCourseEvaluation;
	}

	public String getNumberOfValidationQuestions() {
		return numberOfValidationQuestions;
	}

	public void setNumberOfValidationQuestions(String numberOfValidationQuestions) {
		this.numberOfValidationQuestions = numberOfValidationQuestions;
	}

	public boolean isCourseEvaluationSpecified() {
		return courseEvaluationSpecified;
	}

	public void setCourseEvaluationSpecified(boolean courseEvaluationSpecified) {
		this.courseEvaluationSpecified = courseEvaluationSpecified;
	}

	public String getPreNoResultMessage() {
		return preNoResultMessage;
	}

	public void setPreNoResultMessage(String preNoResultMessage) {
		this.preNoResultMessage = preNoResultMessage;
	}

	public String getPostNoResultMessage() {
		return postNoResultMessage;
	}

	public void setPostNoResultMessage(String postNoResultMessage) {
		this.postNoResultMessage = postNoResultMessage;
	}

	public String getQuizAssessmentNoResultMessage() {
		return quizAssessmentNoResultMessage;
	}

	public void setQuizAssessmentNoResultMessage(String quizAssessmentNoResultMessage) {
		this.quizAssessmentNoResultMessage = quizAssessmentNoResultMessage;
	}

        /**
         * @return the preEnableAdvancedQuestionSelectionType
         */
        public boolean isPreEnableAdvancedQuestionSelectionType() {
            return preEnableAdvancedQuestionSelectionType;
        }

        /**
         * @param preEnableAdvancedQuestionSelectionType the preEnableAdvancedQuestionSelectionType to set
         */
        public void setPreEnableAdvancedQuestionSelectionType(boolean preEnableAdvancedQuestionSelectionType) {
            this.preEnableAdvancedQuestionSelectionType = preEnableAdvancedQuestionSelectionType;
        }

    /**
     * @return the quizAssessmentEnableAdvancedQuestionSelectionType
     */
    public boolean isQuizAssessmentEnableAdvancedQuestionSelectionType() {
        return quizAssessmentEnableAdvancedQuestionSelectionType;
    }

    /**
     * @param quizAssessmentEnableAdvancedQuestionSelectionType the quizAssessmentEnableAdvancedQuestionSelectionType to set
     */
    public void setQuizAssessmentEnableAdvancedQuestionSelectionType(boolean quizAssessmentEnableAdvancedQuestionSelectionType) {
        this.quizAssessmentEnableAdvancedQuestionSelectionType = quizAssessmentEnableAdvancedQuestionSelectionType;
    }

    /**
     * @return the postEnableAdvancedQuestionSelectionType
     */
    public boolean isPostEnableAdvancedQuestionSelectionType() {
        return postEnableAdvancedQuestionSelectionType;
    }

    /**
     * @param postEnableAdvancedQuestionSelectionType the postEnableAdvancedQuestionSelectionType to set
     */
    public void setPostEnableAdvancedQuestionSelectionType(boolean postEnableAdvancedQuestionSelectionType) {
        this.postEnableAdvancedQuestionSelectionType = postEnableAdvancedQuestionSelectionType;
    }

    /**
     * @return the preAssessmentMaximumnoattemptEnabled
     */
    public boolean isPreAssessmentMaximumnoattemptEnabled() {
        return preAssessmentMaximumnoattemptEnabled;
    }

    /**
     * @param preAssessmentMaximumnoattemptEnabled the preAssessmentMaximumnoattemptEnabled to set
     */
    public void setPreAssessmentMaximumnoattemptEnabled(boolean preAssessmentMaximumnoattemptEnabled) {
        this.preAssessmentMaximumnoattemptEnabled = preAssessmentMaximumnoattemptEnabled;
    }

    /**
     * @return the quizAssessmentMaximumnoattemptEnabled
     */
    public boolean isQuizAssessmentMaximumnoattemptEnabled() {
        return quizAssessmentMaximumnoattemptEnabled;
    }

    /**
     * @param quizAssessmentMaximumnoattemptEnabled the quizAssessmentMaximumnoattemptEnabled to set
     */
    public void setQuizAssessmentMaximumnoattemptEnabled(boolean quizAssessmentMaximumnoattemptEnabled) {
        this.quizAssessmentMaximumnoattemptEnabled = quizAssessmentMaximumnoattemptEnabled;
    }

    /**
     * @return the postAssessmentMaximumnoattemptEnabled
     */
    public boolean isPostAssessmentMaximumnoattemptEnabled() {
        return postAssessmentMaximumnoattemptEnabled;
    }

    /**
     * @param postAssessmentMaximumnoattemptEnabled the postAssessmentMaximumnoattemptEnabled to set
     */
    public void setPostAssessmentMaximumnoattemptEnabled(boolean postAssessmentMaximumnoattemptEnabled) {
        this.postAssessmentMaximumnoattemptEnabled = postAssessmentMaximumnoattemptEnabled;
    }

	public boolean getQuizAssessmentEnforcemaximumtimelimitCB() {
		return quizAssessmentEnforcemaximumtimelimitCB;
	}
	public void setQuizAssessmentEnforcemaximumtimelimitCB(
			boolean quizAssessmentEnforcemaximumtimelimitCB) {
		this.quizAssessmentEnforcemaximumtimelimitCB = quizAssessmentEnforcemaximumtimelimitCB;
	}

	public boolean getPostassessmentEnforcemaximumtimelimitCB() {
		return postassessmentEnforcemaximumtimelimitCB;
	}
	public void setPostassessmentEnforcemaximumtimelimitCB(
			boolean postassessmentEnforcemaximumtimelimitCB) {
		this.postassessmentEnforcemaximumtimelimitCB = postassessmentEnforcemaximumtimelimitCB;
	}

	public boolean isQuizAssessmentMaximumNoAttemptCB() {
		return quizAssessmentMaximumNoAttemptCB;
	}

	public void setQuizAssessmentMaximumNoAttemptCB(boolean quizAssessmentMaximumNoAttemptCB) {
		this.quizAssessmentMaximumNoAttemptCB = quizAssessmentMaximumNoAttemptCB;
	}

	public boolean isQuizAssessmentAdvancedQuestionSelectionTypeCB() {
		return quizAssessmentAdvancedQuestionSelectionTypeCB;
	}

	public void setQuizAssessmentAdvancedQuestionSelectionTypeCB(
			boolean quizAssessmentAdvancedQuestionSelectionTypeCB) {
		this.quizAssessmentAdvancedQuestionSelectionTypeCB = quizAssessmentAdvancedQuestionSelectionTypeCB;
	}

	public boolean isPostAdvancedQuestionSelectionTypeCB() {
		return postAdvancedQuestionSelectionTypeCB;
	}

	public void setPostAdvancedQuestionSelectionTypeCB(
			boolean postAdvancedQuestionSelectionTypeCB) {
		this.postAdvancedQuestionSelectionTypeCB = postAdvancedQuestionSelectionTypeCB;
	}

	public boolean isPostAssessmentMaximumNoAttemptCB() {
		return postAssessmentMaximumNoAttemptCB;
	}

	public void setPostAssessmentMaximumNoAttemptCB(
			boolean postAssessmentMaximumNoAttemptCB) {
		this.postAssessmentMaximumNoAttemptCB = postAssessmentMaximumNoAttemptCB;
	}

    /**
     * @return the certificateName
     */
    public String getCertificateName() {
        return certificateName;
    }

    /**
     * @param certificateName the certificateName to set
     */
    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

	/**
	 * @return the mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified
	 */
	public boolean isMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified() {
		return mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified;
	}

	/**
	 * @param mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified the mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified to set
	 */
	public void setMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified(
			boolean mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified) {
		this.mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified = mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified;
	}

	/**
	 * @return the mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration
	 */
	public int getMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration() {
		return mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration;
	}

	/**
	 * @param mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration the mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration to set
	 */
	public void setMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration(
			int mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration) {
		this.mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration = mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration;
	}

	/**
	 * @return the mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationUnit
	 */
	public String getMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationUnit() {
		return mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationUnit;
	}

	/**
	 * @param mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationUnit the mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationUnit to set
	 */
	public void setMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationUnit(
			String mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationUnit) {
		this.mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationUnit = mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationUnit;
	}

	/**
	 * @return the specialQuestionnaireSpecified
	 */
	public boolean isSpecialQuestionnaireSpecified() {
		return specialQuestionnaireSpecified;
	}

	/**
	 * @param specialQuestionnaireSpecified the specialQuestionnaireSpecified to set
	 */
	public void setSpecialQuestionnaireSpecified(
			boolean specialQuestionnaireSpecified) {
		this.specialQuestionnaireSpecified = specialQuestionnaireSpecified;
	}

	/**
	 * @return the mustCompleteSpecialQuestionnaire
	 */
	public boolean isMustCompleteSpecialQuestionnaire() {
		return mustCompleteSpecialQuestionnaire;
	}

	/**
	 * @param mustCompleteSpecialQuestionnaire the mustCompleteSpecialQuestionnaire to set
	 */
	public void setMustCompleteSpecialQuestionnaire(
			boolean mustCompleteSpecialQuestionnaire) {
		this.mustCompleteSpecialQuestionnaire = mustCompleteSpecialQuestionnaire;
	}

	/**
	 * @return the displaySpecialQuestionnaire
	 */
	public String getDisplaySpecialQuestionnaire() {
		return displaySpecialQuestionnaire;
	}

	/**
	 * @param displaySpecialQuestionnaire the displaySpecialQuestionnaire to set
	 */
	public void setDisplaySpecialQuestionnaire(String displaySpecialQuestionnaire) {
		this.displaySpecialQuestionnaire = displaySpecialQuestionnaire;
	}

	/**
	 * @return the requiredProctorValidation
	 */
	public boolean isRequireProctorValidation() {
		return requireProctorValidation;
	}

	/**
	 * @param requireProctorValidation the requiredProctorValidation to set
	 */
	public void setRequireProctorValidation(boolean requireProctorValidation) {
		this.requireProctorValidation = requireProctorValidation;
	}
	

	public boolean isPreAllowPauseResumeAssessment() {
		return preAllowPauseResumeAssessment;
	}

	public void setPreAllowPauseResumeAssessment(
			boolean preAllowPauseResumeAssessment) {
		this.preAllowPauseResumeAssessment = preAllowPauseResumeAssessment;
	}

	public boolean isPostAllowPauseResumeAssessment() {
		return postAllowPauseResumeAssessment;
	}

	public void setPostAllowPauseResumeAssessment(
			boolean postAllowPauseResumeAssessment) {
		this.postAllowPauseResumeAssessment = postAllowPauseResumeAssessment;
	}

	public boolean isQuizAllowPauseResumeAssessment() {
		return quizAllowPauseResumeAssessment;
	}

	public void setQuizAllowPauseResumeAssessment(
			boolean quizAllowPauseResumeAssessment) {
		this.quizAllowPauseResumeAssessment = quizAllowPauseResumeAssessment;
	}

	public String getProctorValidatorName() {
		return proctorValidatorName;
	}

	public void setProctorValidatorName(String proctorValidatorName) {
		this.proctorValidatorName = proctorValidatorName;
	}

	public boolean isRequireLearnerValidation() {
		return requireLearnerValidation;
	}

	public void setRequireLearnerValidation(boolean requireLearnerValidation) {
		this.requireLearnerValidation = requireLearnerValidation;
	}

	public boolean isCaRealEstateCE() {
		return caRealEstateCE;
	}

	public void setCaRealEstateCE(boolean caRealEstateCE) {
		this.caRealEstateCE = caRealEstateCE;
	}

	public boolean isProctorEnableAllowed(){
		return true;
		//return (!this.isPreAllowPauseResumeAssessment() && !this.isQuizAllowPauseResumeAssessment() && !this.isPostAllowPauseResumeAssessment());
	}

	public boolean isPreLockoutAssessmentActiveWindow() {
		return preLockoutAssessmentActiveWindow;
	}

	public void setPreLockoutAssessmentActiveWindow(
			boolean preLockoutAssessmentActiveWindow) {
		this.preLockoutAssessmentActiveWindow = preLockoutAssessmentActiveWindow;
	}

	public boolean isQuizLockoutAssessmentActiveWindow() {
		return quizLockoutAssessmentActiveWindow;
	}

	public void setQuizLockoutAssessmentActiveWindow(
			boolean quizLockoutAssessmentActiveWindow) {
		this.quizLockoutAssessmentActiveWindow = quizLockoutAssessmentActiveWindow;
	}

	public boolean isPostLockoutAssessmentActiveWindow() {
		return postLockoutAssessmentActiveWindow;
	}

	public void setPostLockoutAssessmentActiveWindow(
			boolean postLockoutAssessmentActiveWindow) {
		this.postLockoutAssessmentActiveWindow = postLockoutAssessmentActiveWindow;
	}

	public Boolean getCertificateEnabled() {
		return certificateEnabled;
	}
	
	public boolean isPostEnableviewAssessmentResults() {
		return postEnableviewAssessmentResults;
	}

	public void setPostEnableviewAssessmentResults(
			boolean postEnableviewAssessmentResults) {
		this.postEnableviewAssessmentResults = postEnableviewAssessmentResults;
	}

	public boolean isEnableDefineUniqueQuestionValidation() {
		return enableDefineUniqueQuestionValidation;
	}

	public void setEnableDefineUniqueQuestionValidation(
			boolean enableDefineUniqueQuestionValidation) {
		this.enableDefineUniqueQuestionValidation = enableDefineUniqueQuestionValidation;
	}

	public boolean isEnableSelfRegistrationProctor() {
		return enableSelfRegistrationProctor;
	}

	public void setEnableSelfRegistrationProctor(
			boolean enableSelfRegistrationProctor) {
		this.enableSelfRegistrationProctor = enableSelfRegistrationProctor;
	}

	public boolean isEnableSmartProfileValidation() {
		return enableSmartProfileValidation;
	}

	public void setEnableSmartProfileValidation(boolean enableSmartProfileValidation) {
		this.enableSmartProfileValidation = enableSmartProfileValidation;
	}
	
	public List<UniqueQuestionsVO> getLstUniqueQuestionsVO() {
		return lstUniqueQuestionsVO;
	}

	public void setLstUniqueQuestionsVO(List<UniqueQuestionsVO> lstUniqueQuestionsVO) {
		this.lstUniqueQuestionsVO = lstUniqueQuestionsVO;
	}

}