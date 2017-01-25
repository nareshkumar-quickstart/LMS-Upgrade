package com.softech.vu360.lms.model;

import javax.transaction.Transactional;

import org.junit.Test;

@Transactional
public class JPAEntityBrandDomainTestService  extends TestBaseDAO<Object>{
	
	//@Test
	public void testBrandDomainIneset() throws Exception {
		
		BrandDomain b = new BrandDomain();
		b.setBrand("ddd");
		b.setDomain("domain");
		b.setLanguage("eng");
		crudSave(BrandDomain.class, b);
	}
	@Test
	public void testBrandDomainUpdate() throws Exception {
		
		BrandDomain b = new BrandDomain();
		b.setBrand("fffdddf");
		b.setDomain("yyydddyy");
		b.setLanguage("eng");
		b = (BrandDomain) crudSave(BrandDomain.class, b);

		
		BrandDomain c = (BrandDomain) crudFindById(BrandDomain.class, b.getId());
		c.setLanguage("spn");
		update(c);
		
	}
	
	
	
}