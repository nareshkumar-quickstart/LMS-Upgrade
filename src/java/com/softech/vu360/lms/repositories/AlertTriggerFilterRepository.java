package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.AlertTriggerFilter;

public interface AlertTriggerFilterRepository extends CrudRepository<AlertTriggerFilter, Long> {
	List<AlertTriggerFilter> findByIdIn(Long[] ids);
	List<AlertTriggerFilter> findByAlerttriggerId(Long alertTriggerId);
	List<AlertTriggerFilter> findByAlerttriggerIdAndAlerttriggerIsDeleteFalseAndIsDeleteFalse(Long alertTriggerId);
}
