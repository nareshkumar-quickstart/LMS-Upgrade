package com.softech.vu360.lms.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.softech.vu360.lms.model.AlertTrigger;

public interface AlertTriggerRepository extends CrudRepository<AlertTrigger, Long> {

	List<AlertTrigger> findByIdIn(Long[] ids);
	List<AlertTrigger> findByAvailableAlertEventsId(Integer id);
	List<AlertTrigger> findByAlertIdAndIsDeleteIsFalse(Long alertId);

}
