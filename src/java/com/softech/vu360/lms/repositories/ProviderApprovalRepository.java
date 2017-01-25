package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.ProviderApproval;

public interface ProviderApprovalRepository extends CrudRepository<ProviderApproval, Long> {
	@Query("SELECT pa FROM  ProviderApproval pa WHERE UPPER(pa.approvedProviderName) LIKE %:approvedProviderName% "
			+ "AND UPPER(pa.providerApprovalNumber) LIKE %:providerApprovalNumber% AND pa.deleted=FALSE "
			+ "AND pa.active=:isActive  ")
	List<ProviderApproval> findProviderApprovals(@Param("approvedProviderName") String approvedProviderName, @Param("providerApprovalNumber") String providerApprovalNumber, 
			@Param("isActive") Boolean isActive);
	
	List<ProviderApproval> findByApprovedProviderNameLikeIgnoreCaseAndProviderApprovalNumberLikeIgnoreCaseAndDeletedIsFalse(@Param("approvedProviderName") String approvedProviderName, @Param("providerApprovalNumber") String providerApprovalNumber);

	ProviderApproval findByIdAndDeletedFalse(Long providerApprovalId);
}
