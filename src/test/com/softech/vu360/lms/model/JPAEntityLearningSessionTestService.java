package com.softech.vu360.lms.model;


import java.sql.Date;

import javax.transaction.Transactional;

import org.junit.Test;


@Transactional
public class JPAEntityLearningSessionTestService  extends TestBaseDAO<Object>{

	
	@Test
	public void testLMSAdminAllowedEntityUpdate() throws Exception {
		
		LearningSession b = new LearningSession();
		b.setBrandName("d");
		b.setCourseApprovalId(1L);
		b.setCourseId("1");
		b.setEnrollmentId(4343L);
		b.setExternalLMSSessionID("ddddddddddd");
		b.setExternalLMSUrl("dddddddd");
		b.setLearnerId(434L);
		b.setLearningSessionID("dddddddddds");
		b.setLmsProvider(34);
		b.setRedirectURI("ddddddd");
		b.setSessionEndDateTime(new Date(System.currentTimeMillis()));
		b.setSessionStartDateTime(new Date(System.currentTimeMillis()));
		b.setSource("dddddddddd");
		b.setUniqueUserGUID("ssssssss");
		
		b = (LearningSession) crudSave(LearningSession.class, b);
		
		LearningSession c = (LearningSession) crudFindById(LearningSession.class, b.getId());
		c.setBrandName("xxxxxxx");
		update(c);
		
	}
}
