package com.softech.vu360.lms.web.controller.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.DistributorEntitlement;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.web.controller.model.AddCustomerEntitlementsForm;
import com.softech.vu360.lms.web.controller.model.CustomerEntitlementsCourseGroup;
import com.softech.vu360.lms.web.controller.model.OrganisationalGroupEntitlementItem;
import com.softech.vu360.lms.web.controller.model.SurveyCourse;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.FieldsValidation;

public class AddCustomerEntitlementsValidator implements Validator {

	private static final Logger log = Logger.getLogger(AddCustomerEntitlementsValidator.class.getName());
	private EntitlementService entitlementService;
	private CourseAndCourseGroupService courseAndCourseGroupService = null;

	public boolean supports(Class clazz) {
		return AddCustomerEntitlementsForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		AddCustomerEntitlementsForm form = (AddCustomerEntitlementsForm)obj;
		validateDetailsPage(form, errors);
		validateOrgGrpEntitlementPage(form, errors);
		validateCoursePage(form, errors);
		validateCourseGroupPage(form, errors);
	}

	public void validateDetailsPage(AddCustomerEntitlementsForm form, Errors errors) {

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

		if (StringUtils.isBlank(form.getEntitlementName())) {
			errors.rejectValue("entitlementName", "error.custEntitlement.entitlementName.required");
		} else if (FieldsValidation.isInValidGlobalName(form.getEntitlementName())) {
			errors.rejectValue("entitlementName", "error.custEntitlement.entitlementName.invalid");
		}

		if (this.checkCustomerEntitlement(form.getEntitlementName()))
			errors.rejectValue("entitlementName", "error.custEntitlement.entitlementName.exists");

		if (!form.isMaxEnrollments()) {
			if (StringUtils.isBlank(form.getNoOfMaxEnrollments())) {
				errors.rejectValue("noOfMaxEnrollments", "error.custEntitlement.noOfMaxEnrollments.required");
			} else if (!StringUtils.isNumeric(form.getNoOfMaxEnrollments())) {
				errors.rejectValue("noOfMaxEnrollments", "error.custEntitlement.noOfMaxEnrollments.invalid");
			}else {
				try {
					if (Integer.parseInt(form.getNoOfMaxEnrollments()) <= 0) {
						errors.rejectValue("noOfMaxEnrollments", "error.custEntitlement.noOfMaxEnrollments.enroll");
					}
				} catch (Exception e) {
					errors.rejectValue("noOfMaxEnrollments", "error.custEntitlement.noOfMaxEnrollments.invalid");
				}
			}
		}

		if (StringUtils.isBlank(form.getStartDate())) {
			errors.rejectValue("startDate", "error.custEntitlement.startDate.required");
		}

		if (form.isTermsOfService()) {
			if (StringUtils.isBlank(form.getDays())) {
				errors.rejectValue("days", "error.custEntitlement.days.required");
			} else if (!StringUtils.isNumeric(form.getDays())) {
				errors.rejectValue("days", "error.custEntitlement.days.invalid");
			} else {
				try {
					Integer.parseInt(form.getDays());
					if( Integer.parseInt(form.getDays()) <= 0 ) {
						errors.rejectValue("days", "error.custEntitlement.days.notPositive");
					}
				} catch (Exception e) {
					errors.rejectValue("days", "error.custEntitlement.days.invalid");
				}
			}
		} else {
			if (StringUtils.isBlank(form.getFiexedEndDate())) {
				errors.rejectValue("fiexedEndDate", "error.custEntitlement.fiexedEndDate.required");
			} else {
				try {
					Date sDate = formatter.parse(form.getStartDate());
					Date eDate = formatter.parse(form.getFiexedEndDate());
					if (!formatter.format(sDate).equals(form.getStartDate())) {
						errors.rejectValue("startDate", "lms.addEnrollmentValidator.date.invalid");

					} 
					if (!formatter.format(eDate).equals(form.getFiexedEndDate())) {

						errors.rejectValue("fiexedEndDate", "error.addEnrollment.enrollmentInvalidDate");

					} 
					Date date = new Date();
					date.setDate(date.getDate()-1);
					if( sDate.before(date) || eDate.before(date) ) {
						errors.rejectValue("fiexedEndDate", "lms.addEnrollmentValidator.dateLessThanToday.error");
					} else if( sDate.equals(eDate) || eDate.before(sDate) ) {
						errors.rejectValue("fiexedEndDate", "lms.addEnrollmentValidator.datesCrossed.error");
					}
				} catch (ParseException e) {
					errors.rejectValue("fiexedEndDate", "error.addEnrollment.enrollmentInvalidDate");
					e.printStackTrace();
				}
			}
		}
		if (form.isEntitlementType())
			this.checkCourseGroup(errors);
		else
			this.checkCourse(form,errors);
	}

	public void validateOrgGrpEntitlementPage(AddCustomerEntitlementsForm form, Errors errors) {

		boolean anySelected = true;

		if(form.getOrganisationalGroupEntitlementItems() != null || form.getOrganisationalGroupEntitlementItems().size() > 0) {
			for (OrganisationalGroupEntitlementItem OrgGroupEnt : (List<OrganisationalGroupEntitlementItem>)form.getOrganisationalGroupEntitlementItems() ) {
				if ( !StringUtils.isBlank(OrgGroupEnt.getMaxEnrollments()) ){
					if( !StringUtils.isNumeric(OrgGroupEnt.getMaxEnrollments()) ) {
						anySelected = false;
						break;
					}else {
						try {
							Integer.parseInt(OrgGroupEnt.getMaxEnrollments());

						} catch (Exception e) {
							anySelected = false;
							break;
						}
					}

				}
			}
		}
		if( anySelected == false ) {
			errors.rejectValue("organisationalGroupEntitlementItems", "error.editCustEntitlement.invalidEnroll");
		}
	}

	public void validateCoursePage(AddCustomerEntitlementsForm form, Errors errors) {
		boolean anySelected = false;
		if (form.getCourseSearchType().isEmpty()) {
			if (form.getCourseGroups() != null) {
				anySelected = true;
			}
		} else {
			if(form.getSelectedCourses() == null || form.getSelectedCourses().size() == 0) {
				anySelected = false;
			} else {
				for(SurveyCourse course : (List<SurveyCourse>)form.getSelectedCourses()) {
					if(course.isSelected()) {
						anySelected = true;
						break;
					}
				}
			}
		}
		if( anySelected == false ) {
			errors.rejectValue("selectedCourses", "error.custEntitlement.selectedCourses.required");
		}
	}

	public void validateCourseGroupPage(AddCustomerEntitlementsForm form, Errors errors) {
		boolean anySelected = false;
		if (form.getCourseGroupSearchType().isEmpty()) {
			if (form.getGroups() != null) {
				anySelected = true;
			}
		} else {
			if(form.getSelectedCourseGroups() == null || form.getSelectedCourseGroups().size() == 0) {
				anySelected = false;
			} else {
				for(CustomerEntitlementsCourseGroup courseGroup : (List<CustomerEntitlementsCourseGroup>)form.getSelectedCourseGroups()) {
					if(courseGroup.isSelected()) {
						anySelected = true;
					}
				}
			}
		}
		if( anySelected == false ) {
			errors.rejectValue("selectedCourseGroups", "error.custEntitlement.selectedCourseGroups.required");
		}
	}

	private boolean checkCustomerEntitlement(String custEntName) {
		/*VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Customer customer = null;
		if (loggedInUser.isLMSAdministrator())
			customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
		else
			customer = loggedInUser.getLearner().getCustomer();

		List<CustomerEntitlement> customerEntitlementList = entitlementService.getAllCustomerEntitlements(customer);
		for (CustomerEntitlement custEntitlement : customerEntitlementList) {
			if (custEntitlement.getName().equalsIgnoreCase(custEntName))
				return true;
		}*/
		return false;
	}

	public void checkCourse(AddCustomerEntitlementsForm form, Errors errors) {
		log.debug("----JASON:enterCheckCourse");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
		Customer customer = details.getCurrentCustomer();
		List<DistributorEntitlement> distributorEntitlements = entitlementService.getAllDistributorEntitlements(customer.getDistributor());
		int count=0;

		for (DistributorEntitlement distributorEntitlement:distributorEntitlements){
			count=count+ distributorEntitlement.getCourseGroups().size();
		}
		long[] courseIdArray = new long[count];
		count = 0;

		for(int loop1=0;loop1<distributorEntitlements.size();loop1++){
			DistributorEntitlement distributorEntitlement = distributorEntitlements.get(loop1);
			for(int loop2=0;loop2<distributorEntitlement.getCourseGroups().size();loop2++,count++){
				courseIdArray[count] = distributorEntitlement.getCourseGroups().get(loop2).getId().longValue();
			}
		}	
		
		if ( courseIdArray.length == 0 ) {
			errors.rejectValue("entitlementType", "error.custEntitlement.entitlementType.course");
		}
		 
	}

	public void checkCourseGroup(Errors errors) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
		Customer customer = details.getCurrentCustomer();
		List<DistributorEntitlement> distributorEntitlements = entitlementService.getAllDistributorEntitlements(customer.getDistributor());
		Set<Long> courseGroupIdSet = new HashSet<Long>();
		for(int loop1=0;loop1<distributorEntitlements.size();loop1++){
			DistributorEntitlement distributorEntitlement = distributorEntitlements.get(loop1);
			for(int loop2=0;loop2<distributorEntitlement.getCourseGroups().size();loop2++){
				courseGroupIdSet.add(distributorEntitlement.getCourseGroups().get(loop2).getId().longValue());
			}
		}	
		long[] courseGroupIdArray = new long[courseGroupIdSet.size()];
		Iterator iter = courseGroupIdSet.iterator();
		int i=0;
		while (iter.hasNext()) {
			courseGroupIdArray[i++] = (Long) iter.next();
		}

		if ( courseGroupIdArray.length == 0 ) {
			errors.rejectValue("entitlementType", "error.custEntitlement.entitlementType.courseGroup");
		}
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

}