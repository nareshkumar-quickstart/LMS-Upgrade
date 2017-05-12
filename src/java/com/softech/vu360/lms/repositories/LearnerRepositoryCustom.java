/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.VU360User;

/**
 * @author marium.saud
 *
 */
public interface LearnerRepositoryCustom {
	public List<Learner> getFilteredRecipientsByAlert(Long alertId);
	public List<Learner> getLearnerByOrganizationalGroups(Long orgGroupIdArray[]);
	public List<Learner> findLearnerByLearnerGroupID(Long learnerGroupId);
	public List<Learner> findLearnersByVU360UserIn(List<VU360User> users);
}
