package com.softech.vu360.lms.web.controller.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.MultipleSelectSurveyQuestion;
import com.softech.vu360.lms.model.SingleSelectSurveyQuestion;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyAnswerItem;
import com.softech.vu360.lms.model.SurveyQuestion;
import com.softech.vu360.lms.model.TextBoxSurveyQuestion;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.ManageSurveyForm;
import com.softech.vu360.lms.web.controller.model.SurveyCourse;
import com.softech.vu360.lms.web.controller.validator.ManageSurveyValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

/**
 * @author Saptarshi
 *
 */
public class ManageSurveyWizardController extends AbstractWizardFormController {
	
	private static final Logger log = Logger.getLogger(ManageSurveyWizardController.class.getName());
	private String closeTemplate = null;
	private String cancelTemplate = null;
	private EntitlementService entitlementService = null;
	private SurveyService surveyService = null;
	
	public static final String SURVEY_QUESTION_MULTIPLE_CHOICE_MULTIPLE_SELECT = "survey.question.multiplechoice.multipleselect";
	public static final String SURVEY_QUESTION_MULTIPLE_CHOICE_SINGLE_SELECT = "survey.question.multiplechoice.singleselect";
	public static final String SURVEY_QUESTION_DROP_DOWN_SINGLE_SELECT = "survey.question.dropdown.singleselect";
	public static final String SURVEY_QUESTION_TEXT_BOX_256_CHARACTERS = "survey.question.textbox.256characters";
	public static final String SURVEY_QUESTION_TEXT_BOX_UNLIMITED = "survey.question.textbox.unlimited";
	public static final String SURVEY_QUESTION_MULTIPLE_CHOICE_RATING_SELECT = "survey.question.multiplechoice.ratingselect";
	private static final String[] SURVEY_QUESTION_TYPES = {
		SURVEY_QUESTION_MULTIPLE_CHOICE_MULTIPLE_SELECT
		, SURVEY_QUESTION_MULTIPLE_CHOICE_SINGLE_SELECT
		, SURVEY_QUESTION_DROP_DOWN_SINGLE_SELECT
		, SURVEY_QUESTION_TEXT_BOX_256_CHARACTERS
		, SURVEY_QUESTION_TEXT_BOX_UNLIMITED
	};


	public ManageSurveyWizardController() {
		super();
		setCommandName("manageSurveyForm");
		setCommandClass(ManageSurveyForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"manager/userGroups/survey/add-New-Survey-Information"
				, "manager/userGroups/survey/list-New-Survey-Questions"
				, "manager/userGroups/survey/add-New-Survey-Question_Container"
				, "manager/userGroups/survey/closeSurveyPopup"
				, "manager/userGroups/survey/add-New-Survey-Courses"
				,"manager/userGroups/survey/add-New-Survey-withoutCourses"
		});
	}


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#isFormSubmission(javax.servlet.http.HttpServletRequest)
	 */
	protected boolean isFormSubmission(HttpServletRequest request) {
		try {
			int currentpage = this.getCurrentPage(request);
			if (currentpage == 1 && request.getParameter("action") != null
					&& request.getParameter("action").equals("addquestion"))
				return true;
		} catch (Exception e) {
			log.debug("First request");
		}		
		return super.isFormSubmission(request);
	}

	protected int getTargetPage(HttpServletRequest request, 
			Object command,	Errors errors, int currentPage) {
		ManageSurveyForm manageSurveyForm = (ManageSurveyForm)command;
		// Auto-generated method stub
		if(currentPage==2){
			if(request.getParameter("action")!=null){
				if(request.getParameter("action").equals("cancelquestion")){
					return 3;
				}else if(request.getParameter("action").equals("savequestion") && !errors.hasErrors()){
					return 3;
				}
			}
		}else if(currentPage==1){
			if(request.getParameter("action")!=null){
				if(request.getParameter("action").equals("deletequestion")
						||request.getParameter("action").equals("paging")){
					return 1;
				}else if( !request.getParameter("action").equals("previous") &&  manageSurveyForm.getSurveyEvent().equalsIgnoreCase(Survey.SURVEY_EVENTS[3] ) && ! request.getParameter("action").equals("refreshquestion") && ! request.getParameter("action").equals("addquestion") ){
 
					return 5;	 
				}
			} 
		} 
			
		 
		
		return super.getTargetPage(request, command, errors, currentPage);
	}


	protected void onBindAndValidate(HttpServletRequest request, 
			Object command, BindException errors, int currentPage) throws Exception {
		
		ManageSurveyValidator validator = (ManageSurveyValidator)this.getValidator();
		ManageSurveyForm form = (ManageSurveyForm)command;
		if(request.getParameter("action")!=null) {
			ManageSurveyForm manageSurveyForm = (ManageSurveyForm)command;
			if( currentPage == 1 ) {
				if( request.getParameter("action").equals("addquestion")){
					manageSurveyForm.setSurveyQuestionType(SURVEY_QUESTION_MULTIPLE_CHOICE_MULTIPLE_SELECT);
					MultipleSelectSurveyQuestion question = new MultipleSelectSurveyQuestion();
					manageSurveyForm.setCurrentMultipleSelectSurveyQuestion(question);
				}else if(request.getParameter("action").equals("deletequestion")
						||request.getParameter("action").equals("paging")){
					//nothing
				}else if(request.getParameter("action").equals("refreshquestion")){
					//to prevent the error message from appearing in the question list page when we return from the 
					//question container popup without any question added...
				}else{
					//this is where the next was invoked...so do the validation call here
					//we cannot validate the question list page in the validatePage function..so here
					validator.validateSecondPage(form, errors);
				}
			}
			if( currentPage == 2 ) {
				String questionType = manageSurveyForm.getSurveyQuestionType();
				if(request.getParameter("action").equals("changequestiontype")){
					if(questionType.equalsIgnoreCase(SURVEY_QUESTION_MULTIPLE_CHOICE_MULTIPLE_SELECT)){
						MultipleSelectSurveyQuestion question = new MultipleSelectSurveyQuestion();
						manageSurveyForm.setCurrentMultipleSelectSurveyQuestion(question);
						manageSurveyForm.getCurrentMultipleSelectSurveyQuestion().setRequired(Boolean.FALSE);
					}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_MULTIPLE_CHOICE_SINGLE_SELECT)){
						SingleSelectSurveyQuestion question = new SingleSelectSurveyQuestion(Boolean.FALSE);
						manageSurveyForm.setCurrentSingleSelectSurveyQuestion(question);
						manageSurveyForm.getCurrentSingleSelectSurveyQuestion().setRequired(Boolean.FALSE);
					}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_DROP_DOWN_SINGLE_SELECT)){
						SingleSelectSurveyQuestion question = new SingleSelectSurveyQuestion(Boolean.TRUE);
						manageSurveyForm.setCurrentSingleSelectSurveyQuestion(question);
						manageSurveyForm.getCurrentSingleSelectSurveyQuestion().setRequired(Boolean.FALSE);
					}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_TEXT_BOX_256_CHARACTERS)){
						TextBoxSurveyQuestion question = new TextBoxSurveyQuestion(Boolean.FALSE);
						manageSurveyForm.setCurrentFillInTheBlankSurveyQuestion(question);
						manageSurveyForm.getCurrentFillInTheBlankSurveyQuestion().setRequired(Boolean.FALSE);
					}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_TEXT_BOX_UNLIMITED)){
						TextBoxSurveyQuestion question = new TextBoxSurveyQuestion(Boolean.TRUE);
						manageSurveyForm.setCurrentFillInTheBlankSurveyQuestion(question);
						manageSurveyForm.getCurrentFillInTheBlankSurveyQuestion().setRequired(Boolean.FALSE);
					}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_MULTIPLE_CHOICE_RATING_SELECT)){
					}
				}else if(request.getParameter("action").equals("savequestion")){

					if(questionType.equalsIgnoreCase(SURVEY_QUESTION_MULTIPLE_CHOICE_MULTIPLE_SELECT)){
						MultipleSelectSurveyQuestion question = manageSurveyForm.getCurrentMultipleSelectSurveyQuestion();
						question.setText(manageSurveyForm.getSurveyQuestionText());
						question.setDisplayOrder(manageSurveyForm.getDisplayQuestionOrder());
						question.setRequired(manageSurveyForm.isSurveyQuestionRequired());
						manageSurveyForm.setDisplayQuestionOrder(manageSurveyForm.getDisplayQuestionOrder()+1);
						readSurveyAnswerChoices(question);
					}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_MULTIPLE_CHOICE_SINGLE_SELECT)){
						SingleSelectSurveyQuestion question = manageSurveyForm.getCurrentSingleSelectSurveyQuestion();
						question.setText(manageSurveyForm.getSurveyQuestionText());
						question.setDisplayOrder(manageSurveyForm.getDisplayQuestionOrder());
						question.setRequired(manageSurveyForm.isSurveyQuestionRequired());
						manageSurveyForm.setDisplayQuestionOrder(manageSurveyForm.getDisplayQuestionOrder()+1);
						readSurveyAnswerChoices(question);
					}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_DROP_DOWN_SINGLE_SELECT)){
						SingleSelectSurveyQuestion question = manageSurveyForm.getCurrentSingleSelectSurveyQuestion();
						question.setText(manageSurveyForm.getSurveyQuestionText());
						question.setDisplayOrder(manageSurveyForm.getDisplayQuestionOrder());
						question.setRequired(manageSurveyForm.isSurveyQuestionRequired());
						manageSurveyForm.setDisplayQuestionOrder(manageSurveyForm.getDisplayQuestionOrder()+1);
						readSurveyAnswerChoices(question);
					}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_TEXT_BOX_256_CHARACTERS)){
						TextBoxSurveyQuestion question = manageSurveyForm.getCurrentFillInTheBlankSurveyQuestion();
						question.setText(manageSurveyForm.getSurveyQuestionText());
						question.setDisplayOrder(manageSurveyForm.getDisplayQuestionOrder());
						question.setRequired(manageSurveyForm.isSurveyQuestionRequired());
						manageSurveyForm.setDisplayQuestionOrder(manageSurveyForm.getDisplayQuestionOrder()+1);
					}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_TEXT_BOX_UNLIMITED)){
						TextBoxSurveyQuestion question = manageSurveyForm.getCurrentFillInTheBlankSurveyQuestion();
						question.setText(manageSurveyForm.getSurveyQuestionText());
						question.setDisplayOrder(manageSurveyForm.getDisplayQuestionOrder());
						question.setRequired(manageSurveyForm.isSurveyQuestionRequired());
						manageSurveyForm.setDisplayQuestionOrder(manageSurveyForm.getDisplayQuestionOrder()+1);
					}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_MULTIPLE_CHOICE_RATING_SELECT)){
					}
					validator.validateQuestion(form, errors);
				}
			}
			// else{
			// //we cannot validate the question list page in the validatePage function..so here
			// if(currentPage==1){
			//	validator.validateSecondPage(form, errors);
			// }
			// }
		}
		super.onBindAndValidate(request, command, errors, currentPage);
	}


	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception {
		return new ModelAndView(cancelTemplate);
	}

	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception {
		
		// Auto-generated method stub
		Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentCustomer();
		Distributor distributor = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentDistributor();
		Survey newSurvey=new Survey();

		ManageSurveyForm manageSurveyForm = (ManageSurveyForm)command;
		newSurvey.setName(manageSurveyForm.getSurveyName());
		newSurvey.setEvent(manageSurveyForm.getSurveyEvent());
		if(manageSurveyForm.isPublished()){
			newSurvey.setStatus(Survey.PUBLISHED);
		}else{
			newSurvey.setStatus(Survey.NOTPUBLISHED);
		}
		if(!manageSurveyForm.isAllQuestionPerPage()){
			newSurvey.setIsShowAll(false);
			newSurvey.setQuestionsPerPage(Integer.parseInt(manageSurveyForm.getQuestionsPerPage()));
		}
		List<Course> selectedCourses = new ArrayList<Course>();
		for(SurveyCourse surveyCourse:manageSurveyForm.getSurveyCourses()){
			if(surveyCourse.isSelected()){
				selectedCourses.add(surveyCourse.getCourse());
			}
		}
		newSurvey.setCourses(selectedCourses);
		if(distributor==null){
			customer.initializeOwnerParams();
			newSurvey.setOwner(customer);
		}else{
			newSurvey.setOwner(distributor);
		}

		for (int counter=0;counter<manageSurveyForm.getSurveyQuestions().size();counter++)
			manageSurveyForm.getSurveyQuestions().get(counter).setSurvey(newSurvey);
		newSurvey.setQuestions(manageSurveyForm.getSurveyQuestions());

		surveyService.addSurvey(newSurvey);

		return new ModelAndView(cancelTemplate);
	}


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#postProcessPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors,
			int page) throws Exception{
		
		// Auto-generated method stub
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
		
		Map<String, Object> PagerAttributeMap = new HashMap <String, Object>();
		PagerAttributeMap.put("pageIndex",pageIndex);
		session.setAttribute("showAll", showAll);
		
		if( page == 2 && !errors.hasErrors() ) {
			if( request.getParameter("action") != null ) {
				
				ManageSurveyForm manageSurveyForm = (ManageSurveyForm)command;
				
				if( request.getParameter("action").equals("savequestion") ) {
					String questionType = manageSurveyForm.getSurveyQuestionType();
					if(questionType.equalsIgnoreCase(SURVEY_QUESTION_MULTIPLE_CHOICE_MULTIPLE_SELECT)){
						MultipleSelectSurveyQuestion question = manageSurveyForm.getCurrentMultipleSelectSurveyQuestion();
						manageSurveyForm.getSurveyQuestions().add(question);
					}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_MULTIPLE_CHOICE_SINGLE_SELECT)){
						SingleSelectSurveyQuestion question = manageSurveyForm.getCurrentSingleSelectSurveyQuestion();
						manageSurveyForm.getSurveyQuestions().add(question);
					}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_DROP_DOWN_SINGLE_SELECT)){
						SingleSelectSurveyQuestion question = manageSurveyForm.getCurrentSingleSelectSurveyQuestion();
						manageSurveyForm.getSurveyQuestions().add(question);
					}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_TEXT_BOX_256_CHARACTERS)){
						TextBoxSurveyQuestion question = manageSurveyForm.getCurrentFillInTheBlankSurveyQuestion();
						manageSurveyForm.getSurveyQuestions().add(question);
					}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_TEXT_BOX_UNLIMITED)){
						TextBoxSurveyQuestion question = manageSurveyForm.getCurrentFillInTheBlankSurveyQuestion();
						manageSurveyForm.getSurveyQuestions().add(question);
					}else if(questionType.equalsIgnoreCase(SURVEY_QUESTION_MULTIPLE_CHOICE_RATING_SELECT)){

					}
					manageSurveyForm.setSurveyQuestionText("");
				}
				//increment the deletable list by one to the end of the said list
				manageSurveyForm.getDeleteableQuestions().add(new Boolean(false));
			}
		} else if( page == 1 ) {
			if( request.getParameter("action") != null ) {
				if( request.getParameter("action").equals("deletequestion") ) {
					
					ManageSurveyForm form = (ManageSurveyForm)command;

					if(form.getDeleteableQuestions()!=null && form.getDeleteableQuestions().size()>0){
						List<SurveyQuestion> questionList = form.getSurveyQuestions();
						for(int i = form.getDeleteableQuestions().size()-1; i>=0; i--){
							if(form.getDeleteableQuestions().get(i)){
								questionList.remove(i);
							}
						}
					}
					//reinitialize the deletable questions list
					int numberOfQuestions = form.getSurveyQuestions().size();
					List<Boolean> deleteableQuestions = new ArrayList<Boolean>();
					for(int i=0; i<numberOfQuestions; i++)
						deleteableQuestions.add(new Boolean(false));
					form.setDeleteableQuestions(deleteableQuestions);
				}
			}
		} else if( page == 4 ) {
			if( request.getParameter("action") != null ) {
				if( request.getParameter("action").equals("search") ) {
					
					ManageSurveyForm manageSurveyForm = (ManageSurveyForm)command;

					com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					String searchType = manageSurveyForm.getSearchType();

					List<Course> courseList = null;

					List<SurveyCourse> surveyCourseList = new ArrayList<SurveyCourse>();
					Distributor distributor = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentDistributor();
					if( distributor == null ) {
						Long customerId = null;

						if (user.isLMSAdministrator()){
							customerId = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomerId();
						}
						else{
							customerId= user.getLearner().getCustomer().getId();
						}
						
						if( searchType.equalsIgnoreCase("simplesearch") ) {
							
							courseList = entitlementService.getAllCoursesByEntitlement(customerId);
							if( courseList != null && !courseList.isEmpty() ) {
								long[] courseIdArray = new long[courseList.size()]; 
								int count = 0;
								for( Course course : courseList ) {
									courseIdArray[count] = course.getId();
									count = count + 1;
								}
								courseList = entitlementService.findCoursesByCustomer(courseIdArray
										,"","","",manageSurveyForm.getSimpleSearchKey());
							}
							courseList = entitlementService.getCoursesByEntitlement(customerId, manageSurveyForm.getSimpleSearchKey());
							
						} else if( searchType.equalsIgnoreCase("advancedsearch") ) {
							
							courseList = entitlementService.getAllCoursesByEntitlement(customerId);
							if( courseList != null && !courseList.isEmpty() ) {
								long[] courseIdArray = new long[courseList.size()]; 
								int count = 0;
								for ( Course course : courseList ) {
									courseIdArray[count] = course.getId();
									count = count + 1;
								}
								courseList = null;
								courseList = entitlementService.findCoursesByCustomer(courseIdArray
										,manageSurveyForm.getSearchCourseName()
										,manageSurveyForm.getSearchCourseID()
										,manageSurveyForm.getSearchKeyword(),"");
							}
						} else if( searchType.equalsIgnoreCase("allsearch") ) {
							courseList = entitlementService.getAllCoursesByEntitlement(customerId);
						}
					} else {

						long[] courseIdArray = entitlementService.getCourseIDArrayForDistributor(distributor);
						if ( courseIdArray.length > 0 ) {
							if( searchType.equalsIgnoreCase("simplesearch") ) {
								courseList = entitlementService.findCoursesByDistributor(courseIdArray, 
										"", "", "", manageSurveyForm.getSimpleSearchKey());
								
							} else if( searchType.equalsIgnoreCase("advancedsearch") ) {
								courseList = entitlementService.findCoursesByDistributor(courseIdArray
										,manageSurveyForm.getSearchCourseName()
										, manageSurveyForm.getSearchCourseID()
										,manageSurveyForm.getSearchKeyword(), "");

							} else if( searchType.equalsIgnoreCase("allsearch") ) {
								courseList = entitlementService.findCoursesByDistributor(courseIdArray, "", "", "", "");
							}
						}
					}
					/** Added by Dyutiman...
					 *  manual sorting
					 */
					request.setAttribute("PagerAttributeMap", PagerAttributeMap);
					
					if( courseList != null && sortDirection != null && sortColumnIndex != null ) {
						// sorting against course name
						if( sortColumnIndex.equalsIgnoreCase("0") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {
								for( int i = 0 ; i < courseList.size() ; i ++ ) {
									for( int j = 0 ; j < courseList.size() ; j ++ ) {
										if( i != j ) {
											Course c1 = (Course) courseList.get(i);
											Course c2 = (Course) courseList.get(j);
											if( c1.getCourseTitle().toUpperCase().compareTo
													(c2.getCourseTitle().toUpperCase()) > 0 ) {
												Course tempLG = courseList.get(i);
												courseList.set(i, courseList.get(j));
												courseList.set(j, tempLG);
											}
										}
									}
								}
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 0);
							} else {
								for( int i = 0 ; i < courseList.size() ; i ++ ) {
									for( int j = 0 ; j < courseList.size() ; j ++ ) {
										if( i != j ) {
											Course c1 = (Course) courseList.get(i);
											Course c2 = (Course) courseList.get(j);
											if( c1.getCourseTitle().toUpperCase().compareTo
													(c2.getCourseTitle().toUpperCase()) < 0 ) {
												Course tempLG = courseList.get(i);
												courseList.set(i, courseList.get(j));
												courseList.set(j, tempLG);
											}
										}
									}
								}
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 0);
							}
						}
					}	
					Set<Course> uniqueSet = new LinkedHashSet<Course>();
					if( courseList != null )
						for(int i=0; i<courseList.size();i++){
							uniqueSet.add(courseList.get(i));
						}
					courseList = new ArrayList<Course>(uniqueSet);
					if( courseList != null ) {
						for(Course course : courseList) {
							SurveyCourse surveyCourse = new SurveyCourse();
							surveyCourse.setCourse(course);
							surveyCourse.setSelected(false);
							surveyCourseList.add(surveyCourse);
						}
					}
					manageSurveyForm.setSurveyCourses(surveyCourseList);
				}
			}
		}
		super.postProcessPage(request, command, errors, page);
	}


	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors, int page) throws Exception {
		Map model = new HashMap();

		switch(page){
		case 0:
			model.put("surveyEvents", Survey.SURVEY_EVENTS);
			return model; 
		case 1:
			ManageSurveyForm form = (ManageSurveyForm)command;
			if( form.getSurveyEvent().equalsIgnoreCase(Survey.SURVEY_EVENTS[3]))
				model.put("finish", Survey.SURVEY_EVENTS[3]);
			return model;
		case 2:
			model.put("surveyQuestionTypes", SURVEY_QUESTION_TYPES);
			return model;
		case 3:
			break;
		case 4:
			break;
		default :
			break;
		}	
		return super.referenceData(request, command, errors, page);
	}

	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		ManageSurveyValidator validator = (ManageSurveyValidator)this.getValidator();
		ManageSurveyForm form = (ManageSurveyForm)command;
		switch(page){
		case 0:
			validator.validateFirstPage(form, errors);
			break;
		case 1:
			//validator.validateSecondPage(form, errors);
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			if(finish){
				validator.validateFinishPage(form, errors);
			}
			break;
		default:
			break;
		}
		super.validatePage(command, errors, page, finish);
	}

	protected Object formBackingObject(HttpServletRequest request)
	throws Exception {
		// Auto-generated method stub
		return super.formBackingObject(request);
	}

	protected void onBindOnNewForm(HttpServletRequest request, Object command,
			BindException errors) throws Exception {
		// Auto-generated method stub
		super.onBindOnNewForm(request, command, errors);
	}

	public String getCloseTemplate() {
		return closeTemplate;
	}

	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
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
//						if(str.length()>50)
//							str = str.substring(0,49);
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
	 * @return the entitlementService
	 */
	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	/**
	 * @param entitlementService the entitlementService to set
	 */
	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	/**
	 * @return the cancelTemplate
	 */
	public String getCancelTemplate() {
		return cancelTemplate;
	}

	/**
	 * @param cancelTemplate the cancelTemplate to set
	 */
	public void setCancelTemplate(String cancelTemplate) {
		this.cancelTemplate = cancelTemplate;
	}

}