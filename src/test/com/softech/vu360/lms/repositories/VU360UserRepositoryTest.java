package com.softech.vu360.lms.repositories;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.TrainingAdministrator;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.util.GUIDGeneratorUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
@Transactional
public class VU360UserRepositoryTest {

	@Inject
	private VU360UserRepository vu360UserRepository;
	
	@Inject
	private VU360UserService vu360UserService;
	
	//@Test
	public void findByUsernameOrEmailAddressAndDomain() {
		String userName = "owsCust@ows.com";
		String domain="";
		List<VU360User> userList = vu360UserRepository.findUserByUsernameAndDomain(userName,domain);
		if (userList != null && userList.size() > 0) {
			for (VU360User user : userList) {userList.get(0).getLearner().getCustomer().getCustomFields();
				System.out.println(userList.get(0).getLearner().getCustomer().getCustomFields().get(0).getFieldLabel());
			}
		}
	}

	//@Test
	public void getUserByFirstNameAndLastName() {
		String firstName = "Corey123";
		String lastName = "Cotton123";
		Customer customer = new Customer();
		customer.setId(106l);
		List<VU360User> userList = vu360UserRepository
				.getUserByFirstNameAndLastName(customer, firstName, lastName);

		if (userList != null && userList.size() > 0) {
			for (VU360User user : userList) {
				System.out.println(user.toString());
			}
		}
	}

	//@Test
	public void getActiveUserByUsername() {

		String username = "Manager";
		List<VU360User> userList = vu360UserRepository
				.getActiveUserByUsername(username);
		if (userList != null && userList.size() > 0) {
			for (VU360User user : userList) {
				System.out.println(user.toString());
			}
		}
	}

	//@Test
	public void findUserByEmailAddress() {
		String email = "yasir.hasan@360training.com";
		List<VU360User> userList = vu360UserRepository
				.findUserByEmailAddress(email);
		if (userList != null && userList.size() > 0) {
			for (VU360User user : userList) {
				System.out.println(user.toString());
			}
		}
	}

	//@Test
	public void findUserByUserName() {
		String username = "admin";
		VU360User userList = vu360UserRepository.findUserByUserName(username);
		if (userList != null) {
			System.out.println(userList.getEmailAddress());
		}

	}

	//@Test
	public void saveUser() {

		Learner learner = new Learner();

		//Customer customer = (Customer) crudFindById(Customer.class, new Long(103));
		
		// ==========Setting User
		VU360User newUser = new VU360User();

		newUser.setFirstName("Kaunain");
		// UI has not provided me the middlename
		newUser.setLastName("Wajeeh");
		newUser.setEmailAddress("kaunain.wajeeh@360training.com");
		newUser.setPassword("12345");
		newUser.setUsername("kaunain");

		newUser.setAccountNonExpired(true);
		newUser.setAcceptedEULA(new Boolean(false));
		newUser.setNewUser(new Boolean(true));
		newUser.setAccountNonLocked(true);
		newUser.setChangePasswordOnLogin(false);
		newUser.setCredentialsNonExpired(true);
		newUser.setEnabled(true);
		newUser.setVissibleOnReport(true);
		newUser.setExpirationDate(null);
		String guid = GUIDGeneratorUtil.generateGUID();
		newUser.setUserGUID(guid);
		Calendar calender = Calendar.getInstance();
		Date createdDate = calender.getTime();
		newUser.setCreatedDate(createdDate);

		LearnerProfile newLearnerProfile = new LearnerProfile();

		Address newAddress = new Address();
		newAddress.setStreetAddress("Test_Street");
		newAddress.setStreetAddress2("Test_Street2");
		newAddress.setCity("Karachi");
		newAddress.setState("Sindh");
		newAddress.setZipcode("78906");
		newAddress.setCountry("Pakistan");
		newLearnerProfile.setLearnerAddress(newAddress);

		Address newAddress2 = new Address();
		newAddress2.setStreetAddress("Test_StreetAddress");
		newAddress2.setStreetAddress2("Test_Street2");
		newAddress2.setCity("karachi");
		newAddress2.setState("Sindh");
		newAddress2.setZipcode("75850");
		newAddress2.setCountry("Pakistan");
		newLearnerProfile.setLearnerAddress2(newAddress2);

		newLearnerProfile.setMobilePhone("03039289654");

		// Setting user in learner
		learner.setVu360User(newUser);
		// Setting learnerProfile in learner
		learner.setLearnerProfile(newLearnerProfile);
		// Setting customer in learner
		learner.setCustomer(null);//customer);
		// Setting learner to LearnerProfile
		newLearnerProfile.setLearner(learner);
		// Setting learner to user
		newUser.setLearner(learner);

		vu360UserRepository.saveUser(newUser);
	}

	//@Test
	public void updateUser() {

		VU360User user = vu360UserRepository.findOne(13l);
		user.setEnabled(false);
		vu360UserRepository.updateUser(user);

	}

	//@Test
	public void getListOfUsersByGUID() {

		String guids[] = { "1D5831AD-0FB8-4FE6-902B-9FDA356542D3",
				"9F1BB7E3F1F44D6C95D03905702592FE",
				"dcda5167-8af6-48d3-99cf-4337d19d378a" };
		List<String> guidList = Arrays.asList(guids);
		List<VU360User> userList = vu360UserRepository
				.getListOfUsersByGUID(guidList);
		if (userList != null && userList.size() > 0) {
			for (VU360User user : userList) {
				System.out.println(user.toString());
			}
		}
	}

	//@Test
	public void getUserByGuid(){
		VU360User user=vu360UserRepository.getUserByGUID("9F1BB7E3F1F44D6C95D03905702592FE");
		if(user!=null){
			System.out.println(user.getLastName());
		}
	}
	
	//@Test
	public void getUpdatedUserById(){
		VU360User user=vu360UserRepository.getUpdatedUserById(13l);
		if(user!=null){
			System.out.println(user.getLastName());
		}
	}
	
	//@Test
	public void getUserByEmailFirstNameLastName() {
			List<VU360User> userList=null;
			String email="pabitra.chakraborty@tathya.com";
			String firstName="Ashis"; 
			String lastName="Mukherjee";
			
			userList=vu360UserRepository.getUserByEmailFirstNameLastName(email, firstName, lastName);
			if(userList!=null && userList.size()>0){
				for(VU360User user:userList){
					System.out.println(user.getUserGUID());
				}
			}
		}
	
	//@Test
	public void findUsers() {

		@SuppressWarnings({"unchecked"})
		Map<Object, Object> userMap = vu360UserRepository.findUsers("Corey123",
				"Cotton123", "Corey.Cotton@360Training.com", null, 0, 100,
				"firstName", 0);
		int count = (int) userMap.get("recordSize");
		@SuppressWarnings("unchecked")
		List<VU360User> userList = (List<VU360User>) userMap.get("list");
		System.out.println(count);
		if (userList != null) {
			for (VU360User user : userList) {
				System.out.println(user.getUserGUID());
			}
		}

	}

	//@Test
	public void findAllLearners() {

		@SuppressWarnings({})
		Map<Object, Object> userMap = vu360UserRepository.findAllLearners(
				"Corey123", "Cotton123", "Corey.Cotton@360Training.com", null,
				"firstName", 0);
		int count = (int) userMap.get("recordSize");
		List<VU360User> userList = (List<VU360User>) userMap.get("list");
		System.out.println(count);
		if (userList != null) {
			for (VU360User user : userList) {
				System.out.println(user.getUserGUID());
			}
		}

	}

	//@Test
	public void deleteLMSAdministrator() {

		VU360User admin = vu360UserRepository.getUserById(13L);
		vu360UserRepository.deleteLMSAdministrator(admin);

	}

	//@Test
	public void deleteLMSTrainingAdministrator() {
		VU360User admin = vu360UserRepository.getUserById(13L);
		TrainingAdministrator tA = admin.getTrainingAdministrator();
		vu360UserRepository.deleteLMSTrainingAdministrator(tA);
	}

	//@Test
	public void deleteLMSTrainingManager() {

		VU360User admin = vu360UserRepository.getUserById(13L);
		vu360UserRepository.deleteLMSTrainingManager(admin);
	}

	//@Test
	public void searchCustomerUser() {

		Customer customer = vu360UserRepository.getUserById(106l).getLearner()
				.getCustomer();
		List<VU360User> users = vu360UserRepository.searchCustomerUsers(
				customer, "Corey123", "Cotton123",
				"Corey.Cotton@360training.com", "firstName", 1);
		if (users != null && users.size() > 0) {
			for (VU360User user : users) {
				System.out.println(user.getUserGUID());
			}
		}

	}

	//Tuesday
	
	public void searchCustomerUsers() {

		Customer customer = vu360UserRepository.getUserById(106l).getLearner()
				.getCustomer();
		Map users = vu360UserRepository.searchCustomerUsers(customer,
				"Corey123", "Cotton123", "Corey.Cotton@360training.com", 0, 5,
				"firstName", 1);
		if (users != null && users.size() > 0) {
			@SuppressWarnings("unchecked")
			List<VU360User> user = (List<VU360User>) users.get("list");
			if (user != null && user.size() > 0) {
				for (VU360User use : user) {
					System.out.println(use.getUserGUID());
				}
			}
		}

	}

	//@Test
	public void refreshVU360User() {
		VU360User user=vu360UserRepository.getUserById(13l);
		user.setFirstName("Chakorbarty");
		vu360UserRepository.updateUser(user);
		VU360User user2=vu360UserRepository.getUserById(13l);
		System.out.println(user2.getFirstName());

	}

	//@Test
	public void saveVU360UserAudit() {
		/*I made this method, but when I ran the test, the entity is not mapped and the table
		 * does not exist, though it is entered in Entity mapping sheet*/
		VU360User user=vu360UserRepository.getUserById(13l);
		vu360UserRepository.saveVU360UserAudit(user, "", 1l);
	}

	// @Test
	public void getUserByIds() {
			String ids[] = { "3", "11", "13" };
			List<VU360User> userList = vu360UserRepository.getUserByIds(ids);
			if (userList != null && userList.size() > 0) {
				for (VU360User user : userList) {
					System.out.println(user.toString());
				}
			}
		}
	
	//@Test
	public void findAll(){
		List<VU360User> list=(List<VU360User>)vu360UserRepository.findAll();
		 if(list!=null && list.size()>0){
			 for(VU360User user:list){
				 System.out.println(user.getFirstName());
				 break;
			 }
		 }
	}
	
	//@Test
	public void getById(){
		VU360User user=vu360UserRepository.findOne(13l);
		VU360User user2=vu360UserRepository.getUserById(13l);
		System.out.println(user.getLastName());
		System.out.println(user2.getFirstName());
		
	}
	
	//Added By Marium  Saud
	//@Test
	public void Vu360User_should_findByUsername_In(){
		Vector<String> v=new Vector<String>();
		v.add("yasir.hasan@360training.com");
		v.add("admin.manager@360training.com");
		v.add("manager");
		List<VU360User> userList=vu360UserRepository.findByUsernameIn(v);
		if(userList!=null && userList.size()>0){
			 for(VU360User user:userList){
				 System.out.println("User ID is :"+user.getId()+ " User FirstName is :" +user.getFirstName());
			 }
		 }
	}
	
//	@org.springframework.transaction.annotation.Transactional
//	@Rollback(false)
//	@Test
	public void Vu360User_should_update_AccountNotLocked(){
		VU360User user=vu360UserRepository.findOne(132L);
		user.setAccountNonLocked(false);
		user=vu360UserRepository.save(user);
		 System.out.println("User ID is :"+user.getId()+ " User AccountNonLocked set to :" +user.getAccountNonLocked());
		
	}
	
	//@Rollback(false)
	//@Test
	public void Vu360User_should_update_Password(){
		VU360User user=vu360UserRepository.findOne(211233L);
		user.setPassword("lms12345");
		user=vu360UserRepository.save(user);
		 System.out.println("User ID is :"+user.getId()+ " User Password set to :" +user.getPassword());
		
	}

	//@Test
	@Transactional
	public void getAllLearners(){
		//VU360User u = vu360UserRepository.findOne(new Long(1220));
		VU360User u = vu360UserRepository.findOne(new Long(3));

		List<VU360User> ls = vu360UserRepository.getAllLearners("a","a", "a", null, u );
		Assert.assertNotNull(ls);
		
	}
	
	//Added By Marium Saud
	//@Test
	public void Vu360User_should_findByEmailAddress(){
		String emailAddress="asma.hussain@360training.com";
		int usersCount=vu360UserRepository.countByEmailAddress(emailAddress);
		System.out.println("No. of Users count" +usersCount );
	}
	
	//@Test
	public void VU360User_showAllLearners(){
		VU360User u = vu360UserRepository.findOne(new Long(558716));
		Long[] idbucekt = {558716L,558700L,558701L,558702L,558703L,558704L,558720L,558705L,558722L,558707L,558708L,558709L};
		List<VU360User> ls = vu360UserRepository.showAll(null, 
				vu360UserService.hasAdministratorRole(u),u.getTrainingAdministrator().isManagesAllOrganizationalGroups(), u.getLearner().getCustomer().getId(), u.getId(), 
				"", "2016", "", "", idbucekt, false, false, null, null, false, "firstName", 1);
		Assert.assertNotNull(ls);
	}

	@Test
	public void VU360User_hasAtLeastOnePermssionEnabled() {
		vu360UserRepository.hasAtLeastOnePermssionOfRoleEnabled(1L, 1L);
	}
}
