/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.Author;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.RegistrationInvitation;
import com.softech.vu360.lms.model.VU360User;

/**
 * @author marium.saud
 *
 */
public interface RegistrationInvitationRepository extends CrudRepository<RegistrationInvitation, Long> , RegistrationInvitationRepositoryCustom{
	
	public List<RegistrationInvitation> findDistinctByOrgGroupsIdInAndCustomerIdAndInvitationNameContainingIgnoreCase(@Param("lstOrgGrpObjArr") Object[] lstOrgGrpObjArr,@Param("customerID") Long customerID, @Param("invitationName") String invitationName);
	
    public List<RegistrationInvitation> findByCustomerIdAndInvitationNameContainingIgnoreCase(@Param("customerID") long customerID, @Param("invitationName") String invitationName);

	Collection<OrganizationalGroup> findByOrgGroupsIdIn(@Param("ids")Collection<Long> ids);

}
