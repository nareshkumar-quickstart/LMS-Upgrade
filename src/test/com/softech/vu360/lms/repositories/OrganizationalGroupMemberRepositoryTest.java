package com.softech.vu360.lms.repositories;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.OrganizationalGroupMember;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class OrganizationalGroupMemberRepositoryTest{//  extends SpringJUnitConfigAbstractTest {
	@Autowired
	private OrganizationalGroupMemberRepository ogmRepository;
	
	@Autowired
	private OrganizationalGroupRepository ogRepository;
	
	@Autowired
	private LearnerRepository lRepository;
	
	//@Test
	public void findByOrganizationalgroupIdIn() {
		Long arr [] = {1L,2L};
		List<OrganizationalGroupMember> og = ogmRepository.findByOrganizationalGroupIdIn(arr);
		System.out.println(og.get(0).getLearner());
	}

	//@Test
	@Transactional
	public void findByorganizationalGroupIdAndLearnerIdIn() {
		Long arrLearner [] = {101459L};
		List<OrganizationalGroupMember> og = ogmRepository.findByOrganizationalGroupIdLearnerIdIn(14557L, arrLearner);
		System.out.println(og.get(0).getLearner());
		//for(OrganizationalGroupMember o : og){
		//	ogmRepository.delete(o);
		//}
		
	}
	
	//@Test
	public void findByOrganizationalgroupId() {
		List<OrganizationalGroupMember> og = ogmRepository.findByOrganizationalGroupId(11754L);
		System.out.println(og.get(0).getOrganizationalGroup());
	}
	
	//@Test
	@Transactional
	public void insert(){
		OrganizationalGroup cusOrd = ogRepository.findOne(new Long(14154));
		Learner l = lRepository.findOne(new Long(1047895l));
		
		OrganizationalGroupMember o1 = new OrganizationalGroupMember();
		o1.setLearner(l);
		o1.setOrganizationalGroup(cusOrd);
		ogmRepository.saveOGM(o1);
	}
	
	
	//@Test
	@Transactional
	public void update(){
	//	ogmRepository.updateLearner(1047895L, 14154L, 101462L);
	}
	
	//@Test
	@Transactional
	public void findByLearnerIdOrderByOrganizationalGroupNameAsc() {
		List<OrganizationalGroupMember> og = ogmRepository.findByLearnerIdOrderByOrganizationalGroupNameAsc(98580L);
		System.out.println(og.get(0).getOrganizationalGroup());
	}
	
	
	@Test
	@Transactional
	public void OrganizationalGroupMember_should_find_By_LearnerId() {
		List<OrganizationalGroupMember> og = ogmRepository.findByLearnerId(98580L);
		System.out.println(og.get(0).getOrganizationalGroup());
	}
	
}
