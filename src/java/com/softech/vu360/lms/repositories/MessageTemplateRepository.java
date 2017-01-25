package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.MessageTemplate;

public interface MessageTemplateRepository extends CrudRepository<MessageTemplate, Long> {

	MessageTemplate findByEventType(String eventType);
	MessageTemplate findByTriggerType(String triggerType);
	
}
