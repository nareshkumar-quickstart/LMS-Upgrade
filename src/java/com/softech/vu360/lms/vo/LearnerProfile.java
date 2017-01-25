package com.softech.vu360.lms.vo;

import java.io.Serializable;

public class LearnerProfile implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private TimeZone timeZone;

	private Address learnerAddress = null;
	private Address learnerAddress2 = null;
	private String officePhone = null;
	private String officePhoneExtn = null;
	private String mobilePhone = null;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public Address getLearnerAddress() {
		return learnerAddress;
	}

	public void setLearnerAddress(Address learnerAddress) {
		this.learnerAddress = learnerAddress;
	}

	public String getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	public String getOfficePhoneExtn() {
		return officePhoneExtn;
	}

	public void setOfficePhoneExtn(String officePhoneExtn) {
		this.officePhoneExtn = officePhoneExtn;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public Address getLearnerAddress2() {
		return learnerAddress2;
	}

	public void setLearnerAddress2(Address learnerAddress2) {
		this.learnerAddress2 = learnerAddress2;
	}

}