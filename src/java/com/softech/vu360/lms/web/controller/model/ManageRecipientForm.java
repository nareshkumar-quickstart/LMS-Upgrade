package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.AlertRecipient;
import com.softech.vu360.lms.model.EmailAddress;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;


public class ManageRecipientForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	String[] selectedLearnersId=null;
	private String recipientType;
	private String firstName="";
	private String lastName="";
	private String emailAddress="";
	private String alertRecipientGroupName="";
	private String learnergroupName;
	private String organizationlgroupName;
	private String emailaddress;
	private Long id;
	private Long alertId;
	List<VU360User> selectedLearners = new ArrayList<VU360User>();
	public String getAlertRecipientGroupName() {
		return alertRecipientGroupName;
	}
	public void setAlertRecipientGroupName(String alertRecipientGroupName) {
		this.alertRecipientGroupName = alertRecipientGroupName;
	}
	
	private String alertRecipientGroupType;
	private AlertRecipient recipient=new AlertRecipient();
	
	
	public AlertRecipient getRecipient() {
		return recipient;
	}
	public void setRecipient(AlertRecipient recipient) {
		this.recipient = recipient;
	}
	public List<AlertRecipient> getRecips() {
		return recips;
	}
	public void setRecips(List<AlertRecipient> recips) {
		this.recips = recips;
	}
	private List<AlertRecipient> recips = new ArrayList<AlertRecipient>();
	
    private List<MngAlert> mngAlerts = new ArrayList<MngAlert>();
    
    List<Learner> learnerListFromDB =  new ArrayList<Learner>();
    List<LearnerGroup> learnerGroupListFromDB =  new ArrayList<LearnerGroup>();
    List<OrganizationalGroup> organizationalGroupListFromDB =  new ArrayList<OrganizationalGroup>();
    List<EmailAddress> EmailAddressFromDB =  new ArrayList<EmailAddress>();
	
    public List<MngAlert> getMngAlerts() {
		return mngAlerts;
	}
	public void setMngAlerts(List<MngAlert> mngAlerts) {
		this.mngAlerts = mngAlerts;
	}
	public String getRecipientType() {
		return recipientType;
	}
	public void setRecipientType(String recipientType) {
		this.recipientType = recipientType;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<VU360User> getSelectedLearners() {
		return selectedLearners;
	}
	public void setSelectedLearners(List<VU360User> selectedLearners) {
		this.selectedLearners = selectedLearners;
	}
	public List<Learner> getLearnerListFromDB() {
		return learnerListFromDB;
	}
	public void setLearnerListFromDB(List<Learner> learnerListFromDB) {
		this.learnerListFromDB = learnerListFromDB;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public List<LearnerGroup> getLearnerGroupListFromDB() {
		return learnerGroupListFromDB;
	}
	public void setLearnerGroupListFromDB(List<LearnerGroup> learnerGroupListFromDB) {
		this.learnerGroupListFromDB = learnerGroupListFromDB;
	}
	public List<OrganizationalGroup> getOrganizationalGroupListFromDB() {
		return organizationalGroupListFromDB;
	}
	public void setOrganizationalGroupListFromDB(
			List<OrganizationalGroup> organizationalGroupListFromDB) {
		this.organizationalGroupListFromDB = organizationalGroupListFromDB;
	}
	public List<EmailAddress> getEmailAddressFromDB() {
		return EmailAddressFromDB;
	}
	public void setEmailAddressFromDB(List<EmailAddress> emailAddressFromDB) {
		EmailAddressFromDB = emailAddressFromDB;
	}
	public String[] getSelectedLearnersId() {
		return selectedLearnersId;
	}
	public void setSelectedLearnersId(String[] selectedLearnersId) {
		this.selectedLearnersId = selectedLearnersId;
	}
	public String getLearnergroupName() {
		return learnergroupName;
	}
	public void setLearnergroupName(String learnergroupName) {
		this.learnergroupName = learnergroupName;
	}
	public String getOrganizationlgroupName() {
		return organizationlgroupName;
	}
	public void setOrganizationlgroupName(String organizationlgroupName) {
		this.organizationlgroupName = organizationlgroupName;
	}
	public String getEmailaddress() {
		return emailaddress;
	}
	public void setEmailaddress(String emailaddress) {
		this.emailaddress = emailaddress;
	}
	public Long getAlertId() {
		return alertId;
	}
	public void setAlertId(Long alertId) {
		this.alertId = alertId;
	}
	public String getAlertRecipientGroupType() {
		return alertRecipientGroupType;
	}
	public void setAlertRecipientGroupType(String alertRecipientGroupType) {
		this.alertRecipientGroupType = alertRecipientGroupType;
	}

}