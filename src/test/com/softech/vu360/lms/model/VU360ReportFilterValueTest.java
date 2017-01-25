package com.softech.vu360.lms.model;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.junit.Test;

/**
 * 
 * @author muhammad.rehan
 */

@Transactional
public class VU360ReportFilterValueTest extends TestBaseDAO<VU360ReportFilter> {

	@PersistenceUnit(unitName="lmsPersistenceUnit")
	   EntityManagerFactory emf;
	   EntityManager entityManager;
	   EntityTransaction transaction;
	   
	//@Test
	public void getCustomerPreferences(){
	//	VU360ReportFilterValue de =getById(new Long(201), VU360ReportFilterValue.class);
		
	}
	
	
	//@Test
	public void VU360ReportFilterValue_should_save() throws Exception {
		VU360ReportFilterValue rfv = new VU360ReportFilterValue();
		
		VU360ReportFilter rf = (VU360ReportFilter)crudFindById(VU360ReportFilter.class, new Long(2503));
		rfv.setFilter(rf);
		rf.setValue(rfv);
		
		rfv.setDateValue(new Date());
		//rfv.setNumericValue(Double.parseDouble("77"));
		rfv.setStringValue("zero123");
		rfv=(VU360ReportFilterValue)crudSave(VU360ReportFilterValue.class, rfv);
	}
	
	//@Test
	public void VU360ReportFilterValue_should_update() throws Exception {
		VU360ReportFilterValue cp = (VU360ReportFilterValue)crudFindById(VU360ReportFilterValue.class, new Long(801));
		cp.setStringValue("zero 12 3 4");
		cp=(VU360ReportFilterValue)crudSave(VU360ReportFilterValue.class, cp);
	}
	
	
	//@Test
	public void VU360ReportFilterOperand_should_find() throws Exception {
		entityManager = emf.createEntityManager();
		Query q = entityManager.createQuery("FROM VU360ReportFilterOperand");
		
		List <VU360ReportFilterOperand> ro = (List<VU360ReportFilterOperand>)q.getResultList();
		
		System.out.println(ro.get(0).getDataType());
	}
	
	@Test
	public void VU360ReportFilter_should_update () throws Exception {
		VU360ReportFilter cp = (VU360ReportFilter)crudFindById(VU360ReportFilter.class, new Long(2326));
		VU360ReportFilterOperand cpo = (VU360ReportFilterOperand)crudFindById(VU360ReportFilterOperand.class, new Long(2));
		cp.setOperand(cpo);
		cp=(VU360ReportFilter)crudSave(VU360ReportFilter.class, cp);
	}
}
