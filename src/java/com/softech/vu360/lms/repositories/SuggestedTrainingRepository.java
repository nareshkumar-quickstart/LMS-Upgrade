package com.softech.vu360.lms.repositories;

import java.util.List;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.SuggestedTraining;
import com.softech.vu360.lms.model.SurveyOwner;

public interface SuggestedTrainingRepository extends CrudRepository<SuggestedTraining, Long>	{

	
	List<SuggestedTraining> findBySurveyId(long id);
	@Query(value="SELECT t1.* FROM SURVEY t0, SUGGESTEDTRAINING t1 WHERE ((((((t0.OWNER_ID = :surveyOwnerId)\n"
			+ " OR (t0.OWNER_ID = :distributorId)) AND (t0.ISLOCKEDTF = :isLocked)) AND (t0.READONLYTF = :readOnly))\n"
			+ " AND (UPPER(t0.NAME) LIKE %:surveyName%)) AND (t0.ID = t1.SURVEY_ID))",nativeQuery=true)
	List<SuggestedTraining> findBySurveyNameAndSurveyIdOrDistributorIdAndIsLockedAndReadOnly(@Param("surveyOwnerId") Long surveyOwnerId,@Param("distributorId") Long distributorId,@Param("surveyName") String surveyName,@Param("isLocked") boolean isLocked,@Param("readOnly") boolean readOnly);
	@Query(value="SELECT t1.ID, t1.SURVEY_ID FROM SURVEY t0, SUGGESTEDTRAINING t1 WHERE\n"
			+ " ((((t0.OWNER_ID = :surveyOwnerId) OR (t0.OWNER_ID = :distributorId)) AND (UPPER(t0.NAME) LIKE %:surveyName%))\n"
			+ " AND (t0.ID = t1.SURVEY_ID))",nativeQuery=true)
	List<SuggestedTraining> findBySurveyNameAndSurveyIdOrDistributorId(@Param("surveyOwnerId") Long surveyOwnerId,@Param("distributorId") Long distributorId,@Param("surveyName") String surveyName);
	
	@Query(value="SELECT t1.ID, t1.SURVEY_ID FROM SURVEY t0, SUGGESTEDTRAINING t1 WHERE\n"
			+ " (((((t0.OWNER_ID = :surveyOwnerId) OR (t0.OWNER_ID = :distributorId)) AND (t0.ISLOCKEDTF = :isLocked)) AND\n"
			+ " (UPPER(t0.NAME) LIKE %:surveyName%)) AND (t0.ID = t1.SURVEY_ID))",nativeQuery=true)
	List<SuggestedTraining> findBySurveyNameAndSurveyIdOrDistributorIdAndIsLocked(@Param("surveyOwnerId") Long surveyOwnerId,@Param("distributorId") Long distributorId,@Param("surveyName") String surveyName,@Param("isLocked") boolean isLocked);
	
	@Query(value="SELECT t1.ID, t1.SURVEY_ID FROM SURVEY t0, SUGGESTEDTRAINING t1 WHERE (((((t0.OWNER_ID = :surveyOwnerId) OR (t0.OWNER_ID = :distributorId))\n"
			+ " AND (t0.READONLYTF = :readOnly)) AND (UPPER(t0.NAME) LIKE %:surveyName%)) AND (t0.ID = t1.SURVEY_ID))",nativeQuery=true)
	List<SuggestedTraining> findBySurveyNameAndSurveyIdOrDistributorIdAndReadOnly(@Param("surveyOwnerId") Long surveyOwnerId,@Param("distributorId") Long distributorId,@Param("surveyName") String surveyName,@Param("readOnly") boolean readOnly);
	
	
	
	@Query(value="SELECT t1.ID, t1.SURVEY_ID FROM SURVEY t0, SUGGESTEDTRAINING t1 WHERE (((((((t0.OWNER_ID = :surveyOwnerId)\n"
			+ " OR (t0.OWNER_ID = :distributorId)) AND (t0.ISLOCKEDTF = :isLocked)) AND (t0.READONLYTF = :readOnly)) AND (UPPER(t0.NAME) LIKE %:surveyName%))\n"
			+ " AND (UPPER(t0.STATUS) = :surveyStatus)) AND (t0.ID = t1.SURVEY_ID))",nativeQuery=true)
	List<SuggestedTraining> findBySurveyNameAndSurveyStatusAndSurveyIdOrDistributorIdAndIsLockedAndReadOnly(@Param("surveyOwnerId") Long surveyOwnerId,@Param("surveyStatus") String surveyStatus ,@Param("distributorId") Long distributorId,@Param("surveyName") String surveyName,@Param("isLocked") boolean isLocked,@Param("readOnly") boolean readOnly);
	
	@Query(value="SELECT t1.ID, t1.SURVEY_ID FROM SURVEY t0, SUGGESTEDTRAINING t1 WHERE (((((t0.OWNER_ID = :surveyOwnerId)\n"
			+ " OR (t0.OWNER_ID = :distributorId)) AND (UPPER(t0.NAME) LIKE %:surveyName%)) AND (UPPER(t0.STATUS) = :surveyStatus))\n"
			+ " AND (t0.ID = t1.SURVEY_ID))",nativeQuery=true)
	List<SuggestedTraining> findBySurveyNameAndSurveyStatusAndSurveyIdOrDistributorId(@Param("surveyOwnerId") Long surveyOwnerId,@Param("surveyStatus") String surveyStatus ,@Param("distributorId") Long distributorId,@Param("surveyName") String surveyName);
	
	@Query(value="SELECT t1.ID, t1.SURVEY_ID FROM SURVEY t0, SUGGESTEDTRAINING t1 WHERE ((((((t0.OWNER_ID = :surveyOwnerId)\n"
			+ " OR (t0.OWNER_ID = :distributorId)) AND (t0.ISLOCKEDTF = :isLocked)) AND (UPPER(t0.NAME) LIKE %:surveyName%))\n"
			+ " AND (UPPER(t0.STATUS) = :surveyStatus)) AND (t0.ID = t1.SURVEY_ID))",nativeQuery=true)
	List<SuggestedTraining> findBySurveyNameAndSurveyStatusAndSurveyIdOrDistributorIdAndIsLocked(@Param("surveyOwnerId") Long surveyOwnerId,@Param("surveyStatus") String surveyStatus ,@Param("distributorId") Long distributorId,@Param("surveyName") String surveyName,@Param("isLocked") boolean isLocked);
	
	@Query(value="SELECT t1.ID, t1.SURVEY_ID FROM SURVEY t0, SUGGESTEDTRAINING t1 WHERE ((((((t0.OWNER_ID = :surveyOwnerId)\n"
			+ " OR (t0.OWNER_ID = :distributorId)) AND (t0.READONLYTF = :readOnly)) AND (UPPER(t0.NAME) LIKE %:surveyName%))\n"
			+ " AND (UPPER(t0.STATUS) = :surveyStatus)) AND (t0.ID = t1.SURVEY_ID))",nativeQuery=true)
	List<SuggestedTraining> findBySurveyNameAndSurveyStatusAndSurveyIdOrDistributorIdAndReadOnly(@Param("surveyOwnerId") Long surveyOwnerId,@Param("surveyStatus") String surveyStatus ,@Param("distributorId") Long distributorId,@Param("surveyName") String surveyName,@Param("readOnly") boolean readOnly);
	
	
	
	
	@Query(value="SELECT t1.ID, t1.SURVEY_ID FROM SURVEY t0, SUGGESTEDTRAINING t1 WHERE ((((t0.OWNER_ID = :surveyOwnerId)"
			+ "\n AND (t0.OWNER_TYPE = :ownerType)) AND (UPPER(t0.NAME) LIKE %:surveyName%)) AND (t0.ID = t1.SURVEY_ID))",nativeQuery=true)
	List<SuggestedTraining> findBySurveyNameAndSurveyIdAndOwnerType(@Param("surveyOwnerId") Long surveyOwnerId,@Param("surveyName") String surveyName,@Param("ownerType") String ownerType);
	
	@Query(value="SELECT t1.ID, t1.SURVEY_ID FROM SURVEY t0, SUGGESTEDTRAINING t1 WHERE ((((((t0.OWNER_ID = :surveyOwnerId)\n"
			+ " AND (t0.ISLOCKEDTF = :isLocked)) AND (t0.READONLYTF = :readOnly)) AND (t0.OWNER_TYPE = :ownerType)) AND (UPPER(t0.NAME) LIKE %:surveyName%))"
			+ "\n AND (t0.ID = t1.SURVEY_ID))",nativeQuery=true)
	List<SuggestedTraining> findBySurveyNameAndSurveyIdAndOwnerTypeAndIsLockedAndReadOnly(@Param("surveyOwnerId") Long surveyOwnerId,@Param("surveyName") String surveyName,@Param("ownerType") String ownerType,@Param("isLocked") boolean isLocked,@Param("readOnly") boolean readOnly);

	@Query(value="SELECT t1.ID, t1.SURVEY_ID FROM SURVEY t0, SUGGESTEDTRAINING t1 WHERE (((((t0.OWNER_ID = :surveyOwnerId)\n"
			+ " AND (t0.ISLOCKEDTF = :isLocked)) AND (t0.OWNER_TYPE = :ownerType)) AND\n"
			+ " (UPPER(t0.NAME) LIKE %:surveyName%)) AND (t0.ID = t1.SURVEY_ID))",nativeQuery=true)
	List<SuggestedTraining> findBySurveyNameAndSurveyIdAndOwnerTypeAndIsLocked(@Param("surveyOwnerId") Long surveyOwnerId,@Param("surveyName") String surveyName,@Param("ownerType") String ownerType,@Param("isLocked") boolean isLocked);
	
	@Query(value="SELECT t1.ID, t1.SURVEY_ID FROM SURVEY t0, SUGGESTEDTRAINING t1 WHERE (((((t0.OWNER_ID = :surveyOwnerId)\n"
			+ " AND (t0.READONLYTF = :readOnly)) AND (t0.OWNER_TYPE = :ownerType)) AND (UPPER(t0.NAME) LIKE %:surveyName%))\n"
			+ " AND (t0.ID = t1.SURVEY_ID))",nativeQuery=true)
	List<SuggestedTraining> findBySurveyNameAndSurveyIdAndOwnerTypeAndReadOnly(@Param("surveyOwnerId") Long surveyOwnerId,@Param("surveyName") String surveyName,@Param("ownerType") String ownerType,@Param("readOnly") boolean readOnly);
	
	
	
	
	@Query(value="SELECT t1.ID, t1.SURVEY_ID FROM SURVEY t0, SUGGESTEDTRAINING t1 WHERE ((((((t0.OWNER_ID = :surveyOwnerId) AND\n"
			+ " (t0.OWNER_ID = :surveyOwnerId)) AND (t0.OWNER_TYPE = :ownerType)) AND (UPPER(t0.NAME) LIKE %:surveyName%))\n"
			+ " AND (UPPER(t0.STATUS) = :status)) AND (t0.ID = t1.SURVEY_ID))",nativeQuery=true)
	List<SuggestedTraining> findBySurveyNameAndSurveyStatusAndSurveyIdAndOwnerTypeAndStatus(@Param("surveyOwnerId") Long surveyOwnerId,@Param("surveyName") String surveyName,@Param("status") String status,@Param("ownerType") String ownerType);
	
	@Query(value="SELECT t1.ID, t1.SURVEY_ID FROM SURVEY t0, SUGGESTEDTRAINING t1 WHERE ((((((((t0.OWNER_ID = :surveyOwnerId)\n"
			+ " AND (t0.ISLOCKEDTF = :isLocked)) AND (t0.READONLYTF = :readOnly)) AND (t0.OWNER_ID = :surveyOwnerId)) AND (t0.OWNER_TYPE = :ownerType))\n"
			+ " AND (UPPER(t0.NAME) LIKE %:surveyName%)) AND (UPPER(t0.STATUS) = :status)) AND (t0.ID = t1.SURVEY_ID))",nativeQuery=true)
	List<SuggestedTraining> findBySurveyNameAndSurveyStatusAndSurveyIdAndOwnerTypeAndIsLockedAndReadOnlyAndStatus(@Param("surveyOwnerId") Long surveyOwnerId,@Param("surveyName") String surveyName,@Param("status") String status,@Param("ownerType") String ownerType,@Param("isLocked") boolean isLocked,@Param("readOnly") boolean readOnly);
	
	@Query(value="SELECT t1.ID, t1.SURVEY_ID FROM SURVEY t0, SUGGESTEDTRAINING t1 WHERE (((((((t0.OWNER_ID = :surveyOwnerId)\n"
			+ " AND (t0.ISLOCKEDTF = :isLocked)) AND (t0.OWNER_ID = :surveyOwnerId)) AND (t0.OWNER_TYPE = :ownerType)) AND (UPPER(t0.NAME) LIKE %:surveyName%))\n"
			+ " AND (UPPER(t0.STATUS) = :status)) AND (t0.ID = t1.SURVEY_ID))",nativeQuery=true)
	List<SuggestedTraining> findBySurveyNameAndSurveyStatusAndSurveyIdAndOwnerTypeAndIsLockedAndStatus(@Param("surveyOwnerId") Long surveyOwnerId,@Param("surveyName") String surveyName,@Param("status") String status,@Param("ownerType") String ownerType,@Param("isLocked") boolean isLocked);
	
	@Query(value="SELECT t1.ID, t1.SURVEY_ID FROM SURVEY t0, SUGGESTEDTRAINING t1 WHERE (((((((t0.OWNER_ID = :surveyOwnerId)"
			+ "\n AND (t0.READONLYTF = :readOnly)) AND (t0.OWNER_ID = :surveyOwnerId)) AND (t0.OWNER_TYPE = :ownerType))\n"
			+ " AND (UPPER(t0.NAME) LIKE %:surveyName%)) AND (UPPER(t0.STATUS) = :status)) AND (t0.ID = t1.SURVEY_ID))",nativeQuery=true)
	List<SuggestedTraining> findBySurveyNameAndSurveyStatusAndSurveyIdAndOwnerTypeAndReadOnlyAndStatus(@Param("surveyOwnerId") Long surveyOwnerId,@Param("surveyName") String surveyName,@Param("status") String status,@Param("ownerType") String ownerType,@Param("readOnly") boolean readOnly);

}
