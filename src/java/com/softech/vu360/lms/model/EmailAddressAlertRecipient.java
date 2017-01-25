package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
/**
 * 
 * @author marium.saud
 *
 */
@Entity
@DiscriminatorValue("EmailAddressAlertRecipient")
public class EmailAddressAlertRecipient extends AlertRecipient{
	
	
	private static final long serialVersionUID = 1L;
	
	@ManyToMany
    @JoinTable(name="ALERTRECIPIENT_EMAILADDRESS", joinColumns = @JoinColumn(name="ALERTRECIPIENT_ID"),inverseJoinColumns = @JoinColumn(name="EMAILADDRESS_ID"))
	private List<EmailAddress> emailAddress = new ArrayList<EmailAddress>();

	public List<EmailAddress> getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(List<EmailAddress> emailAddress) {
		this.emailAddress = emailAddress;
	}

	
	
}
