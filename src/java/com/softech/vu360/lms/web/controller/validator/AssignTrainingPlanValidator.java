package com.softech.vu360.lms.web.controller.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.EnrollmentCourseView;
import com.softech.vu360.lms.model.SynchronousCourse;
import com.softech.vu360.lms.model.TrainingPlan;
import com.softech.vu360.lms.model.TrainingPlanCourse;
import com.softech.vu360.lms.model.WebinarCourse;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.TrainingPlanService;
import com.softech.vu360.lms.web.controller.model.AddTrainingPlanForm;
import com.softech.vu360.lms.web.controller.model.LearnerGroupEnrollmentItem;
import com.softech.vu360.lms.web.controller.model.LearnerItemForm;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

public class AssignTrainingPlanValidator implements Validator {

	private TrainingPlanService trainingPlanService;
	private EntitlementService entitlementService;

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
		//validateSeventhPage(form,errors);
	}

	public void validateSixthPage(AddTrainingPlanForm form, Errors errors) {
		
		
		//by OWS so that it lets the course passed
		if(!form.isNonSyncCourseSelected())
			return;
		//end by OWS
						
		GregorianCalendar todayDate =new GregorianCalendar();
		todayDate.setTime(new Date());
		todayDate.set(GregorianCalendar.HOUR_OF_DAY,0);
		todayDate.set(GregorianCalendar.MINUTE,0);
		todayDate.set(GregorianCalendar.SECOND,0);
		todayDate.set(GregorianCalendar.MILLISECOND,0);
				
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Customer customer =((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentCustomer();
		Date tPStartDate = null;
		Date tpEndDate = null;
		try {
			if(StringUtils.isBlank(form.getAllCourseStartDate())) {
				errors.rejectValue("allCourseStartDate", "error.addEnrollment.StartEndDateRequired");
			} /*else if ( startDate.before(date) ) {
				errors.rejectValue("allCourseStartDate", "lms.assignPlan.dateLessThanToday.error");
			} */
			else if(StringUtils.isBlank(form.getAllCourseEndDate())) {
				errors.rejectValue("allCourseStartDate", "error.addEnrollment.StartEndDateRequired");
			} /* else if ( startDate.before(date) ) {
				errors.rejectValue("allCourseStartDate", "lms.assignPlan.endDateLessThanToday.error");
			}*/
			else{
				tPStartDate = formatter.parse(form.getAllCourseStartDate());
				tpEndDate = formatter.parse(form.getAllCourseEndDate());
				if( tpEndDate.before(tPStartDate) ) {
					errors.rejectValue("allCourseStartDate", "error.assignPlan.endDateNotValid1");
				}
				else {
					List<Date> contractEndDates = new ArrayList <Date>();
					List<Date> contractStartDates = new ArrayList <Date>();
					String planId = form.getSelectedTrainingPlanId();
					TrainingPlan selectedTrainingPlan = trainingPlanService.getTrainingPlanByID(
							Long.valueOf(planId));
					for( TrainingPlanCourse course : selectedTrainingPlan.getCourses() ) {
						List<CustomerEntitlement> customerEntitlementList = entitlementService.getCustomerEntitlementsByCourseId(customer,course.getCourse().getId());
						 contractEndDates = new ArrayList <Date>();
						 contractStartDates = new ArrayList <Date>();
						for( CustomerEntitlement ent : customerEntitlementList ) {							
							if( ent.getEndDate() != null && ent.getEndDate().after(todayDate.getTime())) {																
								   contractEndDates.add(ent.getEndDate());
								   contractStartDates.add(ent.getStartDate());								
							}else if(ent.getEndDate() == null){
								Calendar entEndDate = Calendar.getInstance();
//								entEndDate.setDate(ent.getStartDate().getDate() + ent.getDefaultTermOfServiceInDays());
								entEndDate.setTime(ent.getStartDate());
								entEndDate.add(Calendar.DAY_OF_MONTH, ent.getDefaultTermOfServiceInDays());
								if(entEndDate.getTime().after(todayDate.getTime())){ 
								   contractEndDates.add(entEndDate.getTime());
								   contractStartDates.add(ent.getStartDate());
								}
							}													
						}		
					}	
					
					SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
										
					if ( tPStartDate.before(todayDate.getTime()) && !contractEndDates.isEmpty()) {							
						Date minEndDate = Collections.min(contractEndDates);
						String minEnrollmentEndDate = sdf.format(minEndDate);
						String todaysDate = sdf.format(todayDate.getTime());
						form.setAllCourseStartDateRange( todaysDate + " and " + minEnrollmentEndDate );
						form.setAllCourseEnrollmentMinEndDate(minEnrollmentEndDate);
						
						errors.rejectValue("allCourseStartDate", "lms.assignPlan.dateLessThanToday.error");
					} 
					
										
					if( contractEndDates != null && !contractEndDates.isEmpty() ) {
						Date maxEndDate = Collections.max(contractEndDates); 
						if( tPStartDate.after(maxEndDate) ) {
							errors.rejectValue("allCourseStartDate", "error.assignPlan.startDateNotValid2");
						}
					}
					if( contractStartDates != null && !contractStartDates.isEmpty() ) {
						Date minStartDate = Collections.min(contractStartDates);
						if( tPStartDate.before(minStartDate) ) {
							errors.rejectValue("allCourseStartDate", "error.assignPlan.startDateNotValid1");
						}
					}
					if( contractEndDates != null && !contractEndDates.isEmpty() ) {
						Date maxEndDate = Collections.max(contractEndDates);
						if( tpEndDate.after(maxEndDate) ) {
							errors.rejectValue("allCourseStartDate", "error.assignPlan.endDateNotValid2","abc");
							//DateFormat.getDateInstance(DateFormat.MEDIUM );
							String minEnrollmentEndDate = sdf.format(maxEndDate);
							form.setAllCourseEnrollmentMinEndDate(minEnrollmentEndDate);
				
						}
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			errors.rejectValue("allCourseStartDate", "lms.addEnrollmentValidator.date.invalid");

		}

	}


	public void validateFifthPage(AddTrainingPlanForm form, Errors errors) {
		if(form.getOrgGroups() == null) {
			errors.rejectValue("orgGroups", "error.sendMail.orgGroupRequired");
		}
	}

	public void validateFourthPage(AddTrainingPlanForm form, Errors errors) {

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
		else { // [09/17/2010] LMS-6859 :: Validations on Synchronous Courses enrollment
			if( form.isSyncCourseSelected() ) {
				for (EnrollmentCourseView item : form.getEnrollmentCourseViewList() ) {				
					if (item.getSelected()
							&& (item.getCourseType().equalsIgnoreCase(SynchronousCourse.COURSE_TYPE) || item.getCourseType().equalsIgnoreCase(WebinarCourse.COURSE_TYPE)) ) {
						
						if (item.getSyncClasses() == null) {
							errors.rejectValue("learners", "error.assignTrainingPlan.seatsUnavailable.courses");
							break;
						}						
					}
				}			
			}	
		}
	}

	public void validateThirdPage(AddTrainingPlanForm form, Errors errors) {

		boolean anySelected = false;
		for(LearnerGroupEnrollmentItem lgroup : form.getLearnerGroupTrainingItems()){
			if(lgroup.isSelected() == true) {
				anySelected = true;
			}
		}
		if( anySelected == false ) {
			errors.rejectValue("selectedLearnerGroups", "error.sendMail.learnerGroupRequired");
		}
	}

	public void validateSecondPage(AddTrainingPlanForm form, Errors errors) {
		if( form.getTrainingPlanMethod().equalsIgnoreCase("LearnerGroup") ) {
			if ( form.getLearnerGroupTrainingItems() == null || form.getLearnerGroupTrainingItems().isEmpty() ) {
				errors.rejectValue("trainingPlanMethod", "error.addEnrollment.learnerGroupNotPresent");
			}
		}
	}

	public void validateFirstPage(AddTrainingPlanForm form, Errors errors) {
		if( StringUtils.isBlank(form.getSelectedTrainingPlanId()) ) {
			errors.rejectValue("trainingPlanName", "error.assignTrainingPlan.plan");
		}
		if (form.getEnrollmentCourseViewList().isEmpty()) {
			errors.rejectValue("trainingPlanName", "error.assignTrainingPlan.noContractSeats");
		}
	}

	public TrainingPlanService getTrainingPlanService() {
		return trainingPlanService;
	}

	public void setTrainingPlanService(TrainingPlanService trainingPlanService) {
		this.trainingPlanService = trainingPlanService;
	}

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

}