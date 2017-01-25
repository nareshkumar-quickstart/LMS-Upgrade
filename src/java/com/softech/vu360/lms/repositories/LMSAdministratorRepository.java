package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.LMSAdministrator;
import com.softech.vu360.lms.model.Learner;

/**
 * 
 * @author haider.ali
 *
 */
public interface LMSAdministratorRepository extends CrudRepository<LMSAdministrator, Long>, LMSAdministratorRepositoryCustom   {

	public LMSAdministrator findBydistributorGroupsId(Long id);
	public List<LMSAdministrator> findByIdIn(List<Long> id);
	public List<LMSAdministrator> findByDistributorGroups_IdIn(List<Long> id);
	public List<LMSAdministrator> findByDistributorGroupsDistributorsInAndGlobalAdministratorFalseAndVu360UserFirstNameLikeOrVu360UserLastNameLikeOrVu360UserEmailAddressLike(List<Distributor> distributors, String firstName, String lastName, String email);
	public List<LMSAdministrator> findByGlobalAdministratorFalseAndVu360UserFirstNameLikeOrVu360UserLastNameLikeOrVu360UserEmailAddressLike(String firstName, String lastName, String email);

	

}