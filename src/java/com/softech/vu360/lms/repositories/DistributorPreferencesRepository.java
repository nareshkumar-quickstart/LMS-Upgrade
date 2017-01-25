package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.DistributorPreferences;

/**
 * 
 * @author haider.ali
 *
 */
public interface DistributorPreferencesRepository extends CrudRepository<DistributorPreferences, Long> {
	
	//public DistributorGroup findByName(String name);
}
