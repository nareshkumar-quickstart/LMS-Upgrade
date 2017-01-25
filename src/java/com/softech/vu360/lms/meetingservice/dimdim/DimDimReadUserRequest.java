package com.softech.vu360.lms.meetingservice.dimdim;

import net.sf.json.JSONObject;

public class DimDimReadUserRequest extends DimDimRequest {


	//{"dimdimEnterpriseName":"vcs1","object":"account","dimdimGroupName":"all","sessionKey":"1cb99364-352f-11e0-bdbd-842b2b4f9719"}
	public DimDimReadUserRequest(){
		setURL(DimDimConfiguration.getInstance().getReadUserURL());
	}

	private String enterpriseName;
	
	@Override
	public JSONObject toJSON() {
		JSONObject request = new JSONObject();
		setParameter(request,"object", "account");
		setParameter(request,"dimdimUserName", this.userName);
		setParameter(request,"dimdimGroupName", super.getDefaultGroupName());
		setParameter(request, "dimdimEnterpriseName", enterpriseName==null?DimDimConfiguration.getInstance().getEnterpriseName():enterpriseName);
		JSONObject obj = new JSONObject();
		obj.put("request",request);
		
		//setParameter(request,"sessionKey", this.getAuthToken());
		return obj;
	}

	/**
	 * @return the enterpriseName
	 */
	public String getEnterpriseName() {
		return enterpriseName;
	}

	/**
	 * @param enterpriseName the enterpriseName to set
	 */
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
}
