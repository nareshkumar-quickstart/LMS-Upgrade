package com.softech.vu360.lms.web.controller.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.EnrollmentCourseView;
import com.softech.vu360.lms.web.controller.model.AddTrainingPlanForm;
import com.softech.vu360.lms.web.controller.model.LearnerGroupEnrollmentItem;
import com.softech.vu360.lms.web.controller.model.LearnerItemForm;
import com.softech.vu360.lms.web.controller.model.TrainingCourse;
import com.softech.vu360.lms.web.controller.model.TrainingCustomerEntitlement;

/**
 * 
 * @author Saptarshi
 *
 */
public class AddTrainingPlanValidator implements Validator {

	public boolean supports(Class clazz) {
		return AddTrainingPlanForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		AddTrainingPlanForm form = (AddTrainingPlanForm)obj;
		validateFirstPage(form,errors);
		validateSecondPage(form,errors);
		validateThirdPage(form,errors);
		validateFourthPage(form,errors);
		validateFifthPage(form,errors);
		validateSixthPage(form,errors);
		validateSeventhPage(form,errors);
		validateCourses(form, errors);
	}

	public void validateFirstPage(AddTrainingPlanForm form, Errors errors) {
		boolean coursePresent = false;
		if( StringUtils.isBlank(form.getTrainingPlanName()) ) {
			errors.rejectValue("trainingPlanName", "error.trainingPlan.planName");
		}
		// checking if any course is present...
//		for( TrainingCustomerEntitlement item : form.getCustomerEntitlements() ) {
//			if( item.getCourses() != null && item.getCourses().size() > 0 ) {
//				coursePresent = true;
//			}
//		}
//		if( coursePresent == false ) {
//			errors.rejectValue("trainingPlanName", "error.addEnrollment.EntitlementNotPresent");
//		}
	}

	public void validateSecondPage(AddTrainingPlanForm form, Errors errors) {
		if( form.getTrainingPlanMethod().equalsIgnoreCase("LearnerGroup") ) {
			if ( form.getLearnerGroupTrainingItems() == null || form.getLearnerGroupTrainingItems().isEmpty() ) {
				errors.rejectValue("trainingPlanMethod", "error.addEnrollment.learnerGroupNotPresent");
			}
		}
	}
	public void validateCourses(AddTrainingPlanForm form, Errors errors) {
		
			boolean isSelected=false;
			for(EnrollmentCourseView courseView:form.getEnrollmentCourseViewList()){
				if(courseView.isSelected()){
					isSelected=true;
					break;
				}
				
				
			}
			
			if(!isSelected)
				errors.rejectValue("enrollmentCourseViewList", "error.trainingPlan.Course");
		
	}
	
	public void validateThirdPage(AddTrainingPlanForm form, Errors errors) {
		
		boolean coursePresent = false;
		boolean anySelected = false;
		// checking if any learner group is selected...
		for(LearnerGroupEnrollmentItem lgroup : form.getLearnerGroupTrainingItems()){
			if(lgroup.isSelected() == true) {
				anySelected = true;
			}
		}
		if( anySelected == false ) {
			errors.rejectValue("selectedLearnerGroups", "error.sendMail.learnerGroupRequired");
		}
		// checking if any course is present...
		for( TrainingCustomerEntitlement item : form.getCustomerEntitlements() ) {
			if( item.getCourses() != null && item.getCourses().size() > 0 ) {
				coursePresent = true;
			}
		}
		if( coursePresent == false ) {
			errors.rejectValue("selectedLearnerGroups", "error.addEnrollment.EntitlementNotPresent");
		}
	}
	
	public void validateFourthPage(AddTrainingPlanForm form, Errors errors) {
		
		boolean coursePresent = false;
		boolean anySelected = false;
		// checking if any learner is selected...
		if(form.getLearners() == null || form.getLearners().size() == 0) {
			anySelected = false;
		} else {
			for(LearnerItemForm learner : form.getLearners()) {
				if(learner.isSelected() == true) {
					anySelected = true;
				}
			}
		}
		if( anySelected == false ) {
			errors.rejectValue("learners", "error.sendMail.LearnerRequired");
		}
		// checking if any course is present...
		for( TrainingCustomerEntitlement item : form.getCustomerEntitlements() ) {
			if( item.getCourses() != null && item.getCourses().size() > 0 ) {
				coursePresent = true;
			}
		}
		if( coursePresent == false ) {
			errors.rejectValue("learners", "error.addEnrollment.EntitlementNotPresent");
		}
	}

	public void validateFifthPage(AddTrainingPlanForm form, Errors errors) {
		boolean coursePresent = false;
		if(form.getOrgGroups() == null) {
			errors.rejectValue("orgGroups", "error.sendMail.orgGroupRequired");
		}
		// checking if any course is present...
		for( TrainingCustomerEntitlement item : form.getCustomerEntitlements() ) {
			if( item.getCourses() != null && item.getCourses().size() > 0 ) {
				coursePresent = true;
			}
		}
		if( coursePresent == false ) {
			errors.rejectValue("orgGroups", "error.addEnrollment.EntitlementNotPresent");
		}
	}

	public void validateSixthPage(AddTrainingPlanForm form, Errors errors) {
		boolean anySelected = false;
		for(TrainingCustomerEntitlement trCustEnt : form.getCustomerEntitlements()) {
			if(trCustEnt.getCourses() != null){
				for(TrainingCourse trCourse : trCustEnt.getCourses()) {
					if(trCourse.getSelected() == true) {
						anySelected = true;
						break;
					}
				}
			}
		}
		
		if( anySelected == false ) {
			errors.rejectValue("customerEntitlements", "error.trainingPlan.Course");
		}
	}

	public void validateSeventhPage(AddTrainingPlanForm form, Errors errors) {
		Date date = new Date();
		date.setDate(date.getDate()-1);
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = formatter.parse(form.getAllCourseStartDate());
			endDate = formatter.parse(form.getAllCourseEndDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if( form.getAllCourseStartDate().isEmpty() || form.getAllCourseEndDate().isEmpty() ) {
			errors.rejectValue("allCourseStartDate", "error.addEnrollment.StartEndDateRequired");
		} else if ( startDate.before(date) || endDate.before(date) ) {
			errors.rejectValue("allCourseStartDate", "lms.addEnrollmentValidator.dateLessThanToday.error");
		} else if ( startDate.equals(endDate) || endDate.before(startDate) ) {
			errors.rejectValue("allCourseStartDate", "lms.addEnrollmentValidator.datesCrossed.error");
		}
	}

	public void validateSelectedCourses(AddTrainingPlanForm form, Errors errors){
		boolean anySelected = false;
		for(EnrollmentCourseView courseView : form.getEnrollmentCourseViewList()){
			
			if(courseView.isSelected()){
				anySelected=true;
				break;
			}
		}
		if( anySelected == false ) {
			errors.rejectValue("customerEntitlements", "error.trainingPlan.Course");
		}
	}

	public void validateAddEditCoursePage(AddTrainingPlanForm form, Errors errors) {
		if( StringUtils.isBlank(form.getSelectedTrainingPlanId()) ) {
			errors.rejectValue("selectedTrainingPlanId", "error.addTrainingPlanCourse.trainingPlanNotPresent");
		}
	}
}