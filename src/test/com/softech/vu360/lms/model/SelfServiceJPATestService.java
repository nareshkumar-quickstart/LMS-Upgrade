package com.softech.vu360.lms.model;

import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.junit.Test;

/*
 * @ author kaunain.wajeeh
 */


public class SelfServiceJPATestService extends TestBaseDAO<SelfServiceProductDetails> {

	@Test
	public void getAllTemplates() throws Exception {

		@SuppressWarnings("unchecked")
		List<SelfServiceProductDetails> templateList = getAll("SelfServiceProductDetails",
				SelfServiceProductDetails.class);
		for (SelfServiceProductDetails mt : templateList) {
			assertNotNull(mt);
		}
	}

	@Test
	public void testPersist() {

		SelfServiceProductDetails template = new SelfServiceProductDetails();
		template.setProductCode("001");
		template.setProductType("aaa");
		template.setProductCategory("Core");
		template.setExernalOrginizationId(1l);
		template.setEffectiveFrom(new Date());
		template.setEffectiveTo(new Date());
		template.setPurchaseDate(new Date());
		template.setActualProductType("Abc");
		template.setActualproductId(1l);
		SelfServiceProductDetails msg = (SelfServiceProductDetails) crudSave(SelfServiceProductDetails.class,
				template);
		assertNotNull(msg);

	}

	@Test
	public void testUpdate() {
		SelfServiceProductDetails template = this.getById(9755l, SelfServiceProductDetails.class);
		template.setProductType("TestProductTypeUpdated");
		template = update(template);
		assertNotNull(template);
	}

}
