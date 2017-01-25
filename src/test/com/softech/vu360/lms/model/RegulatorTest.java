package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author raja.ali 
 */
@Transactional
public class RegulatorTest extends TestBaseDAO<Regulator> {

	
	@Before
	public void setRequiredService() {
		

	}

	@Test
	public void regulator_test() throws Exception {
		System.out.println("#### Unit Test Regulator using JUnit ####");
		
		//** Regulator ****
		//Regulator objSaved = regulator_should_save();
		Regulator objRegulator = getById(new Long("2"), Regulator.class);
		//Regulator objRegulator = getById(new Long("1"), Regulator.class);
		
		List<Regulator> objSearch = (ArrayList<Regulator>)getAll("Regulator", Regulator.class); 
		for (Iterator iterator = objSearch.iterator(); iterator.hasNext();) {
			Regulator regulator = (Regulator) iterator.next();
		}
		
		
		System.out.println("........Finish.........");
		
		
		
		
	}
	
	
	private Regulator regulator_should_save() {
		Regulator objRegulator = new Regulator();
		try{
			
			objRegulator = (Regulator) crudSave(Regulator.class, objRegulator);
			System.out.println("objRegulator.id::"+objRegulator.getId());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return objRegulator;
	}
	

}
