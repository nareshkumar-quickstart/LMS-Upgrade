package com.softech.vu360.lms.model;


import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;

public class CreditReportingFieldAuditTest extends TestBaseDAO {

	 private static final Class VU360ReportExecutionSummary = null;
	@PersistenceUnit(unitName="lmsPersistenceUnit")
	   EntityManagerFactory emf;
	   EntityManager entityManager;
	   EntityTransaction transaction;
	   
/*	public ReportDAO reportDAO;
	public VU360UserDAO vU360UserDAO;
	
	@Before
	public void setRequiredService() {
		reportDAO = (ReportDAO) applicationContext.getBean("reportDAO");
		vU360UserDAO = (VU360UserDAO) applicationContext.getBean("userDAO");
	}
	*/
	
	/*******************start*********************VU360Report*****************************************************/
	
	
	/*  <T> void inspect(Class<T> klazz, CreditReportingFieldAudit report) {
		 java.lang.reflect.Field[] fields = klazz.getDeclaredFields();
	        System.out.printf("%d fields:%n", fields.length);
	        for (java.lang.reflect.Field field : fields) {
	            System.out.println( field.getType().getSimpleName() +" - "+ field.getName() +" - "+ report.field.getName());
	        }
	    }
	 */
	   
	   
	   //***************start***************************CreditReportingFieldAudit******************************************************
	   //@Test
		public void CreditReportingFieldAudit_should_find() throws Exception {
			entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();
			CreditReportingFieldAudit crfA = entityManager.find(CreditReportingFieldAudit.class, new Long(1));
			//inspect(CreditReportingFieldAudit.class, report);
			 System.out.println(crfA.getFieldLabel());
			 System.out.println(crfA.getFieldType());
			 System.out.println(crfA.getCreatedOn());
		}
	   
	//@Test
	  public void CreditReportingFieldAudit_should_save() throws Exception {
		
		entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		
		CreditReportingFieldAudit crfAudit = new CreditReportingFieldAudit();
		crfAudit.setCreditReportingFieldId(2l);
		crfAudit.setCreatedBy(5132l);
		crfAudit.setFieldLabel("testinf label");
		crfAudit.setUpdatedBy(5132l);
		crfAudit.setCreatedOn(new Date());
		entityManager.persist(crfAudit);
	
		entityManager.getTransaction().commit();
		
	}
	
	   
		//@Test
		public void CreditReportingFieldAudit_should_update() throws Exception {
			
			entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();
			
			CreditReportingFieldAudit crfAudit = entityManager.find(CreditReportingFieldAudit.class, new Long(1));
			crfAudit.setCreditReportingFieldId(2l);
			crfAudit.setCreatedBy(5132l);
			crfAudit.setFieldLabel("testinf label");
			crfAudit.setUpdatedBy(5132l);
			crfAudit.setCreatedOn(new Date());
			crfAudit.setFieldLabel("12345678");
			entityManager.merge(crfAudit);
		
			entityManager.getTransaction().commit();
			
		}
		//***************end***************************CreditReportingFieldAudit******************************************************
		
		//***************start***************************CreditReportingFieldAudit******************************************************
		
		//@Test
		public void RegulatorCategoryAudit_should_find() throws Exception {
			entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();
			RegulatorCategoryAudit crfA = entityManager.find(RegulatorCategoryAudit.class, new Long(52));
			System.out.println(crfA.getCreatedOn());
			System.out.println(crfA.getDisplayName());
			System.out.println(crfA.getOperation());
		
		}
		
	 // @Test
	  public void RegulatorCategoryAudit_should_save() throws Exception {
		
		entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		
		RegulatorCategoryAudit crfAudit = new RegulatorCategoryAudit();
		crfAudit.setCategoryType("Designation");
		crfAudit.setCertificate("certificate");
		crfAudit.setCourseApprovalPeriod(1);
		crfAudit.setCourseApprovalPeriodUnit("courseApprovalPeriodUnit");
		crfAudit.setCourseApprovalRequired(true);

		crfAudit.setMaximumOnlineHours(12);
		crfAudit.setMinimumSeatTime(12);
		crfAudit.setPreAssessmentNumberOfQuestions(1);
		crfAudit.setMaximumOnlineHours(12);
		crfAudit.setRegulatorCategoryAuditId(5l);
		
		crfAudit.setCreatedBy(5132l);
		crfAudit.setUpdatedBy(5132l);
		crfAudit.setCreatedOn(new Date());
		entityManager.persist(crfAudit);
	
		entityManager.getTransaction().commit();
		
	}
		
	 // @Test
	  public void RegulatorCategoryAudit_should_update() throws Exception {
		
		entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		
		RegulatorCategoryAudit crfAudit = entityManager.find(RegulatorCategoryAudit.class, new Long(2751));
		crfAudit.setCategoryType("Designation");
		crfAudit.setCertificate("certificate");
		crfAudit.setCourseApprovalPeriod(1);
		crfAudit.setCourseApprovalPeriodUnit("courseApprovalPeriodUnit");
		crfAudit.setCourseApprovalRequired(true);

		crfAudit.setMaximumOnlineHours(122);
		crfAudit.setMinimumSeatTime(122);
		crfAudit.setPreAssessmentNumberOfQuestions(1);
		crfAudit.setMaximumOnlineHours(122);
		crfAudit.setRegulatorCategoryAuditId(5l);
		
		crfAudit.setCreatedBy(5132l);
		crfAudit.setUpdatedBy(5132l);
		crfAudit.setCreatedOn(new Date());
		entityManager.merge(crfAudit);
	
		entityManager.getTransaction().commit();
		
	}
		
	//***************end***************************CreditReportingFieldAudit******************************************************
		
	  
	  
	//***************start***************************RegulatoryApprovalAudit******************************************************
	//@Test
	public void RegulatorCategoryAudit_should_find2() throws Exception {
		entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		RegulatoryApprovalAudit crfA = entityManager.find(RegulatoryApprovalAudit.class, new Long(52));
		System.out.println(crfA.getApprovalType());
		System.out.println(crfA.getContainOwnerId());
		System.out.println(crfA.getCreatedBy());
		System.out.println(crfA.getCreatedOn());
		System.out.println(crfA.getId());
		System.out.println(crfA.getRegulatorApprovaId());
		System.out.println(crfA.getRegulatoryCategoryId());
		System.out.println(crfA.getUpdatedOn());
		System.out.println(crfA.getUpdatedBy());
	
	}
	
	//@Test
	public void RegulatorCategoryAudit_should_save2() throws Exception {
		
		entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		
		RegulatoryApprovalAudit crfAudit = new RegulatoryApprovalAudit();
		crfAudit.setApprovalType("approvalType");
		crfAudit.setContainOwnerId(22l);
		crfAudit.setDelete(true);
		crfAudit.setOperation("operation");
		crfAudit.setRegulatorApprovaId(523l);
		crfAudit.setRegulatoryCategoryId(222l);
		crfAudit.setUpdatedBy(5132l);
		crfAudit.setUpdatedOn(new Date());
		
		crfAudit.setCreatedBy(5132l);
		crfAudit.setUpdatedBy(5132l);
		crfAudit.setCreatedOn(new Date());
		entityManager.persist(crfAudit);
		entityManager.getTransaction().commit();
	}
		
	public void RegulatorCategoryAudit_should_update2() throws Exception {
		
		entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		
		RegulatoryApprovalAudit crfAudit = entityManager.find(RegulatoryApprovalAudit.class, new Long(52));
		crfAudit.setApprovalType("approvalType");
		crfAudit.setContainOwnerId(22l);
		crfAudit.setDelete(true);
		crfAudit.setOperation("operation");
		crfAudit.setRegulatorApprovaId(523l);
		crfAudit.setRegulatoryCategoryId(222l);
		crfAudit.setUpdatedBy(5132l);
		crfAudit.setUpdatedOn(new Date());
		
		crfAudit.setCreatedBy(5132l);
		crfAudit.setUpdatedBy(5132l);
		crfAudit.setCreatedOn(new Date());
		entityManager.persist(crfAudit);
		entityManager.getTransaction().commit();
	}
	//***************end***************************RegulatoryApprovalAudit******************************************************
		
}
