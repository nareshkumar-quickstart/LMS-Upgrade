package com.softech.vu360.lms.web.controller.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.web.controller.model.AssignSurveyForm;
import com.softech.vu360.lms.web.controller.model.LearnerGroupEnrollmentItem;
import com.softech.vu360.lms.web.controller.model.SurveyItem;

public class AssignSurveyValidator implements Validator {

	private static final Logger log = Logger.getLogger(AssignSurveyValidator.class.getName());
	private EntitlementService entitlementService;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;

	public boolean supports(Class clazz) {
		return AssignSurveyForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		AssignSurveyForm AssignSurveyForm = (AssignSurveyForm)obj;
		validateFirstPage(AssignSurveyForm,errors);
		validateSecondPage(AssignSurveyForm,errors);
		validateThirdPage(AssignSurveyForm,errors);
		validateFourthPage(AssignSurveyForm,errors);
		validateFifthPage(AssignSurveyForm,errors);
		validateSixthPage(AssignSurveyForm,errors);
		//validateSeventhPage(AssignSurveyForm,errors);
	}

	public void validateFirstPage(AssignSurveyForm form, Errors errors) {
		if( form.getEnrollmentMethod().equalsIgnoreCase("LearnerGroup") ) {
			if ( form.getLearnerGroupEnrollmentItems() == null || form.getLearnerGroupEnrollmentItems().isEmpty() ) {
				errors.rejectValue("enrollmentMethod", "error.addEnrollment.learnerGroupNotPresent");
			}else {
				// do nothing
			}
		}
	}

	public void validateSecondPage(AssignSurveyForm form, Errors errors) {

		boolean coursePresent = false;
		//boolean orgGroupEntitlemetPresent =true;
		if( form.getSelectedLearners() == null || form.getSelectedLearners().size() <= 0 ) {
			errors.rejectValue("learners", "lms.assignSurvey.LearnerRequired");
		}/* else if( form.getCourseEntitlementItems() == null || form.getCourseEntitlementItems().isEmpty() ) {
			errors.rejectValue("learners", "error.addEnrollment.EntitlementNotPresent");
		} 
		for( CourseEntitlementItem item : form.getCourseEntitlementItems() ) {
			if( item.getCourses() != null && item.getCourses().size() > 0 ) {
				coursePresent = true;
			}
		}
		if( coursePresent == false ) {
			errors.rejectValue("learners", "error.addEnrollment.EntitlementNotPresent");
		}
		*/
	}

	public void validateThirdPage(AssignSurveyForm form, Errors errors) {
 
		boolean coursePresent = false;
		if(form.getGroups() == null) {
			errors.rejectValue("groups", "lms.assignSurvey.orgGroupRequired");
		}
		
		/*else if( form.getCourseEntitlementItems() == null || form.getCourseEntitlementItems().isEmpty() ) {
			errors.rejectValue("groups", "error.addEnrollment.EntitlementNotPresent");
		} 
		for( CourseEntitlementItem item : form.getCourseEntitlementItems() ) {

			if( item.getCourses() != null && item.getCourses().size() > 0 ) {
				coursePresent = true;
			}
		}
		if( coursePresent == false ) {
			errors.rejectValue("groups", "error.addEnrollment.EntitlementNotPresent");
		}
		*/
	}

	public void validateFourthPage(AssignSurveyForm form, Errors errors) {

		boolean coursePresent = false;
		boolean anySelected = false;
		for(LearnerGroupEnrollmentItem lgroup : form.getLearnerGroupEnrollmentItems()){
			if(lgroup.isSelected() == true) {
				anySelected = true;
			}
		}
		if( anySelected == false ) {
			errors.rejectValue("selectedLearnerGroups", "lms.assignSurvey.caption.learnerGroupRequired");
		}
		/*
		   if( form.getCourseEntitlementItems() == null || form.getCourseEntitlementItems().isEmpty() ) {
		 
			errors.rejectValue("selectedLearnerGroups", "error.addEnrollment.EntitlementNotPresent");
		} 
		for( CourseEntitlementItem item : form.getCourseEntitlementItems() ) {
			if( item.getCourses() != null && item.getCourses().size() > 0 ) {
				coursePresent = true;
			}
		}
		if( coursePresent == false ) {
			errors.rejectValue("selectedLearnerGroups", "error.addEnrollment.EntitlementNotPresent");
		}
		*/
	}

	public void validateFifthPage(AssignSurveyForm form, Errors errors) {
		boolean anySelected = false;
		if( ! form.getCourseSearchStay().trim().equalsIgnoreCase("stay")){
			
			for( SurveyItem surveyItem : form.getSurveyItemList() ) {
				 
				if( surveyItem.isSelected() ) {
					anySelected = true;
					break;
				}
				 
			}
			if( anySelected == false ) {
				errors.rejectValue("surveyItemList", "lms.assignSurvey.searchSurvey.caption.selectSurvey");
			}
		}
		
		for( SurveyItem surveyItem : form.getSurveyItemList() ) {
			if( surveyItem.isSelected() && surveyItem.getSurvey().isInspection() ) {
				if( (form.getEnrollmentMethod().equalsIgnoreCase("Learner") && form.getSelectedLearners().size()!=1)
					|| (!form.getEnrollmentMethod().equalsIgnoreCase("Learner"))
				) {
					errors.rejectValue("surveyItemList", "lms.assignSurvey.searchSurvey.caption.inspection.single.user.only");
					break;
				}
			}
		}
	}

	public void validateSixthPage(AssignSurveyForm form, Errors errors) {
 
		
		int error = 1;
		Date date = new Date(System.currentTimeMillis());
		date=DateUtils.truncate(date, Calendar.DATE);
		//date.setDate(date.getDate()-1);
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date startDate = null;
		Date endDate = null;

		if( StringUtils.isBlank(form.getSurveyDateAssignment()) ){
			
			errors.rejectValue("allSurveyStartDate", "lms.assignSurvey.selectDateOption.error");
			return ;
		}
		
		if( form.getSurveyDateAssignment().equalsIgnoreCase("open") ) {
			return ;  // no validation required
		}
		
		if( form.getSurveyDateAssignment().equalsIgnoreCase("all") ) 
		{
			try 
			{
				if( form.getAllSurveyStartDate().isEmpty() || form.getAllSurveyEndDate().isEmpty() ) 
				{
					errors.rejectValue("allSurveyStartDate", "lms.assignSurvey.StartEndDateRequired");
				} 
				else 
				{
					startDate = formatter.parse(form.getAllSurveyStartDate());
					endDate = formatter.parse(form.getAllSurveyEndDate());
					startDate=DateUtils.truncate(startDate, Calendar.DATE);
					endDate=DateUtils.truncate(endDate, Calendar.DATE);
					if ( startDate.before(date) || endDate.before(date) ) 
					{
						errors.rejectValue("allSurveyStartDate", "lms.assignSurvey.dateLessThanToday.error");
					} 
					else if ( endDate.before(startDate) ) 
					{
						errors.rejectValue("allSurveyStartDate", "lms.assignSurvey.datesCrossed.error");
					} 
				
				}
			} catch (ParseException e) 
			{
				log.error("Exception occured while validating enrollment",e);
				errors.rejectValue("allSurveyStartDate", "error.addEnrollment.enrollmentInvalidDate");
			}
		} 
		else if( form.getSurveyDateAssignment().equalsIgnoreCase("individual") )
		{ 
		 
			for( SurveyItem surveyItem : form.getSurveyItemList()) 
			{
				if(surveyItem.isSelected()) 
				{
					
					if(StringUtils.isBlank(surveyItem.getSurveyStartDate()) || StringUtils.isBlank(surveyItem.getSurveyEndDate())) 
					{
						error = 0;
						errors.rejectValue("allSurveyStartDate", "lms.assignSurvey.StartEndDateRequired");
						break;
					} 
					else 
					{
						try 
						{

							startDate = formatter.parse(surveyItem.getSurveyStartDate());
							startDate = DateUtils.truncate(startDate, Calendar.DATE);
							endDate = formatter.parse(surveyItem.getSurveyEndDate() );
							endDate = DateUtils.truncate(endDate, Calendar.DATE);
 
							if ( startDate.before(date) || endDate.before(date) ) 
							{
								error = 0;
								errors.rejectValue("allSurveyStartDate", "lms.assignSurvey.dateLessThanToday.error");
								break;
							} 
							else if ( endDate.before(startDate) ) 
							{
								error = 0;
								errors.rejectValue("allSurveyStartDate", "lms.assignSurvey.datesCrossed.error");
								break;
							} 
						
						} catch (ParseException e) 
						{
							log.error("Exception occured while validating enrollment",e);
							error = 0;
							errors.rejectValue("allSurveyStartDate", "error.addEnrollment.enrollmentInvalidDate");
							break;
						}
					}
				}
				if( error == 0 )
					break;				
			}

		}
		 
	}
	
		
	public void validateEighthPage(AssignSurveyForm form, Errors errors) {

		if( form.getSelectedCustomers() == null || form.getSelectedCustomers().size() <= 0 ) {
			errors.rejectValue("customers", "lms.assignSurvey.CustomerRequired");
		}
	}
		
	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	public void setOrgGroupLearnerGroupService(
			OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

}