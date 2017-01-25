/**
 * 
 */
package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.LearnerPreferences;

/**
 * @author marium.saud
 *
 */
public interface LearnerPreferencesRepository extends CrudRepository<LearnerPreferences, Long> {
	
}
