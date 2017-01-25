package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.AlertRecipient;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class ManageRecepientsGroupForm  implements ILMSBaseInterface{
List <AlertRecipient> alertRecipients = new ArrayList<AlertRecipient>();
private static final long serialVersionUID = 1L;
public ManageRecepientsGroupForm(){
	AlertRecipient alertRecipient = new AlertRecipient();
	alertRecipient.setAlertRecipientGroupName("aa");
	alertRecipients.add(alertRecipient);
	
	AlertRecipient alertRecipient2 = new AlertRecipient();
	alertRecipient.setAlertRecipientGroupName("aa2");
	alertRecipients.add(alertRecipient2);
}

public List<AlertRecipient> getAlertRecipients() {
	return alertRecipients;
}

public void setAlertRecipients(List<AlertRecipient> alertRecipients) {
	this.alertRecipients = alertRecipients;
}



}
