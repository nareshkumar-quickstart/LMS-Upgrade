package com.softech.vu360.lms.web.restful.request.customer.contract.add.types;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "organizationGroupEnrollment"
})
public class OrganizationGroupsEnrollments {

	@XmlElement(name = "OrganizationGroupEnrollment", required = true)
	private List<OrganizationGroupEnrollment> organizationGroupEnrollment;

	public List<OrganizationGroupEnrollment> getOrganizationGroupEnrollment() {
		return organizationGroupEnrollment;
	}

	public void setOrganizationGroupEnrollment(List<OrganizationGroupEnrollment> organizationGroupEnrollment) {
		this.organizationGroupEnrollment = organizationGroupEnrollment;
	}

	@Override
	public String toString() {
		return "OrganizationGroupsEnrollments [organizationGroupEnrollment="
				+ organizationGroupEnrollment + "]";
	}
	
}
