package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.softech.vu360.lms.model.Distributor;


/**
 * 
 * @author haider.ali
 * created date 16-DEC-2015
 */
public interface DistributorRepositoryCustom {

	public List<Distributor> getAllowedDistributorByGroupId(String groupId, String lmsAdminId);
	public void deleteDistributorByGroupIdAndAdministratorIdAndAllowedDistributorId(String groupId, String administratorId, String allowedDistributorId);
	
	public Page<Distributor> findDistributorsByName(RepositorySpecificationsBuilder<Distributor> repositorySpecificationsBuilder, PageRequest pageRequest);
	

	

}
