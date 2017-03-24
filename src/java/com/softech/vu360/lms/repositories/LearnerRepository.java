/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;

/**
 * @author marium.saud
 *
 *
 */
public interface LearnerRepository extends CrudRepository<Learner, Long>, LearnerRepositoryCustom {
	

	public List<Learner> findByVu360User_LmsRoles_Id(Long id);
	
	public Long countByVu360User_LmsRoles_Id(Long id);
	
	public List<Learner> findByCustomer_Distributor_Id(Long id);
	
	public List<Learner> findByCustomerId(Long id);
	
	public List<Learner> findByIdIn(Long[] ids);
	
	public List<Learner> findByCustomerIdAndVu360UserFirstNameLikeOrVu360UserLastNameLikeOrVu360UserEmailAddressLike(Long id, String firstName, String lastName, String email);
	public List<Learner> findByCustomerDistributorIdAndVu360UserFirstNameLikeOrVu360UserLastNameLikeOrVu360UserEmailAddressLike(Long id, String firstName, String lastName, String email);
	public List<Learner> findByCustomerDistributorInAndVu360UserFirstNameLikeOrVu360UserLastNameLikeOrVu360UserEmailAddressLike(List<Distributor> distributors, String firstName, String lastName, String email);
	public List<Learner> findByVu360UserFirstNameLikeOrVu360UserLastNameLikeOrVu360UserEmailAddressLike(String firstName, String lastName, String email);
	
	public Learner findFirstByCustomerIdOrderByIdAsc(Long id);
	
	@Query(value = "SELECT DISTINCT L.*  " +
			"FROM VU360USER U, LEARNER L, ALERTRECIPIENT_LEARNER AL " +
			"WHERE U.FIRSTNAME LIKE :firstName " +
			"AND U.LASTNAME LIKE :lastName " +
			"AND U.EMAILADDRESS LIKE :emailAddress " +
			"AND L.VU360USER_ID = U.ID " +
			"AND AL.LEARNER_ID = L.ID " +
			"AND AL.ALERTRECIPIENT_ID = :alertRecipientId", nativeQuery=true)
	List<Learner> getLearnersUnderAlertRecipient(@Param("alertRecipientId")Long alertRecipientId , @Param("firstName")String firstName , @Param("lastName")String lastName , @Param("emailAddress")String emailAddress);
	
	@Query(value = "SELECT DISTINCT L.* FROM VU360USER U, LEARNER L, ALERTTRIGGERFILTER_LEARNER ATFL WHERE L.VU360USER_ID = U.ID AND ATFL.LEARNER_ID = L.ID AND ATFL.ALERTTRIGGERFILTER_ID = :alertTriggerFilterId AND U.FIRSTNAME LIKE %:firstName% AND U.LASTNAME LIKE %:lastName% AND U.EMAILADDRESS LIKE %:emailAddress%", nativeQuery = true)
	List<Learner> getLearnersUnderAlertTriggerFilter(@Param("alertTriggerFilterId")Long alertTriggerFilterId , @Param("firstName")String firstName , @Param("lastName")String lastName , @Param("emailAddress")String emailAddress);
	
	@Query(value = "SELECT DISTINCT L.* FROM VU360USER U, LEARNER L, ALERTTRIGGERFILTER_LEARNER ATFL WHERE L.VU360USER_ID = U.ID AND ATFL.LEARNER_ID = L.ID AND ATFL.ALERTTRIGGERFILTER_ID = :alertTriggerFilterId", nativeQuery = true)
	List<Learner> getLearnersUnderAlertTriggerFilter(@Param("alertTriggerFilterId")Long alertTriggerFilterId);
	
	@EntityGraph(value = "Learner.getLearnerFromLearnerGroupMemberComposition", type = EntityGraphType.LOAD)
	List<Learner> findByLearnerGroupId(@Param("id") Long learnerGroupId);
	
	Learner findByVu360UserId(Long vu360UserId);
	
	@Query(value = "if exists(select le.course_id from LEARNERENROLLMENT le, learnercoursestatistics lcs, courseapproval ca, courseconfigurationtemplate cct, courseconfiguration cc where (le.learner_id = :learnerId and (le.enrollmentstatus = '"+ LearnerEnrollment.ACTIVE +"') and cast(le.enddate as date) >= cast(getdate() as date)) and lcs.learnerenrollment_id = le.ID and lcs.status in ('"+ LearnerCourseStatistics.IN_PROGRESS +"', '"+ LearnerCourseStatistics.IN_COMPLETE +"', '"+ LearnerCourseStatistics.LOCKED +"', '"+ LearnerCourseStatistics.AFFIDAVIT_PENDING +"', '"+ LearnerCourseStatistics.AFFIDAVIT_RECEIVED +"', '"+ LearnerCourseStatistics.AFFIDAVIT_DISPUTED +"', '"+ LearnerCourseStatistics.AFFIDAVIT_PENDING +"', '"+ LearnerCourseStatistics.USER_DECLINED_AFFIDAVIT +"', '"+ LearnerCourseStatistics.REPORTED +"') and ca.course_id = le.course_id and cct.ID = ca.courseconfigurationtemplate_id and cc.courseconfigurationtemplate_id = cct.id and (cc.VALIDATION_REQUIREIDENTITYVALIDATION = 1 and cc.PROFILEBASED_VALIDATION_TF = 0 and cc.DEFINEUNIQUEQUESTION_VALIDATION_TF = 0)) select cast(1 as bit) else select cast(0 as bit) " , nativeQuery=true)
	public boolean hasAnyInProgressEnrollmentOfStandardValidationQuestions(@Param("learnerId") long learnerId);
}
