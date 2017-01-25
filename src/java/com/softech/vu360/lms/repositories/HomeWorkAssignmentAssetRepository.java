package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.HomeWorkAssignmentAsset;

public interface HomeWorkAssignmentAssetRepository extends CrudRepository<HomeWorkAssignmentAsset, Long> {
	//public List<HomeWorkAssignmentAsset> getUploadedHomeWorkDocuments(Long courseId)
	@Query("SELECT r FROM  #{#entityName} r WHERE r.homeWorkAssignmentCourse.id =:ID")
	List<HomeWorkAssignmentAsset> findByHomeWorkAssignmentCourseId(@Param("ID") Long courseId);
	
	void deleteById(Long id);
	
	
}
