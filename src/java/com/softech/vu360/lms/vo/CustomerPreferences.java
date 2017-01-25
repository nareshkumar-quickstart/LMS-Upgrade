package com.softech.vu360.lms.vo;

import java.io.Serializable;

public class CustomerPreferences implements Serializable {

	private static final long serialVersionUID = 1L;

	private Boolean enableRegistrationEmailsForNewCustomers = Boolean.FALSE;
	private Boolean enableEnrollmentEmailsForNewCustomers = Boolean.FALSE;
	private Boolean enableSelfEnrollmentEmailsForNewCustomers = Boolean.FALSE;
	private Long id;
	private Boolean isAudioEnabled = Boolean.FALSE;
	private Boolean isBlankSearchEnabled = Boolean.FALSE;
	private Boolean isCaptioningEnabled = Boolean.FALSE;
	private String bandwidth = BANDWIDTH_HIGH;
	private int volume = 0;
	private Boolean isVedioEnabled = Boolean.FALSE;
	private Boolean isAudioLocked = Boolean.FALSE;
	private Boolean isVolumeLocked = Boolean.FALSE;
	private Boolean isCaptioningLocked = Boolean.FALSE;
	private Boolean isBandwidthLocked = Boolean.FALSE;
	private Boolean isVideoLocked = Boolean.FALSE;
	private Boolean isRegistrationEmailLocked = Boolean.FALSE;
	private Boolean isEnrollmentEmailLocked = Boolean.FALSE;
	private Boolean isCourseCompletionCertificateEmailEnabled = Boolean.TRUE;
	private Boolean isCourseCompletionCertificateEmailLocked = Boolean.FALSE;

	public static final String BANDWIDTH_HIGH = "high";
	public static final String BANDWIDTH_LOW = "low";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean isAudioEnabled() {
		return isAudioEnabled;
	}

	public void setAudioEnabled(Boolean isAudioEnabled) {
		this.isAudioEnabled = isAudioEnabled;
	}

	public Boolean isBlankSearchEnabled() {
		return isBlankSearchEnabled;
	}

	public void setBlankSearchEnabled(Boolean isBlankSearchEnabled) {
		this.isBlankSearchEnabled = isBlankSearchEnabled;
	}

	public Boolean isCaptioningEnabled() {
		return isCaptioningEnabled;
	}

	public void setCaptioningEnabled(Boolean isCaptioningEnabled) {
		this.isCaptioningEnabled = isCaptioningEnabled;
	}

	public String getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	public Boolean isVedioEnabled() {
		return isVedioEnabled;
	}

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
		return enableRegistrationEmailsForNewCustomers;
	}

	public void setEnableRegistrationEmailsForNewCustomers(Boolean enableRegistrationEmailsForNewCustomers) {
		this.enableRegistrationEmailsForNewCustomers = enableRegistrationEmailsForNewCustomers;
	}

	public Boolean isEnableEnrollmentEmailsForNewCustomers() {
		return enableEnrollmentEmailsForNewCustomers;
	}

	public void setEnableEnrollmentEmailsForNewCustomers(Boolean enableEnrollmentEmailsForNewCustomers) {
		this.enableEnrollmentEmailsForNewCustomers = enableEnrollmentEmailsForNewCustomers;
	}

	public Boolean isEnableSelfEnrollmentEmailsForNewCustomers() {
		return enableSelfEnrollmentEmailsForNewCustomers;
	}

	public void setEnableSelfEnrollmentEmailsForNewCustomers(Boolean enableSelfEnrollmentEmailsForNewCustomers) {
		this.enableSelfEnrollmentEmailsForNewCustomers = enableSelfEnrollmentEmailsForNewCustomers;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public Boolean isRegistrationEmailLocked() {
		return isRegistrationEmailLocked;
	}

	public void setRegistrationEmailLocked(Boolean isRegistrationEmailLocked) {
		this.isRegistrationEmailLocked = isRegistrationEmailLocked;
	}

	public Boolean isEnrollmentEmailLocked() {
		return isEnrollmentEmailLocked;
	}

	public void setEnrollmentEmailLocked(Boolean isEnrollmentEmailLocked) {
		this.isEnrollmentEmailLocked = isEnrollmentEmailLocked;
	}

	public void setCourseCompletionCertificateEmailEnabled(Boolean isCourseCompletionCertificateEmailEnabled) {
		this.isCourseCompletionCertificateEmailEnabled = isCourseCompletionCertificateEmailEnabled;
	}

	public Boolean isCourseCompletionCertificateEmailEnabled() {
		return isCourseCompletionCertificateEmailEnabled;
	}

	public void setCourseCompletionCertificateEmailLocked(Boolean isCourseCompletionCertificateEmailLocked) {
		this.isCourseCompletionCertificateEmailLocked = isCourseCompletionCertificateEmailLocked;
	}

	public Boolean isCourseCompletionCertificateEmailLocked() {
		return isCourseCompletionCertificateEmailLocked;
	}

}