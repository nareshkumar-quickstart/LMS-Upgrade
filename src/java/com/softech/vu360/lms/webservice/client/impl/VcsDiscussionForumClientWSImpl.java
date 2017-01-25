/**
 * 
 */
package com.softech.vu360.lms.webservice.client.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.DiscussionForumCourse;
import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.webservice.client.VcsDiscussionForumClientWS;
import com.softech.vu360.lms.webservice.message.vcs.dfc.forum.Category;
import com.softech.vu360.lms.webservice.message.vcs.dfc.forum.CreateForum;
import com.softech.vu360.lms.webservice.message.vcs.dfc.forum.CreateForumResponse;
import com.softech.vu360.lms.webservice.message.vcs.dfc.forum.DeleteForum;
import com.softech.vu360.lms.webservice.message.vcs.dfc.forum.DeleteForumResponse;
import com.softech.vu360.lms.webservice.message.vcs.dfc.forum.Forum;
import com.softech.vu360.lms.webservice.message.vcs.dfc.user.GetUserAvatarName;
import com.softech.vu360.lms.webservice.message.vcs.dfc.user.GetUserAvatarNameResponse;
import com.softech.vu360.lms.webservice.message.vcs.dfc.user.RegisterUser;
import com.softech.vu360.lms.webservice.message.vcs.dfc.user.RegisterUserResponse;
import com.softech.vu360.lms.webservice.message.vcs.dfc.user.RevokeUserPermissions;
import com.softech.vu360.lms.webservice.message.vcs.dfc.user.RevokeUserPermissionsResponse;
import com.softech.vu360.lms.webservice.message.vcs.dfc.user.SaveUserAvatar;
import com.softech.vu360.lms.webservice.message.vcs.dfc.user.SaveUserAvatarResponse;
import com.softech.vu360.lms.webservice.message.vcs.dfc.user.UserAccount;
import com.softech.vu360.lms.webservice.message.vcs.dfc.user.WSResponse;
import com.softech.vu360.util.VU360Properties;

/**
 * This class implements all the interface methods for example,
 * createForum which will call phpBB Discussion Forum web service to create forums. 
 * 
 * @author sana.majeed
 */
public class VcsDiscussionForumClientWSImpl implements VcsDiscussionForumClientWS {
	
	private static final Logger log = Logger.getLogger( VcsDiscussionForumClientWSImpl.class.getName() );
	
	// Get the end-points from vu360-lms.properties file
	private static final String VCS_DFC_FORUM_ENDPOINT = VU360Properties.getVU360Property("vcsDFCForumEndPoint"); 
	private static final String VCS_DFC_USER_ENDPOINT = VU360Properties.getVU360Property("vcsDFCUserEndPoint");
	
	private static final String DFC_USER_TYPE_LEARNER = "L";
	private static final String DFC_USER_TYPE_INSTRUCTOR = "I";
	
	private static final String DFC_USER_CONTEXT_PATH = "com.softech.vu360.lms.webservice.message.vcs.dfc.user";
	private static final String DFC_FORUM_CONTEXT_PATH = "com.softech.vu360.lms.webservice.message.vcs.dfc.forum";
	
	/**
	 * LMS will call this event to create forum on phpBB.
	 */
	@Override
	public boolean createForumEvent(DiscussionForumCourse dfCourse) {		
		
		if (dfCourse == null) {
			return false;
		}
		
		Category category = this.getCategoryRequestObject( dfCourse.getContentOwner() );
		Forum forum = this.getForumRequestObject( dfCourse );
		
		return this.createForumOnPhpbb(category, forum);		
	}
	
	protected Category getCategoryRequestObject ( ContentOwner contentOwner ) {
		
		Category category = new Category();
		category.setCategoryTitle( contentOwner.getName() );
		category.setCategoryGUID( contentOwner.getGuid().replace("-", "") );		
		
		return category;
	}
	
	protected Forum getForumRequestObject ( DiscussionForumCourse dfCourse ) {
		
		Forum forum = new Forum();
		forum.setForumName( dfCourse.getCourseTitle() );
		forum.setForumDesc( dfCourse.getDescription() );
		forum.setForumGUID( dfCourse.getCourseGUID().replace("-", "") );
		
		return forum;		
	}
	
	/**
	 * Create new forum on phpBB if GUID not exists, and update otherwise.
	 * @param category
	 * @param forum
	 * @return
	 */
	protected boolean createForumOnPhpbb ( Category category, Forum forum ) {
		
		try {
			CreateForum request = new CreateForum();
			request.setCategory(category);
			request.setForum(forum);
			
			// setup web service template; static code for all web services
			WebServiceTemplate webServiceTemplate = this.getWebServiceSetup(VCS_DFC_FORUM_ENDPOINT, DFC_FORUM_CONTEXT_PATH);
			
			//call web service
	    	CreateForumResponse response = (CreateForumResponse) webServiceTemplate.marshalSendAndReceive(request);
	    	com.softech.vu360.lms.webservice.message.vcs.dfc.forum.WSResponse wsResponse = response.getWSResponse();
	    	log.debug("createForumOnPhpbb > " + wsResponse.getMessage());
	    	if (wsResponse.getSuccess() != 1) {	    		
	    		throw new Exception(wsResponse.getMessage());
	    	}
	    	return true;
		}
		catch (Exception e) {
			//e.printStackTrace();
			log.debug("createForumOnPhpbb > Error occured during createForum service call. \nError: " + e.getMessage());
		}
		
		return false;		
	}

	/**
	 * LMS will call this event to update forum information on phpBB.
	 */
	@Override
	public boolean updateForumEvent(DiscussionForumCourse dfCourse) {
		
		if (dfCourse == null) {
			return false;
		}
		
		Category category = this.getCategoryRequestObject( dfCourse.getContentOwner() );
		Forum forum = this.getForumRequestObject( dfCourse );
		
		return this.createForumOnPhpbb(category, forum);
	}

	/**
	 * LMS will call this event to register Instructor on phpBB.
	 */
	@Override
	public boolean registerInstructorEvent(Instructor instructor, String courseGUID) {
		
		if (instructor == null || StringUtils.isBlank(courseGUID) ) {
			return false;
		}
		courseGUID = courseGUID.replace("-", "");
		
		UserAccount userAccount = this.getUserAccountRequestObject( instructor.getUser(), DFC_USER_TYPE_INSTRUCTOR );
		return this.registerUserOnPhpbb(userAccount, courseGUID);
	}
	
	protected UserAccount getUserAccountRequestObject ( VU360User user, String userType ) {
		
		UserAccount userAccount = new UserAccount();
		userAccount.setUserName( user.getUsername() );
		userAccount.setPassword( user.getPassword() );
		userAccount.setEmail( user.getEmailAddress() );
		userAccount.setUserGUID( user.getUserGUID().replace("-", "") );
		userAccount.setUserType( userType );
		
		return userAccount;
	}
	
	
	/**
	 * Create new user on phpBB if GUID not exists, and update otherwise; then grant access to given forum.
	 * @param userAccount
	 * @param forumGUID
	 * @return
	 */	
	protected boolean registerUserOnPhpbb ( UserAccount userAccount, String forumGUID ) {
		
		try {
			RegisterUser request = new RegisterUser();
			request.setUserAccount( userAccount );
			request.setForumGUID( forumGUID );
			
			// setup web service template; static code for all web services
			WebServiceTemplate webServiceTemplate = this.getWebServiceSetup(VCS_DFC_USER_ENDPOINT, DFC_USER_CONTEXT_PATH);
			
			//call web service
	    	RegisterUserResponse response = (RegisterUserResponse) webServiceTemplate.marshalSendAndReceive(request);
	    	
	    	WSResponse wsResponse = response.getWSResponse();
	    	log.debug("registerUserOnPhpbb > " + wsResponse.getMessage());
	    	if (wsResponse.getSuccess() != 1) {	    		
	    		throw new Exception(wsResponse.getMessage());
	    	}
	    	return true;	    	
		}
		catch (Exception e) {
			//e.printStackTrace();
			log.debug("registerUserOnPhpbb > Error occured during registerUser service call. \nError: " + e.getMessage());
		}
		return false;		
	}
	
	/**
	 * LMS will call this event to revoke Instructor permissions from forum on phpBB.
	 */
	@Override
	public boolean revokeInstructorPermissionsEvent(String instructorGUID, String courseGUID) {
		
		if ( StringUtils.isBlank(instructorGUID) || StringUtils.isBlank(courseGUID) ) {
			return false;
		}		
		instructorGUID = instructorGUID.replace("-", "");
		courseGUID = courseGUID.replace("-", "");
		
		return this.revokeUserPermissionsOnPhpbb(instructorGUID, DFC_USER_TYPE_INSTRUCTOR, courseGUID);		
	}
	
	/**
	 * Revoke user permission from given forum based on user type on phpBB.
	 * @param userGUID
	 * @param userType
	 * @param forumGUID
	 * @return
	 */
	protected boolean revokeUserPermissionsOnPhpbb ( String userGUID, String userType, String forumGUID ) {
		
		try {
			RevokeUserPermissions request = new RevokeUserPermissions();
			request.setUserGUID( userGUID );
			request.setUserType( userType );
			request.setForumGUID( forumGUID );
			
			// setup web service template; static code for all web services
			WebServiceTemplate webServiceTemplate = this.getWebServiceSetup(VCS_DFC_USER_ENDPOINT, DFC_USER_CONTEXT_PATH);
			
	    	//call web service
	    	RevokeUserPermissionsResponse response = (RevokeUserPermissionsResponse) webServiceTemplate.marshalSendAndReceive(request);
	    	
	    	WSResponse wsResponse = response.getWSResponse();
	    	log.debug("revokeUserPermissionsOnPhpbb > " + wsResponse.getMessage());
	    	if (wsResponse.getSuccess() != 1) {	    		
	    		throw new Exception(wsResponse.getMessage());
	    	}
	    	return true;	    	
		}
		catch (Exception e) {
			//e.printStackTrace();
			log.debug("registerUserOnPhpbb > Error occured during registerUser service call. \nError: " + e.getMessage());
		}
		return false;
	}

	/**
	 *  LMS will call this event to delete forum from phpBB.
	 */
	@Override
	public boolean deleteForumEvent(String courseGUID) {

		if ( StringUtils.isBlank(courseGUID) ) {
			return false;
		}
		
		return this.deleteForumFromPhpbb(courseGUID);		
	}
	
	/**
	 * Delete forum from phpBB.
	 * @param forumGUID
	 * @return
	 */
	protected boolean deleteForumFromPhpbb ( String forumGUID ) {
		
		try {
			DeleteForum request = new DeleteForum();
			request.setForumGUID( forumGUID );
			
			// setup web service template; static code for all web services
			WebServiceTemplate webServiceTemplate = this.getWebServiceSetup(VCS_DFC_FORUM_ENDPOINT, DFC_FORUM_CONTEXT_PATH);
						
			//call web service
	    	DeleteForumResponse response = (DeleteForumResponse) webServiceTemplate.marshalSendAndReceive(request);
	    	
	    	com.softech.vu360.lms.webservice.message.vcs.dfc.forum.WSResponse wsResponse = response.getWSResponse();
	    	log.debug("revokeUserPermissionsOnPhpbb > " + wsResponse.getMessage());
	    	if (wsResponse.getSuccess() != 1) {	    		
	    		throw new Exception(wsResponse.getMessage());
	    	}
	    	return true;	    	
		}
		catch (Exception e) {
			//e.printStackTrace();
			log.debug("deleteForumFromPhpbb > Error occured during deleteForum service call. \nError: " + e.getMessage());
		}
		return false;
		
	}

	/**
	 * LMS will call this event to register Learner on phpBB.
	 */
	@Override
	public boolean registerLearnerEvent(Learner learner, String courseGUID) {
		
		if (learner == null || StringUtils.isBlank(courseGUID) ) {
			return false;
		}
		courseGUID = courseGUID.replace("-", "");		
		UserAccount userAccount = this.getUserAccountRequestObject(learner.getVu360User(), DFC_USER_TYPE_LEARNER);
		
		return this.registerUserOnPhpbb(userAccount, courseGUID);		
	}

	/**
	 * LMS will call this event to get Avatar name as per phpBB configurations to upload file using this name
	 */
	@Override
	public String getUserAvatarNameEvent(String userGUID) {
		
		if ( StringUtils.isBlank(userGUID) ) {
			return null;
		}
		
		userGUID = userGUID.replace("-", "");
		return this.getUserAvatarNameFromPhpbb(userGUID);
	}
	
	/**
	 * Get User Avatar Name from phpBB to be used for Avatar Uploading
	 * @param userGUID
	 * @return
	 */
	protected String getUserAvatarNameFromPhpbb ( String userGUID ) {
				
		try {
			GetUserAvatarName request = new GetUserAvatarName();
			request.setUserGUID( userGUID );
			
			// setup web service template; static code for all web services
			WebServiceTemplate webServiceTemplate = this.getWebServiceSetup(VCS_DFC_USER_ENDPOINT, DFC_USER_CONTEXT_PATH);
			
	    	//call web service
	    	GetUserAvatarNameResponse response = (GetUserAvatarNameResponse) webServiceTemplate.marshalSendAndReceive(request);
	    	
	    	WSResponse wsResponse = response.getWSResponse();
	    	log.debug("getUserAvatarNameFromPhpbb > " + wsResponse.getMessage());
	    	if (wsResponse.getSuccess() != 1) {	    		
	    		throw new Exception(wsResponse.getMessage());
	    	}
	    	
	    	// wsResponse.getMessage() :: contains avatar name IFF [wsResponse.getSuccess() == 1]
	    	return wsResponse.getMessage();	    	
		}
		catch (Exception e) {
			//e.printStackTrace();
			log.debug("getUserAvatarNameFromPhpbb > Error occured during getUserAvatarName service call. \nError: " + e.getMessage());
		}		
		return null;
	}

	/**
	 * LMS will call this event to save User Avatar in phpBB 
	 */
	@Override
	public boolean saveUserAvatarEvent(String userGUID, String avatar, int avatarWidth, int avatarHeight) {
		
		if ( StringUtils.isBlank(userGUID) || StringUtils.isBlank(avatar) || (avatarWidth == 0) || (avatarHeight == 0 ) ) {
			return false;
		}
		
		userGUID = userGUID.replace("-", "");
		return this.saveAvatarOnPhpbb(userGUID, avatar, avatarWidth, avatarHeight);		
	}
	
	/**
	 * Save User Avatar information on phpBB
	 * @param userGUID
	 * @param avatar
	 * @param avatarWidth
	 * @param avatarHeight
	 * @return
	 */
	protected boolean saveAvatarOnPhpbb ( String userGUID, String avatar, int avatarWidth, int avatarHeight ) {
		
		try {
			SaveUserAvatar request = new SaveUserAvatar();
			request.setUserGUID( userGUID );
			request.setAvatar( avatar );
			request.setAvatarWidth( avatarWidth );
			request.setAvatarHeight( avatarHeight );
			
			// setup web service template; static code for all web services
			WebServiceTemplate webServiceTemplate = this.getWebServiceSetup(VCS_DFC_USER_ENDPOINT, DFC_USER_CONTEXT_PATH);
	    	
	    	//call web service
	    	SaveUserAvatarResponse response = (SaveUserAvatarResponse) webServiceTemplate.marshalSendAndReceive(request);
	    	
	    	WSResponse wsResponse = response.getWSResponse();
	    	log.info("saveAvatarOnPhpbb > " + wsResponse.getMessage());
	    	if (wsResponse.getSuccess() != 1) {	    		
	    		throw new Exception(wsResponse.getMessage());
	    	}
	    	
	    	return true;	    	
		}
		catch (Exception e) {
			//e.printStackTrace();
			log.info("saveAvatarOnPhpbb > Error occured during saveUserAvatar service call. \nError: " + e.getMessage());
		}		
		return false;
	}

	private WebServiceTemplate getWebServiceSetup (String uri, String contextPath ) throws Exception {
		
		WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
		webServiceTemplate.setDefaultUri( uri );
		
		//set marshaller/unmarshallar
    	org.springframework.oxm.jaxb.Jaxb2Marshaller marshaller = new org.springframework.oxm.jaxb.Jaxb2Marshaller();
    	org.springframework.oxm.jaxb.Jaxb2Marshaller unmarshaller = new org.springframework.oxm.jaxb.Jaxb2Marshaller();
    	webServiceTemplate.setMarshaller( marshaller );
    	webServiceTemplate.setUnmarshaller( unmarshaller );
    	
    	//set the context-path same as complete package name used in ObjectFactory class
    	marshaller.setContextPath( contextPath );
    	unmarshaller.setContextPath( contextPath );
    	
    	//assure properties are properly set
    	marshaller.afterPropertiesSet();
    	unmarshaller.afterPropertiesSet();
    	
    	return webServiceTemplate;		
	}
	
}
