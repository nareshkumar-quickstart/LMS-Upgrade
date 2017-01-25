package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.LearningSession;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class LearningSessionRepositoryTest {

	@Inject
	private LearningSessionRepository repoLearningSession;
	
	@Inject
    private LearningSessionRepository learningSessionRepository;
	
	//@Test
	public void learningSession_should_find_By_enrollmentId() {
		Long enrollmentId = 2L;
		List<LearningSession> listLearningSession = repoLearningSession.findByEnrollmentIdAndCourseApprovalIdGreaterThan(enrollmentId, 0L);
		
		System.out.println("..........");
	}
	
	//@Test
	public void learningSession_should_find_By_enrollmentId_And_learnerId_And_courseId() {
		Long enrollmentId = 2L;
		Long learnerId = 5L;
		String courseId = "";
		List<LearningSession> listLearningSession = repoLearningSession.findByEnrollmentIdAndCourseApprovalIdGreaterThanAndLearnerIdAndCourseId(enrollmentId, 0L, learnerId, courseId);
		
		System.out.println("..........");
	}
	
	//Added By Marium Saud
//	@Transactional
//	@Test
	public void LearningSession_should_update(){
		LearningSession ls=repoLearningSession.findOne(55L);;
		ls.setRedirectURI("http://qa-lms.360training.com/vu360-lms/lrn_surveyResponse.do?method=showSurveyResponseView&learningSessionId=c1ae9142-264d-4c24-b8b7-e8448588bb81");
		repoLearningSession.save(ls);
	}
	
//	@Test
	public void LearningSession_should_findBy_CourseApprovalIdIn_And_CourseApprovalId_GreaterThan(){
		List<Long> approvalIdLst = new ArrayList<Long>();
		approvalIdLst.add(265L);
		approvalIdLst.add(266L);
		approvalIdLst.add(268L);
		approvalIdLst.add(353L);
	
		List<LearningSession> learningSessionList=repoLearningSession.findByCourseApprovalIdInAndCourseApprovalIdGreaterThan(approvalIdLst, 0L);
		for(LearningSession session:learningSessionList){
			System.out.println(session.getBrandName()+ " "+ session.getId());
		}
		
	}
	
	//@Test
	public void LearningSession_should_findLearningSessionId_By_First_EnrollmentId_And_LearnerId_And_CourseId_OrderByIdDesc(){
		LearningSession ls=repoLearningSession.findOne(1L);
		LearningSession result=repoLearningSession.findFirstByEnrollmentIdAndLearnerIdAndCourseIdOrderByIdDesc(ls.getEnrollmentId(), ls.getLearnerId(), ls.getCourseId());
		System.out.println("LearningSessionId is " +result.getLearningSessionID());
	}
	
//	@Test
	public void LearningSession_should_findCourseApprovalId_First_By_EnrollmentId_And_CourseApprovalId_NotNull_And_CourseApprovalId_GreaterThan_OrderById_Desc(){
		LearningSession result=repoLearningSession.findFirstByEnrollmentIdAndCourseApprovalIdNotNullAndCourseApprovalIdGreaterThanOrderByIdDesc(241435L, 0L);
		System.out.println("CourseApprovalId is " +result.getCourseApprovalId());
	}
	
//	@Test
	public void LearningSession_should_save(){
		LearningSession ls=repoLearningSession.findOne(53L);
		LearningSession new_ls=ls;
		new_ls.setId(null);
		new_ls=repoLearningSession.save(new_ls);
		System.out.println("Id is " +new_ls.getId());
	}

	
//	@Test
	public void learningSession_isCourseApprovalLinkedWithLearnerEnrollment() {
		Long enrollmentId = 2L;
		Long learnerId = 5L;
		String courseId = "";
		boolean listLearningSession = repoLearningSession.isCourseApprovalLinkedWithLearnerEnrollment(Arrays.asList(new Long[]{5L,56L}));
		
		System.out.println("..........");
	}
	@Test
	public void learningSession_isCourseApprovalLinkedWithLearnerEnrollment1() {
		LearningSession result=   learningSessionRepository.findFirstByExternalLMSSessionIDAndSource(null,"AICC");
		if(result!=null)
		System.out.println(result.getExternalLMSSessionID());
		//192335
		
	}
	
}
