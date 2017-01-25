package com.softech.vu360.lms.web.controller.validator;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.SurveyAnswerItem;
import com.softech.vu360.lms.model.SurveyQuestion;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.web.controller.manager.ManageSurveyController;
import com.softech.vu360.lms.web.controller.manager.ManageSurveyWizardController;
import com.softech.vu360.lms.web.controller.model.AdminSurveyForm;
import com.softech.vu360.lms.web.controller.model.SurveyCourse;
import com.softech.vu360.util.FieldsValidation;





public class AdminSurveyValidator implements Validator {

	private SurveyService surveyService = null;

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return AdminSurveyForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		AdminSurveyForm form = (AdminSurveyForm)obj;
		validateFirstPage(form,errors);
		//validateSecondPage(form,errors);
		//validateQuestion(form,errors);
		//validateFinishPage(form,errors);
	}

	public void validateFirstPage( AdminSurveyForm form, Errors errors ) {
		if( StringUtils.isBlank(form.getSurveyName()) ) {
			errors.rejectValue("surveyName", "error.surveyName.required");
		}
		if( StringUtils.isBlank(form.getSurveyEvent()) )	{
			errors.rejectValue("surveyEvent", "error.surveyEvent.required");
		}
		if( form.isAllQuestionPerPage() == false ) {
			if( StringUtils.isBlank(form.getQuestionsPerPage()) ) {
				errors.rejectValue("questionsPerPage", "error.questionsPerPageBlank.required");
			} else if ( !StringUtils.isNumeric(form.getQuestionsPerPage()) )
				errors.rejectValue("questionsPerPage", "error.questionsPerPageNumeric.required");
		}
		if( form.isElectronicSignature() ) {
			if( StringUtils.isBlank(form.getElectronicSignatureValue()) ) {
				errors.rejectValue("electronicSignatureValue", "error.manager.addSurvey.eSignature");
			}
		}
		if( form.isLinks() ) {
			if( StringUtils.isBlank(form.getLinksValue()) ) {
				errors.rejectValue("linksValue", "error.manager.addSurvey.links");
			}
		}
	}

	/*public void validateSurveyName(ManageSurveyForm form, Errors errors) {
		Survey survey = surveyService.findSurveyByName(form.getSurveyName());
		if(form.getSid() == 0) {
			if(survey != null)
				errors.rejectValue("surveyName", "error.surveyName.exists");
		} else {
			if(survey != null && form.getSid() != survey.getId())
				errors.rejectValue("surveyName", "error.surveyName.Change");
		}
	}*/

	public void validateSecondPage(AdminSurveyForm form, Errors errors) {
		if(form.getSurveyQuestions().size() == 0){
			errors.rejectValue("surveyQuestions", "error.surveyQuestions.required");
		}
	}

	public void validateQuestion( AdminSurveyForm form, Errors errors ) {
		
		String questionType = form.getSurveyQuestionType();
		//form.getSurveyQuestionText()=="" is added to validate blank question LMS-9775
		if( questionType.equalsIgnoreCase(ManageSurveyWizardController.SURVEY_QUESTION_MULTIPLE_CHOICE_MULTIPLE_SELECT) ) {
			errors.setNestedPath("currentMultipleSelectSurveyQuestion");
			if( StringUtils.isBlank(form.getCurrentMultipleSelectSurveyQuestion().getText()) && form.getSurveyQuestionText()=="") {
				errors.rejectValue("text", "error.question.required");
			}
			this.validateAnswers(form.getCurrentMultipleSelectSurveyQuestion(), errors);
			//this.checkQuestion(form,form.getCurrentMultipleSelectSurveyQuestion().getText(),errors);
		} else if ( questionType.equalsIgnoreCase(ManageSurveyWizardController.SURVEY_QUESTION_MULTIPLE_CHOICE_SINGLE_SELECT) ) {
			errors.setNestedPath("currentSingleSelectSurveyQuestion");
			if( StringUtils.isBlank(form.getCurrentSingleSelectSurveyQuestion().getText()) && form.getSurveyQuestionText()=="") {
				errors.rejectValue("text", "error.question.required");
			}
			this.validateAnswers(form.getCurrentSingleSelectSurveyQuestion(), errors);
			//this.checkQuestion(form,form.getCurrentSingleSelectSurveyQuestion().getText(),errors);
		} else if ( questionType.equalsIgnoreCase(ManageSurveyWizardController.SURVEY_QUESTION_DROP_DOWN_SINGLE_SELECT) ) {
			errors.setNestedPath("currentSingleSelectSurveyQuestion");
			if( StringUtils.isBlank(form.getCurrentSingleSelectSurveyQuestion().getText()) && form.getSurveyQuestionText()=="") {
				errors.rejectValue("text", "error.question.required");
			}
			this.validateAnswers(form.getCurrentSingleSelectSurveyQuestion(), errors);
			//this.checkQuestion(form,form.getCurrentSingleSelectSurveyQuestion().getText(),errors);
		} else if ( questionType.equalsIgnoreCase(ManageSurveyWizardController.SURVEY_QUESTION_TEXT_BOX_256_CHARACTERS) ) {
			errors.setNestedPath("currentFillInTheBlankSurveyQuestion");
			if( StringUtils.isBlank(form.getCurrentFillInTheBlankSurveyQuestion().getText()) && form.getSurveyQuestionText()=="") {
				errors.rejectValue("text", "error.question.required");
			}
			//this.checkQuestion(form,form.getCurrentFillInTheBlankSurveyQuestion().getText(),errors);
		} else if ( questionType.equalsIgnoreCase(ManageSurveyWizardController.SURVEY_QUESTION_TEXT_BOX_UNLIMITED) ) {
			errors.setNestedPath("currentFillInTheBlankSurveyQuestion");
			if( StringUtils.isBlank(form.getCurrentFillInTheBlankSurveyQuestion().getText()) && form.getSurveyQuestionText()=="") {
				errors.rejectValue("text", "error.question.required");
			}
			//this.checkQuestion(form,form.getCurrentFillInTheBlankSurveyQuestion().getText(),errors);
		} else if  ( questionType.equalsIgnoreCase(ManageSurveyWizardController.SURVEY_QUESTION_MULTIPLE_CHOICE_RATING_SELECT) ) {
			errors.setNestedPath("currentRatingQuestion");

		} else if ( questionType.equalsIgnoreCase(ManageSurveyController.SURVEY_QUESTION_CUSTOM) ) {
			errors.setNestedPath("currentCustomSurveyQuestion");
			if( StringUtils.isBlank(form.getCurrentCustomSurveyQuestion().getText()) && form.getSurveyQuestionText()=="") {
				errors.rejectValue("text", "error.question.required");
			}
			errors.setNestedPath("");
			
			for( int i = 0 ; i < form.getCustomQuestionResponceTypes().size() ; i ++ ) {
				String resType = form.getCustomQuestionResponceTypes().get(i);
				String label = form.getResponceLabels().get(i);
				String choices = form.getAnswerChoices().get(i);
				if( StringUtils.isBlank(label) ) {
					errors.rejectValue("responceLabels", "error.survey.custonresponce.label");
				}
				if( resType.equalsIgnoreCase(ManageSurveyController.SURVEY_CUSTOM_RESPONCE_MULTIPLE_SELECT) || 
						resType.equalsIgnoreCase(ManageSurveyController.SURVEY_CUSTOM_RESPONCE_SINGLE_SELECT) ) {
					if( StringUtils.isBlank(choices) ) {
						errors.rejectValue("answerChoices", "error.survey.custonresponce.choices");
					}
					this.validateResponseAnswers(choices, errors);
				}
			}
		}
		errors.setNestedPath("");
	}

	private void validateAnswers( SurveyQuestion question, Errors errors ) {
		List<SurveyAnswerItem> answers = question.getSurveyAnswers();
		if( answers == null || answers.size() <= 0 ) {
			errors.rejectValue("surveyAnswerLines", "error.question.answer.required");
		}else if( answers.size() == 1 ) {
			errors.rejectValue("surveyAnswerLines", "error.question.answer.multiple");
		} else {
			for (SurveyAnswerItem answerItem : answers) {
				if(answerItem.getLabel().trim().length() > 255) {
					errors.rejectValue("surveyAnswerLines", "error.question.answer.optionLength");
					break;
				}
			}
		}
	}
	private void validateResponseAnswers(String choices, Errors errors) {
		String str;
		try {
			BufferedReader reader = new BufferedReader(new StringReader(choices));
			int i = 0;
			while ((str = reader.readLine()) != null) {
				if (str.length() <= 0) {
					errors.rejectValue("answerChoices","error.question.answer.required");
				} else if (str.trim().length() > 255) {
					errors.rejectValue("answerChoices","error.question.answer.optionLength");
				}
				i++;
			}
			if (i == 1) {
				errors.rejectValue("answerChoices",	"error.question.answer.multiple");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void validateFinishPage(AdminSurveyForm form, Errors errors) {
		//commenting out the selected course validation logic so as to allow the users to save survey by keeping courses optional 
		List<SurveyCourse> surveyCourses = form.getSurveyCourses();
		int flag = 0;
		for (int i=0;i<surveyCourses.size();i++) 
			if(surveyCourses.get(i).isSelected() == true) {
				flag = 1;
				break;
			}
		if( flag == 0 ) {
			errors.rejectValue("surveyCourses", "error.course.required");
		}
	}

	/*private void checkQuestion(ManageSurveyForm form, String question, Errors errors) {
		if ( form.getSurveyQuestions().size() > 0 ) {
			for (int i=0; i<form.getSurveyQuestions().size(); i++ ) {
				if(form.getSurveyQuestions().get(i).getText().equalsIgnoreCase(question))
					errors.rejectValue("text", "error.duplicate.question");
			}
		}
	}*/

	public void validateSaveFlag(AdminSurveyForm form, Errors errors) {
		if (StringUtils.isBlank(form.getFlag().getFlagName()))		{
			errors.rejectValue("flag.flagName", "error.surveyTemplateFlag.flagName.required");
		}
		if (StringUtils.isBlank(form.getFlag().getSubject())){
			errors.rejectValue("flag.subject", "error.surveyTemplateFlag.subject.required");
		}
		if (StringUtils.isBlank(form.getFlag().getTo())){
			errors.rejectValue("flag.to", "error.surveyTemplateFlag.to.required");
		}else if(!FieldsValidation.isEmailValid(form.getFlag().getTo())){
			errors.rejectValue("flag.to", "error.surveyTemplateFlag.to.invalidFormat","Invalid email address");
		}
		if (StringUtils.isBlank(form.getFlag().getMessage())){
			errors.rejectValue("flag.message", "error.surveyTemplateFlag.message.required");
		}
		if (form.getSelectedAnswerItems() ==null ){
			errors.rejectValue("selectedAnswerItems", "error.surveyTemplateFlag.selectedAnswerItems.required");
		}else if (form.getSelectedAnswerItems().length == 0 ){
			errors.rejectValue("selectedAnswerItems", "error.surveyTemplateFlag.selectedAnswerItems.required");
		}
	}
	public void validateSurveyResult(AdminSurveyForm form, Errors errors) {
		boolean flag=true;
		if(form.getAnsweredSurveyQyestionId()!=null){
		nextItem : for(Long deletableId : form.getSurveyQuestionsToBeDeletedFromDB()){
			if(flag){
			for(Long surveyResultId : form.getAnsweredSurveyQyestionId()){
				if(deletableId.equals(surveyResultId)){
					errors.rejectValue("flag.flagName", "error.surveyTemplateFlag.answeredQuestion.invalid");
					flag=false;
					continue nextItem;
				}
			}
			}
		}
	}
	}

	/**
	 * @return the surveyService
	 */
	public SurveyService getSurveyService() {
		return surveyService;
	}

	/**
	 * @param surveyService the surveyService to set
	 */
	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

}
