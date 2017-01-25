
package com.softech.vu360.lms.web.controller.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.AlertTriggerFilter;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.web.controller.model.FilterTriggerForm;


public class AddFilterValidator implements Validator{
	
	private SurveyService surveyService;
	
	public boolean supports(Class clazz) {
		return FilterTriggerForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		FilterTriggerForm form = (FilterTriggerForm)obj;
		validateFirstPage(form,errors);
		/*validateThirdPage(form,errors);
		validateFourthPage(form,errors);
		validateFifthPage(form,errors);
		validateSixthPage(form,errors);*/
	}
	public void validateFirstPage( FilterTriggerForm form, Errors errors ) {
		//errors.setNestedPath("alert");
		List<AlertTriggerFilter> alertFilters = new ArrayList<AlertTriggerFilter>();
		if( StringUtils.isBlank(form.getFilterName()) ) {
			errors.rejectValue("filterName", "lms.manager.manageAlert.manageFilter.caption.provideFilterName");
		}
		
			
		if(form.getFilterName()!=null && form.getFilterName()!="" ) {
			alertFilters = surveyService.findByAlerttriggerIdAndAlerttriggerIsDeleteFalseAndIsDeleteFalse(form.getTriggerId());
			for(AlertTriggerFilter alertTriggerFilter : alertFilters){
				if(alertTriggerFilter.getFilterName().equals(form.getFilterName())){
					errors.rejectValue("filterName", "error.surveyTemplateFlag.filterName.alreadyExist");
					break;
				}
			}
		}
		
	}
	
	public void validateLearnerSelectPage(FilterTriggerForm form, Errors errors) {
		if(form.getSelectedLearner() == null || form.getSelectedLearner().length <= 0) {
			errors.rejectValue("selectedLearner", "error.recipient.learner.not.selected");
		}
	}
	
	public void validateLearnerGroupSelectPage(FilterTriggerForm form, Errors errors) {
		if(form.getLearnerGroup() == null || form.getLearnerGroup().length <= 0) {
			errors.rejectValue("learnerGroup", "error.surveyTemplateFlag.filterName.learnerGroupNotSelected");
		}
	}
	
	public void validateOrgGroupSelectPage(FilterTriggerForm form, Errors errors) {
		if(form.getGroups() == null || form.getGroups().length <= 0) {
			errors.rejectValue("groups", "error.surveyTemplateFlag.filterName.orgGroupNotSelected");
		}
	}
	
	public void validateCourseSelectPage(FilterTriggerForm form, Errors errors) {
		if(form.getCourses() == null || form.getCourses().length <= 0) {
			errors.rejectValue("courses", "error.surveyTemplateFlag.filterName.courseNotSelected");
		}
	}
	
	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	/*public void validateThirdPage(FilterTriggerForm form, Errors errors) {
			 
			boolean coursePresent = false;
			if(form.getSelectedLearner()== null) {
				errors.rejectValue("selectedLearner", "lms.error.surveyTemplateFlag.Learner.required");
			}
		
	}
	public void validateFourthPage(FilterTriggerForm form, Errors errors) {
		 
		boolean coursePresent = false;
		if(form.getLearnerGroup()== null) {
			errors.rejectValue("learnerGroup", "lms.error.surveyTemplateFlag.learnerGroup.required");
		}
	}
	public void validateFifthPage(FilterTriggerForm form, Errors errors) {
			 
			boolean coursePresent = false;
			if(form.getOrgGroup()== null) {
				errors.rejectValue("orgGroup", "lms.error.surveyTemplateFlag.orgGroup.required");
			}
		
	}
	public void validateSixthPage(FilterTriggerForm form, Errors errors) {
		 
		boolean coursePresent = false;
		if(form.getCourses()== null) {
			errors.rejectValue("courses", "lms.error.surveyTemplateFlag.course.required");
		}
	
}
	*/

}
