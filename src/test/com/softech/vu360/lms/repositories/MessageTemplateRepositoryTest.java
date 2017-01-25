package com.softech.vu360.lms.repositories;

import javax.inject.Inject;

import org.junit.Test;

import com.softech.vu360.lms.model.MessageTemplate;

public class MessageTemplateRepositoryTest extends SpringJUnitConfigAbstractTest {

	@Inject
	private MessageTemplateRepository messageTemplateRepository;
	
	@Test
	public void findByEventTypest() {
		String eventType = "";
		MessageTemplate messageTemplate = messageTemplateRepository.findByEventType(eventType);
		System.out.println(messageTemplate);
	}
	
	@Test
	public void findByTriggerType() {
		String triggerType = "";
		MessageTemplate messageTemplate = messageTemplateRepository.findByTriggerType(triggerType);
		System.out.println(messageTemplate);
	}

}
