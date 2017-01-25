package com.softech.vu360.lms.meetingservice.dimdim;

import net.sf.json.JSONObject;

public class DimDimLoginRequest extends DimDimRequest {

	public DimDimLoginRequest(boolean admin){
		this.setURL(DimDimConfiguration.getInstance().getAdminLoginURL());
	}
	public DimDimLoginRequest(){
		this.setURL(DimDimConfiguration.getInstance().getLoginURL());
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	private String groupName;
	
	@Override
	public JSONObject toJSON() {
		JSONObject requestPara = new JSONObject();
		JSONObject request = new JSONObject();
		//obj1.put("request", obj2);
		super.setParameter(requestPara, "account", userName);
		super.setParameter(requestPara, "password", password);
		super.setParameter(requestPara, "groupName", this.getGroupName()==null?super.getDefaultGroupName():this.getGroupName());
		super.setParameter(request, "request", requestPara);
		return request;
	}
}
