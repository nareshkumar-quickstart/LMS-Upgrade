package com.softech.vu360.lms.meetingservice;

import com.softech.vu360.util.Brander;

public interface SynchronousMeetingService {
public String getJoinMeetingURL(Brander brand,String meetingNumber,String attendeeName,String attendeeEmail,String meetingPassword)throws MeetingServiceException;
}
