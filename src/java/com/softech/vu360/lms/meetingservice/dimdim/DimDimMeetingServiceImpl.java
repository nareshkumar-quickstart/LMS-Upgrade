package com.softech.vu360.lms.meetingservice.dimdim;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.softech.vu360.lms.meetingservice.MeetingServiceException;
import com.softech.vu360.lms.meetingservice.SynchronousMeetingService;
import com.softech.vu360.lms.model.InstructorSynchronousClass;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.util.Brander;

/**
 * @author Faisal Ahmed Siddiqui
 */
public class DimDimMeetingServiceImpl implements SynchronousMeetingService {

	Logger log = Logger.getLogger(DimDimMeetingServiceImpl.class);
	@Override
	public String getJoinMeetingURL(Brander brand,String hostAccountName,String attendeeName,String attendeeEmail,String meetingPassword)throws MeetingServiceException{
		
		StringBuilder joinMeetingURL=new StringBuilder(brand.getBrandElement("lms.dimdim.learner.joinmeeting.url").trim());
		//account=?&displayName=?&attendeeKey=?&email=?
		joinMeetingURL.append("account=");joinMeetingURL.append(hostAccountName);
		joinMeetingURL.append("&displayName=");joinMeetingURL.append(attendeeName);
		joinMeetingURL.append("&email=");joinMeetingURL.append(attendeeEmail);
		if(StringUtils.isNotBlank(meetingPassword))
			joinMeetingURL.append("&attendeeKey=");joinMeetingURL.append(meetingPassword);
			
		return joinMeetingURL.toString();
	}
	/*
	 * using dimdim super admin credentials read if user exist
	 * if adminLoginResponse is null then this method will create admin login session by itself 
	 */
	public boolean userExist(InstructorSynchronousClass instSynchClass,DimDimLoginResponse loginResponse){
		boolean userExists = false;
		if(loginResponse==null)
			loginResponse = loginAdminUser();
		
		if(!loginResponse.anyError()){
			DimDimReadUserRequest readUserRequest = new DimDimReadUserRequest();
			readUserRequest.setUserName(instSynchClass.getSynchronousClass().getGuid());
			readUserRequest.setAuthToken(loginResponse.getAuthToken());
			DimDimConnection con1 = new DimDimConnection();
			DimDimReadUserResponse readUserResponse = (DimDimReadUserResponse) con1.sendAndReceive(readUserRequest);
			userExists = readUserResponse.userExists().booleanValue();
		}
		return userExists;
	}

	private DimDimLoginResponse loginAdminUser(){
		DimDimLoginRequest loginRequest = new DimDimLoginRequest(true);
		String userName = DimDimConfiguration.getInstance().getEnterpriseAdminUserName();
		String password = DimDimConfiguration.getInstance().getEnterpriseAdminPassword();
		loginRequest.setUserName(userName);
		loginRequest.setPassword(password);
		DimDimConnection con1 = new DimDimConnection();
		DimDimLoginResponse loginResponse = (DimDimLoginResponse)con1.sendAndReceive(loginRequest);
		return loginResponse;
	}
	/*
	 * if you don't know the adminLogin, just pass null. 
	 * but if this method being called with another business method then it can pass adminLogin to avoid redundant login calls on dimdim
	 */
	private DimDimUpdateUserResponse updateDimDimUser(InstructorSynchronousClass instSynchClass,DimDimLoginResponse adminLogin){
		if(adminLogin==null)
			adminLogin = loginAdminUser();
		
		DimDimUpdateUserRequest updateUserRequest = new DimDimUpdateUserRequest();
		updateUserRequest.setUserName(DimDimConfiguration.getInstance().getEnterpriseAdminUserName());
		updateUserRequest.setAuthToken(adminLogin.getAuthToken());
		setUserRequestParameters(updateUserRequest, instSynchClass);		
		DimDimConnection con1 = new DimDimConnection();
		DimDimUpdateUserResponse updateUserResposne = (DimDimUpdateUserResponse)con1.sendAndReceive(updateUserRequest);
		if(updateUserResposne.anyError()){
			//TODO translate error and throw an exception
		}
		return updateUserResposne;
	}
	
	public String startMeeting(InstructorSynchronousClass instSynchClass,Brander brander){
		// first check if user starting a meeting has got account on dimdim
		DimDimLoginResponse adminLoginResponse = loginAdminUser();
		boolean userExist = userExist(instSynchClass,adminLoginResponse);
		if(!userExist){
			//create new user if not exist
			createDimDimUser(instSynchClass,adminLoginResponse);
		}
		
		// login using instructor credential to start meeting
		DimDimLoginRequest loginRequest = new DimDimLoginRequest();
		String userName = instSynchClass.getSynchronousClass().getGuid();
		String password = instSynchClass.getSynchronousClass().getMeetingPassCode();
		loginRequest.setUserName(userName);
		loginRequest.setPassword(password);
		
		DimDimConnection con1 = new DimDimConnection();
		DimDimLoginResponse loginResponse = (DimDimLoginResponse)con1.sendAndReceive(loginRequest);
		
		String startMeetingURL = null;
		if(!loginResponse.anyError())//if not error then go ahead 
		{
			DimDimStartMeetingRequest startMeetingReq = new DimDimStartMeetingRequest();
			startMeetingReq.setAuthToken(loginResponse.getAuthToken());
			startMeetingReq.setClientId(userName);
			startMeetingReq.setUserName(userName);
			startMeetingReq.setEnterpriseName(DimDimConfiguration.getInstance().getEnterpriseName());
			startMeetingReq.setLobbyEnabled(true);// we dont wan't waiting area
			startMeetingReq.setAttendeeKey(instSynchClass.getSynchronousClass().getMeetingPassCode());
			startMeetingReq.setHostReturnURL("http://localhost:8080/lms");
			startMeetingReq.setDisplayName(instSynchClass.getInstructor().getUser().getFirstName()+" "+instSynchClass.getInstructor().getUser().getLastName());
			//startMeetingReq.setGroupName("all");
			//startMeetingReq.setRoomName("default");// TODO need to double check if we should update it with class room name 
			DimDimConnection con2 = new DimDimConnection();
			DimDimStartMeetingResponse startMeetingResponse = (DimDimStartMeetingResponse)con2.sendAndReceive(startMeetingReq);
			log.debug("session-key:"+startMeetingResponse.getSessionKey());
			startMeetingURL = DimDimConfiguration.getInstance().getStartMeetingConsoleURL()+loginResponse.getAuthToken();
		}else{
			//handle error here and throw exception 
		}
		return startMeetingURL;
	}
	public boolean isMeetingInProgress(String userName){
		DimDimCheckMeetingRequest checkMeetingRequest = new DimDimCheckMeetingRequest();
		checkMeetingRequest.setUserName(userName);
		checkMeetingRequest.setEnterpriseName(DimDimConfiguration.getInstance().getEnterpriseName());
		//checkMeetingRequest.setGroupName(groupName) its optional
		//checkMeetingRequest.setRoomName("default");// TODO in 1 LMS user 1 User in DimDim we will be using default as room name
		DimDimConnection con = new DimDimConnection();
		DimDimCheckMeetingResponse checkMeetingResponse = (DimDimCheckMeetingResponse)con.sendAndReceive(checkMeetingRequest);
		if(!checkMeetingResponse.anyError()){
			return checkMeetingResponse.getMeetingExists();
		}else{
			//handle error here...
			return false;
		}
	}
	
	public DimDimCreateUserResponse createDimDimUser(InstructorSynchronousClass instClass,DimDimLoginResponse adminLoginResponse){
		
		if(adminLoginResponse==null)
			adminLoginResponse = loginAdminUser();
		
		DimDimCreateUserRequest createUserRequest = new DimDimCreateUserRequest();
		createUserRequest.setUserName(DimDimConfiguration.getInstance().getEnterpriseAdminUserName());
		createUserRequest.setAuthToken(adminLoginResponse.getAuthToken());
		setUserRequestParameters(createUserRequest, instClass);
		
		DimDimConnection con1 = new DimDimConnection();
		DimDimCreateUserResponse response = (DimDimCreateUserResponse) con1.sendAndReceive(createUserRequest);
		if(response.anyError()){
			//TODO throw exception here and catch in the client code to display user friendly message  
		}
		return response;
	}
	private void setUserRequestParameters(DimDimCreateUserRequest createUserRequest, InstructorSynchronousClass instClass){
		VU360User user = instClass.getInstructor().getUser();
		//createUserRequest.setPassword(DimDimConfiguration.getInstance().getEnterpriseAdminPassword());
		//createUserRequest.setCity(user.getLearner().getLearnerProfile().getLearnerAddress().getCity());
		//createUserRequest.setCountry(user.getLearner().getLearnerProfile().getLearnerAddress().getCountry());
		createUserRequest.setDisplayName(instClass.getSynchronousClass().getSectionName());
		createUserRequest.setEnterpriseName(DimDimConfiguration.getInstance().getEnterpriseName());
		//createUserRequest.setEmail(user.getEmailAddress());
		createUserRequest.setFirstName(instClass.getSynchronousClass().getSectionName());
		//createUserRequest.setGroupName("all");
		//createUserRequest.setLastName(user.getLastName());
		//createUserRequest.setLocale(locale) //TODO we don't have locale information in user profile
		createUserRequest.setNewUserName(instClass.getSynchronousClass().getGuid());// this is supposed to be without dashes
		createUserRequest.setNotify(false);//we don't want to notify user on creation in DIMDIM
		
		createUserRequest.setPassword(instClass.getSynchronousClass().getMeetingPassCode());
		createUserRequest.setPhoneNumber(instClass.getInstructor().getUser().getLearner().getCustomer().getPhoneNumber());
		
		if(instClass.getInstructor().getUser().getLearner().getCustomer().getBillingAddress().getState()!=null)
			createUserRequest.setState(instClass.getInstructor().getUser().getLearner().getCustomer().getBillingAddress().getState());
		
		if(instClass.getInstructor().getUser().getLearner().getCustomer().getBillingAddress().getStreetAddress()!=null)
			createUserRequest.setStreet(user.getLearner().getLearnerProfile().getLearnerAddress().getStreetAddress()+" "+ user.getLearner().getLearnerProfile().getLearnerAddress().getStreetAddress2());
		//createUserRequest.setTimeZone(user.getLearner().getLearnerProfile().getTimeZone().)TODO have to change timezone format according to dimdim
		if(instClass.getInstructor().getUser().getLearner().getCustomer().getBillingAddress().getZipcode()!=null)
			createUserRequest.setZipCode(instClass.getInstructor().getUser().getLearner().getCustomer().getBillingAddress().getZipcode());
		
	}
}
