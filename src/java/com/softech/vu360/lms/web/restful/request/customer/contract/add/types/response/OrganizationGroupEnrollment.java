package com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response;

public class OrganizationGroupEnrollment extends com.softech.vu360.lms.web.restful.request.customer.contract.add.types.OrganizationGroupEnrollment {

	private String errorCode;
	private String errorMessage;
	
	public String getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
