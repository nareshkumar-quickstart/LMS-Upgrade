package com.softech.vu360.lms.meetingservice.webex;

public class JoinMeetingURLRequest implements WebExRequest
{

	private String meetingNumber;
	private String attendeeName;
	private String attendeeEmail;
	private String meetingPassword;
	
	public String getMeetingNumber() {
		return meetingNumber;
	}
	
	public void setMeetingNumber(String meetingNumber) {
		this.meetingNumber = meetingNumber;
	}
	public String serialize()
	{
        StringBuilder reqXML = new StringBuilder();
        reqXML.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
        reqXML.append("<serv:message xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        reqXML.append(" xmlns:serv=\"http://www.webex.com/schemas/2002/06/service\"");
        reqXML.append(" xsi:schemaLocation=\"http://www.webex.com/schemas/2002/06/service\">\r\n");
        reqXML.append("<header>\r\n");
        reqXML.append("<securityContext>\r\n");
        reqXML.append("<webExID>"); reqXML.append(WebExProperties.getId()); reqXML.append("</webExID>\r\n");
        reqXML.append("<password>"); reqXML.append(WebExProperties.getPassword()); reqXML.append("</password>\r\n");
        reqXML.append("<siteID>"); reqXML.append(WebExProperties.getSideId()); reqXML.append("</siteID>\r\n");
        reqXML.append("<partnerID>"); reqXML.append(WebExProperties.getPartnerId()); reqXML.append("</partnerID>\r\n");
        reqXML.append("<email>"); reqXML.append(WebExProperties.getEmail()); reqXML.append("</email>\r\n");
        reqXML.append("</securityContext>\r\n");
        reqXML.append("</header>\r\n");
        reqXML.append("<body>\r\n");
//      reqXML.append("<bodyContent xsi:type=\"java:com.webex.service.binding.meeting.LstsummaryMeeting\">");
        reqXML.append("<bodyContent xsi:type=\"java:com.webex.service.binding.meeting.GetjoinurlMeeting\">");
        reqXML.append("<sessionKey>");reqXML.append(meetingNumber); reqXML.append("</sessionKey>"); 
        reqXML.append("<attendeeName>");reqXML.append(attendeeName);reqXML.append("</attendeeName>");
        reqXML.append("<attendeeEmail>");reqXML.append(attendeeEmail);reqXML.append("</attendeeEmail>");
        reqXML.append("<meetingPW>");reqXML.append(meetingPassword);reqXML.append("</meetingPW>"); 
        reqXML.append("<RegID>");reqXML.append(attendeeEmail);reqXML.append("</RegID>");
        reqXML.append("</bodyContent>\r\n");
        reqXML.append("</body>\r\n");
        reqXML.append("</serv:message>\r\n");
        return reqXML.toString();
	}

	public String getAttendeeName() {
		return attendeeName;
	}

	public void setAttendeeName(String attendeeName) {
		this.attendeeName = attendeeName;
	}

	public String getAttendeeEmail() {
		return attendeeEmail;
	}

	public void setAttendeeEmail(String attendeeEmail) {
		this.attendeeEmail = attendeeEmail;
	}

	public String getMeetingPassword() {
		return meetingPassword;
	}

	public void setMeetingPassword(String meetingPassword) {
		this.meetingPassword = meetingPassword;
	}
}
