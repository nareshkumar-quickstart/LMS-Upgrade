package com.softech.vu360.lms.web.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.LearningSession;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.ExternalStatisticsProcessor;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.SecurityAndRolesService;
import com.softech.vu360.lms.service.StatisticsService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.VU360Branding;

/**
 * AICC Controller that allows access to all content
 * via AICC adapter.
 * 
 * @author Jason Burns
 */
public class AICCController implements Controller {
	
	private static final Logger log = Logger.getLogger(AICCController.class.getName());
		
	private CustomerService customerService = null;
	private EntitlementService entitlementService = null;
	private CourseAndCourseGroupService courseService = null;
	private VU360UserService userService = null;
	private EnrollmentService enrollmentService = null;
	private LearnerService learnerService = null;
	private SecurityAndRolesService securityService = null;
	private OrgGroupLearnerGroupService groupService = null;
	private ExternalStatisticsProcessor aiccHandler = null;
	private StatisticsService statsService = null;
	private String errorTemplate = null;
	private String launchTemplate = null;
	private String statsProcesed = null;

	public ModelAndView handleRequest( HttpServletRequest request, HttpServletResponse response ) {
		String cmd = request.getParameter("cmd");
		if ( cmd != null && !cmd.isEmpty() ) {
			// temporary hack to do putparam asynchronously
			String learningSessionId = request.getParameter("lsid");
			LearningSession ls = statsService.getLearningSessionByLearningSessionId(learningSessionId);
			aiccHandler.handleLearingSessionCompleteEvent(ls,"");
			Map<Object, Object> context = new HashMap<Object, Object>();
			return new ModelAndView(statsProcesed, "context", context);
		}
		
		response.setHeader("p3p", "CP=\"IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT\"");
		
		log.debug("starting AICC session");
		
		Enumeration<String> paramNames = (Enumeration<String>) request.getParameterNames();
				
		String aicc_sid = null;
		String aicc_url = null;
		String course_id = null;
		String customer_id = null;
		
		while(paramNames.hasMoreElements())
		{
			String param = paramNames.nextElement();
			if( param.toLowerCase().equals("aicc_sid"))
				aicc_sid = request.getParameter(param);
			else if( param.toLowerCase().equals("aicc_url"))
				aicc_url = request.getParameter(param);
			else if( param.toLowerCase().equals("customer_id"))
				customer_id = request.getParameter(param);
			else if( param.toLowerCase().equals("course_id"))
				course_id = request.getParameter(param);
		}

		// decode the URL
		try {
			aicc_url = URLDecoder.decode(aicc_url, "UTF-8");
		}
		catch (UnsupportedEncodingException ex) {
			log.error("could not decode URL from aicc launch:"+ex.getMessage(), ex);
		}
		
		String brand = request.getParameter("brand");
		if ( brand == null || brand.trim().isEmpty() ) {
			brand = request.getParameter("BRAND");
		}
		
		String lang = request.getParameter("language");
		if ( lang == null || lang.trim().isEmpty() ) {
			lang = request.getParameter("LANGUAGE");
		}
		
		boolean errors = false;
		Map<Object, Object> context = new HashMap<Object, Object>();
		if ( aicc_sid == null || aicc_sid.trim().isEmpty() ) {
			log.debug("aicc_sid null or empty");
			errors = true;
			context.put("aicc_sid", Boolean.TRUE);
		}
		if ( aicc_url == null || aicc_url.trim().isEmpty() ) {
			log.debug("aicc_url null or empty");
			errors = true;
			context.put("aicc_url", Boolean.TRUE);
		}
		if ( course_id == null || course_id.trim().isEmpty() ) {
			log.debug("course_id null or empty");
			errors = true;
			context.put("course_id", Boolean.TRUE);
		}
		if ( customer_id == null || customer_id.trim().isEmpty() ) {
			log.debug("customer_id null or empty");
			errors = true;
			context.put("customer_id", Boolean.TRUE);
		}
		
		if ( errors ) {
			return new ModelAndView(errorTemplate, "context", context);
		}
		
		// check launch params for customer and course
		Customer cust = customerService.getCustomerByCustomerCode(customer_id);
		Course course = courseService.getCourseByGUID(course_id);
		if ( course == null ) {
			// did not find by GUID need to be backwards compatible to original version
			// scheme of looking up courses by businesskey
			course = courseService.getCourseByCourseId(course_id);
		}
		
		if ( cust == null ) {
			log.debug("customer not found:"+customer_id);
			errors = true;
			context.put("invalid_customer", Boolean.TRUE);
			return new ModelAndView(errorTemplate, "context", context);
		}
		if ( !cust.getAiccInterfaceEnabled() ) {
			log.debug("customer is not setup for AICC:"+customer_id);
			errors = true;
			context.put("aicc_disabled", Boolean.TRUE);
			return new ModelAndView(errorTemplate, "context", context);
		}
		if ( course == null ) {
			log.debug("course not found:"+course_id);
			errors = true;
			context.put("invalid_course", Boolean.TRUE);
			return new ModelAndView(errorTemplate, "context", context);
		}
		
		if ( errors ) {
			return new ModelAndView(errorTemplate, "context", context);
		}
		
		com.softech.vu360.lms.vo.Language language = new com.softech.vu360.lms.vo.Language();
		language.setLanguage(Language.DEFAULT_LANG);
		
		if ( cust.getBrandName() == null || (cust.getBrandName() != null && cust.getBrandName().trim().isEmpty()) ) {
			// use the default brand
			brand = null;
		}
		else {
			// use the customer's brand
			brand = cust.getBrandName().trim();
		}
		
		Brander brander = VU360Branding.getInstance().getBrander(brand, language);
		request.getSession().setAttribute(VU360Branding.BRAND, brander.getName());
		
		// perform AICC getParams
		StringBuffer getParamUrl = new StringBuffer(aicc_url);
		StringBuffer postData = new StringBuffer();
		HttpURLConnection connection = null;
		BufferedReader rd = null;
		StringBuilder result = null;
		String line = null;
		try {
			// for those LMSs looking on the request line
			if ( aicc_url.toString().indexOf("?") > 0 ) {
				getParamUrl.append("&");
			}
			else {
				getParamUrl.append("?");
			}
			getParamUrl.append("command=getparam&version=");
			getParamUrl.append(URLEncoder.encode("4.0", "UTF-8"));
			getParamUrl.append("&session_id=");
			getParamUrl.append(URLEncoder.encode(aicc_sid, "UTF-8"));

			postData.append("command=getparam&version=");
			postData.append(URLEncoder.encode("4.0", "UTF-8"));
			postData.append("&session_id=");
			postData.append(URLEncoder.encode(aicc_sid, "UTF-8"));

			log.error("getparam URL:"+getParamUrl.toString());
			log.error("postData:"+postData.toString());

			URL serverAddress = null;

			serverAddress = new URL(getParamUrl.toString());
			// set up out communications stuff
			connection = null;

			// Set up the initial connection
			connection = (HttpURLConnection) serverAddress.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("CONTENT-TYPE", "application/x-www-form-urlencoded;charset=utf-8");
			connection.setRequestProperty("Content-Length", String.valueOf(postData.toString().getBytes().length));
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setReadTimeout(10000);
			
			// write to the body for the POST
			OutputStream out = connection.getOutputStream();
			Writer outWriter = new OutputStreamWriter(out, "UTF-8");
			outWriter.write(postData.toString());
			outWriter.flush();
			outWriter.close();

			connection.connect();

			// read the result from the server
			rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			result = new StringBuilder();

			while ((line = rd.readLine()) != null) {
				result.append(line + '\n');
			}
			
			log.info(result.toString());
		}
		catch (Exception e) {
			log.error("error during getparam call:"+e.getMessage(), e);
			log.error("getparam URL:"+getParamUrl.toString());
			log.info("postData:"+postData.toString());
			log.info("aicc server response");			
			if ( result != null ) {
				log.info(result.toString());
			}
			else {
				log.info("no result from server.");
			}
			context.put("error_getparam", Boolean.TRUE);			
			return new ModelAndView(errorTemplate, "context", context);
		}
		finally {
			// close the connection, set all objects to null
			connection.disconnect();
			rd = null;
			connection = null;
		}
		String errorMessage=null;
		try {
		
			// parse getParam results
			log.info("received get param from server:"+result.toString());
			
			String[] lines = result.toString().split("\n");
			String[] parts;
			
			String studentID = null;
			String studentName = null;
			
			for (String aString : lines) {
				if ( !aString.isEmpty() ) {
					parts = aString.split("=");
					if ( parts != null && parts.length == 2 ) {
						if ( parts[0].trim().equalsIgnoreCase("Student_ID") ) {
							studentID = parts[1].trim();
						}
						else if ( parts[0].trim().equalsIgnoreCase("Student_Name") ) {
							studentName = parts[1].trim();
						}
					}
					else {
						log.info("unknown name/value pair:"+aString);
					}
				}
			}
			
			if ( studentID == null || studentID.isEmpty() ) {
				log.info("studentID not found");
				log.info("getparam URL:"+getParamUrl.toString());
				log.info("postData:"+postData.toString());
				log.info("aicc server response");
				if ( result != null ) {
					log.info(result.toString());
				}
				else {
					log.info("no result from server.");
				}
	
				context.put("studentid_not_found", Boolean.TRUE);
				return new ModelAndView(errorTemplate, "context", context);
			}
			
			String firstName = studentName;
			String lastName = null;
			log.info("AICC Student Name:"+studentName);
			if ( studentName != null ) {
				String[] names = studentName.split(",");
				if ( names.length > 1 ) {
					firstName = names[1];
					lastName = names[0];
				}
				else {
					firstName = names[0];
					lastName = names[0];
				}
			}
			else {
				log.info("studentName not found");
				log.info("getparam URL:"+getParamUrl.toString());
				log.info("postData:"+postData.toString());
				log.info("aicc server response");
				if ( result != null ) {
					log.info(result.toString());
				}
				else {
					log.info("no result from server.");
				}
	
				context.put("studentname_not_found", Boolean.TRUE);
				return new ModelAndView(errorTemplate, "context", context);
	
			}
			
			VU360User user=null;
			try{
			  user= this.getUserForCourseLaunch(customer_id, cust,studentID, firstName, lastName);
			}
			catch(Exception exp){
				context.put("error_searchUser",exp.getMessage());
				throw exp;
			}
																
			LearnerEnrollment enrollment = null;
			try{	
				enrollment = this.getEnrollmentForCourse(context, cust, course, user,aicc_sid);
			}
			catch(Exception exp){
				enrollment= null;
				return logErrorAndReturnTemplate(exp.getMessage(), context);				
			}	
			
			// launch course as usual - set source=AICC and call-back URL so that stats service can call back the referring LMS
			// need to figure out how to create/fake out a logged in/authenticated user credential so 
			// that we try to protect the launch URL from hackers....
			try {
				context.put("externallmssessionid", URLEncoder.encode(aicc_sid, "UTF-8"));
				context.put("externallmsurl", URLEncoder.encode(aicc_url, "UTF-8"));
			}
			catch (UnsupportedEncodingException ex) {
				log.error("cannot encode variables for AICC launch:"+ex.getMessage(), ex);
			}
			context.put("enrollmentId", enrollment.getId());
			context.put("courseGUID", course.getCourseGUID());
			context.put("lmsProvider", cust.getLmsProvider());
			context.put("course", course);
			log.debug("redirecting AICC session to launch page");
			return new ModelAndView(launchTemplate, "context", context);
		}
		catch (Exception e) {
			log.error("error during getparam call:"+e.getMessage(), e);
			log.info("getparam URL:"+getParamUrl.toString());
			log.info("postData:"+postData.toString());
			log.info("aicc server response");						
			if ( result != null ) {
				log.info(result.toString());
			}
			else {
				log.info("no result from server.");
			}			
			context.put("error_getparam", Boolean.TRUE);
			return new ModelAndView(errorTemplate, "context", context);
		}
	}

	/**
	 * Gets existing enrollments or creates a new enrollment for user 
	 * @param course_id
	 * @param customer_id
	 * @param context
	 * @param cust
	 * @param course
	 * @param user
	 * @return
	 * @throws Exception
	 */
	private LearnerEnrollment getEnrollmentForCourse(Map<Object, Object> context, Customer cust, Course course, VU360User user,String externalLMSSessionId)throws Exception {
		// place user in security context so we do not get a login page when we redirect for launch course
		Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		//Get active enrollment for course. 
		//LearnerEnrollment enrollment = enrollmentService.getAICCLearnerEnrollment(externalLMSSessionId,user.getLearner().getId(),course.getId());
		LearnerEnrollment enrollment = enrollmentService.getAICCLearnerEnrollment(externalLMSSessionId,user.getLearner().getId(),course.getId(), course.getCourseGUID());
		
		//If enrollment end date has passed for an uncompleted course then exit.  
		if(enrollment!=null && enrollment.getEnrollmentEndDate().compareTo(new Date(System.currentTimeMillis()))<0){ 
			LearnerCourseStatistics courseStats= statsService.loadForUpdateLearnerCourseStatistics(enrollment.getCourseStatistics().getId());
			if(/*!courseStats.isCompleted()*/ !courseStats.isCourseCompleted())
			   throw new Exception("Your course cannot be launched because enrollment end date '" + enrollment.getEnrollmentEndDate() + "' has passed.");								 				 				 				
		}
		//If not active enrollment exists and course is retired, then exit. 
		if(enrollment == null && course.isRetired()) 				
		   throw new Exception("Your course cannot be launched because it is retired and is not available for enrollment.");
		
		
		//If no active enrollment exists, enroll into a valid contract
		if (enrollment == null ) {
			CustomerEntitlement custEntitlement = entitlementService.customerHasActiveEntitlementFor(cust, course);				
			if ( custEntitlement == null ) //if no contract found then exit
				throw new Exception("Entitlement to course: "+course.getId()+" has expired or does not exist for customer: "+cust.getName());				
			
			// create new enrollment
			log.debug("creating new enrollment");
			LearnerEnrollment newEnrollment = new LearnerEnrollment();
			newEnrollment.setCourse(course);
			newEnrollment.setCustomerEntitlement(custEntitlement);
			newEnrollment.setLearner(user.getLearner());
			newEnrollment.setEnrollmentStartDate(new Date(System.currentTimeMillis()));
			newEnrollment.setEnrollmentStatus(LearnerEnrollment.ACTIVE);
			newEnrollment.setEnrollmentEndDate(custEntitlement.getEntitlementEndDate());
			log.debug("calling service to create enrollment");
			enrollment = enrollmentService.addEnrollment(user.getLearner(), custEntitlement, null, newEnrollment);
			log.debug("creating new enrollment complete");
		}
		return enrollment;
	}

	private ModelAndView logErrorAndReturnTemplate(String logStmt,Map<Object, Object> context){
		 log.debug(logStmt);
		 context.put("enrollment_error_text",logStmt); 
		 context.put("enrollment_error", Boolean.TRUE);
		 return new ModelAndView(errorTemplate, "context", context);
	}
	/**
	 * lookup this learner to see if they already exist in LS 360 and have an active enrollment
	   if not, create learner/active enrollment
	   since we are not trusting that 3rd party LMSs will guarantee global uniqueness in studentId,
	   we assume that the studentId is only unique within their system and create a global unique
	   ID in LS 360 based upon AICC-<customerCode>-<studentID>'.  For example:  AICC-VUCUS-8603-88983	 
	 * @param customerCode
	 * @param cust
	 * @param studentID
	 * @param firstName
	 * @param lastName
	 * @return
	 */
	private VU360User getUserForCourseLaunch(String customercode, Customer cust,
			String userName, String firstName, String lastName) {										
		VU360User user=null;
		
		/*STEP 1:SEARCH BY GUID*/
		//for users created thru this controller,username="AICC-"+customercode.toUpperCase()+"-"+student_id
		String GUID= "AICC-"+customercode.toUpperCase()+"-"+userName;
		if(userName.toUpperCase().indexOf("AICC-"+customercode.toUpperCase())==0){
			GUID=userName;
		}
		user= userService.getUserByGUID(GUID);
		this.checkUserAuthentication(user);			
		
		/*STEP 2: SEARCH BY USERNAME*/
		if (user == null){				
			try {
				user = userService.findUserByUserName(userName);
				if(user!=null && user.getLearner().getCustomer().getId() == cust.getId())
				{
					user = updateUserGUID(user,GUID);
				}
				else
				{
					user=null;
				}
			}catch(UsernameNotFoundException ex) {			 			 	
				user= null;
				if(!ex.getMessage().equalsIgnoreCase("FOUND NO SUCH USER NAME")){
			 	   throw ex;
			 	}	    					
			}
		}		
		
		/*STEP 3:SEARCH BY FIRST/LAST NAME*/
		if(user == null){
			// TODO:  try looking up the student by first and last name.  If found update their GUID to 
			//        be the correct one to be found in above logic.
			List<VU360User> results = userService.getUserByFirstNameAndLastName(cust, firstName, lastName); 			
			if (results != null && results.size()!=0  && results.size() == 1 ) {
					user = results.get(0);
					// now update this userGUID to be the one coming from external system, note this may need to change down
					// the road if we ever come across a user that wants to launch from multiple 3rd party systems yet have a 
					// single account in LS 360
					/* TOO:  write code to just update the user guid rather then the existing LMS logic of
					 *       user.deepMerge
					 user.setUserGUID("AICC-"+customer_id.toUpperCase()+"-"+studentID);
					 userService.updateUser(user.getId(), user);
					 user = userService.getUserByGUID("AICC-"+customer_id.toUpperCase()+"-"+studentID);
					 */	
					user = updateUserGUID(user,GUID);
					this.checkUserAuthentication(user);					
			}
		 }														
		
		/*STEP 4:CREATE NEW USER*/
		if (user == null) {
			user = createNewUser(cust,firstName, lastName,GUID);
		}		
			
	 return user;
	}

	private void checkUserAuthentication(VU360User user){
		if (user != null) {
			userService.checkForPermission(user);
		}
	}
	private VU360User updateUserGUID(VU360User user,String GUID) {
		user= userService.loadForUpdateVU360User(user.getId());
		user.setUserGUID(GUID);
		return userService.updateUser(-1, user);		
	}
	
	private VU360User createNewUser(Customer cust,String firstName, String lastName,String GUID) {
		VU360User user;
		// TODO:  later we will be able to limit the number of registered users so we will need to add
		//        logic here to determine if we are able to add a new student
		// cannot find learner anywhere, create new learner in LS 360
		Learner learner = new Learner();
		VU360User newUser = new VU360User();
		newUser.setUserGUID(GUID);
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setUsername(GUID);
		newUser.setEmailAddress(GUID);
		newUser.setPassword(GUID);  // TODO:  change me to a password generator
		//newUser.setAccountNonLocked(false); // by default lock the account so user cannot login using this account;
		//newUser.setEnabled(false); // disable the account so user cannot login using this account;
		
		learner.setCustomer(cust);
		LearnerProfile lp = new LearnerProfile();
		Address addr1 = new Address();
		Address addr2 = new Address();
		lp.setLearnerAddress(addr1);
		lp.setLearnerAddress2(addr2);
		learner.setLearnerProfile(lp);
		lp.setLearner(learner);
		//LearnerPreferences learnPref = new LearnerPreferences();
		//learnPref.setLearner(learner);
		//learner.setPreference(learnPref);
		//learnPref.updateValuesByCustomerPreferences(cust.getCustomerPreferences());
		learner.setVu360User(newUser);
		newUser.setLearner(learner);
		newUser.setCreatedDate(new Date(System.currentTimeMillis()));
		newUser.setLastUpdatedDate(new Date(System.currentTimeMillis()));
							
		LMSRole lrnRole = securityService.getLearnerRoleTemplateByCustomer(cust);
		OrganizationalGroup rootOrgGroup = groupService.getRootOrgGroupForCustomer(cust.getId());
		
		// everyone is a learner
		newUser.addLmsRole(lrnRole);
		learner.setCustomer(cust);
		newUser.setLearner(learner);
		List<OrganizationalGroup> groups = new ArrayList<OrganizationalGroup>();
		groups.add(rootOrgGroup);
		learner = learnerService.addLearner(cust, groups, null, learner);
		user = userService.getUserByGUID(GUID);
		return user;
	}
	/**
	 * @return the customerService
	 */
	public CustomerService getCustomerService() {
		return customerService;
	}

	/**
	 * @param customerService the customerService to set
	 */
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	/**
	 * @return the entitlementService
	 */
	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	/**
	 * @param entitlementService the entitlementService to set
	 */
	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	/**
	 * @return the courseService
	 */
	public CourseAndCourseGroupService getCourseService() {
		return courseService;
	}

	/**
	 * @param courseService the courseService to set
	 */
	public void setCourseService(CourseAndCourseGroupService courseService) {
		this.courseService = courseService;
	}

	/**
	 * @return the userService
	 */
	public VU360UserService getUserService() {
		return userService;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(VU360UserService userService) {
		this.userService = userService;
	}

	/**
	 * @return the enrollmentService
	 */
	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	/**
	 * @param enrollmentService the enrollmentService to set
	 */
	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

	/**
	 * @return the learnerService
	 */
	public LearnerService getLearnerService() {
		return learnerService;
	}

	/**
	 * @param learnerService the learnerService to set
	 */
	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	/**
	 * @return the securityService
	 */
	public SecurityAndRolesService getSecurityService() {
		return securityService;
	}

	/**
	 * @param securityService the securityService to set
	 */
	public void setSecurityService(SecurityAndRolesService securityService) {
		this.securityService = securityService;
	}

	/**
	 * @return the groupService
	 */
	public OrgGroupLearnerGroupService getGroupService() {
		return groupService;
	}

	/**
	 * @param groupService the groupService to set
	 */
	public void setGroupService(OrgGroupLearnerGroupService groupService) {
		this.groupService = groupService;
	}

	/**
	 * @return the aiccHandler
	 */
	public ExternalStatisticsProcessor getAiccHandler() {
		return aiccHandler;
	}

	/**
	 * @param aiccHandler the aiccHandler to set
	 */
	public void setAiccHandler(ExternalStatisticsProcessor aiccHandler) {
		this.aiccHandler = aiccHandler;
	}

	/**
	 * @return the statsService
	 */
	public StatisticsService getStatsService() {
		return statsService;
	}

	/**
	 * @param statsService the statsService to set
	 */
	public void setStatsService(StatisticsService statsService) {
		this.statsService = statsService;
	}

	/**
	 * @return the errorTemplate
	 */
	public String getErrorTemplate() {
		return errorTemplate;
	}

	/**
	 * @param errorTemplate the errorTemplate to set
	 */
	public void setErrorTemplate(String errorTemplate) {
		this.errorTemplate = errorTemplate;
	}

	/**
	 * @return the launchTemplate
	 */
	public String getLaunchTemplate() {
		return launchTemplate;
	}

	/**
	 * @param launchTemplate the launchTemplate to set
	 */
	public void setLaunchTemplate(String launchTemplate) {
		this.launchTemplate = launchTemplate;
	}

	/**
	 * @return the statsProcesed
	 */
	public String getStatsProcesed() {
		return statsProcesed;
	}

	/**
	 * @param statsProcesed the statsProcesed to set
	 */
	public void setStatsProcesed(String statsProcesed) {
		this.statsProcesed = statsProcesed;
	}
}