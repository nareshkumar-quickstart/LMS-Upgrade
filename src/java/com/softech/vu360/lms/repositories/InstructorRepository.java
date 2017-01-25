package com.softech.vu360.lms.repositories;

import java.util.List;
import java.util.Vector;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Instructor;

public interface InstructorRepository extends CrudRepository<Instructor, Long>, InstructorRepositoryCustom {
	
	public List<Instructor> findByIdNotInAndFirstNameAndLastNameAndContentOwnerId(Vector<Long> instructorIds, String firstName, String lastName, Long contentOwnerId);
	
	
}
