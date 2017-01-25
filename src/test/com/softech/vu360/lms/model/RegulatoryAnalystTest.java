package com.softech.vu360.lms.model;

import javax.transaction.Transactional;

import org.junit.Test;

/**
 * 
 * @author marium.saud
 */
@Transactional
public class RegulatoryAnalystTest extends TestBaseDAO<RegulatoryAnalyst> {


//	@Test
	public void RegulatoryAnalyst_should_save() throws Exception {

		RegulatoryAnalyst regulatoryAnalyst=new RegulatoryAnalyst();
		regulatoryAnalyst.setForAllContentOwner(true);
		
		ContentOwner contentOwner=(ContentOwner)crudFindById(ContentOwner.class, new Long(1));
		regulatoryAnalyst.getContentOwners().add(contentOwner);
		
		VU360User user=(VU360User)crudFindById(VU360User.class, new Long(11));
		regulatoryAnalyst.setUser(user);
		
		try{
			
			regulatoryAnalyst=save(regulatoryAnalyst);
			System.out.println(regulatoryAnalyst.getId());
		}
		catch(Exception ex){
			System.out.println(ex);
		}

	}

	@Test
	public void RegulatoryAnalyst_should_update() throws Exception {

		RegulatoryAnalyst updateRecord=this.getById(new Long(4254), RegulatoryAnalyst.class);
		updateRecord.setContentOwners(null);
		this.update(updateRecord);

	}
	
}
