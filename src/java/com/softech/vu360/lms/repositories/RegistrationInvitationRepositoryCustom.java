/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.RegistrationInvitation;
import com.softech.vu360.lms.model.VU360User;

/**
 * @author marium.saud
 *
 */
public interface RegistrationInvitationRepositoryCustom {

	public Object[] addRegistrationInvitation(@Param("REG_ID") Long registrationInvitationID,@Param("REG_UTILIZED") Integer registrationUtilized);
}
