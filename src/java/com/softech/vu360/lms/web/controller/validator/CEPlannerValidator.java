package com.softech.vu360.lms.web.controller.validator;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.CommentSurveyQuestion;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.MultipleSelectSurveyQuestion;
import com.softech.vu360.lms.model.PersonalInformationSurveyQuestion;
import com.softech.vu360.lms.model.SingleSelectSurveyQuestion;
import com.softech.vu360.lms.model.TextBoxSurveyQuestion;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.model.CEPlannerForm;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.FieldsValidation;
import com.softech.vu360.util.VU360Branding;




/**
 * @author Dyutiman
 * created on 19th June 2010
 *
 */

public class CEPlannerValidator implements Validator {
	private VU360UserService vu360UserService;
	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return CEPlannerForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		CEPlannerForm form = (CEPlannerForm)obj;
		validateFirstPage(form, errors);
		validateFourthPage(form, errors);
	}
	

	private void validatePasswords(CEPlannerForm cEPlannerForm, Errors errors) {
        if (StringUtils.isBlank(cEPlannerForm.getPassword())) {
            errors.rejectValue("password", "error.password.required","Password required");
        } else if ( !FieldsValidation.isPasswordCorrect(cEPlannerForm.getPassword()) ) {

        	errors.rejectValue("password", "error.password.invalidlength","	Password must contain alphabets and numbers and must be at least 8 characters long");
        }

        if (StringUtils.isBlank(cEPlannerForm.getConfirmpassword())) {
            errors.rejectValue("confirmpassword", "error.addNewLearner.confirmPassword.required","Confirm Password required");
        } else if (StringUtils.length(cEPlannerForm.getConfirmpassword()) < 8) {
            errors.rejectValue("confirmpassword", "error.addNewLearner.confirmPassword.invalidlength" , "Invalid length of Confirm password");
        }

        if (!StringUtils.equals(cEPlannerForm.getPassword(), cEPlannerForm.getConfirmpassword())) {
            errors.rejectValue("emailAdd", "error.password.matchPassword","Password and confirm password do not match");
        }
    }
	
	 
	public void validateFirstPage(CEPlannerForm form, Errors errors) {
		
		validatePasswords(form, errors); 
		
		if( StringUtils.isBlank(form.getCompanyName()) ) {
			errors.rejectValue("companyName", "error.cePlanner.companyName.required");
		}
		if( StringUtils.isBlank(form.getFirstName()) ) {
			errors.rejectValue("firstName", "error.cePlanner.firstName.required");
		}
		if( StringUtils.isBlank(form.getLastName()) ) {
			errors.rejectValue("lastName", "error.cePlanner.lastName.required");
		}
		if( StringUtils.isBlank(form.getEmailAdd()) || !FieldsValidation.isEmailValid(form.getEmailAdd())){
			errors.rejectValue("emailAdd", "error.cePlanner.email.required");
		}
		if( StringUtils.isBlank(form.getUsername()) ) {
			errors.rejectValue("username", "error.cePlanner.username.required");
		}
//		if( StringUtils.isBlank(form.getSurveyId()) ||  form.getSurveyId().equals(null)) {
//			errors.rejectValue("surveyId", "error.cePlanner.survey.required");
//		}
		/*if( StringUtils.isBlank(form.getPassword()) ) {
			errors.rejectValue("password", "error.cePlanner.password.required");
		}
		if( StringUtils.isBlank(form.getConfirmpassword()) ) {
			errors.rejectValue("confirmpassword", "error.cePlanner.confirmpassword.required");
		} else {
			if( !StringUtils.isBlank(form.getPassword()) ) {
				if( !form.getPassword().equals(form.getConfirmpassword()) ) {
					errors.rejectValue("emailAdd", "error.cePlanner.password.mismatch");
				}
			}
		}*/
		
		if( StringUtils.isBlank(form.getPhone()) ) {
			errors.rejectValue("phone", "error.cePlanner.phone.required");
		}
			
		if( StringUtils.isBlank(form.getAddress1()) ) {
			errors.rejectValue("address1", "error.cePlanner.address1.required");
		}
		
		if( StringUtils.isBlank(form.getCity1()) ) {
			errors.rejectValue("city1", "error.cePlanner.city1.required");
		}
		if( StringUtils.isBlank(form.getState1()) ) {
			errors.rejectValue("state1", "error.cePlanner.state1.required");
		}
		if( StringUtils.isBlank(form.getZip1()) ) {
			errors.rejectValue("zip1", "error.cePlanner.zip1.required");
		}
		if( StringUtils.isBlank(form.getCountry1()) ) {
			errors.rejectValue("country1", "error.cePlanner.country1.required");
		}
		if( StringUtils.isBlank(form.getAddress2()) ) {
			errors.rejectValue("address2", "error.cePlanner.address2.required");
		}
		
		if( StringUtils.isBlank(form.getCity2()) ) {
			errors.rejectValue("city2", "error.cePlanner.city2.required");
		}
		if( StringUtils.isBlank(form.getState2()) ) {
			errors.rejectValue("state2", "error.cePlanner.state2.required");
		}
		if( StringUtils.isBlank(form.getZip2()) ) {
			errors.rejectValue("zip2", "error.cePlanner.zip2.required");
		}
		if( StringUtils.isBlank(form.getCountry2()) ) {
			errors.rejectValue("country2", "error.cePlanner.country2.required");
		}
		
		
		if (!StringUtils.isBlank(form.getPhone())){
			if (FieldsValidation.isInValidOffPhone((form.getPhone()))){
				errors.rejectValue("phone", "error.addNewLearner.Phone.all.invalidText", "Bad characters not allowed (Phone)");
			}
		}
		
		
		if (!StringUtils.isBlank(form.getCity1())){
			if (FieldsValidation.isInValidGlobalName(form.getCity1())){
				errors.rejectValue("city1", "error.addNewLearner.city2.all.invalidText" ,"Bad characters not allowed (City1)");
			}
		}
		
		if (!StringUtils.isBlank(form.getCity2())){
			if (FieldsValidation.isInValidGlobalName(form.getCity2())){
				errors.rejectValue("city2", "error.addNewLearner.city1.all.invalidText" ,"Bad characters not allowed (City2)");
			}
		}
		
		

		
		//for zip code validation
		
		
			String country = null ;
			String zipCode = null ;
			// -----------------------------------------------------------------------------
			// 			for learner address 1 Zip Code   									//
			// -----------------------------------------------------------------------------
			com.softech.vu360.lms.vo.Language lang=new com.softech.vu360.lms.vo.Language();
			lang.setLanguage(Language.DEFAULT_LANG);
			Brander brander = VU360Branding.getInstance().getBrander("default", lang);
			country = form.getCountry1();
			zipCode = form.getZip1();

            if( ! ZipCodeValidator.isZipCodeValid(country, zipCode, brander, null) ) {
            	
            	errors.rejectValue("zip1", ZipCodeValidator.getCountryZipCodeError(form.getCountry1(), brander) ,brander.getBrandElement(ZipCodeValidator.getCountryZipCodeError(form.getCountry1(), brander)));
            }				
		
			// -----------------------------------------------------------------------------
			// 			for learner address 2 Zip Code   									//
			// -----------------------------------------------------------------------------
			country = form.getCountry2();
			zipCode = form.getZip2();

            if( ! ZipCodeValidator.isZipCodeValid(country, zipCode, brander, null) ) {
            	
            	errors.rejectValue("zip2", ZipCodeValidator.getCountryZipCodeError(form.getCountry2(), brander) , brander.getBrandElement(ZipCodeValidator.getCountryZipCodeError(form.getCountry2(), brander)) );
            }	
			
		
	}
	
	public void validateFourthPage(CEPlannerForm form, Errors errors) {
		
		for( int i = 0 ; i <= form.getNumberOfLearners() ; i ++ ) {
			if( StringUtils.isBlank(form.getFirstNames().get(i)) ) {
				errors.rejectValue("firstNames", "error.cePlanner.firstNames.required");
				break;
			}
		}
		for( int i = 0 ; i <= form.getNumberOfLearners() ; i ++ ) {
			if( StringUtils.isBlank(form.getLastNames().get(i)) ) {
				errors.rejectValue("lastNames", "error.cePlanner.lastNames.required");
				break;
			}
		}
		for( int i = 0 ; i <= form.getNumberOfLearners() ; i ++ ) {
			if( StringUtils.isBlank(form.getEmailAddresses().get(i)) ) {
				errors.rejectValue("emailAddresses", "error.cePlanner.email-addresses.required");
				break;
			}
			
		}
		int equalFlag=0;
		
		for( int i = 0 ; i <= form.getNumberOfLearners() ; i ++ ) {
			
			if (vu360UserService.isEmailAddressInUse((form.getEmailAddresses().get(i)))) {
				errors.rejectValue("emailAddresses", "error.cePlanner.email-addresses.required.exist");
				equalFlag=1;
				break;
			}
			if(form.getNumberOfLearners()>1){
				for( int j = i+1 ; j <= form.getNumberOfLearners() ; j ++ ) {
					
					if((form.getEmailAddresses().get(i)).equals(form.getEmailAddresses().get(j))){
					errors.rejectValue("emailAddresses", "error.cePlanner.email-addresses.required.unique");
					
					equalFlag=1;
					break;
					}
					if(equalFlag==1)
					break;
					
				}
			}
			if(equalFlag==1)
			break;
			
		}
		for( int i = 0 ; i <= form.getNumberOfLearners(); i ++ ) {
				if (StringUtils.isBlank(form.getPasswords().get(i))) {
		            errors.rejectValue("password", "error.password.required","Password required");
		            break;
		        } else if ( !FieldsValidation.isPasswordCorrect(form.getPasswords().get(i)) ) {
		        	errors.rejectValue("password", "error.password.invalidlength","	Password must contain alphabets and numbers and must be at least 8 characters long");
		        	break;
		        }
		}
	}
	
	public void validateThirdPage(CEPlannerForm form, Errors errors) {
		if(StringUtils.isBlank(form.getNumberOfReps()) ||  !StringUtils.isNumeric(form.getNumberOfReps()) || Integer.parseInt(form.getNumberOfReps())<1){
			errors.rejectValue("numberOfReps", "error.cePlanner.numberOfReps.required");			
		}		
	}

	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	public void validateSurvey(CEPlannerForm form, Errors errors) {
		
		//String [] questionsArray = request.getParameterValues("questionsArray");
		boolean selected =false ; 
		//log.debug(questionsArray.length);
		//TakeSurveyForm form = form.getTakeSurveyForm();
		// get all survey questions

//		for(String questionIdString : questionsArray){
//			long questionId =0 ;
//			try{
//				questionId = Long.parseLong(questionIdString);
//
//			}catch(NumberFormatException e){
//				e.printStackTrace();
//				continue;
//			}
			int listCount=0;
			
			for(int i=0;i<form.getSurvey().getQuestionList().size(); i++){
				//if( questionId == takeSurveyForm.getSurvey().getQuestionList().get(i).getSurveyQuestionRef().getId().longValue()  ) { // if question is found in list


					com.softech.vu360.lms.model.SurveyQuestion surveyQuestion = form.getSurvey().getQuestionList().get(i).getSurveyQuestionRef();

					if(surveyQuestion instanceof CommentSurveyQuestion){

						if( StringUtils.isBlank( form.getSurvey().getQuestionList().get(listCount).getAnswerText() ) ) {
							errors.rejectValue("survey.questionList["+listCount+"]", "error" ,"errorDetails");
						}

					}else if(surveyQuestion instanceof MultipleSelectSurveyQuestion){
						
						if(form.getSurvey().getQuestionList().get(listCount).getSurveyQuestionRef().getRequired().booleanValue()){
							selected = false;
							for ( int k=0 ; k<form.getSurvey().getQuestionList().get(listCount).getAnswerItems().size() ; k++) {								
								if (form.getSurvey().getQuestionList().get(listCount).getAnswerItems().get(k).isSelected()) {
									selected = true ;
								}
							}
							if( selected == false ){ // if selected is still set to false
								errors.rejectValue("survey.questionList["+listCount+"]", "error" ,"errorDetails");
							}
						}

						
						if(i==form.getSurvey().getQuestionList().size()){
							return;
						}
					}else if(surveyQuestion instanceof SingleSelectSurveyQuestion){

						/*if(form.getSurvey().getQuestionList().get(listCount).getSingleSelectAnswerId() ==null){

							errors.rejectValue("survey.questionList["+listCount+"]", "error" ,"errorDetails");
						}*/	
						
						if(form.getSurvey().getQuestionList().get(listCount).getSurveyQuestionRef().getRequired().booleanValue()){
							selected = false;																
							if (form.getSurvey().getQuestionList().get(listCount).getSingleSelectAnswerId() != null) {
								selected = true ;
							}								
							if( selected == false ){ // if selected is still set to false
								errors.rejectValue("survey.questionList["+listCount+"]", "error" ,"errorDetails");
							}
						}							
						if(i==form.getSurvey().getQuestionList().size()){
							return;
						}

					} else if(surveyQuestion instanceof PersonalInformationSurveyQuestion){
						List<com.softech.vu360.lms.web.controller.model.survey.AvailablePersonalInformationfield> personalInfoItems = form.getSurvey().getQuestionList().get(listCount).getPersonalInfoItems();
						boolean flag = false;
						if(!surveyQuestion.getRequired().booleanValue()){ // if all answer required is not selected
							for (com.softech.vu360.lms.web.controller.model.survey.AvailablePersonalInformationfield aPIF : personalInfoItems) {
								if (aPIF.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().equalsIgnoreCase("FIRSTNAME")) {
									if (aPIF.getAnswerText().isEmpty() && aPIF.getAvailablePersonalInformationfieldRef().isFieldsRequired()) {
										flag = true;
										errors.rejectValue("survey.questionList["+listCount+"]", "First Name is required");
										
									}
								} else if (aPIF.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().equalsIgnoreCase("LASTNAME")) {
									if (aPIF.getAnswerText().isEmpty() && aPIF.getAvailablePersonalInformationfieldRef().isFieldsRequired()) {
										flag = true;
										errors.rejectValue("survey.questionList["+listCount+"]", "Last Name is required");
										
									}
								} else if (aPIF.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().equalsIgnoreCase("EMAILADDRESS")) {
									if (aPIF.getAnswerText().isEmpty() && aPIF.getAvailablePersonalInformationfieldRef().isFieldsRequired()) {
										errors.rejectValue("survey.questionList["+listCount+"]", "Email Address is required");
										flag = true;
										
									}
									if ( !FieldsValidation.isEmailValid(aPIF.getAnswerText()) && aPIF.getAvailablePersonalInformationfieldRef().isFieldsRequired()) {
										errors.rejectValue("survey.questionList["+listCount+"]", "Email Address is required");
										flag = true;
										
									}
								}else if (aPIF.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().equalsIgnoreCase("MIDDLENAME")) {
									if (aPIF.getAnswerText().isEmpty() && aPIF.getAvailablePersonalInformationfieldRef().isFieldsRequired()) {
										errors.rejectValue("survey.questionList["+listCount+"]", "Middle Name is required");
										flag = true;
										
									}
								}else if (aPIF.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().equalsIgnoreCase("STREETADDRESS")) {
									if (aPIF.getAnswerText().isEmpty() && aPIF.getAvailablePersonalInformationfieldRef().isFieldsRequired()) {
										errors.rejectValue("survey.questionList["+listCount+"]", "Street Address is required");
										flag = true;
										
									}
								}else if (aPIF.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().equalsIgnoreCase("STREETADDRESS2")) {
									if (aPIF.getAnswerText().isEmpty() && aPIF.getAvailablePersonalInformationfieldRef().isFieldsRequired()) {
										errors.rejectValue("survey.questionList["+listCount+"]", "Street Address2 is required");
										flag = true;
										
									}
								
								}else if (aPIF.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().equalsIgnoreCase("OFFICEPHONE")) {
									if (aPIF.getAnswerText().isEmpty() && aPIF.getAvailablePersonalInformationfieldRef().isFieldsRequired()) {
										errors.rejectValue("survey.questionList["+listCount+"]", "Office Phone is required");
										flag = true;
										
									}
								
								}else if (aPIF.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().equalsIgnoreCase("ZONE")) {
									if (aPIF.getAnswerText().isEmpty() && aPIF.getAvailablePersonalInformationfieldRef().isFieldsRequired()) {
										errors.rejectValue("survey.questionList["+listCount+"]", "Prefferd Zone is required");
										flag = true;
										
									}
								}
							}
							if( flag ){		
								errors.rejectValue("survey.questionList["+listCount+"]", "error" ,"errorDetails");
							}
						}else{		// if all answer required is selected
							for (com.softech.vu360.lms.web.controller.model.survey.AvailablePersonalInformationfield aPIF : personalInfoItems) {									
								if (aPIF.getAnswerText().isEmpty()) {										
									errors.rejectValue("survey.questionList["+listCount+"]", "First Name is required");											
								}
							}
						}
					}else if(surveyQuestion instanceof TextBoxSurveyQuestion){
						if(form.getSurvey().getQuestionList().get(listCount).getSurveyQuestionRef().getRequired().booleanValue()){
							selected = false;																
							if (!StringUtils.isBlank(form.getSurvey().getQuestionList().get(listCount).getAnswerText())) {
								selected = true ;
							}								
							if( selected == false ){ // if selected is still set to false
								errors.rejectValue("survey.questionList["+listCount+"]", "error" ,"errorDetails");
							}
						}							
						if(i==form.getSurvey().getQuestionList().size()){
							return;
						}
					}


				//}
				listCount++;
			}

		//}
		
	}
	
}