package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.EmailAddress;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class RecipientEmailForm  implements ILMSBaseInterface{

	private ArrayList<String> emailAddresses = new ArrayList <String>();
	private int numberOfEmails = 0;
	private long recipientId;
	private String action = "";
	String emailAddress[] ;
	List<EmailAddress> emailAddressList=  new ArrayList<EmailAddress>();
	private static final long serialVersionUID = 1L;
	

	public ArrayList<String> getEmailAddresses() {
		return emailAddresses;
	}

	public void setEmailAddresses(ArrayList<String> emailAddresses) {
		this.emailAddresses = emailAddresses;
	}

	public int getNumberOfEmails() {
		return numberOfEmails;
	}

	public void setNumberOfEmails(int numberOfEmails) {
		this.numberOfEmails = numberOfEmails;
	}

	public long getrecipientId() {
		return recipientId;
	}

	public void setrecipientId(long recipientId) {
		this.recipientId = recipientId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String[] getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String[] emailAddress) {
		this.emailAddress = emailAddress;
	}

	public List<EmailAddress> getEmailAddressList() {
		return emailAddressList;
	}

	public void setEmailAddressList(List<EmailAddress> emailAddressList) {
		this.emailAddressList = emailAddressList;
	}
}
