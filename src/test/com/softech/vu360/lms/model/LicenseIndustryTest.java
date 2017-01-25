package com.softech.vu360.lms.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;


public class LicenseIndustryTest extends TestBaseDAO {
	@PersistenceUnit(unitName="lmsPersistenceUnit")
	   EntityManagerFactory emf;
	   EntityManager entityManager;
	   EntityTransaction transaction;
	
	  
		@Before
		public void setRequiredService() {
			//vU360UserDAO = (VU360UserDAO) applicationContext.getBean("userDAO");
		}
		
	   
	   
	// LicenseIndustry ///////////////////////////////////////////////////////////////////////////////////////////////////////////////   
	//@Test
	public void LicenseIndustry_should_find() throws Exception {
		entityManager = emf.createEntityManager();
		LicenseIndustry summ = entityManager.find(LicenseIndustry.class, new Long(1));
		System.out.println(summ.getName());
		System.out.println(summ.getShortName());
		
	}
	
	//@Test
	public void LicenseIndustry_should_findAll() throws Exception {
		entityManager = emf.createEntityManager();
		List<LicenseIndustry> results = entityManager.createQuery("Select a from LicenseIndustry a", LicenseIndustry.class).getResultList();
		System.out.println(results.get(0).getName());
		System.out.println(results.get(1).getShortName());
	}
	
	
	//LicenseOfLearner/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//@Test
	public void LicenseOfLearner_should_find() throws Exception {
		entityManager = emf.createEntityManager();
		LicenseOfLearner summ = entityManager.find(LicenseOfLearner.class, new Long(1102));
		System.out.println(summ.getLicenseCertificate());
		System.out.println(summ.getState());
		System.out.println(summ.getSupportingInformation());
		System.out.println(summ.getActive());
		System.out.println(summ.getId());
		System.out.println(summ.getIndustryCredential().getId());
		System.out.println(summ.getLearner().getVu360User().getName());
		System.out.println(summ.getLearnerLicenseType());
		System.out.println(summ.getLicenseIndustry().getName());
		System.out.println(summ.getUpdatedBy().getUsername());
		System.out.println(summ.getUpdateOn());
	}
	
	//@Test
	public void LicenseOfLearner_should_save() throws Exception {
		entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();

		LicenseType lt = new LicenseType();
		lt.setId(1l);
		LicenseIndustry objli = new LicenseIndustry(); 
		objli.setId(1l);
		IndustryCredential objlic = new IndustryCredential();
		objlic.setId(1l);
		//VU360User vu360User = vU360UserDAO.findUserByUserName("admin");
		LearnerLicenseType objlt = new LearnerLicenseType();
		objlt.setId(2l);
		//objlt.setLearner(vu360User.getLearner());
		objlt.setLicenseType(lt);
		
		LicenseOfLearner licenseOfLearner = new LicenseOfLearner();
		licenseOfLearner.setLicenseIndustry(objli);
		licenseOfLearner.setLicenseCertificate("licenseCertificate");
		licenseOfLearner.setState("Dubai");
		licenseOfLearner.setIndustryCredential( objlic );
		licenseOfLearner.setSupportingInformation("SupportingInformation");
		licenseOfLearner.setActive(Boolean.TRUE);
		//licenseOfLearner.setUpdatedBy(vu360User);
		licenseOfLearner.setUpdateOn( new Timestamp(System.currentTimeMillis()));
		//licenseOfLearner.setLearner(vu360User.getLearner());
		licenseOfLearner.setLearnerLicenseType(objlt);
		entityManager.merge(licenseOfLearner);
		entityManager.getTransaction().commit();
	}	
	
	//@Test
	public void LicenseOfLearner_should_update() throws Exception {
		entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();

		LicenseIndustry objli = new LicenseIndustry(); 
		objli.setId(1l);
		IndustryCredential objlic = new IndustryCredential();
		objlic.setId(1l);
		
		LicenseOfLearner licenseOfLearner = entityManager.find(LicenseOfLearner.class, new Long(1102));
		
		licenseOfLearner.setLicenseIndustry(objli);
		licenseOfLearner.setLicenseCertificate("licenseCertificate");
		licenseOfLearner.setState("Dubai");
		licenseOfLearner.setIndustryCredential( objlic );
		
		licenseOfLearner.setSupportingInformation("SupportingInformation");
		licenseOfLearner.setActive(Boolean.TRUE);
		
		//VU360User vu360User = vU360UserDAO.findUserByUserName("admin");
		//licenseOfLearner.setUpdatedBy(vu360User);
		licenseOfLearner.setUpdateOn( new Timestamp(System.currentTimeMillis()));
		//licenseOfLearner.setLearner(vu360User.getLearner());
		
		entityManager.merge(licenseOfLearner);
		entityManager.getTransaction().commit();
	}
	
	
	
	
	
	
	//LearnerLicenseAlert/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
		//@Test
		public void LearnerLicenseAlert_should_find() throws Exception {
			entityManager = emf.createEntityManager();
			LearnerLicenseAlert summ = entityManager.find(LearnerLicenseAlert.class, new Long(302));
			System.out.println(summ.getAlertName());
			System.out.println(summ.getCreatedDate());
			System.out.println(summ.getDaysAfterEvent());
			System.out.println(summ.getDaysBeforeEvent());
			System.out.println(summ.getLearner());
			System.out.println(summ.getLearnerlicense());
	
			System.out.println(summ.getTriggerSingleDate());
			System.out.println(summ.getUpdatedDate());
			
		}
		
		//@Test
		public void LearnerLicenseAlert_should_save() throws Exception {
			entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();

			//VU360User vu360User = vU360UserDAO.findUserByUserName("admin");
			
			LearnerLicenseAlert lla = new LearnerLicenseAlert();
			lla.setAlertName("alertName");
			lla.setCreatedDate(new Date());
			lla.setDaysAfterEvent(1);
			lla.setDaysBeforeEvent(1);
			lla.setDelete(false);
			//lla.setLearner(vu360User.getLearner()); 
			lla.setLearnerlicense(entityManager.find(LicenseOfLearner.class, new Long(1102)));
			lla.setTriggerSingleDate(new Date());
			lla.setUpdatedDate(new Date());

			entityManager.merge(lla);
			entityManager.getTransaction().commit();
		}	
		
		//@Test
		public void LearnerLicenseAlert_should_update() throws Exception {
			entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();

			//VU360User vu360User = vU360UserDAO.findUserByUserName("admin");
			
			LearnerLicenseAlert lla = entityManager.find(LearnerLicenseAlert.class, new Long(3101));
			lla.setAlertName("alertName1 2 3");
			lla.setCreatedDate(new Date());
			lla.setDaysAfterEvent(12);
			lla.setDaysBeforeEvent(12);
			lla.setDelete(false);
			//lla.setLearner(vu360User.getLearner()); 
			lla.setLearnerlicense(entityManager.find(LicenseOfLearner.class, new Long(1102)));
			lla.setTriggerSingleDate(new Date());
			lla.setUpdatedDate(new Date());

			entityManager.merge(lla);
			entityManager.getTransaction().commit();
		}
		
		
		
		//LearnerLicenseType/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//@Test
		public void LearnerLicenseType_should_find() throws Exception {
			entityManager = emf.createEntityManager();
			LearnerLicenseType summ = entityManager.find(LearnerLicenseType.class, new Long(1));
			System.out.println(summ.getLicenseType().getLicenseType());
			System.out.println(summ.getLearner().getVu360User().getLastName());
		}
		
		//@Test
		public void LearnerLicenseType_should_findAll() throws Exception {
			entityManager = emf.createEntityManager();
			List<LearnerLicenseType> results = entityManager.createQuery("Select a from LearnerLicenseType a where a.learner.id=1037844", LearnerLicenseType.class).getResultList();
			System.out.println(results.get(0).getLicenseType());
			
		}
		
		//@Test
		public void LearnerLicenseType_should_save() throws Exception {
			entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();

			//VU360User vu360User = vU360UserDAO.findUserByUserName("admin");
			LicenseType lt = new LicenseType();
			lt.setId(1l);
			
			LearnerLicenseType lla = new LearnerLicenseType();
			//lla.setLearner(vu360User.getLearner());
			lla.setLicenseType(lt);
			
			entityManager.merge(lla);
			entityManager.getTransaction().commit();
		}	
				
		//@Test
		public void LearnerLicenseType_should_update() throws Exception {
			entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();

			//VU360User vu360User = vU360UserDAO.findUserByUserName("test_user1@360training.com");
			
			LearnerLicenseType lla = entityManager.find(LearnerLicenseType.class, new Long(2));
			//lla.setLearner(vu360User.getLearner());
			
			entityManager.merge(lla);
			entityManager.getTransaction().commit();
		}	
		
		//LegacyCredential/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//@Test
		public void LegacyCredential_should_find() throws Exception {
			entityManager = emf.createEntityManager();	
			LegacyCredential lla = entityManager.find(LegacyCredential.class, new Integer(26));
			System.out.println(lla.getAddress());
			System.out.println(lla.getAddress2());
			System.out.println(lla.getApprovalDateEnd());
			System.out.println(lla.getApprovalDateStart());
			System.out.println(lla.getApprovalDateStart());
			System.out.println(lla.getAsvId());
			System.out.println(lla.getCareer());
			System.out.println(lla.getCareerTitleId());
			System.out.println(lla.getCertificateId());
			System.out.println(lla.getCertificateNum());
			System.out.println(lla.getCertificateNumber());
			System.out.println(lla.getCertificateTemplate());
			System.out.println(lla.getCid());
			System.out.println(lla.getCity());
			System.out.println(lla.getCityState());
			System.out.println(lla.getCompAddress());
			System.out.println(lla.getCompany());
			System.out.println(lla.getCompCity());
			System.out.println(lla.getCompDate());
			System.out.println(lla.getCompDate2());
			System.out.println(lla.getCompDate3());
			System.out.println(lla.getCompDate3UK());
			System.out.println(lla.getCompDate4());
			System.out.println(lla.getCompDate5());
			System.out.println(lla.getCompDate6());
			System.out.println(lla.getCompDateUK());
			System.out.println(lla.getCompState());
			System.out.println(lla.getCompZip());
			System.out.println(lla.getCourseHours());
			System.out.println(lla.getCourseId());
			System.out.println(lla.getCourseName());
			System.out.println(lla.getCourseNum());
			System.out.println(lla.getRegDate());
			System.out.println(lla.getStartDate());
			System.out.println(lla.getCompDate());
			System.out.println(lla.getCompDate2());
			System.out.println(lla.getCompDate3());
			System.out.println(lla.getCompDate3UK());
			System.out.println(lla.getCompDate4());
			System.out.println(lla.getCompDate5());
			System.out.println(lla.getCompDate6());
			System.out.println(lla.getCompDateUK());
			System.out.println(lla.getCertificateId());
			System.out.println(lla.getCertificateNumber());
			System.out.println(lla.getCertificateNum());
			System.out.println(lla.getCourseHours());
			System.out.println(lla.getCourseNumber());
			System.out.println(lla.getGrade());
			System.out.println(lla.getCourseSeq());
			System.out.println(lla.getCourt());
			System.out.println(lla.getTicketNum());
			System.out.println(lla.getApprovalDateStart());
			System.out.println(lla.getApprovalDateEnd());
			System.out.println(lla.getCareer());
			System.out.println(lla.getCareerTitleId());
			System.out.println(lla.getAsvId());
			System.out.println(lla.getStateID());
			System.out.println(lla.getRegulatorState());
			System.out.println(lla.getVertId());
			System.out.println(lla.getVertical());
			System.out.println(lla.getEnrollmentIntegrationDate());
			System.out.println(lla.isExecuted());
			System.out.println(lla.getExecutionDate());
			System.out.println(lla.isError());
			System.out.println(lla.getErrorDescription());
			System.out.println(lla.getLmsEnrollmentId());
			System.out.println(lla.getCertificateTemplate());
			System.out.println(lla.getEpoch());
		
			
		}	

		// InstructorCourse /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//@Test
		public void InstructorCourse_should_find() throws Exception {
			entityManager = emf.createEntityManager();
			InstructorCourse summ = entityManager.find(InstructorCourse.class, new Long(1001));
			System.out.println(summ.getInstructorType());
			System.out.println(summ.getInstructor().getLastName());
			System.out.println(summ.getCourse().getCourseTitle());
		}
		
		//@Test
		public void InstructorCourse_should_save() throws Exception {
			entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();

			//VU360User vu360User = vU360UserDAO.findUserByUserName("admin");
			LicenseType lt = new LicenseType();
			lt.setId(1l);
			
			InstructorCourse lla = new InstructorCourse();
			//lla.setInstructor(vu360User.getInstructor());
			lla.setInstructorType("LeadLead");
			lla.setCourse(entityManager.find(Course.class, new Long(118743)));
			
			entityManager.merge(lla);
			entityManager.getTransaction().commit();
		}	
		
		
		//@Test
		public void InstructorCourse_should_update() throws Exception {
			entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();

			//VU360User vu360User = vU360UserDAO.findUserByUserName("admin");
			LicenseType lt = new LicenseType();
			lt.setId(1l);
			
			InstructorCourse lla = entityManager.find(InstructorCourse.class, new Long(2851));
			//lla.setInstructor(vu360User.getInstructor());
			lla.setInstructorType("LeadLead123");
			lla.setCourse(entityManager.find(Course.class, new Long(118742)));
			
			entityManager.merge(lla);
			entityManager.getTransaction().commit();
		}	
				
		
		// InstructorSynchronousClass /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//@Test
		public void InstructorSynchronousClass_should_find() throws Exception {
			entityManager = emf.createEntityManager();
			InstructorSynchronousClass summ = entityManager.find(InstructorSynchronousClass.class, new Long(2));
			System.out.println(summ.getInstructorType());
			System.out.println(summ.getInstructor().getLastName());
			System.out.println(summ.getSynchronousClass().getGuid());
		}
		
		//@Test
		public void InstructorSynchronousClass_should_save() throws Exception {
			entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();

			//VU360User vu360User = vU360UserDAO.findUserByUserName("admin");
			LicenseType lt = new LicenseType();
			lt.setId(1l);
			
			InstructorSynchronousClass lla = new InstructorSynchronousClass();
			//lla.setInstructor(vu360User.getInstructor());
			lla.setInstructorType("LeadLead");
			lla.setSynchronousClass(entityManager.find(SynchronousClass.class, new Long(2)));
			
			entityManager.merge(lla);
			entityManager.getTransaction().commit();
		}	
		
		
		//@Test
		public void InstructorSynchronousClass_should_update() throws Exception {
			entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();

			//VU360User vu360User = vU360UserDAO.findUserByUserName("admin");
			LicenseType lt = new LicenseType();
			lt.setId(1l);
			
			InstructorSynchronousClass lla = entityManager.find(InstructorSynchronousClass.class, new Long(6052));
			//lla.setInstructor(vu360User.getInstructor());
			lla.setInstructorType("LeadLead123");
			
			
			entityManager.merge(lla);
			entityManager.getTransaction().commit();
		}	
		
		
		
		// LearnerGroupMember /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//@Test
		public void LearnerGroupMember_should_find() throws Exception { 
			entityManager = emf.createEntityManager();
			Query q = entityManager.createQuery("select p from LearnerGroupMember p where p.key.learnerGroup.id in (:id)");
			q.setParameter("id", 7353L);
			List <LearnerGroupMember> lst = (List<LearnerGroupMember>)q.getResultList();
			
		//	System.out.println(lst.get(0).getOrganizationalGroup().getName());
			System.out.println(lst.get(0).getLearner().getId());
		}
				
		
		//-------------------------------------------------------------------------------------------------------------------------		
		//-------------------------------------------------------------------------------------------------------------------------
		// OrganizationalGroupMember /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//-------------------------------------------------------------------------------------------------------------------------
		//-------------------------------------------------------------------------------------------------------------------------
		//@Test
		public void OrganizationalGroupMember_should_find() throws Exception { 
			entityManager = emf.createEntityManager();
			Query q = entityManager.createQuery("SELECT o FROM OrganizationalGroupMember o WHERE o.organizationalGroup.id = :Id");
			q.setParameter("Id", 14154L);
			List <OrganizationalGroupMember> lst = (List<OrganizationalGroupMember>)q.getResultList();
			
		//	System.out.println(lst.get(0).getOrganizationalGroup().getName());
			System.out.println(lst.get(0).getLearner().getId());
		}
		
		@Test
		@Transactional
		public void OrganizationalGroupMember_should_save() throws Exception {
			entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();
			
			OrganizationalGroup cusOrd = entityManager.find(OrganizationalGroup.class, new Long(14154));
			
			OrganizationalGroupMember o = new OrganizationalGroupMember();
			o.setLearner(entityManager.find(Learner.class, new Long(1047895l)));
			o.setOrganizationalGroup(cusOrd);
			entityManager.persist(o);
			entityManager.getTransaction().commit();
		}
		
		//@Test
		public void OrganizationalGroupMember_should_delete() throws Exception {
			entityManager = emf.createEntityManager();
			
			entityManager.getTransaction().begin();
			
			//Query q = entityManager.createQuery("SELECT o FROM OrganizationalGroupMember o WHERE o.learner.id = :Id");
			Query q = entityManager.createQuery("SELECT o FROM OrganizationalGroupMember o WHERE o.learner.id = :Id and o.organizationalGroup.id=:oId");
			q.setParameter("Id", 1047895L);
			q.setParameter("oId", 14154L);
			List <OrganizationalGroupMember> lst = (List<OrganizationalGroupMember>)q.getResultList();
			
			for (OrganizationalGroupMember organizationalGroupMember : lst) {
				entityManager.remove(organizationalGroupMember);
			}
			
			entityManager.flush();
			entityManager.getTransaction().commit();
		}
		
		
		//@Test
		public void OrganizationalGroupMember_should_update() throws Exception {
			entityManager = emf.createEntityManager();
			
			Query q = entityManager.createQuery("SELECT o FROM OrganizationalGroupMember o WHERE o.learner.id = :Id and o.organizationalGroup.id=:oId");
			q.setParameter("Id", 1047895L);
			q.setParameter("oId", 14154L);
			List <OrganizationalGroupMember> lst = (List<OrganizationalGroupMember>)q.getResultList();
			
			for (OrganizationalGroupMember organizationalGroupMember : lst) {
				entityManager.getTransaction().begin();
				Query q2 = entityManager.createQuery("update OrganizationalGroupMember o set o.learner.id = :NewId where o.organizationalGroup.id=:orgId and o.learner.id=:learnerId");
				q2.setParameter("NewId", 98632L);
				q2.setParameter("orgId", 14154L);
				q2.setParameter("learnerId", 1047895L);
				q2.executeUpdate();
				entityManager.getTransaction().commit();
			}
			
			
		}
		
}
