/**
 * 
 */
package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.AICCAssignableUnit;

/**
 * @author marium.saud
 *
 */
public interface AICCAssignableUnitRepository extends CrudRepository<AICCAssignableUnit, Long> {
	
	AICCAssignableUnit findByCourseId(Long courseId);
	

}
