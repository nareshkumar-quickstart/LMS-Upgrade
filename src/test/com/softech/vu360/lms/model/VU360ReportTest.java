package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;

public class VU360ReportTest extends TestBaseDAO {

	 private static final Class VU360ReportExecutionSummary = null;
	@PersistenceUnit(unitName="lmsPersistenceUnit")
	   EntityManagerFactory emf;
	   EntityManager entityManager;
	   EntityTransaction transaction;
	   
	@Before
	public void setRequiredService() {
	}
	
	
	/*******************start*********************VU360Report*****************************************************/
/*	//@Test
	public void Report_Should_find() throws Exception {
		VU360Report report = reportDAO.loadReport(new Long(267));
		System.out.println(report.getDescription());
		System.out.println(report.getOwner().getName());
		System.out.println(report.getDerivedFrom().getDescription());
		System.out.println(report.getOriginalSystemReport().getDescription());
		//System.out.println(report.getExecutionSummaries().get(0).getCsvLocation());
		System.out.println(report.getFields().get(0).getDataType());
		System.out.println(report.getFilters().get(0).getId());
	}
*/	
	//@Test
/*	public void Report_Should_save() throws Exception {
		VU360Report report = new VU360Report();
		VU360Report report2 = reportDAO.loadReport(new Long(253));
		VU360User vu360User = vU360UserDAO.findUserByUserName("admin");
		report.setCategory("Registration");
		report.setDataSource("DataSource");
		report.setDerivedFrom(report2);
		report.setDescription("description");
		//report.setExecutionSummaries(executionSummaries);
		report.setFavorite(true);
		//report.setFields(fields);
		//report.setFilters(filters);
		report.setMode("MANAGER");
		report.setOriginalSystemReport(report2);
		report.setOwner(vu360User);
		//report.setParameter(parameter);
		report.setSqlTemplateUri("vm/reportsql/mgr_PrfLearnerByCourse.vm");
		report.setSystemOwned(true);
		report.setTitle("Rehan JAP RTeport");
		reportDAO.saveReport(report);
		
	}
*/	
	
	//@Test
	public void Report_Should_delete() throws Exception {
		VU360Report report = new VU360Report();
		report.setId(5932l);
		//reportDAO.removeReport(report);
		
	}
	
	
	//@Test
	public void Report_Should_update() throws Exception {
		
		 entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		
		VU360Report report = entityManager.find(VU360Report.class, 5930l);
		//VU360User vu360User = vU360UserDAO.findUserByUserName("admin");
		report.setCategory("Registration1111");
		report.setDataSource("DataSource111");
		report.setDescription("description111");
		report.setFavorite(true);
		report.setMode("MANAGER");
		//report.setOwner(vu360User);
		report.setSqlTemplateUri("vm/reportsql/mgr_PrfLearnerByCourse.vm");
		report.setSystemOwned(true);
		report.setTitle("Rehan JAP RTeport");
		entityManager.merge(report);

		entityManager.getTransaction().commit();
		
	}
	
	// util function
	List<VU360ReportExecutionSummary> getExecutionSummaries(VU360Report report, VU360User user){
		List<VU360ReportExecutionSummary> lst = new ArrayList<VU360ReportExecutionSummary>(); 
		VU360ReportExecutionSummary summ = new VU360ReportExecutionSummary();
		summ.setCsvLocation("abc");
		summ.setExecutionDate(new Date());
		summ.setHtmlLocation("cfef");
		summ.setReport(report);
		summ.setUser(user);
		lst.add(summ);
		return lst;
		
	}
	
	// util function
	List<VU360ReportField> getVU360ReportField(VU360Report report){
		List<VU360ReportField> lst = new ArrayList<VU360ReportField>(); 
		
		VU360ReportField summ = new VU360ReportField();
		summ.setColumnFormat("string");
		summ.setDataType("string");
		summ.setDbColumnName("MIDDLENAME");
		summ.setDefaultDisplayOrder(1);
		summ.setDefaultSortOrder(1);
		summ.setDisplayName("MIDDLENAME");
		summ.setDisplayOrder(1);
		summ.setEnabled(true);
		summ.setFieldOrder(1);
		summ.setFilterable(true);
		summ.setSortOrder(1);
		summ.setSortType(1);
		summ.setVisible(true);
		summ.setVisibleByDefault(true);
		summ.setVu360report(report);
		lst.add(summ);
		return lst;
		
	}
	
	/*******************end*********************VU360Report*****************************************************/
	
	
	/*******************start*********************VU360ReportExecutionSummary*****************************************************/
	//@Test
	public void VU360ReportExecutionSummary_should_find() throws Exception {
		entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		VU360ReportExecutionSummary summ = entityManager.find(VU360ReportExecutionSummary.class, 22605l);
			
		System.out.println(summ.getCsvLocation());
		System.out.println(summ.getUser().getName());
		System.out.println(summ.getReport().getId());
	}
	
	//@Test
	public void VU360ReportExecutionSummary_should_save() throws Exception {
		VU360ReportExecutionSummary summ = new VU360ReportExecutionSummary();
		summ.setCsvLocation("12345");
		summ.setExecutionDate(new Date());
		summ.setHtmlLocation("67890");
		
/*		VU360User vu360User = vU360UserDAO.findUserByUserName("admin");
		VU360Report report = reportDAO.loadReport(new Long(5930));
		summ.setReport(report);
		summ.setUser(vu360User);
    	reportDAO.saveReportExecutionSummary(summ);
*/
	}
	
	//@Test
	public void VU360ReportExecutionSummary_should_update() throws Exception {
		entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		VU360ReportExecutionSummary summ = entityManager.find(VU360ReportExecutionSummary.class, 22605l);
		summ.setCsvLocation("1234");
		entityManager.merge(summ);
		entityManager.getTransaction().commit();
	}
	/*******************end*********************VU360ReportExecutionSummary*****************************************************/
	
	
	
	
	
	/*******************start*********************VU360ReportField*****************************************************/
	//@Test
	public void VU360ReportField_should_find() throws Exception {
		entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		VU360ReportField summ = entityManager.find(VU360ReportField.class, 19105l);
			
		System.out.println(summ.getDbColumnName());
	
	}
	
	
	
	//@Test
	/*public void insertReportField() throws Exception {
		VU360ReportField summ = new VU360ReportField();
		summ.setDbColumnName("IDIDIDID");
    	//reportDAO.saveReportField(summ);
	}
	*/
	//@Test
	public void VU360ReportField_should_update() throws Exception {
		entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		VU360ReportField summ = entityManager.find(VU360ReportField.class, 19107l);
		summ.setDbColumnName("IDIDIDID1234");
		entityManager.merge(summ);
		entityManager.getTransaction().commit();
	}
	/*******************end*********************VU360ReportField*****************************************************/
	
	
	
	
	/*******************start*********************VU360ReportFilter*****************************************************/
	//@Test
	public void VU360ReportFilter_should_find() throws Exception {
		entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		VU360ReportFilter summ = entityManager.find(VU360ReportFilter.class, new Long(201));
		System.out.println(summ.getId());
		System.out.println(summ.getReport().getSqlTemplateUri());
	}
		
		
		
	//@Test
	/*public void insertVU360ReportFilter() throws Exception {
		VU360ReportField summ = new VU360ReportField();
		summ.setDbColumnName("IDIDIDID");
    	//reportDAO.saveReportField(summ);
	}
		*/
	
		//@Test
		/*public void VU360ReportField_should_update() throws Exception {
			entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();
			VU360ReportField summ = entityManager.find(VU360ReportField.class, 19107l);
			summ.setDbColumnName("IDIDIDID1234");
			entityManager.merge(summ);
			entityManager.getTransaction().commit();
		}
	*/
		/*******************end*********************VU360ReportFilter*****************************************************/
	
		
		
		
		
		
		
		/*******************start*********************CCILEAD*****************************************************/
		//@Test
		public void CCILead_should_find() throws Exception {
			entityManager = emf.createEntityManager();
			Query q = entityManager.createQuery("SELECT o FROM CCILead o WHERE o.cciLeadId = :cciLeadId");
			q.setParameter("cciLeadId", "1");
			List <CCILead> lstCCILead = (List<CCILead>)q.getResultList();
			
			System.out.println(lstCCILead.get(0).getCciLeadId());
		}
			
			
			
		//@Test
		public void CCILead_should_update() throws Exception {
			entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();
			CCILead summ = new CCILead();
			summ.setCciLeadId("5678");
			CustomerOrder cusOrd = entityManager.find(CustomerOrder.class, new Long(3));
			summ.setCustomerOrder(cusOrd);
			entityManager.merge(summ);
			entityManager.getTransaction().commit();
		}
		/*******************end*********************CCILEAD*****************************************************/
		
		
		/*******************start*********************CustomerOrder*****************************************************/
		@Test
		public void CustomerOrder_should_find() throws Exception {
			entityManager = emf.createEntityManager();
			CustomerOrder summ = entityManager.find(CustomerOrder.class, 19107l);
			
			System.out.println(summ.getOrderAmount());
			System.out.println(summ.getTransactionGUID());
			System.out.println(summ.getOrderGUID());
		}
			
		//@Test
		public void CustomerOrder_should_save() throws Exception {
			entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();
			CustomerOrder summ = new CustomerOrder();
			summ.setCustomer(entityManager.find(Customer.class, new Long(3)));
			
			List<LineItem> lineItem = new ArrayList<LineItem>();
			LineItem li = new LineItem();
			li.setItemGUID("item GUID");
			li.setOrderInfo(summ);
			li.setQuantity(1);
			li.setRefundQty(1);
			li.setSubItem("sub item");
			li.setCustomerEntitlement(entityManager.find(CustomerEntitlement.class, new Long(959259)));
			lineItem.add(li);
			
			summ.setOrderAmount(120f);
			summ.setOrderDate(new Date());
			summ.setOrderGUID("1 2 ka four");
			summ.setTransactionGUID("4 2 ka one");
			summ.setLineItem(lineItem);
			
			entityManager.merge(summ);
			entityManager.getTransaction().commit();
		}
		/*******************end*********************CustomerOrder*****************************************************/
		
		
		/*******************start*********************OrderLineItem*****************************************************/
		//@Test
		public void LineItem_should_find() throws Exception {
			entityManager = emf.createEntityManager();
			LineItem summ = entityManager.find(LineItem.class, new Long(11953));
			
			System.out.println(summ.getItemGUID());
			
		}
			
		@Test
		public void LineItem_should_update() throws Exception {
			entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();
			LineItem li = new LineItem();// entityManager.find(LineItem.class, new Long(11953));
			li.setCustomerEntitlement(entityManager.find(CustomerEntitlement.class, new Long(959259)));
			li.setItemGUID("item GUID . . .. . .");
			li.setOrderInfo(entityManager.find(CustomerOrder.class, new Long(1)));
			li.setQuantity(12345);
			li.setRefundQty(12345);
			li.setSubItem("sub item . . . . .. ");
			entityManager.merge(li);
			entityManager.getTransaction().commit();
			
		}		
}
