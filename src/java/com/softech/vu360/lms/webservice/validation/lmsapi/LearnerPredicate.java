package com.softech.vu360.lms.webservice.validation.lmsapi;

import org.apache.commons.lang.StringUtils;

public class LearnerPredicate {

	public static boolean isValidUsername(String username) {
		return isValid(username);
	}
	
	public static boolean isCustomerCodeMathWithLearnerCustomerCode(String customerCode, String learnerCustomerCode) {
		return customerCode.equalsIgnoreCase(learnerCustomerCode);
	}
	
	private static boolean isValid(String value) {
		return StringUtils.isNotEmpty(value) || StringUtils.isNotBlank(value);
	}
	
}
