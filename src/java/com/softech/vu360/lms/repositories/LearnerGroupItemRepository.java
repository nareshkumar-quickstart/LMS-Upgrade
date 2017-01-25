/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.LearnerGroupItem;
import com.softech.vu360.lms.model.LearnerGroupItemPK;

/**
 * @author marium.saud
 *
 */
public interface LearnerGroupItemRepository extends CrudRepository<LearnerGroupItem, LearnerGroupItemPK> , LearnerGroupItemRepositoryCustom{
	
	//public List<LearnerGroupItem> getLearnerGroupItemsByLearnerGroupId(String learnerGroupId);
	public List<LearnerGroupItem> findByLearnerGroupId(Long learnerGroupId);
	
}
