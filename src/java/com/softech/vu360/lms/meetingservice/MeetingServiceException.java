package com.softech.vu360.lms.meetingservice;

public class MeetingServiceException extends Exception{

	private String errorCode=null;
	private String reason=null;
	private String message=null;
	public MeetingServiceException(String message){
		this.message=message;
	}
	@Override
	public String getMessage(){
		return errorCode+":"+message;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
