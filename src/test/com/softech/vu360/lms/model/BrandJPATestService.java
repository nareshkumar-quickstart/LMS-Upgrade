package com.softech.vu360.lms.model;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;

/*
 * 		Kaunain.Wajeeh
 */


@Transactional
public class BrandJPATestService extends TestBaseDAO<Brand> {

	@Test
	public void getAllTemplates() throws Exception {

		@SuppressWarnings("unchecked")
		List<Brand> brandList = getAll("Brand", Brand.class);
		for (Brand mt : brandList) {
			assertNotNull(mt);
		}
	}

	
	@Test
	public void testPersist() {

		Brand template = new Brand();
		template.setBrandKey("TestKey");
		template.setBrandName("TestName");
		Brand msg = (Brand) crudSave(Brand.class, template);
		assertNotNull(msg);

	}

	@Test
	public void testUpdate() {
		Brand brand = this.getById(212l, Brand.class);
		brand.setBrandKey("UpdatedKey");
		brand = update(brand);
		assertNotNull(brand);
	}
	


}
