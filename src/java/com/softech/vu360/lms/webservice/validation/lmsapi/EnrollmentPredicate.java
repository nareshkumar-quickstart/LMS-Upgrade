package com.softech.vu360.lms.webservice.validation.lmsapi;

import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.util.CollectionUtils;

public class EnrollmentPredicate {

	public static boolean isValidEnrollmentStartDate(XMLGregorianCalendar enrollmentStartDate) {
		return enrollmentStartDate != null;
	}
	
	public static boolean isValidEnrollmentEndDate(XMLGregorianCalendar enrollmentEndDate) {
		return enrollmentEndDate != null;
	}
	
	public static boolean isValidEnrollmentEndDate(Date enrollmentEndDate) {
		return enrollmentEndDate != null;
	}
	
	public static boolean isEnrollmentStartDateBeforeTodayDate(Date enrollmentStartDate, GregorianCalendar todayDate) {
		return enrollmentStartDate.before(todayDate.getTime());
	}
	
	public static boolean isEnrollmentStartDateAfterMaxEntitlementEndDate(Date enrollmentStartDate, List<Date> entitlementEndDates) {
		boolean afterMaxEndDate = false;
		if (!CollectionUtils.isEmpty(entitlementEndDates)) {
			Date maxEndDate = Collections.max(entitlementEndDates);
			if (enrollmentStartDate.after(maxEndDate) ) {
				afterMaxEndDate = true;
			}
		}
		return afterMaxEndDate;
	}
	
	public static boolean isEnrollmentStartDateBeforeMinEntitlementStartDate(Date enrollmentStartDate, List<Date> entitlementStartDates) {
		boolean beforeMinDate = false;
		if (!CollectionUtils.isEmpty(entitlementStartDates)) {
			Date minStartDate = Collections.min(entitlementStartDates);
			if (enrollmentStartDate.before(minStartDate) ) {
				beforeMinDate = true;
			}
		}
		return beforeMinDate;
	}
	
	public static boolean isEnrollmentEndDateBeforeEnrollmentStartDate(Date enrollmentStartDate, Date enrollmentEndDate) {
		return enrollmentEndDate.before(enrollmentStartDate);
	}
	
	public static boolean isEnrollmentEndDateBeforeTodayDate(Date enrollmentEndDate, GregorianCalendar todayDate) {
		return enrollmentEndDate.before(todayDate.getTime());
	}
	
	public static boolean isEnrollmentEndDateAfterMaxEntitlementEndDate(Date enrollmentEndDate, List<Date> entitlementEndDates) {
		boolean afterMaxEndDate = false;
		if (!CollectionUtils.isEmpty(entitlementEndDates)) {
			Date maxEndDate = Collections.max(entitlementEndDates);
			maxEndDate.setHours(23);
			maxEndDate.setMinutes(59);
			maxEndDate.setSeconds(59);
			if (enrollmentEndDate.after(maxEndDate) ) {
				afterMaxEndDate = true;
			}
		}
		return afterMaxEndDate;
	}
	
}
