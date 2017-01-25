/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.VU360User;

/**
 * @author marium.saud
 *
 */
public interface VU360UserRepository extends CrudRepository<VU360User, Long>, VU360UserRepositoryCustom , JpaRepository<VU360User, Long>, JpaSpecificationExecutor<VU360User>{

	List<VU360User> findByUsernameIn(@Param("lstUserName") Collection<String> lstUserName);	
	
	@EntityGraph(value = "VU360User.allJoins", type = EntityGraphType.LOAD)
	public VU360User getUserById(@Param("id") Long id);
	
	public List<VU360User> findByLearnerIdIn(Long[] learnerId);
	
	public VU360User findFirstByUserGUID(@Param("userGUID") String userGUID );
	
	public int countByEmailAddress(@Param("emailAddress") String emailAddress);
	
}
