/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.LMSFeature;

/**
 * @author marium.saud
 * @Kaunain.Wajeeh
 *
 */
public interface LMSFeatureRepository extends CrudRepository<LMSFeature, Long>, LMSFeatureRepositoryCustom {
	
	public List<LMSFeature> findByRoleType(String roleType);
	
	public List<LMSFeature> findByRoleTypeAndIdNotIn(String roleType, List<Long> ids);

}
