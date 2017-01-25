package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.CourseRestrictedIP;

public interface CourseRestrictedIPRepository extends CrudRepository<CourseRestrictedIP, Long>, CourseRestrictedIPRepositoryCustom {

	
	
}
