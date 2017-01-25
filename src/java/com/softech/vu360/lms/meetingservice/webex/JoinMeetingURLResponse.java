package com.softech.vu360.lms.meetingservice.webex;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JoinMeetingURLResponse extends WebExResponse {

	private String joinMeetingURL;
	@Override
	public void handlePostDeserialization(Document document)
	{
		NodeList list = document.getElementsByTagName("joinMeetingURL");
		Node node = list.item(0);
		this.joinMeetingURL = node.getTextContent();
	}
	public String getJoinMeetingURL() {
		return joinMeetingURL;
	}
	public void setJoinMeetingURL(String joinURL) {
		this.joinMeetingURL = joinURL;
	}
}
