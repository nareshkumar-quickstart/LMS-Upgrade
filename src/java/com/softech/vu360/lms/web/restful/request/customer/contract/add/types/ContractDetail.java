package com.softech.vu360.lms.web.restful.request.customer.contract.add.types;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "contractEndType",
    "customerGuid",
    "contractName",
    "contractType",
    "allowSelfEnrollment",
    "maximumEnrollments",
    "startDate",
    "termOfServices",
    "endDate",
    "transactionAmount"
})
public class ContractDetail {

	@XmlAttribute(name = "contractEndType", required = true)
	private String contractEndType;
	
	@XmlAttribute(name = "allowUnlimitedEnrollments", required = true)
	protected Boolean allowUnlimitedEnrollments;
	
	@XmlElement(name = "CustomerGuid", required = true)
	private String customerGuid;
	
	@XmlElement(name = "ContractName", required = true)
	protected String contractName;
	
	@XmlElement(name = "ContractType", required = true)
	protected String contractType;
	
	@XmlElement(name = "AllowSelfEnrollment", required = true)
	protected Boolean allowSelfEnrollment;
	
	@XmlElement(name = "MaximumEnrollments", required = true)
	protected Integer maximumEnrollments;
	
	@XmlElement(name = "StartDate", required = true)
	//@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	protected Date startDate;
	
	@XmlElement(name = "TermOfServices", required = true)
	protected Integer termOfServices;
	
	@XmlElement(name = "EndDate", required = true)
	//@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	protected Date endDate;
	
	@XmlElement(name = "TransactionAmount")
	protected BigDecimal transactionAmount;
	
	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}
	
	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getContractEndType() {
		return contractEndType;
	}

	public void setContractEndType(String contractEndType) {
		this.contractEndType = contractEndType;
	}

	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public String getContractName() {
		return contractName;
	}
	
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	
	public String getCustomerGuid() {
		return customerGuid;
	}
	
	public void setCustomerGuid(String customerGuid) {
		this.customerGuid = customerGuid;
	}
	
	public Boolean getAllowSelfEnrollment() {
		return allowSelfEnrollment;
	}
	
	public void setAllowSelfEnrollment(Boolean allowSelfEnrollment) {
		this.allowSelfEnrollment = allowSelfEnrollment;
	}
	
	public Integer getTermOfServices() {
		return termOfServices;
	}
	
	public void setTermOfServices(Integer termOfServices) {
		this.termOfServices = termOfServices;
	}
	
	public Integer getMaximumEnrollments() {
		return maximumEnrollments;
	}

	public void setMaximumEnrollments(Integer maximumEnrollments) {
		this.maximumEnrollments = maximumEnrollments;
	}

	public Boolean getAllowUnlimitedEnrollments() {
		return allowUnlimitedEnrollments;
	}

	public void setAllowUnlimitedEnrollments(Boolean allowUnlimitedEnrollments) {
		this.allowUnlimitedEnrollments = allowUnlimitedEnrollments;
	}

	@Override
	public String toString() {
		return "ContractDetail [transactionAmount=" + transactionAmount
				+ ", endDate=" + endDate + ", contractType=" + contractType
				+ ", startDate=" + startDate + ", contractName=" + contractName
				+ ", customerGuid=" + customerGuid + ", allowSelfEnrollment="
				+ allowSelfEnrollment + ", termOfServices=" + termOfServices
				+ ", maximumEnrollments=" + maximumEnrollments + "]";
	}
	
}
