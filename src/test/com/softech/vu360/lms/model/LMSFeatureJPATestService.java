package com.softech.vu360.lms.model;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

public class LMSFeatureJPATestService extends TestBaseDAO<LMSFeature> {

	@Test
	public void getAllTemplates() throws Exception {

		@SuppressWarnings("unchecked")
		List<LMSFeature> tzList = getAll("LMSFeature", LMSFeature.class);
		for (LMSFeature mt : tzList) {
			assertNotNull(mt);
		}
	}

	@Test
	public void testPersist() {

		LMSFeature template = new LMSFeature();
		template.setDisplayOrder(1);
		template.setFeatureCode("TestFCode");
		template.setFeatureDescription("TestDescription");
		template.setFeatureGroup("TestGroup");
		template.setFeatureName("TestName");
		template.setLmsMode("TestMode");
		template.setRoleType("TestRole");

		LMSFeature msg = (LMSFeature) crudSave(LMSFeature.class, template);
		assertNotNull(msg);

	}

	@Test
	public void testUpdate() {
		LMSFeature tz = this.getById(198l, LMSFeature.class);
		tz.setFeatureName("UpdatedFeatureName");
		tz = update(tz);
		assertNotNull(tz);
	}

}
