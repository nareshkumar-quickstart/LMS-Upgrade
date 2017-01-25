package com.softech.vu360.lms.web.controller.model.accreditation;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;


public class ManageApproval  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private long id;
	private String approvalName;
	private String approvalNumber;
	private String approvalType;
	private String regulatorName;
	private String active;
	
	
	/**
	 * @return the approvalName
	 */
	public String getApprovalName() {
		return approvalName;
	}
	/**
	 * @param approvalName the approvalName to set
	 */
	public void setApprovalName(String approvalName) {
		this.approvalName = approvalName;
	}
	/**
	 * @return the approvalNumber
	 */
	public String getApprovalNumber() {
		return approvalNumber;
	}
	/**
	 * @param approvalNumber the approvalNumber to set
	 */
	public void setApprovalNumber(String approvalNumber) {
		this.approvalNumber = approvalNumber;
	}
	/**
	 * @return the approvalType
	 */
	public String getApprovalType() {
		return approvalType;
	}
	/**
	 * @param approvalType the approvalType to set
	 */
	public void setApprovalType(String approvalType) {
		this.approvalType = approvalType;
	}
	/**
	 * @return the regulatorName
	 */
	public String getRegulatorName() {
		return regulatorName;
	}
	/**
	 * @param regulatorName the regulatorName to set
	 */
	public void setRegulatorName(String regulatorName) {
		this.regulatorName = regulatorName;
	}
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the active
	 */
	public String getActive() {
		return active;
	}
	/**
	 * @param active the active to set
	 */
	public void setActive(String active) {
		this.active = active;
	}
	
	
}
