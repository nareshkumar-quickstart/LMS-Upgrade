package com.softech.vu360.lms.meetingservice;

import org.apache.commons.lang.StringUtils;

import com.softech.vu360.lms.meetingservice.dimdim.DimDimMeetingServiceImpl;
import com.softech.vu360.lms.meetingservice.globalscholar.GlobalScholarMeetingServiceImpl;
import com.softech.vu360.lms.meetingservice.webex.WebExMeetingServiceImpl;


public class SynchronousMeetingServiceFactory {

	WebExMeetingServiceImpl webExMeetingService;
	GlobalScholarMeetingServiceImpl globalScholarMeetingService;
	DimDimMeetingServiceImpl dimdimMeetingService;

	public SynchronousMeetingService getInstance(String meetingType)
	{
		SynchronousMeetingService meetingService=null;
		if(StringUtils.isNotBlank(meetingType) && meetingType.equalsIgnoreCase("GlobalScholar"))
		{
				meetingService=globalScholarMeetingService;
		}
		else if(StringUtils.isNotBlank(meetingType) && meetingType.equalsIgnoreCase("dimdim")){
			meetingService = dimdimMeetingService;
		}
		else
		{
			meetingService=webExMeetingService;
		}
		return meetingService;
	}
	public WebExMeetingServiceImpl getWebExMeetingService() {
		return webExMeetingService;
	}
	public void setWebExMeetingService(WebExMeetingServiceImpl webExMeetingService) {
		this.webExMeetingService = webExMeetingService;
	}
	public GlobalScholarMeetingServiceImpl getGlobalScholarMeetingService() {
		return globalScholarMeetingService;
	}
	public void setGlobalScholarMeetingService(
			GlobalScholarMeetingServiceImpl globalScholarMeetingService) {
		this.globalScholarMeetingService = globalScholarMeetingService;
	}
	/**
	 * @return the dimdimMeetingService
	 */
	public DimDimMeetingServiceImpl getDimdimMeetingService() {
		return dimdimMeetingService;
	}
	/**
	 * @param dimdimMeetingService the dimdimMeetingService to set
	 */
	public void setDimdimMeetingService(
			DimDimMeetingServiceImpl dimdimMeetingService) {
		this.dimdimMeetingService = dimdimMeetingService;
	}
}
