package com.softech.vu360.lms.model;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author raja.ali 
 */
@Transactional
public class SCOObjectiveTest extends TestBaseDAO<SCOObjective> {

	
	@Before
	public void setRequiredService() {
		

	}

	@Test
	public void sCOObjective_test() throws Exception {
		System.out.println("#### Unit Test SCOObjective using JUnit ####");
		
		//** SCOObjective ****
		//SCOObjective objSCOObjective = sCOObjective_should_save();
		SCOObjective objSCOObjective = getById(new Long("4"), SCOObjective.class);
		//SCOObjective objSCOObjective = getById(new Long("5"), SCOObjective.class);
		

		System.out.println("........Finish.........");
	}
	
	
	private SCOObjective sCOObjective_should_save() {
		SCOObjective objSCOObjective = new SCOObjective();
		try{
			
			objSCOObjective.setDescription("description");
			objSCOObjective.setScoObjectiveId("xxx");
			objSCOObjective.setSco((SCO)crudFindById(SCO.class, new Long("1")));
			
			objSCOObjective = (SCOObjective) crudSave(SCOObjective.class, objSCOObjective);
			
			System.out.println("objSCOObjective.id::"+objSCOObjective.getId());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return objSCOObjective;
	}
	

}
