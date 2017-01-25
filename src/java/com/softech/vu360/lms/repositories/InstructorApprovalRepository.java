package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.InstructorApproval;

public interface InstructorApprovalRepository extends CrudRepository<InstructorApproval, Long> {

	@Query("SELECT ia FROM  InstructorApproval ia WHERE UPPER(ia.approvedInstructorName) LIKE %:approvedInstructorName% "
			+ "AND UPPER(ia.instructorApprovalNumber) LIKE %:instructorApprovalNumber% AND ia.deleted=FALSE "
			+ "AND ia.active=:isActive  ")
	List<InstructorApproval> findInstructorApprovals(@Param("approvedInstructorName") String approvedInstructorName, @Param("instructorApprovalNumber") String instructorApprovalNumber, 
			@Param("isActive") Boolean isActive);
	
	List<InstructorApproval> findByApprovedInstructorNameLikeIgnoreCaseAndInstructorApprovalNumberLikeIgnoreCaseAndDeletedIsFalse(@Param("approvedInstructorName") String approvedInstructorName, @Param("instructorApprovalNumber") String instructorApprovalNumber);
	
	List<InstructorApproval> findByRegulatorCategoryIdIn(List<Long> regulatroCategoryIds);

	InstructorApproval findByIdAndDeletedFalse(Long instructorApprovalId); 
	
}
