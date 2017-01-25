package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.Contact;

public interface ContactRepository extends CrudRepository<Contact, Long> {

	@Query("SELECT c FROM  Contact c WHERE c.firstName LIKE %:firstName% and c.lastName LIKE %:lastName% "
			+ "AND c.emailAddress LIKE %:emailAddress% AND c.phone LIKE %:phone% AND c.regulator.id =:regulatorId")
	List<Contact> findContactByRegulator(@Param("firstName") String firstName, @Param("lastName") String lastName,
			@Param("emailAddress") String emailAddress, @Param("phone") String phone,
			@Param("regulatorId") Long regulatorId);

	public List<Contact> findByRegulatorId(Long id);

}
