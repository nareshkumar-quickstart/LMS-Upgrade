package com.softech.vu360.lms.web.controller.accreditation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.Author;
import com.softech.vu360.lms.model.Certificate;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.CourseConfiguration;
import com.softech.vu360.lms.model.CourseConfigurationTemplate;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.ValidationQuestion;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.AuthorService;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.vo.UniqueQuestionsVO;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.accreditation.CourseConfigForm;
import com.softech.vu360.lms.web.controller.validator.Accreditation.AddCourseConfigValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.webservice.client.LCMSClientWS;
import com.softech.vu360.util.CourseTemplateSort;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.HtmlEncoder;

/**
 * @author PG
 * 
 */
public class ManageAndEditCourseConfigController extends VU360BaseMultiActionController {

	private static final Logger log = Logger.getLogger(ManageAndEditInstructorController.class.getName());
	
	private final String PROCTOR_VALIDATOR_ANSI = "ansi";
	private final String PROCTOR_VALIDATOR_NY_INSURANCE = "nyInsurance";
	private final String PROCTOR_VALIDATOR_TREC = "TREC";
	private final String ONLINE_PROCTORING = "ONLINEPROCTORING";
	private final String REMOTE_PROCTORING = "REMOTEPROCTORING";
	
	private AccreditationService accreditationService;
	private CourseAndCourseGroupService courseAndCourseGroupService;
	@Inject
	private AuthorService authorService;

	private String searchCourseConfigTemplate = null;
	private String editCourseConfigTemplate = null;

	private LCMSClientWS lcmsClientWS = null;

	public ManageAndEditCourseConfigController() {
		super();
	}

	public ManageAndEditCourseConfigController(Object delegate) {
		super(delegate);
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, 
			HttpServletRequest request, HttpServletResponse response ) throws Exception {
		request.setAttribute("newPage","true");
		return new ModelAndView(searchCourseConfigTemplate);
	}

	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
		if( command instanceof CourseConfigForm ) {
			CourseConfigForm form = (CourseConfigForm)command;
			List<UniqueQuestionsVO> lstUniqueQuestionsVO = new ArrayList<>();

			if( methodName.equals("editCourseConfig")){
				int qId = 0;
				CourseConfiguration courseConfig = null;
				Long id = Long.parseLong(request.getParameter("templateId"));
				if( id != null ) 
				{
                    //Course Configuration can be null if someone associated(incorrectly) an asset as certifcate with 
                    //assetType != Certificate, from LCMS.
                    //TODO: Not sure if this is the right way to do it though.
					
					//TODO: Replace two queries with a single one that gives the Course Config Template whether Certificate is associated or not
					courseConfig = accreditationService.getCourseConfigurationByTemplateIdWithAssociatedCertificate(id);
					if(courseConfig == null)
						courseConfig = accreditationService.getCourseConfigurationByTemplateId(id, true);
					
                    if( null != courseConfig){
                        CourseConfiguration courseConfigNew = null;
                        Long courseConfigId = courseConfig.getId();
                        courseConfigNew = accreditationService.loadForUpdateCourseConfiguration(courseConfigId);
                        form.setCourseConfiguration(courseConfigNew);
                    }else{
                    	
                        //form.getCourseConfiguration().setCompletionCertificate(null);
                        
                    	form.setCompletionCertificate(null);
                        form.setCertificateName("");
                        
                    }
				} else {
					courseConfig = form.getCourseConfiguration();
				}
			

				if(form != null && form.getCourseConfiguration()!= null){
					if(StringUtils.isBlank(form.getCourseConfiguration().getAcknowledgeText())  == false ){
						form.setAcknowledgeText(form.getCourseConfiguration().getAcknowledgeText().replaceAll("\"","'"));			
						form.setAcknowledgeText( form.getCourseConfiguration().getAcknowledgeText().replaceAll("\r\n", "") );
						form.setAcknowledgeText( form.getCourseConfiguration().getAcknowledgeText().replaceAll("\n", "<br>") );
						form.setAcknowledgeText( form.getCourseConfiguration().getAcknowledgeText().replaceAll("\r", "<br>") );
					}
					
					List<ValidationQuestion>  lsValidationQuestions = accreditationService.getUniqueValidationQuestionByCourseConfigurationId(form.getCourseConfiguration().getId());

					//List<ValidationQuestion>  lsValidationQuestions = accreditationService.getUniqueValidationQuestion(form.getCourseConfiguration().getId());

					form.setLstUniqueQuestionsVO(null);
					if(lsValidationQuestions != null && !lsValidationQuestions.isEmpty()){
						for(ValidationQuestion validationQuestion:lsValidationQuestions){
							UniqueQuestionsVO uniqueQuestionsVO = new UniqueQuestionsVO();
							uniqueQuestionsVO.setQuestion(validationQuestion.getQuestion());
							uniqueQuestionsVO.setQuestionId(qId++);
							uniqueQuestionsVO.setId(validationQuestion.getId().toString());
							if(validationQuestion.getQuestionType().equals("True False")){
								uniqueQuestionsVO.setQuestionType("Qtype_1");
							}
							else if(validationQuestion.getQuestionType().equals("Text Entry")){
								uniqueQuestionsVO.setQuestionType("Qtype_0");
							}
							lstUniqueQuestionsVO.add(uniqueQuestionsVO);
						}
						form.setLstUniqueQuestionsVO(lstUniqueQuestionsVO);
					}	
				
				//form.setAllowNumberofAttemptsToAnswerCorrectly(form.getCourseConfiguration().getAllowNumberofAttemptsToAnswerCorrectly()+"");
				form.setAllowSecondsToAnswerEachQuestion(form.getCourseConfiguration().getAllowSecondsToAnswerEachQuestion()+"");
				form.setDaysOfRegistraion(form.getCourseConfiguration().getDaysOfRegistraion()+"");
				form.setIdleTimeAmount(form.getCourseConfiguration().getIdleTimeAmount()+"");
				form.setMinutesAfterFirstCourseLaunch(form.getCourseConfiguration().getMinutesAfterFirstCourseLaunch()+"");
				form.setNumberOfCoursesLaunch(form.getCourseConfiguration().getNumberOfCoursesLaunch()+"");
				form.setActiontotakeuponidletimeout(form.getCourseConfiguration().getActiontotakeuponidletimeout());
				
				//Validation
				form.setRequireIdentityValidationEverySeconds(form.getCourseConfiguration().getRequireIdentityValidationEverySeconds()+"");
				form.setUnitOfDaysOfRegistration(form.getCourseConfiguration().getUnitOfDaysOfRegistration());
				form.setValidationNoMissedQuestionsAllowed(form.getCourseConfiguration().getValidationNoMissedQuestionsAllowed()+"");
				form.setValidationTimeBetweenQuestion(form.getCourseConfiguration().getValidationTimeBetweenQuestion()+"");
				form.setEnableIdentityValidation(form.getCourseConfiguration().getEnableIdentityValidation());
				form.setEnableSmartProfileValidation(form.getCourseConfiguration().isRequireSmartProfileValidation());
				form.setEnableDefineUniqueQuestionValidation(form.getCourseConfiguration().isRequireDefineUniqueQuestionValidation());
				form.setNumberOfValidationQuestions(form.getCourseConfiguration().getNumberOfValidationQuestions()+"");
				
				// course Policy
				form.setShowStandardIntroduction(form.getCourseConfiguration().getShowStandardIntroduction());
				
				form.setShowOrientationScenes(form.getCourseConfiguration().getShowOrientationScenes());
				form.setShowEndOfCourseScene(form.getCourseConfiguration().getShowEndOfCourseScene());
				form.setShowContent(form.getCourseConfiguration().getShowContent());
				form.setEnforceTimedOutlineAllScenes(form.getCourseConfiguration().getEnforceTimedOutlineAllScenes());
				form.setAllowUserToReviewCourseAfterCompletion(form.getCourseConfiguration().getAllowUserToReviewCourseAfterCompletion());
				form.setActiontotakeuponidletimeout(form.getCourseConfiguration().getActiontotakeuponidletimeout());
				form.setPlayerCourseFlow(form.getCourseConfiguration().getPlayerCourseFlow());
				form.setRespondToCourseEvaluation(form.getCourseConfiguration().getRespondToCourseEvaluation());
				
				Boolean isCertificateEnabled = form.getCourseConfiguration().isCertificateEnabled();
				if (isCertificateEnabled != null) {
					form.setCertificateEnabled(isCertificateEnabled);
				} else {
					form.setCertificateEnabled(false);
				}
				
                if(form.isCertificateEnabled()){
                	form.setCompletionCertificate(form.getCourseConfiguration().getCompletionCertificate());
                    if( null != form.getCompletionCertificate()){
                    	form.setCertificateName(form.getCompletionCertificate().getName());
                	}else{
                		form.setCertificateName("");
                	}
				}else{
					form.setCertificateName("");
				}
                
//				AddCourseConfigWizardController.initCourseConfigFormWithCertificate(request, form, accreditationService);
						
				
				// Course Completion
                log.debug("Rate Course in editmode" + form.getCourseConfiguration().getRateCourse());
                form.setRateCourse(form.getCourseConfiguration().getRateCourse());
                form.setSuggestedCourse(form.getCourseConfiguration().getSuggestedCourse());
                form.setMustCompleteCourseEvaluation(form.getCourseConfiguration().getMustCompleteCourseEvaluation());
				/*
                if((form.getCourseConfiguration().isSuggestedCourse()) || (!form.getCourseConfiguration().isSuggestedCourse()))
                 {
                    form.setSuggestedCourse(form.getCourseConfiguration().isSuggestedCourse());
                 }
                 if((form.getCourseConfiguration().isRateCourse()) || (!form.getCourseConfiguration().isRateCourse()))
                 {
                     form.setRateCourse(form.getCourseConfiguration().isRateCourse());
                 }
                 */
				form.setDisplayCourseEvaluation(form.getCourseConfiguration().getDisplayCourseEvaluation());
				form.setMustAttemptPostAssessment(form.getCourseConfiguration().getMustAttemptPostAssessment());
				form.setMustDemonstratePostAssessmentMastery(form.getCourseConfiguration().getMustDemonstratePostAssessmentMastery());
				form.setMustDemonstratePreAssessmentMastery(form.getCourseConfiguration().getMustDemonstratePreAssessmentMastery());
				form.setMustDemonstrateQuizAssessmentMastery(form.getCourseConfiguration().getMustDemonstrateQuizMastery());
				form.setMustMasterAllLessonActivities(form.getCourseConfiguration().isMustMasterAllLessonActivities());
				form.setMustCompleteAnySurveys(form.getCourseConfiguration().getMustCompleteAnySurveys());
				form.setMustViewEverySceneInTheCourse(form.getCourseConfiguration().getMustViewEverySceneInTheCourse());
				form.setCanOnlyBeCompleteAfterNumberOfCourseLaunches(form.getCourseConfiguration().getCanOnlyBeCompleteAfterNumberOfCourseLaunches());
				form.setCompleteWithinTimePeriod(form.getCourseConfiguration().getCompleteWithinTimePeriod());
				form.setCompleteWithinDaysOfRegistration(form.getCourseConfiguration().getCompleteWithinDaysOfRegistration());
				form.setUnitOfTimeToComplete(form.getCourseConfiguration().getUnitOfTimeToComplete());
				
				Boolean isPlayerStrictlyEnforcePolicyToBeUsed = form.getCourseConfiguration().isPlayerStrictlyEnforcePolicyToBeUsed();
				if (isPlayerStrictlyEnforcePolicyToBeUsed != null) {
					form.setPlayerStrictlyEnforcePolicyToBeUsed(isPlayerStrictlyEnforcePolicyToBeUsed);
				} else {
					form.setPlayerStrictlyEnforcePolicyToBeUsed(false);
				}
				
				form.setCourseEvaluationSpecified(form.getCourseConfiguration().getCourseEvaluationSpecified());
				
				//for pre/post/quiz
				//PRE
				
				
					form.setPreAssessmentEnabled(form.getCourseConfiguration().isPreassessmentEnabled());
					
					form.setPreAssessmentMaximumnoattemptEnabled(form.getCourseConfiguration().isEnablePreAssessmentMaximumNoAttempt());
					form.setQuizAssessmentMaximumnoattemptEnabled(form.getCourseConfiguration().isEnableQuizMaximumNoAttempt());
					form.setPostAssessmentMaximumnoattemptEnabled(form.getCourseConfiguration().isEnablePostAssessmentMaximumNoAttempt());
					
					form.setPreassessmentEnforcemaximumtimelimit(form.getCourseConfiguration().getPreassessmentMaximumtimelimit()+"");
					
					if(form.getCourseConfiguration().getPreassessmentMaximumtimelimit() > 0)
						form.setPreassessmentEnforcemaximumtimelimitCB(true);
					else
						form.setPreassessmentEnforcemaximumtimelimitCB(false);
					
//					form.setPreassessmentEnforcemaximumtimelimitCB(form.getCourseConfiguration().isPreEnforeceMaximumtimelimit());
					form.setPreassessmentMasteryscore(form.getCourseConfiguration().getPreassessmentMasteryscore()+"");
					form.setPreassessmentMaximumnoattempt(form.getCourseConfiguration().getPreassessmentMaximumnoattempt()+"");
					form.setPreActionToTakeAfterMaximumAttemptsReached(
                                                form.getCourseConfiguration().getPreAssessmentConfiguration().
                                                getActionToTakeAfterMaximumAttemptsReached());
					form.setPreassessmentNoquestion(form.getCourseConfiguration().getPreassessmentNoquestion()+"");
					form.setPreGradingBehavior(form.getCourseConfiguration().getPreAssessmentConfiguration().getGradingBehavior());
					form.setPreMaximumTimeLimitMinutes(form.getCourseConfiguration().getPreAssessmentConfiguration().getMaximumTimeLimitMinutes());
					form.setPreMinimumSeatTimeBeforeAssessmentStart(form.getCourseConfiguration().getPreAssessmentConfiguration().getMinimumSeatTimeBeforeAssessmentStart());
					form.setPreScoringType(form.getCourseConfiguration().getPreAssessmentConfiguration().getScoringType());
					form.setPreAllowQuestionSkipping(form.getCourseConfiguration().getPreAssessmentConfiguration().getAllowQuestionSkipping());
					form.setPreEnableAssessmentProctoring(form.getCourseConfiguration().getPreAssessmentConfiguration().getEnableAssessmentProctoring());
					form.setPreEnableContentRemediation(form.getCourseConfiguration().getPreAssessmentConfiguration().getEnableContentRemediation());
					form.setPreEnableRestrictiveAssessmentMode(form.getCourseConfiguration().getPreAssessmentConfiguration().getEnableRestrictiveAssessmentMode());
					form.setPreEnableWeightedScoring(form.getCourseConfiguration().getPreAssessmentConfiguration().getEnableWeightedScoring());
					form.setPreRandomizeAnswers(form.getCourseConfiguration().getPreAssessmentConfiguration().getRandomizeAnswers());
					form.setPreRandomizeQuestions(form.getCourseConfiguration().getPreAssessmentConfiguration().getRandomizeQuestions());
					form.setPreShowQuestionAnswerReview(form.getCourseConfiguration().getPreAssessmentConfiguration().getShowQuestionAnswerReview());
					form.setPreShowQuestionLevelResults(form.getCourseConfiguration().getPreAssessmentConfiguration().getShowQuestionLevelResults());
					form.setPreUseUniqueQuestionsOnRetake(form.getCourseConfiguration().getPreAssessmentConfiguration().getUseUniqueQuestionsOnRetake());
					form.setPreAdvancedQuestionSelectionType(form.getCourseConfiguration().getPreAssessmentConfiguration().getAdvancedQuestionSelectionType());
					form.setPreNoResultMessage(form.getCourseConfiguration().getPreNoResultMessage());
					form.setPreAllowPauseResumeAssessment(form.getCourseConfiguration().isPreAllowPauseResumeAssessment());
					form.setPreLockoutAssessmentActiveWindow(form.getCourseConfiguration().isPreLockoutAssessmentActiveWindow());
				//POST
				
					form.setPostAssessmentEnabled(form.getCourseConfiguration().isPostassessmentEnabled());
					
					form.setPostassessmentEnforcemaximumtimelimit(form.getCourseConfiguration().getPostassessmentMaximumtimelimit()+"");
					
					if(form.getCourseConfiguration().getPostassessmentMaximumtimelimit() > 0)
						form.setPostassessmentEnforcemaximumtimelimitCB(true);
					else
						form.setPostassessmentEnforcemaximumtimelimitCB(false);
					
					form.setPostassessmentMasteryscore(form.getCourseConfiguration().getPostassessmentMasteryscore()+"");
					form.setPostassessmentMaximumnoattempt(form.getCourseConfiguration().getPostassessmentMaximumnoattempt()+"");
                                        form.setPostActionToTakeAfterMaximumAttemptsReached(
                                                form.getCourseConfiguration().getPostAssessmentConfiguration().
                                                getActionToTakeAfterMaximumAttemptsReached());
                                        
					form.setPostassessmentNoquestion(form.getCourseConfiguration().getPostassessmentNoquestion()+"");
					form.setPostGradingBehavior(form.getCourseConfiguration().getPostAssessmentConfiguration().getGradingBehavior());
					form.setPostMaximumTimeLimitMinutes(form.getCourseConfiguration().getPostAssessmentConfiguration().getMaximumTimeLimitMinutes());
					form.setPostMinimumSeatTimeBeforeAssessmentStart(form.getCourseConfiguration().getPostAssessmentConfiguration().getMinimumSeatTimeBeforeAssessmentStart()+"");
					form.setPostScoringType(form.getCourseConfiguration().getPostAssessmentConfiguration().getScoringType());
					form.setPostAllowQuestionSkipping(form.getCourseConfiguration().getPostAssessmentConfiguration().getAllowQuestionSkipping());
					form.setPostEnableAssessmentProctoring(form.getCourseConfiguration().getPostAssessmentConfiguration().getEnableAssessmentProctoring());
					form.setPostEnableContentRemediation(form.getCourseConfiguration().getPostAssessmentConfiguration().getEnableContentRemediation());
					form.setPostEnableRestrictiveAssessmentMode(form.getCourseConfiguration().getPostAssessmentConfiguration().getEnableRestrictiveAssessmentMode());
					form.setPostEnableWeightedScoring(form.getCourseConfiguration().getPostAssessmentConfiguration().getEnableWeightedScoring());
					form.setPostRandomizeAnswers(form.getCourseConfiguration().getPostAssessmentConfiguration().getRandomizeAnswers());
					form.setPostRandomizeQuestions(form.getCourseConfiguration().getPostAssessmentConfiguration().getRandomizeQuestions());
					form.setPostShowQuestionAnswerReview(form.getCourseConfiguration().getPostAssessmentConfiguration().getShowQuestionAnswerReview());
					form.setPostShowQuestionLevelResults(form.getCourseConfiguration().getPostAssessmentConfiguration().getShowQuestionLevelResults());
					form.setPostUseUniqueQuestionsOnRetake(form.getCourseConfiguration().getPostAssessmentConfiguration().getUseUniqueQuestionsOnRetake());
					form.setPostAdvancedQuestionSelectionType(form.getCourseConfiguration().getPostAssessmentConfiguration().getAdvancedQuestionSelectionType());
					form.setPostNoResultMessage(form.getCourseConfiguration().getPostNoResultMessage());
					form.setPostAllowPauseResumeAssessment(form.getCourseConfiguration().isPostAllowPauseResumeAssessment());
					form.setPostLockoutAssessmentActiveWindow(form.getCourseConfiguration().isPostLockoutAssessmentActiveWindow());
					//form.setPostEnableviewAssessmentResults(form.getCourseConfiguration().getPostAssessmentConfiguration().isViewassessmentresultsEnabled());
					form.setPostEnableviewAssessmentResults(form.getCourseConfiguration().isViewassessmentresultsEnabled());
					form.setEnableDefineUniqueQuestionValidation(form.getCourseConfiguration().isRequireDefineUniqueQuestionValidation());
					form.setEnableSelfRegistrationProctor(form.isEnableSelfRegistrationProctor());
					form.setEnableSmartProfileValidation(form.isEnableSmartProfileValidation());
										
				//QUIZ
					
					form.setQuizAssessmentEnabled(form.getCourseConfiguration().isQuizEnabled());
					
					form.setQuizAssessmentEnforcemaximumtimelimit(form.getCourseConfiguration().getQuizMaximumtimelimit()+"");
					
					if(form.getCourseConfiguration().getQuizMaximumtimelimit() > 0)
						form.setQuizAssessmentEnforcemaximumtimelimitCB(true);
					else
						form.setQuizAssessmentEnforcemaximumtimelimitCB(false);
					
					
					form.setQuizAssessmentMasteryscore(form.getCourseConfiguration().getQuizMasteryscore()+"");
					form.setQuizAssessmentMaximumnoattempt(form.getCourseConfiguration().getQuizMaximumnoattempt()+"");
                                        form.setQuizAssessmentActionToTakeAfterMaximumAttemptsReached(
                                                form.getCourseConfiguration().getQuizAssessmentConfiguration().
                                                getActionToTakeAfterMaximumAttemptsReached());
                                        
					form.setQuizAssessmentNoquestion(form.getCourseConfiguration().getQuizNoquestion()+"");
					form.setQuizAssessmentGradingBehavior(form.getCourseConfiguration().getQuizAssessmentConfiguration().getGradingBehavior());
					form.setQuizAssessmentMaximumTimeLimitMinutes(form.getCourseConfiguration().getQuizAssessmentConfiguration().getMaximumTimeLimitMinutes());
					form.setQuizAssessmentMinimumSeatTimeBeforeAssessmentStart(form.getCourseConfiguration().getQuizAssessmentConfiguration().getMinimumSeatTimeBeforeAssessmentStart());
					form.setQuizAssessmentScoringType(form.getCourseConfiguration().getQuizAssessmentConfiguration().getScoringType());
					form.setQuizAssessmentAllowQuestionSkipping(form.getCourseConfiguration().getQuizAssessmentConfiguration().getAllowQuestionSkipping());
					form.setQuizAssessmentEnableAssessmentProctoring(form.getCourseConfiguration().getQuizAssessmentConfiguration().getEnableAssessmentProctoring());
					form.setQuizAssessmentEnableContentRemediation(form.getCourseConfiguration().getQuizAssessmentConfiguration().getEnableContentRemediation());
					form.setQuizAssessmentEnableRestrictiveAssessmentMode(form.getCourseConfiguration().getQuizAssessmentConfiguration().getEnableRestrictiveAssessmentMode());
					form.setQuizAssessmentEnableWeightedScoring(form.getCourseConfiguration().getQuizAssessmentConfiguration().getEnableWeightedScoring());
					form.setQuizAssessmentRandomizeAnswers(form.getCourseConfiguration().getQuizAssessmentConfiguration().getRandomizeAnswers());
					form.setQuizAssessmentRandomizeQuestions(form.getCourseConfiguration().getQuizAssessmentConfiguration().getRandomizeQuestions());
					form.setQuizAssessmentShowQuestionAnswerReview(form.getCourseConfiguration().getQuizAssessmentConfiguration().getShowQuestionAnswerReview());
					form.setQuizAssessmentShowQuestionLevelResults(form.getCourseConfiguration().getQuizAssessmentConfiguration().getShowQuestionLevelResults());
					form.setQuizAssessmentUseUniqueQuestionsOnRetake(form.getCourseConfiguration().getQuizAssessmentConfiguration().getUseUniqueQuestionsOnRetake());
					form.setQuizAssessmentAdvancedQuestionSelectionType(form.getCourseConfiguration().getQuizAssessmentConfiguration().getAdvancedQuestionSelectionType());
					form.setQuizAssessmentNoResultMessage(form.getCourseConfiguration().getQuizNoResultMessage());
					form.setQuizAllowPauseResumeAssessment(form.getCourseConfiguration().isQuizAllowPauseResumeAssessment());
					form.setQuizLockoutAssessmentActiveWindow(form.getCourseConfiguration().isQuizLockoutAssessmentActiveWindow());
				//end pre/post/quiz
				
					//Seat Time
					
					//Max Seat Time Enforcement
					Boolean isSeattimeenabled = form.getCourseConfiguration().isSeattimeenabled();
					if (isSeattimeenabled != null) {
						form.setSeattimeenabled(isSeattimeenabled);
					} else {
						form.setSeattimeenabled(false);
					}
					
					form.setSeattimeinhour("" + form.getCourseConfiguration().getSeattimeinhour());
					form.setSeattimeinmin("" + form.getCourseConfiguration().getSeattimeinmin());
					form.setMessageseattimecourselaunch(form.getCourseConfiguration().getMessageseattimecourselaunch());
					form.setMessageseattimeexceeds(form.getCourseConfiguration().getMessageseattimeexceeds());
					
					
					//Min Seat Time Enforcement
					
					form.setPostMinimumSeatTimeBeforeAssessmentStart(form.getCourseConfiguration().getPostMinimumSeatTimeBeforeAssessmentStart()+"");
					form.setPostMinimumSeatTimeBeforeAssessmentStartUnits(form.getCourseConfiguration().getPostMinimumSeatTimeBeforeAssessmentStartUnits());
					form.setDisplaySeatTimeTextMessage(form.getCourseConfiguration().isDisplaySeatTimeTextMessage());
					form.setLockPostAssessmentBeforeSeatTime(form.getCourseConfiguration().isLockPostAssessmentBeforeSeatTime());
							
					//acknowledgement
					
					form.setEndOfCourseInstructions(form.getCourseConfiguration().getEndOfCourseInstructions());
					form.setAcknowledgeEnabled(form.getCourseConfiguration().getAcknowledgeEnabled());
					form.setAcknowledgeText(form.getCourseConfiguration().getAcknowledgeText());
					
					Integer mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration = form.getCourseConfiguration().getMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration();
					if (mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration != null) {
						form.setMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration(mustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration);
					}
					
					Boolean isMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified = form.getCourseConfiguration().isMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified();
					if (isMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified != null) {
						form.setMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified(isMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified);
					}
					
					form.setMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationUnit(form.getCourseConfiguration().getMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationUnit());
					
					Boolean isSpecialQuestionnaireSpecified = form.getCourseConfiguration().isSpecialQuestionnaireSpecified();
					if (isSpecialQuestionnaireSpecified != null) {
						form.setSpecialQuestionnaireSpecified(isSpecialQuestionnaireSpecified);
					}
					
					form.setDisplaySpecialQuestionnaire(form.getCourseConfiguration().getDisplaySpecialQuestionnaire());
					
					Boolean isMustCompleteSpecialQuestionnaire = form.getCourseConfiguration().isMustCompleteSpecialQuestionnaire();
					if (isMustCompleteSpecialQuestionnaire != null) {
						form.setMustCompleteSpecialQuestionnaire(isMustCompleteSpecialQuestionnaire);
					}
					
					Boolean isRequireProctorValidation = form.getCourseConfiguration().isRequireProctorValidation();
					if (isRequireProctorValidation != null) {
						form.setRequireProctorValidation(isRequireProctorValidation);
						if(isRequireProctorValidation){
							Boolean isRequiredAnsi = form.getCourseConfiguration().isRequiredAnsi();
							if (isRequiredAnsi != null) {
								if(isRequiredAnsi) {
									form.setProctorValidatorName(PROCTOR_VALIDATOR_ANSI);
								} else if(form.getCourseConfiguration().isRequiredNyInsurance()!=null){
									Boolean isRequiredNyInsurance = form.getCourseConfiguration().isRequiredNyInsurance();
									if (isRequiredNyInsurance != null) {
										if (isRequiredNyInsurance) {
											form.setProctorValidatorName(PROCTOR_VALIDATOR_NY_INSURANCE);
										}
									}
								} else if(form.getCourseConfiguration().isRequireSelfRegistrationProctor()!=null){
									Boolean isRequireSelfRegistrationProctor = form.getCourseConfiguration().isRequireSelfRegistrationProctor();
									if (isRequireSelfRegistrationProctor != null) {
										if (isRequireSelfRegistrationProctor) {
											form.setProctorValidatorName(PROCTOR_VALIDATOR_TREC);
										}
									}
								}
							}		
						}
					}
		
					Boolean isRequireLearnerValidation = form.getCourseConfiguration().isRequireLearnerValidation();
					if (isRequireLearnerValidation != null) {
						form.setRequireLearnerValidation(isRequireLearnerValidation);
					}
					
					Boolean isCaRealEstateCE = form.getCourseConfiguration().isCaRealEstateCE();
					if (isCaRealEstateCE != null) {
						form.setCaRealEstateCE(isCaRealEstateCE);
					}
				}
			}
		}	
	}

	public ModelAndView searchCourseConfig( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
//			session = request.getSession();
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().
			getPrincipal();
			ContentOwner contentOwner = null;
			if( loggedInUser.getRegulatoryAnalyst() != null )

				contentOwner = accreditationService.findContentOwnerByRegulatoryAnalyst(
						loggedInUser.getRegulatoryAnalyst());
			
			Map<Object, Object> context = new HashMap<Object, Object>();
			Map<String, String> PagerAttributeMap = new HashMap <String, String>();
			String name = (request.getParameter("templateName") == null) ? "" : request.getParameter("templateName");
			String date = (request.getParameter("templateDate") == null) ? "" : request.getParameter("templateDate");
			Date dt = null;
			try {
				if(!date.isEmpty()){
					DateFormat formatter= new SimpleDateFormat("MM/dd/yyyy");   
					dt=formatter.parse(date);
				}else{
					dt=null;
				}
			} catch (Exception e) {
				log.debug("Exception: "+ e);
			}
			//List<CourseConfigurationTemplate> configTemplate = accreditationService.findTemplates(name,dt);
			List<CourseConfigurationTemplate> configTemplate = accreditationService.findTemplates(name,dt, contentOwner.getId());

			name = HtmlEncoder.escapeHtmlFull(name).toString();
			date = HtmlEncoder.escapeHtmlFull(date).toString();

			String pageIndex = request.getParameter("pageIndex");
			if( StringUtils.isBlank(pageIndex)) pageIndex = "0";

			PagerAttributeMap.put("pageIndex",pageIndex);
			if( configTemplate.size() <= 10 ) {
				PagerAttributeMap.put("pageIndex", "0");
			}

			// for sorting...
			String sortColumnIndex = request.getParameter("sortColumnIndex");
//			if( StringUtils.isBlank(sortColumnIndex) && session.getAttribute("sortColumnIndex") != null )
//				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String sortDirection = request.getParameter("sortDirection");
//			if( StringUtils.isBlank(sortDirection)&& session.getAttribute("sortDirection") != null )
//				sortDirection = session.getAttribute("sortDirection").toString();
			String showAll = request.getParameter("showAll");
			if ( showAll == null ) showAll = "false";
			if ( showAll.isEmpty() ) showAll = "true";
			context.put("showAll", showAll);

			if( StringUtils.isNotBlank(sortColumnIndex) && StringUtils.isNotBlank(sortDirection) ) {

				request.setAttribute("PagerAttributeMap", PagerAttributeMap);

				// sorting against Template name
				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						CourseTemplateSort courseTemplateSort = new CourseTemplateSort();
						courseTemplateSort.setSortBy("name");
						courseTemplateSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(configTemplate,courseTemplateSort);
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 0);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 0);
					} else {
						CourseTemplateSort courseTemplateSort = new CourseTemplateSort();
						courseTemplateSort.setSortBy("name");
						courseTemplateSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(configTemplate,courseTemplateSort);
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 0);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 0);
					}
					// sorting against date
				}else if( sortColumnIndex.equalsIgnoreCase("1") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						CourseTemplateSort courseTemplateSort = new CourseTemplateSort();
						courseTemplateSort.setSortBy("date");
						courseTemplateSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(configTemplate,courseTemplateSort);
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 1);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 1);
					} else {
						CourseTemplateSort courseTemplateSort = new CourseTemplateSort();
						courseTemplateSort.setSortBy("date");
						courseTemplateSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(configTemplate,courseTemplateSort);
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 1);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 1);
					}
				}
			}
			context.put("templates", configTemplate);
			context.put("name", name);
			context.put("date", date);
			
			return new ModelAndView(searchCourseConfigTemplate, "context", context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(searchCourseConfigTemplate);
	}
	
	/**
	 * @added by Dyutiman
	 * method to delete a template.
	 * it is setting the active field 'false'. hence the template is not hard deleted from db.
	 * 
	 */
	public ModelAndView deleteCourseConfig( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {

		try {
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().
			getPrincipal();
			ContentOwner contentOwner = null;
			String errorMessage=null;
			if( loggedInUser.getRegulatoryAnalyst() != null )

				contentOwner = accreditationService.findContentOwnerByRegulatoryAnalyst(
						loggedInUser.getRegulatoryAnalyst());
			Map<Object, Object> context = new HashMap<Object, Object>();
			String name = (request.getParameter("templateName") == null) ? "" : request.getParameter("templateName");
			String date = (request.getParameter("templateDate") == null) ? "" : request.getParameter("templateDate");
			Date dt;
			if( !date.isEmpty() ) {
				DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");   
				dt = formatter.parse(date);
			} else {
				dt = null;
			}
			if( request.getParameterValues("row") != null ) {
				long[] id = new long[request.getParameterValues("row").length];
				String []checkID = request.getParameterValues("row");
				for ( int i = 0 ; i < id.length ; i++ ) {
					id[i] = Long.valueOf(checkID[i]);
					
                                        boolean deleteAllowed = true;
                                        List<CourseApproval> courseApprovals = accreditationService.getCourseApprovalByCourseConfigurationTemplate(id[i]);
                                        for (CourseApproval courseApproval : courseApprovals) {
                                            if(courseApproval.isDeleted()){
                                                continue;
                                            }else{
                                                deleteAllowed = false;
                                                break;
                                            }
                                        }
                                        
					if(deleteAllowed && courseAndCourseGroupService.getCoursesByCourseConfigurationTemplate(id[i]).isEmpty()){
						CourseConfigurationTemplate temp = accreditationService.loadForUpdateTemplate(id[i]);
						temp.setActive(false);
						accreditationService.saveCourseConfigurationTemplate(temp);
					}
					else{
						errorMessage="error.courseConfigTemplate.deleteError";
					}
				}
			}
			List<CourseConfigurationTemplate> configTemplate = new ArrayList<CourseConfigurationTemplate>();//accreditationService.findTemplates(name,dt, contentOwner.getId());
			context.put("templates", configTemplate);
			context.put("errorMessage",errorMessage);
			return new ModelAndView(searchCourseConfigTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(searchCourseConfigTemplate);
	}

	public ModelAndView editCourseConfig( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			CourseConfigForm form = (CourseConfigForm)command;
			/*return from select certificate - start*/
            if( StringUtils.isNotEmpty(request.getParameter("certificateId"))){
                String certificateId = request.getParameter("certificateId");
                if( NumberUtils.isNumber(certificateId)){
                    Certificate certificate = accreditationService.getCertificateById(Long.parseLong(certificateId));
                    form.getCourseConfiguration().setCompletionCertificate(certificate);
                    accreditationService.saveCourseConfiguration(form.getCourseConfiguration());
                    form.setCertificateName(certificate.getName());
                    form.setCompletionCertificate(certificate);
                    form.setCertificateEnabled(true);                  
               }
            }
            /*return from select certificate - end*/
		
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editCourseConfigTemplate);
	}

	private void saveCourseConfiguration(Object command, HttpServletRequest request)throws Exception{
		
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		VU360User VU360User =  VU360UserAuthenticationDetails.getCurrentUser();
		Author author = authorService.getAuthorByVU360UserID(VU360User.getId());
		
		final int MAX_PRE_POST_QUIZ_ATTEMPT = 9999999; 
		
		ArrayList<String> lstUniqueQuestions = null;
		List<UniqueQuestionsVO> uniqueQuestionList = new ArrayList<UniqueQuestionsVO>();
	
		CourseConfigForm form = (CourseConfigForm)command;
		CourseConfiguration mycourseConfiguration = form.getCourseConfiguration();
		
		List<UniqueQuestionsVO> lstUQV = mycourseConfiguration.getLstUniqueQuestionsVO();
		UniqueQuestionsVO uniqueQuesVO = null;
		List<UniqueQuestionsVO> lstUniquesQueVO = new ArrayList<>();
		 
		mycourseConfiguration.getCourseConfigTemplate().setLastUpdatedDate(new Date());
		
		ArrayList<String> parameterNames = new ArrayList<String>();
		 Enumeration enumeration = request.getParameterNames();
		    while (enumeration.hasMoreElements()) {
		        String parameterName = (String) enumeration.nextElement();
		       
		        if(parameterName.contains("uquestionName_")){
		        	String id = parameterName.substring(parameterName.indexOf('_')+1);
		        	uniqueQuesVO =  getUniqueQuestion(Integer.parseInt(id),request);
		        	lstUniquesQueVO.add(uniqueQuesVO);
		        }
		        
		    }
		 
		 if(lstUniquesQueVO!=null && !lstUniquesQueVO.isEmpty()){
			 form.getCourseConfiguration().setLstUniqueQuestionsVO(lstUniquesQueVO);
			 form.setLstUniqueQuestionsVO(lstUniquesQueVO);
		 }
		
/*
		    if(request.getParameterValues("data") != null){
	    	   lstUniqueQuestions = new ArrayList<String>(Arrays.asList(request.getParameterValues("data")));
	    	   String question = null;
	    	   String questionType = null;
	    	   UniqueQuestionsVO uniqueQuestionsVO = null;
	    	   for(String  uniqueQuestion:lstUniqueQuestions){
	    		   if(!uniqueQuestion.contains("Qtype_")){
	    			   uniqueQuestionsVO = new UniqueQuestionsVO();
	    			   question = uniqueQuestion;
	    			   uniqueQuestionsVO.setQuestion(question);
	    		}
	    		   else{
	    			   questionType = uniqueQuestion;
	    			   uniqueQuestionsVO.setQuestionType(questionType);
	    			   uniqueQuestionList.add(uniqueQuestionsVO);
	    			   uniqueQuestionsVO = null;
	    		   }
	    	    }
	    	    form.getCourseConfiguration().setLstUniqueQuestionsVO(uniqueQuestionList);
	    	   }
	    */	   
		
		if(StringUtils.isNotBlank(form.getIdleTimeAmount()+"")){
			mycourseConfiguration.setIdleTimeAmount(Integer.parseInt(form.getIdleTimeAmount()));
		}
		if(StringUtils.isNotBlank(form.getNumberOfCoursesLaunch()+"")){
			mycourseConfiguration.setNumberOfCoursesLaunch(Integer.parseInt(form.getNumberOfCoursesLaunch()));
		}
		if(StringUtils.isNotBlank(form.getRequireIdentityValidationEverySeconds()+"")){
			mycourseConfiguration.setRequireIdentityValidationEverySeconds(Integer.parseInt(form.getRequireIdentityValidationEverySeconds()));
		}	
		if(StringUtils.isNotBlank(form.getMinutesAfterFirstCourseLaunch())){
			mycourseConfiguration.setMinutesAfterFirstCourseLaunch(Integer.parseInt(form.getMinutesAfterFirstCourseLaunch()));
		}
		if(StringUtils.isNotBlank(form.getPreassessmentMasteryscore())){
			mycourseConfiguration.setPreassessmentMasteryscore(Integer.parseInt(form.getPreassessmentMasteryscore()));
		}
		if(!form.isPreAssessmentMaximumNoAttemptCB() ||(Integer.parseInt(form.getPreassessmentMaximumnoattempt()) > MAX_PRE_POST_QUIZ_ATTEMPT)){											
			form.setPreassessmentMaximumnoattempt("1");
			form.setPreActionToTakeAfterMaximumAttemptsReached("");
		}
		
		if(StringUtils.isNotBlank(form.getPreassessmentMaximumnoattempt())){
			mycourseConfiguration.setPreassessmentMaximumnoattempt(Integer.parseInt(form.getPreassessmentMaximumnoattempt()));
		}
		
		if(form.getPreActionToTakeAfterMaximumAttemptsReached()!=null){
			mycourseConfiguration.setPreActionToTakeAfterMaximumAttemptsReached(form.getPreActionToTakeAfterMaximumAttemptsReached());
		}
		
		if(StringUtils.isNotBlank(form.getPreassessmentNoquestion())){
			mycourseConfiguration.setPreassessmentNoquestion(Integer.parseInt(form.getPreassessmentNoquestion()));
		}
		
		if(StringUtils.isNotBlank(form.getPostassessmentMasteryscore())){
			mycourseConfiguration.setPostassessmentMasteryscore(Integer.parseInt(form.getPostassessmentMasteryscore()));
		}
		
		if(!form.isPostAssessmentMaximumNoAttemptCB() ||(Integer.parseInt(form.getPostassessmentMaximumnoattempt()) > MAX_PRE_POST_QUIZ_ATTEMPT)){											
					form.setPostassessmentMaximumnoattempt("1");
					form.setPostActionToTakeAfterMaximumAttemptsReached("");
		}
		
		if(StringUtils.isNotBlank(form.getPostassessmentMaximumnoattempt())){
			mycourseConfiguration.setPostassessmentMaximumnoattempt(Integer.parseInt(form.getPostassessmentMaximumnoattempt()));
		}
		if(form.getPostActionToTakeAfterMaximumAttemptsReached()!=null){
			mycourseConfiguration.setPostActionToTakeAfterMaximumAttemptsReached(form.getPostActionToTakeAfterMaximumAttemptsReached());
		}
		
		if(StringUtils.isNotBlank(form.getPostassessmentNoquestion())){
			mycourseConfiguration.setPostassessmentNoquestion(Integer.parseInt(form.getPostassessmentNoquestion()));
		}
		if(StringUtils.isNotBlank(form.getQuizAssessmentMasteryscore())){
			mycourseConfiguration.setQuizMasteryscore(Integer.parseInt(form.getQuizAssessmentMasteryscore()));
		}
		
		if(!form.isQuizAssessmentMaximumNoAttemptCB() ||(Integer.parseInt(form.getQuizAssessmentMaximumnoattempt()) > MAX_PRE_POST_QUIZ_ATTEMPT)){											
					form.setQuizAssessmentMaximumnoattempt("1");
					form.setQuizAssessmentActionToTakeAfterMaximumAttemptsReached("");
		}
		if(StringUtils.isNotBlank(form.getQuizAssessmentMaximumnoattempt())){
			mycourseConfiguration.setQuizMaximumnoattempt(Integer.parseInt(form.getQuizAssessmentMaximumnoattempt()));
		}
		if(form.getQuizAssessmentActionToTakeAfterMaximumAttemptsReached()!=null){
			mycourseConfiguration.setQuizActionToTakeAfterMaximumAttemptsReached(form.getQuizAssessmentActionToTakeAfterMaximumAttemptsReached());
		}
		
		if(StringUtils.isNotBlank(form.getQuizAssessmentNoquestion())){
			mycourseConfiguration.setQuizNoquestion(Integer.parseInt(form.getQuizAssessmentNoquestion()));
		}
		
		
		
		
		
		//Validation
		if(StringUtils.isNotBlank(form.getValidationNoMissedQuestionsAllowed())){
			mycourseConfiguration.setValidationNoMissedQuestionsAllowed(Integer.parseInt(form.getValidationNoMissedQuestionsAllowed()));
		}
		if(StringUtils.isNotBlank(form.getValidationTimeBetweenQuestion())){
			mycourseConfiguration.setValidationTimeBetweenQuestion(Integer.parseInt(form.getValidationTimeBetweenQuestion()));
		}
//		if(StringUtils.isNotBlank(form.getAllowNumberofAttemptsToAnswerCorrectly())){
//			mycourseConfiguration.setAllowNumberofAttemptsToAnswerCorrectly(Integer.parseInt(form.getAllowNumberofAttemptsToAnswerCorrectly()));
//		}
		if(StringUtils.isNotBlank(form.getAllowSecondsToAnswerEachQuestion())){
			mycourseConfiguration.setAllowSecondsToAnswerEachQuestion(Integer.parseInt(form.getAllowSecondsToAnswerEachQuestion()));
		}
		if(!(form.getNumberOfValidationQuestions().isEmpty() || form.getNumberOfValidationQuestions()==null)){
			mycourseConfiguration.setNumberOfValidationQuestions(FormUtil.parseNumber(form.getNumberOfValidationQuestions()));
		}
		
		// course Policy
		
		mycourseConfiguration.setShowStandardIntroduction(form.isShowStandardIntroduction());
		
		mycourseConfiguration.setShowOrientationScenes(form.isShowOrientationScenes());
		mycourseConfiguration.setShowEndOfCourseScene(form.isShowEndOfCourseScene());
		mycourseConfiguration.setShowContent(form.isShowContent());
		mycourseConfiguration.setEnforceTimedOutlineAllScenes(form.isEnforceTimedOutlineAllScenes());
		mycourseConfiguration.setAllowUserToReviewCourseAfterCompletion(form.isAllowUserToReviewCourseAfterCompletion());
		
		mycourseConfiguration.setUnitOfDaysOfRegistration(form.getUnitOfDaysOfRegistration());
		mycourseConfiguration.setActiontotakeuponidletimeout(form.getActiontotakeuponidletimeout());
		mycourseConfiguration.setPlayerCourseFlow(form.getPlayerCourseFlow());
		mycourseConfiguration.setRespondToCourseEvaluation(form.isRespondToCourseEvaluation());
		
		mycourseConfiguration.setCertificateEnabled(form.isCertificateEnabled());
		
		if( form.isCertificateEnabled()){
            mycourseConfiguration.setCompletionCertificate(form.getCompletionCertificate());
        }else{
        	mycourseConfiguration.setCompletionCertificate(null);
        }
        
                    
                    
        // Course Completion

		mycourseConfiguration.setMustCompleteCourseEvaluation(form.isMustCompleteCourseEvaluation());
		log.debug("Rate Course in editmode" + form.isRateCourse());
		mycourseConfiguration.setRateCourse(form.isRateCourse());
		mycourseConfiguration.setSuggestedCourse(form.isSuggestedCourse());
		/*
		if((form.getCourseConfiguration().isSuggestedCourse()) || (!form.getCourseConfiguration().isSuggestedCourse()))
        {
            mycourseConfiguration.setSuggestedCourse(form.getCourseConfiguration().isSuggestedCourse());
        }
        if((form.getCourseConfiguration().isRateCourse()) || (!form.getCourseConfiguration().isRateCourse()))
        {
            mycourseConfiguration.setRateCourse(form.getCourseConfiguration().isRateCourse());
        }
        */
		mycourseConfiguration.setDisplayCourseEvaluation(form.getDisplayCourseEvaluation());
		mycourseConfiguration.setMustAttemptPostAssessment(form.isMustAttemptPostAssessment());
		mycourseConfiguration.setMustDemonstratePostAssessmentMastery(form.isMustDemonstratePostAssessmentMastery());
		mycourseConfiguration.setMustDemonstratePreAssessmentMastery(form.isMustDemonstratePreAssessmentMastery());
		mycourseConfiguration.setMustDemonstrateQuizMastery(form.isMustDemonstrateQuizAssessmentMastery());
		mycourseConfiguration.setMustMasterAllLessonActivities(form.isMustMasterAllLessonActivities());
		mycourseConfiguration.setDaysOfRegistraion(Integer.parseInt(form.getDaysOfRegistraion()));
		mycourseConfiguration.setMustCompleteAnySurveys(form.isMustCompleteAnySurveys());
		mycourseConfiguration.setMustViewEverySceneInTheCourse(form.isMustViewEverySceneInTheCourse());
		mycourseConfiguration.setCanOnlyBeCompleteAfterNumberOfCourseLaunches(form.isCanOnlyBeCompleteAfterNumberOfCourseLaunches());
		mycourseConfiguration.setCompleteWithinTimePeriod(form.isCompleteWithinTimePeriod());
		mycourseConfiguration.setCompleteWithinDaysOfRegistration(form.isCompleteWithinDaysOfRegistration());
		mycourseConfiguration.setUnitOfTimeToComplete(form.getUnitOfTimeToComplete());
		
		mycourseConfiguration.setPlayerStrictlyEnforcePolicyToBeUsed(form.isPlayerStrictlyEnforcePolicyToBeUsed());
		mycourseConfiguration.setValidationStrictlyEnforcePolicyToBeUsed(form.isPlayerStrictlyEnforcePolicyToBeUsed());

		//LMS-12023 CHANGE START
		if(mycourseConfiguration.getId() == null){
			mycourseConfiguration.setCreatedBy(loggedInUser.getId());
			mycourseConfiguration.setCreatedDate(new Date());
			mycourseConfiguration.setLastUpdatedBy(null);
			mycourseConfiguration.setLastUpdatedDate(null);
		}
		else{
			mycourseConfiguration.setLastUpdatedBy(loggedInUser.getId());
			mycourseConfiguration.setLastUpdatedDate(new Date());	
		}
		
		
		//LMS-12023 CHANGE END

		mycourseConfiguration.setCourseEvaluationSpecified(form.isCourseEvaluationSpecified());
		
		
		
		
		
		//for pre/post/quiz
		//PRE
		
		mycourseConfiguration.setPreassessmentEnabled(form.isPreAssessmentEnabled());
		mycourseConfiguration.setPreAllowPauseResumeAssessment(form.isPreAllowPauseResumeAssessment());
		mycourseConfiguration.setPreLockoutAssessmentActiveWindow(form.isPreLockoutAssessmentActiveWindow());
		
		mycourseConfiguration.setQuizAllowPauseResumeAssessment(form.isQuizAllowPauseResumeAssessment());
		mycourseConfiguration.setQuizLockoutAssessmentActiveWindow(form.isQuizLockoutAssessmentActiveWindow());
		mycourseConfiguration.setPostAllowPauseResumeAssessment(form.isPostAllowPauseResumeAssessment());
		
		mycourseConfiguration.setPostLockoutAssessmentActiveWindow(form.isPostLockoutAssessmentActiveWindow());
		mycourseConfiguration.setViewassessmentresultsEnabled(form.isPostEnableviewAssessmentResults());

		
		if(!(form.getPreActionToTakeAfterMaximumAttemptsReached().isEmpty() || form.getPreActionToTakeAfterMaximumAttemptsReached()==null)){
			mycourseConfiguration.setPreActionToTakeAfterMaximumAttemptsReached(form.getPreActionToTakeAfterMaximumAttemptsReached());
		}
		
		mycourseConfiguration.setPreEnforeceMaximumtimelimit(form.isPreassessmentEnforcemaximumtimelimitCB());
		if(!form.isPreassessmentEnforcemaximumtimelimitCB())
		{
			mycourseConfiguration.setPreassessmentMaximumtimelimit(0);
		}
		else
		{
			mycourseConfiguration.setPreassessmentMaximumtimelimit(FormUtil.parseNumber(form.getPreassessmentEnforcemaximumtimelimit()));
		}
		
		
		if(!(form.getPreassessmentMasteryscore().isEmpty() || form.getPreassessmentMasteryscore()==null)){
			mycourseConfiguration.setPreassessmentMasteryscore(FormUtil.parseNumber(form.getPreassessmentMasteryscore()));
		}
		if(!(form.getPreassessmentMaximumnoattempt().isEmpty() || form.getPreassessmentMaximumnoattempt()==null)){
			mycourseConfiguration.setPreassessmentMaximumnoattempt(FormUtil.parseNumber(form.getPreassessmentMaximumnoattempt()));
		}
		if(!(form.getPreassessmentNoquestion().isEmpty() || form.getPreassessmentNoquestion()==null)){
			mycourseConfiguration.setPreassessmentNoquestion(FormUtil.parseNumber(form.getPreassessmentNoquestion()));
		}
		if(!(form.getPreGradingBehavior()==null || form.getPreGradingBehavior().isEmpty())){
			mycourseConfiguration.setPreGradingBehavior(form.getPreGradingBehavior());
		}
		
		
			mycourseConfiguration.setPreMinimumSeatTimeBeforeAssessmentStart(form.getPreMinimumSeatTimeBeforeAssessmentStart());

		if(!(form.getPreScoringType().isEmpty() || form.getPreScoringType()==null)){
			mycourseConfiguration.setPreScoringType(form.getPreScoringType());
		}
		
		boolean isCheckedPreScoring = true;
		if(!(form.getPreScoringType().isEmpty() || form.getPreScoringType()==null)){
			if(form.getPreScoringType().equals("No Results"))
			{
				mycourseConfiguration.setEnablePreAssessmentMaximumNoAttempt(true);
				mycourseConfiguration.setPreassessmentMaximumnoattempt(1);
				isCheckedPreScoring = false;
			}
		}
		
		if(isCheckedPreScoring)
		{
			mycourseConfiguration.setEnablePreAssessmentMaximumNoAttempt(form.isPreAssessmentMaximumnoattemptEnabled());
		}
		if(!form.isPreAdvancedQuestionSelectionTypeCB()){											
			form.setPreAdvancedQuestionSelectionType("");				
		}
		mycourseConfiguration.setPreAdvancedQuestionSelectionType(form.getPreAdvancedQuestionSelectionType());
		mycourseConfiguration.setPreAllowQuestionSkipping(form.isPreAllowQuestionSkipping());
		mycourseConfiguration.setPreEnableAssessmentProctoring(form.isPreEnableAssessmentProctoring());
		mycourseConfiguration.setPreEnableContentRemediation(form.isPreEnableContentRemediation());
		mycourseConfiguration.setPreEnableRestrictiveAssessmentMode(form.isPreEnableRestrictiveAssessmentMode());
		mycourseConfiguration.setPreEnableWeightedScoring(form.isPreEnableWeightedScoring());
		mycourseConfiguration.setPreRandomizeAnswers( form.isPreRandomizeAnswers());
		mycourseConfiguration.setPreRandomizeQuestions(form.isPreRandomizeQuestions());
		mycourseConfiguration.setPreShowQuestionAnswerReview(form.isPreShowQuestionAnswerReview());
		mycourseConfiguration.setPreShowQuestionLevelResults(form.isPreShowQuestionLevelResults());
		mycourseConfiguration.setPreUseUniqueQuestionsOnRetake( form.isPreUseUniqueQuestionsOnRetake());
		mycourseConfiguration.setPreNoResultMessage(form.getPreNoResultMessage());
		                              
		//POST
		
		mycourseConfiguration.setPostAssessmentEnabled(form.isPostAssessmentEnabled());
		
		if(!(form.getPostActionToTakeAfterMaximumAttemptsReached().isEmpty() || form.getPostActionToTakeAfterMaximumAttemptsReached()==null)){
			mycourseConfiguration.setPostActionToTakeAfterMaximumAttemptsReached(form.getPostActionToTakeAfterMaximumAttemptsReached());
		}

		mycourseConfiguration.setPostEnforeceMaximumtimelimit(form.getPostassessmentEnforcemaximumtimelimitCB());
		if(!form.getPostassessmentEnforcemaximumtimelimitCB())
		{
			mycourseConfiguration.setPostassessmentMaximumtimelimit(0);
		}
		else
		{
			mycourseConfiguration.setPostassessmentMaximumtimelimit(FormUtil.parseNumber(form.getPostassessmentEnforcemaximumtimelimit()));
		}
		
		
		if(!(form.getPostassessmentMasteryscore().isEmpty() || form.getPostassessmentMasteryscore()==null)){
			mycourseConfiguration.setPostassessmentMasteryscore(FormUtil.parseNumber(form.getPostassessmentMasteryscore()));
		}
		if(!(form.getPostassessmentMaximumnoattempt().isEmpty() || form.getPostassessmentMaximumnoattempt()==null)){
			mycourseConfiguration.setPostassessmentMaximumnoattempt(FormUtil.parseNumber(form.getPostassessmentMaximumnoattempt()));
		}
		if(!(form.getPostassessmentNoquestion().isEmpty() || form.getPostassessmentNoquestion()==null)){
			mycourseConfiguration.setPostassessmentNoquestion(FormUtil.parseNumber(form.getPostassessmentNoquestion()));
		}
		if(!(form.getPostGradingBehavior()==null || form.getPostGradingBehavior().isEmpty())){
			mycourseConfiguration.setPostGradingBehavior(form.getPostGradingBehavior());
		}

			mycourseConfiguration.setPostMinimumSeatTimeBeforeAssessmentStart(FormUtil.parseNumber(form.getPostMinimumSeatTimeBeforeAssessmentStart()));


		if(!(form.getPostScoringType().isEmpty() || form.getPostScoringType()==null)){
			mycourseConfiguration.setPostScoringType(form.getPostScoringType());
		}
		
		boolean isCheckedPostScoring = true;
		if(!(form.getPostScoringType().isEmpty() || form.getPostScoringType()==null)){
			if(form.getPostScoringType().equals("No Results"))
			{
				mycourseConfiguration.setEnablePostAssessmentMaximumNoAttempt(true);
				mycourseConfiguration.setPostassessmentMaximumnoattempt(1);
				isCheckedPostScoring = false;
			}
		}
		
		if(isCheckedPostScoring)
		{
			mycourseConfiguration.setEnablePostAssessmentMaximumNoAttempt(form.isPostAssessmentMaximumnoattemptEnabled());
		}
		
		if(!form.isPostAdvancedQuestionSelectionTypeCB()){											
			form.setPostAdvancedQuestionSelectionType("");				
		}
		mycourseConfiguration.setPostAdvancedQuestionSelectionType(form.getPostAdvancedQuestionSelectionType());
		
		mycourseConfiguration.setPostAllowQuestionSkipping(form.isPostAllowQuestionSkipping());
		mycourseConfiguration.setPostEnableAssessmentProctoring(form.isPostEnableAssessmentProctoring());
		mycourseConfiguration.setPostEnableContentRemediation(form.isPostEnableContentRemediation());
		mycourseConfiguration.setPostEnableRestrictiveAssessmentMode(form.isPostEnableRestrictiveAssessmentMode());
		mycourseConfiguration.setPostEnableWeightedScoring(form.isPostEnableWeightedScoring());
		mycourseConfiguration.setPostRandomizeAnswers( form.isPostRandomizeAnswers());
		mycourseConfiguration.setPostRandomizeQuestions(form.isPostRandomizeQuestions());
		mycourseConfiguration.setPostShowQuestionAnswerReview(form.isPostShowQuestionAnswerReview());
		mycourseConfiguration.setPostShowQuestionLevelResults(form.isPostShowQuestionLevelResults());
		mycourseConfiguration.setPostUseUniqueQuestionsOnRetake( form.isPostUseUniqueQuestionsOnRetake());
		mycourseConfiguration.setPostNoResultMessage(form.getPostNoResultMessage());
		
		//QUIZ
		
		mycourseConfiguration.setQuizAssessmentEnabled(form.isQuizAssessmentEnabled());
		
		if(!(form.getQuizAssessmentActionToTakeAfterMaximumAttemptsReached().isEmpty() || form.getQuizAssessmentActionToTakeAfterMaximumAttemptsReached()==null)){
			mycourseConfiguration.setQuizActionToTakeAfterMaximumAttemptsReached(form.getQuizAssessmentActionToTakeAfterMaximumAttemptsReached());
		}

		mycourseConfiguration.setQuizEnforeceMaximumtimelimit(form.getQuizAssessmentEnforcemaximumtimelimitCB());
		if(!form.getQuizAssessmentEnforcemaximumtimelimitCB())
		{
			mycourseConfiguration.setQuizMaximumtimelimit(0);
		}
		else
		{
			mycourseConfiguration.setQuizMaximumtimelimit(FormUtil.parseNumber(form.getQuizAssessmentEnforcemaximumtimelimit()));
		}
		
		
		if(!(form.getQuizAssessmentMasteryscore().isEmpty() || form.getQuizAssessmentMasteryscore()==null)){
			mycourseConfiguration.setQuizMasteryscore(FormUtil.parseNumber(form.getQuizAssessmentMasteryscore()));
		}
		if(!(form.getQuizAssessmentMaximumnoattempt().isEmpty() || form.getQuizAssessmentMaximumnoattempt()==null)){
			mycourseConfiguration.setQuizMaximumnoattempt(FormUtil.parseNumber(form.getQuizAssessmentMaximumnoattempt()));
		}
		if(!(form.getQuizAssessmentNoquestion().isEmpty() || form.getQuizAssessmentNoquestion()==null)){
			mycourseConfiguration.setQuizNoquestion(FormUtil.parseNumber(form.getQuizAssessmentNoquestion()));
		}
		if(!(form.getQuizAssessmentGradingBehavior()==null || form.getQuizAssessmentGradingBehavior().isEmpty())){
			mycourseConfiguration.setQuizGradingBehavior(form.getQuizAssessmentGradingBehavior());
		}

			mycourseConfiguration.setQuizMinimumSeatTimeBeforeAssessmentStart(form.getQuizAssessmentMinimumSeatTimeBeforeAssessmentStart());


		if(!(form.getQuizAssessmentScoringType().isEmpty() || form.getQuizAssessmentScoringType()==null)){
			mycourseConfiguration.setQuizScoringType(form.getQuizAssessmentScoringType());
		}
		
		boolean isCheckedQuizScoring = true;
		if(!(form.getQuizAssessmentScoringType().isEmpty() || form.getQuizAssessmentScoringType()==null)){
			if(form.getQuizAssessmentScoringType().equals("No Results"))
			{
				mycourseConfiguration.setEnableQuizMaximumNoAttempt(true);
				mycourseConfiguration.setQuizMaximumnoattempt(1);
				isCheckedQuizScoring = false;
			}
		}
		
		if(isCheckedQuizScoring)
		{
			mycourseConfiguration.setEnablePreAssessmentMaximumNoAttempt(form.isPreAssessmentMaximumnoattemptEnabled());
		}
		if(!form.isQuizAssessmentAdvancedQuestionSelectionTypeCB()){											
			form.setQuizAssessmentAdvancedQuestionSelectionType("");				
		}
		mycourseConfiguration.setQuizAdvancedQuestionSelectionType(form.getQuizAssessmentAdvancedQuestionSelectionType());
		
		mycourseConfiguration.setQuizAllowQuestionSkipping(form.isQuizAssessmentAllowQuestionSkipping());
		mycourseConfiguration.setQuizEnableAssessmentProctoring(form.isQuizAssessmentEnableAssessmentProctoring());
		mycourseConfiguration.setQuizEnableContentRemediation(form.isQuizAssessmentEnableContentRemediation());
		mycourseConfiguration.setQuizEnableRestrictiveAssessmentMode(form.isQuizAssessmentEnableRestrictiveAssessmentMode());
		mycourseConfiguration.setQuizEnableWeightedScoring(form.isQuizAssessmentEnableWeightedScoring());
		mycourseConfiguration.setQuizRandomizeAnswers( form.isQuizAssessmentRandomizeAnswers());
		mycourseConfiguration.setQuizRandomizeQuestions(form.isQuizAssessmentRandomizeQuestions());
		mycourseConfiguration.setQuizShowQuestionAnswerReview(form.isQuizAssessmentShowQuestionAnswerReview());
		mycourseConfiguration.setQuizShowQuestionLevelResults(form.isQuizAssessmentShowQuestionLevelResults());
		mycourseConfiguration.setQuizUseUniqueQuestionsOnRetake( form.isQuizAssessmentUseUniqueQuestionsOnRetake());
		mycourseConfiguration.setQuizNoResultMessage(form.getQuizAssessmentNoResultMessage());
		
		
		
		mycourseConfiguration.setEnablePreAssessmentMaximumNoAttempt(form.isPreAssessmentMaximumnoattemptEnabled());
		mycourseConfiguration.setEnableQuizMaximumNoAttempt(form.isQuizAssessmentMaximumnoattemptEnabled());
		mycourseConfiguration.setEnablePostAssessmentMaximumNoAttempt(form.isPostAssessmentMaximumnoattemptEnabled());
			
		
		//end pre/post/quiz
		
		//Seat Time
		
		//Max Seat Time Enforcement
		mycourseConfiguration.setSeattimeenabled(form.isSeattimeenabled());
		mycourseConfiguration.setSeattimeinhour( Integer.valueOf(form.getSeattimeinhour()));
		mycourseConfiguration.setSeattimeinmin(Integer.valueOf(form.getSeattimeinmin()));
		mycourseConfiguration.setMessageseattimecourselaunch(form.getMessageseattimecourselaunch());
		mycourseConfiguration.setMessageseattimeexceeds(form.getMessageseattimeexceeds());
		
		
		//Min Seat Time Enforcement
		
		mycourseConfiguration.setPostMinimumSeatTimeBeforeAssessmentStart(FormUtil.parseNumber(form.getPostMinimumSeatTimeBeforeAssessmentStart()));
		mycourseConfiguration.setPostMinimumSeatTimeBeforeAssessmentStartUnits(form.getPostMinimumSeatTimeBeforeAssessmentStartUnits());
		mycourseConfiguration.setDisplaySeatTimeTextMessage(form.isDisplaySeatTimeTextMessage());
		mycourseConfiguration.setLockPostAssessmentBeforeSeatTime(form.isLockPostAssessmentBeforeSeatTime());
		
		//acknowledgement
		
		mycourseConfiguration.setEndOfCourseInstructions(form.getEndOfCourseInstructions());
		mycourseConfiguration.setAcknowledgeEnabled(form.isAcknowledgeEnabled());
		mycourseConfiguration.setAcknowledgeText(form.getAcknowledgeText());

		mycourseConfiguration.setMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration(form.getMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration());
		mycourseConfiguration.setMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified(form.isMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified());
		mycourseConfiguration.setMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationUnit(form.getMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationUnit());
		
		mycourseConfiguration.setSpecialQuestionnaireSpecified(form.isSpecialQuestionnaireSpecified());
		mycourseConfiguration.setDisplaySpecialQuestionnaire(form.getDisplaySpecialQuestionnaire());
		mycourseConfiguration.setMustCompleteSpecialQuestionnaire(form.isMustCompleteSpecialQuestionnaire());
		
		//Proctor and Learner Validation
		mycourseConfiguration.setRequireProctorValidation(form.isRequireProctorValidation());
			
		if(form.isRequireProctorValidation()){
			if(form.getProctorValidatorName().equalsIgnoreCase(PROCTOR_VALIDATOR_ANSI))
				mycourseConfiguration.setRequiredAnsi(true);
			else
				mycourseConfiguration.setRequiredAnsi(false);
			
			if(form.getProctorValidatorName().equalsIgnoreCase(PROCTOR_VALIDATOR_NY_INSURANCE))
				mycourseConfiguration.setRequiredNyInsurance(true);
			else
				mycourseConfiguration.setRequiredNyInsurance(false);
			
			if(form.getProctorValidatorName().equalsIgnoreCase(PROCTOR_VALIDATOR_TREC))
			    mycourseConfiguration.setRequireSelfRegistrationProctor(true);
			else
			    mycourseConfiguration.setRequireSelfRegistrationProctor(false);
			
			if(form.getProctorValidatorName().equalsIgnoreCase(ONLINE_PROCTORING))
			    mycourseConfiguration.setRequireOnlineProctoring(true);
			else
			    mycourseConfiguration.setRequireOnlineProctoring(false);
			
			if(form.getProctorValidatorName().equalsIgnoreCase(REMOTE_PROCTORING))
			    mycourseConfiguration.setRequireRemoteProctoring(true);
			else
			    mycourseConfiguration.setRequireRemoteProctoring(false);
		}
		else{
			mycourseConfiguration.setRequiredNyInsurance(false);
			mycourseConfiguration.setRequiredAnsi(false);
			mycourseConfiguration.setRequireSelfRegistrationProctor(false);
			mycourseConfiguration.setRequireOnlineProctoring(false);
			mycourseConfiguration.setRequireRemoteProctoring(false);
		}
		
		mycourseConfiguration.setRequireLearnerValidation(form.isRequireLearnerValidation());
		if(mycourseConfiguration.isRequireLearnerValidation()){
			mycourseConfiguration.setCaRealEstateCE(true);
		}else{
			mycourseConfiguration.setCaRealEstateCE(false);
		}
		
		mycourseConfiguration.setEnableIdentityValidation(form.isEnableIdentityValidation());
		if(form.isEnableIdentityValidation()) {
		   mycourseConfiguration.setRequireSmartProfileValidation(form.isEnableSmartProfileValidation());
		   mycourseConfiguration.setRequireDefineUniqueQuestionValidation(form.isEnableDefineUniqueQuestionValidation());
		}
		
		accreditationService.saveCourseConfiguration(mycourseConfiguration);
		
		if(mycourseConfiguration!=null && form.isEnableDefineUniqueQuestionValidation()){
			if(form.getCourseConfiguration().getLstUniqueQuestionsVO()!=null){
				
				for(UniqueQuestionsVO uniqueQuestionsVO :form.getCourseConfiguration().getLstUniqueQuestionsVO()){
					ValidationQuestion validationQuestion = new ValidationQuestion();
					validationQuestion.setQuestion(uniqueQuestionsVO.getQuestion());
					
					if(uniqueQuestionsVO.getQuestionType().equals("Qtype_0")){
					     validationQuestion.setQuestionType("Text Entry");
					}
					else if(uniqueQuestionsVO.getQuestionType().equals("Qtype_1")){
					     validationQuestion.setQuestionType("True False");
					}
                    validationQuestion.setModifiedBy(author);
					validationQuestion.setModifiedDate(new Date());
					
					if(StringUtils.isEmpty(uniqueQuestionsVO.getId())){
						validationQuestion.setLanguage(VU360User.getLanguage());
						validationQuestion.setIsActive(true);
						validationQuestion.setCourseConfiguration(mycourseConfiguration);
						validationQuestion.setCreatedBy(author);
						validationQuestion.setCreatedDate(new Date());
						accreditationService.saveValidationQuestion(validationQuestion);
						uniqueQuestionsVO.setId(validationQuestion.getId().toString());
						validationQuestion.setAnswerQuery("SELECT ANSWER AS ANSWERTEXT FROM dbo.VALIDATIONQUESTION AS VQ INNER JOIN dbo.LEARNERVALIDATIONANSWERS AS LVA ON VQ.ID = LVA.QUESTION_ID WHERE LVA.LEARNER_ID= @LEARNER_ID AND VQ.ID = " + validationQuestion.getId());
						accreditationService.saveValidationQuestion(validationQuestion);
					}
					else if(!StringUtils.isEmpty(uniqueQuestionsVO.getId())){ 	
					ValidationQuestion updatedvalidationQuestion = accreditationService.loadForUpdateValidationQuestion(Long.parseLong(uniqueQuestionsVO.getId()));		
					
					if(updatedvalidationQuestion != null){
						updatedvalidationQuestion.setQuestion(uniqueQuestionsVO.getQuestion());
						
						if(uniqueQuestionsVO.getQuestionType().equals("Qtype_0")){
							updatedvalidationQuestion.setQuestionType("Text Entry");
						}
						else if(uniqueQuestionsVO.getQuestionType().equals("Qtype_1")){
							updatedvalidationQuestion.setQuestionType("True False");
						}
						updatedvalidationQuestion.setModifiedBy(author);
						updatedvalidationQuestion.setModifiedDate(new Date());
						
						updatedvalidationQuestion.setAnswerQuery("SELECT ANSWER AS ANSWERTEXT FROM dbo.VALIDATIONQUESTION AS VQ INNER JOIN dbo.LEARNERVALIDATIONANSWERS AS LVA ON VQ.ID = LVA.QUESTION_ID WHERE LVA.LEARNER_ID= @LEARNER_ID AND VQ.ID = " + updatedvalidationQuestion.getId());
						accreditationService.saveValidationQuestion(updatedvalidationQuestion);
					}
					
						
				}
			}
		 }
		if (request.getParameter("uquestionDeleteId") != null) {
			String[] selectedUniqueQuestionValues =
			request.getParameter("uquestionDeleteId").toString().split(",");
			List<String> lststrUniqueQuestions = Arrays.asList(selectedUniqueQuestionValues);
			List<Long> lstUniqueQuestionIds = new ArrayList<>();
			for (String uniqueQuestionsId : lststrUniqueQuestions) {
			    if (StringUtils.isNotBlank(uniqueQuestionsId.trim())) {
				   lstUniqueQuestionIds.add(Long.parseLong(uniqueQuestionsId.trim()));
				}
			}
			if (lstUniqueQuestionIds != null && !lstUniqueQuestionIds.isEmpty()) {
			   accreditationService.deleteValidationQuestion(lstUniqueQuestionIds);
			}
		}
	}	
	}

	public ModelAndView saveCourseConfig( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		
		try{
			
			if( errors.hasErrors() ) {
				return new ModelAndView(editCourseConfigTemplate);
			}

			saveCourseConfiguration(command,request);
			
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
					 
		return new ModelAndView(searchCourseConfigTemplate);
	}

	public ModelAndView publish( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {

		CourseConfigForm form = (CourseConfigForm)command;

		if(!getLcmsClientWS().invokeCourseConfigurationUpdated(getAccreditationService().getCourseConfigurationById(form.getCourseConfiguration().getId()))){
			errors.reject("error.courseConfiguration.publish.failure", "");
		}
		
		return new ModelAndView(editCourseConfigTemplate);
	}

	public ModelAndView deleteQuestion( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {

		try{
			
			CourseConfigForm form = (CourseConfigForm)command;
			if(request.getParameter("uquestionDeleteId") !=null){
			String[] selectedUniqueQuestionValues = request.getParameter("uquestionDeleteId").toString().split(",");
			List<String> lststrUniqueQuestions = Arrays.asList(selectedUniqueQuestionValues);
			List<Long> lstUniqueQuestionIds = new ArrayList<>();
			for(String uniqueQuestionsId : lststrUniqueQuestions){
				if( StringUtils.isNotBlank(uniqueQuestionsId.trim()) ) {
					lstUniqueQuestionIds.add(Long.parseLong(uniqueQuestionsId.trim()));
				}	
			 }
		 if(lstUniqueQuestionIds != null && !lstUniqueQuestionIds.isEmpty()){
			   accreditationService.deleteValidationQuestion(lstUniqueQuestionIds);
		     }
		  }	

		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
					 
		return new ModelAndView(searchCourseConfigTemplate);
	
	}
	
	public ModelAndView saveAndPublish( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {

		try{
			
			CourseConfigForm form = (CourseConfigForm)command;

			if( errors.hasErrors() ) {
				return new ModelAndView(editCourseConfigTemplate);
			}

			saveCourseConfiguration(command,request);

			if(!getLcmsClientWS().invokeCourseConfigurationUpdated(getAccreditationService().getCourseConfigurationById(form.getCourseConfiguration().getId()))){
				errors.reject("error.courseConfiguration.publish.failure", "");
			}
			
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
					 
		return new ModelAndView(editCourseConfigTemplate);
	
	}

	public ModelAndView unlinkCertificate( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		
		CourseConfigForm form = (CourseConfigForm)command;
		CourseConfiguration courseConfig = accreditationService.loadForUpdateCourseConfiguration(form.getCourseConfiguration().getId());		
		if(courseConfig.getCompletionCertificate() != null){
			courseConfig.setCompletionCertificate(null);
			courseConfig.setCertificateName("");
			accreditationService.saveCourseConfiguration(courseConfig);
			form.setCompletionCertificate(null);
			form.setCertificateName("");
		}
		return new ModelAndView(editCourseConfigTemplate);
		
	}
	
	protected void validate( HttpServletRequest request, Object command,
			BindException errors, String methodName ) throws Exception {
		AddCourseConfigValidator validator = (AddCourseConfigValidator)this.getValidator();
		CourseConfigForm form = (CourseConfigForm)command;

		/*
		ArrayList<String> parameterNames = new ArrayList<String>();
		 Enumeration enumeration = request.getParameterNames();
		 UniqueQuestionsVO uniqueQuesVO = null;
		 List<UniqueQuestionsVO> lstUniquesQueVO = new ArrayList<>();
		    while (enumeration.hasMoreElements()) {
		        String parameterName = (String) enumeration.nextElement();
		       
		        if(parameterName.contains("uquestionName_")){
		        	String id = parameterName.substring(parameterName.indexOf('_')+1);
		        	  
		        	uniqueQuesVO =  getUniqueQuestion(Integer.parseInt(id),request);
		        	lstUniquesQueVO.add(uniqueQuesVO);
		        }
		        
		    }
		 
		 if(lstUniquesQueVO!=null && !lstUniquesQueVO.isEmpty()){
			 form.getCourseConfiguration().setLstUniqueQuestionsVO(lstUniquesQueVO);
		 }
        */
		
		if( methodName.equals("saveCourseConfig")) {
			validator.validateStep1(form, errors);
			validator.validateStep2(form, errors);
			validator.validateStep3(form, errors);
			validator.validateStep4(form, errors);
			validator.validateStep5(form, errors);
			validator.validateStep6(form, errors);
			validator.validateStep7(form, errors);
			validator.validateStep9(form, errors);
			validator.validateStep10(form, errors);
		}
	}

	public UniqueQuestionsVO getUniqueQuestion(int id,HttpServletRequest request){
		UniqueQuestionsVO uniqueQuestionsVO = new UniqueQuestionsVO();
		String question = request.getParameter("uquestionName_"+id);
		String questionType = request.getParameter("uquestionType_"+id);
		String questionId = request.getParameter("uquestionId_"+id);
		//String questionId = request.getParameter("");
		uniqueQuestionsVO.setQuestion(question);
		uniqueQuestionsVO.setQuestionType(questionType);
		uniqueQuestionsVO.setId(questionId);
		uniqueQuestionsVO.setQuestionId(id);
		return uniqueQuestionsVO;
		
	}
	
	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	public String getEditCourseConfigTemplate() {
		return editCourseConfigTemplate;
	}

	public void setEditCourseConfigTemplate(String editCourseConfigTemplate) {
		this.editCourseConfigTemplate = editCourseConfigTemplate;
	}

	public String getSearchCourseConfigTemplate() {
		return searchCourseConfigTemplate;
	}

	public void setSearchCourseConfigTemplate(String searchCourseConfigTemplate) {
		this.searchCourseConfigTemplate = searchCourseConfigTemplate;
	}

	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	public LCMSClientWS getLcmsClientWS() {
		return lcmsClientWS;
	}

	public void setLcmsClientWS(LCMSClientWS lcmsClientWS) {
		this.lcmsClientWS = lcmsClientWS;
	}

	
}