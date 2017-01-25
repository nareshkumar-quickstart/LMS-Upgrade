package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerGroupMember;
import com.softech.vu360.lms.model.LearnerGroupMemberPK;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class LearnerGroupMemberRepositoryTest{
	@Autowired
	private LearnerGroupMemberRepository lgmRepository;
	@Inject
	private LearnerGroupRepository learnerGrpRepository;
	@Inject
	private LearnerRepository learnerRepository;

	//@Test
	@Transactional
	public void findByorganizationalGroupIdAndLearnerIdIn() {
		Long [] arr  = {7353L, 5153L};
		List<LearnerGroupMember> og = lgmRepository.findByLearnerGroupIdIn(arr);
		System.out.println(og.get(0).getLearner());
		
	}
	
	//Added by marium.saud to test Composite keys using EmbeddableClass
	@Transactional
	//@Test
	public void LearnerGroupMember_should_save(){
		LearnerGroupMember lg=new LearnerGroupMember();
		
		LearnerGroup learnerGroup=learnerGrpRepository.findOne(5971L);
		Learner learner=learnerRepository.findOne(122459L);
		lg.setLearner(learner);
		lg.setLearnerGroup(learnerGroup);
		lgmRepository.save(lg);
		
	}

	//@Test
	@Transactional
	public void findByLearnerGroupIdAndLearnerIdIn() {
		Long [] arrlearner  = {116025L, 116026L};
		List<LearnerGroupMember> og = lgmRepository.findByLearnerGroupIdAndLearnerIdIn( 7254L, arrlearner);

		for (LearnerGroupMember lg : og) {
			lgmRepository.delete(lg);
		}
		
	}

}
