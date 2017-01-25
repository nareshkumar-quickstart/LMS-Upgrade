package com.softech.vu360.lms.model;

import org.junit.Test;

public class JPAEntityLMSResourseTestService extends TestBaseDAO{

	
	@Test
	public void testLMSResourseEntityUpdate() throws Exception {
		
		Resource b = new Resource();
		b.setContentowner((ContentOwner) crudFindById(ContentOwner.class, new Long(57656)));
		//b.setCustomfields(crudFindById ); //not usage of this
		//b.setCustomfieldValues(customfieldValues); //not usage of this
		b.setResourceType((ResourceType) crudFindById(ResourceType.class, new Long(2160)));
		b.setActive(true);
		b.setAssetTagNumber("dd");
		b.setDescription("sss");
		b.setName("dddd");
		
		b = (Resource) crudSave(Resource.class, b);
		
		Resource c = (Resource) crudFindById(Resource.class, b.getId());
		c.setActive(false);
		update(c);
		
	}
}
