package com.softech.vu360.lms.model;

/**
 * @author jason
 * 
 */
public class VU360ReportParameter implements SearchableKey {
	
	private String  paramName = null;
	private String  paramValue = null;
	
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	
}
