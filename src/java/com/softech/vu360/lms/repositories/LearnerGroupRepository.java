/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.LearnerGroup;

/**
 * @author marium.saud
 *
 */
public interface LearnerGroupRepository extends CrudRepository<LearnerGroup, Long> , LearnerGroupRepositoryCustom {
	
	/**Added by Marium.saud.*/
	public LearnerGroup findFirstByNameIgnoreCaseAndCustomerId(String name,long customerID);

	//public List<LearnerGroup> getAllLearnerGroups(Customer customer,VU360User loggedInUser)
	public List<LearnerGroup> findByCustomerId(Long customerId);
	public List<LearnerGroup> findByCustomerIdAndOrganizationalGroupIdIn(Long customerId, Long [] orgIds);
	
	
	//public List<LearnerGroup> findLearnerGroupByName( String name, Customer customer,VU360User loggedInUser);
	public List<LearnerGroup> findByCustomerIdAndNameLike(Long customerId,  String name);
	public List<LearnerGroup> findByCustomerIdAndOrganizationalGroupIdInAndNameLike(Long customer, Long[] orgId, String name);
	
	
	//public List<LearnerGroup> getLearnerGroupsByOrgGroup(Long orgGroupId)
	public List<LearnerGroup> findByOrganizationalGroupId(Long orgId);
	
	//public void deleteLearnerGroups(long learnerGroupIdArray[] )
	void deleteByIdIn(List<Long> Ids);
	
	@Query(value = "SELECT COUNT(lr.ID), lg.ID FROM LEARNER lr, LEARNERGROUP lg, LEARNER_LEARNERGROUP lgl WHERE lg.ID IN (:LearnerID) AND lg.ID = lgl.LEARNERGROUP_ID AND lr.ID = lgl.LEARNER_ID GROUP BY lg.ID", nativeQuery = true)
	List<Object[]> getLearnerCountByLearnerGroup( @Param("LearnerID") Long [] learnerId);
	
	@Query(value = "SELECT COUNT(lr.ID), og.ID FROM LEARNER_ORGANIZATIONALGROUP l_og "
			    + " RIGHT OUTER JOIN LEARNER lr ON lr.ID = l_og.LEARNER_ID"
				+ " RIGHT OUTER JOIN ORGANIZATIONALGROUP og on og.ID = l_og.ORGANIZATIONALGROUP_ID  "
				+ " WHERE og.CUSTOMER_ID = :customerID GROUP BY og.ID", nativeQuery = true)
	List<Object[]> getLearnerCountByOrganizationalGroup( @Param("customerID") Long customerID);
	
	//public List<LearnerGroup>  getLearnerGroupsByLearnerGroupIDs(String learnerGroupId[])
	public List<LearnerGroup>  findByIdIn(Long learnerGroupId[]);
	
	@Query(value = "SELECT ID , CUSTOMER_ID, NAME, ORGANIZATIONALGROUP_ID "
			+ "FROM #{#entityName} "
			+ "WHERE NAME LIKE :groupName "
			+ "and ID IN "
			+ "    (SELECT LEARNERGROUP_ID "
			+ "     FROM ALERTRECIPIENT_LEARNERGROUP "
			+ "     WHERE ALERTRECIPIENT_ID = :alertRecipientId"
			+ "    )", nativeQuery=true)
	List<LearnerGroup> getLearnerGroupsUnderAlertRecipient(@Param("alertRecipientId")Long alertRecipientId , @Param("groupName")String groupName);
	
	@Query(value = "SELECT ID , CUSTOMER_ID, NAME, ORGANIZATIONALGROUP_ID "
			+ "FROM #{#entityName} "
			+ "WHERE NAME LIKE %:groupName% "
			+ "and ID IN "
			+ "    (SELECT LEARNERGROUP_ID "
			+ "     FROM ALERTTRIGGERFILTER_LEARNERGROUP "
			+ "    WHERE ALERTTRIGGERFILTER_ID  = :alertTriggerFilterId"
			+ "    )", nativeQuery=true)
	List<LearnerGroup> getLearnerGroupsUnderAlertTriggerFilter(@Param("alertTriggerFilterId")Long alertTriggerFilterId , @Param("groupName")String groupName);
	
}
