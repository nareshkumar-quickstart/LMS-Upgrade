package com.softech.vu360.lms.web.filter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.softech.vu360.lms.model.ActiveDirectoryUser;
import com.softech.vu360.lms.service.ActiveDirectoryService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.util.VU360Properties;



public class CustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	
	  private static Logger log = Logger.getLogger(CustomAuthenticationProvider.class);
	  private UserDetailsService userDetailsService;
	  private ActiveDirectoryService activeDirectoryService = null;	
	  private LearnerService learnerService;
	  
	    private SaltSource saltSource;
	    private PasswordEncoder passwordEncoder;
		private static final String adEnabled = VU360Properties.getVU360Property("ad.integration.enabled");		
	    private static final String DISTRUBTOR_CODE_MEGASITE = VU360Properties.getVU360Property("lms.distributor.code.megasite"); 

		@Override
		protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
            // changed here----------------------------------------------
               Object salt = null;
             com.softech.vu360.lms.vo.VU360User vU360User = null;
             boolean isUserExist = false;
             if (saltSource != null) {
               vU360User = (com.softech.vu360.lms.vo.VU360User)userDetails;
               salt = saltSource.getSalt(vU360User);
             }
               
             //String encodedPassword = passwordEncoder.encodePassword(authentication.getCredentials().toString(), salt);
             log.info("CustomAuthenticationProvider.additionalAuthenticationChecks()--------------Start");
             
             // if user's password exists encrypted in DB
             if (passwordEncoder.isPasswordValid(userDetails.getPassword(), authentication.getCredentials().toString(), salt)) {
               isUserExist = true;
             }
             
             // if user's password exists in plan text in DB
             if(userDetails.getPassword().equalsIgnoreCase(authentication.getCredentials().toString())){
               isUserExist = true;
             }
             
             log.info("CustomAuthenticationProvider.additionalAuthenticationChecks()--------------End");
             
             if(!isUserExist){
               /*throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials",
                   "Bad credentials"), userDetails);*/
               throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials","Bad credentials"));
             }
             
    }

		
		@Override
		protected UserDetails retrieveUser(String username,	UsernamePasswordAuthenticationToken authentication)	throws AuthenticationException  {
			UserDetails userDtl = null;
			com.softech.vu360.lms.vo.VU360User vUser = null;
			log.debug("In retrieveUser Method");
	        String password = authentication.getCredentials().toString();
	        
	        try {
	        	
				if (StringUtils.isBlank(username)) {
					log.error("Blank User Id");
					throw new BadCredentialsException(
							messages.getMessage("No user detail found from DB", "Bad credentials"));
				}
				userDtl = userDetailsService.loadUserByUsername(username);
				vUser = (com.softech.vu360.lms.vo.VU360User)userDtl;
				
				if (adEnabled.equals("true")) {
	
					log.info("AD is enabled");
	
					if (vUser != null && vUser.getLearner().getCustomer().isAiccInterfaceEnabled()) {
						return userDtl;
					}
	
	        		/**
	        		 * For CAS,
	        		 * By pass this also, when Learner 
	        		 * or Manager belongs to MegaSite
	        		 */
	        		if(
	    				(vUser.isLearnerMode() || vUser.isManagerMode()) &&
	    				(
	    						(vUser.getLearner() != null) && 
	    						(vUser.getLearner().getCustomer() != null) && 
	    						(vUser.getLearner().getCustomer().getDistributor() != null) &&
	    						(vUser.getLearner().getCustomer().getDistributor().getDistributorCode() != null) &&
	    						(vUser.getLearner().getCustomer().getDistributor().getDistributorCode().isEmpty()))
						)
			        {
	        		
	        		boolean isAuthenticated = activeDirectoryService.authenticateADUser(username, password);
		        	

	        		if(!isAuthenticated) {//User does not exist in AD
	        			
		        		boolean findUserInAD = activeDirectoryService.findADUser(username);	        			
	        			
		        		if(userDtl==null || findUserInAD){
		            		log.error("No user detail found from DB");
		            		
		            		  /*throw new BadCredentialsException(messages.getMessage("No user detail found from DB",
		                              "Bad credentials"), userDtl);*/
		            		  throw new BadCredentialsException(messages.getMessage("No user detail found from DB","Bad credentials"));
		        		}
		        		
		        		//verify user password in Database and then add to AD if password matches otherwise throw exception
		        		if (!isCorrectPassword(vUser.getPassword(), authentication.getCredentials().toString(), vUser)){
		        			log.error("Username and password do not match in DB");
		            		
		            		  /*throw new BadCredentialsException(messages.getMessage("Username and password do not match in DB",
		                              "Bad credentials"), userDtl);*/
		        			throw new BadCredentialsException(messages.getMessage("No user detail found from DB","Bad credentials"));
		        		}
		        		
			        		com.softech.vu360.lms.vo.VU360User user = getVU360UserObjectForAD((com.softech.vu360.lms.vo.VU360User)userDtl);
			        		
			        		
			        		try{
			        			user.setPassword(password);//Because the password is encrypted
			        						        			
				        		ActiveDirectoryUser directoryUser = activeDirectoryService.addUser(user); //Add user in AD
			        			
								if(directoryUser== null) {//User not created
									log.error("User not created on AD: "+user.getUsername());
					    			return userDtl;
					        	}
			        			
			        		}catch(Exception e){
			        			log.error("User not created on AD: "+user.getUsername());
			        			return userDtl;
			        		}		        		
		        		
		        	}else{// code execute when user exist on Active Directory
		        		
		        			
		        		
		        		    com.softech.vu360.lms.vo.VU360User objUser = (com.softech.vu360.lms.vo.VU360User) userDtl;
		        			 
		        		    // if both AD and DB passwords are not same
		        			if(! isCorrectPassword(objUser.getPassword(), authentication.getCredentials().toString(), objUser))
		        			{
		        				// update password in DB to sync both AD and DB
		        				objUser.setPassword( encriptPassword(password, objUser)  );
		        				//userDtl = learnerService.updateUser(objUser); // TO DO: 2016-09-04
		        			}
	        		}
		        }
		        else{
		        	//userDtl = userDetailsService.loadUserByUsername(username);
	        		if(userDtl==null){
	            		log.error("No user detail found from DB");
	            		throw  new BadCredentialsException("No user detail found from DB");
	        		}
	        		userDtl = vUser;
	        		return userDtl;
		        }
	        }
	        }
	        catch(AuthenticationException e) {
                log.error(e.getMessage());
                throw e; // Log here and send email message saying error
            }
	        
	        userDtl = vUser;
	        return userDtl;
		}
	    
		
		boolean isCorrectPassword(String pass1, String pass2, com.softech.vu360.lms.vo.VU360User objUser){
			Object salt = null;
			  boolean isUserExist = false;
			  
			  if (saltSource != null){ 
				  	
	               salt = saltSource.getSalt(objUser);
			  }
			 
			  if (passwordEncoder.isPasswordValid(pass1, pass2, salt)) {
				 isUserExist = true;
             
             // if user's password exists in plan text in DB
			 }else if(pass1.equalsIgnoreCase(pass2))
            	 isUserExist = true;
             
             return isUserExist;
		}	
		
		
		public String encriptPassword(String password, com.softech.vu360.lms.vo.VU360User objUser){
			Object salt = null;
			
			if (saltSource != null) {
	               salt = saltSource.getSalt(objUser);
			}
			return passwordEncoder.encodePassword(password, salt);
		}
		
		public LearnerService getLearnerService() {
			return learnerService;
		}


		public void setLearnerService(LearnerService learnerService) {
			this.learnerService = learnerService;
		}


		public UserDetailsService getUserDetailsService() {
			return userDetailsService;
		}

		public void setUserDetailsService(UserDetailsService userDetailsService) {
			this.userDetailsService = userDetailsService;
		}

		public SaltSource getSaltSource() {
			return saltSource;
		}

		public void setSaltSource(SaltSource saltSource) {
			this.saltSource = saltSource;
		}

		public ActiveDirectoryService getActiveDirectoryService() {
			return activeDirectoryService;
		}

		public void setActiveDirectoryService(
				ActiveDirectoryService activeDirectoryService) {
			this.activeDirectoryService = activeDirectoryService;
		}
		
	    public PasswordEncoder getPasswordEncoder() {
			return passwordEncoder;
		}

		public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
			this.passwordEncoder = passwordEncoder;
		}
		
		private com.softech.vu360.lms.vo.VU360User getVU360UserObjectForAD(com.softech.vu360.lms.vo.VU360User inUserObj){
			com.softech.vu360.lms.vo.VU360User user = new com.softech.vu360.lms.vo.VU360User();
			try{
				user.setEmailAddress(inUserObj.getEmailAddress());
				user.setFirstName(inUserObj.getFirstName());
				user.setLastName(inUserObj.getLastName());
				user.setMiddleName(inUserObj.getMiddleName());
				user.setPassword(inUserObj.getPassword());
				user.setUsername(inUserObj.getUsername());
				user.setFirstName(inUserObj.getFirstName());
				user.setMiddleName(inUserObj.getMiddleName());
				user.setLastName(inUserObj.getLastName());
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return user;
		}
}
