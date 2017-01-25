package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.InstructorSynchronousClass;

public interface InstructorSynchronousClassRepository extends
		CrudRepository<InstructorSynchronousClass, Long> {

	public List<InstructorSynchronousClass> findBySynchronousClassIdAndInstructorType(
			Long synchronousClassId, String instructorType);

	public List<InstructorSynchronousClass> findBySynchronousClassId(
			Long synchronousClassId);

	public List<InstructorSynchronousClass> findByInstructorIdAndSynchronousClassCourseRetired(
			Long instructorId, boolean retired);

	/* Methods moved from SynchronousClassDao */
	public InstructorSynchronousClass findBySynchronousClassIdAndInstructorId(
			Long synchronousClassId, Long instructorId);

	@Query("Select p from InstructorSynchronousClass p WHERE p.synchronousClass.id = :id AND p.instructorType='Lead'")
	public List<InstructorSynchronousClass> getLeadInstructorsOfSynchClass(
			@Param("id") Long id);

	@Query("Select p from InstructorSynchronousClass p WHERE p.synchronousClass.id = :synchClassId AND p.instructor.user.firstName LIKE %:firstName% AND p.instructor.user.lastName LIKE %:lastName% AND p.instructorType = :instType")
	public List<InstructorSynchronousClass> findSynchronousClassInstructors(
			@Param("synchClassId") Long synchClassId,
			@Param("firstName") String firstName,
			@Param("lastName") String lastName,
			@Param("instType") String instType);
	public List<InstructorSynchronousClass> findByInstructorId(Long instructorId);
	
	public Integer deleteByIdIn(List<long[]> l);

}
