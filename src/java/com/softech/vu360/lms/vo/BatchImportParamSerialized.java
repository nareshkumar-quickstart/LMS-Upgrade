package com.softech.vu360.lms.vo;
/**
 * User: Faisal A. Siddiqui
 * Date: May 27, 2010
 * @since LMS-5781
 */


import java.io.Serializable;
import java.util.List;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.util.Brander;


public class BatchImportParamSerialized implements Serializable {
    
    private static final long serialVersionUID = 2L;
	
    private final Customer customer;
    private final boolean accLocked;
    private final boolean accVisibleOnReport;
    private final boolean isFirstRowHeader;
    private final boolean notifyLearnerOnRegistration;
    private final boolean changePasswordOnLogin;	// [9/23/2010] LMS-4958 :: Manager Mode > Batch Import: Option to Change Password on Next Login
    
    private final String delimiter;
    private String file;
    private final String loginURL;
    private final String actionOnDuplicateRecords;
    private final Brander brander;
    private final VU360User loggedInUser;
    private final List<CustomField> customFields;
    
    private String customerGuid;
    private String userName;
    private boolean sendCustomEmail;
    private String from;
    private String[] to;
	private String[] cc;
	private String[] bcc;
	private String subject;
	private String text;
	private String[] monitoringEmailAddress;
	private boolean readCsvFileContent;
    
	public BatchImportParamSerialized(Customer customer,String file, String delimiter, String actionOnDuplicateRecords,boolean accVisible,boolean accLocked,boolean isFirstRowHeader,boolean notifyLearnerOnRegistration, String loginURL,VU360User loggedInUser, Brander brander, boolean changePasswordOnLogin, List<CustomField> customFields, boolean readCsvFileContent)
	{
		this.customer=customer;
		this.file=file;
		this.delimiter=delimiter;
		this.actionOnDuplicateRecords=actionOnDuplicateRecords;
		this.accVisibleOnReport=accVisible;
		this.accLocked=accLocked;
		this.isFirstRowHeader=isFirstRowHeader;
		this.notifyLearnerOnRegistration=notifyLearnerOnRegistration;
		this.loginURL=loginURL;
		this.loggedInUser=loggedInUser;
		//this.velocityEngine=velocityEngine;
		this.brander=brander;
		this.changePasswordOnLogin = changePasswordOnLogin;		// [9/23/2010] LMS-4958 :: Manager Mode > Batch Import: Option to Change Password on Next Login
		this.customFields = customFields;
		this.readCsvFileContent = readCsvFileContent;
	}


	public String getFile() {
		return file;
	}


	public void setFile(String file) {
		this.file = file;
	}


	public Customer getCustomer() {
		return customer;
	}


	public boolean isAccLocked() {
		return accLocked;
	}


	public boolean isAccVisibleOnReport() {
		return accVisibleOnReport;
	}


	public boolean isFirstRowHeader() {
		return isFirstRowHeader;
	}


	public boolean isNotifyLearnerOnRegistration() {
		return notifyLearnerOnRegistration;
	}


	public boolean isChangePasswordOnLogin() {
		return changePasswordOnLogin;
	}


	public String getDelimiter() {
		return delimiter;
	}


	public String getLoginURL() {
		return loginURL;
	}


	public String getActionOnDuplicateRecords() {
		return actionOnDuplicateRecords;
	}


	public Brander getBrander() {
		return brander;
	}


/*	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}*/


	public VU360User getLoggedInUser() {
		return loggedInUser;
	}


	public List<CustomField> getCustomFields() {
		return customFields;
	}


	public String getCustomerGuid() {
		return customerGuid;
	}


	public void setCustomerGuid(String customerGuid) {
		this.customerGuid = customerGuid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isSendCustomEmail() {
		return sendCustomEmail;
	}

	public void setSendCustomEmail(boolean sendCustomEmail) {
		this.sendCustomEmail = sendCustomEmail;
	}


	public String getFrom() {
		return from;
	}


	public void setFrom(String from) {
		this.from = from;
	}


	public String[] getTo() {
		return to;
	}


	public void setTo(String[] to) {
		this.to = to;
	}


	public String[] getCc() {
		return cc;
	}


	public void setCc(String[] cc) {
		this.cc = cc;
	}


	public String[] getBcc() {
		return bcc;
	}


	public void setBcc(String[] bcc) {
		this.bcc = bcc;
	}


	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}

	public String[] getMonitoringEmailAddress() {
		return monitoringEmailAddress;
	}

	public void setMonitoringEmailAddress(String[] monitoringEmailAddress) {
		this.monitoringEmailAddress = monitoringEmailAddress;
	}


	public boolean isReadCsvFileContent() {
		return readCsvFileContent;
	}


	public void setReadCsvFileContent(boolean readCsvFileContent) {
		this.readCsvFileContent = readCsvFileContent;
	}
	
}
