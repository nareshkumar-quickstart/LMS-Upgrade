package com.softech.vu360.lms.meetingservice.dimdim;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

public class DimDimLoginResponse extends DimDimResponse{

	private static final Logger log = Logger.getLogger(DimDimLoginResponse.class);
	private String authToken;
	public String getAuthToken() {
		return authToken;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	// this method will only be used by DimDimConnection
	@Override
	public void parse(JSONObject jsonResponse) {
		log.info(jsonResponse.toString());
		log.info("result:" + jsonResponse.getString("result"));
		setResult(jsonResponse.getString("result"));
		JSONObject reponseAuth = jsonResponse.getJSONObject("response");
		authToken = reponseAuth.getString("authToken");
		log.info("authToken:" + reponseAuth.getString("authToken"));
	}
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("result:"+this.getResult());
		builder.append(",authToken:"+this.getAuthToken());
		return builder.toString();
	}
}
