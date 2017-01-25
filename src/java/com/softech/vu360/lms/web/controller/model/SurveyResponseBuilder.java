package com.softech.vu360.lms.web.controller.model;

import java.util.Collections;
import java.util.List;

import com.softech.vu360.lms.model.AggregateSurveyQuestion;
import com.softech.vu360.lms.model.AggregateSurveyQuestionItem;
import com.softech.vu360.lms.model.AvailablePersonalInformationfield;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.MultipleSelectSurveyQuestion;
import com.softech.vu360.lms.model.PersonalInformationSurveyQuestion;
import com.softech.vu360.lms.model.SingleSelectSurveyQuestion;
import com.softech.vu360.lms.model.SurveyAnswerItem;
import com.softech.vu360.lms.model.SurveyResultAnswer;
import com.softech.vu360.lms.model.TextBoxSurveyQuestion;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author Saptarshi
 *
 */
public class SurveyResponseBuilder  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private static final String MULTICHOICE_MULTISELECT_TEMPLATE="vm/manager/userGroups/survey/Survey Approvals/survey/multichoiceMultiselect.vm";
	private static final String MULTICHOICE_SINGLESELECT_TEMPLATE="vm/manager/userGroups/survey/Survey Approvals/survey/multichoiceSingleselect.vm";
	private static final String DROPDOWN_SINGLESELECT_TEMPLATE="vm/manager/userGroups/survey/Survey Approvals/survey/dropdownSingleselect.vm";
	private static final String COMMENT_LIMITEDTEXT_TEMPLATE="vm/manager/userGroups/survey/Survey Approvals/survey/commentLimitedtext.vm";
	private static final String COMMENT_UNLIMITEDTEXT_TEMPLATE="vm/manager/userGroups/survey/Survey Approvals/survey/commentUnlimitedtext.vm";
	private static final String SUGGESTED_TRAINING_MULTICHOICE_MULTISELECT_TEMPLATE="vm/learner/survey/suggestedTraining/multichoiceMultiselect.vm";
	private static final String SUGGESTED_TRAINING_MULTICHOICE_SINGLESELECT_TEMPLATE="vm/learner/survey/suggestedTraining/multichoiceSingleselect.vm";
	private static final String PERSONAL_INFORMATION_TEMPLATE="vm/manager/userGroups/survey/Survey Approvals/survey/personalInformation.vm";
	private static final String CUSTOM_MULTICHOICE_MULTISELECT_TEMPLATE="vm/learner/survey/customMultichoiceMultiselect.vm";

	private com.softech.vu360.lms.web.controller.model.survey.Survey surveyView;
	public SurveyResponseBuilder(com.softech.vu360.lms.model.Survey survey) {
		surveyView = new com.softech.vu360.lms.web.controller.model.survey.Survey(survey);
	}

	public void buildQuestion(com.softech.vu360.lms.model.SurveyQuestion surveyQuestion, SurveyResultAnswer surveyResultAnswer){
		//The com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion objects 
		//will be built here and assembled to the surveyView
		com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion question = null;
		if(surveyQuestion instanceof TextBoxSurveyQuestion){
			TextBoxSurveyQuestion q = (TextBoxSurveyQuestion)surveyQuestion;
			question = new com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion(surveyQuestion);
			if (surveyResultAnswer != null) {
				question.setAnswerText(surveyResultAnswer.getSurveyAnswerText());
				if (surveyResultAnswer.getComments() != null && surveyResultAnswer.getComments().size() > 0) {
					question.setComment(surveyResultAnswer.getComments().get(0).getComment());
				}
			}
			if(q.isSingleLineResponse()){
				question.setTemplatePath(COMMENT_LIMITEDTEXT_TEMPLATE);
			} else {
				question.setTemplatePath(COMMENT_UNLIMITEDTEXT_TEMPLATE);
			}
		}else if(surveyQuestion instanceof MultipleSelectSurveyQuestion){
			MultipleSelectSurveyQuestion q = (MultipleSelectSurveyQuestion)surveyQuestion;
			question = new com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion(surveyQuestion);
			List<SurveyAnswerItem> surveyAnswerItems =  q.getSurveyAnswers();
			Collections.sort(surveyAnswerItems);
			for(int i=0; i<surveyAnswerItems.size(); i++){
				SurveyAnswerItem surveyAnswerItem = surveyAnswerItems.get(i);
				com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem item = new com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem(surveyAnswerItem);
				if (surveyResultAnswer != null && surveyResultAnswer.getSurveyAnswerItems() != null) {
					if (surveyResultAnswer != null && surveyResultAnswer.getComments().size() > 0) {
						question.setComment(surveyResultAnswer.getComments().get(0).getComment());
					}
					for (SurveyAnswerItem answerItem : surveyResultAnswer.getSurveyAnswerItems()) {
						if (answerItem.getId().compareTo(item.getSurveyAnswerItemRef().getId()) == 0) {
							item.setSelected(true);
							break;
						}
					}
				}
				question.addAnswerItem(item);
			}
			question.setTemplatePath(MULTICHOICE_MULTISELECT_TEMPLATE);
		}else if(surveyQuestion instanceof SingleSelectSurveyQuestion){
			SingleSelectSurveyQuestion q = (SingleSelectSurveyQuestion)surveyQuestion;
			question = new com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion(surveyQuestion);
			List<SurveyAnswerItem> surveyAnswerItems =  q.getSurveyAnswers();
			Collections.sort(surveyAnswerItems);
			for(int i=0; i<surveyAnswerItems.size(); i++){
				SurveyAnswerItem surveyAnswerItem = surveyAnswerItems.get(i);
				com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem item = new com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem(surveyAnswerItem);
				if (surveyResultAnswer != null && surveyResultAnswer.getSurveyAnswerItems() != null) {
					if (surveyResultAnswer != null && surveyResultAnswer.getComments().size() > 0) {
						question.setComment(surveyResultAnswer.getComments().get(0).getComment());
					}
					for (SurveyAnswerItem answerItem : surveyResultAnswer.getSurveyAnswerItems()) {
						if (answerItem.getId().compareTo(item.getSurveyAnswerItemRef().getId()) == 0) {
							question.setSingleSelectAnswerId(item.getSurveyAnswerItemRef().getId());
							item.setSelected(true);
							break;
						}

					}
				}
				question.addAnswerItem(item);
			}
			if(q.isDropdown()){
				question.setTemplatePath(DROPDOWN_SINGLESELECT_TEMPLATE);
			}else{
				question.setTemplatePath(MULTICHOICE_SINGLESELECT_TEMPLATE);
			}
		}

		//TODO handle the other survey question types
		if(question!=null){
			surveyView.addQuestion(question);
		}
	}

	public void buildPersonalInformationQuestion(com.softech.vu360.lms.model.SurveyQuestion surveyQuestion,  SurveyResultAnswer surveyResultAnswer, Learner learner){

		com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion question = null;
		if (surveyQuestion instanceof PersonalInformationSurveyQuestion){
			PersonalInformationSurveyQuestion q = (PersonalInformationSurveyQuestion)surveyQuestion;
			question = new com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion(surveyQuestion);
			List<AvailablePersonalInformationfield> personalInformationfields =  q.getPersonalInformationfields();
			for(int i=0; i<personalInformationfields.size(); i++){
				AvailablePersonalInformationfield availablePersonalInformationfield = personalInformationfields.get(i);
				com.softech.vu360.lms.web.controller.model.survey.AvailablePersonalInformationfield item = new com.softech.vu360.lms.web.controller.model.survey.AvailablePersonalInformationfield(availablePersonalInformationfield);
				if (surveyResultAnswer != null ) {
					if (surveyResultAnswer != null && surveyResultAnswer.getComments().size() > 0) {
						question.setComment(surveyResultAnswer.getComments().get(0).getComment());
					}
					if (availablePersonalInformationfield.getAvailablePersonalInformationfieldItem().getDbColumnName().compareToIgnoreCase("FIRSTNAME") == 0) {
						item.setAnswerText(learner.getVu360User().getFirstName());
					} else if (availablePersonalInformationfield.getAvailablePersonalInformationfieldItem().getDbColumnName().compareToIgnoreCase("MIDDLENAME") == 0) {
						item.setAnswerText(learner.getVu360User().getMiddleName());
					} else if (availablePersonalInformationfield.getAvailablePersonalInformationfieldItem().getDbColumnName().compareToIgnoreCase("LASTNAME") == 0) {
						item.setAnswerText(learner.getVu360User().getLastName());
					} else if (availablePersonalInformationfield.getAvailablePersonalInformationfieldItem().getDbColumnName().compareToIgnoreCase("STREETADDRESS") == 0) {
						item.setAnswerText(learner.getLearnerProfile().getLearnerAddress().getStreetAddress());
					} else if (availablePersonalInformationfield.getAvailablePersonalInformationfieldItem().getDbColumnName().compareToIgnoreCase("STREETADDRESS2") == 0) {
						item.setAnswerText(learner.getLearnerProfile().getLearnerAddress().getStreetAddress2());
					} else if (availablePersonalInformationfield.getAvailablePersonalInformationfieldItem().getDbColumnName().compareToIgnoreCase("EMAILADDRESS") == 0) {
						item.setAnswerText(learner.getVu360User().getEmailAddress());
					} else if (availablePersonalInformationfield.getAvailablePersonalInformationfieldItem().getDbColumnName().compareToIgnoreCase("OFFICEPHONE") == 0) {
						item.setAnswerText(learner.getLearnerProfile().getOfficePhone());
					} else if (availablePersonalInformationfield.getAvailablePersonalInformationfieldItem().getDbColumnName().compareToIgnoreCase("ZONE") == 0) {
						item.setAnswerText(learner.getLearnerProfile().getTimeZone().getZone());
					}
				}
				question.addPersonalInfoItem(item);
			}
			question.setTemplatePath(PERSONAL_INFORMATION_TEMPLATE);
		}

		//TODO handle the other survey question types
		if(question!=null){
			surveyView.addQuestion(question);
		}
	}

	public void buildCustomQuestion(com.softech.vu360.lms.model.SurveyQuestion surveyQuestion, SurveyResultAnswer surveyResultAnswer){
		//The com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion objects 
		//will be built here and assembled to the surveyView
		com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion question = null;
		if(surveyQuestion instanceof TextBoxSurveyQuestion){
			TextBoxSurveyQuestion q = (TextBoxSurveyQuestion)surveyQuestion;
			question = new com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion(surveyQuestion);
			if (surveyResultAnswer != null) {
				question.setAnswerText(surveyResultAnswer.getSurveyAnswerText());
				if (surveyResultAnswer.getComments() != null && surveyResultAnswer.getComments().size() > 0) {
					question.setComment(surveyResultAnswer.getComments().get(0).getComment());
				}
			}
			if(q.isSingleLineResponse()){
				question.setTemplatePath(COMMENT_LIMITEDTEXT_TEMPLATE);
			} else {
				question.setTemplatePath(COMMENT_UNLIMITEDTEXT_TEMPLATE);
			}
		}else if(surveyQuestion instanceof MultipleSelectSurveyQuestion){
			MultipleSelectSurveyQuestion q = (MultipleSelectSurveyQuestion)surveyQuestion;
			question = new com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion(surveyQuestion);
			List<SurveyAnswerItem> surveyAnswerItems =  q.getSurveyAnswers();
			Collections.sort(surveyAnswerItems);
			for(int i=0; i<surveyAnswerItems.size(); i++){
				SurveyAnswerItem surveyAnswerItem = surveyAnswerItems.get(i);
				com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem item = new com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem(surveyAnswerItem);
				if (surveyResultAnswer != null && surveyResultAnswer.getSurveyAnswerItems() != null) {
					if (surveyResultAnswer != null && surveyResultAnswer.getComments().size() > 0) {
						question.setComment(surveyResultAnswer.getComments().get(0).getComment());
					}
					for (SurveyAnswerItem answerItem : surveyResultAnswer.getSurveyAnswerItems()) {
						if (answerItem.getId().compareTo(item.getSurveyAnswerItemRef().getId()) == 0) {
							item.setSelected(true);
							break;
						}
					}
				}
				question.addAnswerItem(item);
			}
			question.setTemplatePath(MULTICHOICE_MULTISELECT_TEMPLATE);
		}else if(surveyQuestion instanceof SingleSelectSurveyQuestion){
			SingleSelectSurveyQuestion q = (SingleSelectSurveyQuestion)surveyQuestion;
			question = new com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion(surveyQuestion);
			List<SurveyAnswerItem> surveyAnswerItems =  q.getSurveyAnswers();
			Collections.sort(surveyAnswerItems);
			for(int i=0; i<surveyAnswerItems.size(); i++){
				SurveyAnswerItem surveyAnswerItem = surveyAnswerItems.get(i);
				com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem item = new com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem(surveyAnswerItem);
				if (surveyResultAnswer != null && surveyResultAnswer.getSurveyAnswerItems() != null) {
					if (surveyResultAnswer != null && surveyResultAnswer.getComments().size() > 0) {
						question.setComment(surveyResultAnswer.getComments().get(0).getComment());
					}
					for (SurveyAnswerItem answerItem : surveyResultAnswer.getSurveyAnswerItems()) {
						if (answerItem.getId().compareTo(item.getSurveyAnswerItemRef().getId()) == 0) {
							question.setSingleSelectAnswerId(item.getSurveyAnswerItemRef().getId());
							item.setSelected(true);
							break;
						}
						
					}
				}
				question.addAnswerItem(item);
			}
			if(q.isDropdown()){
				question.setTemplatePath(DROPDOWN_SINGLESELECT_TEMPLATE);
			}else{
				question.setTemplatePath(MULTICHOICE_SINGLESELECT_TEMPLATE);
			}
		}
		
		//TODO handle the other survey question types
		if(question!=null){
			surveyView.addQuestion(question);
		}
	}
	
	
	public void buildAggregateSurveyQuestion(com.softech.vu360.lms.model.AggregateSurveyQuestion surveyQuestion, 
			List<com.softech.vu360.lms.model.AggregateSurveyQuestionItem> aggregateSurveyQuestionItems,  List<SurveyResultAnswer> ResultAnswersList){
		com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion question = null;
		if(surveyQuestion instanceof AggregateSurveyQuestion){
			AggregateSurveyQuestion q = (AggregateSurveyQuestion)surveyQuestion;
			question = new com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion(q);
			SurveyResponseBuilder builder = new SurveyResponseBuilder(q.getSurvey()); 
			//Collections.sort(aggregateSurveyQuestionItems);
			int i=0;
			for( AggregateSurveyQuestionItem ansItem : aggregateSurveyQuestionItems) {
				
					builder.buildCustomQuestion(ansItem.getQuestion(),ResultAnswersList.get(i));
					i++;
			}
			com.softech.vu360.lms.web.controller.model.survey.Survey aggrSurveyView = builder.getSurveyView();
			/*List<SurveyAnswerItem> surveyAnswerItems =  q.getSurveyAnswers();
			Collections.sort(surveyAnswerItems);
			for(int i=0; i<surveyAnswerItems.size(); i++){
				SurveyAnswerItem surveyAnswerItem = surveyAnswerItems.get(i);
				com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem item = new com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem(surveyAnswerItem);
				question.addAnswerItem(item);
			}*/
			question.setTemplatePath(CUSTOM_MULTICHOICE_MULTISELECT_TEMPLATE);
			question.setSurveyView(aggrSurveyView);
			
			if(question!=null){
				/*if (surveyView.getAggrSurveyRef() == null) {
					surveyView.setAggrSurveyRef(new com.softech.vu360.lms.web.controller.model.survey.Survey(q.getSurvey()));
					surveyView.setAggrSurveyRef(aggrSurveyView);
				} else {
					surveyView.setAggrSurveyRef(aggrSurveyView);
				}*/
				surveyView.addQuestion(question);
			}
		}
		
	}

	public void buildSuggestedTrainingQuestion(com.softech.vu360.lms.model.SurveyQuestion surveyQuestion, List<SurveyAnswerItem> surveyAnswerItemList){
		//The com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion objects 
		//will be built here and assembled to the surveyView
		com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion question = null;
		if(surveyQuestion instanceof TextBoxSurveyQuestion){
			TextBoxSurveyQuestion q = (TextBoxSurveyQuestion)surveyQuestion;
			question = new com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion(surveyQuestion);
			if(q.isSingleLineResponse()){
				question.setTemplatePath(COMMENT_LIMITEDTEXT_TEMPLATE);
			} else {
				question.setTemplatePath(COMMENT_UNLIMITEDTEXT_TEMPLATE);
			}
		}else if(surveyQuestion instanceof MultipleSelectSurveyQuestion){
			MultipleSelectSurveyQuestion q = (MultipleSelectSurveyQuestion)surveyQuestion;
			question = new com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion(surveyQuestion);
			List<SurveyAnswerItem> surveyAnswerItems =  q.getSurveyAnswers();
			Collections.sort(surveyAnswerItems);
			for(int i=0; i<surveyAnswerItems.size(); i++){
				SurveyAnswerItem surveyAnswerItem = surveyAnswerItems.get(i);
				com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem item = new com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem(surveyAnswerItem);
				if (surveyAnswerItemList != null) {
					for (SurveyAnswerItem answerItem : surveyAnswerItemList) {
						if (answerItem.getId().compareTo(item.getSurveyAnswerItemRef().getId()) == 0) {
							item.setSelected(true);
							break;
						}
					}
				}
				question.addAnswerItem(item);
			}
			question.setTemplatePath(SUGGESTED_TRAINING_MULTICHOICE_MULTISELECT_TEMPLATE);
		}else if(surveyQuestion instanceof SingleSelectSurveyQuestion){
			SingleSelectSurveyQuestion q = (SingleSelectSurveyQuestion)surveyQuestion;
			question = new com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion(surveyQuestion);
			List<SurveyAnswerItem> surveyAnswerItems =  q.getSurveyAnswers();
			Collections.sort(surveyAnswerItems);
			for(int i=0; i<surveyAnswerItems.size(); i++){
				SurveyAnswerItem surveyAnswerItem = surveyAnswerItems.get(i);
				com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem item = new com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem(surveyAnswerItem);
				if (surveyAnswerItemList != null) {
					for (SurveyAnswerItem answerItem : surveyAnswerItemList) {
						if (answerItem.getId().compareTo(item.getSurveyAnswerItemRef().getId()) == 0) {
							question.setSingleSelectAnswerId(item.getSurveyAnswerItemRef().getId());
							item.setSelected(true);
							break;
						}

					}
				}
				question.addAnswerItem(item);
			}
			if(q.isDropdown()){
				question.setTemplatePath(DROPDOWN_SINGLESELECT_TEMPLATE);
			}else{
				question.setTemplatePath(SUGGESTED_TRAINING_MULTICHOICE_SINGLESELECT_TEMPLATE);
			}
		}

		//TODO handle the other survey question types
		if(question!=null){
			surveyView.addQuestion(question);
		}
	}

	public com.softech.vu360.lms.web.controller.model.survey.Survey getSurveyView() {
		return surveyView;
	}

}
