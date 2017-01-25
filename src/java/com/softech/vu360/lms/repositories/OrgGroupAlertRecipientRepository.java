package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.AlertRecipient;
import com.softech.vu360.lms.model.OrgGroupAlertRecipient;

public interface OrgGroupAlertRecipientRepository extends CrudRepository<OrgGroupAlertRecipient, Long> {
	List<AlertRecipient> findByAlertRecipientGroupNameIgnoreCaseLikeAndAlertIdAndIsDeleteIsFalse(String name, Long Id);
}
