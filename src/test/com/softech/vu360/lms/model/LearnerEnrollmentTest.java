package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;

import org.junit.Before;

/**
 * 
 * @author marium.saud
 * This Unit Test contains insert/update/delete operations for all entities that are making relation with Learner; which includes namely :
 * LearnerCourseStatistics
 * CustomerEntitlement (of type SubscriptionEntitlement,OrgGroupEntitlement,CustomerEntitlement)
 * OrgGroupEntitlement
 * LockedCourse
 * ProctorEnrollment
 * SynchronousClass
 * Subscription
 * SubscriptionCourse
 * LearnerHomeworkAssignmentSubmission
 * LearnerHomeworkAssignmentSubmissionAsset
 * OrganizationalGroup
 * 
 */
@Transactional
public class LearnerEnrollmentTest extends TestBaseDAO<LearnerEnrollment> {


	@Before
	public void setRequiredService() {

	}

	//=============================================CRUD Function for LearnerEnrollments===================================//
	
//	@Test
	public void LearnerEnrollment_Of_Type_Customer_And_Subscription_Entitlement_should_save() throws Exception {
		
		//==========addEnrollmentsForCourseEntitlements=======//
		/* LearnerEnrollment learnerEnrollment=new LearnerEnrollment();
		 
		 //get Learner
		 Learner learner= (Learner) crudFindById(Learner.class, new Long(5));
		 
		 //get CustomerEntitlement
		 CustomerEntitlement entitlement = (CustomerEntitlement) crudFindById(CustomerEntitlement.class, new Long(2440)); //customer of type entitlement Course
		 
		//get course
		 WebinarCourse course=(WebinarCourse)crudFindById(WebinarCourse.class, new Long(116717));
		 
		 //Orgentitlement of type course
		 OrgGroupEntitlement orgGrpEntitlement=(OrgGroupEntitlement)crudFindById(OrgGroupEntitlement.class, new Long(2133)); // mapped by customerentitlement
		
		 //get synchronous class
		 SynchronousClass syncClasses = (SynchronousClass) crudFindById(SynchronousClass.class, new Long(17702)); //mapped with course id
		
		 //set learnerstats
		 LearnerCourseStatistics courseStats = new LearnerCourseStatistics();
		 courseStats.setStatus(LearnerCourseStatistics.NOT_STARTED);
		
		 //setting object for learnerenrollment
		 learnerEnrollment.setCourse(course);
		 learnerEnrollment.setSynchronousClass(syncClasses);
		 learnerEnrollment.setLearner(learner);
		 learnerEnrollment.setCustomerEntitlement(entitlement);
		 learnerEnrollment.setOrgGroupEntitlement(orgGrpEntitlement);
		 learnerEnrollment.setCourseStatistics(courseStats);
		 learnerEnrollment.setEnrollmentStartDate(new Date());
		 learnerEnrollment.setLaunchInN3(false);
		 learnerEnrollment.setEnrollmentStatus(LearnerEnrollment.ACTIVE);
		 courseStats.setLearnerEnrollment(learnerEnrollment); */
		 
		//==========addEnrollmentsForSubscriptionEntitlements=======//
		 LearnerEnrollment learnerEnrollment=new LearnerEnrollment();
		 
		 //get Learner
		 Learner learner= (Learner) crudFindById(Learner.class, new Long(5));
		 
		 //get CustomerEntitlement of type subscription
		 CustomerEntitlement entitlement = (CustomerEntitlement) crudFindById(CustomerEntitlement.class, new Long(221556)); //customerEntitlem,ent of type subscription
		 
		 //get Subscription
		 Subscription subswcription=(Subscription)crudFindById(Subscription.class, new Long(3462));
		 
		 //get subscription course
		 SubscriptionCourse cscCourse=(SubscriptionCourse)crudFindById(SubscriptionCourse.class, new Long(39001)); //mapped by sucscriptionID
		 //set learnerstats
		 LearnerCourseStatistics courseStats = new LearnerCourseStatistics();
		 courseStats.setStatus(LearnerCourseStatistics.NOT_STARTED);
		
		 //setting object for learnerenrollment
		 learnerEnrollment.setCourse(cscCourse.getCourse());
		 learnerEnrollment.setSynchronousClass(null);
		 learnerEnrollment.setLearner(learner);
		 learnerEnrollment.setCustomerEntitlement(entitlement);
		 learnerEnrollment.setOrgGroupEntitlement(null);
		 learnerEnrollment.setCourseStatistics(courseStats);
		 learnerEnrollment.setSubscription(subswcription);
		 learnerEnrollment.setEnrollmentStartDate(new Date());
		 learnerEnrollment.setLaunchInN3(Boolean.FALSE);
		 learnerEnrollment.setEnrollmentDate(new Date());
		 learnerEnrollment.setEnrollmentStatus(LearnerEnrollment.ACTIVE);
		 courseStats.setLearnerEnrollment(learnerEnrollment);
	    

		 try{
			 learnerEnrollment=(LearnerEnrollment)crudSave(LearnerEnrollment.class, learnerEnrollment);
		 }
		 catch(Exception ex){
			 System.out.println(learnerEnrollment.getId());
		 }
	    
	}

//	@Test
	public void LearnerEnrollment_should_update() throws Exception {

		LearnerEnrollment record = (LearnerEnrollment) crudFindById(LearnerEnrollment.class, new Long(260952));
		record.setCourseStatistics(null);
		record.setEnrollmentDate(new Date());
		record.setEnrollmentStartDate(new Date());
		record.setEnrollmentEndDate(new Date());
		crudSave(LearnerEnrollment.class, record);

	}
	
	//=============================================CRUD Function for LearnerHomeWorkAssignmentSubmission and LearnerHomeWorkAssignmentSubmissionAsset====//
	//@Test
	public void LearnerHomeworkAssignmentSubmission_should_save_with_Asset_LearnerHomeworkAssignmentSubmissionAsset() throws Exception{
		LearnerHomeworkAssignmentSubmission submission=new LearnerHomeworkAssignmentSubmission();
		//==========Setting objects for LearenrHomeWorkAssignmentSubmissionAsset
		List<LearnerHomeworkAssignmentSubmissionAsset> submissionAssetList=new ArrayList<LearnerHomeworkAssignmentSubmissionAsset>();
		LearnerHomeworkAssignmentSubmissionAsset submissionAsset=new LearnerHomeworkAssignmentSubmissionAsset();
		//Setting doc
		Document document = new Document();
		//setting contentOwner
		ContentOwner contentOwner=(ContentOwner)crudFindById(ContentOwner.class, new Long(454));
		document.setFileName("Test_Doc_HomeworkSubmissionAsset");
		document.setContentowner(contentOwner);
		document.setName("Test_HomeWork_Assignment_Submission_Asset");
		submissionAsset.setDocument(document);
		submissionAssetList.add(submissionAsset);
		
		//============Setting Instructor
		Instructor instructor=(Instructor)crudFindById(Instructor.class, 702);
		submission.setGrader(instructor);
		//===========Setting Learner Enrollment
		LearnerEnrollment learnerEnrollment=(LearnerEnrollment)crudFindById(LearnerEnrollment.class, new Long(4122));
		submission.setLearnerEnrollment(learnerEnrollment);
		submission.setStatus("Submitted");
		//==========Setting LearnerHomeWorkAssignmentSubmission into LearnerHomeWorkAssignmentSubmissionAsset 
		submissionAssetList.get(0).setHomeworkAssignmentSubmission(submission);
		submission.setSubmittedWork(submissionAssetList);
		crudSave(LearnerHomeworkAssignmentSubmission.class, submission);
		
	}
	
	//@Test
	public void LearnerHomeWorkAssignmentSubmission_should_update() throws Exception{
		
		LearnerHomeworkAssignmentSubmission updateRecord=(LearnerHomeworkAssignmentSubmission)crudFindById(LearnerHomeworkAssignmentSubmission.class, "1356");
		updateRecord.setStatus("Completed");
		updateRecord.getSubmittedWork().get(0).setDocument(null);
		crudSave(LearnerHomeworkAssignmentSubmission.class, updateRecord);
	}
	
	//=============================================CRUD Function for Proctor====//

	//@Test
	public void Proctor_should_save() throws Exception{
		
		Proctor proctor=new Proctor();
		proctor.setFirstLogin(true);
		proctor.setStatus("Active");
		VU360User user=(VU360User)crudFindById(VU360User.class, new Long(1070));
		proctor.setUser(user);
		proctor.setUsername("Test_Username");
		proctor.setPassword("12345");
		List<Credential> credentialList=new ArrayList<Credential>();
		Credential credential=(Credential)crudFindById(Credential.class, new Long(2));
		credentialList.add(credential);
		proctor.setCredentials(credentialList);
		crudSave(Proctor.class, proctor);
	}
	
//	@Test
	public void Proctor_should_update() throws Exception{

		Proctor updateRecord=(Proctor)crudFindById(Proctor.class, new Long(1506));
		updateRecord.setUsername("Test_UserName_Updated");
		updateRecord.getCredentials().remove(0);
		crudSave(Proctor.class, updateRecord);
	}
	
	//=============================================CRUD Function for OrgGroupEntitlement====//
	//@Test
	public void OrgGroupEntitlement_should_save_with_CustomerEntitlement() throws Exception{
		
		OrgGroupEntitlement orgEntitlement=new OrgGroupEntitlement();
		
		//============Setting organizational Group
		//Get customer
		Customer customer=(Customer)crudFindById(Customer.class, new Long(1));
		//Get organizational Group to set Organizational Group
		List<OrganizationalGroup> orgGrpList=(List<OrganizationalGroup>)getAll("OrganizationalGroup", OrganizationalGroup.class);
		OrganizationalGroup orgGrp=new OrganizationalGroup();
		orgGrp.setId(orgGrpList.get(0).getId());
//		orgGrp.setRootOrgGroup(null);
//		orgGrp.setParentOrgGroup(null);
//		List<OrganizationalGroup> organizationalGroup = new ArrayList <OrganizationalGroup>();
//		organizationalGroup.add(orgGrp);
//		orgGrp.setChildrenOrgGroups(organizationalGroup);
		orgEntitlement.setOrganizationalGroup(orgGrp);
		//============Setting Customer Entitlement
		CustomerEntitlement customerEntitlement=new CourseCustomerEntitlement();
		customerEntitlement.setCustomer(customer);
		customerEntitlement.setName("Test_OrganizationalGroup_CustomerEntitlement");
		
		orgEntitlement.setCustomerEntitlement(customerEntitlement);
		orgEntitlement.setMaxNumberSeats(3);
		orgEntitlement.setEndDate(new Date());
		orgEntitlement.setStartDate(new Date());
		
		crudSave(OrgGroupEntitlement.class, orgEntitlement);
		
	}
	
	//=============================================CRUD Function for CustomerEntitlement====//
	//@Test
	public void CustomerEntitlement_should_save() throws Exception {
		
//		CustomerEntitlement customerEntitlement=new CourseCustomerEntitlement();
//		CustomerEntitlement customerEntitlement=new CourseGroupCustomerEntitlement();
		CustomerEntitlement customerEntitlement=new SubscriptionCustomerEntitlement();
		//===============Setting Customer
		Customer customer=(Customer)crudFindById(Customer.class, new Long(103));
		customerEntitlement.setCustomer(customer);
		customerEntitlement.setName("Test_SubscriptionCustomerEntitlement");
		customerEntitlement.setEndDate(new Date());
		customerEntitlement.setStartDate(new Date());
		crudSave(CustomerEntitlement.class, customerEntitlement);
	}
	
	//@Test
	public void CustomerEntitlement_should_update() throws Exception{

		CustomerEntitlement updateRecord=(CustomerEntitlement)crudFindById(CustomerEntitlement.class, new Long(223458));
		updateRecord.setName("Test_CourseGroupCustomerEntitlement");
		crudSave(CustomerEntitlement.class, updateRecord);
	}
	
	
	//=============================================CRUD Function for Subscription====//
//	@Test
	public void Subscription_should_save(){
		
		Subscription subscription = new Subscription();
		
		//get CustomerEntitlement of type subscription
		CustomerEntitlement entitlement = (CustomerEntitlement) crudFindById(CustomerEntitlement.class, new Long(221556)); //customerEntitlem,ent of type subscription
		 
		//get SubscriptionKit
		SubscriptionKit kit=(SubscriptionKit)crudFindById(SubscriptionKit.class, new Long(1));
		
		//get Learner
		Learner learner= (Learner) crudFindById(Learner.class, new Long(5));
		
		subscription.setCustomerEntitlement(entitlement);
		subscription.setSubscriptionCode("Test_Subscription_Code");
		subscription.setSubscriptionName("Test_Subscription_Name");
		subscription.setSubscriptionType("Monthly");
		subscription.setSubscriptionQty(8);;
		subscription.setCreateDate(new Date());
		subscription.setSubscriptionStatus("Active");
		subscription.setCustomerType(entitlement.getCustomer().getCustomerType());
		subscription.setSubscriptionKit(kit);
		subscription.getVu360User().add(learner.getVu360User());
		
		try{
			subscription=(Subscription)crudSave(Subscription.class, subscription);
		}
		catch(Exception ex){
			System.out.println(subscription.getId());
		}
	}
	
//	@Test
	public void Subscription_should_update(){
		
		Subscription subscription=(Subscription)crudFindById(Subscription.class, new Long(4960));
		subscription.setVu360User(null);
		subscription.setSubscriptionCode("Test_Subscription_Code_Updated");
		crudSave(Subscription.class, subscription);
	}
	
	//=============================================CRUD Function for OrganizationalGroup====//
	
//	@Test
	public void OrganizationalGroup_should_save(){
		
		OrganizationalGroup newOrganizationalGroup = new OrganizationalGroup();
		newOrganizationalGroup.setName("Test_Organizational_Group");
		
		Customer customer=(Customer)crudFindById(Customer.class, new Long(1));
		newOrganizationalGroup.setCustomer(customer);
		
		try{
			newOrganizationalGroup=(OrganizationalGroup)crudSave(OrganizationalGroup.class, newOrganizationalGroup);
			System.out.println(newOrganizationalGroup.getId());
		}
		catch(Exception ex){
			
		}
	}
	
//	@Test
	public void OrganizationalGroup_should_update(){
		
		OrganizationalGroup updateRecord=(OrganizationalGroup)crudFindById(OrganizationalGroup.class, new Long(72105));
		OrganizationalGroup parent=new OrganizationalGroup();
		parent.setRootOrgGroup(updateRecord);
		parent.setParentOrgGroup(updateRecord);
		List<OrganizationalGroup> orgList=new ArrayList<OrganizationalGroup>();
		OrganizationalGroup children=new OrganizationalGroup();
		children.setRootOrgGroup(updateRecord);
		children.setParentOrgGroup(parent);
		orgList.add(children);
		updateRecord.setChildrenOrgGroups(orgList);
		crudSave(OrganizationalGroup.class, updateRecord);
		
	}
	
	@PersistenceUnit(unitName="lmsPersistenceUnit")
	EntityManagerFactory emf;
	public EntityManagerFactory getEmf() {
		return emf;
	}

	public void setEmf(EntityManagerFactory emf) {
		this.emf = emf;
	}

}
