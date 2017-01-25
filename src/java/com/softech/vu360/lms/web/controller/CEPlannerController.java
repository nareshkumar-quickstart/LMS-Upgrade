package com.softech.vu360.lms.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.AggregateSurveyQuestion;
import com.softech.vu360.lms.model.AggregateSurveyQuestionItem;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.MultipleSelectSurveyQuestion;
import com.softech.vu360.lms.model.PersonalInformationSurveyQuestion;
import com.softech.vu360.lms.model.SingleSelectSurveyQuestion;
import com.softech.vu360.lms.model.SuggestedTraining;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyAnswerItem;
import com.softech.vu360.lms.model.SurveyQuestion;
import com.softech.vu360.lms.model.TextBoxSurveyQuestion;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.model.CEPlannerForm;
import com.softech.vu360.lms.web.controller.model.SurveyQuestionBuilder;
import com.softech.vu360.lms.web.controller.model.survey.TakeSurveyForm;
import com.softech.vu360.lms.web.controller.validator.CEPlannerValidator;
import com.softech.vu360.lms.webservice.client.StorefrontClientWS;
import com.softech.vu360.lms.webservice.message.storefront.synccustomer.ShowSyncCustomerResponse;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.LabelBean;
import com.softech.vu360.util.VU360Branding;

/**
 * @author Dyutiman
 * created on 17th June 2010
 *
 */
public class CEPlannerController extends AbstractWizardFormController {

	private String finishTemplate = null;	
	private String cancelTemplate = null;
	private int NUMBER_OF_LEARNERS_TOBE_ADDED = 0;

	private SurveyService surveyService;
	private CustomerService customerService;
	private DistributorService distributorService;
	private VU360UserService vu360UserService;
	private StorefrontClientWS storefrontClientWS=null;
	private String viewLandingPageTemplate;
	private String errorTemplate;
	

	public CEPlannerController() {
		super();
		setCommandName("cePlannerForm");
		setCommandClass(CEPlannerForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"cePlanner/cePlannerCreateProfile"
				, "cePlanner/cePlannerSurvey"
				, "cePlanner/cePlannerTrainingPlan"
				, "cePlanner/cePlannerLearners"});
	}

	protected Object formBackingObject( HttpServletRequest request ) throws Exception {

		Object command = super.formBackingObject(request);
		CEPlannerForm form = (CEPlannerForm)command;
		
//		TakeSurveyForm takeSurveyForm = null;
//		if(form.getTakeSurveyForm()==null){
//			takeSurveyForm = new TakeSurveyForm();
//			
//			
//			
//			
//			
//			form.setTakeSurveyForm(takeSurveyForm);
//		}
//		else{
//			takeSurveyForm = form.getTakeSurveyForm();
//		}
//		
		
		
		ArrayList<String> firstNames = new ArrayList <String>();
		ArrayList<String> lastNames = new ArrayList <String>();
		ArrayList<String> emailAddresses = new ArrayList <String>();
		ArrayList<String> passwords = new ArrayList <String>();
		String surveyId = request.getParameter("Id");
		
		firstNames.add("");
		lastNames.add("");
		emailAddresses.add("");
		passwords.add("");
		form.setFirstNames(firstNames);
		form.setLastNames(lastNames);
		form.setEmailAddresses(emailAddresses);
		form.setPasswords(passwords);
		form.setSurveyId(surveyId);
//		takeSurveyForm.setSurveyId(Long.parseLong(surveyId));
		return command;
	}

	@SuppressWarnings("unchecked")
	protected Map<String, Object> referenceData(HttpServletRequest request, Object command,	Errors errors, int page) throws Exception {

		CEPlannerForm form = (CEPlannerForm)command;
		//TakeSurveyForm takeSurveyForm = form.getTakeSurveyForm();

		switch(page) {

		case 0:
			break;
		case 1:	
			String wizardControl = request.getParameter("wizardControl");
			form.setWizardControl(wizardControl);
			int i;
			int j = 0;
			int index = 0;
			int questionNo = 0;
			int nextPageIndex = 0;
			int questionIndex = 0;
			Map<String, Object> model = new HashMap<String, Object>();
			Survey survey;
//			if( !StringUtils.isBlank(form.getSurveyId()) ) {
			if( !StringUtils.isBlank(form.getSurveyId().toString()) ) {
				logger.info("Survey ID is : " + form.getSurveyId() );
				survey = surveyService.getSurveyByID(Long.parseLong(form.getSurveyId())); //surveys.get(0);
				//survey = surveyService.getSurveyByID(form.getSurveyId());
				if(survey==null)
					survey=new Survey();
			} else {
				survey = new Survey();
			}
			if( request.getParameter("nextPageIndex") != null && !survey.getIsShowAll() ) {
				index = Integer.parseInt(request.getParameter("nextPageIndex"));
				questionIndex = index*survey.getQuestionsPerPage();
				questionNo = index*survey.getQuestionsPerPage();;
			}
/*
			SurveyQuestionBuilder builder = new SurveyQuestionBuilder(survey); 

			List<SurveyQuestion> surveyQuestionList = survey.getQuestions();
			Collections.sort(surveyQuestionList);

			for( i = questionIndex ; i < surveyQuestionList.size() ; i++ ) {
				j++;
				com.softech.vu360.lms.model.SurveyQuestion question = surveyQuestionList.get(i);
				
				if(question instanceof AggregateSurveyQuestion){
					builder.buildAggregateSurveyQuestion((AggregateSurveyQuestion)question, surveyService.getAggregateSurveyQuestionItemsByQuestionId(question.getId()), null);
				}
				else{
					builder.buildQuestion(question,null);
				}
				
				
				
				if( !survey.isShowAll() && j == survey.getQuestionsPerPage() ) {
					i++;
					break;
				}
			}
			com.softech.vu360.lms.web.controller.model.survey.Survey surveyView = builder.getSurveyView();
			form.setSurvey(surveyView);
			//form.setSurvey(surveyView);
*/
			
			
			int totalQuestions = 0;
			
			SurveyQuestionBuilder builder = new SurveyQuestionBuilder(survey); 		
			List<SurveyQuestion> surveyQuestionList = survey.getQuestions();
			Collections.sort(surveyQuestionList);
			List<Long> sqIndex = new ArrayList<Long>();
			for(i=questionIndex; i<surveyQuestionList.size(); i++){
				totalQuestions++;
				
				com.softech.vu360.lms.model.SurveyQuestion question = surveyQuestionList.get(i);

					if (question instanceof AggregateSurveyQuestion) {
						
						AggregateSurveyQuestion aggrtQuestion = (AggregateSurveyQuestion)question;
						List<AggregateSurveyQuestionItem> aggregateSurveyQuestionItems = surveyService.getAggregateSurveyQuestionItemsByQuestionId(aggrtQuestion.getId());
						if (aggregateSurveyQuestionItems != null) {
							builder.buildAggregateSurveyQuestion(aggrtQuestion, aggregateSurveyQuestionItems,null);
						}

					} else if (question instanceof PersonalInformationSurveyQuestion) {
						builder.buildPersonalInformationQuestion(question, null, null);
					} else {
						builder.buildQuestion(question,null);
					}
				//}
				if (!survey.getIsShowAll() && totalQuestions == survey.getQuestionsPerPage() ) {
					i++;
					break;
				}
			}
			com.softech.vu360.lms.web.controller.model.survey.Survey surveyView = builder.getSurveyView();
			//set it up in the form object
			if( form.getSurvey() == null   || request.getParameter("launch") != null )
				form.setSurvey(surveyView);
			else
				form.getSurvey().getQuestionList().addAll(surveyView.getQuestionList());

			Set<com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion> set = new HashSet<com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion>(form.getSurvey().getQuestionList());
			ArrayList<com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion> newUniqueQuestionList = new ArrayList<com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion>(set);
			//Added to save the custom question responses to the db
			Collections.sort(newUniqueQuestionList);
			for( j=0;j<newUniqueQuestionList.size();j++){
				if(newUniqueQuestionList.get(j).getSurveyQuestionRef() instanceof AggregateSurveyQuestion){
					sqIndex.add((long) j);
				}
			}
			int z=0;
			for(int k=0;k<newUniqueQuestionList.size();k++){
				com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion squestion =newUniqueQuestionList.get(k);
					if(squestion.getSurveyQuestionRef() instanceof AggregateSurveyQuestion){
						newUniqueQuestionList.get(k).setIndex(sqIndex.get(z));
						z++;
					}
			}
			
			form.getSurvey().setQuestionList(newUniqueQuestionList);

			
			
			
			
			
			
			
			
			
			
			
			
			
			if( i == surveyQuestionList.size() || survey.getIsShowAll() ) 
				nextPageIndex = 0;
			else
				nextPageIndex = index+1;

			boolean showTerms = surveyView.getSurveyRef().isElectronicSignatureRequire();
			if( showTerms ) 
				model.put("showTerms", surveyView.getSurveyRef().getElectronicSignature());
			else
				model.put("showTerms", "false");

			boolean showLinks = surveyView.getSurveyRef().isLinkSelected();
			if( showLinks )
				model.put("showLinks", surveyService.getSurveyLinksById(survey.getId()));
			else
				model.put("showLinks", "false");

			model.put("nextPageIndex", nextPageIndex);
			model.put("questionNo", questionNo);
			model.put("totalQuestions", totalQuestions);

			return model;
		case 2:
			long[] ids = new long[1];
			//ids[0] = Long.valueOf(form.getSurveyId());
			ids[0] = Long.valueOf(form.getSurveyId());
			
			List<SuggestedTraining> suggTrainings = surveyService.getSuggestedTrainingsBySurveyIDs(ids);
			Set<Course> courseSet = new HashSet<Course>();
			for (SuggestedTraining sugTran : suggTrainings) {
				//for (com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion question : form.getSurvey().getQuestionList()) {
				for (com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion question : form.getSurvey().getQuestionList()) {
					List<com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem> answerItems = question.getAnswerItems();
					for (com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem ansItem : answerItems) {
						if (ansItem.isSelected() || (question.getSingleSelectAnswerId() != null && question.getSingleSelectAnswerId() != 0 )) {
							boolean flag = false;
							for (SurveyAnswerItem item : sugTran.getResponses()) {
								if (ansItem.getSurveyAnswerItemRef().getId().compareTo(item.getId()) == 0 
										|| question.getSingleSelectAnswerId().compareTo(item.getId()) == 0 ) {
									courseSet.addAll(sugTran.getCourses());
									flag = true;
									break;
								}
							}
							if (flag) {
								break;
							}
						}
					}
				}
			}
			if (courseSet != null && courseSet.size() > 0) {
				List<Course> courses = new ArrayList<Course>();
				Iterator<Course> itr = courseSet.iterator(); 
				while(itr.hasNext()) {
					Course c = itr.next(); 
					courses.add(c);
				} 
				form.setCourses(courses);
			}
			break;
		case 3:
			
			break;
		default:
			break;
		}
		return super.referenceData(request, command, errors, page);
	}
	
	

	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

		CEPlannerForm form = (CEPlannerForm)command;
		if (page == 2 && (this.getTargetPage(request, 2) == 3) && 
				(!StringUtils.isBlank(form.getNumberOfReps()) &&  StringUtils.isNumeric(form.getNumberOfReps())) ) {
			this.NUMBER_OF_LEARNERS_TOBE_ADDED= 0;
			ArrayList<String> firstNames = form.getFirstNames();
			ArrayList<String> lastNames = form.getLastNames();
			ArrayList<String> emailAddresses = form.getEmailAddresses();
			ArrayList<String> passwords = form.getPasswords();
			firstNames.add("");
			lastNames.add("");
			emailAddresses.add("");
			passwords.add("");
			for (int loop = NUMBER_OF_LEARNERS_TOBE_ADDED;loop < Integer.parseInt(form.getNumberOfReps())-1; loop++) {
				NUMBER_OF_LEARNERS_TOBE_ADDED++;
				firstNames.add("");
				lastNames.add("");
				emailAddresses.add("");
				passwords.add("");
				form.setFirstNames(firstNames);
				form.setLastNames(lastNames);
				form.setEmailAddresses(emailAddresses);
				form.setPasswords(passwords);
				form.setNumberOfLearners(NUMBER_OF_LEARNERS_TOBE_ADDED);
			}
		}
		if( page == 3 ) {
			String action = form.getAction();
			int deleteIndex = Integer.parseInt(form.getDeleteIndex());
			if( !StringUtils.isBlank(action) && action.equalsIgnoreCase("add") ) {
				NUMBER_OF_LEARNERS_TOBE_ADDED++;
				ArrayList<String> firstNames = form.getFirstNames();
				ArrayList<String> lastNames = form.getLastNames();
				ArrayList<String> emailAddresses = form.getEmailAddresses();
				ArrayList<String> passwords = form.getPasswords();
				firstNames.add("");
				lastNames.add("");
				emailAddresses.add("");
				passwords.add("");
				form.setFirstNames(firstNames);
				form.setLastNames(lastNames);
				form.setEmailAddresses(emailAddresses);
				form.setPasswords(passwords);
				form.setNumberOfLearners(NUMBER_OF_LEARNERS_TOBE_ADDED);
				form.setNumberOfReps((Integer.parseInt(form.getNumberOfReps())+1)+"");
			} else if( !StringUtils.isBlank(action) && action.equalsIgnoreCase("delete") ) {
				NUMBER_OF_LEARNERS_TOBE_ADDED--;
				ArrayList<String> firstNames = form.getFirstNames();
				ArrayList<String> lastNames = form.getLastNames();
				ArrayList<String> emailAddresses = form.getEmailAddresses();
				ArrayList<String> passwords = form.getPasswords();
				//firstNames.remove(firstNames.size()-1);
				//lastNames.remove(lastNames.size()-1);
				//emailAddresses.remove(emailAddresses.size()-1);
				//passwords.remove(passwords.size()-1);
				
				firstNames.remove(deleteIndex);
				lastNames.remove(deleteIndex);
				emailAddresses.remove(deleteIndex);
				passwords.remove(deleteIndex);
				form.setFirstNames(firstNames);
				form.setLastNames(lastNames);
				form.setEmailAddresses(emailAddresses);
				form.setPasswords(passwords);
				form.setNumberOfLearners(NUMBER_OF_LEARNERS_TOBE_ADDED);
				form.setNumberOfReps((Integer.parseInt(form.getNumberOfReps())-1)+"");
			}
		}
		super.postProcessPage(request, command, errors, page);
	}

	protected void validatePage(Object command, Errors errors, int page, boolean finish) {

		CEPlannerValidator validator = (CEPlannerValidator)this.getValidator();
		CEPlannerForm form = (CEPlannerForm)command;
		if(form.getValidateAction().equalsIgnoreCase("donotValidate"))
			return;
		switch( page ) {
		case 0:
			validator.validateFirstPage(form, errors);
			break;
		case 1:
			validator.validateSurvey(form,errors);
			break;
		case 2:
			validator.validateThirdPage(form, errors);
			
			break;
		case 3:
			if( !StringUtils.isBlank(form.getAction()) && !form.getAction().equalsIgnoreCase("add") && !form.getAction().equalsIgnoreCase("delete") ) {
				validator.validateFourthPage(form, errors);
			}
			break;
		default:
			break;
		}
		super.validatePage(command, errors, page, finish);
	}

	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse responce, Object command, BindException errors)
	throws Exception {
		// TODO
		
		
		HashMap<Long, Long> questionsMapping = new HashMap<Long, Long>();
		HashMap<Long, Long> answersMapping = new HashMap<Long, Long>();
		CEPlannerForm form = (CEPlannerForm)command;
		//TakeSurveyForm takeSurveyForm = form.getTakeSurveyForm();
		Distributor distributor = new Distributor();
		//distributor.setId(form.getSurvey().getSurveyRef().getOwner().getId());
		distributor.setId(form.getSurvey().getSurveyRef().getOwner().getId());
		String finalURL="";
		Map context = new HashMap<Object, Object>();
		
		Customer customer = customerService.getCustomerByEmail(form.getEmailAdd());
		if(customer==null)
			customer = customerService.addCustomer(null, distributor, form);
		
		
		
		//Now for Survey & Its Result;
		
		
		
		//Clone it
		Survey oldSurvey = surveyService.getSurveyByID(Long.parseLong(form.getSurveyId()));
		Survey newSurvey = oldSurvey.getClone();
		customer.initializeOwnerParams();
		newSurvey.setOwner(customer);
		
		
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
					
					
					//Map old Ids with New Ids for Questions
					
					questionsMapping.put(question.getId(), newlyAddedQuestion.getId());
					
					//Map old Ids with New Ids for Answers
					for(SurveyAnswerItem oldItem : question.getSurveyAnswers()){
						for(SurveyAnswerItem newAnswerItem : newlyAddedQuestion.getSurveyAnswers()){
							
							if(oldItem.getLabel().equalsIgnoreCase(newAnswerItem.getLabel())){
								answersMapping.put(oldItem.getId(), newAnswerItem.getId());
								break;
							}
						}
					}
					
					
					
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
					
					
					//Map old Ids with New Ids for Questions
					
					questionsMapping.put(question.getId(), newlyAddedQuestion.getId());
					
					//Map old Ids with New Ids for Answers
					for(SurveyAnswerItem oldItem : question.getSurveyAnswers()){
						for(SurveyAnswerItem newAnswerItem : newlyAddedQuestion.getSurveyAnswers()){
							
							if(oldItem.getLabel().equalsIgnoreCase(newAnswerItem.getLabel())){
								answersMapping.put(oldItem.getId(), newAnswerItem.getId());
								break;
							}
						}
					}
					
					
					
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
					
					
					//Map old Ids with New Ids for Questions
					
					questionsMapping.put(question.getId(), newlyAddedQuestion.getId());
					
					//Map old Ids with New Ids for Answers
					for(SurveyAnswerItem oldItem : question.getSurveyAnswers()){
						for(SurveyAnswerItem newAnswerItem : newlyAddedQuestion.getSurveyAnswers()){
							
							if(oldItem.getLabel().equalsIgnoreCase(newAnswerItem.getLabel())){
								answersMapping.put(oldItem.getId(), newAnswerItem.getId());
								break;
							}
						}
					}
					
					
					
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
					
					
					//Map old Ids with New Ids for Questions
					
					questionsMapping.put(question.getId(), newlyAddedQuestion.getId());
					
					//Map old Ids with New Ids for Answers
					for(SurveyAnswerItem oldItem : question.getSurveyAnswers()){
						for(SurveyAnswerItem newAnswerItem : newlyAddedQuestion.getSurveyAnswers()){
							
							if(oldItem.getLabel().equalsIgnoreCase(newAnswerItem.getLabel())){
								answersMapping.put(oldItem.getId(), newAnswerItem.getId());
								break;
							}
						}
					}
					
					
					
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
				
				
				
				//Map old Ids with New Ids for Questions
				
				questionsMapping.put(question.getId(), newQuestion.getId());
				
				//Map old Ids with New Ids for Answers
				for(SurveyAnswerItem oldItem : question.getSurveyAnswers()){
					for(SurveyAnswerItem newAnswerItem1 : newQuestion.getSurveyAnswers()){
						
						if(oldItem.getLabel().equalsIgnoreCase(newAnswerItem1.getLabel())){
							answersMapping.put(oldItem.getId(), newAnswerItem1.getId());
							break;
						}
					}
				}
				
				
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
						
						
						//Map old Ids with New Ids for Questions
						
						questionsMapping.put(oldAggItem.getQuestion().getId(), newAggItem.getQuestion().getId());
						
						//Map old Ids with New Ids for Answers
						for(SurveyAnswerItem oldItem : oldAggItem.getQuestion().getSurveyAnswers()){
							for(SurveyAnswerItem newAnswerItem1 : newAggItem.getQuestion().getSurveyAnswers()){
								
								if(oldItem.getLabel().equalsIgnoreCase(newAnswerItem1.getLabel())){
									answersMapping.put(oldItem.getId(), newAnswerItem1.getId());
									break;
								}
							}
						}
						
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
						
						//Map old Ids with New Ids for Questions
						
						questionsMapping.put(oldAggItem.getQuestion().getId(), newAggItem.getQuestion().getId());
						
						//Map old Ids with New Ids for Answers
						for(SurveyAnswerItem oldItem : oldAggItem.getQuestion().getSurveyAnswers()){
							for(SurveyAnswerItem newAnswerItem1 : newAggItem.getQuestion().getSurveyAnswers()){
								
								if(oldItem.getLabel().equalsIgnoreCase(newAnswerItem1.getLabel())){
									answersMapping.put(oldItem.getId(), newAnswerItem1.getId());
									//break;
								}
							}
						}
						
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
						
						//Map old Ids with New Ids for Questions
						
						questionsMapping.put(oldAggItem.getQuestion().getId(), newAggItem.getQuestion().getId());
						
						//Map old Ids with New Ids for Answers
						for(SurveyAnswerItem oldItem : oldAggItem.getQuestion().getSurveyAnswers()){
							for(SurveyAnswerItem newAnswerItem1 : newAggItem.getQuestion().getSurveyAnswers()){
								
								if(oldItem.getLabel().equalsIgnoreCase(newAnswerItem1.getLabel())){
									answersMapping.put(oldItem.getId(), newAnswerItem1.getId());
									//break;
								}
							}
						}
						
					}
					
					
					
					newAggItem.setAggregateSurveyQuestion((AggregateSurveyQuestion)newQuestion);
					
					aggItemsNew.add(newAggItem);
					//surveyService.saveAggregateSurveyQuestionItem(aggItemsNew);
					
				}
				
				surveyService.saveAggregateSurveyQuestionItem(aggItemsNew);
			}
			
			
		}
		
//NOw Replace IDs
		
		for(com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion oldQuestion :  form.getSurvey().getQuestionList()){

			if(oldQuestion.getSurveyQuestionRef() instanceof AggregateSurveyQuestion){
			
				if(questionsMapping.get(oldQuestion.getSurveyQuestionRef().getId())!=null){
					oldQuestion.getSurveyQuestionRef().setId(questionsMapping.get(oldQuestion.getSurveyQuestionRef().getId()));
				}
				
				
				for(com.softech.vu360.lms.web.controller.model.survey.AggregateSurveyQuestionItem oldaggItem :  oldQuestion.getAggregateAnswerItems()){
					if(questionsMapping.get(oldaggItem.getAggregateSurveyQuestionItemRef().getQuestion().getId())!=null){
						oldaggItem.getAggregateSurveyQuestionItemRef().getQuestion().setId(questionsMapping.get(oldaggItem.getAggregateSurveyQuestionItemRef().getQuestion().getId()));
					}
					if(questionsMapping.get(oldaggItem.getAggregateSurveyQuestionItemRef().getAggregateSurveyQuestion().getId())!=null){
						oldaggItem.getAggregateSurveyQuestionItemRef().getAggregateSurveyQuestion().setId(questionsMapping.get(oldaggItem.getAggregateSurveyQuestionItemRef().getAggregateSurveyQuestion().getId()));
					}
					
					//for Answer
					
					if(oldQuestion.getSingleSelectAnswerId()!=null){
					
						if(answersMapping.get(oldQuestion.getSingleSelectAnswerId())!=null){
							oldQuestion.setSingleSelectAnswerId(answersMapping.get(oldQuestion.getSingleSelectAnswerId()));
							
						}
					}
					
					for(SurveyAnswerItem oldAns : oldaggItem.getAggregateSurveyQuestionItemRef().getQuestion().getSurveyAnswers()){
						if(answersMapping.get(oldAns.getId())!=null){
							oldAns.setId(answersMapping.get(oldAns.getId()));
//							oldAns.getSurveyQuestion().setId(oldaggItem.getAggregateSurveyQuestionItemRef().getQuestion().getId());
							oldAns.getSurveyQuestion().setId(questionsMapping.get(oldAns.getSurveyQuestion().getId()));
						}
					}
					
					
				}
				
				
				
			}
			else{ // i.e. non aggre
				
				if(oldQuestion.getSurveyQuestionRef() instanceof SingleSelectSurveyQuestion){
					if(questionsMapping.get(oldQuestion.getSurveyQuestionRef().getId())!=null){
						oldQuestion.getSurveyQuestionRef().setId(questionsMapping.get(oldQuestion.getSurveyQuestionRef().getId()));
					}
					//for Answer
					
						
						if(answersMapping.get(oldQuestion.getSingleSelectAnswerId())!=null){
							oldQuestion.setSingleSelectAnswerId(answersMapping.get(oldQuestion.getSingleSelectAnswerId()));
						}
					
						for(SurveyAnswerItem oldAns : oldQuestion.getSurveyQuestionRef().getSurveyAnswers()){
							
							if(answersMapping.get(oldAns.getId())!=null){
								oldAns.setId(answersMapping.get(oldAns.getId()));
								oldAns.getSurveyQuestion().setId(oldQuestion.getSurveyQuestionRef().getId());
							}
						}
					
				}
				else{
				
			
				if(questionsMapping.get(oldQuestion.getSurveyQuestionRef().getId())!=null){
					oldQuestion.getSurveyQuestionRef().setId(questionsMapping.get(oldQuestion.getSurveyQuestionRef().getId()));
				}
				//for Answers
				for(SurveyAnswerItem oldAns : oldQuestion.getSurveyQuestionRef().getSurveyAnswers()){
					
					if(answersMapping.get(oldAns.getId())!=null){
						oldAns.setId(answersMapping.get(oldAns.getId()));
						oldAns.getSurveyQuestion().setId(oldQuestion.getSurveyQuestionRef().getId());
					}
				}
				}
			}
			
		}
		
		for(SurveyQuestion oldQuestion :  form.getSurvey().getSurveyRef().getQuestions()){
						

			
			if(oldQuestion instanceof AggregateSurveyQuestion){
			
			
				
				for(com.softech.vu360.lms.model.AggregateSurveyQuestionItem oldaggItem :  surveyService.getAggregateSurveyQuestionItemsByQuestionId(oldQuestion.getId())){
					if(questionsMapping.get(oldaggItem.getQuestion().getId())!=null){
						oldaggItem.getQuestion().setId(questionsMapping.get(oldaggItem.getQuestion().getId()));
					}
					if(questionsMapping.get(oldaggItem.getAggregateSurveyQuestion().getId())!=null){
						oldaggItem.getAggregateSurveyQuestion().setId(questionsMapping.get(oldaggItem.getAggregateSurveyQuestion().getId()));
					}
					
					//for Answer
					
					
					
					for(SurveyAnswerItem oldAns : oldaggItem.getQuestion().getSurveyAnswers()){
						if(answersMapping.get(oldAns.getId())!=null){
							oldAns.setId(answersMapping.get(oldAns.getId()));
							oldAns.getSurveyQuestion().setId(oldaggItem.getQuestion().getId());
						}
					}
					
					
				}
				
				if(questionsMapping.get(oldQuestion.getId())!=null){
					oldQuestion.setId(questionsMapping.get(oldQuestion.getId()));
				}
	
				
			}
			else{ // i.e. non aggre
				
				if(oldQuestion instanceof SingleSelectSurveyQuestion){
					if(questionsMapping.get(oldQuestion.getId())!=null){
						oldQuestion.setId(questionsMapping.get(oldQuestion.getId()));
					}
					//for Answer
					
						
											
						for(SurveyAnswerItem oldAns : oldQuestion.getSurveyAnswers()){
							
							if(answersMapping.get(oldAns.getId())!=null){
								oldAns.setId(answersMapping.get(oldAns.getId()));
								oldAns.getSurveyQuestion().setId(oldQuestion.getId());
							}
						}
					
				}
				else{
				
			
				if(questionsMapping.get(oldQuestion.getId())!=null){
					oldQuestion.setId(questionsMapping.get(oldQuestion.getId()));
				}
				//for Answers
				for(SurveyAnswerItem oldAns : oldQuestion.getSurveyAnswers()){
					
					if(answersMapping.get(oldAns.getId())!=null){
						oldAns.setId(answersMapping.get(oldAns.getId()));
						oldAns.getSurveyQuestion().setId(oldQuestion.getId());
					}
				}
				}
			}
			
		
			
			
			
			
			
		}
		
		
		
		
		VU360User user = vu360UserService.getUsersByEmailAddress(customer.getEmail()).get(0);
		form.setSurveyee(user);
		form.setSurveyId(newSurvey.getId().toString());
		//takeSurveyForm.getSurvey().setSurveyRef(newSurvey);
		form.setLearner(user.getLearner());
		form.setCourseId(new Long(0));
		form.setLearningSessionId("");
		
		

		form.getSurvey().getSurveyRef().setId(newSurvey.getId());
		
		
		
		TakeSurveyForm takeSurveyForm = new TakeSurveyForm();
		
		takeSurveyForm.setSurveyee(user);
		takeSurveyForm.setSurveyId(newSurvey.getId());
		//takeSurveyForm.getSurvey().setSurveyRef(newSurvey);
		takeSurveyForm.setLearner(user.getLearner());
		takeSurveyForm.setCourseId(new Long(0));
		takeSurveyForm.setLearningSessionId("");
		takeSurveyForm.setSurvey(form.getSurvey());
		
				
		
		//Now Save results against the new survey
		surveyService.addSurveyResultForCEPlanner(user, takeSurveyForm, questionsMapping,answersMapping);
		
		/*
		 * TSM - Technology Stack Migration
		 * 
		 * Following code piece was written to enforce
		 * cache update and such calls are now unnecessary.  
		 * Cache must not be enforced in such manner, therefore
		 * this code has been taken off
		 * 
		 * */
		//newSurvey = surveyService.refreshSurveys(newSurvey.getId());
		
				
		form.setSurvey(null);
		form.setSurveyId(null);
		
		
		
		//Call SF service to create Customer 
		try{
			ShowSyncCustomerResponse response = storefrontClientWS.createCustomerOnStoreFront(customer, form.getUsername(), form.getPassword());
			if(response!=null){
				if(response.getSuccess().equalsIgnoreCase("true")){
					finalURL=getFinalUrl(response,form.getNumberOfReps(), customer);
								
					System.out.println("FINAL URL : " + finalURL);
					responce.sendRedirect(finalURL);
					return null;
				}
				else{
					logger.error("Error in WS : " + response.getMessage());
					
					context.put("error",response.getMessage().toString());
					return new ModelAndView(errorTemplate, "context", context);
					
				}
				
			}
		}
		catch(Exception ex){
			logger.error(ex, ex);
			context.put("error",ex.getMessage());
			return new ModelAndView(errorTemplate, "context", context);
		}
		
		context.put("error","Error in Web Service Calling.");
		return new ModelAndView(errorTemplate, "context", context);
		
		
		
	}

	private String getFinalUrl(ShowSyncCustomerResponse response,String numberOfReps, Customer customer) {

		com.softech.vu360.lms.vo.Language lang = new com.softech.vu360.lms.vo.Language();
		lang.setLanguage(Language.DEFAULT_LANG);
		
		Brander brander = VU360Branding.getInstance().getBrander(customer.getBrandName(), lang);
		List<LabelBean> products = brander.getBrandMapElements("self.service.firmElement.SKU");
		
		String strProductURLSet="";
		int counter=1;
		for( LabelBean product : products ) {
			
			strProductURLSet+="&productId_"+ counter + "=" + product.getLabel() + "&quantity_" + counter + "=" + numberOfReps;
			counter++;
			
		}
		String finalURL = response.getStoreURL()+"&numberOfProducts=" + products.size() +  strProductURLSet;

		return finalURL;
	}

	public String getFinishTemplate() {
		return finishTemplate;
	}

	public void setFinishTemplate(String finishTemplate) {
		this.finishTemplate = finishTemplate;
	}

	public String getCancelTemplate() {
		return cancelTemplate;
	}

	public void setCancelTemplate(String cancelTemplate) {
		this.cancelTemplate = cancelTemplate;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public DistributorService getDistributorService() {
		return distributorService;
	}

	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}

	public StorefrontClientWS getStorefrontClientWS() {
		return storefrontClientWS;
	}

	public void setStorefrontClientWS(StorefrontClientWS storefrontClientWS) {
		this.storefrontClientWS = storefrontClientWS;
	}

	
	public String getViewLandingPageTemplate() {
		return viewLandingPageTemplate;
	}

	public void setViewLandingPageTemplate(String viewLandingPageTemplate) {
		this.viewLandingPageTemplate = viewLandingPageTemplate;
	}

	public String getErrorTemplate() {
		return errorTemplate;
	}

	public void setErrorTemplate(String errorTemplate) {
		this.errorTemplate = errorTemplate;
	}

	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	
	

	

}