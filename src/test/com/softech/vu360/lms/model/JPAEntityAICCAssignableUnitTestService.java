package com.softech.vu360.lms.model;

import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;

@Transactional
public class JPAEntityAICCAssignableUnitTestService  extends TestBaseDAO<Object>{
	
	@Test
	public void testAICCAssignableUnit() throws Exception {
		
		AICCAssignableUnit cf = new AICCAssignableUnit();
		cf.setCommandLine("xxxxxxxxxxxxxxxxxxxxx");
		cf.setCreatedDate(new Date());
		cf.setCourse( (Course) crudFindById(Course.class, new Long(104241)));
		
		
		cf = (AICCAssignableUnit) crudSave(AICCAssignableUnit.class, cf);
		
		AICCAssignableUnit cc = (AICCAssignableUnit) crudFindById(AICCAssignableUnit.class, cf.getId());
		cc.setCommandLine("aaaaaaaaaaaaaaa");
		update(cc);
		
	}
	
	
	
}