package com.softech.vu360.lms.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.TimeZone;
/**
 * 
 * @author marium.saud
 *
 */
public interface TimeZoneRepository extends CrudRepository<TimeZone, Integer> {

	TimeZone findByZone(String zoneName);
	
	@Query(value = "SELECT * FROM TIMEZONE TZ, LEARNERPROFILE LF WHERE TZ.ID = LF.TIMEZONE_ID AND LF.ID = :learnerProfileId", nativeQuery = true)
	TimeZone findByLearnerProfileId(@Param("learnerProfileId")Long learnerProfileId);
	
	
}
