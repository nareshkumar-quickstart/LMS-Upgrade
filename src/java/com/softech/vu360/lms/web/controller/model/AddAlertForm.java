package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.model.Alert;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class AddAlertForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	public AddAlertForm(){
	
	}
	private Alert alert = new Alert();
	private String fromName="";
	private String alertSubject="";
	private String alertMessageBody = "";
	private String isDefaultMessage = "true";
	
	private String message="";
		
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Alert getAlert() {
		return alert;
	}
	public void setAlert(Alert alert) {
		this.alert = alert;
	}
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	public String getAlertSubject() {
		return alertSubject;
	}
	public void setAlertSubject(String alertSubject) {
		this.alertSubject = alertSubject;
	}
	public String getAlertMessageBody() {
		return alertMessageBody;
	}
	public void setAlertMessageBody(String alertMessageBody) {
		this.alertMessageBody = alertMessageBody;
	}
	public String getIsDefaultMessage() {
		return isDefaultMessage;
	}
	public void setIsDefaultMessage(String isDefaultMessage) {
		this.isDefaultMessage = isDefaultMessage;
	}
}
