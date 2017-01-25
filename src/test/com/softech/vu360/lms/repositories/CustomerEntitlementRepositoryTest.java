package com.softech.vu360.lms.repositories;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.Learner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LmsTestConfig.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class CustomerEntitlementRepositoryTest {

	@Inject
	public CustomerEntitlementRepository customerEntitlementRespository;
	@Inject
	private OrgGroupEntitlementRepository orgGroupEntitlementRespository;
	@Inject
	private CourseCustomerEntitlementItemRepository courseCustomerEntitlementItemRespository;
	@Inject
	private CourseGroupCustomerEntitlementItemRepository courseGroupCustomerEntitlementItemRespository;
	@Inject
	private LearnerEnrollmentRepository learnerEnrollmentRespository;
	@Inject
	private TrainingPlanAssignmentRepository trainingPlanAssignmentRespository;
	@Inject
	private LearnerSCOStatisticsRepository learnerSCOStatisticsRespository;
	@Inject
	private LearnerSelfStudyCourseActivityRepository learnerSelfStudyCourseActivityRespository;
	@Inject
	private LearnerCourseStatisticsRepository learnerCourseStatisticsRespository;;
	@Inject
	private LearnerRepository learnerRespository;;
	@Inject
	private CustomerEntitlementRepository customerEntitlementRepository;
	@Inject
	public CustomerRepository customerRespository;

	// @Test
	public void customerEntitlementList() {
		Customer c = new Customer();
		c.setId(new Long(1));
		List<CustomerEntitlement> ls = customerEntitlementRepository
				.findByCustomer(c);
		System.out.print(ls.size());

	}

	// @Test
	public void customerEntitlementLists() {
		Customer c = new Customer();
		c.setId(new Long(1));
		List<CustomerEntitlement> ls = customerEntitlementRepository
				.findByCustomerId(c.getId());
		System.out.print(ls.size());

	}

	// @Test
	public void findCustomersWithEntitlementByDistributor() {
		List<CustomerEntitlement> custList = customerEntitlementRepository.findCustomersWithEntitlementByDistributor(381l);
		if (custList != null && custList.size() > 0) {
			for (CustomerEntitlement cust : custList) {
				System.out.println(cust.getCustomer().getName());
			}
		}
	}

	//@Test
	public void findOne() {
		int availableSeats=0;
		CustomerEntitlement entitlement = customerEntitlementRepository
				.findOne(51l);
		if (entitlement != null) {
			availableSeats=entitlement.getMaxNumberSeats()-entitlement.getNumberSeatsUsed();
		}
		System.out.println(availableSeats);
	}

	
	//@Test
		//@Transactional
		public void getCustomerEntitlementsForLearner(){
			Learner learner = learnerRespository.findOne(480l);
			List<CustomerEntitlement> ls = customerEntitlementRespository.findCustomerIdAndAllowSelfEnrollmentTrue(learner);
			Assert.assertNotNull(ls);
			
		}

		@Test
		public void getActiveCustomerEntitlementsForCustomer(){
			Customer c = new Customer();
			c.setId(new Long(1));
			
			List<CustomerEntitlement> ls = customerEntitlementRespository.getActiveCustomerEntitlementsForCustomer(c.getId());
			Assert.assertNotNull(ls);
		}

		
		//@Test
		public void saveCustomerEntitlement()
		{
			CustomerEntitlement en = customerEntitlementRespository.findOne(223569L);
			//en.setId(null);
			customerEntitlementRespository.save(en);
		}

		//@Test
		public void getCustomerEntitlementById()
		{
			CustomerEntitlement en = customerEntitlementRespository.findOne(223569L);
			Assert.assertNotNull(en);

		}

		//@Test
		public void getAllCustomerEntitlements()
		{
			Customer c = customerRespository.findOne(223569L);
			List<CustomerEntitlement> ls = customerEntitlementRespository.findByCustomerOrderByIdDesc(c);
			Assert.assertNotNull(ls);

		}

		
		//@Test
		public void getAllCustomerEntitlementsExcept()
		{
			
			Long[] notGetIds = new Long[]{57l,56l,54l};
			Customer c = customerRespository.findOne(103L);
			List<CustomerEntitlement> ls = customerEntitlementRespository.findByCustomerAndIdNotIn(c, Arrays.asList(notGetIds));
			Assert.assertNotNull(ls);

		}

		
		//@Test
		public void isCustomerEntitlementExistByName()
		{
			Boolean d = customerEntitlementRespository.isCustomerEntitlementExistByName("Yahoo");
			Assert.assertTrue(d);
		}

		//@Test
		public void readCustomerEntitlementById()
		{
			CustomerEntitlement d = customerEntitlementRespository.findById(194905l);
			Assert.assertNotNull(d);
		}
		
		//@Test
		public void loadForUpdateCustomerEntitlement()
		{
			CustomerEntitlement d = customerEntitlementRespository.findById(43l);
			Assert.assertNotNull(d);
		}
		
		//@Test
		public void getSystemManagedContract()
		{
			Customer c = customerRespository.findOne(25555L);
			List<CustomerEntitlement> d = customerEntitlementRespository.findByCustomerAndIsSystemManagedTrue(c);
			Assert.assertNotNull(d);
		}
		
		
		
		@Test
		public void findByCourseTypeByCustomerAndIsSystemManagedTrue()
		{
			Customer c = customerRespository.findOne(1L);
			List<CustomerEntitlement> d = customerEntitlementRespository.findByCourseTypeByCustomerAndIsSystemManagedTrue(c.getId());
			Assert.assertNotNull(d);
		}
		
		//@Test
		public void getValidOrgGroupEntitlementsByCutomerEntitlement()
		{
			Customer c = customerRespository.findOne(1L);
			List<CustomerEntitlement> d = customerEntitlementRespository.findByCustomerAndMaxNumberSeatsGreaterThan(c, 0);
			Assert.assertNotNull(d);
		}
		
		//@Test
		public void deleteCustomerEntitlement()
		{
			CustomerEntitlement c = customerEntitlementRespository.findOne(204L);
			customerEntitlementRespository.deleteCustomerEntitlement(c);
			Assert.assertNotNull(c);
		}
		
		

		//@Test
		//@Transactional
		public void getSimilarCustomerEntitlements()
		{
			customerEntitlementRespository.getSimilarCustomerEntitlements(251L, new Long(0));
			
		}
		
				
		//@Test
		//@Transactional
		public void removeEntitlementsWithEnrollments(){

			CustomerEntitlement c = customerEntitlementRespository.findOne(204L);
			customerEntitlementRespository.deleteCustomerEntitlement(c);
			
		}
		
		
		//@Test
		public void customerEntitlement_getCustomerEntitlementsByCourse() {
			Customer customer = new Customer();
			customer.setId(1L);
			String courseName = "my course";
			String courseGUID = null;
			String keywords="";
			List<CustomerEntitlement> entitlement = customerEntitlementRepository.getCustomerEntitlementsByCutomer(customer);
			if (entitlement != null) {
				
			}
			//System.out.println(entitlement);
		}
		
		//@Test
		public void customerEntitlement_getCustomerEntitlementsByCourseId() {
			
			List<CustomerEntitlement> entitlement = customerEntitlementRepository.getCustomerEntitlementsByCourseId(1L);
			if (entitlement != null) {
				
			}
			//System.out.println(entitlement);
		}
		
		//@Test
		public void findByCustomer(){
			Customer customer = new Customer();
			customer.setId(53L);
			System.out.print("***************************************"+customerEntitlementRepository.findByCustomer(customer).get(0).getName());
		}

}
