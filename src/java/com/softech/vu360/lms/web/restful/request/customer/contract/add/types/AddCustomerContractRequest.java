package com.softech.vu360.lms.web.restful.request.customer.contract.add.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "resellerId",
    "key",
    "contractDetail",
    "courseGroupDetail",
    "organizationGroupDetail",
})
public class AddCustomerContractRequest {

	@XmlAttribute(name = "resellerId", required = true)
	private Long resellerId;
	
	@XmlAttribute(name = "key", required = true)
    private String key;
   
    @XmlElement(name = "ContractDetail", required = true)
    private ContractDetail contractDetail;
    
    @XmlElement(name = "CourseGroupDetail")
    private CourseGroupDetail courseGroupDetail;
    
    @XmlElement(name = "CourseDetail")
    private CourseDetail courseDetail;
    
    @XmlElement(name = "OrganizationGroupDetail")
	private OrganizationGroupDetail organizationGroupDetail;
  
	public OrganizationGroupDetail getOrganizationGroupDetail() {
		return organizationGroupDetail;
	}
	
	public void setOrganizationGroupDetail(OrganizationGroupDetail organizationGroupDetail) {
		this.organizationGroupDetail = organizationGroupDetail;
	}
	
	public CourseGroupDetail getCourseGroupDetail() {
		return courseGroupDetail;
	}
	
	public void setCourseGroupDetail(CourseGroupDetail courseGroupDetail) {
		this.courseGroupDetail = courseGroupDetail;
	}
	
	public ContractDetail getContractDetail() {
		return contractDetail;
	}
	
	public void setContractDetail(ContractDetail contractDetail) {
		this.contractDetail = contractDetail;
	}
	
	public Long getResellerId() {
		return resellerId;
	}

	public void setResellerId(Long resellerId) {
		this.resellerId = resellerId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public CourseDetail getCourseDetail() {
		return courseDetail;
	}

	public void setCourseDetail(CourseDetail courseDetail) {
		this.courseDetail = courseDetail;
	}

	@Override
	public String toString() {
		return "AddCustomerContractRequest [resellerId=" + resellerId
				+ ", key=" + key + ", contractDetail=" + contractDetail
				+ ", courseGroupDetail=" + courseGroupDetail
				+ ", organizationGroupDetail=" + organizationGroupDetail + "]";
	}
	
}
