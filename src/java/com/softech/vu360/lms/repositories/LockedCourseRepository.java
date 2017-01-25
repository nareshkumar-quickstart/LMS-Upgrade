/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;
import java.util.Vector;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.LockedCourse;

/**
 * @author marium.saud
 *
 */
public interface LockedCourseRepository extends CrudRepository<LockedCourse, Long> {

	public List<LockedCourse> findByIdIn(Vector<Long> statusList);
	public LockedCourse findFirstByEnrollmentIdAndCourselockedTrueOrderByIdDesc(Long lockedCourseId);
}
