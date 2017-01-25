/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "ASSESSMENTCONFIGURATION")
public class AssessmentConfiguration implements SearchableKey {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public enum ActionToTakeAfterFailingMaxAttempt {
            RetakeContent("Retake Content"),
            GoToNextLesson("Go To Next Lesson"),
            ContinueCourse("Continue Course");
            
            String value = null;
            ActionToTakeAfterFailingMaxAttempt(String value){
                this.value = value;
            }
            
            public String getValue(){
                return value;
            }
        }
	
	public AssessmentConfiguration (){
		scoringType = SCORETYPE_PERCENTSCORE;
                gradingBehavior = GRADINGBEHAVIOR_AFTERASSESSMENTSUBMITTED;
	}

	public static final String PREASSESSMENT = "PreAssessment";
	public static final String QUIZ = "Quiz";
	public static final String POSTASSESSMENT = "PostAssessment";
	
	public static final String STATS_PREASSESSMENT = "PreAssessmentResultStatistic";
	public static final String STATS_QUIZ = "QuizAssessmentResultStatistic";
	public static final String STATS_POSTASSESSMENT = "PostAssessmentResultStatistic";

	
	public static final String GRADINGBEHAVIOR_AFTEREACHQUESTIONANSWERED = "AfterEachQuestionIsAnswered";
	public static final String GRADINGBEHAVIOR_AFTERASSESSMENTSUBMITTED = "AfterAssessmentIsSubmitted";
	public static final String SCORETYPE_PERCENTSCORE = "Percent Score";
	public static final String SCORETYPE_PASSFAIL = "Pass/Fail";
	public static final String SCORETYPE_NORESULT = "No Results";
	
	//@Id
    //@javax.persistence.TableGenerator(name = "ASSESSMENTCONFIGURATION_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "AssessmentConfiguration", allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.TABLE, generator = "ASSESSMENTCONFIGURATION_ID")
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "seqAssessmentConfigurationId")
	@GenericGenerator(name = "seqAssessmentConfigurationId", strategy = "com.softech.vu360.lms.model.PrimaryKeyGenerator", parameters = {
	        @Parameter(name = "table_name", value = "VU360_SEQ"),
	        @Parameter(name = "value_column_name", value = "NEXT_ID"),
	        @Parameter(name = "segment_column_name", value = "TABLE_NAME"),
	        @Parameter(name = "segment_value", value = "ASSESSMENTCONFIGURATION") })
	private Long id;
	
	@Column(name = "ASSESSMENTTYPE")
	private String assessmentType = null;
	
	@Column(name = "ENABLED")
	private Boolean assessmentEnabled = Boolean.FALSE;
	
	@Column(name = "ENABLEVIEWASSESSMENTRESULT")
	private Boolean viewassessmentresultsEnabled = Boolean.FALSE;
	
	@Column(name = "MAXIMUMNOATTEMPT")
	private Integer maximunNumberAttemptsAllowed = -1;
	
	@Column(name = "NOQUESTION")
	private Integer numberQuestionsToPresent = -1;
	
	@Column(name = "MASTERYSCORE")
	private Integer masteryScore = -1;
	
	@Column(name = "ACTIONTOTAKEAFTERFAILINGMAXATTEMPT")
	private String actionToTakeAfterMaximumAttemptsReached = null;
	
	@Transient
	private Boolean enforceMaximumTimeLimit = Boolean.FALSE;
	
	@Column(name = "ENFORCEMAXIMUMTIMELIMIT")
	private Integer maximumTimeLimitMinutes = -1;
	
	@Column(name = "ENFORCEUNIQUEQUESTIONSONRETAKE")
	private Boolean useUniqueQuestionsOnRetake = Boolean.FALSE;
	
	@Column(name = "MINIMUMTIMEBEFORESTART")
	private Integer minimumSeatTimeBeforeAssessmentStart = -1;
	
	@Column(name = "MINIMUMTIMEBEFORESTART_UNIT")
	private String minimumSeatTimeBeforeAssessmentStartUnits = "Minutes";
	
	@Column(name = "CONTENTREMEDIATION")
	private Boolean enableContentRemediation = Boolean.FALSE;
	
	@Column(name = "PROCTOREDASSESSMENT")
	private Boolean enableAssessmentProctoring = Boolean.FALSE;
	
	@Column(name = "QUESTIONLEVELRESULT")
	private Boolean showQuestionLevelResults = Boolean.FALSE;
	
	@Column(name = "RANDOMIZEQUESTION")
	private Boolean randomizeQuestions = Boolean.TRUE;
	
	@Column(name = "RANDOMIZEANSWERS")
	private Boolean randomizeAnswers = Boolean.TRUE;
	
	@Column(name = "SCORETYPE")
	private String scoringType = null;
	
	@Column(name = "SHOWQUESTIONANSWERSUMMARY")
	private Boolean showQuestionAnswerReview = Boolean.FALSE;
	
	@Column(name = "RESTRICTIVEASSESSMENTMODETF")
	private Boolean enableRestrictiveAssessmentMode = Boolean.FALSE;
	
	@Column(name = "GRADEQUESTIONS")
	private String gradingBehavior = null;
	
	@Column(name = "SCOREWEIGHTTF")
	private Boolean enableWeightedScoring = Boolean.FALSE;
	
	@Column(name = "ADVANCEQUESTIONSELECTIONTYPE")
	private String advancedQuestionSelectionType = null;
	
	@Column(name = "ALLOWSKIPPINGQUESTION")
	private Boolean allowQuestionSkipping = Boolean.TRUE;
	
	@OneToOne (cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name="COURSECONFIGURATION_ID")
	private CourseConfiguration courseConfiguration = null;
	
	@Column(name = "DISPLAYSEATTIMESATISFIEDMESSAGETF")
	private Boolean displaySeatTimeTextMessage = Boolean.FALSE;
	
	@Column(name = "ALLOWPOSTASSESSMENTAFTERSEATTIMESATISFIEDTF")
	private Boolean lockPostAssessmentBeforeSeatTime = Boolean.FALSE;
	
	@Column(name = "TURNOFFASSESSMENTSCORINGCUSTOMMESSAGE")
	private String noRsultMessage="";
	
	@Column(name = "ENABLEMAXIMUMATTEMPTHANDLERTF")
	private Boolean enableAssessmentMaximumNoAttempt  = Boolean.FALSE;
	
	@Column(name = "ALLOWPAUSERESUMEASSESSMENT")
	private Boolean allowPauseResumeAssessment  = Boolean.FALSE;
	
	@Column(name = "ENABLELOCKOUTCLICKAWAYFROMACTIVEWINDOWTF")
	private Boolean lockOutAssessmentActiveWindow = Boolean.FALSE;
	
	/** 
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	@Override
	public String getKey() {
		return id.toString();
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the assessmentType
	 */
	public String getAssessmentType() {
		return assessmentType;
	}
	/**
	 * @param assessmentType the assessmentType to set
	 */
	public void setAssessmentType(String assessmentType) {
		this.assessmentType = assessmentType;
	}
	/**
	 * @return the maximunNumberAttemptsAllowed
	 */
	public Integer getMaximunNumberAttemptsAllowed() {
		return maximunNumberAttemptsAllowed;
	}
	/**
	 * @param maximunNumberAttemptsAllowed the maximunNumberAttemptsAllowed to set
	 */
	public void setMaximunNumberAttemptsAllowed(Integer maximunNumberAttemptsAllowed) {
		this.maximunNumberAttemptsAllowed = maximunNumberAttemptsAllowed;
	}
	/**
	 * @return the numberQuestionsToPresent
	 */
	public Integer getNumberQuestionsToPresent() {
		return numberQuestionsToPresent;
	}
	/**
	 * @param numberQuestionsToPresent the numberQuestionsToPresent to set
	 */
	public void setNumberQuestionsToPresent(Integer numberQuestionsToPresent) {
		this.numberQuestionsToPresent = numberQuestionsToPresent;
	}
	/**
	 * @return the masteryScore
	 */
	public Integer getMasteryScore() {
		return masteryScore;
	}
	/**
	 * @param masteryScore the masteryScore to set
	 */
	public void setMasteryScore(Integer masteryScore) {
		this.masteryScore = masteryScore;
	}
	/**
	 * @return the actionToTakeAfterMaximumAttemptsReached
	 */
	public String getActionToTakeAfterMaximumAttemptsReached() {
		return actionToTakeAfterMaximumAttemptsReached;
	}
	/**
	 * @param actionToTakeAfterMaximumAttemptsReached the actionToTakeAfterMaximumAttemptsReached to set
	 */
	public void setActionToTakeAfterMaximumAttemptsReached(
			String actionToTakeAfterMaximumAttemptsReached) {
		this.actionToTakeAfterMaximumAttemptsReached = actionToTakeAfterMaximumAttemptsReached;
	}
	/**
	 * @return the enforceMaximumTimeLimit
	 */
	public  Boolean getEnforceMaximumTimeLimit() {
		if(enforceMaximumTimeLimit==null){
			enforceMaximumTimeLimit=Boolean.FALSE;
		}
		return enforceMaximumTimeLimit;
	}
	/**
	 * @param enforceMaximumTimeLimit the enforceMaximumTimeLimit to set
	 */
	public void setEnforceMaximumTimeLimit(Boolean enforceMaximumTimeLimit) {
		if(enforceMaximumTimeLimit == null){
			this.enforceMaximumTimeLimit=Boolean.FALSE;
		}
		else{
			this.enforceMaximumTimeLimit = enforceMaximumTimeLimit;
		}
		
	}
	/**
	 * @return the useUniqueQuestionsOnRetake
	 */
	public  Boolean getUseUniqueQuestionsOnRetake() {
		if(useUniqueQuestionsOnRetake==null){
			useUniqueQuestionsOnRetake=Boolean.FALSE;
		}
		return useUniqueQuestionsOnRetake;
	}
	/**
	 * @param useUniqueQuestionsOnRetake the useUniqueQuestionsOnRetake to set
	 */
	public void setUseUniqueQuestionsOnRetake(Boolean useUniqueQuestionsOnRetake) {
		if(useUniqueQuestionsOnRetake == null){
			this.useUniqueQuestionsOnRetake=Boolean.FALSE;
		}
		else{
			this.useUniqueQuestionsOnRetake = useUniqueQuestionsOnRetake;
		}
		
	}
	/**
	 * @return the minimumSeatTimeBeforeAssessmentStart
	 */
	public Integer getMinimumSeatTimeBeforeAssessmentStart() {
		return minimumSeatTimeBeforeAssessmentStart;
	}
	/**
	 * @param minimumSeatTimeBeforeAssessmentStart the minimumSeatTimeBeforeAssessmentStart to set
	 */
	public void setMinimumSeatTimeBeforeAssessmentStart(
			Integer minimumSeatTimeBeforeAssessmentStart) {
		this.minimumSeatTimeBeforeAssessmentStart = minimumSeatTimeBeforeAssessmentStart;
	}
	/**
	 * @return the maximumTimeLimitMinutes
	 */
	public Integer isMaximumTimeLimitMinutes() {
		return maximumTimeLimitMinutes;
	}
	/**
	 * @param maximumTimeLimitMinutes the maximumTimeLimitMinutes to set
	 */
	public void setMaximumTimeLimitMinutes(Integer maximumTimeLimitMinutes) {
		this.maximumTimeLimitMinutes = maximumTimeLimitMinutes;
	}
	/**
	 * @return the minimumSeatTimeBeforeAssessmentStartUnits
	 */
	public String getMinimumSeatTimeBeforeAssessmentStartUnits() {
		return minimumSeatTimeBeforeAssessmentStartUnits;
	}
	/**
	 * @param minimumSeatTimeBeforeAssessmentStartUnits the minimumSeatTimeBeforeAssessmentStartUnits to set
	 */
	public void setMinimumSeatTimeBeforeAssessmentStartUnits(
			String minimumSeatTimeBeforeAssessmentStartUnits) {
		this.minimumSeatTimeBeforeAssessmentStartUnits = minimumSeatTimeBeforeAssessmentStartUnits;
	}
	/**
	 * @return the enableContentRemediation
	 */
	public  Boolean getEnableContentRemediation() {
		if(enableContentRemediation==null){
			enableContentRemediation=Boolean.FALSE;
		}
		return enableContentRemediation;
	}
	/**
	 * @param enableContentRemediation the enableContentRemediation to set
	 */
	public void setEnableContentRemediation(Boolean enableContentRemediation) {
		if(enableContentRemediation == null){
			this.enableContentRemediation=Boolean.FALSE;
		}
		else{
			this.enableContentRemediation = enableContentRemediation;
		}
		
	}
	/**
	 * @return the enableAssessmentProctoring
	 */
	public  Boolean getEnableAssessmentProctoring() {
		if(enableAssessmentProctoring==null){
			enableAssessmentProctoring=Boolean.FALSE;
		}
		return enableAssessmentProctoring;
	}
	/**
	 * @param enableAssessmentProctoring the enableAssessmentProctoring to set
	 */
	public void setEnableAssessmentProctoring(Boolean enableAssessmentProctoring) {
		if(enableAssessmentProctoring == null){
			this.enableAssessmentProctoring=Boolean.FALSE;
		}
		else{
			this.enableAssessmentProctoring = enableAssessmentProctoring;
		}
		
	}
	/**
	 * @return the showQuestionLevelResults
	 */
	public  Boolean getShowQuestionLevelResults() {
		if(showQuestionLevelResults==null){
			showQuestionLevelResults=Boolean.FALSE;
		}
		return showQuestionLevelResults;
	}
	/**
	 * @param showQuestionLevelResults the showQuestionLevelResults to set
	 */
	public void setShowQuestionLevelResults(Boolean showQuestionLevelResults) {
		if(showQuestionLevelResults ==null){
			this.showQuestionLevelResults=Boolean.FALSE;
		}
		else{
			this.showQuestionLevelResults = showQuestionLevelResults;
		}
		
	}
	/**
	 * @return the randomizeQuestions
	 */
	public  Boolean getRandomizeQuestions() {
		if(randomizeQuestions==null){
			randomizeQuestions=Boolean.TRUE;
		}
		return randomizeQuestions;
	}
	/**
	 * @param randomizeQuestions the randomizeQuestions to set
	 */
	public void setRandomizeQuestions(Boolean randomizeQuestions) {
		if(randomizeQuestions==null){
			this.randomizeQuestions=Boolean.TRUE;
		}
		else{
			this.randomizeQuestions = randomizeQuestions;
		}
		
	}
	/**
	 * @return the randomizeAnswers
	 */
	public  Boolean getRandomizeAnswers() {
		if(randomizeAnswers==null){
			randomizeAnswers=Boolean.TRUE;
		}
		return randomizeAnswers;
	}
	/**
	 * @param randomizeAnswers the randomizeAnswers to set
	 */
	public void setRandomizeAnswers(Boolean randomizeAnswers) {
		if(randomizeAnswers==null){
			this.randomizeAnswers=Boolean.TRUE;
		}
		else{
			this.randomizeAnswers = randomizeAnswers;
		}
		
	}
	/**
	 * @return the scoringType
	 */
	public String getScoringType() {
		return scoringType;
	}
	/**
	 * @param scoringType the scoringType to set
	 */
	public void setScoringType(String scoringType) {
		this.scoringType = scoringType;
	}
	/**
	 * @return the showQuestionAnswerReview
	 */
	public  Boolean getShowQuestionAnswerReview() {
		if(showQuestionAnswerReview==null){
			showQuestionAnswerReview=Boolean.FALSE;
		}
		return showQuestionAnswerReview;
	}
	/**
	 * @param showQuestionAnswerReview the showQuestionAnswerReview to set
	 */
	public void setShowQuestionAnswerReview(Boolean showQuestionAnswerReview) {
		if(showQuestionAnswerReview==null){
			this.showQuestionAnswerReview=Boolean.FALSE;
		}
		else{
			this.showQuestionAnswerReview = showQuestionAnswerReview;
		}
		
	}
	/**
	 * @return the enableRestrictiveAssessmentMode
	 */
	public  Boolean getEnableRestrictiveAssessmentMode() {
		if(enableRestrictiveAssessmentMode==null){
			enableRestrictiveAssessmentMode=Boolean.FALSE;
		}
		return enableRestrictiveAssessmentMode;
	}
	/**
	 * @param enableRestrictiveAssessmentMode the enableRestrictiveAssessmentMode to set
	 */
	public void setEnableRestrictiveAssessmentMode(
			 Boolean enableRestrictiveAssessmentMode) {
		if(enableRestrictiveAssessmentMode){
			this.enableRestrictiveAssessmentMode=Boolean.FALSE;
		}
		else{
			this.enableRestrictiveAssessmentMode = enableRestrictiveAssessmentMode;
		}
		
	}
	/**
	 * @return the gradingBehavior
	 */
	public String getGradingBehavior() {
		return gradingBehavior;
	}
	/**
	 * @param gradingBehavior the gradingBehavior to set
	 */
	public void setGradingBehavior(String gradingBehavior) {
		this.gradingBehavior = gradingBehavior;
	}
	/**
	 * @return the enableWeightedScoring
	 */
	public  Boolean getEnableWeightedScoring() {
		if(enableWeightedScoring==null){
			enableWeightedScoring=Boolean.FALSE;
		}
		return enableWeightedScoring;
	}
	/**
	 * @param enableWeightedScoring the enableWeightedScoring to set
	 */
	public void setEnableWeightedScoring(Boolean enableWeightedScoring) {
		if(enableWeightedScoring==null){
			this.enableWeightedScoring=Boolean.FALSE;
		}
		else{
			this.enableWeightedScoring = enableWeightedScoring;		
		}
		
	}
	/**
	 * @return the advancedQuestionSelectionType
	 */
	public String getAdvancedQuestionSelectionType() {
		return advancedQuestionSelectionType;
	}
	/**
	 * @param advancedQuestionSelectionType the advancedQuestionSelectionType to set
	 */
	public void setAdvancedQuestionSelectionType(
			String advancedQuestionSelectionType) {
		this.advancedQuestionSelectionType = advancedQuestionSelectionType;
	}
	/**
	 * @return the allowQuestionSkipping
	 */
	public  Boolean getAllowQuestionSkipping() {
		if(allowQuestionSkipping==null){
			allowQuestionSkipping=Boolean.TRUE;
		}
		return allowQuestionSkipping;
	}
	/**
	 * @param allowQuestionSkipping the allowQuestionSkipping to set
	 */
	public void setAllowQuestionSkipping(Boolean allowQuestionSkipping) {
		if(allowQuestionSkipping==null){
			this.allowQuestionSkipping=Boolean.FALSE;
		}
		else{
			this.allowQuestionSkipping = allowQuestionSkipping;
		}
	}
	/**
	 * @return the assessmentEnabled
	 */
	public  Boolean getAssessmentEnabled() {
		if(assessmentEnabled==null){
			assessmentEnabled=Boolean.FALSE;
		}
		return assessmentEnabled;
	}
	/**
	 * @param assessmentEnabled the assessmentEnabled to set
	 */
	public void setAssessmentEnabled(Boolean assessmentEnabled) {
		if(assessmentEnabled==null){
			this.assessmentEnabled=Boolean.FALSE;
		}
		else{
			this.assessmentEnabled = assessmentEnabled;
		}
	}
	/**
	 * @return the maximumTimeLimitMinutes
	 */
	public Integer getMaximumTimeLimitMinutes() {
		return maximumTimeLimitMinutes;
	}
	public CourseConfiguration getCourseConfiguration() {
		return courseConfiguration;
	}
	public void setCourseConfiguration(CourseConfiguration courseConfiguration) {
		this.courseConfiguration = courseConfiguration;
	}
	public  Boolean getDisplaySeatTimeTextMessage() {
		if(displaySeatTimeTextMessage==null){
			displaySeatTimeTextMessage=Boolean.FALSE;
		}
		return displaySeatTimeTextMessage;
	}
	public void setDisplaySeatTimeTextMessage(Boolean displaySeatTimeTextMessage) {
		if(displaySeatTimeTextMessage==null){
			this.displaySeatTimeTextMessage=Boolean.FALSE;
		}
		else{
			this.displaySeatTimeTextMessage = displaySeatTimeTextMessage;
		}
	}
	public  Boolean getLockPostAssessmentBeforeSeatTime() {
		if(lockPostAssessmentBeforeSeatTime==null){
			lockPostAssessmentBeforeSeatTime=Boolean.FALSE;
		}
		return lockPostAssessmentBeforeSeatTime;
	}
	public void setLockPostAssessmentBeforeSeatTime(
			 Boolean lockPostAssessmentBeforeSeatTime) {
		if(lockPostAssessmentBeforeSeatTime==null){
			this.lockPostAssessmentBeforeSeatTime=Boolean.FALSE;
		}
		else{
			this.lockPostAssessmentBeforeSeatTime = lockPostAssessmentBeforeSeatTime;
		}
	}
	public String getNoRsultMessage() {
		return noRsultMessage;
	}
	public void setNoRsultMessage(String noRsultMessage) {
		this.noRsultMessage = noRsultMessage;
	}
	
	public  Boolean getEnableAssessmentMaximumNoAttempt() {
		if(enableAssessmentMaximumNoAttempt==null){
			enableAssessmentMaximumNoAttempt=Boolean.FALSE;
		}
		return enableAssessmentMaximumNoAttempt;
	}
	public void setEnableAssessmentMaximumNoAttempt(
			 Boolean enableAssessmentMaximumNoAttempt) {
		if(enableAssessmentMaximumNoAttempt==null){
			this.enableAssessmentMaximumNoAttempt=Boolean.FALSE;
		}
		else{
			this.enableAssessmentMaximumNoAttempt = enableAssessmentMaximumNoAttempt;
		}
	}
	public  Boolean getAllowPauseResumeAssessment() {
		if(allowPauseResumeAssessment==null){
			allowPauseResumeAssessment=Boolean.FALSE;
		}
		return allowPauseResumeAssessment;
	}
	public void setAllowPauseResumeAssessment(Boolean allowPauseResumeAssessment) {
		if(allowPauseResumeAssessment==null){
			this.allowPauseResumeAssessment=Boolean.FALSE;
		}
		else{
			this.allowPauseResumeAssessment = allowPauseResumeAssessment;
		}
	}
	public  Boolean getLockOutAssessmentActiveWindow() {
		if(lockOutAssessmentActiveWindow==null){
			lockOutAssessmentActiveWindow=Boolean.FALSE;
		}
		return lockOutAssessmentActiveWindow;
	}
	public void setLockOutAssessmentActiveWindow(
			 Boolean lockOutAssessmentActiveWindow) {
		if(lockOutAssessmentActiveWindow==null){
			this.lockOutAssessmentActiveWindow=Boolean.FALSE;
		}
		else{
			this.lockOutAssessmentActiveWindow = lockOutAssessmentActiveWindow;
		}
	}
	public Boolean getViewassessmentresultsEnabled() {
		if(viewassessmentresultsEnabled == null){
			viewassessmentresultsEnabled = Boolean.FALSE;
		}
		return viewassessmentresultsEnabled;
	}
	public void setViewassessmentresultsEnabled(Boolean viewassessmentresultsEnabled) {
		this.viewassessmentresultsEnabled = viewassessmentresultsEnabled;
	}
	
	
}
