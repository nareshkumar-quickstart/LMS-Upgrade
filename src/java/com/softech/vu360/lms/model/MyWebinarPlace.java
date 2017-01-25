package com.softech.vu360.lms.model;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class MyWebinarPlace implements ILMSBaseInterface {
	
	private static final long serialVersionUID = 1L;

	private String mtNumber;
	private String topic;
	private String agenda;
	private String date;
	private String time_hrs;
	private String time_mins;
	private String time_type;
	private String event_reference;
	
	public MyWebinarPlace(){
		
	}
	

	public MyWebinarPlace(String mtNumber, String topic, String agenda,	String date, String time_hrs, String time_mins, String time_type,
			String event_reference) {

		this.mtNumber = mtNumber;
		this.topic = topic;
		this.agenda = agenda;
		this.date = date;
		this.time_hrs = time_hrs;
		this.time_mins = time_mins;
		this.time_type = time_type;
		this.event_reference = event_reference;
	}


	public String getMtNumber() {
		return mtNumber;
	}


	public void setMtNumber(String mtNumber) {
		this.mtNumber = mtNumber;
	}


	public String getTopic() {
		return topic;
	}


	public void setTopic(String topic) {
		this.topic = topic;
	}


	public String getAgenda() {
		return agenda;
	}


	public void setAgenda(String agenda) {
		this.agenda = agenda;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getTime_hrs() {
		return time_hrs;
	}


	public void setTime_hrs(String time_hrs) {
		this.time_hrs = time_hrs;
	}


	public String getTime_mins() {
		return time_mins;
	}


	public void setTime_mins(String time_mins) {
		this.time_mins = time_mins;
	}


	public String getTime_type() {
		return time_type;
	}


	public void setTime_type(String time_type) {
		this.time_type = time_type;
	}


	public String getEvent_reference() {
		return event_reference;
	}


	public void setEvent_reference(String event_reference) {
		this.event_reference = event_reference;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	

}
