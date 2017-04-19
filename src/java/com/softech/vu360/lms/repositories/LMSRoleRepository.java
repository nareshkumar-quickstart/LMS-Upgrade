package com.softech.vu360.lms.repositories;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LMSRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author marium.saud
 *
 */
public interface LMSRoleRepository extends CrudRepository<LMSRole, Long>, LMSRoleRepositoryCustom {
	public LMSRole findByRoleNameAndOwner(String roleName,Customer customer);
	public List<LMSRole> findByRoleTypeAndOwnerId(String roleType, Long customerId);
	public List<LMSRole> getByOwner(Customer customer);		
	public LMSRole findByOwnerAndRoleTypeAndIsSystemCreatedTrue(Customer customer,String roleType);
	public LMSRole findByOwnerIdAndIsSystemCreatedTrue(Long customerId);
	public List<LMSRole> findByIdIn(@Param(value = "lstRoleIdArray") Long[] lstRoleIdArray);
	
	@Override
	//@CacheEvict(value = "getDisabledCustomerLMSFeatures", key = "#p0.owner.id")
	public void delete(LMSRole lmsRole);
	
	@Override
	//@CacheEvict(value = "getDisabledCustomerLMSFeatures", allEntries = true)
	public void delete(Long id);
	
	@Override
	//@CacheEvict(value = "getDisabledCustomerLMSFeatures", key = "#p0[0].owner.id")
	public void delete(Iterable<? extends LMSRole> lmsRole);
	
	@Override
	//@CacheEvict(value = "getDisabledCustomerLMSFeatures", allEntries = true)
	void deleteAll();

	@Override
	//@CacheEvict(value = "getDisabledCustomerLMSFeatures", allEntries = true)
	public <S extends LMSRole> S save(S entity);


	
}
