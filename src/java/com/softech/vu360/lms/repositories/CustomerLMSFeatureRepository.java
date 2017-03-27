package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerLMSFeature;

/**
 * 
 * @author haider.ali
 * @Kaunain - added methods
 *
 */
public interface CustomerLMSFeatureRepository extends
		CrudRepository<CustomerLMSFeature, Long> {

	CustomerLMSFeature findById(Long id);
	
	List<CustomerLMSFeature> findByCustomer(Customer customer);
	
	public List<CustomerLMSFeature> findByCustomerAndLmsFeature_RoleTypeOrderByLmsFeature_FeatureCode(
			Customer customer, String roleType);
	
	//@Cacheable(value = "getDisabledCustomerLMSFeatures", key = "#p0")
	@Query("select p from CustomerLMSFeature p INNER JOIN FETCH p.lmsFeature f where p.customer.id=:custId and p.enabled=0 ")
	public List<CustomerLMSFeature> findByCustomerAndEnabledFalse(@Param("custId") Long custId);
	
	public List<CustomerLMSFeature> getAllByCustomerAndEnabledTrue(Customer customer);
	
	@Override
	//@CacheEvict(value = "getDisabledCustomerLMSFeatures", key = "#p0.customer.id")
	public void delete(CustomerLMSFeature customerLMSFeature);
	
	@Override
	//@CacheEvict(value = "getDisabledCustomerLMSFeatures", allEntries = true)
	public void delete(Long id);
	
	@Override
	//@CacheEvict(value = "getDisabledCustomerLMSFeatures", key = "#p0[0].customer.id")
	public void delete(Iterable<? extends CustomerLMSFeature> customerLMSFeature);
	
	@Override
	//@CacheEvict(value = "getDisabledCustomerLMSFeatures", allEntries = true)
	void deleteAll();

	@Override
	//@CacheEvict(value = "getDisabledCustomerLMSFeatures", allEntries = true)
	public <S extends CustomerLMSFeature> S save(S entity);
	
	public CustomerLMSFeature findByLmsFeatureFeatureCodeEqualsAndCustomerIdEquals(String featureCode, Long customerID);
	
}