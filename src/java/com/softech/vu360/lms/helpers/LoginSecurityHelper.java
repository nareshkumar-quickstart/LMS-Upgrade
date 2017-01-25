package com.softech.vu360.lms.helpers;

import java.security.InvalidParameterException;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import com.softech.vu360.lms.vo.VU360User;
import com.softech.vu360.lms.web.controller.manager.ApplicationContextProvider;

/**
 * @author Raja Wajahat Ali
 */
public class LoginSecurityHelper {
	
	private static final Logger log = Logger.getLogger(LoginSecurityHelper.class.getName());
	private static ShaPasswordEncoder passwordEncoder = (ShaPasswordEncoder) ApplicationContextProvider.getApplicationContext().getBean("passwordEncoder");
	private static SaltSource saltSource = (SaltSource) ApplicationContextProvider.getApplicationContext().getBean("saltSource");
	
	public static String getEncryptedPassword(VU360User vu360User) {
		Object salt;
		String pwd; 
		if(vu360User!=null && vu360User.getPassword()!=null && vu360User.getUserGUID()!=null){
			salt = saltSource.getSalt(vu360User); // salt is the GUID
			pwd = passwordEncoder.encodePassword(vu360User.getPassword(), salt);
			return pwd;
		}
		else{
			throw new InvalidParameterException();
		}
	}

}