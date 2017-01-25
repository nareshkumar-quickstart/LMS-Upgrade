package com.softech.vu360.lms.model;

import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.junit.Test;

/*
 * @author kaunain.wajeeh
 */

public class SynchronousSessionJPATestService extends
		TestBaseDAO<SynchronousSession> {

	@Test
	public void getAllTemplates() throws Exception {

		@SuppressWarnings("unchecked")
		List<SynchronousSession> templateList = getAll("SynchronousSession",
				SynchronousSession.class);
		for (SynchronousSession mt : templateList) {
			assertNotNull(mt);
		}
	}

	@Test
	public void testPersist() {

		SynchronousSession template = new SynchronousSession();
		template.setLocation(null);
		template.setMeetingGUID("TestGuid");
		template.setResources(null);
		template.setStartDateTime(new Date());
		template.setStartDateTimeZ(new Date());
		@SuppressWarnings("unchecked")
		List<SynchronousClass> syncList = this.getAll("SynchronousClass",
				SynchronousClass.class);
		template.setSynchronousClass(syncList.get(0));
		SynchronousSession msg = (SynchronousSession) crudSave(
				SynchronousSession.class, template);
		assertNotNull(msg);

	}

	@Test
	public void testUpdate() {
		SynchronousSession template = this
				.getById(4l, SynchronousSession.class);
		template.setMeetingGUID("TestUpdatedGuid");
		template = update(template);
		assertNotNull(template);
	}

}
