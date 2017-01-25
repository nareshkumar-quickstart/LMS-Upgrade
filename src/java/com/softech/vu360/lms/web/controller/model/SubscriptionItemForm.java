package com.softech.vu360.lms.web.controller.model;
import com.softech.vu360.lms.model.Subscription;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class SubscriptionItemForm  implements ILMSBaseInterface {
	
	private static final long serialVersionUID = 1L;
	private boolean isSelected = false;
	
	private Subscription subscription;
	private boolean seatavailable = false;
	
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public Subscription getSubscription() {
		return subscription;
	}
	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}
	public boolean isSeatavailable() {
		return seatavailable;
	}
	public void setSeatavailable(boolean seatavailable) {
		this.seatavailable = seatavailable;
	}
	
	

}
