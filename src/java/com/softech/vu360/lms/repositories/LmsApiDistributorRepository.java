package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;
import com.softech.vu360.lms.model.lmsapi.LmsApiDistributor;

/**
 * Repository interface for {@code LmsApiDistributor}s.
 * 
 * @author muhammad.rehan
 * 
 */
public interface LmsApiDistributorRepository  extends CrudRepository<LmsApiDistributor, Long> {
	
	LmsApiDistributor findLmsApiByDistributorId(long distributorId) throws Exception;
	LmsApiDistributor findByApiKey(String key) throws Exception;
	
}
