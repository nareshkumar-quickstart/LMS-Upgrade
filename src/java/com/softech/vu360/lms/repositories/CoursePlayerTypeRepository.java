/**
 * 
 */
package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.CoursePlayerType;

/**
 * @author marium.saud
 *
 */
public interface CoursePlayerTypeRepository extends CrudRepository<CoursePlayerType, Long> {
	
	CoursePlayerType findByCourseId(Long courseId);

}
