package com.softech.vu360.lms.model;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;

public class LMSProductsTest extends TestBaseDAO {
	@PersistenceUnit(unitName="lmsPersistenceUnit")
	   EntityManagerFactory emf;
	   EntityManager entityManager;
	   EntityTransaction transaction;
	   
		@Before
		public void setRequiredService() {
		}
	   
	   /*******************start*********************OrderLineItem*****************************************************/
		//@Test
		public void LMSProducts_should_find() throws Exception {
			entityManager = emf.createEntityManager();
			LMSProducts summ = entityManager.find(LMSProducts.class, new Long(1001));
			System.out.println(summ.getProductName());
			
		}
			
		//@Test
		public void LMSProducts_should_save() throws Exception {
			entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();
			LMSProducts s = new LMSProducts();
			s.setProductName("testing");
			entityManager.merge(s);
			entityManager.getTransaction().commit();
		}	
		
		//@Test
		public void LMSProducts_should_update() throws Exception {
			entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();
			LMSProducts s = entityManager.find(LMSProducts.class, new Long(1));
			s.setProductName("testing123");
			entityManager.merge(s);
			entityManager.getTransaction().commit();
		}
		
		//@Test
		public void LMSProductPurchase_should_find() throws Exception {
			entityManager = emf.createEntityManager();
			LMSProductPurchase summ = entityManager.find(LMSProductPurchase.class, new Long(1));
		//	System.out.println(summ.getProductName());
			
		}
		
		//@Test
		public void LMSProductPurchase_should_save() throws Exception {
			entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();
			LMSProducts s = entityManager.find(LMSProducts.class, new Long(1));
			LMSProductPurchase pp = new LMSProductPurchase();
			pp.setLmsProduct(s);
			pp.setCustomer(entityManager.find(Customer.class, new Long(1)));
			entityManager.merge(pp);
			entityManager.getTransaction().commit();
		}
		
		//@Test
		public void LMSProductCourseType_should_find() throws Exception {
			entityManager = emf.createEntityManager();
			LMSProductCourseType summ = entityManager.find(LMSProductCourseType.class, new Long(1));
			
			
			entityManager = emf.createEntityManager();
			Query q = entityManager.createQuery("SELECT o FROM LMSProductCourseType o WHERE o.lmsProduct.id = :Id");
			q.setParameter("Id", new Long(1));
			List <LMSProductCourseType> lst = (List<LMSProductCourseType>)q.getResultList();
			
			System.out.println(lst.get(0).getCourseType());
			
		}
		
		//@Test
		public void LMSProductCourseType_should_save() throws Exception {
			entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();
			LMSProducts p = new  LMSProducts();//entityManager.find(LMSProducts.class, new Long(1));
			p.setProductName("productName");
			LMSProductCourseType pp = new LMSProductCourseType();
			
			pp.setLmsProduct(p);
			pp.setCourseType("abc");
			entityManager.merge(pp);
			entityManager.getTransaction().commit();
		}
		
		////////////////////////////////////////// OrderRequestSOAPEnvelop /////////////////////////////////////////////////////
		//@Test
		public void OrderRequestSOAPEnvelop_should_find() throws Exception {
			entityManager = emf.createEntityManager();
			OrderRequestSOAPEnvelop summ = entityManager.find(OrderRequestSOAPEnvelop.class, new Long(62176));
			System.out.println(summ.getSoapEnvelop());
		}
		
		//@Test
		public void OrderRequestSOAPEnvelop_should_save() throws Exception {
			entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();
			OrderRequestSOAPEnvelop summ = entityManager.find(OrderRequestSOAPEnvelop.class, new Long(234750));
			summ.setCustomerName("customerName123");
			summ.setEventDate(new Date());
			summ.setOrderError("orderError");
			summ.setOrderId("11223344");
			summ.setSoapEnvelop("soapEnvelop");
			summ.setTransactionGUID("transactionGUID");
			entityManager.merge(summ);
			entityManager.getTransaction().commit();
			
		}
		
		//@Test
		public void OrderRequestSOAPEnvelop_should_update() throws Exception {
			entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();
			OrderRequestSOAPEnvelop summ = new OrderRequestSOAPEnvelop();
			summ.setCustomerName("customerName");
			summ.setEventDate(new Date());
			summ.setOrderError("orderError");
			summ.setOrderId("11223344");
			summ.setSoapEnvelop("soapEnvelop");
			summ.setTransactionGUID("transactionGUID");
			entityManager.persist(summ);
			entityManager.getTransaction().commit();
			
		}
		
		//////////////////////////////////////////OrderRequestSOAPEnvelop /////////////////////////////////////////////////////
		
		
		//////////////////////////////////////////LcmsResource /////////////////////////////////////////////////////
		//@Test
		public void LcmsResource_should_find() throws Exception {
			entityManager = emf.createEntityManager();
			LcmsResource summ = entityManager.find(LcmsResource.class, new Long(3));
			System.out.println(summ.getResourceKey());
			System.out.println(summ.getResourceValue());
			System.out.println(summ.getBrand().getBrandKey());
			System.out.println(summ.getLanguage().getDisplayName());
			System.out.println(summ.getLocaleId());
		}
		
		//@Test
		public void LcmsResource_should_update() throws Exception {
			entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();
			LcmsResource summ = entityManager.find(LcmsResource.class, new Long(3));
			Brand b = new Brand();
			b.setId(1l);
			summ.setBrand(b);
			
			summ.setLanguage(entityManager.find(Language.class, new Long(4)));
			summ.setLocaleId(3);
			summ.setResourceKey("key");
			summ.setResourceValue("value");
			
			entityManager.merge(summ);
			entityManager.getTransaction().commit();
		
		}
		//////////////////////////////////////////LcmsResource /////////////////////////////////////////////////////
		
		
		//	////////////////////////////////////////getRegulatorCategoryCreditReportingFieldAudit /////////////////////////////////////////////////////
		//@Test
		public void RegulatorCategoryCreditReportingFieldAudit_should_find() throws Exception {
			entityManager = emf.createEntityManager();
			RegulatorCategoryCreditReportingFieldAudit summ = entityManager.find(RegulatorCategoryCreditReportingFieldAudit.class, new Long(547));
			System.out.println(summ.getOperation());
			System.out.println(summ.getCreatedBy());
			System.out.println(summ.getCreatedOn());
			System.out.println(summ.getCreditReportingFieldId());
			System.out.println(summ.getRegulatorCategoryId());
		}
		
		//@Test
		public void RegulatorCategoryCreditReportingFieldAudit_should_save() throws Exception {
			entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();
			RegulatorCategoryCreditReportingFieldAudit summ = new RegulatorCategoryCreditReportingFieldAudit();
		
			summ.setCreatedBy(5l);
			summ.setCreatedOn(new Date());
			summ.setCreditReportingFieldId(4955l);
			summ.setOperation("AAAA");
			summ.setRegulatorCategoryId(5158l);
			summ.setUpdatedBy(5l);
			summ.setUpdatedOn(new Date());
			
			entityManager.merge(summ);
			entityManager.getTransaction().commit();
		
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		
		
		
		//////////////////////////////////////////LmsSfOrderAuditLog /////////////////////////////////////////////////////
			@Test
			public void LmsSfOrderAuditLog_should_find() throws Exception {
				entityManager = emf.createEntityManager();
				LmsSfOrderAuditLog summ = entityManager.find(LmsSfOrderAuditLog.class, new Long(1));
				System.out.println(summ.getActionType());
				System.out.println(summ.getCourseGroupGUID());
				System.out.println(summ.getMessage());
				
			}
			
			//@Test
			public void LmsSfOrderAuditLog_should_save() throws Exception {
				entityManager = emf.createEntityManager();
				entityManager.getTransaction().begin();
				LmsSfOrderAuditLog summ = new LmsSfOrderAuditLog();
			
				summ.setActionType("actionType");
				summ.setCourseGroupGUID("courseGroupGUID");
				summ.setCourseGUID("courseGUID");
				summ.setMessage("message");
				summ.setOrderId("orderId");
				summ.setStatus("status");
				summ.setValidCourseGUID(true);
				
				entityManager.merge(summ);
				entityManager.getTransaction().commit();
			
			}
			
		//////////////////////////////////////////LicenseType /////////////////////////////////////////////////////
		@Test
		
		public void LicenseType_should_find() throws Exception {
			entityManager = emf.createEntityManager();
			LicenseType summ = entityManager.find(LicenseType.class, new Long(1));
			System.out.println(summ.getLicenseType());
		
		}
			
		
}
