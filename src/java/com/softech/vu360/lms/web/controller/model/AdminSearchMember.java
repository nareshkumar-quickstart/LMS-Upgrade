package com.softech.vu360.lms.web.controller.model;

import java.util.Date;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.lms.web.filter.AdminSearchType;

public class AdminSearchMember  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name = null;
	private String eMail = null;
	private boolean status = true;
	private AdminSearchType currentSearchType = AdminSearchType.NONE;
	private String customerCode=null;
	private String resellerCode=null;
	private Customer customer;	
	private String contractCreationDate = null;
	// LMS-14443
	private String originalContractCreationDate = null;
	private String recentContractCreationDate = null;
	
	public Date originalContractCreationDatetime = null;
	public Date recentContractCreationDatetime = null;
	// End:LMS-14443
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		
		return name.replace(" ", "&nbsp;");
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the eMail
	 */
	public String getEMail() {
		return eMail;
	}
	/**
	 * @param mail the eMail to set
	 */
	public void setEMail(String mail) {
		eMail = mail;
	}
	/**
	 * @return the status
	 */
	public boolean isStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}
	/**
	 * @return the currentSearchType
	 */
	public AdminSearchType getCurrentSearchType() {
		return currentSearchType;
	}
	/**
	 * @param currentSearchType the currentSearchType to set
	 */
	public void setCurrentSearchType(AdminSearchType currentSearchType) {
		this.currentSearchType = currentSearchType;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getResellerCode() {
		return resellerCode;
	}
	public void setResellerCode(String resellerCode) {
		this.resellerCode = resellerCode;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public String getContractCreationDate() {
		return contractCreationDate;
	}
	public void setContractCreationDate(String contractCreationDate) {
		this.contractCreationDate = contractCreationDate;
	}
	// LMS-14443
	public String getOriginalContractCreationDate() {
		return originalContractCreationDate;
	}
	public void setOriginalContractCreationDate(String originalContractCreationDate) {
		this.originalContractCreationDate = originalContractCreationDate;
	}
	public String getRecentContractCreationDate() {
		return recentContractCreationDate;
	}
	public void setRecentContractCreationDate(String recentContractCreationDate) {
		this.recentContractCreationDate = recentContractCreationDate;
	}
	
	// End:LMS-14443
	

}
