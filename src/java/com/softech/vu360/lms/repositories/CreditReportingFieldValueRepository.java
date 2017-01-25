/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.CreditReportingFieldValue;
import com.softech.vu360.lms.model.Learner;

/**
 * @author marium.saud
 *
 */
public interface CreditReportingFieldValueRepository extends CrudRepository<CreditReportingFieldValue, Long> , CreditReportingFieldValueRepositoryCustom {

	public List<CreditReportingFieldValue> findByLearnerprofile_Learner_Id(Long learnerID);
	List<CreditReportingFieldValue> findByReportingCustomFieldId(Long creditReportingFieldId);
	List<CreditReportingFieldValue> findByReportingCustomFieldIdAndLearnerprofileId(Long creditReportingFieldId, Long learnerprofileId);
	List<CreditReportingFieldValue> findByLearnerprofileId(Long learnerprofileId);
	
}
