package com.softech.vu360.lms.meetingservice.globalscholar;

import com.softech.vu360.lms.meetingservice.SynchronousMeetingService;
import com.softech.vu360.util.Brander;

public class GlobalScholarMeetingServiceImpl implements SynchronousMeetingService {

	
	@Override
	public String getJoinMeetingURL(Brander brand,String meetingNumber,String attendeeName,String attendeeEmail,String meetingPassword){
		// TODO Implementation of Global Scholar for joining meeting will go here..
		return "http://www.globalscholar.com";
	}

}
