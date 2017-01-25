package com.softech.vu360.lms.model;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class EnrollmentSubscriptionView implements ILMSBaseInterface{

	private long enrollmentId;
	private long entitlementId;
	private long totalSeats;
	private Integer seatsUsed = null;
	private Integer seatsRemaining = null;
	private Integer maxseats = null;
	
	//private long seatsRemaining;
	private String entitlementName = null;
	private String subscriptionName = null;
	private String subscriptionCode = null;
	private  Boolean selected = false;
	private Subscription subscription=null;
	
	
	public long getEnrollmentId() {
		return enrollmentId;
	}
	public void setEnrollmentId(long enrollmentId) {
		this.enrollmentId = enrollmentId;
	}
	public long getEntitlementId() {
		return entitlementId;
	}
	public void setEntitlementId(long entitlementId) {
		this.entitlementId = entitlementId;
	}
	public long getTotalSeats() {
		return totalSeats;
	}
	public void setTotalSeats(long totalSeats) {
		this.totalSeats = totalSeats;
	}
	public Integer getSeatsUsed() {
		return seatsUsed;
	}
	public void setSeatsUsed(Integer seatsUsed) {
		this.seatsUsed = seatsUsed;
	}
	/*
	public long getSeatsRemaining() {
		return seatsRemaining;
	}
	public void setSeatsRemaining(long seatsRemaining) {
		this.seatsRemaining = seatsRemaining;
	}
	*/
	public String getEntitlementName() {
		return entitlementName;
	}
	public void setEntitlementName(String entitlementName) {
		this.entitlementName = entitlementName;
	}
	public String getSubscriptionName() {
		return subscriptionName;
	}
	public void setSubscriptionName(String subscriptionName) {
		this.subscriptionName = subscriptionName;
	}
	public String getSubscriptionCode() {
		return subscriptionCode;
	}
	public void setSubscriptionCode(String subscriptionCode) {
		this.subscriptionCode = subscriptionCode;
	}
	public  Boolean isSelected() {
		return selected;
	}
	public boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	public Subscription getSubscription() {
		return subscription;
	}
	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}
	public Integer getSeatsRemaining() {
		return seatsRemaining;
	}
	public void setSeatsRemaining(Integer seatsRemaining) {
		this.seatsRemaining = seatsRemaining;
	}
	public Integer getMaxseats() {
		return maxseats;
	}
	public void setMaxseats(Integer maxseats) {
		this.maxseats = maxseats;
	}
	
	
	

}
