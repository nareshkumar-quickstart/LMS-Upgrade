package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class InterceptorAlertForm implements ILMSBaseInterface {
	private Long id;
	private String createdDate = null;
	private String alertName = null;
	private String alertMessageBody = null;
	private static final long serialVersionUID = 1L;
	
	
	public String getAlertName() {
		return alertName;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public void setAlertName(String alertName) {
		this.alertName = alertName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAlertMessageBody() {
		return alertMessageBody;
	}
	public void setAlertMessageBody(String alertMessageBody) {
		this.alertMessageBody = alertMessageBody;
	}
	
}
