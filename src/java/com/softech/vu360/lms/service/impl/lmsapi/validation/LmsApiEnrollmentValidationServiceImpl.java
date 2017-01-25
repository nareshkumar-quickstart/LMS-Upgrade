package com.softech.vu360.lms.service.impl.lmsapi.validation;

import static com.softech.vu360.lms.webservice.validation.lmsapi.CoursePredicate.isValidCoursesGuid;
import static com.softech.vu360.lms.webservice.validation.lmsapi.EnrollmentPredicate.isEnrollmentEndDateAfterMaxEntitlementEndDate;
import static com.softech.vu360.lms.webservice.validation.lmsapi.EnrollmentPredicate.isEnrollmentEndDateBeforeEnrollmentStartDate;
import static com.softech.vu360.lms.webservice.validation.lmsapi.EnrollmentPredicate.isEnrollmentEndDateBeforeTodayDate;
import static com.softech.vu360.lms.webservice.validation.lmsapi.EnrollmentPredicate.isEnrollmentStartDateAfterMaxEntitlementEndDate;
import static com.softech.vu360.lms.webservice.validation.lmsapi.EnrollmentPredicate.isEnrollmentStartDateBeforeMinEntitlementStartDate;
import static com.softech.vu360.lms.webservice.validation.lmsapi.EnrollmentPredicate.isEnrollmentStartDateBeforeTodayDate;
import static com.softech.vu360.lms.webservice.validation.lmsapi.EnrollmentPredicate.isValidEnrollmentEndDate;
import static com.softech.vu360.lms.webservice.validation.lmsapi.EnrollmentPredicate.isValidEnrollmentStartDate;
import static com.softech.vu360.lms.webservice.validation.lmsapi.LearnerPredicate.isValidUsername;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.lmsapi.LmsApiEntitlementService;
import com.softech.vu360.lms.service.lmsapi.validation.LmsApiEnrollmentValidationService;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.enrollment.LearnerCoursesEnrollRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.types.enrollment.LearnerCourses;
import com.softech.vu360.lms.webservice.message.lmsapi.types.enrollment.LearnerEnrollCourses;

@Service
public class LmsApiEnrollmentValidationServiceImpl implements LmsApiEnrollmentValidationService {

	@Autowired
	private LmsApiEntitlementService lmsApiEntitlementService;
	
	public void setLmsApiEntitlementService(LmsApiEntitlementService lmsApiEntitlementService) {
		this.lmsApiEntitlementService = lmsApiEntitlementService;
	}

	@Override
	public boolean validateLearnerCoursesEnrollRequest(LearnerCoursesEnrollRequest request, VU360User manager, String customerCode) throws Exception {
		boolean notifyLearnersByEmail = request.isNotifyLearnersByEmail();
		List<LearnerCourses> learnerCoursesList = request.getLearnerCourses();
		if (notifyLearnersByEmail) {
			if (manager == null) {
				throw new Exception("No manager found for customer: " + customerCode);
			}
		}
		
		if (CollectionUtils.isEmpty(learnerCoursesList)) {
			throw new Exception("LearnerCourses element is required and must not be empty");
		}
		return true;
	}

	@Override
	public Map<Boolean, List<LearnerCourses>> getLearnerCoursesMap(List<LearnerCourses> learnerCoursesList) {
		
		List<LearnerCourses> validLearnerCourses = new ArrayList<>();
		List<LearnerCourses> invalidLearnerCourses = new ArrayList<>();
		Map<Boolean, List<LearnerCourses>> learnerCoursesMap = new LinkedHashMap<>();
		
		for (LearnerCourses learnerCourses : learnerCoursesList) {
			if (isValidLearnerCourses(learnerCourses)) {
				validLearnerCourses.add(learnerCourses);
			} else {
				invalidLearnerCourses.add(learnerCourses);
			}
		}
		
		if (!CollectionUtils.isEmpty(validLearnerCourses)) {
			learnerCoursesMap.put(Boolean.TRUE, validLearnerCourses);
		}
		
		if (!CollectionUtils.isEmpty(invalidLearnerCourses)) {
			learnerCoursesMap.put(Boolean.FALSE, invalidLearnerCourses);
		}
		
		return learnerCoursesMap;
		
	}

	@Override
	public List<LearnerCourses> getValidLearnerCourses(Map<Boolean, List<LearnerCourses>> learnerCoursesMap) {
		return getLearnerCourses(learnerCoursesMap, Boolean.TRUE);
	}
	
	@Override
	public Map<LearnerCourses, String> getInvalidLearnerCourses(Map<Boolean, List<LearnerCourses>> learnerCoursesMap) {
		Map<LearnerCourses, String> invalidLearnerCoursesMap = null;
		List<LearnerCourses> invalidLearnerCoursesList = getLearnerCourses(learnerCoursesMap, Boolean.FALSE);
		if (!CollectionUtils.isEmpty(invalidLearnerCoursesList)) {
			invalidLearnerCoursesMap = new HashMap<>();
			for (LearnerCourses invalidLearnerCourses : invalidLearnerCoursesList) {
				String userName = invalidLearnerCourses.getUserId();
				LearnerEnrollCourses courses = invalidLearnerCourses.getCourses();
				String errorMessage = "";
				if (courses == null) {
					errorMessage = "Courses element is required";
				} else if (!isValidUsername(userName)) {
					errorMessage = "UserId can not be empty or blank";
				} else {
					XMLGregorianCalendar enrollmentStartDate = courses.getEnrollmentStartDate();
					XMLGregorianCalendar enrollmentEndDate = courses.getEnrollmentEndDate();
					List<String> coursesGuid = courses.getCourseId();
					errorMessage = getEnrollmentCredentialsErrorMessage(enrollmentStartDate, enrollmentEndDate, coursesGuid);
				}
				invalidLearnerCoursesMap.put(invalidLearnerCourses, errorMessage);
			} //end of for
		}
		return invalidLearnerCoursesMap;
	}
	
	@Override
	public boolean isValidAtEnrollmentLevel(Date enrollmentStartDate, Date enrollmentEndDate, List<Date> entitlementStartDates, List<Date> entitlementEndDates) {
		
		boolean valid = false;
		GregorianCalendar todayDate = getTodayDate();
		boolean validEnrollmentStartDate = !isEnrollmentStartDateBeforeTodayDate(enrollmentStartDate, todayDate) &&
			   !isEnrollmentStartDateAfterMaxEntitlementEndDate(enrollmentStartDate, entitlementEndDates) &&
			   !isEnrollmentStartDateBeforeMinEntitlementStartDate(enrollmentStartDate, entitlementStartDates);
		
		boolean validEnrollmentEndDate = false;
		if (isValidEnrollmentEndDate(enrollmentEndDate)) {
			validEnrollmentEndDate = !isEnrollmentEndDateBeforeEnrollmentStartDate(enrollmentStartDate, enrollmentEndDate) &&
					!isEnrollmentEndDateBeforeTodayDate(enrollmentEndDate, todayDate) &&
					!isEnrollmentEndDateAfterMaxEntitlementEndDate(enrollmentEndDate, entitlementEndDates);
		} else {
			validEnrollmentEndDate = true;
		}
			   
		if (validEnrollmentStartDate && validEnrollmentEndDate) {
			valid = true;
		}
		return valid;	   
	}
	
	@Override
    public String getEnrollmentLevelErrorMessage(List<CustomerEntitlement> customerEntitlements, Date todayDate, Date enrollmentStartDate, Date enrollmentEndDate) {
    	String errorMessage = "";
    	if (!CollectionUtils.isEmpty(customerEntitlements)) {
    		Map<Boolean, List<CustomerEntitlement>> seatAvailableEntitlementsMap = lmsApiEntitlementService.getSeatAvailableEntitlementsMap(customerEntitlements);
			if (!CollectionUtils.isEmpty(seatAvailableEntitlementsMap)) {
				List<CustomerEntitlement> validSeatAvailableCustomerEntitlements  = seatAvailableEntitlementsMap.get(Boolean.TRUE);
				if (!CollectionUtils.isEmpty(validSeatAvailableCustomerEntitlements)) {
					List<Date> entitlementStartDates = lmsApiEntitlementService.getEntitlementStartDates(validSeatAvailableCustomerEntitlements, todayDate);
					List<Date> entitlementEndDates = lmsApiEntitlementService.getEntitlementEndDates(validSeatAvailableCustomerEntitlements, todayDate);
					errorMessage = getEnrollmentLevelErrorMessage(enrollmentStartDate, enrollmentEndDate, entitlementStartDates, entitlementEndDates);
				}
			}
    	}
    	return errorMessage;
    }
	
	private String getEnrollmentLevelErrorMessage(Date enrollmentStartDate, Date enrollmentEndDate, List<Date> entitlementStartDates, List<Date> entitlementEndDates) {
		String errorMessage = "";
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
		GregorianCalendar todayDate = getTodayDate();
		if (!CollectionUtils.isEmpty(entitlementStartDates) && !CollectionUtils.isEmpty(entitlementEndDates)) {
			if (isEnrollmentStartDateBeforeTodayDate(enrollmentStartDate, todayDate)) {
				Date minEndDate = Collections.min(entitlementEndDates);
				String minEnrollmentEndDate = sdf.format(minEndDate);
				String todaysDate = sdf.format(todayDate.getTime());
				errorMessage = "Start date must be in between " + todaysDate + " and " + minEnrollmentEndDate;
			} else if (isEnrollmentStartDateAfterMaxEntitlementEndDate(enrollmentStartDate, entitlementEndDates)) {
				errorMessage = "Start Date cannot be after any Entitlement's End Date.";
			} else if (isEnrollmentStartDateBeforeMinEntitlementStartDate(enrollmentStartDate, entitlementStartDates)) {
				errorMessage = "Start Date cannot be before any Entitlement's Start Date.";
			} else if (enrollmentEndDate != null) {
				if (isEnrollmentEndDateBeforeEnrollmentStartDate(enrollmentStartDate, enrollmentEndDate)) {
					errorMessage = "Start date can not be after End Date";
				} else if (isEnrollmentEndDateBeforeTodayDate(enrollmentEndDate, todayDate)) {
					Date minEndDate = Collections.min(entitlementEndDates);
					String minEnrollmentEndDate = sdf.format(minEndDate);
					String todaysDate = sdf.format(todayDate.getTime());
					errorMessage = "End date must be in between " + todaysDate + " and " + minEnrollmentEndDate;
				} else if (isEnrollmentEndDateAfterMaxEntitlementEndDate(enrollmentEndDate, entitlementEndDates)) {
					Date maxEndDate = Collections.max(entitlementEndDates);
					String minEnrollmentEndDate = sdf.format(maxEndDate);
					errorMessage = "End Date must not be later than " + minEnrollmentEndDate;
				}
			}
		}
		return errorMessage;
	}
	
	private String getUserNameErrorMessage(String userName) {
		
		String errorMessage = "";
		if (!isValidUsername(userName)) {
			errorMessage = "UserId can not be empty or blank";
		}
		return errorMessage;
	}
	
	private String getEnrollmentCredentialsErrorMessage(XMLGregorianCalendar enrollmentStartDate, XMLGregorianCalendar enrollmentEndDate, List<String> coursesGuid) {
		
		String errorMessage = "";
		if (!isValidCoursesGuid(coursesGuid)) {
			errorMessage = "Courses element is required and must not be empty";
		} else if (!isValidEnrollmentStartDate(enrollmentStartDate)) {
			errorMessage = "enrolmentStartDate is mandotory";
		} else if (!isValidEnrollmentEndDate(enrollmentEndDate)) {
			errorMessage = "enrolmentEndDate is mandotory";
		}
		return errorMessage;
	}
	
	private boolean isValidLearnerCourses(LearnerCourses learnerCourses) {
		boolean validLearnerCourses = false;
		boolean validCourseEnrollmentCredentials = false;
		String userName = learnerCourses.getUserId();
		LearnerEnrollCourses courses = learnerCourses.getCourses();
		if (courses != null) {
			XMLGregorianCalendar enrollmentStartDate = courses.getEnrollmentStartDate();
			XMLGregorianCalendar enrollmentEndDate = courses.getEnrollmentEndDate();
			List<String> coursesGuid = courses.getCourseId();
			validCourseEnrollmentCredentials = isValidCourseEnrollmentCredentials(enrollmentStartDate, enrollmentEndDate, coursesGuid);
		}
		
		if (isValidUsername(userName) && validCourseEnrollmentCredentials) {
			validLearnerCourses = true;
		}
		return validLearnerCourses;
	}
	
	private boolean isValidCourseEnrollmentCredentials(XMLGregorianCalendar enrollmentStartDate, XMLGregorianCalendar enrollmentEndDate, List<String> coursesGuid) {
		return isValidCoursesGuid(coursesGuid) &&
			   isValidEnrollmentStartDate(enrollmentStartDate) &&
			   isValidEnrollmentEndDate(enrollmentEndDate);		
	}
	
	private List<LearnerCourses> getLearnerCourses(Map<Boolean, List<LearnerCourses>> learnerCoursesMap, Boolean key) {
		List<LearnerCourses> learnerCourses = null;
		if (!CollectionUtils.isEmpty(learnerCoursesMap)) {
			learnerCourses = learnerCoursesMap.get(key);
		}
		return learnerCourses;
	}
	
	private GregorianCalendar getTodayDate() {
		
		GregorianCalendar todayDate =new GregorianCalendar();
		todayDate.setTime(new Date());
		todayDate.set(GregorianCalendar.HOUR_OF_DAY,0);
		todayDate.set(GregorianCalendar.MINUTE,0);
		todayDate.set(GregorianCalendar.SECOND,0);
		todayDate.set(GregorianCalendar.MILLISECOND,0);
		
		return todayDate;
		
	} //end of getTodayDate()
	
}
