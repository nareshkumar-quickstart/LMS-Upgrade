package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;
import com.softech.vu360.lms.model.lmsapi.LmsApiCustomer;

/**
 * Repository interface for {@code LmsApiCustomer}s.
 * 
 * @author muhammad.rehan
 * 
 */
public interface LmsApiCustomerRepository extends CrudRepository<LmsApiCustomer, Long> {
	
	public LmsApiCustomer findLmsApiByCustomerId(long distributorId) throws Exception;
	public LmsApiCustomer findByApiKey(String key) throws Exception;
	
}
