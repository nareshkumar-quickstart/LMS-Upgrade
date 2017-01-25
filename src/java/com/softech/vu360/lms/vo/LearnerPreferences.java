package com.softech.vu360.lms.vo;

import java.io.Serializable;

public class LearnerPreferences implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Boolean isAudioEnabled = Boolean.FALSE;
	private Boolean isCaptioningEnabled = Boolean.FALSE;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Boolean isAudioEnabled() {
		return isAudioEnabled;
	}
	public void setIsAudioEnabled(Boolean isAudioEnabled) {
		this.isAudioEnabled = isAudioEnabled;
	}
	public Boolean isCaptioningEnabled() {
		return isCaptioningEnabled;
	}
	public void setIsCaptioningEnabled(Boolean isCaptioningEnabled) {
		this.isCaptioningEnabled = isCaptioningEnabled;
	}
	
	

}
