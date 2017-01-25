package com.softech.vu360.lms.vo;

import java.io.Serializable;

public class MarketoPacketSerialized  implements Serializable{
	private static final long serialVersionUID = 3L;
	
	private final String firstName;
	private final String lastName;
	private final String emailAddress;
	private final String company;
	private final String courseName;
	private final String storeName;
	private final String eventName;
	private final String eventDate;
	private final String customerType;
	
	public MarketoPacketSerialized (String firstName, String lastName, String emailAddress, String company, String courseName, String storeName, String eventName, String eventDate, String customerType){
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.company = company;
		this.courseName = courseName;
		this.storeName = storeName;
		this.eventName = eventName;
		this.eventDate = eventDate;
		this.customerType = customerType;
	}

	public String getCustomerType() {
		return customerType;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getCompany() {
		return company;
	}

	public String getCourseName() {
		return courseName;
	}

	public String getStoreName() {
		return storeName;
	}

	public String getEventName() {
		return eventName;
	}

	public String getEventDate() {
		return eventDate;
	}
}
