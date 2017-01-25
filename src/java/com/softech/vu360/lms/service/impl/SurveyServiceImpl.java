package com.softech.vu360.lms.service.impl;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.AggregateSurveyQuestion;
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
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearningSession;
import com.softech.vu360.lms.model.MessageTemplate;
import com.softech.vu360.lms.model.MultipleSelectSurveyQuestion;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.PredictSurveyQuestionMapping;
import com.softech.vu360.lms.model.SingleSelectSurveyQuestion;
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
import com.softech.vu360.lms.model.TPAnswer;
import com.softech.vu360.lms.model.TPQuestion;
import com.softech.vu360.lms.model.TextBoxSurveyQuestion;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.WrittenTrainingPlan;
import com.softech.vu360.lms.repositories.AggregateSurveyQuestionItemRepository;
import com.softech.vu360.lms.repositories.AlertQueueRepository;
import com.softech.vu360.lms.repositories.AlertRecipientRepository;
import com.softech.vu360.lms.repositories.AlertRepository;
import com.softech.vu360.lms.repositories.AlertTriggerFilterRepository;
import com.softech.vu360.lms.repositories.AlertTriggerRepository;
import com.softech.vu360.lms.repositories.AvailableAlertEventRepository;
import com.softech.vu360.lms.repositories.AvailablePersonalInformationfieldItemRepository;
import com.softech.vu360.lms.repositories.CourseRepository;
import com.softech.vu360.lms.repositories.EmailAddressAlertRecipientRepository;
import com.softech.vu360.lms.repositories.EmailAddressRepository;
import com.softech.vu360.lms.repositories.LearnerAlertRecipientRepository;
import com.softech.vu360.lms.repositories.LearnerGroupAlertRecipientRepository;
import com.softech.vu360.lms.repositories.LearnerGroupRepository;
import com.softech.vu360.lms.repositories.LearnerRepository;
import com.softech.vu360.lms.repositories.MessageTemplateRepository;
import com.softech.vu360.lms.repositories.OrgGroupAlertRecipientRepository;
import com.softech.vu360.lms.repositories.OrganizationalGroupRepository;
import com.softech.vu360.lms.repositories.PredictSurveyQuestionMappingRepository;
import com.softech.vu360.lms.repositories.SuggestedTrainingRepository;
import com.softech.vu360.lms.repositories.SurveyAnswerItemRepository;
import com.softech.vu360.lms.repositories.SurveyFlagRepository;
import com.softech.vu360.lms.repositories.SurveyFlagTemplateRepository;
import com.softech.vu360.lms.repositories.SurveyLearnerRepository;
import com.softech.vu360.lms.repositories.SurveyLinkRepository;
import com.softech.vu360.lms.repositories.SurveyQuestionBankRepository;
import com.softech.vu360.lms.repositories.SurveyQuestionRepository;
import com.softech.vu360.lms.repositories.SurveyRepository;
import com.softech.vu360.lms.repositories.SurveyResultAnswerFileRepository;
import com.softech.vu360.lms.repositories.SurveyResultAnswerRepository;
import com.softech.vu360.lms.repositories.SurveyResultRepository;
import com.softech.vu360.lms.repositories.SurveyReviewCommentRepository;
import com.softech.vu360.lms.repositories.SurveySectionRepository;
import com.softech.vu360.lms.repositories.SurveySectionSurveyQuestionBankRepository;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.StatisticsService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.vo.SaveSurveyParam;
import com.softech.vu360.lms.vo.SurveyVO;
import com.softech.vu360.lms.web.controller.model.AddCourseInSuggestedTrainingForm;
import com.softech.vu360.lms.web.controller.model.SuggestedTrainingForm;
import com.softech.vu360.lms.web.controller.model.SurveyFlagAndSurveyResult;
import com.softech.vu360.lms.web.controller.model.SurveyItem;
import com.softech.vu360.lms.web.controller.model.survey.SurveyAnalysis;
import com.softech.vu360.lms.web.controller.model.survey.TakeSurveyForm;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.VU360Branding;

/**
 * 
 * @author Saptarshi
 *
 */
@Transactional
public class SurveyServiceImpl implements SurveyService	{
	
	
	@Inject 
	private CourseRepository courseRepository = null;
	
	@Inject 
	private AlertQueueRepository alertQueueRepository = null;
	
	@Inject 
	private AvailableAlertEventRepository alertAvailableAlertEventRepository = null;

	private static final Logger log=Logger.getLogger(SurveyServiceImpl.class);
	//private SurveyDAO surveyDAO = null;
	private EntitlementService entitlementService = null;
	private LearnerService learnerService = null; 
	//private StatisticsDAO statisticsDAO = null;
	private StatisticsService statisticsService=null;
	private CourseAndCourseGroupService courseAndCourseGroupService = null;
	@Autowired
	private SurveySectionSurveyQuestionBankRepository surveySectionSurveyQuestionBankRepository;
	@Autowired
	private SurveyReviewCommentRepository surveyReviewCommentRepository;
	@Autowired
	private SurveyResultAnswerRepository surveyResultAnswerRepository;
	@Autowired
	private SurveyResultRepository surveyResultRepository;
	@Autowired
	private SurveyFlagRepository surveyFlagRepository;
	@Autowired
	private SuggestedTrainingRepository suggestedTrainingRepository;
	@Autowired
	private SurveyQuestionRepository surveyQuestionRepository;
	@Autowired
	private AggregateSurveyQuestionItemRepository aggregateSurveyQuestionItemRepository;
	@Autowired
	private SurveyRepository surveyRepository;
	@Autowired
	private SurveyAnswerItemRepository surveyAnswerItemRepository;
	@Autowired
	private SurveyFlagRepository surveyFlagRespository;
	@Autowired
	private SurveyLinkRepository surveyLinkRepository;
	@Autowired
	private SurveyFlagTemplateRepository surveyFlagTemplateRepository;
	@Autowired
	private AvailablePersonalInformationfieldItemRepository availablePersonalInformationfieldItemRepository;
	@Autowired
	private PredictSurveyQuestionMappingRepository predictSurveyQuestionMappingRepository;
	@Autowired
	private SurveyResultAnswerFileRepository surveyResultAnswerFileRepository;
	@Autowired
	private SurveyQuestionBankRepository surveyQuestionBankRepository;
	@Autowired
	private SurveySectionRepository surveySectionRepository;
	@Autowired
	private SurveyLearnerRepository surveyLearnerRepository;
	@Autowired
	private AlertRepository alertRepository;
	@Autowired
	private AlertRecipientRepository alertRecipientRepository;
	@Autowired
	private LearnerAlertRecipientRepository learnerAlertRecipientRepository;
	@Autowired
	private LearnerGroupAlertRecipientRepository learnerGroupAlertRecipientRepository;
	@Autowired
	private OrgGroupAlertRecipientRepository orgGroupAlertRecipientRepository;
	@Autowired
	private EmailAddressAlertRecipientRepository emailAddressAlertRecipientRepository;
	@Autowired
	private LearnerRepository learnerRepository;
	@Autowired
	private AlertTriggerFilterRepository alertTriggerFilterRepository;
	@Autowired
	private AlertTriggerRepository alertTriggerRepository;
	@Autowired
	private LearnerGroupRepository learnerGroupRepository;
	
	@Inject
	private OrganizationalGroupRepository organizationalGroupRepository;
	
	@Inject
	private EmailAddressRepository emailAddressRepository;
	
	@Inject
	private MessageTemplateRepository messageTemplateRepository;

	private static final String BLANK_SEARCH = "";
	
	public List<Survey> getAllSurvey(SurveyOwner surveyOwner) {
		return surveyRepository.findByOwnerAndIsLockedFalse(surveyOwner);
	}


	public Map<String,Object>  getSurveyQASummary(VU360User user,TakeSurveyForm form){
		Map<String,Object> questionAnswerMap = new HashMap<String , Object>();
		for (int i=0;i<form.getSurvey().getQuestionList().size();i++) {
			com.softech.vu360.lms.model.SurveyQuestion question = form.getSurvey().getQuestionList().get(i).getSurveyQuestionRef();
			List<String> answerList = new ArrayList<String>();
			if (question instanceof MultipleSelectSurveyQuestion) {
				for (com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem answerItem : form.getSurvey().getQuestionList().get(i).getAnswerItems()) {
					if (answerItem.isSelected()) {
						answerList.add(  answerItem.getSurveyAnswerItemRef().getLabel() );
					}
				}
				questionAnswerMap.put(  question.getText() , answerList);

			} else if (question instanceof SingleSelectSurveyQuestion) {

				if(form.getSurvey().getQuestionList().get(i).getSingleSelectAnswerId() !=null){
					for (com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem answerItem : form.getSurvey().getQuestionList().get(i).getAnswerItems()) {
						if (answerItem.getSurveyAnswerItemRef().getId().compareTo(form.getSurvey().getQuestionList().get(i).getSingleSelectAnswerId()) == 0) {
							answerList.add(  answerItem.getSurveyAnswerItemRef().getLabel() );
							break;
						}
					}
				}
				questionAnswerMap.put(  question.getText(),answerList );
			} else {
				answerList.add( form.getSurvey().getQuestionList().get(i).getAnswerText() ); 			 
				questionAnswerMap.put(  question.getText(),answerList );
			}
		}
		return questionAnswerMap;	
	}


	//TODO: REFACTORING, PERFORMANCE-DEFECT
	public SurveyResult addSurveyResult(VU360User user,TakeSurveyForm form){
		Long surveyId = form.getSurveyId();
		com.softech.vu360.lms.model.Survey survey = this.loadForUpdateSurvey(surveyId); //get the Survey object somehow????
		com.softech.vu360.lms.model.Course course = null;
		SurveyResult surveyResult = null ;
		Learner learner=learnerService.loadForUpdateLearner(form.getLearner().getId());
		if( ! form.getSurvey().getSurveyRef().getEvent().equalsIgnoreCase("survey.event.manual") ) {
			course = courseAndCourseGroupService.getCourseById(form.getCourseId()); //get the course object somehow????
			surveyResult = surveyResultRepository.findOneBySurveyeeAndCourseAndSurvey(user,course,survey);
		} else {
			surveyResult = surveyResultRepository.findOneBySurveyeeAndSurvey(user, survey);
		}
		List<SurveyResultAnswer> surveyResultAnswerList=null;
		List<SurveyFlagTemplate> surveyFlagTemplates = surveyFlagTemplateRepository.findBySurveyId(surveyId);
		List<SurveyFlag> surveyFlags = new ArrayList<SurveyFlag>();
		Course surveycourse = courseAndCourseGroupService.loadForUpdateCourse(form.getCourseId());
		boolean sendbackSurvey = false;
		
		if(surveyResult != null){ // to update if answer exists in case of sendback surveys
			surveyResult = surveyResultRepository.findOne(surveyResult.getId());
			surveyResultAnswerList = surveyResult.getAnswers();
			for (int i=0;i<surveyResultAnswerList.size();i++){
				surveyFlags.add(surveyFlagRepository.findOneByAnswerline(surveyResultAnswerList.get(i)));
			}
			sendbackSurvey = true;
		}else{
			surveyResult = new SurveyResult();
			surveyResultAnswerList = new ArrayList<SurveyResultAnswer>();
			surveyResult.setSurvey(survey);
			surveyResult.setSurveyee(user);			
			surveyResult.setCourse(surveycourse);
			surveyFlags= new ArrayList<SurveyFlag>();
		}

		log.debug("courseId="+form.getCourseId());
		
		

		for (int i=0;i<form.getSurvey().getQuestionList().size();i++) {
			com.softech.vu360.lms.model.SurveyQuestion question = form.getSurvey().getQuestionList().get(i).getSurveyQuestionRef();
			List<SurveyAnswerItem> surveyAnswerItems=null;
			SurveyFlagTemplate flagTemplate=null;
			if(surveyFlagTemplates !=null && surveyFlagTemplates.size()>0){
				for(SurveyFlagTemplate surveyFlagTemplate:surveyFlagTemplates){
					if(surveyFlagTemplate.getQuestion().getId().equals(question.getId())){
						surveyAnswerItems=surveyFlagTemplate.getSurveyAnswers();
						flagTemplate=surveyFlagTemplate;
						break;
					}
				}
			}

			if (question instanceof MultipleSelectSurveyQuestion) {
				SurveyResultAnswer surveyResultAnswer = new SurveyResultAnswer();
				List<SurveyAnswerItem> surveyAnswerList = new ArrayList<SurveyAnswerItem>();
				surveyResultAnswer.setQuestion(question);
				surveyResultAnswer.setSurveyAnswerItems(surveyAnswerList);
				surveyResultAnswer.setSurveyResult(surveyResult);
				surveyResultAnswerList.add(surveyResultAnswer);
				for (com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem answerItem : form.getSurvey().getQuestionList().get(i).getAnswerItems()) {
					if (answerItem.isSelected()) {
						surveyAnswerList.add(answerItem.getSurveyAnswerItemRef());
						if (surveyAnswerItems != null) {
							for(SurveyAnswerItem surveyAnswerItem:surveyAnswerItems ){
								if(surveyAnswerItem.compareTo(answerItem.getSurveyAnswerItemRef())==0){
									//	generate flag
									if(sendbackSurvey == false){
										SurveyFlag surveyFlag= new SurveyFlag();
										surveyFlag.setFlagTemplate(flagTemplate);
										surveyFlag.setResponseProvider(user);
										surveyFlag.setTriggerDate(new Date());
										surveyFlag.setAnswerline(surveyResultAnswer);
										surveyFlags.add(surveyFlag);
										break;
									}
									else{
										for(int j=0;j<surveyFlags.size();j++){		
											if(surveyFlags.get(j) != null){
												surveyFlags.get(j).setSentBackToLearner(false);
											}
										}
									}
								}
							}
						}
					}
				}
			} else if (question instanceof SingleSelectSurveyQuestion) {
				SurveyResultAnswer surveyResultAnswer = new SurveyResultAnswer();
				List<SurveyAnswerItem> surveyAnswerList = new ArrayList<SurveyAnswerItem>();
				if(form.getSurvey().getQuestionList().get(i).getSingleSelectAnswerId() !=null){
					surveyResultAnswer.setQuestion(question);
					surveyResultAnswer.setSurveyAnswerItems(surveyAnswerList);
					surveyResultAnswer.setSurveyResult(surveyResult);
					surveyResultAnswerList.add(surveyResultAnswer);
					for (com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem answerItem : form.getSurvey().getQuestionList().get(i).getAnswerItems()) {
						if (answerItem.getSurveyAnswerItemRef().getId().compareTo(form.getSurvey().getQuestionList().get(i).getSingleSelectAnswerId()) == 0) {
							surveyAnswerList.add(answerItem.getSurveyAnswerItemRef());
							if (surveyAnswerItems != null) {
								for(SurveyAnswerItem surveyAnswerItem:surveyAnswerItems ){
									if(surveyAnswerItem.compareTo(answerItem.getSurveyAnswerItemRef())==0){
										//	generate flag
										if(sendbackSurvey == false){
											SurveyFlag surveyFlag= new SurveyFlag();
											surveyFlag.setFlagTemplate(flagTemplate);
											surveyFlag.setResponseProvider(user);
											surveyFlag.setTriggerDate(new Date());
											surveyFlag.setAnswerline(surveyResultAnswer);
											surveyFlags.add(surveyFlag);
											break;
										}
										else{
											for(int j=0;j<surveyFlags.size();j++){	
												if(surveyFlags.get(j) != null){
													surveyFlags.get(j).setSentBackToLearner(false);
												}
											}
										}
									}
								}
							}
							break;
						}
					}
				}
			} else if (question instanceof AggregateSurveyQuestion) {
				if (form.getSurvey().getQuestionList().get(i).getSurveyView().getQuestionList() != null 
						&& form.getSurvey().getQuestionList().get(i).getSurveyView().getQuestionList().size() > 0) {

					for (int j=0; j<form.getSurvey().getQuestionList().get(i).getSurveyView().getQuestionList().size(); j++ ) {
						com.softech.vu360.lms.model.SurveyQuestion q = form.getSurvey().getQuestionList().get(i).getSurveyView().getQuestionList().get(j).getSurveyQuestionRef();
						if (q instanceof MultipleSelectSurveyQuestion) {
							SurveyResultAnswer surveyResultAnswer = new SurveyResultAnswer();
							List<SurveyAnswerItem> surveyAnswerList = new ArrayList<SurveyAnswerItem>();
							surveyResultAnswer.setQuestion(q);
							surveyResultAnswer.setSurveyAnswerItems(surveyAnswerList);
							surveyResultAnswer.setSurveyResult(surveyResult);
							surveyResultAnswerList.add(surveyResultAnswer);
							for (com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem answerItem : form.getSurvey().getQuestionList().get(i).getSurveyView().getQuestionList().get(j).getAnswerItems()) {
								if (answerItem.isSelected()) {
									surveyAnswerList.add(answerItem.getSurveyAnswerItemRef());
									if (surveyAnswerItems != null) {
										for(SurveyAnswerItem surveyAnswerItem:surveyAnswerItems ){
											if(surveyAnswerItem.compareTo(answerItem.getSurveyAnswerItemRef())==0){
												//	generate flag
												SurveyFlag surveyFlag= new SurveyFlag();
												surveyFlag.setFlagTemplate(flagTemplate);
												surveyFlag.setResponseProvider(user);
												surveyFlag.setTriggerDate(new Date());
												surveyFlag.setAnswerline(surveyResultAnswer);
												surveyFlags.add(surveyFlag);
												break;
											}
										}
									}
								}
							}
						} else if (q instanceof SingleSelectSurveyQuestion) {
							SurveyResultAnswer surveyResultAnswer = new SurveyResultAnswer();
							List<SurveyAnswerItem> surveyAnswerList = new ArrayList<SurveyAnswerItem>();
							if(form.getSurvey().getQuestionList().get(i).getSurveyView().getQuestionList().get(j).getSingleSelectAnswerId() !=null){
								surveyResultAnswer.setQuestion(q);
								surveyResultAnswer.setSurveyAnswerItems(surveyAnswerList);
								surveyResultAnswer.setSurveyResult(surveyResult);
								surveyResultAnswerList.add(surveyResultAnswer);
								for (com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem answerItem : form.getSurvey().getQuestionList().get(i).getSurveyView().getQuestionList().get(j).getAnswerItems()) {
									if (answerItem.getSurveyAnswerItemRef().getId().compareTo(form.getSurvey().getQuestionList().get(i).getSurveyView().getQuestionList().get(j).getSingleSelectAnswerId()) == 0) {
										surveyAnswerList.add(answerItem.getSurveyAnswerItemRef());
										if (surveyAnswerItems != null) {
											for(SurveyAnswerItem surveyAnswerItem:surveyAnswerItems ){
												if(surveyAnswerItem.compareTo(answerItem.getSurveyAnswerItemRef())==0){
													//	generate flag
													SurveyFlag surveyFlag= new SurveyFlag();
													surveyFlag.setFlagTemplate(flagTemplate);
													surveyFlag.setResponseProvider(user);
													surveyFlag.setTriggerDate(new Date());
													surveyFlag.setAnswerline(surveyResultAnswer);
													surveyFlags.add(surveyFlag);
													break;
												}
											}
										}
										break;
									}
								}
							}
						}
						
						else if (q instanceof TextBoxSurveyQuestion) {
							SurveyResultAnswer surveyResultAnswer = new SurveyResultAnswer();
							surveyResultAnswer.setQuestion(q);
							surveyResultAnswer.setSurveyAnswerText(form.getSurvey().getQuestionList().get(i).getSurveyView().getQuestionList().get(j).getAnswerText());
							surveyResultAnswer.setSurveyResult(surveyResult);
							surveyResultAnswerList.add(surveyResultAnswer);
						}
					}
				}
			} else {
				SurveyResultAnswer surveyResultAnswer = new SurveyResultAnswer();
				surveyResultAnswer.setQuestion(question);
				surveyResultAnswer.setSurveyAnswerText(form.getSurvey().getQuestionList().get(i).getAnswerText());
				List<com.softech.vu360.lms.web.controller.model.survey.AvailablePersonalInformationfield> personalInfoItems = form.getSurvey().getQuestionList().get(i).getPersonalInfoItems();
				if (personalInfoItems != null && personalInfoItems.size() > 0) {
					for (com.softech.vu360.lms.web.controller.model.survey.AvailablePersonalInformationfield pIField : personalInfoItems) {
						if (pIField.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().compareToIgnoreCase("FIRSTNAME") == 0) {
							learner.getVu360User().setFirstName(pIField.getAnswerText());
						} else if (pIField.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().compareToIgnoreCase("MIDDLENAME") == 0) {
							learner.getVu360User().setMiddleName(pIField.getAnswerText());
						} else if (pIField.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().compareToIgnoreCase("LASTNAME") == 0) {
							learner.getVu360User().setLastName(pIField.getAnswerText());
						} else if (pIField.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().compareToIgnoreCase("STREETADDRESS") == 0) {
							learner.getLearnerProfile().getLearnerAddress().setStreetAddress(pIField.getAnswerText());
						} else if (pIField.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().compareToIgnoreCase("STREETADDRESS2") == 0) {
							learner.getLearnerProfile().getLearnerAddress().setStreetAddress2(pIField.getAnswerText());
						} else if (pIField.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().compareToIgnoreCase("EMAILADDRESS") == 0) {
							learner.getVu360User().setEmailAddress(pIField.getAnswerText());
						} else if (pIField.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().compareToIgnoreCase("OFFICEPHONE") == 0) {
							learner.getLearnerProfile().setOfficePhone(pIField.getAnswerText());
						} else if (pIField.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().compareToIgnoreCase("ZONE") == 0) {
							if( !StringUtils.isBlank(pIField.getAnswerText())){
								learner.getLearnerProfile().getTimeZone().setZone(pIField.getAnswerText());
							}
						}
					}
					learnerService.saveLearner(learner);
				}
				surveyResultAnswer.setSurveyResult(surveyResult);
				surveyResultAnswerList.add(surveyResultAnswer);
			}
		}
		surveyResult.setAnswers(surveyResultAnswerList);
		// updating survey first  
		surveyResult = surveyResultRepository.save(surveyResult);
		for(SurveyFlag surveyFlag: surveyFlags){
			if(sendbackSurvey == false){
				surveyFlagRepository.save(surveyFlag);
				sendFlagTemplateMail(surveyFlag);
			}else{
				if(surveyFlag != null){
				surveyFlag = surveyFlagRepository.findOne(surveyFlag.getId());
					surveyFlagRepository.save(surveyFlag);
					sendFlagTemplateMail(surveyFlag);
				}
			}
		}


		if( ! form.getSurvey().getSurveyRef().getEvent().equalsIgnoreCase("survey.event.manual") )
			statisticsService.surveyFiredEvent(user.getLearner(), surveycourse);
		return surveyResult;
	}

	public void addSuggestedTraining(AddCourseInSuggestedTrainingForm form){
		SuggestedTraining suggestedTraining = form.getSuggTraining();
		List<SurveyAnswerItem> surveyAnswerList = new ArrayList<SurveyAnswerItem>();
		for (int i=0;i<form.getSurveyView().getQuestionList().size();i++) {
			com.softech.vu360.lms.model.SurveyQuestion question = form.getSurveyView().getQuestionList().get(i).getSurveyQuestionRef();
			if (question instanceof MultipleSelectSurveyQuestion) {
				for (com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem answerItem : form.getSurveyView().getQuestionList().get(i).getAnswerItems()) {
					if (answerItem.isSelected()) {
						surveyAnswerList.add(answerItem.getSurveyAnswerItemRef());
					}
				}
			} else if (question instanceof SingleSelectSurveyQuestion) {
				
				if(form.getSurveyView().getQuestionList().get(i).getSingleSelectAnswerId() !=null){
					for (com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem answerItem : form.getSurveyView().getQuestionList().get(i).getAnswerItems()) {
						if (answerItem.getSurveyAnswerItemRef().getId().compareTo(form.getSurveyView().getQuestionList().get(i).getSingleSelectAnswerId()) == 0) {
							surveyAnswerList.add(answerItem.getSurveyAnswerItemRef());
							break;
						}
					}
				}
			}
		}
		// updating survey first  
		suggestedTraining.setResponses(surveyAnswerList);
		suggestedTraining.setCourses(form.getCourseList());
		suggestedTraining.setSurvey(form.getSurvey());
		suggestedTraining = suggestedTrainingRepository.save(suggestedTraining);
	}
	
	public SuggestedTraining addSuggestedTrainingAndCoursesUnderIt(AddCourseInSuggestedTrainingForm form){	// Note: LMS-7612
		SuggestedTraining suggestedTraining = form.getSuggTraining();
		suggestedTraining.setCourses(form.getCourseList());
		suggestedTraining = suggestedTrainingRepository.save(suggestedTraining);
		return suggestedTraining;
	}
	
	public void editSuggestedTraining(SuggestedTrainingForm form){
		SuggestedTraining suggestedTraining = suggestedTrainingRepository.findOne(form.getSuggTraining().getId());
		List<SurveyAnswerItem> surveyAnswerList = new ArrayList<SurveyAnswerItem>();
		if(form.getSurveyView() != null && form.getSurveyView().getQuestionList() != null){
			for (int i=0;i<form.getSurveyView().getQuestionList().size();i++) {
				com.softech.vu360.lms.model.SurveyQuestion question = form.getSurveyView().getQuestionList().get(i).getSurveyQuestionRef();
				if (question instanceof MultipleSelectSurveyQuestion) {
					for (com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem answerItem : form.getSurveyView().getQuestionList().get(i).getAnswerItems()) {
						if (answerItem.isSelected()) {
							surveyAnswerList.add(answerItem.getSurveyAnswerItemRef());
						}
					}
				} else if (question instanceof SingleSelectSurveyQuestion) {
					if(form.getSurveyView().getQuestionList().get(i).getSingleSelectAnswerId() !=null){
						for (com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem answerItem : form.getSurveyView().getQuestionList().get(i).getAnswerItems()) {
							if (answerItem.getSurveyAnswerItemRef().getId().compareTo(form.getSurveyView().getQuestionList().get(i).getSingleSelectAnswerId()) == 0) {
								surveyAnswerList.add(answerItem.getSurveyAnswerItemRef());
								break;
							}
						}
					}
				} 
			}
			suggestedTraining.setResponses(surveyAnswerList);
			suggestedTraining = suggestedTrainingRepository.save(suggestedTraining);
		}
	}

	private void sendFlagTemplateMail(SurveyFlag surveyFlag){
		Brander brander= VU360Branding.getInstance().getBrander("default", new com.softech.vu360.lms.vo.Language());
		String fromAddress =  brander.getBrandElement("lms.email.batchUpload.fromAddress");
		String fromCommonName =  brander.getBrandElement("lms.email.batchUpload.fromCommonName");
		try{
			SendMailService.sendSMTPMessage( surveyFlag.getFlagTemplate().getTo(),
					fromAddress,
					fromCommonName,
					surveyFlag.getFlagTemplate().getSubject(),
					surveyFlag.getFlagTemplate().getMessage());	
		}catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	public SurveyResult addAnonymousSurveyResult( TakeSurveyForm form ) {
		Long surveyId = form.getSurveyId();
		com.softech.vu360.lms.model.Survey survey = getSurveyByID(surveyId);
		SurveyResult surveyResult = new SurveyResult();
		List<SurveyResultAnswer> surveyResultAnswerList = new ArrayList<SurveyResultAnswer>();

		surveyResult.setSurvey(survey);
		surveyResult.setSurveyee(form.getSurveyee());
		Course surveycourse = courseAndCourseGroupService.getCourseById(form.getCourseId());
		surveyResult.setCourse(surveycourse);

		for( int i = 0 ; i < form.getSurvey().getQuestionList().size() ; i++ ) {
			com.softech.vu360.lms.model.SurveyQuestion question = form.getSurvey().getQuestionList().get(i).getSurveyQuestionRef();
			if( question instanceof MultipleSelectSurveyQuestion ) {
				SurveyResultAnswer surveyResultAnswer = new SurveyResultAnswer();
				List<SurveyAnswerItem> surveyAnswerList = new ArrayList<SurveyAnswerItem>();
				for( com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem answerItem : form.getSurvey().getQuestionList().get(i).getAnswerItems() ) {
					if( answerItem.isSelected() ) {
						surveyAnswerList.add(answerItem.getSurveyAnswerItemRef());
					}
				}
				surveyResultAnswer.setQuestion(question);
				surveyResultAnswer.setSurveyAnswerItems(surveyAnswerList);
				surveyResultAnswer.setSurveyResult(surveyResult);
				surveyResultAnswerList.add(surveyResultAnswer);
			} else if ( question instanceof SingleSelectSurveyQuestion ) {
				SurveyResultAnswer surveyResultAnswer = new SurveyResultAnswer();
				List<SurveyAnswerItem> surveyAnswerList = new ArrayList<SurveyAnswerItem>();
				if( form.getSurvey().getQuestionList().get(i).getSingleSelectAnswerId() != null ) {
					for( com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem answerItem : form.getSurvey().getQuestionList().get(i).getAnswerItems() ) {
						if( answerItem.getSurveyAnswerItemRef().getId().compareTo(form.getSurvey().getQuestionList().get(i).getSingleSelectAnswerId()) == 0 ) {
							surveyAnswerList.add(answerItem.getSurveyAnswerItemRef());
							break;
						}
					}
				}
				surveyResultAnswer.setQuestion(question);
				surveyResultAnswer.setSurveyAnswerItems(surveyAnswerList);
				surveyResultAnswer.setSurveyResult(surveyResult);
				surveyResultAnswerList.add(surveyResultAnswer);
			} else {
				SurveyResultAnswer surveyResultAnswer = new SurveyResultAnswer();
				surveyResultAnswer.setQuestion(question);
				surveyResultAnswer.setSurveyAnswerText(form.getSurvey().getQuestionList().get(i).getAnswerText());
				surveyResultAnswer.setSurveyResult(surveyResult);
				surveyResultAnswerList.add(surveyResultAnswer);
			}
		}
		surveyResult.setAnswers(surveyResultAnswerList);
		// updating survey first  
		surveyResult = surveyResultRepository.save(surveyResult);
		return surveyResult;
	}

	public void deleteSurvey(Survey survey) {
		surveyRepository.delete(survey);
	}

	@Transactional
	public void deleteSurveyQuestions(Long surveyQuestionIdArray[]){
		long[] questionIdArray = new long[surveyQuestionIdArray.length];
		long[] customquestionIdArray = new long[surveyQuestionIdArray.length];
		List<Long> customquestionIdList = new ArrayList<Long>();
		for(int i=0; i<surveyQuestionIdArray.length; i++)
			questionIdArray[i] = surveyQuestionIdArray[i].longValue();

		for(Object qId : surveyQuestionIdArray){
			
			SurveyQuestion surveyQuestion=surveyQuestionRepository.findOne(Long.parseLong(qId.toString()));
		    if(surveyQuestion instanceof AggregateSurveyQuestion){
		    	customquestionIdList.add((Long) qId);
		    	
		    }
		}
		if(customquestionIdList.size()>0){
			for(int i=0; i<customquestionIdList.toArray().length; i++)
				customquestionIdArray[i] = new Long(customquestionIdList.get(i));
		//surveyDAO.deleteFromAggregateSurveyQuestion(customuestionIdArray.toArray());
		}
		if(customquestionIdArray.length>0){
			aggregateSurveyQuestionItemRepository.deleteByIdIn(convertPrimitiveLongArrayToLongList(customquestionIdArray));
		}
		surveyQuestionRepository.deleteByIdIn(convertPrimitiveLongArrayToLongList(questionIdArray));

	}


	private List<Long> convertPrimitiveLongArrayToLongList(long[] customquestionIdArray) {
		return Arrays.asList(ArrayUtils.toObject(customquestionIdArray));
	}
	
	@Transactional
	public void deleteSurveyQuestions(Object surveyQuestionIdArray[]){
		long[] questionIdArray = new long[surveyQuestionIdArray.length];
		long[] customquestionIdArray = new long[surveyQuestionIdArray.length];
		List<Long> customquestionIdList = new ArrayList<Long>();
		List<Long> questionIdList = new ArrayList<Long>();
		for(int i=0; i<surveyQuestionIdArray.length; i++)
			questionIdArray[i] = new Long(surveyQuestionIdArray[i].toString());
		
		for(Object qId : surveyQuestionIdArray){
		
			SurveyQuestion surveyQuestion= surveyQuestionRepository.findOne(Long.parseLong(qId.toString()));
		    if(surveyQuestion instanceof AggregateSurveyQuestion){
		    	customquestionIdList.add((Long) qId);
		    	
		    }
		}
		if(customquestionIdList.size()>0){
			for(int i=0; i<customquestionIdList.toArray().length; i++)
				customquestionIdArray[i] = new Long(customquestionIdList.get(i));
		//surveyDAO.deleteFromAggregateSurveyQuestion(customuestionIdArray.toArray());
		}
		for(int k=0;k<customquestionIdArray.length;k++){
			List<AggregateSurveyQuestionItem> aggregateSurveyQuestionItems= aggregateSurveyQuestionItemRepository.findByAggregateSurveyQuestionIdOrderByDisplayOrderAsc(customquestionIdArray[k]);
			if(aggregateSurveyQuestionItems!=null){
			for(AggregateSurveyQuestionItem sggregateSurveyQuestionItem : aggregateSurveyQuestionItems){
				questionIdList.add(sggregateSurveyQuestionItem.getQuestion().getId());
			}
		   }
		}
		long[] tempquestionIdArray = new long[questionIdList.size()];
		for(int i=0; i<questionIdList.toArray().length; i++){
			if(questionIdList.get(i)!=null){
			tempquestionIdArray[i] = new Long(questionIdList.get(i));
			}
		}
		if(customquestionIdArray.length>0){
			aggregateSurveyQuestionItemRepository.deleteByIdIn(convertPrimitiveLongArrayToLongList(customquestionIdArray));
		}
		surveyQuestionRepository.deleteByIdIn(convertPrimitiveLongArrayToLongList(questionIdArray));
		if(tempquestionIdArray.length>0){
			surveyQuestionRepository.deleteByIdIn(convertPrimitiveLongArrayToLongList(tempquestionIdArray));
		}
	}
	
	@Transactional
	public void deleteCustomResponse(Object surveyQuestionIdArray[]){
		List<Long> questionIdArray = new ArrayList<Long>(surveyQuestionIdArray.length);
		for(int i=0; i<surveyQuestionIdArray.length; i++)
			questionIdArray.add(Long.valueOf(surveyQuestionIdArray[i].toString()));
		aggregateSurveyQuestionItemRepository.deleteByQuestionIdIn(questionIdArray);
		surveyQuestionRepository.deleteByIdIn(questionIdArray);
	}

	public Survey saveSurvey(Survey survey){
		return surveyRepository.save(survey);
	}
	
	public Survey addSurvey(Survey survey){
		return surveyRepository.save(survey);
	}

	public Survey getSurveyByID(long id) {
		Survey survey=surveyRepository.findOne(id);
		if(survey==null)
			return null;
		/*Ahsun- Refactoring code to remove concurrent access exception- Start*/
		List<SurveyQuestion> surveyQuestionList= survey.getQuestions();
		List<AggregateSurveyQuestionItem> aggregateQItemList= new ArrayList<AggregateSurveyQuestionItem>();
		List<SurveyQuestion> elementsToRemove= new ArrayList<SurveyQuestion>();
		/*get Aggregate Question Item List list for all survey questions*/
		for(SurveyQuestion question : surveyQuestionList ){
			aggregateQItemList.addAll(this.getAggregateSurveyQuestionItemsByQuestionId(question.getId()));
		}
		/*get common questions (to later remove) from both lists */
		for (AggregateSurveyQuestionItem aggregateSurveyQuestionItem: aggregateQItemList){
			for(SurveyQuestion question : survey.getQuestions() ){
			    if(question.getId() == aggregateSurveyQuestionItem.getQuestion().getId())
				   elementsToRemove.add(question);
			}
		}
		surveyQuestionList.removeAll(elementsToRemove);
		survey.setQuestions(surveyQuestionList);
		/*Ahsun-Refactoring code to remove concurrent access exception- End*/
		
		return survey;
	}

	public List<AggregateSurveyQuestionItem> getAggregateSurveyQuestionItemsByQuestionId(long id){
		/**
		 * Modified By Marium Saud : Wrong method implementation as compared from TopLink
		 */
		//return aggregateSurveyQuestionItemRepository.findByQuestionId(id);
		return aggregateSurveyQuestionItemRepository.findByAggregateSurveyQuestionIdOrderByDisplayOrderAsc(id);
	}

	public Survey   loadForUpdateSurvey(long id){
		return surveyRepository.findOne(id);
	}

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.service.SurveyService#findSurveyByName(java.lang.String)
	 */
	public Survey findSurveyByName(String name) {
		return surveyRepository.findFirstByName(name);
	}

	public void deleteSurveys(long surveyIdArray[]){
		List<Survey> surveys=surveyRepository.findByIdIn(convertPrimitiveLongArrayToLongList(surveyIdArray));
		for (Survey survey : surveys) {
			survey.setIsLocked(true);
		}
		surveyRepository.save(surveys);
	}

	public void deleteSurveyQuestionAnswerItems(List<SurveyAnswerItem> surveyAnswerItems) {
		surveyAnswerItemRepository.delete(surveyAnswerItems);
	}

	public List<SurveyResult> findSurveyResult(long id) {
		return surveyResultRepository.findBySurveyId(id);
	}
	public List<SurveyResult> findAllSurveyResults() {
		return (List<SurveyResult>) surveyResultRepository.findAll();
	}

	public SurveyAnalysis getSurveyResponseAnalysis(Long surveyId) {
		Survey surveyAnalyzed = surveyRepository.findOne(surveyId);
		List<SurveyResult> results = surveyResultRepository.findBySurveyId(surveyId);
		SurveyAnalysis analysis = new SurveyAnalysis(surveyAnalyzed);
		analysis.initialize();

		if(results!=null && results.size()>0){
			for(SurveyResult aresult: results ){
				analysis.analyze(aresult);
			}
		}
		return analysis;
	}
	
	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.service.SurveyService#saveEditedQuestion(long, java.util.List)
	 */
	public Survey saveEditedQuestion(long id, List<SurveyQuestion> surveyQuestionList) {
		Survey survey = this.loadForUpdateSurvey(id);
		List<SurveyQuestion> surveyQns = survey.getQuestions();
		for (int i=0; i<surveyQuestionList.size(); i++) {
			for (int j=0; j<surveyQns.size(); j++) {
				if (surveyQuestionList.get(i).getId().compareTo(surveyQns.get(j).getId()) == 0) {
					deleteSurveyQuestionAnswerItems(surveyQns.get(j).getSurveyAnswers());
					surveyQns.get(j).setText(surveyQuestionList.get(i).getText());
					// Required check box was not saved
					surveyQns.get(j).setRequired(surveyQuestionList.get(i).getRequired());
					if (surveyQuestionList.get(i).getSurveyAnswers() != null && surveyQuestionList.get(i).getSurveyAnswers().size() > 0)
						surveyQns.get(j).setSurveyAnswers(surveyQuestionList.get(i).getSurveyAnswers());
					break;
				}
			}
		}
		survey.setQuestions(surveyQns);
		return survey;
	}

	public List<Survey> getSurveysByCourses(long courseIdArray[],VU360User loggedInUser, String search) {
		List<Survey> surveys = new ArrayList<Survey>();
		Long[] courseIds = new Long[courseIdArray.length];
		for(int index = 0; index < courseIdArray.length; index ++) {
			courseIds[index] = courseIdArray[index];
		}
		if(search.length() > 0) {
			surveys.addAll(surveyRepository.findByCoursesIdInAndIsLockedIsFalseAndStatusAndOwnerIdOrOwnerIdAndNameLike(courseIds, Survey.PUBLISHED, loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getLearner().getCustomer().getDistributor().getId(), search));
		} else {
			surveys.addAll(surveyRepository.findByCoursesIdInAndIsLockedIsFalseAndStatusAndOwnerIdOrOwnerId(courseIds, Survey.PUBLISHED, loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getLearner().getCustomer().getDistributor().getId()));
		}
		return surveys;
	}
	
	public List<Survey> getCurrentSurveyListByCoursesForUser(VU360User loggedInUser){
		List<Survey> surveyList= surveyRepository.getCurrentSurveyListByCoursesForUser(loggedInUser.getLearner().getId(),loggedInUser.getLearner().getCustomer().getId());
		/*if(surveyList==null){
			return null;
		}
		List<Survey> surveyList=new ArrayList<>(aList.size());
		for (Object[] objects : aList) {
			Survey survey=new Survey();
			
			survey.setId(Long.valueOf(objects[0].toString()));//0
			if(objects[1]!=null)survey.setName(objects[1].toString());
			if(objects[2]!=null)survey.setStatus(objects[2].toString());
			if(objects[3]!=null)survey.setLocked(Boolean.valueOf(objects[3].toString()));
			if(objects[4]!=null)survey.setEvent(objects[4].toString());
			if(objects[5]!=null)survey.setShowAll(Boolean.valueOf(objects[5].toString()));
			if(objects[6]!=null)survey.setQuestionsPerPage(Integer.valueOf(objects[6].toString()));
			Owner owner= new Owner();
			if(objects[7]!=null)owner.setId(Long.valueOf(objects[7].toString()));
			//survey.setOWNER_DISTRIBUTORID(objects[8].toString()); not using in entity
			if(objects[9]!=null)owner.setOwnerType(objects[9].toString());
			survey.setOwner(owner);
			if(objects[10]!=null)survey.setRememberPriorResponse(Boolean.valueOf(objects[10].toString()));
			if(objects[11]!=null)survey.setAllowAnonymousResponse(Boolean.valueOf(objects[11].toString()));
			if(objects[12]!=null)survey.setElectronicSignatureRequire(Boolean.valueOf(objects[12].toString()));
			if(objects[13]!=null)survey.setElectronicSignature(objects[13].toString());
			if(objects[14]!=null)survey.setLinkSelected(Boolean.valueOf(objects[14].toString()));
			if(objects[15]!=null)survey.setReadonly(Boolean.valueOf(objects[15].toString()));
			//survey.setSURVEYTYPE(objects[16].toString()); not using in entity
			if(objects[17]!=null)survey.setInspection(Boolean.valueOf(objects[17].toString()));
			surveyList.add(survey);
		}*/
		return surveyList;
	}
	
	public List<Survey> getSurveysByCourses(long courseIdArray[],VU360User loggedInUser){
		return this.getSurveysByCourses(courseIdArray, loggedInUser, BLANK_SEARCH );

	}

	public List<Survey> getCompletedSurveysByUser(VU360User loggedInUser, String search){
		List<LearnerEnrollment> enrollments = entitlementService.getLearnerEnrollmentsByLearner(loggedInUser.getLearner(), null);
		List<Survey> completedSurveyForCourse = getCompletedSurveysByLearnerEnrollments(loggedInUser, search, enrollments);
		return completedSurveyForCourse;
	}
	
	public List<Survey> getCompletedSurveysByLearnerEnrollments(VU360User loggedInUser, String search, List<LearnerEnrollment> enrollments){
		List<SurveyResult> surveyresults = surveyResultRepository.findBySurveyee(loggedInUser);
		List<Survey> completedSurvey = new ArrayList<Survey>();
		List<Survey> completedSurveyForCourse = new ArrayList<Survey>();
		for(SurveyResult mySurveyResult : surveyresults){
			completedSurvey.add(mySurveyResult.getSurvey());
		}

		/*
		 * Lets see whether this survey is completed for all course associated with Survey
		 */
		for(Survey completedSurveyitem : completedSurvey){
			boolean incomplete = false;
			for(Course surveyCourse :completedSurveyitem.getCourses()){
				for(LearnerEnrollment enrollment : enrollments){
					if(surveyCourse.getId().equals(enrollment.getCourse().getId())){
						//Check Survey result has been posted for this course
						SurveyResult result = surveyResultRepository.findOneBySurveyeeAndCourseAndSurvey(loggedInUser, surveyCourse, completedSurveyitem);
						if(result == null){
							incomplete = true;
							break;
						} else if (result != null && result.getSurvey().isRememberPriorResponse()) {
							incomplete = true;
							break;
						}
					}
				}
			}
			if(!incomplete){
				completedSurveyForCourse.add(completedSurveyitem);
			}
		}
		return completedSurveyForCourse;
	}

	public List<Survey> getCompletedSurveysByUser(VU360User loggedInUser){
		List<SurveyResult> surveyresults = surveyResultRepository.findBySurveyee(loggedInUser);
		List<Survey> completedSurvey = new ArrayList<Survey>();
		List<Survey> completedSurveyForCourse = new ArrayList<Survey>();
		for(SurveyResult mySurveyResult : surveyresults){
			completedSurvey.add(mySurveyResult.getSurvey());
		}
		/*
		 * Lets see whether this survey is completed for all course associated with Survey
		 */
		List<LearnerEnrollment> enrollments = entitlementService.getLearnerEnrollmentsByLearner(loggedInUser.getLearner(), null);
		for(Survey completedSurveyitem : completedSurvey){
			boolean incomplete = false;
			for(Course surveyCourse :completedSurveyitem.getCourses()){
				for(LearnerEnrollment enrollment : enrollments){
					if(surveyCourse.getId().equals(enrollment.getCourse().getId())){
						//Check Survey result has been posted for this course
						SurveyResult result = surveyResultRepository.findOneBySurveyeeAndCourseAndSurvey(loggedInUser, surveyCourse, completedSurveyitem);
						if(result == null){
							incomplete = true;
							break;
						}
					}
				}
			}
			if(!incomplete){
				completedSurveyForCourse.add(completedSurveyitem);
			}
		}


		return completedSurveyForCourse;
	}

	public SurveyResult getSurveyResultByUserAndCourse(VU360User loggedInUser,Course course, Survey survey){
		return surveyResultRepository.findOneBySurveyeeAndCourseAndSurvey(loggedInUser, course, survey);
	}

	public SurveyResult getSurveyResultByUserAndCourse(VU360User loggedInUser,Course course, Survey survey, String search){
		return surveyResultRepository.findOneBySurveyeeAndCourseAndSurveyAndSurveyNameLike(loggedInUser, course, survey, "%"+search+"%");
	}

	public SurveyResult getSurveyResultByLearnerAndSurvey(VU360User loggedInUser, Survey survey){
		return surveyResultRepository.findOneBySurveyeeAndSurvey(loggedInUser, survey);
	}
	
	public SurveyResultAnswer getSurveyResultByLearnerAndSurveyAndQuestion(Long questionId, Long surveyResultId){
		return surveyResultAnswerRepository.findByQuestionIdAndSurveyResultId(questionId, surveyResultId);
	}

	public SurveyResult getSurveyResultByLearnerAndSurvey(VU360User loggedInUser, Survey survey, String search)	{
		if(!StringUtils.isBlank(search)) {
			return surveyResultRepository.findOneBySurveyeeAndCourseIsNullAndSurveyAndSurveyNameIsLike(loggedInUser, survey, search);
		} else {
			return surveyResultRepository.findOneBySurveyeeAndCourseIsNullAndSurvey(loggedInUser, survey);
		}
	}
	
	// [2/15/2011] LMS-8972 :: Course Evaluation Criteria is appearing incorrectly
	@Override
	public boolean isCourseEvaluationCompleted ( Long learnerEnrollmentId ) {
		List<SurveyResult> surveyResults = surveyResultRepository.findByLearningSessionLearnerEnrollmentId( learnerEnrollmentId );
		return (CollectionUtils.isNotEmpty(surveyResults));		
	}

	public List<Survey> getDueSurveysByLearnerEnrollment(long learnerEnrollmentId, String search){

		LearnerEnrollment enrollment=entitlementService.getLearnerEnrollmentById(learnerEnrollmentId);

		List<Survey> mySurveys = getSurveysByCourses(new long[]{enrollment.getCourse().getId()},enrollment.getLearner().getVu360User(),search);
		List<Survey> completedSurveys = getCompletedSurveysByUser(enrollment.getLearner().getVu360User(), search);
		List<Survey> dueSurveys = new ArrayList<Survey>();
		boolean found=false;
		for(Survey mySurvey:mySurveys){
			found=false;
			for(Survey completedSurvey:completedSurveys){
				if(mySurvey.getId().longValue()==completedSurvey.getId().longValue()){
					found=true;
					break;
				}
			}
			if(!found){
				dueSurveys.add(mySurvey);
			}
		}
		return dueSurveys;
	}
	public List<Survey> getDueSurveysByLearnerEnrollment(long learnerEnrollmentId){

		LearnerEnrollment enrollment=entitlementService.getLearnerEnrollmentById(learnerEnrollmentId);

		List<Survey> mySurveys = getSurveysByCourses(new long[]{enrollment.getCourse().getId()},enrollment.getLearner().getVu360User());
		List<Survey> completedSurveys = getCompletedSurveysByUser(enrollment.getLearner().getVu360User());
		List<Survey> dueSurveys = new ArrayList<Survey>();
		boolean found=false;
		for(Survey mySurvey:mySurveys){
			found=false;
			for(Survey completedSurvey:completedSurveys){
				if(mySurvey.getId().longValue()==completedSurvey.getId().longValue()){
					found=true;
					break;
				}
			}
			if(!found){
				dueSurveys.add(mySurvey);
			}
		}
		return dueSurveys;
	}

	public List<Survey> getDueSurveysByUser(VU360User loggedInUser){
		Learner learner = loggedInUser.getLearner();
		
		List<Survey> myDueSurveys = getCurrentSurveyListByCoursesForUser(loggedInUser);
		
		List <Learner>learnerInList = new ArrayList<Learner>(1);
		learnerInList.add(learner);

		List<Survey> manualSurveyList = surveyRepository.getNonFinishedManualSurveyAssignmentOfLearners(learner.getId(), learner.getCustomer().getId(), loggedInUser.getId());

		if(manualSurveyList != null && !manualSurveyList.isEmpty()){
			myDueSurveys.addAll(manualSurveyList);
		}
		return myDueSurveys;
	}
	
	@Override
	public List<Survey> getDueSurveysByUser(com.softech.vu360.lms.vo.VU360User loggedInUser){
		com.softech.vu360.lms.vo.Learner learner = loggedInUser.getLearner();
		
		//List<Survey> myDueSurveys = getCurrentSurveyListByCoursesForUser(loggedInUser);
		List<Survey> myDueSurveys= surveyRepository.getCurrentSurveyListByCoursesForUser(loggedInUser.getLearner().getId(),loggedInUser.getLearner().getCustomer().getId());
		
/*		List <Learner>learnerInList = new ArrayList<Learner>(1);
		learnerInList.add(learner);*/

		List<Survey> manualSurveyList = surveyRepository.getNonFinishedManualSurveyAssignmentOfLearners(learner.getId(), learner.getCustomer().getId(), loggedInUser.getId());

		if(manualSurveyList != null && !manualSurveyList.isEmpty()){
			myDueSurveys.addAll(manualSurveyList);
		}
		return myDueSurveys;
	}

	
	@Override
	public List<Survey> getOverDueSurveysByUser(VU360User loggedInUser){
		Learner learner = loggedInUser.getLearner();

		List<Survey> mySurveys = new ArrayList<Survey>();

		/*Get Surveys Not Linked to Courses*/
		List <Learner>learnerInList = new ArrayList<Learner>(1);
		learnerInList.add(learner);
		List <SurveyLearner> learnerSurveyList = this.getSurveyAssignmentOfLearners(learnerInList); 
		for(SurveyLearner lnrSurvey:learnerSurveyList)
			if(getSurveyResultByLearnerAndSurvey(loggedInUser, lnrSurvey.getSurvey()) == null){//unAnswered Survey
			   if(lnrSurvey.getEndDate().before(Calendar.getInstance().getTime())){
				   mySurveys.add(lnrSurvey.getSurvey());
			   }
			}  

		return mySurveys;
	}
	
	public Map<String,Integer> getTotalSurveyByUser(VU360User loggedInUser, String search){
		Learner learner = loggedInUser.getLearner();
		List<LearnerEnrollment> enrollments = entitlementService.getLearnerEnrollmentsForLearner(learner);
		long courseId[] = new long[enrollments.size()];
		int arraySize = 0;
		for(LearnerEnrollment myenrollment:enrollments){
			courseId[arraySize++] = myenrollment.getCourse().getId();
		}

		List<Survey> mySurveys = new ArrayList<Survey>();
		if(courseId.length > 0 ){
			mySurveys = getSurveysByCourses(courseId,loggedInUser,search);
		}
		/*
		 * As per requirement Total survey should be weighted sum of course.
		 * A survey may be associated with multiple course and vice versa.
		 * mySurveys provides a list of DISTICT surveys 
		 * need to get count of course wise survey.
		 */
		Map<String,Integer> surveyComplete = new HashMap<String,Integer>();
		int totalSurveyCount = 0;
		int completedSurveyCount = 0;
		for(int courseSize=0; courseSize<courseId.length;courseSize++){
			for(Survey survey : mySurveys){
				for(Course surveycourse:survey.getCourses()){
					if(courseId[courseSize]== surveycourse.getId().longValue()){
						totalSurveyCount ++;
						if(this.getSurveyResultByUserAndCourse(loggedInUser, surveycourse, survey)!=null){
							completedSurveyCount ++;
						}
					}
				}
			}

		}
		surveyComplete.put("TotalCount", totalSurveyCount);
		surveyComplete.put("CompletedCount", completedSurveyCount);
		return surveyComplete;

	}

	public Map<String,Integer> getTotalSurveyByUser(VU360User loggedInUser){
		Learner learner = loggedInUser.getLearner();
		List<LearnerEnrollment> enrollments = entitlementService.getLearnerEnrollmentsForLearner(learner);
		long courseId[] = new long[enrollments.size()];
		int arraySize = 0;
		for(LearnerEnrollment myenrollment:enrollments){
			courseId[arraySize++] = myenrollment.getCourse().getId();
		}

		List<Survey> mySurveys = new ArrayList<Survey>();
		if(courseId.length > 0 ){
			mySurveys = getSurveysByCourses(courseId,loggedInUser);
		}
		/*
		 * As per requirement Total survey should be weighted sum of course.
		 * A survey may be associated with multiple course and vice versa.
		 * mySurveys provides a list of DISTICT surveys 
		 * need to get count of course wise survey.
		 */
		Map<String,Integer> surveyComplete = new HashMap<String,Integer>();
		int totalSurveyCount = 0;
		int completedSurveyCount = 0;
		for(int courseSize=0; courseSize<courseId.length;courseSize++){
			for(Survey survey : mySurveys){
				for(Course surveycourse:survey.getCourses()){
					if(courseId[courseSize]== surveycourse.getId().longValue()){
						totalSurveyCount ++;
						if(this.getSurveyResultByUserAndCourse(loggedInUser, surveycourse, survey)!=null){
							completedSurveyCount ++;
						}
					}
				}
			}

		}
		surveyComplete.put("TotalCount", totalSurveyCount);
		surveyComplete.put("CompletedCount", completedSurveyCount);
		return surveyComplete;

	}

	public List<Survey> getDueSurveysByLearningSession(String learningSessionId, String search){
		LearningSession learningSession = statisticsService.getLearningSessionByLearningSessionId(learningSessionId);
		LearnerEnrollment enrollment = null;
		if(learningSession !=null)
			enrollment = entitlementService.getLearnerEnrollmentById(learningSession.getEnrollmentId());

		List<Survey> myDueSurveys = getDueSurveysByUser(enrollment.getLearner().getVu360User());
		
		return myDueSurveys;
	}


	public List<Survey> getDueSurveysByLearningSession(String learningSessionId){
		LearningSession learningSession = statisticsService.getLearningSessionByLearningSessionId(learningSessionId);
		return getDueSurveysByLearningSession(learningSession);
	}

	public List<Survey> getDueSurveysByLearningSession(LearningSession learningSession){
		LearnerEnrollment enrollment = null;
		if(learningSession !=null)
			enrollment = entitlementService.getLearnerEnrollmentById(learningSession.getEnrollmentId());

		List<Survey> mySurveys = new ArrayList<Survey>();
		List<Survey> myDueSurveys = new ArrayList<Survey>();
		mySurveys = getDueSurveysByUser(enrollment.getLearner().getVu360User());
		/*As per logic 
		 * the LCMS to present the survey(s) after the current learning session has ended.
				Surveys configured	Show Survey at End of Session
			Start	Middle	End	0 %	25 %	50 %	75 %	100 %
			On		Off		Off	x	 	 	 	
			On		On		Off	x	 		x		x	 
			On		On		On	x	 		x		x	
			Off		On		On	 	 		x		x	
			Off		Off		On	 	 	 	 	
			On		Off		On	x	 	 	 	
			Off		On		Off	 	 		x		x	 
						(PercentComplete at Launch)	

		 */
		
		/* LMS-16379
		 * The below line of code is used to retrieve the updated record for LearnerCourseStatistics. 
		 * Since retrieving from enrollment is not fetching the updated value for percentComplete field. 
		 */
		LearnerCourseStatistics statUpdated = statisticsService.getLearnerCourseStatisticsById(enrollment.getCourseStatistics().getId());
		System.out.println("~~~ PercentComplete is .........."+statUpdated.getPercentComplete());
		//Double percentcompleted = enrollment.getCourseStatistics().getPercentComplete();
		Double percentcompleted = statUpdated.getPercentComplete();
		Boolean courseStartSurveyEvent = true;
		Boolean courseMiddleSurveyEvent = false;
		Boolean courseEndSurveyEvent = false;
		if(percentcompleted >=50 && percentcompleted<100){
			courseMiddleSurveyEvent =true;
		}else if(percentcompleted >=100){
			courseMiddleSurveyEvent =true;
			courseEndSurveyEvent = true;
		}
		if(mySurveys !=null && mySurveys.size()>0)
			for(Survey survey : mySurveys){
				if(this.getSurveyResultByUserAndCourse(enrollment.getLearner().getVu360User(), enrollment.getCourse(), survey)==null){
					System.out.println("~~~ courseStartSurveyEvent:"+courseStartSurveyEvent+" and survey.getEvent():"+survey.getEvent());
					if(courseStartSurveyEvent && survey.getEvent().equalsIgnoreCase(Survey.SURVEY_EVENT_BEFORE_COURSE_START_CODE)){
						myDueSurveys.add(survey);
						continue;
					}
					if(courseMiddleSurveyEvent && survey.getEvent().equalsIgnoreCase(Survey.SURVEY_EVENT_MIDDLE_COURSE_CODE)){
						myDueSurveys.add(survey);
						continue;
					}
					if(courseEndSurveyEvent && survey.getEvent().equalsIgnoreCase(Survey.SURVEY_EVENT_AFTER_COURSE_ENDS_CODE)){
						myDueSurveys.add(survey);
						continue;
					}

				}
			}
		return myDueSurveys;
	}


	public List<Survey> getSurveyByName(SurveyOwner surveyowner, String surveyName, String surveyStatus, String isLocked,String readonly) {
		return surveyRepository.getSurveysByOwnerAndSurveyName(surveyowner, surveyName, surveyStatus, isLocked, readonly,null);
	}
	
	public List<Survey> getManualSurveyByName(SurveyOwner surveyowner, String surveyName, String surveyStatus, String isLocked,String readonly) {
		return surveyRepository.getSurveysByOwnerAndSurveyName(surveyowner, surveyName, surveyStatus, isLocked, readonly,Survey.SURVEY_EVENT_MANUAL_CODE);
/*	DAO code :	
 * List<Survey> surveys = null;
		if(surveyowner.getOwnerType().equalsIgnoreCase("CUSTOMER")){
			Customer customer=(Customer)surveyowner;
			whereClause = builder.getField("OWNER_ID").equal(customer.getId()).and(builder.getField("OWNER_TYPE").equal(customer.getOwnerType()));
			whereClause = whereClause.or(builder.getField("OWNER_ID").equal(customer.getDistributor().getId()).and(builder.getField("OWNER_TYPE").equal(customer.getDistributor().getOwnerType())));
			if(!isLocked.equalsIgnoreCase(Survey.RETIRE_SURVEY.All.toString())){
				if(isLocked.equalsIgnoreCase(Survey.RETIRE_SURVEY.Yes.toString()))
					whereClause=whereClause.and(builder.get("isLocked").equal(true));
				else if(isLocked.equalsIgnoreCase(Survey.RETIRE_SURVEY.No.toString()))
					whereClause=whereClause.and(builder.get("isLocked").equal(false));
			}
			if(!readonly.equalsIgnoreCase(Survey.Editable.All.toString())){
				if(readonly.equalsIgnoreCase(Survey.Editable.Yes.toString()))
					whereClause=whereClause.and(builder.get("readonly").equal(false));
				else if(readonly.equalsIgnoreCase(Survey.Editable.No.toString()))
					whereClause=whereClause.and(builder.get("readonly").equal(true));
			}
			whereClause=whereClause.and(builder.get("name").likeIgnoreCase("%"+surveyName+"%"));
			whereClause=whereClause.and(builder.get("event").equal(Survey.SURVEY_EVENT_MANUAL_CODE));
			if (!surveyStatus.isEmpty()) {
				whereClause=whereClause.and(builder.get("status").equalsIgnoreCase(surveyStatus));
			}
		}else{
			if (surveyStatus.isEmpty()) {
				whereClause = builder.getField("OWNER_ID").equal(surveyowner.getId());
				whereClause=whereClause.and(builder.getField("OWNER_TYPE").equal(surveyowner.getOwnerType()));
			}else {
				whereClause=whereClause.and(builder.get("status").equalsIgnoreCase(surveyStatus));
			}
		}
*/
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


	public StatisticsService getStatisticsService() {
		return statisticsService;
	}

	public void setStatisticsService(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	/**
	 * @return the courseAndCourseGroupService
	 */
	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	/**
	 * @param courseAndCourseGroupService the courseAndCourseGroupService to set
	 */
	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	public List<Survey> findManualSurveys(String surveyName ,String surveyStatus ,String  retiredSurvey, Customer customer  ,int intLimit ){
		Long[] ids=new Long[2];
		ids[0]=customer.getId();
		ids[1]=customer.getDistributor().getId();
		List<Survey> allSurveys = surveyRepository.findManualSurveys(surveyName , surveyStatus ,  retiredSurvey,ids  , intLimit );
		return allSurveys;
	}

	public SurveyVO saveSurveyAssignment(SaveSurveyParam saveSurveyParam) throws ParseException {
		SurveyVO surveyVO = getSurveyData(saveSurveyParam); 
		//surveyLearnerRepository.save(surveyVO.getLearnerSurveyList()) ;
		List<SurveyLearner> surveyLearners = surveyVO.getLearnerSurveyList();
		for (Iterator iterator = surveyLearners.iterator(); iterator.hasNext();) {
			SurveyLearner surveyLearner = (SurveyLearner) iterator.next();
			surveyLearnerRepository.saveSL(surveyLearner) ;
			
		}
		return surveyVO;
	}

	private SurveyVO getSurveyData(SaveSurveyParam saveSurveyParam) throws ParseException {
		SurveyVO surveyVO = new SurveyVO();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		List<Survey> selectedSurveys = new ArrayList<Survey>();
		List<SurveyLearner> learnerSurveyList = new ArrayList<SurveyLearner>();

		for ( SurveyItem surveyItem : saveSurveyParam.getSurveyItemList()){
			if( surveyItem.isSelected() )
				selectedSurveys.add(surveyItem.getSurvey()) ;
		}

		for(Learner learner : saveSurveyParam.getLearnersToBeAssigned()){
			for( SurveyItem  surveyItem : saveSurveyParam.getSurveyItemList()){
				if( ! surveyItem.isSelected() )
					continue;
				SurveyLearner learnerSurvey = new SurveyLearner();
				learnerSurvey.setLearner(learner);
				learnerSurvey.setSurvey( surveyItem.getSurvey());
				learnerSurvey.setNotifyOnCompletion(saveSurveyParam.isNotifyMeOnLearnerSurveyCompletions());
				learnerSurvey.setSurveyNeverExpired(saveSurveyParam.isOpenSurvey());
				learnerSurvey.setUserToNotify(saveSurveyParam.getUser()); 

				if( saveSurveyParam.isModifyAllSurveys() ){

					Date surveyStartDate = formatter.parse(saveSurveyParam.getStartDate());
					Date surveyEndDate = formatter.parse(saveSurveyParam.getEndDate());

					learnerSurvey.setEndDate( surveyEndDate);
					learnerSurvey.setStartDate(surveyStartDate);

				}
				else if( saveSurveyParam.isOpenSurvey() ){
					learnerSurvey.setStartDate(new Date()); // start date will be today's date
					learnerSurvey.setSurveyNeverExpired(true);
				}	        	
				else if( ! saveSurveyParam.isModifyAllSurveys() ){
					Date surveyStartDate = formatter.parse(surveyItem.getSurveyStartDate());
					Date surveyEndDate = formatter.parse(surveyItem.getSurveyEndDate());

					learnerSurvey.setEndDate( surveyEndDate);
					learnerSurvey.setStartDate(surveyStartDate);
				}

				learnerSurveyList.add(learnerSurvey);
			}   
		}
		surveyVO.setSurveyList(selectedSurveys );
		surveyVO.setLearnerList( saveSurveyParam.getLearnersToBeAssigned() );
		surveyVO.setLearnerSurveyList(learnerSurveyList);
		return surveyVO;
	}
	
	public List<SurveyLearner> getSurveyAssignmentOfLearners( List<Learner> learners ){
		List<SurveyLearner> surveyLearnerList = surveyLearnerRepository.findByLearnerInAndSurveyIsLockedIsFalse(learners);
		return surveyLearnerList ;
	}
	
	public List<SurveyLearner> getLearnerSurveys(VU360User user){
		List<Learner> learners = new ArrayList<Learner>();
		List<SurveyLearner> surveyLearnerList = null;
		learners.add( user.getLearner() );
		surveyLearnerList = surveyLearnerRepository.findByLearnerInAndSurveyIsLockedIsFalse( learners );
		return surveyLearnerList ;
	}

	public List<SurveyLearner> getLearnerSurveys(VU360User user, String search){
		List<Learner> learners = new ArrayList<Learner>();
		List<SurveyLearner> surveyLearnerList = null;
		learners.add( user.getLearner() );
		if(StringUtils.isBlank(search))
		{
			surveyLearnerList = surveyLearnerRepository.findByLearnerInAndSurveyIsLockedIsFalse(learners);
		}else{
			surveyLearnerList = surveyLearnerRepository.findByLearnerInAndSurveyIsLockedIsFalseAndSurveyNameLike( learners, "%"+search+"%" );
		}

		return surveyLearnerList ;
	}

	public VU360User getManagerOfSurveyLearner(VU360User user , Survey survey){
		VU360User manager = null;

		List<SurveyLearner> surveyLearnerList = surveyLearnerRepository.findByLearnerAndSurvey(user.getLearner(), survey);

		if( surveyLearnerList != null && surveyLearnerList.size() > 0 ){
			manager = surveyLearnerList.get(0).getUserToNotify();
		}
		return manager ;
	}
	
	public SurveyQuestion addSurveyQuestion(SurveyQuestion surveyQuestion)
	{
		return surveyQuestionRepository.save(surveyQuestion);
	}

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.service.SurveyService#getFlagByID(long)
	 */
	public SurveyFlagTemplate getFlagTemplateByID(long id) {
		return surveyFlagTemplateRepository.findOne(id);
	}

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.service.SurveyService#findAllFlag(java.lang.String)
	 */
	public List<SurveyFlagTemplate> findAllFlagTemplate(String flagName, long surveryId) {
		return surveyFlagTemplateRepository.findByFlagNameIgnoreCaseLikeAndSurveyId("%" + flagName + "%",surveryId);
	}

	public void saveSurveyLinkList(List<SurveyLink> surveyLinks){

		surveyLinkRepository.save(surveyLinks);

	}

	public Set<SurveyResult> getNotReviewedFlaggedSurveyResult(SurveyOwner surveyowner){

		Set<SurveyResult> notReviewedFlaggedSurveyResult = new HashSet<SurveyResult>();
		List<SurveyFlag> notReviewedFlags = surveyFlagRepository.findByFlagTemplateSurveyOwner(surveyowner);
		for(SurveyFlag notReviewedFlag : notReviewedFlags){
			notReviewedFlaggedSurveyResult.add(surveyResultRepository.findOne(notReviewedFlag.getAnswerline().getSurveyResult().getId()));
		}
		return notReviewedFlaggedSurveyResult;
	}
	public List<SurveyAnswerItem>getSurveyAnswerItemByIdArray(long []surveyAnswerItemArray){
		return surveyAnswerItemRepository.findByIdIn(convertPrimitiveLongArrayToLongList(surveyAnswerItemArray));
	}
	public SurveyResult getSurveyResultBySurveyResultID(long id){
		return surveyResultRepository.findOne(id);
	}
	public SurveyQuestion getSurveyQuestionById(long id){
		return surveyQuestionRepository.findOne(id);
	}
	public SurveyLink saveSurveyLink(SurveyLink surveyLink){
		return surveyLinkRepository.save(surveyLink);
	}

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.service.SurveyService#saveAggregateSurveyQuestionItem(java.util.List)
	 */
	public void saveAggregateSurveyQuestionItem(List<AggregateSurveyQuestionItem> aggregateSurveyQuestions) {
		aggregateSurveyQuestionItemRepository.save(aggregateSurveyQuestions);
	}

	public SurveyFlagTemplate saveSurveyFlagTemplate(SurveyFlagTemplate surveyFlagTemplate){
		return surveyFlagTemplateRepository.save(surveyFlagTemplate);
	}

	public List<SurveyLink>getSurveyLinksById(long surveyId){
		return surveyLinkRepository.findBySurveyId(surveyId);
	}
	public void deleteSurveyFlagTemplates(long []idArray){
		Long[] ids = new Long[idArray.length];
		int index = 0;
		for(long id: idArray) {
			ids[index++] = id;
		}
		surveyFlagTemplateRepository.deleteByIdIn(ids);
	}

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.service.SurveyService#saveSurveyResult(com.softech.vu360.lms.model.SurveyResult)
	 */
	public SurveyResult saveSurveyResult(SurveyResult surveyResult) {
		return surveyResultRepository.save(surveyResult);
	}

	public List<SuggestedTraining> getSuggestedTrainingsBySurveyID(long id){
		return suggestedTrainingRepository.findBySurveyId(id);

	}
	public SuggestedTraining loadForUpdateSuggestedTraining(long id){
		return suggestedTrainingRepository.findOne(id);
	}

	public SurveyResult loadForUpdateSurveyResult(long id){
		return surveyResultRepository.findOne(id);
	}
	
	public SurveyResultAnswer loadForUpdateSurveyResultAnswer(long id){
		return surveyResultAnswerRepository.findById(id);
	}
	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.service.SurveyService#getSuggestedTrainingsBySurveyIDs(long[])
	 */
	public List<SuggestedTraining> getSuggestedTrainingsBySurveyIDs(long[] idArray) {
		List<SuggestedTraining> suggestedTrainingList = new ArrayList<SuggestedTraining>();
		for (int i=0; i<idArray.length; i++) {
			List<SuggestedTraining> suggTrainings = suggestedTrainingRepository.findBySurveyId(idArray[i]);
			suggestedTrainingList.addAll(suggTrainings);
		}

		return suggestedTrainingList;
	}
	
	public List<AvailablePersonalInformationfieldItem> getAllAvailablePersonalInformationfields(){
		
		return (List<AvailablePersonalInformationfieldItem>) availablePersonalInformationfieldItemRepository.findAll();
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
	
	// LMS 7617
	public Set<SurveyFlagAndSurveyResult> getNotReviewedFlaggedSurveyResult2(SurveyOwner surveyowner,String surveyName,String status,String firstName,String lastName,String userName){
		Set<SurveyFlagAndSurveyResult> notReviewedFlaggedSurveyResults = new HashSet<SurveyFlagAndSurveyResult>();
/*		List<SurveyFlag> flags = null;
		if(status.equalsIgnoreCase(SurveyFlag.REVIEWED)) {
//			flags = surveyFlagRepository.findByIsReviewedIsTrueAndFlagTemplateSurveySurveyOwnerIdAndOwnerType((Long)surveyowner.getId(), surveyowner.getOwnerType());
		} else if (status.equalsIgnoreCase(SurveyFlag.SENTBACKTOLEARNER)) {
//			flags = surveyFlagRepository.findByIsSentBackToLearnerIsTrueAndFlagTemplateSurveyOwnerIdAndOwnerType((Long)surveyowner.getId(), surveyowner.getOwnerType());
		} else if(status.equalsIgnoreCase(SurveyFlag.AWAITINGRESPONSE)) {
//			flags = surveyFlagRepository.findByIsReviewedIsFalseAndIsSentBackToLearnerIsFalseAndFlagTemplateSurveyOwnerIdAndOwnerType((Long)surveyowner.getId(), surveyowner.getOwnerType());
		}
		
		for(SurveyFlag flag : flags){
			SurveyResult surveyResult = flag.getAnswerline().getSurveyResult();
			Survey survey = surveyResult.getSurvey();
			VU360User user = surveyResult.getSurveyee();
			if(!(survey.getName().equalsIgnoreCase(surveyName)
					&& user.getFirstName().equalsIgnoreCase(firstName)
					&& user.getLastName().equalsIgnoreCase(lastName)
					&& user.getUsername().equalsIgnoreCase(userName))) {
				surveyResult = null;
			}
			if(surveyResult != null){
				SurveyFlagAndSurveyResult surveyFlagAndSurveyResult = new SurveyFlagAndSurveyResult();
				surveyFlagAndSurveyResult.setSurveyResult(surveyResult);
				surveyFlagAndSurveyResult.setSurveyFlag(flag);
				notReviewedFlaggedSurveyResults.add(surveyFlagAndSurveyResult);
			}
		}
*/		
		return notReviewedFlaggedSurveyResults;
	}
	
	public SurveyFlag saveSurveyFlag(SurveyFlag surveyFlag){
		return surveyFlagRepository.save(surveyFlag);
	}
	
//	//LMS : 7617
//	public SurveyResult getSurveyResultBySurveyResultIDName(long id,String surveyName,String status,String firstName,String lastName,String userName){
//		return surveyDAO.getSurveyResultBySurveyResultIDName(id, surveyName, status, firstName, lastName, userName);
//	}
	
	//LMS : 7617
	public SurveyFlag loadSurveyFlagForUpdate(long id){
		return surveyFlagRepository.findOne(id);
	}
	
	//LMS : 7617
	public List<SurveyResult> getSendBackSurveyResultsByLoggedInUser(VU360User loggenInUser){
		List<SurveyFlag> surveyFlags = surveyFlagRepository.findByResponseProviderAndSentBackToLearnerIsTrue(loggenInUser);
		List<SurveyResult> sendBackSurveyResults = new ArrayList<SurveyResult>();
		for(int i=0;i<surveyFlags.size();i++){
			sendBackSurveyResults.add(surveyFlags.get(i).getAnswerline().getSurveyResult());
		}
		return sendBackSurveyResults;
	}
	
	//[Note: LMS: 7614]
	public void deleteSuggestedTraining(SuggestedTraining suggestedTraining){
		suggestedTrainingRepository.delete(suggestedTraining);
	}
	
	public SuggestedTraining loadSuggestedTrainingForUpdate(Long suggestedTrainingId){
		return suggestedTrainingRepository.findOne(suggestedTrainingId);
	}
	
	public SuggestedTraining saveSuggestedTraining(SuggestedTraining suggestedTraining){
		return (SuggestedTraining)suggestedTrainingRepository.save(suggestedTraining);
	}
	
	//************************************* ALERT MODULE *******************************************
	public Alert addAlert(Alert alert){
		return alertRepository.save(alert);
	}
	
	public Alert getAlertByID(long id){
		return alertRepository.findOne(id);
	}
	
	public List<Alert> findAlert(Long loggedInUserId , String alertName){
		return alertRepository.findByCreatedByIdAndAlertNameIgnoreCaseLikeAndIsDeleteIsFalse(loggedInUserId , "%"+alertName+"%");
	}
	
	public void deleteAlerts(long alertIdArray[]){
		List<Long> ids = toLongList(alertIdArray);
		List<Alert> alerts = (List<Alert>) alertRepository.findAll(ids);
		for(Alert alert: alerts) {
			alert.setIsDelete(Boolean.TRUE);
		}
		alertRepository.save(alerts);
	}

	public Alert loadAlertForUpdate(long id){
		return alertRepository.findOne(id);
	}
	
	public List<Alert> getAllAlertByCreatedUserId(Long createdUserId){
		return alertRepository.findByCreatedByIdAndIsDeleteIsFalse(createdUserId);
	}
	
	public AlertRecipient addAlertRecipient(AlertRecipient alertRecipient){
		return alertRecipientRepository.save(alertRecipient);
	}
	
	public AlertRecipient loadAlertRecipientForUpdate(long id){
		return alertRecipientRepository.findOne(id);
	}
	
	public List<AlertRecipient> findAlertRecipient(String name, String type, Long alertId){
		
		List<AlertRecipient> alertRecipientList = new ArrayList<>();
		
		if(type.equalsIgnoreCase(AlertRecipient.Type.LEARNER.getValue())) {
			alertRecipientList = (List<AlertRecipient>)learnerAlertRecipientRepository.findByAlertRecipientGroupNameIgnoreCaseLikeAndAlertIdAndIsDeleteIsFalse("%"+name+"%", alertId);
		} else if(type.equalsIgnoreCase(AlertRecipient.Type.LEARNERGROUP.getValue())) {
			alertRecipientList = (List<AlertRecipient>)learnerGroupAlertRecipientRepository.findByAlertRecipientGroupNameIgnoreCaseLikeAndAlertIdAndIsDeleteIsFalse("%"+name+"%", alertId);
		} else if(type.equalsIgnoreCase(AlertRecipient.Type.ORGGROUP.getValue())) {
			alertRecipientList = (List<AlertRecipient>)orgGroupAlertRecipientRepository.findByAlertRecipientGroupNameIgnoreCaseLikeAndAlertIdAndIsDeleteIsFalse("%"+name+"%", alertId);
		} else if(type.equalsIgnoreCase(AlertRecipient.Type.EMAILADDRESS.getValue())) {
			alertRecipientList = (List<AlertRecipient>)emailAddressAlertRecipientRepository.findByAlertRecipientGroupNameIgnoreCaseLikeAndAlertIdAndIsDeleteIsFalse("%"+name+"%", alertId);
		} else if(type.equalsIgnoreCase(AlertRecipient.Type.DEFAULT.getValue())) {
			alertRecipientList = (List<AlertRecipient>)alertRecipientRepository.findByAlertRecipientGroupNameIgnoreCaseLikeAndAlertIdAndIsDeleteIsFalse("%"+name+"%", alertId);
		}else{
			alertRecipientList = (List<AlertRecipient>)alertRecipientRepository.findByAlertRecipientGroupNameIgnoreCaseLikeAndAlertIdAndIsDeleteIsFalse("%"+name+"%", alertId);
		}
		
		return alertRecipientList;
	}
	
	public void deleteAlertRecipients(long alertRecipientIdArray[]){
		alertRecipientRepository.deleteByIdIn(toLong(alertRecipientIdArray));
	}
	
	public AlertRecipient getAlertRecipientById(Long id){
		return alertRecipientRepository.findOne(id);
	}

    public List<Learner> getFilteredRecipientsByAlert(Long alertId) {
        return learnerRepository.getFilteredRecipientsByAlert(alertId);
    }

    public List<Course> getFilteredCoursesByAlert(Long alertId) {
        return courseRepository.findCourseByAlertId(alertId);
    }

	public List<Learner> getLearnersUnderAlertRecipient(Long alertRecipientId , String firstName , String lastName , String emailAddress ){
		return learnerRepository.getLearnersUnderAlertRecipient(alertRecipientId, "%"+firstName+"%", "%"+lastName+"%", "%"+emailAddress+"%");
	}
	public List<LearnerGroup> getLearnerGroupsUnderAlertRecipient(Long alertRecipientId , String groupName ){
		return learnerGroupRepository.getLearnerGroupsUnderAlertRecipient(alertRecipientId , "%"+groupName+"%" );
	}
	public List<OrganizationalGroup> getOrganisationGroupsUnderAlertRecipient(Long alertRecipientId , String groupName ){
		return organizationalGroupRepository.getOrganisationGroupsUnderAlertRecipient(alertRecipientId , "%"+groupName+"%" );
	}
	public List<EmailAddress> getEmailAddressUnderAlertRecipient(Long alertRecipientId , String emailAddress ){
		return emailAddressRepository.getEmailAddressUnderAlertRecipient(alertRecipientId , "%"+emailAddress+"%" );
	}
	
	public List<AvailableAlertEvent> getAllAvailableAlertEvents(){
		return (List<AvailableAlertEvent>) alertAvailableAlertEventRepository.findAll();
	}
	
	public AlertTrigger addAlertTrigger(AlertTrigger alertTrigger){
		return alertTriggerRepository.save(alertTrigger);
	}
	
	public List<AlertTrigger> getAllAlertTriggerByAlertId(Long alertId){
		return alertTriggerRepository.findByAlertIdAndIsDeleteIsFalse(alertId);
	}
	
	public List<AlertRecipient> getAllAlertRecipientByAlertId(Long alertId) {
		return alertRecipientRepository.findByAlertId(alertId);
	}
	
	public void deleteAlertTriggers(long alertTriggerIdArray[]){
		Long[] ids = toLong(alertTriggerIdArray);
		List<AlertTrigger> alertTriggers = alertTriggerRepository.findByIdIn(ids);
		for (AlertTrigger alertTrigger : alertTriggers){

			alertTrigger.setDelete(Boolean.TRUE);
		}
		alertTriggerRepository.save(alertTriggers);
	}
	
	public AlertTrigger loadAlertTriggerForUpdate(long id){		
		return alertTriggerRepository.findOne(id);
	}
	
	public AlertTrigger getAlertTriggerByID(long id){
		return alertTriggerRepository.findOne(id);
	}
	
	public AlertTriggerFilter addAlertTriggerFilter(AlertTriggerFilter alertTriggerFilter){
		return alertTriggerFilterRepository.save(alertTriggerFilter);
	}
	
	public AlertTriggerFilter getAlertTriggerFilterByID(long id){
		return alertTriggerFilterRepository.findOne(id);
	}
	
	public List<AlertTriggerFilter> getAllAlertTriggerFilterByAlertTriggerId(Long AlertTriggerId){
		return alertTriggerFilterRepository.findByAlerttriggerId(AlertTriggerId);
	}
	
	public void deleteAlertTriggerFilter(long alertTriggerFilterIdArray[]){
		Long[] ids = toLong(alertTriggerFilterIdArray);
		List<AlertTriggerFilter> alertTriggerFilters = alertTriggerFilterRepository.findByIdIn(ids);
		for(AlertTriggerFilter alertTriggerFilter : alertTriggerFilters){
			alertTriggerFilter.setDelete(Boolean.TRUE);
		}
		alertTriggerFilterRepository.save(alertTriggerFilters);
	}
	
	public AlertTriggerFilter loadAlertTriggerFilterForUpdate(long id){
		return alertTriggerFilterRepository.findOne(id);
	}
	
	public List<Learner> getLearnersUnderAlertTriggerFilter(Long AlertTriggerFilterId, String firstName,
			String lastName, String emailAddress) {
		return learnerRepository.getLearnersUnderAlertTriggerFilter(AlertTriggerFilterId, firstName, lastName,
				emailAddress);
	}

	public List<Learner> getLearnersUnderAlertTriggerFilter(Long AlertTriggerFilterId) {
		return learnerRepository.getLearnersUnderAlertTriggerFilter(AlertTriggerFilterId);
	}

	public List<LearnerGroup> getLearnerGroupsUnderAlertTriggerFilter(Long alertTriggerFilterId , String groupName ){
		return learnerGroupRepository.getLearnerGroupsUnderAlertTriggerFilter(alertTriggerFilterId , groupName );
	}
	public List<OrganizationalGroup> getOrganisationGroupsUnderAlertTriggerFilter(Long alertTriggerFilterId , String groupName ){
		return organizationalGroupRepository.getOrganisationGroupsUnderAlertTriggerFilter(alertTriggerFilterId , "%"+groupName+"%" );
	}
	public List<Course> getCourseUnderAlertTriggerFilter(Long alertTriggerFilterId , String name , String courseType){
		//"SELECT ID , NAME , COURSETYPE  FROM COURSE WHERE NAME LIKE '%" + name + "%' 
		// and COURSETYPE IN('"+ ScormCourse.COURSE_TYPE + "','" + 
		// WebLinkCourse.COURSE_TYPE + "','" + DiscussionForumCourse.COURSE_TYPE + 
		// "','" + SynchronousCourse.COURSE_TYPE + "','" + WebinarCourse.COURSE_TYPE +"','" + 
		// HomeworkAssignmentCourse.COURSE_TYPE +"','" + InstructorConnectCourse.COURSE_TYPE +
		// "','" + SelfPacedCourse.COURSE_TYPE +"')  
		// and ID IN (SELECT COURSE_ID FROM ALERTTRIGGERFILTER_COURSE WHERE ALERTTRIGGERFILTER_ID = " + alertTriggerFilterId + ")"
		return courseRepository.getCourseUnderAlertTriggerFilter(alertTriggerFilterId , name , courseType);
	}
	public List<Course> getSuggestedTrainingCoursesBySurveyId(long surveyId){
		List<SuggestedTraining> lst = suggestedTrainingRepository.findBySurveyId(surveyId);
		for(SuggestedTraining obj : lst){
			return obj.getCourses();
		}
		return null;
	}
	
	//LMS 8256
	public List<Course> getSuggestedTrainingCoursesBySurveyIdAndContentOwner(long surveyId , String contentOwnerName){
		List<Course> coursesToReturn = new ArrayList<Course>();
		List<Course> courses = new ArrayList<Course>();
		List<SuggestedTraining> lst = suggestedTrainingRepository.findBySurveyId(surveyId);
		for(SuggestedTraining obj : lst){
			courses = obj.getCourses();
		}
		for(int i=0 ;i<courses.size() ; i++){
			if(courses.get(i).getContentOwner().getName().equals(contentOwnerName))
			coursesToReturn.add(courses.get(i));
		}
		return coursesToReturn;
	}
	
	public List<SurveyQuestion> getSurveyQuestionsBySurveyId(long surveyId){
		return surveyQuestionRepository.findBySurveyId(surveyId);
	}
	
	public List<SurveyAnswerItem> getSurveyAnswerItemsBySurveyId(long surveyId){
		List<SurveyQuestion> surveyQuestions = new ArrayList<SurveyQuestion>();
		List<SurveyAnswerItem> surveyAnswerItems = new ArrayList<SurveyAnswerItem>();
		List<SurveyAnswerItem> totalSurveyAnswerItems = new ArrayList<SurveyAnswerItem>();
		surveyQuestions = surveyQuestionRepository.findBySurveyId(surveyId);
		
		for(int i=0; i< surveyQuestions.size(); i++){
			surveyAnswerItems = surveyQuestions.get(i).getSurveyAnswers();
		}
		
		for(int i=0; i< surveyAnswerItems.size(); i++){
			totalSurveyAnswerItems.add(surveyAnswerItems.get(i));
		}
		return totalSurveyAnswerItems;
	}
	
	public AlertQueue saveAlertQueue(AlertQueue alertQueue){
		return alertQueueRepository.save(alertQueue);
	}

	public AlertQueue loadAlertQueueForUpdate(long id){
		return alertQueueRepository.findOne(id);
	}
	
	public List<AlertQueue> getAllAlertQueues(){
		return (List<AlertQueue>) alertQueueRepository.findAll();
	}
	public List<AlertQueue> getAlertQueueByUserId(long userId){
		return alertQueueRepository.findByUserId(userId);
	}
	
	public List<AlertTrigger> getAllAlertTrigger(){
		return  (List<AlertTrigger>) alertTriggerRepository.findAll();
	}
	public AlertQueue getAlertQueueByLearnerIdAndEventTable(long learnerId , String tableName ,  long tableNameRowId, long triggerId){
		return alertQueueRepository.findByTableNameAndTableNameIdAndTriggerIdAndLearnerId(tableName, tableNameRowId, triggerId, learnerId);
	}
	public AlertQueue getAlertQueueForDateDrivenTriggerByLearnerId(long learnerId , String triggerType){
		return alertQueueRepository.findByTriggerTypeAndLearnerId(triggerType, learnerId);
	}
	public MessageTemplate getMessageTemplateByEventType(String eventType){
		return messageTemplateRepository.findByEventType(eventType);
	}
	public List<AlertQueue> getAllAlertQueuesWithPendingMailStatus(){
		return alertQueueRepository.findByPendingMailStatus(true);
	}
	public MessageTemplate getMessageTemplateByTriggerType(String triggerType){
		return messageTemplateRepository.findByTriggerType(triggerType);
	}
	
	public List<Survey> getSurveysHavingSuggestedTraining(SurveyOwner surveyOwner, String surveyName, String surveyStatus , String isLockedStr,String readOnlyStr){
		//return surveyDAO.getSurveysHavingSuggestedTrainingAndSurveyResult(surveyowner, surveyName, surveyStatus, isLocked, readonly);
	    List<Survey> surveys = new ArrayList<Survey>();
	    List<SuggestedTraining> suggestedTrainings  = new ArrayList();//surveyDAO.getSuggestedTrainingBySurveyName(surveyowner, surveyName, surveyStatus, isLocked ,readonly);
	    Boolean isLocked=null;
		Boolean readOnly=null;
		
		if(surveyOwner.getOwnerType().equalsIgnoreCase("CUSTOMER")){
			Customer cus=(Customer)surveyOwner;
			if (surveyStatus.isEmpty()) {
				if(!isLockedStr.equalsIgnoreCase(Survey.RETIRE_SURVEY.All.toString())){
					if(isLockedStr.equalsIgnoreCase(Survey.RETIRE_SURVEY.Yes.toString()))
						isLocked=true;	
						//whereClause=whereClause.and(builder.get("survey").get("isLocked").equal(true));
					else if(isLockedStr.equalsIgnoreCase(Survey.RETIRE_SURVEY.No.toString()))
						isLocked=false;
						//whereClause=whereClause.and(builder.get("survey").get("isLocked").equal(false));
				}
				if(!readOnlyStr.equalsIgnoreCase(Survey.Editable.All.toString())){
					if(readOnlyStr.equalsIgnoreCase(Survey.Editable.Yes.toString()))
						readOnly=false;
						//whereClause=whereClause.and(builder.get("survey").get("readonly").equal(false));
					else if(readOnlyStr.equalsIgnoreCase(Survey.Editable.No.toString()))
						readOnly=true;
						//whereClause=whereClause.and(builder.get("survey").get("readonly").equal(true));
				}
				
				if(isLocked!=null && readOnly!=null)
					suggestedTrainings = suggestedTrainingRepository.findBySurveyNameAndSurveyIdOrDistributorIdAndIsLockedAndReadOnly(cus.getId(), cus.getDistributor().getId(), surveyName, isLocked, readOnly);
				else if(isLocked!=null && readOnly==null)
					suggestedTrainings = suggestedTrainingRepository.findBySurveyNameAndSurveyIdOrDistributorIdAndIsLocked(cus.getId(), cus.getDistributor().getId(), surveyName, isLocked);
				else if(isLocked==null && readOnly!=null)
					suggestedTrainings = suggestedTrainingRepository.findBySurveyNameAndSurveyIdOrDistributorIdAndReadOnly(cus.getId(), cus.getDistributor().getId(), surveyName, readOnly);
				else
					suggestedTrainings = suggestedTrainingRepository.findBySurveyNameAndSurveyIdOrDistributorId(cus.getId(), cus.getDistributor().getId(), surveyName);
				
			} else {
				if(!isLockedStr.equalsIgnoreCase(Survey.RETIRE_SURVEY.All.toString())){
					if(isLockedStr.equalsIgnoreCase(Survey.RETIRE_SURVEY.Yes.toString()))
						isLocked=true;
				//		whereClause=whereClause.and(builder.get("survey").get("isLocked").equal(true));
					else if(isLockedStr.equalsIgnoreCase(Survey.RETIRE_SURVEY.No.toString()))
						isLocked=false;
					//	whereClause=whereClause.and(builder.get("survey").get("isLocked").equal(false));
				}
				if(!readOnlyStr.equalsIgnoreCase(Survey.Editable.All.toString())){
					if(readOnlyStr.equalsIgnoreCase(Survey.Editable.Yes.toString()))
						readOnly=false;
						//whereClause=whereClause.and(builder.get("survey").get("readonly").equal(false));
					else if(readOnlyStr.equalsIgnoreCase(Survey.Editable.No.toString()))
						readOnly=true;
						//whereClause=whereClause.and(builder.get("survey").get("readonly").equal(true));
				}
				
				if(isLocked!=null && readOnly!=null)
					suggestedTrainings = suggestedTrainingRepository.findBySurveyNameAndSurveyStatusAndSurveyIdOrDistributorIdAndIsLockedAndReadOnly(cus.getId(), surveyStatus, cus.getDistributor().getId(), surveyName, isLocked, readOnly);
				else if(isLocked!=null && readOnly==null)
					suggestedTrainings = suggestedTrainingRepository.findBySurveyNameAndSurveyStatusAndSurveyIdOrDistributorIdAndIsLocked(cus.getId(), surveyStatus, cus.getDistributor().getId(), surveyName, isLocked);
				else if(isLocked==null && readOnly!=null)
					suggestedTrainings = suggestedTrainingRepository.findBySurveyNameAndSurveyStatusAndSurveyIdOrDistributorIdAndReadOnly(cus.getId(), surveyStatus, cus.getDistributor().getId(), surveyName, readOnly);
				else
					suggestedTrainings = suggestedTrainingRepository.findBySurveyNameAndSurveyStatusAndSurveyIdOrDistributorId(cus.getId(), surveyStatus, cus.getDistributor().getId(), surveyName);
					
			}
		}else{
			if (surveyStatus.isEmpty()) {
				if(!isLockedStr.equalsIgnoreCase(Survey.RETIRE_SURVEY.All.toString())){
					if(isLockedStr.equalsIgnoreCase(Survey.RETIRE_SURVEY.Yes.toString()))
						isLocked=true;
				//		whereClause=whereClause.and(builder.get("survey").get("isLocked").equal(true));
					else if(isLockedStr.equalsIgnoreCase(Survey.RETIRE_SURVEY.No.toString()))
						isLocked=false;
					//	whereClause=whereClause.and(builder.get("survey").get("isLocked").equal(false));
				}	
				if(!readOnlyStr.equalsIgnoreCase(Survey.Editable.All.toString())){
					if(readOnlyStr.equalsIgnoreCase(Survey.Editable.Yes.toString()))
						readOnly=true;
						//whereClause=whereClause.and(builder.get("survey").get("readonly").equal(true));
					else if(readOnlyStr.equalsIgnoreCase(Survey.Editable.No.toString()))
						readOnly=false;
						//whereClause=whereClause.and(builder.get("survey").get("readonly").equal(false));
				}		
				if(isLocked!=null && readOnly!=null)
					suggestedTrainings = suggestedTrainingRepository.findBySurveyNameAndSurveyIdAndOwnerTypeAndIsLockedAndReadOnly(surveyOwner.getId(),  surveyName,surveyOwner.getOwnerType(), isLocked, readOnly);
				else if(isLocked!=null && readOnly==null)
					suggestedTrainings = suggestedTrainingRepository.findBySurveyNameAndSurveyIdAndOwnerTypeAndIsLocked(surveyOwner.getId(), surveyName,surveyOwner.getOwnerType(),isLocked);
				else if(isLocked==null && readOnly!=null)
					suggestedTrainings = suggestedTrainingRepository.findBySurveyNameAndSurveyIdAndOwnerTypeAndReadOnly(surveyOwner.getId(), surveyName, surveyOwner.getOwnerType(),readOnly);
				else
					suggestedTrainings = suggestedTrainingRepository.findBySurveyNameAndSurveyIdAndOwnerType(surveyOwner.getId(), surveyName,surveyOwner.getOwnerType());
					
				
			}
	
			else {
				
				if(!isLockedStr.equalsIgnoreCase(Survey.RETIRE_SURVEY.All.toString())){
					if(isLockedStr.equalsIgnoreCase(Survey.RETIRE_SURVEY.Yes.toString()))
						isLocked=true;
				//		whereClause=whereClause.and(builder.get("survey").get("isLocked").equal(true));
					else if(isLockedStr.equalsIgnoreCase(Survey.RETIRE_SURVEY.No.toString()))
						isLocked=false;
					//	whereClause=whereClause.and(builder.get("survey").get("isLocked").equal(false));
				}
				if(!readOnlyStr.equalsIgnoreCase(Survey.Editable.All.toString())){
					if(readOnlyStr.equalsIgnoreCase(Survey.Editable.Yes.toString()))
						readOnly=false;
						//whereClause=whereClause.and(builder.get("survey").get("readonly").equal(false));
					else if(readOnlyStr.equalsIgnoreCase(Survey.Editable.No.toString()))
						readOnly=true;
						//whereClause=whereClause.and(builder.get("survey").get("readonly").equal(true));
				}
				if(isLocked!=null && readOnly!=null)
					suggestedTrainings = suggestedTrainingRepository.findBySurveyNameAndSurveyStatusAndSurveyIdAndOwnerTypeAndIsLockedAndReadOnlyAndStatus(surveyOwner.getId(), surveyName,surveyStatus,surveyOwner.getOwnerType(), isLocked, readOnly);
				else if(isLocked!=null && readOnly==null)
					suggestedTrainings = suggestedTrainingRepository.findBySurveyNameAndSurveyStatusAndSurveyIdAndOwnerTypeAndIsLockedAndStatus(surveyOwner.getId(), surveyName,surveyStatus,surveyOwner.getOwnerType(), isLocked);
				else if(isLocked==null && readOnly!=null)
					suggestedTrainings = suggestedTrainingRepository.findBySurveyNameAndSurveyStatusAndSurveyIdAndOwnerTypeAndReadOnlyAndStatus(surveyOwner.getId(), surveyName,surveyStatus,surveyOwner.getOwnerType(), readOnly);
				else
					suggestedTrainings = suggestedTrainingRepository.findBySurveyNameAndSurveyStatusAndSurveyIdAndOwnerTypeAndStatus(surveyOwner.getId(), surveyName,surveyStatus,surveyOwner.getOwnerType());
					
				
			}
		}
	    
	    
		if(suggestedTrainings.size() >0){
			for(int i=0;i<suggestedTrainings.size() ; i++){
				if(surveyResultRepository.findBySurveyId(suggestedTrainings.get(i).getSurvey().getId()).size() >0){
					
					surveys.add(suggestedTrainings.get(i).getSurvey());
				}
				}
			}
		
		return surveys;
		
	}

	public SurveyResultAnswer getSurveyResultAnswerBySurveyQuestionId(long id){
		return surveyResultAnswerRepository.findFirstByQuestionId(id);
	}
	public SurveyReviewComment addSurveyReviewComment(SurveyReviewComment surveyReviewComment){
		return surveyReviewCommentRepository.save(surveyReviewComment);
	}
	
	public SurveyReviewComment loadForUpdateSurveyReviewComment(long id) {
		return surveyReviewCommentRepository.findOne(id);
	}
	
	public SurveyReviewComment getSurveyReviewCommentByResultId(long surveyResultId) {
		return surveyReviewCommentRepository.findByAnswerId(surveyResultId);
	}
	
	public SurveyReviewComment updateSurveyReviewComment(SurveyReviewComment surveyReviewComment) {
		return surveyReviewCommentRepository.save(surveyReviewComment);
	}
	
	public List<SurveyResultAnswer> getSurveyResultAnswersByQuestionId(long questionId) {
		return surveyResultAnswerRepository.findByQuestionId(questionId);
	}
	public SurveyResultAnswer getSurveyResultAnswerByQuestionIdAndVU360UserId(long questionId, long userId) {
		return this.getSurveyResultAnswerByQuestionIdAndResultId(questionId, userId);
	}
	
	public SurveyResultAnswer getSurveyResultAnswerByQuestionIdAndResultId(long questionId, long userId) {
		return surveyResultAnswerRepository.findByQuestionIdAndSurveyResultSurveyeeId(questionId, userId);
	}
	
	public boolean generateTrainingNeedAnalysis(Long surveyId, Brander brand ){		
		//Get the TOPLink DAO to get the data in a Model object
		//Once the model object is obtained we will call VelocityTemplateMappingServiceImpl
		List<SurveyResult> results = surveyResultRepository.findBySurveyId(surveyId);
		String templateUri = brand.getBrandElement("");
		
		try {
			TemplateServiceImpl tmpSvc = TemplateServiceImpl.getInstance();
			Map<Object, Object> context = new HashMap<Object, Object>();
			HashMap<Object, Object> attrs = new HashMap<Object, Object>(context);
			context.put("results", results.get(0));
			try {
				tmpSvc.renderTemplate(templateUri, attrs);
			}
			catch(Exception e) {
				log.error("could not render velocity template", e);
				e.printStackTrace();
				return false;
			}
			//TODO:WriteRTFFileToDisk
		}
		catch (Throwable th) {
			log.error("could not execute report", th);	
			return false;
		}
		return true;
	}
	
	public WrittenTrainingPlan generateWrittenTrainingPlanSurveyResults(VU360User user, long surveyId){
		Survey tpSurvey = this.getSurveyByID(surveyId);
		SurveyResult result=this.getSurveyResultByLearnerAndSurvey(user, tpSurvey);
		WrittenTrainingPlan tp=new WrittenTrainingPlan();
		
		tp.setCompanyName(user.getLearner().getCustomer().getName());
		tp.setCountry(user.getLearner().getLearnerProfile().getLearnerAddress().getCountry());
		tp.setCity(user.getLearner().getLearnerProfile().getLearnerAddress().getCity());
		tp.setEmailAddress(user.getEmailAddress());
		tp.setFirstName(user.getFirstName());
		tp.setLastName(user.getLastName());
		tp.setMainAddress(user.getLearner().getLearnerProfile().getLearnerAddress().getStreetAddress());
		tp.setPhone(user.getLearner().getLearnerProfile().getOfficePhone());
		tp.setState(user.getLearner().getLearnerProfile().getLearnerAddress().getState());
		tp.setZipCode(user.getLearner().getLearnerProfile().getLearnerAddress().getZipcode());
		
		Class c = null;
		try{
			c = Class.forName("com.softech.vu360.lms.model.WrittenTrainingPlan");
		}
		catch(Exception e){
			log.error(e.getMessage());
		}
		
		List<TPAnswer> tpAnswers = null;
		TPQuestion tpQuestion = null;
		HashMap<Long,TPQuestion> questionsList = new HashMap<Long, TPQuestion>();

		if(result==null)
			return tp;
		
		log.debug("Survey Result size : " + result.getAnswers().size());
		
		for(SurveyResultAnswer answer : result.getAnswers()){
			tpAnswers = new ArrayList<TPAnswer>();
			if(answer.getQuestion() instanceof MultipleSelectSurveyQuestion || answer.getQuestion() instanceof SingleSelectSurveyQuestion ){
				for(SurveyAnswerItem item : answer.getSurveyAnswerItems()){
					TPAnswer tpAnswer = new TPAnswer();
					tpAnswer.setAnswerText(item.getLabel());
					tpAnswer.setId(item.getId());
					tpAnswers.add(tpAnswer);
				}
			}
			else {
				TPAnswer tpAnswer = new TPAnswer();
				tpAnswer.setAnswerText(answer.getSurveyAnswerText());
				tpAnswer.setId(answer.getId());
				tpAnswers.add(tpAnswer);
			}
			tpQuestion = new TPQuestion();
			tpQuestion.setId(answer.getQuestion().getId());
			tpQuestion.setQuestion(answer.getQuestion().getText());
			tpQuestion.setDisplayOrder(answer.getQuestion().getDisplayOrder());
			tpQuestion.setAnswers(tpAnswers);
			questionsList.put(answer.getQuestion().getId(),tpQuestion);
		}
		log.debug("SIZEEE : " + questionsList.size());
		
		//ADD AGS Questions 
		for(SurveyQuestion agsQ : tpSurvey.getQuestions()){
			if(agsQ instanceof AggregateSurveyQuestion){
				tpQuestion = new TPQuestion();
				tpQuestion.setId(agsQ.getId());
				tpQuestion.setQuestion(agsQ.getText());
				tpQuestion.setDisplayOrder(agsQ.getDisplayOrder());
				//associate its answers
				for(AggregateSurveyQuestionItem aggItem : this.getAggregateSurveyQuestionItemsByQuestionId(agsQ.getId())){
					TPQuestion q = questionsList.get(aggItem.getQuestion().getId());
					if(q!=null)
						tpQuestion.getAnswers().addAll(q.getAnswers());
				}
				questionsList.put(agsQ.getId(), tpQuestion);
			}
		}
		int i=1;
		
		for(TPQuestion question : questionsList.values()){
			try {
				Method method = c.getMethod("setQuestion" + i,TPQuestion.class);
				if(method!=null){
					method.invoke(tp,question);
				}
				i++;
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
			}
		}
		return tp;
	}

	public SurveyResult addSurveyResultForCEPlanner(VU360User user,TakeSurveyForm form, HashMap<Long, Long> questionListMapping,HashMap<Long, Long> answerListMapping){
		Long surveyId = form.getSurveyId();
		com.softech.vu360.lms.model.Survey survey = this.loadForUpdateSurvey(surveyId); //get the Survey object somehow????
		com.softech.vu360.lms.model.Course course = null;
		SurveyResult surveyResult = null ;
		Learner learner=learnerService.loadForUpdateLearner(form.getLearner().getId());
		if( ! form.getSurvey().getSurveyRef().getEvent().equalsIgnoreCase("survey.event.manual") ) {
			course = courseAndCourseGroupService.getCourseById(form.getCourseId()); //get the course object somehow????
			surveyResult = surveyResultRepository.findOneBySurveyeeAndCourseAndSurvey(user,course,survey);
		} else {
			surveyResult = surveyResultRepository.findOneBySurveyeeAndSurvey(user, survey);
		}
		List<SurveyResultAnswer> surveyResultAnswerList=null;
		List<SurveyFlagTemplate> surveyFlagTemplates =surveyFlagTemplateRepository.findBySurveyId(surveyId);
		List<SurveyFlag> surveyFlags = new ArrayList<SurveyFlag>();
		Course surveycourse = courseAndCourseGroupService.loadForUpdateCourse(form.getCourseId());
		boolean sendbackSurvey = false;
		
		if(surveyResult != null){ // to update if answer exists in case of sendback surveys
			surveyResult = surveyResultRepository.findOne(surveyResult.getId());
			surveyResultAnswerList = surveyResult.getAnswers();
			for (int i=0;i<surveyResultAnswerList.size();i++){
				surveyFlags.add(surveyFlagRepository.findOneByAnswerline(surveyResultAnswerList.get(i)));
			}
			sendbackSurvey = true;
		}else{
			surveyResult = new SurveyResult();
			surveyResultAnswerList = new ArrayList<SurveyResultAnswer>();
			surveyResult.setSurvey(survey);
			surveyResult.setSurveyee(user);			
			surveyResult.setCourse(surveycourse);
			surveyFlags= new ArrayList<SurveyFlag>();
		}
		log.debug("courseId="+form.getCourseId());

		for (int i=0;i<form.getSurvey().getQuestionList().size();i++) {
			com.softech.vu360.lms.model.SurveyQuestion question = form.getSurvey().getQuestionList().get(i).getSurveyQuestionRef();
			List<SurveyAnswerItem> surveyAnswerItems=null;
			SurveyFlagTemplate flagTemplate=null;
			if(surveyFlagTemplates !=null && surveyFlagTemplates.size()>0){
				for(SurveyFlagTemplate surveyFlagTemplate:surveyFlagTemplates){
					if(surveyFlagTemplate.getQuestion().getId().equals(question.getId())){
						surveyAnswerItems=surveyFlagTemplate.getSurveyAnswers();
						flagTemplate=surveyFlagTemplate;
						break;
					}
				}
			}

			if (question instanceof MultipleSelectSurveyQuestion) {
				SurveyResultAnswer surveyResultAnswer = new SurveyResultAnswer();
				List<SurveyAnswerItem> surveyAnswerList = new ArrayList<SurveyAnswerItem>();
				surveyResultAnswer.setQuestion(question);
				surveyResultAnswer.setSurveyAnswerItems(surveyAnswerList);
				surveyResultAnswer.setSurveyResult(surveyResult);
				surveyResultAnswerList.add(surveyResultAnswer);
				for (com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem answerItem : form.getSurvey().getQuestionList().get(i).getAnswerItems()) {
					if (answerItem.isSelected()) {
						surveyAnswerList.add(answerItem.getSurveyAnswerItemRef());
						if (surveyAnswerItems != null) {
							for(SurveyAnswerItem surveyAnswerItem:surveyAnswerItems ){
								if(surveyAnswerItem.compareTo(answerItem.getSurveyAnswerItemRef())==0){
									//	generate flag
									if(sendbackSurvey == false){
										SurveyFlag surveyFlag= new SurveyFlag();
										surveyFlag.setFlagTemplate(flagTemplate);
										surveyFlag.setResponseProvider(user);
										surveyFlag.setTriggerDate(new Date());
										surveyFlag.setAnswerline(surveyResultAnswer);
										surveyFlags.add(surveyFlag);
										break;
									}
									else{
										for(int j=0;j<surveyFlags.size();j++){		
											if(surveyFlags.get(j) != null){
												surveyFlags.get(j).setSentBackToLearner(false);
											}
										}
									}
								}
							}
						}
					}
				}
			} else if (question instanceof SingleSelectSurveyQuestion) {
				SurveyResultAnswer surveyResultAnswer = new SurveyResultAnswer();
				List<SurveyAnswerItem> surveyAnswerList = new ArrayList<SurveyAnswerItem>();
				if(form.getSurvey().getQuestionList().get(i).getSingleSelectAnswerId() !=null){
					surveyResultAnswer.setQuestion(question);
					surveyResultAnswer.setSurveyAnswerItems(surveyAnswerList);
					surveyResultAnswer.setSurveyResult(surveyResult);
					surveyResultAnswerList.add(surveyResultAnswer);
					for (com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem answerItem : form.getSurvey().getQuestionList().get(i).getAnswerItems()) {
						if (answerItem.getSurveyAnswerItemRef().getId().compareTo(form.getSurvey().getQuestionList().get(i).getSingleSelectAnswerId()) == 0) {
							surveyAnswerList.add(answerItem.getSurveyAnswerItemRef());
							if (surveyAnswerItems != null) {
								for(SurveyAnswerItem surveyAnswerItem:surveyAnswerItems ){
									if(surveyAnswerItem.compareTo(answerItem.getSurveyAnswerItemRef())==0){
										//	generate flag
										if(sendbackSurvey == false){
											SurveyFlag surveyFlag= new SurveyFlag();
											surveyFlag.setFlagTemplate(flagTemplate);
											surveyFlag.setResponseProvider(user);
											surveyFlag.setTriggerDate(new Date());
											surveyFlag.setAnswerline(surveyResultAnswer);
											surveyFlags.add(surveyFlag);
											break;
										}
										else{
											for(int j=0;j<surveyFlags.size();j++){	
												if(surveyFlags.get(j) != null){
													surveyFlags.get(j).setSentBackToLearner(false);
												}
											}
										}
									}
								}
							}
							break;
						}
					}
				}
			} else if (question instanceof AggregateSurveyQuestion) {
				if (form.getSurvey().getQuestionList().get(i).getSurveyView().getQuestionList() != null 
						&& form.getSurvey().getQuestionList().get(i).getSurveyView().getQuestionList().size() > 0) {

					for (int j=0; j<form.getSurvey().getQuestionList().get(i).getSurveyView().getQuestionList().size(); j++ ) {
						com.softech.vu360.lms.model.SurveyQuestion q = form.getSurvey().getQuestionList().get(i).getSurveyView().getQuestionList().get(j).getSurveyQuestionRef();
						/*com.softech.vu360.lms.model.SurveyQuestion surveyFlagTemplateQuestion =null;
						List<SurveyAnswerItem> surveyAnswerItems=null;
						SurveyFlagTemplate flagTemplate=null;
						if(surveyFlagTemplates !=null && surveyFlagTemplates.size()>0){
							for(SurveyFlagTemplate surveyFlagTemplate:surveyFlagTemplates){
								if(surveyFlagTemplate.getQuestion().getId().equals(question.getId())){
									surveyAnswerItems=surveyFlagTemplate.getSurveyAnswers();
									flagTemplate=surveyFlagTemplate;
									break;
								}
							}
						}*/

						if (q instanceof MultipleSelectSurveyQuestion) {
							SurveyResultAnswer surveyResultAnswer = new SurveyResultAnswer();
							List<SurveyAnswerItem> surveyAnswerList = new ArrayList<SurveyAnswerItem>();
							surveyResultAnswer.setQuestion(q);
							surveyResultAnswer.setSurveyAnswerItems(surveyAnswerList);
							surveyResultAnswer.setSurveyResult(surveyResult);
							surveyResultAnswerList.add(surveyResultAnswer);
							for (com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem answerItem : form.getSurvey().getQuestionList().get(i).getSurveyView().getQuestionList().get(j).getAnswerItems()) {
								if (answerItem.isSelected()) {
									surveyAnswerList.add(answerItem.getSurveyAnswerItemRef());
									if (surveyAnswerItems != null) {
										for(SurveyAnswerItem surveyAnswerItem:surveyAnswerItems ){
											if(surveyAnswerItem.compareTo(answerItem.getSurveyAnswerItemRef())==0){
												//	generate flag
												SurveyFlag surveyFlag= new SurveyFlag();
												surveyFlag.setFlagTemplate(flagTemplate);
												surveyFlag.setResponseProvider(user);
												surveyFlag.setTriggerDate(new Date());
												surveyFlag.setAnswerline(surveyResultAnswer);
												surveyFlags.add(surveyFlag);
												break;
											}
										}
									}
								}
							}
						} else if (q instanceof SingleSelectSurveyQuestion) {
							SurveyResultAnswer surveyResultAnswer = new SurveyResultAnswer();
							List<SurveyAnswerItem> surveyAnswerList = new ArrayList<SurveyAnswerItem>();
							if(form.getSurvey().getQuestionList().get(i).getSurveyView().getQuestionList().get(j).getSingleSelectAnswerId() !=null){
								surveyResultAnswer.setQuestion(q);
								surveyResultAnswer.setSurveyAnswerItems(surveyAnswerList);
								surveyResultAnswer.setSurveyResult(surveyResult);
								surveyResultAnswerList.add(surveyResultAnswer);
								for (com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem answerItem : form.getSurvey().getQuestionList().get(i).getSurveyView().getQuestionList().get(j).getAnswerItems()) {
									if (answerItem.getSurveyAnswerItemRef().getId().compareTo(form.getSurvey().getQuestionList().get(i).getSurveyView().getQuestionList().get(j).getSingleSelectAnswerId()) == 0) {
										surveyAnswerList.add(answerItem.getSurveyAnswerItemRef());
										if (surveyAnswerItems != null) {
											for(SurveyAnswerItem surveyAnswerItem:surveyAnswerItems ){
												if(surveyAnswerItem.compareTo(answerItem.getSurveyAnswerItemRef())==0){
													//	generate flag
													SurveyFlag surveyFlag= new SurveyFlag();
													surveyFlag.setFlagTemplate(flagTemplate);
													surveyFlag.setResponseProvider(user);
													surveyFlag.setTriggerDate(new Date());
													surveyFlag.setAnswerline(surveyResultAnswer);
													surveyFlags.add(surveyFlag);
													break;
												}
											}
										}
										break;
									}
								}
							}
						}
						
						else if (q instanceof TextBoxSurveyQuestion) {
							SurveyResultAnswer surveyResultAnswer = new SurveyResultAnswer();
							surveyResultAnswer.setQuestion(q);
							surveyResultAnswer.setSurveyAnswerText(form.getSurvey().getQuestionList().get(i).getSurveyView().getQuestionList().get(j).getAnswerText());
							
							surveyResultAnswer.setSurveyResult(surveyResult);
							surveyResultAnswerList.add(surveyResultAnswer);
						}
					}
				}
			} else {
				SurveyResultAnswer surveyResultAnswer = new SurveyResultAnswer();
				surveyResultAnswer.setQuestion(question);
				surveyResultAnswer.setSurveyAnswerText(form.getSurvey().getQuestionList().get(i).getAnswerText());
				List<com.softech.vu360.lms.web.controller.model.survey.AvailablePersonalInformationfield> personalInfoItems = form.getSurvey().getQuestionList().get(i).getPersonalInfoItems();
				if (personalInfoItems != null && personalInfoItems.size() > 0) {
					for (com.softech.vu360.lms.web.controller.model.survey.AvailablePersonalInformationfield pIField : personalInfoItems) {
						if (pIField.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().compareToIgnoreCase("FIRSTNAME") == 0) {
							learner.getVu360User().setFirstName(pIField.getAnswerText());
						} else if (pIField.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().compareToIgnoreCase("MIDDLENAME") == 0) {
							learner.getVu360User().setMiddleName(pIField.getAnswerText());
						} else if (pIField.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().compareToIgnoreCase("LASTNAME") == 0) {
							learner.getVu360User().setLastName(pIField.getAnswerText());
						} else if (pIField.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().compareToIgnoreCase("STREETADDRESS") == 0) {
							learner.getLearnerProfile().getLearnerAddress().setStreetAddress(pIField.getAnswerText());
						} else if (pIField.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().compareToIgnoreCase("STREETADDRESS2") == 0) {
							learner.getLearnerProfile().getLearnerAddress().setStreetAddress2(pIField.getAnswerText());
						} else if (pIField.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().compareToIgnoreCase("EMAILADDRESS") == 0) {
							learner.getVu360User().setEmailAddress(pIField.getAnswerText());
						} else if (pIField.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().compareToIgnoreCase("OFFICEPHONE") == 0) {
							learner.getLearnerProfile().setOfficePhone(pIField.getAnswerText());
						} else if (pIField.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().compareToIgnoreCase("ZONE") == 0) {
							if( !StringUtils.isBlank(pIField.getAnswerText())){
								learner.getLearnerProfile().getTimeZone().setZone(pIField.getAnswerText());
							}
						}
					}
					learnerService.saveLearner(learner);
				}
				surveyResultAnswer.setSurveyResult(surveyResult);
				surveyResultAnswerList.add(surveyResultAnswer);
			}
		}
		//Map new IDs
		for(SurveyResultAnswer surveyAnswer : surveyResultAnswerList){
			if(answerListMapping.get(surveyAnswer.getId())!=null)
				surveyAnswer.setId(answerListMapping.get(surveyAnswer.getId()));
			
			if(questionListMapping.get(surveyAnswer.getQuestion().getId())!=null)
				surveyAnswer.getQuestion().setId(questionListMapping.get(surveyAnswer.getQuestion().getId()));
			
			if(surveyAnswer.getSurveyAnswerItems()!=null && surveyAnswer.getSurveyAnswerItems().size()>0){
				for(SurveyAnswerItem surveyAnswerItem : surveyAnswer.getSurveyAnswerItems()){
					if(questionListMapping.get(surveyAnswerItem.getSurveyQuestion().getId())!=null){
						surveyAnswerItem.getSurveyQuestion().setId(questionListMapping.get(surveyAnswerItem.getSurveyQuestion().getId()).longValue());
					}	
					if(answerListMapping.get(surveyAnswerItem.getId())!=null)
						surveyAnswerItem.setId(answerListMapping.get(surveyAnswerItem.getId()));
					}
				}
			}
		
		//End Map new IDs
		surveyResult.setAnswers(surveyResultAnswerList);
		// updating survey first  
		surveyResult = surveyResultRepository.save(surveyResult);
		for(SurveyFlag surveyFlag: surveyFlags){
			if(sendbackSurvey == false){
				surveyFlagRepository.save(surveyFlag);
				sendFlagTemplateMail(surveyFlag);
			}else{
				if(surveyFlag != null){
				surveyFlag = surveyFlagRepository.findOne(surveyFlag.getId());
				surveyFlagRepository.save(surveyFlag);
					sendFlagTemplateMail(surveyFlag);
				}
			}
		}

		if( ! form.getSurvey().getSurveyRef().getEvent().equalsIgnoreCase("survey.event.manual") )
			statisticsService.surveyFiredEvent(user.getLearner(), surveycourse);
		
		return surveyResult;
	}
	
	/*
	 * TSM - Technology Stack Migration
	 * 
	 * Following code piece was written to enforce
	 * cache update and such calls are now unnecessary.  
	 * Cache must not be enforced in such manner, therefore
	 * this code has been taken off
	 * 
	 * */
	/*public Survey refreshSurveys(Long surveyId){
		return surveyDAO.refreshSurveys(surveyId);
	}*/

    public boolean isRequiredProctorApproval(Long tableNameId, String[] businessKeys) {
        return surveyRepository.isAlertQueueRequiredProctorApproval(tableNameId, Arrays.toString(businessKeys).toString().replace("[", "").replace("]", "").replaceAll(" ", ""));
    }

	@Override
	public List<SurveySection> getSurveySectionsBySurveyId(Long surveyId) {
		try {
			return surveySectionRepository.findBySurveyId(surveyId);
		}catch (ObjectRetrievalFailureException e) {
			log.error("There was an error while retrieving Survey Sections by surveyId (surveyId = " + surveyId + "). Error Message: " + e.getMessage(), e);
		}
		return null;
	}
	
	@Override
	public SurveySection getSurveySectionByID(Long id) {
		return surveySectionRepository.findOne(id);
	}
	
	@Override
	@Transactional
	public SurveySection saveSurveySection(SurveySection surveySection) {
		return surveySectionRepository.save(surveySection);
	}
	
	@Override
	public PredictSurveyQuestionMapping getPredictSurveyQuestionMappingByLMSSurveyId(Long surveyId, Long questionId) {
		return predictSurveyQuestionMappingRepository.findByLmsSurveyIdAndLmsQuestionId(surveyId, questionId);
	}
	
	@Override
	public PredictSurveyQuestionMapping savePredictSurveyQuestionMapping(PredictSurveyQuestionMapping predictSurveyQuestionMapping) {
		return predictSurveyQuestionMappingRepository.save(predictSurveyQuestionMapping);
	}
	
	@Override
	public SurveyQuestion getSurveyQuestionBySurveySectionQuestionID(Long id) {
		return surveyQuestionRepository.findOne(id);
	}

	@Override
	public SurveySectionSurveyQuestionBank saveSurveySectionSurveyQuestionBank(SurveySectionSurveyQuestionBank sectionSurveyQuestionBank) {
		return surveySectionSurveyQuestionBankRepository.saveSurveySectionSurveyQuestionBank(sectionSurveyQuestionBank);
	}

	@Override
	public void delete(SurveySectionSurveyQuestionBank sectionSurveyQuestionBank) {
		surveySectionSurveyQuestionBankRepository.delete(sectionSurveyQuestionBank);
	}

	@Override
	public SurveyQuestionBank saveSurveyQuestionBank(SurveyQuestionBank surveyQuestionBank) {
		return surveyQuestionBankRepository.save(surveyQuestionBank);
	}

	@Override
	public void delete(SurveyQuestionBank surveyQuestionBank) {
		surveyQuestionBankRepository.delete(surveyQuestionBank);
	}
	
	@Override
	public SurveyAnswerItem saveSurveyAnswerItem(SurveyAnswerItem answerItem) {
		return surveyAnswerItemRepository.save(answerItem);
	}

	@Override
	public void delete(SurveyAnswerItem answerItem) {
		surveyAnswerItemRepository.delete(answerItem);
	}

	@Override
	public List<SurveySection> getRootSurveySections(Survey survey) {
		return getRootSurveySections(survey.getId());
	}

	@Override
	public List<SurveySection> getRootSurveySections(Long surveyId) {
		return surveySectionRepository.findBySurveyIdAndParentIsNull(surveyId);
	}

	@Override
	public List<PredictSurveyQuestionMapping> getPredictSurveyQuestionMappingBySurveyId(
			Long lmsSurveyId) {
		return predictSurveyQuestionMappingRepository.findByLmsSurveyId(lmsSurveyId);
	}

	@Override
	public Integer getNextSectionCloneNumber(Long predictSurveyId, Long lmsSurveyId, String predictSectionId) {
		PredictSurveyQuestionMapping entity = predictSurveyQuestionMappingRepository.findFirstBySurveyIdAndLmsSurveyIdAndSurveySectionIdOrderByClonesAsc(predictSurveyId, lmsSurveyId, predictSectionId);
		if(entity!=null && entity.getClones()!=null) {
			return entity.getClones()+1;
		} else {
			return 1;
		}
	}

	@Override
	public PredictSurveyQuestionMapping getPredictSurveyQuestionMapping(Long surveyId, String sectionId, Long frameworkId, Long questionId) {
		return predictSurveyQuestionMappingRepository.findByLmsSurveyIdAndLmsSectionIdAndLmsFrameworkIdAndLmsQuestionId(surveyId, sectionId, frameworkId, questionId);
	}

	@Override
	public SurveyQuestionBank getSurveyQuestionBankById(Long surveyQuestionBankId) {
		return surveyQuestionBankRepository.findOne(surveyQuestionBankId);
	}
	
	@Override
	public List<SurveyResult> getSurveyResultsBySurveyId(Long surveyId) {
		return surveyResultRepository.findBySurveyId(surveyId);
	}

	@Override
	public SurveyResultAnswer getSurveyResultAnswer(Long questionId, Long frameworkId, Long sectionId, Long userId) {
		return surveyResultAnswerRepository.findByQuestionIdAndSurveyQuestionBankIdAndSurveySectionIdAndSurveyResultSurveyeeId(questionId, frameworkId, sectionId, userId);
	}

	@Override
	public SurveyAnswerItem getAnswerItemById(Long answerId) {
		return surveyAnswerItemRepository.findOne(answerId);
	}

	@Override
	public Integer getStartedSurveys(Long surveyId) {
		return surveyResultRepository.countBySurveyId(surveyId);
	}

	@Override
	public Integer getAnsweredQuestionCount(Long surveyId, Long sectionId, Long questionId) {
		return surveyResultRepository.countByAnsweredQuestion(surveyId, sectionId, questionId);
	}

	@Override
	public Integer getQuestionAnswersCount(Long surveyId, Long sectionId, Long questionId) {
		return surveyResultRepository.countByQuestionsAnswered(surveyId, sectionId, questionId);
	}

	@Override
	public Integer getAnswerCount(Long surveyId, Long sectionId, Long questionId, Long answerId) {
		return surveyResultRepository.countByAnswer(surveyId, sectionId, questionId, answerId);
	}

	@Override
	public PredictSurveyQuestionMapping getPredictSurveyQuestionMappingBySectionId(Long lmsSectiondId) {
		return predictSurveyQuestionMappingRepository.findByLmsSectionId(lmsSectiondId);
	}

	@Override
	public SurveyResultAnswerFile getSurveyFileById(Long fileId) {
		return surveyResultAnswerFileRepository.findOne(fileId);
	}
	
	@Override
	public PredictSurveyQuestionMapping getPredictSurveyQuestionMappingByFrameworkId(Long lmsFrameworkId) {
		return predictSurveyQuestionMappingRepository.findByLmsFrameworkId(lmsFrameworkId);
	}

	@Override
	public List<Survey> getAssignedInspectionSurveys(SurveyOwner surveyOwner) {
		List<SurveyLearner> surveyAssignments = surveyLearnerRepository.findBySurveyOwnerAndSurveyIsInspectionIsTrue(surveyOwner);
		List<Survey> surveys = new ArrayList<Survey>();
		for (SurveyLearner surveyLearner : surveyAssignments) {
			Survey survey = surveyLearner.getSurvey();
			surveys.remove(survey);
			surveys.add(survey);
		}
		return surveys;
	}
	
	//TODO: move both of them into a helper file
	private Long[] toLong(long[] alertIdArray) {
		Long[] ids = new Long[alertIdArray.length];
		int index = 0;
		for(long id: alertIdArray) {
			ids[index++] = new Long(id);
		}
		return ids;
	}
	
	private List<Long> toLongList(long[] alertIdArray) {
		List<Long> ids = new ArrayList<Long>();
		for(long id: alertIdArray) {
			ids.add(new Long(id));
		}
		return ids;
	}

	@Override
	public List<AlertTriggerFilter> findByAlerttriggerIdAndAlerttriggerIsDeleteFalseAndIsDeleteFalse(
			Long alertTriggerId) {
		return alertTriggerFilterRepository.findByAlerttriggerIdAndAlerttriggerIsDeleteFalseAndIsDeleteFalse(alertTriggerId);
	}

}
