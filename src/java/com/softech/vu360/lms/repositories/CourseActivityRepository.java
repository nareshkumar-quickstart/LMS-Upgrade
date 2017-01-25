package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.CourseActivity;
import com.softech.vu360.lms.model.Location;

public interface CourseActivityRepository extends CrudRepository<CourseActivity, Long>{
	
	public List<CourseActivity> findByIdIn(long[] ids);
	public List<CourseActivity> findByGradeBookId(Long gradeBookId);
	
	public void deleteByIdIn(long[] courseActivityIdArray);
	
	@Query("SELECT ca FROM  CourseActivity ca WHERE ca.activityName LIKE %:activityName% ")
	public List<CourseActivity> findActivityNameLike(@Param("activityName") String activityName);

}
