package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author muhammad.saleem
 *
 */

@Entity
@Table(name = "LEARNERPREFERENCE")
public class LearnerPreferences implements SearchableKey {

	public static final String BANDWIDTH_HIGH = "high";
	public static final String BANDWIDTH_LOW = "low";
	
	@Id
	@javax.persistence.TableGenerator(name = "LEARNERPREFERENCE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LEARNERPREFERENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "LEARNERPREFERENCE_ID")
	private Long id;
	
	@OneToOne
    @JoinColumn(name="LEARNER_ID")
	private Learner learner ;
	
	@Column(name="AUDIOTF")
	private Boolean isAudioEnabled = false;
	
	@Column(name="CAPTIONINGTF")
	private Boolean isCaptioningEnabled = false;
	
	@Column(name="BANDWIDTHTF")
	private String bandwidth = BANDWIDTH_HIGH;
	
	@Column(name="VOLUME")
	private Integer volume = 0;
	
	@Column(name="VIDEOTF")
	private Boolean isVedioEnabled = false;
	
	@Column(name="ENROLLMENTEMAILTF")
	private Boolean isEnrollmentEmailEnabled = true;
	
	@Column(name="REGISTRATIONEMAILTF")
	private Boolean inRegistrationEmialEnabled = true;
	
	@Column(name="AVATAR")
	private String avatar = null;
	
	@Column(name="COURSECOMPLETIONCERTIFICATEEMAILTF")
	private Boolean isCourseCompletionCertificateEmailEnabled = true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the learner
	 */
	public Learner getLearner() {
		return this.learner;
	}

	/**
	 * @param learner
	 *            the learner to set
	 */
	public void setLearner(Learner learner) {
		this.learner = learner;
	}

	/**
	 * @return the isAudioEnabled
	 */
	public  Boolean isAudioEnabled() {
		return isAudioEnabled;
	}

	/**
	 * @param isAudioEnabled
	 *            the isAudioEnabled to set
	 */
	public void setAudioEnabled(Boolean isAudioEnabled) {
		this.isAudioEnabled = isAudioEnabled;
	}

	/**
	 * @return the isCaptioningEnabled
	 */
	public  Boolean isCaptioningEnabled() {
		return isCaptioningEnabled;
	}

	/**
	 * @param isCaptioningEnabled
	 *            the isCaptioningEnabled to set
	 */
	public void setCaptioningEnabled(Boolean isCaptioningEnabled) {
		this.isCaptioningEnabled = isCaptioningEnabled;
	}

	/**
	 * @return the bandwidth
	 */
	public String getBandwidth() {
		return bandwidth;
	}

	/**
	 * @param bandwidth
	 *            the bandwidth to set
	 */
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	/**
	 * @return the volume
	 */
	public Integer getVolume() {
		return volume;
	}

	/**
	 * @param volume
	 *            the volume to set
	 */
	public void setVolume(Integer volume) {
		this.volume = volume;
	}

	/**
	 * @return the isVedioEnabled
	 */
	public  Boolean isVedioEnabled() {
		return isVedioEnabled;
	}

	/**
	 * @param isVedioEnabled
	 *            the isVedioEnabled to set
	 */
	public void setVedioEnabled(Boolean isVedioEnabled) {
		this.isVedioEnabled = isVedioEnabled;
	}

	/**
	 * @return the isEnrollmentEmailEnabled
	 */
	public  Boolean isEnrollmentEmailEnabled() {
		return isEnrollmentEmailEnabled;
	}

	/**
	 * @param isEnrollmentEmailEnabled
	 *            the isEnrollmentEmailEnabled to set
	 */
	public void setEnrollmentEmailEnabled(Boolean isEnrollmentEmailEnabled) {
		this.isEnrollmentEmailEnabled = isEnrollmentEmailEnabled;
	}

	/**
	 * @return the inRegistrationEmialEnabled
	 */
	public  Boolean isInRegistrationEmialEnabled() {
		return inRegistrationEmialEnabled;
	}

	/**
	 * @param inRegistrationEmialEnabled
	 *            the inRegistrationEmialEnabled to set
	 */
	public void setInRegistrationEmialEnabled(Boolean inRegistrationEmialEnabled) {
		this.inRegistrationEmialEnabled = inRegistrationEmialEnabled;
	}

	/**
	 * @param avatar the avatar to set
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	/**
	 * @return the avatar
	 */
	public String getAvatar() {
		return avatar;
	}
	public void updateValuesByCustomerPreferences(CustomerPreferences customerPreferences){
		this.setAudioEnabled(customerPreferences.isAudioEnabled());
		this.setBandwidth(customerPreferences.getBandwidth());
		this.setCaptioningEnabled(customerPreferences.isCaptioningEnabled());
		this.setEnrollmentEmailEnabled(customerPreferences.isEnableEnrollmentEmailsForNewCustomers());
		this.setInRegistrationEmialEnabled(customerPreferences.isEnableRegistrationEmailsForNewCustomers());
		this.setVedioEnabled(customerPreferences.isVedioEnabled());
		this.setVolume(customerPreferences.getVolume());
		this.setCourseCompletionCertificateEmailEnabled( customerPreferences.isCourseCompletionCertificateEmailEnabled() );
	}

	/**
	 * LMS-8318
	 * @param isCourseCompletionCertificateEmailEnabled the isCourseCompletionCertificateEmailEnabled to set
	 */
	public void setCourseCompletionCertificateEmailEnabled(
			 Boolean isCourseCompletionCertificateEmailEnabled) {
		this.isCourseCompletionCertificateEmailEnabled = isCourseCompletionCertificateEmailEnabled;
	}

	/**
	 * LMS-8318
	 * @return the isCourseCompletionCertificateEmailEnabled
	 */
	public  Boolean isCourseCompletionCertificateEmailEnabled() {
		return isCourseCompletionCertificateEmailEnabled;
	}

}
