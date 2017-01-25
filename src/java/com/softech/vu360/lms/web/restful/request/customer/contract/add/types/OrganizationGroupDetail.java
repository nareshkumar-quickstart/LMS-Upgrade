package com.softech.vu360.lms.web.restful.request.customer.contract.add.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "organizationGroupsEnrollments"
})
public class OrganizationGroupDetail {

	@XmlElement(name = "OrganizationGroupsEnrollments")
	private OrganizationGroupsEnrollments organizationGroupsEnrollments;

	public OrganizationGroupsEnrollments getOrganizationGroupsEnrollments() {
		return organizationGroupsEnrollments;
	}

	public void setOrganizationGroupsEnrollments(OrganizationGroupsEnrollments organizationGroupsEnrollments) {
		this.organizationGroupsEnrollments = organizationGroupsEnrollments;
	}

	@Override
	public String toString() {
		return "OrganizationGroupDetail [organizationGroupsEnrollments="
				+ organizationGroupsEnrollments + "]";
	}
	
}
