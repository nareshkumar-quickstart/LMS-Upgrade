package com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response;

public class OrganizationGroupDetailResponse {

	private String errorMessage;
    private String errorCode;
	private OrganizationalGroups organizationalGroups;
    private InvalidOrganizationGroupsEnrollments invalidOrganizationGroupsEnrollments;
    
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public OrganizationalGroups getOrganizationalGroups() {
		return organizationalGroups;
	}
	
	public void setOrganizationalGroups(OrganizationalGroups organizationalGroups) {
		this.organizationalGroups = organizationalGroups;
	}
	
	public InvalidOrganizationGroupsEnrollments getInvalidOrganizationGroupsEnrollments() {
		return invalidOrganizationGroupsEnrollments;
	}
	
	public void setInvalidOrganizationGroupsEnrollments(InvalidOrganizationGroupsEnrollments invalidOrganizationGroupsEnrollments) {
		this.invalidOrganizationGroupsEnrollments = invalidOrganizationGroupsEnrollments;
	}
    
}
