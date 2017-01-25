package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerGroupItem;
import com.softech.vu360.lms.model.OrganizationalGroup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class LearnerGroupRepositoryTest {//extends SpringJUnitConfigAbstractTest {
	@Autowired
	private LearnerGroupRepository lgRepository;
	@Inject
	private CustomerRepository customerRepository;
	@Inject
	private OrganizationalGroupRepository orgGrpRepository;
	
	
	//@Test
	@Transactional
	public void findById() {
		long l= 54;
		LearnerGroup og = lgRepository.findOne(l);
		List<LearnerGroupItem>  lgi = og.getLearnerGroupItems();
		System.out.println(lgi.get(0).getLearnerGroup());
		
	}
	
	@Test
	@Transactional
	public void findByIdIn() {
		Long [] l = {54L, 56L};
		List<LearnerGroup> og = lgRepository.findByIdIn(l);
		System.out.println(og.get(0));
		
	}
//	@Test
	public void findByCustomerIdAndNameIn() {
		//List<LearnerGroup> og = lgRepository.findByCustomerId(103L);
		//System.out.println(og.get(0).getName());
		
		Object []args = {606L, 607L};
		Long lngarr [] = Arrays.copyOf(args, args.length, Long[].class);
		List<LearnerGroup> og2 = lgRepository.findByCustomerIdAndOrganizationalGroupIdIn(503L, lngarr);
		System.out.println(og2.get(0).getName());
	}
	
	//@Test
	public void findByCustomerIdAndNameLike(){
		List<LearnerGroup> og2 = lgRepository.findByCustomerIdAndNameLike(103L, "%new%");
		System.out.println(og2.get(0).getName());
	}
	
	//@Test
	public void findByCustomerIdAndOrganizationalGroupIdAndNameLike(){
		Long[] orgGroupIdArray = {2L, 4L};
		List<LearnerGroup> og2 = lgRepository.findByCustomerIdAndOrganizationalGroupIdInAndNameLike(103L,orgGroupIdArray, "%new%");
		System.out.println(og2.get(0).getName());
	}
	
	//@Test
	public void findByOrganizationalGroupId(){
		List<LearnerGroup> og2 = lgRepository.findByOrganizationalGroupId(2L);
		System.out.println(og2.get(0).getName());
	}
	
	
	//@Test
	//@Transactional
	public void deleteByIdIn(){
		List<Long> args = new ArrayList<Long>();
		args.add(12355L);
		
		lgRepository.deleteByIdIn(args);
	}
	
	//Addede By Marium Saud
	//@Test
	public void LearnerGroup_should_getByNameAndCustomerId() {
		LearnerGroup learnerGroup = lgRepository.findFirstByNameIgnoreCaseAndCustomerId("Quality Assurance", 503L);
		System.out.println("LearnerGroup : ID = "+learnerGroup.getId()+" Name = "+learnerGroup.getName());
	}
	
	//@Test
	public void LearnerGroup_should_getById() {
		LearnerGroup learnerGroup = lgRepository.findOne(153L);
		System.out.println("LearnerGroup : ID = "+learnerGroup.getId()+" Name = "+learnerGroup.getName());
	}
	
	//@Test
	public void LearnerGroup_should_save() {
		LearnerGroup learnerGroup = new LearnerGroup();

		OrganizationalGroup organizationalGroup = orgGrpRepository.findOne(114L);
		Customer customer=customerRepository.findOne(103L);
		learnerGroup.setName("Test_LearnerGroup");
		learnerGroup.setOrganizationalGroup(organizationalGroup);
		learnerGroup.setCustomer(customer);
		lgRepository.save(learnerGroup);
	}

	//@Test
	public void getLearner_Count() {
		Long [] lg = {12806L, 12807L};
		List<Object[]> learnerGroup = lgRepository.getLearnerCountByLearnerGroup(lg);
		HashMap<Long,Long> learnerGroupMap = new HashMap<Long, Long>();
		
		for (int i=0; i< learnerGroup.size(); i++){
			learnerGroupMap.put(Long.valueOf(learnerGroup.get(i)[1].toString()), Long.valueOf(learnerGroup.get(i)[0].toString()));
		}
		


		//LMS-6764 : Set learner count to zero, if not fetched from database
		if (learnerGroupMap.size() < lg.length) {
			Long tempCount = null;
			for (int i=0; i< lg.length; i++) {
				tempCount = learnerGroupMap.get( lg[i] );
				if (tempCount == null) {
					learnerGroupMap.put( Long.valueOf(lg[i].toString()) , 0L);
				}
			}
		}
	}
	
	
	
	//@Test
	public void getLearner_Count_ByOrg() {
		Long customerID=1L;
		List<Object[]> learnerGroup = lgRepository.getLearnerCountByOrganizationalGroup(customerID);
		HashMap<Long,Long> learnerGroupMap = new HashMap<Long, Long>();
		
		for (int i=0; i< learnerGroup.size(); i++){
			learnerGroupMap.put(Long.valueOf(learnerGroup.get(i)[1].toString()), Long.valueOf(learnerGroup.get(i)[0].toString()));
		}

	}
	
	//@Test
	public void getLearnerGroupsUnderAlertRecipient() {
		Long alertRecipientId = 1l;
		String groupName = "";
		List<LearnerGroup> learnerGroups = lgRepository.getLearnerGroupsUnderAlertRecipient(alertRecipientId, groupName);
		System.out.println(learnerGroups);
	}
	
	//@Test
	public void getLearnerGroupsUnderAlertTriggerFilter() {
		Long alertTriggerFilterId = 5054l;
		String groupName = "UG1";
		List<LearnerGroup> learnerGroups = lgRepository.getLearnerGroupsUnderAlertRecipient(alertTriggerFilterId, groupName);
		System.out.println(learnerGroups);
	}
	
	@Test
	public void getLearnerGroupByLearnerId(){
		List<LearnerGroup> lg=lgRepository.findByLearnerIdOrderByLearnerGroupNameAsc(1060594L);
		System.out.println(lg.size());
	}
	
}
