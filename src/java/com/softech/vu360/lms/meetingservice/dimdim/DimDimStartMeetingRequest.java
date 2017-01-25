package com.softech.vu360.lms.meetingservice.dimdim;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

public class DimDimStartMeetingRequest extends DimDimRequest {

	Logger log = Logger.getLogger(DimDimStartMeetingRequest.class);
	public DimDimStartMeetingRequest(){
		this.setURL(DimDimConfiguration.getInstance().getStartMeetingURL());
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	private String clientId;
	private String groupName;
	private String enterpriseName;
	private String roomName;
	private String agenda;
	private String meetingName;
	private String displayName;
	private Boolean autoAssignMikeOnJoin;
	private Boolean autoHandsFreeOnAVLoad;
	private Boolean assistentEnabled;
	private Boolean privateChatEnabled;
	private Boolean publicChatEnabled;
	private Boolean lobbyEnabled;
	private Boolean screenShareEnabled;
	private Boolean whiteboardEnabled;
	private Boolean cobrowserEnabled;
	private Boolean documentSharingEnabled;
	private Boolean participantListEnabled;
	private Boolean recordingEnabled;
	private Integer maxParticipants;
	private Integer meetingLengthMinutes;
	private String internationalTollNumber;
	private String moderatorPhonePassCode;
	private String attendeePhonePassCode;
	private String attendees;// probably can email all students has active enrollment on this course 
	private String attendeeKey;
	private String hostReturnURL;
	public String getHostReturnURL() {
		return hostReturnURL;
	}
	public void setHostReturnURL(String hostReturnURL) {
		this.hostReturnURL = hostReturnURL;
	}
	@Override
	public JSONObject toJSON() {
		JSONObject request = new JSONObject();
		//request.put("clientId", "faisal.ahmed");optional
		super.setParameter(request,"enterpriseName", this.getEnterpriseName());
		super.setParameter(request,"groupName", this.getGroupName());
		super.setParameter(request,"account", super.getUserName());//required
		super.setParameter(request,"roomName", this.getRoomName());
		super.setParameter(request,"agenda", this.getAgenda());
		super.setParameter(request,"meetingName", this.getMeetingName());
		super.setParameter(request,"displayName", this.getDisplayName());
		//super.setParameter(request,"audioVideo", "vcs1"); // 
		super.setParameter(request,"autoAssignMikeOnJoin", this.isAutoAssignMikeOnJoin()); //optional Provides control to let you assign the microphone to the attendee automatically on joining the meeting Default is set to false
		super.setParameter(request,"autoHandsFreeOnAVLoad", this.isAutoHandsFreeOnAVLoad()); //optional Enables the Hands-Free option on loading of the audio video broadcaster in the meeting Default is set to false
		super.setParameter(request,"assistentEnabled", this.isAssistentEnabled());//optional  Enables the Meeting Assistant to be displayed at the start of the meeting Default is set to true
		super.setParameter(request,"privateChatEnabled", this.isPrivateChatEnabled());
		super.setParameter(request,"publicChatEnabled", this.isPublicChatEnabled()); 
		super.setParameter(request,"lobbyEnabled", this.isLobbyEnabled());//optional Enables the waiting area before the start of the meeting
		super.setParameter(request,"screenShareEnabled", this.isScreenShareEnabled());
		super.setParameter(request,"whiteboardEnabled", this.isWhiteboardEnabled());
		super.setParameter(request,"cobrowserEnabled", this.isCobrowserEnabled());//optional This is used to enable/disable co-browsing feature in the meeting
		super.setParameter(request,"documentSharingEnabled", this.isDocumentSharingEnabled());//optional 
		super.setParameter(request,"participantListEnabled", this.isParticipantListEnabled());
		super.setParameter(request,"recordingEnabled", this.isRecordingEnabled()); //optional This is used to enable/disable recording feature in the meeting
		super.setParameter(request,"maxParticipants", this.getMaxParticipants());// optional should be unlimited
		super.setParameter(request,"meetingLengthMinutes", this.getMeetingLengthMinutes());//optional Defines the duration of the meeting in minutes 
		super.setParameter(request,"internationalTollNumber", this.getInternationalTollNumber());//optional Defines the international dial in phone number that attendees have to call in order to connect to a conference call
		super.setParameter(request,"moderatorPhonePassCode", this.getModeratorPhonePassCode()); //optional Defines the pass code that the host or the moderator has to enter while setting up a conference call
		super.setParameter(request,"attendeePhonePassCode", this.getAttendeePhonePassCode());//optional Defines the pass code that an attendee has to enter in order to join the conference call
		super.setParameter(request,"attendees", this.getAttendees()); // optional
		super.setParameter(request,"attendeeKey", this.getAttendeeKey()); // optinoal meeting pass code from schedule
		super.setParameter(request, "hostReturnUrl", hostReturnURL);
		log.info(request.toString());
		return request;

	}
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getAgenda() {
		return agenda;
	}
	public void setAgenda(String agenda) {
		this.agenda = agenda;
	}
	public String getMeetingName() {
		return meetingName;
	}
	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public Boolean isAutoAssignMikeOnJoin() {
		return autoAssignMikeOnJoin;
	}
	public void setAutoAssignMikeOnJoin(Boolean autoAssignMikeOnJoin) {
		this.autoAssignMikeOnJoin = autoAssignMikeOnJoin;
	}
	public Boolean isAutoHandsFreeOnAVLoad() {
		return autoHandsFreeOnAVLoad;
	}
	public void setAutoHandsFreeOnAVLoad(Boolean autoHandsFreeOnAVLoad) {
		this.autoHandsFreeOnAVLoad = autoHandsFreeOnAVLoad;
	}
	public Boolean isAssistentEnabled() {
		return assistentEnabled;
	}
	public void setAssistentEnabled(Boolean assistentEnabled) {
		this.assistentEnabled = assistentEnabled;
	}
	public Boolean isPrivateChatEnabled() {
		return privateChatEnabled;
	}
	public void setPrivateChatEnabled(Boolean privateChatEnabled) {
		this.privateChatEnabled = privateChatEnabled;
	}
	public Boolean isPublicChatEnabled() {
		return publicChatEnabled;
	}
	public void setPublicChatEnabled(Boolean publicChatEnabled) {
		this.publicChatEnabled = publicChatEnabled;
	}
	public Boolean isLobbyEnabled() {
		return lobbyEnabled;
	}
	public void setLobbyEnabled(Boolean lobbyEnabled) {
		this.lobbyEnabled = lobbyEnabled;
	}
	public Boolean isScreenShareEnabled() {
		return screenShareEnabled;
	}
	public void setScreenShareEnabled(Boolean screenShareEnabled) {
		this.screenShareEnabled = screenShareEnabled;
	}
	public Boolean isWhiteboardEnabled() {
		return whiteboardEnabled;
	}
	public void setWhiteboardEnabled(Boolean whiteboardEnabled) {
		this.whiteboardEnabled = whiteboardEnabled;
	}
	public Boolean isCobrowserEnabled() {
		return cobrowserEnabled;
	}
	public void setCobrowserEnabled(Boolean cobrowserEnabled) {
		this.cobrowserEnabled = cobrowserEnabled;
	}
	public Boolean isDocumentSharingEnabled() {
		return documentSharingEnabled;
	}
	public void setDocumentSharingEnabled(Boolean documentSharingEnabled) {
		this.documentSharingEnabled = documentSharingEnabled;
	}
	public Boolean isParticipantListEnabled() {
		return participantListEnabled;
	}
	public void setParticipantListEnabled(Boolean participantListEnabled) {
		this.participantListEnabled = participantListEnabled;
	}
	public Boolean isRecordingEnabled() {
		return recordingEnabled;
	}
	public void setRecordingEnabled(Boolean recordingEnabled) {
		this.recordingEnabled = recordingEnabled;
	}
	public Integer getMaxParticipants() {
		return maxParticipants;
	}
	public void setMaxParticipants(Integer maxParticipants) {
		this.maxParticipants = maxParticipants;
	}
	public Integer getMeetingLengthMinutes() {
		return meetingLengthMinutes;
	}
	public void setMeetingLengthMinutes(Integer meetingLengthMinutes) {
		this.meetingLengthMinutes = meetingLengthMinutes;
	}
	public String getInternationalTollNumber() {
		return internationalTollNumber;
	}
	public void setInternationalTollNumber(String internationalTollNumber) {
		this.internationalTollNumber = internationalTollNumber;
	}
	public String getModeratorPhonePassCode() {
		return moderatorPhonePassCode;
	}
	public void setModeratorPhonePassCode(String moderatorPhonePassCode) {
		this.moderatorPhonePassCode = moderatorPhonePassCode;
	}
	public String getAttendeePhonePassCode() {
		return attendeePhonePassCode;
	}
	public void setAttendeePhonePassCode(String attendeePhonePassCode) {
		this.attendeePhonePassCode = attendeePhonePassCode;
	}
	public String getAttendees() {
		return attendees;
	}
	public void setAttendees(String attendees) {
		this.attendees = attendees;
	}
	public String getAttendeeKey() {
		return attendeeKey;
	}
	public void setAttendeeKey(String attendeeKey) {
		this.attendeeKey = attendeeKey;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

}
