package com.softech.vu360.lms.repositories;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LearnerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LmsTestConfig.class)
@Transactional
public class LearnerRepositoryTest {

	@Inject
	private LearnerRepository learnerRepository;

	@Inject
	private LMSRoleRepository lmsRoleRepo;

	@Inject
	private VU360UserRepository userRepo;

	//LearnerService learnerService;

	@Autowired
	ApplicationContext context;

	@Before
	public void CustomerRepositoryInit() {
		//learnerService = (LearnerService) context.getBean("learnerService");
	}

	// @Test
	public void Learner_should_get_getVu360User_LmsRole_Id() {
		List<Learner> list = learnerRepository.findByVu360User_LmsRoles_Id(107L);
		System.out.println("List size is  " + list.size());
	}

//	 @Test
	public void Learner_should_getById() {
		Learner learner = learnerRepository.findOne(109L);
		System.out.println("Learner id is  " + learner.getVu360User().getFirstName());
	}

	// @Test
	public void Learner_should_getByCustomer_DistributorId() {
		List<Learner> learner = learnerRepository.findByCustomer_Distributor_Id(403L);
		System.out.println("Learner id is  " + learner.size());
	}

	// @Test
	public void Learner_should_findLearner_ByCustomerId() {
		List<Learner> learnerByCustomer = learnerRepository.findByCustomerId(603L);
		System.out.println("learnerByCustomer size is  " + learnerByCustomer.size());
	}

	// @Test
	public void Learner_should_findLearner_ByIdIn() {
		Long[] le = new Long[4];
		le[0] = 1L;
		le[1] = 2L;
		le[2] = 106L;
		le[3] = 107L;
		List<Learner> learnerById = learnerRepository.findByIdIn(le);
		for (Learner learner : learnerById) {
			System.out.println("UserName for the learner is " + learner.getVu360User().getFirstName());
		}
	}

	// @Test
	public void Learner_should_save() {

		try {
			Learner learner = learnerRepository.findOne(119L);
			Learner learnerClone = new Learner();
			learnerClone.setCustomer(learner.getCustomer());
			learnerClone.setLearnerProfile(learner.getLearnerProfile());
			learnerClone.setPreference(learner.getPreference());
			learnerClone.setVu360User(learner.getVu360User());
			learnerClone = learnerRepository.save(learnerClone);
			System.out.println("Learner id is " + learnerClone.getId());
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	// @Test
	// @Transactional
	// @Rollback(false)
	public void Learner_should_getAllUsersInLmsRole_with_Page() {

		try {
			LMSRole lmsRole = lmsRoleRepo.findOne(109L);
			VU360User user = userRepo.findOne(121L);

			Map<Object, Object> list = null;//learnerService.getAllUsersInLmsRole(lmsRole, user, 1, 10, "id", 2);
			List<VU360User> result = (List<VU360User>) list.get("list");
			for (VU360User users : result) {
				System.out.println("UserName is " + users.getFirstName());
			}
		}

		catch (Exception ex) {
			System.out.println(ex);
		}
	}

	//@Test
	@Transactional
	@Rollback(false)
	public void Learner_should_getAllUsersInLmsRole_without_page() {

		try {
			LMSRole lmsRole = lmsRoleRepo.findOne(109L);
			VU360User user = userRepo.findOne(121L);

			Map<Object, Object> list = null;//learnerService.getAllUsersInLmsRole(lmsRole, user, "id", 1);
			List<VU360User> result = (List<VU360User>) list.get("list");
			for (VU360User users : result) {
				System.out.println("UserName is " + users.getFirstName());
			}
		}

		catch (Exception ex) {
			System.out.println(ex);
		}
	}

	// @Test
	// @Transactional
	// @Rollback(false)
	public void Learner_should_findLearner1_without_page() {

		try {
			VU360User user = userRepo.findOne(121L);

			List<VU360User> result = null;//learnerService.findLearner(user.getFirstName(), user.getLastName(),	user.getEmailAddress(), user);

			for (VU360User users : result) {
				System.out.println("UserName is " + user.getId() + " " + users.getFirstName());
			}
		}

		catch (Exception ex) {
			System.out.println(ex);
		}
	}

	// @Test
	// @Transactional
	// @Rollback(false)
	public void Learner_should_findLearner1_without_searchCriteria() {
		VU360User user = userRepo.findOne(121L);
		Map<Object, Object> result = null;//learnerService.findLearner1(user.getFirstName(), user.getLastName(), user.getEmailAddress(), user, 0, 10, "firstName", 0);
		List<VU360User> results = (List<VU360User>) result.get("list");
		for (VU360User users : results) {
			System.out.println("UserName is " + users.getFirstName());
		}
	}

	// @Test
	// @Transactional
	// @Rollback(false)
	public void Learner_should_findAllLearners() {
		VU360User user = userRepo.findOne(121L);
		Map<Object, Object> result = null;//learnerService.findAllLearners("", user, "firstName", 0);
		List<VU360User> results = (List<VU360User>) result.get("list");
		for (VU360User users : results) {
			System.out.println("UserName is " + users.getFirstName());
		}
	}

	// @Test
	// @Transactional
	// @Rollback(false)
	public void Learner_should_findLearner() {
		VU360User user = userRepo.findOne(121L);
		List<VU360User> results = null;//learnerService.findAllLearner(user.getFirstName(), user.getLastName(),	user.getEmailAddress(), user, 0, 10, "", 0, 20);
		for (VU360User users : results) {
			System.out.println("UserName is " + users.getFirstName());
		}
	}

	// @Test
	// @Transactional
	// @Rollback(false)
	public void Learner_should_findActiveLearner() {
		VU360User user = userRepo.findOne(121L);
		Map<Object, Object> userList = null;//learnerService.findActiveLearners(user.getFirstName(), user.getLastName(), user.getEmailAddress().trim(), user, true, true, true, 0, 10, "firstName", 0);
		List<VU360User> results = (List<VU360User>) userList.get("list");
		for (VU360User users : results) {
			System.out.println("UserName is " + user.getId() + " " + users.getFirstName());
		}
	}

	// @Test
	// @Transactional
	// @Rollback(false)
	public void Learner_should_findAllLearners_Of_Customers_Of_Reseller() { // Not
																			// tested
																			// due
																			// to
																			// data
																			// discrepency
		VU360User user = userRepo.findOne(121L);
		Map<Object, Object> userList = null;// learnerService.findAllLearnersOfCustomersOfReseller(user.getFirstName(),	user.getLastName(), user.getEmailAddress(), 1, -1, "firstName", 0, null);
		List<VU360User> results = (List<VU360User>) userList.get("list");
		for (VU360User users : results) {
			System.out.println("UserName is " + user.getId() + " " + users.getFirstName());
		}
	}

	// @Test
	// @Transactional
	// @Rollback(false)
	public void Learner_should_getLearnerForSelectedCustomer() {
		Learner learner = learnerRepository.findFirstByCustomerIdOrderByIdAsc(103L);
		System.out.println("Learner id is " + learner.getId());

	}

	// @Test
	public void getLearnersByCustomer() {
		List<Learner> learnerByCustomer = learnerRepository
				.findByCustomerIdAndVu360UserFirstNameLikeOrVu360UserLastNameLikeOrVu360UserEmailAddressLike(103L,
						"shuja", "rahman", "admin2@admin2.com");

		for (Learner learner : learnerByCustomer) {
			System.out.println("UserName for the learner is " + learner.getVu360User().getFirstName());
		}

	}

	// @Test
	public void getLearnersByDistributor() {
		List<Learner> learnerByCustomer = learnerRepository
				.findByCustomerDistributorIdAndVu360UserFirstNameLikeOrVu360UserLastNameLikeOrVu360UserEmailAddressLike(
						3L, "shuja", "rahman", "admin2@admin2.com");

		for (Learner learner : learnerByCustomer) {
			System.out.println("UserName for the learner is " + learner.getVu360User().getFirstName());
		}

	}

	//@Test
	public void Learner_should_findByFilteredRecipientsByAlertId() {
		learnerRepository.getFilteredRecipientsByAlert(14303L);
	}

	//@Test
//	public void countByVu360User_LmsRoles_Id() {
//		Long count = learnerRepository.countByVu360User_LmsRoles_Id(1631211L);
//		System.out.println(count);
//	}
	
	//@Test
	public void getLearnersUnderAlertRecipient() {

		Long alertRecipientId = 1038094L;
		String firstName = "saaliz";
		String lastName = "ali";
		String emailAddress = "saaliz@yahoo.com";

		try {
			List<Learner> learnerByCustomer = learnerRepository.getLearnersUnderAlertRecipient(alertRecipientId, firstName, lastName, emailAddress);
			System.out.println(learnerByCustomer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	public void getLearnersUnderAlertTriggerFilter() {

		Long alertTriggerFilterId = 1L;
		String firstName = "Ramiz";
		String lastName = "Uddin";
		String emailAddress = "ramiz.uddin@reseller.lms.com";

		try {
			List<Learner> learnerByCustomer = learnerRepository.getLearnersUnderAlertRecipient(alertTriggerFilterId, firstName, lastName, emailAddress);
			System.out.println(learnerByCustomer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	public void getLearners_LearnerGroupMemberComposition_By_LearnerGroupId(){
		List<Learner> learnersList= learnerRepository.findByLearnerGroupId(7353L);
		System.out.println(learnersList.get(0).getVu360User().getFirstName());
	}
	
	//@Test
	public void getLearners_By_Vu360User(){
		Learner learner= learnerRepository.findByVu360UserId(1220L);
		System.out.println(learner.getId());
	}
	
	@Test
	public void getLearners_getLearnersUnderAlertTriggerFilter() {
		List<Learner> learners = learnerRepository.getLearnersUnderAlertTriggerFilter(5223L, "", "", "");
		System.out.println(learners);
	}
	
	//@Test
	public void getAllLearners_LearnerGroupMemberComposition_By_LearnerGroupId(){
			List<Learner> learnersList= learnerRepository.findLearnerByLearnerGroupID(32953L);
			System.out.println(learnersList.get(0).getVu360User().getFirstName());
	}
}
