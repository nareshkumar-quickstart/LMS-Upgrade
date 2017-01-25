package com.softech.vu360.lms.model;

import static org.junit.Assert.assertNotNull;

import java.sql.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;

import com.softech.vu360.lms.model.lmsapi.LmsApiCustomer;

@Transactional
public class JPAEntityTestService  extends TestBaseDAO{

	@PersistenceUnit(unitName="lmsPersistenceUnit")
	EntityManagerFactory emf;
	private EntityManager entityManager;
	
	
	

	@Before
	public void setRequiredService() {
		entityManager = emf.createEntityManager();
	}

	
	
	//@Test
	public void testSurvey() throws Exception {
		System.out.println("#### Unit Test Survey using JUnit ####");
		
		Survey objSurvey = new Survey();
		objSurvey.setName("Unit Test Survey using JUnit - 01");
		objSurvey.setAllowAnonymousResponse(false);
		objSurvey.setEvent("Testing Event");
		objSurvey.setStatus(Survey.NOTPUBLISHED);
		objSurvey.setIsShowAll(false);
		objSurvey.setQuestionsPerPage(Integer.parseInt("5"));
		objSurvey.setRememberPriorResponse(false);
		objSurvey.setAllowAnonymousResponse(false);
		objSurvey.setElectronicSignatureRequire(false);
		objSurvey.setElectronicSignature("Signature");
		objSurvey.setLinkSelected(false);
		//objSurvey.setOwner();

		
		//** Save ***
		Survey survey = (Survey) crudSave(Survey.class, objSurvey);
		
		//** Find By ID ***
		survey = (Survey) crudFindById(Survey.class, survey.getId());
		
		//** Find All ***
		List l = crudFindAll(Survey.class, "Survey");
		
		
		
		
		
		
	}
	
/*	//@Test
	public void testVU360UserEnity() throws Exception {
		VU360User vu360User = vU360UserDAO.findUserByUserName("admin");
		assertNotNull(vu360User);
		assertNotNull(vu360User.getRegulatoryAnalyst());
		System.out.print(vu360User.getRegulatoryAnalyst());
	}
*/
	
	//@Test
	/*public void testInstructorEnity() throws Exception {
		Instructor instructor = instructorDAO.getInstructorById(203);
		List<CustomField> ls = instructor.getCustomfields();
		for (Iterator iterator = ls.iterator(); iterator.hasNext();) {
			CustomField customField = (CustomField) iterator.next();
			assertNotNull(customField);
		}
		List<CustomFieldValue> lss = instructor.getCustomfieldValues();
		for (Iterator iterator = lss.iterator(); iterator.hasNext();) {
			CustomFieldValue customFieldValue = (CustomFieldValue) iterator.next();
			assertNotNull(customFieldValue);
			
			
		}
		
		
		assertNotNull(instructor);
		
	}*/

	//@Test
	public void testSynchronClassEnity() throws Exception {
		/*		
		SynchronousClass synchronousClass = synchronousClassDAO.getSynchronousClassById(new Long(2));
		assertNotNull(synchronousClass);
 		List<SynchronousSession> ls = synchronousClass.getSynchronousSessions();
		for (Iterator iterator = ls.iterator(); iterator.hasNext();) {
			SynchronousSession synchronousSession = (SynchronousSession) iterator.next();
			print(synchronousSession.getId().toString());
			
		}
		print(synchronousClass.getLocation().getCustomfields().toString());*/
		/*
		SynchronousClass synchronousClass1 = synchronousClassDAO.getSynchronousClassById(new Long(353));
		List<Resource> rs = synchronousClass1.getResources();
		for (Iterator iterator = rs.iterator(); iterator.hasNext();) {
			Resource resource = (Resource) iterator.next();
			print(resource.toString());
			
		}*/

	}
	
/*	//@Test
	public void testSynchronSessionEnity() throws Exception {
		SynchronousSession session = synchronousSessionDAO.findSynchClassSessionBySessionId(new Long(4));
		assertNotNull(session.getSynchronousClass());
		assertNotNull(session.getLocation());
		List<Resource> rs = session.getResources();
		for (Iterator iterator = rs.iterator(); iterator.hasNext();) {
			Resource resource = (Resource) iterator.next();
			//print(resource.toString());
			
		}
	}
*/	
	//@Test
	public void testCourseEnity() throws Exception {
		//Course course = courseAndCourseGroupDAO.getCourseById(new Long(94785));
		//print(course.toString());
		//print(course.getLanguage().toString());
		//print(course.getContentOwner().toString());
		//print(course.getBrand().toString());
		//print(course.getCourseConfigTemplate().toString());
		//print(course.getCoursePlayerType().toString());
		//print(course.getAICCAssignableUnit().toString());
		
		
		//assertNotNull(course);
	}
	
	
	//@Test
		public void testAccrediationEnity() throws Exception {
			Regulator regulator = (Regulator) crudFindById(Regulator.class, new Long(5));
			//print(course.toString());
			//print(course.getLanguage().toString());
			//print(course.getContentOwner().toString());
			//print(course.getBrand().toString());
			//print(course.getCourseConfigTemplate().toString());
			//print(course.getCoursePlayerType().toString());
			System.out.println(regulator.getDocuments().toString());
			
			
			assertNotNull(regulator);
		}
		
		//@Test
		public void testSurveyEnity() throws Exception {
			// this is complex mapping, unable to understand.	
			//List<AggregateSurveyQuestionItem> aggregateSurveyQuestionItem  = surveyDAO.getAggregateSurveyQuestionItemsByQuestionId(new Long(13174));
			
		}
		

		//@Test
		public void testUserWidgetInsertEnity() throws Exception {
			
			//insert userWidget
			UserWidget u = new UserWidget();
			u.setPosition(6L);
			u.setUser((VU360User) crudFindById(VU360User.class, new Long(3)));
			u.setWidget((Widget) crudFindById(Widget.class, new Long(1)));
			crudInsert(UserWidget.class, u );
		}
		
		//@Test
		public void testgetWidgetAndFilterEnity() throws Exception {
			
			Widget widget = (Widget) crudFindById(Widget.class, new Long(1));
			assertNotNull(widget);

			List<WidgetFilter> ls = widget.getFilters();
			for (Iterator iterator = ls.iterator(); iterator.hasNext();) {
				WidgetFilter widgetFilter = (WidgetFilter) iterator.next();
				System.out.println(widgetFilter.toString());
			}

		}
		
		//@Test
		public void testUpdatetWidgetEnity() throws Exception {
			
			Widget widget = (Widget) crudFindById(Widget.class, new Long(1));
			assertNotNull(widget);

			widget.setDescription("test");
			crudUpdate(Widget.class, widget);

		}
		
		
		
		//@Test
		public void testLmsAPICustomerEnity() throws Exception {
			LmsApiCustomer lms = (LmsApiCustomer) crudFindById(LmsApiCustomer.class, new Long(51));
			
			System.out.println(lms);
			assertNotNull(lms);
		}
		
		//@Test
		public void testResourceTypeEnity() throws Exception {
			
			ResourceType resourceType = new ResourceType();
			resourceType.setActive(true);
			resourceType.setDescription("ssss");
			resourceType.setName("name");
			resourceType.setContentOwner( (ContentOwner) crudFindById(ContentOwner.class, new Long(53)) );
			crudInsert(ResourceType.class, resourceType);

			ResourceType psqms_ = (ResourceType) crudFindById(ResourceType.class, new Long(394850));
			psqms_.setActive(false);
			crudUpdate(ResourceType.class, psqms_);
			
			System.out.println(psqms_);
			assertNotNull(psqms_);
			
			
		}
		

		//@Test
		public void testSCOInterationResponseEnity() throws Exception {
			SCOInteractionResponse sco = new SCOInteractionResponse();
			sco.setPattern("44");
			sco.setScoInteractionResponseId("446");
			sco.setScoInteractionId("444");;
			sco.setType("1");
			crudInsert(SCOInteractionResponse.class, sco);
			
			System.out.println(sco);
			assertNotNull(sco);
		}
		
		
		//@Test
		public void testSCOInteractionEnity() throws Exception {
			SCOInteraction sco = new SCOInteraction();
			
			LearnerSCOAssessment lsco = new LearnerSCOAssessment();
			lsco.setId(12304L);
			sco.setLearnerSCOAssessment(lsco);
			
			sco.setDescription("s");
			sco.setInteractionId("1");
			sco.setLatency("3");
			sco.setLearnerResponse("43");
			sco.setResult("ddd");
			sco.setTime("4");
			sco.setType("42");
			sco.setWeighting(43D);

			crudInsert(SCOInteraction.class, sco);
			
			System.out.println(sco);
			assertNotNull(sco);
			
			
			SCOInteraction c = (SCOInteraction) crudFindById(SCOInteraction.class, new Long(13712));
			LearnerSCOAssessment cs = c.getLearnerSCOAssessment();
			System.out.println(cs);
			
			List<SCOInteractionResponse> lst = c.getInteractionResponses();
			for (Iterator iterator = lst.iterator(); iterator.hasNext();) {
				SCOInteractionResponse scoInteractionResponse = (SCOInteractionResponse) iterator.next();
				System.out.println("....  "+scoInteractionResponse);
			}

			
			List<SCOInteractionObjective> lst1 = c.getInteractionObjectives();
			for (Iterator iterator = lst.iterator(); iterator.hasNext();) {
				SCOInteractionObjective sCOInteractionObjective = (SCOInteractionObjective) iterator.next();
				System.out.println("....00000  "+sCOInteractionObjective);
			}

		}
		
		//@Test
		public void testRegistrationInvitation() throws Exception {
			RegistrationInvitation objRegistrationInvitation = new RegistrationInvitation();

			//** Save ****
			/*try{
				objRegistrationInvitation.setInvitationMessage("invitationMessage");
				objRegistrationInvitation.setInvitationName("invitationName");
				objRegistrationInvitation.setIsUnlimited(true);
				objRegistrationInvitation.setMaximumRegistration(10);
				objRegistrationInvitation.setPasscode("passcode");
				objRegistrationInvitation.setCustomer((Customer)crudFindById(Customer.class, new Long("1")));
				objRegistrationInvitation.setRegistrationUtilized(2);
				
				List<LearnerGroup> learnerGroups = new ArrayList<LearnerGroup>();
				learnerGroups.add((LearnerGroup)crudFindById(LearnerGroup.class, new Long("12005")));
				learnerGroups.add((LearnerGroup)crudFindById(LearnerGroup.class, new Long("12007")));
				learnerGroups.add((LearnerGroup)crudFindById(LearnerGroup.class, new Long("12007")));
				objRegistrationInvitation.setLearnerGroups(learnerGroups);
				
				List<OrganizationalGroup> orgGroups = new ArrayList<OrganizationalGroup>();
				orgGroups.add((OrganizationalGroup)crudFindById(OrganizationalGroup.class, new Long("14410")));
				orgGroups.add((OrganizationalGroup)crudFindById(OrganizationalGroup.class, new Long("14411")));
				orgGroups.add((OrganizationalGroup)crudFindById(OrganizationalGroup.class, new Long("14412")));
				objRegistrationInvitation.setOrgGroups(orgGroups);
				
				objRegistrationInvitation = (RegistrationInvitation) crudSave(RegistrationInvitation.class, objRegistrationInvitation);
				System.out.println("objRegistrationInvitation.id::"+objRegistrationInvitation.getId());
			}
			catch(Exception e){
				e.printStackTrace();
			}*/
			
			//** Find ****
			try{
				objRegistrationInvitation = (RegistrationInvitation)crudFindById(RegistrationInvitation.class, new Long("6751"));
				
				System.out.println("............");
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		//@Test
		public void testLearnerGroupItem() throws Exception {
			LearnerGroupItem objLearnerGroupItem = new LearnerGroupItem();

			//** Save ****
			try{
				objLearnerGroupItem.setLearnerGroup((LearnerGroup)crudFindById(LearnerGroup.class, new Long("12005")));
				objLearnerGroupItem.setCourse((Course)crudFindById(Course.class, new Long("94676")));
				objLearnerGroupItem.setCourseGroup((CourseGroup)crudFindById(CourseGroup.class, new Long("10")));
				
				objLearnerGroupItem = (LearnerGroupItem) crudSave(LearnerGroupItem.class, objLearnerGroupItem);
				System.out.println("objLearnerGroupItem.id::"+objLearnerGroupItem.getCourse().getId());
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			//** Find ****
			try{
				//objLearnerGroupItem = (LearnerGroupItem)crudFindById(LearnerGroupItem.class, new Long("6751"));
				
				System.out.println("............");
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
		@Test
		public void testAlertTriggerFilter() throws Exception {
			LearnerAlertFilter objAlertTriggerFilter = new LearnerAlertFilter();

			//** Save ****
			/*try{
				objAlertTriggerFilter.setDelete(false);
				objAlertTriggerFilter.setFilterName("filterName");
				//objAlertTriggerFilter.setType("LearnerAlertFilter");
				objAlertTriggerFilter.setAlertTrigger((AlertTrigger)crudFindById(AlertTrigger.class, new Long("10603")));
				List<Learner> learners = new ArrayList<Learner>();
				learners.add((Learner)crudFindById(Learner.class, new Long("296")));
				learners.add((Learner)crudFindById(Learner.class, new Long("297")));
				objAlertTriggerFilter.setLearner(learners );
				
				
				objAlertTriggerFilter = (LearnerAlertFilter) crudSave(LearnerAlertFilter.class, objAlertTriggerFilter);
				System.out.println("objLearnerGroupItem.id::"+objAlertTriggerFilter.getId());
			}
			catch(Exception e){
				e.printStackTrace();
			}*/
			
			//** Find ****
			try{
				//** LearnerAlertFilter
				LearnerAlertFilter objFind_LearnerAlertFilter = (LearnerAlertFilter)crudFindById(LearnerAlertFilter.class, new Long("260100"));
				
				//CourseAlertTriggeerFilter
				//CourseAlertTriggeerFilter objFind_CourseAlertTriggeerFilter = (CourseAlertTriggeerFilter)crudFindById(CourseAlertTriggeerFilter.class, new Long("5053"));
				
				//** LearnerGroupAlertFilter
				LearnerGroupAlertFilter objFind_LearnerGroupAlertFilter = (LearnerGroupAlertFilter)crudFindById(LearnerGroupAlertFilter.class, new Long("5054"));
				
				
				System.out.println("............");
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		//@Test
		public void testLearnerSCOAssessmentEnity() throws Exception {
			
			LearnerSCOStatistics lscoSt = new LearnerSCOStatistics();
			lscoSt.setId(14852L);
			
/*			SCOInteraction scInteraction = new SCOInteraction();
			scInteraction.setDescription("ddddd");
			scInteraction.setInteractionId("12304");
			scInteraction.setLatency("ddd");
			List list = new ArrayList();
			list.add(scInteraction);
*/			
			LearnerSCOAssessment lsco = new LearnerSCOAssessment();
			lsco.setAttemptDate(new Date(System.currentTimeMillis()));
			lsco.setLearnerScoStatistic(lscoSt);
			lsco.setMasteryAcheived(true);
			lsco.setMaxScore(44);
			lsco.setMinScore(55);
			lsco.setRawScore(55);
			lsco.setScaledScore(4D);
			lsco.setLearnerScoStatistic(lscoSt);
			//lsco.setScoInteractions(list);

			crudInsert(LearnerSCOAssessment.class, lsco);
			System.out.println(lsco);
			assertNotNull(lsco);
			
			
			LearnerSCOAssessment dd = (LearnerSCOAssessment) crudFindById(LearnerSCOAssessment.class, new Long(12304));
			List<SCOInteraction> sd = dd.getScoInteractions();
			for (Iterator iterator = sd.iterator(); iterator.hasNext();) {
				SCOInteraction scoInteraction = (SCOInteraction) iterator.next();
				System.out.print(".......  "+scoInteraction);
			}
			
		}
		

	/*
	 * @author raja.ali
	 * Description: This method will return a list of objects of type Clazz
	 * Maximum 10 records will be return as set in query.
	 * Usage: Please have a look into testSurvey() method of this test
	 */
	public List crudFindAll(Class clazz, String entityName){
		List result = null;
		try{
			String SQL_QUERY = "select p from "+entityName+" p ";
			
			Query query = entityManager.createQuery(SQL_QUERY, clazz);
			
			query.setFirstResult(0);
			query.setMaxResults(10);
			//query.setParameter("username", "wajahat");
			result = query.getResultList();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}

	/*
	 * @author raja.ali
	 * Description:This method will save an object of type Clazz
	 * The object's primary key setter method must be set to null
	 * If save transaction is successful then return the same object with the assigned primary key
	 * Usage: Please have a look into testSurvey() method of this test
	 */
	public Object crudSave(Class clazz, Object obj){
		try{
			entityManager.getTransaction().begin();
			Object savedObj  = (clazz.cast(entityManager.merge((clazz.cast(obj)))));
			entityManager.getTransaction().commit();
			
			return savedObj;
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public void crudInsert(Class clazz, Object obj){
		try{
			entityManager.getTransaction().begin();
			entityManager.persist(obj);
			entityManager.getTransaction().commit();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	/*
	 * @author raja.ali
	 * Description:This method will take primary key object like Long, String and class type like Learner, Survey etc
	 * If an object is found then it will be returned
	 * Usage: Please have a look into testSurvey() method of this test
	 */
	public Object crudFindById(Class clazz, Object obj){
		try{
			return entityManager.find(clazz, obj);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public void crudUpdate(Class clazz, Object obj){
		try{
			entityManager.getTransaction().begin();
			entityManager.persist((clazz.cast(obj)));
			entityManager.getTransaction().commit();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}



	public EntityManagerFactory getEmf() {
		return emf;
	}



	public void setEmf(EntityManagerFactory emf) {
		this.emf = emf;
	}




}
