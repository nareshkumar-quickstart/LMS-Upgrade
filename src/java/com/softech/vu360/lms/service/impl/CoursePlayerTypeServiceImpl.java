/**
 * 
 */
package com.softech.vu360.lms.service.impl;

import javax.inject.Inject;

import com.softech.vu360.lms.model.CoursePlayerType;
import com.softech.vu360.lms.repositories.CoursePlayerTypeRepository;
import com.softech.vu360.lms.service.CoursePlayerTypeService;

/**
 * @author marium.saud
 *
 */
public class CoursePlayerTypeServiceImpl implements CoursePlayerTypeService {

	@Inject
	CoursePlayerTypeRepository coursePlayerTypeRepository;

	@Override
	public CoursePlayerType findCoursePlayerTypeByCourseId(Long courseId) {
			return coursePlayerTypeRepository.findByCourseId(courseId);
	}

}
