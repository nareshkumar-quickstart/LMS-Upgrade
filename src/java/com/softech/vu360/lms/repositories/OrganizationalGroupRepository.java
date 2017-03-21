/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.OrganizationalGroup;

/**
 * @author marium.saud
 *
 */
public interface OrganizationalGroupRepository extends CrudRepository<OrganizationalGroup, Long>,OrganizationalGroupRepositoryCustom {
	
	//public List<OrganizationalGroup>  getOrgGroupByNames(String orgGroupNames[],Customer customer);
	@Query("select og from OrganizationalGroup og JOIN Customer c on og.customer.id = c.id WHERE c.id=:customerId AND og.name IN (:orgGroupNames)")
	public List<OrganizationalGroup>  findByCustomerIdAndNameIn(@Param("customerId") Long customerId, @Param("orgGroupNames") String orgGroupNames[]);

	
	//public List<OrganizationalGroup>  getOrgGroupsById(String orgGroupId[]);//legacy function, implemented below according to Spring data
	/** reason behind nativeQuery because this function is taking String array but it should be of type Long
	 *  reason behind to not change String to long because of big change in controllers
	 */
	@Query(value = "SELECT * FROM ORGANIZATIONALGROUP WHERE id in (:ids)", nativeQuery = true)
	public List<OrganizationalGroup>  findByIdIn(@Param("ids") String orgGroupId[]);
	
	
	//public List<OrganizationalGroup> getAllOrganizationalGroups(Customer customer);//legacy function, implemented below according to Spring data
	//public List<OrganizationalGroup>  getOrgGroupsByCustomer(Customer customer, VU360User loggedInUser);//legacy function, implemented below according to Spring data
	@Query("select og from OrganizationalGroup og JOIN Customer c on og.customer.id = c.id WHERE c.id=:customerId")
	public List<OrganizationalGroup>  findByCustomerId(@Param("customerId") Long customerId);
	
	
	//public OrganizationalGroup getRootOrgGroupForCustomer(Customer customer)
	@Query("select og from OrganizationalGroup og JOIN Customer c on og.customer.id = c.id WHERE c.id=:customerId AND og.rootOrgGroup IS NULL")
	public List<OrganizationalGroup>  findByCustomerIdAndRootOrgGroupIsNull(@Param("customerId") Long customerId);

	public void deleteById(Long Id);
	
	@Query(value = "SELECT * "
			+ "FROM #{#entityName} "
			+ "WHERE NAME LIKE :groupName "
			+ "and ID IN "
			+ "    (SELECT ORGANIZATIONALGROUP_ID "
			+ "     FROM ALERTRECIPIENT_ORGANIZATIONALGROUP "
			+ "     WHERE ALERTRECIPIENT_ID = :alertRecipientId"
			+ "    )", nativeQuery=true)
	List<OrganizationalGroup> getOrganisationGroupsUnderAlertRecipient(@Param("alertRecipientId")Long alertRecipientId , @Param("groupName")String groupName );
	
	@Query(value = "SELECT * "
			+ "FROM #{#entityName} "
			+ "WHERE NAME LIKE :groupName "
			+ "and ID IN "
			+ "    (SELECT ORGANIZATIONALGROUP_ID "
			+ "	    FROM ALERTTRIGGERFILTER_ORGANIZATIONALGROUP "
			+ "     WHERE ALERTTRIGGERFILTER_ID = :alertTriggerFilterId"
			+ "    )", nativeQuery=true)
	List<OrganizationalGroup> getOrganisationGroupsUnderAlertTriggerFilter(@Param("alertTriggerFilterId")Long alertTriggerFilterId , @Param("groupName")String groupName );
	
}
