package com.softech.vu360.lms.model;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

/*
* @author kaunain.wajeeh
*/

public class TrainingAdministratorJPATestService extends TestBaseDAO<TrainingAdministrator> {

	@Test
	public void getAllTemplates() throws Exception {

		@SuppressWarnings("unchecked")
		
		//TrainingAdministrator ta = new TrainingAdministrator();
		//ta.setId(805L);
		TrainingAdministrator templateList = (TrainingAdministrator) crudFindById(TrainingAdministrator.class, new Long(805));
		
		
		List<OrganizationalGroup> mg  = templateList.getManagedGroups() ;
		
		List<OrganizationalGroup> mg2  = templateList.getManagedGroups().get(0).getChildrenOrgGroups() ;
	}

	//@Test
	public void testPersist() {

		TrainingAdministrator template = new TrainingAdministrator();
		template.setCustomer(null);
		template.setManagedGroups(null);
		template.setManagesAllOrganizationalGroups(true);
		@SuppressWarnings("unchecked")
		List<VU360User> user=this.getAll("VU360User", VU360User.class);
		template.setVu360User(user.get(0));
		TrainingAdministrator msg = (TrainingAdministrator) crudSave(
				TrainingAdministrator.class, template);
		assertNotNull(msg);

	}

	@Test
	public void testUpdate() {
		TrainingAdministrator template = this.getById(1l,
				TrainingAdministrator.class);
		template.setManagesAllOrganizationalGroups(false);
		template = update(template);
		assertNotNull(template);
	}

}
