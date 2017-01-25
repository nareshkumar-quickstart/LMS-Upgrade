package com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response;

import java.util.List;

public class InvalidOrganizationGroupsEnrollments {

	private List<OrganizationGroupEnrollment> organizationGroupEnrollment;

	public List<OrganizationGroupEnrollment> getOrganizationGroupEnrollment() {
		return organizationGroupEnrollment;
	}

	public void setOrganizationGroupEnrollment(List<OrganizationGroupEnrollment> organizationGroupEnrollment) {
		this.organizationGroupEnrollment = organizationGroupEnrollment;
	}
	
}
