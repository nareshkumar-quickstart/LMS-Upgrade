package com.softech.vu360.lms.meetingservice.dimdim;

import net.sf.json.JSONObject;

public class DimDimReadUserResponse extends DimDimResponse{

	private Boolean userExist;

	// this method will only be used by DimDimConnection
	public void parse(JSONObject jsonResponse) {
		if(jsonResponse.getString("result").equalsIgnoreCase("true")){
			userExist = Boolean.TRUE;
		}else{
			userExist = Boolean.FALSE;
		}
	}

	public Boolean userExists(){
		return userExist;
	}
}
