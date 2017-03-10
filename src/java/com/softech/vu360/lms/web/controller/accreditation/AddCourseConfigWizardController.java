package com.softech.vu360.lms.web.controller.accreditation;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
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
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Certificate;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.CourseConfiguration;
import com.softech.vu360.lms.model.CourseConfigurationTemplate;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.VU360UserNew;
import com.softech.vu360.lms.model.ValidationQuestion;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.LanguageService;
import com.softech.vu360.lms.vo.UniqueQuestionsVO;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.accreditation.CourseConfigForm;
import com.softech.vu360.lms.web.controller.validator.Accreditation.AddCourseConfigValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.GUIDGeneratorUtil;

/**
 * @author PG
 * @created on 5-july-2009
 *
 */
public class AddCourseConfigWizardController extends AbstractWizardFormController {

	private static final Logger log = Logger.getLogger(AddRegulatorWizardController.class.getName());

	private AccreditationService accreditationService;
	@Inject
	private LanguageService languageService;
	private String cancelTemplate = null;
	private String finishTemplate = null;
	private String selectCertificateController = null;
	private final String PROCTOR_VALIDATOR_ANSI = "ansi";
	private final String PROCTOR_VALIDATOR_NY_INSURANCE = "nyInsurance";
	private final String PROCTOR_VALIDATOR_TREC = "TREC";
	
	public AddCourseConfigWizardController() {
		super();
		setCommandName("courseConfigForm");
		setCommandClass(CourseConfigForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"accreditation/approvals/addCourseConfig/step1"
				, "accreditation/approvals/addCourseConfig/step2"
				, "accreditation/approvals/addCourseConfig/step3"
				, "accreditation/approvals/addCourseConfig/step4"
				, "accreditation/approvals/addCourseConfig/step5"
				, "accreditation/approvals/addCourseConfig/step6"
				, "accreditation/approvals/addCourseConfig/proctorCourseConfig"
				, "accreditation/approvals/addCourseConfig/step7"
				, "accreditation/approvals/addCourseConfig/step8"
				, "accreditation/approvals/addCourseConfig/step9"
				, "accreditation/approvals/addCourseConfig/step10"
				, "accreditation/approvals/addCourseConfig/step11"
				, "/acc_addApprovalCertificate.do?entity=CourseConfiguration&redirectTo=addCourseConfiguration"
		});
	}

	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		// Auto-generated method stub
		super.initBinder(request, binder);
	}

	protected void onBindOnNewForm(HttpServletRequest request, Object command,
			BindException errors) throws Exception {
		// Auto-generated method stub
		CourseConfigForm form = (CourseConfigForm)command;
		form.setMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration(12);// default values as set in LCMS
		form.setMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationUnit(CourseConfiguration.UNITS_MONTH);// default values as set in LCMS
		form.setRequireProctorValidation(false);
		form.setRequireLearnerValidation(false);
		form.setCaRealEstateCE(false);
		super.onBindOnNewForm(request, command, errors);
	}

	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors) throws Exception {
		// Auto-generated method stub
		return super.showForm(request, response, errors);
	}

	protected Object formBackingObject( HttpServletRequest request ) throws Exception {
		Object command = super.formBackingObject(request);
		CourseConfigForm courseConfigForm=(CourseConfigForm) command;
		CourseConfiguration courseConfiguration=new CourseConfiguration();
		CourseConfigurationTemplate courseConfigurationTemplate=new CourseConfigurationTemplate();
		courseConfiguration.setCourseConfigTemplate(courseConfigurationTemplate);
		courseConfigForm.setCourseConfiguration(courseConfiguration);
		return command;
	}

	protected Map<Object, Object> referenceData(HttpServletRequest request, Object command, Errors errors,
			int page) throws Exception {
		CourseConfigForm form = (CourseConfigForm) command;

		switch (page) {
			case 0:
				break;
			case 1:
				if (StringUtils.isBlank(form.getCourseConfiguration()
						.getAcknowledgeText()))
					form.getCourseConfiguration().setAcknowledgeText("");
				form.getCourseConfiguration().setAcknowledgeText(
						form.getCourseConfiguration().getAcknowledgeText()
								.replaceAll("\"", "'"));
	
				form.getCourseConfiguration().setAcknowledgeText(
						form.getCourseConfiguration().getAcknowledgeText()
								.replaceAll("\r\n", ""));
				form.getCourseConfiguration().setAcknowledgeText(
						form.getCourseConfiguration().getAcknowledgeText()
								.replaceAll("\n", "<br>"));
				form.getCourseConfiguration().setAcknowledgeText(
						form.getCourseConfiguration().getAcknowledgeText()
								.replaceAll("\r", "<br>"));
	
				initCourseConfigFormWithCertificate(request, form,
						accreditationService);
	
				break;
			case 2:
				String acknowledgeText = "";
				if (request.getParameter("message") != null) {
					acknowledgeText = request.getParameter("message");
					form.getCourseConfiguration().setAcknowledgeText(
							acknowledgeText);
				}
	
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				/*
				 * {
				 * 
				 * CourseConfiguration mycourseConfiguration =
				 * form.getCourseConfiguration();
				 * 
				 * List<UniqueQuestionsVO> lstUQV =
				 * mycourseConfiguration.getLstUniqueQuestionsVO();
				 * UniqueQuestionsVO uniqueQuesVO = null; List<UniqueQuestionsVO>
				 * lstUniquesQueVO = new ArrayList<>();
				 * 
				 * ArrayList<String> parameterNames = new ArrayList<String>();
				 * Enumeration enumeration = request.getParameterNames(); while
				 * (enumeration.hasMoreElements()) { String parameterName = (String)
				 * enumeration.nextElement();
				 * 
				 * if(parameterName.contains("uquestionName_")){ String id =
				 * parameterName.substring(parameterName.indexOf('_')+1);
				 * uniqueQuesVO = getUniqueQuestion(Integer.parseInt(id),request);
				 * lstUniquesQueVO.add(uniqueQuesVO); }
				 * 
				 * }
				 * 
				 * if(lstUniquesQueVO!=null && !lstUniquesQueVO.isEmpty()){
				 * form.getCourseConfiguration
				 * ().setLstUniqueQuestionsVO(lstUniquesQueVO); } }
				 */
				break;
			case 8: {
				UniqueQuestionsVO uniqueQuesVO = null;
				List<UniqueQuestionsVO> lstUniquesQueVO = new ArrayList<>();
	
				Enumeration enumeration = request.getParameterNames();
				while (enumeration.hasMoreElements()) {
					String parameterName = (String) enumeration.nextElement();
	
					if (parameterName.contains("uquestionName_")) {
						String id = parameterName.substring(parameterName
								.indexOf('_') + 1);
						uniqueQuesVO = getUniqueQuestion(Integer.parseInt(id),
								request);
						lstUniquesQueVO.add(uniqueQuesVO);
					}
	
				}
	
				if (lstUniquesQueVO != null && !lstUniquesQueVO.isEmpty()) {
					form.getCourseConfiguration().setLstUniqueQuestionsVO(
							lstUniquesQueVO);
				}
	
			}
	
				break;
			case 9:
				break;
			case 10:
				break;
			case 11: // select configuration template
				break;
			default:
				break;
		}
		return super.referenceData(request, page);
	}

	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception {
		CourseConfigForm form = (CourseConfigForm) command;
		CourseConfiguration courseConfiguration = form.getCourseConfiguration();
		
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().
		getPrincipal();
		
		VU360UserNew VU360User =  VU360UserAuthenticationDetails.getCurrentSimpleUser();
		Language userLanguage = languageService.getUserLanguageById(loggedInUser.getLanguage().getId());
		ContentOwner contentOwner = null;
		if( loggedInUser.getRegulatoryAnalyst() != null )

			contentOwner = accreditationService.findContentOwnerByRegulatoryAnalyst(
					loggedInUser.getRegulatoryAnalyst());
		
		if( contentOwner != null ) {
			courseConfiguration.getCourseConfigTemplate().setContentOwner(contentOwner);//Current content owner is to be set
			courseConfiguration.getCourseConfigTemplate().setLastUpdatedDate(new Date());//Current date is to be updated
			courseConfiguration.setGuid(GUIDGeneratorUtil.generateGUID());
			if(!(form.getIdleTimeAmount().isEmpty() || form.getIdleTimeAmount()==null)){
				courseConfiguration.setIdleTimeAmount(FormUtil.parseNumber(form.getIdleTimeAmount()));
			}
			if(!(form.getNumberOfCoursesLaunch().isEmpty() || form.getNumberOfCoursesLaunch()==null)){
				courseConfiguration.setNumberOfCoursesLaunch(FormUtil.parseNumber(form.getNumberOfCoursesLaunch()));
			}
			if(!(form.getMinutesAfterFirstCourseLaunch().isEmpty() || form.getMinutesAfterFirstCourseLaunch()==null)){
				courseConfiguration.setMinutesAfterFirstCourseLaunch(FormUtil.parseNumber(form.getMinutesAfterFirstCourseLaunch()));
			}
			
			if(!(form.getPreassessmentMasteryscore().isEmpty() || form.getPreassessmentMasteryscore()==null)){
				courseConfiguration.setPreassessmentMasteryscore(FormUtil.parseNumber(form.getPreassessmentMasteryscore()));
			}
			if(!(form.getPreassessmentMaximumnoattempt().isEmpty() || form.getPreassessmentMaximumnoattempt()==null)){
				courseConfiguration.setPreassessmentMaximumnoattempt(FormUtil.parseNumber(form.getPreassessmentMaximumnoattempt()));
			}
			if(!(form.getPreassessmentNoquestion().isEmpty() || form.getPreassessmentNoquestion()==null)){
				courseConfiguration.setPreassessmentNoquestion(FormUtil.parseNumber(form.getPreassessmentNoquestion()));
			}
			if(!(form.getPostassessmentMasteryscore().isEmpty() || form.getPostassessmentMasteryscore()==null)){
				courseConfiguration.setPostassessmentMasteryscore(FormUtil.parseNumber(form.getPostassessmentMasteryscore()));
			}
			if(!(form.getPostassessmentMaximumnoattempt().isEmpty() || form.getPostassessmentMaximumnoattempt()==null)){
				courseConfiguration.setPostassessmentMaximumnoattempt(FormUtil.parseNumber(form.getPostassessmentMaximumnoattempt()));
			}
			if(!(form.getPostassessmentNoquestion().isEmpty() || form.getPostassessmentNoquestion()==null)){
				courseConfiguration.setPostassessmentNoquestion(FormUtil.parseNumber(form.getPostassessmentNoquestion()));
			}
			if(!(form.getQuizAssessmentMasteryscore().isEmpty() || form.getQuizAssessmentMasteryscore()==null)){
				courseConfiguration.setQuizMasteryscore(FormUtil.parseNumber(form.getQuizAssessmentMasteryscore()));
			}
			if(!(form.getQuizAssessmentMaximumnoattempt().isEmpty() || form.getQuizAssessmentMaximumnoattempt()==null)){
				courseConfiguration.setQuizMaximumnoattempt(FormUtil.parseNumber(form.getQuizAssessmentMaximumnoattempt()));
			}
			if(!(form.getQuizAssessmentNoquestion().isEmpty() || form.getQuizAssessmentNoquestion()==null)){
				courseConfiguration.setQuizNoquestion(FormUtil.parseNumber(form.getQuizAssessmentNoquestion()));
			}
			if(!(form.getRequireIdentityValidationEverySeconds().isEmpty() || form.getRequireIdentityValidationEverySeconds()==null)){
				courseConfiguration.setRequireIdentityValidationEverySeconds(FormUtil.parseNumber(form.getRequireIdentityValidationEverySeconds()));
			}
			if(!(form.getValidationNoMissedQuestionsAllowed().isEmpty() || form.getValidationNoMissedQuestionsAllowed()==null)){
				courseConfiguration.setValidationNoMissedQuestionsAllowed(FormUtil.parseNumber(form.getValidationNoMissedQuestionsAllowed()));
			}
			if(!(form.getValidationTimeBetweenQuestion().isEmpty() || form.getValidationTimeBetweenQuestion()==null)){
				courseConfiguration.setValidationTimeBetweenQuestion(FormUtil.parseNumber(form.getValidationTimeBetweenQuestion()));
			}
//			if(!(form.getAllowNumberofAttemptsToAnswerCorrectly().isEmpty() || form.getAllowNumberofAttemptsToAnswerCorrectly()==null)){
//				courseConfiguration.setAllowNumberofAttemptsToAnswerCorrectly(FormUtil.parseNumber(form.getAllowNumberofAttemptsToAnswerCorrectly()));
//			}
			if(!(form.getAllowSecondsToAnswerEachQuestion().isEmpty() || form.getAllowSecondsToAnswerEachQuestion()==null)){
				courseConfiguration.setAllowSecondsToAnswerEachQuestion(FormUtil.parseNumber(form.getAllowSecondsToAnswerEachQuestion()));
			}
			if(!(form.getNumberOfValidationQuestions().isEmpty() || form.getNumberOfValidationQuestions()==null)){
				courseConfiguration.setNumberOfValidationQuestions(FormUtil.parseNumber(form.getNumberOfValidationQuestions()));
			}
			
			if(!(form.getDaysOfRegistraion().isEmpty() || form.getDaysOfRegistraion()==null)){
				courseConfiguration.setDaysOfRegistraion(FormUtil.parseNumber(form.getDaysOfRegistraion()));
			}
			if(!(form.getUnitOfTimeToComplete()==null || form.getUnitOfTimeToComplete().isEmpty())){
				courseConfiguration.setUnitOfTimeToComplete(form.getUnitOfTimeToComplete());
			}
			
			courseConfiguration.setEnableIdentityValidation(form.getCourseConfiguration().getEnableIdentityValidation());
			courseConfiguration.setRequireSmartProfileValidation(form.isEnableSmartProfileValidation());
			courseConfiguration.setRequireDefineUniqueQuestionValidation(form.isEnableDefineUniqueQuestionValidation());
                 
			if( form.isCertificateEnabled()){
            	courseConfiguration.setCompletionCertificate(form.getCompletionCertificate());
                courseConfiguration.setCertificateEnabled(form.isCertificateEnabled());
            }
                        
			courseConfiguration.setDisplayCourseEvaluation(form.getDisplayCourseEvaluation());
						
			courseConfiguration.setEndOfCourseInstructions(form.getEndOfCourseInstructions());
			courseConfiguration.setAcknowledgeEnabled(form.isAcknowledgeEnabled());
			courseConfiguration.setAcknowledgeText(form.getAcknowledgeText());
			courseConfiguration.setPlayerStrictlyEnforcePolicyToBeUsed(form.isPlayerStrictlyEnforcePolicyToBeUsed());
			courseConfiguration.setActiontotakeuponidletimeout(form.getActiontotakeuponidletimeout());
			courseConfiguration.setPlayerCourseFlow(form.getPlayerCourseFlow());
			//courseConfiguration.setRespondToCourseEvaluation(form.isRespondToCourseEvaluation());
			//courseConfiguration.setCourseEvaluationSpecified(form.isCourseEvaluationSpecified());
			
			
			//for pre/post/quiz
			//PRE
			
			courseConfiguration.setPreassessmentEnabled(form.isPreAssessmentEnabled());
			
			
			if( form.isPreAssessmentMaximumnoattemptEnabled() && !(form.getPreActionToTakeAfterMaximumAttemptsReached().isEmpty() || form.getPreActionToTakeAfterMaximumAttemptsReached()==null)){
				courseConfiguration.setPreActionToTakeAfterMaximumAttemptsReached(form.getPreActionToTakeAfterMaximumAttemptsReached());
			}

			
			if(!(form.getPreassessmentMasteryscore().isEmpty() || form.getPreassessmentMasteryscore()==null)){
				courseConfiguration.setPreassessmentMasteryscore(FormUtil.parseNumber(form.getPreassessmentMasteryscore()));
			}
			if(!(form.getPreassessmentMaximumnoattempt().isEmpty() || form.getPreassessmentMaximumnoattempt()==null)){
				courseConfiguration.setPreassessmentMaximumnoattempt(FormUtil.parseNumber(form.getPreassessmentMaximumnoattempt()));
			}
			if(!(form.getPreassessmentNoquestion().isEmpty() || form.getPreassessmentNoquestion()==null)){
				courseConfiguration.setPreassessmentNoquestion(FormUtil.parseNumber(form.getPreassessmentNoquestion()));
			}
			if(!(form.getPreGradingBehavior().isEmpty() || form.getPreGradingBehavior()==null)){
				courseConfiguration.setPreGradingBehavior(form.getPreGradingBehavior());
			}
			
				courseConfiguration.setPreMaximumTimeLimitMinutes(FormUtil.parseNumber(form.getPreassessmentEnforcemaximumtimelimit()));
			    courseConfiguration.setPreEnforeceMaximumtimelimit(form.isPreassessmentEnforcemaximumtimelimitCB());
				courseConfiguration.setPreMinimumSeatTimeBeforeAssessmentStart(form.getPreMinimumSeatTimeBeforeAssessmentStart());
				courseConfiguration.setPreLockoutAssessmentActiveWindow(form.isPreLockoutAssessmentActiveWindow());

			if(!(form.getPreScoringType().isEmpty() || form.getPreScoringType()==null)){
				courseConfiguration.setPreScoringType(form.getPreScoringType());
			}
			
			boolean isCheckedPreScoring = true;
			if(!(form.getPreScoringType().isEmpty() || form.getPreScoringType()==null)){
				if(form.getPreScoringType().equals("No Results"))
				{
					courseConfiguration.setEnablePreAssessmentMaximumNoAttempt(true);
					courseConfiguration.setPreassessmentMaximumnoattempt(1);
					isCheckedPreScoring = false;
				}
			}
			
			if(isCheckedPreScoring)
			{
				courseConfiguration.setEnablePreAssessmentMaximumNoAttempt(form.isPreAssessmentMaximumnoattemptEnabled());
			}
			
			if( form.isPreEnableAdvancedQuestionSelectionType() && 
                            (form.getPreAdvancedQuestionSelectionType()!=null && !form.getPreAdvancedQuestionSelectionType().isEmpty()))
                        {
				courseConfiguration.setPreAdvancedQuestionSelectionType(form.getPreAdvancedQuestionSelectionType());
			}else{
                            courseConfiguration.setPreAdvancedQuestionSelectionType("");
                        }
			
			courseConfiguration.setPreAllowQuestionSkipping(form.isPreAllowQuestionSkipping());
			courseConfiguration.setPreEnableAssessmentProctoring(form.isPreEnableAssessmentProctoring());
			courseConfiguration.setPreEnableContentRemediation(form.isPreEnableContentRemediation());
			courseConfiguration.setPreEnableRestrictiveAssessmentMode(form.isPreEnableRestrictiveAssessmentMode());
			courseConfiguration.setPreEnableWeightedScoring(form.isPreEnableWeightedScoring());
			courseConfiguration.setPreRandomizeAnswers( form.isPreRandomizeAnswers());
			courseConfiguration.setPreRandomizeQuestions(form.isPreRandomizeQuestions());
			courseConfiguration.setPreShowQuestionAnswerReview(form.isPreShowQuestionAnswerReview());
			courseConfiguration.setPreShowQuestionLevelResults(form.isPreShowQuestionLevelResults());
			courseConfiguration.setPreUseUniqueQuestionsOnRetake( form.isPreUseUniqueQuestionsOnRetake());
			courseConfiguration.setPreNoResultMessage(form.getPreNoResultMessage());
			
			if(!form.isPreassessmentEnforcemaximumtimelimitCB())
			{
				courseConfiguration.setPreassessmentMaximumtimelimit(0);
			}
			else
			{
				courseConfiguration.setPreassessmentMaximumtimelimit(FormUtil.parseNumber(form.getPreassessmentEnforcemaximumtimelimit()));
			}
			                              
			//POST
			
			courseConfiguration.setPostAssessmentEnabled(form.isPostAssessmentEnabled());
			
			if( form.isPostAssessmentMaximumnoattemptEnabled() && !(form.getPostActionToTakeAfterMaximumAttemptsReached().isEmpty() || form.getPostActionToTakeAfterMaximumAttemptsReached()==null))
            {
				courseConfiguration.setPostActionToTakeAfterMaximumAttemptsReached(form.getPostActionToTakeAfterMaximumAttemptsReached());
            }

			if(!(form.getPostassessmentMasteryscore().isEmpty() || form.getPostassessmentMasteryscore()==null)){
				courseConfiguration.setPostassessmentMasteryscore(FormUtil.parseNumber(form.getPostassessmentMasteryscore()));
			}
			if(!(form.getPostassessmentMaximumnoattempt().isEmpty() || form.getPostassessmentMaximumnoattempt()==null)){
				courseConfiguration.setPostassessmentMaximumnoattempt(FormUtil.parseNumber(form.getPostassessmentMaximumnoattempt()));
			}
			if(!(form.getPostassessmentNoquestion().isEmpty() || form.getPostassessmentNoquestion()==null)){
				courseConfiguration.setPostassessmentNoquestion(FormUtil.parseNumber(form.getPostassessmentNoquestion()));
			}
			if(!(form.getPostGradingBehavior().isEmpty() || form.getPostGradingBehavior()==null)){
				courseConfiguration.setPostGradingBehavior(form.getPostGradingBehavior());
			}

				courseConfiguration.setPostMaximumTimeLimitMinutes(FormUtil.parseNumber(form.getPostassessmentEnforcemaximumtimelimit()));
				courseConfiguration.setPostEnforeceMaximumtimelimit(form.getPostassessmentEnforcemaximumtimelimitCB());
				courseConfiguration.setPostMinimumSeatTimeBeforeAssessmentStart(FormUtil.parseNumber(form.getPostMinimumSeatTimeBeforeAssessmentStart()));


			if(!(form.getPostScoringType().isEmpty() || form.getPostScoringType()==null)){
				courseConfiguration.setPostScoringType(form.getPostScoringType());
			}
			
			boolean isCheckedPostScoring = true;
			if(!(form.getPostScoringType().isEmpty() || form.getPostScoringType()==null)){
				if(form.getPostScoringType().equals("No Results"))
				{
					courseConfiguration.setEnablePostAssessmentMaximumNoAttempt(true);
					courseConfiguration.setPostassessmentMaximumnoattempt(1);
					isCheckedPostScoring=false;
				}
			}
			if(isCheckedPostScoring)
			{
				courseConfiguration.setEnablePostAssessmentMaximumNoAttempt(form.isPostAssessmentMaximumnoattemptEnabled());
			}
			
			if(form.isPostEnableAdvancedQuestionSelectionType() && 
                            form.getPostAdvancedQuestionSelectionType()!=null && !form.getPostAdvancedQuestionSelectionType().isEmpty()){
				courseConfiguration.setPostAdvancedQuestionSelectionType(form.getPostAdvancedQuestionSelectionType());
			}else{
                            courseConfiguration.setPostAdvancedQuestionSelectionType("");
                        }
			
			courseConfiguration.setPostAllowQuestionSkipping(form.isPostAllowQuestionSkipping());
			courseConfiguration.setPostEnableAssessmentProctoring(form.isPostEnableAssessmentProctoring());
			courseConfiguration.setPostEnableContentRemediation(form.isPostEnableContentRemediation());
			courseConfiguration.setPostEnableRestrictiveAssessmentMode(form.isPostEnableRestrictiveAssessmentMode());
			courseConfiguration.setPostEnableWeightedScoring(form.isPostEnableWeightedScoring());
			courseConfiguration.setPostRandomizeAnswers( form.isPostRandomizeAnswers());
			courseConfiguration.setPostRandomizeQuestions(form.isPostRandomizeQuestions());
			courseConfiguration.setPostShowQuestionAnswerReview(form.isPostShowQuestionAnswerReview());
			courseConfiguration.setPostShowQuestionLevelResults(form.isPostShowQuestionLevelResults());
			courseConfiguration.setPostUseUniqueQuestionsOnRetake( form.isPostUseUniqueQuestionsOnRetake());
			courseConfiguration.setPostNoResultMessage(form.getPostNoResultMessage());
			
			if(!form.getPostassessmentEnforcemaximumtimelimitCB())
			{
				courseConfiguration.setPostassessmentMaximumtimelimit(0);
			}
			else
			{
				courseConfiguration.setPostassessmentMaximumtimelimit(FormUtil.parseNumber(form.getPostassessmentEnforcemaximumtimelimit()));
			}
			
			//QUIZ
			
			courseConfiguration.setQuizAssessmentEnabled(form.isQuizAssessmentEnabled());
			courseConfiguration.setPostLockoutAssessmentActiveWindow(form.isPostLockoutAssessmentActiveWindow());
			courseConfiguration.setViewassessmentresultsEnabled(form.isPostEnableviewAssessmentResults());
			
			if( form.isQuizAssessmentMaximumnoattemptEnabled() && 
                            !(form.getQuizAssessmentActionToTakeAfterMaximumAttemptsReached().isEmpty() || form.getQuizAssessmentActionToTakeAfterMaximumAttemptsReached()==null)){
				courseConfiguration.setQuizActionToTakeAfterMaximumAttemptsReached(form.getQuizAssessmentActionToTakeAfterMaximumAttemptsReached());
			}

			if(!(form.getQuizAssessmentMasteryscore().isEmpty() || form.getQuizAssessmentMasteryscore()==null)){
				courseConfiguration.setQuizMasteryscore(FormUtil.parseNumber(form.getQuizAssessmentMasteryscore()));
			}
			if(!(form.getQuizAssessmentMaximumnoattempt().isEmpty() || form.getQuizAssessmentMaximumnoattempt()==null)){
				courseConfiguration.setQuizMaximumnoattempt(FormUtil.parseNumber(form.getQuizAssessmentMaximumnoattempt()));
			}
			if(!(form.getQuizAssessmentNoquestion().isEmpty() || form.getQuizAssessmentNoquestion()==null)){
				courseConfiguration.setQuizNoquestion(FormUtil.parseNumber(form.getQuizAssessmentNoquestion()));
			}
			if(!(form.getQuizAssessmentGradingBehavior().isEmpty() || form.getQuizAssessmentGradingBehavior()==null)){
				courseConfiguration.setQuizGradingBehavior(form.getQuizAssessmentGradingBehavior());
			}

			courseConfiguration.setQuizMaximumTimeLimitMinutes(FormUtil.parseNumber(form.getQuizAssessmentEnforcemaximumtimelimit()));
			courseConfiguration.setQuizEnforeceMaximumtimelimit(form.getQuizAssessmentEnforcemaximumtimelimitCB());


				courseConfiguration.setQuizMinimumSeatTimeBeforeAssessmentStart(form.getQuizAssessmentMinimumSeatTimeBeforeAssessmentStart());


			if(!(form.getQuizAssessmentScoringType().isEmpty() || form.getQuizAssessmentScoringType()==null)){
				courseConfiguration.setQuizScoringType(form.getQuizAssessmentScoringType());
			}
			
			boolean isCheckedQuizScoring = true;
			if(!(form.getQuizAssessmentScoringType().isEmpty() || form.getQuizAssessmentScoringType()==null)){
				if(form.getQuizAssessmentScoringType().equals("No Results"))
				{
					courseConfiguration.setEnableQuizMaximumNoAttempt(true);
					courseConfiguration.setQuizMaximumnoattempt(1);
					isCheckedQuizScoring=false;
				}
			}
			if(isCheckedQuizScoring)
			{
				courseConfiguration.setEnableQuizMaximumNoAttempt(form.isQuizAssessmentMaximumnoattemptEnabled());
			}
			
			if( form.isQuizAssessmentEnableAdvancedQuestionSelectionType() && 
                            (form.getQuizAssessmentAdvancedQuestionSelectionType()!=null && !form.getQuizAssessmentAdvancedQuestionSelectionType().isEmpty())){
				courseConfiguration.setQuizAdvancedQuestionSelectionType(form.getQuizAssessmentAdvancedQuestionSelectionType());
			}else{
                            courseConfiguration.setQuizAdvancedQuestionSelectionType("");
                        }
			
			courseConfiguration.setQuizAllowQuestionSkipping(form.isQuizAssessmentAllowQuestionSkipping());
			courseConfiguration.setQuizEnableAssessmentProctoring(form.isQuizAssessmentEnableAssessmentProctoring());
			courseConfiguration.setQuizEnableContentRemediation(form.isQuizAssessmentEnableContentRemediation());
			courseConfiguration.setQuizEnableRestrictiveAssessmentMode(form.isQuizAssessmentEnableRestrictiveAssessmentMode());
			courseConfiguration.setQuizEnableWeightedScoring(form.isQuizAssessmentEnableWeightedScoring());
			courseConfiguration.setQuizRandomizeAnswers( form.isQuizAssessmentRandomizeAnswers());
			courseConfiguration.setQuizRandomizeQuestions(form.isQuizAssessmentRandomizeQuestions());
			courseConfiguration.setQuizShowQuestionAnswerReview(form.isQuizAssessmentShowQuestionAnswerReview());
			courseConfiguration.setQuizShowQuestionLevelResults(form.isQuizAssessmentShowQuestionLevelResults());
			courseConfiguration.setQuizUseUniqueQuestionsOnRetake( form.isQuizAssessmentUseUniqueQuestionsOnRetake());
			courseConfiguration.setQuizNoResultMessage(form.getQuizAssessmentNoResultMessage());
			
			
			if(!form.getQuizAssessmentEnforcemaximumtimelimitCB())
			{
				courseConfiguration.setQuizMaximumtimelimit(0);
			}
			else
			{
				courseConfiguration.setQuizMaximumtimelimit(FormUtil.parseNumber(form.getQuizAssessmentEnforcemaximumtimelimit()));
			}
			
			courseConfiguration.setPreAllowPauseResumeAssessment(form.isPreAllowPauseResumeAssessment());
			courseConfiguration.setQuizAllowPauseResumeAssessment(form.isQuizAllowPauseResumeAssessment());
			courseConfiguration.setPostAllowPauseResumeAssessment(form.isPostAllowPauseResumeAssessment());
			courseConfiguration.setQuizLockoutAssessmentActiveWindow(form.isQuizLockoutAssessmentActiveWindow());
			
			//end pre/post/quiz
			
			
			//Seat Time
			
			//Max Seat Time Enforcement
			courseConfiguration.setSeattimeenabled(form.isSeattimeenabled());
			courseConfiguration.setSeattimeinhour(Integer.valueOf(form.getSeattimeinhour()));
			courseConfiguration.setSeattimeinmin( Integer.valueOf(form.getSeattimeinmin()));
			courseConfiguration.setMessageseattimecourselaunch(form.getMessageseattimecourselaunch());
			courseConfiguration.setMessageseattimeexceeds(form.getMessageseattimeexceeds());
			
			
			//Min Seat Time Enforcement
			
			courseConfiguration.setPostMinimumSeatTimeBeforeAssessmentStart(FormUtil.parseNumber(form.getPostMinimumSeatTimeBeforeAssessmentStart()));
			courseConfiguration.setPostMinimumSeatTimeBeforeAssessmentStartUnits(form.getPostMinimumSeatTimeBeforeAssessmentStartUnits());
			courseConfiguration.setDisplaySeatTimeTextMessage(form.isDisplaySeatTimeTextMessage());
			courseConfiguration.setLockPostAssessmentBeforeSeatTime(form.isLockPostAssessmentBeforeSeatTime());
					
					
			//LMS-12023 CHANGE START
			courseConfiguration.setValidationStrictlyEnforcePolicyToBeUsed(form.isPlayerStrictlyEnforcePolicyToBeUsed());
			courseConfiguration.setCreatedBy(loggedInUser.getId());
			courseConfiguration.setCreatedDate(new Date());
			courseConfiguration.setLastUpdatedBy(null);
			courseConfiguration.setLastUpdatedDate(null);
			//LMS-12023 CHANGE END
			
			courseConfiguration.setMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified(form.isMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationSpecified());
			courseConfiguration.setMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration(form.getMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistration());
			courseConfiguration.setMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationUnit(form.getMustStartCourseWithinSpecifiedAmountOfTimeAfterRegistrationUnit());
			
 			courseConfiguration.setSpecialQuestionnaireSpecified(form.isSpecialQuestionnaireSpecified());
			courseConfiguration.setDisplaySpecialQuestionnaire(form.getDisplaySpecialQuestionnaire());
			courseConfiguration.setMustCompleteSpecialQuestionnaire(form.isMustCompleteSpecialQuestionnaire());
			
			courseConfiguration.setRequireProctorValidation(form.isRequireProctorValidation());

			if(form.isRequireProctorValidation()){
				if(form.getProctorValidatorName().equalsIgnoreCase(PROCTOR_VALIDATOR_ANSI))
					courseConfiguration.setRequiredAnsi(true);
				else
					courseConfiguration.setRequiredAnsi(false);
				
				if(form.getProctorValidatorName().equalsIgnoreCase(PROCTOR_VALIDATOR_NY_INSURANCE))
					courseConfiguration.setRequiredNyInsurance(true);
				else
					courseConfiguration.setRequiredNyInsurance(false);
				if(form.getProctorValidatorName().equalsIgnoreCase(PROCTOR_VALIDATOR_TREC))
					courseConfiguration.setRequireSelfRegistrationProctor(true);
				else
				    courseConfiguration.setRequireSelfRegistrationProctor(false);
			}
			else{
				courseConfiguration.setRequiredNyInsurance(false);
				courseConfiguration.setRequiredAnsi(false);
			}
			courseConfiguration.setRequireLearnerValidation(form.isRequireLearnerValidation());
			courseConfiguration.setCaRealEstateCE(form.isCaRealEstateCE());
			
			courseConfiguration.setRequireDefineUniqueQuestionValidation(form.getCourseConfiguration().isRequireDefineUniqueQuestionValidation());
			courseConfiguration.setRequireSmartProfileValidation(form.getCourseConfiguration().isRequireSmartProfileValidation());

			courseConfiguration.setSuggestedCourse(form.isSuggestedCourse());
			courseConfiguration.setRateCourse(form.isRateCourse());
			log.debug("Allow Course Rating Value " + form.isRateCourse());
			accreditationService.saveCourseConfiguration(courseConfiguration);
			
			if(courseConfiguration!=null){
				if(form.getCourseConfiguration().getEnableIdentityValidation() && form.getCourseConfiguration().isRequireDefineUniqueQuestionValidation() && form.getCourseConfiguration().getLstUniqueQuestionsVO() !=null && !form.getCourseConfiguration().getLstUniqueQuestionsVO().isEmpty()){
					for(UniqueQuestionsVO uniqueQuestionsVO :form.getCourseConfiguration().getLstUniqueQuestionsVO()){
						ValidationQuestion validationQuestion = new ValidationQuestion();
						validationQuestion.setQuestion(uniqueQuestionsVO.getQuestion());
						
						if(uniqueQuestionsVO.getQuestionType().equals("Qtype_0")){
						     validationQuestion.setQuestionType("Text Entry");
						}
						else if(uniqueQuestionsVO.getQuestionType().equals("Qtype_1")){
						     validationQuestion.setQuestionType("True False");
						}
						
						validationQuestion.setCourseConfiguration(courseConfiguration);
						validationQuestion.setLanguage(userLanguage);
						validationQuestion.setIsActive(true);
						validationQuestion.setCreatedBy(VU360User);
						validationQuestion.setCreatedDate(new Date());
						validationQuestion.setModifiedBy(VU360User);
						validationQuestion.setModifiedDate(new Date());
						
						accreditationService.saveValidationQuestion(validationQuestion);
						
						ValidationQuestion updatedvalidationQuestion = accreditationService.loadForUpdateValidationQuestion(validationQuestion.getId());		
						if(updatedvalidationQuestion != null){
							updatedvalidationQuestion.setAnswerQuery("SELECT ANSWER AS ANSWERTEXT FROM dbo.VALIDATIONQUESTION AS VQ INNER JOIN dbo.LEARNERVALIDATIONANSWERS AS LVA ON VQ.ID = LVA.QUESTION_ID WHERE LVA.LEARNER_ID= @LEARNER_ID AND VQ.ID = " + updatedvalidationQuestion.getId());
							accreditationService.saveValidationQuestion(updatedvalidationQuestion);
						
						
					}
				}
			}
		}
	}	
		return new ModelAndView(finishTemplate);
	}


	protected void validatePage(Object command, Errors errors, int page, boolean finish) {

		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().
		getPrincipal();
		ContentOwner contentOwner = null;
		if( loggedInUser.getRegulatoryAnalyst() != null )

			contentOwner = accreditationService.findContentOwnerByRegulatoryAnalyst(
					loggedInUser.getRegulatoryAnalyst());

		AddCourseConfigValidator validator = (AddCourseConfigValidator)this.getValidator();
		CourseConfigForm form = (CourseConfigForm)command;
		errors.setNestedPath("");
		switch(page) {
		case 0:
			List<CourseConfigurationTemplate> configTemplate = accreditationService.findTemplates(form.getCourseConfiguration().getCourseConfigTemplate().getName(),null,contentOwner.getId());
			if(configTemplate.size()>0){
				for(CourseConfigurationTemplate item:configTemplate){
					if(item.getName().trim().equals(form.getCourseConfiguration().getCourseConfigTemplate().getName().trim())){
						errors.rejectValue("courseConfiguration.courseConfigTemplate.name", "error.courseConfigTemplate.name.duplicate");
					}else{
						validator.validateStep1(form, errors);
					}
				}
			}else{
				validator.validateStep1(form, errors);
			}
			break;
		case 1:
			validator.validateStep2(form, errors);
			break;
		case 2:
			validator.validateStep3(form, errors);
			break;
		case 3:
			validator.validateStep4(form, errors);
			break;
		case 4:
			validator.validateStep5(form, errors);
			break;
		case 5:
			validator.validateStep6(form, errors);
			break;
		case 6:
			validator.validateStep6(form, errors);
			break;
		case 7:
			validator.validateStep7(form, errors);
			break;
                    
		case 8:
                        
			break;
		case 9:
			validator.validateStep9(form, errors);
			
			break;
		case 10:
			validator.validateStep10(form, errors);
			break;
		default:
			break;
		}
		super.validatePage(command, errors, page, finish);
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
		return uniqueQuestionsVO;
		
	}

	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		log.debug("IN processCancel");
		return new ModelAndView(cancelTemplate);
	}

	public String getCancelTemplate() {
		return cancelTemplate;
	}

	public void setCancelTemplate(String cancelTemplate) {
		this.cancelTemplate = cancelTemplate;
	}

	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	public String getFinishTemplate() {
		return finishTemplate;
	}

	public void setFinishTemplate(String finishTemplate) {
		this.finishTemplate = finishTemplate;
	}
        
        /**
         * Initializes the CourseConfiguration with the selected certificate.
         * @param request
         * @param form 
         * @param accreditationService 
         */
        public void initCourseConfigFormWithCertificate(HttpServletRequest request, CourseConfigForm form, 
                AccreditationService accreditationService){
            if( StringUtils.isNotEmpty(request.getParameter("certificateId"))){
                String certificateId = request.getParameter("certificateId");
                if( NumberUtils.isNumber(certificateId)){
                    Certificate certificate = accreditationService.getCertificateById(
                        Long.parseLong(certificateId));
                    
                    form.setCertificateName(certificate.getName());
                    form.setCompletionCertificate(certificate);
                    form.setCertificateEnabled(true);
                }
            }
        }

		/**
		 * @return the selectCertificateController
		 */
		public String getSelectCertificateController() {
			return selectCertificateController;
		}

		/**
		 * @param selectCertificateController the selectCertificateController to set
		 */
		public void setSelectCertificateController(String selectCertificateController) {
			this.selectCertificateController = selectCertificateController;
		}

        
}
