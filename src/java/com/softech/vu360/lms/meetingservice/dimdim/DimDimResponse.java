package com.softech.vu360.lms.meetingservice.dimdim;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

/**
 * @author Faisal Ahmed Siddiqui
 */

public abstract class DimDimResponse {
	private String result;
	private JSONObject jsonResponse;
	protected Logger log = Logger.getLogger(this.getClass());
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public JSONObject getJsonResponse() {
		return jsonResponse;
	}
	public void setJsonResponse(JSONObject jsonResponse) {
		this.jsonResponse = jsonResponse;
	}

	public abstract void parse(JSONObject responseJSON);
	public boolean anyError(){
		boolean error = true;
		if (result!=null && result.equalsIgnoreCase("true")){
			error = false;
		}
		return error;
	}
}
