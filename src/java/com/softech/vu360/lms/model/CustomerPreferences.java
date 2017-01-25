package com.softech.vu360.lms.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@Table(name = "CUSTOMERPREFERENCE")
public class CustomerPreferences implements SearchableKey {

	private static final long serialVersionUID = 1L;
	public static final String BANDWIDTH_HIGH = "high";
	public static final String BANDWIDTH_LOW = "low";
	
	@Id
	@javax.persistence.TableGenerator(name = "CUSTOMERPREFERENCE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "CUSTOMERPREFERENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CUSTOMERPREFERENCE_ID")
	private Long id;
	
	@Column(name = "EnableRegistrationEmailsTF")
	private Boolean enableRegistrationEmailsForNewCustomers = false;
	
	@Column(name = "EnableEnrollmentEmails")
	private Boolean enableEnrollmentEmailsForNewCustomers = false;
	
	@Column(name = "EnableSelfEnrollmentEmails")
	private Boolean enableSelfEnrollmentEmailsForNewCustomers = false;
	
	//Not in use
	@Transient
	private List<CustomUserField> customUserProfileFields = null;
	
	@OneToOne (fetch = FetchType.LAZY)
	@JoinColumn(name="CUSTOMER_ID")
	private Customer customer = null;
	
	@Column(name = "AudioTF")
	private Boolean isAudioEnabled = false;
	
	@Column(name = "BlankSearchTF")
    private Boolean isBlankSearchEnabled = false;
	
	@Column(name = "CaptioningTF")
	private Boolean isCaptioningEnabled = false;
	
	@Column(name = "Bandwidth")
	private String bandwidth = BANDWIDTH_HIGH;
	
	@Column(name = "Volume")
	private Integer volume = 0;
	
	@Column(name = "VideoTF")
	private Boolean isVedioEnabled = false;
	
	@Column(name = "AUDIOLOCKTF")
	private Boolean isAudioLocked = false;
	
	@Column(name = "VOLUMELOCKTF")
	private Boolean isVolumeLocked = false;
	
	@Column(name = "CAPTIONINGLOCKTF")
	private Boolean isCaptioningLocked = false;
	
	@Column(name = "BANDWIDTHLOCKTF")
	private Boolean isBandwidthLocked = false;
	
	@Column(name = "VIDEOLOCKTF")
	private Boolean isVideoLocked = false;
	
	@Column(name = "REGISTRATIONEMAILLOCKEDTF")
	private Boolean isRegistrationEmailLocked = false;
	
	@Column(name = "ENROLLMENTEMAILLOCKEDTF")
	private Boolean isEnrollmentEmailLocked = false;
	
	@Column(name = "COURSECOMPLETIONCERTIFICATEEMAILTF")
	private Boolean isCourseCompletionCertificateEmailEnabled = true;
	
	@Column(name = "COURSECOMPLETIONCERTIFICATEEMAILLOCKEDTF")
	private Boolean isCourseCompletionCertificateEmailLocked = false;
	
	

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	public String getKey() {
		return id.toString();
	}

	/**
	 * @return the customUserProfileFields
	 */
	public List<CustomUserField> getCustomUserField() {
		return customUserProfileFields;
	}

	/**
	 * @param customUserProfileFields
	 *            the customUserProfileFields to set
	 */
	public void setCustomUserField(List<CustomUserField> customUserProfileFields) {
		this.customUserProfileFields = customUserProfileFields;
	}

	/**
	 * @return the customUserProfileFields
	 */
	public List<CustomUserField> getCustomUserProfileFields() {
		return customUserProfileFields;
	}

	/**
	 * @param customUserProfileFields
	 *            the customUserProfileFields to set
	 */
	public void setCustomUserProfileFields(
			List<CustomUserField> customUserProfileFields) {
		this.customUserProfileFields = customUserProfileFields;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return the isAudioEnabled
	 */
	public Boolean isAudioEnabled() {
		if(isAudioEnabled==null){
			isAudioEnabled=Boolean.FALSE;
		    }
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
     * @return the isBlankSearchEnabled
     * TODO FIND PROPER WORKROUND.
     */
    public Boolean isBlankSearchEnabled() {

    	if(this.isBlankSearchEnabled==null)
    		this.isBlankSearchEnabled= new Boolean (false);

        return isBlankSearchEnabled;
    }

    /**
     * @param isBlankSearchEnabled
     * the isBlankSearchEnabled to set
     * 
     * TODO FIND PROPER WORKROUND.
     */
    public void setBlankSearchEnabled(Boolean isBlankSearchEnabled) {
    	if(isBlankSearchEnabled==null)
    		isBlankSearchEnabled= new Boolean (false);
    	
        this.isBlankSearchEnabled = isBlankSearchEnabled;
    }



    /**
	 * @return the isCaptioningEnabled
	 */
	public Boolean isCaptioningEnabled() {
		if(isCaptioningEnabled==null){
			isCaptioningEnabled=Boolean.FALSE;
		    }
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
	 * @return the isVedioEnabled
	 */
	public Boolean isVedioEnabled() {
		return isVedioEnabled;
	}

	/**
	 * @param isVedioEnabled
	 *            the isVedioEnabled to set
	 */
	public void setVedioEnabled(Boolean isVedioEnabled) {
		this.isVedioEnabled = isVedioEnabled;
	}

	public Boolean isAudioLocked() {
		return isAudioLocked;
	}

	public Boolean isBandwidthLocked() {
		return isBandwidthLocked;
	}

	public Boolean isVolumeLocked() {
		return isVolumeLocked;
	}

	public void setVolumeLocked(Boolean isVolumeLocked) {
		this.isVolumeLocked = isVolumeLocked;
	}

	public Boolean isCaptioningLocked() {
		return isCaptioningLocked;
	}

	public void setCaptioningLocked(Boolean isCaptioningLocked) {
		this.isCaptioningLocked = isCaptioningLocked;
	}

	public Boolean isVideoLocked() {
		return isVideoLocked;
	}

	public void setVideoLocked(Boolean isVideoLocked) {
		this.isVideoLocked = isVideoLocked;
	}

	public void setAudioLocked(Boolean isAudioLocked) {
		this.isAudioLocked = isAudioLocked;
	}

	public void setBandwidthLocked(Boolean isBandwidthLocked) {
		this.isBandwidthLocked = isBandwidthLocked;
	}

	public Boolean isEnableRegistrationEmailsForNewCustomers() {
		if(enableRegistrationEmailsForNewCustomers==null){
			enableRegistrationEmailsForNewCustomers=Boolean.FALSE;
		    }
		return enableRegistrationEmailsForNewCustomers;
	}

	public void setEnableRegistrationEmailsForNewCustomers(
			Boolean enableRegistrationEmailsForNewCustomers) {
		this.enableRegistrationEmailsForNewCustomers = enableRegistrationEmailsForNewCustomers;
	}

	public Boolean isEnableEnrollmentEmailsForNewCustomers() {
		if(enableEnrollmentEmailsForNewCustomers==null){
			enableEnrollmentEmailsForNewCustomers=Boolean.FALSE;
		    }
		return enableEnrollmentEmailsForNewCustomers;
	}

	public void setEnableEnrollmentEmailsForNewCustomers(
			Boolean enableEnrollmentEmailsForNewCustomers) {
		this.enableEnrollmentEmailsForNewCustomers = enableEnrollmentEmailsForNewCustomers;
	}

	public Boolean isEnableSelfEnrollmentEmailsForNewCustomers() {
		if(enableSelfEnrollmentEmailsForNewCustomers==null){
			enableSelfEnrollmentEmailsForNewCustomers=Boolean.FALSE;
		    }
		return enableSelfEnrollmentEmailsForNewCustomers;
	}

	public void setEnableSelfEnrollmentEmailsForNewCustomers(
			Boolean enableSelfEnrollmentEmailsForNewCustomers) {
		this.enableSelfEnrollmentEmailsForNewCustomers = enableSelfEnrollmentEmailsForNewCustomers;
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
	 * @return the isRegistrationEmailLocked
	 */
	public Boolean isRegistrationEmailLocked() {
		 if(isRegistrationEmailLocked==null){
			 isRegistrationEmailLocked=Boolean.FALSE;
		    }
		return isRegistrationEmailLocked;
	}

	/**
	 * @param isRegistrationEmailLocked the isRegistrationEmailLocked to set
	 */
	public void setRegistrationEmailLocked(Boolean isRegistrationEmailLocked) {
		this.isRegistrationEmailLocked = isRegistrationEmailLocked;
	}

	/**
	 * @return the isEnrollmentEmailLocked
	 */
	public Boolean isEnrollmentEmailLocked() {
		if(isEnrollmentEmailLocked==null){
			isEnrollmentEmailLocked=Boolean.FALSE;
		    }
		return isEnrollmentEmailLocked;
	}

	/**
	 * @param isEnrollmentEmailLocked the isEnrollmentEmailLocked to set
	 */
	public void setEnrollmentEmailLocked(Boolean isEnrollmentEmailLocked) {
		this.isEnrollmentEmailLocked = isEnrollmentEmailLocked;
	}

	/**
	 * LMS-8318
	 * @param isCourseCompletionCertificateEmailEnabled the isCourseCompletionCertificateEmailEnabled to set
	 */
	public void setCourseCompletionCertificateEmailEnabled(Boolean isCourseCompletionCertificateEmailEnabled) {
		this.isCourseCompletionCertificateEmailEnabled = isCourseCompletionCertificateEmailEnabled;
	}

	/**
	 * LMS-8318
	 * @return the isCourseCompletionCertificateEmailEnabled
	 */
	public Boolean isCourseCompletionCertificateEmailEnabled() {

		if(this.isCourseCompletionCertificateEmailEnabled==null)
    		this.isCourseCompletionCertificateEmailEnabled= new Boolean (true);

		return isCourseCompletionCertificateEmailEnabled;
	}

	/**
	 * LMS-8318
	 * @param isCourseCompletionCertificateEmailLocked the isCourseCompletionCertificateEmailLocked to set
	 */
	public void setCourseCompletionCertificateEmailLocked(Boolean isCourseCompletionCertificateEmailLocked) {
		this.isCourseCompletionCertificateEmailLocked = isCourseCompletionCertificateEmailLocked;
	}

	/**
	 * LMS-8318
	 * @return the isCourseCompletionCertificateEmailLocked
	 */
	public Boolean isCourseCompletionCertificateEmailLocked() {
		
		if(this.isCourseCompletionCertificateEmailLocked==null)
    		this.isCourseCompletionCertificateEmailLocked= new Boolean (false);

		return isCourseCompletionCertificateEmailLocked;
	}

}