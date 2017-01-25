package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.Alert;

public interface AlertRepository extends CrudRepository<Alert, Long> {

	List<Alert> findByCreatedByIdAndAlertNameIgnoreCaseLikeAndIsDeleteIsFalse(Long loggedInUserId, String alertName);
	void deleteByIdIn(Long[] alertIdArray);
	List<Alert> findByCreatedByIdAndIsDeleteIsFalse(Long createdUserId);

}
