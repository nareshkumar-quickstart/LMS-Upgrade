package com.softech.vu360.lms.rest;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softech.vu360.lms.helpers.LoginSecurityHelper;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.ActiveDirectoryService;
import com.softech.vu360.lms.service.VU360UserService;

@Controller
@RequestMapping(value = "/userEvents")
public class UserEventService {

	@Inject
	private ActiveDirectoryService activeDirectoryService = null;
	
	@Inject
	private VU360UserService userService  = null;
	
	
	@RequestMapping(value = "/update", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String changePassword(@RequestParam("username") String username,
			@RequestParam("updatedValue") String password) {
		
		
		if(username==null || username.equals("") || password==null || password.equals(""))
			return "FAIL";
		
	
		VU360User dbUser = userService.findUserByUserName(username);
		try{
			if (dbUser != null && activeDirectoryService.isADIntegrationEnabled()){ // if success and AD is enabled
				// AND if any of the AD field is changed
				dbUser.setPassword(password);// we require plain password in AD
				dbUser.setPassWordChanged(true);
				activeDirectoryService.updateUser(dbUser);// edit user to AD
				

				com.softech.vu360.lms.vo.VU360User voUser = new com.softech.vu360.lms.vo.VU360User();
				voUser.setId(dbUser.getId());
				voUser.setPassword(dbUser.getPassword());
				voUser.setUserGUID(dbUser.getUserGUID());
				
				String encodedPassword = LoginSecurityHelper.getEncryptedPassword(voUser);
				dbUser.setPassword(encodedPassword);
				dbUser = userService.updateUser(dbUser.getId(), dbUser);	
			}
		}catch(Exception ex){
			return "FAIL";
		}
		return "PASSED";
	}
	

}
