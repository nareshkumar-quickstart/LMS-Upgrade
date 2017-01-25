package com.softech.vu360.lms.repositories;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Brand;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerGroupItem;
import com.softech.vu360.lms.model.LearnerGroupItemPK;
import com.softech.vu360.lms.model.LearnerGroupMember;
/**
 * 
 * @author marium.saud
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class LearnerGroupItemRepositoryTest {
	
	@Inject
	private LearnerGroupItemRepository learenrGroupItemRepository;
	@Inject
	private CourseRepository courseRepository;
	@Inject
	private CourseGroupRepository courseGroupRepository;
	@Inject
	private LearnerGroupRepository learnerGrpRepository;
	@Inject
	private LearnerRepository learnerRepository;
	@Inject
	private LearnerGroupMemberRepository memberRepo;
	@Inject
	private DistributorRepository distRepo;
	
	
	//@Test
	public void LearnerGroupItem_should_getByLearnerGroupId() {
		List<LearnerGroupItem> learenrGroupItemList = learenrGroupItemRepository.findByLearnerGroupId(5304L);
				System.out.println("LearnerGroupItemList contains = "+((Collection<LearnerGroupItem>)learenrGroupItemList).size());
	}

	//@Test
	public void LearnerGroupItem_should_save() {
		LearnerGroupItem learnerGrpItem=new LearnerGroupItem();
		
		LearnerGroup learnerGroup=learnerGrpRepository.findOne(7803L);
		Course course=courseRepository.findOne(78140L);
		
		LearnerGroupItemPK pk=new LearnerGroupItemPK(learnerGroup,course);
		
		
		CourseGroup courseGroup=courseGroupRepository.findOne(73L);
		
//		learnerGrpItem.setLearnerGroup(pk.getLearnerGroup());
//		learnerGrpItem.setCourse(pk.getCourse());
		learnerGrpItem.setCourseGroup(courseGroup);
		
		learnerGrpItem=learenrGroupItemRepository.save(learnerGrpItem);
		System.out.println(learnerGrpItem.getLearnerGroup().getId());

		
	}

}
