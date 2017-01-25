package com.softech.vu360.lms.meetingservice.dimdim;

import net.sf.json.JSONObject;

public class DimDimCheckMeetingResponse extends DimDimResponse {

	public String getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}

	public String getMeetingName() {
		return meetingName;
	}

	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}

	public String getAgenda() {
		return agenda;
	}

	public void setAgenda(String agenda) {
		this.agenda = agenda;
	}

	public Boolean getEmailRequired() {
		return emailRequired;
	}

	public void setEmailRequired(Boolean emailRequired) {
		this.emailRequired = emailRequired;
	}

	public Boolean getEmailValidationRequired() {
		return emailValidationRequired;
	}

	public void setEmailValidationRequired(Boolean emailValidationRequired) {
		this.emailValidationRequired = emailValidationRequired;
	}

	public Boolean getAttendeeKeyRequired() {
		return attendeeKeyRequired;
	}

	public void setAttendeeKeyRequired(Boolean attendeeKeyRequired) {
		this.attendeeKeyRequired = attendeeKeyRequired;
	}

	public Boolean getLobbyEnabled() {
		return lobbyEnabled;
	}

	public void setLobbyEnabled(Boolean lobbyEnabled) {
		this.lobbyEnabled = lobbyEnabled;
	}

	public Boolean getMeetingExists() {
		return meetingExists;
	}

	public void setMeetingExists(Boolean meetingExists) {
		this.meetingExists = meetingExists;
	}

	private String meetingId;
	private String meetingName;
	private String agenda;
	private Boolean emailRequired;
	private Boolean emailValidationRequired;
	private Boolean attendeeKeyRequired;
	private Boolean lobbyEnabled;
	private Boolean meetingExists;
	
	@Override
	public void parse(JSONObject responseJSON) {
		JSONObject response = responseJSON.getJSONObject("response");
		this.meetingExists = Boolean.valueOf(response.getString("meetingExists"));
		if(meetingExists.booleanValue()){
			this.meetingId = response.getString("meetingId");
			this.meetingName = response.getString("meetingName");
			this.agenda = response.getString("agenda");
			this.attendeeKeyRequired = Boolean.valueOf(response.getString("attendeeKeyRequired"));
			this.emailRequired = Boolean.valueOf(response.getString("emailRequired"));
			this.emailValidationRequired = Boolean.valueOf(response.getString("emailValidationRequired"));
			this.lobbyEnabled = Boolean.valueOf(response.getString("lobbyEnabled"));
		}
	}

}
