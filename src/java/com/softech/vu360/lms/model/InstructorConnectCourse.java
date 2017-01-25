/**
 *
 */
package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@DiscriminatorValue("Instructor Connect")
public class InstructorConnectCourse extends Course {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String COURSE_TYPE="Instructor Connect";
	public static final String WEB_EX="WebEx";
	public static final String LS_360="LS 360";
	public static final String OTHERS="Others";
	
	@Column(name = "INSTRUCTORCONNECTTYPE")
	private String instructorType;
	
	@Column(name = "INSTRUCTORCONNECTMEETINGID")
	private String meetingId;
	
	@Column(name = "INSTRUCTORCONNECTMEETINGPASSCODE")
	private String meetingPasscode;
	
	@Column(name = "INSTRUCTORCONNECTEMAILADDRESS")
	private String emailAddress;
	
	public InstructorConnectCourse() {
		super();
        setCourseType(COURSE_TYPE);
	}

	public String getInstructorType() {
		return instructorType;
	}

	public void setInstructorType(String instructorType) {
		this.instructorType = instructorType;
	}

	public String getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}

	public String getMeetingPasscode() {
		return meetingPasscode;
	}

	public void setMeetingPasscode(String meetingPasscode) {
		this.meetingPasscode = meetingPasscode;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	@Override
	public String getCourseType() {
		// TODO Auto-generated method stub
		return COURSE_TYPE;
	}
}
