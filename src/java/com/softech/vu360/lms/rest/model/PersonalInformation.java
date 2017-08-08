package com.softech.vu360.lms.rest.model;

import java.util.List;

public class PersonalInformation {

	private Long vu360UserId;
	private Long learnerId;
	private String firstName;
	private String middleName;
	private String lastName;
	private LearnerProfileAddress address1;
	private LearnerProfileAddress address2;
	private String emailAddress;
	private String phone;
	private String officePhone;
	private String officeExtension;
	private String userName;
	private String password;
	private List<LearnerProfileTimeZone> timeZone;

	public Long getVu360UserId() {
		return vu360UserId;
	}

	public void setVu360UserId(Long vu360UserId) {
		this.vu360UserId = vu360UserId;
	}

	public Long getLearnerId() {
		return learnerId;
	}

	public void setLearnerId(Long learnerId) {
		this.learnerId = learnerId;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public LearnerProfileAddress getAddress1() {
		return address1;
	}
	public void setAddress1(LearnerProfileAddress address1) {
		this.address1 = address1;
	}
	public LearnerProfileAddress getAddress2() {
		return address2;
	}
	public void setAddress2(LearnerProfileAddress address2) {
		this.address2 = address2;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getOfficePhone() {
		return officePhone;
	}
	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}
	public String getOfficeExtension() {
		return officeExtension;
	}
	public void setOfficeExtension(String officeExtension) {
		this.officeExtension = officeExtension;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public List<LearnerProfileTimeZone> getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(List<LearnerProfileTimeZone> timeZone) {
		this.timeZone = timeZone;
	}
}
