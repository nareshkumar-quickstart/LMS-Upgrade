package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.WebLinkCourse;

public interface WebLinkCourseRepository  extends CrudRepository<WebLinkCourse, Long> {
	List<WebLinkCourse> findByCourseTitleIgnoreCaseLikeAndContentOwner_Customer(String courseTitle,Customer customer);
	
	//public void deleteCustomCourse(long customCourseIdArray[])
	void deleteByIdIn(Long[] customCourseIdArray);
	List<WebLinkCourse> findByIdIn(Long[] customCourseIdArray);
}
