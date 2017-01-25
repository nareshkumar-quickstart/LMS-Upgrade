package com.softech.vu360.lms.meetingservice.dimdim;

import net.sf.json.JSONObject;

public abstract class DimDimRequest {
	String URL ;
	String authToken;
	String userName;
	String password;
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public String getAuthToken() {
		return authToken;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setParameter(JSONObject request, String key,Object value){
		if(value!=null){
			request.put(key, value);
		}
	}
	private static final String DEFAULT_GROUP_NAME="all";
	public String getDefaultGroupName(){
		return DEFAULT_GROUP_NAME;
	}
	// this method will be overriden by child classes to construct exact json for request
	public abstract JSONObject toJSON();
}
