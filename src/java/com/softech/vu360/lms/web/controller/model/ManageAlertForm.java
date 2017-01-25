package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Alert;
import com.softech.vu360.lms.model.AlertRecipient;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;



public class ManageAlertForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private boolean defaultMessage=false;
	private String alertName = "";
    private AlertRecipient recipient=new AlertRecipient();
   

	public AlertRecipient getRecipient() {
		return recipient;
	}

	public void setRecipient(AlertRecipient recipient) {
		this.recipient = recipient;
	}

	public List<AlertRecipient> getRecips() {
		return recips;
	}

	public void setRecips(List<AlertRecipient> recips) {
		this.recips = recips;
	}

	private List<AlertRecipient> recips = new ArrayList<AlertRecipient>();

	public String getAlertName() {
		return alertName;
	}

	public void setAlertName(String alertName) {

		this.alertName = alertName;
	}

	private List<Alert> alerts = new ArrayList<Alert>();

	private Alert alert = new Alert();
    private List<MngAlert> mngAlerts = new ArrayList<MngAlert>();
    public List<Alert> getAlerts() {

		return alerts;
	}

	public void setAlerts(List<Alert> alerts) {

		this.alerts = alerts;
	}

	public Alert getAlert() {

		return alert;
	}

	public void setAlert(Alert alert) {

		this.alert = alert;
	}

   public List<MngAlert> getMngAlerts() {

		return mngAlerts;
	}

	public void setMngAlerts(List<MngAlert> mngAlerts) {

		this.mngAlerts = mngAlerts;
	}

	public boolean isDefaultMessage() {
		return defaultMessage;
	}

	public void setDefaultMessage(boolean defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	
	
}

