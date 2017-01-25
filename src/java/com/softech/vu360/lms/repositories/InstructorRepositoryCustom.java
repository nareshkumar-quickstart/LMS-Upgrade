package com.softech.vu360.lms.repositories;

import java.util.List;
import java.util.Vector;

import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.vo.VU360User;

public interface InstructorRepositoryCustom {
	List<Instructor> findInstructorByfirstNamelastNameemailAddress(String firstName, String lastName, String emailAddress,String sortBy, String sortDirection);
	List<Instructor> findInstructor(String firstName, String lastName, String emailAddress, VU360User user, String sortBy, String sortDirection);
	List<Instructor> findInstructorByContentIdfirstNamelastName(Vector<Long> vctrInstructorIds, String firstName, String lastName, Long contentOwnerId, int pageIndex, int pageSize, int sortDirection, String sortColumn );
	List<Instructor> searchInstructorByFirstName(String firstName);
}
