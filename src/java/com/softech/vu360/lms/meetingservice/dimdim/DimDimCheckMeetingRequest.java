package com.softech.vu360.lms.meetingservice.dimdim;

import net.sf.json.JSONObject;

public class DimDimCheckMeetingRequest extends DimDimRequest {

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	private String groupName;
	private String roomName;
	private String enterpriseName;
	private String clientId;
	
	public DimDimCheckMeetingRequest(){
		setURL(DimDimConfiguration.getInstance().getCheckMeetingURL());
	}
	
	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject request = new JSONObject();
		//setParameter(request,"clientId",getClientId());
		setParameter(request, "accountName", getUserName());
		setParameter(request, "enterpriseName", getEnterpriseName());
		//setParameter(request,"groupName",getGroupName()==null?super.getDefaultGroupName():getGroupName());
		setParameter(request,"roomName",getRoomName());
		return request;
	}

}
