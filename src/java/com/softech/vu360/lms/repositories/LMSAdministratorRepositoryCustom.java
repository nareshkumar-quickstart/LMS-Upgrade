package com.softech.vu360.lms.repositories;

import java.util.List;

import com.softech.vu360.lms.model.LMSAdministrator;
import com.softech.vu360.lms.model.LMSAdministratorAllowedDistributor;
import com.softech.vu360.lms.model.VU360User;

/**
 * 
 * @author haider.ali
 *
 */
public interface LMSAdministratorRepositoryCustom {

	public void assignLMSAdministratorsToDistributorGroup(List<LMSAdministratorAllowedDistributor> allowedResellerOfSelectedGroup) ;
	public List<LMSAdministrator> findLMSAdministrators(String firstName,String lastName,String email,VU360User loggedInUser);




}
