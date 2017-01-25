package com.softech.vu360.lms.meetingservice.webex;

import org.apache.commons.lang.StringUtils;

import com.softech.vu360.lms.meetingservice.MeetingServiceException;
import com.softech.vu360.lms.meetingservice.SynchronousMeetingService;
import com.softech.vu360.util.Brander;

public class WebExMeetingServiceImpl implements SynchronousMeetingService {

	
	@Override
	public String getJoinMeetingURL(Brander brand,String meetingNumber,String attendeeName,String attendeeEmail,String meetingPassword)throws MeetingServiceException{
		
		StringBuilder joinMeetingURL=new StringBuilder(brand.getBrandElement("lms.learner.class.joinmeeting.url").trim());
		
		String joinMeetingTemplate = brand.getBrandElement("lms.webex.joinmeeting.command.template").trim();//"AT=JM&MK=[?]&PW=[?]&AN=[?]&AE=[?]&RID=[?]";
		joinMeetingTemplate = StringUtils.replaceOnce(joinMeetingTemplate, "?", meetingNumber);
		joinMeetingTemplate = StringUtils.replaceOnce(joinMeetingTemplate, "?", meetingPassword);
		joinMeetingTemplate = StringUtils.replaceOnce(joinMeetingTemplate, "?", attendeeName);
		joinMeetingTemplate = StringUtils.replaceOnce(joinMeetingTemplate, "?", attendeeEmail);
		joinMeetingTemplate = StringUtils.replaceOnce(joinMeetingTemplate, "?", attendeeEmail);
		joinMeetingURL.append(joinMeetingTemplate);
		return joinMeetingURL.toString();
	}

}
