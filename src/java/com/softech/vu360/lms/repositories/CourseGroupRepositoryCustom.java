package com.softech.vu360.lms.repositories;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.Distributor;

public interface CourseGroupRepositoryCustom {
	
	
	public List<Map<Object, Object>> findByAvailableCourseGroups(Distributor distributor, String courseName, String entityId,	String keywords);
	
	public List<Map<Object, Object>> findByAvailableCourseGroups(Distributor distributor, List<Long> courseGroudIdList);

	public List<CourseGroup> findCoursesAndCourseGroupsByDistributor(
			Distributor distributor, String courseName, String entityId,
			String keywords, String contractType);
	
	public List<CourseGroup> getCourseGroupsByDistributor(Long distributorId);

	public List<CourseGroup> getAllChildCourseGroupsForCourseGroups(List<CourseGroup> courseGroups);	
	
	public List<Map<Object, Object>> findCourseGroups(String courseName, String entityId, String keywords);
	
	
}
