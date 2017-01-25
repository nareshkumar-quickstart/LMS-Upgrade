package com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddCustomerContractResponse", propOrder = {
    "contractDetailResponse",
	"courseGroupDetailResponse",
	"courseDetailResponse",
	"organizationGroupDetailResponse"
})
public class AddCustomerContractResponse {

	@XmlAttribute(name = "ErrorCode")
	private String errorCode;
	
	@XmlAttribute(name = "ErrorMessage")
	private String errorMessage;
	
	@XmlElement(name = "OrganizationGroupDetailResponse")
    private OrganizationGroupDetailResponse organizationGroupDetailResponse;
	
	@XmlElement(name = "CourseDetailResponse")
    private CourseDetailResponse courseDetailResponse;
	
	@XmlElement(name = "CourseGroupDetailResponse")
    private CourseGroupDetailResponse courseGroupDetailResponse;
	
	@XmlElement(name = "ContractDetailResponse")
    private ContractDetailResponse contractDetailResponse;
    
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
	
	public OrganizationGroupDetailResponse getOrganizationGroupDetailResponse() {
		return organizationGroupDetailResponse;
	}
	
	public void setOrganizationGroupDetailResponse(OrganizationGroupDetailResponse organizationGroupDetailResponse) {
		this.organizationGroupDetailResponse = organizationGroupDetailResponse;
	}
	
	public CourseDetailResponse getCourseDetailResponse() {
		return courseDetailResponse;
	}
	
	public void setCourseDetailResponse(CourseDetailResponse courseDetailResponse) {
		this.courseDetailResponse = courseDetailResponse;
	}
	
	public CourseGroupDetailResponse getCourseGroupDetailResponse() {
		return courseGroupDetailResponse;
	}
	
	public void setCourseGroupDetailResponse(CourseGroupDetailResponse courseGroupDetailResponse) {
		this.courseGroupDetailResponse = courseGroupDetailResponse;
	}
	public ContractDetailResponse getContractDetailResponse() {
		return contractDetailResponse;
	}
	
	public void setContractDetailResponse(ContractDetailResponse contractDetailResponse) {
		this.contractDetailResponse = contractDetailResponse;
	}
    
}
