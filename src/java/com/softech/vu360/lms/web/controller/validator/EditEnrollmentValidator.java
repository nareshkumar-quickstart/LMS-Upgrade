/**
 * 
 */
package com.softech.vu360.lms.web.controller.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.web.controller.model.CourseItem;
import com.softech.vu360.lms.web.controller.model.ViewLearnerEnrollmentForm;
import com.softech.vu360.lms.web.controller.model.ViewLearnerEnrollmentItem;
import com.softech.vu360.lms.web.controller.model.ViewLearnerEntitlementItem;
import com.softech.vu360.util.FieldsValidation;

/**
 * @author tapas
 *
 */
public class EditEnrollmentValidator implements Validator{


	private static final Logger log = Logger.getLogger(EditEnrollmentValidator.class.getName());

	public boolean supports(Class clazz) {
		return ViewLearnerEnrollmentForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		ViewLearnerEnrollmentForm viewLearnerEnrollmentForm = (ViewLearnerEnrollmentForm)obj;
		validateSelectEnrollment(viewLearnerEnrollmentForm,errors);
		validateEnrollmentCourses(viewLearnerEnrollmentForm,errors);
		validateExtendEnrollments(viewLearnerEnrollmentForm,errors);
		validateSwapEnrollment(viewLearnerEnrollmentForm,errors);
		validateCourseSwapEnrollment(viewLearnerEnrollmentForm,errors);
	}

	public void validateCourseSwapEnrollment(
			ViewLearnerEnrollmentForm viewLearnerEnrollmentForm, Errors errors) {
		
		boolean anySelected = false;
		int index = 0;

		List<CourseItem> courseItems = viewLearnerEnrollmentForm.getCourseItems();
		for (CourseItem courseItem : courseItems){

			if(courseItem.getSelected()){
				anySelected = true;
				index ++;
			}
		}
		if( anySelected == false ) {
			errors.rejectValue("viewLearnerEntitlementItems", "error.addEnrollment.enrollmentRequired");
		}
		if(index > 1) {
			errors.rejectValue("viewLearnerEntitlementItems", "error.addEnrollment.courseNumberExceded");
		}
	}

	@SuppressWarnings("static-access")
	public void validateSwapEnrollment(	ViewLearnerEnrollmentForm viewLearnerEnrollmentForm, Errors errors) {

		boolean anySelected = false;
		int index = 0;

		List<ViewLearnerEntitlementItem> viewLearnerEntitlementItems = viewLearnerEnrollmentForm.getViewLearnerEntitlementItems();
		for (ViewLearnerEntitlementItem viewLearnerEntitlementItem : viewLearnerEntitlementItems){

			for (ViewLearnerEnrollmentItem viewLearnerEnrollmentItem : viewLearnerEntitlementItem.getViewLearnerEnrollmentItems()){
				if(viewLearnerEnrollmentItem.isSelected()){
					anySelected = true;
					index ++;
					if(viewLearnerEnrollmentItem.getLearnerEnrollment().getEnrollmentStatus().equalsIgnoreCase(LearnerEnrollment.EXPIRED)) {
						errors.rejectValue("viewLearnerEntitlementItems", "error.addEnrollment.expiredEnrollmentNotSwapped");
					}
					// [1/7/2011] LMS-8487 :: Admin Mode > Swap Enrollment: System should not allows swapping of completed course.
					else if(viewLearnerEnrollmentItem.getLearnerEnrollment().getEnrollmentStatus().equalsIgnoreCase(LearnerEnrollment.ACTIVE)
							&& viewLearnerEnrollmentItem.getLearnerEnrollment().getCourseStatistics().isCourseCompleted()) {
						errors.rejectValue("viewLearnerEntitlementItems", "error.addEnrollment.completedEnrollmentNotSwapped");
					} else if(viewLearnerEnrollmentItem.getLearnerEnrollment().getCourseStatistics().getStatus().equalsIgnoreCase(viewLearnerEnrollmentItem.getLearnerEnrollment().getCourseStatistics().REPORTED)) {
						errors.rejectValue("viewLearnerEntitlementItems", "error.addEnrollment.reportedEnrollmentNotSwapped");
					}
				}
			}
		}
		if( anySelected == false ) {
			errors.rejectValue("viewLearnerEntitlementItems", "error.addEnrollment.enrollmentRequired");
		}
		if(index > 1) {
			errors.rejectValue("viewLearnerEntitlementItems", "error.addEnrollment.courseNumberExceded");
		}
	}
	
	public void validateUnlockEntollment(ViewLearnerEnrollmentForm viewLearnerEnrollmentForm, Errors errors) {
		boolean anySelected = false;
		boolean alrearyUnlockedMessage = true;
		int index = 0;
		final String LOCK_TYPE_VALIDATIONFAILED = "ValidationFailed";
		
		List<ViewLearnerEntitlementItem> viewLearnerEntitlementItems = viewLearnerEnrollmentForm.getViewLearnerEntitlementItems();
		for (ViewLearnerEntitlementItem viewLearnerEntitlementItem : viewLearnerEntitlementItems){

			for (ViewLearnerEnrollmentItem viewLearnerEnrollmentItem : viewLearnerEntitlementItem.getViewLearnerEnrollmentItems()){
				if(viewLearnerEnrollmentItem.isSelected()){
					anySelected = true;
					index ++;
					if(viewLearnerEnrollmentItem.getLearnerEnrollment().getEnrollmentStatus().equalsIgnoreCase(LearnerEnrollment.EXPIRED)) {
						errors.rejectValue("viewLearnerEntitlementItems", "error.addEnrollment.expiredEnrollmentNotUnlocked");
					}
					
					if((viewLearnerEnrollmentItem.getLearnerEnrollment().getLastLockedCourse() == null || 
							!viewLearnerEnrollmentItem.getLearnerEnrollment().getLastLockedCourse().isCourselocked()) && alrearyUnlockedMessage) {
						errors.rejectValue("viewLearnerEntitlementItems", "error.addEnrollment.enrollmentCourseAlreadyUnloack");
						alrearyUnlockedMessage =false;
					}
					else if((viewLearnerEnrollmentItem.getLearnerEnrollment().getLastLockedCourse() != null && 
							!viewLearnerEnrollmentItem.getLearnerEnrollment().getLastLockedCourse().getLocktype().equalsIgnoreCase(LOCK_TYPE_VALIDATIONFAILED)) && alrearyUnlockedMessage) {
						errors.rejectValue("viewLearnerEntitlementItems", "error.addEnrollment.enrollmentCourse.validationFailedLockType");
						alrearyUnlockedMessage =false;
					}
				}
			}
		}
		if( anySelected == false ) {
			errors.rejectValue("viewLearnerEntitlementItems", "error.addEnrollment.enrollmentRequired");
		}
		if(index > 1) {
			errors.rejectValue("viewLearnerEntitlementItems", "error.addEnrollment.courseNumberExceded");
		}
	}

	public boolean validateEnrollmentCourses(
			ViewLearnerEnrollmentForm viewLearnerEnrollmentForm, Errors errors) {

		boolean anyReady = false;
		List<ViewLearnerEntitlementItem> viewLearnerEntitlementItems = viewLearnerEnrollmentForm.getViewLearnerEntitlementItems();
		for (ViewLearnerEntitlementItem viewLearnerEntitlementItem : viewLearnerEntitlementItems) {

			for (ViewLearnerEnrollmentItem viewLearnerEnrollmentItem : viewLearnerEntitlementItem.getViewLearnerEnrollmentItems()){
				if(viewLearnerEnrollmentItem.getReady()){
					anyReady = true;
					break;
				}
			}
		}
		if( anyReady == false ) {
			errors.rejectValue("viewLearnerEntitlementItems", "error.addEnrollment.enrollmentRequired");
			return false;
		}
		return true;
	}

	public void validateSelectEnrollment(
			ViewLearnerEnrollmentForm viewLearnerEnrollmentForm,Errors errors){

		boolean anySelected = false;

		List<ViewLearnerEntitlementItem> viewLearnerEntitlementItems = viewLearnerEnrollmentForm.getViewLearnerEntitlementItems();
		for (ViewLearnerEntitlementItem viewLearnerEntitlementItem : viewLearnerEntitlementItems){

			for (ViewLearnerEnrollmentItem viewLearnerEnrollmentItem : viewLearnerEntitlementItem.getViewLearnerEnrollmentItems()){
				if(viewLearnerEnrollmentItem.isSelected()){
					anySelected = true;
					if(viewLearnerEnrollmentItem.getLearnerEnrollment().getEnrollmentStatus().equals(viewLearnerEnrollmentItem.getLearnerEnrollment().getCourseStatistics().REPORTED))
						errors.rejectValue("viewLearnerEntitlementItems", "error.addEnrollment.extendsEnrollmentReported");
					break;
				} 
			}
		}
		if( anySelected == false ) {
			errors.rejectValue("viewLearnerEntitlementItems", "error.addEnrollment.enrollmentRequired");
		}
	}
	
	/**
	 * Implemented method for If Enrollments already expired, reported and completed then system should not drop them
	 * @param viewLearnerEnrollmentForm
	 * @param errors
	 */
	public void validateDropEnrollments(ViewLearnerEnrollmentForm viewLearnerEnrollmentForm, Errors errors) {
		
		List<ViewLearnerEntitlementItem> viewLearnerEntitleItems = viewLearnerEnrollmentForm.getViewLearnerEntitlementItems();
		
		for(ViewLearnerEntitlementItem viewEntitlementItem : viewLearnerEntitleItems) {
			for(ViewLearnerEnrollmentItem viewLearnerEnrollmentItem : viewEntitlementItem.getViewLearnerEnrollmentItems()) {
				if(viewLearnerEnrollmentItem.isSelected()) {
					if(viewLearnerEnrollmentItem.getLearnerEnrollment().getCourseStatistics().getStatus().equalsIgnoreCase(viewLearnerEnrollmentItem.getLearnerEnrollment().getCourseStatistics().COMPLETED)) {
						errors.rejectValue("viewLearnerEntitlementItems", "lms.enrollment.showEnrollments.completedCourseCannotDrop.error");
						break;
					} else if(viewLearnerEnrollmentItem.getLearnerEnrollment().getCourseStatistics().getStatus().equalsIgnoreCase(viewLearnerEnrollmentItem.getLearnerEnrollment().getCourseStatistics().REPORTED)) {
						errors.rejectValue("viewLearnerEntitlementItems", "lms.enrollment.showEnrollments.reportedCourseCannotDrop.error");
						break;
					} else if(viewLearnerEnrollmentItem.getLearnerEnrollment().getCourseStatistics().getStatus().equalsIgnoreCase(viewLearnerEnrollmentItem.getLearnerEnrollment().EXPIRED)) {
						errors.rejectValue("viewLearnerEntitlementItems", "lms.enrollment.showEnrollments.expireCourseCannotDrop.error");
						break;
					}
				}
			}
		}
	}

	public void validateExtendEnrollments(ViewLearnerEnrollmentForm viewLearnerEnrollmentForm,Errors errors){

		boolean anyError = false;
		List<ViewLearnerEntitlementItem> viewLearnerEntitlementItems = viewLearnerEnrollmentForm.getViewLearnerEntitlementItems();
		for (ViewLearnerEntitlementItem viewLearnerEntitlementItem : viewLearnerEntitlementItems){

			for (ViewLearnerEnrollmentItem viewLearnerEnrollmentItem : viewLearnerEntitlementItem.getViewLearnerEnrollmentItems()){
				if(viewLearnerEnrollmentItem.isSelected()){

					if (viewLearnerEnrollmentItem.getNewEnrollmentEndDate()== null || viewLearnerEnrollmentItem.getNewEnrollmentEndDate().isEmpty()){

						errors.rejectValue("viewLearnerEntitlementItems", "error.addEnrollment.enrollmentEndDateBlank");
						anyError = true;
						break;
					} else if (!FieldsValidation.isValidDate(viewLearnerEnrollmentItem.getNewEnrollmentEndDate())){
						errors.rejectValue("viewLearnerEntitlementItems", "error.addEnrollment.enrollmentInvalidDate");
						anyError = true;
						break;
					} else {
						SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
						Date newEnrollmentDate = null;
						try{
							newEnrollmentDate=formatter.parse(viewLearnerEnrollmentItem.getNewEnrollmentEndDate());
						}
						catch (ParseException e) {
							e.printStackTrace();
						}
						Date startDate=null;
						Date endDate = viewLearnerEntitlementItem.getEntitlement().getEndDate();
						if( endDate == null)
						{
							
							startDate=new java.util.Date(viewLearnerEntitlementItem.getEntitlement().getStartDate().getTime());
							Calendar c=Calendar.getInstance();
							c.setTime(startDate);
							c.add(Calendar.DAY_OF_YEAR, viewLearnerEntitlementItem.getEntitlement().getDefaultTermOfServiceInDays());
							endDate=c.getTime();
						}
						if( newEnrollmentDate.after(endDate)){
							errors.rejectValue("viewLearnerEntitlementItems", "error.addEnrollment.enrollmentEndDate");
							anyError = true;
							break;
						}else if( newEnrollmentDate.before(viewLearnerEntitlementItem.getEntitlement().getStartDate())){
							errors.rejectValue("viewLearnerEntitlementItems", "error.addEnrollment.enrollmentStartDate");
							anyError = true;
							break;
						}
					}
				}
			}
			if(anyError == true)
				break;	
		}
		//if(anyError == true)
		//	errors.rejectValue("viewLearnerEntitlementItems", "error.addEnrollment.enrollmentInvalidDate");
	}

}