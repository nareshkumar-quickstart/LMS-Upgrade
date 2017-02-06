package com.softech.vu360.lms.web.controller.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.EnrollmentCourseView;
import com.softech.vu360.lms.model.SynchronousCourse;
import com.softech.vu360.lms.model.WebinarCourse;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.web.controller.manager.ManageLearnerController;
import com.softech.vu360.lms.web.controller.model.EnrollmentDetailsForm;
import com.softech.vu360.lms.web.controller.model.LearnerGroupEnrollmentItem;

public class AddEnrollmentValidator implements Validator {

	private static final Logger log = Logger.getLogger(ManageLearnerController.class.getName());
	private EntitlementService entitlementService;
	private EnrollmentService enrollmentService;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;

	public boolean supports(Class clazz) {
		return EnrollmentDetailsForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		EnrollmentDetailsForm enrollmentDetailsForm = (EnrollmentDetailsForm)obj;
		validateFirstPage(enrollmentDetailsForm,errors);
		validateSecondPage(enrollmentDetailsForm,errors);
		validateThirdPage(enrollmentDetailsForm,errors);
		validateFourthPage(enrollmentDetailsForm,errors);
		validateFifthPage(enrollmentDetailsForm,errors);
		validateSixthPage(enrollmentDetailsForm,errors);
		validateSeventhPage(enrollmentDetailsForm,errors);
		validateNinthPage(enrollmentDetailsForm,errors);
		validateTenthPage(enrollmentDetailsForm,errors);
		//validateSeventhPage(enrollmentDetailsForm,errors);
	}

	public void validateFirstPage(EnrollmentDetailsForm form, Errors errors) {
		if( form.getEnrollmentMethod().equalsIgnoreCase("LearnerGroup") ) {
			if ( form.getLearnerGroupEnrollmentItems() == null || form.getLearnerGroupEnrollmentItems().isEmpty() ) {
				errors.rejectValue("enrollmentMethod", "error.addEnrollment.learnerGroupNotPresent");
			}else {
				// do nothing
			}
		}
	}
	

	public void validateSecondPage(EnrollmentDetailsForm form, Errors errors) {

		boolean coursePresent = false;
		//boolean orgGroupEntitlemetPresent =true;
		if( form.getSelectedLearners() == null || form.getSelectedLearners().size() <= 0 ) {
			errors.rejectValue("learners", "error.addEnrollment.LearnerRequired");
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

	public void validateThirdPage(EnrollmentDetailsForm form, Errors errors) {
 
		boolean coursePresent = false;
		if(form.getGroups() == null) {
			errors.rejectValue("groups", "error.addEnrollment.orgGroupRequired");
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

	public void validateFourthPage(EnrollmentDetailsForm form, Errors errors) {

		boolean coursePresent = false;
		boolean anySelected = false;
		for(LearnerGroupEnrollmentItem lgroup : form.getLearnerGroupEnrollmentItems()){
			if(lgroup.isSelected() == true) {
				anySelected = true;
			}
		}
		if( anySelected == false ) {
			errors.rejectValue("selectedLearnerGroups", "error.addEnrollment.learnerGroupRequired");
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

	public void validateFifthPage(EnrollmentDetailsForm form, Errors errors) {
		boolean anySelected = false;
		int selectedCoursesCount= 0;
		Long seatsRemaining= new Long(0);
		if( ! form.getCourseSearchStay().trim().equalsIgnoreCase("stay") ) {
			for( EnrollmentCourseView ecv : form.getEnrollmentCourseViewList() ) {
				seatsRemaining=ecv.getTotalSeats()-ecv.getSeatsUsed();
				if( ecv.getSelected() ) {
					anySelected = true;
					selectedCoursesCount++;
					//break;
				}
			}
			if( anySelected == false ) {
				errors.rejectValue("courseEntitlementItems", "error.addEnrollment.CourseRequired");
			}
		}
		
		int learnersToBeEnrolled=form.getLearnersToBeEnrolled();		
		if(seatsRemaining>0 && learnersToBeEnrolled*selectedCoursesCount>seatsRemaining.intValue()){
			errors.rejectValue("courseEntitlementItems", "error.addEnrollment.TooManyEnrollments");
		}
	}

	public void validateSixthPage(EnrollmentDetailsForm form, Errors errors) {

		
		//by OWS so that it lets the course passed
		if ( !form.isSelectedNonSyncCourse() )
			return;
		//end by OWS
		
		int error = 1;
		Date date = new Date(System.currentTimeMillis());
		date=DateUtils.truncate(date, Calendar.DATE);
		//date.setDate(date.getDate()-1);
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date startDate = null;
		Date endDate = null;
		List <Date>startDates = new ArrayList <Date>();
		List <Date>endDates = new ArrayList <Date>();

		//Course/course groups are selected from different entitlements. Need to check whether enrollment start
		//date should not be less than entitlement start date
		//and enrollment end date should not be more than entitlement end date

		/*
		 * Get the startdates and enddates of selected entitlements
		 */ 
		//for( CourseEntitlementItem allCustomerEntitlements :form.getCourseEntitlementItems())
		
		for( EnrollmentCourseView enrollCourse : form.getEnrollmentCourseViewList() ) 
		{
			if(enrollCourse.getSelected()) 
			{
				Date sd1 = enrollCourse.getEntitlementStartDate(); 
					//allCustomerEntitlements.getEntitlement().getStartDate();
				Date ed1 = enrollCourse.getEntitlementEndDate();
					// allCustomerEntitlements.getEntitlement().getEndDate();
				if( ed1 == null ) 
				{
					ed1 = DateUtils.addDays(sd1, enrollCourse.getTermsOfService());
				}
				startDates.add(sd1);
				endDates.add(ed1);
			}
		}

		
		Date maxsd = (Date)Collections.max(startDates);
		Date mined = (Date)Collections.min(endDates);
		//truncate hours/minutes/seconds from date object
		maxsd=DateUtils.truncate(maxsd, Calendar.DATE);
		mined=DateUtils.truncate(mined, Calendar.DATE);
		if( form.isModifyAllEntitlements() == true ) 
		{
			try 
			{
				if( form.getAllCourseStartDate().isEmpty() || form.getAllCourseEndDate().isEmpty() ) 
				{
					errors.rejectValue("allCourseStartDate", "error.addEnrollment.StartEndDateRequired");
				} 
				else 
				{
					startDate = formatter.parse(form.getAllCourseStartDate());
					endDate = formatter.parse(form.getAllCourseEndDate());
					startDate=DateUtils.truncate(startDate, Calendar.DATE);
					endDate=DateUtils.truncate(endDate, Calendar.DATE);
					if ( startDate.before(date) || endDate.before(date) ) 
					{
						errors.rejectValue("allCourseStartDate", "lms.addEnrollmentValidator.dateLessThanToday.error");
					} 
					else if ( endDate.before(startDate) ) 
					{
						errors.rejectValue("allCourseStartDate", "lms.addEnrollmentValidator.datesCrossed.error");
					} 
					else 
					{
						if ( startDate.before(maxsd) ) 
						{
							errors.rejectValue("allCourseStartDate", "error.addEnrollment.startDateNotValid");
						} 
						else if ( endDate.after(mined) ) 
						{
							errors.rejectValue("allCourseStartDate", "error.addEnrollment.EndDateNotValid");
						}
					}
				}
			} catch (ParseException e) 
			{
				log.error("Exception occured while validating enrollment",e);
				errors.rejectValue("allCourseStartDate", "error.addEnrollment.enrollmentInvalidDate");
			}
		} 
		else 
		{
			Date eDate = null;
			Date sDate = null;
			//for( int loop = 0 ; loop < form.getCourseEntitlementItems().size() ; loop ++ ) {
			for(EnrollmentCourseView enrollCourse : form.getEnrollmentCourseViewList()) 
			{
				if(enrollCourse.getSelected()) 
				{
					//for( CourseEntitlementDetails course : form.getCourseEntitlementDetails() ) {
					//--if(enrollCourse.getEnrollmentStartDate().isEmpty() || enrollCourse.getEnrollmentEndDate().isEmpty()) {
					if(StringUtils.isBlank(enrollCourse.getEnrollmentStartDate()) || StringUtils.isBlank(enrollCourse.getEnrollmentEndDate())) 
					{
						error = 0;
						errors.rejectValue("allCourseStartDate", "error.addEnrollment.StartEndDateRequired");
						break;
					} 
					else 
					{
						try 
						{
							//CourseEntitlementItem course = form.getCourseEntitlementItems().get(loop);
							sDate = enrollCourse.getEntitlementStartDate();
							if( enrollCourse.getEntitlementEndDate() == null  ) 
							{ 
								eDate = DateUtils.addDays(enrollCourse.getEntitlementStartDate(), enrollCourse.getTermsOfService());
							} 
							else 
							{
								eDate = enrollCourse.getEntitlementEndDate();
								//eDate = course.getEntitlement().getEndDate();
							}

							startDate = formatter.parse(enrollCourse.getEnrollmentStartDate());
							startDate = DateUtils.truncate(startDate, Calendar.DATE);
							endDate = formatter.parse(enrollCourse.getEnrollmentEndDate());
							endDate = DateUtils.truncate(endDate, Calendar.DATE);
							eDate = DateUtils.truncate(eDate, Calendar.DATE);
							sDate = DateUtils.truncate(sDate, Calendar.DATE);
							if ( startDate.before(date) || endDate.before(date) ) 
							{
								error = 0;
								errors.rejectValue("allCourseStartDate", "lms.addEnrollmentValidator.dateLessThanToday.error");
								break;
							} 
							else if ( endDate.before(startDate) ) 
							{
								error = 0;
								errors.rejectValue("allCourseStartDate", "lms.addEnrollmentValidator.datesCrossed.error");
								break;
							} 
							else if ( startDate.before(sDate) ) //enrollment start date can't be less entitlement start date
							{ 
								error = 0;
								errors.rejectValue("allCourseStartDate", "error.addEnrollment.startDateNotValid");
								break;
							} 
							else if ( endDate.after(eDate) )//enrollment date can't be greater than entitlement end date 
							{
								error = 0;
								errors.rejectValue("allCourseStartDate", "error.addEnrollment.EndDateNotValid");
								break;
							}
						} catch (ParseException e) 
						{
							log.error("Exception occured while validating enrollment",e);
							error = 0;
							errors.rejectValue("allCourseStartDate", "error.addEnrollment.enrollmentInvalidDate");
							break;
						}
					}
				}
				if( error == 0 )
					break;				
			}
		}
	}
	
	public void validateSeventhPage(EnrollmentDetailsForm form, Errors errors) {		
		int totalCourses = 0, erronousCourses = 0;
		
		if( form.isSelectedSyncCourse() ) {
			for (EnrollmentCourseView item : form.getEnrollmentCourseViewList() ) {				
				if (item.getSelected()) {
					totalCourses++;
					
					if ( item.getCourseType().equalsIgnoreCase(SynchronousCourse.COURSE_TYPE) || item.getCourseType().equalsIgnoreCase(WebinarCourse.COURSE_TYPE)) {
						if (item.getSyncClasses() == null) {
							erronousCourses++;
						}
					}
				}
			}
			
			if (erronousCourses > 0) {
				if (totalCourses == erronousCourses) {
					errors.rejectValue("courseEntitlementItems", "error.addEnrollment.seatsUnavailable.allCourses");
				}
				else {
					errors.rejectValue("courseEntitlementItems", "error.addEnrollment.seatsUnavailable.fewCourses");
				}				
			}
			
		}
	}
		public void validateNinthPage(EnrollmentDetailsForm form, Errors errors) {

			
			if( form.getSurveys() == null ) {
				errors.rejectValue("surveys", "error.addEnrollment.surveyRequired");
			}
		}
		public void validateTenthPage(EnrollmentDetailsForm form, Errors errors) {
			if( form.getEnrollCourses() == null ) {
				errors.rejectValue("enrollCourses", "error.addEnrollment.surveyCourseRequired");
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

	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

}