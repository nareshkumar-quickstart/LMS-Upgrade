package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.AlertQueue;

public interface AlertQueueRepository extends CrudRepository<AlertQueue, Long>{
	
	public List<AlertQueue> findByUserId(Long userId);
	public AlertQueue findByTableNameAndTableNameIdAndTriggerIdAndLearnerId(String tableName ,  Long tableNameId, Long triggerId,Long learnerId);
	public AlertQueue findByTriggerTypeAndLearnerId(String triggerType,Long learnerId);
	public List<AlertQueue> findByPendingMailStatus(boolean pendingMailStatus);
}
