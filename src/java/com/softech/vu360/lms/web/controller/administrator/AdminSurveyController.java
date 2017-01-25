package com.softech.vu360.lms.web.controller.administrator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.AggregateSurveyQuestion;
import com.softech.vu360.lms.model.AggregateSurveyQuestionItem;
import com.softech.vu360.lms.model.AvailablePersonalInformationfield;
import com.softech.vu360.lms.model.AvailablePersonalInformationfieldItem;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.MultipleSelectSurveyQuestion;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.PersonalInformationSurveyQuestion;
import com.softech.vu360.lms.model.PredictSurveyQuestionMapping;
import com.softech.vu360.lms.model.SingleSelectSurveyQuestion;
import com.softech.vu360.lms.model.SuggestedTraining;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyAnswerItem;
import com.softech.vu360.lms.model.SurveyFlagTemplate;
import com.softech.vu360.lms.model.SurveyLink;
import com.softech.vu360.lms.model.SurveyQuestion;
import com.softech.vu360.lms.model.SurveyQuestionBank;
import com.softech.vu360.lms.model.SurveyResult;
import com.softech.vu360.lms.model.SurveyResultAnswer;
import com.softech.vu360.lms.model.SurveyResultAnswerFile;
import com.softech.vu360.lms.model.SurveyReviewComment;
import com.softech.vu360.lms.model.SurveySection;
import com.softech.vu360.lms.model.SurveySectionSurveyQuestionBank;
import com.softech.vu360.lms.model.TextBoxSurveyQuestion;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.predict.ExportToPredict;
import com.softech.vu360.lms.model.predict.PredictSurveyAnswerItem;
import com.softech.vu360.lms.model.predict.PredictSurveyResult;
import com.softech.vu360.lms.model.predict.PredictSurveyResultAnswer;
import com.softech.vu360.lms.model.predict.PredictSurveyResultAnswerFile;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.util.VelocityPagerTool;
import com.softech.vu360.lms.vo.SurveyPreviewVO;
import com.softech.vu360.lms.vo.SurveyQuestionBankVO;
import com.softech.vu360.lms.vo.SurveySectionVO;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.AdminSurveyForm;
import com.softech.vu360.lms.web.controller.model.LearnerSurvey;
import com.softech.vu360.lms.web.controller.model.LearnerSurveyCourse;
import com.softech.vu360.lms.web.controller.model.ManageFlag;
import com.softech.vu360.lms.web.controller.model.ManagePersonalInformation;
import com.softech.vu360.lms.web.controller.model.SurveyCourse;
import com.softech.vu360.lms.web.controller.model.SurveyQuestionBuilder;
import com.softech.vu360.lms.web.controller.model.SurveyResponseBuilder;
import com.softech.vu360.lms.web.controller.model.SurveyStatus;
import com.softech.vu360.lms.web.controller.model.survey.SurveyAnalysis;
import com.softech.vu360.lms.web.controller.validator.AdminSurveyValidator;
import com.softech.vu360.lms.web.filter.AdminSearchType;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserMode;
import com.softech.vu360.lms.webservice.client.impl.RestClient;
import com.softech.vu360.util.FlagSort;
import com.softech.vu360.util.VU360Properties;


/**
 * 
 * @author Saptarshi
 *
 */
public class AdminSurveyController extends VU360BaseMultiActionController {

	private static final Logger log = Logger.getLogger(AdminSurveyController.class.getName());

	private LearnerService learnerService = null;
	private VU360UserService vu360UserService = null;
	private SurveyService surveyService = null;
	private EntitlementService entitlementService = null;
	private CustomerService customerService = null;
	private DistributorService distributorService = null;
	
	private HttpSession session = null;

	private static final String MANAGE_SURVEY_PAGING_ACTION = "paging";
	public static final String SURVEY_QUESTION_MULTIPLE_CHOICE_MULTIPLE_SELECT = "survey.question.multiplechoice.multipleselect";
	public static final String SURVEY_QUESTION_MULTIPLE_CHOICE_SINGLE_SELECT = "survey.question.multiplechoice.singleselect";
	public static final String SURVEY_QUESTION_DROP_DOWN_SINGLE_SELECT = "survey.question.dropdown.singleselect";
	public static final String SURVEY_QUESTION_TEXT_BOX_256_CHARACTERS = "survey.question.textbox.256characters";
	public static final String SURVEY_QUESTION_TEXT_BOX_UNLIMITED = "survey.question.textbox.unlimited";
	public static final String SURVEY_QUESTION_MULTIPLE_CHOICE_RATING_SELECT = "survey.question.multiplechoice.ratingselect";
	public static final String SURVEY_QUESTION_PERSONAL_INFORMATION = "survey.question.personal.information";
	public static final String SURVEY_QUESTION_CUSTOM = "survey.question.custom";
	private int NUMBER_OF_CUSTOM_RESPONCES = 1;
	private boolean orderFlag=true;
	private boolean flag=false;
	private static final String[] SURVEY_QUESTION_TYPES = {
		SURVEY_QUESTION_MULTIPLE_CHOICE_MULTIPLE_SELECT
		, SURVEY_QUESTION_MULTIPLE_CHOICE_SINGLE_SELECT
		
		, SURVEY_QUESTION_TEXT_BOX_256_CHARACTERS
		, SURVEY_QUESTION_TEXT_BOX_UNLIMITED
		, SURVEY_QUESTION_PERSONAL_INFORMATION
		, SURVEY_QUESTION_CUSTOM
	};

	private static final String[] PERSONAL_INFO_LABELS = {
		"First Name"
		, "Middle Name"
		, "Last Name"
		, "Address1"
		, "Address2"
		, "Email Address"
		, "Phone"
		, "Preferred Time Zone"
	};

	public static final String SURVEY_CUSTOM_RESPONCE_MULTIPLE_SELECT = "survey.customresponce.multipleselect";
	public static final String SURVEY_CUSTOM_RESPONCE_SINGLE_SELECT = "survey.customresponce.singleselect";
	public static final String SURVEY_CUSTOM_RESPONCE_TEXT = "survey.customresponce.textbox";
	public static final String SURVEY_CUSTOM_RESPONCE_BLANKS = "survey.customresponce.fillblanks";

	private static final String[] CUSTOM_RESPONCE_TYPES = {
		SURVEY_CUSTOM_RESPONCE_MULTIPLE_SELECT
		, SURVEY_CUSTOM_RESPONCE_SINGLE_SELECT
		, SURVEY_CUSTOM_RESPONCE_TEXT
		//, SURVEY_CUSTOM_RESPONCE_BLANKS
	};

	private String failureTemplate = null;
	private String defaultManageSurveysTemplate;
	private String editSurveyInfoTemplate;
	private String addQuestionTemplate;
	private String redirectTemplate;
	private String closeQuestionTemplate;
	private String addCourseTemplate;
	private String editCourseTemplate;
	private String addQuestionContainerTemplate;
	private String closeQuestionContainerTemplate;
	private String saveQuestionInListTemplate;
	private String multipleChoiceMultipleSelectTemplate;
	private String surveyAnalyzeTemplate;
	private String surveyAnalyzeSectionTemplate;
	private String editQuestionContainerTemplate;
	private String learnerSurveyResponseTemplate;
	private String surveyResponseTemplate;
	private String displayManageFlagsTemplate;
	private String addNewFlagTemplate;
	private String editFlagTemplate;
	private String trainingPlanAnalysisTemplate;
	private String trainingPlanAnalysisDetailsTemplate;
	private String addNewSurveysTemplate;
	private String surveyAnalizeIndividualTemplate;
	private String surveyAnalizeResponseTemplate;
	private int edtiableFlag=1;
	private int deleteCustomQuestion=0;
	private Long customQuestionId;
	private int enableAddRemove=0;
	private String analyzeSurveySectionTemplate = null;
	
	protected static String SURVER_PARSER_PROPERTIES = VU360Properties.getVU360Property("lms.predict.importexport.propFilePath" );

	public AdminSurveyController() {
		super();
	}

	public AdminSurveyController(Object delegate) {
		super(delegate);
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute("newPage","true");
		return this.searchSurvey(request, response, null, null);

	}
	/**
	 * Callback method for doing extra binding related stuff.
	 */
	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {

		if( command instanceof AdminSurveyForm ) {
			AdminSurveyForm form = (AdminSurveyForm)command;
			orderFlag=true;
			if( methodName.equals("showQuestionView")
					||methodName.equals("showCoursesView")
					||methodName.equals("showSurveyInfoView")
					||methodName.equals("showResponseSummaryView")
					||methodName.equals("addNewFlag")) {
				form.reset();
				//load the survey data from the command.getSid() into the command object using service
				Survey survey = surveyService.loadForUpdateSurvey(form.getSid());
				List<SurveyQuestion> surveyQuestions = new ArrayList<SurveyQuestion>();
				form.setSurveyName(survey.getName());
				List<SurveyQuestion> surveyQuestionList = survey.getQuestions();
				//initialize the list of deleteable questions
			    List<AggregateSurveyQuestionItem> aggregateSurveyQuestionItems=new ArrayList<AggregateSurveyQuestionItem>();
				for(SurveyQuestion surveyQuestion : surveyQuestionList){
					if(surveyQuestion instanceof AggregateSurveyQuestion){
					aggregateSurveyQuestionItems = surveyService.getAggregateSurveyQuestionItemsByQuestionId(surveyQuestion.getId());
					for(AggregateSurveyQuestionItem aggregateSurveyQuestionItem : aggregateSurveyQuestionItems){
							surveyQuestions.add(aggregateSurveyQuestionItem.getQuestion());
					}
					} 
				}
					
				form.setSurveyQuestionList(surveyQuestions);
				Collections.sort(surveyQuestionList);
				form.setSurveyQuestions(surveyQuestionList);
				int length = surveyQuestionList.size();
				if(length>0) {
					SurveyQuestion surveyQuestion = surveyQuestionList.get(length-1);
					int displayOrder = surveyQuestion.getDisplayOrder();
					form.setDisplayQuestionOrder(displayOrder+1);
				}else {
					form.setDisplayQuestionOrder(0);
				}
				form.setSurveyEvent(survey.getEvent());
				form.setPublished(survey.getStatus().equalsIgnoreCase(Survey.PUBLISHED));
				form.setLocked(survey.getIsLocked());
				if (survey.getIsShowAll() == Boolean.FALSE) {
					form.setAllQuestionPerPage(false);
					form.setQuestionsPerPage(survey.getQuestionsPerPage()+"");
				}
				List<SurveyCourse> selectedCourses = new ArrayList<SurveyCourse>();
				for(Course course:survey.getCourses()){
					SurveyCourse surveyCourse = new SurveyCourse();
					surveyCourse.setSelected(true);
					surveyCourse.setCourse(course);
					selectedCourses.add(surveyCourse);
				}
				form.setSurveyCourses(selectedCourses);

			}else if(methodName.equals("saveQuestionInList") 
					|| methodName.equals("saveEditSurveyQuestionInList")){ 
				//extra binding work for the save questions functionality
				String questionType = form.getSurveyQuestionType();
				if(questionType.equalsIgnoreCase(SURVEY_QUESTION_MULTIPLE_CHOICE_MULTIPLE_SELECT)){
					MultipleSelectSurveyQuestion question = form.getCurrentMultipleSelectSurveyQuestion();
					question.setRequired(form.isSurveyQuestionRequired());
					question.setDisplayOrder(form.getDisplayQuestionOrder());
					form.setDisplayQuestionOrder(form.getDisplayQuestionOrder()+1);
					readSurveyAnswerChoices(question);
				}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_MULTIPLE_CHOICE_SINGLE_SELECT)){
					SingleSelectSurveyQuestion question = form.getCurrentSingleSelectSurveyQuestion();
					question.setRequired(form.isSurveyQuestionRequired());
					question.setDisplayOrder(form.getDisplayQuestionOrder());
					form.setDisplayQuestionOrder(form.getDisplayQuestionOrder()+1);
					readSurveyAnswerChoices(question);
				}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_DROP_DOWN_SINGLE_SELECT)){
					SingleSelectSurveyQuestion question = form.getCurrentSingleSelectSurveyQuestion();
					question.setRequired(form.isSurveyQuestionRequired());
					question.setDisplayOrder(form.getDisplayQuestionOrder());
					form.setDisplayQuestionOrder(form.getDisplayQuestionOrder()+1);
					readSurveyAnswerChoices(question);
				}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_TEXT_BOX_256_CHARACTERS)){
					TextBoxSurveyQuestion question = form.getCurrentFillInTheBlankSurveyQuestion();
					question.setRequired(form.isSurveyQuestionRequired());
					question.setDisplayOrder(form.getDisplayQuestionOrder());
					form.setDisplayQuestionOrder(form.getDisplayQuestionOrder()+1);
				}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_TEXT_BOX_UNLIMITED)){
					TextBoxSurveyQuestion question = form.getCurrentFillInTheBlankSurveyQuestion();
					question.setRequired(form.isSurveyQuestionRequired());
					question.setDisplayOrder(form.getDisplayQuestionOrder());
					form.setDisplayQuestionOrder(form.getDisplayQuestionOrder()+1);
				}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_MULTIPLE_CHOICE_RATING_SELECT)){
					// do noting
				}else if ( questionType.equalsIgnoreCase(SURVEY_QUESTION_PERSONAL_INFORMATION) ) {
					PersonalInformationSurveyQuestion question = form.getCurrentPersonalInformationQuestion();
					question.setDisplayOrder(form.getDisplayQuestionOrder());
					form.setDisplayQuestionOrder(form.getDisplayQuestionOrder()+1);
				}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_CUSTOM)){
					AggregateSurveyQuestion question = form.getCurrentCustomSurveyQuestion();
					question.setDisplayOrder(form.getDisplayQuestionOrder());
					form.setDisplayQuestionOrder(form.getDisplayQuestionOrder()+1);
				}
			} else if(methodName.equals("showAdminSurveyView")) {
				Survey survey = surveyService.getSurveyByID(form.getSid());

				List<SurveyLink> surveyLinks=surveyService.getSurveyLinksById(form.getSid());

				form.setSurveyName(survey.getName());
				List<SurveyQuestion> surveyQuestionList = survey.getQuestions();
				Collections.sort(surveyQuestionList);
				form.setSurveyQuestions(surveyQuestionList);
				int length = surveyQuestionList.size();
				if(length>0) {
					SurveyQuestion surveyQuestion = surveyQuestionList.get(length-1);
					int displayOrder = surveyQuestion.getDisplayOrder();
					form.setDisplayQuestionOrder(displayOrder+1);
				}else {
					form.setDisplayQuestionOrder(0);
				}
				form.setSurveyEvent(survey.getEvent());
				form.setPublished(survey.getStatus().equalsIgnoreCase(Survey.PUBLISHED));
				form.setLocked(survey.getIsLocked());
				if (survey.getIsShowAll() == Boolean.FALSE) {
					form.setAllQuestionPerPage(false);
					form.setQuestionsPerPage(survey.getQuestionsPerPage()+"");
				}
				List<SurveyCourse> selectedCourses = new ArrayList<SurveyCourse>();
				for(Course course:survey.getCourses()){
					SurveyCourse surveyCourse = new SurveyCourse();
					surveyCourse.setSelected(true);
					surveyCourse.setCourse(course);
					selectedCourses.add(surveyCourse);
				}
				form.setSurveyCourses(selectedCourses);

				if(surveyLinks != null && surveyLinks.size()>0){
					String linkvalues="";
					form.setLinks(true);
					for(SurveyLink surveyLink:surveyLinks){
						linkvalues=linkvalues+ surveyLink.getUrlName() + "\n";
					}
					form.setLinksValue(linkvalues);
				}
				form.setAllowAnonymousResponse(survey.isAllowAnonymousResponse());
				form.setElectronicSignature(survey.isElectronicSignatureRequire());
				form.setRememberPriorResponse(survey.isRememberPriorResponse());
				form.setElectronicSignatureValue(survey.getElectronicSignature());
				form.setReadOnly(survey.isReadonly());
			}

		}
	}

	/**
	 * Callback method for doing validations before the processing method is executed.
	 * Be sure to check if there are any errors before doing anything
	 * in the processing method
	 */
	protected void validate(HttpServletRequest request, Object command, BindException errors, String methodName) throws Exception {
		// implement the validations based on the method name
		AdminSurveyForm form = (AdminSurveyForm)command;
		AdminSurveyValidator validator = (AdminSurveyValidator)this.getValidator();	
		//validation before saving the question just entered
		//if(methodName.equals("saveQuestionInList") || methodName.equals("saveEditSurveyQuestionInList") ){
		//	validator.validateQuestion(form, errors);
		//}else 
		if(methodName.equals("saveSurveyInfoAndDisplaySurveys")){
			validator.validateFirstPage(form, errors);
		}else if(methodName.equals("updateSurveyCoursesAndDisplaySurveys")){
			validator.validateFinishPage(form, errors);
		}else if(methodName.equals("saveQuestionAndDisplaySurveys")){
			validator.validateSecondPage(form, errors);
		}
	}


	/**
	 * handler methods
	 */
	public ModelAndView displaySurveys(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		VU360UserAuthenticationDetails details = null;
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
			 details = (VU360UserAuthenticationDetails)auth.getDetails();
			if(details.getCurrentMode()==VU360UserMode.ROLE_LMSADMINISTRATOR){
				if(details.getCurrentSearchType()!=AdminSearchType.CUSTOMER){
					if(details.getCurrentSearchType()!=AdminSearchType.DISTRIBUTOR){
						return new ModelAndView(failureTemplate,"isRedirect","customerDistributor");
					}
				}
			}
		}
		
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		if(details.getCurrentSearchType()!=AdminSearchType.DISTRIBUTOR){
			
			
			context.put("showCopyLink", true);
		}
		else{
			context.put("showCopyLink", false);
		}
		
		return new ModelAndView(defaultManageSurveysTemplate, "context",context);
	}

	public ModelAndView searchSurvey( HttpServletRequest request, HttpServletResponse response, 
			Object command, BindException errors ) throws Exception {
		log.debug("in searchSurvey");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer customer = null;

		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
			if( details.getCurrentCustomer() != null && details.getCurrentSearchType()==AdminSearchType.CUSTOMER || details.getCurrentSearchType()==AdminSearchType.DISTRIBUTOR){
				customer = details.getCurrentCustomer();
			}else{
				return new ModelAndView(failureTemplate,"isRedirect","cd");
			}
		} else {
			// admin has not selected any customer
			return new ModelAndView(failureTemplate,"isRedirect","c");
		}
		
		HttpSession session = request.getSession();
		String surveyName = request.getParameter("searchSurveyName");
		String surveyStatus = request.getParameter("status");
		String sortDirection = request.getParameter("sortDirection");
		if( (StringUtils.isBlank(sortDirection)) && session.getAttribute("sortDirection") != null )
			sortDirection = session.getAttribute("sortDirection").toString();
		String sortColumnIndex = request.getParameter("sortColumnIndex");
		if( (StringUtils.isBlank(sortColumnIndex)) && session.getAttribute("sortColumnIndex") != null )
			sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
		String showAll = "true";//request.getParameter("showAll"); //commented for LMS:7126
		//if ( showAll == null ) showAll = "false";
		String pageIndex = request.getParameter("pageCurrIndex");
		if( pageIndex == null ) pageIndex = "0";
		log.debug("pageIndex = " + pageIndex);
		String isRetire = request.getParameter("retire");
		String isEditable = request.getParameter("editable");
		surveyName = (surveyName == null)? "" : surveyName.trim();
		surveyStatus = (surveyStatus == null || surveyStatus.equalsIgnoreCase("All"))? "" : surveyStatus.trim();
		isRetire = (isRetire == null)? "false" : isRetire.trim();
		isEditable = (isEditable == null)? "false" : isEditable.trim();
		customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentCustomer();
		Distributor distributor = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentDistributor();
		Map<Object, Object> context = new HashMap<Object, Object>();
		List<Survey> surveyList = new ArrayList<Survey>();
		String paging = request.getParameter("paging");
		context.put("showAll", showAll);
		context.put("searchSurveyName", surveyName);
		context.put("import", request.getAttribute("import"));

		Map<String, Object> PagerAttributeMap = new HashMap <String, Object>();
		PagerAttributeMap.put("pageIndex",pageIndex);

		if (StringUtils.isNotBlank(paging) && paging.equalsIgnoreCase(MANAGE_SURVEY_PAGING_ACTION)) {
			surveyName = (String)session.getAttribute("surveyName");
			surveyStatus = (String)session.getAttribute("surveyStatus");
			isRetire = (String)session.getAttribute("isRetire");
			isEditable = (String)session.getAttribute("isEditable");
			session.setAttribute("surveyName", surveyName);
			session.setAttribute("surveyStatus", surveyStatus);
			session.setAttribute("isRetire", isRetire);
			session.setAttribute("isEditable", isEditable);
			
			context.put("surveyName", surveyName);
			context.put("surveyStatus", surveyStatus);
			context.put("isRetire", isRetire);
			context.put("isEditable", isEditable);
		} else {
			if( request.getParameter("searchSurveyName") != null &&
					!StringUtils.isBlank(request.getParameter("searchSurveyName")) ) {
				surveyName = request.getParameter("searchSurveyName");
			} 
			session.setAttribute("surveyName", surveyName);
			session.setAttribute("surveyStatus", surveyStatus);
			session.setAttribute("isRetire", isRetire);
			session.setAttribute("isEditable", isEditable);
			
			context.put("surveyName", surveyName);
			context.put("surveyStatus", surveyStatus);
			context.put("isRetire", isRetire);
			context.put("isEditable", isEditable);
		}

		if( distributor != null && ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentMode().equals(VU360UserMode.ROLE_LMSADMINISTRATOR) ) {
			surveyList = (List<Survey>) surveyService.getSurveyByName(distributor,surveyName,surveyStatus,isRetire,isEditable);
		} else {
			surveyList = (List<Survey>) surveyService.getSurveyByName(customer,surveyName,surveyStatus,isRetire,isEditable); //??? all surveys...no filters???
		}

		/** Added by Dyutiman...
		 *  manual sorting
		 */
		request.setAttribute("PagerAttributeMap", PagerAttributeMap);

		if( StringUtils.isNotBlank(sortDirection) && StringUtils.isNotBlank(sortColumnIndex) ) {
			// sorting against survey name
			if( sortColumnIndex.equalsIgnoreCase("0") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					for( int i = 0 ; i < surveyList.size() ; i ++ ) {
						for( int j = 0 ; j < surveyList.size() ; j ++ ) {
							if( i != j ) {
								Survey survey1 = (Survey) surveyList.get(i);
								Survey survey2 = (Survey) surveyList.get(j);
								if( survey1.getName().toUpperCase().compareTo
										(survey2.getName().toUpperCase()) > 0 ) {
									Survey tempLG = surveyList.get(i);
									surveyList.set(i, surveyList.get(j));
									surveyList.set(j, tempLG);
								}
							}
						}
					}
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 0);
				} else {
					for( int i = 0 ; i < surveyList.size() ; i ++ ) {
						for( int j = 0 ; j < surveyList.size() ; j ++ ) {
							if( i != j ) {
								Survey survey1 = (Survey) surveyList.get(i);
								Survey survey2 = (Survey) surveyList.get(j);
								if( survey1.getName().toUpperCase().compareTo
										(survey2.getName().toUpperCase()) < 0 ) {
									Survey tempLG = surveyList.get(i);
									surveyList.set(i, surveyList.get(j));
									surveyList.set(j, tempLG);
								}
							}
						}
					}
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 0);
				}
				// sorting against survey status	
			} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					for( int i = 0 ; i < surveyList.size() ; i ++ ) {
						for( int j = 0 ; j < surveyList.size() ; j ++ ) {
							if( i != j ) {
								Survey survey1 = (Survey) surveyList.get(i);
								Survey survey2 = (Survey) surveyList.get(j);
								if( survey1.getStatus().toUpperCase().compareTo
										(survey2.getStatus().toUpperCase()) > 0 ) {
									Survey tempLG = surveyList.get(i);
									surveyList.set(i, surveyList.get(j));
									surveyList.set(j, tempLG);
								}
							}
						}
					}
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 1);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 1);
				} else {
					for( int i = 0 ; i < surveyList.size() ; i ++ ) {
						for( int j = 0 ; j < surveyList.size() ; j ++ ) {
							if( i != j ) {
								Survey survey1 = (Survey) surveyList.get(i);
								Survey survey2 = (Survey) surveyList.get(j);
								if( survey1.getStatus().toUpperCase().compareTo
										(survey2.getStatus().toUpperCase()) < 0 ) {
									Survey tempLG = surveyList.get(i);
									surveyList.set(i, surveyList.get(j));
									surveyList.set(j, tempLG);
								}
							}
						}
					}
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 1);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 1);
				}
				// sorting against survey lock status	
			} else if( sortColumnIndex.equalsIgnoreCase("2") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					for( int i = 0 ; i < surveyList.size() ; i ++ ) {
						for( int j = 0 ; j < surveyList.size() ; j ++ ) {
							if( i != j ) {
								Survey survey1 = (Survey) surveyList.get(i);
								//Survey survey2 = (Survey) surveyList.get(j);
								if( survey1.getIsLocked() ) {
									Survey tempLG = surveyList.get(i);
									surveyList.set(i, surveyList.get(j));
									surveyList.set(j, tempLG);
								}
							}
						}
					}
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 2);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 2);
				} else {
					for( int i = 0 ; i < surveyList.size() ; i ++ ) {
						for( int j = 0 ; j < surveyList.size() ; j ++ ) {
							if( i != j ) {
								//Survey survey1 = (Survey) surveyList.get(i);
								Survey survey2 = (Survey) surveyList.get(j);
								if( survey2.getIsLocked() ) {
									Survey tempLG = surveyList.get(i);
									surveyList.set(i, surveyList.get(j));
									surveyList.set(j, tempLG);
								}
							}
						}
					}
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 2);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 2);
				}
			}
		}
		context.put("allSurveyList", surveyList);
		
		VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
		if(details.getCurrentSearchType()==AdminSearchType.DISTRIBUTOR){
			
			
			context.put("showCopyLink", true);
		}
		else{
			context.put("showCopyLink", false);
		}
		
		
		return new ModelAndView(defaultManageSurveysTemplate, "context",context);
	}


	public ModelAndView deleteSurvey(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		if  ( request.getParameterValues("survey") != null ) {
			long[] id = new long[request.getParameterValues("survey").length];
			String []checkID = request.getParameterValues("survey");
			for (int i=0;i<id.length;i++) {
				id[i] = Long.valueOf(checkID[i]);
				surveyService.deleteSurveys(id);
			}
		}
		Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentCustomer();
		Distributor distributor = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentDistributor();
		List<Survey> surveyList = new ArrayList<Survey>();
		if(distributor !=null && ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentMode().equals(VU360UserMode.ROLE_LMSADMINISTRATOR)){
			surveyList = (List<Survey>) surveyService.getAllSurvey(distributor);
		}else{
			surveyList = (List<Survey>) surveyService.getAllSurvey(customer); //??? all surveys...no filters???
		}
		context.put("allSurveyList", surveyList);
		return new ModelAndView(defaultManageSurveysTemplate, "context",context);
	}




	public ModelAndView addNewSurvey( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors )
	throws Exception {

		AdminSurveyForm adminSurveyForm = (AdminSurveyForm)command;
		adminSurveyForm.setSurveyName("");
		adminSurveyForm.setPublished(false);
		adminSurveyForm.setQuestionsPerPage("");
		adminSurveyForm.setRememberPriorResponse(false);
		adminSurveyForm.setAllowAnonymousResponse(false);
		adminSurveyForm.setElectronicSignature(false);
		adminSurveyForm.setElectronicSignatureValue("");
		adminSurveyForm.setLinks(false);
		adminSurveyForm.setLinksValue("");
		adminSurveyForm.setSid(0);
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("surveyEvents", Survey.SURVEY_EVENTS);
		Distributor distributor = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentDistributor();
		if (distributor != null) {
			context.put("role", "distributor");
		} else {
			context.put("role", "");
		}
		return new ModelAndView(addNewSurveysTemplate, "context", context);
	}

	public ModelAndView saveNewSurvey( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors )
	throws Exception {
		Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentCustomer();
		Distributor distributor = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentDistributor();
		Survey newSurvey = new Survey();

		AdminSurveyForm adminSurveyForm = (AdminSurveyForm)command;
		newSurvey.setName(adminSurveyForm.getSurveyName());
		newSurvey.setEvent(adminSurveyForm.getSurveyEvent());
		if( adminSurveyForm.isPublished() ) {
			newSurvey.setStatus(Survey.PUBLISHED);
		}else{
			newSurvey.setStatus(Survey.NOTPUBLISHED);
		}
		if( !adminSurveyForm.isAllQuestionPerPage() ) {
			newSurvey.setIsShowAll(false);
			newSurvey.setQuestionsPerPage(Integer.parseInt(adminSurveyForm.getQuestionsPerPage()));
		}
		newSurvey.setRememberPriorResponse(adminSurveyForm.isRememberPriorResponse());
		newSurvey.setAllowAnonymousResponse(adminSurveyForm.isAllowAnonymousResponse());
		if( adminSurveyForm.isElectronicSignature() ) {
			newSurvey.setElectronicSignatureRequire(adminSurveyForm.isElectronicSignature());
			newSurvey.setElectronicSignature(adminSurveyForm.getElectronicSignatureValue());
		}

		newSurvey.setLinkSelected(adminSurveyForm.isLinks());

		if( distributor == null ) {
			customer.initializeOwnerParams();
			newSurvey.setOwner(customer);
		}else{
			distributor.initializeOwnerParams();
			newSurvey.setOwner(distributor);
		}
		newSurvey.setReadonly(adminSurveyForm.isEditable());
		//surveyService.addSurvey(newSurvey,surveyLink);
		newSurvey = surveyService.addSurvey(newSurvey);
		List<SurveyLink> surveyLinks = null;
		if( adminSurveyForm.isLinks() ) {
			String links = adminSurveyForm.getLinksValue();
			surveyLinks = this.readSurveyLinks(links, newSurvey);
			surveyService.saveSurveyLinkList(surveyLinks);
		}
		//context.put("target", "displaySurveys");
		//return new ModelAndView(redirectTemplate,"context",context);
		return new ModelAndView(redirectTemplate);
	}

	private List<SurveyLink> readSurveyLinks( String links, Survey survey) {

		String str;
		int i = 0;
		BufferedReader reader = new BufferedReader(new StringReader(links));
		try {
			List<SurveyLink> surveyLinkList = new ArrayList<SurveyLink>();
			while ((str = reader.readLine()) != null) {
				if (str.length() > 0){
					if(!StringUtils.isBlank(str)) {
						SurveyLink link = new SurveyLink();
						link.setUrlName(str);
						link.setDisplayOrder(i++);
						link.setSurvey(survey);
						surveyLinkList.add(link);
					}
				}
			}
			return surveyLinkList;

		} catch(IOException e) {
			e.printStackTrace();
		}		
		return null;
	}

	public ModelAndView showAdminSurveyView( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors )
	throws Exception {

		Map<String, Object> context = new HashMap<String, Object>();
		context.put("surveyEvents", Survey.SURVEY_EVENTS);
		Distributor distributor = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentDistributor();
		if (distributor != null) {
			context.put("role", "distributor");
		} else {
			context.put("role", "");
		}
		return new ModelAndView(addNewSurveysTemplate, "context", context);
	}
	
	public ModelAndView saveAdminSurveyView( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors )
	throws Exception {
		
		Map<String, Object> context = new HashMap<String, Object>();
		AdminSurveyForm adminSurveyForm = (AdminSurveyForm)command;
		
		Survey survey = surveyService.loadForUpdateSurvey(adminSurveyForm.getSid());
		Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentCustomer();
		Distributor distributor = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentDistributor();
		
		if(distributor==null && survey.getOwner() instanceof Distributor){
			Survey newSurvey = new Survey();
			customer.initializeOwnerParams();
			newSurvey.setOwner(customer);
			
			newSurvey.setName(adminSurveyForm.getSurveyName());
			newSurvey.setEvent(adminSurveyForm.getSurveyEvent());
			if(adminSurveyForm.isPublished()){
				newSurvey.setStatus(Survey.PUBLISHED);
			}else{
				newSurvey.setStatus(Survey.NOTPUBLISHED);
			}
			
			if(!adminSurveyForm.isAllQuestionPerPage()){
				newSurvey.setIsShowAll(false);
				newSurvey.setQuestionsPerPage(Integer.parseInt(adminSurveyForm.getQuestionsPerPage()));
			}else {
				newSurvey.setIsShowAll(true);
			}
			
			newSurvey.setRememberPriorResponse(adminSurveyForm.isRememberPriorResponse());
			newSurvey.setAllowAnonymousResponse(adminSurveyForm.isAllowAnonymousResponse());
			if(adminSurveyForm.isElectronicSignature()) {
				newSurvey.setElectronicSignatureRequire(adminSurveyForm.isElectronicSignature());
				newSurvey.setElectronicSignature(adminSurveyForm.getElectronicSignatureValue());
			}
			newSurvey.setLinkSelected(adminSurveyForm.isLinks());
			newSurvey.setReadonly(adminSurveyForm.isEditable());
			surveyService.addSurvey(newSurvey); //!!!the service function name should be save survey
			
			List<SurveyLink> surveyLinks = null;
			if(adminSurveyForm.isLinks()) {
				String links = adminSurveyForm.getLinksValue();
				surveyLinks = this.readSurveyLinks(links, newSurvey);
				surveyService.saveSurveyLinkList(surveyLinks);
			}
			
		}else{
			survey.setName(adminSurveyForm.getSurveyName());
			survey.setEvent(adminSurveyForm.getSurveyEvent());
			if(adminSurveyForm.isPublished()){
				survey.setStatus(Survey.PUBLISHED);
			}else{
				survey.setStatus(Survey.NOTPUBLISHED);
			}

			if(!adminSurveyForm.isAllQuestionPerPage()){
				survey.setIsShowAll(false);
				survey.setQuestionsPerPage(Integer.parseInt(adminSurveyForm.getQuestionsPerPage()));
			}else {
				survey.setIsShowAll(true);
			}
			
			survey.setRememberPriorResponse(adminSurveyForm.isRememberPriorResponse());
			survey.setAllowAnonymousResponse(adminSurveyForm.isAllowAnonymousResponse());
			if(adminSurveyForm.isElectronicSignature()) {
				survey.setElectronicSignatureRequire(adminSurveyForm.isElectronicSignature());
				survey.setElectronicSignature(adminSurveyForm.getElectronicSignatureValue());
			}
			
			survey.setLinkSelected(adminSurveyForm.isLinks());
			survey.setReadonly(adminSurveyForm.isEditable());
			survey=	surveyService.addSurvey(survey); //!!!the service function name should be save survey
			List<SurveyLink> surveyLinks = null;
			if(adminSurveyForm.isLinks()) {
				String links = adminSurveyForm.getLinksValue();
				surveyLinks = this.readSurveyLinks(links, survey);
				surveyService.saveSurveyLinkList(surveyLinks);
			}
		}
		adminSurveyForm.setSid(0);
		context.put("target", "displaySurveys");
		return new ModelAndView(redirectTemplate,"context",context);
	}
	
	
	public ModelAndView createTrainingNeedsAnalysis( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors )
	throws Exception {
		AdminSurveyForm form = (AdminSurveyForm)command;
		if  ( request.getParameterValues("survey") != null ) {
			long[] ids = new long[request.getParameterValues("survey").length];
			String []checkID = request.getParameterValues("survey");
			for (int i=0;i<ids.length;i++) {
				ids[i] = Long.valueOf(checkID[i]);
			}
			List<SuggestedTraining> suggTrainings = surveyService.getSuggestedTrainingsBySurveyIDs(ids);
			form.setSuggTrainings(suggTrainings);
		}
		return new ModelAndView(trainingPlanAnalysisTemplate);
	}

	public ModelAndView showTrainingPlanAnalysisDetails( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors )
	throws Exception {

		AdminSurveyForm form = (AdminSurveyForm)command;
		Map<String, Object> map;
		Map<String, Object> context = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> values = new ArrayList <Map<String, Object>>();

		for( Survey s : form.getSelectedSurveys() ) {
			map = new HashMap<String, Object>();
			map.put("surveyName", s.getName());
			String ownerName = "";
			if( s.getOwner() instanceof ContentOwner ) {
				ownerName = ((ContentOwner)s.getOwner()).getName();
			} else if( s.getOwner() instanceof Customer ) {
				ownerName = ((Customer)s.getOwner()).getName();
			} else if( s.getOwner() instanceof Distributor ) {
				ownerName = ((Distributor)s.getOwner()).getName();
			}
			map.put("ownerName", ownerName);
			map.put("courses", s.getCourses());
			values.add(map);
		}
		context.put("values", values);
		return new ModelAndView(trainingPlanAnalysisDetailsTemplate, "context", context);
	}

	/**
	 * handler methods for edit survey information
	 */
	public ModelAndView showSurveyInfoView(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("surveyEvents", Survey.SURVEY_EVENTS);
		return new ModelAndView(editSurveyInfoTemplate, "context", context);
	}

	@SuppressWarnings("unchecked")
	public ModelAndView saveSurveyInfoAndDisplaySurveys(HttpServletRequest request, 
			HttpServletResponse response, Object command, BindException errors) throws Exception {

		// save the questions that were added to the form.question list but was not added to db
		if( errors.hasErrors() ) {
			Map<Object, Object> context = new HashMap<Object, Object>();
			context.put("surveyEvents", Survey.SURVEY_EVENTS);
			return new ModelAndView(editSurveyInfoTemplate, "context", context);
		}
		AdminSurveyForm adminSurveyForm = (AdminSurveyForm)command;
		Survey survey = surveyService.loadForUpdateSurvey(adminSurveyForm.getSid());

		survey.setName(adminSurveyForm.getSurveyName());
		survey.setEvent(adminSurveyForm.getSurveyEvent());
		
		if(adminSurveyForm.isPublished()){
			survey.setStatus(Survey.PUBLISHED);
		}else{
			survey.setStatus(Survey.NOTPUBLISHED);
		}

		if(!adminSurveyForm.isAllQuestionPerPage()){
			survey.setIsShowAll(false);
			survey.setQuestionsPerPage(Integer.parseInt(adminSurveyForm.getQuestionsPerPage()));
		}else {
			survey.setIsShowAll(true);
		}
		surveyService.addSurvey(survey); //!!!the service function name should be save survey

		//redirect to the display survey page
		Map context = new HashMap();
		context.put("target", "displaySurveys");
		return new ModelAndView(redirectTemplate,"context",context);
	}

	@SuppressWarnings("unchecked")
	public ModelAndView cancelSurveyInfoAndDisplaySurveys(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		//redirect to the display survey page
		Map context = new HashMap();
		AdminSurveyForm form = (AdminSurveyForm)command;
		form.setSid(0);
		context.put("target", "displaySurveys");
		return new ModelAndView(redirectTemplate,"context",context);
	}

	/**
	 * Brings up the questions list page for the selected survey
	 * 
	 */
	public ModelAndView questionView(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		 AdminSurveyForm form = (AdminSurveyForm)command;
		 flag=false;
		 return new ModelAndView("redirect:adm_manageSyrvey.do?method=showQuestionView&sid="+form.getSid()); 
	 }
	
	public ModelAndView showQuestionView(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		AdminSurveyForm form = (AdminSurveyForm)command;
		HttpSession session = request.getSession();
	 	if(!flag){
	 	session.setAttribute("isShowAll", "0");
	 	}
		List<SurveyQuestion> surveyQuestions = form.getSurveyQuestions();
     	List<Long> answeredSurveyQyestionId = new ArrayList<Long>();
         //initialize the list of deleteable questions
     nextItem : for(int i=0;i<form.getSurveyQuestionList().size();i++){
        if(form.getSurveyQuestionList().get(i)!=null){
             for(int k=0;k<form.getSurveyQuestions().size();k++){
                     if(((form.getSurveyQuestions().get(k).getId()).equals(form.getSurveyQuestionList().get(i).getId()))){
                             surveyQuestions.remove(form.getSurveyQuestions().get(k));
                             continue  nextItem;
                     }
             }
             }
     }
     form.setSurveyQuestions(surveyQuestions);
     for(SurveyQuestion surveyQuestion : surveyQuestions){
             if(surveyService.getSurveyResultAnswerBySurveyQuestionId(surveyQuestion.getId())!=null){
                     answeredSurveyQyestionId.add(surveyQuestion.getId());
             }
     }
     form.setAnsweredSurveyQyestionId(answeredSurveyQyestionId);
         int numberOfQuestions = form.getSurveyQuestions().size();
         List<Boolean> deleteableQuestions = new ArrayList<Boolean>();
         for(int i=0; i<numberOfQuestions; i++)
                 deleteableQuestions.add(new Boolean(false));
         form.setDeleteableQuestions(deleteableQuestions);
         return new ModelAndView(addQuestionTemplate);
 }

	public ModelAndView addNewQuestionView(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        Map<Object, Object> context = new HashMap<Object, Object>();
        context.put("surveyQuestionTypes", SURVEY_QUESTION_TYPES);
        AdminSurveyForm form = (AdminSurveyForm)command;
		HttpSession session = request.getSession();
        flag=false;
		session.setAttribute("isShowAll", "0");
        form.setEditableQuestionId(0);
        form.setSurveyQuestionText("");
        form.setSurveyQuestionType(SURVEY_QUESTION_MULTIPLE_CHOICE_MULTIPLE_SELECT);
        form.setCurrentMultipleSelectSurveyQuestion(null);
        form.setCurrentSingleSelectSurveyQuestion(null);
        form.setCurrentFillInTheBlankSurveyQuestion(null);
        form.setCurrentPersonalInformationQuestion(null);
        form.setSurveyQuestionRequired(false);
        form.setAllQuestionPerPage(false);
        form.setCustomQuestionResponceTypes( new ArrayList <String>());
        form.setResponceLabels(new ArrayList <String>());
        form.setAnswerChoices(new ArrayList <String>());
        form.setIsMultiline(new ArrayList <Boolean>());
        form.setResRequired(new ArrayList <Boolean>());
        form.setCurrentMultipleSelectSurveyQuestion(null);
        form.setCurrentSingleSelectSurveyQuestion(null);
        form.setCurrentFillInTheBlankSurveyQuestion(null);
        NUMBER_OF_CUSTOM_RESPONCES=1;
        edtiableFlag=1;
        enableAddRemove=1;
        return this.addQuestionView(request, response, command, errors);
    }

	/**
	 * setup the question type in the combobox for adding questions
	 * Also used to display the other add Q forms when 
	 * the question type is changed on the same page
	 */
	public ModelAndView addQuestionView(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) 
	throws Exception {

		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("surveyQuestionTypes", SURVEY_QUESTION_TYPES);
		
			if( StringUtils.isBlank(request.getParameter("addRemove")) ) {
				NUMBER_OF_CUSTOM_RESPONCES = 1;
				
			}
		
		if(NUMBER_OF_CUSTOM_RESPONCES < 1){
			NUMBER_OF_CUSTOM_RESPONCES=1;
		}
		context.put("resNumber", NUMBER_OF_CUSTOM_RESPONCES);
		AdminSurveyForm form = (AdminSurveyForm)command;
		if(request.getParameter("type")!=null){
			if(request.getParameter("type").equals("add")){
				form.setSurveyQuestionText("");
			}
			}
		//setting the default question type if there is none
		if(StringUtils.isBlank(form.getSurveyQuestionType())){
			form.setSurveyQuestionType(SURVEY_QUESTION_MULTIPLE_CHOICE_MULTIPLE_SELECT);
		}

		if( form.getSurveyQuestionType().equalsIgnoreCase(SURVEY_QUESTION_MULTIPLE_CHOICE_MULTIPLE_SELECT) ) {
			MultipleSelectSurveyQuestion question = new MultipleSelectSurveyQuestion();
			form.setSurveyQuestionRequired(false);
			form.setCurrentMultipleSelectSurveyQuestion(question);
		} else if ( form.getSurveyQuestionType().equalsIgnoreCase(SURVEY_QUESTION_MULTIPLE_CHOICE_SINGLE_SELECT) ) {
			SingleSelectSurveyQuestion question = new SingleSelectSurveyQuestion();
			form.setSurveyQuestionRequired(false);
			form.setCurrentSingleSelectSurveyQuestion(question);
		} else if ( form.getSurveyQuestionType().equalsIgnoreCase(SURVEY_QUESTION_DROP_DOWN_SINGLE_SELECT) ) {
			SingleSelectSurveyQuestion question = new SingleSelectSurveyQuestion();
			form.setSurveyQuestionRequired(false);
			form.setCurrentSingleSelectSurveyQuestion(question);
		} else if ( form.getSurveyQuestionType().equalsIgnoreCase(SURVEY_QUESTION_TEXT_BOX_256_CHARACTERS) ) {
			TextBoxSurveyQuestion question = new TextBoxSurveyQuestion();
			form.setSurveyQuestionRequired(false);
			form.setCurrentFillInTheBlankSurveyQuestion(question);
		} else if( form.getSurveyQuestionType().equalsIgnoreCase(SURVEY_QUESTION_TEXT_BOX_UNLIMITED) ) {
			TextBoxSurveyQuestion question = new TextBoxSurveyQuestion();
			form.setSurveyQuestionRequired(false);
			form.setCurrentFillInTheBlankSurveyQuestion(question);
		} else if ( form.getSurveyQuestionType().equalsIgnoreCase(SURVEY_QUESTION_MULTIPLE_CHOICE_RATING_SELECT) ) {

		} else if ( form.getSurveyQuestionType().equalsIgnoreCase(SURVEY_QUESTION_PERSONAL_INFORMATION) ) {
			PersonalInformationSurveyQuestion question = new PersonalInformationSurveyQuestion();
			form.setCurrentPersonalInformationQuestion(question);
			List<AvailablePersonalInformationfieldItem> personalInfoItems = surveyService.getAllAvailablePersonalInformationfields();
			List<ManagePersonalInformation> mngPersonalInfos = new ArrayList<ManagePersonalInformation>();
			for (AvailablePersonalInformationfieldItem item : personalInfoItems) {
				ManagePersonalInformation personalInfo = new ManagePersonalInformation();
				personalInfo.setPersonalInfoItem(item);
				mngPersonalInfos.add(personalInfo);
			}
			form.setMngPersonalInfos(mngPersonalInfos);

		} else if ( form.getSurveyQuestionType().equalsIgnoreCase(SURVEY_QUESTION_CUSTOM) ) {

			context.put("customResponceTypes", CUSTOM_RESPONCE_TYPES);
			
			if( form.getCustomQuestionResponceTypes().size() != NUMBER_OF_CUSTOM_RESPONCES ) {
				List<String> responceTypes = form.getCustomQuestionResponceTypes();
				List<String> responceLabels = form.getResponceLabels();
				List<String> ansChoices = form.getAnswerChoices();
				//form.setIsMultiline(new ArrayList <Boolean>());
				List<Boolean> isMulti = form.getIsMultiline();
				List<Boolean> isRequired = form.getResRequired();
				for( int i = form.getCustomQuestionResponceTypes().size() ; i < NUMBER_OF_CUSTOM_RESPONCES ; i ++ ) {
					responceTypes.add(form.getCustomQuestionResponceTypes().size(), SURVEY_CUSTOM_RESPONCE_MULTIPLE_SELECT);
					responceLabels.add(form.getResponceLabels().size(), "");
					ansChoices.add(form.getAnswerChoices().size(), "");
					isMulti.add(form.getIsMultiline().size(), Boolean.FALSE);
					isRequired.add(form.getResRequired().size(), Boolean.FALSE);
				}
				form.setCustomQuestionResponceTypes(responceTypes);
				form.setResponceLabels(responceLabels);
				form.setAnswerChoices(ansChoices);
				form.setIsMultiline(isMulti);
				form.setResRequired(isRequired);
			}
			AggregateSurveyQuestion question = new AggregateSurveyQuestion();
			form.setCurrentCustomSurveyQuestion(question);
		}
		return new ModelAndView(addQuestionContainerTemplate, "context", context);
	}

	public ModelAndView addRemoveResponces(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) 
	throws Exception {
		if( ! StringUtils.isBlank(request.getParameter("addRemove")) ) {
			if( request.getParameter("addRemove").equals("add") )
				NUMBER_OF_CUSTOM_RESPONCES++;
			else if( request.getParameter("addRemove").equals("remove") )
				NUMBER_OF_CUSTOM_RESPONCES--;
		}
		return this.addQuestionView(request, response, command, errors);
	}
	
	public ModelAndView editaddRemoveResponces(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) 
	throws Exception {
		if( ! StringUtils.isBlank(request.getParameter("addRemove")) ) {
			AdminSurveyForm form = (AdminSurveyForm)command;
			List<Boolean> deleteableQuestionsCustom = new ArrayList<Boolean>();
			if( request.getParameter("addRemove").equals("add") ){
				NUMBER_OF_CUSTOM_RESPONCES++;
				customQuestionId=form.getCurrentCustomSurveyQuestion().getId();
				deleteCustomQuestion=1;
		         deleteableQuestionsCustom.add(new Boolean(true));
		         form.setDeleteableQuestions(deleteableQuestionsCustom);
			}
			else if( request.getParameter("addRemove").equals("remove") )
				NUMBER_OF_CUSTOM_RESPONCES--;
				customQuestionId=form.getCurrentCustomSurveyQuestion().getId();
				deleteCustomQuestion=1;
				 deleteableQuestionsCustom.add(new Boolean(true));
		         form.setDeleteableQuestions(deleteableQuestionsCustom);
		}
		return this.addQuestionView(request, response, command, errors);
	}

	/**
	 * setup the questiontype in the popup for adding questions
	 * and display the popup for the add question.
	 * Also used to display the other add Q forms when 
	 * the question type is changed on the same page
	 */
	public ModelAndView addCourseView(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("surveyQuestionTypes", SURVEY_QUESTION_TYPES);
		AdminSurveyForm form = (AdminSurveyForm)command;

		//setting the default question type if there is none
		if(StringUtils.isBlank(form.getSurveyQuestionType())){
			form.setSurveyQuestionType(SURVEY_QUESTION_MULTIPLE_CHOICE_MULTIPLE_SELECT);
		}

		if(form.getSurveyQuestionType().equalsIgnoreCase(SURVEY_QUESTION_MULTIPLE_CHOICE_MULTIPLE_SELECT)){
			MultipleSelectSurveyQuestion question = new MultipleSelectSurveyQuestion();
			form.setCurrentMultipleSelectSurveyQuestion(question);
		}else if(form.getSurveyQuestionType().equalsIgnoreCase(SURVEY_QUESTION_MULTIPLE_CHOICE_SINGLE_SELECT)){
			SingleSelectSurveyQuestion question = new SingleSelectSurveyQuestion();
			form.setCurrentSingleSelectSurveyQuestion(question);
		}else if(form.getSurveyQuestionType().equalsIgnoreCase(SURVEY_QUESTION_DROP_DOWN_SINGLE_SELECT)){
			SingleSelectSurveyQuestion question = new SingleSelectSurveyQuestion();
			form.setCurrentSingleSelectSurveyQuestion(question);
		}else if(form.getSurveyQuestionType().equalsIgnoreCase(SURVEY_QUESTION_TEXT_BOX_256_CHARACTERS)){
			TextBoxSurveyQuestion question = new TextBoxSurveyQuestion();
			form.setCurrentFillInTheBlankSurveyQuestion(question);
		}else if(form.getSurveyQuestionType().equalsIgnoreCase(SURVEY_QUESTION_TEXT_BOX_UNLIMITED)){
			TextBoxSurveyQuestion question = new TextBoxSurveyQuestion();
			form.setCurrentFillInTheBlankSurveyQuestion(question);
		}else if(form.getSurveyQuestionType().equalsIgnoreCase(SURVEY_QUESTION_MULTIPLE_CHOICE_RATING_SELECT)){

		}else if ( form.getSurveyQuestionType().equalsIgnoreCase(SURVEY_QUESTION_PERSONAL_INFORMATION) ) {
			PersonalInformationSurveyQuestion question = new PersonalInformationSurveyQuestion();
			form.setCurrentPersonalInformationQuestion(question);
		} 
		return new ModelAndView(editCourseTemplate, "context", context);
	}

	/**
	 * saving the question just entered from the page
	 * note that we are checking if there are any validation errors
	 * before moving the question list and going to the next page
	 */
	public ModelAndView saveQuestionInList(HttpServletRequest request, HttpServletResponse response, 
			Object command, BindException errors) throws Exception {
		//save the questions in the form object
		AdminSurveyForm adminSurveyForm = (AdminSurveyForm)command;
		String questionType = adminSurveyForm.getSurveyQuestionType();
		//validator is to be putted early in the page as acc. LMS-9775 to get rid from crashing
		AdminSurveyValidator validator = (AdminSurveyValidator)this.getValidator();
		validator.validateQuestion(adminSurveyForm, errors);
		if(errors.hasErrors()){
			Map<Object, Object> context = new HashMap<Object, Object>();
			context.put("customResponceTypes", CUSTOM_RESPONCE_TYPES);
			context.put("resNumber", NUMBER_OF_CUSTOM_RESPONCES);
			context.put("surveyQuestionTypes", SURVEY_QUESTION_TYPES);			
			return new ModelAndView(addQuestionContainerTemplate, "context", context);
		}
		//validator ends here 
		if(questionType.equalsIgnoreCase(SURVEY_QUESTION_MULTIPLE_CHOICE_MULTIPLE_SELECT)){
			MultipleSelectSurveyQuestion question = adminSurveyForm.getCurrentMultipleSelectSurveyQuestion();
			question.setText(adminSurveyForm.getSurveyQuestionText());
			question.setRequired(adminSurveyForm.isSurveyQuestionRequired());
			adminSurveyForm.setCurrentMultipleSelectSurveyQuestion(question);
			if(StringUtils.isBlank(request.getParameter("editableQuestionId"))){
				adminSurveyForm.getSurveyQuestions().add(question);
			}
		}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_MULTIPLE_CHOICE_SINGLE_SELECT)){
			SingleSelectSurveyQuestion question = adminSurveyForm.getCurrentSingleSelectSurveyQuestion();
			question.setText(adminSurveyForm.getSurveyQuestionText());
			question.setRequired(adminSurveyForm.isSurveyQuestionRequired());
			adminSurveyForm.setCurrentSingleSelectSurveyQuestion(question);
			
			if(StringUtils.isBlank(request.getParameter("editableQuestionId"))){
				adminSurveyForm.getSurveyQuestions().add(question);
			}
		}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_DROP_DOWN_SINGLE_SELECT)){
			SingleSelectSurveyQuestion question = adminSurveyForm.getCurrentSingleSelectSurveyQuestion();
			question.setText(adminSurveyForm.getSurveyQuestionText());
			adminSurveyForm.setCurrentSingleSelectSurveyQuestion(question);
			adminSurveyForm.getSurveyQuestions().add(question);
		} else if ( questionType.equalsIgnoreCase(SURVEY_QUESTION_TEXT_BOX_256_CHARACTERS) ) {
			TextBoxSurveyQuestion question = adminSurveyForm.getCurrentFillInTheBlankSurveyQuestion();
			question.setText(adminSurveyForm.getSurveyQuestionText());
			question.setRequired(adminSurveyForm.isSurveyQuestionRequired());
			adminSurveyForm.setCurrentFillInTheBlankSurveyQuestion(question);
			question.setSingleLineResponse(true);
			if(StringUtils.isBlank(request.getParameter("editableQuestionId"))){
				adminSurveyForm.getSurveyQuestions().add(question);
			}
		} else if ( questionType.equalsIgnoreCase(SURVEY_QUESTION_TEXT_BOX_UNLIMITED) ) {
			TextBoxSurveyQuestion question = adminSurveyForm.getCurrentFillInTheBlankSurveyQuestion();
			question.setText(adminSurveyForm.getSurveyQuestionText());
			question.setRequired(adminSurveyForm.isSurveyQuestionRequired());
			adminSurveyForm.setCurrentFillInTheBlankSurveyQuestion(question);
			question.setSingleLineResponse(false);
			
			if(StringUtils.isBlank(request.getParameter("editableQuestionId"))){
				adminSurveyForm.getSurveyQuestions().add(question);
			}
		}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_MULTIPLE_CHOICE_RATING_SELECT)){

		} else if ( questionType.equalsIgnoreCase(SURVEY_QUESTION_PERSONAL_INFORMATION) ) {
			PersonalInformationSurveyQuestion question = adminSurveyForm.getCurrentPersonalInformationQuestion();
			question.setRequired(adminSurveyForm.isSurveyQuestionRequired());
			List<AvailablePersonalInformationfield> personalInfoFields = new ArrayList<AvailablePersonalInformationfield>();
				int counter = 0;
				for (ManagePersonalInformation item : adminSurveyForm.getMngPersonalInfos()) {
				if (item.isSelected()) {
					AvailablePersonalInformationfield personalInfoField = new AvailablePersonalInformationfield();
					personalInfoField.setAvailablePersonalInformationfieldItem(item.getPersonalInfoItem());
					personalInfoField.setFieldsRequired(item.isRequired());
					personalInfoField.setDisplayOrder(counter++);
					personalInfoFields.add(personalInfoField);
				}
			}
			question.setPersonalInformationfields(personalInfoFields);
			question.setText(adminSurveyForm.getSurveyQuestionText());
			adminSurveyForm.setCurrentPersonalInformationQuestion(question);
			adminSurveyForm.getSurveyQuestions().add(question);
		} else if ( questionType.equalsIgnoreCase(SURVEY_QUESTION_CUSTOM) ) {
			orderFlag=false;
			int noOfItems = adminSurveyForm.getAggregateSurveyQuestionItem().size();
			List<AggregateSurveyQuestionItem> aggregateSurveyQuestionItem = new ArrayList<AggregateSurveyQuestionItem>();
			AggregateSurveyQuestion question = adminSurveyForm.getCurrentCustomSurveyQuestion();
			question.setRequired(adminSurveyForm.isSurveyQuestionRequired());
			question.setText(adminSurveyForm.getSurveyQuestionText());
			if(adminSurveyForm.getEditableQuestionId()!=0){
			SurveyQuestion surveyquest = surveyService.getSurveyQuestionById(adminSurveyForm.getEditableQuestionId());
			question.setDisplayOrder(surveyquest.getDisplayOrder());
			}
			adminSurveyForm.setCurrentCustomSurveyQuestion(question);
			adminSurveyForm.getSurveyQuestions().add(question);
			int loopIndex=0;
			if(deleteCustomQuestion==1){
				loopIndex=NUMBER_OF_CUSTOM_RESPONCES;
			}
			else if(deleteCustomQuestion==0){
				loopIndex=adminSurveyForm.getCustomQuestionResponceTypes().size();
			}

			for( int i = 0 ; i < loopIndex ; i ++ ) {
				String resType = adminSurveyForm.getCustomQuestionResponceTypes().get(i);
				String label = adminSurveyForm.getResponceLabels().get(i);
				String choices = adminSurveyForm.getAnswerChoices().get(i);
				//boolean isMulti = manageSurveyForm.getIsMultiline().get(i);
				Boolean isRequired = adminSurveyForm.getResRequired().get(i);
				if( resType.equalsIgnoreCase(SURVEY_CUSTOM_RESPONCE_MULTIPLE_SELECT) ) {
					AggregateSurveyQuestionItem aggrSurveyQuestion = new AggregateSurveyQuestionItem();
					MultipleSelectSurveyQuestion surveyQun = new MultipleSelectSurveyQuestion();
					surveyQun.setText(label);
					readCustomResponceChoices( surveyQun, choices);
					if(adminSurveyForm.getEditableQuestionId()!=0 && noOfItems>0){
                    	surveyQun.setDisplayOrder(adminSurveyForm.getAggregateSurveyQuestionItem().get(i).getDisplayOrder());
                    	aggrSurveyQuestion.setDisplayOrder(adminSurveyForm.getAggregateSurveyQuestionItem().get(i).getDisplayOrder());
        				}
                    else{
                    surveyQun.setDisplayOrder(i);
                    aggrSurveyQuestion.setDisplayOrder(i);
                    }
					//aggrSurveyQuestion.setDisplayOrder(i);
					aggrSurveyQuestion.setQuestion(surveyQun);
					aggrSurveyQuestion.getQuestion().setRequired(isRequired);
					aggrSurveyQuestion.setAggregateSurveyQuestion(question);
					aggregateSurveyQuestionItem.add(aggrSurveyQuestion);
					--noOfItems;
				} else if( resType.equalsIgnoreCase(SURVEY_CUSTOM_RESPONCE_SINGLE_SELECT) ) {
					AggregateSurveyQuestionItem aggrSurveyQuestion = new AggregateSurveyQuestionItem();
					SingleSelectSurveyQuestion surveyQun = new SingleSelectSurveyQuestion();
					surveyQun.setText(label);
					readCustomResponceChoices( surveyQun, choices);
					if(adminSurveyForm.getEditableQuestionId()!=0 && noOfItems>0){
                    	surveyQun.setDisplayOrder(adminSurveyForm.getAggregateSurveyQuestionItem().get(i).getDisplayOrder());
                    	aggrSurveyQuestion.setDisplayOrder(adminSurveyForm.getAggregateSurveyQuestionItem().get(i).getDisplayOrder());
        				}
                    else{
                    surveyQun.setDisplayOrder(i);
                    aggrSurveyQuestion.setDisplayOrder(i);
                    }
					//aggrSurveyQuestion.setDisplayOrder(i);
					aggrSurveyQuestion.setQuestion(surveyQun);
					aggrSurveyQuestion.getQuestion().setRequired(isRequired);
					aggrSurveyQuestion.setAggregateSurveyQuestion(question);
					aggregateSurveyQuestionItem.add(aggrSurveyQuestion);
					--noOfItems;
				} else if( resType.equalsIgnoreCase(SURVEY_CUSTOM_RESPONCE_TEXT) ) {
					AggregateSurveyQuestionItem aggrSurveyQuestion = new AggregateSurveyQuestionItem();
					TextBoxSurveyQuestion surveyQun = new TextBoxSurveyQuestion();
					boolean isMulti = adminSurveyForm.getIsMultiline().get(i);
					surveyQun.setText(label);
					surveyQun.setSingleLineResponse(isMulti);
					if(adminSurveyForm.getEditableQuestionId()!=0 && noOfItems>0){
                    	surveyQun.setDisplayOrder(adminSurveyForm.getAggregateSurveyQuestionItem().get(i).getDisplayOrder());
                    	aggrSurveyQuestion.setDisplayOrder(adminSurveyForm.getAggregateSurveyQuestionItem().get(i).getDisplayOrder());
        				}
                    else{
                    surveyQun.setDisplayOrder(i);
                    aggrSurveyQuestion.setDisplayOrder(i);
                    }
					aggrSurveyQuestion.setQuestion(surveyQun);
					aggrSurveyQuestion.getQuestion().setRequired(isRequired);
					aggrSurveyQuestion.setAggregateSurveyQuestion(question);
					aggregateSurveyQuestionItem.add(aggrSurveyQuestion);
				} else if( resType.equalsIgnoreCase(SURVEY_CUSTOM_RESPONCE_BLANKS) ) {
					/*FillIntheBlankSurveyAnswerItem ansItem = new FillIntheBlankSurveyAnswerItem();
					ansItem.setLabel(label);
					ansItem.setSurveyQuestion(question);
					ansItem.setDisplayOrder(i);*/
				}
			}
			adminSurveyForm.setAggregateSurveyQuestionItem(aggregateSurveyQuestionItem);
		}
		/*AdminSurveyValidator validator = (AdminSurveyValidator)this.getValidator();
		validator.validateQuestion(adminSurveyForm, errors);*/

		Survey survey = surveyService.loadForUpdateSurvey(adminSurveyForm.getSid());
		
		for(SurveyQuestion surveyQuestion:adminSurveyForm.getSurveyQuestions()) {
			if(surveyQuestion.getSurvey()==null && survey != null){
				surveyQuestion.setSurvey(survey);
				surveyService.addSurveyQuestion(surveyQuestion);
			}
		}

			
		
		//if there are validation errors in saving
		//go back to the add question page with errors
		/*if(errors.hasErrors()){
			Map<Object, Object> context = new HashMap<Object, Object>();
			context.put("surveyQuestionTypes", SURVEY_QUESTION_TYPES);			
			return new ModelAndView(addQuestionContainerTemplate, "context", context);
		}*/
		adminSurveyForm.setSurveyQuestionText("");
		adminSurveyForm.setSurveyQuestionRequired(false);
		//increment the deletable list by one to the end of the said list
		adminSurveyForm.getDeleteableQuestions().add(new Boolean(false));

		Map<String, String> context = new HashMap <String, String>();
		context.put("target", "refreshQuestions");
		saveQuestionAndDisplaySurveys(request, response, command, errors);
		
		if(deleteCustomQuestion==1){
			deleteQuestions(request, response, command, errors);
		}
		//return new ModelAndView(redirectTemplate,"context",context);
		return new ModelAndView("redirect:adm_manageSyrvey.do?method=showQuestionView&sid="+adminSurveyForm.getSid());
	}

	public ModelAndView cancelQuestionInList(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) 
	throws Exception {
		AdminSurveyForm form = (AdminSurveyForm)command;
		Map<String, String> context = new HashMap <String, String>();
		context.put("target", "refreshQuestions");
		customQuestionId=null;
		deleteCustomQuestion=0;
		return new ModelAndView("redirect:adm_manageSyrvey.do?method=showQuestionView&sid="+form.getSid());
	}

	public ModelAndView refreshQuestions(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		return new ModelAndView(addQuestionTemplate);
	}
	public ModelAndView showAllQuestions(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		AdminSurveyForm form = (AdminSurveyForm)command;
		HttpSession session = request.getSession();
		session.setAttribute("isShowAll", "1");
		return new ModelAndView(addQuestionTemplate);
	}
	public ModelAndView removeResponse(HttpServletRequest request, HttpServletResponse response,
            Object command, BindException errors) throws Exception {
		
		AdminSurveyForm form = (AdminSurveyForm)command;
	       
        Survey survey = surveyService.loadForUpdateSurvey(form.getSid());
        if(form.getResponseCheck()!=null && form.getResponseCheck().size()>0){
            List<Long> surveyQuestionsToBeDeletedFromDB = new ArrayList<Long>();
            List<Long> tempSurveyQuestions = new ArrayList<Long>();
            List<AggregateSurveyQuestionItem> aggregateSurveyQuestionItems = new ArrayList<AggregateSurveyQuestionItem>();

	            for(int i = form.getResponseCheck().size()-1; i>=0; i--){
	            	System.out.println("@@@@@@@@"+form.getResponseCheck().size());
	          	 aggregateSurveyQuestionItems=surveyService.getAggregateSurveyQuestionItemsByQuestionId(form.getEditableQuestionId());
	            	 	for(AggregateSurveyQuestionItem aggrgteSurveyQuestionItem : aggregateSurveyQuestionItems){
	                    	if(aggrgteSurveyQuestionItem.getQuestion().getId()!=null && Long.parseLong(form.getResponseCheck().get(i))==aggrgteSurveyQuestionItem.getQuestion().getId()){
	                    		tempSurveyQuestions.add(aggrgteSurveyQuestionItem.getQuestion().getId());
	                    		form.getAggregateSurveyQuestionItem().remove(i);
	                    	}
	                    }
	            		surveyService.deleteCustomResponse(tempSurveyQuestions.toArray());
	                       
	                }
	            
        
        surveyService.addSurvey(survey);
        }
        return editQuestion(request, response, command, errors);
	}
	@SuppressWarnings("unchecked")
	public ModelAndView deleteQuestions(HttpServletRequest request, HttpServletResponse response, 
			Object command, BindException errors) throws Exception {

		AdminSurveyForm form = (AdminSurveyForm)command;
		Survey survey = surveyService.loadForUpdateSurvey(form.getSid());
        if(form.getDeleteableQuestions()!=null && form.getDeleteableQuestions().size()>0){
            List<Long> surveyQuestionsToBeDeletedFromDB = new ArrayList<Long>();
            if(deleteCustomQuestion==0){
	            for(int i = form.getDeleteableQuestions().size()-1; i>=0; i--){
	            if(form.getDeleteableQuestions().get(i)){
	                    SurveyQuestion question = form.getSurveyQuestions().get(i);
	                    log.debug("Deleting Survey Question - "+question.getText()+ " and it has the id = "+ question.getId());
	                    //form.getSurveyQuestions().remove(i);
	                    if(question.getId()!=null)
	                        surveyQuestionsToBeDeletedFromDB.add(question.getId());
	                       
	                }
	            }
            }
            else if(deleteCustomQuestion==1){
            	 surveyQuestionsToBeDeletedFromDB.add(customQuestionId);
            	 List<Long> tempSurveyQuestions = new ArrayList<Long>();
         		 List<AggregateSurveyQuestionItem> aggregateSurveyQuestionItems = new ArrayList<AggregateSurveyQuestionItem>();

         	 aggregateSurveyQuestionItems=surveyService.getAggregateSurveyQuestionItemsByQuestionId(customQuestionId);
         	 	for(AggregateSurveyQuestionItem aggrgteSurveyQuestionItem : aggregateSurveyQuestionItems){
                 	if(aggrgteSurveyQuestionItem.getQuestion().getId()!=null){
                 		tempSurveyQuestions.add(aggrgteSurveyQuestionItem.getQuestion().getId());
                 	}
                 }
         		surveyService.deleteCustomResponse(tempSurveyQuestions.toArray());
         		
         		
            }
            deleteCustomQuestion=0;
            //delete the required questions - from db - if any
           
            List<SurveyQuestion> surveyQuestions = new ArrayList<SurveyQuestion>();
            surveyQuestions.addAll(survey.getQuestions());
               

            for(SurveyQuestion question : surveyQuestions){
               
                if(surveyQuestionsToBeDeletedFromDB.contains(question.getId()))
                        survey.getQuestions().remove(question);
               
            }
            form.setSurveyQuestionsToBeDeletedFromDB(surveyQuestionsToBeDeletedFromDB);
            AdminSurveyValidator validator = (AdminSurveyValidator)this.getValidator();
            validator.validateSurveyResult(form, errors);
            if(errors.hasErrors()){   
               
                return new ModelAndView(addQuestionTemplate);
            }
            for(int i = form.getDeleteableQuestions().size()-1; i>=0; i--){
                if(form.getDeleteableQuestions().get(i)){
                        SurveyQuestion question = form.getSurveyQuestions().get(i);
                        form.getSurveyQuestions().remove(i);                       
                    }
                }
            surveyService.deleteSurveyQuestions(surveyQuestionsToBeDeletedFromDB.toArray());
            surveyService.addSurvey(survey);
            //initialize the list of deleteable questions
            int numberOfQuestions = form.getSurveyQuestions().size();
            List<Boolean> deleteableQuestions = new ArrayList<Boolean>();
            for(int i=0; i<numberOfQuestions; i++)
                deleteableQuestions.add(new Boolean(false));
            form.setDeleteableQuestions(deleteableQuestions);
        }
        //redirect to the showQuestionView
        Map context = new HashMap();
        context.put("target", "refreshQuestions");
        return new ModelAndView(redirectTemplate,"context",context);
    }

	@SuppressWarnings("unchecked")
	public ModelAndView deleteCourses(HttpServletRequest request, HttpServletResponse response, 
			Object command, BindException errors) throws Exception {

		//		int startIndex=Integer.parseInt(request.getParameter("pageStartIndex"));
		int endIndex=Integer.parseInt(request.getParameter("pageEndIndex"));
		AdminSurveyForm manageSurveyForm = (AdminSurveyForm)command;
		Survey survey = surveyService.loadForUpdateSurvey(manageSurveyForm.getSid());
		List<Course> selectedCourses = new ArrayList<Course>();
		List<SurveyCourse> surveyCourses = manageSurveyForm.getSurveyCourses();

		for(int i=0;i<=endIndex;i++){

			if(manageSurveyForm.getDeleteableCourses().get(i)==null || !manageSurveyForm.getDeleteableCourses().get(i)){

				selectedCourses.add(surveyCourses.get(i).getCourse());
			}
		}

		survey.setCourses(selectedCourses);
		surveyService.addSurvey(survey); //!!!the service function name should be save survey
		//
		Map context = new HashMap();

		context.put("target", "showCoursesView");
		return new ModelAndView(redirectTemplate,"context",context);
	}

public ModelAndView moveInShowall(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		AdminSurveyForm form = (AdminSurveyForm)command;
		Survey survey = surveyService.loadForUpdateSurvey(form.getSid());
		List<SurveyQuestion> surveyQuestions = new ArrayList<SurveyQuestion>();
		form.setSurveyName(survey.getName());
		List<SurveyQuestion> surveyQuestionList = survey.getQuestions();
		//initialize the list of deleteable questions
	    List<AggregateSurveyQuestionItem> aggregateSurveyQuestionItems=new ArrayList<AggregateSurveyQuestionItem>();
		for(SurveyQuestion surveyQuestion : surveyQuestionList){
			if(surveyQuestion instanceof AggregateSurveyQuestion){
			aggregateSurveyQuestionItems = surveyService.getAggregateSurveyQuestionItemsByQuestionId(surveyQuestion.getId());
			for(AggregateSurveyQuestionItem aggregateSurveyQuestionItem : aggregateSurveyQuestionItems){
					surveyQuestions.add(aggregateSurveyQuestionItem.getQuestion());
			}
			} 
		}
			
		form.setSurveyQuestionList(surveyQuestions);
		Collections.sort(surveyQuestionList);
		form.setSurveyQuestions(surveyQuestionList);
		return showQuestionView(request, response, command, errors);
		
	}
	@SuppressWarnings("unchecked")
	public ModelAndView moveQuestion(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		AdminSurveyForm form = (AdminSurveyForm)command;
		int qid = Integer.parseInt(request.getParameter("qid"));
		String param = request.getParameter("moveParam");
		String pageIndex = request.getParameter("pageCurrIndex");
		String pageLastIndex = request.getParameter("pageLastIndex");
		
		SurveyQuestion question = form.getSurveyQuestions().get(qid);
		log.debug(" INSIDE moveQuestion :: qid :: "+qid+" param :: "+param+" questions ::"+form.getDeleteableQuestions().size()+" pageIndex :: "+pageIndex+" :>question:>"+question.getId());
		if(form.getSurveyQuestions().size()>1){
		if(param.equals("top"))
		{

			SurveyQuestion topQuestion = form.getSurveyQuestions().get(Integer.parseInt(pageIndex));
			int topOrder = topQuestion.getDisplayOrder();
			topQuestion.setDisplayOrder(question.getDisplayOrder());
			question.setDisplayOrder(topOrder);
			surveyService.addSurveyQuestion(topQuestion);
			surveyService.addSurveyQuestion(question);			
		}
		if(param.equals("bottom"))
		{
			int bottomIndex = Integer.parseInt(pageIndex)+Integer.parseInt(pageLastIndex);
			if(bottomIndex>form.getSurveyQuestions().size())
			bottomIndex=form.getSurveyQuestions().size()-1;
			SurveyQuestion bottomQuestion = form.getSurveyQuestions().get(bottomIndex);
			int bottomOrder = bottomQuestion.getDisplayOrder();
			bottomQuestion.setDisplayOrder(question.getDisplayOrder());
			question.setDisplayOrder(bottomOrder);
			surveyService.addSurveyQuestion(bottomQuestion);
			surveyService.addSurveyQuestion(question);			
		}
		if(param.equals("up"))
		{

			SurveyQuestion upQuestion = form.getSurveyQuestions().get(qid-1);
			int upOrder = upQuestion.getDisplayOrder();
			upQuestion.setDisplayOrder(question.getDisplayOrder());
			question.setDisplayOrder(upOrder);
			surveyService.addSurveyQuestion(upQuestion);
			surveyService.addSurveyQuestion(question);			
		}
		if(param.equals("down"))
		{

			SurveyQuestion downQuestion = form.getSurveyQuestions().get(qid+1);
			int downOrder = downQuestion.getDisplayOrder();
			downQuestion.setDisplayOrder(question.getDisplayOrder());
			question.setDisplayOrder(downOrder);
			surveyService.addSurveyQuestion(downQuestion);
			surveyService.addSurveyQuestion(question);			
		}
		}
		HttpSession session = request.getSession();
		Map context = new HashMap();
		if(session.getAttribute("isShowAll").equals("1")){
			flag=true;
			return moveInShowall(request, response, command, errors);
		}
		else
		{
			
			context.put("target", "showQuestionView");
			return new ModelAndView(redirectTemplate,"context",context);	
		}
	}

	@SuppressWarnings("unchecked")
	public ModelAndView saveQuestionAndDisplaySurveys(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		if(errors.hasErrors()){
			return new ModelAndView(addQuestionTemplate);
		}
		//save the questions that were added to the form.question list but was not added to db
		AdminSurveyForm manageSurveyForm = (AdminSurveyForm)command;
		String questionType = manageSurveyForm.getSurveyQuestionType();
		if(orderFlag && questionType.equalsIgnoreCase(SURVEY_QUESTION_CUSTOM)){
			SurveyQuestion surveyquest = surveyService.getSurveyQuestionById(manageSurveyForm.getEditableQuestionId());
			manageSurveyForm.getCurrentCustomSurveyQuestion().setDisplayOrder(surveyquest.getDisplayOrder());
		}
		List<Long> tempSurveyQuestions = new ArrayList<Long>();
		List<AggregateSurveyQuestionItem> aggregateSurveyQuestionItems = new ArrayList<AggregateSurveyQuestionItem>();
		Survey survey = surveyService.loadForUpdateSurvey(manageSurveyForm.getSid());

		for(SurveyQuestion s:manageSurveyForm.getSurveyQuestions()){
			if(s.getSurvey()==null)
				s.setSurvey(survey);
		}
		//COMMENTED
		survey.setQuestions(manageSurveyForm.getSurveyQuestions());
		survey = surveyService.addSurvey(survey); //!!!the service function name should be save survey
		if(edtiableFlag==0 && manageSurveyForm.getSurveyQuestionType().equalsIgnoreCase(SURVEY_QUESTION_CUSTOM)){//execute only when editing
			aggregateSurveyQuestionItems=surveyService.getAggregateSurveyQuestionItemsByQuestionId(manageSurveyForm.getEditableQuestionId());
		for(AggregateSurveyQuestionItem aggrgteSurveyQuestionItem : aggregateSurveyQuestionItems){
        	if(aggrgteSurveyQuestionItem.getQuestion().getId()!=null){
        		tempSurveyQuestions.add(aggrgteSurveyQuestionItem.getQuestion().getId());
        	}
        	
          }
		surveyService.deleteCustomResponse(tempSurveyQuestions.toArray());
		}
		edtiableFlag=1; 
		if( manageSurveyForm.getAggregateSurveyQuestionItem() != null && manageSurveyForm.getAggregateSurveyQuestionItem().size() > 0 ) {
			for( AggregateSurveyQuestionItem agrSurveyItems : manageSurveyForm.getAggregateSurveyQuestionItem() ) {
				if( agrSurveyItems.getQuestion().getSurvey() == null ) {
					agrSurveyItems.getQuestion().setSurvey(survey);
					//agrSurveyItems.getAggregateSurveyQuestion().setSurvey(survey);
				}
			}
			
		}
			surveyService.saveAggregateSurveyQuestionItem(manageSurveyForm.getAggregateSurveyQuestionItem());
		
			
		//redirect to the display survey page
		Map context = new HashMap();
		context.put("target", "displaySurveys");
		return new ModelAndView(redirectTemplate,"context",context);
	}

	@SuppressWarnings("unchecked")
	public ModelAndView cancelQuestionAndDisplaySurveys(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		//redirect to the display survey page
		Map context = new HashMap();
		context.put("target", "displaySurveys");
		return new ModelAndView(redirectTemplate,"context",context);
	}

	/**
	 * handler method courses pages
	 */
	public ModelAndView showCoursesView(HttpServletRequest request, HttpServletResponse response, 
			Object command, BindException errors) throws Exception {

		AdminSurveyForm form = (AdminSurveyForm)command;
		List<SurveyCourse> courses = form.getSurveyCourses();
		Map<Object, Object> context = new HashMap<Object, Object>();

		if( courses != null && courses.size() > 0 ) {

			HttpSession session = request.getSession();
			String sortDirection = request.getParameter("sortDirection");
			if( (StringUtils.isBlank(sortDirection)) && session.getAttribute("sortDirection") != null )
				sortDirection = session.getAttribute("sortDirection").toString();
			String sortColumnIndex = request.getParameter("sortColumnIndex");
			if( (StringUtils.isBlank(sortColumnIndex)) && session.getAttribute("sortColumnIndex") != null )
				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String showAll = request.getParameter("showAll");
			if ( showAll == null ) showAll = "false";
			String pageIndex = request.getParameter("pageCurrIndex");
			if( pageIndex == null ) pageIndex = "0";
			log.debug("pageIndex = " + pageIndex);
			context.put("showAll", showAll);

			Map<String, Object> PagerAttributeMap = new HashMap <String, Object>();
			PagerAttributeMap.put("pageIndex",pageIndex);

			/** Added by Dyutiman...
			 *  manual sorting
			 */
			request.setAttribute("PagerAttributeMap", PagerAttributeMap);

			if( StringUtils.isNotBlank(sortDirection) && StringUtils.isNotBlank(sortColumnIndex)) {
				// sorting against course name
				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						for( int i = 0 ; i < courses.size() ; i ++ ) {
							for( int j = 0 ; j < courses.size() ; j ++ ) {
								if( i != j ) {
									SurveyCourse c1 = (SurveyCourse) courses.get(i);
									SurveyCourse c2 = (SurveyCourse) courses.get(j);
									if( c1.getCourse().getCourseTitle().toUpperCase().compareTo
											(c2.getCourse().getCourseTitle().toUpperCase()) > 0 ) {
										SurveyCourse tempLG = courses.get(i);
										courses.set(i, courses.get(j));
										courses.set(j, tempLG);
									}
								}
							}
						}
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 0);
						session.setAttribute("sortDirection", 0);
						session.setAttribute("sortColumnIndex", 0);
					} else {
						for( int i = 0 ; i < courses.size() ; i ++ ) {
							for( int j = 0 ; j < courses.size() ; j ++ ) {
								if( i != j ) {
									SurveyCourse c1 = (SurveyCourse) courses.get(i);
									SurveyCourse c2 = (SurveyCourse) courses.get(j);
									if( c1.getCourse().getCourseTitle().toUpperCase().compareTo
											(c2.getCourse().getCourseTitle().toUpperCase()) < 0 ) {
										SurveyCourse tempLG = courses.get(i);
										courses.set(i, courses.get(j));
										courses.set(j, tempLG);
									}
								}
							}
						}
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 0);
						session.setAttribute("sortDirection", 1);
						session.setAttribute("sortColumnIndex", 0);
					}
				}
			}
			form.setSurveyCourses(courses);
			int numberOfCourses = courses.size();
			List<Boolean> deleteableCourses = new ArrayList<Boolean>();
			for(int i=0; i<numberOfCourses; i++)
				deleteableCourses.add(new Boolean(false));
			form.setDeleteableCourses(deleteableCourses);
		}
		return new ModelAndView(addCourseTemplate,"context",context);
	}


	public ModelAndView searchCourses(HttpServletRequest request, HttpServletResponse response, 
			Object command, BindException errors) throws Exception {

		AdminSurveyForm manageSurveyForm = (AdminSurveyForm)command;
		Survey survey = surveyService.loadForUpdateSurvey(manageSurveyForm.getSid());
		String searchType = manageSurveyForm.getSearchType();

		HttpSession session = request.getSession();
		Map<Object, Object> context = new HashMap<Object, Object>();

		String sortDirection = request.getParameter("sortDirection");
		if( (StringUtils.isBlank(sortDirection)) && session.getAttribute("sortDirection") != null )
			sortDirection = session.getAttribute("sortDirection").toString();
		String sortColumnIndex = request.getParameter("sortColumnIndex");
		if( (StringUtils.isBlank(sortColumnIndex)) && session.getAttribute("sortColumnIndex") != null )
			sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
		String showAll = request.getParameter("showAll");
		if ( showAll == null ) showAll = "false";
		String pageIndex = request.getParameter("pageCurrIndex");
		if( pageIndex == null ) pageIndex = "0";
		log.debug("pageIndex = " + pageIndex);
		context.put("showAll", showAll);
		context.put("methodName", "searchCourses");

		Map<String, Object> PagerAttributeMap = new HashMap <String, Object>();
		PagerAttributeMap.put("pageIndex",pageIndex);

		List<Course> courseList = new ArrayList<Course>();
		List<SurveyCourse> surveyCourseList = new ArrayList<SurveyCourse>();
		
		if( survey.getOwner().getOwnerType().equalsIgnoreCase(Customer.CUSTOMER)) {
			Customer customer = customerService.getCustomerById(survey.getOwner().getId());

			if( searchType.equalsIgnoreCase("simplesearch") ) {
				courseList = entitlementService.getCoursesByEntitlement(customer.getId(), manageSurveyForm.getSimpleSearchKey());
			}else if( searchType.equalsIgnoreCase("advancedsearch") ) {
				/*courseList = entitlementService.getCoursesByEntitlement(customer
						,manageSurveyForm.getSearchCourseName()
						,manageSurveyForm.getSearchCourseID()
						,manageSurveyForm.getSearchKeyword());*/
				List<Course> allCourseList = entitlementService.getAllCoursesByEntitlement(customer.getId());
				for(Course selectedCourse : allCourseList){
					if(StringUtils.isBlank(manageSurveyForm.getSearchCourseName())){
						courseList.add(selectedCourse);
					}else if(StringUtils.isNotBlank(selectedCourse.getCourseTitle())&& selectedCourse.getCourseTitle().contains(manageSurveyForm.getSearchCourseName())){
						courseList.add(selectedCourse);
					}
				}
			}else if( searchType.equalsIgnoreCase("allsearch") ) {
				courseList = entitlementService.getAllCoursesByEntitlement(customer.getId());
			}
		} else {
			Distributor distributor = distributorService.getDistributorById(survey.getOwner().getId());
			long[] courseIdArray = entitlementService.getCourseIDArrayForDistributor(distributor);
			if (courseIdArray.length>0){
				if( searchType.equalsIgnoreCase("simplesearch") ) {
					courseList = entitlementService.findCoursesByDistributor(courseIdArray, "", "", "", manageSurveyForm.getSimpleSearchKey());
				}else if( searchType.equalsIgnoreCase("advancedsearch") ) {
					courseList = entitlementService.findCoursesByDistributor(courseIdArray
							,manageSurveyForm.getSearchCourseName()
							, manageSurveyForm.getSearchCourseID()
							,manageSurveyForm.getSearchKeyword(), "");

				}else if( searchType.equalsIgnoreCase("allsearch") ) {
					courseList = entitlementService.findCoursesByDistributor(courseIdArray, "", "", "", "");
				}
			}
		}
		Set<Course> uniqueSet = new LinkedHashSet<Course>();
		if( courseList != null && !courseList.isEmpty() ) {
			for(int i=0; i<courseList.size();i++){
				uniqueSet.add(courseList.get(i));
			}
		}
		courseList = new ArrayList<Course>(uniqueSet);
		if( CollectionUtils.isNotEmpty(courseList)) {
			for(Course course : courseList) {
				SurveyCourse surveyCourse = new SurveyCourse();
				surveyCourse.setCourse(course);
				for (int i=0;i<survey.getCourses().size();i++) 
					if (survey.getCourses().get(i).getId().equals(course.getId()))
						surveyCourse.setSelected(true);
				surveyCourseList.add(surveyCourse);
			}
		}
		/** Added by Dyutiman...
		 *  manual sorting
		 */
		request.setAttribute("PagerAttributeMap", PagerAttributeMap);

		if( StringUtils.isNotBlank(sortDirection) && StringUtils.isNotBlank(sortColumnIndex) ) {
			// sorting against course name
			if( sortColumnIndex.equalsIgnoreCase("0") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					for( int i = 0 ; i < surveyCourseList.size() ; i ++ ) {
						for( int j = 0 ; j < surveyCourseList.size() ; j ++ ) {
							if( i != j ) {
								SurveyCourse c1 = (SurveyCourse) surveyCourseList.get(i);
								SurveyCourse c2 = (SurveyCourse) surveyCourseList.get(j);
								if( c1.getCourse().getCourseTitle().toUpperCase().compareTo
										(c2.getCourse().getCourseTitle().toUpperCase()) > 0 ) {
									SurveyCourse tempLG = surveyCourseList.get(i);
									surveyCourseList.set(i, surveyCourseList.get(j));
									surveyCourseList.set(j, tempLG);
								}
							}
						}
					}
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 0);
				} else {
					for( int i = 0 ; i < surveyCourseList.size() ; i ++ ) {
						for( int j = 0 ; j < surveyCourseList.size() ; j ++ ) {
							if( i != j ) {
								SurveyCourse c1 = (SurveyCourse) surveyCourseList.get(i);
								SurveyCourse c2 = (SurveyCourse) surveyCourseList.get(j);
								if( c1.getCourse().getCourseTitle().toUpperCase().compareTo
										(c2.getCourse().getCourseTitle().toUpperCase()) < 0 ) {
									SurveyCourse tempLG = surveyCourseList.get(i);
									surveyCourseList.set(i, surveyCourseList.get(j));
									surveyCourseList.set(j, tempLG);
								}
							}
						}
					}
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 0);
				}
			}
		}
		manageSurveyForm.setSurveyCourses(surveyCourseList);
		surveyService.addSurvey(survey);
		//return showCoursesView(request,response,manageSurveyForm,errors);
		return new ModelAndView(editCourseTemplate, "context", context);
	}

	@SuppressWarnings("unchecked")
	public ModelAndView updateSurveyCoursesAndDisplaySurveys(HttpServletRequest request, 
			HttpServletResponse response, Object command, BindException errors) throws Exception {

		// save the courses selected
		if(errors.hasErrors()){
			return new ModelAndView(addCourseTemplate);
		}

		AdminSurveyForm manageSurveyForm = (AdminSurveyForm)command;
		Survey survey = surveyService.loadForUpdateSurvey(manageSurveyForm.getSid());
		List<Course> selectedCourses = new ArrayList<Course>();
		for(SurveyCourse surveyCourse:manageSurveyForm.getSurveyCourses()){
			if(surveyCourse.isSelected()){
				selectedCourses.add(surveyCourse.getCourse());
			}
		}
		survey.setCourses(selectedCourses);
		surveyService.addSurvey(survey); //!!!the service function name should be save survey

		//redirect to the display survey page
		Map context = new HashMap();
		context.put("target", "showCoursesView");
		return new ModelAndView(redirectTemplate,"context",context);

	}

	@SuppressWarnings("unchecked")
	public ModelAndView cancelSurveyCoursesAndDisplaySurveys(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		//redirect to the display survey page
		Map context = new HashMap();
		context.put("target", "addCourseTemplate");
		return new ModelAndView(addCourseTemplate,"context",context);
	}

	public ModelAndView editQuestion(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("surveyQuestionTypes", SURVEY_QUESTION_TYPES);
		AdminSurveyForm form = (AdminSurveyForm)command;
		HttpSession session = request.getSession();
		flag=false;
		session.setAttribute("isShowAll", "0");
		List<Boolean> resRequired = new ArrayList <Boolean>();
		SurveyQuestion selectedSurveyQuestion = null;
		String answerLines = null;
		String questionId = request.getParameter("editableQuestionId");
		//ManageSurveyValidator validator = (ManageSurveyValidator)this.getValidator();	
		//validator.validateSurveyResult(form, errors);

		if(errors.hasErrors()){
			return new ModelAndView(addQuestionTemplate);
		}
		
		List<SurveySection> surveySections = surveyService.getSurveySectionsBySurveyId(form.getSid());
		if(surveySections != null && !surveySections.isEmpty()) {
			context.put("displayNotes", "true");
		} else {
			context.put("displayNotes", "false");
		}
		
		Map<Long, String> notes = new HashMap<Long, String>();

		for (SurveyQuestion surveyQuestion : form.getSurveyQuestions()) {
			if (surveyQuestion.getId() != null)
				if (surveyQuestion.getId().compareTo(form.getEditableQuestionId()) == 0)
					selectedSurveyQuestion = surveyQuestion;
			if(surveyQuestion.getNotes() != null) 
				notes.put(surveyQuestion.getId(), surveyQuestion.getNotes());
		}
		
		context.put("notes", notes);
		context.put("questionId", Long.valueOf(questionId));

		if(selectedSurveyQuestion instanceof MultipleSelectSurveyQuestion){
			MultipleSelectSurveyQuestion question = (MultipleSelectSurveyQuestion)selectedSurveyQuestion;
			form.setSurveyQuestionText(question.getText());
			answerLines = this.getAnswer(question.getSurveyAnswers());
			question.setSurveyAnswerLines(answerLines);
			form.setSurveyQuestionRequired(question.getRequired());
			form.setCurrentMultipleSelectSurveyQuestion(question);
			form.setSurveyQuestionType(SURVEY_QUESTION_MULTIPLE_CHOICE_MULTIPLE_SELECT);
		}else if(selectedSurveyQuestion instanceof SingleSelectSurveyQuestion){
			SingleSelectSurveyQuestion question = (SingleSelectSurveyQuestion)selectedSurveyQuestion;
			form.setSurveyQuestionText(question.getText());
			form.setSurveyQuestionRequired(question.getRequired());
			answerLines = this.getAnswer(question.getSurveyAnswers());
			question.setSurveyAnswerLines(answerLines);
			form.setCurrentSingleSelectSurveyQuestion(question);
			if(question.isDropdown()){
				form.setSurveyQuestionType(SURVEY_QUESTION_DROP_DOWN_SINGLE_SELECT);
			}else{
				form.setSurveyQuestionType(SURVEY_QUESTION_MULTIPLE_CHOICE_SINGLE_SELECT);
			}
		}else if( selectedSurveyQuestion instanceof TextBoxSurveyQuestion ) {
			TextBoxSurveyQuestion question = (TextBoxSurveyQuestion)selectedSurveyQuestion;
			form.setSurveyQuestionText(question.getText());
			form.setSurveyQuestionRequired(question.getRequired());
			form.setCurrentFillInTheBlankSurveyQuestion(question);
			if( question.isSingleLineResponse() ) {
					form.setSurveyQuestionType(SURVEY_QUESTION_TEXT_BOX_256_CHARACTERS);
				} else {
					form.setSurveyQuestionType(SURVEY_QUESTION_TEXT_BOX_UNLIMITED);
				}
			
		}else if ( selectedSurveyQuestion instanceof AggregateSurveyQuestion ) {
			
			form.setSurveyQuestionText("");
            form.setSurveyQuestionType(SURVEY_QUESTION_MULTIPLE_CHOICE_MULTIPLE_SELECT);
            form.setCurrentMultipleSelectSurveyQuestion(null);
            form.setCurrentSingleSelectSurveyQuestion(null);
            form.setCurrentFillInTheBlankSurveyQuestion(null);
            form.setCurrentPersonalInformationQuestion(null);
            form.setSurveyQuestionRequired(false);
            form.setAllQuestionPerPage(false);
            form.setCustomQuestionResponceTypes( new ArrayList <String>());
            form.setResponceLabels(new ArrayList <String>());
            form.setAnswerChoices(new ArrayList <String>());
            //form.setIsMultiline(new ArrayList <Boolean>());
			//-----
			AggregateSurveyQuestion question = (AggregateSurveyQuestion)selectedSurveyQuestion;
			List<AggregateSurveyQuestionItem> aggregateSurveyQuestionItem = surveyService.getAggregateSurveyQuestionItemsByQuestionId(question.getId());
			form.setSurveyQuestionText(question.getText());
			form.setSurveyQuestionRequired(question.getRequired());
			for (AggregateSurveyQuestionItem agrQuestion : aggregateSurveyQuestionItem) {
				if (agrQuestion.getQuestion() instanceof MultipleSelectSurveyQuestion) {
					answerLines = this.getAnswer(agrQuestion.getQuestion().getSurveyAnswers());
					agrQuestion.getQuestion().setSurveyAnswerLines(answerLines);
				}
			}
			if( aggregateSurveyQuestionItem != null ) {
				NUMBER_OF_CUSTOM_RESPONCES = aggregateSurveyQuestionItem.size();
			}
			context.put("resNumber", NUMBER_OF_CUSTOM_RESPONCES);
			//form.setCurrentMultipleSelectSurveyQuestion(question);
			context.put("customResponceTypes", CUSTOM_RESPONCE_TYPES);
			form.setSurveyQuestionType(SURVEY_QUESTION_CUSTOM);
			form.setCurrentCustomSurveyQuestion(question);
			//--------today
			//form.getSurveyQuestions().add(question);
			//--------today
			for( int i = 0 ; i < aggregateSurveyQuestionItem.size() ; i ++ ) {
				AggregateSurveyQuestionItem aggrSurveyQuestion = aggregateSurveyQuestionItem.get(i);
				//String resType = form.getCustomQuestionResponceTypes().get(i);
				//String label = form.getResponceLabels().get(i);
				//String choices = form.getAnswerChoices().get(i);
				//boolean isMulti = form.getIsMultiline().get(i);
				if( aggrSurveyQuestion.getQuestion() instanceof  MultipleSelectSurveyQuestion) {
					MultipleSelectSurveyQuestion surveyQun = (MultipleSelectSurveyQuestion)aggrSurveyQuestion.getQuestion();
					form.getCustomQuestionResponceTypes().add(SURVEY_CUSTOM_RESPONCE_MULTIPLE_SELECT);
					form.getResponceLabels().add(surveyQun.getText());
					String ansChoice = this.getAnswer(surveyQun.getSurveyAnswers());
					form.getAnswerChoices().add(ansChoice);
					form.getResRequired().add(surveyQun.getRequired());
				} else if(aggrSurveyQuestion.getQuestion() instanceof SingleSelectSurveyQuestion) {//added to show the single type response in custom question
					SingleSelectSurveyQuestion surveyQun = (SingleSelectSurveyQuestion)aggrSurveyQuestion.getQuestion();
					//SingleSelectSurveyQuestion surveyQun = new SingleSelectSurveyQuestion();
					form.getCustomQuestionResponceTypes().add(SURVEY_CUSTOM_RESPONCE_SINGLE_SELECT);
					form.getResponceLabels().add(surveyQun.getText());
					String ansChoice = this.getAnswer(surveyQun.getSurveyAnswers());
					form.getAnswerChoices().add(ansChoice);
					form.getResRequired().add(surveyQun.getRequired());
				}
				else if(aggrSurveyQuestion.getQuestion() instanceof TextBoxSurveyQuestion) {
					TextBoxSurveyQuestion surveyQun = (TextBoxSurveyQuestion)aggrSurveyQuestion.getQuestion();
					form.getCustomQuestionResponceTypes().add(SURVEY_CUSTOM_RESPONCE_TEXT);
					form.getResponceLabels().add(surveyQun.getText());
					String ansChoice = this.getAnswer(surveyQun.getSurveyAnswers());
					form.getAnswerChoices().add(ansChoice);
					if( surveyQun.isSingleLineResponse() ){
						form.getIsMultiline().add(false);
					}
					else{
						form.getIsMultiline().add(true);
					}
					form.getResRequired().add(surveyQun.getRequired());
				}//ends here---added by amit
			}
			form.setAggregateSurveyQuestionItem(aggregateSurveyQuestionItem);
		} else if ( selectedSurveyQuestion instanceof PersonalInformationSurveyQuestion ) {
			PersonalInformationSurveyQuestion question = (PersonalInformationSurveyQuestion)selectedSurveyQuestion;
			form.setSurveyQuestionText(question.getText());
			form.setSurveyQuestionRequired(question.getRequired());
			//======================================================
			List<AvailablePersonalInformationfieldItem> personalInfoItems = surveyService.getAllAvailablePersonalInformationfields();
			List<ManagePersonalInformation> mngPersonalInfos = new ArrayList<ManagePersonalInformation>();
			for (AvailablePersonalInformationfieldItem item : personalInfoItems) {
				ManagePersonalInformation personalInfo = new ManagePersonalInformation();
				personalInfo.setPersonalInfoItem(item);
				for (AvailablePersonalInformationfield aPIField : question.getPersonalInformationfields()) {
					if (aPIField.getAvailablePersonalInformationfieldItem().getDbColumnName().equalsIgnoreCase(item.getDbColumnName())) {
						personalInfo.setSelected(true);
						if (aPIField.isFieldsRequired()) {
							personalInfo.setRequired(true);
						}
					}
				}
				mngPersonalInfos.add(personalInfo);
			}
			form.setMngPersonalInfos(mngPersonalInfos);
			
			//======================================================
			form.setCurrentPersonalInformationQuestion(question);
			form.setSurveyQuestionType(SURVEY_QUESTION_PERSONAL_INFORMATION);
			//form.setSurveyQuestionText("");
		}
		return new ModelAndView(editQuestionContainerTemplate, "context", context);
	}


	public ModelAndView saveEditSurveyQuestionInList(HttpServletRequest request, 
			HttpServletResponse response, Object command, BindException errors) throws Exception {
		edtiableFlag=0;
		AdminSurveyForm form = (AdminSurveyForm)command;
		Survey survey = surveyService.loadForUpdateSurvey(form.getSid());
		form.getEditableSurveyQuestions().clear();
		
		//save the questions in the form object
		String questionType = form.getSurveyQuestionType();
		if(questionType.equalsIgnoreCase(SURVEY_QUESTION_MULTIPLE_CHOICE_MULTIPLE_SELECT)){
			MultipleSelectSurveyQuestion question = form.getCurrentMultipleSelectSurveyQuestion();
			question.setText(form.getSurveyQuestionText());
			question.setRequired(form.isSurveyQuestionRequired());
			if(form.getEditableQuestionId()!=0){
				SurveyQuestion surveyquest = surveyService.getSurveyQuestionById(form.getEditableQuestionId());
				question.setDisplayOrder(surveyquest.getDisplayOrder());
				}
			form.setCurrentMultipleSelectSurveyQuestion(question);
			form.getEditableSurveyQuestions().add(question);
		}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_MULTIPLE_CHOICE_SINGLE_SELECT)){
			SingleSelectSurveyQuestion question = form.getCurrentSingleSelectSurveyQuestion();
			question.setText(form.getSurveyQuestionText());
			question.setRequired(form.isSurveyQuestionRequired());
			if(form.getEditableQuestionId()!=0){
				SurveyQuestion surveyquest = surveyService.getSurveyQuestionById(form.getEditableQuestionId());
				question.setDisplayOrder(surveyquest.getDisplayOrder());
				}
			form.setCurrentSingleSelectSurveyQuestion(question);
			form.getEditableSurveyQuestions().add(question);
		}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_DROP_DOWN_SINGLE_SELECT)){
			SingleSelectSurveyQuestion question = form.getCurrentSingleSelectSurveyQuestion();
			question.setText(form.getSurveyQuestionText());
			question.setRequired(form.isSurveyQuestionRequired());
			if(form.getEditableQuestionId()!=0){
				SurveyQuestion surveyquest = surveyService.getSurveyQuestionById(form.getEditableQuestionId());
				question.setDisplayOrder(surveyquest.getDisplayOrder());
				}
			form.setCurrentSingleSelectSurveyQuestion(question);
			form.getEditableSurveyQuestions().add(question);
		}else if( questionType.equalsIgnoreCase(SURVEY_QUESTION_TEXT_BOX_256_CHARACTERS) ) {
			TextBoxSurveyQuestion question = form.getCurrentFillInTheBlankSurveyQuestion();
			question.setText(form.getSurveyQuestionText());
			question.setRequired(form.isSurveyQuestionRequired());
			if(form.getEditableQuestionId()!=0){
				SurveyQuestion surveyquest = surveyService.getSurveyQuestionById(form.getEditableQuestionId());
				question.setDisplayOrder(surveyquest.getDisplayOrder());
				}
			form.setCurrentFillInTheBlankSurveyQuestion(question);
			question.setSingleLineResponse(true);
			form.getEditableSurveyQuestions().add(question);
		}else if( questionType.equalsIgnoreCase(SURVEY_QUESTION_TEXT_BOX_UNLIMITED) ) {
			TextBoxSurveyQuestion question = form.getCurrentFillInTheBlankSurveyQuestion();
			question.setText(form.getSurveyQuestionText());
			question.setRequired(form.isSurveyQuestionRequired());
			form.setCurrentFillInTheBlankSurveyQuestion(question);
			if(form.getEditableQuestionId()!=0){
				SurveyQuestion surveyquest = surveyService.getSurveyQuestionById(form.getEditableQuestionId());
				question.setDisplayOrder(surveyquest.getDisplayOrder());
				}
			question.setSingleLineResponse(false);
			form.getEditableSurveyQuestions().add(question);
		}else if( questionType.equalsIgnoreCase(SURVEY_QUESTION_MULTIPLE_CHOICE_RATING_SELECT) ) {
			// do nothing.
		}else if ( questionType.equalsIgnoreCase(SURVEY_QUESTION_PERSONAL_INFORMATION) ) {
			PersonalInformationSurveyQuestion question = form.getCurrentPersonalInformationQuestion();
			question.setRequired(form.isSurveyQuestionRequired());
			List<AvailablePersonalInformationfield> personalInfoFields = new ArrayList<AvailablePersonalInformationfield>();
			for (ManagePersonalInformation item : form.getMngPersonalInfos()) {
				if (item.isSelected()) {
					AvailablePersonalInformationfield personalInfoField = new AvailablePersonalInformationfield();
					personalInfoField.setAvailablePersonalInformationfieldItem(item.getPersonalInfoItem());
					personalInfoField.setFieldsRequired(item.isRequired());
					personalInfoFields.add(personalInfoField);
				}
			}
			question.setPersonalInformationfields(personalInfoFields);
			question.setText(form.getSurveyQuestionText());
			if(form.getEditableQuestionId()!=0){
				SurveyQuestion surveyquest = surveyService.getSurveyQuestionById(form.getEditableQuestionId());
				question.setDisplayOrder(surveyquest.getDisplayOrder());
				}
			//question = (PersonalInformationSurveyQuestion)surveyService.addSurveyQuestion(question);
			form.setCurrentPersonalInformationQuestion(question);
			form.getEditableSurveyQuestions().add(question);
		}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_MULTIPLE_CHOICE_RATING_SELECT)){

		}
		else if ( questionType.equalsIgnoreCase(SURVEY_QUESTION_CUSTOM) ) {
            List<AggregateSurveyQuestionItem> aggregateSurveyQuestionItem = new ArrayList<AggregateSurveyQuestionItem>();
            AggregateSurveyQuestion question = form.getCurrentCustomSurveyQuestion();
            question.setRequired(form.isSurveyQuestionRequired());
            question.setText(form.getSurveyQuestionText());
            form.setCurrentCustomSurveyQuestion(question);
            form.getEditableSurveyQuestions().add(question);
            for( int i = 0 ; i < form.getCustomQuestionResponceTypes().size() ; i ++ ) {
                String resType = form.getCustomQuestionResponceTypes().get(i);
                String label = form.getResponceLabels().get(i);
                String choices = form.getAnswerChoices().get(i);
                Boolean isRequired = form.getResRequired().get(i);
                 if( resType.equalsIgnoreCase(SURVEY_CUSTOM_RESPONCE_MULTIPLE_SELECT) ) {
                    AggregateSurveyQuestionItem aggrSurveyQuestion = new AggregateSurveyQuestionItem();
                   
                    MultipleSelectSurveyQuestion surveyQun = new MultipleSelectSurveyQuestion();
                    surveyQun.setText(label);
                    readCustomResponceChoices( surveyQun, choices);
					
					if(form.getEditableQuestionId()!=0 ){
                    	surveyQun.setDisplayOrder(form.getAggregateSurveyQuestionItem().get(i).getDisplayOrder());
                    	aggrSurveyQuestion.setDisplayOrder(form.getAggregateSurveyQuestionItem().get(i).getDisplayOrder());
        				}
                    else{
                    surveyQun.setDisplayOrder(i);
                    aggrSurveyQuestion.setDisplayOrder(i);
                    }
                    aggrSurveyQuestion.setQuestion(surveyQun);
                    aggrSurveyQuestion.getQuestion().setRequired(isRequired);
                    aggrSurveyQuestion.setAggregateSurveyQuestion(question);
                    aggregateSurveyQuestionItem.add(aggrSurveyQuestion);
                }else if( resType.equalsIgnoreCase(SURVEY_CUSTOM_RESPONCE_SINGLE_SELECT) ) {
                    AggregateSurveyQuestionItem aggrSurveyQuestion = new AggregateSurveyQuestionItem();
                    SingleSelectSurveyQuestion surveyQun = new SingleSelectSurveyQuestion();
                    surveyQun.setText(label);
                    readCustomResponceChoices( surveyQun, choices);
					if(form.getEditableQuestionId()!=0 ){
                    	surveyQun.setDisplayOrder(form.getAggregateSurveyQuestionItem().get(i).getDisplayOrder());
                    	aggrSurveyQuestion.setDisplayOrder(form.getAggregateSurveyQuestionItem().get(i).getDisplayOrder());
        				}
                    else{
                    surveyQun.setDisplayOrder(i);
                    aggrSurveyQuestion.setDisplayOrder(i);
                    }
                    aggrSurveyQuestion.setQuestion(surveyQun);
                    aggrSurveyQuestion.getQuestion().setRequired(isRequired);
                    aggrSurveyQuestion.setAggregateSurveyQuestion(question);
                    aggregateSurveyQuestionItem.add(aggrSurveyQuestion);
                } else if( resType.equalsIgnoreCase(SURVEY_CUSTOM_RESPONCE_TEXT) ) {
                    AggregateSurveyQuestionItem aggrSurveyQuestion = new AggregateSurveyQuestionItem();
                    TextBoxSurveyQuestion surveyQun = new TextBoxSurveyQuestion();
                    surveyQun.setText(label);
                    boolean isMulti = form.getIsMultiline().get(i);
                    surveyQun.setSingleLineResponse(isMulti);
					if(form.getEditableQuestionId()!=0 ){
                    	surveyQun.setDisplayOrder(form.getAggregateSurveyQuestionItem().get(i).getDisplayOrder());
                    	aggrSurveyQuestion.setDisplayOrder(form.getAggregateSurveyQuestionItem().get(i).getDisplayOrder());
        				}
                    else{
                    surveyQun.setDisplayOrder(i);
                    aggrSurveyQuestion.setDisplayOrder(i);
                    }
              
                    aggrSurveyQuestion.setQuestion(surveyQun);
                    aggrSurveyQuestion.getQuestion().setRequired(isRequired);
                    aggrSurveyQuestion.setAggregateSurveyQuestion(question);
                    aggregateSurveyQuestionItem.add(aggrSurveyQuestion);
                } else if( resType.equalsIgnoreCase(SURVEY_CUSTOM_RESPONCE_BLANKS) ) {
                    /*FillIntheBlankSurveyAnswerItem ansItem = new FillIntheBlankSurveyAnswerItem();
                    ansItem.setLabel(label);
                    ansItem.setSurveyQuestion(question);
                    ansItem.setDisplayOrder(i);*/
                }
            }
            form.setAggregateSurveyQuestionItem(aggregateSurveyQuestionItem);
           
        }
		AdminSurveyValidator validator = (AdminSurveyValidator)this.getValidator();
		validator.validateQuestion(form, errors);
		if(errors.hasErrors()){
			Map<Object, Object> context = new HashMap<Object, Object>();
			context.put("customResponceTypes", CUSTOM_RESPONCE_TYPES);
			context.put("resNumber", NUMBER_OF_CUSTOM_RESPONCES);
			context.put("surveyQuestionTypes", SURVEY_QUESTION_TYPES);			
			return new ModelAndView(editQuestionContainerTemplate, "context", context);
		}
		
		if (form.getEditableSurveyQuestions() != null && form.getEditableSurveyQuestions().size() > 0) {
			survey = surveyService.saveEditedQuestion(form.getSid(), form.getEditableSurveyQuestions());
		}
		surveyService.addSurvey(survey);
		
		

		Map<String, String> context = new HashMap <String, String>();
		context.put("target", "showQuestionView");
		//return new ModelAndView(redirectTemplate,"context",context);
		saveQuestionAndDisplaySurveys(request, response, command, errors);
	
		return new ModelAndView("redirect:adm_manageSyrvey.do?method=showQuestionView&sid="+form.getSid());
	}


	private String getAnswer(List<SurveyAnswerItem> answerList) {
		String answer = "";
		Collections.sort(answerList);
		for (SurveyAnswerItem answerItem : answerList) {
			answer = answer + answerItem.getLabel() + "\n";
		}
		return answer;
	}

	//handler method courses pages
	public ModelAndView showResponseSummaryView(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		AdminSurveyForm form = (AdminSurveyForm)command;
		//Long surveyId = form.getSurveyId();
		Long surveyId = form.getSid();
		Survey survey = surveyService.getSurveyByID(surveyId);
		if(survey.getSections()!=null && survey.getSections().size()>0) {
			log.debug("this survey has sections");
			return showResponseSummarySectionView(request, response, form, errors);
		}

		//com.softech.vu360.lms.model.Survey survey = surveyService.getSurveyByID(surveyId); //get the Survey object somehow????
		SurveyAnalysis analysis = surveyService.getSurveyResponseAnalysis(surveyId);

		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("surveyResponseAnalysis", analysis);

		return new ModelAndView(surveyAnalyzeTemplate, "context", context);
	}
	
	private void calculateSection(SurveySectionVO sectionVO, Long surveyId) {
		log.debug("calculating section " + sectionVO.getId());
		if(sectionVO.getChildren()!=null && sectionVO.getChildren().size()>0) {
			for (SurveySectionVO childsectionVO : sectionVO.getChildren()) {
				calculateSection(childsectionVO, surveyId);
			}
		}
		
		List<SurveyQuestionBankVO> frameworks = sectionVO.getSurveyQuestionBanks();
		for (SurveyQuestionBankVO surveyFrameworkVO : frameworks) {
			
			// check for all questions in a section
			List<com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion> questions = surveyFrameworkVO.getSurveyQuestions();
			for (com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion questionVO : questions) {
				
				// setting answer question count
				Integer answeredQuestions = surveyService.getAnsweredQuestionCount(surveyId, sectionVO.getId(), questionVO.getSurveyQuestionRef().getId());
				questionVO.setTotalCompletedQuestion(answeredQuestions);
				// setting skipped question count
				Integer skippedQuestions = surveyService.getStartedSurveys(surveyId).intValue() - answeredQuestions.intValue();
				questionVO.setSkippedQuestion(skippedQuestions);
				
				Integer responseSum = surveyService.getQuestionAnswersCount(surveyId, sectionVO.getId(), questionVO.getSurveyQuestionRef().getId());
				questionVO.setSumResponseCount(responseSum);
				
				List<com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem> answers = questionVO.getAnswerItems();
				for (com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem answerVO : answers) {
					Integer responseCount = surveyService.getAnswerCount(surveyId, sectionVO.getId(), questionVO.getSurveyQuestionRef().getId(), answerVO.getSurveyAnswerItemRef().getId());
					answerVO.setResponseCount(responseCount);
					Float responsePercent = (responseCount.floatValue()/questionVO.getSumResponseCount().floatValue())*100.0f;
					if(responsePercent.isNaN()) {
						responsePercent = 0.0f;
					}
					DecimalFormat decimalFormat = new DecimalFormat("##.##");
					responsePercent = new Float(decimalFormat.format(responsePercent));
					answerVO.setResponsePercent(responsePercent);
				}
				
			}
			
		}
	}
	
	private SurveyAnalysis doCalculate(SurveyAnalysis analysis) {
		logger.debug("calculating ...");
		Survey survey = analysis.getAnalyzedSurvey();
		Integer startedSurvey = surveyService.getStartedSurveys(survey.getId());
		analysis.setStartedSurveyCount(startedSurvey);
		analysis.setCompletedSurveyCount(startedSurvey);
		List<SurveySectionVO> sections = analysis.getSectionVOs();
		for (SurveySectionVO surveySectionVO : sections) {
			calculateSection(surveySectionVO, survey.getId());
		}
		return analysis;
	}
	
	private SurveySectionVO createEmptySection(SurveySection surveySection) {
		logger.debug("creating section " + surveySection.getId() + " " + surveySection.getName());
		// setting section
		SurveySectionVO surveySectionResultSummaryVO = new SurveySectionVO();
		surveySectionResultSummaryVO.setId(surveySection.getId());
		surveySectionResultSummaryVO.setName(surveySection.getName());
		
		// setting children recursively
		List<SurveySection> childrenSections = surveySection.getChildren();
		for (SurveySection childSurveySection : childrenSections) {
			logger.debug("creating child section " + childSurveySection.getId() + " " + childSurveySection.getName());
			SurveySectionVO childSection = createEmptySection(childSurveySection);
			surveySectionResultSummaryVO.getChildren().add(childSection);
		}
		
		List<SurveySectionSurveyQuestionBank> frameworkMappings = surveySection.getSurveySectionSurveyQuestionBanks();
		if(frameworkMappings!=null && frameworkMappings.size()>0) {
			
			// setting section > frameworks
			for (SurveySectionSurveyQuestionBank surveySectionSurveyQuestionBank : frameworkMappings) {
				SurveyQuestionBank framework = surveySectionSurveyQuestionBank.getSurveyQuestionBank();
				logger.debug("creating framework " + framework.getId() + " " + framework.getName());
				SurveyQuestionBankVO frameworkResultSummaryVO = new SurveyQuestionBankVO();
				surveySectionResultSummaryVO.getSurveyQuestionBanks().add(frameworkResultSummaryVO);
				frameworkResultSummaryVO.setId(framework.getId());
				// setting framework > questions
				List<SurveyQuestion> questions = framework.getQuestions();
				for (SurveyQuestion surveyQuestion : questions) {
					logger.debug("creating question " + surveyQuestion.getId() + " " + surveyQuestion.getText());
					com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion questionResultSummaryVO = new com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion(surveyQuestion);
					// setting framework > questions > answers
					List<SurveyAnswerItem> surveyAnswers = surveyQuestion.getSurveyAnswers();
					for (SurveyAnswerItem surveyAnswer : surveyAnswers) {
						logger.debug("creating answer " + surveyAnswer.getId() + " " + surveyAnswer.getLabel());
						com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem answerResultSummaryVO = new com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem(surveyAnswer);
						questionResultSummaryVO.getAnswerItems().add(answerResultSummaryVO);
					}
					
					frameworkResultSummaryVO.getSurveyQuestions().add(questionResultSummaryVO);
				}
			}
		}
		
		return surveySectionResultSummaryVO;
	}
	
	private SurveyAnalysis doAnanyzeSurvey(Long surveyId) {
		logger.debug("analyzing survey with id " + surveyId);
		Survey survey = surveyService.getSurveyByID(surveyId);
		SurveyAnalysis analysis = new SurveyAnalysis(survey);
		
		// creating empty hierarchy
		List<SurveySection> surveySections = surveyService.getRootSurveySections(surveyId);
		for (SurveySection surveySection : surveySections) {
			SurveySectionVO emptyResultSummaryVO = createEmptySection(surveySection);
			analysis.getSectionVOs().add(emptyResultSummaryVO);
		}
		
		analysis = doCalculate(analysis);
		
		return analysis;
	}
	
	public ModelAndView showResponseSummarySectionView(HttpServletRequest request, HttpServletResponse response, AdminSurveyForm form, BindException errors) throws Exception {
		log.debug("in showResponseSummarySectionView");
		Long surveyId = form.getSid();
		SurveyAnalysis analysis = doAnanyzeSurvey(surveyId);
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("surveyResponseAnalysis", analysis);
		return new ModelAndView(surveyAnalyzeSectionTemplate, "context", context);
	}

	public ModelAndView closeSurveyAnalyzeAndDisplaySurveys(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		//redirect to the display survey page
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("target", "displaySurveys");
		return new ModelAndView(redirectTemplate,"context",context);
	}

	public ModelAndView showSurveyAnalizeForIndividuals(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		AdminSurveyForm form = (AdminSurveyForm)command;

		/*Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getProxyCustomer();
		Distributor distributor = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getProxyDistributor();*/
		List<SurveyResult> surveyResultList = null;
		//Survey survey = surveyService.getSurveyByID(form.getSid());
		//VU360User user = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		surveyResultList = surveyService.findSurveyResult(form.getSid());
		/*if( distributor != null && ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentMode().equals(VU360UserMode.ROLE_LMSADMINISTRATOR) ) {
			surveyResultSet = (Set<SurveyResult>) surveyService.getNotReviewedFlaggedSurveyResult(distributor);
		} else {
			surveyResultSet = (Set<SurveyResult>) surveyService.getNotReviewedFlaggedSurveyResult(customer); //??? all surveys...no filters???
		}*/

		/*List<SurveyResult> surveyResults = new ArrayList<SurveyResult>();

		if (surveyResultSet != null && surveyResultSet.size() > 0) {
			Iterator<SurveyResult> itr = surveyResultSet.iterator(); 
			while(itr.hasNext()) {
				SurveyResult sr = itr.next(); 
				surveyResults.add(sr);
			} 
		}*/
		form.setSurveyResults(surveyResultList);

		return new ModelAndView(surveyAnalizeIndividualTemplate);
	}

	public ModelAndView showSurveyAnalizeResponse(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		log.debug("in showSurveyAnalizeResponse");
		AdminSurveyForm form = (AdminSurveyForm)command;
		List<Long> sqIndex = new ArrayList<Long>();
		Long vuId = null;
		int nextPageIndex = 0;
		int questionIndex = 0;
		int questionNo = 0;
		int i; int j = 0; int index = 0;
		if(request.getParameter("vuId")!= null){
			vuId=Long.parseLong(((request.getParameter("vuId")).trim()));
		}
		
		VU360User user = vu360UserService.getUserById(vuId);
		Learner learner = user.getLearner();
		Survey survey0 = surveyService.getSurveyByID(form.getSid());
		Map<Object, Object> context = new HashMap<Object, Object>();
		SurveyResult surveyResult = surveyService.getSurveyResultByLearnerAndSurvey(user, survey0);
		form.setSurveyResult(surveyResult);
		com.softech.vu360.lms.model.Survey survey = surveyResult.getSurvey();
		List<SurveyResultAnswer> surveyResultAnswers = null;
		if(surveyResult != null) {
			surveyResultAnswers = surveyResult.getAnswers();
//			surveyService.refresh(surveyResultAnswers);
		}
		
		if(survey.getSections()!=null && !survey.getSections().isEmpty()) {
			log.debug("Hurray!!! survey section found");
			return showSurveySectionAnalizeResponse(survey, request, form, surveyResultAnswers, learner);
		} else {
			if( request.getParameter("nextPageIndex") != null && !survey.getIsShowAll() ) {
				index = Integer.parseInt(request.getParameter("nextPageIndex"));
				questionIndex = index*survey.getQuestionsPerPage();
				questionNo = index*survey.getQuestionsPerPage();
			}
			SurveyResponseBuilder builder = new SurveyResponseBuilder(survey); 
			List<SurveyQuestion> surveyQuestionList = survey.getQuestions();
			Map<Long, List<SurveyResultAnswer>> surveyResultAnswerMap = new HashMap<Long, List<SurveyResultAnswer>>();
			Collections.sort(surveyQuestionList);


			boolean flag = false;
			for( i = questionIndex ; i<surveyQuestionList.size() ; i++ ) {
				j++;
				com.softech.vu360.lms.model.SurveyQuestion question = surveyQuestionList.get(i);
				flag = false;
				if (question instanceof AggregateSurveyQuestion) {
	                AggregateSurveyQuestion aggrtQuestion = (AggregateSurveyQuestion)question;
	                List<AggregateSurveyQuestionItem> aggregateSurveyQuestionItems = surveyService.getAggregateSurveyQuestionItemsByQuestionId(aggrtQuestion.getId());
	                List<SurveyResultAnswer> ResultAnswersList = new ArrayList<SurveyResultAnswer>();
	                int cc=i;
	                for(AggregateSurveyQuestionItem aggregateSurveyQuestionItemNew : aggregateSurveyQuestionItems) {
	               
	                    ResultAnswersList.add(surveyService.getSurveyResultByLearnerAndSurveyAndQuestion(aggregateSurveyQuestionItemNew.getQuestion().getId(), surveyResult.getId()));
	                cc++;
	                }
	                if (aggregateSurveyQuestionItems != null) {
	                    builder.buildAggregateSurveyQuestion(aggrtQuestion, aggregateSurveyQuestionItems,ResultAnswersList);
	                }
	                flag = true;
	            }
				if (surveyResultAnswers != null) {
					for(SurveyResultAnswer surveyResultAnswer : surveyResultAnswers) {
						if (question.getId().compareTo(surveyResultAnswer.getQuestion().getId()) == 0) {
							if (question instanceof PersonalInformationSurveyQuestion) {
								builder.buildPersonalInformationQuestion(question, surveyResultAnswer, learner);
								
								flag = true;
							} else {
								if (surveyResultAnswer.getQuestion().getId().compareTo(question.getId()) == 0) {
									builder.buildQuestion(question, surveyResultAnswer);
									flag = true;
									break;
								} 
							}
						}
					}
				}
				if (!flag) {
					builder.buildQuestion(question, null);
				}
				if( !survey.getIsShowAll() && j == survey.getQuestionsPerPage() ) {
					i++;
					break;
				}
				
				List<SurveyResultAnswer> surveyAnswersList = new ArrayList<SurveyResultAnswer>();
				SurveyResultAnswer surveyResultAnswer = surveyService.getSurveyResultAnswerByQuestionIdAndResultId(question.getId().longValue(), learner.getVu360User().getId().longValue());
				
				if(surveyResultAnswer != null) {
					List<SurveyAnswerItem> surveyAnswerItems = surveyResultAnswer.getSurveyAnswerItems();
					if(surveyAnswerItems != null && !surveyAnswerItems.isEmpty()) {
						if(question instanceof MultipleSelectSurveyQuestion) {
							surveyAnswersList.add(surveyResultAnswer);
							surveyResultAnswerMap.put(question.getId(), surveyAnswersList);
						} else if(question instanceof SingleSelectSurveyQuestion) {
							surveyAnswersList.add(surveyResultAnswer);
							surveyResultAnswerMap.put(question.getId(), surveyAnswersList);
						} else if(question instanceof AggregateSurveyQuestion) {
							if(question instanceof MultipleSelectSurveyQuestion) {
								surveyAnswersList.add(surveyResultAnswer);
								surveyResultAnswerMap.put(question.getId(), surveyAnswersList);
							} else if(question instanceof SingleSelectSurveyQuestion) {
								surveyAnswersList.add(surveyResultAnswer);
								surveyResultAnswerMap.put(question.getId(), surveyAnswersList);
							} else if(question instanceof TextBoxSurveyQuestion) {
								if(!StringUtils.isEmpty(surveyResultAnswer.getSurveyAnswerText()) && !surveyResultAnswer.getSurveyAnswerText().equals(" ")) {
									surveyAnswersList.add(surveyResultAnswer);
									surveyResultAnswerMap.put(question.getId(), surveyAnswersList);
								}
									
							}
						}
					} else if(!StringUtils.isEmpty(surveyResultAnswer.getSurveyAnswerText()) && !surveyResultAnswer.getSurveyAnswerText().equals(" ")) {
						surveyAnswersList.add(surveyResultAnswer);
						surveyResultAnswerMap.put(question.getId(), surveyAnswersList);
					}
				}
			}
			com.softech.vu360.lms.web.controller.model.survey.Survey surveyView = builder.getSurveyView();
			//Added to provide question index to the custom multi choice multi select
			for(int z=0;z<surveyView.getQuestionList().size();z++){
				if(surveyView.getQuestionList().get(z).getSurveyQuestionRef() instanceof AggregateSurveyQuestion){
					sqIndex.add((long) z);
				}
			}
			int z=0;
			for(int k=0;k<surveyView.getQuestionList().size();k++){
				com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion squestion =surveyView.getQuestionList().get(k);
					if(squestion.getSurveyQuestionRef() instanceof AggregateSurveyQuestion){
						surveyView.getQuestionList().get(k).setIndex(sqIndex.get(z));
						z++;
					}
			}
			//set it in the form...
			form.setSurvey(surveyView);
			if( i == surveyQuestionList.size() || survey.getIsShowAll() ) 
				nextPageIndex = 0;
			else
				nextPageIndex = index + 1;
			context.put("nextPageIndex", nextPageIndex);
			context.put("questionNo", questionNo);
			context.put("surveyResultAnswers", surveyResultAnswerMap);
			return new ModelAndView(surveyAnalizeResponseTemplate, "context", context);
		}

		
	}
	
	protected SurveySectionVO populateSectionVO(SurveySection section, com.softech.vu360.lms.web.controller.model.survey.Survey surveyVO, Long userId, Map<String, SurveyResultAnswer> surveyResultAnswerMap) {
		SurveySectionVO vo = new SurveySectionVO();
		vo.setId(section.getId());
		vo.setName(section.getName());
		vo.setDescription(section.getDescription());
		
		for(SurveySectionSurveyQuestionBank frameworkMapping : section.getSurveySectionSurveyQuestionBanks()) {
			SurveyQuestionBank framework = frameworkMapping.getSurveyQuestionBank();
			SurveyQuestionBankVO surveyQuestionBankVO = new SurveyQuestionBankVO();
			surveyQuestionBankVO.setId(framework.getId());
			surveyQuestionBankVO.setName(framework.getName());
			surveyQuestionBankVO.setDescription(framework.getDescription());
			vo.getSurveyQuestionBanks().add(surveyQuestionBankVO);
			
			for(SurveyQuestion surveyQuestion : framework.getQuestions()) {
				com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion questionVO = new com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion(surveyQuestion);
				String questionClassName = questionVO.getSurveyQuestionRef().getClass().getName();
				if( questionClassName.equalsIgnoreCase(MultipleSelectSurveyQuestion.class.getName()) ) {
					questionVO.setTemplatePath(SurveyQuestionBuilder.MULTICHOICE_MULTISELECT_TEMPLATE);
				} else if ( questionClassName.equalsIgnoreCase(SingleSelectSurveyQuestion.class.getName()) ) {
					questionVO.setTemplatePath(SurveyQuestionBuilder.MULTICHOICE_SINGLESELECT_TEMPLATE);
				} else if ( questionClassName.equalsIgnoreCase(TextBoxSurveyQuestion.class.getName()) ) {
					questionVO.setTemplatePath(SurveyQuestionBuilder.COMMENT_UNLIMITEDTEXT_TEMPLATE);
				} else if ( questionClassName.equalsIgnoreCase(PersonalInformationSurveyQuestion.class.getName()) ) {
					questionVO.setTemplatePath(SurveyQuestionBuilder.PERSONAL_INFORMATION_TEMPLATE);
				} else if ( questionClassName.equalsIgnoreCase(AggregateSurveyQuestion.class.getName()) ) {
					questionVO.setTemplatePath(SurveyQuestionBuilder.CUSTOM_MULTICHOICE_MULTISELECT_TEMPLATE);
				}
				questionVO.setCanHaveFile(surveyQuestion.isCanHaveFile());
				questionVO.setFileRequired(surveyQuestion.isFileRequired());

				SurveyResultAnswer surveyResultAnswer = surveyService.getSurveyResultAnswer(surveyQuestion.getId(), framework.getId(), section.getId(), userId);
//				surveyResultAnswer = (SurveyResultAnswer) surveyService.refresh(surveyResultAnswer);
				for(SurveyAnswerItem answerItem : surveyQuestion.getSurveyAnswers()) {
					com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem answerVO = new com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem(answerItem);
					if(surveyResultAnswer!=null){
						if(surveyResultAnswer.getSurveyAnswerItems().contains(answerItem)) {
							answerVO.setSelected(true);
							questionVO.setSingleSelectAnswerId(answerItem.getId());
						}
						String answerText = null;
						if(surveyResultAnswer.getSurveyAnswerText()!=null && !(surveyResultAnswer.getSurveyAnswerText().trim().isEmpty())) {
							answerText = surveyResultAnswer.getSurveyAnswerText();
							questionVO.setAnswerText(answerText);
						}
					}
					questionVO.getAnswerItems().add(answerVO);
				}
				
				if(surveyResultAnswer!=null) {
					List<SurveyResultAnswerFile> surveyResultAnswerFiles = surveyResultAnswer.getSurveyResultAnswerFiles();
					for (SurveyResultAnswerFile surveyResultAnswerFile : surveyResultAnswerFiles) {
						com.softech.vu360.lms.web.controller.model.survey.SurveyResultAnswerFile fileVO = new com.softech.vu360.lms.web.controller.model.survey.SurveyResultAnswerFile( surveyResultAnswerFile );
						questionVO.getAnswerFiles().add(fileVO);
					}
				}
				
				surveyQuestionBankVO.getSurveyQuestions().add(questionVO);
				surveyVO.getQuestionList().add(questionVO);
			}
			
			SurveyQuestionBank surveyQuestionBank = frameworkMapping.getSurveyQuestionBank();
			for(SurveyQuestion question : surveyQuestionBank.getQuestions()) {
				SurveyResultAnswer surveyResultAnswer = surveyService.getSurveyResultAnswer(question.getId(), surveyQuestionBank.getId(), section.getId(), userId);
//				surveyResultAnswer = (SurveyResultAnswer) surveyService.refresh(surveyResultAnswer);
				if(surveyResultAnswer!=null && ((surveyResultAnswer.getSurveyAnswerText()!=null && !surveyResultAnswer.getSurveyAnswerText().trim().isEmpty()) || !surveyResultAnswer.getSurveyAnswerItems().isEmpty())) {
					surveyResultAnswerMap.put( (question.getId()+"."+surveyQuestionBank.getId()+"."+section.getId()), surveyResultAnswer);
				}
			}
		}
		
		if(section.getChildren()!=null && section.getChildren().size()>0) {
			for(SurveySection childSection : section.getChildren()) {
				vo.getChildren().add( populateSectionVO(childSection, surveyVO, userId, surveyResultAnswerMap) );
			}
		}
		
		return vo;
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView showSurveySectionAnalizeResponse(Survey survey, HttpServletRequest request, AdminSurveyForm form, List<SurveyResultAnswer> surveyResultAnswers, Learner learner) {
		log.debug("in showSurveySectionAnalizeResponse");
		Map<Object, Object> context = new HashMap<Object, Object>();
		SurveyPreviewVO surveyPreviewVO = new SurveyPreviewVO();
		List<SurveySectionVO> surveySectionVOs = new ArrayList<SurveySectionVO>();
		Map<String, SurveyResultAnswer> surveyResultAnswerMap = new HashMap<String, SurveyResultAnswer>();
		com.softech.vu360.lms.web.controller.model.survey.Survey surveyVO = new com.softech.vu360.lms.web.controller.model.survey.Survey(survey);
		
		List<SurveySection> rootSections = surveyService.getRootSurveySections(survey);
//		rootSections = (List<SurveySection>) surveyService.refresh(rootSections);
		for(SurveySection surveySection : rootSections) {
			SurveySectionVO surveySectionVO = populateSectionVO(surveySection, surveyVO, learner.getVu360User().getId(), surveyResultAnswerMap);
			surveySectionVOs.add(surveySectionVO);
		}
		
		form.setSurvey(surveyVO);
		surveyPreviewVO.setSurvey(survey);
		surveyPreviewVO.setSurveySectionVO(surveySectionVOs);
		context.put("survey", surveyPreviewVO);
		context.put("surveyResultAnswers", surveyResultAnswerMap);
		context.put("controller", "admin");
		return new ModelAndView(analyzeSurveySectionTemplate, "context", context);
	}
	
	public ModelAndView showSurveyFile(HttpServletRequest request, HttpServletResponse response, AdminSurveyForm form, BindException errors) throws Exception {
		log.debug("in showSurveyFile");
		Long fileId = ServletRequestUtils.getLongParameter(request, "fileId", 0);
		log.debug("got fileId=" + fileId);
		if(fileId>0l) {
			SurveyResultAnswerFile answerFile = surveyService.getSurveyFileById(fileId);
			log.debug("got answerFile=" + answerFile);
			if(answerFile!=null) {
				MimetypesFileTypeMap typeMap = new MimetypesFileTypeMap();
				String contentType = typeMap.getContentType(answerFile.getFileName());
				response.setHeader("Content-Type", contentType);
				if(contentType.compareToIgnoreCase("application/octet-stream")==0) {
					response.setHeader("Content-Disposition", ("attachment; filename=\"" + answerFile.getFileName() + "\"") );
				} else {
					response.setHeader("Content-Disposition", ("filename=\"" + answerFile.getFileName() + "\"") );
				}
				
				String filePath = answerFile.getFilePath();
				File file = new File(filePath);
				FileInputStream fileInputStream = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				while(fileInputStream.read(bytes)>-1) {
					response.getOutputStream().write(bytes);
				}
			}
		}
		return null;
	}

	public ModelAndView saveSurveySectionAnalizeResponse(HttpServletRequest request, HttpServletResponse response, AdminSurveyForm form, BindException errors) throws Exception {
		log.debug("in saveSurveySectionAnalizeResponse");
		String[] reviewcomments = request.getParameterValues("reviewcomment");
		String[] reviewcommentids = request.getParameterValues("reviewcommentid");
		String[] sectionIds = request.getParameterValues("sectionsArray");
		String[] frameworkIds = request.getParameterValues("questionBanksArray");
		String[] questionIds = request.getParameterValues("questionsArray");
		Long userId = Long.valueOf(request.getParameter("vuId"));
		
		for(int idx=0; idx<questionIds.length; ++idx) {
			Long questionId = Long.parseLong(questionIds[idx]);
			Long frameworkId = Long.parseLong(frameworkIds[idx]);
			Long sectionId = Long.parseLong(sectionIds[idx]);
			Long commentId = 0L;
			try {
				commentId = Long.parseLong(reviewcommentids[idx]);
			} catch(NumberFormatException e) {
				
			}
			String comment = reviewcomments[idx];
			
			SurveyReviewComment reviewComment = new SurveyReviewComment();
			if(commentId.longValue()>0) {
				reviewComment = surveyService.loadForUpdateSurveyReviewComment(commentId); 
			} else {
				SurveyResultAnswer answer = surveyService.getSurveyResultAnswer(questionId, frameworkId, sectionId, userId);
				if(answer!=null) {
				answer.getComments().add(reviewComment);
					reviewComment.setAnswer(answer);
					reviewComment.setCommentDate(new Date());
				}
			}
			if(comment!=null && !comment.trim().isEmpty()) {
				reviewComment.setComment(comment);
				surveyService.updateSurveyReviewComment(reviewComment);
			}
		}
		
		return new ModelAndView("redirect:adm_manageSyrvey.do?method=showSurveyAnalizeForIndividuals&sid="+form.getSurvey().getSurveyRef().getId());
	}
	
	public ModelAndView saveSurveyAnalizeResponse(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		log.debug("in saveSurveyAnalizeResponse");
		AdminSurveyForm form = (AdminSurveyForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		com.softech.vu360.lms.web.controller.model.survey.Survey survey = form.getSurvey();
		List<com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion> questionList = survey.getQuestionList();
		SurveyResult surveyResult = form.getSurveyResult();
		Long userId = Long.valueOf(request.getParameter("vuId"));
		if(survey.getSurveyRef().getSections().size()>0) {
			return saveSurveySectionAnalizeResponse(request, response, form, errors);
		}
		int index=0;
		int temp=0;
		boolean flag=false;
		if(surveyResult != null) {
			List<SurveyResultAnswer> newResultAnswersList = new ArrayList<SurveyResultAnswer>();
			for(int i =0; i<questionList.size(); i++) {
				List<SurveyReviewComment> commentList = new ArrayList<SurveyReviewComment>();
				SurveyResultAnswer surveyResultAnswer = surveyService.getSurveyResultAnswerByQuestionIdAndVU360UserId(questionList.get(i).getSurveyQuestionRef().getId().longValue(), userId);
				if(surveyResultAnswer != null) {
					if(questionList.get(index).getSurveyView()!=null){
						for(int k=0;k<questionList.get(index).getSurveyView().getQuestionList().size();k++){
								if((surveyResultAnswer.getQuestion().getId()).equals(questionList.get(index).getSurveyView().getQuestionList().get(k).getSurveyQuestionRef().getId())){
									//temp--;
									flag=true;
								}
								else{
									//temp++;
									flag=false;
								}
								//index=index-1;
							}
						//temp++;
						}
						else{
							temp++;
						}
						if(flag){
							temp++;
						}
								
						String comment = questionList.get(i).getComment();
						SurveyReviewComment reviewComment = new SurveyReviewComment();
							
						SurveyReviewComment updateSurveyReviewComment = null;
							
						SurveyReviewComment oldSurveyReviewComment = surveyService.getSurveyReviewCommentByResultId(surveyResultAnswer.getId().longValue());
							
						if(oldSurveyReviewComment != null
								&& oldSurveyReviewComment.getAnswer().getSurveyResult().getSurveyee().getId().longValue() == userId.longValue()) {
							updateSurveyReviewComment = surveyService.loadForUpdateSurveyReviewComment(oldSurveyReviewComment.getId().longValue());
							updateSurveyReviewComment.setComment(comment);
							commentList.add(updateSurveyReviewComment);
							surveyResultAnswer.setComments(commentList);
							newResultAnswersList.add(surveyResultAnswer);
							surveyService.updateSurveyReviewComment(updateSurveyReviewComment);
						} else if(surveyResultAnswer.getSurveyResult().getSurveyee().getId().longValue() == userId.longValue()){
							reviewComment.setComment(comment);
							reviewComment.setCommentedBy(surveyResult.getSurveyee().getName());
							reviewComment.setCommentDate(new Date());
							reviewComment.setAnswer(surveyResultAnswer);
							commentList.add(reviewComment);
							surveyResultAnswer.setComments(commentList);
							newResultAnswersList.add(surveyResultAnswer);
							surveyService.addSurveyReviewComment(reviewComment);
						}
							
						index=temp;
				}
					
			}
			surveyResult.setAnswers(newResultAnswersList);
			surveyService.saveSurveyResult(surveyResult);
		}
		context.put("target", "showSurveyAnalizeForIndividuals");
		return new ModelAndView(redirectTemplate,"context",context);
	}
	
	public ModelAndView cancelSurveyAnalizeResponse(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("target", "showSurveyAnalizeForIndividuals");
		return new ModelAndView(redirectTemplate,"context",context);
	}
	
	public ModelAndView learnerSurveyResponseView(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		//AdminSurveyForm form = (AdminSurveyForm)command;
		return new ModelAndView(learnerSurveyResponseTemplate);
	}

	@SuppressWarnings("unchecked")
	public ModelAndView searchLearnerSurveyResponse(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		AdminSurveyForm form = (AdminSurveyForm)command;
		//VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		List<OrganizationalGroup> tempManagedGroups = vu360UserService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());

		Map<Object,Object> results = new HashMap<Object,Object>();
		Map<Object, Object> context = new HashMap<Object, Object>();
		Map<String,String> pagerAttributeMap = new HashMap<String,String>();
		List<VU360User> userList=null;
		int pageNo = form.getPageIndex()<0 ? 0 : form.getPageIndex()/VelocityPagerTool.DEFAULT_PAGE_SIZE;
		if(form.getSearchType().equalsIgnoreCase("allsearch")) {
			results = learnerService.findAllLearners("", 
					loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
					loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
					loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
					"firstName", 0);
			userList = (List<VU360User>)results.get("list");
		} else {
			results = learnerService.findLearner1(form.getFirstName()
					,form.getLastName(), form.getMailAddress(), 
					loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
					loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
					loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
					pageNo
					, VelocityPagerTool.DEFAULT_PAGE_SIZE
					, "firstName", 0);
			userList = (List<VU360User>)results.get("list");
		}
		Integer totalResults = (Integer)results.get("recordSize");
		List<LearnerSurvey> learnerSurveyList = new ArrayList<LearnerSurvey>();
		for (VU360User user : userList) {
			LearnerSurvey learnerSurvey = new LearnerSurvey();
			learnerSurvey.setUser(user);
			Map<String,Integer> surveyComplete = new HashMap<String,Integer>();
			surveyComplete = surveyService.getTotalSurveyByUser(user);
			int completedSurvey = surveyComplete.get("CompletedCount");
			int totalSurvey = surveyComplete.get("TotalCount");
			learnerSurvey.setTotalSurvey(totalSurvey);
			learnerSurvey.setCompletedSurvey(completedSurvey);
			learnerSurveyList.add(learnerSurvey);
		}
		context.put("learners", userList);
		context.put("learnerSurveyList", learnerSurveyList);
		context.put("totalResults", totalResults);
		pagerAttributeMap.put("pageIndex", Integer.toString(form.getPageIndex()));
		pagerAttributeMap.put("totalCount", totalResults.toString());
		request.setAttribute("PagerAttributeMap", pagerAttributeMap);
		return new ModelAndView(learnerSurveyResponseTemplate,"context",context);
	}

	public ModelAndView showSurveyResponse(HttpServletRequest request, HttpServletResponse response, 
			Object command, BindException errors) throws Exception {

		AdminSurveyForm form = (AdminSurveyForm)command;
		VU360User user = vu360UserService.getUserById(form.getLearnerId());
		List<Survey> dueSurveyList = surveyService.getDueSurveysByUser(user);
		List<Survey> completedSurveyList = surveyService.getCompletedSurveysByUser(user);
		List<Survey> surveyList = new ArrayList<Survey>();
		List<LearnerEnrollment> enrollments = entitlementService.getLearnerEnrollmentsForLearner(user.getLearner());
		List<SurveyStatus> learnerSurveyStatusList = new ArrayList<SurveyStatus>();

		List<Course> surveyCourses = new ArrayList<Course>();
		for(LearnerEnrollment lenrollment : enrollments){
			surveyCourses.add(lenrollment.getCourse());
		}

		surveyList.addAll(dueSurveyList);
		surveyList.addAll(completedSurveyList);

		learnerSurveyStatusList = this.arrangeLearnerResponseList(learnerSurveyStatusList, surveyList, enrollments, user, surveyCourses );

		//Collections.sort(learnerSurveyStatusList);
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("user", user);
		context.put("surveyList", learnerSurveyStatusList);
		return new ModelAndView(surveyResponseTemplate,"context",context);
	}

	private List<SurveyStatus> arrangeLearnerResponseList(List<SurveyStatus> learnerResponseList
			, List<Survey> surveyList, List<LearnerEnrollment> enrollments, VU360User user
			, List<Course> surveyCourses) {

		for(Course c : surveyCourses) {
			SurveyStatus surveyStatus = new SurveyStatus();
			List<LearnerSurveyCourse> surveyItemList = new ArrayList<LearnerSurveyCourse>();
			for(Survey s : surveyList) {
				for (Course surveyCourse : s.getCourses()) {
					if (surveyCourse.getId().compareTo(c.getId()) == 0 ) {
						surveyStatus.setCourseName(c.getCourseTitle());
						LearnerSurveyCourse surveyItem = new LearnerSurveyCourse();
						surveyItem.setSurveyName(s.getName());
						if(surveyService.getSurveyResultByUserAndCourse(user, c, s)==null)
							surveyItem.setStatus("Incomplete");
						else
							surveyItem.setStatus("Completed");
						surveyItemList.add(surveyItem);
						break;
					}
				}
			}
			if (surveyItemList != null && surveyItemList.size() > 0) {
				Collections.sort(surveyItemList);
				surveyStatus.setSurveyItem(surveyItemList);
				learnerResponseList.add(surveyStatus);
			}
		}
		return learnerResponseList;
	}


	/**
	 * handler methods for displaying manage flags page
	 */
	public ModelAndView showManageFlags(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		AdminSurveyForm form = (AdminSurveyForm)command;
		try {
			List<SurveyFlagTemplate> surveyFlagTemplates = surveyService.findAllFlagTemplate(form.getSearchedFlagName(),form.getSid());
			if(surveyFlagTemplates != null) {
				List<ManageFlag> mngFlags = new ArrayList<ManageFlag>();
				for (SurveyFlagTemplate surveyFlagTemplate : surveyFlagTemplates) {
					ManageFlag mngFlag = new ManageFlag();
					mngFlag.setFlag(surveyFlagTemplate);

					if(surveyFlagTemplate.getQuestion() instanceof MultipleSelectSurveyQuestion){
						MultipleSelectSurveyQuestion question = (MultipleSelectSurveyQuestion)surveyFlagTemplate.getQuestion();
						mngFlag.setQuestionText(question.getText());
						mngFlags.add(mngFlag);
					}else if(surveyFlagTemplate.getQuestion() instanceof SingleSelectSurveyQuestion){
						SingleSelectSurveyQuestion question = (SingleSelectSurveyQuestion)surveyFlagTemplate.getQuestion();
						mngFlag.setQuestionText(question.getText());
						mngFlags.add(mngFlag);
					}else if(surveyFlagTemplate.getQuestion() instanceof TextBoxSurveyQuestion){
						TextBoxSurveyQuestion question = (TextBoxSurveyQuestion)surveyFlagTemplate.getQuestion();
						mngFlag.setQuestionText(question.getText());
						mngFlags.add(mngFlag);
					}
				}
				form.setMngFlags(mngFlags);
			}
		} catch(Exception e) {
			System.out.println(e);
		}
		return new ModelAndView(displayManageFlagsTemplate);
	}

	/**
	 * handler methods for search flags
	 */
	public ModelAndView searchFlags(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		AdminSurveyForm form = (AdminSurveyForm)command;
		try {
			session= request.getSession();
			Map<Object, Object> context = new HashMap<Object, Object>();

			List<SurveyFlagTemplate> flags = surveyService.findAllFlagTemplate(form.getSearchedFlagName(),form.getSid());
			if(flags != null) {
				List<ManageFlag> mngFlags = new ArrayList<ManageFlag>();
				for (SurveyFlagTemplate flag : flags) {
					ManageFlag mngFlag = new ManageFlag();
					mngFlag.setFlag(flag);

					if(flag.getQuestion() instanceof MultipleSelectSurveyQuestion){
						MultipleSelectSurveyQuestion question = (MultipleSelectSurveyQuestion)flag.getQuestion();
						mngFlag.setQuestionText(question.getText());
						mngFlags.add(mngFlag);
					}else if(flag.getQuestion() instanceof SingleSelectSurveyQuestion){
						SingleSelectSurveyQuestion question = (SingleSelectSurveyQuestion)flag.getQuestion();
						mngFlag.setQuestionText(question.getText());
						mngFlags.add(mngFlag);
					}else if(flag.getQuestion() instanceof TextBoxSurveyQuestion){
						TextBoxSurveyQuestion question = (TextBoxSurveyQuestion)flag.getQuestion();
						mngFlag.setQuestionText(question.getText());
						mngFlags.add(mngFlag);
					}
				}

				//============================For Sorting============================
				Map<String,String> pagerAttributeMap = new HashMap<String,String>();
				String sortColumnIndex = request.getParameter("sortColumnIndex");
				if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
					sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
				String sortDirection = request.getParameter("sortDirection");
				if( sortDirection == null && session.getAttribute("sortDirection") != null )
					sortDirection = session.getAttribute("sortDirection").toString();
				String pageIndex = request.getParameter("pageCurrIndex");
				if( pageIndex == null ) {
					if(session.getAttribute("pageCurrIndex")==null)pageIndex="0";
					else pageIndex = session.getAttribute("pageCurrIndex").toString();
				}

				if( sortColumnIndex != null && sortDirection != null ) {

					if( sortColumnIndex.equalsIgnoreCase("0") ) {
						if( sortDirection.equalsIgnoreCase("0") ) {
							FlagSort sort = new FlagSort();
							sort.setSortBy("flagName");
							sort.setSortDirection(Integer.parseInt(sortDirection));
							Collections.sort(mngFlags,sort);
							session.setAttribute("sortDirection", 0);
							session.setAttribute("sortColumnIndex", 0);
							context.put("sortDirection", 0);
							context.put("sortColumnIndex", 0);
						} else {
							FlagSort sort = new FlagSort();
							sort.setSortBy("flagName");
							sort.setSortDirection(Integer.parseInt(sortDirection));
							Collections.sort(mngFlags,sort);
							session.setAttribute("sortDirection", 1);
							session.setAttribute("sortColumnIndex", 0);
							context.put("sortDirection", 1);
							context.put("sortColumnIndex", 0);
						}
					} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
						if( sortDirection.equalsIgnoreCase("0") ) {
							FlagSort sort = new FlagSort();
							sort.setSortBy("questionText");
							sort.setSortDirection(Integer.parseInt(sortDirection));
							Collections.sort(mngFlags,sort);
							session.setAttribute("sortDirection", 0);
							session.setAttribute("sortColumnIndex", 1);
							context.put("sortDirection", 0);
							context.put("sortColumnIndex", 1);
						} else {
							FlagSort sort = new FlagSort();
							sort.setSortBy("questionText");
							sort.setSortDirection(Integer.parseInt(sortDirection));
							Collections.sort(mngFlags,sort);
							session.setAttribute("sortDirection", 1);
							session.setAttribute("sortColumnIndex", 1);
							context.put("sortDirection", 1);
							context.put("sortColumnIndex", 1);
						}
					}
				}	

				form.setMngFlags(mngFlags);

				//context.put("sortDirection", sortDirection);
				//context.put("sortColumnIndex", sortColumnIndex);
				context.put("showAll",  (request.getParameter("showAll") == null) ? "false" : request.getParameter("showAll"));
				session.setAttribute("pageCurrIndex", pageIndex);
				pagerAttributeMap.put("showAll", (request.getParameter("showAll") == null) ? "false" : request.getParameter("showAll"));
				request.setAttribute("PagerAttributeMap", pagerAttributeMap);
			}

			return new ModelAndView(displayManageFlagsTemplate, "context", context);
		} catch (Exception e) {
			log.debug(e);
		}
		return new ModelAndView(displayManageFlagsTemplate);
	}

	/**
	 * handler methods for delete flags
	 */
	public ModelAndView deleteFlags(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		//AdminSurveyForm form = (AdminSurveyForm)command;

		String[] selectedFlags= request.getParameterValues("flag") ;
		if (selectedFlags !=null && selectedFlags.length>0)
		{
			log.debug("====== deleteFlags ==========> >>>>>>  " + selectedFlags.length);
			long[] selectedFlagIds = new long [selectedFlags.length];
			int count=0;
			for(String selectedFlag : selectedFlags){
				selectedFlagIds[count]= Long.parseLong(selectedFlag);
			}
			surveyService.deleteSurveyFlagTemplates(selectedFlagIds);
		}
		Map<String, String> context = new HashMap <String, String>();
		context.put("target", "showManageFlags");
		return new ModelAndView(redirectTemplate,"context",context);
	}

	/**
	 * handler methods for displaying Add New SurveyFlagTemplate Page
	 */
	public ModelAndView addNewFlag(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		AdminSurveyForm form = (AdminSurveyForm)command;
		//form.
		form.setFlagId(0);
		form.getFlag().setFlagName("");
		form.getFlag().setTo("");
		form.getFlag().setMessage("");
		form.getFlag().setSendMe(false);
		form.getFlag().setSubject("");
		if (form.getSurveyQuestions() != null && form.getSurveyQuestions().size() > 0) {
			List<SurveyQuestion> flagSurveyQuestions = new ArrayList<SurveyQuestion>();
			form.setSelectedQuestionId(form.getSurveyQuestions().get(0).getId());
			for (SurveyQuestion question : form.getSurveyQuestions()){
				if(question instanceof MultipleSelectSurveyQuestion){
					flagSurveyQuestions.add(question);
				} else if(question instanceof SingleSelectSurveyQuestion){
					flagSurveyQuestions.add(question);
				}
			}
			form.setFlagSurveyQuestions(flagSurveyQuestions);
			if(flagSurveyQuestions.size()>0){
				form.setSelectedQuestionId(form.getFlagSurveyQuestions().get(0).getId());
				for (SurveyQuestion question : form.getFlagSurveyQuestions()){
					if(question.getId().longValue() == form.getSelectedQuestionId()){
						form.setSurveyAnswers(question.getSurveyAnswers());
						break;
					}
				}
			}
		}
		if( StringUtils.isBlank(form.getFlag().getMessage() ) )
			form.getFlag().setMessage("");
		form.getFlag().setMessage(form.getFlag().getMessage().replaceAll("\"","'"));

		form.getFlag().setMessage( form.getFlag().getMessage().replaceAll("\r\n", "") );
		form.getFlag().setMessage( form.getFlag().getMessage().replaceAll("\n", "<br>") );
		form.getFlag().setMessage( form.getFlag().getMessage().replaceAll("\r", "<br>") );
		return new ModelAndView(addNewFlagTemplate);
	}

	public ModelAndView answerItems(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		AdminSurveyForm form = (AdminSurveyForm)command;
		for (SurveyQuestion question : form.getSurveyQuestions()){

			if(question.getId().longValue() == form.getSelectedQuestionId()){
				form.setSurveyAnswers(question.getSurveyAnswers());
			}
		}
		return new ModelAndView(addNewFlagTemplate);
	}

	/**
	 * handler methods for save new flag
	 */
	public ModelAndView saveNewFlag(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		AdminSurveyForm form = (AdminSurveyForm)command;
		SurveyFlagTemplate flag = form.getFlag();
		flag.setId(null);
		String[] selectedAnswerItems=request.getParameterValues("selectedAnswerItems");
		/*SurveyQuestion question=surveyService.getSurveyQuestionById(form.getSelectedQuestionId()) ;
		flag.setQuestion(question);*/
		String message = "" ;
		if(request.getParameter("message") != null ) {
			message = request.getParameter("message") ;
			form.getFlag().setMessage(message);
		}
		form.setSelectedAnswerItems(selectedAnswerItems);
		AdminSurveyValidator validator= (AdminSurveyValidator) getValidator();
		validator.validateSaveFlag(form, errors);
		if( errors.hasErrors() ) {

			return new ModelAndView(addNewFlagTemplate);
		}
		Survey survey=surveyService.getSurveyByID(form.getSid()) ;
		flag.setSurvey(survey);
		if(selectedAnswerItems != null && selectedAnswerItems.length>0){
			long[] answerItemIds = new long[selectedAnswerItems.length];
			int count=0;
			for(String item:selectedAnswerItems  ){
				answerItemIds[count]= Long.parseLong(item);

			}
			List<SurveyAnswerItem> surveyAnswerItems=surveyService.getSurveyAnswerItemByIdArray(answerItemIds);
			flag.setSurveyAnswers(surveyAnswerItems);
		}

		surveyService.saveSurveyFlagTemplate(flag);
		return  showManageFlags( request,  response,  command,  errors);
	}

	/**
	 * handler methods for showing flag details in edit mode
	 */
	public ModelAndView showFlagDetails(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		AdminSurveyForm form = (AdminSurveyForm)command;
		String flagId=request.getParameter("flagId");
		SurveyFlagTemplate surveyFlagTemplate=null; 
		if (flagId !=null);
		surveyFlagTemplate=surveyService.getFlagTemplateByID(Long.parseLong(flagId));
		form.setFlag(surveyFlagTemplate);
		if( StringUtils.isBlank(form.getFlag().getMessage() ) )
			form.getFlag().setMessage("");
		form.getFlag().setMessage(form.getFlag().getMessage().replaceAll("\"","'"));

		form.getFlag().setMessage( form.getFlag().getMessage().replaceAll("\r\n", "") );
		form.getFlag().setMessage( form.getFlag().getMessage().replaceAll("\n", "<br>") );
		form.getFlag().setMessage( form.getFlag().getMessage().replaceAll("\r", "<br>") );
		form.setSelectedQuestionId(surveyFlagTemplate.getQuestion().getId());
		form.setSurveyQuestions(surveyFlagTemplate.getSurvey().getQuestions());
		form.setFlagSurveyQuestions(surveyFlagTemplate.getSurvey().getQuestions());		
		form.setSurveyAnswers(surveyFlagTemplate.getSurveyAnswers());
		form.getFlag().setTo(surveyFlagTemplate.getTo());
		// surveyService.gets
		return new ModelAndView(editFlagTemplate);
	}

	/**
	 * handler methods for saving flag details in edit mode
	 */
	public ModelAndView updateFlagDetails(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		AdminSurveyForm form = (AdminSurveyForm)command;
		SurveyFlagTemplate flag = form.getFlag();
		String[] selectedAnswerItems=request.getParameterValues("selectedAnswerItems");
		/*SurveyQuestion question=surveyService.getSurveyQuestionById(form.getSelectedQuestionId()) ;
		flag.setQuestion(question);*/
		if(selectedAnswerItems != null && selectedAnswerItems.length>0){
			long[] answerItemIds = new long[selectedAnswerItems.length];
			int count=0;
			for(String item:selectedAnswerItems  ){
				answerItemIds[count]= Long.parseLong(item);

			}
			List<SurveyAnswerItem> surveyAnswerItems=surveyService.getSurveyAnswerItemByIdArray(answerItemIds);
			flag.setSurveyAnswers(surveyAnswerItems);
		}
		String message = "" ;
		if(request.getParameter("message") != null ) {
			message = request.getParameter("message") ;
			form.getFlag().setMessage(message);
		}
		AdminSurveyValidator validator= (AdminSurveyValidator) getValidator();
		validator.validateSaveFlag(form, errors);
		if( errors.hasErrors() ) {

			return new ModelAndView(editFlagTemplate);
		}
		surveyService.saveSurveyFlagTemplate(flag);
		return new ModelAndView(displayManageFlagsTemplate);
	}


	//specific properties injections
	public String getDefaultManageSurveysTemplate() {
		return defaultManageSurveysTemplate;
	}

	public void setDefaultManageSurveysTemplate(String defaultManageSurveysTemplate) {
		this.defaultManageSurveysTemplate = defaultManageSurveysTemplate;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public String getAddQuestionTemplate() {
		return addQuestionTemplate;
	}

	public void setAddQuestionTemplate(String addQuestionTemplate) {
		this.addQuestionTemplate = addQuestionTemplate;
	}

	public String getAddCourseTemplate() {
		return addCourseTemplate;
	}

	public void setAddCourseTemplate(String addCourseTemplate) {
		this.addCourseTemplate = addCourseTemplate;
	}

	public String getAddQuestionContainerTemplate() {
		return addQuestionContainerTemplate;
	}

	public void setAddQuestionContainerTemplate(String addQuestionContainerTemplate) {
		this.addQuestionContainerTemplate = addQuestionContainerTemplate;
	}

	public String getSaveQuestionInListTemplate() {
		return saveQuestionInListTemplate;
	}

	public void setSaveQuestionInListTemplate(String saveQuestionInListTemplate) {
		this.saveQuestionInListTemplate = saveQuestionInListTemplate;
	}

	public String getMultipleChoiceMultipleSelectTemplate() {
		return multipleChoiceMultipleSelectTemplate;
	}

	public void setMultipleChoiceMultipleSelectTemplate(
			String multipleChoiceMultipleSelectTemplate) {
		this.multipleChoiceMultipleSelectTemplate = multipleChoiceMultipleSelectTemplate;
	}

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public String getCloseQuestionTemplate() {
		return closeQuestionTemplate;
	}

	public void setCloseQuestionTemplate(String closeQuestionTemplate) {
		this.closeQuestionTemplate = closeQuestionTemplate;
	}


	private void readSurveyAnswerChoices( SurveyQuestion question ) {
		String str;
		int i = 0;
		BufferedReader reader = new BufferedReader(new StringReader(question.getSurveyAnswerLines()));
		try {
			List<SurveyAnswerItem> answers = new ArrayList<SurveyAnswerItem>();
			while ((str = reader.readLine()) != null) {
				if (str.length() > 0){
					if(!StringUtils.isBlank(str)) {
						SurveyAnswerItem answer = new SurveyAnswerItem();
						answer.setSurveyQuestion(question);
						answer.setLabel(str);
						answer.setDisplayOrder(i++);
						answers.add(answer);
					}
				}
			}
			question.setSurveyAnswers(answers);
		} catch(IOException e) {
			e.printStackTrace();
		}		
	}

	private void readCustomResponceChoices( SurveyQuestion question, String choices ) {
		String str;
		int i = 0;
		BufferedReader reader = new BufferedReader(new StringReader(choices));
		try {
			List<SurveyAnswerItem> answers = new ArrayList<SurveyAnswerItem>();
			while ((str = reader.readLine()) != null) {
				if (str.length() > 0){
					if(!StringUtils.isBlank(str)) {
						SurveyAnswerItem answer = new SurveyAnswerItem();
						answer.setSurveyQuestion(question);
						answer.setLabel(str);
						answer.setDisplayOrder(i++);
						answers.add(answer);
					}
				}
			}
			question.setSurveyAnswers(answers);
		} catch(IOException e) {
			e.printStackTrace();
		}		
	}

	/**
	 * Added by ::  Dyutiman
	 * return choices of custom answer items for multiple select / single select type of answers.
	 * @return list of answer item options
	 */
	/*private List<CustomSurveyAnswerItemOption> readCustomResponceChoices( String choices, SurveyAnswerItem ansItem ) {
		String str;
		int i = 0;
		BufferedReader reader = new BufferedReader(new StringReader(choices));
		try {
			List<CustomSurveyAnswerItemOption> options = new ArrayList<CustomSurveyAnswerItemOption>();
			while( (str = reader.readLine()) != null ) {
				if( str.length() > 0 ) {
					if( !StringUtils.isBlank(str) ) {
						CustomSurveyAnswerItemOption option = new CustomSurveyAnswerItemOption();
						option.setLabel(str);
						option.setResponse(ansItem);
						option.setDisplayOrder(i++);
						options.add(option);
					}
				}
			}
			return options;
		} catch( IOException e ) {
			e.printStackTrace();
		}
		return null;
	}*/

	
	
	public ModelAndView copySurveyToCustomers( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors )
	throws Exception {

		//Clone it
		String surveyId = "";
		surveyId = request.getParameter("sid");
		Distributor distributor = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentDistributor();
		if(StringUtils.isNotBlank(surveyId)){
			
			Survey oldSurvey = surveyService.getSurveyByID(Long.parseLong(surveyId));
			
			List<Customer> customersList = customerService.findCustomersByDistributor(distributor);
			
			 for(Customer customer : customersList){
			
				Survey newSurvey = oldSurvey.getClone();
				customer.initializeOwnerParams();
				newSurvey.setOwner(customer);
				
				newSurvey.setId(null);//To create new row for survey else it will update the existing record
				newSurvey = surveyService.addSurvey(newSurvey);
			
				SurveyQuestion newQuestion = null;
				for(SurveyQuestion question : oldSurvey.getQuestions()){
					
					if(question instanceof SingleSelectSurveyQuestion){
						newQuestion = ((SingleSelectSurveyQuestion)question).getClone();
						if(newQuestion!=null ){
							
							newQuestion.setSurvey(newSurvey);
							SurveyAnswerItem newItem = null;
							for(SurveyAnswerItem item : question.getSurveyAnswers()){
								newItem=item.getClone();
								if(newItem!=null){
									newItem.setSurveyQuestion(newQuestion);
									newQuestion.getSurveyAnswers().add(newItem);
								}
								
							}
							
							//surveyService.addSurveyQuestion(newQuestion);
							SurveyQuestion newlyAddedQuestion =surveyService.getSurveyQuestionById(surveyService.addSurveyQuestion(newQuestion).getId()); 
							newSurvey.getQuestions().add(newlyAddedQuestion);
							
							
							
						}
					}
					if(question instanceof MultipleSelectSurveyQuestion){
						newQuestion = ((MultipleSelectSurveyQuestion)question).getClone();
						if(newQuestion!=null ){
							
							newQuestion.setSurvey(newSurvey);
							SurveyAnswerItem newItem = null;
							for(SurveyAnswerItem item : question.getSurveyAnswers()){
								newItem=item.getClone();
								if(newItem!=null){
									newItem.setSurveyQuestion(newQuestion);
									newQuestion.getSurveyAnswers().add(newItem);
								}
								
							}
							
							//surveyService.addSurveyQuestion(newQuestion);
							SurveyQuestion newlyAddedQuestion =surveyService.getSurveyQuestionById(surveyService.addSurveyQuestion(newQuestion).getId()); 
							newSurvey.getQuestions().add(newlyAddedQuestion);
							
							
							
							
							
						}
					}
					if(question instanceof PersonalInformationSurveyQuestion){
						newQuestion = ((PersonalInformationSurveyQuestion)question).getClone();
						if(newQuestion!=null ){
							
							newQuestion.setSurvey(newSurvey);
							SurveyAnswerItem newItem = null;
							for(SurveyAnswerItem item : question.getSurveyAnswers()){
								newItem=item.getClone();
								if(newItem!=null){
									newItem.setSurveyQuestion(newQuestion);
									newQuestion.getSurveyAnswers().add(newItem);
								}
								
							}
							
							//surveyService.addSurveyQuestion(newQuestion);
							SurveyQuestion newlyAddedQuestion =surveyService.getSurveyQuestionById(surveyService.addSurveyQuestion(newQuestion).getId()); 
							newSurvey.getQuestions().add(newlyAddedQuestion);
							
							
							
							
							
							
						}
					}
					if(question instanceof TextBoxSurveyQuestion){
						newQuestion = ((TextBoxSurveyQuestion)question).getClone();
						if(newQuestion!=null ){
							
							newQuestion.setSurvey(newSurvey);
							SurveyAnswerItem newItem = null;
							for(SurveyAnswerItem item : question.getSurveyAnswers()){
								newItem=item.getClone();
								if(newItem!=null){
									newItem.setSurveyQuestion(newQuestion);
									newQuestion.getSurveyAnswers().add(newItem);
								}
								
							}
							
							//surveyService.addSurveyQuestion(newQuestion);
							SurveyQuestion newlyAddedQuestion =surveyService.getSurveyQuestionById(surveyService.addSurveyQuestion(newQuestion).getId()); 
							newSurvey.getQuestions().add(newlyAddedQuestion);
							
							
							
							
							
						}
					}
					if(question instanceof AggregateSurveyQuestion){
						//newQuestion = ((AggregateSurveyQuestion)question).getClone();
						
						
						//clone agg items
						List<AggregateSurveyQuestionItem> oldAggItems = surveyService.getAggregateSurveyQuestionItemsByQuestionId(question.getId());
						List<AggregateSurveyQuestionItem> aggItemsNew = new ArrayList<AggregateSurveyQuestionItem>();
						AggregateSurveyQuestionItem newAggItem = null;
						
						newQuestion = ((AggregateSurveyQuestion)question).getClone();
						
						newQuestion.setSurvey(newSurvey);
						newQuestion = surveyService.getSurveyQuestionById(surveyService.addSurveyQuestion(newQuestion).getId());
						
						
						
						
						
						
						for(AggregateSurveyQuestionItem oldAggItem : oldAggItems){
							
							newAggItem = new AggregateSurveyQuestionItem();
							newAggItem.setDisplayOrder(oldAggItem.getDisplayOrder());
							
							
							
							
							
							
							SurveyQuestion newAggQuestion = null;
							
							if(oldAggItem.getQuestion() instanceof SingleSelectSurveyQuestion){
								newAggQuestion = ((SingleSelectSurveyQuestion)oldAggItem.getQuestion()).getClone();
								newAggQuestion.setSurvey(newSurvey);
								SurveyAnswerItem newAnswerItem = null;
								for(SurveyAnswerItem answerItem : oldAggItem.getQuestion().getSurveyAnswers()){
									newAnswerItem=answerItem.getClone();
									if(newAnswerItem!=null){
										newAnswerItem.setSurveyQuestion(newAggQuestion);
										newAggQuestion.getSurveyAnswers().add(newAnswerItem);
									}
									
								}
								newAggItem.setQuestion(surveyService.getSurveyQuestionById(surveyService.addSurveyQuestion(newAggQuestion).getId()));
								
								
								
								
							}
							if(oldAggItem.getQuestion() instanceof MultipleSelectSurveyQuestion){
								newAggQuestion = ((MultipleSelectSurveyQuestion)oldAggItem.getQuestion()).getClone();
								newAggQuestion.setSurvey(newSurvey);
								SurveyAnswerItem newAnswerItem = null;
								for(SurveyAnswerItem answerItem : oldAggItem.getQuestion().getSurveyAnswers()){
									newAnswerItem=answerItem.getClone();
									if(newAnswerItem!=null){
										newAnswerItem.setSurveyQuestion(newAggQuestion);
										newAggQuestion.getSurveyAnswers().add(newAnswerItem);
									}
									
								}
								newAggItem.setQuestion(surveyService.getSurveyQuestionById(surveyService.addSurveyQuestion(newAggQuestion).getId()));
								
								
								
							}
							if(oldAggItem.getQuestion() instanceof TextBoxSurveyQuestion){
								newAggQuestion = ((TextBoxSurveyQuestion)oldAggItem.getQuestion()).getClone();
								newAggQuestion.setSurvey(newSurvey);
								
								
								SurveyAnswerItem newAnswerItem = null;
								for(SurveyAnswerItem answerItem : oldAggItem.getQuestion().getSurveyAnswers()){
									newAnswerItem=answerItem.getClone();
									if(newAnswerItem!=null){
										newAnswerItem.setSurveyQuestion(newAggQuestion);
										newAggQuestion.getSurveyAnswers().add(newAnswerItem);
									}
									
								}
								newAggItem.setQuestion(surveyService.getSurveyQuestionById(surveyService.addSurveyQuestion(newAggQuestion).getId()));
								
								
								
							}
							
							
							
							newAggItem.setAggregateSurveyQuestion((AggregateSurveyQuestion)newQuestion);
							
							aggItemsNew.add(newAggItem);
							//surveyService.saveAggregateSurveyQuestionItem(aggItemsNew);
							
						}
						
						surveyService.saveAggregateSurveyQuestionItem(aggItemsNew);
					}
					
					
				}
			
			
			}
			
		}
		
		
		return displaySurveys(request, response);
	}
	
	
	
	/**
	 * @return the closeQuestionContainerTemplate
	 */
	public String getCloseQuestionContainerTemplate() {
		return closeQuestionContainerTemplate;
	}

	/**
	 * @param closeQuestionContainerTemplate the closeQuestionContainerTemplate to set
	 */
	public void setCloseQuestionContainerTemplate(
			String closeQuestionContainerTemplate) {
		this.closeQuestionContainerTemplate = closeQuestionContainerTemplate;
	}

	/**
	 * @return the redirectTemplate
	 */
	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	/**
	 * @param redirectTemplate the redirectTemplate to set
	 */
	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}

	/**
	 * @return the editSurveyInfoTemplate
	 */
	public String getEditSurveyInfoTemplate() {
		return editSurveyInfoTemplate;
	}

	/**
	 * @param editSurveyInfoTemplate the editSurveyInfoTemplate to set
	 */
	public void setEditSurveyInfoTemplate(String editSurveyInfoTemplate) {
		this.editSurveyInfoTemplate = editSurveyInfoTemplate;
	}

	/**
	 * @return the editQuestionContainerTemplate
	 */
	public String getEditQuestionContainerTemplate() {
		return editQuestionContainerTemplate;
	}

	/**
	 * @param editQuestionContainerTemplate the editQuestionContainerTemplate to set
	 */
	public void setEditQuestionContainerTemplate(
			String editQuestionContainerTemplate) {
		this.editQuestionContainerTemplate = editQuestionContainerTemplate;
	}

	/**
	 * @return the surveyAnalyzeTemplate
	 */
	public String getSurveyAnalyzeTemplate() {
		return surveyAnalyzeTemplate;
	}

	/**
	 * @param surveyAnalyzeTemplate the surveyAnalyzeTemplate to set
	 */
	public void setSurveyAnalyzeTemplate(String surveyAnalyzeTemplate) {
		this.surveyAnalyzeTemplate = surveyAnalyzeTemplate;
	}

	/**
	 * @return the learnerSurveyResponseTemplate
	 */
	public String getLearnerSurveyResponseTemplate() {
		return learnerSurveyResponseTemplate;
	}

	/**
	 * @param learnerSurveyResponseTemplate the learnerSurveyResponseTemplate to set
	 */
	public void setLearnerSurveyResponseTemplate(String learnerSurveyResponseTemplate) {
		this.learnerSurveyResponseTemplate = learnerSurveyResponseTemplate;
	}

	/**
	 * @return the surveyResponseTemplate
	 */
	public String getSurveyResponseTemplate() {
		return surveyResponseTemplate;
	}

	/**
	 * @param surveyResponseTemplate the surveyResponseTemplate to set
	 */
	public void setSurveyResponseTemplate(String surveyResponseTemplate) {
		this.surveyResponseTemplate = surveyResponseTemplate;
	}

	/**
	 * @return the learnerService
	 */
	public LearnerService getLearnerService() {
		return learnerService;
	}

	/**
	 * @param learnerService the learnerService to set
	 */
	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	/**
	 * @return the failureTemplate
	 */
	public String getFailureTemplate() {
		return failureTemplate;
	}

	/**
	 * @param failureTemplate the failureTemplate to set
	 */
	public void setFailureTemplate(String failureTemplate) {
		this.failureTemplate = failureTemplate;
	}

	/**
	 * @return the vu360UserService
	 */
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	/**
	 * @param vu360UserService the vu360UserService to set
	 */
	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}
	public String getEditCourseTemplate() {
		return editCourseTemplate;
	}

	public void setEditCourseTemplate(String editCourseTemplate) {
		this.editCourseTemplate = editCourseTemplate;
	}

	/**
	 * @return the addNewFlagTemplate
	 */
	public String getAddNewFlagTemplate() {
		return addNewFlagTemplate;
	}

	/**
	 * @param addNewFlagTemplate the addNewFlagTemplate to set
	 */
	public void setAddNewFlagTemplate(String addNewFlagTemplate) {
		this.addNewFlagTemplate = addNewFlagTemplate;
	}

	/**
	 * @return the displayManageFlagsTemplate
	 */
	public String getDisplayManageFlagsTemplate() {
		return displayManageFlagsTemplate;
	}

	/**
	 * @param displayManageFlagsTemplate the displayManageFlagsTemplate to set
	 */
	public void setDisplayManageFlagsTemplate(String displayManageFlagsTemplate) {
		this.displayManageFlagsTemplate = displayManageFlagsTemplate;
	}

	/**
	 * @return the editFlagTemplate
	 */
	public String getEditFlagTemplate() {
		return editFlagTemplate;
	}

	/**
	 * @param editFlagTemplate the editFlagTemplate to set
	 */
	public void setEditFlagTemplate(String editFlagTemplate) {
		this.editFlagTemplate = editFlagTemplate;
	}

	public String getTrainingPlanAnalysisTemplate() {
		return trainingPlanAnalysisTemplate;
	}

	public void setTrainingPlanAnalysisTemplate(String trainingPlanAnalysisTemplate) {
		this.trainingPlanAnalysisTemplate = trainingPlanAnalysisTemplate;
	}

	public String getTrainingPlanAnalysisDetailsTemplate() {
		return trainingPlanAnalysisDetailsTemplate;
	}

	public void setTrainingPlanAnalysisDetailsTemplate(
			String trainingPlanAnalysisDetailsTemplate) {
		this.trainingPlanAnalysisDetailsTemplate = trainingPlanAnalysisDetailsTemplate;
	}

	/**
	 * @return the addNewSurveysTemplate
	 */
	public String getAddNewSurveysTemplate() {
		return addNewSurveysTemplate;
	}

	/**
	 * @param addNewSurveysTemplate the addNewSurveysTemplate to set
	 */
	public void setAddNewSurveysTemplate(String addNewSurveysTemplate) {
		this.addNewSurveysTemplate = addNewSurveysTemplate;
	}

	/**
	 * @return the surveyAnalizeIndividualTemplate
	 */
	public String getSurveyAnalizeIndividualTemplate() {
		return surveyAnalizeIndividualTemplate;
	}

	/**
	 * @param surveyAnalizeIndividualTemplate the surveyAnalizeIndividualTemplate to set
	 */
	public void setSurveyAnalizeIndividualTemplate(
			String surveyAnalizeIndividualTemplate) {
		this.surveyAnalizeIndividualTemplate = surveyAnalizeIndividualTemplate;
	}

	/**
	 * @return the surveyAnalizeResponseTemplate
	 */
	public String getSurveyAnalizeResponseTemplate() {
		return surveyAnalizeResponseTemplate;
	}

	/**
	 * @param surveyAnalizeResponseTemplate the surveyAnalizeResponseTemplate to set
	 */
	public void setSurveyAnalizeResponseTemplate(
			String surveyAnalizeResponseTemplate) {
		this.surveyAnalizeResponseTemplate = surveyAnalizeResponseTemplate;
	}
	
	public String getSurveyAnalyzeSectionTemplate() {
		return surveyAnalyzeSectionTemplate;
	}

	public void setSurveyAnalyzeSectionTemplate(String surveyAnalyzeSectionTemplate) {
		this.surveyAnalyzeSectionTemplate = surveyAnalyzeSectionTemplate;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	public ModelAndView exportSurvey(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Long surveyId=ServletRequestUtils.getLongParameter(request, "id", 0);
		log.debug( "Results will be exported for Survey id " + surveyId );

        String path = VU360Properties.getVU360Property("lms.predict.ws.endpoint.host") + "/predict360/ws/restapi/surveyservice/import";
        String fileUploadServicePath = VU360Properties.getVU360Property("lms.predict.ws.endpoint.host") + "/predict360/ws/restapi/fileservice/upload";
        int timeout = 30*60*1000;
        final SimpleDateFormat jsonDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        ExportToPredict exportVO = new ExportToPredict();

        
        List<PredictSurveyQuestionMapping> quesMaps = surveyService.getPredictSurveyQuestionMappingBySurveyId(surveyId);
        Map <String,PredictSurveyQuestionMapping> aMap=null;
        if(quesMaps==null || quesMaps.size()==0){
    		return this.searchSurvey(request, response, null, null);
        }

        exportVO.setSurveyId(quesMaps.get(0).getSurveyId());
        exportVO.setPredictSurveyQuestionMappings(quesMaps);
        Survey survey=surveyService.getSurveyByID(surveyId);
        exportVO.setSurveyStatus(survey.getStatus());

        aMap=new HashMap<String, PredictSurveyQuestionMapping>(quesMaps.size());
        for (PredictSurveyQuestionMapping predictSurveyQuestionMapping : quesMaps) {
			aMap.put(predictSurveyQuestionMapping.getLmsSectionId()+"-"+predictSurveyQuestionMapping.getLmsFrameworkId()+"-"+predictSurveyQuestionMapping.getLmsQuestionId(), predictSurveyQuestionMapping);
		}

        List<SurveyResult> surveyResList=surveyService.getSurveyResultsBySurveyId(surveyId);
        List<PredictSurveyResult> pSurveyResults = new ArrayList<PredictSurveyResult>();
		if ( surveyResList!=null && surveyResList.size() > 0 ) {
			logger.debug( surveyResList.size() + " Survey Results were exported to Predict360 ");
			for (SurveyResult sr : surveyResList) {
				PredictSurveyResult psr = new PredictSurveyResult();
				psr.setResponseDate(jsonDateFormat.format(sr.getResponseDate()));
				psr.setSurveyee(sr.getSurveyee().getUsername());
				List<PredictSurveyResultAnswer> pAnswers = new ArrayList<PredictSurveyResultAnswer>();
				for(SurveyResultAnswer answer : sr.getAnswers()){
					PredictSurveyResultAnswer pAnswer = new PredictSurveyResultAnswer();
					pAnswer.setQuesMap(aMap.get(answer.getSurveySection().getId()+"-"+answer.getSurveyQuestionBank().getId()+"-"+answer.getQuestion().getId()));
					pAnswer.setSurveyAnswerText(answer.getSurveyAnswerText());
					List<PredictSurveyAnswerItem> pSelectedAnswers =new ArrayList<PredictSurveyAnswerItem>();
					for(SurveyAnswerItem item : answer.getSurveyAnswerItems()){
						PredictSurveyAnswerItem pSelectedAnswer = new PredictSurveyAnswerItem();
						pSelectedAnswer.setLabel(item.getLabel());
						pSelectedAnswer.setValue(item.getValue());
						pSelectedAnswers.add(pSelectedAnswer);
					}
					pAnswer.setSurveyAnswerItems(pSelectedAnswers);
					
					for(SurveyResultAnswerFile answerFile : answer.getSurveyResultAnswerFiles()) {
						log.debug("uploadloading file : " + answerFile.getFilePath());
						File file = new File(answerFile.getFilePath());
						JSONObject fileResponse = new RestClient().postFile(fileUploadServicePath, file);
						String fileUploadedPath = "";
						int status = fileResponse.getInt("status");
						int errorCode = fileResponse.getInt("errorCode");
						if(status==200 && errorCode==0) {
							fileUploadedPath = fileResponse.getString("message");
						}
						log.debug(fileResponse);
						
						PredictSurveyResultAnswerFile answerFileVO = new PredictSurveyResultAnswerFile();
						answerFileVO.setFileName(answerFile.getFileName());
						answerFileVO.setFile(fileUploadedPath);
						
						pAnswer.getAnswerFiles().add(answerFileVO);
					}
					
					pAnswers.add(pAnswer);
				}
				psr.setAnswers(pAnswers);
				pSurveyResults.add(psr);
			}
			
			exportVO.setSurveyResults(pSurveyResults);
			logger.debug(exportVO);
			String callResponse = new RestClient().postForObject(exportVO, path, timeout);
			logger.debug(callResponse);
			JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(callResponse);
			boolean exportStatus = false;
			if(jsonObject.get("message")!=null && (jsonObject.get("message").toString().compareToIgnoreCase("OK")==0)) {
				exportStatus = true;
			}
			request.setAttribute("exportStatus", exportStatus);
			return searchSurvey(request, response, command, errors);
		} else {
			logger.debug( "No Survey Results were found for export!" );
		}
		return this.searchSurvey(request, response, null, null);
	}
	
	protected void pushFileForDownload(HttpServletRequest request, HttpServletResponse response, String fileName) throws IOException {
		File file = new File( fileName );
		FileInputStream is = new FileInputStream(file);
		byte[] buf = new byte[512];
		response.setContentType( "text/csv" );
		response.setContentLength( Integer.valueOf(file.length()+""));
		response.setHeader( "Content-disposition" , "attachment;filename=SurveyResult.csv");
		while(is.read(buf)!=-1) {
			response.getOutputStream().write(buf);
		}
		is.close();
	}

	public String getAnalyzeSurveySectionTemplate() {
		return analyzeSurveySectionTemplate;
	}

	public void setAnalyzeSurveySectionTemplate(String analyzeSurveySectionTemplate) {
		this.analyzeSurveySectionTemplate = analyzeSurveySectionTemplate;
	}

	/**
	 * @return the distributorService
	 */
	public DistributorService getDistributorService() {
		return distributorService;
	}

	/**
	 * @param distributorService the distributorService to set
	 */
	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}
	
	
}
