package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.AlertRecipient;
import com.softech.vu360.lms.model.EmailAddressAlertRecipient;

public interface EmailAddressAlertRecipientRepository extends CrudRepository<EmailAddressAlertRecipient, Long> {
	List<AlertRecipient> findByAlertRecipientGroupNameIgnoreCaseLikeAndAlertIdAndIsDeleteIsFalse(String name, Long Id);
}
