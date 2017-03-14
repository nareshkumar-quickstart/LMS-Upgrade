/**
 * 
 */
package com.softech.vu360.lms.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LMSFeature;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.TrainingAdministrator;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.VU360UserNew;

/**
 * @author jason
 *
 */
public interface VU360UserNewService {
	
	public VU360UserNew getSimpleUserById(Long id);
	
	public VU360UserNew updateUser(VU360UserNew updatedUser);
	
	public VU360UserNew getVU360UserNewById(Long id);
	
	public VU360UserNew  loadForUpdateVU360UserNew(Long id);
	
	public VU360UserNew updateNumLogons(VU360UserNew updatedUser);
	
	public VU360UserNew getVU360UserByLearnerId(Long learnerId);

	
}
