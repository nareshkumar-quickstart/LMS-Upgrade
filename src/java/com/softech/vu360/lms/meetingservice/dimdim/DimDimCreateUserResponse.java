package com.softech.vu360.lms.meetingservice.dimdim;

import net.sf.json.JSONObject;

public class DimDimCreateUserResponse extends DimDimResponse {

	String dimdimUniqueId;
	@Override
	public void parse(JSONObject responseJSON) {
		// TODO Auto-generated method stub
		if(!this.anyError()){
		JSONObject dataResponse = responseJSON.getJSONObject("response").getJSONObject("data");
		dimdimUniqueId = dataResponse.getString("dimdimUniqueId");
		}
	}

}
