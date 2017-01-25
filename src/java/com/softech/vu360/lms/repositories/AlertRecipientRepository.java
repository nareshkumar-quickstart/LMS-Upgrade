package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.AlertRecipient;

public interface AlertRecipientRepository extends CrudRepository<AlertRecipient, Long> {
	List<AlertRecipient> findByAlertRecipientGroupNameIgnoreCaseLikeAndAlertIdAndIsDeleteIsFalse(String name, Long Id);
	void deleteByIdIn(Long[] ids);
	List<AlertRecipient> findByAlertId(Long alertId);
}
