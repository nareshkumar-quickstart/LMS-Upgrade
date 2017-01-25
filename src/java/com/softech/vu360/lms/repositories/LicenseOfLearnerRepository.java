package com.softech.vu360.lms.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.LicenseOfLearner;

public interface LicenseOfLearnerRepository extends CrudRepository<LicenseOfLearner, Long> {
	List<LicenseOfLearner> findByIdIn(Long[] licenseOfLearnerId);
	List<LicenseOfLearner> findByActiveTrueAndLearnerId(Long learnerId);
	List<LicenseOfLearner> findByLearnerId(Long learnerId);
	List<LicenseOfLearner> findByActiveTrueAndLearnerIdAndIndustryCredential_Credential_OfficialLicenseNameLike(Long learnerId, String licensename);
	
	/**
	 * This new overloaded method has been added inorder to avoid too many (more than 2100) learner Ids using 'IN' clause and to resolve 'StackOverflow Exception' 
	 * 
	 */
	@Query(value = " SELECT new map ( C.officialLicenseName as OFFICIALLICENSENAME , C.id as ID, LL.learner.id as LEARNER_ID) FROM LicenseOfLearner LL  "+
			 	   " JOIN LL.industryCredential IC "+
			 	   " JOIN IC.credential C " +
			 	   " JOIN LL.learner L "+
				   " WHERE " + 
				   " LL.active = true  AND L.customer.id = :customerId")  
	List<Map<Object, Object>> findLicenseNameByLearnerIds(@Param("customerId") Long customerId);
	
	@Query(value = " SELECT new map ( C.officialLicenseName as OFFICIALLICENSENAME , C.id as ID, LL.learner.id as LEARNER_ID) FROM LicenseOfLearner LL  "+
		 	   " JOIN LL.industryCredential IC "+
		 	   " JOIN IC.credential C " +
		 	   " JOIN LL.learner L "+
		 	   " JOIN L.vu360User VU "+
			   " WHERE " + 
			   " LL.active = true  AND L.customer.id = :customerId AND UPPER(VU.firstName) LIKE %:firstName% AND UPPER(VU.lastName) LIKE %:lastName% AND UPPER(VU.emailAddress) LIKE %:emailAddress%")  
	List<Map<Object, Object>> findLicenseNameByLearnerIds(@Param("customerId") Long customerId, @Param("firstName") String firstName, @Param("lastName") String lastName,@Param("emailAddress") String emailAddress);
	
	@Query(value = " SELECT new map ( LL.id as ID, LL.id as LICENSE_ID ) from LearnerLicenseAlert LA join LA.learnerlicense LL " +
				   " WHERE " +
				   " LL.active = true AND LA.isDelete = false AND LL.id IN  ( :leranerlicenseids ) ")  
	List<Map<Object, Object>> findByLearnerLicenseIds(@Param("leranerlicenseids") List<Long> leranerlicenseids);
}


