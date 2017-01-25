/**
 * 
 */
package com.softech.vu360.lms.service;

import com.softech.vu360.lms.model.CoursePlayerType;

/**
 * @author marium.saud
 *
 */
public interface CoursePlayerTypeService {
	
	public CoursePlayerType findCoursePlayerTypeByCourseId(Long courseId);

}
