package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.EmailAddress;

public interface EmailAddressRepository extends CrudRepository<EmailAddress, Long> {

	@Query(value = "SELECT ID , EMAIL "
			+ "FROM #{#entityName} "
			+ "WHERE EMAIL LIKE :emailAddress "
			+ "and ID IN "
			+ "    (SELECT EMAILADDRESS_ID "
			+ "    FROM ALERTRECIPIENT_EMAILADDRESS "
			+ "    WHERE ALERTRECIPIENT_ID = :alertRecipientId)", nativeQuery=true)
	List<EmailAddress> getEmailAddressUnderAlertRecipient(@Param("alertRecipientId")Long alertRecipientId, @Param("emailAddress")String emailAddress);
	
}
