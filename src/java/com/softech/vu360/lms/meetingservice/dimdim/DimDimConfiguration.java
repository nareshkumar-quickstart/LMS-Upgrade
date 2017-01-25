package com.softech.vu360.lms.meetingservice.dimdim;

import com.softech.vu360.util.VU360Properties;

public class DimDimConfiguration {
	private static DimDimConfiguration instance = null;
	private String loginURL;
	private String adminLoginURL;
	private String startMeetingURL;
	private String endMeetingURL;
	private String checkMeetingURL;
	private String joinMeetingURL;
	private String createUserURL;
	private String checkUserURL;
	private String updateUserURL;
	
	private String enterpriseAdminUserName;
	private String enterpriseAdminPassword;
	
	private String startMeetingConsoleURL;
	
	private String enterpriseName;
	
	private DimDimConfiguration(){
		String enterprise = VU360Properties.getVU360Property("dimdim.enterprise.url"); 
		String enterpriseAdmin = VU360Properties.getVU360Property("dimdim.adminenterprise.url");
		
		loginURL = enterprise+"/api/auth/login";
		adminLoginURL = enterpriseAdmin+"/api/auth/login";
		
		startMeetingURL = enterprise+"/api/conf/start_meeting";
		endMeetingURL = enterprise+"/api/conf/end_meeting";
		checkMeetingURL = enterprise+"/api/conf/check_meeting";
		joinMeetingURL = enterprise+"/api/conf/join_meeting";
		
		createUserURL = enterpriseAdmin+"/api/admn/create_entity";
		checkUserURL = enterpriseAdmin + "/api/admn/read";
		updateUserURL = enterpriseAdmin + "/api/admn/set_entity_parameters";
		
		enterpriseAdminUserName = VU360Properties.getVU360Property("dimdim.enterprise.adminuser");
		enterpriseAdminPassword = VU360Properties.getVU360Property("dimdim.enterprise.adminpassword");
		
		startMeetingConsoleURL = VU360Properties.getVU360Property("dimdim.startmeeting.consoleURL");
		
		enterpriseName = VU360Properties.getVU360Property("dimdim.enterprise.name");

	}
	public String getUpdateUserURL() {
		return updateUserURL;
	}
	public String getAdminLoginURL() {
		return adminLoginURL;
	}
	public String getReadUserURL() {
		return checkUserURL;
	}
	public String getStartMeetingConsoleURL() {
		return startMeetingConsoleURL;
	}
	public static synchronized DimDimConfiguration getInstance(){
		if(instance==null){
			instance = new DimDimConfiguration();
		}
		return instance;
	}
	public String getStartMeetingURL(){
		return startMeetingURL;
	}
	public String getJoinMeetingURL(){
		return joinMeetingURL;
	}
	public String getCheckMeetingURL(){
		return checkMeetingURL;
	}
	public String getEndMeetingURL(){
		return endMeetingURL;
	}
	
	public String getLoginURL(){
		return loginURL;
	}
	public String getCreateUserURL(){
		return createUserURL;
	}

	public String getEnterpriseAdminUserName(){
		return enterpriseAdminUserName;
	}
	
	public String getEnterpriseAdminPassword(){
		return enterpriseAdminPassword;
	}
	
	public String getStartMeetingConsoleURL(String authToken){
		startMeetingConsoleURL = startMeetingConsoleURL + authToken;
		return startMeetingConsoleURL;
	}
	
	public String getEnterpriseName(){
		return enterpriseName;
	}
}
