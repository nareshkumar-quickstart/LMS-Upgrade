package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "DISTRIBUTORPREFERENCE")
public class DistributorPreferences implements SearchableKey {

	private static final long serialVersionUID = 3619338503897937337L;
	
	public static final String BANDWIDTH_HIGH = "high";
	public static final String BANDWIDTH_LOW = "low";
	
	@Id
    @javax.persistence.TableGenerator(name = "DISTRIBUTORPREFERENCE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "DISTRIBUTORPREFERENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "DISTRIBUTORPREFERENCE_ID")
	private Long id;
	
	@OneToOne
	//@PrimaryKeyJoinColumn
	@JoinColumn(name="DISTRIBUTOR_ID")
	private Distributor distributor = null;
	
	@Column(name = "EnableRegistrationEmailsForNewCustomersTF")
	private Boolean enableRegistrationEmailsForNewCustomers = false;
	
	@Column(name = "EnableEnrollmentEmailsForNewCustomers")
	private Boolean enableEnrollmentEmailsForNewCustomers = false;
	
	@Column(name = "EnableSelfEnrollmentEmailsForNewCustomers")
	private Boolean enableSelfEnrollmentEmailsForNewCustomers = false;
	
	@Column(name = "AudioTF")
	private Boolean isAudioEnabled = false;
	
	@Column(name = "CaptioningTF")
	private Boolean isCaptioningEnabled = false;
	
	@Column(name = "Bandwidth")
	private String bandwidth = BANDWIDTH_HIGH;
	
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
	
	@Column(name = "Volume")
	private Integer volume = 0;
	
	@Column(name = "REGISTRAIONEMAILLOCKEDTF")
	private Boolean isRegistrationEmailLocked = false;
	
	@Column(name = "ENROLLMENTEMAILLOCKEDTF")
	private Boolean isEnrollmentEmailLocked = false;
	
	@Column(name = "COURSECOMPLETIONCERTIFICATEEMAILTF")
	private Boolean isCourseCompletionCertificateEmailEnabled = Boolean.TRUE;
	
	@Column(name = "COURSECOMPLETIONCERTIFICATEEMAILLOCKEDTF")
	private Boolean isCourseCompletionCertificateEmailLocked = Boolean.FALSE;
	
	

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
	 * @return the distributor
	 */
	public Distributor getDistributor() {
		return distributor;
	}

	/**
	 * @param distributor
	 *            the distributor to set
	 */
	public void setDistributor(Distributor distributor) {
		this.distributor = distributor;
	}

	/**
	 * @return the enableRegistrationEmailsForNewCustomers
	 */
	public  Boolean isEnableRegistrationEmailsForNewCustomers() {
		return enableRegistrationEmailsForNewCustomers;
	}

	/**
	 * @param enableRegistrationEmailsForNewCustomers
	 *            the enableRegistrationEmailsForNewCustomers to set
	 */
	public void setEnableRegistrationEmailsForNewCustomers(
			 Boolean enableRegistrationEmailsForNewCustomers) {
		this.enableRegistrationEmailsForNewCustomers = enableRegistrationEmailsForNewCustomers;
	}

	/**
	 * @return the enableEnrollmentEmailsForNewCustomers
	 */
	public  Boolean isEnableEnrollmentEmailsForNewCustomers() {
		return enableEnrollmentEmailsForNewCustomers;
	}

	/**
	 * @param enableEnrollmentEmailsForNewCustomers
	 *            the enableEnrollmentEmailsForNewCustomers to set
	 */
	public void setEnableEnrollmentEmailsForNewCustomers(
			 Boolean enableEnrollmentEmailsForNewCustomers) {
		this.enableEnrollmentEmailsForNewCustomers = enableEnrollmentEmailsForNewCustomers;
	}

	/**
	 * @return the enableSelfEnrollmentEmailsForNewCustomers
	 */
	public  Boolean isEnableSelfEnrollmentEmailsForNewCustomers() {
		return enableSelfEnrollmentEmailsForNewCustomers;
	}

	/**
	 * @param enableSelfEnrollmentEmailsForNewCustomers
	 *            the enableSelfEnrollmentEmailsForNewCustomers to set
	 */
	public void setEnableSelfEnrollmentEmailsForNewCustomers(
			 Boolean enableSelfEnrollmentEmailsForNewCustomers) {
		this.enableSelfEnrollmentEmailsForNewCustomers = enableSelfEnrollmentEmailsForNewCustomers;
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
	 * @return the isAudioLocked
	 */
	public  Boolean isAudioLocked() {
		return isAudioLocked;
	}

	/**
	 * @param isAudioLocked
	 *            the isAudioLocked to set
	 */
	public void setAudioLocked(Boolean isAudioLocked) {
		this.isAudioLocked = isAudioLocked;
	}

	/**
	 * @return the isVolumeLocked
	 */
	public  Boolean isVolumeLocked() {
		return isVolumeLocked;
	}

	/**
	 * @param isVolumeLocked
	 *            the isVolumeLocked to set
	 */
	public void setVolumeLocked(Boolean isVolumeLocked) {
		this.isVolumeLocked = isVolumeLocked;
	}

	/**
	 * @return the isCaptioningLocked
	 */
	public  Boolean isCaptioningLocked() {
		return isCaptioningLocked;
	}

	/**
	 * @param isCaptioningLocked
	 *            the isCaptioningLocked to set
	 */
	public void setCaptioningLocked(Boolean isCaptioningLocked) {
		this.isCaptioningLocked = isCaptioningLocked;
	}

	/**
	 * @return the isBandwidthLocked
	 */
	public  Boolean isBandwidthLocked() {
		return isBandwidthLocked;
	}

	/**
	 * @param isBandwidthLocked
	 *            the isBandwidthLocked to set
	 */
	public void setBandwidthLocked(Boolean isBandwidthLocked) {
		this.isBandwidthLocked = isBandwidthLocked;
	}

	/**
	 * @return the isVideoLocked
	 */
	public  Boolean isVideoLocked() {
		return isVideoLocked;
	}

	/**
	 * @param isVideoLocked
	 *            the isVideoLocked to set
	 */
	public void setVideoLocked(Boolean isVideoLocked) {
		this.isVideoLocked = isVideoLocked;
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
	public  Boolean isRegistrationEmailLocked() {
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
	public  Boolean isEnrollmentEmailLocked() {
		return isEnrollmentEmailLocked;
	}

	/**
	 * @param isEnrollmentEmailLocked the isEnrollmentEmailLocked to set
	 */
	public void setEnrollmentEmailLocked(Boolean isEnrollmentEmailLocked) {
		this.isEnrollmentEmailLocked = isEnrollmentEmailLocked;
	}

	public void setCourseCompletionCertificateEmailEnabled(Boolean isCourseCompletionCertificateEmailEnabled) {
		if(isCourseCompletionCertificateEmailEnabled==null){
			this.isCourseCompletionCertificateEmailEnabled=Boolean.TRUE;
		}else{
			this.isCourseCompletionCertificateEmailEnabled = isCourseCompletionCertificateEmailEnabled;
		}
	}

	public  Boolean isCourseCompletionCertificateEmailEnabled() {
		if(isCourseCompletionCertificateEmailEnabled==null){
			isCourseCompletionCertificateEmailEnabled=Boolean.TRUE;
		}
		return isCourseCompletionCertificateEmailEnabled;
	}

	/**
	 * LMS-8318
	 * @param isCourseCompletionCertificateEmailLocked the isCourseCompletionCertificateEmailLocked to set
	 */
	public void setCourseCompletionCertificateEmailLocked(Boolean isCourseCompletionCertificateEmailLocked) {
		if(isCourseCompletionCertificateEmailLocked==null){
			this.isCourseCompletionCertificateEmailLocked=Boolean.FALSE;
		}else{
			this.isCourseCompletionCertificateEmailLocked = isCourseCompletionCertificateEmailLocked;
		}
	}

	/**
	 * LMS-8318
	 * @return the isCourseCompletionCertificateEmailLocked
	 */
	public  Boolean isCourseCompletionCertificateEmailLocked() {
		if(isCourseCompletionCertificateEmailLocked==null){
			isCourseCompletionCertificateEmailLocked=Boolean.FALSE;
		}
		return isCourseCompletionCertificateEmailLocked;
	}
}