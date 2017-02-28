 /**
 * 
 */
package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Parameter;

import com.softech.vu360.lms.vo.UniqueQuestionsVO;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "COURSECONFIGURATION")
public class CourseConfiguration implements SearchableKey {
	
	private static final long serialVersionUID = 1L;
	public static final String UNITS_MONTH="Months";
	public static final String UNITS_MINUTE="Minutes";
	public static final String UNITS_DAYS="Days";
	
	public CourseConfiguration(){
		
		this.assessmentConfigurations = new ArrayList<AssessmentConfiguration>();
		AssessmentConfiguration preAssessmentConfig = new AssessmentConfiguration();
		preAssessmentConfig.setAssessmentType(AssessmentConfiguration.PREASSESSMENT);
		preAssessmentConfig.setCourseConfiguration(this);
                preAssessmentConfig.setActionToTakeAfterMaximumAttemptsReached(
                    AssessmentConfiguration.ActionToTakeAfterFailingMaxAttempt.GoToNextLesson.getValue());
                
		AssessmentConfiguration postAssessmentConfig = new AssessmentConfiguration();
		postAssessmentConfig.setAssessmentType(AssessmentConfiguration.POSTASSESSMENT);
		postAssessmentConfig.setCourseConfiguration(this);
                postAssessmentConfig.setActionToTakeAfterMaximumAttemptsReached(
                    AssessmentConfiguration.ActionToTakeAfterFailingMaxAttempt.RetakeContent.getValue());
                
		AssessmentConfiguration quizAssessmentConfig = new AssessmentConfiguration();
		quizAssessmentConfig.setAssessmentType(AssessmentConfiguration.QUIZ);
		quizAssessmentConfig.setCourseConfiguration(this);
                quizAssessmentConfig.setActionToTakeAfterMaximumAttemptsReached(
                    AssessmentConfiguration.ActionToTakeAfterFailingMaxAttempt.GoToNextLesson.getValue());
		
		this.assessmentConfigurations.add(preAssessmentConfig);
		this.assessmentConfigurations.add(quizAssessmentConfig);
		this.assessmentConfigurations.add(postAssessmentConfig);
		
	}
	
	
	/*
	@Id
    @javax.persistence.TableGenerator(name = "COURSECONFIGURATION_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "courseconfiguration", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COURSECONFIGURATION_ID")
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "seqCourseConfigurationId")
	@GenericGenerator(name = "seqCourseConfigurationId", strategy = "com.softech.vu360.lms.model.PrimaryKeyGenerator", parameters = {
	        @Parameter(name = "table_name", value = "VU360_SEQ"),
	        @Parameter(name = "value_column_name", value = "NEXT_ID"),
	        @Parameter(name = "segment_column_name", value = "TABLE_NAME"),
	        @Parameter(name = "segment_value", value = "COURSECONFIGURATION") })
	private Long id;
	
	@OneToOne (cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name="COURSECONFIGURATIONTEMPLATE_ID")
	private CourseConfigurationTemplate courseConfigTemplate = null;
	//private Course course = null;
	
	// course Policy
	
	@Column(name = "PLAYER_ENABLEINTROPAGE")
	private Boolean showStandardIntroduction = Boolean.FALSE;
	
	@Column(name = "PLAYER_ORIENTATIONKEY")
	private Boolean showOrientationScenes = Boolean.FALSE;
	
	@Column(name = "PLAYER_ENABLEENDOFCOURSESCENE")
	private Boolean showEndOfCourseScene = Boolean.FALSE;
	
	@Column(name = "PLAYER_ENABLECONTENT")
	private Boolean showContent = Boolean.TRUE;
	
	@Column(name = "PLAYER_ENFORCETIMEDOUTLINE")
	private Boolean enforceTimedOutlineAllScenes = Boolean.TRUE;
	
	@Column(name = "PLAYER_IDLEUSERTIMEOUT")
	private Integer idleTimeAmount = 0;
	
	@Column(name = "PLAYER_ALLOWUSERTOREVIEWCOURSEAFTERCOMPLETION")
	private Boolean allowUserToReviewCourseAfterCompletion = Boolean.FALSE;
	
	@Column(name = "PLAYER_MUSTCOMPLETECOURSEEVALUATION")
	private Boolean mustCompleteCourseEvaluation = Boolean.FALSE;
	
	@Column(name = "PLAYER_SHOW_COURSE_SUGGESTED_TF")
	private Boolean suggestedCourse = Boolean.FALSE;
	
	@Column(name = "ALLOW_RATING_TF")
	private Boolean rateCourse = Boolean.FALSE;
	
	@Column(name = "ALLOW_FACEBOOK_TF")
	private Boolean allowFaceook = Boolean.FALSE;
	

	// Course Completion
	
	@OneToOne
    @JoinColumn(name="COMPLETIONCERTIFICATEASSET_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private Certificate completionCertificate = null;
	
	@Column(name = "COMPLETION_POSTASSESSMENTATTEMPTED")
	private Boolean mustAttemptPostAssessment = Boolean.FALSE;
	
	@Column(name = "COMPLETION_POSTASSESSMENTMASTERY")
	private Boolean mustDemonstratePostAssessmentMastery = Boolean.FALSE;
	
	@Column(name = "COMPLETION_PREASSESSMENTMASTERY")
	private Boolean mustDemonstratePreAssessmentMastery = Boolean.FALSE;
	
	@Column(name = "COMPLETION_QUIZMASTERY")
	private Boolean mustDemonstrateQuizMastery = Boolean.FALSE;
	
	@Column(name = "COMPLETION_SURVEY")
	private Boolean mustCompleteAnySurveys = Boolean.FALSE;
	
	@Column(name = "COMPLETION_VIEWEVERYSCENEINCOURSE")
	private Boolean mustViewEverySceneInTheCourse = Boolean.FALSE;
	
	@Column(name = "COMPLETION_COMPLETEAFTERUNIQUENUMBERVISITS")
	private Boolean canOnlyBeCompleteAfterNumberOfCourseLaunches = Boolean.FALSE;
	
	@Column(name = "COMPLETION_COMPLETEAFTERNOUNIQUECOURSEVISIT")
	private Integer numberOfCoursesLaunch = 0;
	
	@Column(name = "COMPLETION_COMPLETEWITHINTIMEPERIOD")
	private Boolean completeWithinTimePeriod = Boolean.FALSE;
	
	@Column(name = "COMPLETION_COMPLETEWITHINDAYSOFREGISTRATION")
	private Boolean completeWithinDaysOfRegistration = Boolean.FALSE;
	
	@Column(name = "COMPLETION_UNITOFMUSTCOMPLETEWITHINSPECIFIEDAMOUNTOFTIME")
	private String unitOfTimeToComplete=UNITS_MINUTE;
	
	@Column(name = "COMPLETION_MUSTCOMPLETEWITHINSPECIFIEDAMOUNTOFTIMEMINUTE")
	private Integer minutesAfterFirstCourseLaunch = 0;
	
	@Column(name = "COMPLETION_MUSTCOMPLETEWITHINSPECIFIEDAMOUNTOFTIMEDAY")
	private Integer daysOfRegistraion = 0;
	
	@Transient
	private String unitOfDaysOfRegistration  = UNITS_DAYS;
	
	@Column(name = "PLAYER_COURSEEVALUATION")
	private Boolean courseEvaluationSpecified = Boolean.FALSE;
	
	@Column(name = "COURSE_STRICLYENFORCEPOLICYTOBEUSED")
	private Boolean courseStrictlyEnforcePolicyToBeUsed;
	
	@Column(name = "COURSECONFIGURATION_GUID")
	private String guid;
	
	@Column(name = "PLAYER_COURSEFLOW")
	private String playerCourseFlow; //enum
	
	@Column(name = "PLAYER_STRICLYENFORCEPOLICYTOBEUSED")
	private Boolean playerStrictlyEnforcePolicyToBeUsed;
	
	@Column(name = "PLAYER_DISPLAYCOURSEEVALUATION")
	private String displayCourseEvaluation;
	
	@Column(name = "COMPLETIONCERTIFICATEENABLEDTF")
	private Boolean certificateEnabled = Boolean.FALSE;
	
	@Transient
	private String certificateName;
	
	@Column(name = "COMPLETION_RESPONDTOCOURSEEVALUATION")
	private Boolean respondToCourseEvaluation = Boolean.FALSE;
	
	

	// TODO:  Refactor the following list into three instances of AssessmentConfiguration
	//        this is temporary in order to meet the timeline required for LCMS changes.
	/*private AssessmentConfiguration preAssessmentConfiguration = null;
	private AssessmentConfiguration postAssessmentConfiguration = null;
	private AssessmentConfiguration quizAssessmentConfiguration = null;
	*/
	@OneToMany (mappedBy = "courseConfiguration" , cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<AssessmentConfiguration> assessmentConfigurations = null;

	// enable acknowledgement
	@Nationalized
	@Column(name = "EMBEDDED_ACKNOWLEDGMENT_TEXT")
	private String acknowledgeText = null;
	
	@Column(name = "EMBEDDED_ACKNOWLEDGMENT_ENABLEDTF")
	private Boolean acknowledgeEnabled = Boolean.FALSE;

	// Validation
	
	@Column(name = "VALIDATION_REQUIREIDENTITYVALIDATION")
	private Boolean enableIdentityValidation = Boolean.FALSE;
	
	@Transient
	private Integer requireIdentityValidationEverySeconds = 0;
	
	@Column(name = "VALIDATION_TIMETOANSWERQUESTION")
	private Integer allowSecondsToAnswerEachQuestion = 0;
	//private Integer allowNumberofAttemptsToAnswerCorrectly = 0;// can't map
	
	@Column(name = "VALIDATION_TIMEBETWEENQUESTION")
	private Integer validationTimeBetweenQuestion = 600;
	
	@Column(name = "VALIDATION_NOMISSEDQUESTIONSALLOWED")
	private Integer validationNoMissedQuestionsAllowed = 0;
	
	@Column(name = "VALIDATION_STRICLYENFORCEPOLICYTOBEUSED")
	private Boolean validationStrictlyEnforcePolicyToBeUsed;
	
	
	@Column(name = "VALIDATION_NOVALIDATIONQUESTION")
	private Integer numberOfValidationQuestions=1;
	
	
	// End Of Course
	@Nationalized
	@Column(name = "ENDOFCOURSEINSTRUCTIONS")
	private String endOfCourseInstructions=null;
	

	// Seat Time
	
	@Column(name = "SEATTIMEENABLED")
	private Boolean seattimeenabled;
	
	@Column(name = "MAXSEATTIMEPERDAYHOUR")
	private Integer seattimeinhour;
	
	@Column(name = "MAXSEATTIMEPERDAYMINUTE")
	private Integer seattimeinmin;
	
	@Column(name = "MESSAGESEATTIMEEXCEEDS")
	private String messageseattimeexceeds;
	
	@Column(name = "MESSAGESEATTIMECOURSELAUNCH")
	private String messageseattimecourselaunch;
	
	// Idle TimeOut
	
	@Column(name = "ACTIONTOTAKEUPONIDLETIMEOUT")
	private String actiontotakeuponidletimeout;
	
	//LMS-12023 CHANGE
	
	@Column(name = "CreateUserId")
	private Long createdBy;
	
	@Column(name = "CreatedDate")
	private Date createdDate;
	
	@Column(name = "LastUpdateUser")
	private Long lastUpdatedBy;
	
	@Column(name = "LastUpdatedDate")
	private Date lastUpdatedDate;
	
	//LMS-12023 CHANGE

	@Column(name = "MUST_START_COURSE_WITHIN_SPECIFIED_AMOUNT_OF_TIME_AFTER_REGISTRATION_DATE_TF")
	private Boolean mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified;
	
	@Column(name = "MUST_START_COURSE_WITH_IN_SPECIFIED_AMOUNT_OF_TIME_AFTER_REGISTRATION_DATE")
	private Integer mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration;
	
	@Column(name = "UNIT_MUST_START_COURSE_WITHIN_SPECIFIED_AMOUNT_OF_TIME_AFTER_REGISTRATION_DATE")
	private String mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationUnit;
	
	@Column(name = "PLAYER_SPECIALQUESTIONNAIRE")
	private Boolean specialQuestionnaireSpecified;
	
	@Column(name = "PLAYER_MUSTCOMPLETESPECIALQUESTIONNAIRE")
	private Boolean mustCompleteSpecialQuestionnaire;
	
	@Column(name = "PLAYER_DISPLAYSPECIALQUESTIONNAIRE")
	private String displaySpecialQuestionnaire;
	
	// Proctor Policies and Learner Policies
	
	@Column(name = "REQUIRE_PROCTOR_VALIDATION_TF")
	private Boolean requireProctorValidation;
	
	@Column(name = "ANSI_VALIDATION_TF")
	private Boolean requiredAnsi;
	
	@Column(name = "NY_VALIDATION_TF")
	private Boolean requiredNyInsurance;
	
	@Column(name = "REQUIRE_LEARNER_VALIDATION_TF")
	private Boolean requireLearnerValidation;
	
	@Column(name = "CA_VALIDATION_TF")
	private Boolean caRealEstateCE;
	
	@Column(name="RESTRICT_INCOMPLETE_JS_TEMPLATE")
	private Boolean mustMasterAllLessonActivities;
	
	@Column(name="DEFINEUNIQUEQUESTION_VALIDATION_TF")
	private Boolean requireDefineUniqueQuestionValidation;
	
	@Column(name="SELF_REGISTRATION_PROCTOR_TF")
	private Boolean requireSelfRegistrationProctor;
	
	@Column(name="PROFILEBASED_VALIDATION_TF")
	private Boolean requireSmartProfileValidation;
	
	//Learners Unique Questions
	@Transient
	private List<UniqueQuestionsVO> lstUniqueQuestionsVO = new ArrayList<>();
	
	public AssessmentConfiguration getPreAssessmentConfiguration() {
		for (AssessmentConfiguration asmntConfig : assessmentConfigurations) {
			if ( asmntConfig.getAssessmentType().equalsIgnoreCase(AssessmentConfiguration.PREASSESSMENT) ) {
				return asmntConfig;
			}
		}
		return null;
	}
	
	public AssessmentConfiguration getQuizAssessmentConfiguration() {
		for (AssessmentConfiguration asmntConfig : assessmentConfigurations) {
			if ( asmntConfig.getAssessmentType().equalsIgnoreCase(AssessmentConfiguration.QUIZ) ) {
				return asmntConfig;
			}
		}
		return null;		
	}
	
	public AssessmentConfiguration getPostAssessmentConfiguration() {
		for (AssessmentConfiguration asmntConfig : assessmentConfigurations) {
			if ( asmntConfig.getAssessmentType().equalsIgnoreCase(AssessmentConfiguration.POSTASSESSMENT) ) {
				return asmntConfig;
			}
		}
		return null;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return id.toString();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the courseConfigTemplate
	 */
	public CourseConfigurationTemplate getCourseConfigTemplate() {
		return courseConfigTemplate;
	}

	/**
	 * @param courseConfigTemplate
	 *            the courseConfigTemplate to set
	 */
	public void setCourseConfigTemplate(
			CourseConfigurationTemplate courseConfigTemplate) {
		this.courseConfigTemplate = courseConfigTemplate;
	}

		/**
	 * @param showStandardIntroduction
	 *            the showStandardIntroduction to set
	 */
	public void setShowStandardIntroduction(Boolean showStandardIntroduction) {
		if(showStandardIntroduction==null){
			this.showStandardIntroduction=Boolean.FALSE;
		}else{
			this.showStandardIntroduction = showStandardIntroduction;
		}
	}

	
	/**
	 * @param showOrientationScenes
	 *            the showOrientationScenes to set
	 */
	public void setShowOrientationScenes(Boolean showOrientationScenes) {
		if(showOrientationScenes==null){
			this.showOrientationScenes=Boolean.FALSE;
		}else{
			this.showOrientationScenes = showOrientationScenes;
		}
	}

	

	/**
	 * @param showEndOfCourseScene
	 *            the showEndOfCourseScene to set
	 */
	public void setShowEndOfCourseScene(Boolean showEndOfCourseScene) {
		if(showEndOfCourseScene==null){
			this.showEndOfCourseScene=Boolean.FALSE;
		}else{
			this.showEndOfCourseScene = showEndOfCourseScene;
		}
	}

	

	/**
	 * @param showContent
	 *            the showContent to set
	 */
	public void setShowContent(Boolean showContent) {
		if(showContent==null){
			this.showContent=Boolean.TRUE;
		}else{
			this.showContent = showContent;
		}
	}

	

	/**
	 * @param enforceTimedOutlineAllScenes
	 *            the enforceTimedOutlineAllScenes to set
	 */
	public void setEnforceTimedOutlineAllScenes(
			Boolean enforceTimedOutlineAllScenes) {
		
		if(enforceTimedOutlineAllScenes==null){
			this.enforceTimedOutlineAllScenes=Boolean.TRUE;
		}else{
			this.enforceTimedOutlineAllScenes = enforceTimedOutlineAllScenes;
		}
	}

	/**
	 * @return the idleTimeAmount
	 */
	public Integer getIdleTimeAmount() {
		return idleTimeAmount;
	}

	/**
	 * @param idleTimeAmount
	 *            the idleTimeAmount to set
	 */
	public void setIdleTimeAmount(Integer idleTimeAmount) {
		this.idleTimeAmount = idleTimeAmount;
	}

	

	

	/**
	 * @param allowUserToReviewCourseAfterCompletion
	 *            the allowUserToReviewCourseAfterCompletion to set
	 */
	public void setAllowUserToReviewCourseAfterCompletion(
			Boolean allowUserToReviewCourseAfterCompletion) {
		
		if(allowUserToReviewCourseAfterCompletion==null){
			this.allowUserToReviewCourseAfterCompletion=Boolean.FALSE;
		}else{
			this.allowUserToReviewCourseAfterCompletion = allowUserToReviewCourseAfterCompletion;
		}
	}

	

	/**
	 * @param mustAttemptPostAssessment
	 *            the mustAttemptPostAssessment to set
	 */
	public void setMustAttemptPostAssessment(Boolean mustAttemptPostAssessment) {
		if(mustAttemptPostAssessment==null){
			this.mustAttemptPostAssessment=Boolean.FALSE;
		}else{
			this.mustAttemptPostAssessment = mustAttemptPostAssessment;
		}
	}

	

	/**
	 * @param mustDemonstratePostAssessmentMastery
	 *            the mustDemonstratePostAssessmentMastery to set
	 */
	public void setMustDemonstratePostAssessmentMastery(
			Boolean mustDemonstratePostAssessmentMastery) {
		if(mustDemonstratePostAssessmentMastery==null){
			this.mustDemonstratePostAssessmentMastery=Boolean.FALSE;
		}else{
			this.mustDemonstratePostAssessmentMastery = mustDemonstratePostAssessmentMastery;
		}
	}

	
	/**
	 * @param mustDemonstratePreAssessmentMastery
	 *            the mustDemonstratePreAssessmentMastery to set
	 */
	public void setMustDemonstratePreAssessmentMastery(
			Boolean mustDemonstratePreAssessmentMastery) {
		if(mustDemonstratePreAssessmentMastery==null){
			this.mustDemonstratePreAssessmentMastery=Boolean.FALSE;
		}else{
			this.mustDemonstratePreAssessmentMastery = mustDemonstratePreAssessmentMastery;
		}
	}

	

	/**
	 * @param mustDemonstrateQuizMastery
	 *            the mustDemonstrateQuizMastery to set
	 */
	public void setMustDemonstrateQuizMastery(Boolean mustDemonstrateQuizMastery) {
		if(mustDemonstrateQuizMastery==null){
			this.mustDemonstrateQuizMastery=Boolean.FALSE;
		}else{
			this.mustDemonstrateQuizMastery = mustDemonstrateQuizMastery;
		}
	}

	

	/**
	 * @param mustCompleteAnySurveys
	 *            the mustCompleteAnySurveys to set
	 */
	public void setMustCompleteAnySurveys(Boolean mustCompleteAnySurveys) {
		if(mustCompleteAnySurveys==null){
			this.mustCompleteAnySurveys=Boolean.FALSE;
		}else{
			this.mustCompleteAnySurveys = mustCompleteAnySurveys;
		}
	}

	

	/**
	 * @param mustViewEverySceneInTheCourse
	 *            the mustViewEverySceneInTheCourse to set
	 */
	public void setMustViewEverySceneInTheCourse(
			Boolean mustViewEverySceneInTheCourse) {
		if(mustViewEverySceneInTheCourse==null){
			this.mustViewEverySceneInTheCourse=Boolean.FALSE;
		}else{
			this.mustViewEverySceneInTheCourse = mustViewEverySceneInTheCourse;
		}
	}

	

	/**
	 * @param canOnlyBeCompleteAfterNumberOfCourseLaunches
	 *            the canOnlyBeCompleteAfterNumberOfCourseLaunches to set
	 */
	public void setCanOnlyBeCompleteAfterNumberOfCourseLaunches(
			Boolean canOnlyBeCompleteAfterNumberOfCourseLaunches) {
		if(canOnlyBeCompleteAfterNumberOfCourseLaunches==null){
			this.canOnlyBeCompleteAfterNumberOfCourseLaunches=Boolean.FALSE;
		}else{
			this.canOnlyBeCompleteAfterNumberOfCourseLaunches = canOnlyBeCompleteAfterNumberOfCourseLaunches;
		}
		
	}

	/**
	 * @return the numberOfCoursesLaunch
	 */
	public Integer getNumberOfCoursesLaunch() {
		return numberOfCoursesLaunch;
	}

	/**
	 * @param numberOfCoursesLaunch
	 *            the numberOfCoursesLaunch to set
	 */
	public void setNumberOfCoursesLaunch(Integer numberOfCoursesLaunch) {
		this.numberOfCoursesLaunch = numberOfCoursesLaunch;
	}

	

	/**
	 * @param completeWithinTimePeriod
	 *            the completeWithinTimePeriod to set
	 */
	public void setCompleteWithinTimePeriod(Boolean completeWithinTimePeriod) {
		if(completeWithinTimePeriod==null){
			this.completeWithinTimePeriod=Boolean.FALSE;
		}else{
			this.completeWithinTimePeriod = completeWithinTimePeriod;
		}
	}

	

	/**
	 * @param completeWithinDaysOfRegistration
	 *            the completeWithinDaysOfRegistration to set
	 */
	public void setCompleteWithinDaysOfRegistration(
			Boolean completeWithinDaysOfRegistration) {
		if(completeWithinDaysOfRegistration==null){
			this.completeWithinDaysOfRegistration=Boolean.FALSE;
		}else{
			this.completeWithinDaysOfRegistration = completeWithinDaysOfRegistration;
		}
	}

	/**
	 * @return the minutesAfterFirstCourseLaunch
	 */
	public Integer getMinutesAfterFirstCourseLaunch() {
		return minutesAfterFirstCourseLaunch;
	}

	/**
	 * @param minutesAfterFirstCourseLaunch
	 *            the minutesAfterFirstCourseLaunch to set
	 */
	public void setMinutesAfterFirstCourseLaunch(
			Integer minutesAfterFirstCourseLaunch) {
		this.minutesAfterFirstCourseLaunch = minutesAfterFirstCourseLaunch;
	}

	/**
	 * @return the daysOfRegistraion
	 */
	public Integer getDaysOfRegistraion() {
		return daysOfRegistraion;
	}

	/**
	 * @param daysOfRegistraion
	 *            the daysOfRegistraion to set
	 */
	public void setDaysOfRegistraion(Integer daysOfRegistraion) {
		this.daysOfRegistraion = daysOfRegistraion;
	}

	/**
	 * @return the preassessmentEnabled
	 */
	public Boolean isPreassessmentEnabled() {
		return getPreAssessmentConfiguration().getAssessmentEnabled();
	}

	/**
	 * @param preassessmentEnabled
	 *            the preassessmentEnabled to set
	 */
	public void setPreassessmentEnabled(Boolean preassessmentEnabled) {
		getPreAssessmentConfiguration().setAssessmentEnabled(preassessmentEnabled);
	}

	/**
	 * @return the preassessmentNoquestion
	 */
	public Integer getPreassessmentNoquestion() {
		return getPreAssessmentConfiguration().getNumberQuestionsToPresent();
	}

	/**
	 * @param preassessmentNoquestion
	 *            the preassessmentNoquestion to set
	 */
	public void setPreassessmentNoquestion(Integer preassessmentNoquestion) {
		getPreAssessmentConfiguration().setNumberQuestionsToPresent(preassessmentNoquestion);
	}

	/**
	 * @return the preassessmentMasteryscore
	 */
	public Integer getPreassessmentMasteryscore() {
		return getPreAssessmentConfiguration().getMasteryScore();
	}

	/**
	 * @param preassessmentMasteryscore
	 *            the preassessmentMasteryscore to set
	 */
	public void setPreassessmentMasteryscore(Integer preassessmentMasteryscore) {
		getPreAssessmentConfiguration().setMasteryScore(preassessmentMasteryscore);
	}

	/**
	 * @return the preassessmentRandomizequestion
	 */
	public Boolean isPreassessmentRandomizequestion() {
		return getPreAssessmentConfiguration().getRandomizeQuestions();
	}

	/**
	 * @param preassessmentRandomizequestion
	 *            the preassessmentRandomizequestion to set
	 */
	public void setPreassessmentRandomizequestion(
			Boolean preassessmentRandomizequestion) {
		getPreAssessmentConfiguration().setRandomizeQuestions(preassessmentRandomizequestion);
	}

	/**
	 * @return the preassessmentRandomizeanswers
	 */
	public Boolean isPreassessmentRandomizeanswers() {
		return getPreAssessmentConfiguration().getRandomizeAnswers();
	}

	/**
	 * @param preassessmentRandomizeanswers
	 *            the preassessmentRandomizeanswers to set
	 */
	public void setPreassessmentRandomizeanswers(
			Boolean preassessmentRandomizeanswers) {
		getPreAssessmentConfiguration().setRandomizeAnswers(preassessmentRandomizeanswers);
	}

	/**
	 * @return the preassesmentenforceUniqueQuestionRetake
	 */
	public Boolean isPreassesmentenforceUniqueQuestionRetake() {
		return getPreAssessmentConfiguration().getUseUniqueQuestionsOnRetake();
	}

	/**
	 * @param preassesmentenforceUniqueQuestionRetake
	 *            the preassesmentenforceUniqueQuestionRetake to set
	 */
	public void setPreassesmentenforceUniqueQuestionRetake(
			Boolean preassesmentenforceUniqueQuestionRetake) {
		getPreAssessmentConfiguration().setUseUniqueQuestionsOnRetake(preassesmentenforceUniqueQuestionRetake);
	}

	/**
	 * @return the preassessmentAllowskippingquestion
	 */
	public Boolean isPreassessmentAllowskippingquestion() {
		return getPreAssessmentConfiguration().getAllowQuestionSkipping();
	}

	/**
	 * @param preassessmentAllowskippingquestion
	 *            the preassessmentAllowskippingquestion to set
	 */
	public void setPreassessmentAllowskippingquestion(
			Boolean preassessmentAllowskippingquestion) {
		getPreAssessmentConfiguration().setAllowQuestionSkipping(preassessmentAllowskippingquestion);
	}

	/**
	 * @return the preassessmentActiontotakeafterfailingmaxattempt
	 */
	public String getPreassessmentActiontotakeafterfailingmaxattempt() {
		return getPreAssessmentConfiguration().getActionToTakeAfterMaximumAttemptsReached();
	}

	/**
	 * @param preassessmentActiontotakeafterfailingmaxattempt
	 *            the preassessmentActiontotakeafterfailingmaxattempt to set
	 */
	public void setPreassessmentActiontotakeafterfailingmaxattempt(
			String preassessmentActiontotakeafterfailingmaxattempt) {
		getPreAssessmentConfiguration().setActionToTakeAfterMaximumAttemptsReached(preassessmentActiontotakeafterfailingmaxattempt);
	}

	/**
	 * @return the preassessmentMaximumnoattempt
	 */
	public Integer getPreassessmentMaximumnoattempt() {
		return getPreAssessmentConfiguration().getMaximunNumberAttemptsAllowed();
	}

	/**
	 * @param preassessmentMaximumnoattempt
	 *            the preassessmentMaximumnoattempt to set
	 */
	public void setPreassessmentMaximumnoattempt(
			Integer preassessmentMaximumnoattempt) {
		getPreAssessmentConfiguration().setMaximunNumberAttemptsAllowed(preassessmentMaximumnoattempt);
	}

	/**
	 * @return the preassessmentProctoredassessment
	 */
	public Boolean isPreassessmentProctoredassessment() {
		return getPreAssessmentConfiguration().getEnableAssessmentProctoring();
	}

	/**
	 * @param preassessmentProctoredassessment
	 *            the preassessmentProctoredassessment to set
	 */
	public void setPreassessmentProctoredassessment(
			Boolean preassessmentProctoredassessment) {
		getPreAssessmentConfiguration().setEnableAssessmentProctoring(preassessmentProctoredassessment);
	}
	
	
	
	public Boolean isPreEnforeceMaximumtimelimit(){
		return getPreAssessmentConfiguration().getEnforceMaximumTimeLimit();
	}
	public void setPreEnforeceMaximumtimelimit(Boolean preEnforeceMaximumtimelimit){
		getPreAssessmentConfiguration().setEnforceMaximumTimeLimit(preEnforeceMaximumtimelimit);
	}
	

	/**
	 * @return the preassessmentEnforcemaximumtimelimit
	 */
	public Integer getPreassessmentMaximumtimelimit() {
		return getPreAssessmentConfiguration().getMaximumTimeLimitMinutes();
	}

	/**
	 * @param preassessmentEnforcemaximumtimelimit
	 *            the preassessmentEnforcemaximumtimelimit to set
	 */
	public void setPreassessmentMaximumtimelimit(
			Integer preassessmentEnforcemaximumtimelimit) {
		getPreAssessmentConfiguration().setMaximumTimeLimitMinutes(preassessmentEnforcemaximumtimelimit);
	}

	/**
	 * @return the preassessmentScoreAsYouGo
	 */
	public Boolean isPreassessmentScoreAsYouGo() {
		return (getPreAssessmentConfiguration().getGradingBehavior().trim().equalsIgnoreCase(AssessmentConfiguration.GRADINGBEHAVIOR_AFTEREACHQUESTIONANSWERED));
	}

	/**
	 * @param preassessmentScoreAsYouGo
	 *            the preassessmentScoreAsYouGo to set
	 */
	public void setPreassessmentScoreAsYouGo(Boolean preassessmentScoreAsYouGo) {
		if ( preassessmentScoreAsYouGo ) {
			getPreAssessmentConfiguration().setGradingBehavior(AssessmentConfiguration.GRADINGBEHAVIOR_AFTEREACHQUESTIONANSWERED);
		}
	}

	/**
	 * @return the preassessmentScoretype
	 */
	public String getPreassessmentScoretype() {
		return getPreAssessmentConfiguration().getScoringType();
	}

	/**
	 * @param preassessmentScoretype
	 *            the preassessmentScoretype to set
	 */
	public void setPreassessmentScoretype(String preassessmentScoretype) {
		getPreAssessmentConfiguration().setScoringType(preassessmentScoretype);
	}

	/**
	 * @return the preassessmentQuestionlevelresult
	 */
	public Boolean isPreassessmentQuestionlevelresult() {
		return getPreAssessmentConfiguration().getShowQuestionLevelResults();
	}

	/**
	 * @param preassessmentQuestionlevelresult
	 *            the preassessmentQuestionlevelresult to set
	 */
	public void setPreassessmentQuestionlevelresult(
			Boolean preassessmentQuestionlevelresult) {
		getPreAssessmentConfiguration().setShowQuestionLevelResults(preassessmentQuestionlevelresult);
	}

	/**
	 * @return the postassessmentEnabled
	 */
	public Boolean isPostassessmentEnabled() {
		return getPostAssessmentConfiguration().getAssessmentEnabled();
	}

	/**
	 * @param postassessmentEnabled
	 *            the postassessmentEnabled to set
	 */
	public void setPostassessmentEnabled(Boolean postassessmentEnabled) {
		getPostAssessmentConfiguration().setAssessmentEnabled(postassessmentEnabled);
	}

	/**
	 * @return the postassessmentNoquestion
	 */
	public Integer getPostassessmentNoquestion() {
		return getPostAssessmentConfiguration().getNumberQuestionsToPresent();
	}

	/**
	 * @param postassessmentNoquestion
	 *            the postassessmentNoquestion to set
	 */
	public void setPostassessmentNoquestion(Integer postassessmentNoquestion) {
		getPostAssessmentConfiguration().setNumberQuestionsToPresent(postassessmentNoquestion);
	}

	/**
	 * @return the postassessmentMasteryscore
	 */
	public Integer getPostassessmentMasteryscore() {
		return getPostAssessmentConfiguration().getMasteryScore();
	}

	/**
	 * @param postassessmentMasteryscore
	 *            the postassessmentMasteryscore to set
	 */
	public void setPostassessmentMasteryscore(Integer postassessmentMasteryscore) {
		getPostAssessmentConfiguration().setMasteryScore(postassessmentMasteryscore);
	}

	/**
	 * @return the postassessmentRandomizequestion
	 */
	public Boolean isPostassessmentRandomizequestion() {
		return getPostAssessmentConfiguration().getRandomizeQuestions();
	}

	/**
	 * @param postassessmentRandomizequestion
	 *            the postassessmentRandomizequestion to set
	 */
	public void setPostassessmentRandomizequestion(
			Boolean postassessmentRandomizequestion) {
		getPostAssessmentConfiguration().setRandomizeQuestions(postassessmentRandomizequestion);
	}

	/**
	 * @return the postassessmentRandomizeanswers
	 */
	public Boolean isPostassessmentRandomizeanswers() {
		return getPostAssessmentConfiguration().getRandomizeAnswers();
	}

	/**
	 * @param postassessmentRandomizeanswers
	 *            the postassessmentRandomizeanswers to set
	 */
	public void setPostassessmentRandomizeanswers(
			Boolean postassessmentRandomizeanswers) {
		getPostAssessmentConfiguration().setRandomizeAnswers(postassessmentRandomizeanswers);
	}

	/**
	 * @return the postassesmentenforceUniqueQuestionRetake
	 */
	public Boolean isPostassesmentenforceUniqueQuestionRetake() {
		return getPostAssessmentConfiguration().getUseUniqueQuestionsOnRetake();
	}

	/**
	 * @param postassesmentenforceUniqueQuestionRetake
	 *            the postassesmentenforceUniqueQuestionRetake to set
	 */
	public void setPostassesmentenforceUniqueQuestionRetake(
			Boolean postassesmentenforceUniqueQuestionRetake) {
		getPostAssessmentConfiguration().setUseUniqueQuestionsOnRetake(postassesmentenforceUniqueQuestionRetake);
	}

	/**
	 * @return the postassessmentAllowskippingquestion
	 */
	public Boolean isPostassessmentAllowskippingquestion() {
		return getPostAssessmentConfiguration().getAllowQuestionSkipping();
	}

	/**
	 * @param postassessmentAllowskippingquestion
	 *            the postassessmentAllowskippingquestion to set
	 */
	public void setPostassessmentAllowskippingquestion(
			Boolean postassessmentAllowskippingquestion) {
		getPostAssessmentConfiguration().setAllowQuestionSkipping(postassessmentAllowskippingquestion);
	}

	/**
	 * @return the postassessmentActiontotakeafterfailingmaxattempt
	 */
	public String getPostassessmentActiontotakeafterfailingmaxattempt() {
		return getPostAssessmentConfiguration().getActionToTakeAfterMaximumAttemptsReached();
	}

	/**
	 * @param postassessmentActiontotakeafterfailingmaxattempt
	 *            the postassessmentActiontotakeafterfailingmaxattempt to set
	 */
	public void setPostassessmentActiontotakeafterfailingmaxattempt(
			String postassessmentActiontotakeafterfailingmaxattempt) {
		getPostAssessmentConfiguration().setActionToTakeAfterMaximumAttemptsReached(postassessmentActiontotakeafterfailingmaxattempt);
	}

	/**
	 * @return the postassessmentMaximumnoattempt
	 */
	public Integer getPostassessmentMaximumnoattempt() {
		return getPostAssessmentConfiguration().getMaximunNumberAttemptsAllowed();
	}

	/**
	 * @param postassessmentMaximumnoattempt
	 *            the postassessmentMaximumnoattempt to set
	 */
	public void setPostassessmentMaximumnoattempt(
			Integer postassessmentMaximumnoattempt) {
		getPostAssessmentConfiguration().setMaximunNumberAttemptsAllowed(postassessmentMaximumnoattempt);
	}

	/**
	 * @return the postassessmentProctoredassessment
	 */
	public Boolean isPostassessmentProctoredassessment() {
		return getPostAssessmentConfiguration().getEnableAssessmentProctoring();
	}

	/**
	 * @param postassessmentProctoredassessment
	 *            the postassessmentProctoredassessment to set
	 */
	public void setPostassessmentProctoredassessment(
			Boolean postassessmentProctoredassessment) {
		getPostAssessmentConfiguration().setEnableAssessmentProctoring(postassessmentProctoredassessment);
	}

	
	public Boolean isPostEnforeceMaximumtimelimit(){
		return getPostAssessmentConfiguration().getEnforceMaximumTimeLimit();
	}
	public void setPostEnforeceMaximumtimelimit(Boolean postEnforeceMaximumtimelimit){
		getPostAssessmentConfiguration().setEnforceMaximumTimeLimit(postEnforeceMaximumtimelimit);
	}
	
	/**
	 * @return the postassessmentEnforcemaximumtimelimit
	 */
	public Integer getPostassessmentMaximumtimelimit() {
		return getPostAssessmentConfiguration().getMaximumTimeLimitMinutes();
	}

	/**
	 * @param postassessmentEnforcemaximumtimelimit
	 *            the postassessmentEnforcemaximumtimelimit to set
	 */
	public void setPostassessmentMaximumtimelimit(
			Integer postassessmentEnforcemaximumtimelimit) {
		getPostAssessmentConfiguration().setMaximumTimeLimitMinutes(postassessmentEnforcemaximumtimelimit);
	}

	/**
	 * @return the postassessmentScoreAsYouGo
	 */
	public Boolean isPostassessmentScoreAsYouGo() {
		return (getPostAssessmentConfiguration().getGradingBehavior().trim().equalsIgnoreCase(AssessmentConfiguration.GRADINGBEHAVIOR_AFTEREACHQUESTIONANSWERED));
	}

	/**
	 * @param postassessmentScoreAsYouGo
	 *            the postassessmentScoreAsYouGo to set
	 */
	public void setPostassessmentScoreAsYouGo(Boolean postassessmentScoreAsYouGo) {
		if ( postassessmentScoreAsYouGo ) {
			getPostAssessmentConfiguration().setGradingBehavior(AssessmentConfiguration.GRADINGBEHAVIOR_AFTEREACHQUESTIONANSWERED);
		}

	}

	/**
	 * @return the postassessmentScoretype
	 */
	public String getPostassessmentScoretype() {
		return getPostAssessmentConfiguration().getScoringType();
	}

	/**
	 * @param postassessmentScoretype
	 *            the postassessmentScoretype to set
	 */
	public void setPostassessmentScoretype(String postassessmentScoretype) {
		getPostAssessmentConfiguration().setScoringType(postassessmentScoretype);
	}

	/**
	 * @return the postassessmentQuestionlevelresult
	 */
	public Boolean isPostassessmentQuestionlevelresult() {
		return getPostAssessmentConfiguration().getShowQuestionLevelResults();
	}

	/**
	 * @param postassessmentQuestionlevelresult
	 *            the postassessmentQuestionlevelresult to set
	 */
	public void setPostassessmentQuestionlevelresult(
			Boolean postassessmentQuestionlevelresult) {
		getPostAssessmentConfiguration().setShowQuestionLevelResults(postassessmentQuestionlevelresult);
	}

	/**
	 * @return the quizEnabled
	 */
	public Boolean isQuizEnabled() {
		return getQuizAssessmentConfiguration().getAssessmentEnabled();
	}

	/**
	 * @param quizEnabled
	 *            the quizEnabled to set
	 */
	public void setQuizEnabled(Boolean quizEnabled) {
		getQuizAssessmentConfiguration().setAssessmentEnabled(quizEnabled);
	}

	/**
	 * @return the quizNoquestion
	 */
	public Integer getQuizNoquestion() {
		return getQuizAssessmentConfiguration().getNumberQuestionsToPresent();
	}

	/**
	 * @param quizNoquestion
	 *            the quizNoquestion to set
	 */
	public void setQuizNoquestion(Integer quizNoquestion) {
		getQuizAssessmentConfiguration().setNumberQuestionsToPresent(quizNoquestion);
	}

	/**
	 * @return the quizMasteryscore
	 */
	public Integer getQuizMasteryscore() {
		return getQuizAssessmentConfiguration().getMasteryScore();
	}

	/**
	 * @param quizMasteryscore
	 *            the quizMasteryscore to set
	 */
	public void setQuizMasteryscore(Integer quizMasteryscore) {
		getQuizAssessmentConfiguration().setMasteryScore(quizMasteryscore);
	}

	/**
	 * @return the quizRandomizequestion
	 */
	public Boolean isQuizRandomizequestion() {
		return getQuizAssessmentConfiguration().getRandomizeQuestions();
	}

	/**
	 * @param quizRandomizequestion
	 *            the quizRandomizequestion to set
	 */
	public void setQuizRandomizequestion(Boolean quizRandomizequestion) {
		getQuizAssessmentConfiguration().setRandomizeQuestions(quizRandomizequestion);
	}

	/**
	 * @return the quizRandomizeanswers
	 */
	public Boolean isQuizRandomizeanswers() {
		return getQuizAssessmentConfiguration().getRandomizeAnswers();
	}

	/**
	 * @param quizRandomizeanswers
	 *            the quizRandomizeanswers to set
	 */
	public void setQuizRandomizeanswers(Boolean quizRandomizeanswers) {
		getQuizAssessmentConfiguration().setRandomizeAnswers(quizRandomizeanswers);
	}

	/**
	 * @return the quizenforceUniqueQuestionRetake
	 */
	public Boolean isQuizenforceUniqueQuestionRetake() {
		return getQuizAssessmentConfiguration().getUseUniqueQuestionsOnRetake();
	}

	/**
	 * @param quizenforceUniqueQuestionRetake
	 *            the quizenforceUniqueQuestionRetake to set
	 */
	public void setQuizenforceUniqueQuestionRetake(
			Boolean quizenforceUniqueQuestionRetake) {
		getQuizAssessmentConfiguration().setUseUniqueQuestionsOnRetake(quizenforceUniqueQuestionRetake);
	}

	/**
	 * @return the quizAllowskippingquestion
	 */
	public Boolean isQuizAllowskippingquestion() {
		return getQuizAssessmentConfiguration().getAllowQuestionSkipping();
	}

	/**
	 * @param quizAllowskippingquestion
	 *            the quizAllowskippingquestion to set
	 */
	public void setQuizAllowskippingquestion(Boolean quizAllowskippingquestion) {
		getQuizAssessmentConfiguration().setAllowQuestionSkipping(quizAllowskippingquestion);
	}

	/**
	 * @return the quizActiontotakeafterfailingmaxattempt
	 */
	public String getQuizActiontotakeafterfailingmaxattempt() {
		return getQuizAssessmentConfiguration().getActionToTakeAfterMaximumAttemptsReached();
	}

	/**
	 * @param quizActiontotakeafterfailingmaxattempt
	 *            the quizActiontotakeafterfailingmaxattempt to set
	 */
	public void setQuizActiontotakeafterfailingmaxattempt(
			String quizActiontotakeafterfailingmaxattempt) {
		getQuizAssessmentConfiguration().setActionToTakeAfterMaximumAttemptsReached(quizActiontotakeafterfailingmaxattempt);
	}

	/**
	 * @return the quizMaximumnoattempt
	 */
	public Integer getQuizMaximumnoattempt() {
		return getQuizAssessmentConfiguration().getMaximunNumberAttemptsAllowed();
	}

	/**
	 * @param quizMaximumnoattempt
	 *            the quizMaximumnoattempt to set
	 */
	public void setQuizMaximumnoattempt(Integer quizMaximumnoattempt) {
		getQuizAssessmentConfiguration().setMaximunNumberAttemptsAllowed(quizMaximumnoattempt);
	}

	/**
	 * @return the quizProctoredassessment
	 */
	public Boolean isQuizProctoredassessment() {
		return getQuizAssessmentConfiguration().getEnableAssessmentProctoring();
	}

	/**
	 * @param quizProctoredassessment
	 *            the quizProctoredassessment to set
	 */
	public void setQuizProctoredassessment(Boolean quizProctoredassessment) {
		getQuizAssessmentConfiguration().setEnableAssessmentProctoring(quizProctoredassessment);
	}
	
	public Boolean isQuizEnforeceMaximumtimelimit(){
		return getQuizAssessmentConfiguration().getEnforceMaximumTimeLimit();
	}
	public void setQuizEnforeceMaximumtimelimit(Boolean quizEnforeceMaximumtimelimit){
		getQuizAssessmentConfiguration().setEnforceMaximumTimeLimit(quizEnforeceMaximumtimelimit);
	}

	/**
	 * @return the quizEnforcemaximumtimelimit
	 */
	public Integer getQuizMaximumtimelimit() {
		return getQuizAssessmentConfiguration().getMaximumTimeLimitMinutes();
	}

	/**
	 * @param quizEnforcemaximumtimelimit
	 *            the quizEnforcemaximumtimelimit to set
	 */
	public void setQuizMaximumtimelimit(Integer quizMaximumtimelimit) {
		getQuizAssessmentConfiguration().setMaximumTimeLimitMinutes(quizMaximumtimelimit);
	}

	/**
	 * @return the quizScoreAsYouGo
	 */
	public Boolean isQuizScoreAsYouGo() {
		return (getQuizAssessmentConfiguration().getGradingBehavior().trim().equalsIgnoreCase(AssessmentConfiguration.GRADINGBEHAVIOR_AFTEREACHQUESTIONANSWERED));
	}

	/**
	 * @param quizScoreAsYouGo
	 *            the quizScoreAsYouGo to set
	 */
	public void setQuizScoreAsYouGo(Boolean quizScoreAsYouGo) {
		if ( quizScoreAsYouGo ) {
			getQuizAssessmentConfiguration().setGradingBehavior(AssessmentConfiguration.GRADINGBEHAVIOR_AFTEREACHQUESTIONANSWERED);
		}

	}

	/**
	 * @return the quizScoretype
	 */
	public String getQuizScoretype() {
		return getQuizAssessmentConfiguration().getScoringType();
	}

	/**
	 * @param quizScoretype
	 *            the quizScoretype to set
	 */
	public void setQuizScoretype(String quizScoretype) {
		getQuizAssessmentConfiguration().setScoringType(quizScoretype);
	}

	/**
	 * @return the quizQuestionlevelresult
	 */
	public Boolean isQuizQuestionlevelresult() {
		return getQuizAssessmentConfiguration().getShowQuestionLevelResults();
	}

	/**
	 * @param quizQuestionlevelresult
	 *            the quizQuestionlevelresult to set
	 */
	public void setQuizQuestionlevelresult(Boolean quizQuestionlevelresult) {
		getQuizAssessmentConfiguration().setShowQuestionLevelResults(quizQuestionlevelresult);
	}

	/**
	 * @param enableIdentityValidation
	 *            the enableIdentityValidation to set
	 */
	public void setEnableIdentityValidation(Boolean enableIdentityValidation) {
		if(enableIdentityValidation==null){
			this.enableIdentityValidation=Boolean.FALSE;
		}else{
			this.enableIdentityValidation = enableIdentityValidation;
		}
	}

	/**
	 * @return the requireIdentityValidationEverySeconds
	 */
	public Integer getRequireIdentityValidationEverySeconds() {
		return requireIdentityValidationEverySeconds;
	}

	/**
	 * @param requireIdentityValidationEverySeconds
	 *            the requireIdentityValidationEverySeconds to set
	 */
	public void setRequireIdentityValidationEverySeconds(
			Integer requireIdentityValidationEverySeconds) {
		this.requireIdentityValidationEverySeconds = requireIdentityValidationEverySeconds;
	}

	/**
	 * @return the allowSecondsToAnswerEachQuestion
	 */
	public Integer getAllowSecondsToAnswerEachQuestion() {
		return allowSecondsToAnswerEachQuestion;
	}

	/**
	 * @param allowSecondsToAnswerEachQuestion
	 *            the allowSecondsToAnswerEachQuestion to set
	 */
	public void setAllowSecondsToAnswerEachQuestion(
			Integer allowSecondsToAnswerEachQuestion) {
		this.allowSecondsToAnswerEachQuestion = allowSecondsToAnswerEachQuestion;
	}

//	/**
//	 * @return the allowNumberofAttemptsToAnswerCorrectly
//	 */
//	public Integer getAllowNumberofAttemptsToAnswerCorrectly() {
//		return allowNumberofAttemptsToAnswerCorrectly;
//	}
//
//	/**
//	 * @param allowNumberofAttemptsToAnswerCorrectly
//	 *            the allowNumberofAttemptsToAnswerCorrectly to set
//	 */
//	public void setAllowNumberofAttemptsToAnswerCorrectly(
//			Integer allowNumberofAttemptsToAnswerCorrectly) {
//		this.allowNumberofAttemptsToAnswerCorrectly = allowNumberofAttemptsToAnswerCorrectly;
//	}

	/**
	 * @return the preAssesmentShowQuestionAnswerSummary
	 */
	public Boolean isPreAssesmentShowQuestionAnswerSummary() {
		return getPreAssessmentConfiguration().getShowQuestionAnswerReview();
	}

	/**
	 * @param preAssesmentShowQuestionAnswerSummary
	 *            the preAssesmentShowQuestionAnswerSummary to set
	 */
	public void setPreAssesmentShowQuestionAnswerSummary(
			Boolean preAssesmentShowQuestionAnswerSummary) {
		getPreAssessmentConfiguration().setShowQuestionAnswerReview(preAssesmentShowQuestionAnswerSummary);
	}

	/**
	 * @return the postAssesmentShowQuestionAnswerSummary
	 */
	public Boolean isPostAssesmentShowQuestionAnswerSummary() {
		return getPostAssessmentConfiguration().getShowQuestionAnswerReview();
	}

	/**
	 * @param postAssesmentShowQuestionAnswerSummary
	 *            the postAssesmentShowQuestionAnswerSummary to set
	 */
	public void setPostAssesmentShowQuestionAnswerSummary(
			Boolean postAssesmentShowQuestionAnswerSummary) {
		getPostAssessmentConfiguration().setShowQuestionAnswerReview(postAssesmentShowQuestionAnswerSummary);
	}

	/**
	 * @return the quizShowQuestionAnswerSummary
	 */
	public Boolean isQuizShowQuestionAnswerSummary() {
		return getQuizAssessmentConfiguration().getShowQuestionAnswerReview();
	}

	/**
	 * @param quizShowQuestionAnswerSummary
	 *            the quizShowQuestionAnswerSummary to set
	 */
	public void setQuizShowQuestionAnswerSummary(
			Boolean quizShowQuestionAnswerSummary) {
		getQuizAssessmentConfiguration().setShowQuestionAnswerReview(quizShowQuestionAnswerSummary);
	}

	/**
	 * @return the validationTimeBetweenQuestion
	 */
	public Integer getValidationTimeBetweenQuestion() {
		return validationTimeBetweenQuestion;
	}

	/**
	 * @param validationTimeBetweenQuestion
	 *            the validationTimeBetweenQuestion to set
	 */
	public void setValidationTimeBetweenQuestion(
			Integer validationTimeBetweenQuestion) {
		this.validationTimeBetweenQuestion = validationTimeBetweenQuestion;
	}

	/**
	 * @return the validationNoMissedQuestionsAllowed
	 */
	public Integer getValidationNoMissedQuestionsAllowed() {
		return validationNoMissedQuestionsAllowed;
	}

	/**
	 * @param validationNoMissedQuestionsAllowed
	 *            the validationNoMissedQuestionsAllowed to set
	 */
	public void setValidationNoMissedQuestionsAllowed(
			Integer validationNoMissedQuestionsAllowed) {
		this.validationNoMissedQuestionsAllowed = validationNoMissedQuestionsAllowed;
	}

	/**
	 * @param acknowledgeText
	 *            the acknowledgeText to set
	 */
	public void setAcknowledgeText(String acknowledgeText) {
		this.acknowledgeText = acknowledgeText;
	}

	/**
	 * @return the acknowledgeText
	 */
	public String getAcknowledgeText() {
		return acknowledgeText;
	}

	/**
	 * @param acknowledgeEnabled
	 *            the acknowledgeEnabled to set
	 */
	public void setAcknowledgeEnabled(Boolean acknowledgeEnabled) {
		this.acknowledgeEnabled = acknowledgeEnabled;
	}

	public String getUnitOfDaysOfRegistration() {
		return unitOfDaysOfRegistration;
	}

	public void setUnitOfDaysOfRegistration(String unitOfDaysOfRegistration) {
		this.unitOfDaysOfRegistration = unitOfDaysOfRegistration;
	}

	public String getUnitOfTimeToComplete() {
		return unitOfTimeToComplete;
	}

	public void setUnitOfTimeToComplete(String unitOfTimeToComplete) {
		this.unitOfTimeToComplete = unitOfTimeToComplete;
	}

	
	public void setCourseEvaluationSpecified(Boolean courseEvaluationSpecified) {
		if(courseEvaluationSpecified==null){
			this.courseEvaluationSpecified=Boolean.FALSE;
		}else{
			this.courseEvaluationSpecified = courseEvaluationSpecified;
		}
	}

//	public Boolean isCompleteSurveys() {
//		return completeSurveys;
//	}
//
//	public void setCompleteSurveys(Boolean completeSurveys) {
//		this.completeSurveys = completeSurveys;
//	}
//
//	public Course getCourse() {
//		return course;
//	}
//
//	public void setCourse(Course course) {
//		this.course = course;
//	}
	

	// implement the business logic to check for course completion
	public Boolean meetsPlayEverySceneRequirement(LearnerCourseStatistics lcs) {
		if ( /*lcs.isCompleted()*/lcs.isCourseCompleted() ) {
			return true;
		}
		if ( lcs.getPercentComplete() >= 100 ) {
			return true;
		}
		return false;
	}
	
	public Boolean meetsUniqueLaunchesRequirement(LearnerCourseStatistics lcs) {
		if ( /*lcs.isCompleted()*/lcs.isCourseCompleted() ) {
			return true;
		}
		if ( lcs.getLaunchesAccrued() >= this.getNumberOfCoursesLaunch() ) {
			return true;
		}
		return false;
	}
	
	public Boolean meetsPreAssessmentScoreRequirement(LearnerCourseStatistics lcs) {
		if ( /*lcs.isCompleted()*/lcs.isCourseCompleted() ) {
			return true;
		}
		if ( lcs.getPretestScore() >= this.getPreassessmentMasteryscore() ) {
			return true;
		}
		return false;
	}
	
	public Boolean meetsPostAssessmentScoreRequirement(LearnerCourseStatistics lcs) {
		if ( /*lcs.isCompleted()*/lcs.isCourseCompleted() ) {
			return true;
		}
		if(lcs.getLastPostTestDate() != null){
			if ( lcs.getHighestPostTestScore() >= this.getPostassessmentMasteryscore() ) {
				return true;
			}
		}
		
		return false;
	}
	
	public Boolean meetsQuizAssessmentScoreRequirement(LearnerCourseStatistics lcs) {
		if ( /*lcs.isCompleted()*/lcs.isCourseCompleted() ) {
			return true;
		}		// TODO:  LATER/Known Issue.  Cheat the wind for now and just use the average
			    //        this needs to check each unique quiz per content object
			    //        to ensure that at least one quiz per content object met the
		        //        mastery score
		if ( lcs.getAverageQuizScore() >= this.getQuizMasteryscore()) {
			return true;
		}
		return false;
	}
	
	public Boolean meetsTimeInMinutesFromInitialStartRequirement(LearnerCourseStatistics lcs) {
		if ( /*lcs.isCompleted()*/lcs.isCourseCompleted() ) {
			return true;
		}	
		
		Date firstAccessDate = lcs.getFirstAccessDate();
		
		if(firstAccessDate==null)
			return false;
		
		Date now = Calendar.getInstance().getTime();
		Calendar cal = Calendar.getInstance();
		cal.setTime(firstAccessDate);
		//cal.add(Calendar.MINUTE, this.getMinutesAfterFirstCourseLaunch());
		//cal.add(Calendar.MINUTE, this.getMinutesAfterFirstCourseLaunch());
		
		if(unitOfTimeToComplete.equalsIgnoreCase("DAYS"))
			cal.add(Calendar.DATE, this.getMinutesAfterFirstCourseLaunch());
		if(unitOfTimeToComplete.equalsIgnoreCase("Months"))
			cal.add(Calendar.MONTH, this.getMinutesAfterFirstCourseLaunch());
		
		if(unitOfTimeToComplete.equalsIgnoreCase("minutes"))
			cal.add(Calendar.MINUTE, this.getMinutesAfterFirstCourseLaunch());
		
		if(this.getUnitOfTimeToComplete().equalsIgnoreCase(CourseConfiguration.UNITS_MONTH) ||
				this.getUnitOfTimeToComplete().equalsIgnoreCase(CourseConfiguration.UNITS_DAYS)){
			/*Allow student the entire last day*/
			cal.set(Calendar.HOUR_OF_DAY,23);
			cal.set(Calendar.MINUTE,59);
			cal.set(Calendar.SECOND,59);
			
		}
		
		
		//complete = (now.before(maxLimit.getTime()) || now.equals(maxLimit.getTime()));
		
		if ( cal.getTime().after(now) ) {
			return true;
		}
		return false;
	}
	
	public Boolean meetsTimeInDaysFromRegistrationRequirement(LearnerCourseStatistics lcs) {
		
		if ( /*lcs.isCompleted()*/ lcs.isCourseCompleted() ) {
			return true;
		}
		Date enrollmentStartDate = lcs.getLearnerEnrollment().getEnrollmentStartDate();
		
		Date now = new Date(System.currentTimeMillis());
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(enrollmentStartDate);
		cal.add(Calendar.DAY_OF_YEAR, this.getDaysOfRegistraion());
		
		if ( cal.getTime().after(now) ) {
			return true;
		}
		return false;
	}
	public Boolean meetsPostAssessmentAttempted(LearnerCourseStatistics lcs) {
	
		Boolean flag=true;
		if(getMustAttemptPostAssessment())
		{
			flag=(lcs.getLastPostTestDate() != null);
		}
		return flag;
	}

	public Boolean isCourseStrictlyEnforcePolicyToBeUsed() {
		return courseStrictlyEnforcePolicyToBeUsed;
	}

	public void setCourseStrictlyEnforcePolicyToBeUsed(
			Boolean courseStrictlyEnforcePolicyToBeUsed) {
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

	public Boolean isPlayerStrictlyEnforcePolicyToBeUsed() {
		return playerStrictlyEnforcePolicyToBeUsed;
	}

	public void setPlayerStrictlyEnforcePolicyToBeUsed(
			Boolean playerStrictlyEnforcePolicyToBeUsed) {
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

	public String getCertificateName() {
		return certificateName;
	}

	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}

	public Boolean isValidationStrictlyEnforcePolicyToBeUsed() {
		return validationStrictlyEnforcePolicyToBeUsed;
	}

	public void setValidationStrictlyEnforcePolicyToBeUsed(
			Boolean validationStrictlyEnforcePolicyToBeUsed) {
		this.validationStrictlyEnforcePolicyToBeUsed = validationStrictlyEnforcePolicyToBeUsed;
	}

	public String getEndOfCourseInstructions() {
		return endOfCourseInstructions;
	}

	public void setEndOfCourseInstructions(String endOfCourseInstructions) {
		this.endOfCourseInstructions = endOfCourseInstructions;
	}

	public Boolean isSeattimeenabled() {
		return seattimeenabled;
	}

	public void setSeattimeenabled(Boolean seattimeenabled) {
		this.seattimeenabled = seattimeenabled;
	}

	public Integer getSeattimeinhour() {
		return seattimeinhour;
	}

	public void setSeattimeinhour(Integer seattimeinhour) {
		this.seattimeinhour = seattimeinhour;
	}

	public Integer getSeattimeinmin() {
		return seattimeinmin;
	}

	public void setSeattimeinmin(Integer seattimeinmin) {
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

	
	
	public void setPreAssessmentEnabled(Boolean assessmentEnabled) {
		getPreAssessmentConfiguration().setAssessmentEnabled(assessmentEnabled) ;
	}
	public void setPostAssessmentEnabled(Boolean assessmentEnabled) {
		getPostAssessmentConfiguration().setAssessmentEnabled(assessmentEnabled) ;
	}
	public void setQuizAssessmentEnabled(Boolean assessmentEnabled) {
		getQuizAssessmentConfiguration().setAssessmentEnabled(assessmentEnabled) ;
	}

	public void setPreMaximunNumberAttemptsAllowed(Integer maximunNumberAttemptsAllowed) {
		getPreAssessmentConfiguration().setMaximunNumberAttemptsAllowed(maximunNumberAttemptsAllowed);
	}
	public void setPostMaximunNumberAttemptsAllowed(Integer maximunNumberAttemptsAllowed) {
		getPostAssessmentConfiguration().setMaximunNumberAttemptsAllowed(maximunNumberAttemptsAllowed);
	}
	public void setQuizMaximunNumberAttemptsAllowed(Integer maximunNumberAttemptsAllowed) {
		getQuizAssessmentConfiguration().setMaximunNumberAttemptsAllowed(maximunNumberAttemptsAllowed);
	}
	
	public void setPreNumberQuestionsToPresent(Integer numberQuestionsToPresent) {
		getPreAssessmentConfiguration().setNumberQuestionsToPresent(numberQuestionsToPresent);
		
	}
	public void setPostNumberQuestionsToPresent(Integer numberQuestionsToPresent) {
		getPostAssessmentConfiguration().setNumberQuestionsToPresent(numberQuestionsToPresent);
		
	}
	public void setQuizNumberQuestionsToPresent(Integer numberQuestionsToPresent) {
		getQuizAssessmentConfiguration().setNumberQuestionsToPresent(numberQuestionsToPresent);
		
	}

	public void setPreMasteryScore(Integer masteryScore) {
		getPreAssessmentConfiguration().setMasteryScore(masteryScore);
	}
	public void setPostMasteryScore(Integer masteryScore) {
		getPostAssessmentConfiguration().setMasteryScore(masteryScore);
	}
	public void setQuizMasteryScore(Integer masteryScore) {
		getQuizAssessmentConfiguration().setMasteryScore(masteryScore);
	}
	
	

	public void setPreActionToTakeAfterMaximumAttemptsReached(
			String actionToTakeAfterMaximumAttemptsReached) {
		getPreAssessmentConfiguration().setActionToTakeAfterMaximumAttemptsReached(actionToTakeAfterMaximumAttemptsReached);
	}

	public void setPostActionToTakeAfterMaximumAttemptsReached(
			String actionToTakeAfterMaximumAttemptsReached) {
		getPostAssessmentConfiguration().setActionToTakeAfterMaximumAttemptsReached(actionToTakeAfterMaximumAttemptsReached);
	}
	public void setQuizActionToTakeAfterMaximumAttemptsReached(
			String actionToTakeAfterMaximumAttemptsReached) {
		getQuizAssessmentConfiguration().setActionToTakeAfterMaximumAttemptsReached(actionToTakeAfterMaximumAttemptsReached);
	}
	
	public void setPreMaximumTimeLimitMinutes(Integer maximumTimeLimitMinutes) {
		getPreAssessmentConfiguration().setMaximumTimeLimitMinutes(maximumTimeLimitMinutes);
	}
	public void setPostMaximumTimeLimitMinutes(Integer maximumTimeLimitMinutes) {
		getPostAssessmentConfiguration().setMaximumTimeLimitMinutes(maximumTimeLimitMinutes);
	}
	public void setQuizMaximumTimeLimitMinutes(Integer maximumTimeLimitMinutes) {
		getQuizAssessmentConfiguration().setMaximumTimeLimitMinutes(maximumTimeLimitMinutes);
	}

	public void setPreUseUniqueQuestionsOnRetake(Boolean useUniqueQuestionsOnRetake) {
		getPreAssessmentConfiguration().setUseUniqueQuestionsOnRetake(useUniqueQuestionsOnRetake);
		
	}
	public void setPostUseUniqueQuestionsOnRetake(Boolean useUniqueQuestionsOnRetake) {
		getPostAssessmentConfiguration().setUseUniqueQuestionsOnRetake(useUniqueQuestionsOnRetake);
		
	}
	public void setQuizUseUniqueQuestionsOnRetake(Boolean useUniqueQuestionsOnRetake) {
		getQuizAssessmentConfiguration().setUseUniqueQuestionsOnRetake(useUniqueQuestionsOnRetake);
		
	}

	public void setPreMinimumSeatTimeBeforeAssessmentStart(
			Integer minimumSeatTimeBeforeAssessmentStart) {
		getPreAssessmentConfiguration().setMinimumSeatTimeBeforeAssessmentStart(minimumSeatTimeBeforeAssessmentStart);
	}
	public void setPostMinimumSeatTimeBeforeAssessmentStart(
			Integer minimumSeatTimeBeforeAssessmentStart) {
		getPostAssessmentConfiguration().setMinimumSeatTimeBeforeAssessmentStart(minimumSeatTimeBeforeAssessmentStart);
	}
	public Integer getPostMinimumSeatTimeBeforeAssessmentStart() {
		return getPostAssessmentConfiguration().getMinimumSeatTimeBeforeAssessmentStart();
	}
	public void setQuizMinimumSeatTimeBeforeAssessmentStart(
			Integer minimumSeatTimeBeforeAssessmentStart) {
		getQuizAssessmentConfiguration().setMinimumSeatTimeBeforeAssessmentStart(minimumSeatTimeBeforeAssessmentStart);
	}

	public void setPreMinimumSeatTimeBeforeAssessmentStartUnits(
			String minimumSeatTimeBeforeAssessmentStartUnits) {
		getPreAssessmentConfiguration().setMinimumSeatTimeBeforeAssessmentStartUnits(minimumSeatTimeBeforeAssessmentStartUnits);
	}
	public void setPostMinimumSeatTimeBeforeAssessmentStartUnits(
			String minimumSeatTimeBeforeAssessmentStartUnits) {
		getPostAssessmentConfiguration().setMinimumSeatTimeBeforeAssessmentStartUnits(minimumSeatTimeBeforeAssessmentStartUnits);
	}
	public String getPostMinimumSeatTimeBeforeAssessmentStartUnits() {
		return getPostAssessmentConfiguration().getMinimumSeatTimeBeforeAssessmentStartUnits();
	}
	public void setQuizMinimumSeatTimeBeforeAssessmentStartUnits(
			String minimumSeatTimeBeforeAssessmentStartUnits) {
		getQuizAssessmentConfiguration().setMinimumSeatTimeBeforeAssessmentStartUnits(minimumSeatTimeBeforeAssessmentStartUnits);
	}

	public void setPreEnableContentRemediation(Boolean enableContentRemediation) {
		getPreAssessmentConfiguration().setEnableContentRemediation(enableContentRemediation);
	}
	public void setPostEnableContentRemediation(Boolean enableContentRemediation) {
		getPostAssessmentConfiguration().setEnableContentRemediation(enableContentRemediation);
	}
	public void setQuizEnableContentRemediation(Boolean enableContentRemediation) {
		getQuizAssessmentConfiguration().setEnableContentRemediation(enableContentRemediation);
	}

	public void setPreEnableAssessmentProctoring(Boolean enableAssessmentProctoring) {
		getPreAssessmentConfiguration().setEnableAssessmentProctoring(enableAssessmentProctoring);
	}
	public void setPostEnableAssessmentProctoring(Boolean enableAssessmentProctoring) {
		getPostAssessmentConfiguration().setEnableAssessmentProctoring(enableAssessmentProctoring);
	}
	public void setQuizEnableAssessmentProctoring(Boolean enableAssessmentProctoring) {
		getQuizAssessmentConfiguration().setEnableAssessmentProctoring(enableAssessmentProctoring);
	}

	public void setPreShowQuestionLevelResults(Boolean showQuestionLevelResults) {
		getPreAssessmentConfiguration().setShowQuestionLevelResults(showQuestionLevelResults);
	}
	public void setPostShowQuestionLevelResults(Boolean showQuestionLevelResults) {
		getPostAssessmentConfiguration().setShowQuestionLevelResults(showQuestionLevelResults);
	}
	public void setQuizShowQuestionLevelResults(Boolean showQuestionLevelResults) {
		getQuizAssessmentConfiguration().setShowQuestionLevelResults(showQuestionLevelResults);
	}

	public void setPreRandomizeQuestions(Boolean randomizeQuestions) {
		getPreAssessmentConfiguration().setRandomizeQuestions(randomizeQuestions);
	}
	public void setPostRandomizeQuestions(Boolean randomizeQuestions) {
		getPostAssessmentConfiguration().setRandomizeQuestions(randomizeQuestions);
	}
	public void setQuizRandomizeQuestions(Boolean randomizeQuestions) {
		getQuizAssessmentConfiguration().setRandomizeQuestions(randomizeQuestions);
	}

	public void setPreRandomizeAnswers(Boolean randomizeAnswers) {
		getPreAssessmentConfiguration().setRandomizeAnswers(randomizeAnswers);
	}
	public void setPostRandomizeAnswers(Boolean randomizeAnswers) {
		getPostAssessmentConfiguration().setRandomizeAnswers(randomizeAnswers);
	}
	public void setQuizRandomizeAnswers(Boolean randomizeAnswers) {
		getQuizAssessmentConfiguration().setRandomizeAnswers(randomizeAnswers);
	}

	public void setPreScoringType(String scoringType) {
		getPreAssessmentConfiguration().setScoringType(scoringType);
	}
	public void setPostScoringType(String scoringType) {
		getPostAssessmentConfiguration().setScoringType(scoringType);
	}
	public void setQuizScoringType(String scoringType) {
		getQuizAssessmentConfiguration().setScoringType(scoringType);
	}

	public void setPreShowQuestionAnswerReview(Boolean showQuestionAnswerReview) {
		getPreAssessmentConfiguration().setShowQuestionAnswerReview(showQuestionAnswerReview);
	}
	public void setPostShowQuestionAnswerReview(Boolean showQuestionAnswerReview) {
		getPostAssessmentConfiguration().setShowQuestionAnswerReview(showQuestionAnswerReview);
	}
	public void setQuizShowQuestionAnswerReview(Boolean showQuestionAnswerReview) {
		getQuizAssessmentConfiguration().setShowQuestionAnswerReview(showQuestionAnswerReview);
	}

	public void setPreEnableRestrictiveAssessmentMode(
			Boolean enableRestrictiveAssessmentMode) {
		getPreAssessmentConfiguration().setEnableRestrictiveAssessmentMode(enableRestrictiveAssessmentMode);
	}
	
	public Boolean isPreLockoutAssessmentActiveWindow() {
		return getPreAssessmentConfiguration().getLockOutAssessmentActiveWindow();
	}
	
	public void setPreLockoutAssessmentActiveWindow(Boolean lockOutAssessmentActiveWindow) {
		getPreAssessmentConfiguration().setLockOutAssessmentActiveWindow(lockOutAssessmentActiveWindow);
	}
	public boolean isViewassessmentresultsEnabled() {
		return getPostAssessmentConfiguration().getViewassessmentresultsEnabled();
	}
	public void setViewassessmentresultsEnabled(boolean viewassessmentresultsEnabled) {
		getPostAssessmentConfiguration().setViewassessmentresultsEnabled(viewassessmentresultsEnabled);
	}
	public void setPostEnableRestrictiveAssessmentMode(
			Boolean enableRestrictiveAssessmentMode) {
		getPostAssessmentConfiguration().setEnableRestrictiveAssessmentMode(enableRestrictiveAssessmentMode);
	}
	
	public Boolean isPostLockoutAssessmentActiveWindow() {
		return getPostAssessmentConfiguration().getLockOutAssessmentActiveWindow();
	}
	
	public void setPostLockoutAssessmentActiveWindow(Boolean lockOutAssessmentActiveWindow) {
		getPostAssessmentConfiguration().setLockOutAssessmentActiveWindow(lockOutAssessmentActiveWindow);
	}
	
	public void setQuizEnableRestrictiveAssessmentMode(
			Boolean enableRestrictiveAssessmentMode) {
		getQuizAssessmentConfiguration().setEnableRestrictiveAssessmentMode(enableRestrictiveAssessmentMode);
	}
	
	public Boolean isQuizLockoutAssessmentActiveWindow() {
		return getQuizAssessmentConfiguration().getLockOutAssessmentActiveWindow();
	}
	
	public void setQuizLockoutAssessmentActiveWindow(Boolean lockOutAssessmentActiveWindow) {
		getQuizAssessmentConfiguration().setLockOutAssessmentActiveWindow(lockOutAssessmentActiveWindow);
	}

	public void setPreGradingBehavior(String gradingBehavior) {
		getPreAssessmentConfiguration().setGradingBehavior(gradingBehavior);
	}
	public void setPostGradingBehavior(String gradingBehavior) {
		getPostAssessmentConfiguration().setGradingBehavior(gradingBehavior);
	}
	public void setQuizGradingBehavior(String gradingBehavior) {
		getQuizAssessmentConfiguration().setGradingBehavior(gradingBehavior);
	}

	public void setPreEnableWeightedScoring(Boolean enableWeightedScoring) {
		getPreAssessmentConfiguration().setEnableWeightedScoring(enableWeightedScoring);
	}
	public void setPostEnableWeightedScoring(Boolean enableWeightedScoring) {
		getPostAssessmentConfiguration().setEnableWeightedScoring(enableWeightedScoring);
	}
	public void setQuizEnableWeightedScoring(Boolean enableWeightedScoring) {
		getQuizAssessmentConfiguration().setEnableWeightedScoring(enableWeightedScoring);
	}

	public void setPreAdvancedQuestionSelectionType(
			String advancedQuestionSelectionType) {
		getPreAssessmentConfiguration().setAdvancedQuestionSelectionType(advancedQuestionSelectionType);
	}
	public void setPostAdvancedQuestionSelectionType(
			String advancedQuestionSelectionType) {
		getPostAssessmentConfiguration().setAdvancedQuestionSelectionType(advancedQuestionSelectionType);
	}
	public void setQuizAdvancedQuestionSelectionType(
			String advancedQuestionSelectionType) {
		getQuizAssessmentConfiguration().setAdvancedQuestionSelectionType(advancedQuestionSelectionType);
	}

	public void setPreAllowQuestionSkipping(Boolean allowQuestionSkipping) {
		getPreAssessmentConfiguration().setAllowQuestionSkipping(allowQuestionSkipping);
	}
	
	public void setPostAllowQuestionSkipping(Boolean allowQuestionSkipping) {
		getPostAssessmentConfiguration().setAllowQuestionSkipping(allowQuestionSkipping);
	}
	public void setQuizAllowQuestionSkipping(Boolean allowQuestionSkipping) {
		getQuizAssessmentConfiguration().setAllowQuestionSkipping(allowQuestionSkipping);
	}

	
	public void setMustCompleteCourseEvaluation(Boolean mustCompleteCourseEvaluation) {
		if(mustCompleteCourseEvaluation==null){
			this.mustCompleteCourseEvaluation=Boolean.FALSE;
		}else{
			this.mustCompleteCourseEvaluation = mustCompleteCourseEvaluation;
		}
	}
	
	public void setSuggestedCourse(Boolean suggestedCourse) {
		if(suggestedCourse==null){
			this.suggestedCourse=Boolean.FALSE;
		}else{
			this.suggestedCourse = suggestedCourse;
		}
	}
    
	public void setRateCourse(Boolean rateCourse) {
		if(rateCourse==null){
			this.rateCourse=Boolean.FALSE;
		}else{
			this.rateCourse = rateCourse;
		}
	}
	
	public Boolean getRateCourse() {
		if(rateCourse==null){
			rateCourse=Boolean.FALSE;
		}
		return rateCourse;
	}
	
	public Boolean isDisplaySeatTimeTextMessage() {
		return getPostAssessmentConfiguration().getDisplaySeatTimeTextMessage();
	}
	public void setDisplaySeatTimeTextMessage(Boolean displaySeatTimeTextMessage) {
		getPostAssessmentConfiguration().setDisplaySeatTimeTextMessage(displaySeatTimeTextMessage);
	}
	public Boolean isLockPostAssessmentBeforeSeatTime() {
		return getPostAssessmentConfiguration().getLockPostAssessmentBeforeSeatTime();
	}
	public void setLockPostAssessmentBeforeSeatTime(
			Boolean lockPostAssessmentBeforeSeatTime) {
		getPostAssessmentConfiguration().setLockPostAssessmentBeforeSeatTime(lockPostAssessmentBeforeSeatTime);
	}
	
	public Certificate getCompletionCertificate() {
		return completionCertificate;
	}

	public void setCompletionCertificate(Certificate completionCertificate) {
		this.completionCertificate = completionCertificate;
	}

	public void setRespondToCourseEvaluation(Boolean respondToCourseEvaluation) {
		if(respondToCourseEvaluation==null){
			this.respondToCourseEvaluation=Boolean.FALSE;
		}else{
			this.respondToCourseEvaluation = respondToCourseEvaluation;
		}
	}

	public Integer getNumberOfValidationQuestions() {
		return numberOfValidationQuestions;
	}

	public void setNumberOfValidationQuestions(Integer numberOfValidationQuestions) {
		this.numberOfValidationQuestions = numberOfValidationQuestions;
	}

	
	public void setPreNoResultMessage(String noRsultMessage){
		getPreAssessmentConfiguration().setNoRsultMessage(noRsultMessage);
	}
	public String getPreNoResultMessage(){
		return getPreAssessmentConfiguration().getNoRsultMessage();
	}
	
	public void setPostNoResultMessage(String noRsultMessage){
		getPostAssessmentConfiguration().setNoRsultMessage(noRsultMessage);
	}
	public String getPostNoResultMessage(){
		return getPostAssessmentConfiguration().getNoRsultMessage();
	}
	
	public void setQuizNoResultMessage(String noRsultMessage){
		getQuizAssessmentConfiguration().setNoRsultMessage(noRsultMessage);
	}
	public String getQuizNoResultMessage(){
		return getQuizAssessmentConfiguration().getNoRsultMessage();
	}

	/**
	 * @return the createdBy
	 */
	public Long getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the lastUpdatedBy
	 */
	public Long getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	/**
	 * @param lastUpdatedBy the lastUpdatedBy to set
	 */
	public void setLastUpdatedBy(Long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	/**
	 * @return the lastUpdatedDate
	 */
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	/**
	 * @param lastUpdatedDate the lastUpdatedDate to set
	 */
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	
	
	public void setEnablePreAssessmentMaximumNoAttempt(
			Boolean enableAssessmentMaximumNoAttempt) {
		getPreAssessmentConfiguration().setEnableAssessmentMaximumNoAttempt(enableAssessmentMaximumNoAttempt);
	}
	
	public Boolean isEnablePreAssessmentMaximumNoAttempt() {
		return getPreAssessmentConfiguration().getEnableAssessmentMaximumNoAttempt();
	}
	
	public void setEnableQuizMaximumNoAttempt(
			Boolean enableAssessmentMaximumNoAttempt) {
		getQuizAssessmentConfiguration().setEnableAssessmentMaximumNoAttempt(enableAssessmentMaximumNoAttempt);
	}
	
	public Boolean isEnableQuizMaximumNoAttempt() {
		return getQuizAssessmentConfiguration().getEnableAssessmentMaximumNoAttempt();
	}
	
	public void setEnablePostAssessmentMaximumNoAttempt(
			Boolean enableAssessmentMaximumNoAttempt) {
		getPostAssessmentConfiguration().setEnableAssessmentMaximumNoAttempt(enableAssessmentMaximumNoAttempt);
	}
	
	public Boolean isEnablePostAssessmentMaximumNoAttempt() {
		return getPostAssessmentConfiguration().getEnableAssessmentMaximumNoAttempt();
	}

	/**
	 * @return the mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified
	 */
	public Boolean isMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified() {
		return mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified;
	}

	/**
	 * @param mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified the mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified to set
	 */
	public void setMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified(
			Boolean mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified) {
		this.mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified = mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified;
	}

	/**
	 * @return the mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration
	 */
	public Integer getMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration() {
		return mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration;
	}

	/**
	 * @param mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration the mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration to set
	 */
	public void setMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration(
			Integer mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration) {
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
	public Boolean isSpecialQuestionnaireSpecified() {
		return specialQuestionnaireSpecified;
	}

	/**
	 * @param specialQuestionnaireSpecified the specialQuestionnaireSpecified to set
	 */
	public void setSpecialQuestionnaireSpecified(
			Boolean specialQuestionnaireSpecified) {
		this.specialQuestionnaireSpecified = specialQuestionnaireSpecified;
	}

	/**
	 * @return the mustCompleteSpecialQuestionnaire
	 */
	public Boolean isMustCompleteSpecialQuestionnaire() {
		return mustCompleteSpecialQuestionnaire;
	}

	/**
	 * @param mustCompleteSpecialQuestionnaire the mustCompleteSpecialQuestionnaire to set
	 */
	public void setMustCompleteSpecialQuestionnaire(
			Boolean mustCompleteSpecialQuestionnaire) {
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
	public Boolean isRequireProctorValidation() {
		return requireProctorValidation;
	}

	/**
	 * @param requiredProctorValidation the requiredProctorValidation to set
	 */
	public void setRequireProctorValidation(Boolean requireProctorValidation) {
		this.requireProctorValidation = requireProctorValidation;
	}
	
	
	public Boolean isPreAllowPauseResumeAssessment() {
		return getPreAssessmentConfiguration().getAllowPauseResumeAssessment();
	}

	public void setPreAllowPauseResumeAssessment(
			Boolean preAllowPauseResumeAssessment) {
		getPreAssessmentConfiguration().setAllowPauseResumeAssessment(preAllowPauseResumeAssessment);
	}

	public Boolean isPostAllowPauseResumeAssessment() {
		return getPostAssessmentConfiguration().getAllowPauseResumeAssessment();
	}

	public void setPostAllowPauseResumeAssessment(
			Boolean postAllowPauseResumeAssessment) {
		getPostAssessmentConfiguration().setAllowPauseResumeAssessment(postAllowPauseResumeAssessment);
	}

	public Boolean isQuizAllowPauseResumeAssessment() {
		return getQuizAssessmentConfiguration().getAllowPauseResumeAssessment();
	}

	public void setQuizAllowPauseResumeAssessment(
			Boolean quizAllowPauseResumeAssessment) {
		getQuizAssessmentConfiguration().setAllowPauseResumeAssessment(quizAllowPauseResumeAssessment);
	}

	public List<AssessmentConfiguration> getAssessmentConfigurations() {
		return assessmentConfigurations;
	}

	public void setAssessmentConfigurations(
			List<AssessmentConfiguration> assessmentConfigurations) {
		this.assessmentConfigurations = assessmentConfigurations;
	}

	public Boolean isRequiredAnsi() {
		return requiredAnsi;
	}

	public void setRequiredAnsi(Boolean requiredAnsi) {
		this.requiredAnsi = requiredAnsi;
	}

	public Boolean isRequiredNyInsurance() {
		return requiredNyInsurance;
	}

	public void setRequiredNyInsurance(Boolean requiredNyInsurance) {
		this.requiredNyInsurance = requiredNyInsurance;
	}

	public Boolean isRequireLearnerValidation() {
		return requireLearnerValidation;
	}

	public void setRequireLearnerValidation(Boolean requireLearnerValidation) {
		this.requireLearnerValidation = requireLearnerValidation;
	}

	public Boolean isCaRealEstateCE() {
		return caRealEstateCE;
	}

	public void setCaRealEstateCE(Boolean caRealEstateCE) {
		this.caRealEstateCE = caRealEstateCE;
	}

	public void setAllowFaceook(Boolean allowFaceook) {
		if(allowFaceook==null){
			this.allowFaceook=Boolean.FALSE;
		}else{
			this.allowFaceook = allowFaceook;
		}
	}

	public Boolean getShowStandardIntroduction() {
		if(showStandardIntroduction==null){
			showStandardIntroduction=Boolean.FALSE;
		}
		return showStandardIntroduction;
	}

	public Boolean getShowOrientationScenes() {
		if(showOrientationScenes==null){
			showOrientationScenes=Boolean.FALSE;
		}
		return showOrientationScenes;
	}

	public Boolean getShowEndOfCourseScene() {
		if(showEndOfCourseScene==null){
			showEndOfCourseScene=Boolean.FALSE;
		}
		return showEndOfCourseScene;
	}

	public Boolean getShowContent() {
		if(showContent==null){
			showContent=Boolean.FALSE;
		}
		return showContent;
	}

	public Boolean getEnforceTimedOutlineAllScenes() {
		if(enforceTimedOutlineAllScenes==null){
			enforceTimedOutlineAllScenes=Boolean.TRUE;
		}
		return enforceTimedOutlineAllScenes;
	}

	public Boolean getAllowUserToReviewCourseAfterCompletion() {
		if(allowUserToReviewCourseAfterCompletion==null){
			allowUserToReviewCourseAfterCompletion=Boolean.FALSE;
		}
		return allowUserToReviewCourseAfterCompletion;
	}

	public Boolean getMustCompleteCourseEvaluation() {
		if(mustCompleteCourseEvaluation==null){
			mustCompleteCourseEvaluation=Boolean.FALSE;
		}
		return mustCompleteCourseEvaluation;
	}

	public Boolean getSuggestedCourse() {
		if(suggestedCourse==null){
			suggestedCourse=Boolean.FALSE;
		}
		return suggestedCourse;
	}

	public Boolean getAllowFaceook() {
		if(allowFaceook==null){
			allowFaceook=Boolean.FALSE;
		}
		return allowFaceook;
	}

	public Boolean getMustAttemptPostAssessment() {
		if(mustAttemptPostAssessment==null){
			mustAttemptPostAssessment=Boolean.FALSE;
		}
		return mustAttemptPostAssessment;
	}

	public Boolean getMustDemonstratePostAssessmentMastery() {
		if(mustDemonstratePostAssessmentMastery==null){
			mustDemonstratePostAssessmentMastery=Boolean.FALSE;
		}
		return mustDemonstratePostAssessmentMastery;
	}

	public Boolean getMustDemonstratePreAssessmentMastery() {
		if(mustDemonstratePreAssessmentMastery==null){
			mustDemonstratePreAssessmentMastery=Boolean.FALSE;
		}
		return mustDemonstratePreAssessmentMastery;
	}

	public Boolean getMustDemonstrateQuizMastery() {
		if(mustDemonstrateQuizMastery==null){
			mustDemonstrateQuizMastery=Boolean.FALSE;
		}
		return mustDemonstrateQuizMastery;
	}

	public Boolean getMustCompleteAnySurveys() {
		if(mustCompleteAnySurveys==null){
			mustCompleteAnySurveys=Boolean.FALSE;
		}
		return mustCompleteAnySurveys;
	}

	public Boolean getMustViewEverySceneInTheCourse() {
		if(mustViewEverySceneInTheCourse==null){
			mustViewEverySceneInTheCourse=Boolean.FALSE;
		}
		return mustViewEverySceneInTheCourse;
	}

	public Boolean getCanOnlyBeCompleteAfterNumberOfCourseLaunches() {
		if(canOnlyBeCompleteAfterNumberOfCourseLaunches==null){
			canOnlyBeCompleteAfterNumberOfCourseLaunches=Boolean.FALSE;
		}
		return canOnlyBeCompleteAfterNumberOfCourseLaunches;
	}

	public Boolean getCompleteWithinTimePeriod() {
		if(completeWithinTimePeriod==null){
			completeWithinTimePeriod=Boolean.FALSE;
		}
		return completeWithinTimePeriod;
	}

	public Boolean getCompleteWithinDaysOfRegistration() {
		if(completeWithinDaysOfRegistration==null){
			completeWithinDaysOfRegistration=Boolean.FALSE;
		}
		return completeWithinDaysOfRegistration;
	}

	public Boolean getCourseEvaluationSpecified() {
		if(courseEvaluationSpecified==null){
			courseEvaluationSpecified=Boolean.FALSE;
		}
		return courseEvaluationSpecified;
	}

	public Boolean getCourseStrictlyEnforcePolicyToBeUsed() {
		return courseStrictlyEnforcePolicyToBeUsed;
	}

	public Boolean getPlayerStrictlyEnforcePolicyToBeUsed() {
		return playerStrictlyEnforcePolicyToBeUsed;
	}

	public Boolean getCertificateEnabled() {
		return certificateEnabled;
	}

	public Boolean getRespondToCourseEvaluation() {
		if(respondToCourseEvaluation==null){
			respondToCourseEvaluation=Boolean.FALSE;
		}
		return respondToCourseEvaluation;
	}

	public Boolean getAcknowledgeEnabled() {
		return acknowledgeEnabled;
	}

	public Boolean getEnableIdentityValidation() {
		if(enableIdentityValidation==null){
			enableIdentityValidation=Boolean.FALSE;
		}
		return enableIdentityValidation;
	}

	public Boolean getValidationStrictlyEnforcePolicyToBeUsed() {
		return validationStrictlyEnforcePolicyToBeUsed;
	}

	public Boolean getSeattimeenabled() {
		return seattimeenabled;
	}

	public Boolean getMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified() {
		return mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified;
	}

	public Boolean getSpecialQuestionnaireSpecified() {
		return specialQuestionnaireSpecified;
	}

	public Boolean getMustCompleteSpecialQuestionnaire() {
		return mustCompleteSpecialQuestionnaire;
	}

	public Boolean getRequireProctorValidation() {
		return requireProctorValidation;
	}

	public Boolean getRequiredAnsi() {
		return requiredAnsi;
	}

	public Boolean getRequiredNyInsurance() {
		return requiredNyInsurance;
	}

	public Boolean getRequireLearnerValidation() {
		return requireLearnerValidation;
	}

	public Boolean getCaRealEstateCE() {
		return caRealEstateCE;
	}

	public Boolean isMustMasterAllLessonActivities() {
		return mustMasterAllLessonActivities;
	}

	public void setMustMasterAllLessonActivities(
			Boolean mustMasterAllLessonActivities) {
		this.mustMasterAllLessonActivities = mustMasterAllLessonActivities;
	}

	public Boolean isRequireDefineUniqueQuestionValidation() {
		return requireDefineUniqueQuestionValidation;
	}

	public void setRequireDefineUniqueQuestionValidation(
			Boolean requireDefineUniqueQuestionValidation) {
		this.requireDefineUniqueQuestionValidation = requireDefineUniqueQuestionValidation;
	}

	public Boolean isRequireSelfRegistrationProctor() {
		return requireSelfRegistrationProctor;
	}

	public void setRequireSelfRegistrationProctor(
			Boolean requireSelfRegistrationProctor) {
		this.requireSelfRegistrationProctor = requireSelfRegistrationProctor;
	}

	public Boolean isRequireSmartProfileValidation() {
		return requireSmartProfileValidation;
	}

	public void setRequireSmartProfileValidation(
			Boolean requireSmartProfileValidation) {
		this.requireSmartProfileValidation = requireSmartProfileValidation;
	}

	public List<UniqueQuestionsVO> getLstUniqueQuestionsVO() {
		return lstUniqueQuestionsVO;
	}

	public void setLstUniqueQuestionsVO(List<UniqueQuestionsVO> lstUniqueQuestionsVO) {
		this.lstUniqueQuestionsVO = lstUniqueQuestionsVO;
	}
	
	
}
