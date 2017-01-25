package com.softech.vu360.lms.web.restful.request.customer.contract.add.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "orgGroupHierarchy",
    "maximumEnrollments"
})
public class OrganizationGroupEnrollment {

	@XmlAttribute(name = "OrgGroupHierarchy", required = true)
	protected String orgGroupHierarchy;
	
	@XmlAttribute(name = "MaximumEnrollments", required = true)
    protected Integer maximumEnrollments;
    
	public String getOrgGroupHierarchy() {
		return orgGroupHierarchy;
	}
	
	public void setOrgGroupHierarchy(String orgGroupHierarchy) {
		this.orgGroupHierarchy = orgGroupHierarchy;
	}
	
	public Integer getMaximumEnrollments() {
		return maximumEnrollments;
	}
	
	public void setMaximumEnrollments(Integer maximumEnrollments) {
		this.maximumEnrollments = maximumEnrollments;
	}

	@Override
	public String toString() {
		return "OrganizationGroupEnrollment [orgGroupHierarchy="
				+ orgGroupHierarchy + ", maximumEnrollments="
				+ maximumEnrollments + "]";
	}
    	
}
