package com.softech.vu360.lms.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.LearnerLicenseAlert;
								   
/**
 * Repository interface for {@code LearnerLicenseAlert}s.
 * 
 * @author muhammad.rehan
 * 
 */
public interface LearnerLicenseAlertRepository extends CrudRepository<LearnerLicenseAlert, Long> {
	//public List<LearnerLicenseAlert> getLearnerLicenseAlert(long learnerid);
	//public List<LearnerLicenseAlert> getFilteredLearnerLicenseAlert(long learnerid,String alertname)
	@Query("SELECT l FROM  #{#entityName} l join fetch l.learner lr WHERE lr.id =:learnerId and isDelete = false")
	List<LearnerLicenseAlert> findByLearnerIdByAndInActive(@Param("learnerId") Long learnerId);
	
	//public List<LearnerLicenseAlert> getFilteredLearnerLicenseAlert(long learnerid,String alertname)
	@Query("SELECT l FROM  #{#entityName} l join fetch l.learner lr WHERE lr.id =:learnerId AND isDelete = false AND l.alertName LIKE %:alertName% ")
	List<LearnerLicenseAlert> findByAlertNameAndLearnerIdByAndInActive(@Param("learnerId") Long learnerId, @Param("alertName") String alertName);
	
	@Query("SELECT l FROM  #{#entityName} l join fetch l.learner lr WHERE lr.id =:learnerId")
	List<LearnerLicenseAlert> findByLearnerId(@Param("learnerId") Long learnerId);
	
	//public List<LearnerLicenseAlert> getLearnerLicenseAlert(){
	//@Query("SELECT l FROM  #{#entityName} l WHERE l.triggerSingleDate =:triggerSingleDate and isDelete = false")
	List<LearnerLicenseAlert> findBytriggerSingleDateAndIsDeleteFalse(@Param("triggerSingleDate") Date date);
	
	//public int getLearnerLicenseAlert(long learnerid,long learnerlicenseid)
	@Query("SELECT l FROM  #{#entityName} l join fetch l.learnerlicense lr WHERE ISDELETE = false AND l.learner.id=:learnerId AND lr.id=:learnerlicenseId")
	List<LearnerLicenseAlert> countByLearnerIdByLearnerlicenseId(@Param("learnerId") Long learnerId, @Param("learnerlicenseId") Long learnerlicenseId);
	
	//public List<LearnerLicenseAlert> getLearnerLicenseAlert(long[] licenseOfLearnerId)
	List<LearnerLicenseAlert> findByIdIn(Long[] pkIds);
}
